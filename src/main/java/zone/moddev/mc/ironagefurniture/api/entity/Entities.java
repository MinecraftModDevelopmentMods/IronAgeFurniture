package zone.moddev.mc.ironagefurniture.api.entity;

import zone.moddev.mc.ironagefurniture.Ironagefurniture;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.neoforge.registries.DeferredHolder;

public class Entities
{
	public static final DeferredRegister<EntityType<?>> REGISTER = DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, Ironagefurniture.MODID);

    public static final DeferredHolder<EntityType<?>, EntityType<Seat>> SEAT = register("seat", EntityType.Builder.<Seat>of((type, world) -> new Seat(world), MobCategory.MISC).sized(0.0F, 0.0F));
    public static final DeferredHolder<EntityType<?>, EntityType<FallingShieldChair>> FALLING_SHIELD_CHAIR = register("falling_shield_chair",
        EntityType.Builder.<FallingShieldChair>of(FallingShieldChair::new, MobCategory.MISC)
            .sized(0.98F, 0.98F).clientTrackingRange(10).updateInterval(1));
	public static final DeferredHolder<EntityType<?>, EntityType<ThrownLavaLamp>> THROWN_LAVA_LAMP = register("thrown_lava_lamp",
		EntityType.Builder.<ThrownLavaLamp>of(ThrownLavaLamp::new, MobCategory.MISC)
			.sized(0.25F, 0.25F).clientTrackingRange(4).updateInterval(10));
    public static final DeferredHolder<EntityType<?>, EntityType<ReleasedLavaLamp>> RELEASED_LAVA_LAMP = register("released_lava_lamp",
        EntityType.Builder.<ReleasedLavaLamp>of(ReleasedLavaLamp::new, MobCategory.MISC)
            .sized(.25F, .375F).clientTrackingRange(10).updateInterval(1));

    private static <T extends Entity> DeferredHolder<EntityType<?>, EntityType<T>> register(String name, EntityType.Builder<T> builder)
    {
        return REGISTER.register(name, () -> builder.build(name));
    }
}
