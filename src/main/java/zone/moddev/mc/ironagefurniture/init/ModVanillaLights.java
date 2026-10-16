package zone.moddev.mc.ironagefurniture.init;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.neoforge.registries.DeferredHolder;

import zone.moddev.mc.ironagefurniture.Ironagefurniture;
import zone.moddev.mc.ironagefurniture.ModItems;
import zone.moddev.mc.ironagefurniture.api.blocks.lightholder.LightHolderSconceFloor;
import zone.moddev.mc.ironagefurniture.api.blocks.lightholder.LightHolderSconceWall;
import zone.moddev.mc.ironagefurniture.api.blocks.lightsource.glow.LightSourceGlowdust;
import zone.moddev.mc.ironagefurniture.api.blocks.lightsource.glow.LightSourceSconceGlowFloor;
import zone.moddev.mc.ironagefurniture.api.blocks.lightsource.glow.LightSourceSconceGlowWall;
import zone.moddev.mc.ironagefurniture.api.blocks.lightsource.lava.LightSourceLava;
import zone.moddev.mc.ironagefurniture.api.blocks.lightsource.lava.LightSourceSconceLavaFloor;
import zone.moddev.mc.ironagefurniture.api.blocks.lightsource.lava.LightSourceSconceLavaWall;
import zone.moddev.mc.ironagefurniture.api.blocks.lightsource.lava.ObsideanLump;
import zone.moddev.mc.ironagefurniture.api.blocks.lightsource.red.LightSourceRed;
import zone.moddev.mc.ironagefurniture.api.blocks.lightsource.red.LightSourceSconceRedFloor;
import zone.moddev.mc.ironagefurniture.api.blocks.lightsource.red.LightSourceSconceRedWall;
import zone.moddev.mc.ironagefurniture.api.blocks.lightsource.red.illuminated.red.LightSourceRedEight;
import zone.moddev.mc.ironagefurniture.api.blocks.lightsource.red.illuminated.red.LightSourceRedEleven;
import zone.moddev.mc.ironagefurniture.api.blocks.lightsource.red.illuminated.red.LightSourceRedFifteen;
import zone.moddev.mc.ironagefurniture.api.blocks.lightsource.red.illuminated.red.LightSourceRedFive;
import zone.moddev.mc.ironagefurniture.api.blocks.lightsource.red.illuminated.red.LightSourceRedFour;
import zone.moddev.mc.ironagefurniture.api.blocks.lightsource.red.illuminated.red.LightSourceRedFourteen;
import zone.moddev.mc.ironagefurniture.api.blocks.lightsource.red.illuminated.red.LightSourceRedNine;
import zone.moddev.mc.ironagefurniture.api.blocks.lightsource.red.illuminated.red.LightSourceRedOne;
import zone.moddev.mc.ironagefurniture.api.blocks.lightsource.red.illuminated.red.LightSourceRedSeven;
import zone.moddev.mc.ironagefurniture.api.blocks.lightsource.red.illuminated.red.LightSourceRedSix;
import zone.moddev.mc.ironagefurniture.api.blocks.lightsource.red.illuminated.red.LightSourceRedTen;
import zone.moddev.mc.ironagefurniture.api.blocks.lightsource.red.illuminated.red.LightSourceRedThirteen;
import zone.moddev.mc.ironagefurniture.api.blocks.lightsource.red.illuminated.red.LightSourceRedThree;
import zone.moddev.mc.ironagefurniture.api.blocks.lightsource.red.illuminated.red.LightSourceRedTwelve;
import zone.moddev.mc.ironagefurniture.api.blocks.lightsource.red.illuminated.red.LightSourceRedTwo;
import zone.moddev.mc.ironagefurniture.api.blocks.lightsource.red.illuminated.sconce.floor.LightSourceSconceRedFloorEight;
import zone.moddev.mc.ironagefurniture.api.blocks.lightsource.red.illuminated.sconce.floor.LightSourceSconceRedFloorEleven;
import zone.moddev.mc.ironagefurniture.api.blocks.lightsource.red.illuminated.sconce.floor.LightSourceSconceRedFloorFifteen;
import zone.moddev.mc.ironagefurniture.api.blocks.lightsource.red.illuminated.sconce.floor.LightSourceSconceRedFloorFive;
import zone.moddev.mc.ironagefurniture.api.blocks.lightsource.red.illuminated.sconce.floor.LightSourceSconceRedFloorFour;
import zone.moddev.mc.ironagefurniture.api.blocks.lightsource.red.illuminated.sconce.floor.LightSourceSconceRedFloorFourteen;
import zone.moddev.mc.ironagefurniture.api.blocks.lightsource.red.illuminated.sconce.floor.LightSourceSconceRedFloorNine;
import zone.moddev.mc.ironagefurniture.api.blocks.lightsource.red.illuminated.sconce.floor.LightSourceSconceRedFloorOne;
import zone.moddev.mc.ironagefurniture.api.blocks.lightsource.red.illuminated.sconce.floor.LightSourceSconceRedFloorSeven;
import zone.moddev.mc.ironagefurniture.api.blocks.lightsource.red.illuminated.sconce.floor.LightSourceSconceRedFloorSix;
import zone.moddev.mc.ironagefurniture.api.blocks.lightsource.red.illuminated.sconce.floor.LightSourceSconceRedFloorTen;
import zone.moddev.mc.ironagefurniture.api.blocks.lightsource.red.illuminated.sconce.floor.LightSourceSconceRedFloorThirteen;
import zone.moddev.mc.ironagefurniture.api.blocks.lightsource.red.illuminated.sconce.floor.LightSourceSconceRedFloorThree;
import zone.moddev.mc.ironagefurniture.api.blocks.lightsource.red.illuminated.sconce.floor.LightSourceSconceRedFloorTwelve;
import zone.moddev.mc.ironagefurniture.api.blocks.lightsource.red.illuminated.sconce.floor.LightSourceSconceRedFloorTwo;
import zone.moddev.mc.ironagefurniture.api.blocks.lightsource.red.illuminated.sconce.wall.LightSourceSconceRedWallEight;
import zone.moddev.mc.ironagefurniture.api.blocks.lightsource.red.illuminated.sconce.wall.LightSourceSconceRedWallEleven;
import zone.moddev.mc.ironagefurniture.api.blocks.lightsource.red.illuminated.sconce.wall.LightSourceSconceRedWallFifteen;
import zone.moddev.mc.ironagefurniture.api.blocks.lightsource.red.illuminated.sconce.wall.LightSourceSconceRedWallFive;
import zone.moddev.mc.ironagefurniture.api.blocks.lightsource.red.illuminated.sconce.wall.LightSourceSconceRedWallFour;
import zone.moddev.mc.ironagefurniture.api.blocks.lightsource.red.illuminated.sconce.wall.LightSourceSconceRedWallFourteen;
import zone.moddev.mc.ironagefurniture.api.blocks.lightsource.red.illuminated.sconce.wall.LightSourceSconceRedWallNine;
import zone.moddev.mc.ironagefurniture.api.blocks.lightsource.red.illuminated.sconce.wall.LightSourceSconceRedWallOne;
import zone.moddev.mc.ironagefurniture.api.blocks.lightsource.red.illuminated.sconce.wall.LightSourceSconceRedWallSeven;
import zone.moddev.mc.ironagefurniture.api.blocks.lightsource.red.illuminated.sconce.wall.LightSourceSconceRedWallSix;
import zone.moddev.mc.ironagefurniture.api.blocks.lightsource.red.illuminated.sconce.wall.LightSourceSconceRedWallTen;
import zone.moddev.mc.ironagefurniture.api.blocks.lightsource.red.illuminated.sconce.wall.LightSourceSconceRedWallThirteen;
import zone.moddev.mc.ironagefurniture.api.blocks.lightsource.red.illuminated.sconce.wall.LightSourceSconceRedWallThree;
import zone.moddev.mc.ironagefurniture.api.blocks.lightsource.red.illuminated.sconce.wall.LightSourceSconceRedWallTwelve;
import zone.moddev.mc.ironagefurniture.api.blocks.lightsource.red.illuminated.sconce.wall.LightSourceSconceRedWallTwo;
import zone.moddev.mc.ironagefurniture.api.blocks.lightsource.redtorch.LightSourceSconceRedTorchFloor;
import zone.moddev.mc.ironagefurniture.api.blocks.lightsource.redtorch.LightSourceSconceRedTorchFloorUnlit;
import zone.moddev.mc.ironagefurniture.api.blocks.lightsource.redtorch.LightSourceSconceRedTorchWall;
import zone.moddev.mc.ironagefurniture.api.blocks.lightsource.redtorch.LightSourceSconceRedTorchWallUnlit;
import zone.moddev.mc.ironagefurniture.api.blocks.lightsource.soultorch.LightSourceSconceSoulTorchFloor;
import zone.moddev.mc.ironagefurniture.api.blocks.lightsource.soultorch.LightSourceSconceSoulTorchWall;
import zone.moddev.mc.ironagefurniture.api.blocks.lightsource.torch.LightSourceSconceTorchFloor;
import zone.moddev.mc.ironagefurniture.api.blocks.lightsource.torch.LightSourceSconceTorchFloorUnlit;
import zone.moddev.mc.ironagefurniture.api.blocks.lightsource.torch.LightSourceSconceTorchWall;
import zone.moddev.mc.ironagefurniture.api.blocks.lightsource.torch.LightSourceSconceTorchWallUnlit;
import zone.moddev.mc.ironagefurniture.api.items.ThrowableLavaLampBlockItem;

