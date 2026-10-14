package zone.moddev.mc.ironagefurniture.fixture;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.GameRules;
import net.minecraft.world.GameType;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import zone.moddev.mc.ironagefurniture.BlockObjectHolder;
import zone.moddev.mc.ironagefurniture.api.SconceMetalData;
import zone.moddev.mc.ironagefurniture.api.blocks.base.FurnitureBlock;
import zone.moddev.mc.ironagefurniture.api.blocks.lightsource.lava.LightSourceSconceLavaWall;
import zone.moddev.mc.ironagefurniture.api.entity.ReleasedLavaLamp;
import zone.moddev.mc.ironagefurniture.api.enumerations.SconceMetal;

/** Mines an elevated wall sconce, then follows its vial all the way to the ground. */
final class WallLavaMiningRuntimeProbe {
    private final List<ReleasedLavaLamp> released = new ArrayList<>();
    private BlockPos cancelledBreak;
    private boolean cancelSpawn;

    @SubscribeEvent public void breaking(BlockEvent.BreakEvent event) {
        if (event.getPos().equals(cancelledBreak)) event.setCanceled(true);
    }
    @SubscribeEvent public void joining(EntityJoinWorldEvent event) {
        if (event.getEntity() instanceof ReleasedLavaLamp) {
            released.add((ReleasedLavaLamp)event.getEntity());
            if (cancelSpawn) event.setCanceled(true);
        }
    }

