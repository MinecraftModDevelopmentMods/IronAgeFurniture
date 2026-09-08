package zone.moddev.mc.ironagefurniture.api.Blocks;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import com.google.common.collect.Lists;
import zone.moddev.mc.ironagefurniture.Ironagefurniture;
import zone.moddev.mc.ironagefurniture.api.Enumerations.CanopyBedPart;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Biomes;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class MultiBlockBed extends BlockHBase {
	public static final PropertyEnum<CanopyBedPart> PART = PropertyEnum.<CanopyBedPart>create("part",
		CanopyBedPart.class);

	public static final int LEFT_SIDE = -1;
	public static final int SINGLE_SIDE = 0;
	public static final int RIGHT_SIDE = 1;

	private static final AxisAlignedBB FOOT_LOWER_BB = new AxisAlignedBB(0.0D, 0.3125D, 0.0D, 1.0D, 0.625D, 0.9375D);
	private static final AxisAlignedBB HEAD_LOWER_BB = new AxisAlignedBB(0.0D, 0.3125D, 0.0625D, 1.0D, 0.625D, 1.0D);
	private static final AxisAlignedBB CANOPY_BB = new AxisAlignedBB(0.0D, 0.8125D, 0.0D, 1.0D, 1.0D, 1.0D);
	private static final Set<BlockPos> REMOVING_PARTS = new HashSet<BlockPos>();
	private static final CanopyBedPart[] PARTS = new CanopyBedPart[] {
		CanopyBedPart.FOOT_LOWER,
		CanopyBedPart.FOOT_UPPER,
		CanopyBedPart.HEAD_LOWER,
		CanopyBedPart.HEAD_UPPER
	};

	private final int bedSide;
	private Block singleBlock;
	private Block doubleLeftBlock;
	private Block doubleRightBlock;

	public MultiBlockBed(Material materialIn, String name, float resistance, float hardness, int bedSide) {
		super(materialIn);
		this.bedSide = bedSide;
		this.blockResistance = resistance;
		this.blockHardness = hardness;
		this.setSoundType(SoundType.WOOD);
		this.setHarvestLevel("axe", 0);
		this.setCreativeTab(Ironagefurniture.ironagefurnitureTab);
		this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.SOUTH)
			.withProperty(PART, CanopyBedPart.FOOT_LOWER));
	}

	public MultiBlockBed setSingleBlock(Block singleBlock) {
		this.singleBlock = singleBlock;
		return this;
	}

	public MultiBlockBed setDoubleBlocks(Block doubleLeftBlock, Block doubleRightBlock) {
		this.doubleLeftBlock = doubleLeftBlock;
		this.doubleRightBlock = doubleRightBlock;
		return this;
	}

	@Override
	public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
		if (!this.isPlaceableBaseBlock()) {
			return false;
		}

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
		EnumFacing bedFacing = placer.getHorizontalFacing().getOpposite();

		if (!this.canPlaceStructure(world, pos, bedFacing)) {
			for (EnumFacing facingOption : EnumFacing.Plane.HORIZONTAL) {
				if (this.canPlaceStructure(world, pos, facingOption)) {
					bedFacing = facingOption;
					break;
				}
			}
		}

		return this.getDefaultState().withProperty(FACING, bedFacing).withProperty(PART, CanopyBedPart.FOOT_LOWER);
	}

	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer,
			ItemStack stack) {
		if (worldIn.isRemote || !this.isPlaceableBaseBlock()) {
			return;
		}

		BlockPos basePos = this.getBasePos(pos, state);
		EnumFacing facing = state.getValue(FACING);

		for (int side : this.getStructureSides()) {
			for (CanopyBedPart part : PARTS) {
				if (side == this.bedSide && part == CanopyBedPart.FOOT_LOWER) {
					continue;
				}

				Block block = this.getStructureBlock(side);

				if (block != null) {
					worldIn.setBlockState(this.getPartPos(basePos, facing, side, part),
						block.getDefaultState().withProperty(FACING, facing).withProperty(PART, part), 3);
				}
			}
		}
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
		if (worldIn.isRemote) {
			return true;
		}

		BlockPos sleepPos = this.getSleepPos(pos, state);
		IBlockState sleepState = worldIn.getBlockState(sleepPos);

		if (!(sleepState.getBlock() instanceof MultiBlockBed)) {
			return true;
		}

		MultiBlockBed sleepBlock = (MultiBlockBed)sleepState.getBlock();

		if (worldIn.provider.canRespawnHere() && worldIn.getBiome(sleepPos) != Biomes.HELL) {
			if (sleepBlock.getPlayerInBed(worldIn, sleepPos) != null) {
				playerIn.sendStatusMessage(new TextComponentTranslation("tile.bed.occupied", new Object[0]));
				return true;
			}

			EntityPlayer.SleepResult sleepResult = playerIn.trySleep(sleepPos);

			if (sleepResult == EntityPlayer.SleepResult.NOT_POSSIBLE_NOW) {
				playerIn.sendStatusMessage(new TextComponentTranslation("tile.bed.noSleep", new Object[0]));
			} else if (sleepResult == EntityPlayer.SleepResult.NOT_SAFE) {
				playerIn.sendStatusMessage(new TextComponentTranslation("tile.bed.notSafe", new Object[0]));
			}

			return true;
		}

		sleepBlock.explodeBed(worldIn, sleepPos, sleepState);
		return true;
	}

	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
		if (!worldIn.isRemote && !REMOVING_PARTS.contains(pos)) {
			this.removeOtherParts(worldIn, pos, state);
		}

		super.breakBlock(worldIn, pos, state);
	}

	@Override
	public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
		return Lists.newArrayList(new ItemStack(Item.getItemFromBlock(this.getBaseBlock())));
	}

	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return Item.getItemFromBlock(this.getBaseBlock());
	}

	@Override
	public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state) {
		return new ItemStack(Item.getItemFromBlock(this.getBaseBlock()));
	}

	@Override
	public boolean isBed(IBlockState state, IBlockAccess world, BlockPos pos, Entity player) {
		return true;
	}

	@Override
	public BlockPos getBedSpawnPosition(IBlockState state, IBlockAccess world, BlockPos pos, EntityPlayer player) {
		if (world instanceof World) {
			BlockPos sleepPos = this.getSleepPos(pos, state);
			IBlockState sleepState = world.getBlockState(sleepPos);

			if (sleepState.getBlock() instanceof MultiBlockBed) {
				return this.getSafeExitLocation((World)world, sleepPos, sleepState, 0);
			}
		}

		return null;
	}

	@Override
	public void setBedOccupied(IBlockAccess world, BlockPos pos, EntityPlayer player, boolean occupied) {
	}

	@Override
	public EnumFacing getBedDirection(IBlockState state, IBlockAccess world, BlockPos pos) {
		return ((EnumFacing)state.getValue(FACING)).getOpposite();
	}

	@Override
	public boolean isBedFoot(IBlockAccess world, BlockPos pos) {
		IBlockState state = world.getBlockState(pos);

		if (!(state.getBlock() instanceof MultiBlockBed)) {
			return false;
		}

		return state.getValue(PART) == CanopyBedPart.FOOT_LOWER;
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
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return this.rotateBoundingBox(this.getPartBoundingBox(state), state.getValue(FACING));
	}

	@Override
	public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox,
			List<AxisAlignedBB> collidingBoxes, Entity entityIn) {
		AxisAlignedBB bb = this.rotateBoundingBox(this.getPartBoundingBox(state), state.getValue(FACING));
		super.addCollisionBoxToList(pos, entityBox, collidingBoxes, bb);
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return this.getDefaultState()
			.withProperty(FACING, EnumFacing.getHorizontal(meta & 3))
			.withProperty(PART, CanopyBedPart.byMetadata(meta >> 2));
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		int meta = ((EnumFacing)state.getValue(FACING)).getHorizontalIndex();
		meta += ((CanopyBedPart)state.getValue(PART)).ordinal() * 4;
		return meta;
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] { FACING, PART });
	}

	private boolean canPlaceStructure(World worldIn, BlockPos basePos, EnumFacing facing) {
		for (int side : this.getStructureSides()) {
			for (CanopyBedPart part : PARTS) {
				BlockPos partPos = this.getPartPos(basePos, facing, side, part);

				if (!worldIn.isAirBlock(partPos)) {
					return false;
				}

				if (!part.isUpper() && !this.hasSupport(worldIn, partPos)) {
					return false;
				}
			}
		}

		return true;
	}

	private boolean hasSupport(World worldIn, BlockPos partPos) {
		BlockPos supportPos = partPos.down();
		return worldIn.getBlockState(supportPos).isSideSolid(worldIn, supportPos, EnumFacing.UP);
	}

	private BlockPos getPartPos(BlockPos basePos, EnumFacing facing, int side, CanopyBedPart part) {
		BlockPos partPos = basePos;

		if (part.isUpper()) {
			partPos = partPos.up();
		}

		if (part.isHead()) {
			partPos = partPos.offset(facing.getOpposite());
		}

		if (side == RIGHT_SIDE) {
			partPos = partPos.offset(facing.rotateYCCW());
		}

		return partPos;
	}

	private BlockPos getBasePos(BlockPos pos, IBlockState state) {
		EnumFacing facing = state.getValue(FACING);
		CanopyBedPart part = state.getValue(PART);
		BlockPos basePos = pos;

		if (part.isUpper()) {
			basePos = basePos.down();
		}

		if (part.isHead()) {
			basePos = basePos.offset(facing);
		}

		if (this.bedSide == RIGHT_SIDE) {
			basePos = basePos.offset(facing.rotateY());
		}

		return basePos;
	}

	private BlockPos getSleepPos(BlockPos pos, IBlockState state) {
		BlockPos basePos = this.getBasePos(pos, state);
		return this.getPartPos(basePos, state.getValue(FACING), this.getPartSide(), CanopyBedPart.HEAD_LOWER);
	}

	private EntityPlayer getPlayerInBed(World worldIn, BlockPos pos) {
		for (EntityPlayer entityplayer : worldIn.playerEntities) {
			if (entityplayer.isPlayerSleeping() && pos.equals(entityplayer.bedLocation)) {
				return entityplayer;
			}
		}

		return null;
	}

	private int getPartSide() {
		return this.isDoubleBed() ? this.bedSide : SINGLE_SIDE;
	}

	private boolean isPlaceableBaseBlock() {
		return !this.isDoubleBed() || this.bedSide == LEFT_SIDE;
	}

	private boolean isDoubleBed() {
		return this.bedSide == LEFT_SIDE || this.bedSide == RIGHT_SIDE;
	}

	private int[] getStructureSides() {
		if (this.isDoubleBed()) {
			return new int[] { LEFT_SIDE, RIGHT_SIDE };
		}

		return new int[] { SINGLE_SIDE };
	}

	private Block getStructureBlock(int side) {
		if (!this.isDoubleBed()) {
			return this.singleBlock;
		}

		return side == RIGHT_SIDE ? this.doubleRightBlock : this.doubleLeftBlock;
	}

	private Block getBaseBlock() {
		if (this.isDoubleBed() && this.doubleLeftBlock != null) {
			return this.doubleLeftBlock;
		}

		if (this.singleBlock != null) {
			return this.singleBlock;
		}

		return this;
	}

	private void explodeBed(World worldIn, BlockPos sleepPos, IBlockState sleepState) {
		this.removeStructure(worldIn, sleepPos, sleepState);
		worldIn.newExplosion((Entity)null, (double)sleepPos.getX() + 0.5D, (double)sleepPos.getY() + 0.5D,
			(double)sleepPos.getZ() + 0.5D, 5.0F, true, true);
	}

	private BlockPos getSafeExitLocation(World worldIn, BlockPos sleepPos, IBlockState sleepState, int tries) {
		EnumFacing bedDirection = this.getBedDirection(sleepState, worldIn, sleepPos);
		int x = sleepPos.getX();
		int y = sleepPos.getY();
		int z = sleepPos.getZ();

		for (int row = 0; row <= 1; row++) {
			int minX = x - bedDirection.getFrontOffsetX() * row - 1;
			int minZ = z - bedDirection.getFrontOffsetZ() * row - 1;
			int maxX = minX + 2;
			int maxZ = minZ + 2;

			for (int checkX = minX; checkX <= maxX; checkX++) {
				for (int checkZ = minZ; checkZ <= maxZ; checkZ++) {
					BlockPos exitPos = new BlockPos(checkX, y, checkZ);

					if (this.hasRoomForPlayer(worldIn, exitPos)) {
						if (tries <= 0) {
							return exitPos;
						}

						tries--;
					}
				}
			}
		}

		return null;
	}

	private boolean hasRoomForPlayer(World worldIn, BlockPos pos) {
		return worldIn.getBlockState(pos.down()).isFullyOpaque()
			&& !worldIn.getBlockState(pos).getMaterial().isSolid()
			&& !worldIn.getBlockState(pos.up()).getMaterial().isSolid();
	}

	private void removeOtherParts(World worldIn, BlockPos pos, IBlockState state) {
		BlockPos basePos = this.getBasePos(pos, state);
		EnumFacing facing = state.getValue(FACING);
		Set<BlockPos> structurePositions = this.getStructurePositions(basePos, facing);

		REMOVING_PARTS.addAll(structurePositions);

		try {
			for (int side : this.getStructureSides()) {
				for (CanopyBedPart part : PARTS) {
					BlockPos partPos = this.getPartPos(basePos, facing, side, part);

					if (!partPos.equals(pos) && this.isExpectedPart(worldIn, partPos, side, part)) {
						worldIn.setBlockToAir(partPos);
					}
				}
			}
		} finally {
			REMOVING_PARTS.removeAll(structurePositions);
		}
	}

	private void removeStructure(World worldIn, BlockPos pos, IBlockState state) {
		BlockPos basePos = this.getBasePos(pos, state);
		EnumFacing facing = state.getValue(FACING);
		Set<BlockPos> structurePositions = this.getStructurePositions(basePos, facing);

		REMOVING_PARTS.addAll(structurePositions);

		try {
			for (int side : this.getStructureSides()) {
				for (CanopyBedPart part : PARTS) {
					BlockPos partPos = this.getPartPos(basePos, facing, side, part);

					if (this.isExpectedPart(worldIn, partPos, side, part)) {
						worldIn.setBlockToAir(partPos);
					}
				}
			}
		} finally {
			REMOVING_PARTS.removeAll(structurePositions);
		}
	}

	private Set<BlockPos> getStructurePositions(BlockPos basePos, EnumFacing facing) {
		Set<BlockPos> positions = new HashSet<BlockPos>();

		for (int side : this.getStructureSides()) {
			for (CanopyBedPart part : PARTS) {
				positions.add(this.getPartPos(basePos, facing, side, part));
			}
		}

		return positions;
	}

	private boolean isExpectedPart(World worldIn, BlockPos pos, int side, CanopyBedPart part) {
		IBlockState state = worldIn.getBlockState(pos);

		return state.getBlock() == this.getStructureBlock(side)
			&& state.getValue(PART) == part;
	}

	private AxisAlignedBB getPartBoundingBox(IBlockState state) {
		CanopyBedPart part = state.getValue(PART);

		if (part.isUpper()) {
			return CANOPY_BB;
		}

		return part.isHead() ? HEAD_LOWER_BB : FOOT_LOWER_BB;
	}

	private AxisAlignedBB rotateBoundingBox(AxisAlignedBB bb, EnumFacing facing) {
		switch (facing) {
		case NORTH:
			return new AxisAlignedBB(1.0D - bb.maxX, bb.minY, 1.0D - bb.maxZ, 1.0D - bb.minX, bb.maxY, 1.0D - bb.minZ);
		case EAST:
			return new AxisAlignedBB(bb.minZ, bb.minY, 1.0D - bb.maxX, bb.maxZ, bb.maxY, 1.0D - bb.minX);
		case WEST:
			return new AxisAlignedBB(1.0D - bb.maxZ, bb.minY, bb.minX, 1.0D - bb.minZ, bb.maxY, bb.maxX);
		default:
			return bb;
		}
	}
}
