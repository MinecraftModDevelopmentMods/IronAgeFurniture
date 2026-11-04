package zone.moddev.mc.ironagefurniture.fixture;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.authlib.GameProfile;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.Entity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NumericTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.ChestType;
import net.minecraft.world.phys.AABB;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.FakePlayerFactory;

/** Compares loaded worlds with independently recorded source expectations. */
final class SavedWorldRuntimeProbe {
    private static final Set<String> importedLightStates = java.util.concurrent.ConcurrentHashMap.newKeySet();
    private static JsonObject manifestCache;
    private SavedWorldRuntimeProbe() { }

    static void trace(net.minecraftforge.event.world.ChunkDataEvent.Load event) {
        if (Boolean.getBoolean("iaf.probe.savedWorld") && !Boolean.getBoolean("iaf.probe.savedWorldReload")) {
            try {
                for (JsonElement entry : readManifest().getAsJsonArray("blocks")) {
                    JsonObject expected = entry.getAsJsonObject();
                    if (!(expected.has("alternatives") || singleTorch(expected.get("id").getAsString()))) continue;
                    BlockPos pos = position(expected);
                    if (!event.getChunk().getPos().equals(new net.minecraft.world.level.ChunkPos(pos))) continue;
                    // ProtoChunk events for newly generated spawn terrain have
                    // no level. Only saved full chunks provide import evidence.
                    if (!(event.getWorld() instanceof Level)) continue;
                    Level eventWorld = (Level)event.getWorld();
                    int dimension = eventWorld.dimension() == Level.NETHER ? -1 : eventWorld.dimension() == Level.END ? 1 : 0;
                    if (expected.get("dimension").getAsInt() != dimension) continue;
                    String id = event.getChunk().getBlockState(pos).getBlock().getRegistryName().toString();
                    require(id.equals(expected.get("id").getAsString()), "Imported light state changed before world ticks: " + expected + " -> " + id);
                    importedLightStates.add(expected.get("dimension") + ":" + pos);
                }
            } catch (Exception failure) { throw new IllegalStateException("Could not verify pre-tick light import", failure); }
        }
        if (!Boolean.getBoolean("iaf.probe.traceMigration")) return;
        CompoundTag level = event.getData().getCompound("Level");
        for (Tag entry : level.getList("TileEntities", 10)) {
            CompoundTag tile = (CompoundTag)entry;
            for (Tag value : tile.getList("Items", 10)) {
                CompoundTag item = (CompoundTag)value;
                if (item.getString("id").startsWith("ironagefurniture:")) org.apache.logging.log4j.LogManager.getLogger().info(
                        "IAF IMPORT TRACE: tile={} at {},{},{} item={} count={} slot={} registered={} decoded={}",
                        tile.getString("id"), tile.getInt("x"), tile.getInt("y"), tile.getInt("z"),
                        item.getString("id"), item.getByte("Count"), item.getByte("Slot"),
                        net.minecraftforge.registries.ForgeRegistries.ITEMS.containsKey(new net.minecraft.resources.ResourceLocation(item.getString("id"))),
                        net.minecraft.world.item.ItemStack.of(item).save(new CompoundTag()));
            }
        }
    }

    static void loadChunks(MinecraftServer server) throws Exception {
        JsonObject manifest = readManifest();
        for (JsonElement entry : manifest.getAsJsonArray("chunks")) {
            JsonObject chunk = entry.getAsJsonObject();
            world(server, chunk).setChunkForced(chunk.get("x").getAsInt(), chunk.get("z").getAsInt(), true);
            world(server, chunk).getChunk(new BlockPos(chunk.get("x").getAsInt() * 16, 64, chunk.get("z").getAsInt() * 16));
        }
    }

    static boolean chunksReady(MinecraftServer server) throws Exception {
        for (JsonElement entry : readManifest().getAsJsonArray("chunks")) {
            JsonObject chunk = entry.getAsJsonObject();
            if (!world(server, chunk).isPositionTickingWithEntitiesLoaded(new BlockPos(chunk.get("x").getAsInt() * 16, 64, chunk.get("z").getAsInt() * 16))) return false;
        }
        return true;
    }

