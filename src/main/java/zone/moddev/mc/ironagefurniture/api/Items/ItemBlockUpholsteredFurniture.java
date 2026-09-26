package zone.moddev.mc.ironagefurniture.api.Items;

import java.util.List;
import zone.moddev.mc.ironagefurniture.api.Enumerations.UpholsteryColour;
import zone.moddev.mc.ironagefurniture.api.UpholsteryColourHelper;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.translation.I18n;

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

	@Override public void getSubItems(Item item, CreativeTabs tab, List<ItemStack> subItems) {
		for (UpholsteryColour colour : UpholsteryColour.values())
			subItems.add(UpholsteryColourHelper.createStack(block, 1, colour));
	}
}
