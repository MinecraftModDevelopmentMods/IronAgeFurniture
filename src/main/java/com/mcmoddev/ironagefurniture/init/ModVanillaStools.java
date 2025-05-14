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
import com.mcmoddev.ironagefurniture.api.blocks.furniture.Stool;
import java.util.function.Supplier;

public class ModVanillaStools {
	public static final DeferredRegister<Block> REGISTER = DeferredRegister.create(ForgeRegistries.BLOCKS, Ironagefurniture.MODID);
	
	private static RegistryObject<Block> register(String name, Supplier<Block> block) {
		return register(name, block, new Item.Properties()); 
	}
	
	private static RegistryObject<Block> register(String name, Supplier<Block> block, Item.Properties properties) {
		RegistryObject<Block> registryObject = REGISTER.register(name, block);
		
		ModItems.REGISTER.register(name, () -> new BlockItem(registryObject.get(), properties));
		
		return registryObject; 
	}
	
	public static RegistryObject<Block> chair_wood_ironage_stool_short_oak = register("chair_wood_ironage_stool_short_oak", () -> new Stool(1, 10, SoundType.WOOD, "chair_wood_ironage_stool_short_oak"));
	public static RegistryObject<Block> chair_wood_ironage_stool_short_acacia = register("chair_wood_ironage_stool_short_acacia", () -> new Stool(1, 10, SoundType.WOOD, "chair_wood_ironage_stool_short_acacia"));
	public static RegistryObject<Block> chair_wood_ironage_stool_short_dark_oak = register("chair_wood_ironage_stool_short_dark_oak", () -> new Stool(1, 10, SoundType.WOOD, "chair_wood_ironage_stool_short_dark_oak"));
	public static RegistryObject<Block> chair_wood_ironage_stool_short_birch = register("chair_wood_ironage_stool_short_birch", () -> new Stool(1, 10, SoundType.WOOD, "chair_wood_ironage_stool_short_birch"));
	public static RegistryObject<Block> chair_wood_ironage_stool_short_jungle = register("chair_wood_ironage_stool_short_jungle", () -> new Stool(1, 10, SoundType.WOOD, "chair_wood_ironage_stool_short_jungle"));
	public static RegistryObject<Block> chair_wood_ironage_stool_short_spruce = register("chair_wood_ironage_stool_short_spruce", () -> new Stool(1, 10, SoundType.WOOD, "chair_wood_ironage_stool_short_spruce"));
}
