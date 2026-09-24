package zone.moddev.mc.ironagefurniture.registers;

import zone.moddev.mc.ironagefurniture.Ironagefurniture;
import zone.moddev.mc.ironagefurniture.api.entity.Seat;
import zone.moddev.mc.ironagefurniture.api.entity.ThrownLavaLamp;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EntityClassification;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.fml.RegistryObject;

public class entities {
	public static final DeferredRegister<EntityType<?>> REGISTER = DeferredRegister.create(ForgeRegistries.ENTITIES, Ironagefurniture.MODID);
    public static final RegistryObject<EntityType<Seat>> SEAT = register("seat", EntityType.Builder.<Seat>of((type, world) -> new Seat(world), EntityClassification.MISC).sized(0.0F, 0.0F).setCustomClientFactory((spawnEntity, world) -> new Seat(world)));
    public static final RegistryObject<EntityType<ThrownLavaLamp>> THROWN_LAVA_LAMP = register("thrown_lava_lamp",
            EntityType.Builder.<ThrownLavaLamp>of(ThrownLavaLamp::new, EntityClassification.MISC)
                    .sized(0.25F, 0.25F).clientTrackingRange(4).updateInterval(10));

    private static <T extends Entity> RegistryObject<EntityType<T>> register(String name, EntityType.Builder<T> builder)
    {
        return REGISTER.register(name, () -> builder.build(name));
    }
}
