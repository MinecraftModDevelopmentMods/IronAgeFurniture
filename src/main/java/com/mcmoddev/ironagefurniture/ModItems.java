package com.mcmoddev.ironagefurniture;

import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;


public class ModItems
{
    public static final DeferredRegister<Item> REGISTER = DeferredRegister.create(ForgeRegistries.ITEMS, Ironagefurniture.MODID);

    private static RegistryObject<Item> register(String name, Supplier<Item> item)
    {
        return REGISTER.register(name, item);
    }
}