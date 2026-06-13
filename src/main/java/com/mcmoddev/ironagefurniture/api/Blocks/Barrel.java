package com.mcmoddev.ironagefurniture.api.Blocks;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import com.mcmoddev.ironagefurniture.Ironagefurniture;
import com.mcmoddev.ironagefurniture.api.BarrelFluidCompat;
import com.mcmoddev.ironagefurniture.api.Items.ItemFluidBottle;
import com.mcmoddev.ironagefurniture.api.tile.TileEntityBarrel;
import com.mcmoddev.ironagefurniture.api.tile.TileEntityFoudre;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;

public class Barrel extends Block {
	private static final AxisAlignedBB BARREL_AABB = new AxisAlignedBB(1.0D / 16.0D, 0.0D, 1.0D / 16.0D,
		15.0D / 16.0D, 1.0D, 15.0D / 16.0D);

	public Barrel(Material materialIn, String name, float resistance, float hardness) {
		super(materialIn);
		this.blockResistance = resistance;
		this.blockHardness = hardness;
		this.setSoundType(SoundType.WOOD);
		this.setHarvestLevel("axe", 0);
		this.setCreativeTab(Ironagefurniture.ironagefurnitureTab);
	}

	public int getBarrelCapacity() {
		return TileEntityBarrel.CAPACITY;
	}

	public int getEmptyItemStackLimit() {
		return 16;
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
		if (BarrelFluidCompat.isPamsFreshMilk(heldItem) || this.isBucketInteractionItem(heldItem)) {
			if (worldIn.isRemote) {
				return true;
			}

			TileEntityBarrel barrel = this.getBarrelEntity(worldIn, pos);

			if (barrel == null) {
				return false;
			}

			if (this.tryFillVanillaMilkBucket(barrel, playerIn, hand, heldItem)) {
				return true;
			}

			if (this.tryUsePamsFreshMilk(barrel, playerIn, hand, heldItem)) {
				return true;
			}

			if (heldItem.getItem() instanceof ItemFluidBottle) {
				String label = barrel instanceof TileEntityFoudre ? ((TileEntityFoudre)barrel).getBottleLabel() : null;

				if (ItemFluidBottle.tryUseWithTank(heldItem, barrel.getFluidHandler(), playerIn, hand, label)) {
					barrel.markForFluidUpdate();
				}

				return true;
			}

			if (FluidUtil.interactWithFluidHandler(heldItem, barrel.getFluidHandler(), playerIn)) {
				barrel.markForFluidUpdate();
			}

			return true;
		}

		if (!worldIn.isRemote) {
			BlockPos guiPos = this.getBarrelGuiPos(worldIn, pos, state);
			playerIn.openGui(Ironagefurniture.instance, Ironagefurniture.GUI_BARREL, worldIn,
				guiPos.getX(), guiPos.getY(), guiPos.getZ());
		}

		return true;
	}

	private boolean isBucketInteractionItem(ItemStack heldItem) {
		if (heldItem == null || heldItem.stackSize <= 0) {
			return false;
		}

		if (heldItem.getItem() instanceof ItemFluidBottle) {
			return true;
		}

		if (heldItem.getItem() == Items.BUCKET) {
			return true;
		}

		ItemStack singleItem = heldItem.copy();
		singleItem.stackSize = 1;
		return FluidUtil.getFluidHandler(singleItem) != null;
	}

	private boolean tryFillVanillaMilkBucket(TileEntityBarrel barrel, EntityPlayer playerIn, EnumHand hand,
			ItemStack heldItem) {
		if (heldItem == null || heldItem.stackSize <= 0 || heldItem.getItem() != Items.BUCKET) {
			return false;
		}

		Fluid milk = BarrelFluidCompat.getMilkFluid();

		if (milk == null) {
			return false;
		}

		FluidStack milkStack = new FluidStack(milk, Fluid.BUCKET_VOLUME);
		FluidStack drained = barrel.getFluidHandler().drain(milkStack, false);

		if (drained == null || drained.amount < Fluid.BUCKET_VOLUME) {
			return false;
		}

		barrel.getFluidHandler().drain(milkStack, true);
		playerIn.playSound(milk.getFillSound(milkStack), 1.0F, 1.0F);

		if (!playerIn.capabilities.isCreativeMode) {
			ItemStack milkBucket = new ItemStack(Items.MILK_BUCKET);
			heldItem.stackSize--;

			if (heldItem.stackSize <= 0) {
				playerIn.setHeldItem(hand, milkBucket);
			} else {
				this.giveOrDrop(playerIn, milkBucket);
			}
		}

		barrel.markForFluidUpdate();
		return true;
	}

