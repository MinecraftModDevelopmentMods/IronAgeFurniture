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
import com.mcmoddev.ironagefurniture.api.blocks.lightholder.LightHolderSconceFloor;
import com.mcmoddev.ironagefurniture.api.blocks.lightholder.LightHolderSconceWall;
import com.mcmoddev.ironagefurniture.api.blocks.lightsource.glow.LightSourceGlowdust;
import com.mcmoddev.ironagefurniture.api.blocks.lightsource.glow.LightSourceSconceGlowFloor;
import com.mcmoddev.ironagefurniture.api.blocks.lightsource.glow.LightSourceSconceGlowWall;
import com.mcmoddev.ironagefurniture.api.blocks.lightsource.lava.LightSourceLava;
import com.mcmoddev.ironagefurniture.api.blocks.lightsource.lava.LightSourceSconceLavaFloor;
import com.mcmoddev.ironagefurniture.api.blocks.lightsource.lava.LightSourceSconceLavaWall;
import com.mcmoddev.ironagefurniture.api.blocks.lightsource.lava.ObsideanLump;
import com.mcmoddev.ironagefurniture.api.blocks.lightsource.red.LightSourceRed;
import com.mcmoddev.ironagefurniture.api.blocks.lightsource.red.LightSourceSconceRedFloor;
import com.mcmoddev.ironagefurniture.api.blocks.lightsource.red.LightSourceSconceRedWall;
import com.mcmoddev.ironagefurniture.api.blocks.lightsource.red.illuminated.red.LightSourceRedEight;
import com.mcmoddev.ironagefurniture.api.blocks.lightsource.red.illuminated.red.LightSourceRedEleven;
import com.mcmoddev.ironagefurniture.api.blocks.lightsource.red.illuminated.red.LightSourceRedFifteen;
import com.mcmoddev.ironagefurniture.api.blocks.lightsource.red.illuminated.red.LightSourceRedFive;
import com.mcmoddev.ironagefurniture.api.blocks.lightsource.red.illuminated.red.LightSourceRedFour;
import com.mcmoddev.ironagefurniture.api.blocks.lightsource.red.illuminated.red.LightSourceRedFourteen;
import com.mcmoddev.ironagefurniture.api.blocks.lightsource.red.illuminated.red.LightSourceRedNine;
import com.mcmoddev.ironagefurniture.api.blocks.lightsource.red.illuminated.red.LightSourceRedOne;
import com.mcmoddev.ironagefurniture.api.blocks.lightsource.red.illuminated.red.LightSourceRedSeven;
import com.mcmoddev.ironagefurniture.api.blocks.lightsource.red.illuminated.red.LightSourceRedSix;
import com.mcmoddev.ironagefurniture.api.blocks.lightsource.red.illuminated.red.LightSourceRedTen;
import com.mcmoddev.ironagefurniture.api.blocks.lightsource.red.illuminated.red.LightSourceRedThirteen;
import com.mcmoddev.ironagefurniture.api.blocks.lightsource.red.illuminated.red.LightSourceRedThree;
import com.mcmoddev.ironagefurniture.api.blocks.lightsource.red.illuminated.red.LightSourceRedTwelve;
import com.mcmoddev.ironagefurniture.api.blocks.lightsource.red.illuminated.red.LightSourceRedTwo;
import com.mcmoddev.ironagefurniture.api.blocks.lightsource.red.illuminated.sconce.floor.LightSourceSconceRedFloorEight;
import com.mcmoddev.ironagefurniture.api.blocks.lightsource.red.illuminated.sconce.floor.LightSourceSconceRedFloorEleven;
import com.mcmoddev.ironagefurniture.api.blocks.lightsource.red.illuminated.sconce.floor.LightSourceSconceRedFloorFifteen;
import com.mcmoddev.ironagefurniture.api.blocks.lightsource.red.illuminated.sconce.floor.LightSourceSconceRedFloorFive;
import com.mcmoddev.ironagefurniture.api.blocks.lightsource.red.illuminated.sconce.floor.LightSourceSconceRedFloorFour;
import com.mcmoddev.ironagefurniture.api.blocks.lightsource.red.illuminated.sconce.floor.LightSourceSconceRedFloorFourteen;
import com.mcmoddev.ironagefurniture.api.blocks.lightsource.red.illuminated.sconce.floor.LightSourceSconceRedFloorNine;
import com.mcmoddev.ironagefurniture.api.blocks.lightsource.red.illuminated.sconce.floor.LightSourceSconceRedFloorOne;
import com.mcmoddev.ironagefurniture.api.blocks.lightsource.red.illuminated.sconce.floor.LightSourceSconceRedFloorSeven;
import com.mcmoddev.ironagefurniture.api.blocks.lightsource.red.illuminated.sconce.floor.LightSourceSconceRedFloorSix;
import com.mcmoddev.ironagefurniture.api.blocks.lightsource.red.illuminated.sconce.floor.LightSourceSconceRedFloorTen;
import com.mcmoddev.ironagefurniture.api.blocks.lightsource.red.illuminated.sconce.floor.LightSourceSconceRedFloorThirteen;
import com.mcmoddev.ironagefurniture.api.blocks.lightsource.red.illuminated.sconce.floor.LightSourceSconceRedFloorThree;
import com.mcmoddev.ironagefurniture.api.blocks.lightsource.red.illuminated.sconce.floor.LightSourceSconceRedFloorTwelve;
import com.mcmoddev.ironagefurniture.api.blocks.lightsource.red.illuminated.sconce.floor.LightSourceSconceRedFloorTwo;
import com.mcmoddev.ironagefurniture.api.blocks.lightsource.red.illuminated.sconce.wall.LightSourceSconceRedWallEight;
import com.mcmoddev.ironagefurniture.api.blocks.lightsource.red.illuminated.sconce.wall.LightSourceSconceRedWallEleven;
import com.mcmoddev.ironagefurniture.api.blocks.lightsource.red.illuminated.sconce.wall.LightSourceSconceRedWallFifteen;
import com.mcmoddev.ironagefurniture.api.blocks.lightsource.red.illuminated.sconce.wall.LightSourceSconceRedWallFive;
import com.mcmoddev.ironagefurniture.api.blocks.lightsource.red.illuminated.sconce.wall.LightSourceSconceRedWallFour;
import com.mcmoddev.ironagefurniture.api.blocks.lightsource.red.illuminated.sconce.wall.LightSourceSconceRedWallFourteen;
import com.mcmoddev.ironagefurniture.api.blocks.lightsource.red.illuminated.sconce.wall.LightSourceSconceRedWallNine;
import com.mcmoddev.ironagefurniture.api.blocks.lightsource.red.illuminated.sconce.wall.LightSourceSconceRedWallOne;
import com.mcmoddev.ironagefurniture.api.blocks.lightsource.red.illuminated.sconce.wall.LightSourceSconceRedWallSeven;
import com.mcmoddev.ironagefurniture.api.blocks.lightsource.red.illuminated.sconce.wall.LightSourceSconceRedWallSix;
import com.mcmoddev.ironagefurniture.api.blocks.lightsource.red.illuminated.sconce.wall.LightSourceSconceRedWallTen;
import com.mcmoddev.ironagefurniture.api.blocks.lightsource.red.illuminated.sconce.wall.LightSourceSconceRedWallThirteen;
import com.mcmoddev.ironagefurniture.api.blocks.lightsource.red.illuminated.sconce.wall.LightSourceSconceRedWallThree;
import com.mcmoddev.ironagefurniture.api.blocks.lightsource.red.illuminated.sconce.wall.LightSourceSconceRedWallTwelve;
import com.mcmoddev.ironagefurniture.api.blocks.lightsource.red.illuminated.sconce.wall.LightSourceSconceRedWallTwo;
import com.mcmoddev.ironagefurniture.api.blocks.lightsource.redtorch.LightSourceSconceRedTorchFloor;
import com.mcmoddev.ironagefurniture.api.blocks.lightsource.redtorch.LightSourceSconceRedTorchFloorUnlit;
import com.mcmoddev.ironagefurniture.api.blocks.lightsource.redtorch.LightSourceSconceRedTorchWall;
import com.mcmoddev.ironagefurniture.api.blocks.lightsource.redtorch.LightSourceSconceRedTorchWallUnlit;
import com.mcmoddev.ironagefurniture.api.blocks.lightsource.soultorch.LightSourceSconceSoulTorchFloor;
import com.mcmoddev.ironagefurniture.api.blocks.lightsource.soultorch.LightSourceSconceSoulTorchWall;
import com.mcmoddev.ironagefurniture.api.blocks.lightsource.torch.LightSourceSconceTorchFloor;
import com.mcmoddev.ironagefurniture.api.blocks.lightsource.torch.LightSourceSconceTorchFloorUnlit;
import com.mcmoddev.ironagefurniture.api.blocks.lightsource.torch.LightSourceSconceTorchWall;
import com.mcmoddev.ironagefurniture.api.blocks.lightsource.torch.LightSourceSconceTorchWallUnlit;