    static void prepare(MinecraftServer server) throws Exception {
        JsonObject manifest = readManifest();
        // Dropped stacks can merge during the two ticks needed for block
        // compatibility conversion. Check their import first, then keep the
        // disposable fixture's entities separate for the reload comparison.
        for (JsonElement entry : manifest.getAsJsonArray("entities")) {
            JsonObject expected = entry.getAsJsonObject();
            UUID uuid = UUID.fromString(expected.get("uuid").getAsString());
            Entity entity = world(server, expected).getEntitiesOfClass(Entity.class,
                    new AABB(position(expected)).inflate(32)).stream()
                    .filter(candidate -> uuid.equals(candidate.getUUID())).findFirst()
                    .orElseThrow(() -> new IllegalStateException("Imported entity disappeared: " + expected));
            CompoundTag actual = new CompoundTag();
            entity.saveAsPassenger(actual);
            for (JsonElement item : expected.getAsJsonArray("items")) verifyStack(item.getAsJsonObject(), actual);
            if (entity instanceof net.minecraft.world.entity.item.ItemEntity)
                ((net.minecraft.world.entity.item.ItemEntity)entity).setNeverPickUp();
        }
        // Redstone lights can legitimately change on the first tick. Check
        // their saved identity before that tick as well as afterwards.
        for (JsonElement entry : manifest.getAsJsonArray("blocks")) {
            JsonObject expected = entry.getAsJsonObject();
            if ((expected.has("alternatives") || singleTorch(expected.get("id").getAsString()))
                    && !Boolean.getBoolean("iaf.probe.savedWorldReload"))
                require(importedLightStates.contains(expected.get("dimension") + ":" + position(expected)),
                        "No pre-tick light import evidence: " + expected);
        }
    }

    private static synchronized JsonObject readManifest() throws Exception {
        if (manifestCache != null) return manifestCache;
        JsonObject manifest;
        try (Reader reader = Files.newBufferedReader(Paths.get("saved-world-expectations.json"), StandardCharsets.UTF_8)) {
            manifest = new JsonParser().parse(reader).getAsJsonObject();
        }
        manifestCache = manifest;
        return manifestCache;
    }

    static int run(MinecraftServer server) throws Exception {
        JsonObject manifest = readManifest();
        if (manifest.has("unsupported") && !manifest.getAsJsonObject("unsupported").entrySet().isEmpty()) {
            require(Boolean.getBoolean("iaf.probe.supportedCatalogOnly"),
                    "Source furniture was excluded from expectations: " + manifest.get("unsupported"));
            org.apache.logging.log4j.LogManager.getLogger().warn(
                    "IAF SUPPORTED-CATALOG TEST ONLY; excluded source furniture is NOT verified: {}", manifest.get("unsupported"));
        }
        int cases = 0;
        Set<String> matchedContainerSlots = new HashSet<>();
        for (JsonElement entry : manifest.getAsJsonArray("blocks")) {
            JsonObject expected = entry.getAsJsonObject();
            verifyBlock(server, expected, true);
            cases++;
        }
        for (JsonElement entry : manifest.getAsJsonArray("containers")) {
            JsonObject expected = entry.getAsJsonObject();
            BlockEntity tile = world(server, expected).getBlockEntity(position(expected));
            require(tile != null, "Saved container disappeared: " + expected);
            for (JsonElement item : expected.getAsJsonArray("items")) {
                verifyContainerStack(server, expected, item.getAsJsonObject(), matchedContainerSlots);
                cases++;
            }
        }
        for (JsonElement entry : manifest.getAsJsonArray("entities")) {
            JsonObject expected = entry.getAsJsonObject();
            ServerLevel world = world(server, expected);
            BlockPos pos = position(expected);
            UUID uuid = UUID.fromString(expected.get("uuid").getAsString());
            Entity entity = world.getEntitiesOfClass(Entity.class, new AABB(pos).inflate(32)).stream()
                    .filter(candidate -> uuid.equals(candidate.getUUID())).findFirst()
                    .orElseThrow(() -> new IllegalStateException("Saved entity disappeared: " + expected));
            CompoundTag actual = new CompoundTag();
            entity.saveAsPassenger(actual);
            for (JsonElement item : expected.getAsJsonArray("items")) { verifyStack(item.getAsJsonObject(), actual); cases++; }
        }
        for (JsonElement entry : manifest.getAsJsonArray("players")) {
            JsonObject expected = entry.getAsJsonObject();
            ServerLevel world = server.getLevel(Level.OVERWORLD);
            FakePlayer player = FakePlayerFactory.get(world, new GameProfile(UUID.fromString(expected.get("uuid").getAsString()), "SavedWorldProbe"));
            require(ProbePlayerStorage.storage(world).load(player) != null, "Saved player disappeared");
            CompoundTag actual = player.saveWithoutId(new CompoundTag());
            for (JsonElement item : expected.getAsJsonArray("items")) { verifyStack(item.getAsJsonObject(), actual); cases++; }
            ProbePlayerStorage.storage(world).save(player);
        }
        if (manifest.has("embedded_player") && manifest.getAsJsonArray("embedded_player").size() > 0) {
            CompoundTag actual = server.getWorldData().getLoadedPlayerTag();
            require(actual != null, "Embedded single-player inventory disappeared");
            for (JsonElement item : manifest.getAsJsonArray("embedded_player")) { verifyStack(item.getAsJsonObject(), actual); cases++; }
        }
        require(cases > 0, "The source manifest contains no furniture");
        return cases;
    }

