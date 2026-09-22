package zone.moddev.mc.ironagefurniture.api.Items;

import zone.moddev.mc.ironagefurniture.api.PaddedBenchColourHelper;
import zone.moddev.mc.ironagefurniture.api.Enumerations.PaddedBenchColour;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemBlockPaddedBench extends ItemBlock {
	public ItemBlockPaddedBench(Block block) {
		super(block);
		this.setHasSubtypes(true);
		this.setMaxDamage(0);
	}

	@Override
	public int getMetadata(int damage) {
		return 0;
	}

	@Override
	public String getTranslationKey(ItemStack stack) {
		PaddedBenchColour colour = PaddedBenchColourHelper.getColour(stack);
		String baseName = super.getTranslationKey(stack);
		return colour == PaddedBenchColour.RED
				? baseName : baseName + "." + colour.getSerializedName();
	}

	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> subItems) {
		if (!this.isInCreativeTab(tab)) {
			return;
		}
		for (PaddedBenchColour colour : PaddedBenchColour.creativeOrder()) {
			subItems.add(PaddedBenchColourHelper.setColour(
					new ItemStack(this, 1, colour.getItemMetadata()), colour));
		}
	}

	@Override
	public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, BlockPos pos,
			EnumFacing side, float hitX, float hitY, float hitZ, IBlockState newState) {
		if (!super.placeBlockAt(stack, player, world, pos, side, hitX, hitY, hitZ, newState)) {
			return false;
		}
		PaddedBenchColourHelper.setColour(world, pos,
				PaddedBenchColourHelper.getColour(stack));
		return true;
	}
}
