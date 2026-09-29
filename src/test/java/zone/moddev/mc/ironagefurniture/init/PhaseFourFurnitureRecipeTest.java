package zone.moddev.mc.ironagefurniture.init;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

import java.util.ArrayList;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import zone.moddev.mc.ironagefurniture.BlockObjectHolder;
import zone.moddev.mc.ironagefurniture.IronAgeFurnitureConfiguration;
import zone.moddev.mc.ironagefurniture.Ironagefurniture;
import zone.moddev.mc.ironagefurniture.api.Enumerations.UpholsteryColour;
import zone.moddev.mc.ironagefurniture.api.UpholsteryColourHelper;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Bootstrap;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;

/** Tests the real Phase 4 registration path, not just a recipe helper. */
public class PhaseFourFurnitureRecipeTest {
	@BeforeClass public static void bootstrap() { Bootstrap.register(); }

	@Test public void wingbackAndThroneCraftForEveryCarpetColour() {
		List<IRecipe> recipes = CraftingManager.getInstance().getRecipeList();
		List<IRecipe> original = new ArrayList<IRecipe>(recipes);
		Block previousClassic = Ironagefurniture.BlockRegistry.get("chair_wood_ironage_classic_oak");
		Block previousWingback = BlockObjectHolder.chair_wood_ironage_wingback.get("oak");
		Block previousThrone = BlockObjectHolder.chair_wood_ironage_throne.get("oak");
		boolean previousClassicOption = IronAgeFurnitureConfiguration.GENERATE_CLASSIC_CHAIRS;
		boolean previousWingbackOption = IronAgeFurnitureConfiguration.GENERATE_WINGBACK_CHAIRS;
		boolean previousThroneOption = IronAgeFurnitureConfiguration.GENERATE_THRONES;
		boolean previousBop = IronAgeFurnitureConfiguration.INTEGRATION_BIOMESOPLENTY;
		boolean previousNatura = IronAgeFurnitureConfiguration.INTEGRATION_NATURA;
		boolean previousForestry = IronAgeFurnitureConfiguration.INTEGRATION_FORESTRY;
		boolean previousImmersive = IronAgeFurnitureConfiguration.INTEGRATION_IMMERSIVEENGINEERING;
		try {
			IronAgeFurnitureConfiguration.GENERATE_CLASSIC_CHAIRS = true;
			IronAgeFurnitureConfiguration.GENERATE_WINGBACK_CHAIRS = true;
			IronAgeFurnitureConfiguration.GENERATE_THRONES = true;
			IronAgeFurnitureConfiguration.INTEGRATION_BIOMESOPLENTY = false;
			IronAgeFurnitureConfiguration.INTEGRATION_NATURA = false;
			IronAgeFurnitureConfiguration.INTEGRATION_FORESTRY = false;
			IronAgeFurnitureConfiguration.INTEGRATION_IMMERSIVEENGINEERING = false;
			Ironagefurniture.BlockRegistry.put("chair_wood_ironage_classic_oak", Blocks.BRICK_BLOCK);
			BlockObjectHolder.chair_wood_ironage_wingback.put("oak", Blocks.STONE);
			BlockObjectHolder.chair_wood_ironage_throne.put("oak", Blocks.COBBLESTONE);
			PhaseFourFurnitureInitialiser.registerRecipes();
			List<IRecipe> added = new ArrayList<IRecipe>(recipes);
			added.removeAll(original);

			for (UpholsteryColour colour : UpholsteryColour.values()) {
				ItemStack wingback = craft(added, grid(colour, new ItemStack(Blocks.PLANKS, 1, 0),
						new ItemStack(Blocks.BRICK_BLOCK)), Blocks.STONE);
				assertNotNull("wingback " + colour, wingback);
				assertSame(colour, UpholsteryColourHelper.getColour(wingback));
				ItemStack actualWingback = CraftingManager.getInstance()
						.findMatchingRecipe(grid(colour, new ItemStack(Blocks.PLANKS, 1, 0),
								new ItemStack(Blocks.BRICK_BLOCK)), null);
				assertNotNull("crafting manager wingback " + colour, actualWingback);
				assertSame(wingback.getItem(), actualWingback.getItem());
				assertSame(colour, UpholsteryColourHelper.getColour(actualWingback));
				ItemStack throne = craft(added, grid(colour, new ItemStack(Blocks.PLANKS, 1, 0),
						wingback), Blocks.COBBLESTONE);
				assertNotNull("throne " + colour, throne);
				assertSame(colour, UpholsteryColourHelper.getColour(throne));
				ItemStack actualThrone = CraftingManager.getInstance()
						.findMatchingRecipe(grid(colour, new ItemStack(Blocks.PLANKS, 1, 0),
								wingback), null);
				assertNotNull("crafting manager throne " + colour, actualThrone);
				assertSame(throne.getItem(), actualThrone.getItem());
				assertSame(colour, UpholsteryColourHelper.getColour(actualThrone));
			}
			assertEquals(32, added.size());
			assertFalse(matchesAny(added, grid(UpholsteryColour.RED,
					new ItemStack(Blocks.PLANKS, 1, 1), new ItemStack(Blocks.BRICK_BLOCK))));
			IronAgeFurnitureConfiguration.GENERATE_WINGBACK_CHAIRS = false;
			PhaseFourFurnitureInitialiser.registerRecipes();
			assertEquals(original.size() + 32, recipes.size());
		} finally {
			recipes.clear();
			recipes.addAll(original);
			restore(Ironagefurniture.BlockRegistry, "chair_wood_ironage_classic_oak", previousClassic);
			restore(BlockObjectHolder.chair_wood_ironage_wingback, "oak", previousWingback);
			restore(BlockObjectHolder.chair_wood_ironage_throne, "oak", previousThrone);
			IronAgeFurnitureConfiguration.GENERATE_CLASSIC_CHAIRS = previousClassicOption;
			IronAgeFurnitureConfiguration.GENERATE_WINGBACK_CHAIRS = previousWingbackOption;
			IronAgeFurnitureConfiguration.GENERATE_THRONES = previousThroneOption;
			IronAgeFurnitureConfiguration.INTEGRATION_BIOMESOPLENTY = previousBop;
			IronAgeFurnitureConfiguration.INTEGRATION_NATURA = previousNatura;
			IronAgeFurnitureConfiguration.INTEGRATION_FORESTRY = previousForestry;
			IronAgeFurnitureConfiguration.INTEGRATION_IMMERSIVEENGINEERING = previousImmersive;
		}
	}

