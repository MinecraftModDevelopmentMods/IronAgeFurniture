package zone.moddev.mc.ironagefurniture.api.Items;

import zone.moddev.mc.ironagefurniture.api.Blocks.GlassVaseBlock;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemBlockGlassVase extends ItemBlock {
	private final GlassVaseBlock vaseBlock;

	public ItemBlockGlassVase(Block block) {
		super(block);
		this.vaseBlock = (GlassVaseBlock)block;
		this.setHasSubtypes(true);
		this.setMaxDamage(0);
	}

	@Override
	public int getMetadata(int damage) {
		return damage;
	}

	@Override
	public String getUnlocalizedName(ItemStack stack) {
		return this.block.getUnlocalizedName() + "." + this.vaseBlock.getVariantName(stack.getMetadata());
	}

	public int getVariantCount() {
		return this.vaseBlock.getVariantCount();
	}

	public String getModelName(int meta) {
		return this.vaseBlock.getModelName(meta);
	}
}
