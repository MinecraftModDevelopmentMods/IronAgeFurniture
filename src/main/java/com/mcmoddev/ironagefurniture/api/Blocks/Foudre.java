package com.mcmoddev.ironagefurniture.api.Blocks;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import com.mcmoddev.ironagefurniture.api.Enumerations.FoudrePart;
import com.mcmoddev.ironagefurniture.api.tile.TileEntityBarrel;
import com.mcmoddev.ironagefurniture.api.tile.TileEntityFoudre;

import net.minecraft.block.Block;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class Foudre extends Barrel {
	public static final PropertyDirection FACING = BlockHorizontal.FACING;
	public static final PropertyEnum<FoudrePart> PART = PropertyEnum.<FoudrePart>create("part", FoudrePart.class);

	private static final AxisAlignedBB FULL_BLOCK_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
	private static final FoudrePart[] PARTS = FoudrePart.values();
	private static final Set<BlockPos> REMOVING_PARTS = new HashSet<BlockPos>();

	private final boolean upperLayer;
	private Foudre lowerBlock;
	private Foudre upperBlock;

	public Foudre(Material materialIn, String name, float resistance, float hardness, boolean upperLayer) {
		super(materialIn, name, resistance, hardness);
		this.upperLayer = upperLayer;
		this.lowerBlock = this;
		this.upperBlock = this;
		this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.SOUTH)
			.withProperty(PART, FoudrePart.FRONT_LEFT));
	}

	public void setCompanionBlocks(Foudre lowerBlock, Foudre upperBlock) {
		this.lowerBlock = lowerBlock;
		this.upperBlock = upperBlock;
	}

	@Override
	public int getBarrelCapacity() {
		return TileEntityFoudre.CAPACITY;
	}

	@Override
	public int getEmptyItemStackLimit() {
		return 1;
	}

	@Override
	public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
		for (EnumFacing facing : EnumFacing.Plane.HORIZONTAL) {
			if (this.canPlaceStructure(worldIn, pos, facing)) {
				return true;
			}
		}

		return false;
	}

	@Override
	public boolean canPlaceBlockOnSide(World worldIn, BlockPos pos, EnumFacing side) {
		return side == EnumFacing.UP && this.canPlaceBlockAt(worldIn, pos);
	}

	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY,
			float hitZ, int meta, EntityLivingBase placer, ItemStack stack) {
		EnumFacing foudreFacing = placer == null ? EnumFacing.SOUTH : placer.getHorizontalFacing();

		if (!this.canPlaceStructure(world, pos, foudreFacing)) {
			for (EnumFacing facingOption : EnumFacing.Plane.HORIZONTAL) {
				if (this.canPlaceStructure(world, pos, facingOption)) {
					foudreFacing = facingOption;
					break;
				}
			}
		}

		return this.getDefaultState().withProperty(FACING, foudreFacing).withProperty(PART, FoudrePart.FRONT_LEFT);
	}

	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer,
			ItemStack stack) {
		if (worldIn.isRemote || this.upperLayer) {
			return;
		}

		EnumFacing facing = state.getValue(FACING);

		for (FoudrePart part : PARTS) {
			for (int layer = 0; layer <= 1; layer++) {
				if (part == FoudrePart.FRONT_LEFT && layer == 0) {
					continue;
				}

				Foudre block = layer == 0 ? this.getLowerBlock() : this.getUpperBlock();
				BlockPos partPos = this.getPartPos(pos, facing, part, layer == 1);
				worldIn.setBlockState(partPos, block.getDefaultState().withProperty(FACING, facing)
					.withProperty(PART, part), 3);
			}
		}

		TileEntityBarrel barrel = super.getBarrelEntity(worldIn, pos);

		if (barrel != null) {
			barrel.readFromItemStack(stack);
		}
	}

	@Override
	protected TileEntityBarrel getBarrelEntity(IBlockAccess worldIn, BlockPos pos) {
		return super.getBarrelEntity(worldIn, this.getBasePos(worldIn, pos));
	}

	@Override
	protected BlockPos getBarrelGuiPos(IBlockAccess worldIn, BlockPos pos, IBlockState state) {
		return this.getBasePos(pos, state);
	}

	@Override
	public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state,
			TileEntity te, ItemStack stack) {
		super.harvestBlock(worldIn, player, pos, state, te, stack);

		if (!worldIn.isRemote) {
			this.removeStructure(worldIn, pos, state);
		}
	}

	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
		if (!worldIn.isRemote && !REMOVING_PARTS.contains(pos)) {
			this.removeStructure(worldIn, pos, state);
		}

		super.breakBlock(worldIn, pos, state);
	}

	@Override
	public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
		List<ItemStack> drops = new ArrayList<ItemStack>();
		Item item = Item.getItemFromBlock(this.getLowerBlock());

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
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return Item.getItemFromBlock(this.getLowerBlock());
	}

	@Override
	public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state) {
		return new ItemStack(Item.getItemFromBlock(this.getLowerBlock()));
	}

	@Override
	public boolean hasTileEntity(IBlockState state) {
		return !this.upperLayer && state.getValue(PART) == FoudrePart.FRONT_LEFT;
	}

	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		return this.hasTileEntity(state) ? new TileEntityFoudre() : null;
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return FULL_BLOCK_AABB;
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, World worldIn, BlockPos pos) {
		return FULL_BLOCK_AABB;
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return this.getDefaultState()
			.withProperty(FACING, EnumFacing.getHorizontal(meta & 3))
			.withProperty(PART, FoudrePart.byMetadata(meta >> 2));
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		int meta = ((EnumFacing)state.getValue(FACING)).getHorizontalIndex();
		meta += ((FoudrePart)state.getValue(PART)).ordinal() * 4;
		return meta;
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] { FACING, PART });
	}

	@Override
	public IBlockState withRotation(IBlockState state, Rotation rot) {
		return state.withProperty(FACING, rot.rotate((EnumFacing)state.getValue(FACING)));
	}

	@Override
	public IBlockState withMirror(IBlockState state, Mirror mirrorIn) {
		return state.withRotation(mirrorIn.toRotation((EnumFacing)state.getValue(FACING)));
	}

	private boolean canPlaceStructure(World worldIn, BlockPos basePos, EnumFacing facing) {
		for (FoudrePart part : PARTS) {
			BlockPos lowerPos = this.getPartPos(basePos, facing, part, false);
			BlockPos upperPos = this.getPartPos(basePos, facing, part, true);

			if (!this.isReplaceable(worldIn, lowerPos) || !this.isReplaceable(worldIn, upperPos)) {
				return false;
			}

			if (!this.hasSupport(worldIn, lowerPos)) {
				return false;
			}
		}

		return true;
	}

	private boolean isReplaceable(World worldIn, BlockPos pos) {
		IBlockState state = worldIn.getBlockState(pos);
		Block block = state.getBlock();
		return block.isReplaceable(worldIn, pos);
	}

	private boolean hasSupport(World worldIn, BlockPos pos) {
		BlockPos supportPos = pos.down();
		return worldIn.getBlockState(supportPos).isSideSolid(worldIn, supportPos, EnumFacing.UP);
	}

	private BlockPos getBasePos(IBlockAccess worldIn, BlockPos pos) {
		IBlockState state = worldIn.getBlockState(pos);

		if (!(state.getBlock() instanceof Foudre)) {
			return pos;
		}

		return ((Foudre)state.getBlock()).getBasePos(pos, state);
	}

	private BlockPos getBasePos(BlockPos pos, IBlockState state) {
		EnumFacing facing = state.getValue(FACING);
		FoudrePart part = state.getValue(PART);
		BlockPos basePos = this.upperLayer ? pos.down() : pos;

		if (part.getDepthOffset() > 0) {
			basePos = basePos.offset(facing.getOpposite(), part.getDepthOffset());
		}

		if (part.getLateralOffset() > 0) {
			basePos = basePos.offset(facing.rotateY(), part.getLateralOffset());
		}

		return basePos;
	}

	private BlockPos getPartPos(BlockPos basePos, EnumFacing facing, FoudrePart part, boolean upper) {
		BlockPos partPos = basePos;

		if (part.getDepthOffset() > 0) {
			partPos = partPos.offset(facing, part.getDepthOffset());
		}

		if (part.getLateralOffset() > 0) {
			partPos = partPos.offset(facing.rotateYCCW(), part.getLateralOffset());
		}

		return upper ? partPos.up() : partPos;
	}

	private void removeStructure(World worldIn, BlockPos pos, IBlockState state) {
		BlockPos basePos = this.getBasePos(pos, state);
		EnumFacing facing = state.getValue(FACING);
		Set<BlockPos> structurePositions = this.getStructurePositions(basePos, facing);

		REMOVING_PARTS.addAll(structurePositions);

		try {
			for (FoudrePart part : PARTS) {
				this.removePart(worldIn, this.getPartPos(basePos, facing, part, false), this.getLowerBlock(), part);
				this.removePart(worldIn, this.getPartPos(basePos, facing, part, true), this.getUpperBlock(), part);
			}
		} finally {
			REMOVING_PARTS.removeAll(structurePositions);
		}
	}

	private void removePart(World worldIn, BlockPos pos, Foudre block, FoudrePart part) {
		IBlockState state = worldIn.getBlockState(pos);

		if (state.getBlock() == block && state.getValue(PART) == part) {
			worldIn.setBlockToAir(pos);
		}
	}

	private Set<BlockPos> getStructurePositions(BlockPos basePos, EnumFacing facing) {
		Set<BlockPos> positions = new HashSet<BlockPos>();

		for (FoudrePart part : PARTS) {
			positions.add(this.getPartPos(basePos, facing, part, false));
			positions.add(this.getPartPos(basePos, facing, part, true));
		}

		return positions;
	}

	private Foudre getLowerBlock() {
		return this.lowerBlock == null ? this : this.lowerBlock;
	}

	private Foudre getUpperBlock() {
		return this.upperBlock == null ? this : this.upperBlock;
	}
}