	@Test public void woodenAndCanopyBedsCraftCombineAndRecolour() {
		List<IRecipe> recipes = CraftingManager.getInstance().getRecipeList();
		List<IRecipe> original = new ArrayList<IRecipe>(recipes);
		boolean previousClassicOption = IronAgeFurnitureConfiguration.GENERATE_CLASSIC_CHAIRS;
		boolean previousBop = IronAgeFurnitureConfiguration.INTEGRATION_BIOMESOPLENTY;
		boolean previousNatura = IronAgeFurnitureConfiguration.INTEGRATION_NATURA;
		boolean previousForestry = IronAgeFurnitureConfiguration.INTEGRATION_FORESTRY;
		boolean previousImmersive = IronAgeFurnitureConfiguration.INTEGRATION_IMMERSIVEENGINEERING;
		Block previousWoodSingle = BlockObjectHolder.bed_wood_single.get("oak");
		Block previousWoodDouble = BlockObjectHolder.bed_wood_double.get("oak");
		Block previousCanopySingle = BlockObjectHolder.bed_canopy_single.get("oak");
		Block previousCanopyDouble = BlockObjectHolder.bed_canopy_double_left.get("oak");
		try {
			IronAgeFurnitureConfiguration.GENERATE_CLASSIC_CHAIRS = false;
			IronAgeFurnitureConfiguration.INTEGRATION_BIOMESOPLENTY = false;
			IronAgeFurnitureConfiguration.INTEGRATION_NATURA = false;
			IronAgeFurnitureConfiguration.INTEGRATION_FORESTRY = false;
			IronAgeFurnitureConfiguration.INTEGRATION_IMMERSIVEENGINEERING = false;
			BlockObjectHolder.bed_wood_single.put("oak", Blocks.STONE);
			BlockObjectHolder.bed_wood_double.put("oak", Blocks.COBBLESTONE);
			BlockObjectHolder.bed_canopy_single.put("oak", Blocks.BRICK_BLOCK);
			BlockObjectHolder.bed_canopy_double_left.put("oak", Blocks.GOLD_BLOCK);
			PhaseFourFurnitureInitialiser.registerRecipes();
			List<IRecipe> added = new ArrayList<IRecipe>(recipes);
			added.removeAll(original);

			ItemStack wood = craft(added, shapeless(new ItemStack(Items.BED),
					new ItemStack(Blocks.PLANKS, 1, 0)), Blocks.STONE);
			assertNotNull("single wooden bed", wood);
			assertSame(UpholsteryColour.RED, UpholsteryColourHelper.getColour(wood));
			assertNotNull("double wooden bed", craft(added, shapeless(wood, wood.copy()),
					Blocks.COBBLESTONE));
			for (UpholsteryColour colour : UpholsteryColour.values()) {
				ItemStack carpet = new ItemStack(Blocks.CARPET, 1, colour.getCarpetMetadata());
				ItemStack canopy = craft(added, shapeless(new ItemStack(Items.BED),
						new ItemStack(Blocks.PLANKS, 1, 0), carpet), Blocks.BRICK_BLOCK);
				assertNotNull("single canopy " + colour, canopy);
				assertSame(colour, UpholsteryColourHelper.getColour(canopy));
				ItemStack doubleCanopy = craft(added, shapeless(canopy, canopy.copy()), Blocks.GOLD_BLOCK);
				assertNotNull("double canopy " + colour, doubleCanopy);
				assertSame(colour, UpholsteryColourHelper.getColour(doubleCanopy));
				for (Block bed : new Block[] { Blocks.STONE, Blocks.COBBLESTONE,
						Blocks.BRICK_BLOCK, Blocks.GOLD_BLOCK }) {
					ItemStack recoloured = craft(added, shapeless(new ItemStack(bed), carpet), bed);
					assertNotNull("recolour " + bed + " " + colour, recoloured);
					assertSame(colour, UpholsteryColourHelper.getColour(recoloured));
				}
			}
		} finally {
			recipes.clear();
			recipes.addAll(original);
			restore(BlockObjectHolder.bed_wood_single, "oak", previousWoodSingle);
			restore(BlockObjectHolder.bed_wood_double, "oak", previousWoodDouble);
			restore(BlockObjectHolder.bed_canopy_single, "oak", previousCanopySingle);
			restore(BlockObjectHolder.bed_canopy_double_left, "oak", previousCanopyDouble);
			IronAgeFurnitureConfiguration.GENERATE_CLASSIC_CHAIRS = previousClassicOption;
			IronAgeFurnitureConfiguration.INTEGRATION_BIOMESOPLENTY = previousBop;
			IronAgeFurnitureConfiguration.INTEGRATION_NATURA = previousNatura;
			IronAgeFurnitureConfiguration.INTEGRATION_FORESTRY = previousForestry;
			IronAgeFurnitureConfiguration.INTEGRATION_IMMERSIVEENGINEERING = previousImmersive;
		}
	}

