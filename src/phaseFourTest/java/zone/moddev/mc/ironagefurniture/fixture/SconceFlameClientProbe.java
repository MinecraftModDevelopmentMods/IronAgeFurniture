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
            factories = (Map<ResourceLocation, IParticleFactory<?>>)field.get(game.particleEngine);
            break;
        }
        if (factories == null) throw new IllegalStateException("Client particle factories unavailable");
        IParticleFactory<BasicParticleType> flame = (IParticleFactory<BasicParticleType>)factories.get(ParticleTypes.FLAME.getRegistryName());
        IParticleFactory<BasicParticleType> smoke = (IParticleFactory<BasicParticleType>)factories.get(ParticleTypes.SMOKE.getRegistryName());
        if (flame == null || smoke == null) throw new IllegalStateException("Torch particle factories missing");
        List<Vec3d> flames = new ArrayList<>(), smokes = new ArrayList<>();
        ParticleStatus previous = game.options.particles;
        game.options.particles = ParticleStatus.ALL;
        game.particleEngine.register(ParticleTypes.FLAME, (data, world, x, y, z, dx, dy, dz) -> {
            flames.add(new Vec3d(x, y, z));
            return flame.createParticle(data, world, x, y, z, dx, dy, dz);
        });
        game.particleEngine.register(ParticleTypes.SMOKE, (data, world, x, y, z, dx, dy, dz) -> {
            smokes.add(new Vec3d(x, y, z));
            return smoke.createParticle(data, world, x, y, z, dx, dy, dz);
        });
        int cases = 0;
        try {
            BlockPos pos = game.player.getCommandSenderBlockPosition();
            for (boolean wall : new boolean[]{false, true}) for (boolean lit : new boolean[]{false, true})
                for (boolean wet : new boolean[]{false, true}) for (Direction facing : Direction.Plane.HORIZONTAL)
                    for (SconceMetal metal : SconceMetal.values()) {
                        AdditionalSconce sconce = (AdditionalSconce)PhaseFourLighting.sconce(wall, lit, true, 2);
                        BlockState state = sconce.defaultBlockState().setValue(Candle.WATERLOGGED, wet)
                                .setValue(Candle.DIRECTION, facing).setValue(SconceMetalData.METAL, metal);
                        flames.clear(); smokes.clear();
                        sconce.animateTick(state, game.level, pos, new Random(0));
                        if (flames.size() != (lit ? 2 : 0) || smokes.size() != (lit ? 2 : 0))
                            throw new IllegalStateException("Wrong twin-torch particles: " + state);
                        for (int index = 0; index < flames.size(); index++) {
                            Vec3d expected = sconce.getFlameOffset(state, index).add(new Vec3d(pos));
                            if (flames.get(index).distanceTo(expected) > .000001
                                    || smokes.get(index).distanceTo(expected.add(0, .04, 0)) > .000001)
                                throw new IllegalStateException("Particles left their torch wicks: " + state);
                        }
                        cases++;
                    }
            return cases;
        } finally {
            game.particleEngine.register(ParticleTypes.FLAME, flame);
            game.particleEngine.register(ParticleTypes.SMOKE, smoke);
            game.options.particles = previous;
        }
    }
}