    private static void verifyBlock(MinecraftServer server, JsonObject expected, boolean afterTick) {
        ServerLevel world = world(server, expected);
        BlockPos pos = position(expected);
        BlockState actual = world.getBlockState(pos);
        String actualId = actual.getBlock().getRegistryName().toString();
        boolean sameId = expected.get("id").getAsString().equals(actualId);
        if (afterTick && expected.has("alternatives")) for (JsonElement alternative : expected.getAsJsonArray("alternatives")) sameId |= alternative.getAsString().equals(actualId);
        if (afterTick && singleTorch(expected.get("id").getAsString())) {
            String saved = expected.get("id").getAsString();
            // The fixture also contains redstone torches. Their queued updates
            // can change this flame after import; the pre-tick check above
            // verifies the saved identity, and the separate redstone probe
            // checks the exact powered/wet transitions in a controlled circuit.
            sameId = saved.replaceFirst("_unlit$", "").equals(actualId.replaceFirst("_unlit$", ""));
        }
        require(sameId, "Saved block changed at " + pos + ": " + expected + " -> " + actual);
        for (Map.Entry<String, JsonElement> property : expected.getAsJsonObject("properties").entrySet()) {
            Property<?> key = actual.getBlock().getStateDefinition().getProperty(property.getKey());
            boolean enteredWater = afterTick && expected.has("adjacent_source_water") && "waterlogged".equals(property.getKey())
                    && key != null && "true".equals(propertyName(actual, key));
            require(enteredWater || key != null && property.getValue().getAsString().equals(propertyName(actual, key)),
                    "Saved property changed at " + pos + ": " + property + " -> " + actual);
        }
        if (expected.has("tile")) {
            BlockEntity tile = world.getBlockEntity(pos);
            require(tile != null, "Saved tile disappeared at " + pos);
            find(expected.getAsJsonObject("tile"), tile.save(new CompoundTag()), "tile " + pos);
        }
    }

    private static boolean singleTorch(String id) {
        return id.matches("ironagefurniture:light_metal_ironage_sconce_(floor|wall)_torch_iron(_unlit)?");
    }

    private static void verifyStack(JsonObject expected, CompoundTag root) {
        Tag current = root;
        for (JsonElement part : expected.getAsJsonArray("path")) {
            if (part.isJsonObject()) {
                int slot = part.getAsJsonObject().get("slot").getAsInt();
                Tag found = null;
                for (Tag item : (ListTag)current) if (((CompoundTag)item).getByte("Slot") == slot) { found = item; break; }
                current = found;
            } else if (part.getAsJsonPrimitive().isNumber()) current = ((ListTag)current).get(part.getAsInt());
            else current = ((CompoundTag)current).get(part.getAsString());
            require(current != null, "Saved stack path disappeared: " + expected);
        }
        find(expected.getAsJsonObject("stack"), current, "stack " + expected.get("path"));
    }

    private static void verifyContainerStack(MinecraftServer server, JsonObject container, JsonObject item, Set<String> matched) {
        JsonObject[] halves = container.has("chest_partner")
                ? new JsonObject[] {container, container.getAsJsonObject("chest_partner")} : new JsonObject[] {container};
        for (JsonObject half : halves) {
            if (half != container) {
                BlockPos first = position(container), second = position(half);
                require(first.getY() == second.getY() && Math.abs(first.getX() - second.getX()) + Math.abs(first.getZ() - second.getZ()) == 1,
                        "Source double-chest halves are not neighbours: " + container);
                for (JsonObject paired : halves) {
                    BlockState state = world(server, paired).getBlockState(position(paired));
                    require(world(server, paired).getBlockEntity(position(paired)) instanceof ChestBlockEntity
                            && state.hasProperty(BlockStateProperties.CHEST_TYPE) && state.getValue(BlockStateProperties.CHEST_TYPE) != ChestType.SINGLE,
                            "Cannot recover a moved stack from an unpaired chest: " + paired);
                }
            }
            String key = half.get("dimension") + ":" + position(half) + ":" + item.get("path");
            if (matched.contains(key)) continue;
            BlockEntity tile = world(server, half).getBlockEntity(position(half));
            try {
                verifyStack(item, tile.save(new CompoundTag()));
                require(matched.add(key), "One saved stack satisfied two expectations: " + key);
                return;
            } catch (IllegalStateException mismatch) {
                // Vanilla's old double-chest fixer can swap inventories between
                // halves. Count each matching slot once, without relaxing its data.
            }
        }
        throw new IllegalStateException("Saved container stack missing or changed at " + position(container) + ": " + item);
    }

