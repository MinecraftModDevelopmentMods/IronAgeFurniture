package zone.moddev.mc.ironagefurniture.api.entity;

import zone.moddev.mc.ironagefurniture.Ironagefurniture;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EntityClassification;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.fml.RegistryObject;

@Mod.EventBusSubscriber(modid = Ironagefurniture.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Entities
{
	public static final DeferredRegister<EntityType<?>> REGISTER = DeferredRegister.create(ForgeRegistries.ENTITIES, Ironagefurniture.MODID);

    public static final RegistryObject<EntityType<Seat>> SEAT = register("seat", EntityType.Builder.<Seat>of((type, world) -> new Seat(world), EntityClassification.MISC).sized(0.0F, 0.0F).setCustomClientFactory((spawnEntity, world) -> new Seat(world)));

    private static <T extends Entity> RegistryObject<EntityType<T>> register(String name, EntityType.Builder<T> builder)
    {
        return REGISTER.register(name, () -> builder.build(name));
    }
}