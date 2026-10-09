package zone.moddev.mc.ironagefurniture.fixture;

import com.mojang.authlib.GameProfile;
import java.io.File;
import java.io.FileInputStream;
import java.util.List;
import java.util.UUID;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.ListNBT;
import net.minecraft.server.MinecraftServer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.ChestTileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.FakePlayerFactory;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.event.server.FMLServerAboutToStartEvent;
import net.minecraftforge.registries.ForgeRegistries;
import zone.moddev.mc.ironagefurniture.IronAgeFurnitureConfiguration;

/** Builds and reloads an actual CFM world, including undecoded player stacks. */
final class CfmRuntimeProbe {
    private static final String[] WOODS = {"oak", "spruce", "birch", "jungle", "acacia", "dark_oak"};
    private static final BlockPos CHEST = new BlockPos(128, 80, 160);
    private static final BlockPos DROPS = new BlockPos(144, 80, 160);
    private static final GameProfile PLAYER = new GameProfile(
            UUID.fromString("363fd2af-69a5-42be-b6cd-1cdfbaad7bce"), "CfmUpgradeProbe");

    static void beforeStart(FMLServerAboutToStartEvent event) {
        String mode = System.getProperty("iaf.probe.cfmMode", "none");
        if (!"none".equals(mode)) IronAgeFurnitureConfiguration.CLIENT.FORCE_CFM_CHAIR_CONVERSION.set("converted".equals(mode));
    }

    static int run(MinecraftServer server, ServerWorld world) throws Exception {
        String mode = System.getProperty("iaf.probe.cfmMode", "none");
        boolean installed = ModList.get().isLoaded("cfm");
        boolean converted = "converted".equals(mode) || "recovered".equals(mode);
        verifyRecipes(server, world, installed);
        if ("none".equals(mode)) return installed ? 6 : 0;
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
        return 24 + 12 + 6 + 18;
    }

    private static void verifyLegacy(ServerWorld world, boolean from110) {
        // Saved fixtures use the chair IDs from CFM 4.1.2 and 6.3.2 respectively.
        for (int wood = 0; wood < WOODS.length; wood++) {
            BlockState state = world.getBlockState(from110 ? new BlockPos(wood, 64, 0) : new BlockPos(32 + wood, 100, 32));
            require(state.getBlock() == block(target(WOODS[wood])), "Legacy CFM chair lost: " + WOODS[wood] + " " + state);
            require(state.get(BlockStateProperties.HORIZONTAL_FACING) == Direction.byHorizontalIndex(wood % 4),
                    "Pre-flattening chair facing changed");
            require(!state.get(BlockStateProperties.WATERLOGGED), "Legacy chair became waterlogged");
        }
        ChestTileEntity chest = (ChestTileEntity)world.getTileEntity(from110 ? new BlockPos(0, 64, 2) : new BlockPos(40, 100, 32));
        require(chest != null, "Legacy CFM chest lost");
        ItemStack direct = chest.getStackInSlot(0);
        require(direct.getItem() == item(target("oak")) && direct.getCount() == (from110 ? 2 : 3), "Legacy CFM stored items lost");
        if (from110) {
            require("Legacy Oak".equals(direct.getDisplayName().getString()), "Legacy chair name changed");
            world.getChunk(new BlockPos(2, 80, 2));
            List<ItemEntity> drops = world.getEntitiesWithinAABB(ItemEntity.class, new AxisAlignedBB(1, 78, 1, 4, 82, 4));
            require(drops.size() == 1 && drops.get(0).getItem().getItem() == item(target("dark_oak"))
                    && drops.get(0).getItem().getCount() == 1, "Legacy dropped chair lost");
            return;
        }
        CompoundNBT nested = chest.getStackInSlot(1).getOrCreateTag().getCompound("BlockEntityTag")
                .getList("Items", 10).getCompound(0);
        require(target("oak").equals(nested.getString("id")) && nested.getByte("Count") == 2,
                "Legacy nested CFM items lost");
    }

