package zone.moddev.mc.ironagefurniture.init;

import zone.moddev.mc.ironagefurniture.Ironagefurniture;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/** Client-only item model registration for legacy Forge. */
@SideOnly(Side.CLIENT)
public final class ClientItemInitialiser {

    private ClientItemInitialiser() {
        throw new IllegalAccessError("This class cannot be instantiated");
    }

    public static void registerItemRenders() {
        for (String name : Ironagefurniture.ItemRegistry.keySet()) {
            Item item = Ironagefurniture.ItemRegistry.get(name);
            Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(item, 0,
                    new ModelResourceLocation(Ironagefurniture.MODID + ":" + name, "inventory"));
        }
    }
}
