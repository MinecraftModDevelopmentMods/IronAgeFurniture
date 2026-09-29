package zone.moddev.mc.ironagefurniture.init;

import zone.moddev.mc.ironagefurniture.BlockObjectHolder;
import zone.moddev.mc.ironagefurniture.IronAgeFurnitureConfiguration;
import zone.moddev.mc.ironagefurniture.ItemObjectHolder;
import zone.moddev.mc.ironagefurniture.api.MetalVariantHelper;
import zone.moddev.mc.ironagefurniture.api.MetalVariantHelper.MetalVariant;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

/** Recipes for the additive lighting features; Phase 3 iron recipes remain unchanged. */
public final class PhaseFourLightingRecipes {
	private PhaseFourLightingRecipes() {
	}

	public static void register() {
		if (!IronAgeFurnitureConfiguration.GENERATE_LIGHTS) return;
		if (IronAgeFurnitureConfiguration.GENERATE_SCONCES) {
			for (MetalVariant metal : MetalVariantHelper.getAvailableVariants()) {
				if (metal == MetalVariant.IRON) continue;
				Object nugget = metal == MetalVariant.GOLD ? Items.GOLD_NUGGET : metal.getNuggetOreName();
				if (metal != MetalVariant.GOLD && (!OreDictionary.doesOreNameExist(metal.getNuggetOreName())
						|| OreDictionary.getOres(metal.getNuggetOreName()).isEmpty())) continue;
				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(
					BlockObjectHolder.light_metal_ironage_sconce_floor_empty_iron, 4, metal.getMeta()),
					"xxx", "x  ", "x  ", 'x', nugget));
			}
		}
		if (IronAgeFurnitureConfiguration.GENERATE_CANDLES && ItemObjectHolder.tallow != null
				&& BlockObjectHolder.light_metal_ironage_candle_floor != null) {
			GameRegistry.addSmelting(Items.COOKED_PORKCHOP, new ItemStack(ItemObjectHolder.tallow, 3), 0.1F);
			GameRegistry.addSmelting(Items.COOKED_BEEF, new ItemStack(ItemObjectHolder.tallow, 2), 0.1F);
			GameRegistry.addSmelting(Items.COOKED_MUTTON, new ItemStack(ItemObjectHolder.tallow, 2), 0.1F);
			GameRegistry.addSmelting(Items.COOKED_RABBIT, new ItemStack(ItemObjectHolder.tallow, 1), 0.1F);
			GameRegistry.addSmelting(Items.COOKED_CHICKEN, new ItemStack(ItemObjectHolder.tallow, 1), 0.1F);
			GameRegistry.addSmelting(Items.ROTTEN_FLESH, new ItemStack(ItemObjectHolder.tallow, 1), 0.1F);
			GameRegistry.addRecipe(new ShapelessOreRecipe(
				new ItemStack(BlockObjectHolder.light_metal_ironage_candle_floor, 8),
				ItemObjectHolder.tallow, Items.STRING));
		}
	}
}
