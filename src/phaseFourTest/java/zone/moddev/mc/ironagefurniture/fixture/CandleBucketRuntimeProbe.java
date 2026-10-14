package zone.moddev.mc.ironagefurniture.fixture;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameType;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.FakePlayer;
import zone.moddev.mc.ironagefurniture.api.SconceMetalData;
import zone.moddev.mc.ironagefurniture.api.blocks.lightsource.phasefour.Candle;
import zone.moddev.mc.ironagefurniture.api.enumerations.SconceMetal;
import zone.moddev.mc.ironagefurniture.init.PhaseFourLighting;

/** Sends bucket clicks through Forge's normal block-interaction manager. */
final class CandleBucketRuntimeProbe {
    private CandleBucketRuntimeProbe() { }

    static int run(ServerWorld world, FakePlayer player) {
        int cases = 0;
        BlockPos pos = new BlockPos(1600, 104, 1600);
        for (boolean wall : new boolean[]{false, true}) for (Direction facing : Direction.Plane.HORIZONTAL)
            for (boolean lit : new boolean[]{false, true}) for (boolean wet : new boolean[]{false, true})
                for (Hand hand : Hand.values()) for (GameType mode : new GameType[]{GameType.SURVIVAL, GameType.CREATIVE}) {
                    world.setBlock(pos.below(), Blocks.STONE.defaultBlockState(), 2);
                    world.setBlock(pos.relative(facing.getOpposite()), Blocks.STONE.defaultBlockState(), 2);
                    player.gameMode.setGameModeForPlayer(mode);
                    BlockState candle = PhaseFourLighting.candle(wall, lit).defaultBlockState()
                            .setValue(Candle.DIRECTION, facing).setValue(Candle.WATERLOGGED, wet);
                    BlockState unlit = PhaseFourLighting.candle(wall, false).defaultBlockState()
                            .setValue(Candle.DIRECTION, facing).setValue(Candle.WATERLOGGED, wet);
                    click(world, player, pos, hand, candle, unlit);
                    cases++;
                    for (SconceMetal metal : SconceMetal.values()) for (boolean twin : new boolean[]{false, true})
                        for (int count = twin ? 2 : 1; count <= (twin ? 2 : 4); count++) {
                            BlockState sconce = PhaseFourLighting.sconce(wall, lit, twin, count).defaultBlockState()
                                    .setValue(Candle.DIRECTION, facing).setValue(Candle.WATERLOGGED, wet).setValue(SconceMetalData.METAL, metal);
                            BlockState extinguished = PhaseFourLighting.sconce(wall, false, twin, count).defaultBlockState()
                                    .setValue(Candle.DIRECTION, facing).setValue(Candle.WATERLOGGED, wet).setValue(SconceMetalData.METAL, metal);
                            click(world, player, pos, hand, sconce, extinguished);
                            cases++;
                        }
                }
        return cases;
    }

    private static void click(ServerWorld world, FakePlayer player, BlockPos pos, Hand hand,
            BlockState original, BlockState expected) {
        world.setBlock(pos, original, 2);
        ItemStack bucket = new ItemStack(Items.WATER_BUCKET);
        player.setItemInHand(hand, bucket);
        BlockRayTraceResult hit = new BlockRayTraceResult(new Vec3d(pos), Direction.UP, pos, false);
        require(player.gameMode.useItemOn(player, world, bucket, hand, hit).consumesAction(),
                "Bucket click fell through to water placement", original);
        require(world.getBlockState(pos) == expected, "Bucket changed candle count, water, facing or metal", original);
        require(player.getItemInHand(hand).getItem() == Items.WATER_BUCKET
                && player.getItemInHand(hand).getCount() == 1, "Extinguishing emptied the bucket", original);
        if (!expected.getValue(Candle.WATERLOGGED)) {
            net.minecraft.block.IWaterLoggable holder = (net.minecraft.block.IWaterLoggable)expected.getBlock();
            require(holder.placeLiquid(world, pos, expected, net.minecraft.fluid.Fluids.WATER.getSource(false)),
                    "Extinguished candle refused real water", original);
            BlockState wet = expected.setValue(Candle.WATERLOGGED, true);
            require(world.getBlockState(pos) == wet, "Waterlogging lost holder state", original);
            require(holder.takeLiquid(world, pos, wet) == net.minecraft.fluid.Fluids.WATER
                    && world.getBlockState(pos) == expected, "Draining changed holder state", original);
        }
    }

    private static void require(boolean passed, String message, BlockState state) {
        if (!passed) throw new IllegalStateException(message + ": " + state);
    }
}
