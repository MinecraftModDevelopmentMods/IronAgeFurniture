package zone.moddev.mc.ironagefurniture.fixture;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.client.settings.ParticleStatus;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
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
        Map<ResourceLocation, IParticleFactory<?>> factories = null;
        for (Field field : ParticleManager.class.getDeclaredFields()) {
            if (!field.getGenericType().getTypeName().contains("IParticleFactory")) continue;
            field.setAccessible(true);
            factories = (Map<ResourceLocation, IParticleFactory<?>>)field.get(game.particles);
            break;
        }
        if (factories == null) throw new IllegalStateException("Client particle factories unavailable");
        IParticleFactory<BasicParticleType> flame = (IParticleFactory<BasicParticleType>)factories.get(ParticleTypes.FLAME.getRegistryName());
        IParticleFactory<BasicParticleType> candleFlame = (IParticleFactory<BasicParticleType>)factories.get(PhaseFourLighting.CANDLE_FLAME.getRegistryName());
        IParticleFactory<BasicParticleType> smoke = (IParticleFactory<BasicParticleType>)factories.get(ParticleTypes.SMOKE.getRegistryName());
        if (flame == null || candleFlame == null || smoke == null) throw new IllegalStateException("Sconce particle factories missing");
        List<Vec3d> flames = new ArrayList<>(), smokes = new ArrayList<>();
        ParticleStatus previous = game.gameSettings.particles;
        game.gameSettings.particles = ParticleStatus.ALL;
        game.particles.registerFactory(ParticleTypes.FLAME, (data, world, x, y, z, dx, dy, dz) -> {
            flames.add(new Vec3d(x, y, z));
            return flame.makeParticle(data, world, x, y, z, dx, dy, dz);
        });
        game.particles.registerFactory(ParticleTypes.SMOKE, (data, world, x, y, z, dx, dy, dz) -> {
            smokes.add(new Vec3d(x, y, z));
            return smoke.makeParticle(data, world, x, y, z, dx, dy, dz);
        });
        game.particles.registerFactory(PhaseFourLighting.CANDLE_FLAME, (data, world, x, y, z, dx, dy, dz) -> {
            flames.add(new Vec3d(x, y, z));
            return candleFlame.makeParticle(data, world, x, y, z, dx, dy, dz);
        });
        int cases = 0;
        try {
            BlockPos pos = game.player.getPosition();
            for (boolean wall : new boolean[]{false, true}) for (boolean lit : new boolean[]{false, true})
                for (boolean wet : new boolean[]{false, true}) for (Direction facing : Direction.Plane.HORIZONTAL)
                    for (SconceMetal metal : SconceMetal.values()) for (boolean twin : new boolean[]{false, true})
                        for (int count = twin ? 2 : 1; count <= (twin ? 2 : 4); count++) {
                        AdditionalSconce sconce = (AdditionalSconce)PhaseFourLighting.sconce(wall, lit, twin, count);
                        BlockState state = SconceMetalData.withMetal(sconce.getDefaultState().with(Candle.WATERLOGGED, wet)
                                .with(Candle.DIRECTION, facing), metal);
                        flames.clear(); smokes.clear();
                        sconce.animateTick(state, game.world, pos, new Random(0));
                        List<Vec3d> expectedSmoke = new ArrayList<>();
                        Random smokeRandom = new Random(0);
                        if (lit) for (int index = 0; index < count; index++)
                            if (twin || smokeRandom.nextInt(3) == 0)
                                expectedSmoke.add(sconce.getFlameOffset(state, index).add(new Vec3d(pos)).add(0, .04, 0));
                        if (flames.size() != (lit ? count : 0) || smokes.size() != expectedSmoke.size())
                            throw new IllegalStateException("Wrong sconce particles: " + state);
                        for (int index = 0; index < flames.size(); index++) {
                            Vec3d expected = sconce.getFlameOffset(state, index).add(new Vec3d(pos));
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
            game.particles.registerFactory(ParticleTypes.FLAME, flame);
            game.particles.registerFactory(PhaseFourLighting.CANDLE_FLAME, candleFlame);
            game.particles.registerFactory(ParticleTypes.SMOKE, smoke);
            game.gameSettings.particles = previous;
        }
    }
}