    @SuppressWarnings("unchecked")
    private static void verifyRecipes(MinecraftServer server, ServerWorld world, boolean installed) {
        for (String wood : WOODS) {
            ResourceLocation id = new ResourceLocation("ironagefurniture:cfm_" + wood + "_chair_conversion");
            IRecipe<?> recipe = server.getRecipeManager().getRecipe(id).orElse(null);
            require((recipe != null) == installed, "Wrong conditional recipe presence " + id);
            if (recipe == null) continue;
            IRecipe<CraftingInventory> crafting = (IRecipe<CraftingInventory>)recipe;
            CraftingInventory grid = new CraftingInventory(new Container(null, 0) {
                @Override public boolean canInteractWith(PlayerEntity player) { return false; }
            }, 3, 3);
            grid.setInventorySlotContents(4, new ItemStack(item("cfm:" + wood + "_chair")));
            require(crafting.matches(grid, world), "CFM recipe does not match " + wood);
            require(crafting.getCraftingResult(grid).getItem() == item(target(wood)), "Wrong CFM recipe output " + wood);
            require(crafting.getCraftingResult(grid).getCount() == 1, "Conversion multiplied chairs");
            grid.setInventorySlotContents(0, new ItemStack(Items.STICK));
            require(!crafting.matches(grid, world), "Extra ingredient accepted");
            require(server.getAdvancementManager().getAdvancement(new ResourceLocation(
                    "ironagefurniture:recipes/furniture/cfm_" + wood + "_chair_conversion")) != null,
                    "Missing conditional CFM advancement");
        }
    }

    private static void seed(ServerWorld world) {
        for (int wood = 0; wood < WOODS.length; wood++) {
            Block chair = block("cfm:" + WOODS[wood] + "_chair");
            int facing = 0;
            for (Direction direction : Direction.Plane.HORIZONTAL) {
                BlockPos pos = position(wood, facing++);
                world.setBlockState(pos.down(), Blocks.STONE.getDefaultState());
                world.setBlockState(pos, chair.getDefaultState().with(BlockStateProperties.HORIZONTAL_FACING, direction));
            }
        }
        world.setBlockState(CHEST.down(), Blocks.STONE.getDefaultState());
        world.setBlockState(CHEST, Blocks.CHEST.getDefaultState());
        ChestTileEntity chest = (ChestTileEntity)world.getTileEntity(CHEST);
        world.getChunk(DROPS);
        for (int wood = 0; wood < WOODS.length; wood++) {
            chest.setInventorySlotContents(wood, stack("cfm:" + WOODS[wood] + "_chair"));
            require(world.addEntity(new ItemEntity(world, DROPS.getX() + wood, DROPS.getY(), DROPS.getZ(),
                    stack("cfm:" + WOODS[wood] + "_chair"))), "Fixture item entity could not spawn");
        }
        chest.setInventorySlotContents(6, nested(false));
        chest.markDirty();
    }

    private static void verifySaved(ServerWorld world, boolean converted) {
        for (int wood = 0; wood < WOODS.length; wood++) {
            int facing = 0;
            for (Direction direction : Direction.Plane.HORIZONTAL) {
                BlockState state = world.getBlockState(position(wood, facing++));
                require(state.getBlock() == block(converted ? target(WOODS[wood]) : "cfm:" + WOODS[wood] + "_chair"),
                        "Saved CFM chair lost or not converted: " + WOODS[wood] + " " + state);
                require(state.get(BlockStateProperties.HORIZONTAL_FACING) == direction, "Chair facing changed");
                if (converted) require(!state.get(BlockStateProperties.WATERLOGGED), "Dry chair became wet");
            }
        }
        ChestTileEntity chest = (ChestTileEntity)world.getTileEntity(CHEST);
        require(chest != null, "Saved chest lost");
        for (int wood = 0; wood < WOODS.length; wood++) sameStack(chest.getStackInSlot(wood),
                converted ? target(WOODS[wood]) : "cfm:" + WOODS[wood] + "_chair");
        require(chest.getStackInSlot(6).write(new CompoundNBT()).equals(nested(converted).write(new CompoundNBT())),
                "Nested chest stacks changed or were not converted");
        world.getChunk(DROPS);
        List<ItemEntity> dropped = world.getEntitiesWithinAABB(ItemEntity.class, new AxisAlignedBB(DROPS).grow(8));
        require(dropped.size() == 6, "Saved item entities lost: " + dropped.size());
        for (String wood : WOODS) require(dropped.stream().anyMatch(entity -> entity.getItem().write(new CompoundNBT())
                .equals(stack(converted ? target(wood) : "cfm:" + wood + "_chair").write(new CompoundNBT()))),
                "Dropped chair data lost " + wood);
    }

