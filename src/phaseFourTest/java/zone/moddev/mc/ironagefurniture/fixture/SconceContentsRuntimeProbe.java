package zone.moddev.mc.ironagefurniture.fixture;

import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.item.Items;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameType;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraftforge.common.util.FakePlayer;
import zone.moddev.mc.ironagefurniture.BlockObjectHolder;
import zone.moddev.mc.ironagefurniture.api.blocks.lightsource.phasefour.AdditionalSconce;
import zone.moddev.mc.ironagefurniture.api.blocks.lightsource.phasefour.Candle;
import zone.moddev.mc.ironagefurniture.init.PhaseFourLighting;

/** Actual Forge harvesting, water and content changes for every new sconce. */
final class SconceContentsRuntimeProbe {
    private SconceContentsRuntimeProbe() { }

    static int run(ServerWorld world, FakePlayer player) {
        BlockPos pos = new BlockPos(176, 80, 176);
        int cases = 0;
        for (boolean wall : new boolean[]{false, true}) for (boolean twin : new boolean[]{false, true}) {
            for (int count = twin ? 2 : 1; count <= (twin ? 2 : 4); count++) {
                for (Direction facing : Direction.Plane.HORIZONTAL) for (boolean wet : new boolean[]{false, true}) {
                    Block empty = wall ? BlockObjectHolder.light_metal_ironage_sconce_wall_empty_iron
                            : BlockObjectHolder.light_metal_ironage_sconce_floor_empty_iron;
                    Item contents = twin ? Items.TORCH : PhaseFourLighting.candle(false, true).asItem();
                    world.setBlockState(pos.down(), Blocks.STONE.getDefaultState(), 2);
                    world.setBlockState(pos.offset(facing.getOpposite()), Blocks.STONE.getDefaultState(), 2);
                    world.setBlockState(pos, wet ? Blocks.WATER.getDefaultState() : Blocks.AIR.getDefaultState(), 2);
                    player.interactionManager.setGameType(GameType.SURVIVAL);
                    player.inventory.clear();
                    player.setHeldItem(Hand.MAIN_HAND, new ItemStack(PhaseFourLighting.sconce(false, true, twin, count)));
                    BlockRayTraceResult hit = new BlockRayTraceResult(new Vec3d(pos), wall ? facing : Direction.UP, pos, false);
                    BlockState state = PhaseFourLighting.sconce(false, true, twin, count).getStateForPlacement(
                            new BlockItemUseContext(new ItemUseContext(player, Hand.MAIN_HAND, hit)));
                    require(state != null && state.getBlock() == PhaseFourLighting.sconce(wall, !wet, twin, count), "Wrong sconce placement");
                    state = state.with(Candle.DIRECTION, facing);
                    world.setBlockState(pos, state, 2);
                    AdditionalSconce sconce = (AdditionalSconce) state.getBlock();
                    require(NBTUtil.readBlockState(NBTUtil.writeBlockState(state)) == state, "Sconce state did not round-trip");
                    require(sconce.getPickBlock(state, hit, world, pos, player).getItem()
                            == BlockObjectHolder.light_metal_ironage_sconce_floor_empty_iron.asItem(), "Pick returned hidden contents");
                    List<ItemStack> drops = state.getDrops(new LootContext.Builder(world));
                    require(drops.size() == 2 && drops.get(0).getCount() == 1 && drops.get(1).getItem() == contents
                            && drops.get(1).getCount() == count, "Sconce drop lost contents");
                    if (!wet) require(sconce.receiveFluid(world, pos, state, Fluids.WATER.getStillFluidState(false)), "Water was refused");
                    state = world.getBlockState(pos);
                    require(state.getBlock() == PhaseFourLighting.sconce(wall, false, twin, count)
                            && state.get(Candle.WATERLOGGED) && state.get(Candle.DIRECTION) == facing, "Water extinguishing lost state");
                    player.setHeldItem(Hand.MAIN_HAND, new ItemStack(Items.FLINT_AND_STEEL));
                    activate(world, player, pos, hit, false);
                    require(world.getBlockState(pos) == state, "Wet sconce relit");
                    require(((AdditionalSconce) state.getBlock()).pickupFluid(world, pos, state) == Fluids.WATER, "Could not drain sconce");
                    activate(world, player, pos, hit, true);
                    require(player.getHeldItem(Hand.MAIN_HAND).getDamage() == 1, "Relighting did not use flint and steel");
                    require(world.getBlockState(pos).getBlock() == PhaseFourLighting.sconce(wall, true, twin, count), "Wrong relit state");

                    // Dry torch/candle relighting must not add contents or consume the held stack.
                    world.setBlockState(pos, PhaseFourLighting.sconce(wall, false, twin, count).getDefaultState().with(Candle.DIRECTION, facing), 2);
                    player.setHeldItem(Hand.MAIN_HAND, new ItemStack(contents, 10));
                    activate(world, player, pos, hit, true);
                    require(player.getHeldItem(Hand.MAIN_HAND).getCount() == 10
                            && world.getBlockState(pos).getBlock() == PhaseFourLighting.sconce(wall, true, twin, count), "Relighting added or removed contents");
                    player.setHeldItem(Hand.MAIN_HAND, ItemStack.EMPTY);
                    activate(world, player, pos, hit, true);
                    require(world.getBlockState(pos).getBlock() == empty && player.getHeldItem(Hand.MAIN_HAND).getCount() == count
                            && player.getHeldItem(Hand.MAIN_HAND).getItem() == contents, "Removal lost contents");

                    // Forge's real harvest path, not only a direct getDrops invocation.
                    world.setBlockState(pos, PhaseFourLighting.sconce(wall, false, twin, count).getDefaultState()
                            .with(Candle.DIRECTION, facing).with(Candle.WATERLOGGED, true), 2);
                    clearDrops(world, pos);
                    player.setHeldItem(Hand.MAIN_HAND, new ItemStack(Items.IRON_PICKAXE));
                    require(world.getBlockState(pos).canHarvestBlock(world, pos, player), "Iron pick cannot harvest sconce");
                    require(player.interactionManager.tryHarvestBlock(pos), "Cannot harvest sconce");
                    List<ItemEntity> actual = world.getEntitiesWithinAABB(ItemEntity.class, new AxisAlignedBB(pos).grow(2), entity -> !entity.removed);
                    require(actual.stream().map(ItemEntity::getItem).filter(stack -> stack.getItem() == contents).mapToInt(ItemStack::getCount).sum() == count,
                            "Real harvesting lost contents: " + state + ", live drops=" + actual.stream().map(ItemEntity::getItem).collect(java.util.stream.Collectors.toList()));
                    require(actual.stream().anyMatch(drop -> drop.getItem().getItem() == BlockObjectHolder.light_metal_ironage_sconce_floor_empty_iron.asItem()),
                            "Real harvesting lost frame");
                    require(world.getBlockState(pos).getBlock() == Blocks.WATER, "Harvesting lost water");
                    clearDrops(world, pos);
                    player.interactionManager.setGameType(GameType.CREATIVE);
                    world.setBlockState(pos, PhaseFourLighting.sconce(wall, true, twin, count).getDefaultState().with(Candle.DIRECTION, facing), 2);
                    require(player.interactionManager.tryHarvestBlock(pos), "Creative harvest failed");
                    require(world.getEntitiesWithinAABB(ItemEntity.class, new AxisAlignedBB(pos).grow(2), entity -> !entity.removed).isEmpty(),
                            "Creative harvesting duplicated sconce contents");
                    player.interactionManager.setGameType(GameType.SURVIVAL);
                    world.setBlockState(pos, PhaseFourLighting.sconce(wall, true, twin, count).getDefaultState().with(Candle.DIRECTION, facing), 2);
                    BlockPos support = wall ? pos.offset(facing.getOpposite()) : pos.down();
                    world.setBlockState(support, Blocks.AIR.getDefaultState(), 3);
                    require(world.getBlockState(pos).isAir(), "Sconce survived without support");
                    world.setBlockState(support, Blocks.STONE.getDefaultState(), 2);
                    clearDrops(world, pos);
                    verifyFlames(sconce, facing, twin, wall);

                    BlockPos saved = new BlockPos(192 + (cases % 10) * 3, 80, 192 + (cases / 10) * 3);
                    BlockState expected = PhaseFourLighting.sconce(wall, !wet, twin, count).getDefaultState()
                            .with(Candle.DIRECTION, facing).with(Candle.WATERLOGGED, wet);
                    if (!world.getBlockState(saved).isAir()) require(world.getBlockState(saved) == expected, "Saved sconce changed on reload");
                    world.setBlockState(saved.down(), Blocks.STONE.getDefaultState(), 2);
                    if (wall) world.setBlockState(saved.offset(facing.getOpposite()), Blocks.STONE.getDefaultState(), 2);
                    world.setBlockState(saved, expected, 2);
                    cases++;
                }
            }
        }
        verifyInsertion(world, player, pos);
        return cases;
    }

