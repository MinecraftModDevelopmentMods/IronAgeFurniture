package zone.moddev.mc.ironagefurniture.fixture;

import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameRules;
import net.minecraft.world.GameType;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.FakePlayer;
import zone.moddev.mc.ironagefurniture.BlockObjectHolder;
import zone.moddev.mc.ironagefurniture.api.MineralogyCompat;
import zone.moddev.mc.ironagefurniture.api.SconceMetalData;
import zone.moddev.mc.ironagefurniture.api.blocks.lightsource.phasefour.Candle;
import zone.moddev.mc.ironagefurniture.api.enumerations.SconceMetal;
import zone.moddev.mc.ironagefurniture.init.PhaseFourLighting;

/** Mines the real support and checks live item entities, not just a drop-list method. */
final class SupportLossRuntimeProbe {
    private SupportLossRuntimeProbe() { }

    static int run(ServerWorld world, FakePlayer player) {
        BlockPos pos = new BlockPos(304, 80, 304);
        int cases = 0;
        player.gameMode.setGameModeForPlayer(GameType.SURVIVAL);
        player.setItemInHand(Hand.MAIN_HAND, new ItemStack(Items.IRON_PICKAXE));
        for (boolean wall : new boolean[]{false, true}) for (Direction facing : Direction.Plane.HORIZONTAL)
            for (Block support : wall ? new Block[]{Blocks.STONE} : new Block[]{Blocks.STONE, Blocks.OAK_FENCE})
                for (boolean wet : new boolean[]{true, false}) for (boolean lit : new boolean[]{false, true}) {
                    for (SconceMetal metal : SconceMetal.values()) {
                        BlockState twin = PhaseFourLighting.sconce(wall, lit, true, 2).defaultBlockState()
                                .setValue(Candle.DIRECTION, facing).setValue(Candle.WATERLOGGED, wet).setValue(SconceMetalData.METAL, metal);
                        verify(world, player, pos, twin, wall, support, Items.TORCH, 2, metal, true);
                        cases++;
                        for (int count = 1; count <= 4; count++) {
                            BlockState candles = PhaseFourLighting.sconce(wall, lit, false, count).defaultBlockState()
                                    .setValue(Candle.DIRECTION, facing).setValue(Candle.WATERLOGGED, wet).setValue(SconceMetalData.METAL, metal);
                            verify(world, player, pos, candles, wall, support,
                                    PhaseFourLighting.candle(false, true).asItem(), count, metal, true);
                            cases++;
                        }
                    }
                    BlockState candle = PhaseFourLighting.candle(wall, lit).defaultBlockState()
                            .setValue(Candle.DIRECTION, facing).setValue(Candle.WATERLOGGED, wet);
                    verify(world, player, pos, candle, wall, support, PhaseFourLighting.candle(false, true).asItem(), 1, null, true);
                    cases++;
                    if (lit && MineralogyCompat.isEnabled()) for (SconceMetal metal : SconceMetal.values()) {
                        BlockState rockSalt = PhaseFourLighting.rockSalt(wall).defaultBlockState()
                                .setValue(Candle.DIRECTION, facing).setValue(Candle.WATERLOGGED, wet).setValue(SconceMetalData.METAL, metal);
                        verify(world, player, pos, rockSalt, wall, support, MineralogyCompat.getRockSaltLamp().asItem(), 1, metal, true);
                        cases++;
                    }
                }
        boolean tileDrops = world.getGameRules().getBoolean(GameRules.RULE_DOBLOCKDROPS);
        try {
            world.getGameRules().getRule(GameRules.RULE_DOBLOCKDROPS).set(false, world.getServer());
            for (boolean wall : new boolean[]{false, true}) for (boolean wet : new boolean[]{false, true}) {
                BlockState twin = PhaseFourLighting.sconce(wall, false, true, 2).defaultBlockState()
                        .setValue(Candle.DIRECTION, Direction.NORTH).setValue(Candle.WATERLOGGED, wet).setValue(SconceMetalData.METAL, SconceMetal.GOLD);
                verify(world, player, pos, twin, wall, Blocks.STONE, Items.TORCH, 2, SconceMetal.GOLD, false);
                cases++;
            }
        } finally { world.getGameRules().getRule(GameRules.RULE_DOBLOCKDROPS).set(tileDrops, world.getServer()); }
        world.removeBlock(pos, false);
        clearDrops(world, pos);
        return cases;
    }

    private static void verify(ServerWorld world, FakePlayer player, BlockPos pos, BlockState state,
            boolean wall, Block support, Item contents, int count, SconceMetal metal, boolean expectDrops) {
        BlockPos backing = wall ? pos.relative(state.getValue(Candle.DIRECTION).getOpposite()) : pos.below();
        world.setBlock(backing, support.defaultBlockState(), 2);
        world.setBlock(pos, state, 2);
        require(state.canSurvive(world, pos), "Invalid test support", state);
        clearDrops(world, pos);
        require(player.gameMode.destroyBlock(backing), "Support could not be mined", state);
        require(world.getBlockState(pos).getBlock() == (state.getValue(Candle.WATERLOGGED) ? Blocks.WATER : Blocks.AIR),
                "Support loss left a sconce or lost its water", state);
        List<ItemEntity> drops = world.getEntitiesOfClass(ItemEntity.class, new AxisAlignedBB(pos).inflate(2), entity -> !entity.removed);
        int contentsDropped = drops.stream().map(ItemEntity::getItem).filter(stack -> stack.getItem() == contents)
                .mapToInt(ItemStack::getCount).sum();
        require(contentsDropped == (expectDrops ? count : 0), "Support loss lost or duplicated contents", state);
        int framesDropped = 0;
        for (ItemEntity entity : drops) {
            ItemStack stack = entity.getItem();
            if (!(Block.byItem(stack.getItem()) instanceof zone.moddev.mc.ironagefurniture.api.blocks.base.LightHolderSconce)) continue;
            framesDropped += stack.getCount();
            require(metal == SconceMetalData.get(stack), "Support loss changed the frame metal", state);
            require(stack.getItem() == SconceMetalData.create(BlockObjectHolder.light_metal_ironage_sconce_floor_empty_iron, metal).getItem(),
                    "Support loss returned a hidden frame item", state);
        }
        require(framesDropped == (expectDrops && metal != null ? 1 : 0), "Support loss lost or duplicated frame", state);
        // Another neighbour update must not create a second batch of drops.
        world.setBlock(backing, Blocks.STONE.defaultBlockState(), 3);
        world.setBlock(backing, Blocks.AIR.defaultBlockState(), 3);
        require(world.getEntitiesOfClass(ItemEntity.class, new AxisAlignedBB(pos).inflate(2), entity -> !entity.removed).size() == drops.size(),
                "Support loss repeated its drops", state);
        clearDrops(world, pos);
    }

    private static void clearDrops(ServerWorld world, BlockPos pos) {
        world.getEntitiesOfClass(ItemEntity.class, new AxisAlignedBB(pos).inflate(2)).forEach(entity -> entity.remove());
    }
    private static void require(boolean passed, String message, BlockState state) {
        if (!passed) throw new IllegalStateException(message + ": " + state);
    }
}
