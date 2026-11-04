package zone.moddev.mc.ironagefurniture.fixture;

import java.util.List;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.Items;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.phys.AABB;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.GameType;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraftforge.common.util.FakePlayer;
import zone.moddev.mc.ironagefurniture.BlockObjectHolder;
import zone.moddev.mc.ironagefurniture.api.blocks.lightsource.phasefour.AdditionalSconce;
import zone.moddev.mc.ironagefurniture.api.blocks.lightsource.phasefour.Candle;
import zone.moddev.mc.ironagefurniture.init.PhaseFourLighting;

/** Actual Forge harvesting, water and content changes for every new sconce. */
final class SconceContentsRuntimeProbe {
    private SconceContentsRuntimeProbe() { }

    static int run(ServerLevel world, FakePlayer player) {
        BlockPos pos = new BlockPos(176, 80, 176);
        int cases = 0;
        for (boolean wall : new boolean[]{false, true}) for (boolean twin : new boolean[]{false, true}) {
            for (int count = twin ? 2 : 1; count <= (twin ? 2 : 4); count++) {
                for (Direction facing : Direction.Plane.HORIZONTAL) for (boolean wet : new boolean[]{false, true}) {
                    Block empty = wall ? BlockObjectHolder.light_metal_ironage_sconce_wall_empty_iron
                            : BlockObjectHolder.light_metal_ironage_sconce_floor_empty_iron;
                    Item contents = twin ? Items.TORCH : PhaseFourLighting.candle(false, true).asItem();
                    world.setBlock(pos.below(), Blocks.STONE.defaultBlockState(), 2);
                    world.setBlock(pos.relative(facing.getOpposite()), Blocks.STONE.defaultBlockState(), 2);
                    world.setBlock(pos, wet ? Blocks.WATER.defaultBlockState() : Blocks.AIR.defaultBlockState(), 2);
                    player.setGameMode(GameType.SURVIVAL);
                    player.getInventory().clearContent();
                    player.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(PhaseFourLighting.sconce(false, true, twin, count)));
                    BlockHitResult hit = new BlockHitResult(Vec3.atLowerCornerOf(pos), wall ? facing : Direction.UP, pos, false);
                    BlockState state = PhaseFourLighting.sconce(false, true, twin, count).getStateForPlacement(
                            new BlockPlaceContext(new UseOnContext(player, InteractionHand.MAIN_HAND, hit)));
                    require(state != null && state.getBlock() == PhaseFourLighting.sconce(wall, !wet, twin, count), "Wrong sconce placement");
                    state = state.setValue(Candle.DIRECTION, facing);
                    world.setBlock(pos, state, 2);
                    AdditionalSconce sconce = (AdditionalSconce) state.getBlock();
                    require(NbtUtils.readBlockState(NbtUtils.writeBlockState(state)) == state, "Sconce state did not round-trip");
                    require(sconce.getPickBlock(state, hit, world, pos, player).getItem()
                            == BlockObjectHolder.light_metal_ironage_sconce_floor_empty_iron.asItem(), "Pick returned hidden contents");
                    List<ItemStack> drops = state.getDrops(new LootContext.Builder(world));
                    require(drops.size() == 2 && drops.get(0).getCount() == 1 && drops.get(1).getItem() == contents
                            && drops.get(1).getCount() == count, "Sconce drop lost contents");
                    if (!wet) require(sconce.placeLiquid(world, pos, state, Fluids.WATER.getSource(false)), "Water was refused");
                    state = world.getBlockState(pos);
                    require(state.getBlock() == PhaseFourLighting.sconce(wall, false, twin, count)
                            && state.getValue(Candle.WATERLOGGED) && state.getValue(Candle.DIRECTION) == facing, "Water extinguishing lost state");
                    player.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(Items.FLINT_AND_STEEL));
                    interact(world, player, pos, hit, false);
                    require(world.getBlockState(pos) == state, "Wet sconce relit");
                    require(((AdditionalSconce) state.getBlock()).pickupBlock(world, pos, state).is(net.minecraft.world.item.Items.WATER_BUCKET), "Could not drain sconce");
                    interact(world, player, pos, hit, true);
                    require(player.getItemInHand(InteractionHand.MAIN_HAND).getDamageValue() == 1, "Relighting did not use flint and steel");
                    require(world.getBlockState(pos).getBlock() == PhaseFourLighting.sconce(wall, true, twin, count), "Wrong relit state");

                    // Dry torch/candle relighting must not add contents or consume the held stack.
                    world.setBlock(pos, PhaseFourLighting.sconce(wall, false, twin, count).defaultBlockState().setValue(Candle.DIRECTION, facing), 2);
                    player.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(contents, 10));
                    interact(world, player, pos, hit, true);
                    require(player.getItemInHand(InteractionHand.MAIN_HAND).getCount() == 10
                            && world.getBlockState(pos).getBlock() == PhaseFourLighting.sconce(wall, true, twin, count), "Relighting added or removed contents");
                    player.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
                    interact(world, player, pos, hit, true);
                    require(world.getBlockState(pos).getBlock() == empty && player.getItemInHand(InteractionHand.MAIN_HAND).getCount() == count
                            && player.getItemInHand(InteractionHand.MAIN_HAND).getItem() == contents, "Removal lost contents");

                    // Forge's real harvest path, not only a direct getDrops invocation.
                    world.setBlock(pos, PhaseFourLighting.sconce(wall, false, twin, count).defaultBlockState()
                            .setValue(Candle.DIRECTION, facing).setValue(Candle.WATERLOGGED, true), 2);
                    clearDrops(world, pos);
                    player.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(Items.IRON_PICKAXE));
                    require(world.getBlockState(pos).canHarvestBlock(world, pos, player), "Iron pick cannot harvest sconce");
                    require(player.gameMode.destroyBlock(pos), "Cannot harvest sconce");
                    List<ItemEntity> actual = world.getEntitiesOfClass(ItemEntity.class, new AABB(pos).inflate(2), entity -> !entity.isRemoved());
                    require(actual.stream().map(ItemEntity::getItem).filter(stack -> stack.getItem() == contents).mapToInt(ItemStack::getCount).sum() == count,
                            "Real harvesting lost contents: " + state + ", live drops=" + actual.stream().map(ItemEntity::getItem).collect(java.util.stream.Collectors.toList()));
                    require(actual.stream().anyMatch(drop -> drop.getItem().getItem() == BlockObjectHolder.light_metal_ironage_sconce_floor_empty_iron.asItem()),
                            "Real harvesting lost frame");
                    require(world.getBlockState(pos).getBlock() == Blocks.WATER, "Harvesting lost water");
                    clearDrops(world, pos);
                    player.setGameMode(GameType.CREATIVE);
                    world.setBlock(pos, PhaseFourLighting.sconce(wall, true, twin, count).defaultBlockState().setValue(Candle.DIRECTION, facing), 2);
                    require(player.gameMode.destroyBlock(pos), "Creative harvest failed");
                    require(world.getEntitiesOfClass(ItemEntity.class, new AABB(pos).inflate(2), entity -> !entity.isRemoved()).isEmpty(),
                            "Creative harvesting duplicated sconce contents");
                    player.setGameMode(GameType.SURVIVAL);
                    world.setBlock(pos, PhaseFourLighting.sconce(wall, true, twin, count).defaultBlockState().setValue(Candle.DIRECTION, facing), 2);
                    BlockPos support = wall ? pos.relative(facing.getOpposite()) : pos.below();
                    world.setBlock(support, Blocks.AIR.defaultBlockState(), 3);
                    require(world.getBlockState(pos).isAir(), "Sconce survived without support");
                    world.setBlock(support, Blocks.STONE.defaultBlockState(), 2);
                    clearDrops(world, pos);
                    verifyFlames(sconce, facing, twin, wall);

