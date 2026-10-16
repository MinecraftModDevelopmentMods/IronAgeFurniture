package zone.moddev.mc.ironagefurniture.fixture.migration;

import zone.moddev.mc.ironagefurniture.migration.NativeLegacyBridge;

import java.util.function.BiConsumer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import zone.moddev.mc.ironagefurniture.api.enumerations.SconceMetal;
import zone.moddev.mc.ironagefurniture.api.enumerations.UpholsteryColour;

/** Raw save regressions run with the real item codecs and registry state. */
public final class MigrationProbe {
    public static void run(ServerLevel level, BiConsumer<Boolean, String> check) {
        numericStates(check);
        NumericSconceMigrationProbe.run(level, check);
        SconceMigrationProbe.run(level, check);
        denseCandlePalette(check);
        oldShieldPlayer(level, check);
        cfmContracts(level, check);
        for (UpholsteryColour colour : UpholsteryColour.values()) {
            CompoundTag throughVanilla = chunk("ironagefurniture:chair_wood_ironage_bench_padded_single_oak", false);
            ListTag vanillaTiles = throughVanilla.getCompoundOrEmpty("Level").getListOrEmpty("TileEntities");
            CompoundTag oldTile = tile("ironagefurniture:padded_bench_colour");
            oldTile.putString("Color", colour.getName()); vanillaTiles.add(oldTile);
            CompoundTag scheduled = new CompoundTag(); scheduled.putString("i", "ironagefurniture:chair_wood_ironage_bench_padded_single_oak");
            scheduled.putInt("x", 0); scheduled.putInt("y", 96); scheduled.putInt("z", 0); scheduled.putInt("t", 7);
            ListTag ticks = new ListTag(); ticks.add(scheduled); throughVanilla.getCompoundOrEmpty("Level").put("TileTicks", ticks);
            NativeLegacyBridge.prepareChunk(throughVanilla, 2730);
            CompoundTag fixedTile = (CompoundTag)net.minecraft.util.datafix.DataFixers.getDataFixer().update(
                    net.minecraft.util.datafix.fixes.References.BLOCK_ENTITY,
                    new com.mojang.serialization.Dynamic<>(NbtOps.INSTANCE, vanillaTiles.getCompoundOrEmpty(0)), 2730,
                    net.minecraft.SharedConstants.getCurrentVersion().dataVersion().version()).getValue();
            vanillaTiles.set(0, fixedTile);
            NativeLegacyBridge.finishChunk(throughVanilla);
            check.accept(cell(throughVanilla).getStringOr("Name", "").equals("ironagefurniture:chair_wood_ironage_bench_padded_" + colour.getName() + "_single_oak"),
                    "The real vanilla sign datafixer must not consume upholstery colour");
            check.accept(scheduled.getStringOr("i", "").equals(cell(throughVanilla).getStringOr("Name", "")) && scheduled.getIntOr("t", 0) == 7,
                    "Queued legacy ticks retain their time and target the converted colour");
            CompoundTag item = stack("ironagefurniture:bed_wood_foot_oak", true);
            item.putShort("Damage", (short)colour.ordinal());
            CompoundTag tag = new CompoundTag(); tag.putString("KeepMe", "unrelated"); item.put("tag", tag);
            NativeLegacyBridge.prepareItems(item, 1343);
            check.accept(item.getStringOr("id", "").equals("ironagefurniture:bed_wood_foot_oak" + (colour == UpholsteryColour.RED ? "" : "_" + colour.getName())), "Legacy bed metadata maps to flat colour");
            check.accept(item.getCompoundOrEmpty("tag").getStringOr("KeepMe", "").equals("unrelated"), "Unrelated item data survives");
            CompoundTag first = item.copy(); NativeLegacyBridge.prepareItems(item, 1343);
            check.accept(first.equals(item), "Item import is idempotent");
        }
        for (SconceMetal metal : SconceMetal.values()) {
            CompoundTag item = stack("ironagefurniture:light_metal_ironage_sconce_floor_empty_iron", true);
            item.putShort("Damage", (short)metal.ordinal());
            NativeLegacyBridge.prepareItems(item, 1343);
            check.accept(item.getStringOr("id", "").equals(zone.moddev.mc.ironagefurniture.api.SconceMetalData.itemId(metal).toString()), "Metal subtype survives absent provider");
        }
        for (boolean back : new boolean[]{false, true}) for (UpholsteryColour colour : UpholsteryColour.values()) {
            String oldId = "ironagefurniture:chair_wood_ironage_bench_" + (back ? "back_" : "") + "padded_single_oak";
            CompoundTag root = chunk(oldId, false), tile = tile("ironagefurniture:padded_bench_colour");
            tile.putString("Color", colour.getName()); root.getCompoundOrEmpty("Level").getListOrEmpty("TileEntities").add(tile);
            NativeLegacyBridge.prepareChunk(root, 1976); NativeLegacyBridge.finishChunk(root);
            String expected = "ironagefurniture:chair_wood_ironage_bench_" + (back ? "back_" : "") + "padded_" + colour.getName() + "_single_oak";
            check.accept(cell(root).getStringOr("Name", "").equals(expected), "Placed bench colour recovered");
            CompoundTag first = root.copy(); NativeLegacyBridge.prepareChunk(root, 5006); NativeLegacyBridge.finishChunk(root);
            check.accept(first.equals(root), "Placed bench import is idempotent");
        }
        for (boolean wall : new boolean[]{false, true}) for (int count = 1; count <= 4; count++) for (boolean lit : new boolean[]{false, true}) {
            String suffix = count == 1 ? "" : count == 2 ? "_two" : count == 3 ? "_three" : "_four";
            String id = "ironagefurniture:light_metal_ironage_sconce_" + (wall ? "wall" : "floor") + "_candle_iron" + suffix + (lit ? "" : "_unlit");
            CompoundTag root = chunk(id, false);
            CompoundTag metal = tile("ironagefurniture:sconce_metal"); metal.putString("Metal", "antimony");
            root.getCompoundOrEmpty("Level").getListOrEmpty("TileEntities").add(metal);
            NativeLegacyBridge.prepareChunk(root, 2730); NativeLegacyBridge.finishChunk(root);
            CompoundTag state = cell(root), properties = state.getCompoundOrEmpty("Properties");
            check.accept(state.getStringOr("Name", "").endsWith("_candle_basemetals_antimony") && !properties.contains("metal"), "Candle import preserves metal as a block identity");
            check.accept(properties.getStringOr("lit", "").equals(Boolean.toString(lit)), "Candle import preserves light");
            CompoundTag stored = root.getCompoundOrEmpty("Level").getListOrEmpty("TileEntities").getCompoundOrEmpty(0);
            check.accept(stored.getStringOr("id", "").equals("ironagefurniture:candle_sconce") && stored.getListOrEmpty("Candles").size() == count, "Old count becomes exact stored candles");
            CompoundTag first = root.copy(); NativeLegacyBridge.prepareChunk(root, 5006); NativeLegacyBridge.finishChunk(root);
            check.accept(first.equals(root), "Candle import is idempotent");
        }
        for (boolean wall : new boolean[]{false, true}) {
            CompoundTag root = chunk("ironagefurniture:light_metal_ironage_candle_" + (wall ? "wall" : "floor"), false);
            NativeLegacyBridge.prepareChunk(root, 2730); NativeLegacyBridge.finishChunk(root);
            check.accept(cell(root).getStringOr("Name", "").equals(wall ? "ironagefurniture:light_metal_ironage_sconce_wall_candle_iron" : "minecraft:candle"), "Standalone candles recover as vanilla or wall frames");
        }
        for (int count = 1; count <= 4; count++) {
            CompoundTag floorCandles = chunk("ironagefurniture:light_metal_ironage_candle_floor", false);
            cell(floorCandles).getCompoundOrEmpty("Properties").putString("candles", Integer.toString(count));
            NativeLegacyBridge.prepareChunk(floorCandles, 2730); NativeLegacyBridge.finishChunk(floorCandles);
            check.accept(cell(floorCandles).getCompoundOrEmpty("Properties").getStringOr("candles", "").equals(Integer.toString(count)), "Standalone candle import preserves the stored count");
        }
        CompoundTag bed = chunk("ironagefurniture:bed_canopy_foot_lower_oak", false);
        CompoundTag properties = cell(bed).getCompoundOrEmpty("Properties"); properties.putString("part", "head_upper");
        NativeLegacyBridge.prepareChunk(bed, 2730); NativeLegacyBridge.finishChunk(bed);
        properties = cell(bed).getCompoundOrEmpty("Properties");
        check.accept(properties.getStringOr("facing", "").equals("south") && properties.getStringOr("part", "").equals("head") && properties.getStringOr("upper", "").equals("true"), "Native bed facing and upper layer retain physical orientation");
        CompoundTag nativeChunk = chunk("minecraft:stone", true);
        ListTag nativePalette = nativeChunk.getListOrEmpty("sections").getCompoundOrEmpty(0).getCompoundOrEmpty("block_states").getListOrEmpty("palette");
        nativePalette.set(0, net.minecraft.nbt.StringTag.valueOf("minecraft:air"));
        NativeLegacyBridge.prepareChunk(nativeChunk, 5006); NativeLegacyBridge.finishChunk(nativeChunk);
        check.accept(nativeChunk.getListOrEmpty("sections").size() == 1, "Compact native palettes remain loadable");
        CompoundTag belowZero = chunk("ironagefurniture:chair_wood_ironage_bench_padded_single_oak", true);
        CompoundTag belowSection = belowZero.getListOrEmpty("sections").getCompoundOrEmpty(0);
        belowSection.putByte("Y", (byte)-1);
        CompoundTag colourTile = tile("ironagefurniture:padded_bench_colour");
        colourTile.putInt("y", -16); colourTile.putString("Color", "purple");
        belowZero.getListOrEmpty("block_entities").add(colourTile);
        NativeLegacyBridge.prepareChunk(belowZero, 2865); NativeLegacyBridge.finishChunk(belowZero);
        CompoundTag belowBlocks = belowZero.getListOrEmpty("sections").getCompoundOrEmpty(0).getCompoundOrEmpty("block_states");
        ListTag belowPalette = belowBlocks.getListOrEmpty("palette");
        int belowWidth = Math.max(4, 32 - Integer.numberOfLeadingZeros(belowPalette.size() - 1));
        int belowIndex = new net.minecraft.util.SimpleBitStorage(belowWidth, 4096, belowBlocks.getLongArray("data").orElseThrow()).get(0);
        check.accept(belowPalette.getCompoundOrEmpty(belowIndex).getStringOr("id", "").contains("_padded_purple_single_"), "Negative section Y preserves the tile's real colour");
        check.accept(belowPalette.stream().noneMatch(value -> value instanceof CompoundTag saved && saved.getStringOr("id", "").contains("_padded_single_")), "Unused legacy palette entries no longer produce unknown-block warnings");
        CompoundTag shield = stack("minecraft:shield", true), tag = new CompoundTag();
        tag.putInt("Damage", 47); shield.put("tag", tag);
        CompoundTag chair = stack("ironagefurniture:chair_wood_ironage_shield_oak", false), custom = new CompoundTag(), components = new CompoundTag();
        custom.put("Shield", shield); custom.putInt("IronAgeFurnitureShieldVersion", 2730); components.put("minecraft:custom_data", custom); chair.put("components", components);
        NativeLegacyBridge.finishItems(chair);
        ItemStack decoded = ItemStack.CODEC.parse(level.registryAccess().createSerializationContext(NbtOps.INSTANCE), chair).getOrThrow();
        check.accept(zone.moddev.mc.ironagefurniture.api.ShieldChairItemData.getShield(decoded).getDamageValue() == 47, "Old stored shield damage reaches native components");
        CompoundTag first = chair.copy(); NativeLegacyBridge.finishItems(chair); check.accept(first.equals(chair), "Shield component import is idempotent");
        worldMappingOwnership(check);
    }
    private static void oldShieldPlayer(ServerLevel level, BiConsumer<Boolean, String> check) {
        for (boolean empty : new boolean[]{false, true}) {
            CompoundTag shield = stack("minecraft:shield", true); shield.putShort("Damage", (short)47);
            CompoundTag shieldTag = new CompoundTag(), display = new CompoundTag(); display.putString("Name", "Legacy crest"); shieldTag.put("display", display);
            CompoundTag pattern = new CompoundTag(); pattern.putString("Pattern", "cre"); pattern.putInt("Color", 4);
            ListTag patterns = new ListTag(); patterns.add(pattern); CompoundTag banner = new CompoundTag(); banner.put("Patterns", patterns); banner.putInt("Base", 14);
            shieldTag.put("BlockEntityTag", banner); shield.put("tag", shieldTag);
            CompoundTag chair = stack("ironagefurniture:chair_wood_ironage_shield_oak", true);
            chair.putByte("Slot", (byte)0);
            CompoundTag data = new CompoundTag(), tag = new CompoundTag();
            if (empty) data.putBoolean("Empty", true); else data.put("Shield", shield);
            tag.put("IAFShieldChair", data); tag.putString("KeepMe", "unrelated chair data"); chair.put("tag", tag);
            ListTag items = new ListTag(); items.add(chair); CompoundTag player = new CompoundTag(); player.put("Inventory", items);
            int current = net.minecraft.SharedConstants.getCurrentVersion().dataVersion().version();
            var fixed = net.minecraft.util.datafix.DataFixTypes.PLAYER.update(net.minecraft.util.datafix.DataFixers.getDataFixer(),
                    new com.mojang.serialization.Dynamic<>(NbtOps.INSTANCE, player), 512, current);
            CompoundTag saved = (CompoundTag)fixed.getValue();
            ItemStack decoded = ItemStack.CODEC.parse(level.registryAccess().createSerializationContext(NbtOps.INSTANCE),
                    saved.getListOrEmpty("Inventory").getCompoundOrEmpty(0)).getOrThrow();
            ItemStack fitted = zone.moddev.mc.ironagefurniture.api.ShieldChairItemData.getShield(decoded);
            if (empty) check.accept(fitted.isEmpty(), "The 1.10 empty-frame wrapper stays empty through the native file-fixer path");
            else {
                CompoundTag expected = (CompoundTag)net.minecraft.util.datafix.DataFixers.getDataFixer().update(net.minecraft.util.datafix.fixes.References.ITEM_STACK,
                        new com.mojang.serialization.Dynamic<>(NbtOps.INSTANCE, shield), 512, current).getValue();
                ItemStack exact = ItemStack.CODEC.parse(level.registryAccess().createSerializationContext(NbtOps.INSTANCE), expected).getOrThrow();
                check.accept(ItemStack.matches(exact, fitted), "A 1.10 shield retains its damage, banner and name through native file fixing");
            }
            check.accept(decoded.get(net.minecraft.core.component.DataComponents.CUSTOM_DATA).copyTag().getStringOr("KeepMe", "").equals("unrelated chair data"), "Shield import keeps unrelated chair data");
            CompoundTag first = saved.copy();
            net.minecraft.util.datafix.DataFixTypes.PLAYER.updateToCurrentVersion(net.minecraft.util.datafix.DataFixers.getDataFixer(), saved, current);
            check.accept(first.equals(saved), "The native player import is idempotent");
        }
    }
    private static void denseCandlePalette(BiConsumer<Boolean, String> check) {
        CompoundTag root = chunk("ironagefurniture:light_metal_ironage_candle_floor", false);
        CompoundTag section = root.getCompoundOrEmpty("Level").getListOrEmpty("Sections").getCompoundOrEmpty(0);
        ListTag palette = section.getListOrEmpty("Palette");
        for (String name : new String[]{"stone", "dirt", "sand", "gravel", "cobblestone", "oak_planks", "spruce_planks",
                "birch_planks", "jungle_planks", "acacia_planks", "gold_block", "iron_block", "glass", "obsidian", "netherrack", "end_stone"}) {
            CompoundTag state = new CompoundTag(); state.putString("Name", "minecraft:" + name); palette.add(state);
        }
        long[] dense = new long[320];
        for (int cell = 0; cell < 4096; cell++) {
            long value = cell % palette.size(); int word = cell * 5 / 64, bit = cell * 5 % 64;
            dense[word] |= value << bit;
            if (bit + 5 > 64) dense[word + 1] |= value >>> (64 - bit);
        }
        section.putLongArray("BlockStates", dense);
        NativeLegacyBridge.prepareChunk(root, 1976);
        check.accept(java.util.Arrays.equals(dense, section.getLongArray("BlockStates").orElseThrow()), "Preparing a 1.14 candle palette must not realign its dense indices early");
        section.putLongArray("BlockStates", net.minecraft.util.datafix.fixes.BitStorageAlignFix.addPadding(4096, 5, dense));
        NativeLegacyBridge.finishChunk(root);
        ListTag fixedPalette = section.getListOrEmpty("Palette");
        int width = Math.max(4, 32 - Integer.numberOfLeadingZeros(fixedPalette.size() - 1));
        var data = new net.minecraft.util.SimpleBitStorage(width, 4096, section.getLongArray("BlockStates").orElseThrow());
        for (int cell = 0; cell < 4096; cell++) {
            int original = cell % 18;
            String expected = original == 1 ? "minecraft:candle" : palette.getCompoundOrEmpty(original).getStringOr("Name", "");
            check.accept(fixedPalette.getCompoundOrEmpty(data.get(cell)).getStringOr("Name", "").equals(expected), "Candle import preserves every neighbouring terrain cell after vanilla alignment");
        }
    }
    private static void numericStates(BiConsumer<Boolean, String> check) {
        try {
            var decode = zone.moddev.mc.ironagefurniture.migration.LegacyWorldDataHook.class.getDeclaredMethod(
                    "legacyState", Identifier.class, net.minecraft.world.level.block.Block.class, int.class);
            decode.setAccessible(true);
            var blocks = net.minecraft.core.registries.BuiltInRegistries.BLOCK;
            for (int meta = 0; meta < 16; meta++) {
                Identifier bedId = Identifier.parse("ironagefurniture:bed_canopy_foot_lower_oak");
                var state = (net.minecraft.world.level.block.state.BlockState)decode.invoke(null, bedId, blocks.getValue(bedId), meta);
                check.accept(state.getValue(zone.moddev.mc.ironagefurniture.api.blocks.furniture.FurnitureBed.PART)
                        == ((meta & 8) == 0 ? net.minecraft.world.level.block.state.properties.BedPart.FOOT : net.minecraft.world.level.block.state.properties.BedPart.HEAD), "Legacy canopy head bit follows the old enum order");
                check.accept(state.getValue(zone.moddev.mc.ironagefurniture.api.blocks.furniture.FurnitureBed.UPPER) == ((meta & 4) != 0), "Legacy canopy upper bit follows the old enum order");
                for (boolean lit : new boolean[]{false, true}) for (boolean wall : new boolean[]{false, true}) {
                    Identifier id = Identifier.parse("ironagefurniture:light_metal_ironage_candle_" + (wall ? "wall" : "floor") + (lit ? "" : "_unlit"));
                    var candle = wall ? zone.moddev.mc.ironagefurniture.init.PhaseFourLighting.WALL_CANDLES.get() : net.minecraft.world.level.block.Blocks.CANDLE;
                    var candleState = (net.minecraft.world.level.block.state.BlockState)decode.invoke(null, id, candle, meta);
                    check.accept(candleState.getValue(wall ? zone.moddev.mc.ironagefurniture.api.blocks.lightsource.phasefour.CandleSconceBlock.LIT : net.minecraft.world.level.block.CandleBlock.LIT) == lit,
                            "Numeric standalone candle import preserves its old lit state");
                }
            }
        } catch (ReflectiveOperationException failure) { throw new AssertionError("Numeric legacy state probe failed", failure); }
    }
    private static void worldMappingOwnership(BiConsumer<Boolean, String> check) {
        try {
            Class<?> hook = zone.moddev.mc.ironagefurniture.migration.LegacyWorldDataHook.class;
            var install = hook.getDeclaredMethod("installLegacyBlockStates", CompoundTag.class);
            var restore = hook.getDeclaredMethod("restoreInstalledStates");
            var installedField = hook.getDeclaredField("INSTALLED_STATES");
            install.setAccessible(true); restore.setAccessible(true); installedField.setAccessible(true);
            check.accept(((java.util.Map<?, ?>)installedField.get(null)).isEmpty(), "The ownership test runs only in a new probe world");
            var states = zone.moddev.mc.ironagefurniture.mixin.BlockStateDataAccessor.iaf$getLegacyStateMap();
            int a = 3900 << 4, b = 3901 << 4;
            var beforeA = states[a]; var beforeB = states[b];
            for (int numeric : new int[]{3900, 3901}) {
                CompoundTag snapshot = new CompoundTag(), entry = new CompoundTag(); ListTag ids = new ListTag();
                entry.putString("K", "ironagefurniture:chair_wood_ironage_classic_oak"); entry.putInt("V", numeric);
                ids.add(entry); snapshot.put("ids", ids); install.invoke(null, snapshot);
                check.accept(states[numeric << 4] != (numeric == 3900 ? beforeA : beforeB), "The new world's numeric mapping is installed");
            }
            check.accept(states[a] == beforeA, "Switching worlds restores the previous numeric mapping");
            CompoundTag otherState = new CompoundTag(); otherState.putString("Name", "minecraft:stone");
            var otherOwner = new com.mojang.serialization.Dynamic<>(NbtOps.INSTANCE, otherState);
            states[b] = otherOwner; restore.invoke(null);
            check.accept(states[b] == otherOwner, "Cleanup preserves a later mapping owned by another mod");
            states[b] = beforeB;
        } catch (ReflectiveOperationException failure) { throw new AssertionError("World mapping ownership probe failed", failure); }
    }
    private static void cfmContracts(ServerLevel level, BiConsumer<Boolean, String> check) {
        var mappings = zone.moddev.mc.ironagefurniture.migration.CfmChairMigration.chairMappings();
        check.accept(mappings.size() == 14, "The published legacy CFM chair contract contains fourteen IDs");
        check.accept(!zone.moddev.mc.ironagefurniture.IronAgeFurnitureConfiguration.FORCE_CFM_CONVERSION.getDefault(), "Forced CFM conversion stays opt-in");
        for (var mapping : mappings.entrySet()) {
            CompoundTag item = stack(mapping.getKey(), true); item.putByte("Count", (byte)7); item.putShort("Damage", (short)0);
            CompoundTag custom = new CompoundTag(); custom.putLong("KeepLong", 123456789012345L); custom.putString("KeepText", "legacy chair"); item.put("tag", custom);
            CompoundTag root = new CompoundTag(), nested = new CompoundTag(); ListTag contents = new ListTag(); contents.add(item);
            nested.put("Items", contents); root.put("Nested", nested);
            zone.moddev.mc.ironagefurniture.migration.CfmChairMigration.migratePlayerData(root);
            check.accept(item.getStringOr("id", "").equals(mapping.getValue()) && item.getByteOr("Count", (byte)0) == 7
                    && item.getCompoundOrEmpty("tag").equals(custom), "Removed CFM player recovery preserves nested counts and unrelated data");
            CompoundTag first = root.copy();
            zone.moddev.mc.ironagefurniture.migration.CfmChairMigration.migrateEntityData(root);
            check.accept(root.equals(first), "CFM conversion is idempotent across player/entity recovery paths");
            CompoundTag oldStack = stack(mapping.getKey(), true); oldStack.putByte("Count", (byte)7); oldStack.putByte("Slot", (byte)0); oldStack.put("tag", custom.copy());
            ListTag inventory = new ListTag(); inventory.add(oldStack); CompoundTag player = new CompoundTag(); player.put("Inventory", inventory);
            int current = net.minecraft.SharedConstants.getCurrentVersion().dataVersion().version();
            CompoundTag fixed = (CompoundTag)net.minecraft.util.datafix.DataFixTypes.PLAYER.update(net.minecraft.util.datafix.DataFixers.getDataFixer(),
                    new com.mojang.serialization.Dynamic<>(NbtOps.INSTANCE, player), 2730, current).getValue();
            ItemStack decoded = ItemStack.CODEC.parse(level.registryAccess().createSerializationContext(NbtOps.INSTANCE), fixed.getListOrEmpty("Inventory").getCompoundOrEmpty(0)).getOrThrow();
            check.accept(net.minecraft.core.registries.BuiltInRegistries.ITEM.getKey(decoded.getItem()).toString().equals(mapping.getValue()) && decoded.getCount() == 7,
                    "The real native player datafixing path recovers each supported CFM chair before item decoding");
            check.accept(decoded.get(net.minecraft.core.component.DataComponents.CUSTOM_DATA).copyTag().equals(custom), "Native CFM recovery retains complete unrelated custom data");
            for (String facing : new String[]{"north", "south", "east", "west"}) {
                CompoundTag chunk = chunk(mapping.getKey(), false);
                cell(chunk).getCompoundOrEmpty("Properties").putString("facing", facing);
                NativeLegacyBridge.prepareChunk(chunk, 2730); NativeLegacyBridge.finishChunk(chunk);
                check.accept(cell(chunk).getStringOr("Name", "").equals(mapping.getValue())
                        && cell(chunk).getCompoundOrEmpty("Properties").getStringOr("facing", "").equals(facing), "CFM placed-chair recovery preserves its facing");
            }
        }
        try {
            var rewrite = zone.moddev.mc.ironagefurniture.migration.CfmChairMigration.class.getDeclaredMethod("rewriteItems", CompoundTag.class, boolean.class, java.util.function.Predicate.class);
            rewrite.setAccessible(true);
            CompoundTag guarded = stack("cfm:oak_chair", true), unchanged = guarded.copy();
            java.util.function.Predicate<String> available = id -> true, missing = id -> false;
            check.accept(((Integer)rewrite.invoke(null, guarded, false, available)) == 0 && guarded.equals(unchanged), "The default-off conversion gate leaves installed-provider items alone");
            check.accept(((Integer)rewrite.invoke(null, guarded, true, missing)) == 0 && guarded.equals(unchanged), "An unavailable target never consumes an old CFM item");
            check.accept(((Integer)rewrite.invoke(null, guarded, true, available)) == 1 && guarded.getStringOr("id", "").equals("ironagefurniture:chair_wood_ironage_classic_oak"), "An explicitly enabled conversion uses the exact supported target");
            CompoundTag unrelated = stack("cfm:oak_table", true), original = unrelated.copy();
            check.accept(((Integer)rewrite.invoke(null, unrelated, true, available)) == 0 && unrelated.equals(original), "Unrelated CFM furniture is never guessed or converted");
            var recipeMigration = Class.forName("zone.moddev.mc.ironagefurniture.migration.LegacyRecipeBookMigration").getDeclaredMethod("prepare", CompoundTag.class);
            recipeMigration.setAccessible(true);
            CompoundTag player = new CompoundTag(), book = new CompoundTag(); ListTag learned = new ListTag();
            for (String id : new String[]{"ironagefurniture:bed_recolour", "ironagefurniture:bed_canopy_foot_lower_oak_recolour_blue", "minecraft:bread"}) learned.add(net.minecraft.nbt.StringTag.valueOf(id));
            book.put("recipes", learned); book.put("toBeDisplayed", learned.copy()); player.put("recipeBook", book);
            recipeMigration.invoke(null, player);
            check.accept(book.getListOrEmpty("recipes").size() == 1 && book.getListOrEmpty("recipes").getStringOr(0, "").equals("minecraft:bread")
                    && book.getListOrEmpty("toBeDisplayed").equals(book.getListOrEmpty("recipes")), "Previously learned recolouring entries leave both recipe-book lists without removing unrelated recipes");
            CompoundTag once = player.copy(); recipeMigration.invoke(null, player);
            check.accept(player.equals(once), "Recipe-book cleanup is idempotent");
        } catch (ReflectiveOperationException failure) { throw new AssertionError("CFM conversion-gate contract failed", failure); }
        System.out.println("IAF_CFM_CONTRACT_PASS ids=" + mappings.size() + " liveProvider=false");
    }
    private static CompoundTag stack(String id, boolean old) {
        CompoundTag item = new CompoundTag(); item.putString("id", id);
        if (old) item.putByte("Count", (byte)1); else item.putInt("count", 1);
        return item;
    }
    private static CompoundTag tile(String id) {
        CompoundTag tile = new CompoundTag(); tile.putString("id", id); tile.putInt("x", 0); tile.putInt("y", 96); tile.putInt("z", 0); return tile;
    }
    static CompoundTag chunk(String id, boolean modern) {
        CompoundTag root = new CompoundTag(), level = new CompoundTag(), section = new CompoundTag(), state = new CompoundTag(), properties = new CompoundTag();
        root.putInt("DataVersion", modern ? 5006 : 2730); level.putInt("xPos", 0); level.putInt("zPos", 0);
        properties.putString("facing", "north"); properties.putString("waterlogged", "false");
        state.putString(modern ? "id" : "Name", id); state.put(modern ? "properties" : "Properties", properties);
        ListTag palette = new ListTag(); CompoundTag air = new CompoundTag(); air.putString(modern ? "id" : "Name", "minecraft:air"); palette.add(air); palette.add(state);
        net.minecraft.util.SimpleBitStorage bits = new net.minecraft.util.SimpleBitStorage(4, 4096); bits.set(0, 1);
        section.putByte("Y", (byte)6);
        if (modern) { CompoundTag blocks = new CompoundTag(); blocks.put("palette", palette); blocks.putLongArray("data", bits.getRaw()); section.put("block_states", blocks); }
        else { section.put("Palette", palette); section.putLongArray("BlockStates", bits.getRaw()); }
        ListTag sections = new ListTag(); sections.add(section); level.put(modern ? "sections" : "Sections", sections); level.put(modern ? "block_entities" : "TileEntities", new ListTag());
        if (modern) { root.merge(level); } else root.put("Level", level);
        return root;
    }
    static CompoundTag cell(CompoundTag root) {
        CompoundTag section = root.getCompoundOrEmpty("Level").getListOrEmpty("Sections").getCompoundOrEmpty(0);
        ListTag palette = section.getListOrEmpty("Palette");
        int bits = Math.max(4, 32 - Integer.numberOfLeadingZeros(palette.size() - 1));
        return palette.getCompoundOrEmpty(new net.minecraft.util.SimpleBitStorage(bits, 4096, section.getLongArray("BlockStates").orElseThrow()).get(0));
    }
    private MigrationProbe() { }
}
