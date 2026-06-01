package com.mcmoddev.ironagefurniture.api.Blocks;

import java.util.List;

import com.mcmoddev.ironagefurniture.Ironagefurniture;
import com.mcmoddev.ironagefurniture.api.VasePlantHelper;
import com.mcmoddev.ironagefurniture.api.tile.TileEntityGlassVase;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class GlassVaseBlock extends BlockHBase {
	public static final PropertyEnum<EnumDyeColor> COLOR = PropertyEnum.<EnumDyeColor>create("color", EnumDyeColor.class);
	private static final AxisAlignedBB BOUNDS = new AxisAlignedBB(5.75D / 16.0D, 0.0D, 5.75D / 16.0D,
		10.25D / 16.0D, 6.0D / 16.0D, 10.25D / 16.0D);

	private final String name;

	public GlassVaseBlock(Material materialIn, String name, float resistance, float hardness) {
		super(materialIn);
		this.name = name;
		this.blockResistance = resistance;
		this.blockHardness = hardness;
		this.setSoundType(SoundType.GLASS);
		this.setCreativeTab(Ironagefurniture.ironagefurnitureTab);
		this.setDefaultState(this.blockState.getBaseState().withProperty(COLOR, EnumDyeColor.WHITE));
		this.setHarvestLevel("pickaxe", 0);
	}

	public int getVariantCount() {
		return EnumDyeColor.values().length;
	}

	public String getVariantName(int meta) {
		return this.getColor(meta).getName();
	}

	public String getModelName(int meta) {
		return this.name + "_" + this.getVariantName(meta);
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return this.getDefaultState().withProperty(COLOR, this.getColor(meta));
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return ((EnumDyeColor)state.getValue(COLOR)).getMetadata();
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] { COLOR });
	}

	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing side, float hitX, float hitY,
			float hitZ, int meta, EntityLivingBase placer, ItemStack stack) {
		return this.getDefaultState().withProperty(COLOR, this.getColor(stack == null ? meta : stack.getMetadata()));
	}

	@Override
	public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
		return super.canPlaceBlockAt(worldIn, pos) && this.hasTopSupport(worldIn, pos);
	}

	@Override
	public boolean canPlaceBlockOnSide(World worldIn, BlockPos pos, EnumFacing side) {
		return side == EnumFacing.UP && this.canPlaceBlockAt(worldIn, pos);
	}

	@Override
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, net.minecraft.block.Block blockIn) {
		if (!this.hasTopSupport(worldIn, pos)) {
			this.dropBlockAsItem(worldIn, pos, state, 0);
			worldIn.setBlockState(pos, Blocks.AIR.getDefaultState(), 3);
		}
	}

	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer,
			ItemStack stack) {
		if (worldIn.isRemote || !VasePlantHelper.hasPlant(stack)) {
			return;
		}

		TileEntity tileEntity = worldIn.getTileEntity(pos);

		if (tileEntity instanceof TileEntityGlassVase) {
			((TileEntityGlassVase)tileEntity).setPlant(VasePlantHelper.getPlant(stack));
		}
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
		if (playerIn.isSneaking()) {
			return false;
		}

		TileEntity tileEntity = worldIn.getTileEntity(pos);

		if (!(tileEntity instanceof TileEntityGlassVase)) {
			return false;
		}

		TileEntityGlassVase vase = (TileEntityGlassVase)tileEntity;

		if (vase.hasPlant()) {
			if (heldItem != null && heldItem.stackSize > 0
					&& !VasePlantHelper.isSamePlant(vase.getPlant(), heldItem)) {
				return false;
			}

			if (!worldIn.isRemote) {
				this.returnItem(worldIn, pos, playerIn, vase.removePlant());
			}

			return true;
		}

		if (!VasePlantHelper.isPlantItem(heldItem)) {
			return false;
		}

		if (!worldIn.isRemote) {
			vase.setPlant(VasePlantHelper.copyOne(heldItem));

			if (!playerIn.capabilities.isCreativeMode) {
				heldItem.stackSize--;

				if (heldItem.stackSize <= 0) {
					playerIn.setHeldItem(hand, null);
				}
			}
		}

		return true;
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return BOUNDS;
	}

	@Override
	public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox,
			List<AxisAlignedBB> collidingBoxes, Entity entityIn) {
		addCollisionBoxToList(pos, entityBox, collidingBoxes, BOUNDS);
	}

	@Override
	public int damageDropped(IBlockState state) {
		return ((EnumDyeColor)state.getValue(COLOR)).getMetadata();
	}

	@Override
	public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
		List<ItemStack> drops = super.getDrops(world, pos, state, fortune);
		TileEntity tileEntity = world.getTileEntity(pos);

		if (tileEntity instanceof TileEntityGlassVase) {
			ItemStack plant = ((TileEntityGlassVase)tileEntity).getPlant();

			if (plant != null && plant.stackSize > 0) {
				drops.add(plant.copy());
			}
		}

		return drops;
	}

	@Override
	public void getSubBlocks(Item itemIn, CreativeTabs tab, List<ItemStack> list) {
		for (EnumDyeColor color : EnumDyeColor.values()) {
			list.add(new ItemStack(itemIn, 1, color.getMetadata()));
		}
	}

	public int getDamageValue(World worldIn, BlockPos pos) {
		return ((EnumDyeColor)worldIn.getBlockState(pos).getValue(COLOR)).getMetadata();
	}

	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}

	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	@Override
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.CUTOUT;
	}

	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}

	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		return new TileEntityGlassVase();
	}

	private boolean hasTopSupport(World worldIn, BlockPos pos) {
		BlockPos supportPos = pos.down();
		return worldIn.getBlockState(supportPos).isSideSolid(worldIn, supportPos, EnumFacing.UP);
	}

	private EnumDyeColor getColor(int meta) {
		return EnumDyeColor.byMetadata(meta);
	}

	private void returnItem(World worldIn, BlockPos pos, EntityPlayer playerIn, ItemStack itemStack) {
		if (itemStack == null || itemStack.stackSize <= 0) {
			return;
		}

		if (!playerIn.inventory.addItemStackToInventory(itemStack)) {
			EntityItem entityItem = new EntityItem(worldIn, pos.getX() + 0.5D, pos.getY() + 0.5D,
				pos.getZ() + 0.5D, itemStack);
			worldIn.spawnEntity(entityItem);
		}
	}
}
