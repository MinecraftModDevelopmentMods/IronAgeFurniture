package zone.moddev.mc.ironagefurniture.api.Items;

import zone.moddev.mc.ironagefurniture.api.Blocks.OrnamentBlock;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemBlockOrnament extends ItemBlock {
	private final OrnamentBlock ornamentBlock;

	public ItemBlockOrnament(Block block) {
		super(block);
		this.ornamentBlock = (OrnamentBlock)block;
		this.setHasSubtypes(true);
		this.setMaxDamage(0);
	}

	@Override
	public int getMetadata(int damage) {
		return damage;
	}

	@Override
	public String getUnlocalizedName(ItemStack stack) {
		return this.block.getUnlocalizedName() + "." + this.ornamentBlock.getVariantName(stack.getMetadata());
	}

	public int getVariantCount() {
		return this.ornamentBlock.getVariantCount();
	}

	public String getModelName(int meta) {
		return this.ornamentBlock.getModelName(meta);
	}
}
