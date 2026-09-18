package com.mcmoddev.ironagefurniture.init;

import com.mcmoddev.ironagefurniture.Ironagefurniture;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ItemInitialiser {
	public static Item RegisterItem(Item item, String name) {
		GameRegistry.register(item.setRegistryName(Ironagefurniture.MODID, name));
		Ironagefurniture.ItemRegistry.put(name, item);
		item.setUnlocalizedName(Ironagefurniture.MODID + "." + name);
		return item;
	}
	
	public static void RegisterItemRenders() {

		for(String name : Ironagefurniture.ItemRegistry.keySet()){
			Item i = Ironagefurniture.ItemRegistry.get(name);
    		Minecraft.getMinecraft().getRenderItem().getItemModelMesher()
    				.register(i, 0, new ModelResourceLocation(Ironagefurniture.MODID + ":" + name, "inventory"));
    	}
    }
}
