package com.mcmoddev.ironagefurniture;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import com.mcmoddev.ironagefurniture.api.blocks.furniture.Chair;

import java.util.function.Supplier;

public class ModVanillaChairs {
	public static final DeferredRegister<Block> REGISTER = DeferredRegister.create(ForgeRegistries.BLOCKS, Ironagefurniture.MODID);
		
	public static RegistryObject<Block> chair_wood_ironage_classic_oak = register("chair_wood_ironage_classic_oak", () -> new Chair(1, 10, SoundType.WOOD, "chair_wood_ironage_classic_oak"));
	public static RegistryObject<Block> chair_wood_ironage_classic_acacia = register("chair_wood_ironage_classic_acacia", () -> new Chair(1, 10, SoundType.WOOD, "chair_wood_ironage_classic_acacia"));
	public static RegistryObject<Block> chair_wood_ironage_classic_dark_oak = register("chair_wood_ironage_classic_dark_oak", () -> new Chair(1, 10, SoundType.WOOD, "chair_wood_ironage_classic_dark_oak"));
	public static RegistryObject<Block> chair_wood_ironage_classic_birch = register("chair_wood_ironage_classic_birch", () -> new Chair(1, 10, SoundType.WOOD, "chair_wood_ironage_classic_birch"));
	public static RegistryObject<Block> chair_wood_ironage_classic_jungle = register("chair_wood_ironage_classic_jungle", () -> new Chair(1, 10, SoundType.WOOD, "chair_wood_ironage_classic_jungle"));
	public static RegistryObject<Block> chair_wood_ironage_classic_spruce = register("chair_wood_ironage_classic_spruce", () -> new Chair(1, 10, SoundType.WOOD, "chair_wood_ironage_classic_spruce"));
	
	 private static RegistryObject<Block> register(String name, Supplier<Block> block)
	    {
	        return register(name, block, new Item.Properties());
	    }

	    private static RegistryObject<Block> register(String name, Supplier<Block> block, Item.Properties properties)
	    {
	        RegistryObject<Block> registryObject = REGISTER.register(name, block);
	        ModItems.REGISTER.register(name, () -> new BlockItem(registryObject.get(), properties));
	        return registryObject;
	    }

//	    private static RegistryObject<Block> register(String name, Supplier<Block> block, @Nullable Function<RegistryObject<Block>, BlockItem> function)
//	    {
//	        RegistryObject<Block> registryObject = REGISTER.register(name, block);
//	        if(function != null)
//	        {
//	            ModItems.REGISTER.register(name, () -> function.apply(registryObject));
//	        }
//	        return registryObject;
//	    }
//
//	    private static RegistryObject<Block> registerNoItem(String name, Supplier<Block> block)
//	    {
//	        return REGISTER.register(name, block);
//	    }
}