	private static InventoryCrafting grid(UpholsteryColour colour, ItemStack plank, ItemStack chair) {
		InventoryCrafting grid = new InventoryCrafting(new Container() {
			@Override public boolean canInteractWith(EntityPlayer player) { return false; }
		}, 3, 3);
		grid.setInventorySlotContents(1, new ItemStack(Blocks.CARPET, 1, colour.getCarpetMetadata()));
		grid.setInventorySlotContents(4, plank);
		grid.setInventorySlotContents(7, chair);
		return grid;
	}

	private static InventoryCrafting shapeless(ItemStack... ingredients) {
		InventoryCrafting grid = new InventoryCrafting(new Container() {
			@Override public boolean canInteractWith(EntityPlayer player) { return false; }
		}, 3, 3);
		for (int i = 0; i < ingredients.length; ++i) grid.setInventorySlotContents(i, ingredients[i]);
		return grid;
	}

	private static ItemStack craft(List<IRecipe> recipes, InventoryCrafting grid, Block result) {
		for (IRecipe recipe : recipes) {
			if (recipe.matches(grid, null) && recipe.getCraftingResult(grid).getItem()
					== net.minecraft.item.Item.getItemFromBlock(result)) {
				return recipe.getCraftingResult(grid);
			}
		}
		return null;
	}

	private static boolean matchesAny(List<IRecipe> recipes, InventoryCrafting grid) {
		for (IRecipe recipe : recipes) if (recipe.matches(grid, null)) return true;
		return false;
	}

	private static void restore(java.util.Map<String, Block> map, String key, Block value) {
		if (value == null) map.remove(key);
		else map.put(key, value);
	}
}
