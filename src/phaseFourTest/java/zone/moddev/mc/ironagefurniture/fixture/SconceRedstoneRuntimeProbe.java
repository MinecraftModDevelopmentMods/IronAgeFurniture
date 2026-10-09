package zone.moddev.mc.ironagefurniture.fixture;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.fluid.Fluids;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import zone.moddev.mc.ironagefurniture.api.SconceMetalData;
import zone.moddev.mc.ironagefurniture.api.blocks.lightsource.phasefour.AdditionalSconce;
import zone.moddev.mc.ironagefurniture.api.blocks.lightsource.phasefour.Candle;
import zone.moddev.mc.ironagefurniture.api.enumerations.SconceMetal;
import zone.moddev.mc.ironagefurniture.init.PhaseFourLighting;

/** Changes real redstone blocks and waits for the server's scheduled block ticks. */
final class SconceRedstoneRuntimeProbe {
    private final MinecraftServer server;
    private final ServerWorld world;
    private final List<Fixture> fixtures = new ArrayList<>();
    private int ticks;
    private int loadingTicks;
    private boolean ready;

    private SconceRedstoneRuntimeProbe(MinecraftServer server) {
        this.server = server;
        world = server.getWorld(DimensionType.OVERWORLD);
        for (boolean wall : new boolean[]{false, true}) for (Direction facing : Direction.Plane.HORIZONTAL) {
            for (boolean wet : new boolean[]{false, true}) {
                for (SconceMetal metal : SconceMetal.values()) add(wall, facing, wet, metal, true, 2);
                for (int count = 1; count <= 4; count++) add(wall, facing, wet, SconceMetal.IRON, false, count);
            }
        }
        for (Fixture fixture : fixtures) {
            world.forceChunk(fixture.pos.getX() >> 4, fixture.pos.getZ() >> 4, true);
            world.getChunk(fixture.pos);
            BlockState saved = world.getBlockState(fixture.pos);
            if (saved.getBlock() instanceof AdditionalSconce)
                require(saved == fixture.state(fixture.twin || !fixture.wet), "Saved powered sconce changed", fixture);
            // Keep flowing water inside each cell so neighbouring dry cases stay dry.
            for (Direction side : Direction.values())
                world.setBlockState(fixture.pos.offset(side), Blocks.GLASS.getDefaultState(), 3);
            world.setBlockState(fixture.pos.down(), Blocks.STONE.getDefaultState(), 3);
            world.setBlockState(fixture.pos.offset(fixture.facing.getOpposite()), Blocks.STONE.getDefaultState(), 3);
            power(fixture, false);
            world.setBlockState(fixture.pos, fixture.state(false), 3);
            power(fixture, true);
            require(world.isBlockPowered(fixture.pos), "Test signal did not reach sconce", fixture);
        }
    }

    static void start(MinecraftServer server) {
        try {
            SconceRedstoneRuntimeProbe probe = new SconceRedstoneRuntimeProbe(server);
            MinecraftForge.EVENT_BUS.addListener(probe::tick);
        } catch (Exception failure) {
            server.initiateShutdown(false);
            throw new IllegalStateException("Could not start sconce redstone probe", failure);
        }
    }

    private void add(boolean wall, Direction facing, boolean wet, SconceMetal metal, boolean twin, int count) {
        int index = fixtures.size();
        fixtures.add(new Fixture(new BlockPos(400 + index % 24 * 3, 80, 400 + index / 24 * 3),
                wall, facing, wet, metal, twin, count));
    }

    private void tick(TickEvent.ServerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        try {
            // Newly forced chunks must reach ticking status before timing redstone transitions.
            if (!ready) {
                ready = fixtures.stream().allMatch(fixture -> world.getChunkProvider().canTick(fixture.pos));
                if (!ready) {
                    if (++loadingTicks > 400) throw new IllegalStateException("Sconce fixture chunks never became tickable");
                    return;
                }
            }
            if (++ticks % 8 != 0) return;
            for (Fixture fixture : fixtures) {
                boolean poweredLit = fixture.twin || !fixture.wet;
                if (ticks == 8) {
                    check(fixture, poweredLit, "Redstone did not light waterlogged twin torch");
                    power(fixture, false);
                } else if (ticks == 16) {
                    check(fixture, !fixture.wet, "Waterlogged twin stayed lit after losing power");
                    power(fixture, true);
                    if (fixture.twin) world.setBlockState(fixture.pos, fixture.state(false), 3);
                } else if (ticks == 24) {
                    check(fixture, poweredLit, "Placement in existing signal did not light sconce");
                    if (fixture.twin && !fixture.wet) {
                        BlockState state = world.getBlockState(fixture.pos);
                        require(((AdditionalSconce)state.getBlock()).receiveFluid(world, fixture.pos, state,
                                Fluids.WATER.getStillFluidState(false)), "Powered twin refused water", fixture);
                    }
                } else if (ticks == 32) {
                    if (fixture.twin && !fixture.wet) {
                        require(world.getBlockState(fixture.pos) == fixture.state(true).with(Candle.WATERLOGGED, true),
                                "Adding water extinguished a powered twin", fixture);
                    } else check(fixture, poweredLit, "Powered state changed without a signal change");
                    power(fixture, false);
                    world.setBlockState(fixture.pos, fixture.state(false), 3);
                    power(fixture, true);
                    power(fixture, false);
                } else if (ticks == 40) {
                    check(fixture, false, "Expired redstone pulse relit sconce");
                    power(fixture, true);
                } else if (ticks == 48) {
                    check(fixture, poweredLit, "Repeated signal did not light sconce");
                }
            }
            if (ticks == 48) {
                Files.write(Paths.get("phase-four-pass.properties"), ("status=PASS\nsconce_redstone_cases="
                        + fixtures.size() + "\nsignal_stages=6\n").getBytes(StandardCharsets.UTF_8));
                server.initiateShutdown(false);
            }
        } catch (Exception failure) {
            server.initiateShutdown(false);
            throw new IllegalStateException("Sconce scheduled-redstone probe failed", failure);
        }
    }

    private void power(Fixture fixture, boolean powered) {
        world.setBlockState(fixture.pos.offset(fixture.facing.rotateY()),
                (powered ? Blocks.REDSTONE_BLOCK : Blocks.GLASS).getDefaultState(), 3);
    }

    private void check(Fixture fixture, boolean lit, String message) {
        BlockState state = world.getBlockState(fixture.pos);
        require(state == fixture.state(lit), message + "; actual=" + state, fixture);
        require(state.getLightValue(world, fixture.pos) == (lit ? fixture.twin ? 15 : 11 + fixture.count : 0),
                "Wrong emitted light level", fixture);
    }

    private static void require(boolean passed, String message, Fixture fixture) {
        if (!passed) throw new IllegalStateException(message + " at " + fixture.pos + ", " + fixture.state(false));
    }

    private static final class Fixture {
        private final BlockPos pos;
        private final boolean wall, wet, twin;
        private final Direction facing;
        private final SconceMetal metal;
        private final int count;
        private Fixture(BlockPos pos, boolean wall, Direction facing, boolean wet, SconceMetal metal, boolean twin, int count) {
            this.pos = pos; this.wall = wall; this.facing = facing; this.wet = wet;
            this.metal = metal; this.twin = twin; this.count = count;
        }
        private BlockState state(boolean lit) {
            return PhaseFourLighting.sconce(wall, lit, twin, count).getDefaultState()
                    .with(Candle.DIRECTION, facing).with(Candle.WATERLOGGED, wet).with(SconceMetalData.METAL, metal);
        }
    }
}
