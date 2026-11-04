package zone.moddev.mc.ironagefurniture.fixture;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.GameType;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.common.util.FakePlayer;
import zone.moddev.mc.ironagefurniture.api.SconceMetalData;
import zone.moddev.mc.ironagefurniture.api.blocks.lightsource.phasefour.Candle;
import zone.moddev.mc.ironagefurniture.api.enumerations.SconceMetal;
import zone.moddev.mc.ironagefurniture.init.PhaseFourLighting;

/** Sends bucket clicks through Forge's normal block-interaction manager. */
final class CandleBucketRuntimeProbe {
    private CandleBucketRuntimeProbe() { }

    static int run(ServerLevel world, FakePlayer player) {
        int cases = 0;
        BlockPos pos = new BlockPos(1600, 104, 1600);
        for (boolean wall : new boolean[]{false, true}) for (Direction facing : Direction.Plane.HORIZONTAL)
            for (boolean lit : new boolean[]{false, true}) for (boolean wet : new boolean[]{false, true})
                for (InteractionHand hand : InteractionHand.values()) for (GameType mode : new GameType[]{GameType.SURVIVAL, GameType.CREATIVE}) {
                    world.setBlock(pos.below(), Blocks.STONE.defaultBlockState(), 2);
                    world.setBlock(pos.relative(facing.getOpposite()), Blocks.STONE.defaultBlockState(), 2);
                    player.setGameMode(mode);
                    BlockState candle = PhaseFourLighting.candle(wall, lit).defaultBlockState()
                            .setValue(Candle.DIRECTION, facing).setValue(Candle.WATERLOGGED, wet);
                    BlockState unlit = PhaseFourLighting.candle(wall, false).defaultBlockState()
                            .setValue(Candle.DIRECTION, facing).setValue(Candle.WATERLOGGED, wet);
                    click(world, player, pos, hand, candle, unlit);
                    cases++;
                    for (SconceMetal metal : SconceMetal.values()) for (boolean twin : new boolean[]{false, true})
                        for (int count = twin ? 2 : 0; count <= (twin ? 2 : 4); count++) {
                            BlockState sconce = sconce(wall, lit, twin, count).defaultBlockState()
                                    .setValue(Candle.DIRECTION, facing).setValue(Candle.WATERLOGGED, wet).setValue(SconceMetalData.METAL, metal);
                            BlockState extinguished = sconce(wall, false, twin, count).defaultBlockState()
                                    .setValue(Candle.DIRECTION, facing).setValue(Candle.WATERLOGGED, wet).setValue(SconceMetalData.METAL, metal);
                            click(world, player, pos, hand, sconce, extinguished);
                            cases++;
                        }
                }
        return cases;
    }

    private static net.minecraft.world.level.block.Block sconce(boolean wall, boolean lit, boolean twin, int count) {
        if (count != 0) return PhaseFourLighting.sconce(wall, lit, twin, count);
        return PhaseFourLighting.block("light_metal_ironage_sconce_" + (wall ? "wall" : "floor")
                + "_torch_iron" + (lit ? "" : "_unlit"));
    }

    private static void click(ServerLevel world, FakePlayer player, BlockPos pos, InteractionHand hand,
            BlockState original, BlockState expected) {
        world.setBlock(pos, original, 2);
        ItemStack bucket = new ItemStack(Items.WATER_BUCKET);
        player.setItemInHand(hand, bucket);
        BlockHitResult hit = new BlockHitResult(Vec3.atLowerCornerOf(pos), Direction.UP, pos, false);
        require(player.gameMode.useItemOn(player, world, bucket, hand, hit).consumesAction(),
                "Bucket click fell through to water placement", original);
        require(world.getBlockState(pos) == expected, "Bucket changed candle count, water, facing or metal", original);
        require(player.getItemInHand(hand).getItem() == Items.WATER_BUCKET
                && player.getItemInHand(hand).getCount() == 1, "Extinguishing emptied the bucket", original);
        if (!expected.getValue(Candle.WATERLOGGED)) {
            net.minecraft.world.level.block.SimpleWaterloggedBlock holder = (net.minecraft.world.level.block.SimpleWaterloggedBlock)expected.getBlock();
            require(holder.placeLiquid(world, pos, expected, net.minecraft.world.level.material.Fluids.WATER.getSource(false)),
                    "Extinguished candle refused real water", original);
            BlockState wet = expected.setValue(Candle.WATERLOGGED, true);
            require(world.getBlockState(pos) == wet, "Waterlogging lost holder state", original);
            require(holder.pickupBlock(world, pos, wet).is(net.minecraft.world.item.Items.WATER_BUCKET)
                    && world.getBlockState(pos) == expected, "Draining changed holder state", original);
        }
    }

    private static void require(boolean passed, String message, BlockState state) {
        if (!passed) throw new IllegalStateException(message + ": " + state);
    }
}