import java.util.function.Supplier;

public class ModVanillaLights {
	public static final DeferredRegister<Block> REGISTER = DeferredRegister.create(ForgeRegistries.BLOCKS,
			Ironagefurniture.MODID);

	private static RegistryObject<Block> register(String name, Supplier<Block> block, boolean registerItem) {
		return register(name, block, new Item.Properties(), registerItem);
	}

	private static RegistryObject<Block> register(String name, Supplier<Block> block, Item.Properties properties, boolean registerItem) {
		RegistryObject<Block> registryObject = REGISTER.register(name, block);

		if (registerItem)
			ModItems.REGISTER.register(name, () -> new BlockItem(registryObject.get(), properties));

		return registryObject;
	}

	public static RegistryObject<Block> light_metal_ironage_sconce_floor_empty_iron = register("light_metal_ironage_sconce_floor_empty_iron", () -> new LightHolderSconceFloor(1, 10, SoundType.METAL, "light_metal_ironage_sconce_floor_empty_iron"), true);
	public static RegistryObject<Block> light_metal_ironage_sconce_wall_empty_iron = register("light_metal_ironage_sconce_wall_empty_iron", () -> new LightHolderSconceWall(1, 10, SoundType.METAL, "light_metal_ironage_sconce_wall_empty_iron"), false);
	public static RegistryObject<Block> light_metal_ironage_sconce_floor_torch_iron = register("light_metal_ironage_sconce_floor_torch_iron", () -> new LightSourceSconceTorchFloor(1, 10, SoundType.METAL, "light_metal_ironage_sconce_floor_torch_iron"), false);
	public static RegistryObject<Block> light_metal_ironage_sconce_floor_torch_iron_unlit = register("light_metal_ironage_sconce_floor_torch_iron_unlit", () -> new LightSourceSconceTorchFloorUnlit(1, 10, SoundType.METAL, "light_metal_ironage_sconce_floor_torch_iron_unlit"), false);
	public static RegistryObject<Block> light_metal_ironage_sconce_wall_torch_iron = register("light_metal_ironage_sconce_wall_torch_iron", () -> new LightSourceSconceTorchWall(1, 10, SoundType.METAL, "light_metal_ironage_sconce_wall_torch_iron"), false);
	public static RegistryObject<Block> light_metal_ironage_sconce_wall_torch_iron_unlit = register("light_metal_ironage_sconce_wall_torch_iron_unlit", () -> new LightSourceSconceTorchWallUnlit(1, 10, SoundType.METAL, "light_metal_ironage_sconce_wall_torch_iron_unlit"), false);
	public static RegistryObject<Block> light_metal_ironage_sconce_floor_redtorch_iron = register("light_metal_ironage_sconce_floor_redtorch_iron", () -> new LightSourceSconceRedTorchFloor(1, 10, SoundType.METAL, "light_metal_ironage_sconce_floor_redtorch_iron"), false);
	public static RegistryObject<Block> light_metal_ironage_sconce_floor_redtorch_iron_unlit = register("light_metal_ironage_sconce_floor_redtorch_iron_unlit", () -> new LightSourceSconceRedTorchFloorUnlit(1, 10, SoundType.METAL, "light_metal_ironage_sconce_floor_redtorch_iron_unlit"), false);
	public static RegistryObject<Block> light_metal_ironage_sconce_wall_redtorch_iron = register("light_metal_ironage_sconce_wall_redtorch_iron", () -> new LightSourceSconceRedTorchWall(1, 10, SoundType.METAL, "light_metal_ironage_sconce_wall_redtorch_iron"), false);
	public static RegistryObject<Block> light_metal_ironage_sconce_wall_redtorch_iron_unlit = register("light_metal_ironage_sconce_wall_redtorch_iron_unlit", () -> new LightSourceSconceRedTorchWallUnlit(1, 10, SoundType.METAL, "light_metal_ironage_sconce_wall_redtorch_iron_unlit"), false);
	public static RegistryObject<Block> light_metal_ironage_block_floor_glow_clear = register("light_metal_ironage_block_floor_glow_clear", () -> new LightSourceGlowdust(1, 10, SoundType.GLASS, "light_metal_ironage_block_floor_glow_clear"), true);
	public static RegistryObject<Block> light_metal_ironage_sconce_floor_glow_iron = register("light_metal_ironage_sconce_floor_glow_iron", () -> new LightSourceSconceGlowFloor(1, 10, SoundType.METAL, "light_metal_ironage_sconce_floor_glow_iron"), false);
	public static RegistryObject<Block> light_metal_ironage_sconce_wall_glow_iron = register("light_metal_ironage_sconce_wall_glow_iron", () -> new LightSourceSconceGlowWall(1, 10, SoundType.METAL, "light_metal_ironage_sconce_wall_glow_iron"), false);
	public static RegistryObject<Block> light_metal_ironage_block_floor_lava_clear = register("light_metal_ironage_block_floor_lava_clear", () -> new LightSourceLava(1, 10, SoundType.GLASS, "light_metal_ironage_block_floor_lava_clear"), true);
	public static RegistryObject<Block> light_metal_ironage_sconce_floor_lava_iron = register("light_metal_ironage_sconce_floor_lava_iron", () -> new LightSourceSconceLavaFloor(1, 10, SoundType.METAL, "light_metal_ironage_sconce_floor_lava_iron"), false);
	public static RegistryObject<Block> light_metal_ironage_sconce_wall_lava_iron = register("light_metal_ironage_sconce_wall_lava_iron", () -> new LightSourceSconceLavaWall(1, 10, SoundType.METAL, "light_metal_ironage_sconce_wall_lava_iron"), false);
	public static RegistryObject<Block> light_metal_ironage_block_floor_red_clear = register("light_metal_ironage_block_floor_red_clear", () -> new LightSourceRed(1, 10, SoundType.GLASS, "light_metal_ironage_block_floor_red_clear"), true);
	public static RegistryObject<Block> light_metal_ironage_block_floor_red_clear_one = register("light_metal_ironage_block_floor_red_clear_one", () -> new LightSourceRedOne(1, 10, SoundType.GLASS, "light_metal_ironage_block_floor_red_clear_one"), false);
	public static RegistryObject<Block> light_metal_ironage_block_floor_red_clear_two = register("light_metal_ironage_block_floor_red_clear_two", () -> new LightSourceRedTwo(1, 10, SoundType.GLASS, "light_metal_ironage_block_floor_red_clear_two"), false);
	public static RegistryObject<Block> light_metal_ironage_block_floor_red_clear_three = register("light_metal_ironage_block_floor_red_clear_three", () -> new LightSourceRedThree(1, 10, SoundType.GLASS, "light_metal_ironage_block_floor_red_clear_three"), false);
	public static RegistryObject<Block> light_metal_ironage_block_floor_red_clear_four = register("light_metal_ironage_block_floor_red_clear_four", () -> new LightSourceRedFour(1, 10, SoundType.GLASS, "light_metal_ironage_block_floor_red_clear_four"), false);
	public static RegistryObject<Block> light_metal_ironage_block_floor_red_clear_five = register("light_metal_ironage_block_floor_red_clear_five", () -> new LightSourceRedFive(1, 10, SoundType.GLASS, "light_metal_ironage_block_floor_red_clear_five"), false);
	public static RegistryObject<Block> light_metal_ironage_block_floor_red_clear_six = register("light_metal_ironage_block_floor_red_clear_six", () -> new LightSourceRedSix(1, 10, SoundType.GLASS, "light_metal_ironage_block_floor_red_clear_six"), false);
	public static RegistryObject<Block> light_metal_ironage_block_floor_red_clear_seven = register("light_metal_ironage_block_floor_red_clear_seven", () -> new LightSourceRedSeven(1, 10, SoundType.GLASS, "light_metal_ironage_block_floor_red_clear_seven"), false);
	public static RegistryObject<Block> light_metal_ironage_block_floor_red_clear_eight = register("light_metal_ironage_block_floor_red_clear_eight", () -> new LightSourceRedEight(1, 10, SoundType.GLASS, "light_metal_ironage_block_floor_red_clear_eight"), false);
	public static RegistryObject<Block> light_metal_ironage_block_floor_red_clear_nine = register("light_metal_ironage_block_floor_red_clear_nine", () -> new LightSourceRedNine(1, 10, SoundType.GLASS, "light_metal_ironage_block_floor_red_clear_nine"), false);
	public static RegistryObject<Block> light_metal_ironage_block_floor_red_clear_ten = register("light_metal_ironage_block_floor_red_clear_ten", () -> new LightSourceRedTen(1, 10, SoundType.GLASS, "light_metal_ironage_block_floor_red_clear_ten"), false);
	public static RegistryObject<Block> light_metal_ironage_block_floor_red_clear_eleven = register("light_metal_ironage_block_floor_red_clear_eleven", () -> new LightSourceRedEleven(1, 10, SoundType.GLASS, "light_metal_ironage_block_floor_red_clear_eleven"), false);
	public static RegistryObject<Block> light_metal_ironage_block_floor_red_clear_twelve = register("light_metal_ironage_block_floor_red_clear_twelve", () -> new LightSourceRedTwelve(1, 10, SoundType.GLASS, "light_metal_ironage_block_floor_red_clear_twelve"), false);
	public static RegistryObject<Block> light_metal_ironage_block_floor_red_clear_thirteen = register("light_metal_ironage_block_floor_red_clear_thirteen", () -> new LightSourceRedThirteen(1, 10, SoundType.GLASS, "light_metal_ironage_block_floor_red_clear_thirteen"), false);
	public static RegistryObject<Block> light_metal_ironage_block_floor_red_clear_fourteen = register("light_metal_ironage_block_floor_red_clear_fourteen", () -> new LightSourceRedFourteen(1, 10, SoundType.GLASS, "light_metal_ironage_block_floor_red_clear_fourteen"), false);
	public static RegistryObject<Block> light_metal_ironage_block_floor_red_clear_fifteen = register("light_metal_ironage_block_floor_red_clear_fifteen", () -> new LightSourceRedFifteen(1, 10, SoundType.GLASS, "light_metal_ironage_block_floor_red_clear_fifteen"), false);
	public static RegistryObject<Block> light_metal_ironage_sconce_floor_red_iron = register("light_metal_ironage_sconce_floor_red_iron", () -> new LightSourceSconceRedFloor(1, 10, SoundType.METAL, "light_metal_ironage_sconce_floor_red_iron"), false);
	public static RegistryObject<Block> light_metal_ironage_sconce_floor_red_iron_one = register("light_metal_ironage_sconce_floor_red_iron_one", () -> new LightSourceSconceRedFloorOne(1, 10, SoundType.METAL, "light_metal_ironage_sconce_floor_red_iron_one"), false);
	public static RegistryObject<Block> light_metal_ironage_sconce_floor_red_iron_two = register("light_metal_ironage_sconce_floor_red_iron_two", () -> new LightSourceSconceRedFloorTwo(1, 10, SoundType.METAL, "light_metal_ironage_sconce_floor_red_iron_two"), false);
	public static RegistryObject<Block> light_metal_ironage_sconce_floor_red_iron_three = register("light_metal_ironage_sconce_floor_red_iron_three", () -> new LightSourceSconceRedFloorThree(1, 10, SoundType.METAL, "light_metal_ironage_sconce_floor_red_iron_three"), false);
	public static RegistryObject<Block> light_metal_ironage_sconce_floor_red_iron_four = register("light_metal_ironage_sconce_floor_red_iron_four", () -> new LightSourceSconceRedFloorFour(1, 10, SoundType.METAL, "light_metal_ironage_sconce_floor_red_iron_four"), false);
	public static RegistryObject<Block> light_metal_ironage_sconce_floor_red_iron_five = register("light_metal_ironage_sconce_floor_red_iron_five", () -> new LightSourceSconceRedFloorFive(1, 10, SoundType.METAL, "light_metal_ironage_sconce_floor_red_iron_five"), false);
	public static RegistryObject<Block> light_metal_ironage_sconce_floor_red_iron_six = register("light_metal_ironage_sconce_floor_red_iron_six", () -> new LightSourceSconceRedFloorSix(1, 10, SoundType.METAL, "light_metal_ironage_sconce_floor_red_iron_six"), false);
	public static RegistryObject<Block> light_metal_ironage_sconce_floor_red_iron_seven = register("light_metal_ironage_sconce_floor_red_iron_seven", () -> new LightSourceSconceRedFloorSeven(1, 10, SoundType.METAL, "light_metal_ironage_sconce_floor_red_iron_seven"), false);
	public static RegistryObject<Block> light_metal_ironage_sconce_floor_red_iron_eight = register("light_metal_ironage_sconce_floor_red_iron_eight", () -> new LightSourceSconceRedFloorEight(1, 10, SoundType.METAL, "light_metal_ironage_sconce_floor_red_iron_eight"), false);
	public static RegistryObject<Block> light_metal_ironage_sconce_floor_red_iron_nine = register("light_metal_ironage_sconce_floor_red_iron_nine", () -> new LightSourceSconceRedFloorNine(1, 10, SoundType.METAL, "light_metal_ironage_sconce_floor_red_iron_nine"), false);
	public static RegistryObject<Block> light_metal_ironage_sconce_floor_red_iron_ten = register("light_metal_ironage_sconce_floor_red_iron_ten", () -> new LightSourceSconceRedFloorTen(1, 10, SoundType.METAL, "light_metal_ironage_sconce_floor_red_iron_ten"), false);
	public static RegistryObject<Block> light_metal_ironage_sconce_floor_red_iron_eleven = register("light_metal_ironage_sconce_floor_red_iron_eleven", () -> new LightSourceSconceRedFloorEleven(1, 10, SoundType.METAL, "light_metal_ironage_sconce_floor_red_iron_eleven"), false);
	public static RegistryObject<Block> light_metal_ironage_sconce_floor_red_iron_twelve = register("light_metal_ironage_sconce_floor_red_iron_twelve", () -> new LightSourceSconceRedFloorTwelve(1, 10, SoundType.METAL, "light_metal_ironage_sconce_floor_red_iron_twelve"), false);
	public static RegistryObject<Block> light_metal_ironage_sconce_floor_red_iron_thirteen = register("light_metal_ironage_sconce_floor_red_iron_thirteen", () -> new LightSourceSconceRedFloorThirteen(1, 10, SoundType.METAL, "light_metal_ironage_sconce_floor_red_iron_thirteen"), false);
	public static RegistryObject<Block> light_metal_ironage_sconce_floor_red_iron_fourteen = register("light_metal_ironage_sconce_floor_red_iron_fourteen", () -> new LightSourceSconceRedFloorFourteen(1, 10, SoundType.METAL, "light_metal_ironage_sconce_floor_red_iron_fourteen"), false);
	public static RegistryObject<Block> light_metal_ironage_sconce_floor_red_iron_fifteen = register("light_metal_ironage_sconce_floor_red_iron_fifteen", () -> new LightSourceSconceRedFloorFifteen(1, 10, SoundType.METAL, "light_metal_ironage_sconce_floor_red_iron_fifteen"), false);
	public static RegistryObject<Block> light_metal_ironage_sconce_wall_red_iron = register("light_metal_ironage_sconce_wall_red_iron", () -> new LightSourceSconceRedWall(1, 10, SoundType.METAL, "light_metal_ironage_sconce_wall_red_iron"), false);
	public static RegistryObject<Block> light_metal_ironage_sconce_wall_red_iron_one = register("light_metal_ironage_sconce_wall_red_iron_one", () -> new LightSourceSconceRedWallOne(1, 10, SoundType.METAL, "light_metal_ironage_sconce_wall_red_iron_one"), false);
	public static RegistryObject<Block> light_metal_ironage_sconce_wall_red_iron_two = register("light_metal_ironage_sconce_wall_red_iron_two", () -> new LightSourceSconceRedWallTwo(1, 10, SoundType.METAL, "light_metal_ironage_sconce_wall_red_iron_two"), false);
	public static RegistryObject<Block> light_metal_ironage_sconce_wall_red_iron_three = register("light_metal_ironage_sconce_wall_red_iron_three", () -> new LightSourceSconceRedWallThree(1, 10, SoundType.METAL, "light_metal_ironage_sconce_wall_red_iron_three"), false);
	public static RegistryObject<Block> light_metal_ironage_sconce_wall_red_iron_four = register("light_metal_ironage_sconce_wall_red_iron_four", () -> new LightSourceSconceRedWallFour(1, 10, SoundType.METAL, "light_metal_ironage_sconce_wall_red_iron_four"), false);
	public static RegistryObject<Block> light_metal_ironage_sconce_wall_red_iron_five = register("light_metal_ironage_sconce_wall_red_iron_five", () -> new LightSourceSconceRedWallFive(1, 10, SoundType.METAL, "light_metal_ironage_sconce_wall_red_iron_five"), false);
	public static RegistryObject<Block> light_metal_ironage_sconce_wall_red_iron_six = register("light_metal_ironage_sconce_wall_red_iron_six", () -> new LightSourceSconceRedWallSix(1, 10, SoundType.METAL, "light_metal_ironage_sconce_wall_red_iron_six"), false);
	public static RegistryObject<Block> light_metal_ironage_sconce_wall_red_iron_seven = register("light_metal_ironage_sconce_wall_red_iron_seven", () -> new LightSourceSconceRedWallSeven(1, 10, SoundType.METAL, "light_metal_ironage_sconce_wall_red_iron_seven"), false);
	public static RegistryObject<Block> light_metal_ironage_sconce_wall_red_iron_eight = register("light_metal_ironage_sconce_wall_red_iron_eight", () -> new LightSourceSconceRedWallEight(1, 10, SoundType.METAL, "light_metal_ironage_sconce_wall_red_iron_eight"), false);
	public static RegistryObject<Block> light_metal_ironage_sconce_wall_red_iron_nine = register("light_metal_ironage_sconce_wall_red_iron_nine", () -> new LightSourceSconceRedWallNine(1, 10, SoundType.METAL, "light_metal_ironage_sconce_wall_red_iron_nine"), false);
	public static RegistryObject<Block> light_metal_ironage_sconce_wall_red_iron_ten = register("light_metal_ironage_sconce_wall_red_iron_ten", () -> new LightSourceSconceRedWallTen(1, 10, SoundType.METAL, "light_metal_ironage_sconce_wall_red_iron_ten"), false);
	public static RegistryObject<Block> light_metal_ironage_sconce_wall_red_iron_eleven = register("light_metal_ironage_sconce_wall_red_iron_eleven", () -> new LightSourceSconceRedWallEleven(1, 10, SoundType.METAL, "light_metal_ironage_sconce_wall_red_iron_eleven"), false);
	public static RegistryObject<Block> light_metal_ironage_sconce_wall_red_iron_twelve = register("light_metal_ironage_sconce_wall_red_iron_twelve", () -> new LightSourceSconceRedWallTwelve(1, 10, SoundType.METAL, "light_metal_ironage_sconce_wall_red_iron_twelve"), false);
	public static RegistryObject<Block> light_metal_ironage_sconce_wall_red_iron_thirteen = register("light_metal_ironage_sconce_wall_red_iron_thirteen", () -> new LightSourceSconceRedWallThirteen(1, 10, SoundType.METAL, "light_metal_ironage_sconce_wall_red_iron_thirteen"), false);
	public static RegistryObject<Block> light_metal_ironage_sconce_wall_red_iron_fourteen = register("light_metal_ironage_sconce_wall_red_iron_fourteen", () -> new LightSourceSconceRedWallFourteen(1, 10, SoundType.METAL, "light_metal_ironage_sconce_wall_red_iron_fourteen"), false);
	public static RegistryObject<Block> light_metal_ironage_sconce_wall_red_iron_fifteen = register("light_metal_ironage_sconce_wall_red_iron_fifteen", () -> new LightSourceSconceRedWallFifteen(1, 10, SoundType.METAL, "light_metal_ironage_sconce_wall_red_iron_fifteen"), false);
	public static RegistryObject<Block> light_metal_ironage_sconce_floor_soultorch_iron = register("light_metal_ironage_sconce_floor_soultorch_iron", () -> new LightSourceSconceSoulTorchFloor(1, 10, SoundType.METAL, "light_metal_ironage_sconce_floor_soultorch_iron"), false);
	public static RegistryObject<Block> light_metal_ironage_sconce_wall_soultorch_iron = register("light_metal_ironage_sconce_wall_soultorch_iron", () -> new LightSourceSconceSoulTorchWall(1, 10, SoundType.METAL, "light_metal_ironage_sconce_wall_soultorch_iron"), false);
	public static RegistryObject<Block> obsidian_chunk = register("obsidian_chunk", () -> new ObsideanLump(1, 10, SoundType.STONE, "obsidian_chunk"), true);
}