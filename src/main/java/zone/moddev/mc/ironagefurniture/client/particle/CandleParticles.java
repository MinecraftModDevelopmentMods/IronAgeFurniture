package zone.moddev.mc.ironagefurniture.client.particle;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.FlameParticle;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import zone.moddev.mc.ironagefurniture.Ironagefurniture;
import zone.moddev.mc.ironagefurniture.init.PhaseFourLighting;

/** Vanilla animated flame sprites, scaled to the candle rather than a torch. */
@Mod.EventBusSubscriber(modid = Ironagefurniture.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class CandleParticles {
    private CandleParticles() { }
    @SubscribeEvent public static void register(ParticleFactoryRegisterEvent event) {
        Minecraft.getInstance().particles.registerFactory(PhaseFourLighting.CANDLE_FLAME, sprites -> {
            FlameParticle.Factory flame = new FlameParticle.Factory(sprites);
            return (type, world, x, y, z, dx, dy, dz) ->
                    flame.makeParticle(type, world, x, y, z, dx, dy, dz).multipleParticleScaleBy(.45F);
        });
    }
}
