package zone.moddev.mc.ironagefurniture.fixture;

import com.mojang.authlib.GameProfile;
import java.io.File;
import java.io.FileInputStream;
import java.util.List;
import java.util.UUID;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.AABB;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.FakePlayerFactory;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fmlserverevents.FMLServerAboutToStartEvent;
import net.minecraftforge.registries.ForgeRegistries;
import zone.moddev.mc.ironagefurniture.IronAgeFurnitureConfiguration;

/** Builds and reloads an actual CFM world, including undecoded player stacks. */
final class CfmRuntimeProbe {
    private static final String[] WOODS = {"oak", "spruce", "birch", "jungle", "acacia", "dark_oak", "crimson", "warped"};
    private static final BlockPos CHEST = new BlockPos(128, 80, 160);
    private static final BlockPos DROPS = new BlockPos(144, 80, 160);
    private static final GameProfile PLAYER = new GameProfile(
            UUID.fromString("363fd2af-69a5-42be-b6cd-1cdfbaad7bce"), "CfmUpgradeProbe");

    static void beforeStart(FMLServerAboutToStartEvent event) {
        String mode = System.getProperty("iaf.probe.cfmMode", "none");
        if (!"none".equals(mode)) IronAgeFurnitureConfiguration.CLIENT.FORCE_CFM_CHAIR_CONVERSION.set("converted".equals(mode));
    }

    static int run(MinecraftServer server, ServerLevel world) throws Exception {
        String mode = System.getProperty("iaf.probe.cfmMode", "none");
        boolean installed = ModList.get().isLoaded("cfm");
        boolean converted = "converted".equals(mode) || "recovered".equals(mode);
        verifyRecipes(server, world, installed);
        if ("none".equals(mode)) return installed ? WOODS.length : 0;
        if ("legacy112".equals(mode) || "legacy110".equals(mode)) {
            require(!installed, "Legacy recovery must run without CFM");
            verifyLegacy(world, "legacy110".equals(mode));
            org.apache.logging.log4j.LogManager.getLogger().info("IAF CFM WORLD PROBE PASSED: {}", mode);
            return 8;
        }
        require(installed != "recovered".equals(mode), "Wrong CFM installation for " + mode);
        if ("seed".equals(mode)) seed(world);
        else verifySaved(world, converted);
        verifyPlayer(world, "seed".equals(mode), converted);
        org.apache.logging.log4j.LogManager.getLogger().info("IAF CFM WORLD PROBE PASSED: {}", mode);
        return WOODS.length * 11;
    }

    private static void verifyLegacy(ServerLevel world, boolean from110) {
        // Saved fixtures use the chair IDs from CFM 4.1.2 and 6.3.2 respectively.
        for (int wood = 0; wood < 6; wood++) {
            BlockState state = world.getBlockState(from110 ? new BlockPos(wood, 64, 0) : new BlockPos(32 + wood, 100, 32));
            require(state.getBlock() == block(target(WOODS[wood])), "Legacy CFM chair lost: " + WOODS[wood] + " " + state);
            require(state.getValue(BlockStateProperties.HORIZONTAL_FACING) == Direction.from2DDataValue(wood % 4),
                    "Pre-flattening chair facing changed");
            require(!state.getValue(BlockStateProperties.WATERLOGGED), "Legacy chair became waterlogged");
        }
        ChestBlockEntity chest = (ChestBlockEntity)world.getBlockEntity(from110 ? new BlockPos(0, 64, 2) : new BlockPos(40, 100, 32));
        require(chest != null, "Legacy CFM chest lost");
        ItemStack direct = chest.getItem(0);
        require(direct.getItem() == item(target("oak")) && direct.getCount() == (from110 ? 2 : 3), "Legacy CFM stored items lost");
        if (from110) {
            require("Legacy Oak".equals(direct.getHoverName().getString()), "Legacy chair name changed");
            world.getChunk(new BlockPos(2, 80, 2));
            List<ItemEntity> drops = world.getEntitiesOfClass(ItemEntity.class, new AABB(1, 78, 1, 4, 82, 4));
            require(drops.size() == 1 && drops.get(0).getItem().getItem() == item(target("dark_oak"))
                    && drops.get(0).getItem().getCount() == 1, "Legacy dropped chair lost");
            return;
        }
        CompoundTag nested = chest.getItem(1).getOrCreateTag().getCompound("BlockEntityTag")
                .getList("Items", 10).getCompound(0);
        require(target("oak").equals(nested.getString("id")) && nested.getByte("Count") == 2,
                "Legacy nested CFM items lost");
    }

