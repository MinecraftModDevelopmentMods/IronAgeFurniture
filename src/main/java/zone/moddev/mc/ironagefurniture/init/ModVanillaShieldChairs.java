package zone.moddev.mc.ironagefurniture.init;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import zone.moddev.mc.ironagefurniture.Ironagefurniture;
import zone.moddev.mc.ironagefurniture.ModItems;
import zone.moddev.mc.ironagefurniture.api.blocks.furniture.Chair;
import zone.moddev.mc.ironagefurniture.api.blocks.furniture.ShieldChair;
import zone.moddev.mc.ironagefurniture.api.blocks.furniture.ChairNether;

import java.util.function.Supplier;

public class ModVanillaShieldChairs {
	public static final DeferredRegister<Block> REGISTER = DeferredRegister.create(ForgeRegistries.BLOCKS, Ironagefurniture.MODID);
	
	private static RegistryObject<Block> register(String name, Supplier<Block> block) {
		return register(name, block, new Item.Properties()); 
	}
	
	private static RegistryObject<Block> register(String name, Supplier<Block> block, Item.Properties properties) {
		RegistryObject<Block> registryObject = REGISTER.register(name, block);
		
		ModItems.REGISTER.register(name, () -> new BlockItem(registryObject.get(), RegistrationProperties.item(properties, name).useBlockDescriptionPrefix()));
		
		return registryObject; 
	}
	
	public static RegistryObject<Block> chair_wood_ironage_shield_oak = register("chair_wood_ironage_shield_oak", () -> new ShieldChair(1, 10, SoundType.WOOD, "chair_wood_ironage_shield_oak"));
	public static RegistryObject<Block> chair_wood_ironage_shield_spruce = register("chair_wood_ironage_shield_spruce", () -> new ShieldChair(1, 10, SoundType.WOOD, "chair_wood_ironage_shield_spruce"));
	public static RegistryObject<Block> chair_wood_ironage_shield_birch = register("chair_wood_ironage_shield_birch", () -> new ShieldChair(1, 10, SoundType.WOOD, "chair_wood_ironage_shield_birch"));
	public static RegistryObject<Block> chair_wood_ironage_shield_jungle = register("chair_wood_ironage_shield_jungle", () -> new ShieldChair(1, 10, SoundType.WOOD, "chair_wood_ironage_shield_jungle"));
	public static RegistryObject<Block> chair_wood_ironage_shield_acacia = register("chair_wood_ironage_shield_acacia", () -> new ShieldChair(1, 10, SoundType.WOOD, "chair_wood_ironage_shield_acacia"));
	public static RegistryObject<Block> chair_wood_ironage_shield_dark_oak = register("chair_wood_ironage_shield_dark_oak", () -> new ShieldChair(1, 10, SoundType.WOOD, "chair_wood_ironage_shield_dark_oak"));
	public static RegistryObject<Block> chair_wood_ironage_shield_mangrove = register("chair_wood_ironage_shield_mangrove", () -> new ShieldChair(1, 10, SoundType.WOOD, "chair_wood_ironage_shield_mangrove"));
	public static RegistryObject<Block> chair_wood_ironage_shield_cherry = register("chair_wood_ironage_shield_cherry", () -> new ShieldChair(1, 10, SoundType.WOOD, "chair_wood_ironage_shield_cherry"));
	public static RegistryObject<Block> chair_wood_ironage_shield_bamboo = register("chair_wood_ironage_shield_bamboo", () -> new ShieldChair(1, 10, SoundType.WOOD, "chair_wood_ironage_shield_bamboo"));
	public static RegistryObject<Block> chair_wood_ironage_shield_pale_oak = register("chair_wood_ironage_shield_pale_oak", () -> new ShieldChair(1, 10, SoundType.WOOD, "chair_wood_ironage_shield_pale_oak"));
	public static RegistryObject<Block> chair_wood_ironage_shield_crimson = register("chair_wood_ironage_shield_crimson", () -> new ShieldChair(1, 10, SoundType.WOOD, "chair_wood_ironage_shield_crimson"));
	public static RegistryObject<Block> chair_wood_ironage_shield_warped = register("chair_wood_ironage_shield_warped", () -> new ShieldChair(1, 10, SoundType.WOOD, "chair_wood_ironage_shield_warped"));
}
