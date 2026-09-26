package zone.moddev.mc.ironagefurniture.api.recipes;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;
import zone.moddev.mc.ironagefurniture.api.ShieldChairItemData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Bootstrap;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class ShieldChairRecipeTest {
	@BeforeClass public static void bootstrap() { Bootstrap.register(); }

	private InventoryCrafting inventory(ItemStack shield) {
		InventoryCrafting grid = new InventoryCrafting(new Container() {
			@Override public boolean canInteractWith(EntityPlayer player) { return false; }
		}, 2, 1);
		grid.setInventorySlotContents(0, new ItemStack(Blocks.PLANKS));
		grid.setInventorySlotContents(1, shield);
		return grid;
	}

	@Test public void craftedChairCarriesTheExactShield() {
		ItemStack shield = new ItemStack(Items.SHIELD);
		shield.setItemDamage(43);
		NBTTagCompound banner = new NBTTagCompound();
		banner.setInteger("Base", 4);
		NBTTagCompound data = new NBTTagCompound();
		data.setTag("BlockEntityTag", banner);
		shield.setTagCompound(data);
		ShieldChairRecipe recipe = new ShieldChairRecipe(Blocks.PLANKS, Blocks.STONE);
		InventoryCrafting grid = inventory(shield);
		assertTrue(recipe.matches(grid, null));
		ItemStack recovered = ShieldChairItemData.getShield(recipe.getCraftingResult(grid));
		assertEquals(shield.writeToNBT(new NBTTagCompound()), recovered.writeToNBT(new NBTTagCompound()));
	}

	@Test public void otherItemsCannotFillTheShieldSlot() {
		ShieldChairRecipe recipe = new ShieldChairRecipe(Blocks.PLANKS, Blocks.STONE);
		assertFalse(recipe.matches(inventory(new ItemStack(Items.BOOK)), null));
	}
}