    @SuppressWarnings("unchecked")
    private static void verifyRecipes(MinecraftServer server, ServerLevel world, boolean installed) {
        for (String wood : WOODS) {
            ResourceLocation id = new ResourceLocation("ironagefurniture:cfm_" + wood + "_chair_conversion");
            Recipe<?> recipe = server.getRecipeManager().byKey(id).orElse(null);
            require((recipe != null) == installed, "Wrong conditional recipe presence " + id);
            if (recipe == null) continue;
            Recipe<CraftingContainer> crafting = (Recipe<CraftingContainer>)recipe;
            CraftingContainer grid = new CraftingContainer(new AbstractContainerMenu(null, 0) {
                @Override public boolean stillValid(Player player) { return false; }
            }, 3, 3);
            grid.setItem(4, new ItemStack(item("cfm:" + wood + "_chair")));
            require(crafting.matches(grid, world), "CFM recipe does not match " + wood);
            require(crafting.assemble(grid).getItem() == item(target(wood)), "Wrong CFM recipe output " + wood);
            require(crafting.assemble(grid).getCount() == 1, "Conversion multiplied chairs");
            grid.setItem(0, new ItemStack(Items.STICK));
            require(!crafting.matches(grid, world), "Extra ingredient accepted");
            require(server.getAdvancements().getAdvancement(new ResourceLocation(
                    "ironagefurniture:recipes/furniture/cfm_" + wood + "_chair_conversion")) != null,
                    "Missing conditional CFM advancement");
        }
    }