    private static void verifyPlayer(ServerWorld world, boolean seed, boolean converted) throws Exception {
        FakePlayer player = FakePlayerFactory.get(world, PLAYER);
        player.inventory.clear(); player.getInventoryEnderChest().clear();
        if (seed) {
            for (int wood = 0; wood < WOODS.length; wood++) {
                player.inventory.setInventorySlotContents(wood, stack("cfm:" + WOODS[wood] + "_chair"));
                player.getInventoryEnderChest().setInventorySlotContents(wood, stack("cfm:" + WOODS[wood] + "_chair"));
            }
            player.inventory.setInventorySlotContents(6, nested(false));
            world.getSaveHandler().writePlayerData(player);
            return;
        }
        require(world.getSaveHandler().readPlayerData(player) != null, "Player fixture did not reload");
        for (int wood = 0; wood < WOODS.length; wood++) {
            String id = converted ? target(WOODS[wood]) : "cfm:" + WOODS[wood] + "_chair";
            sameStack(player.inventory.getStackInSlot(wood), id);
            sameStack(player.getInventoryEnderChest().getStackInSlot(wood), id);
        }
        require(player.inventory.getStackInSlot(6).write(new CompoundNBT()).equals(nested(converted).write(new CompoundNBT())),
                "Nested player inventory failed before ItemStack decoding");
        world.getSaveHandler().writePlayerData(player);
        File saved = new File(world.getSaveHandler().getWorldDirectory(), "playerdata/" + PLAYER.getId() + ".dat");
        try (FileInputStream input = new FileInputStream(saved)) {
            CompoundNBT raw = CompressedStreamTools.readCompressed(input);
            String expected = converted ? "ironagefurniture:" : "cfm:";
            require(raw.getList("Inventory", 10).getCompound(0).getString("id").startsWith(expected),
                    "Conversion did not persist in player file");
        }
    }

    private static ItemStack nested(boolean converted) {
        ItemStack box = new ItemStack(Items.SHULKER_BOX);
        ListNBT contents = new ListNBT();
        for (int wood = 0; wood < WOODS.length; wood++) {
            CompoundNBT saved = stack(converted ? target(WOODS[wood]) : "cfm:" + WOODS[wood] + "_chair").write(new CompoundNBT());
            saved.putByte("Slot", (byte)wood); contents.add(saved);
        }
        CompoundNBT tile = new CompoundNBT(); tile.put("Items", contents);
        box.getOrCreateTag().put("BlockEntityTag", tile); return box;
    }
    private static ItemStack stack(String id) {
        ItemStack stack = new ItemStack(item(id), 7);
        stack.setDisplayName(new StringTextComponent("Inherited Chair"));
        stack.addEnchantment(net.minecraft.enchantment.Enchantments.UNBREAKING, 3);
        stack.getOrCreateTag().putString("ExtraData", "keep"); return stack;
    }
    private static void sameStack(ItemStack actual, String id) {
        require(actual.write(new CompoundNBT()).equals(stack(id).write(new CompoundNBT())), "Chair item data changed: " + actual);
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
