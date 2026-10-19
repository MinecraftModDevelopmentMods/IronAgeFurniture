package zone.moddev.mc.ironagefurniture.migration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.core.Direction;
import net.minecraft.util.BitStorage;
import net.minecraft.util.SimpleBitStorage;
import net.minecraft.util.datafix.DataFixers;
import net.minecraft.util.datafix.DataFixTypes;

/** Bridge the old chunk palettes and item tags to native 26.2 save components. */
public final class NativeLegacyBridge {
    // Phase 4 on this branch writes the native Minecraft 26.2 format (4903).
    // A saved current bed must not be reversed again on its next chunk load.
    private static final int CURRENT_FURNITURE_DATA_VERSION = 4903;
    private static final String SHIELD_VERSION = "IronAgeFurnitureShieldVersion";
    private static final String CANDLE_TILE = "IronAgeFurnitureCandleStorage";
    private static final String SHIELD_TILE_VERSION = "IronAgeFurnitureShieldTileVersion";
    private static final String SAVED_TILE_COLOUR = "IronAgeFurnitureSavedUpholsteryColour";

    static void preserveTileColour(CompoundTag tile) {
        // Sign datafixers consume Color as text colour, so protect furniture colour separately.
        if (tile.contains("Color") && !tile.contains(SAVED_TILE_COLOUR)) tile.put(SAVED_TILE_COLOUR, tile.get("Color").copy());
    }
    static void restoreTileColours(CompoundTag level) {
        for (Tag value : level.getListOrEmpty("TileEntities")) {
            CompoundTag tile = (CompoundTag)value;
            if (tile.contains(SAVED_TILE_COLOUR)) {
                tile.put("Color", tile.get(SAVED_TILE_COLOUR).copy());
                tile.remove(SAVED_TILE_COLOUR);
            }
        }
    }