	private boolean tryUsePamsFreshMilk(TileEntityBarrel barrel, EntityPlayer playerIn, EnumHand hand,
			ItemStack heldItem) {
		if (!BarrelFluidCompat.isPamsFreshMilk(heldItem)) {
			return false;
		}

		Fluid milk = BarrelFluidCompat.getMilkFluid();

		if (milk == null) {
			return true;
		}

		FluidStack milkStack = new FluidStack(milk, Fluid.BUCKET_VOLUME);
		int fillAmount = barrel.getFluidHandler().fill(milkStack, false);

		if (fillAmount < Fluid.BUCKET_VOLUME) {
			return true;
		}

		barrel.getFluidHandler().fill(milkStack, true);
		playerIn.playSound(milk.getEmptySound(milkStack), 1.0F, 1.0F);

		if (!playerIn.capabilities.isCreativeMode) {
			heldItem.stackSize--;

			if (heldItem.stackSize <= 0) {
				playerIn.setHeldItem(hand, new ItemStack(Items.BUCKET));
			} else {
				this.giveOrDrop(playerIn, new ItemStack(Items.BUCKET));
			}
		}

		barrel.markForFluidUpdate();
		return true;
	}

	private void giveOrDrop(EntityPlayer playerIn, ItemStack stack) {
		if (stack == null || stack.stackSize <= 0) {
			return;
		}

		if (!playerIn.inventory.addItemStackToInventory(stack)) {
			playerIn.dropItem(stack, false);
		}
	}

	protected TileEntityBarrel getBarrelEntity(IBlockAccess worldIn, BlockPos pos) {
		TileEntity tileEntity = worldIn.getTileEntity(pos);
		return tileEntity instanceof TileEntityBarrel ? (TileEntityBarrel)tileEntity : null;
	}

	protected BlockPos getBarrelGuiPos(IBlockAccess worldIn, BlockPos pos, IBlockState state) {
		return pos;
	}

	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer,
			ItemStack stack) {
		if (worldIn.isRemote) {
			return;
		}

		TileEntityBarrel barrel = this.getBarrelEntity(worldIn, pos);

		if (barrel != null) {
			barrel.readFromItemStack(stack);
		}
	}

	@Override
	public boolean removedByPlayer(IBlockState state, World world, BlockPos pos, EntityPlayer player,
			boolean willHarvest) {
		if (willHarvest) {
			this.onBlockHarvested(world, pos, state, player);
			return true;
		}

		return super.removedByPlayer(state, world, pos, player, willHarvest);
	}

	@Override
	public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state,
			@Nullable TileEntity te, @Nullable ItemStack stack) {
		super.harvestBlock(worldIn, player, pos, state, te, stack);
		worldIn.setBlockToAir(pos);
	}

	@Override
	public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
		List<ItemStack> drops = new ArrayList<ItemStack>();
		Item item = Item.getItemFromBlock(this);

		if (item == null) {
			return drops;
		}

		ItemStack drop = new ItemStack(item, 1, this.damageDropped(state));
		TileEntityBarrel barrel = this.getBarrelEntity(world, pos);

		if (barrel != null) {
			barrel.writeToItemStack(drop);
		}

		drops.add(drop);
		return drops;
	}

	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}

	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		return new TileEntityBarrel();
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return BARREL_AABB;
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, World worldIn, BlockPos pos) {
		return BARREL_AABB;
	}

	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}

	@Override
	public boolean hasComparatorInputOverride(IBlockState state) {
		return true;
	}

	@Override
	public int getComparatorInputOverride(IBlockState blockState, World worldIn, BlockPos pos) {
		TileEntityBarrel barrel = this.getBarrelEntity(worldIn, pos);

		if (barrel == null || barrel.getFluidAmount() <= 0) {
			return 0;
		}

		return MathHelper.floor((float)barrel.getFluidAmount() * 14.0F / (float)barrel.getCapacity()) + 1;
	}
}
