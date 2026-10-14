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
        world.setBlock(pos.below(), Blocks.STONE.defaultBlockState(), 2);
        int cases = 0, blocks = 0;
        for (Block block : ForgeRegistries.BLOCKS.getValues()) {
            if (!(block instanceof LightHolderSconce)) continue;
            blocks++;
            require(SconceMetalData.get(block.defaultBlockState()) == SconceMetal.IRON, "Legacy default is not iron");
            require(!block.defaultBlockState().getValue(FurnitureBlock.WATERLOGGED), "Default sconce is wet: " + block.getRegistryName());
            require(!block.hasTileEntity(block.defaultBlockState()), "Metal added a tile entity");
            for (SconceMetal metal : SconceMetal.values()) for (Direction facing : Direction.Plane.HORIZONTAL) {
                BlockState state = block.defaultBlockState().setValue(SconceMetalData.METAL, metal).setValue(FurnitureBlock.DIRECTION, facing);
                require(NBTUtil.readBlockState(NBTUtil.writeBlockState(state)) == state, "Saved metal state changed");
                BlockRayTraceResult hit = new BlockRayTraceResult(new Vec3d(pos), facing, pos, false);
                ItemStack picked = block.getPickBlock(state, hit, world, pos, player);
                require(!picked.isEmpty() && SconceMetalData.get(picked) == metal, "Pick block lost metal: " + state);
                List<ItemStack> drops = state.getDrops(new LootContext.Builder(world));
                require(drops.stream().anyMatch(stack -> Block.byItem(stack.getItem()) instanceof LightHolderSconce),
                        "No frame drop: " + state);
                for (ItemStack stack : drops) if (Block.byItem(stack.getItem()) instanceof LightHolderSconce)
                    require(SconceMetalData.get(stack) == metal, "Drop lost metal: " + state);
                cases++;
            }
        }
        require(blocks == (zone.moddev.mc.ironagefurniture.init.PhaseFourLighting.rockSalt(false) instanceof LightHolderSconce ? 68 : 66),
                "Metal variants changed sconce registration count: " + blocks);
        Item frame = BlockObjectHolder.light_metal_ironage_sconce_floor_empty_iron.asItem();
        NonNullList<ItemStack> creative = NonNullList.create();
        for (SconceMetal metal : SconceMetal.values())
            SconceMetalData.create(((net.minecraft.item.BlockItem)frame).getBlock(), metal).getItem()
                    .fillItemCategory(Ironagefurniture.IAF_LIGHTS_GROUP, creative);
        long available = java.util.Arrays.stream(SconceMetal.values()).filter(SconceMetalData::available).count();
        if (Boolean.getBoolean("iaf.probe.liveBaseMetals")) {
            require(net.minecraftforge.fml.ModList.get().isLoaded("basemetals"), "Published Base Metals is absent");
            require(available == (Boolean.getBoolean("iaf.probe.baseMetalsDisabled") ? 2 : 23),
                    "Published Base Metals did not supply all twenty-one metal families: " + available);
        }
        require(creative.size() == available && creative.stream().anyMatch(stack -> SconceMetalData.get(stack) == SconceMetal.GOLD),
                "Unavailable Base Metals leaked into Creative or gold is missing");
        for (SconceMetal metal : SconceMetal.values()) {
            require(world.getRecipeManager().byKey(new ResourceLocation("ironagefurniture",
                    "light_metal_ironage_sconce_floor_empty_basemetals_" + metal.getSerializedName())).isPresent()
                            == (metal.isBaseMetal() && SconceMetalData.available(metal)),
                    "Base Metals recipe does not match tag/configuration availability: " + metal);
            for (boolean wall : new boolean[]{false, true}) for (Direction facing : Direction.Plane.HORIZONTAL) {
                world.getEntitiesOfClass(ItemEntity.class, new AxisAlignedBB(pos).inflate(3)).forEach(entity -> entity.remove());
                Block block = wall ? BlockObjectHolder.light_metal_ironage_sconce_wall_empty_iron
                        : BlockObjectHolder.light_metal_ironage_sconce_floor_empty_iron;
                world.setBlock(pos.relative(facing.getOpposite()), Blocks.STONE.defaultBlockState(), 2);
                BlockState state = block.defaultBlockState().setValue(FurnitureBlock.DIRECTION, facing).setValue(SconceMetalData.METAL, metal);
                world.setBlock(pos, state, 2);
                player.gameMode.setGameModeForPlayer(GameType.SURVIVAL);
                player.setItemInHand(Hand.MAIN_HAND, new ItemStack(Items.IRON_PICKAXE));
                player.setPos(pos.getX(), pos.getY(), pos.getZ());
                BlockState iron = state.setValue(SconceMetalData.METAL, SconceMetal.IRON);
                require(Math.abs(state.getDestroySpeed(world, pos)
                        - metal.hardness(iron.getDestroySpeed(world, pos))) < 0.0001F,
                        "Mining hardness ignores the metal: " + metal);
                require(Math.abs(block.getExplosionResistance(state, world, pos, null, null)
                        - metal.resistance(block.getExplosionResistance(iron, world, pos, null, null))) < 0.0001F,
                        "Blast resistance ignores the metal: " + metal);
                if (metal == SconceMetal.ADAMANTINE || metal == SconceMetal.STARSTEEL)
                    require(state.getDestroyProgress(player, world, pos)
                            < iron.getDestroyProgress(player, world, pos),
                            "A tougher metal mines as quickly as iron: " + metal);
                require(state.canHarvestBlock(world, pos, player), "Iron pick cannot mine frame");
                require(player.gameMode.destroyBlock(pos), "Real frame harvesting failed");
                List<ItemEntity> drops = world.getEntitiesOfClass(ItemEntity.class, new AxisAlignedBB(pos).inflate(2), entity -> !entity.removed);
                require(drops.size() == 1 && drops.get(0).getItem().getItem()
                        == SconceMetalData.create(((net.minecraft.item.BlockItem)frame).getBlock(), metal).getItem()
                        && SconceMetalData.get(drops.get(0).getItem()) == metal, "Real harvesting changed frame metal: "
                        + state + " -> " + drops.stream().map(item -> item.getItem().save(new net.minecraft.nbt.CompoundNBT()).toString())
                                .collect(java.util.stream.Collectors.toList()));
                drops.forEach(entity -> entity.remove());
                if (metal == SconceMetal.GOLD || metal == SconceMetal.ADAMANTINE
                        || Boolean.getBoolean("iaf.probe.liveBaseMetals")) verifyContents(world, player, pos, state);
                cases++;
            }
            BlockPos saved = new BlockPos(800 + metal.ordinal() * 3, 80, 224);
            BlockState expected = BlockObjectHolder.light_metal_ironage_sconce_floor_empty_iron.defaultBlockState()
                    .setValue(SconceMetalData.METAL, metal).setValue(FurnitureBlock.DIRECTION, Direction.EAST).setValue(FurnitureBlock.WATERLOGGED, true);
            if (!world.getBlockState(saved).isAir()) require(world.getBlockState(saved) == expected, "Reload changed metal frame");
            world.setBlock(saved.below(), Blocks.STONE.defaultBlockState(), 2);
            world.setBlock(saved, expected, 2);
        }
        verifyRecipe(world);
        for (SconceMetal metal : SconceMetal.values()) {
            if (!metal.isBaseMetal() || !SconceMetalData.available(metal)) continue;
            net.minecraft.item.crafting.ShapedRecipe recipe = (net.minecraft.item.crafting.ShapedRecipe)world.getRecipeManager()
                    .byKey(new ResourceLocation("ironagefurniture", "light_metal_ironage_sconce_floor_empty_basemetals_" + metal.getSerializedName())).get();
            java.util.Collection<Item> nuggets = net.minecraft.tags.ItemTags.getAllTags()
                    .getTagOrEmpty(SconceMetalData.nuggets(metal)).getValues();
            for (Item nugget : nuggets) {
                CraftingInventory grid = grid();
                for (int slot : new int[]{0, 1, 2, 3, 6}) grid.setItem(slot, new ItemStack(nugget));
                require(recipe.matches(grid, world), "Published tagged nugget failed: " + metal + "/" + nugget.getRegistryName());
                ItemStack result = recipe.assemble(grid);
                require(result.getCount() == 4 && SconceMetalData.get(result) == metal, "Recipe lost its metal: " + metal);
                grid.setItem(8, new ItemStack(nugget));
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
                        .byKey(new ResourceLocation("ironagefurniture", "light_metal_ironage_sconce_floor_empty_basemetals_" + metal.getSerializedName())).get();
                CraftingInventory grid = grid();
                for (int slot : new int[]{0, 1, 2, 3, 6}) grid.setItem(slot, new ItemStack(Items.GOLD_NUGGET));
                require(recipe.matches(grid, world), "Synthetic tagged ingredient failed: " + metal);
                ItemStack result = recipe.assemble(grid);
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
            for (Direction side : Direction.values()) world.setBlock(pos.relative(side), Blocks.AIR.defaultBlockState(), 18);
            world.setBlock(pos.below(), Blocks.STONE.defaultBlockState(), 18);
            world.setBlock(pos.relative(empty.getValue(FurnitureBlock.DIRECTION).getOpposite()), Blocks.STONE.defaultBlockState(), 18);
            world.setBlock(pos, empty, 2); player.inventory.clearContent();
            player.setItemInHand(Hand.MAIN_HAND, new ItemStack(light, 8));
            require(empty.getBlock().use(empty, world, pos, player, Hand.MAIN_HAND, hit).consumesAction(), "Cannot fill frame");
            sameMetal(empty, world.getBlockState(pos));
            BlockState filled = world.getBlockState(pos);
            require(!filled.getValue(FurnitureBlock.WATERLOGGED), "Fresh dry frame started wet: " + empty + " -> " + filled);
            require(((net.minecraft.block.IWaterLoggable)filled.getBlock()).placeLiquid(world, pos, filled,
                    Fluids.WATER.getSource(false)), "Cannot waterlog filled frame: " + filled + ", input=" + light.getRegistryName());
            filled = world.getBlockState(pos); sameMetal(empty, filled);
            require(filled.getValue(FurnitureBlock.WATERLOGGED), "Extinguishing lost water");
            ((net.minecraft.block.IWaterLoggable)filled.getBlock()).takeLiquid(world, pos, filled);
            sameMetal(empty, world.getBlockState(pos));
            player.setItemInHand(Hand.MAIN_HAND, new ItemStack(Items.FLINT_AND_STEEL));
            filled = world.getBlockState(pos); filled.getBlock().use(filled, world, pos, player, Hand.MAIN_HAND, hit).consumesAction();
            sameMetal(empty, world.getBlockState(pos));
            player.setItemInHand(Hand.MAIN_HAND, ItemStack.EMPTY);
            filled = world.getBlockState(pos); filled.getBlock().use(filled, world, pos, player, Hand.MAIN_HAND, hit).consumesAction();
            sameMetal(empty, world.getBlockState(pos));
            world.getEntitiesOfClass(ItemEntity.class, new AxisAlignedBB(pos).inflate(2)).forEach(entity -> entity.remove());
        }
    }
    private static void verifyRecipe(ServerWorld world) {
        for (SconceMetal metal : new SconceMetal[]{SconceMetal.IRON, SconceMetal.GOLD}) {
            ResourceLocation id = new ResourceLocation("ironagefurniture",
                    "light_metal_ironage_sconce_floor_empty_" + metal.getSerializedName());
            IRecipe<?> found = world.getRecipeManager().byKey(id)
                    .orElseThrow(() -> new IllegalStateException("Missing sconce recipe: " + id));
            require(found instanceof net.minecraft.item.crafting.ShapedRecipe, "Wrong sconce recipe type: " + id);
            net.minecraft.item.crafting.ShapedRecipe recipe = (net.minecraft.item.crafting.ShapedRecipe)found;
            Item nugget = metal == SconceMetal.IRON ? Items.IRON_NUGGET : Items.GOLD_NUGGET;
            CraftingInventory grid = grid();
            for (int slot : new int[]{0, 1, 2, 3, 6}) grid.setItem(slot, new ItemStack(nugget));
            require(recipe.matches(grid, world), "Sconce recipe does not match: " + id);
            ItemStack result = recipe.assemble(grid);
            require(result.getCount() == 4 && id.equals(result.getItem().getRegistryName())
                    && SconceMetalData.get(result) == metal, "Five nuggets did not make four matching sconces: " + id);
            grid.setItem(8, new ItemStack(nugget));
            require(!recipe.matches(grid, world), "Sconce recipe accepts extra ingredients: " + id);
            grid.setItem(8, ItemStack.EMPTY);
            grid.setItem(0, new ItemStack(metal == SconceMetal.IRON ? Items.GOLD_NUGGET : Items.IRON_NUGGET));
            require(!recipe.matches(grid, world), "Sconce recipe accepts mixed metals: " + id);
        }
    }
    private static CraftingInventory grid() {
        return new CraftingInventory(new Container(null, 0) {
            @Override public boolean stillValid(PlayerEntity player) { return false; }
        }, 3, 3);
    }
    private static void sameMetal(BlockState old, BlockState current) {
        require(current.hasProperty(SconceMetalData.METAL) && SconceMetalData.get(old) == SconceMetalData.get(current),
                "Lighting change lost metal: " + old + " -> " + current);
    }
    private static void require(boolean passed, String message) { if (!passed) throw new IllegalStateException(message); }
}
