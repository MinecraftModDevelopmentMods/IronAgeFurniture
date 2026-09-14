package zone.moddev.mc.ironagefurniture.api.entity;

import zone.moddev.mc.ironagefurniture.Ironagefurniture;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class Entities
{
	public static final DeferredRegister<EntityType<?>> REGISTER = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, Ironagefurniture.MODID);

    public static final RegistryObject<EntityType<Seat>> SEAT = register("seat", EntityType.Builder.<Seat>of((type, world) -> new Seat(world), MobCategory.MISC).sized(0.0F, 0.0F).setCustomClientFactory((spawnEntity, world) -> new Seat(world)));
	public static final RegistryObject<EntityType<ThrownLavaLamp>> THROWN_LAVA_LAMP = register("thrown_lava_lamp",
		EntityType.Builder.<ThrownLavaLamp>of(ThrownLavaLamp::new, MobCategory.MISC)
			.sized(0.25F, 0.25F).clientTrackingRange(4).updateInterval(10));

    private static <T extends Entity> RegistryObject<EntityType<T>> register(String name, EntityType.Builder<T> builder)
    {
        return REGISTER.register(name, () -> builder.build(name));
    }
}
