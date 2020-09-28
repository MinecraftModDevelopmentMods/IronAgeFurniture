package com.mcmoddev.ironagefurniture.lib.util;

import java.util.Comparator;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.mcmoddev.ironagefurniture.lib.interfaces.IMMDCreativeTab;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * This class is an MMDMaterial based Wrapper for making a CreativeTab.
 *
 * @author Jasmine Iwanek
 *
 */
public class MMDCreativeTab extends CreativeTabs implements IMMDCreativeTab {
	private ItemStack iconItem;
	private Block iconBlock;
	
	private boolean searchable;
	private Comparator<ItemStack> comparator;

	public MMDCreativeTab(@Nonnull final String unlocalizedName, @Nonnull final boolean searchable) {
		this(unlocalizedName, searchable, (ItemStack) null);
	}

	public MMDCreativeTab(@Nonnull final String unlocalizedName, @Nonnull final boolean searchable, @Nullable final Block iconBlock) {
		this(unlocalizedName, searchable, new ItemStack(Item.getItemFromBlock(iconBlock)));
	}

	public MMDCreativeTab(@Nonnull final String unlocalizedName, @Nonnull final boolean searchable, @Nullable final Item iconItem) {
		this(unlocalizedName, searchable, new ItemStack(iconItem));
	}
	
	@Override
	public MMDCreativeTab Initialise() {
		if (searchable)
			setBackgroundImageName("item_search.png");
		
		return this;
	}
	
	public MMDCreativeTab(@Nonnull final String unlocalizedName, @Nonnull final boolean searchable, @Nullable final ItemStack iconItem) {
		super(unlocalizedName);
		
		if (iconItem != null) 
			this.iconItem = iconItem;
		
		this.searchable = searchable;
	}

	/* (non-Javadoc)
	 * @see com.mcmoddev.lib.util.IMMDCreativeTab#hasSearchBar()
	 */
	@Override
	public boolean hasSearchBar() {
		return searchable;
	}

	/* (non-Javadoc)
	 * @see com.mcmoddev.lib.util.IMMDCreativeTab#displayAllRelevantItems(net.minecraft.util.NonNullList)
	 */
	@Override
	@SideOnly(Side.CLIENT)
	public void displayAllRelevantItems(@Nonnull final NonNullList<ItemStack> itemList) {
		super.displayAllRelevantItems(itemList);
		if (comparator != null) {
			itemList.sort(comparator);
		}
	}

	/* (non-Javadoc)
	 * @see com.mcmoddev.lib.util.IMMDCreativeTab#createIcon()
	 */
	@Override
	@SideOnly(Side.CLIENT)
	public ItemStack createIcon() {
		return this.iconItem;
	}

	public MMDCreativeTab setIconItem(@Nonnull final Block iconBlock) {
		this.iconBlock = iconBlock;
		this.iconItem = new ItemStack(Item.getItemFromBlock(iconBlock));
		return this;
	}

	public MMDCreativeTab setIconItem(@Nonnull final Item iconItem) {
		this.iconItem = new ItemStack(iconItem);
		return this;
	}
	
	/* (non-Javadoc)
	 * @see com.mcmoddev.lib.util.IMMDCreativeTab#setTabIconItem(net.minecraft.block.Block)
	 */
	@Override
	public void setTabIconItem(@Nonnull final Block iconBlock) {
		this.iconItem = new ItemStack(Item.getItemFromBlock(iconBlock));
	}

	/* (non-Javadoc)
	 * @see com.mcmoddev.lib.util.IMMDCreativeTab#setTabIconItem(net.minecraft.item.Item)
	 */
	@Override
	public void setTabIconItem(@Nonnull final Item iconItem) {
		this.iconItem = new ItemStack(iconItem);
	}

	/* (non-Javadoc)
	 * @see com.mcmoddev.lib.util.IMMDCreativeTab#setTabIconItem(net.minecraft.item.ItemStack)
	 */
	@Override
	public void setTabIconItem(@Nonnull final ItemStack iconItem) {
		this.iconItem = iconItem;
	}

	@Override
	public void setTabIconItem() {
		if (this.iconBlock != null)
			this.setTabIconItem(this.iconBlock);
	}
}
