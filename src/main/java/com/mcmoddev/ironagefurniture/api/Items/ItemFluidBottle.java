package com.mcmoddev.ironagefurniture.api.Items;

import java.util.List;

import javax.annotation.Nullable;

import com.mcmoddev.ironagefurniture.api.DrinkDisplayHelper;
import com.mcmoddev.ironagefurniture.api.DrinkProperties;
import com.mcmoddev.ironagefurniture.api.FoudreBrewingRegistry;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidHandlerItemStackSimple;

public class ItemFluidBottle extends Item {
	public static final int CAPACITY = Fluid.BUCKET_VOLUME / 4;
	public static final String LABEL_TAG = DrinkContainerHelper.BOTTLE_LABEL_TAG;

	public ItemFluidBottle() {
		this.setMaxStackSize(64);
	}

	@Override
	public int getItemStackLimit(ItemStack stack) {
		return 64;
	}

	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt) {
		return new FluidHandlerItemStackSimple(stack, CAPACITY);
	}

	@Override
	public String getItemStackDisplayName(ItemStack stack) {
		FluidStack fluid = getFluid(stack);
		return fluid != null && fluid.getFluid() != null ? DrinkDisplayHelper.getDisplayName(fluid) + " Bottle"
			: super.getItemStackDisplayName(stack);
	}

	@Override
	public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityLivingBase entityLiving) {
		EntityPlayer player = entityLiving instanceof EntityPlayer ? (EntityPlayer)entityLiving : null;

		if (!isFilled(stack) || !FoudreBrewingRegistry.isDrinkable(getFluid(stack))) {
			return stack;
		}

		if (player != null) {
			player.addStat(StatList.getObjectUseStats(this));
			DrinkProperties.consume(player, getFluid(stack), CAPACITY);
		}

		if (player == null || !player.capabilities.isCreativeMode) {
			stack.stackSize--;

			if (stack.stackSize <= 0) {
				return new ItemStack(Items.GLASS_BOTTLE);
			}

			if (player != null) {
				giveOrDrop(player, new ItemStack(Items.GLASS_BOTTLE));
			}
		}

		return stack;
	}

	@Override
	public int getMaxItemUseDuration(ItemStack stack) {
		return 32;
	}

	@Override
	public EnumAction getItemUseAction(ItemStack stack) {
		return EnumAction.DRINK;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn,
			EnumHand hand) {
		if (!isFilled(itemStackIn) || !FoudreBrewingRegistry.isDrinkable(getFluid(itemStackIn))) {
			return new ActionResult<ItemStack>(EnumActionResult.PASS, itemStackIn);
		}

		playerIn.setActiveHand(hand);
		return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemStackIn);
	}

	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		if (!worldIn.isRemote) {
			normalizeFluidAge(stack);
		}
	}

	@Override
	public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
		FluidStack fluid = getFluid(stack);

		if (fluid != null && fluid.getFluid() != null && fluid.amount > 0) {
			tooltip.add(DrinkDisplayHelper.getDisplayName(fluid));
			this.addQualityTooltip(fluid, tooltip);
			DrinkProperties.addTooltip(fluid, fluid.amount, tooltip);
			tooltip.add(fluid.amount + " / " + CAPACITY + " mB");
		}

		String label = getBottleLabel(stack);

		if (label != null && !label.isEmpty()) {
			tooltip.add(TextFormatting.GRAY + "Bottled at " + label);
		}
	}

	public static boolean isFilled(ItemStack stack) {
		return DrinkContainerHelper.isFilled(stack);
	}

	@Nullable
	public static FluidStack getFluid(ItemStack stack) {
		return DrinkContainerHelper.getFluid(stack);
	}

	public static int getFluidColor(ItemStack stack, int fallback) {
		return DrinkContainerHelper.getFluidColor(stack, fallback);
	}

	@Nullable
	public static String getBottleLabel(ItemStack stack) {
		return DrinkContainerHelper.getContainerLabel(stack);
	}

	public static void setBottleLabel(ItemStack stack, @Nullable String label) {
		DrinkContainerHelper.setContainerLabel(stack, label);
	}

	public static boolean tryUseWithTank(ItemStack heldItem, IFluidHandler tank, EntityPlayer player,
			EnumHand hand, @Nullable String fillLabel) {
		return DrinkContainerHelper.tryUseWithTank(heldItem, tank, player, hand, fillLabel);
	}

	private static void normalizeFluidAge(ItemStack stack) {
		DrinkContainerHelper.normalizeFluidAge(stack);
	}

	private void addQualityTooltip(FluidStack fluid, List<String> tooltip) {
		for (String line : DrinkDisplayHelper.getQualityTooltipLines(fluid)) {
			if (!line.isEmpty()) {
				tooltip.add(TextFormatting.GRAY + line);
			}
		}
	}

	private static void giveOrDrop(EntityPlayer player, @Nullable ItemStack stack) {
		DrinkContainerHelper.giveOrDrop(player, stack);
	}
}
