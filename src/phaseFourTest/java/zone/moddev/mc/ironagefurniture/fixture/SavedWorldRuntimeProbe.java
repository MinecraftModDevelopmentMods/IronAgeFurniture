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
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.NumberNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.server.MinecraftServer;
import net.minecraft.state.Property;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.ChestTileEntity;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.state.properties.ChestType;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.FakePlayerFactory;

/** Compares loaded worlds with independently recorded source expectations. */
final class SavedWorldRuntimeProbe {
    private SavedWorldRuntimeProbe() { }

    static void trace(net.minecraftforge.event.world.ChunkDataEvent.Load event) {
        if (!Boolean.getBoolean("iaf.probe.traceMigration")) return;
        CompoundNBT level = event.getData().getCompound("Level");
        for (INBT entry : level.getList("TileEntities", 10)) {
            CompoundNBT tile = (CompoundNBT)entry;
            for (INBT value : tile.getList("Items", 10)) {
                CompoundNBT item = (CompoundNBT)value;
                if (item.getString("id").startsWith("ironagefurniture:")) org.apache.logging.log4j.LogManager.getLogger().info(
                        "IAF IMPORT TRACE: tile={} at {},{},{} item={} count={} slot={} registered={} decoded={}",
                        tile.getString("id"), tile.getInt("x"), tile.getInt("y"), tile.getInt("z"),
                        item.getString("id"), item.getByte("Count"), item.getByte("Slot"),
                        net.minecraftforge.registries.ForgeRegistries.ITEMS.containsKey(new net.minecraft.util.ResourceLocation(item.getString("id"))),
                        net.minecraft.item.ItemStack.of(item).save(new CompoundNBT()));
            }
        }
    }

    static void prepare(MinecraftServer server) throws Exception {
        JsonObject manifest = readManifest();
        for (JsonElement entry : manifest.getAsJsonArray("chunks")) {
            JsonObject chunk = entry.getAsJsonObject();
            world(server, chunk).getChunk(new BlockPos(chunk.get("x").getAsInt() * 16, 64, chunk.get("z").getAsInt() * 16));
        }
        // Dropped stacks can merge during the two ticks needed for block
        // compatibility conversion. Check their import first, then keep the
        // disposable fixture's entities separate for the reload comparison.
        for (JsonElement entry : manifest.getAsJsonArray("entities")) {
            JsonObject expected = entry.getAsJsonObject();
            UUID uuid = UUID.fromString(expected.get("uuid").getAsString());
            Entity entity = world(server, expected).getEntitiesOfClass(Entity.class,
                    new AxisAlignedBB(position(expected)).inflate(32)).stream()
                    .filter(candidate -> uuid.equals(candidate.getUUID())).findFirst()
                    .orElseThrow(() -> new IllegalStateException("Imported entity disappeared: " + expected));
            CompoundNBT actual = new CompoundNBT();
            entity.saveAsPassenger(actual);
            for (JsonElement item : expected.getAsJsonArray("items")) verifyStack(item.getAsJsonObject(), actual);
            if (entity instanceof net.minecraft.entity.item.ItemEntity)
                ((net.minecraft.entity.item.ItemEntity)entity).setNeverPickUp();
        }
        // Redstone lights can legitimately change on the first tick. Check
        // their saved identity before that tick as well as afterwards.
        for (JsonElement entry : manifest.getAsJsonArray("blocks")) {
            JsonObject expected = entry.getAsJsonObject();
            if ((expected.has("alternatives") || singleTorch(expected.get("id").getAsString()))
                    && !Boolean.getBoolean("iaf.probe.savedWorldReload")) verifyBlock(server, expected, false);
        }
    }

    private static JsonObject readManifest() throws Exception {
        JsonObject manifest;
        try (Reader reader = Files.newBufferedReader(Paths.get("saved-world-expectations.json"), StandardCharsets.UTF_8)) {
            manifest = new JsonParser().parse(reader).getAsJsonObject();
        }
        return manifest;
    }

