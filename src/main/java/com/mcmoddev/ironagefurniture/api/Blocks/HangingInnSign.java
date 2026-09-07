package com.mcmoddev.ironagefurniture.api.Blocks;

import com.mcmoddev.ironagefurniture.Ironagefurniture;
import com.mcmoddev.ironagefurniture.api.InnkeeperManager;
import com.mcmoddev.ironagefurniture.api.tile.TileEntityHangingInnSign;

import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class HangingInnSign extends BlockHBase implements ITileEntityProvider {
	private static final AxisAlignedBB NORTH_SOUTH =
		new AxisAlignedBB(0.0D, 0.1125D, 0.35D, 1.0D, 1.0D, 0.65D);
	private static final AxisAlignedBB EAST_WEST =
		new AxisAlignedBB(0.35D, 0.1125D, 0.0D, 0.65D, 1.0D, 1.0D);

	public HangingInnSign(String name) {
		super(Material.WOOD);
		this.setHardness(1.0F);
		this.setResistance(5.0F);
		this.setSoundType(SoundType.WOOD);
		this.setCreativeTab(Ironagefurniture.ironagefurnitureTab);
		this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
	}

	@Override public TileEntity createNewTileEntity(World worldIn, int meta) { return new TileEntityHangingInnSign(); }

	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing side, float hitX, float hitY,
			float hitZ, int meta, EntityLivingBase placer, ItemStack stack) {
		EnumFacing facing = side.getAxis().isHorizontal()
			? side.rotateY()
			: placer.getHorizontalFacing();
		return this.getDefaultState().withProperty(FACING, facing);
	}

	@Override
	public boolean canPlaceBlockOnSide(World world, BlockPos pos, EnumFacing side) {
		if (!side.getAxis().isHorizontal()) {
			return false;
		}

		BlockPos supportPos = pos.offset(side.getOpposite());
		return world.getBlockState(supportPos).isSideSolid(world, supportPos, side)
			&& super.canPlaceBlockOnSide(world, pos, side);
	}

	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
		TileEntity tile = world.getTileEntity(pos);
		if (tile instanceof TileEntityHangingInnSign && placer instanceof EntityPlayer) {
			TileEntityHangingInnSign sign = (TileEntityHangingInnSign)tile;
			sign.setOwner(placer.getUniqueID());
			sign.setInnName(stack != null && stack.hasDisplayName() ? stack.getDisplayName() : "The Inn");

			if (!world.isRemote) {
				((EntityPlayer)placer).openEditSign(sign);
			}
		}
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player,
			EnumHand hand, ItemStack held, EnumFacing side, float hitX, float hitY, float hitZ) {
		TileEntity tile = world.getTileEntity(pos);
		if (!(tile instanceof TileEntityHangingInnSign)) return false;
		TileEntityHangingInnSign sign = (TileEntityHangingInnSign)tile;
		if (world.isRemote) return true;
		if (!sign.isOwner(player.getUniqueID())) {
			player.sendStatusMessage(new TextComponentString("Only the inn owner can assign its keeper."));
			return true;
		}
		if (sign.hasKeeper()) {
			InnkeeperManager.release(sign);
			player.sendStatusMessage(new TextComponentString("The Innkeeper has been released."));
		} else if (InnkeeperManager.assignNearest(sign, player)) {
			player.sendStatusMessage(new TextComponentString("The nearest adult villager is now the Innkeeper."));
		} else {
			player.sendStatusMessage(new TextComponentString("No available adult villager is within the inn."));
		}
		return true;
	}

	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state) {
		TileEntity tile = world.getTileEntity(pos);
		if (!world.isRemote && tile instanceof TileEntityHangingInnSign) InnkeeperManager.release((TileEntityHangingInnSign)tile);
		super.breakBlock(world, pos, state);
	}

	@Override
	public void neighborChanged(IBlockState state, World world, BlockPos pos, net.minecraft.block.Block blockIn) {
		EnumFacing supportDirection = state.getValue(FACING).rotateY();
		BlockPos supportPos = pos.offset(supportDirection);

		if (!world.getBlockState(supportPos).isSideSolid(world, supportPos, supportDirection.getOpposite())) {
			if (!world.isRemote) {
				this.dropBlockAsItem(world, pos, state, 0);
			}
			world.setBlockToAir(pos);
			return;
		}

		super.neighborChanged(state, world, pos, blockIn);
	}

	@Override public IBlockState getStateFromMeta(int meta) { return this.getDefaultState().withProperty(FACING, EnumFacing.getHorizontal(meta & 3)); }
	@Override public int getMetaFromState(IBlockState state) { return state.getValue(FACING).getHorizontalIndex(); }
	@Override protected BlockStateContainer createBlockState() { return new BlockStateContainer(this, FACING); }
	@Override public boolean isOpaqueCube(IBlockState state) { return false; }
	@Override public boolean isFullCube(IBlockState state) { return false; }
	@Override public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return state.getValue(FACING).getAxis() == EnumFacing.Axis.Z ? NORTH_SOUTH : EAST_WEST;
	}
}
