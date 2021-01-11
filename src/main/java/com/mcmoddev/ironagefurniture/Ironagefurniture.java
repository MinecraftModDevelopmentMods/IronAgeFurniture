package com.mcmoddev.ironagefurniture;

import java.util.HashMap;
import java.util.Map;

import com.mcmoddev.ironagefurniture.api.entity.Seat;
import com.mcmoddev.ironagefurniture.init.BlockInitialiser;
import com.mcmoddev.ironagefurniture.init.ItemInitialiser;
import com.mcmoddev.ironagefurniture.lib.util.MMDCreativeTab;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.EntityRegistry;

@Mod(modid = Ironagefurniture.MODID, version = Ironagefurniture.VERSION)
public class Ironagefurniture
{
    public static final String MODID = "ironagefurniture";
    public static final String VERSION = "0.2.0";
    
	public static final Map<String,Block> BlockRegistry = new HashMap<String, Block>();
	public static final Map<String,Item> ItemRegistry = new HashMap<String, Item>();
    
    public static MMDCreativeTab ironagefurnitureTab = new MMDCreativeTab("IronAgeFurniture", true);
	
    
    
    @EventHandler
    public void init(FMLInitializationEvent event)
    {
    	// register renderers
    	if(event.getSide().isClient()) {
    		ItemInitialiser.RegisterItemRenders();
    	}
    	
    	EntityRegistry.registerModEntity(new ResourceLocation(MODID + ":seat"), Seat.class , MODID + ":seat", 0, this, 80, 1, false);
    	
    	ironagefurnitureTab.setIconItem(Blocks.BOOKSHELF);
    }
    
    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
    	IronAgeFurnitureConfiguration.init(event);
    	BlockInitialiser.init();
    	
    }
}
