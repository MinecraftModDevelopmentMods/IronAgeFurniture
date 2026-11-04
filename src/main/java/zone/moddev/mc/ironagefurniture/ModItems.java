package zone.moddev.mc.ironagefurniture;

import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraft.core.Registry;
import net.minecraftforge.fmllegacy.RegistryObject;

import java.util.function.Supplier;


public class ModItems
{
    public static final DeferredRegister<Item> REGISTER = DeferredRegister.create(net.minecraftforge.registries.ForgeRegistries.ITEMS, Ironagefurniture.MODID);

    private static RegistryObject<Item> register(String name, Supplier<Item> item)
    {
        return REGISTER.register(name, item);
    }
}