    private static void verifyInsertion(ServerWorld world, FakePlayer player, BlockPos pos) {
        for (boolean wall : new boolean[]{false, true}) for (boolean wet : new boolean[]{false, true}) {
            Block empty = wall ? BlockObjectHolder.light_metal_ironage_sconce_wall_empty_iron : BlockObjectHolder.light_metal_ironage_sconce_floor_empty_iron;
            BlockRayTraceResult hit = new BlockRayTraceResult(new Vec3d(pos), Direction.NORTH, pos, false);
            world.setBlockState(pos, empty.getDefaultState().with(Candle.DIRECTION, Direction.NORTH).with(Candle.WATERLOGGED, wet), 2);
            player.setHeldItem(Hand.MAIN_HAND, new ItemStack(PhaseFourLighting.candle(false, true), 10));
            player.interactionManager.setGameType(GameType.SURVIVAL);
            for (int count = 1; count <= 4; count++) {
                activate(world, player, pos, hit, true);
                require(world.getBlockState(pos).getBlock() == PhaseFourLighting.sconce(wall, !wet, false, count)
                        && world.getBlockState(pos).get(Candle.WATERLOGGED) == wet, "Candle insertion lost count or water");
            }
            require(player.getHeldItem(Hand.MAIN_HAND).getCount() == 6, "Wrong candle insertion consumption");
            activate(world, player, pos, hit, true);
            require(world.getBlockState(pos).getBlock() == empty && player.getHeldItem(Hand.MAIN_HAND).getCount() == 10, "Full sconce did not return candles");
            if (!wet) {
                player.setHeldItem(Hand.MAIN_HAND, new ItemStack(Items.TORCH, 10));
                activate(world, player, pos, hit, true);
                activate(world, player, pos, hit, true);
                require(world.getBlockState(pos).getBlock() == PhaseFourLighting.sconce(wall, true, true, 2)
                        && player.getHeldItem(Hand.MAIN_HAND).getCount() == 8, "Twin-torch insertion failed");
            }
            player.interactionManager.setGameType(GameType.CREATIVE);
            world.setBlockState(pos, empty.getDefaultState().with(Candle.DIRECTION, Direction.NORTH).with(Candle.WATERLOGGED, wet), 2);
            player.setHeldItem(Hand.MAIN_HAND, new ItemStack(PhaseFourLighting.candle(false, true), 10));
            activate(world, player, pos, hit, true);
            require(player.getHeldItem(Hand.MAIN_HAND).getCount() == 10, "Creative consumed candle");
            player.setHeldItem(Hand.MAIN_HAND, ItemStack.EMPTY);
            activate(world, player, pos, hit, true);
            require(player.getHeldItem(Hand.MAIN_HAND).isEmpty(), "Creative removal duplicated candles");
        }
        player.interactionManager.setGameType(GameType.SURVIVAL);
        world.removeBlock(pos, false);
    }