    static int run(MinecraftServer server) throws Exception {
        JsonObject manifest = readManifest();
        require(!manifest.has("unsupported") || manifest.getAsJsonObject("unsupported").entrySet().isEmpty(),
                "Source furniture was excluded from expectations: " + manifest.get("unsupported"));
        int cases = 0;
        Set<String> matchedContainerSlots = new HashSet<>();
        for (JsonElement entry : manifest.getAsJsonArray("blocks")) {
            JsonObject expected = entry.getAsJsonObject();
            verifyBlock(server, expected, true);
            cases++;
        }
        for (JsonElement entry : manifest.getAsJsonArray("containers")) {
            JsonObject expected = entry.getAsJsonObject();
            TileEntity tile = world(server, expected).getBlockEntity(position(expected));
            require(tile != null, "Saved container disappeared: " + expected);
            for (JsonElement item : expected.getAsJsonArray("items")) {
                verifyContainerStack(server, expected, item.getAsJsonObject(), matchedContainerSlots);
                cases++;
            }
        }
        for (JsonElement entry : manifest.getAsJsonArray("entities")) {
            JsonObject expected = entry.getAsJsonObject();
            ServerWorld world = world(server, expected);
            BlockPos pos = position(expected);
            UUID uuid = UUID.fromString(expected.get("uuid").getAsString());
            Entity entity = world.getEntitiesOfClass(Entity.class, new AxisAlignedBB(pos).inflate(32)).stream()
                    .filter(candidate -> uuid.equals(candidate.getUUID())).findFirst()
                    .orElseThrow(() -> new IllegalStateException("Saved entity disappeared: " + expected));
            CompoundNBT actual = new CompoundNBT();
            entity.saveAsPassenger(actual);
            for (JsonElement item : expected.getAsJsonArray("items")) { verifyStack(item.getAsJsonObject(), actual); cases++; }
        }
        for (JsonElement entry : manifest.getAsJsonArray("players")) {
            JsonObject expected = entry.getAsJsonObject();
            ServerWorld world = server.getLevel(World.OVERWORLD);
            FakePlayer player = FakePlayerFactory.get(world, new GameProfile(UUID.fromString(expected.get("uuid").getAsString()), "SavedWorldProbe"));
            require(ProbePlayerStorage.storage(world).load(player) != null, "Saved player disappeared");
            CompoundNBT actual = player.saveWithoutId(new CompoundNBT());
            for (JsonElement item : expected.getAsJsonArray("items")) { verifyStack(item.getAsJsonObject(), actual); cases++; }
            ProbePlayerStorage.storage(world).save(player);
        }
        if (manifest.has("embedded_player") && manifest.getAsJsonArray("embedded_player").size() > 0) {
            CompoundNBT actual = server.getWorldData().getLoadedPlayerTag();
            require(actual != null, "Embedded single-player inventory disappeared");
            for (JsonElement item : manifest.getAsJsonArray("embedded_player")) { verifyStack(item.getAsJsonObject(), actual); cases++; }
        }
        require(cases > 0, "The source manifest contains no furniture");
        return cases;
    }

