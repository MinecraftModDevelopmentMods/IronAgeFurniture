package com.mcmoddev.ironagefurniture.api.entity;

import com.mcmoddev.ironagefurniture.Ironagefurniture;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(modid = Ironagefurniture.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Entities
{
    private static final List<EntityType> ENTITY_TYPES = new ArrayList<>();

    public static final EntityType<Seat> SEAT = buildType("froggychair:seat", EntityType.Builder.<Seat>of((type, world) -> new Seat(world), EntityClassification.MISC).sized(0.0F, 0.0F).setCustomClientFactory((spawnEntity, world) -> new Seat(world)));

    private static <T extends Entity> EntityType<T> buildType(String id, EntityType.Builder<T> builder)
    {
        EntityType<T> type = builder.build(id);
        type.setRegistryName(id);
        ENTITY_TYPES.add(type);
        return type;
    }

    @SubscribeEvent
    @SuppressWarnings("unused")
    public static void registerTypes(final RegistryEvent.Register<EntityType<?>> event)
    {
        ENTITY_TYPES.forEach(type -> event.getRegistry().register(type));
        ENTITY_TYPES.clear();
    }
}