package zone.moddev.mc.ironagefurniture.fixture;

import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameType;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.registries.ForgeRegistries;
import zone.moddev.mc.ironagefurniture.BlockObjectHolder;
import zone.moddev.mc.ironagefurniture.Ironagefurniture;
import zone.moddev.mc.ironagefurniture.api.SconceMetalData;
import zone.moddev.mc.ironagefurniture.api.blocks.base.LightHolderSconce;
import zone.moddev.mc.ironagefurniture.api.blocks.base.FurnitureBlock;
import zone.moddev.mc.ironagefurniture.api.enumerations.SconceMetal;

/** Checks every saved metal, then crafts and mines frames using the installed provider. */
final class SconceMetalRuntimeProbe {
    private SconceMetalRuntimeProbe() { }
    static int run(ServerWorld world, FakePlayer player) {
        BlockPos pos = new BlockPos(768, 80, 192);
        world.setBlockState(pos.down(), Blocks.STONE.getDefaultState(), 2);
        int cases = 0, blocks = 0;
        for (Block block : ForgeRegistries.BLOCKS.getValues()) {
            if (!(block instanceof LightHolderSconce)) continue;
            blocks++;
            require(SconceMetalData.get(block.getDefaultState()) == SconceMetal.IRON, "Legacy default is not iron");
            require(!block.getDefaultState().get(FurnitureBlock.WATERLOGGED), "Default sconce is wet: " + block.getRegistryName());
            require(!block.hasTileEntity(block.getDefaultState()), "Metal added a tile entity");
            for (SconceMetal metal : SconceMetal.values()) for (Direction facing : Direction.Plane.HORIZONTAL) {
                BlockState state = block.getDefaultState().with(SconceMetalData.METAL, metal).with(FurnitureBlock.DIRECTION, facing);
                require(NBTUtil.readBlockState(NBTUtil.writeBlockState(state)) == state, "Saved metal state changed");
                BlockRayTraceResult hit = new BlockRayTraceResult(new Vec3d(pos), facing, pos, false);
                ItemStack picked = block.getPickBlock(state, hit, world, pos, player);
                require(!picked.isEmpty() && SconceMetalData.get(picked) == metal, "Pick block lost metal: " + state);
                List<ItemStack> drops = state.getDrops(new LootContext.Builder(world));
                require(drops.stream().anyMatch(stack -> Block.getBlockFromItem(stack.getItem()) instanceof LightHolderSconce),
                        "No frame drop: " + state);
                for (ItemStack stack : drops) if (Block.getBlockFromItem(stack.getItem()) instanceof LightHolderSconce)
                    require(SconceMetalData.get(stack) == metal, "Drop lost metal: " + state);
                cases++;
            }
        }
        require(blocks == (zone.moddev.mc.ironagefurniture.init.PhaseFourLighting.rockSalt(false) instanceof LightHolderSconce ? 68 : 66),
                "Metal variants changed sconce registration count: " + blocks);
        Item frame = BlockObjectHolder.light_metal_ironage_sconce_floor_empty_iron.asItem();
        NonNullList<ItemStack> creative = NonNullList.create(); frame.fillItemGroup(Ironagefurniture.IAF_GROUP, creative);
        long available = java.util.Arrays.stream(SconceMetal.values()).filter(SconceMetalData::available).count();
        if (Boolean.getBoolean("iaf.probe.liveBaseMetals")) {
            require(net.minecraftforge.fml.ModList.get().isLoaded("basemetals"), "Published Base Metals is absent");
            require(available == (Boolean.getBoolean("iaf.probe.baseMetalsDisabled") ? 2 : 23),
                    "Published Base Metals did not supply all twenty-one metal families: " + available);
        }
        require(creative.size() == available && creative.stream().anyMatch(stack -> SconceMetalData.get(stack) == SconceMetal.GOLD),
                "Unavailable Base Metals leaked into Creative or gold is missing");
        for (SconceMetal metal : SconceMetal.values()) {
            require(world.getRecipeManager().getRecipe(new ResourceLocation("ironagefurniture",
                    "light_metal_ironage_sconce_floor_empty_basemetals_" + metal.getName())).isPresent()
                            == (metal.isBaseMetal() && SconceMetalData.available(metal)),
                    "Base Metals recipe does not match tag/configuration availability: " + metal);
            for (boolean wall : new boolean[]{false, true}) for (Direction facing : Direction.Plane.HORIZONTAL) {
                Block block = wall ? BlockObjectHolder.light_metal_ironage_sconce_wall_empty_iron
                        : BlockObjectHolder.light_metal_ironage_sconce_floor_empty_iron;
                world.setBlockState(pos.offset(facing.getOpposite()), Blocks.STONE.getDefaultState(), 2);
                BlockState state = block.getDefaultState().with(FurnitureBlock.DIRECTION, facing).with(SconceMetalData.METAL, metal);
                world.setBlockState(pos, state, 2);
                player.interactionManager.setGameType(GameType.SURVIVAL);
                player.setHeldItem(Hand.MAIN_HAND, new ItemStack(Items.IRON_PICKAXE));
                player.setPosition(pos.getX(), pos.getY(), pos.getZ());
                BlockState iron = state.with(SconceMetalData.METAL, SconceMetal.IRON);
                require(Math.abs(state.getBlockHardness(world, pos)
                        - metal.hardness(iron.getBlockHardness(world, pos))) < 0.0001F,
                        "Mining hardness ignores the metal: " + metal);
                require(Math.abs(block.getExplosionResistance(state, world, pos, null, null)
                        - metal.resistance(block.getExplosionResistance(iron, world, pos, null, null))) < 0.0001F,
                        "Blast resistance ignores the metal: " + metal);
                if (metal == SconceMetal.ADAMANTINE || metal == SconceMetal.STARSTEEL)
                    require(state.getPlayerRelativeBlockHardness(player, world, pos)
                            < iron.getPlayerRelativeBlockHardness(player, world, pos),
                            "A tougher metal mines as quickly as iron: " + metal);
                require(state.canHarvestBlock(world, pos, player), "Iron pick cannot mine frame");
                require(player.interactionManager.tryHarvestBlock(pos), "Real frame harvesting failed");
                List<ItemEntity> drops = world.getEntitiesWithinAABB(ItemEntity.class, new AxisAlignedBB(pos).grow(2), entity -> !entity.removed);
                require(drops.size() == 1 && drops.get(0).getItem().getItem() == frame
                        && SconceMetalData.get(drops.get(0).getItem()) == metal, "Real harvesting changed frame metal");
                drops.forEach(Entity::remove);
                if (metal == SconceMetal.GOLD || metal == SconceMetal.ADAMANTINE
                        || Boolean.getBoolean("iaf.probe.liveBaseMetals")) verifyContents(world, player, pos, state);
                cases++;
            }
            BlockPos saved = new BlockPos(800 + metal.ordinal() * 3, 80, 224);
            BlockState expected = BlockObjectHolder.light_metal_ironage_sconce_floor_empty_iron.getDefaultState()
                    .with(SconceMetalData.METAL, metal).with(FurnitureBlock.DIRECTION, Direction.EAST).with(FurnitureBlock.WATERLOGGED, true);
            if (!world.getBlockState(saved).isAir()) require(world.getBlockState(saved) == expected, "Reload changed metal frame");
            world.setBlockState(saved.down(), Blocks.STONE.getDefaultState(), 2);
            world.setBlockState(saved, expected, 2);
        }
        verifyRecipe(world);
        for (SconceMetal metal : SconceMetal.values()) {
            if (!metal.isBaseMetal() || !SconceMetalData.available(metal)) continue;
            net.minecraft.item.crafting.ShapedRecipe recipe = (net.minecraft.item.crafting.ShapedRecipe)world.getRecipeManager()
                    .getRecipe(new ResourceLocation("ironagefurniture", "light_metal_ironage_sconce_floor_empty_basemetals_" + metal.getName())).get();
            java.util.Collection<Item> nuggets = net.minecraft.tags.ItemTags.getCollection()
                    .getOrCreate(SconceMetalData.nuggets(metal)).getAllElements();
            for (Item nugget : nuggets) {
                CraftingInventory grid = grid();
                for (int slot : new int[]{0, 1, 2, 3, 6}) grid.setInventorySlotContents(slot, new ItemStack(nugget));
                require(recipe.matches(grid, world), "Published tagged nugget failed: " + metal + "/" + nugget.getRegistryName());
                ItemStack result = recipe.getCraftingResult(grid);
                require(result.getCount() == 4 && SconceMetalData.get(result) == metal, "Recipe lost its metal: " + metal);
                grid.setInventorySlotContents(8, new ItemStack(nugget));
                require(!recipe.matches(grid, world), "Metal recipe accepts extra ingredients: " + metal);
            }
        }
        if (net.minecraftforge.fml.ModList.get().isLoaded("basemetals")
                && net.minecraftforge.fml.ModList.get().getModContainerById("basemetals").get().getModInfo().getVersion().toString().equals("0.0.0")) {
            require(!SconceMetalData.available(SconceMetal.ANTIMONY), "Incomplete metal tags must hide their variant");
            require(available == (Boolean.getBoolean("iaf.probe.baseMetalsDisabled") ? 2 : 22),
                    "Synthetic tags/configuration did not control all twenty supported metal recipes");
            for (SconceMetal metal : SconceMetal.values()) if (metal.isBaseMetal() && SconceMetalData.available(metal)) {
                net.minecraft.item.crafting.ShapedRecipe recipe = (net.minecraft.item.crafting.ShapedRecipe)world.getRecipeManager()
                        .getRecipe(new ResourceLocation("ironagefurniture", "light_metal_ironage_sconce_floor_empty_basemetals_" + metal.getName())).get();
                CraftingInventory grid = grid();
                for (int slot : new int[]{0, 1, 2, 3, 6}) grid.setInventorySlotContents(slot, new ItemStack(Items.GOLD_NUGGET));
                require(recipe.matches(grid, world), "Synthetic tagged ingredient failed: " + metal);
                ItemStack result = recipe.getCraftingResult(grid);
                require(result.getCount() == 4 && SconceMetalData.get(result) == metal, "Tagged recipe lost metal: " + metal);
            }
        }
        return cases;
    }
    private static void verifyContents(ServerWorld world, FakePlayer player, BlockPos pos, BlockState empty) {
        Item[] lights = {Items.TORCH, Items.REDSTONE_TORCH,
                BlockObjectHolder.light_metal_ironage_block_floor_glow_clear.asItem(),
                BlockObjectHolder.light_metal_ironage_block_floor_red_clear.asItem(),
                BlockObjectHolder.light_metal_ironage_block_floor_lava_clear.asItem(),
                zone.moddev.mc.ironagefurniture.init.PhaseFourLighting.candle(false, true).asItem()};
        BlockRayTraceResult hit = new BlockRayTraceResult(new Vec3d(pos), Direction.UP, pos, false);
        for (Item light : lights) {
            // Draining/breaking the previous case can leave adjacent source
            // water. Give each interaction case a dry, supported test cell.
            for (Direction side : Direction.values()) world.setBlockState(pos.offset(side), Blocks.AIR.getDefaultState(), 18);
            world.setBlockState(pos.down(), Blocks.STONE.getDefaultState(), 18);
            world.setBlockState(pos.offset(empty.get(FurnitureBlock.DIRECTION).getOpposite()), Blocks.STONE.getDefaultState(), 18);
            world.setBlockState(pos, empty, 2); player.inventory.clear();
            player.setHeldItem(Hand.MAIN_HAND, new ItemStack(light, 8));
            require(empty.getBlock().onBlockActivated(empty, world, pos, player, Hand.MAIN_HAND, hit), "Cannot fill frame");
            sameMetal(empty, world.getBlockState(pos));
            BlockState filled = world.getBlockState(pos);
            require(!filled.get(FurnitureBlock.WATERLOGGED), "Fresh dry frame started wet: " + empty + " -> " + filled);
            require(((net.minecraft.block.IWaterLoggable)filled.getBlock()).receiveFluid(world, pos, filled,
                    Fluids.WATER.getStillFluidState(false)), "Cannot waterlog filled frame: " + filled + ", input=" + light.getRegistryName());
            filled = world.getBlockState(pos); sameMetal(empty, filled);
            require(filled.get(FurnitureBlock.WATERLOGGED), "Extinguishing lost water");
            ((net.minecraft.block.IWaterLoggable)filled.getBlock()).pickupFluid(world, pos, filled);
            sameMetal(empty, world.getBlockState(pos));
            player.setHeldItem(Hand.MAIN_HAND, new ItemStack(Items.FLINT_AND_STEEL));
            filled = world.getBlockState(pos); filled.getBlock().onBlockActivated(filled, world, pos, player, Hand.MAIN_HAND, hit);
            sameMetal(empty, world.getBlockState(pos));
            player.setHeldItem(Hand.MAIN_HAND, ItemStack.EMPTY);
            filled = world.getBlockState(pos); filled.getBlock().onBlockActivated(filled, world, pos, player, Hand.MAIN_HAND, hit);
            sameMetal(empty, world.getBlockState(pos));
            world.getEntitiesWithinAABB(ItemEntity.class, new AxisAlignedBB(pos).grow(2)).forEach(Entity::remove);
        }
    }
    private static void verifyRecipe(ServerWorld world) {
        IRecipe<?> found = world.getRecipeManager().getRecipe(new ResourceLocation("ironagefurniture",
                "light_metal_ironage_sconce_floor_empty_gold")).orElseThrow(() -> new IllegalStateException("Missing gold recipe"));
        require(found instanceof net.minecraft.item.crafting.ShapedRecipe, "Wrong gold recipe type");
        net.minecraft.item.crafting.ShapedRecipe recipe = (net.minecraft.item.crafting.ShapedRecipe)found;
        CraftingInventory grid = grid();
        for (int slot : new int[]{0, 1, 2, 3, 6}) grid.setInventorySlotContents(slot, new ItemStack(Items.GOLD_NUGGET));
        require(recipe.matches(grid, world), "Gold recipe does not match");
        ItemStack result = recipe.getCraftingResult(grid);
        require(result.getCount() == 4 && SconceMetalData.get(result) == SconceMetal.GOLD, "Crafting lost gold NBT");
        grid.setInventorySlotContents(8, new ItemStack(Items.GOLD_NUGGET));
        require(!recipe.matches(grid, world), "Recipe accepts extra ingredients");
    }
    private static CraftingInventory grid() {
        return new CraftingInventory(new Container(null, 0) {
            @Override public boolean canInteractWith(PlayerEntity player) { return false; }
        }, 3, 3);
    }
    private static void sameMetal(BlockState old, BlockState current) {
        require(current.has(SconceMetalData.METAL) && SconceMetalData.get(old) == SconceMetalData.get(current),
                "Lighting change lost metal: " + old + " -> " + current);
    }
    private static void require(boolean passed, String message) { if (!passed) throw new IllegalStateException(message); }
}