    static int run(ServerWorld world, FakePlayer player) {
        WallLavaMiningRuntimeProbe listener = new WallLavaMiningRuntimeProbe();
        MinecraftForge.EVENT_BUS.register(listener);
        boolean oldDrops = world.getGameRules().getBoolean(GameRules.RULE_DOBLOCKDROPS);
        GameType oldMode = player.gameMode.getGameModeForPlayer();
        int cases = 0;
        try {
            LightSourceSconceLavaWall sconce = (LightSourceSconceLavaWall)
                    BlockObjectHolder.light_metal_ironage_sconce_wall_lava_iron;
            for (SconceMetal metal : new SconceMetal[]{SconceMetal.IRON, SconceMetal.GOLD, SconceMetal.ADAMANTINE})
                for (Direction facing : Direction.Plane.HORIZONTAL)
                    for (boolean water : new boolean[]{false, true}) for (int mode = 0; mode < 10; mode++) {
                        BlockPos source = new BlockPos(768 + (cases % 16) * 4, 84, 240 + (cases / 16) * 4);
                        BlockPos landing = source.below(4);
                        for (int y = 0; y < 6; y++) world.setBlock(landing.above(y), Blocks.AIR.defaultBlockState(), 2);
                        world.setBlock(landing.below(), Blocks.STONE.defaultBlockState(), 2);
                        if (water) world.setBlock(landing, Blocks.WATER.defaultBlockState(), 2);
                        world.setBlock(source.relative(facing.getOpposite()), Blocks.STONE.defaultBlockState(), 2);
                        clearDrops(world, source);
                        BlockState original = sconce.defaultBlockState().setValue(FurnitureBlock.DIRECTION, facing)
                                .setValue(SconceMetalData.METAL, metal);
                        world.setBlock(source, original, 2);
                        player.setPos(source.getX() + 2, source.getY(), source.getZ() + .5);
                        player.gameMode.setGameModeForPlayer(mode == 4 ? GameType.CREATIVE : GameType.SURVIVAL);
                        ItemStack tool = mode == 1 ? new ItemStack(Items.WOODEN_PICKAXE)
                                : mode == 2 || mode == 3 ? new ItemStack(Items.DIAMOND_PICKAXE) : ItemStack.EMPTY;
                        if (mode == 3) tool.enchant(Enchantments.SILK_TOUCH, 1);
                        if (mode == 6) tool = new ItemStack(BlockObjectHolder.light_metal_ironage_block_floor_lava_clear);
                        player.setItemInHand(Hand.MAIN_HAND, tool);
                        world.getGameRules().getRule(GameRules.RULE_DOBLOCKDROPS).set(mode != 7, world.getServer());
                        listener.cancelledBreak = mode == 8 ? source : null;
                        listener.cancelSpawn = mode == 9;
                        int before = listener.released.size();
                        String context = metal + "/" + facing + "/water=" + water + "/mode=" + mode;
                        if (mode == 5 || mode == 6) {
                            sconce.use(original, world, source, player, Hand.MAIN_HAND,
                                    new BlockRayTraceResult(Vector3d.atLowerCornerOf(source), facing, source, false));
                            require(world.getBlockState(source).getBlock() == BlockObjectHolder.light_metal_ironage_sconce_wall_empty_iron
                                    && SconceMetalData.get(world.getBlockState(source)) == metal
                                    && world.getBlockState(source).getValue(FurnitureBlock.DIRECTION) == facing,
                                    "Safe removal changed the holder: " + context);
                            require(player.getMainHandItem().getItem() == BlockObjectHolder.light_metal_ironage_block_floor_lava_clear.asItem()
                                    && player.getMainHandItem().getCount() == (mode == 6 ? 2 : 1), "Safe removal lost the lamp");
                        } else {
                            // Forge reports a handled mining request even when the
                            // block's removal hook declines it. Check the state and
                            // drops below to verify entity-spawn cancellation.
                            require(player.gameMode.destroyBlock(source) == (mode != 8),
                                    "Wrong mining/cancellation result: " + context);
                            require(world.getBlockState(source) == (mode == 8 || mode == 9 ? original : Blocks.AIR.defaultBlockState()),
                                    "Mining left fire at the wall or lost a cancelled holder: " + context);
                        }
                        boolean falls = mode < 3 || mode == 7;
                        require(listener.released.size() == before + (falls || mode == 9 ? 1 : 0),
                                "Mining did not release exactly one lamp: " + context);
                        if (falls) {
                            ReleasedLavaLamp lamp = listener.released.get(before);
                            require(!lamp.removed && lamp.getY() > landing.getY() + 2, "Lamp did not start in flight");
                            for (int tick = 0; tick < 100 && !lamp.removed; tick++) lamp.tick();
                            require(lamp.removed && !lamp.noPhysics, "Lamp failed to fall and land: " + context);
                            world.removeEntity(lamp, false);
                            require(world.getBlockState(landing).getBlock() == (water ? BlockObjectHolder.obsidian_chunk : Blocks.FIRE),
                                    "Lamp vanished instead of shattering at its landing: " + context);
                            if (water) require(world.getBlockState(landing).getValue(FurnitureBlock.WATERLOGGED), "Water landing lost water");
                        } else {
                            require(world.getBlockState(landing).getBlock() == (water ? Blocks.WATER : Blocks.AIR),
                                    "Safe removal, Silk Touch or cancellation altered the landing");
                        }
                        List<ItemEntity> drops = drops(world, source);
                        int frames = drops.stream().filter(item -> item.getItem().getItem()
                                == SconceMetalData.create(BlockObjectHolder.light_metal_ironage_sconce_floor_empty_iron, metal).getItem())
                                .mapToInt(item -> item.getItem().getCount()).sum();
                        int lamps = drops.stream().filter(item -> item.getItem().getItem()
                                == BlockObjectHolder.light_metal_ironage_block_floor_lava_clear.asItem())
                                .mapToInt(item -> item.getItem().getCount()).sum();
                        require(frames == (mode < 4 ? 1 : 0) && lamps == (mode == 3 ? 1 : 0),
                                "Wrong frame, metal or intact-lamp drops: " + context);
                        require(drops.stream().mapToInt(item -> item.getItem().getCount()).sum() == frames + lamps, "Unexpected extra drops");
                        clearDrops(world, source);
                        world.removeBlock(source, false);
                        listener.cancelSpawn = false;
                        listener.cancelledBreak = null;
                        cases++;
                    }
            org.apache.logging.log4j.LogManager.getLogger().info("IAF WALL LAVA MINING PROBE PASSED: {} cases", cases);
            return cases;
        } finally {
            world.getGameRules().getRule(GameRules.RULE_DOBLOCKDROPS).set(oldDrops, world.getServer());
            player.gameMode.setGameModeForPlayer(oldMode);
            player.setItemInHand(Hand.MAIN_HAND, ItemStack.EMPTY);
            MinecraftForge.EVENT_BUS.unregister(listener);
        }
    }

    /** Existing harvesting probes have a floor immediately below their sconces. */
    static void settleReleased(ServerWorld world, BlockPos source) {
        for (ReleasedLavaLamp lamp : world.getEntitiesOfClass(ReleasedLavaLamp.class, new AxisAlignedBB(source).inflate(2), entity -> !entity.removed)) {
            for (int tick = 0; tick < 100 && !lamp.removed; tick++) lamp.tick();
            require(lamp.removed, "Mined wall lamp failed to land");
            world.removeEntity(lamp, false);
        }
    }
    private static List<ItemEntity> drops(ServerWorld world, BlockPos source) {
        return world.getEntitiesOfClass(ItemEntity.class, new AxisAlignedBB(source).inflate(6), entity -> !entity.removed);
    }
    private static void clearDrops(ServerWorld world, BlockPos source) { drops(world, source).forEach(entity -> world.removeEntity(entity, false)); }
    private static void require(boolean passed, String message) { if (!passed) throw new IllegalStateException(message); }
}
