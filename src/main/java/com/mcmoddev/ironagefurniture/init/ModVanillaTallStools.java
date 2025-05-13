package com.mcmoddev.ironagefurniture.init;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import com.mcmoddev.ironagefurniture.Ironagefurniture;
import com.mcmoddev.ironagefurniture.ModItems;
import com.mcmoddev.ironagefurniture.api.blocks.furniture.Chair;
import com.mcmoddev.ironagefurniture.api.blocks.furniture.Stool;
import com.mcmoddev.ironagefurniture.api.blocks.furniture.TallStool;
import java.util.function.Supplier;

public class ModVanillaTallStools {
	public static final DeferredRegister<Block> REGISTER = DeferredRegister.create(ForgeRegistries.BLOCKS, Ironagefurniture.MODID);
	
	private static RegistryObject<Block> register(String name, Supplier<Block> block) {
		return register(name, block, new Item.Properties()); 
	}
	
	private static RegistryObject<Block> register(String name, Supplier<Block> block, Item.Properties properties) {
		RegistryObject<Block> registryObject = REGISTER.register(name, block);
		
		ModItems.REGISTER.register(name, () -> new BlockItem(registryObject.get(), properties));
		
		return registryObject; 
	}
	
	public static RegistryObject<Block> chair_wood_ironage_stool_tall_acacia = register("chair_wood_ironage_stool_tall_acacia", () -> new TallStool(1, 10, SoundType.WOOD, "chair_wood_ironage_stool_tall_acacia"));
	public static RegistryObject<Block> chair_wood_ironage_stool_tall_dark_oak = register("chair_wood_ironage_stool_tall_dark_oak", () -> new TallStool(1, 10, SoundType.WOOD, "chair_wood_ironage_stool_tall_dark_oak"));
	public static RegistryObject<Block> chair_wood_ironage_stool_tall_birch = register("chair_wood_ironage_stool_tall_birch", () -> new TallStool(1, 10, SoundType.WOOD, "chair_wood_ironage_stool_tall_birch"));
	public static RegistryObject<Block> chair_wood_ironage_stool_tall_jungle = register("chair_wood_ironage_stool_tall_jungle", () -> new TallStool(1, 10, SoundType.WOOD, "chair_wood_ironage_stool_tall_jungle"));
	public static RegistryObject<Block> chair_wood_ironage_stool_tall_oak = register("chair_wood_ironage_stool_tall_oak", () -> new TallStool(1, 10, SoundType.WOOD, "chair_wood_ironage_stool_tall_oak"));
	public static RegistryObject<Block> chair_wood_ironage_stool_tall_spruce = register("chair_wood_ironage_stool_tall_spruce", () -> new TallStool(1, 10, SoundType.WOOD, "chair_wood_ironage_stool_tall_spruce"));
}
