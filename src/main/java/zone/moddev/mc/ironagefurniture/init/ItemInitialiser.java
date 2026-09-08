package zone.moddev.mc.ironagefurniture.init;

import zone.moddev.mc.ironagefurniture.Ironagefurniture;
import zone.moddev.mc.ironagefurniture.IronAgeFurnitureConfiguration;
import zone.moddev.mc.ironagefurniture.ItemObjectHolder;
import zone.moddev.mc.ironagefurniture.api.Items.ItemDrinkware;
import zone.moddev.mc.ironagefurniture.api.Items.ItemFluidBottle;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;

public class ItemInitialiser {

    public static void init() {
        if (IronAgeFurnitureConfiguration.GENERATE_IRON_NUGGETS) {
            ItemObjectHolder.iron_nugget = RegisterItem(
                    new Item().setCreativeTab(Ironagefurniture.ironagefurnitureTab), "iron_nugget");
            OreDictionary.registerOre("nuggetIron", new ItemStack(ItemObjectHolder.iron_nugget));
        }

        if (IronAgeFurnitureConfiguration.GENERATE_LIGHTS && IronAgeFurnitureConfiguration.GENERATE_CANDLES) {
            ItemObjectHolder.tallow = RegisterItem(
                    new Item().setCreativeTab(Ironagefurniture.ironagefurnitureTab), "tallow");
        }

        if (IronAgeFurnitureConfiguration.GENERATE_FLUID_BOTTLES) {
            ItemObjectHolder.fluid_bottle = RegisterItem(new ItemFluidBottle(), "fluid_bottle");
        }

        if (IronAgeFurnitureConfiguration.GENERATE_DRINKWARE) {
            ItemObjectHolder.drinkware = RegisterItem(new ItemDrinkware()
                    .setCreativeTab(Ironagefurniture.ironagefurnitureTab), "drinkware");
        }
    }

    @SideOnly(Side.CLIENT)
    public static void RegisterItemRenders() {
        ClientItemInitialiser.registerItemRenders();
    }

    @SideOnly(Side.CLIENT)
    public static void RegisterItemModels() {
        ClientItemInitialiser.registerItemModels();
    }

    public static Item RegisterItem(Item item, String name) {
        GameRegistry.register(item.setRegistryName(Ironagefurniture.MODID, name));
        Ironagefurniture.ItemRegistry.put(name, item);
        item.setUnlocalizedName(Ironagefurniture.MODID + "." + name);
        return item;
    }
}
