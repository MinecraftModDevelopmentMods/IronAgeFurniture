package zone.moddev.mc.ironagefurniture;

import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.function.Supplier;


public class ModItems
{
    public static final DeferredRegister<Item> REGISTER = DeferredRegister.create(BuiltInRegistries.ITEM, Ironagefurniture.MODID);

    private static DeferredHolder<Item, Item> register(String name, Supplier<Item> item)
    {
        return REGISTER.register(name, item);
    }
}