    private static void find(JsonElement expected, Tag actual, String context) {
        require(actual != null, "Missing NBT: " + context);
        if (expected.isJsonObject()) {
            JsonObject object = expected.getAsJsonObject();
            if (object.has("id") && "minecraft:shield".equals(object.get("id").getAsString()) && object.has("Damage")) {
                try {
                    CompoundTag oldShield = net.minecraft.nbt.TagParser.parseTag(object.toString());
                    CompoundTag fixed = (CompoundTag)net.minecraft.util.datafix.DataFixers.getDataFixer().update(
                            net.minecraft.util.datafix.fixes.References.ITEM_STACK,
                            new com.mojang.serialization.Dynamic<>(net.minecraft.nbt.NbtOps.INSTANCE, oldShield),
                            1343, net.minecraft.SharedConstants.getCurrentVersion().getWorldVersion()).getValue();
                    matchNbt(fixed, actual, context);
                    return;
                } catch (com.mojang.brigadier.exceptions.CommandSyntaxException failure) { throw new IllegalStateException(failure); }
            }
            require(actual instanceof CompoundTag, "Expected compound: " + context);
            for (Map.Entry<String, JsonElement> child : expected.getAsJsonObject().entrySet())
                find(child.getValue(), ((CompoundTag)actual).get(child.getKey()), context + "." + child.getKey());
        } else if (expected.isJsonArray()) {
            require(actual instanceof ListTag && ((ListTag)actual).size() == expected.getAsJsonArray().size(), "List changed: " + context);
            int index = 0;
            for (JsonElement child : expected.getAsJsonArray()) find(child, ((ListTag)actual).get(index++), context);
        } else if (expected.getAsJsonPrimitive().isNumber()) {
            require(actual instanceof NumericTag && expected.getAsDouble() == ((NumericTag)actual).getAsDouble(), "Number changed: " + context + ": " + actual);
        } else {
            require(actual instanceof StringTag && expected.getAsString().equals(actual.getAsString()), "Text changed: " + context + ": " + actual);
        }
    }

    private static void matchNbt(Tag expected, Tag actual, String context) {
        require(actual != null, "Missing fixed shield NBT: " + context);
        if (expected instanceof CompoundTag) {
            require(actual instanceof CompoundTag, "Shield compound changed: " + context);
            for (String key : ((CompoundTag)expected).getAllKeys()) matchNbt(((CompoundTag)expected).get(key), ((CompoundTag)actual).get(key), context + "." + key);
        } else if (expected instanceof NumericTag) {
            require(actual instanceof NumericTag && ((NumericTag)expected).getAsDouble() == ((NumericTag)actual).getAsDouble(), "Shield number changed: " + context);
        } else if (expected instanceof ListTag) {
            require(actual instanceof ListTag && ((ListTag)expected).size() == ((ListTag)actual).size(), "Shield list changed: " + context);
            for (int index = 0; index < ((ListTag)expected).size(); index++) matchNbt(((ListTag)expected).get(index), ((ListTag)actual).get(index), context);
        } else require(expected.equals(actual), "Shield data changed: " + context);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private static String propertyName(BlockState state, Property property) { return property.getName(state.getValue(property)); }
    private static BlockPos position(JsonObject value) { return new BlockPos(value.get("x").getAsInt(), value.get("y").getAsInt(), value.get("z").getAsInt()); }
    private static ServerLevel world(MinecraftServer server, JsonObject value) {
        int dimension = value.get("dimension").getAsInt();
        require(dimension == 0 || dimension == -1 || dimension == 1, "Unsupported source dimension: " + dimension);
        return server.getLevel(dimension == -1 ? Level.NETHER : dimension == 1 ? Level.END : Level.OVERWORLD);
    }
    private static void require(boolean okay, String message) { if (!okay) throw new IllegalStateException(message); }
}