    public static void prepareItems(CompoundTag root, int sourceVersion) {
        LegacyPaddedItemMigration.migrateChunkContents(root);
        walk(root, item -> {
            String id = item.getStringOr("id", "");
            if (!id.startsWith("ironagefurniture:chair_wood_ironage_shield_")) return;
            CompoundTag tag = item.getCompoundOrEmpty("tag");
            normalizeShieldItem(tag, sourceVersion);
            if (!tag.isEmpty()) item.put("tag", tag);
            CompoundTag components = item.getCompoundOrEmpty("components");
            CompoundTag custom = components.getCompoundOrEmpty("minecraft:custom_data");
            normalizeShieldItem(custom, sourceVersion);
            if (!custom.isEmpty()) {
                components.put("minecraft:custom_data", custom); item.put("components", components);
            }
        });
    }
    private static void normalizeShieldItem(CompoundTag data, int sourceVersion) {
        if (data.contains("IAFShieldChair")) {
            CompoundTag old = data.getCompoundOrEmpty("IAFShieldChair").copy();
            if (old.getBooleanOr("Empty", false)) data.put("Shield", new CompoundTag());
            else if (old.contains("Shield")) data.put("Shield", old.getCompoundOrEmpty("Shield").copy());
            old.remove("Empty"); old.remove("Shield");
            if (old.isEmpty()) data.remove("IAFShieldChair"); else data.put("IAFShieldChair", old);
        }
        if (data.contains("Shield") && !data.contains(SHIELD_VERSION)) data.putInt(SHIELD_VERSION, sourceVersion);
    }
    public static void finishItems(CompoundTag root) {
        LegacyPaddedItemMigration.migrateChunkContents(root);
        walk(root, item -> {
            if (!item.getStringOr("id", "").startsWith("ironagefurniture:chair_wood_ironage_shield_")) return;
            CompoundTag components = item.getCompoundOrEmpty("components");
            CompoundTag custom = components.getCompoundOrEmpty("minecraft:custom_data");
            if (!custom.contains("Shield") || components.contains("minecraft:container")) return;
            CompoundTag old = custom.getCompoundOrEmpty("Shield");
            ListTag contents = new ListTag();
            if (!old.isEmpty()) {
                CompoundTag shield = fixShield(old, custom.getIntOr(SHIELD_VERSION, 2730));
                if (!shield.isEmpty()) {
                    CompoundTag slot = new CompoundTag();
                    slot.putInt("slot", 0); slot.put("item", shield); contents.add(slot);
                }
            }
            components.put("minecraft:container", contents);
            custom.remove("Shield"); custom.remove(SHIELD_VERSION);
            if (custom.isEmpty()) components.remove("minecraft:custom_data"); else components.put("minecraft:custom_data", custom);
            item.put("components", components);
        });
    }
    private static CompoundTag fixShield(CompoundTag shield, int version) {
        if (!shield.contains("id")) return new CompoundTag();
        return shield.contains("Count") || shield.contains("tag")
                ? (CompoundTag)DataFixers.getDataFixer().update(net.minecraft.util.datafix.fixes.References.ITEM_STACK,
                    new com.mojang.serialization.Dynamic<>(net.minecraft.nbt.NbtOps.INSTANCE, shield.copy()), version,
                    net.minecraft.SharedConstants.getCurrentVersion().dataVersion().version()).getValue() : shield.copy();
    }
    private static void walk(Tag value, java.util.function.Consumer<CompoundTag> action) {
        if (value instanceof CompoundTag tag) {
            action.accept(tag);
            for (String key : new ArrayList<>(tag.keySet())) walk(tag.get(key), action);
        } else if (value instanceof ListTag list) for (Tag child : list) walk(child, action);
    }
    public static void prepareChunk(CompoundTag root, int sourceVersion) {
        prepareItems(root, sourceVersion);
        if (!root.contains("Level") && !root.contains("sections")) return;
        CompoundTag level = view(root);
        CfmChairMigration.prepareChunk(level);
        for (Tag value : level.getListOrEmpty("TileEntities")) {
            CompoundTag tile = (CompoundTag)value;
            if (tile.getStringOr("id", "").equals("ironagefurniture:padded_bench_colour")) {
                preserveTileColour(tile);
                tile.putString("id", sourceVersion < 704 ? "Sign" : "minecraft:sign");
                tile.putBoolean("IronAgeFurnitureLegacyPaddedBench", true);
            }
            if (tile.getStringOr("id", "").endsWith(":shield_chair") || tile.getStringOr("id", "").equals("ShieldChair")) {
                if (tile.contains("ShieldItem")) { tile.put("Shield", tile.getCompoundOrEmpty("ShieldItem").copy()); tile.remove("ShieldItem"); }
                if (tile.getBooleanOr("ShieldEmpty", false) || tile.getBooleanOr("Empty", false)) tile.put("Shield", new CompoundTag());
                tile.remove("ShieldEmpty"); tile.remove("Empty");
                if (tile.contains("Shield") && !tile.contains(SHIELD_TILE_VERSION)) tile.putInt(SHIELD_TILE_VERSION, sourceVersion);
            }
        }
        if (sourceVersion >= 1451 && sourceVersion < CURRENT_FURNITURE_DATA_VERSION) {
            for (Tag section : level.getListOrEmpty("Sections")) for (Tag value : ((CompoundTag)section).getListOrEmpty("Palette")) {
                CompoundTag state = (CompoundTag)value;
                String id = state.getStringOr("Name", "");
                if (id.startsWith("ironagefurniture:bed_")) {
                    CompoundTag properties = state.getCompoundOrEmpty("Properties");
                    String facing = properties.getStringOr("facing", "north");
                    for (Direction direction : Direction.Plane.HORIZONTAL) if (direction.getName().equals(facing))
                        properties.putString("facing", direction.getOpposite().getName());
                    String part = properties.getStringOr("part", "foot");
                    if (part.endsWith("_lower") || part.endsWith("_upper")) {
                        properties.putString("upper", Boolean.toString(part.endsWith("_upper")));
                        properties.putString("part", part.startsWith("head") ? "head" : "foot");
                    }
                    state.put("Properties", properties);
                }
            }
        }
        LegacyUpholsteryMigration.prepareTiles(level, sourceVersion < 704);
        // Leave packed block indices alone until vanilla has crossed its 1.16
        // alignment fix. Converting candles here would pad an old dense array twice.
        commit(root, level);
    }
    public static CompoundTag finishChunk(CompoundTag root) {
        root.remove("IronAgeFurniturePreparedDataVersion");
        root.getCompoundOrEmpty("Level").remove("IronAgeFurniturePreparedDataVersion");
        if (!root.contains("Level") && !root.contains("sections")) { finishItems(root); return root; }
        CompoundTag level = view(root);
        restoreTileColours(level);
        LegacyWorldDataHook.normalizeLegacyVanillaTileEntityIds(level);
        LegacyPaddedPaletteMigration.migrate(level);
        LegacyUpholsteryMigration.prepareMetalTiles(level);
        LegacyUpholsteryMigration.finishTiles(level);
        candles(level, false);
        normalizeUnusedLegacyPaletteEntries(level);
        LegacySconceMigration.migrate(level);
        for (Tag value : level.getListOrEmpty("TileEntities")) {
            CompoundTag tile = (CompoundTag)value;
            if (tile.getBooleanOr(CANDLE_TILE, false)) {
                tile.putString("id", "ironagefurniture:candle_sconce"); tile.remove(CANDLE_TILE);
            }
            if (tile.getStringOr("id", "").equals("ironagefurniture:shield_chair") && tile.contains("Shield")) {
                tile.put("Shield", fixShield(tile.getCompoundOrEmpty("Shield"), tile.getIntOr(SHIELD_TILE_VERSION, 2730)));
                tile.remove(SHIELD_TILE_VERSION);
            }
        }
        finishItems(level);
        normalizeScheduledTicks(root, level);
        commit(root, level);
        return root;
    }
    private static void normalizeScheduledTicks(CompoundTag root, CompoundTag level) {
        // Queued ticks also name blocks; keep them aligned with the converted palette.
        for (ListTag ticks : java.util.List.of(root.getListOrEmpty("block_ticks"), level.getListOrEmpty("TileTicks")))
            for (Tag value : ticks) {
                CompoundTag tick = (CompoundTag)value;
                var old = net.minecraft.resources.Identifier.tryParse(tick.getStringOr("i", ""));
                if (old == null) continue;
                boolean padded = old.getNamespace().equals("ironagefurniture") && LegacyPaddedBenchIds.isLegacyPaddedPath(old.getPath());
                boolean sconce = old.getNamespace().equals("ironagefurniture") && old.getPath().startsWith("light_metal_ironage_sconce_");
                if (!padded && !sconce && net.minecraft.core.registries.BuiltInRegistries.BLOCK.containsKey(old)) continue;
                if (!old.getNamespace().equals("ironagefurniture") && CfmChairMigration.legacyTarget(old.toString()) == null) continue;
                String actual = paletteNameAt(level, tick.getIntOr("x", 0), tick.getIntOr("y", 0), tick.getIntOr("z", 0));
                var current = net.minecraft.resources.Identifier.tryParse(actual);
                if (current != null && net.minecraft.core.registries.BuiltInRegistries.BLOCK.containsKey(current)) tick.putString("i", current.toString());
            }
    }
    private static String paletteNameAt(CompoundTag level, int x, int y, int z) {
        if ((x >> 4) != level.getIntOr("xPos", 0) || (z >> 4) != level.getIntOr("zPos", 0)) return "";
        for (Tag value : level.getListOrEmpty("Sections")) {
            CompoundTag section = (CompoundTag)value;
            if (section.getIntOr("Y", 0) != (y >> 4)) continue;
            ListTag palette = section.getListOrEmpty("Palette");
            if (palette.isEmpty()) return "";
            int index = 0;
            if (palette.size() > 1) {
                int bits = Math.max(4, 32 - Integer.numberOfLeadingZeros(palette.size() - 1));
                BitStorage data = LegacyPaletteData.read(bits, section.getLongArray("BlockStates").orElseGet(() -> new long[0]));
                if (data == null) return "";
                index = data.get((y & 15) << 8 | (z & 15) << 4 | (x & 15));
            }
            return index < palette.size() ? palette.getCompoundOrEmpty(index).getStringOr("Name", "") : "";
        }
        return "";
    }
    private static void normalizeUnusedLegacyPaletteEntries(CompoundTag level) {
        // Replaced entries may remain in a chunk palette even when no cell uses them.
        // They still pass through the block-state codec, so give them valid fallback names.
        for (Tag section : level.getListOrEmpty("Sections"))
            for (Tag value : ((CompoundTag)section).getListOrEmpty("Palette")) {
                if (!(value instanceof CompoundTag state)) continue;
                var old = net.minecraft.resources.Identifier.tryParse(state.getStringOr("Name", ""));
                if (old == null || !old.getNamespace().equals("ironagefurniture")) continue;
                var current = LegacyPaddedBenchIds.isLegacyPaddedPath(old.getPath())
                        ? net.minecraft.resources.Identifier.fromNamespaceAndPath(old.getNamespace(), LegacyPaddedBenchIds.modernPaddedPath(old, "red"))
                        : LegacyPaddedBenchIds.currentId(old);
                if (old.getPath().startsWith("light_metal_ironage_") && old.getPath().contains("candle")) {
                    boolean wall = old.getPath().contains("_wall");
                    current = old.getPath().startsWith("light_metal_ironage_candle_") && !wall
                            ? net.minecraft.resources.Identifier.withDefaultNamespace("candle")
                            : net.minecraft.resources.Identifier.fromNamespaceAndPath("ironagefurniture",
                                zone.moddev.mc.ironagefurniture.api.SconceMetalData.blockPath("light_metal_ironage_sconce_"
                                    + (wall ? "wall" : "floor") + "_candle_iron",
                                    zone.moddev.mc.ironagefurniture.api.SconceMetalData.material(old.getPath())));
                }
                if (net.minecraft.core.registries.BuiltInRegistries.BLOCK.containsKey(current)) state.putString("Name", current.toString());
            }
    }
    /** A temporary view lets the established palette rules cover both chunk layouts. */
    private static CompoundTag view(CompoundTag root) {
        if (root.contains("Level")) return root.getCompoundOrEmpty("Level");
        CompoundTag view = new CompoundTag();
        view.putInt("xPos", root.getIntOr("xPos", 0)); view.putInt("zPos", root.getIntOr("zPos", 0));
        view.put("TileEntities", root.getListOrEmpty("block_entities"));
        ListTag sections = new ListTag();
        for (Tag value : root.getListOrEmpty("sections")) {
            CompoundTag original = (CompoundTag)value;
            CompoundTag section = original.copy();
            CompoundTag blockStates = original.getCompoundOrEmpty("block_states");
            ListTag palette = new ListTag();
            for (Tag entry : blockStates.getListOrEmpty("palette")) {
                CompoundTag state = entry instanceof CompoundTag compound ? compound.copy() : new CompoundTag();
                if (entry instanceof net.minecraft.nbt.StringTag string) state.putString("id", string.value());
                if (state.contains("id")) { state.putString("Name", state.getStringOr("id", "minecraft:air")); state.remove("id"); }
                if (state.contains("properties")) { state.put("Properties", state.getCompoundOrEmpty("properties")); state.remove("properties"); }
                palette.add(state);
            }
            section.put("Palette", palette);
            section.putLongArray("BlockStates", blockStates.getLongArray("data").orElseGet(() -> new long[0]));
            sections.add(section);
        }
        view.put("Sections", sections);
        return view;
    }
    private static void commit(CompoundTag root, CompoundTag view) {
        if (root.contains("Level")) { root.put("Level", view); return; }
        root.put("block_entities", view.getListOrEmpty("TileEntities"));
        ListTag sections = new ListTag();
        for (Tag value : view.getListOrEmpty("Sections")) {
            CompoundTag section = (CompoundTag)value;
            CompoundTag blockStates = section.getCompoundOrEmpty("block_states");
            ListTag palette = new ListTag();
            for (Tag entry : section.getListOrEmpty("Palette")) {
                CompoundTag state = ((CompoundTag)entry).copy();
                // The lowercase palette schema starts in 26.3, independently of
                // this branch's 26.2 furniture-migration cutoff above.
                if (root.getIntOr("DataVersion", 0) >= 5006) {
                    state.putString("id", state.getStringOr("Name", "minecraft:air")); state.remove("Name");
                    if (state.contains("Properties")) { state.put("properties", state.getCompoundOrEmpty("Properties")); state.remove("Properties"); }
                }
                palette.add(state);
            }
            blockStates.put("palette", palette);
            long[] data = section.getLongArray("BlockStates").orElseGet(() -> new long[0]);
            if (data.length > 0) blockStates.putLongArray("data", data); else blockStates.remove("data");
            section.put("block_states", blockStates); section.remove("Palette"); section.remove("BlockStates");
            sections.add(section);
        }
        root.put("sections", sections);
    }
    private static void candles(CompoundTag level, boolean oldSchema) {
        ListTag tiles = level.getListOrEmpty("TileEntities");
        Map<String, CompoundTag> byPosition = new HashMap<>();
        for (Tag value : tiles) { CompoundTag tile = (CompoundTag)value; byPosition.put(position(tile), tile); }
        for (Tag value : level.getListOrEmpty("Sections")) {
            CompoundTag section = (CompoundTag)value;
            ListTag palette = section.getListOrEmpty("Palette");
            if (palette.isEmpty()) continue;
            boolean found = false;
            for (Tag entry : palette) found |= ((CompoundTag)entry).getStringOr("Name", "").contains("ironage") && ((CompoundTag)entry).getStringOr("Name", "").contains("candle");
            if (!found) continue;
            int bits = Math.max(4, 32 - Integer.numberOfLeadingZeros(palette.size() - 1));
            long[] data = section.getLongArray("BlockStates").orElseGet(() -> new long[0]);
            BitStorage storage = data.length == 0 && palette.size() == 1 ? new SimpleBitStorage(bits, 4096) : LegacyPaletteData.read(bits, data);
            if (storage == null) throw new IllegalStateException("Invalid candle palette encoding");
            ListTag oldPalette = palette.copy();
            for (int cell = 0; cell < 4096; cell++) {
                CompoundTag old = oldPalette.getCompoundOrEmpty(storage.get(cell));
                String id = old.getStringOr("Name", "");
                if (!id.startsWith("ironagefurniture:light_metal_ironage_") || !id.contains("candle")) continue;
                int x = (level.getIntOr("xPos", 0) << 4) + (cell & 15), y = (section.getByteOr("Y", (byte)0) << 4) + (cell >> 8), z = (level.getIntOr("zPos", 0) << 4) + ((cell >> 4) & 15);
                CompoundTag tile = byPosition.get(x + ":" + y + ":" + z);
                if (tile != null && tile.contains("Candles")) continue;
                boolean wall = id.contains("_wall"), standalone = id.startsWith("ironagefurniture:light_metal_ironage_candle_");
                CompoundTag state = old.copy(), properties = old.getCompoundOrEmpty("Properties").copy();
                boolean lit = !id.endsWith("_unlit");
                if (properties.contains("lit")) lit = properties.getStringOr("lit", "false").equals("true");
                int count = id.contains("_four") ? 4 : id.contains("_three") ? 3 : id.contains("_two") ? 2 : 1;
                try { count = Integer.parseInt(properties.getStringOr("candles", Integer.toString(count))); } catch (NumberFormatException ignored) { }
                if (standalone && !wall) {
                    state.putString("Name", "minecraft:candle");
                    properties.remove("facing"); properties.remove("metal");
                    properties.putString("candles", Integer.toString(Math.max(1, Math.min(4, count)))); properties.putString("lit", Boolean.toString(lit));
                } else {
                    count = Math.max(1, Math.min(4, count));
                    state.putString("Name", "ironagefurniture:" + zone.moddev.mc.ironagefurniture.api.SconceMetalData.blockPath(
                            "light_metal_ironage_sconce_" + (wall ? "wall" : "floor") + "_candle_iron",
                            zone.moddev.mc.ironagefurniture.api.SconceMetalData.material(id.substring("ironagefurniture:".length()))));
                    properties.putString("candles", Integer.toString(count)); properties.putString("lit", Boolean.toString(lit));
                    if (tile == null) {
                        tile = new CompoundTag(); tile.putInt("x", x); tile.putInt("y", y); tile.putInt("z", z);
                        tiles.add(tile); byPosition.put(position(tile), tile);
                    }
                    tile.putString("id", oldSchema ? "Sign" : "minecraft:sign"); tile.putBoolean(CANDLE_TILE, true);
                    ListTag candles = new ListTag();
                    for (int slot = 0; slot < count; slot++) {
                        CompoundTag candle = new CompoundTag(); candle.putString("id", "minecraft:candle"); candle.putInt("count", 1); candles.add(candle);
                    }
                    tile.put("Candles", candles);
                }
                state.put("Properties", properties);
                int replacement = palette.indexOf(state);
                if (replacement < 0) { replacement = palette.size(); palette.add(state); }
                int width = Math.max(4, 32 - Integer.numberOfLeadingZeros(palette.size() - 1));
                if (width != bits) {
                    BitStorage expanded = new SimpleBitStorage(width, 4096);
                    for (int index = 0; index < 4096; index++) expanded.set(index, storage.get(index));
                    storage = expanded; bits = width;
                }
                storage.set(cell, replacement);
            }
            section.putLongArray("BlockStates", storage.getRaw());
        }
        level.put("TileEntities", tiles);
    }
    private static String position(CompoundTag tile) { return tile.getIntOr("x", 0) + ":" + tile.getIntOr("y", 0) + ":" + tile.getIntOr("z", 0); }
    private NativeLegacyBridge() { }
}