    private static void verifyFlames(AdditionalSconce sconce, Direction facing, boolean twin, boolean wall) {
        Direction authored = wall ? twin ? Direction.EAST : Direction.SOUTH : Direction.NORTH;
        for (int index = 0; index < sconce.contentsCount(); index++) {
            Vec3d reference = sconce.getFlameOffset(sconce.getDefaultState().with(Candle.DIRECTION, authored), index);
            if (twin && index == 0) require(reference.distanceTo(wall ? new Vec3d(4.5 / 16, 15.6 / 16, 10.5 / 16)
                    : new Vec3d(6.1 / 16, 15.6 / 16, 6.8 / 16)) < .000001, "Twin-torch authored wick moved");
            if (wall && !twin && sconce.contentsCount() == 2 && index == 1)
                require(reference.distanceTo(new Vec3d(6.5 / 16, 13.6 / 16, 5.5 / 16)) < .000001, "Lower wall candle wick moved");
            int turns = (facing.getHorizontalIndex() - authored.getHorizontalIndex()) & 3;
            double x = reference.x, z = reference.z;
            for (int turn = 0; turn < turns; turn++) { double previous = x; x = 1 - z; z = previous; }
            Vec3d actual = sconce.getFlameOffset(sconce.getDefaultState().with(Candle.DIRECTION, facing), index);
            require(actual.distanceTo(new Vec3d(x, reference.y, z)) < .000001, "Flame did not rotate with model");
            require(reference.y > .8 && reference.y < 1.1, "Flame below wick");
        }
    }

    private static void activate(ServerWorld world, FakePlayer player, BlockPos pos, BlockRayTraceResult hit, boolean expected) {
        BlockState state = world.getBlockState(pos);
        require(state.getBlock().onBlockActivated(state, world, pos, player, Hand.MAIN_HAND, hit) == expected, "Unexpected sconce interaction result");
    }
    private static void clearDrops(ServerWorld world, BlockPos pos) {
        world.getEntitiesWithinAABB(ItemEntity.class, new AxisAlignedBB(pos).grow(2)).forEach(Entity::remove);
    }
    private static void require(boolean passed, String message) { if (!passed) throw new IllegalStateException(message); }
}
