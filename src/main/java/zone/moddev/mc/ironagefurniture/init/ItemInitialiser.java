package zone.moddev.mc.ironagefurniture.init;

import zone.moddev.mc.ironagefurniture.Ironagefurniture;
import zone.moddev.mc.ironagefurniture.api.Items.ItemBlockPaddedBench;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ItemInitialiser {
	public static void RegisterItemRenders() {

		for(String name : Ironagefurniture.ItemRegistry.keySet()){
			Item i = Ironagefurniture.ItemRegistry.get(name);
			if (i instanceof ItemBlockPaddedBench) {
				continue;
			}
    		Minecraft.getMinecraft().getRenderItem().getItemModelMesher()
    				.register(i, 0, new ModelResourceLocation(Ironagefurniture.MODID + ":" + name, "inventory"));
    	}
    }
}