                    BlockPos saved = new BlockPos(192 + (cases % 10) * 3, 80, 192 + (cases / 10) * 3);
                    BlockState expected = PhaseFourLighting.sconce(wall, !wet, twin, count).defaultBlockState()
                            .setValue(Candle.DIRECTION, facing).setValue(Candle.WATERLOGGED, wet);
                    if (Boolean.getBoolean("iaf.probe.metalReload")) require(world.getBlockState(saved) == expected, "Saved sconce changed on reload");
                    world.setBlock(saved.below(), Blocks.STONE.defaultBlockState(), 2);
                    if (wall) world.setBlock(saved.relative(facing.getOpposite()), Blocks.STONE.defaultBlockState(), 2);
                    world.setBlock(saved, expected, 2);
                    cases++;
                }
            }
        }
        verifyInsertion(world, player, pos);
        return cases;
    }

    private static void verifyInsertion(ServerLevel world, FakePlayer player, BlockPos pos) {
        for (boolean wall : new boolean[]{false, true}) for (boolean wet : new boolean[]{false, true}) {
            Block empty = wall ? BlockObjectHolder.light_metal_ironage_sconce_wall_empty_iron : BlockObjectHolder.light_metal_ironage_sconce_floor_empty_iron;
            BlockHitResult hit = new BlockHitResult(Vec3.atLowerCornerOf(pos), Direction.NORTH, pos, false);
            world.setBlock(pos, empty.defaultBlockState().setValue(Candle.DIRECTION, Direction.NORTH).setValue(Candle.WATERLOGGED, wet), 2);
            player.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(PhaseFourLighting.candle(false, true), 10));
            player.setGameMode(GameType.SURVIVAL);
            for (int count = 1; count <= 4; count++) {
                interact(world, player, pos, hit, true);
                require(world.getBlockState(pos).getBlock() == PhaseFourLighting.sconce(wall, !wet, false, count)
                        && world.getBlockState(pos).getValue(Candle.WATERLOGGED) == wet, "Candle insertion lost count or water");
            }
            require(player.getItemInHand(InteractionHand.MAIN_HAND).getCount() == 6, "Wrong candle insertion consumption");
            interact(world, player, pos, hit, true);
            require(world.getBlockState(pos).getBlock() == empty && player.getItemInHand(InteractionHand.MAIN_HAND).getCount() == 10, "Full sconce did not return candles");
            if (!wet) {
                player.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(Items.TORCH, 10));
                interact(world, player, pos, hit, true);
                interact(world, player, pos, hit, true);
                require(world.getBlockState(pos).getBlock() == PhaseFourLighting.sconce(wall, true, true, 2)
                        && player.getItemInHand(InteractionHand.MAIN_HAND).getCount() == 8, "Twin-torch insertion failed");
            }
            player.setGameMode(GameType.CREATIVE);
            world.setBlock(pos, empty.defaultBlockState().setValue(Candle.DIRECTION, Direction.NORTH).setValue(Candle.WATERLOGGED, wet), 2);
            player.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(PhaseFourLighting.candle(false, true), 10));
            interact(world, player, pos, hit, true);
            require(player.getItemInHand(InteractionHand.MAIN_HAND).getCount() == 10, "Creative consumed candle");
            player.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
            interact(world, player, pos, hit, true);
            require(player.getItemInHand(InteractionHand.MAIN_HAND).isEmpty(), "Creative removal duplicated candles");
        }
        player.setGameMode(GameType.SURVIVAL);
        world.removeBlock(pos, false);
    }

    private static void verifyFlames(AdditionalSconce sconce, Direction facing, boolean twin, boolean wall) {
        Direction authored = wall ? twin ? Direction.EAST : Direction.SOUTH : Direction.NORTH;
        for (int index = 0; index < sconce.contentsCount(); index++) {
            Vec3 reference = sconce.getFlameOffset(sconce.defaultBlockState().setValue(Candle.DIRECTION, authored), index);
            if (twin && index == 0) require(reference.distanceTo(wall ? new Vec3(4.5 / 16, 15.6 / 16, 10.5 / 16)
                    : new Vec3(6.1 / 16, 15.6 / 16, 6.8 / 16)) < .000001, "Twin-torch authored wick moved");
            if (wall && !twin && sconce.contentsCount() == 2 && index == 1)
                require(reference.distanceTo(new Vec3(6.5 / 16, 13.6 / 16, 5.5 / 16)) < .000001, "Lower wall candle wick moved");
            int turns = (facing.get2DDataValue() - authored.get2DDataValue()) & 3;
            double x = reference.x, z = reference.z;
            for (int turn = 0; turn < turns; turn++) { double previous = x; x = 1 - z; z = previous; }
            Vec3 actual = sconce.getFlameOffset(sconce.defaultBlockState().setValue(Candle.DIRECTION, facing), index);
            require(actual.distanceTo(new Vec3(x, reference.y, z)) < .000001, "Flame did not rotate with model");
            require(reference.y > .8 && reference.y < 1.1, "Flame below wick");
        }
    }

    private static void interact(ServerLevel world, FakePlayer player, BlockPos pos, BlockHitResult hit, boolean expected) {
        BlockState state = world.getBlockState(pos);
        require(state.getBlock().use(state, world, pos, player, InteractionHand.MAIN_HAND, hit).consumesAction() == expected, "Unexpected sconce interaction result");
    }
    private static void clearDrops(ServerLevel world, BlockPos pos) {
        world.getEntitiesOfClass(ItemEntity.class, new AABB(pos).inflate(2)).forEach(entity -> entity.discard());
    }
    private static void require(boolean passed, String message) { if (!passed) throw new IllegalStateException(message); }
}
