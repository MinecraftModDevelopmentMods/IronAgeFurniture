package zone.moddev.mc.ironagefurniture.api.Items;

import zone.moddev.mc.ironagefurniture.api.Enumerations.UpholsteryColour;
import zone.moddev.mc.ironagefurniture.api.UpholsteryColourHelper;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.translation.I18n;

/** A single block item carries all sixteen upholstery choices without extra registry IDs. */
public class ItemBlockUpholsteredFurniture extends ItemBlock {
	public ItemBlockUpholsteredFurniture(Block block) {
		super(block);
		setHasSubtypes(true);
		setMaxDamage(0);
	}

	@Override public int getMetadata(int damage) { return 0; }

	@Override public String getItemStackDisplayName(ItemStack stack) {
		UpholsteryColour colour = UpholsteryColourHelper.getColour(stack);
		String base = super.getItemStackDisplayName(stack);
		return colour == UpholsteryColour.RED ? base
				: I18n.translateToLocal("item.ironagefurniture.upholstery."
						+ colour.getSerializedName()) + " " + base;
	}

	@Override public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> subItems) {
		if (!isInCreativeTab(tab)) return;
		for (UpholsteryColour colour : UpholsteryColour.values())
			subItems.add(UpholsteryColourHelper.createStack(block, 1, colour));
	}
}