    private static void seed(ServerLevel world) {
        for (int wood = 0; wood < WOODS.length; wood++) {
            Block chair = block("cfm:" + WOODS[wood] + "_chair");
            int facing = 0;
            for (Direction direction : Direction.Plane.HORIZONTAL) {
                BlockPos pos = position(wood, facing++);
                world.setBlock(pos.below(), Blocks.STONE.defaultBlockState(), 2);
                world.setBlock(pos, chair.defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, direction), 2);
            }
        }
        world.setBlock(CHEST.below(), Blocks.STONE.defaultBlockState(), 2);
        world.setBlock(CHEST, Blocks.CHEST.defaultBlockState(), 2);
        ChestBlockEntity chest = (ChestBlockEntity)world.getBlockEntity(CHEST);
        world.getChunk(DROPS);
        for (int wood = 0; wood < WOODS.length; wood++) {
            chest.setItem(wood, stack("cfm:" + WOODS[wood] + "_chair"));
            ItemEntity dropped = new ItemEntity(world, DROPS.getX() + wood, DROPS.getY(), DROPS.getZ(),
                    stack("cfm:" + WOODS[wood] + "_chair"));
            dropped.setNoGravity(true);
            dropped.setNeverPickUp();
            require(world.addFreshEntity(dropped), "Fixture item entity could not spawn");
        }
        chest.setItem(WOODS.length, nested(false));
        chest.setChanged();
    }

    private static void verifySaved(ServerLevel world, boolean converted) {
        for (int wood = 0; wood < WOODS.length; wood++) {
            int facing = 0;
            for (Direction direction : Direction.Plane.HORIZONTAL) {
                BlockState state = world.getBlockState(position(wood, facing++));
                require(state.getBlock() == block(converted ? target(WOODS[wood]) : "cfm:" + WOODS[wood] + "_chair"),
                        "Saved CFM chair lost or not converted: " + WOODS[wood] + " " + state);
                require(state.getValue(BlockStateProperties.HORIZONTAL_FACING) == direction, "Chair facing changed");
                if (converted) require(!state.getValue(BlockStateProperties.WATERLOGGED), "Dry chair became wet");
            }
        }
        ChestBlockEntity chest = (ChestBlockEntity)world.getBlockEntity(CHEST);
        require(chest != null, "Saved chest lost");
        for (int wood = 0; wood < WOODS.length; wood++) sameStack(chest.getItem(wood),
                converted ? target(WOODS[wood]) : "cfm:" + WOODS[wood] + "_chair");
        require(chest.getItem(WOODS.length).save(new CompoundTag()).equals(nested(converted).save(new CompoundTag())),
                "Nested chest stacks changed or were not converted");
        world.getChunk(DROPS);
        List<ItemEntity> dropped = world.getEntitiesOfClass(ItemEntity.class, new AABB(DROPS).inflate(8));
        require(dropped.size() == WOODS.length, "Saved item entities lost: " + dropped.size());
        for (String wood : WOODS) require(dropped.stream().anyMatch(entity -> entity.getItem().save(new CompoundTag())
                .equals(stack(converted ? target(wood) : "cfm:" + wood + "_chair").save(new CompoundTag()))),
                "Dropped chair data lost " + wood);
    }

    private static void verifyPlayer(ServerLevel world, boolean seed, boolean converted) throws Exception {
        FakePlayer player = FakePlayerFactory.get(world, PLAYER);
        player.getInventory().clearContent(); player.getEnderChestInventory().clearContent();
        if (seed) {
            for (int wood = 0; wood < WOODS.length; wood++) {
                player.getInventory().setItem(wood, stack("cfm:" + WOODS[wood] + "_chair"));
                player.getEnderChestInventory().setItem(wood, stack("cfm:" + WOODS[wood] + "_chair"));
            }
            player.getInventory().setItem(WOODS.length, nested(false));
            ProbePlayerStorage.storage(world).save(player);
            return;
        }
        require(ProbePlayerStorage.storage(world).load(player) != null, "Player fixture did not reload");
        for (int wood = 0; wood < WOODS.length; wood++) {
            String id = converted ? target(WOODS[wood]) : "cfm:" + WOODS[wood] + "_chair";
            sameStack(player.getInventory().getItem(wood), id);
            sameStack(player.getEnderChestInventory().getItem(wood), id);
        }
        require(player.getInventory().getItem(WOODS.length).save(new CompoundTag()).equals(nested(converted).save(new CompoundTag())),
                "Nested player inventory failed before ItemStack decoding");
        ProbePlayerStorage.storage(world).save(player);
        File saved = new File(ProbePlayerStorage.folder(world), "playerdata/" + PLAYER.getId() + ".dat");
        try (FileInputStream input = new FileInputStream(saved)) {
            CompoundTag raw = NbtIo.readCompressed(input);
            String expected = converted ? "ironagefurniture:" : "cfm:";
            require(raw.getList("Inventory", 10).getCompound(0).getString("id").startsWith(expected),
                    "Conversion did not persist in player file");
        }
    }

    private static ItemStack nested(boolean converted) {
        ItemStack box = new ItemStack(Items.SHULKER_BOX);
        ListTag contents = new ListTag();
        for (int wood = 0; wood < WOODS.length; wood++) {
            CompoundTag saved = stack(converted ? target(WOODS[wood]) : "cfm:" + WOODS[wood] + "_chair").save(new CompoundTag());
            saved.putByte("Slot", (byte)wood); contents.add(saved);
        }
        CompoundTag tile = new CompoundTag(); tile.put("Items", contents);
        box.getOrCreateTag().put("BlockEntityTag", tile); return box;
    }
    private static ItemStack stack(String id) {
        ItemStack stack = new ItemStack(item(id), 7);
        stack.setHoverName(new TextComponent("Inherited Chair"));
        stack.enchant(net.minecraft.world.item.enchantment.Enchantments.UNBREAKING, 3);
        stack.getOrCreateTag().putString("ExtraData", "keep"); return stack;
    }
    private static void sameStack(ItemStack actual, String id) {
        require(actual.save(new CompoundTag()).equals(stack(id).save(new CompoundTag())), "Chair item data changed: " + actual);
    }
    private static BlockPos position(int wood, int facing) { return new BlockPos(128 + wood * 8, 80, 128 + facing * 4); }
    private static String target(String wood) { return "ironagefurniture:chair_wood_ironage_classic_" + wood; }
    private static Block block(String id) { return ForgeRegistries.BLOCKS.getValue(new ResourceLocation(id)); }
    private static Item item(String id) {
        Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(id));
        require(item != null && item != Items.AIR, "Missing fixture item " + id); return item;
    }
    private static void require(boolean okay, String message) { if (!okay) throw new IllegalStateException(message); }
}
