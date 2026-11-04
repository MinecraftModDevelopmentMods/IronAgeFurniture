package zone.moddev.mc.ironagefurniture.fixture;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.ParticleStatus;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;
import zone.moddev.mc.ironagefurniture.api.SconceMetalData;
import zone.moddev.mc.ironagefurniture.api.blocks.lightsource.phasefour.AdditionalSconce;
import zone.moddev.mc.ironagefurniture.api.blocks.lightsource.phasefour.Candle;
import zone.moddev.mc.ironagefurniture.api.enumerations.SconceMetal;
import zone.moddev.mc.ironagefurniture.init.PhaseFourLighting;

/** Observes real client particle factories without replacing their rendered particles. */
final class SconceFlameClientProbe {
    private SconceFlameClientProbe() { }

    @SuppressWarnings("unchecked")
    static int run(Minecraft game) throws ReflectiveOperationException {
        Map<ResourceLocation, ParticleProvider<?>> factories = null;
        for (Field field : ParticleEngine.class.getDeclaredFields()) {
            if (!field.getGenericType().getTypeName().contains("ParticleProvider")) continue;
            field.setAccessible(true);
            factories = (Map<ResourceLocation, ParticleProvider<?>>)field.get(game.particleEngine);
            break;
        }
        if (factories == null) throw new IllegalStateException("Client particle factories unavailable");
        ParticleProvider<SimpleParticleType> flame = (ParticleProvider<SimpleParticleType>)factories.get(ParticleTypes.FLAME.getRegistryName());
        ParticleProvider<SimpleParticleType> candleFlame = (ParticleProvider<SimpleParticleType>)factories.get(PhaseFourLighting.CANDLE_FLAME.getRegistryName());
        ParticleProvider<SimpleParticleType> smoke = (ParticleProvider<SimpleParticleType>)factories.get(ParticleTypes.SMOKE.getRegistryName());
        if (flame == null || candleFlame == null || smoke == null) throw new IllegalStateException("Sconce particle factories missing");
        List<Vec3> flames = new ArrayList<>(), smokes = new ArrayList<>();
        ParticleStatus previous = game.options.particles;
        game.options.particles = ParticleStatus.ALL;
        game.particleEngine.register(ParticleTypes.FLAME, (data, world, x, y, z, dx, dy, dz) -> {
            flames.add(new Vec3(x, y, z));
            return flame.createParticle(data, world, x, y, z, dx, dy, dz);
        });
        game.particleEngine.register(PhaseFourLighting.CANDLE_FLAME, (data, world, x, y, z, dx, dy, dz) -> {
            flames.add(new Vec3(x, y, z));
            return candleFlame.createParticle(data, world, x, y, z, dx, dy, dz);
        });
        game.particleEngine.register(ParticleTypes.SMOKE, (data, world, x, y, z, dx, dy, dz) -> {
            smokes.add(new Vec3(x, y, z));
            return smoke.createParticle(data, world, x, y, z, dx, dy, dz);
        });
        int cases = 0;
        try {
            BlockPos pos = game.player.blockPosition();
            for (boolean wall : new boolean[]{false, true}) for (boolean lit : new boolean[]{false, true})
                for (boolean wet : new boolean[]{false, true}) for (Direction facing : Direction.Plane.HORIZONTAL)
                    for (SconceMetal metal : SconceMetal.values()) for (boolean twin : new boolean[]{false, true})
                        for (int count = twin ? 2 : 1; count <= (twin ? 2 : 4); count++) {
                        AdditionalSconce sconce = (AdditionalSconce)PhaseFourLighting.sconce(wall, lit, twin, count);
                        BlockState state = sconce.defaultBlockState().setValue(Candle.WATERLOGGED, wet)
                                .setValue(Candle.DIRECTION, facing).setValue(SconceMetalData.METAL, metal);
                        flames.clear(); smokes.clear();
                        sconce.animateTick(state, game.level, pos, new Random(0));
                        List<Vec3> expectedSmoke = new ArrayList<>();
                        Random smokeRandom = new Random(0);
                        if (lit) for (int index = 0; index < count; index++)
                            if (twin || smokeRandom.nextInt(3) == 0)
                                expectedSmoke.add(sconce.getFlameOffset(state, index).add(Vec3.atLowerCornerOf(pos)).add(0, .04, 0));
                        if (flames.size() != (lit ? count : 0) || smokes.size() != expectedSmoke.size())
                            throw new IllegalStateException("Wrong sconce particles: " + state);
                        for (int index = 0; index < flames.size(); index++) {
                            Vec3 expected = sconce.getFlameOffset(state, index).add(Vec3.atLowerCornerOf(pos));
                            if (flames.get(index).distanceTo(expected) > .000001)
                                throw new IllegalStateException("Flame left its wick: " + state);
                        }
                        for (int index = 0; index < smokes.size(); index++)
                            if (smokes.get(index).distanceTo(expectedSmoke.get(index)) > .000001)
                                throw new IllegalStateException("Smoke left its wick: " + state);
                        cases++;
                    }
            return cases;
        } finally {
            game.particleEngine.register(ParticleTypes.FLAME, flame);
            game.particleEngine.register(PhaseFourLighting.CANDLE_FLAME, candleFlame);
            game.particleEngine.register(ParticleTypes.SMOKE, smoke);
            game.options.particles = previous;
        }
    }
}