import java.util.function.Supplier;
import java.util.function.Function;

public class ModVanillaLights {
	public static final DeferredRegister<Block> REGISTER = DeferredRegister.create(BuiltInRegistries.BLOCK,
			Ironagefurniture.MODID);

	private static DeferredHolder<Block, Block> register(String name, Function<String, Block> block, boolean registerItem) {
		return register(name, block, new Item.Properties(), registerItem);
	}

	private static DeferredHolder<Block, Block> register(String name, Function<String, Block> block, Item.Properties properties, boolean registerItem) {
		DeferredHolder<Block, Block> registryObject = name.startsWith("light_metal_ironage_sconce_")
                ? SconceVariants.register(REGISTER, name, block) : REGISTER.register(name, () -> block.apply(name));

		if (registerItem)
			ModItems.REGISTER.register(name,
					() -> name.equals("light_metal_ironage_sconce_floor_empty_iron")
                        ? new zone.moddev.mc.ironagefurniture.api.items.MetalSconceBlockItem(registryObject.get(),
                            RegistrationProperties.item(properties, name).useBlockDescriptionPrefix(),
                            zone.moddev.mc.ironagefurniture.api.enumerations.SconceMetal.IRON)
                        : new BlockItem(registryObject.get(), RegistrationProperties.item(properties, name).useBlockDescriptionPrefix()));

		return registryObject;
	}

