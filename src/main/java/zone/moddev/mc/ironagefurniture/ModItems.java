package zone.moddev.mc.ironagefurniture;

import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;


public class ModItems
{
    public static final DeferredRegister<Item> REGISTER = DeferredRegister.create(net.minecraft.core.registries.Registries.ITEM, Ironagefurniture.MODID);

    private static RegistryObject<Item> register(String name, Supplier<Item> item)
    {
        return REGISTER.register(name, item);
    }
}