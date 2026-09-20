package zone.moddev.mc.ironagefurniture.init;

import zone.moddev.mc.ironagefurniture.Ironagefurniture;
import zone.moddev.mc.ironagefurniture.api.Enumerations.PaddedBenchColour;
import zone.moddev.mc.ironagefurniture.api.Items.ItemBlockPaddedBench;

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
            ModelResourceLocation model = new ModelResourceLocation(
                    Ironagefurniture.MODID + ":" + name, "inventory");
            if (item instanceof ItemBlockPaddedBench) {
                for (PaddedBenchColour colour : PaddedBenchColour.values()) {
                    Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(
                            item, colour.getItemMetadata(), model);
                }
            } else {
                Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(item, 0, model);
            }
        }
    }
}
