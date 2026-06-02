package com.mcmoddev.ironagefurniture.api.Items;

import java.util.List;

import com.mcmoddev.ironagefurniture.api.MetalVariantHelper;
import com.mcmoddev.ironagefurniture.api.MetalVariantHelper.MetalVariant;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemBlockMetalVariant extends ItemBlock {
	public ItemBlockMetalVariant(Block block) {
		super(block);
		this.setHasSubtypes(true);
		this.setMaxDamage(0);
	}

	@Override
	public int getMetadata(int damage) {
		return 0;
	}

	@Override
	public String getUnlocalizedName(ItemStack stack) {
		MetalVariant metal = MetalVariant.byMeta(stack.getMetadata());

		if (metal == MetalVariant.IRON) {
			return this.block.getUnlocalizedName();
		}

		return this.block.getUnlocalizedName() + "." + metal.getName();
	}

	@Override
	public void getSubItems(Item itemIn, CreativeTabs tab, List<ItemStack> subItems) {
		for (MetalVariant metal : MetalVariantHelper.getAvailableVariants()) {
			subItems.add(new ItemStack(itemIn, 1, metal.getMeta()));
		}
	}

	@Override
	public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, BlockPos pos,
			EnumFacing side, float hitX, float hitY, float hitZ, IBlockState newState) {
		MetalVariant metal = MetalVariant.byMeta(stack.getMetadata());

		if (!super.placeBlockAt(stack, player, world, pos, side, hitX, hitY, hitZ,
				MetalVariantHelper.withMetal(newState, metal))) {
			return false;
		}

		MetalVariantHelper.setMetal(world, pos, metal);
		return true;
	}

	public int getVariantCount() {
		return MetalVariantHelper.getAvailableVariants().size();
	}

	public int getVariantMeta(int index) {
		return MetalVariantHelper.getAvailableVariants().get(index).getMeta();
	}

	public String getModelName(int meta) {
		String blockName = this.block.getRegistryName().getResourcePath();
		MetalVariant metal = MetalVariant.byMeta(meta);
		return metal == MetalVariant.IRON ? blockName : blockName + "_" + metal.getName();
	}
}