	private static DeferredHolder<Block, Block> registerThrowableLavaLamp(String name, Supplier<Block> block) {
		DeferredHolder<Block, Block> registryObject = REGISTER.register(name, block);
		ModItems.REGISTER.register(name, () -> new ThrowableLavaLampBlockItem(registryObject.get(),
				RegistrationProperties.item(new Item.Properties(), name).useBlockDescriptionPrefix()));
		return registryObject;
	}

	public static DeferredHolder<Block, Block> light_metal_ironage_sconce_floor_empty_iron = register("light_metal_ironage_sconce_floor_empty_iron", path -> new LightHolderSconceFloor(1, 10, SoundType.METAL, path), true);
	public static DeferredHolder<Block, Block> light_metal_ironage_sconce_wall_empty_iron = register("light_metal_ironage_sconce_wall_empty_iron", path -> new LightHolderSconceWall(1, 10, SoundType.METAL, path), false);
	public static DeferredHolder<Block, Block> light_metal_ironage_sconce_floor_torch_iron = register("light_metal_ironage_sconce_floor_torch_iron", path -> new LightSourceSconceTorchFloor(1, 10, SoundType.METAL, path), false);
	public static DeferredHolder<Block, Block> light_metal_ironage_sconce_floor_torch_iron_unlit = register("light_metal_ironage_sconce_floor_torch_iron_unlit", path -> new LightSourceSconceTorchFloorUnlit(1, 10, SoundType.METAL, path), false);
	public static DeferredHolder<Block, Block> light_metal_ironage_sconce_wall_torch_iron = register("light_metal_ironage_sconce_wall_torch_iron", path -> new LightSourceSconceTorchWall(1, 10, SoundType.METAL, path), false);
	public static DeferredHolder<Block, Block> light_metal_ironage_sconce_wall_torch_iron_unlit = register("light_metal_ironage_sconce_wall_torch_iron_unlit", path -> new LightSourceSconceTorchWallUnlit(1, 10, SoundType.METAL, path), false);
	public static DeferredHolder<Block, Block> light_metal_ironage_sconce_floor_redtorch_iron = register("light_metal_ironage_sconce_floor_redtorch_iron", path -> new LightSourceSconceRedTorchFloor(1, 10, SoundType.METAL, path), false);
	public static DeferredHolder<Block, Block> light_metal_ironage_sconce_floor_redtorch_iron_unlit = register("light_metal_ironage_sconce_floor_redtorch_iron_unlit", path -> new LightSourceSconceRedTorchFloorUnlit(1, 10, SoundType.METAL, path), false);
	public static DeferredHolder<Block, Block> light_metal_ironage_sconce_wall_redtorch_iron = register("light_metal_ironage_sconce_wall_redtorch_iron", path -> new LightSourceSconceRedTorchWall(1, 10, SoundType.METAL, path), false);
	public static DeferredHolder<Block, Block> light_metal_ironage_sconce_wall_redtorch_iron_unlit = register("light_metal_ironage_sconce_wall_redtorch_iron_unlit", path -> new LightSourceSconceRedTorchWallUnlit(1, 10, SoundType.METAL, path), false);
	public static DeferredHolder<Block, Block> light_metal_ironage_block_floor_glow_clear = register("light_metal_ironage_block_floor_glow_clear", path -> new LightSourceGlowdust(1, 10, SoundType.GLASS, path), true);
	public static DeferredHolder<Block, Block> light_metal_ironage_sconce_floor_glow_iron = register("light_metal_ironage_sconce_floor_glow_iron", path -> new LightSourceSconceGlowFloor(1, 10, SoundType.METAL, path), false);
	public static DeferredHolder<Block, Block> light_metal_ironage_sconce_wall_glow_iron = register("light_metal_ironage_sconce_wall_glow_iron", path -> new LightSourceSconceGlowWall(1, 10, SoundType.METAL, path), false);
	public static DeferredHolder<Block, Block> light_metal_ironage_block_floor_lava_clear = registerThrowableLavaLamp("light_metal_ironage_block_floor_lava_clear", () -> new LightSourceLava(.3F, 1, SoundType.GLASS, "light_metal_ironage_block_floor_lava_clear"));
	public static DeferredHolder<Block, Block> light_metal_ironage_sconce_floor_lava_iron = register("light_metal_ironage_sconce_floor_lava_iron", path -> new LightSourceSconceLavaFloor(1, 10, SoundType.METAL, path), false);
	public static DeferredHolder<Block, Block> light_metal_ironage_sconce_wall_lava_iron = register("light_metal_ironage_sconce_wall_lava_iron", path -> new LightSourceSconceLavaWall(1, 10, SoundType.METAL, path), false);
	public static DeferredHolder<Block, Block> light_metal_ironage_block_floor_red_clear = register("light_metal_ironage_block_floor_red_clear", path -> new LightSourceRed(1, 10, SoundType.GLASS, path), true);
	public static DeferredHolder<Block, Block> light_metal_ironage_block_floor_red_clear_one = register("light_metal_ironage_block_floor_red_clear_one", path -> new LightSourceRedOne(1, 10, SoundType.GLASS, path), false);
	public static DeferredHolder<Block, Block> light_metal_ironage_block_floor_red_clear_two = register("light_metal_ironage_block_floor_red_clear_two", path -> new LightSourceRedTwo(1, 10, SoundType.GLASS, path), false);
	public static DeferredHolder<Block, Block> light_metal_ironage_block_floor_red_clear_three = register("light_metal_ironage_block_floor_red_clear_three", path -> new LightSourceRedThree(1, 10, SoundType.GLASS, path), false);
	public static DeferredHolder<Block, Block> light_metal_ironage_block_floor_red_clear_four = register("light_metal_ironage_block_floor_red_clear_four", path -> new LightSourceRedFour(1, 10, SoundType.GLASS, path), false);
	public static DeferredHolder<Block, Block> light_metal_ironage_block_floor_red_clear_five = register("light_metal_ironage_block_floor_red_clear_five", path -> new LightSourceRedFive(1, 10, SoundType.GLASS, path), false);
	public static DeferredHolder<Block, Block> light_metal_ironage_block_floor_red_clear_six = register("light_metal_ironage_block_floor_red_clear_six", path -> new LightSourceRedSix(1, 10, SoundType.GLASS, path), false);
	public static DeferredHolder<Block, Block> light_metal_ironage_block_floor_red_clear_seven = register("light_metal_ironage_block_floor_red_clear_seven", path -> new LightSourceRedSeven(1, 10, SoundType.GLASS, path), false);
	public static DeferredHolder<Block, Block> light_metal_ironage_block_floor_red_clear_eight = register("light_metal_ironage_block_floor_red_clear_eight", path -> new LightSourceRedEight(1, 10, SoundType.GLASS, path), false);
	public static DeferredHolder<Block, Block> light_metal_ironage_block_floor_red_clear_nine = register("light_metal_ironage_block_floor_red_clear_nine", path -> new LightSourceRedNine(1, 10, SoundType.GLASS, path), false);
	public static DeferredHolder<Block, Block> light_metal_ironage_block_floor_red_clear_ten = register("light_metal_ironage_block_floor_red_clear_ten", path -> new LightSourceRedTen(1, 10, SoundType.GLASS, path), false);
	public static DeferredHolder<Block, Block> light_metal_ironage_block_floor_red_clear_eleven = register("light_metal_ironage_block_floor_red_clear_eleven", path -> new LightSourceRedEleven(1, 10, SoundType.GLASS, path), false);
	public static DeferredHolder<Block, Block> light_metal_ironage_block_floor_red_clear_twelve = register("light_metal_ironage_block_floor_red_clear_twelve", path -> new LightSourceRedTwelve(1, 10, SoundType.GLASS, path), false);
	public static DeferredHolder<Block, Block> light_metal_ironage_block_floor_red_clear_thirteen = register("light_metal_ironage_block_floor_red_clear_thirteen", path -> new LightSourceRedThirteen(1, 10, SoundType.GLASS, path), false);
	public static DeferredHolder<Block, Block> light_metal_ironage_block_floor_red_clear_fourteen = register("light_metal_ironage_block_floor_red_clear_fourteen", path -> new LightSourceRedFourteen(1, 10, SoundType.GLASS, path), false);
	public static DeferredHolder<Block, Block> light_metal_ironage_block_floor_red_clear_fifteen = register("light_metal_ironage_block_floor_red_clear_fifteen", path -> new LightSourceRedFifteen(1, 10, SoundType.GLASS, path), false);
	public static DeferredHolder<Block, Block> light_metal_ironage_sconce_floor_red_iron = register("light_metal_ironage_sconce_floor_red_iron", path -> new LightSourceSconceRedFloor(1, 10, SoundType.METAL, path), false);
	public static DeferredHolder<Block, Block> light_metal_ironage_sconce_floor_red_iron_one = register("light_metal_ironage_sconce_floor_red_iron_one", path -> new LightSourceSconceRedFloorOne(1, 10, SoundType.METAL, path), false);
	public static DeferredHolder<Block, Block> light_metal_ironage_sconce_floor_red_iron_two = register("light_metal_ironage_sconce_floor_red_iron_two", path -> new LightSourceSconceRedFloorTwo(1, 10, SoundType.METAL, path), false);
	public static DeferredHolder<Block, Block> light_metal_ironage_sconce_floor_red_iron_three = register("light_metal_ironage_sconce_floor_red_iron_three", path -> new LightSourceSconceRedFloorThree(1, 10, SoundType.METAL, path), false);
	public static DeferredHolder<Block, Block> light_metal_ironage_sconce_floor_red_iron_four = register("light_metal_ironage_sconce_floor_red_iron_four", path -> new LightSourceSconceRedFloorFour(1, 10, SoundType.METAL, path), false);
	public static DeferredHolder<Block, Block> light_metal_ironage_sconce_floor_red_iron_five = register("light_metal_ironage_sconce_floor_red_iron_five", path -> new LightSourceSconceRedFloorFive(1, 10, SoundType.METAL, path), false);
	public static DeferredHolder<Block, Block> light_metal_ironage_sconce_floor_red_iron_six = register("light_metal_ironage_sconce_floor_red_iron_six", path -> new LightSourceSconceRedFloorSix(1, 10, SoundType.METAL, path), false);
	public static DeferredHolder<Block, Block> light_metal_ironage_sconce_floor_red_iron_seven = register("light_metal_ironage_sconce_floor_red_iron_seven", path -> new LightSourceSconceRedFloorSeven(1, 10, SoundType.METAL, path), false);
	public static DeferredHolder<Block, Block> light_metal_ironage_sconce_floor_red_iron_eight = register("light_metal_ironage_sconce_floor_red_iron_eight", path -> new LightSourceSconceRedFloorEight(1, 10, SoundType.METAL, path), false);
	public static DeferredHolder<Block, Block> light_metal_ironage_sconce_floor_red_iron_nine = register("light_metal_ironage_sconce_floor_red_iron_nine", path -> new LightSourceSconceRedFloorNine(1, 10, SoundType.METAL, path), false);
	public static DeferredHolder<Block, Block> light_metal_ironage_sconce_floor_red_iron_ten = register("light_metal_ironage_sconce_floor_red_iron_ten", path -> new LightSourceSconceRedFloorTen(1, 10, SoundType.METAL, path), false);
	public static DeferredHolder<Block, Block> light_metal_ironage_sconce_floor_red_iron_eleven = register("light_metal_ironage_sconce_floor_red_iron_eleven", path -> new LightSourceSconceRedFloorEleven(1, 10, SoundType.METAL, path), false);
	public static DeferredHolder<Block, Block> light_metal_ironage_sconce_floor_red_iron_twelve = register("light_metal_ironage_sconce_floor_red_iron_twelve", path -> new LightSourceSconceRedFloorTwelve(1, 10, SoundType.METAL, path), false);
	public static DeferredHolder<Block, Block> light_metal_ironage_sconce_floor_red_iron_thirteen = register("light_metal_ironage_sconce_floor_red_iron_thirteen", path -> new LightSourceSconceRedFloorThirteen(1, 10, SoundType.METAL, path), false);
	public static DeferredHolder<Block, Block> light_metal_ironage_sconce_floor_red_iron_fourteen = register("light_metal_ironage_sconce_floor_red_iron_fourteen", path -> new LightSourceSconceRedFloorFourteen(1, 10, SoundType.METAL, path), false);
	public static DeferredHolder<Block, Block> light_metal_ironage_sconce_floor_red_iron_fifteen = register("light_metal_ironage_sconce_floor_red_iron_fifteen", path -> new LightSourceSconceRedFloorFifteen(1, 10, SoundType.METAL, path), false);
	public static DeferredHolder<Block, Block> light_metal_ironage_sconce_wall_red_iron = register("light_metal_ironage_sconce_wall_red_iron", path -> new LightSourceSconceRedWall(1, 10, SoundType.METAL, path), false);
	public static DeferredHolder<Block, Block> light_metal_ironage_sconce_wall_red_iron_one = register("light_metal_ironage_sconce_wall_red_iron_one", path -> new LightSourceSconceRedWallOne(1, 10, SoundType.METAL, path), false);
	public static DeferredHolder<Block, Block> light_metal_ironage_sconce_wall_red_iron_two = register("light_metal_ironage_sconce_wall_red_iron_two", path -> new LightSourceSconceRedWallTwo(1, 10, SoundType.METAL, path), false);
	public static DeferredHolder<Block, Block> light_metal_ironage_sconce_wall_red_iron_three = register("light_metal_ironage_sconce_wall_red_iron_three", path -> new LightSourceSconceRedWallThree(1, 10, SoundType.METAL, path), false);
	public static DeferredHolder<Block, Block> light_metal_ironage_sconce_wall_red_iron_four = register("light_metal_ironage_sconce_wall_red_iron_four", path -> new LightSourceSconceRedWallFour(1, 10, SoundType.METAL, path), false);
	public static DeferredHolder<Block, Block> light_metal_ironage_sconce_wall_red_iron_five = register("light_metal_ironage_sconce_wall_red_iron_five", path -> new LightSourceSconceRedWallFive(1, 10, SoundType.METAL, path), false);
	public static DeferredHolder<Block, Block> light_metal_ironage_sconce_wall_red_iron_six = register("light_metal_ironage_sconce_wall_red_iron_six", path -> new LightSourceSconceRedWallSix(1, 10, SoundType.METAL, path), false);
	public static DeferredHolder<Block, Block> light_metal_ironage_sconce_wall_red_iron_seven = register("light_metal_ironage_sconce_wall_red_iron_seven", path -> new LightSourceSconceRedWallSeven(1, 10, SoundType.METAL, path), false);
	public static DeferredHolder<Block, Block> light_metal_ironage_sconce_wall_red_iron_eight = register("light_metal_ironage_sconce_wall_red_iron_eight", path -> new LightSourceSconceRedWallEight(1, 10, SoundType.METAL, path), false);
	public static DeferredHolder<Block, Block> light_metal_ironage_sconce_wall_red_iron_nine = register("light_metal_ironage_sconce_wall_red_iron_nine", path -> new LightSourceSconceRedWallNine(1, 10, SoundType.METAL, path), false);
	public static DeferredHolder<Block, Block> light_metal_ironage_sconce_wall_red_iron_ten = register("light_metal_ironage_sconce_wall_red_iron_ten", path -> new LightSourceSconceRedWallTen(1, 10, SoundType.METAL, path), false);
	public static DeferredHolder<Block, Block> light_metal_ironage_sconce_wall_red_iron_eleven = register("light_metal_ironage_sconce_wall_red_iron_eleven", path -> new LightSourceSconceRedWallEleven(1, 10, SoundType.METAL, path), false);
	public static DeferredHolder<Block, Block> light_metal_ironage_sconce_wall_red_iron_twelve = register("light_metal_ironage_sconce_wall_red_iron_twelve", path -> new LightSourceSconceRedWallTwelve(1, 10, SoundType.METAL, path), false);
	public static DeferredHolder<Block, Block> light_metal_ironage_sconce_wall_red_iron_thirteen = register("light_metal_ironage_sconce_wall_red_iron_thirteen", path -> new LightSourceSconceRedWallThirteen(1, 10, SoundType.METAL, path), false);
	public static DeferredHolder<Block, Block> light_metal_ironage_sconce_wall_red_iron_fourteen = register("light_metal_ironage_sconce_wall_red_iron_fourteen", path -> new LightSourceSconceRedWallFourteen(1, 10, SoundType.METAL, path), false);
	public static DeferredHolder<Block, Block> light_metal_ironage_sconce_wall_red_iron_fifteen = register("light_metal_ironage_sconce_wall_red_iron_fifteen", path -> new LightSourceSconceRedWallFifteen(1, 10, SoundType.METAL, path), false);
	public static DeferredHolder<Block, Block> light_metal_ironage_sconce_floor_soultorch_iron = register("light_metal_ironage_sconce_floor_soultorch_iron", path -> new LightSourceSconceSoulTorchFloor(1, 10, SoundType.METAL, path), false);
	public static DeferredHolder<Block, Block> light_metal_ironage_sconce_wall_soultorch_iron = register("light_metal_ironage_sconce_wall_soultorch_iron", path -> new LightSourceSconceSoulTorchWall(1, 10, SoundType.METAL, path), false);
	public static DeferredHolder<Block, Block> obsidian_chunk = register("obsidian_chunk", path -> new ObsideanLump(1, 10, SoundType.STONE, path), true);
}