    private static void verifyBlock(MinecraftServer server, JsonObject expected, boolean afterTick) {
        ServerWorld world = world(server, expected);
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
            TileEntity tile = world.getBlockEntity(pos);
            require(tile != null, "Saved tile disappeared at " + pos);
            find(expected.getAsJsonObject("tile"), tile.save(new CompoundNBT()), "tile " + pos);
        }
    }

    private static boolean singleTorch(String id) {
        return id.matches("ironagefurniture:light_metal_ironage_sconce_(floor|wall)_torch_iron(_unlit)?");
    }

    private static void verifyStack(JsonObject expected, CompoundNBT root) {
        INBT current = root;
        for (JsonElement part : expected.getAsJsonArray("path")) {
            if (part.isJsonObject()) {
                int slot = part.getAsJsonObject().get("slot").getAsInt();
                INBT found = null;
                for (INBT item : (ListNBT)current) if (((CompoundNBT)item).getByte("Slot") == slot) { found = item; break; }
                current = found;
            } else if (part.getAsJsonPrimitive().isNumber()) current = ((ListNBT)current).get(part.getAsInt());
            else current = ((CompoundNBT)current).get(part.getAsString());
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
                    require(world(server, paired).getBlockEntity(position(paired)) instanceof ChestTileEntity
                            && state.hasProperty(BlockStateProperties.CHEST_TYPE) && state.getValue(BlockStateProperties.CHEST_TYPE) != ChestType.SINGLE,
                            "Cannot recover a moved stack from an unpaired chest: " + paired);
                }
            }
            String key = half.get("dimension") + ":" + position(half) + ":" + item.get("path");
            if (matched.contains(key)) continue;
            TileEntity tile = world(server, half).getBlockEntity(position(half));
            try {
                verifyStack(item, tile.save(new CompoundNBT()));
                require(matched.add(key), "One saved stack satisfied two expectations: " + key);
                return;
            } catch (IllegalStateException mismatch) {
                // Vanilla's old double-chest fixer can swap inventories between
                // halves. Count each matching slot once, without relaxing its data.
            }
        }
        throw new IllegalStateException("Saved container stack missing or changed at " + position(container) + ": " + item);
    }

    private static void find(JsonElement expected, INBT actual, String context) {
        require(actual != null, "Missing NBT: " + context);
        if (expected.isJsonObject()) {
            JsonObject object = expected.getAsJsonObject();
            if (object.has("id") && "minecraft:shield".equals(object.get("id").getAsString()) && object.has("Damage")) {
                try {
                    CompoundNBT oldShield = net.minecraft.nbt.JsonToNBT.parseTag(object.toString());
                    CompoundNBT fixed = (CompoundNBT)net.minecraft.util.datafix.DataFixesManager.getDataFixer().update(
                            net.minecraft.util.datafix.TypeReferences.ITEM_STACK,
                            new com.mojang.serialization.Dynamic<>(net.minecraft.nbt.NBTDynamicOps.INSTANCE, oldShield),
                            1343, net.minecraft.util.SharedConstants.getCurrentVersion().getWorldVersion()).getValue();
                    matchNbt(fixed, actual, context);
                    return;
                } catch (com.mojang.brigadier.exceptions.CommandSyntaxException failure) { throw new IllegalStateException(failure); }
            }
            require(actual instanceof CompoundNBT, "Expected compound: " + context);
            for (Map.Entry<String, JsonElement> child : expected.getAsJsonObject().entrySet())
                find(child.getValue(), ((CompoundNBT)actual).get(child.getKey()), context + "." + child.getKey());
        } else if (expected.isJsonArray()) {
            require(actual instanceof ListNBT && ((ListNBT)actual).size() == expected.getAsJsonArray().size(), "List changed: " + context);
            int index = 0;
            for (JsonElement child : expected.getAsJsonArray()) find(child, ((ListNBT)actual).get(index++), context);
        } else if (expected.getAsJsonPrimitive().isNumber()) {
            require(actual instanceof NumberNBT && expected.getAsDouble() == ((NumberNBT)actual).getAsDouble(), "Number changed: " + context + ": " + actual);
        } else {
            require(actual instanceof StringNBT && expected.getAsString().equals(actual.getAsString()), "Text changed: " + context + ": " + actual);
        }
    }

    private static void matchNbt(INBT expected, INBT actual, String context) {
        require(actual != null, "Missing fixed shield NBT: " + context);
        if (expected instanceof CompoundNBT) {
            require(actual instanceof CompoundNBT, "Shield compound changed: " + context);
            for (String key : ((CompoundNBT)expected).getAllKeys()) matchNbt(((CompoundNBT)expected).get(key), ((CompoundNBT)actual).get(key), context + "." + key);
        } else if (expected instanceof NumberNBT) {
            require(actual instanceof NumberNBT && ((NumberNBT)expected).getAsDouble() == ((NumberNBT)actual).getAsDouble(), "Shield number changed: " + context);
        } else if (expected instanceof ListNBT) {
            require(actual instanceof ListNBT && ((ListNBT)expected).size() == ((ListNBT)actual).size(), "Shield list changed: " + context);
            for (int index = 0; index < ((ListNBT)expected).size(); index++) matchNbt(((ListNBT)expected).get(index), ((ListNBT)actual).get(index), context);
        } else require(expected.equals(actual), "Shield data changed: " + context);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private static String propertyName(BlockState state, Property property) { return property.getName(state.getValue(property)); }
    private static BlockPos position(JsonObject value) { return new BlockPos(value.get("x").getAsInt(), value.get("y").getAsInt(), value.get("z").getAsInt()); }
    private static ServerWorld world(MinecraftServer server, JsonObject value) {
        int dimension = value.get("dimension").getAsInt();
        require(dimension == 0 || dimension == -1 || dimension == 1, "Unsupported source dimension: " + dimension);
        return server.getLevel(dimension == -1 ? World.NETHER : dimension == 1 ? World.END : World.OVERWORLD);
    }
    private static void require(boolean okay, String message) { if (!okay) throw new IllegalStateException(message); }
}
