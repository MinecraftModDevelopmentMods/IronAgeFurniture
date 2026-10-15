package zone.moddev.mc.ironagefurniture.fixture;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.serialization.Dynamic;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.UUID;
import com.mojang.authlib.GameProfile;
import net.minecraft.SharedConstants;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.TagParser;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ClientInformation;
import net.minecraft.server.players.NameAndId;
import net.minecraft.util.datafix.DataFixers;
import net.minecraft.util.datafix.fixes.References;
import net.minecraft.world.Container;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.level.storage.TagValueInput;
import zone.moddev.mc.ironagefurniture.api.tile.ShieldChairBlockEntity;
import zone.moddev.mc.ironagefurniture.api.tile.CandleSconceBlockEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;

/** Compare a disposable loaded world with an independently recorded source manifest. */
final class SavedWorldProbe {
    private final JsonObject manifest;
    private final int sourceVersion;
    private int checks;
    SavedWorldProbe() throws Exception {
        manifest = JsonParser.parseString(Files.readString(Path.of("saved-world-expectations.json"))).getAsJsonObject();
        sourceVersion = manifest.has("source_version") ? manifest.get("source_version").getAsInt() : 1343;
    }
    void loadChunks(MinecraftServer server) throws Exception {
        verifyPlayerInventories(server);
        for (JsonElement entry : manifest.getAsJsonArray("chunks")) {
            JsonObject chunk = entry.getAsJsonObject();
            ServerLevel level = level(server, chunk);
            int x = chunk.get("x").getAsInt(), z = chunk.get("z").getAsInt();
            level.setChunkForced(x, z, true);
            level.getChunk(x, z);
        }
        if (!Boolean.getBoolean("iaf.probe.savedWorldReload"))
            for (JsonElement entry : manifest.getAsJsonArray("blocks")) verifyBlock(server, entry.getAsJsonObject(), false);
    }
    int verify(MinecraftServer server) throws Exception {
        require(manifest.getAsJsonObject("unsupported").isEmpty(), "This run must not silently exclude source furniture");
        for (JsonElement entry : manifest.getAsJsonArray("blocks")) {
            verifyBlock(server, entry.getAsJsonObject(), true);
        }
        java.util.Set<String> matchedContainerSlots = new java.util.HashSet<>();
        for (JsonElement entry : manifest.getAsJsonArray("containers")) {
            JsonObject expected = entry.getAsJsonObject();
            require(level(server, expected).getBlockEntity(pos(expected)) instanceof Container, "Original container is missing: " + expected);
            for (JsonElement item : expected.getAsJsonArray("items")) {
                JsonObject record = item.getAsJsonObject();
                compareContainer(server, expected, record, matchedContainerSlots);
            }
        }
        for (JsonElement entry : manifest.getAsJsonArray("entities")) {
            JsonObject expected = entry.getAsJsonObject();
            ServerLevel level = level(server, expected);
            var entity = level.getEntity(UUID.fromString(expected.get("uuid").getAsString()));
            require(entity instanceof ItemEntity, "Original dropped stack is missing: " + expected);
            for (JsonElement value : expected.getAsJsonArray("items")) {
                JsonObject record = value.getAsJsonObject();
                String root = record.getAsJsonArray("path").get(0).getAsString();
                require(root.equals("Item") || root.equals("item"), "Unknown dropped-entity storage path");
                compareStack(level, record.getAsJsonObject("stack"), nestedStack(record, ((ItemEntity)entity).getItem(), 1));
            }
        }
        verifyPlayerInventories(server);
        require(checks > 0, "Empty source manifest");
        return checks;
    }
    private void compareContainer(MinecraftServer server, JsonObject source, JsonObject record, java.util.Set<String> matched) throws Exception {
        java.util.List<JsonObject> halves = source.has("chest_partner") ? java.util.List.of(source, source.getAsJsonObject("chest_partner")) : java.util.List.of(source);
        if (halves.size() == 2) for (int index = 0; index < 2; index++) {
            JsonObject half = halves.get(index); BlockState state = level(server, half).getBlockState(pos(half));
            require(level(server, half).getBlockEntity(pos(half)) instanceof net.minecraft.world.level.block.entity.ChestBlockEntity
                    && state.hasProperty(net.minecraft.world.level.block.state.properties.BlockStateProperties.CHEST_TYPE)
                    && state.getValue(net.minecraft.world.level.block.state.properties.BlockStateProperties.CHEST_TYPE) != net.minecraft.world.level.block.state.properties.ChestType.SINGLE
                    && pos(half).relative(net.minecraft.world.level.block.ChestBlock.getConnectedDirection(state)).equals(pos(halves.get(1-index))),
                    "A moved inventory must belong to the original connected double chest");
        }
        var path = record.getAsJsonArray("path");
        require(path.get(0).getAsString().equals("Items"), "Unknown source container path");
        ItemStack expected = expectedStack(level(server, source), record.getAsJsonObject("stack"), sourceVersion);
        for (JsonObject half : halves) {
            String key = half.get("dimension") + ":" + pos(half) + ":" + path;
            if (matched.contains(key)) continue;
            Container container = (Container)level(server, half).getBlockEntity(pos(half));
            ItemStack actual;
            try { actual = nestedStack(record, container.getItem(path.get(1).getAsJsonObject().get("slot").getAsInt())); }
            catch (AssertionError mismatch) { continue; }
            if (ItemStack.matches(expected, actual)) {
                require(matched.add(key), "A stored stack cannot satisfy two source expectations");
                return;
            }
        }
        throw new AssertionError("Saved container stack changed: " + source + " record=" + record);
    }
    private void verifyPlayerInventories(MinecraftServer server) throws Exception {
        for (JsonElement entry : manifest.getAsJsonArray("players")) {
            JsonObject expected = entry.getAsJsonObject();
            comparePlayer(server, UUID.fromString(expected.get("uuid").getAsString()), expected.getAsJsonArray("items"),
                    expected.has("source_version") ? expected.get("source_version").getAsInt() : sourceVersion);
        }
        if (!manifest.getAsJsonArray("embedded_player").isEmpty()) {
            require(manifest.has("embedded_player_uuid"), "The old singleplayer owner must be identified");
            UUID owner = UUID.fromString(manifest.get("embedded_player_uuid").getAsString());
            require(owner.equals(server.getWorldData().getSinglePlayerUUID()), "Native file migration retains the singleplayer owner");
            comparePlayer(server, owner, manifest.getAsJsonArray("embedded_player"), sourceVersion);
        }
    }
    private void verifyBlock(MinecraftServer server, JsonObject expected, boolean afterTicks) throws Exception {
        BlockState actual = level(server, expected).getBlockState(pos(expected));
        String id = BuiltInRegistries.BLOCK.getKey(actual.getBlock()).toString();
        String saved = expected.get("id").getAsString();
        boolean matches = id.equals(saved);
        if (afterTicks && expected.has("alternatives"))
            for (JsonElement alternative : expected.getAsJsonArray("alternatives")) matches |= id.equals(alternative.getAsString());
        if (afterTicks && saved.matches("ironagefurniture:light_metal_ironage_sconce_(floor|wall)_torch_iron(_unlit)?"))
            matches |= saved.replaceFirst("_unlit$", "").equals(id.replaceFirst("_unlit$", ""));
        require(matches, "Block identity changed: " + expected + " -> " + actual);
        for (Map.Entry<String, JsonElement> property : expected.getAsJsonObject("properties").entrySet()) {
            Property<?> key = actual.getBlock().getStateDefinition().getProperty(property.getKey());
            boolean enteredWater = afterTicks && expected.has("adjacent_source_water") && property.getKey().equals("waterlogged")
                    && key != null && propertyName(actual, key).equals("true");
            require(enteredWater || key != null && property.getValue().getAsString().equals(propertyName(actual, key)),
                    "Block property changed: " + expected + " -> " + actual);
        }
        if (expected.has("tile")) {
            JsonObject tile = expected.getAsJsonObject("tile");
            require(level(server, expected).getBlockEntity(pos(expected)) instanceof ShieldChairBlockEntity, "Stored shield chair exists: " + expected);
            ItemStack shield = new ItemStack(Items.SHIELD);
            if (tile.has("Shield")) shield = tile.getAsJsonObject("Shield").isEmpty() ? ItemStack.EMPTY
                    : expectedStack(level(server, expected), tile.getAsJsonObject("Shield"), sourceVersion);
            else if (tile.has("ShieldItem")) shield = tile.getAsJsonObject("ShieldItem").isEmpty() ? ItemStack.EMPTY
                    : expectedStack(level(server, expected), tile.getAsJsonObject("ShieldItem"), sourceVersion);
            if (truth(tile, "ShieldEmpty") || truth(tile, "Empty")) shield = ItemStack.EMPTY;
            ItemStack actualShield = ((ShieldChairBlockEntity)level(server, expected).getBlockEntity(pos(expected))).shield();
            require(ItemStack.matches(shield, actualShield), "Complete saved shield components survive: " + expected + " -> " + actualShield);
        }
        if (expected.has("candles")) {
            require(level(server, expected).getBlockEntity(pos(expected)) instanceof CandleSconceBlockEntity, "Migrated candle storage exists");
            var candles = ((CandleSconceBlockEntity)level(server, expected).getBlockEntity(pos(expected))).candles();
            require(candles.size() == expected.getAsJsonArray("candles").size(), "Migrated candle count survives");
            for (int slot = 0; slot < candles.size(); slot++)
                require(ItemStack.matches(expectedStack(level(server, expected), expected.getAsJsonArray("candles").get(slot).getAsJsonObject(),
                        SharedConstants.getCurrentVersion().dataVersion().version()), candles.get(slot)), "Exact migrated candle stack");
        }
    }
    private void compareStack(ServerLevel level, JsonObject source, ItemStack actual) throws Exception {
        compareStack(level, source, actual, sourceVersion);
    }
    private void compareStack(ServerLevel level, JsonObject source, ItemStack actual, int version) throws Exception {
        ItemStack expected = expectedStack(level, source, version);
        require(ItemStack.matches(actual, expected), "Saved stack changed: " + source + " -> " + actual);
    }
    private ItemStack expectedStack(ServerLevel level, JsonObject source, int version) throws Exception {
        JsonObject prepared = source.deepCopy();
        JsonObject tag = prepared.has("tag") ? prepared.getAsJsonObject("tag") : new JsonObject();
        if (source.get("id").getAsString().startsWith("ironagefurniture:chair_wood_ironage_shield_") && tag.has("IAFShieldChair")) {
            JsonObject oldChair = tag.getAsJsonObject("IAFShieldChair");
            boolean empty = oldChair.has("Empty") && (oldChair.get("Empty").isJsonPrimitive()
                    && (oldChair.get("Empty").getAsString().equals("true") || oldChair.get("Empty").getAsString().equals("1")));
            if (empty) tag.add("Shield", new JsonObject()); else if (oldChair.has("Shield")) tag.add("Shield", oldChair.get("Shield").deepCopy());
            oldChair.remove("Empty"); oldChair.remove("Shield"); if (oldChair.isEmpty()) tag.remove("IAFShieldChair");
            if (tag.isEmpty()) prepared.remove("tag");
        }
        CompoundTag old = TagParser.parseCompoundFully(prepared.toString());
        CompoundTag fixed = (CompoundTag)DataFixers.getDataFixer().update(References.ITEM_STACK,
                new Dynamic<>(NbtOps.INSTANCE, old), version, SharedConstants.getCurrentVersion().dataVersion().version()).getValue();
        ItemStack expected = ItemStack.CODEC.parse(level.registryAccess().createSerializationContext(NbtOps.INSTANCE), fixed).getOrThrow();
        if (source.get("id").getAsString().startsWith("ironagefurniture:chair_wood_ironage_shield_") && tag.has("Shield")) {
            ItemStack shield = tag.getAsJsonObject("Shield").isEmpty() ? ItemStack.EMPTY : expectedStack(level, tag.getAsJsonObject("Shield"), version);
            expected.set(DataComponents.CONTAINER, ItemContainerContents.fromItems(shield.isEmpty() ? java.util.List.of() : java.util.List.of(shield)));
            var custom = expected.get(DataComponents.CUSTOM_DATA);
            if (custom != null) {
                CompoundTag remaining = custom.copyTag(); remaining.remove("Shield");
                if (remaining.isEmpty()) expected.remove(DataComponents.CUSTOM_DATA);
                else expected.set(DataComponents.CUSTOM_DATA, net.minecraft.world.item.component.CustomData.of(remaining));
            }
        }
        return expected;
    }
    private ItemStack nestedStack(JsonObject record, ItemStack stack) {
        return nestedStack(record, stack, 2);
    }
    private ItemStack nestedStack(JsonObject record, ItemStack stack, int first) {
        var path = record.getAsJsonArray("path");
        for (int index = first; index < path.size(); index += 4) {
            boolean legacy = index + 3 < path.size() && path.get(index).getAsString().equals("tag")
                    && path.get(index + 1).getAsString().equals("BlockEntityTag") && path.get(index + 2).getAsString().equals("Items");
            boolean modern = index + 3 < path.size() && path.get(index).getAsString().equals("components")
                    && path.get(index + 1).getAsString().equals("minecraft:container") && path.get(index + 3).getAsString().equals("item");
            require(legacy || modern, "Unknown nested source inventory path");
            var contents = stack.get(DataComponents.CONTAINER);
            require(contents != null, "Nested inventory component was lost: " + record);
            stack = contents.getTemplateInSlot(path.get(index + (legacy ? 3 : 2)).getAsJsonObject().get("slot").getAsInt())
                    .map(net.minecraft.world.item.ItemStackTemplate::create).orElse(ItemStack.EMPTY);
        }
        return stack;
    }
    private void comparePlayer(MinecraftServer server, UUID uuid, com.google.gson.JsonArray records, int version) throws Exception {
        var storage = server.getPlayerList().getPlayerIo();
        CompoundTag data = storage.load(new NameAndId(uuid, "SavedWorldProbe")).orElseThrow(() -> new AssertionError("Original player data is missing: " + uuid));
        ServerPlayer player = new ServerPlayer(server, server.overworld(), new GameProfile(uuid, "SavedWorldProbe"), ClientInformation.createDefault());
        player.load(TagValueInput.create(ProblemReporter.DISCARDING, server.registryAccess(), data));
        for (JsonElement entry : records) {
            JsonObject record = entry.getAsJsonObject();
            var path = record.getAsJsonArray("path");
            String inventory = path.get(0).getAsString();
            int slot = path.get(1).getAsJsonObject().get("slot").getAsInt();
            ItemStack stack;
            if (inventory.equals("EnderItems")) stack = player.getEnderChestInventory().getItem(slot);
            else {
                require(inventory.equals("Inventory"), "Unknown player storage path: " + record);
                stack = switch (slot) {
                    case -106, 150 -> player.getItemBySlot(EquipmentSlot.OFFHAND);
                    case 100 -> player.getItemBySlot(EquipmentSlot.FEET); case 101 -> player.getItemBySlot(EquipmentSlot.LEGS);
                    case 102 -> player.getItemBySlot(EquipmentSlot.CHEST); case 103 -> player.getItemBySlot(EquipmentSlot.HEAD);
                    default -> player.getInventory().getItem(slot);
                };
            }
            compareStack(server.overworld(), record.getAsJsonObject("stack"), nestedStack(record, stack), version);
        }
        storage.save(player);
    }
    private void require(boolean value, String message) { if (!value) throw new AssertionError(message); checks++; }
    private static boolean truth(JsonObject value, String key) {
        return value.has(key) && (value.get(key).getAsString().equals("true") || value.get(key).getAsString().equals("1"));
    }
    @SuppressWarnings({"unchecked", "rawtypes"})
    private static String propertyName(BlockState state, Property key) { return key.getName(state.getValue(key)); }
    private static BlockPos pos(JsonObject record) { return new BlockPos(record.get("x").getAsInt(), record.get("y").getAsInt(), record.get("z").getAsInt()); }
    private static ServerLevel level(MinecraftServer server, JsonObject record) {
        return server.getLevel(switch (record.get("dimension").getAsInt()) {
            case -1 -> Level.NETHER; case 0 -> Level.OVERWORLD; case 1 -> Level.END;
            default -> throw new AssertionError("Unsupported fixture dimension");
        });
    }
}
