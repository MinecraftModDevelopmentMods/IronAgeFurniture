package zone.moddev.mc.ironagefurniture;

import net.minecraft.world.level.block.Block;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;

/**
 * Holds the always-present blocks used directly by gameplay and rendering code.
 * Furniture blocks are resolved from the registry after their optional catalog
 * has been registered, avoiding missing-object lookups when an integration is
 * not installed.
 */
@Mod.EventBusSubscriber(modid = Ironagefurniture.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
@ObjectHolder(Ironagefurniture.MODID)
public final class BlockObjectHolder {
	public static final Block light_metal_ironage_sconce_floor_empty_iron = null;
	public static final Block light_metal_ironage_sconce_wall_empty_iron = null;

	public static final Block light_metal_ironage_sconce_floor_torch_iron = null;
	public static final Block light_metal_ironage_sconce_floor_torch_iron_unlit = null;
	public static final Block light_metal_ironage_sconce_wall_torch_iron = null;
	public static final Block light_metal_ironage_sconce_wall_torch_iron_unlit = null;

	public static final Block light_metal_ironage_sconce_floor_redtorch_iron = null;
	public static final Block light_metal_ironage_sconce_floor_redtorch_iron_unlit = null;
	public static final Block light_metal_ironage_sconce_wall_redtorch_iron = null;
	public static final Block light_metal_ironage_sconce_wall_redtorch_iron_unlit = null;

	public static final Block light_metal_ironage_block_floor_glow_clear = null;
	public static final Block light_metal_ironage_sconce_floor_glow_iron = null;
	public static final Block light_metal_ironage_sconce_wall_glow_iron = null;

	public static final Block light_metal_ironage_block_floor_lava_clear = null;
	public static final Block light_metal_ironage_sconce_floor_lava_iron = null;
	public static final Block light_metal_ironage_sconce_wall_lava_iron = null;

	public static final Block light_metal_ironage_block_floor_red_clear = null;
	public static final Block light_metal_ironage_block_floor_red_clear_one = null;
	public static final Block light_metal_ironage_block_floor_red_clear_two = null;
	public static final Block light_metal_ironage_block_floor_red_clear_three = null;
	public static final Block light_metal_ironage_block_floor_red_clear_four = null;
	public static final Block light_metal_ironage_block_floor_red_clear_five = null;
	public static final Block light_metal_ironage_block_floor_red_clear_six = null;
	public static final Block light_metal_ironage_block_floor_red_clear_seven = null;
	public static final Block light_metal_ironage_block_floor_red_clear_eight = null;
	public static final Block light_metal_ironage_block_floor_red_clear_nine = null;
	public static final Block light_metal_ironage_block_floor_red_clear_ten = null;
	public static final Block light_metal_ironage_block_floor_red_clear_eleven = null;
	public static final Block light_metal_ironage_block_floor_red_clear_twelve = null;
	public static final Block light_metal_ironage_block_floor_red_clear_thirteen = null;
	public static final Block light_metal_ironage_block_floor_red_clear_fourteen = null;
	public static final Block light_metal_ironage_block_floor_red_clear_fifteen = null;

	public static final Block light_metal_ironage_sconce_floor_red_iron = null;
	public static final Block light_metal_ironage_sconce_floor_red_iron_one = null;
	public static final Block light_metal_ironage_sconce_floor_red_iron_two = null;
	public static final Block light_metal_ironage_sconce_floor_red_iron_three = null;
	public static final Block light_metal_ironage_sconce_floor_red_iron_four = null;
	public static final Block light_metal_ironage_sconce_floor_red_iron_five = null;
	public static final Block light_metal_ironage_sconce_floor_red_iron_six = null;
	public static final Block light_metal_ironage_sconce_floor_red_iron_seven = null;
	public static final Block light_metal_ironage_sconce_floor_red_iron_eight = null;
	public static final Block light_metal_ironage_sconce_floor_red_iron_nine = null;
	public static final Block light_metal_ironage_sconce_floor_red_iron_ten = null;
	public static final Block light_metal_ironage_sconce_floor_red_iron_eleven = null;
	public static final Block light_metal_ironage_sconce_floor_red_iron_twelve = null;
	public static final Block light_metal_ironage_sconce_floor_red_iron_thirteen = null;
	public static final Block light_metal_ironage_sconce_floor_red_iron_fourteen = null;
	public static final Block light_metal_ironage_sconce_floor_red_iron_fifteen = null;

	public static final Block light_metal_ironage_sconce_wall_red_iron = null;
	public static final Block light_metal_ironage_sconce_wall_red_iron_one = null;
	public static final Block light_metal_ironage_sconce_wall_red_iron_two = null;
	public static final Block light_metal_ironage_sconce_wall_red_iron_three = null;
	public static final Block light_metal_ironage_sconce_wall_red_iron_four = null;
	public static final Block light_metal_ironage_sconce_wall_red_iron_five = null;
	public static final Block light_metal_ironage_sconce_wall_red_iron_six = null;
	public static final Block light_metal_ironage_sconce_wall_red_iron_seven = null;
	public static final Block light_metal_ironage_sconce_wall_red_iron_eight = null;
	public static final Block light_metal_ironage_sconce_wall_red_iron_nine = null;
	public static final Block light_metal_ironage_sconce_wall_red_iron_ten = null;
	public static final Block light_metal_ironage_sconce_wall_red_iron_eleven = null;
	public static final Block light_metal_ironage_sconce_wall_red_iron_twelve = null;
	public static final Block light_metal_ironage_sconce_wall_red_iron_thirteen = null;
	public static final Block light_metal_ironage_sconce_wall_red_iron_fourteen = null;
	public static final Block light_metal_ironage_sconce_wall_red_iron_fifteen = null;

	public static final Block light_metal_ironage_sconce_floor_soultorch_iron = null;
	public static final Block light_metal_ironage_sconce_wall_soultorch_iron = null;
	public static final Block obsidian_chunk = null;

	// The creative-tab icon is always part of the vanilla furniture catalog.
	public static final Block chair_wood_ironage_classic_dark_oak = null;

	private BlockObjectHolder() {
	}
}
