package zone.moddev.mc.ironagefurniture.api.Blocks;

import java.util.List;
import java.util.Locale;

import zone.moddev.mc.ironagefurniture.IronAgeFurnitureConfiguration;
import zone.moddev.mc.ironagefurniture.Ironagefurniture;
import zone.moddev.mc.ironagefurniture.ItemObjectHolder;
import zone.moddev.mc.ironagefurniture.api.tile.TileEntityBottleRack;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BottleRack extends BlockHBase {
	public static final PropertyInteger CONNECTIONS = PropertyInteger.create("connections", 0, 15);
	public static final PropertyBool DATA = PropertyBool.create("data");
	public static final PropertyBool STANDING = PropertyBool.create("standing");

	private static final int UP = 1;
	private static final int RIGHT = 2;
	private static final int DOWN = 4;
	private static final int LEFT = 8;

	private static final AxisAlignedBB RACK_BOX_NORTH = new AxisAlignedBB(0.0D, 0.0D, 0.625D,
		1.0D, 1.0D, 1.0D);
	private static final AxisAlignedBB STANDING_RACK_BOX_NORTH = new AxisAlignedBB(0.0D, 0.0D, 0.3125D,
		1.0D, 1.0D, 0.6875D);

	public BottleRack(Material materialIn, String name, float resistance, float hardness) {
		super(materialIn);
		this.blockResistance = resistance;
		this.blockHardness = hardness;
		this.setSoundType(SoundType.WOOD);
		this.setHarvestLevel("axe", 0);
		this.setCreativeTab(Ironagefurniture.ironagefurnitureTab);
		this.setLightOpacity(0);
		this.setDefaultState(this.blockState.getBaseState()
			.withProperty(FACING, EnumFacing.NORTH)
			.withProperty(CONNECTIONS, Integer.valueOf(0))
			.withProperty(DATA, Boolean.valueOf(false))
			.withProperty(STANDING, Boolean.valueOf(false)));
	}

	@Override
	public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
		if (this.canStandingRackStay(worldIn, pos, EnumFacing.NORTH)) {
			return super.canPlaceBlockAt(worldIn, pos);
		}

		for (EnumFacing facing : FACING.getAllowedValues()) {
			if (this.canWallRackStay(worldIn, pos, facing)) {
				return super.canPlaceBlockAt(worldIn, pos);
			}
		}

		return false;
	}

	@Override
	public boolean canPlaceBlockOnSide(World worldIn, BlockPos pos, EnumFacing side) {
		if (side == EnumFacing.UP) {
			return this.canStandingRackStay(worldIn, pos, EnumFacing.NORTH)
				&& super.canPlaceBlockOnSide(worldIn, pos, side);
		}

		return side.getAxis().isHorizontal() && this.canWallRackStay(worldIn, pos, side)
			&& super.canPlaceBlockOnSide(worldIn, pos, side);
	}

	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing side, float hitX, float hitY,
			float hitZ, int meta, EntityLivingBase placer, ItemStack stack) {
		if (side == EnumFacing.UP && this.canStandingRackStay(world, pos, EnumFacing.NORTH)) {
			EnumFacing facing = placer != null ? placer.getHorizontalFacing().getOpposite() : EnumFacing.NORTH;

			return this.getDefaultState()
				.withProperty(FACING, facing)
				.withProperty(CONNECTIONS, Integer.valueOf(0))
				.withProperty(DATA, Boolean.valueOf(false))
				.withProperty(STANDING, Boolean.valueOf(true));
		}

		return this.getDefaultState()
			.withProperty(FACING, side.getAxis().isHorizontal() ? side : EnumFacing.NORTH)
			.withProperty(CONNECTIONS, Integer.valueOf(0))
			.withProperty(DATA, Boolean.valueOf(false))
			.withProperty(STANDING, Boolean.valueOf(false));
	}

	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
		return state.withProperty(CONNECTIONS,
			Integer.valueOf(this.getConnectionMask(worldIn, pos, state.getValue(FACING),
				state.getValue(STANDING).booleanValue())));
	}

	@Override
	public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
		super.onBlockAdded(worldIn, pos, state);
		this.notifyRackAndNeighbors(worldIn, pos);
	}

	@Override
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn) {
		if (!this.canRackStay(worldIn, pos, state)) {
			if (!worldIn.isRemote) {
				this.dropBlockAsItem(worldIn, pos, state, 0);
			}

			worldIn.setBlockState(pos, Blocks.AIR.getDefaultState(), 3);
			return;
		}

		this.removeRackEntityIfEmpty(worldIn, pos);
		this.notifyRackAndNeighbors(worldIn, pos);
	}

	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
		if (!worldIn.isRemote) {
			TileEntity tileEntity = worldIn.getTileEntity(pos);

			if (tileEntity instanceof TileEntityBottleRack) {
				((TileEntityBottleRack)tileEntity).dropBottles(worldIn, pos);
			}
		}

		super.breakBlock(worldIn, pos, state);
		this.notifyRackNeighbors(worldIn, pos);
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
		if (this.isBottleRackItem(heldItem)) {
			if (this.tryPlaceRackFromRackClick(worldIn, pos, state, playerIn, hand, heldItem, side,
					hitX, hitY, hitZ)) {
				return true;
			}

			return false;
		}

		if (playerIn.isSneaking()) {
			return false;
		}

		int slot = this.getSlotForHit(state.getValue(FACING), hitX, hitY, hitZ);
		TileEntityBottleRack rack = this.getRackEntity(worldIn, pos, false);
		boolean hasHeldItem = heldItem != null && heldItem.stackSize > 0;

		if (hasHeldItem && !isValidBottleItem(heldItem)) {
			return false;
		}

		if (worldIn.isRemote) {
			return true;
		}

		if (hasHeldItem) {
			if (rack != null && rack.hasBottle(slot)) {
				ItemStack stored = rack.getBottle(slot);

				if (this.isSameBottleStack(stored, heldItem)) {
					this.returnRackItem(worldIn, pos, playerIn, rack.removeBottle(slot));
					this.removeRackEntityIfEmpty(worldIn, pos);
				}

				return true;
			}

			rack = this.getRackEntity(worldIn, pos, true);

			if (rack != null && rack.insertBottle(slot, heldItem,
					this.getBottleFacingForInsert(pos, state, playerIn))) {
				if (!playerIn.capabilities.isCreativeMode) {
					heldItem.stackSize--;

					if (heldItem.stackSize <= 0) {
						playerIn.setHeldItem(hand, null);
					}
				}
			}

			return true;
		}

		if (rack != null && rack.hasBottle(slot)) {
			this.returnRackItem(worldIn, pos, playerIn, rack.removeBottle(slot));
			this.removeRackEntityIfEmpty(worldIn, pos);
		}

		return true;
	}

	private EnumFacing getBottleFacingForInsert(BlockPos pos, IBlockState state, EntityPlayer playerIn) {
		EnumFacing rackFacing = state.getValue(FACING);

		if (!state.getValue(STANDING).booleanValue() || playerIn == null) {
			return rackFacing;
		}

		EnumFacing playerSide = playerIn.getHorizontalFacing().getOpposite();

		if (playerSide == rackFacing || playerSide == rackFacing.getOpposite()) {
			return playerSide;
		}

		double playerOffsetX = playerIn.posX - (pos.getX() + 0.5D);
		double playerOffsetZ = playerIn.posZ - (pos.getZ() + 0.5D);
		double frontDistance = playerOffsetX * rackFacing.getFrontOffsetX()
			+ playerOffsetZ * rackFacing.getFrontOffsetZ();

		return frontDistance >= 0.0D ? rackFacing : rackFacing.getOpposite();
	}

	public boolean tryPlaceRackFromRackClick(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
		EnumFacing currentFacing = state.getValue(FACING);
		EnumFacing placementSide = this.getRackPlacementSide(currentFacing, side, hitX, hitY, hitZ);

		if (placementSide == null) {
			return false;
		}

		Block rackBlock = ((ItemBlock)heldItem.getItem()).getBlock();
		return this.tryPlaceRack(worldIn, pos.offset(placementSide), currentFacing, rackBlock,
			state.getValue(STANDING).booleanValue(), playerIn, hand, heldItem, hitX, hitY, hitZ);
	}

	@Override
	public boolean hasTileEntity(IBlockState state) {
		return state.getValue(DATA).booleanValue();
	}

	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		return state.getValue(DATA).booleanValue() ? new TileEntityBottleRack() : null;
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
	@SideOnly(Side.CLIENT)
	public net.minecraft.util.BlockRenderLayer getBlockLayer() {
		return net.minecraft.util.BlockRenderLayer.CUTOUT;
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		AxisAlignedBB box = state.getValue(STANDING).booleanValue() ? STANDING_RACK_BOX_NORTH : RACK_BOX_NORTH;
		return this.rotateToFacing(box, state.getValue(FACING));
	}

	@Override
	public AxisAlignedBB getSelectedBoundingBox(IBlockState state, World worldIn, BlockPos pos) {
		return this.getBoundingBox(state, worldIn, pos).offset(pos);
	}

	@Override
	public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox,
			List<AxisAlignedBB> collidingBoxes, Entity entityIn) {
		super.addCollisionBoxToList(pos, entityBox, collidingBoxes, this.getBoundingBox(state, worldIn, pos));
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return this.getDefaultState()
			.withProperty(FACING, EnumFacing.getHorizontal(meta & 3))
			.withProperty(CONNECTIONS, Integer.valueOf(0))
			.withProperty(DATA, Boolean.valueOf((meta & 4) != 0))
			.withProperty(STANDING, Boolean.valueOf((meta & 8) != 0));
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		int meta = state.getValue(FACING).getHorizontalIndex();
		if (state.getValue(DATA).booleanValue()) {
			meta |= 4;
		}
		if (state.getValue(STANDING).booleanValue()) {
			meta |= 8;
		}

		return meta;
	}

	@Override
	public int damageDropped(IBlockState state) {
		return 0;
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] { CONNECTIONS, DATA, FACING, STANDING });
	}

	private EnumFacing getRackPlacementSide(EnumFacing rackFacing, EnumFacing clickedSide, float hitX, float hitY,
			float hitZ) {
		if (clickedSide == EnumFacing.UP || clickedSide == EnumFacing.DOWN) {
			return clickedSide;
		}

		if (this.isRackLateralSide(rackFacing, clickedSide)) {
			return clickedSide;
		}

		if (clickedSide != rackFacing) {
			return null;
		}

		if (hitY >= 0.70F) {
			return EnumFacing.UP;
		}

		if (hitY <= 0.30F) {
			return EnumFacing.DOWN;
		}

		double localX = this.getLocalX(rackFacing, hitX, hitZ);
		return localX >= 0.5D ? this.rotateClockwise(rackFacing) : this.rotateCounterClockwise(rackFacing);
	}

	private boolean tryPlaceRack(World worldIn, BlockPos placePos, EnumFacing rackFacing, Block rackBlock,
			boolean standing, EntityPlayer playerIn, EnumHand hand, ItemStack heldItem, float hitX, float hitY,
			float hitZ) {
		if (!(rackBlock instanceof BottleRack)
				|| !worldIn.getBlockState(placePos).getBlock().isReplaceable(worldIn, placePos)
				|| !((BottleRack)rackBlock).canRackStay(worldIn, placePos, rackFacing, standing)
				|| !playerIn.canPlayerEdit(placePos, rackFacing, heldItem)) {
			return false;
		}

		if (worldIn.isRemote) {
			return true;
		}

		IBlockState placedState = ((BottleRack)rackBlock).getDefaultState()
			.withProperty(FACING, rackFacing)
			.withProperty(CONNECTIONS, Integer.valueOf(0))
			.withProperty(DATA, Boolean.valueOf(false))
			.withProperty(STANDING, Boolean.valueOf(standing));

		if (!worldIn.setBlockState(placePos, placedState, 3)) {
			return false;
		}

		IBlockState actualState = worldIn.getBlockState(placePos);

		if (actualState.getBlock() == rackBlock) {
			ItemBlock.setTileEntityNBT(worldIn, playerIn, placePos, heldItem);
			rackBlock.onBlockPlacedBy(worldIn, placePos, actualState, playerIn, heldItem);
		}

		SoundType soundType = rackBlock.getSoundType(actualState, worldIn, placePos, playerIn);
		worldIn.playSound(playerIn, placePos, soundType.getPlaceSound(), SoundCategory.BLOCKS,
			(soundType.getVolume() + 1.0F) / 2.0F, soundType.getPitch() * 0.8F);

		if (!playerIn.capabilities.isCreativeMode) {
			heldItem.stackSize--;

			if (heldItem.stackSize <= 0) {
				playerIn.setHeldItem(hand, null);
			}
		}

		return true;
	}

	private int getSlotForHit(EnumFacing facing, float hitX, float hitY, float hitZ) {
		double localX = this.clamp(this.getLocalX(facing, hitX, hitZ), 0.0D, 0.999D);
		double localY = this.clamp(hitY, 0.0D, 0.999D);
		int column = (int)Math.floor(localX * 3.0D);
		int row = 2 - (int)Math.floor(localY * 3.0D);
		return row * 3 + column;
	}

	private double getLocalX(EnumFacing facing, float hitX, float hitZ) {
		EnumFacing right = this.rotateClockwise(facing);
		return 0.5D + (hitX - 0.5D) * right.getFrontOffsetX()
			+ (hitZ - 0.5D) * right.getFrontOffsetZ();
	}

	private double clamp(double value, double min, double max) {
		return Math.max(min, Math.min(max, value));
	}

	private boolean canRackStay(IBlockAccess worldIn, BlockPos pos, IBlockState state) {
		return this.canRackStay(worldIn, pos, state.getValue(FACING),
			state.getValue(STANDING).booleanValue());
	}

	private boolean canRackStay(IBlockAccess worldIn, BlockPos pos, EnumFacing facing, boolean standing) {
		return standing ? this.canStandingRackStay(worldIn, pos, facing)
			: this.canWallRackStay(worldIn, pos, facing);
	}

	private boolean canWallRackStay(IBlockAccess worldIn, BlockPos pos, EnumFacing facing) {
		BlockPos supportPos = pos.offset(facing.getOpposite());
		return worldIn.getBlockState(supportPos).isSideSolid(worldIn, supportPos, facing);
	}

	private boolean canStandingRackStay(IBlockAccess worldIn, BlockPos pos, EnumFacing facing) {
		BlockPos supportPos = pos.down();
		IBlockState supportState = worldIn.getBlockState(supportPos);

		if (supportState.isSideSolid(worldIn, supportPos, EnumFacing.UP)) {
			return true;
		}

		return supportState.getBlock() == this
			&& supportState.getValue(FACING) == facing
			&& supportState.getValue(STANDING).booleanValue();
	}

	private int getConnectionMask(IBlockAccess worldIn, BlockPos pos, EnumFacing facing, boolean standing) {
		int connections = 0;

		if (this.connectsTo(worldIn, pos.up(), facing, standing)) {
			connections |= UP;
		}
		if (this.connectsTo(worldIn, pos.offset(this.rotateClockwise(facing)), facing, standing)) {
			connections |= RIGHT;
		}
		if (this.connectsTo(worldIn, pos.down(), facing, standing)) {
			connections |= DOWN;
		}
		if (this.connectsTo(worldIn, pos.offset(this.rotateCounterClockwise(facing)), facing, standing)) {
			connections |= LEFT;
		}

		return connections;
	}

	private boolean connectsTo(IBlockAccess worldIn, BlockPos pos, EnumFacing facing, boolean standing) {
		IBlockState state = worldIn.getBlockState(pos);
		return state.getBlock() == this
			&& state.getValue(FACING) == facing
			&& state.getValue(STANDING).booleanValue() == standing
			&& this.canRackStay(worldIn, pos, facing, standing);
	}

	private void notifyRackAndNeighbors(World worldIn, BlockPos pos) {
		this.notifyRack(worldIn, pos);
		this.notifyRackNeighbors(worldIn, pos);
	}

	private void notifyRackNeighbors(World worldIn, BlockPos pos) {
		this.notifyRack(worldIn, pos.up());
		this.notifyRack(worldIn, pos.down());

		for (EnumFacing direction : EnumFacing.Plane.HORIZONTAL) {
			this.notifyRack(worldIn, pos.offset(direction));
		}
	}

	private void notifyRack(World worldIn, BlockPos pos) {
		if (worldIn.isRemote) {
			return;
		}

		IBlockState state = worldIn.getBlockState(pos);

		if (state.getBlock() == this) {
			worldIn.notifyBlockUpdate(pos, state, state, 3);
		}
	}

	private TileEntityBottleRack getRackEntity(World worldIn, BlockPos pos, boolean create) {
		IBlockState state = worldIn.getBlockState(pos);

		if (state.getBlock() != this) {
			return null;
		}

		if (!state.getValue(DATA).booleanValue()) {
			if (!create || worldIn.isRemote
					|| !worldIn.setBlockState(pos, state.withProperty(DATA, Boolean.valueOf(true)), 2)) {
				return null;
			}

			state = worldIn.getBlockState(pos);
		}

		TileEntity tileEntity = worldIn.getTileEntity(pos);

		if (tileEntity instanceof TileEntityBottleRack) {
			return (TileEntityBottleRack)tileEntity;
		}

		if (!create || worldIn.isRemote) {
			return null;
		}

		tileEntity = this.createTileEntity(worldIn, state);

		if (tileEntity != null) {
			worldIn.setTileEntity(pos, tileEntity);
		}

		return tileEntity instanceof TileEntityBottleRack ? (TileEntityBottleRack)tileEntity : null;
	}

	private void removeRackEntityIfEmpty(World worldIn, BlockPos pos) {
		if (worldIn.isRemote) {
			return;
		}

		TileEntity tileEntity = worldIn.getTileEntity(pos);

		if (!(tileEntity instanceof TileEntityBottleRack)) {
			return;
		}

		if (((TileEntityBottleRack)tileEntity).hasStoredData()) {
			return;
		}

		IBlockState state = worldIn.getBlockState(pos);

		if (state.getBlock() == this && state.getValue(DATA).booleanValue()) {
			worldIn.setBlockState(pos, state.withProperty(DATA, Boolean.valueOf(false)), 2);
		} else {
			worldIn.removeTileEntity(pos);
		}
	}

	private boolean isSameBottleStack(ItemStack storedItem, ItemStack heldItem) {
		return storedItem != null && heldItem != null && heldItem.stackSize > 0
			&& storedItem.isItemEqual(heldItem)
			&& ItemStack.areItemStackTagsEqual(storedItem, heldItem);
	}

	private void returnRackItem(World worldIn, BlockPos pos, EntityPlayer playerIn, ItemStack itemStack) {
		if (itemStack != null && itemStack.stackSize > 0
				&& !playerIn.inventory.addItemStackToInventory(itemStack)) {
			EntityItem entityItem = new EntityItem(worldIn, pos.getX() + 0.5D, pos.getY() + 0.5D,
				pos.getZ() + 0.5D, itemStack);
			worldIn.spawnEntity(entityItem);
		}
	}

	private boolean isBottleRackItem(ItemStack heldItem) {
		return heldItem != null && heldItem.stackSize > 0
			&& heldItem.getItem() instanceof ItemBlock
			&& ((ItemBlock)heldItem.getItem()).getBlock() instanceof BottleRack;
	}

	public static boolean isValidBottleItem(ItemStack itemStack) {
		if (itemStack == null || itemStack.stackSize <= 0) {
			return false;
		}

		Item item = itemStack.getItem();

		if (item == ItemObjectHolder.fluid_bottle) {
			return true;
		}

		if (item == Items.GLASS_BOTTLE || item == Items.POTIONITEM || item == Items.SPLASH_POTION
				|| item == Items.LINGERING_POTION || item == Items.EXPERIENCE_BOTTLE) {
			return true;
		}

		return isHarvestCraftBottleItem(itemStack);
	}

	private static boolean isHarvestCraftBottleItem(ItemStack itemStack) {
		if (!IronAgeFurnitureConfiguration.INTEGRATION_HARVESTCRAFT || !Loader.isModLoaded("harvestcraft")) {
			return false;
		}

		ResourceLocation registryName = itemStack.getItem().getRegistryName();

		if (registryName == null || !"harvestcraft".equals(registryName.getResourceDomain())) {
			return false;
		}

		String path = registryName.getResourcePath().toLowerCase(Locale.ROOT);

		if (containsAny(path, "smoothie", "milkshake", "coffee", "tea", "milk")) {
			return false;
		}

		return containsAny(path, "juice", "soda", "cola", "rootbeer", "cider")
			|| path.equals("freshwateritem")
			|| path.equals("bubblywateritem")
			|| path.endsWith("syrupitem")
			|| equalsAny(path, "oliveoilitem", "sesameoilitem", "vinegaritem", "soysauceitem",
				"hotsauceitem", "hoisinsauceitem", "saladdressingitem", "sweetandsoursauceitem");
	}

	private static boolean containsAny(String value, String... matches) {
		for (String match : matches) {
			if (value.contains(match)) {
				return true;
			}
		}

		return false;
	}

	private static boolean equalsAny(String value, String... matches) {
		for (String match : matches) {
			if (value.equals(match)) {
				return true;
			}
		}

		return false;
	}

	private boolean isRackLateralSide(EnumFacing rackFacing, EnumFacing clickedSide) {
		return clickedSide == this.rotateClockwise(rackFacing)
			|| clickedSide == this.rotateCounterClockwise(rackFacing);
	}

	private AxisAlignedBB rotateToFacing(AxisAlignedBB box, EnumFacing facing) {
		switch (facing) {
		case EAST:
			return this.rotateClockwise(box);
		case SOUTH:
			return this.rotateHalfTurn(box);
		case WEST:
			return this.rotateCounterClockwise(box);
		default:
			return box;
		}
	}

	private AxisAlignedBB rotateClockwise(AxisAlignedBB box) {
		return new AxisAlignedBB(1.0D - box.maxZ, box.minY, box.minX,
			1.0D - box.minZ, box.maxY, box.maxX);
	}

	private AxisAlignedBB rotateHalfTurn(AxisAlignedBB box) {
		return new AxisAlignedBB(1.0D - box.maxX, box.minY, 1.0D - box.maxZ,
			1.0D - box.minX, box.maxY, 1.0D - box.minZ);
	}

	private AxisAlignedBB rotateCounterClockwise(AxisAlignedBB box) {
		return new AxisAlignedBB(box.minZ, box.minY, 1.0D - box.maxX,
			box.maxZ, box.maxY, 1.0D - box.minX);
	}

	private EnumFacing rotateClockwise(EnumFacing facing) {
		switch (facing) {
		case NORTH:
			return EnumFacing.EAST;
		case EAST:
			return EnumFacing.SOUTH;
		case SOUTH:
			return EnumFacing.WEST;
		case WEST:
			return EnumFacing.NORTH;
		default:
			return facing;
		}
	}

	private EnumFacing rotateCounterClockwise(EnumFacing facing) {
		switch (facing) {
		case NORTH:
			return EnumFacing.WEST;
		case WEST:
			return EnumFacing.SOUTH;
		case SOUTH:
			return EnumFacing.EAST;
		case EAST:
			return EnumFacing.NORTH;
		default:
			return facing;
		}
	}
}
