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

public class ModVanillaShieldChairs {
	public static final DeferredRegister<Block> REGISTER = DeferredRegister.create(ForgeRegistries.BLOCKS, Ironagefurniture.MODID);
	
	private static RegistryObject<Block> register(String name, Supplier<Block> block) {
		return register(name, block, new Item.Properties()); 
	}
	
	private static RegistryObject<Block> register(String name, Supplier<Block> block, Item.Properties properties) {
		RegistryObject<Block> registryObject = REGISTER.register(name, block);
		
		ModItems.REGISTER.register(name, () -> new BlockItem(registryObject.get(), properties));
		
		return registryObject; 
	}
	
	public static RegistryObject<Block> chair_wood_ironage_shield_oak = register("chair_wood_ironage_shield_oak", () -> new Chair(1, 10, SoundType.WOOD, "chair_wood_ironage_shield_oak"));
	public static RegistryObject<Block> chair_wood_ironage_shield_acacia = register("chair_wood_ironage_shield_acacia", () -> new Chair(1, 10, SoundType.WOOD, "chair_wood_ironage_shield_acacia"));
	public static RegistryObject<Block> chair_wood_ironage_shield_dark_oak = register("chair_wood_ironage_shield_dark_oak", () -> new Chair(1, 10, SoundType.WOOD, "chair_wood_ironage_shield_dark_oak"));
	public static RegistryObject<Block> chair_wood_ironage_shield_birch = register("chair_wood_ironage_shield_birch", () -> new Chair(1, 10, SoundType.WOOD, "chair_wood_ironage_shield_birch"));
	public static RegistryObject<Block> chair_wood_ironage_shield_jungle = register("chair_wood_ironage_shield_jungle", () -> new Chair(1, 10, SoundType.WOOD, "chair_wood_ironage_shield_jungle"));
	public static RegistryObject<Block> chair_wood_ironage_shield_spruce = register("chair_wood_ironage_shield_spruce", () -> new Chair(1, 10, SoundType.WOOD, "chair_wood_ironage_shield_spruce"));
}
