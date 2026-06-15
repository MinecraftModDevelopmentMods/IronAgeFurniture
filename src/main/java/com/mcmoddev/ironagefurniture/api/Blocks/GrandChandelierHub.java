package com.mcmoddev.ironagefurniture.api.Blocks;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.common.collect.Lists;
import com.mcmoddev.ironagefurniture.BlockObjectHolder;
import com.mcmoddev.ironagefurniture.api.MetalVariantHelper;
import com.mcmoddev.ironagefurniture.api.MetalVariantHelper.MetalVariant;
import com.mcmoddev.ironagefurniture.api.entity.EntityFallingMetalBlock;
import com.mcmoddev.ironagefurniture.api.tile.TileEntityGrandChandelierSconce;
import com.mcmoddev.ironagefurniture.api.tile.TileEntityMetalVariant;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class GrandChandelierHub extends BlockFalling implements ITileEntityProvider {
	public static final PropertyInteger POWER = PropertyInteger.create("power", 0, 15);
	private static final AxisAlignedBB HUB_AABB = new AxisAlignedBB(5.0D / 16.0D, 1.0D / 16.0D, 5.0D / 16.0D,
		11.0D / 16.0D, 12.0D / 16.0D, 11.0D / 16.0D);
	private static final Set<BlockPos> fallingSources = new HashSet<BlockPos>();
	private static boolean removingStructure;

	private final float baseResistance;
	private final float baseHardness;

	public GrandChandelierHub(Material materialIn, String name, float resistance, float hardness) {
		super(materialIn);
		this.baseResistance = resistance;
		this.baseHardness = hardness;
		this.setDefaultState(this.blockState.getBaseState()
			.withProperty(POWER, Integer.valueOf(0))
			.withProperty(MetalVariantHelper.METAL, MetalVariant.IRON));
		this.setUnlocalizedName(name);
		this.setSoundType(SoundType.METAL);
		this.setHardness(hardness);
		this.setResistance(resistance);
		this.setTickRandomly(false);
		this.setCreativeTab(com.mcmoddev.ironagefurniture.Ironagefurniture.ironagefurnitureTab);
	}

	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntityMetalVariant();
	}

	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		return new TileEntityMetalVariant();
	}

	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
		return MetalVariantHelper.withMetal(state, MetalVariantHelper.getMetal(worldIn, pos));
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return this.getDefaultState().withProperty(POWER, Integer.valueOf(meta & 15));
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return ((Integer)state.getValue(POWER)).intValue() & 15;
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] { POWER, MetalVariantHelper.METAL });
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
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.CUTOUT;
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return HUB_AABB;
	}

	@Override
	public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox,
			List<AxisAlignedBB> collidingBoxes, Entity entityIn) {
		addCollisionBoxToList(pos, entityBox, collidingBoxes, HUB_AABB);
	}

	@Override
	public float getBlockHardness(IBlockState blockState, World worldIn, BlockPos pos) {
		return MetalVariantHelper.getHardness(worldIn, pos, this.baseHardness);
	}

	@Override
	public float getExplosionResistance(Entity exploder) {
		return this.baseResistance;
	}

	@Override
	public float getExplosionResistance(World world, BlockPos pos, Entity exploder, net.minecraft.world.Explosion explosion) {
		return MetalVariantHelper.getResistance(world, pos, this.baseResistance);
	}

	@Override
	public boolean canPlaceBlockOnSide(World worldIn, BlockPos pos, EnumFacing side) {
		return side == EnumFacing.DOWN && canPlaceStructureAt(worldIn, pos);
	}

	@Override
	public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
		return canHangFrom(worldIn, pos.up()) || isFallingLandingPos(worldIn, pos);
	}

	@Override
	public boolean canReplace(World worldIn, BlockPos pos, EnumFacing side, ItemStack stack) {
		return super.canReplace(worldIn, pos, side, stack)
			|| (stack == null && isFallingLandingPos(worldIn, pos));
	}

	public static boolean canPlaceStructureAt(World world, BlockPos hubPos) {
		if (!canHangFrom(world, hubPos.up())) {
			return false;
		}
		if (!isReplaceableForStructure(world, hubPos)) {
			return false;
		}
		for (EnumFacing facing : EnumFacing.HORIZONTALS) {
			if (!isReplaceableForStructure(world, hubPos.offset(facing))) {
				return false;
			}
		}
		return true;
	}

	private boolean isFallingLandingPos(World worldIn, BlockPos pos) {
		return !worldIn.isAirBlock(pos.down())
			&& !BlockFalling.canFallThrough(worldIn.getBlockState(pos.down()));
	}

	private static boolean isReplaceableForStructure(World world, BlockPos pos) {
		IBlockState state = world.getBlockState(pos);
		return state.getBlock().isReplaceable(world, pos) || state.getBlock().canReplace(world, pos, EnumFacing.UP, null);
	}

	public static void placeStructure(World world, BlockPos hubPos, MetalVariant metal) {
		IBlockState hubState = MetalVariantHelper.withMetal(BlockObjectHolder.chandelier_grand_hub.getDefaultState(), metal);
		world.setBlockState(hubPos, hubState, 3);
		MetalVariantHelper.setMetal(world, hubPos, metal);

		for (EnumFacing facing : EnumFacing.HORIZONTALS) {
			IBlockState armState = MetalVariantHelper.withMetal(BlockObjectHolder.chandelier_grand_sconce.getDefaultState()
				.withProperty(GrandChandelierSconce.FACING, facing)
				.withProperty(GrandChandelierSconce.LIGHT, GrandChandelierLight.EMPTY), metal);
			BlockPos armPos = hubPos.offset(facing);
			world.setBlockState(armPos, armState, 3);
			MetalVariantHelper.setMetal(world, armPos, metal);

			TileEntity te = world.getTileEntity(armPos);
			if (te instanceof TileEntityGrandChandelierSconce) {
				((TileEntityGrandChandelierSconce)te).setLight(GrandChandelierLight.EMPTY);
			}
		}
	}

	public static BlockPos findHub(World world, BlockPos pos, IBlockState state) {
		if (state.getBlock() == BlockObjectHolder.chandelier_grand_hub) {
			return pos;
		}
		if (state.getBlock() == BlockObjectHolder.chandelier_grand_sconce) {
			EnumFacing facing = state.getValue(GrandChandelierSconce.FACING);
			return pos.offset(facing.getOpposite());
		}
		return null;
	}

	public static boolean isCompleteStructure(World world, BlockPos hubPos) {
		if (world.getBlockState(hubPos).getBlock() != BlockObjectHolder.chandelier_grand_hub) {
			return false;
		}
		for (EnumFacing facing : EnumFacing.HORIZONTALS) {
			IBlockState armState = world.getBlockState(hubPos.offset(facing));
			if (armState.getBlock() != BlockObjectHolder.chandelier_grand_sconce
					|| armState.getValue(GrandChandelierSconce.FACING) != facing) {
				return false;
			}
		}
		return true;
	}

	public static void breakStructureFromPart(World world, BlockPos partPos, IBlockState partState, EntityPlayer player) {
		BlockPos hubPos = findHub(world, partPos, partState);
		if (hubPos != null) {
			((GrandChandelierHub)BlockObjectHolder.chandelier_grand_hub).breakStructure(world, hubPos, player, false);
		}
	}

	public void breakStructure(World world, BlockPos hubPos, EntityPlayer player, boolean startFalling) {
		if (world.isRemote || removingStructure) {
			return;
		}

		removingStructure = true;
		try {
			MetalVariant metal = MetalVariantHelper.getMetal(world, hubPos);
			int power = getHubPower(world, hubPos);
			boolean creative = player != null && player.capabilities.isCreativeMode;
			boolean shouldTryFalling = startFalling && !creative && canStartFalling(world, hubPos);
			List<BlockPos> armPositions = Lists.newArrayList();
			List<GrandChandelierLight> armLights = Lists.newArrayList();

			for (EnumFacing facing : EnumFacing.HORIZONTALS) {
				BlockPos armPos = hubPos.offset(facing);
				armPositions.add(armPos);
				armLights.add(getArmLight(world, armPos));
			}

			for (BlockPos armPos : armPositions) {
				world.setBlockToAir(armPos);
			}
			if (!shouldTryFalling) {
				world.setBlockToAir(hubPos);
			}

			for (int i = 0; i < armPositions.size(); i++) {
				releaseArmLight(world, armPositions.get(i), armLights.get(i), !creative);
			}

			if (creative) {
				return;
			}

			if (shouldTryFalling && spawnFallingHub(world, hubPos, metal, power)) {
				return;
			}

			if (shouldTryFalling) {
				world.setBlockToAir(hubPos);
			}

			if (world.getGameRules().getBoolean("doTileDrops")) {
				spawnAsEntity(world, hubPos, MetalVariantHelper.getDrop(BlockObjectHolder.chandelier_grand_hub, metal, 1));
			}
		} finally {
			removingStructure = false;
		}
	}

	private static boolean canStartFalling(World world, BlockPos hubPos) {
		return BlockFalling.canFallThrough(world.getBlockState(hubPos.down()));
	}

	private boolean spawnFallingHub(World world, BlockPos hubPos, MetalVariant metal, int power) {
		IBlockState fallingState = MetalVariantHelper.withMetal(this.getDefaultState()
			.withProperty(POWER, Integer.valueOf(power)), metal);
		EntityFallingMetalBlock falling = new EntityFallingMetalBlock(world,
			hubPos.getX() + 0.5D, hubPos.getY(), hubPos.getZ() + 0.5D, fallingState);
		falling.tileEntityData = new NBTTagCompound();
		falling.tileEntityData.setString("Metal", metal.getName());
		BlockPos sourcePos = new BlockPos(hubPos);
		fallingSources.add(sourcePos);

		if (world.spawnEntity(falling)) {
			return true;
		}

		fallingSources.remove(sourcePos);
		return false;
	}

	private static GrandChandelierLight getArmLight(World world, BlockPos armPos) {
		TileEntity te = world.getTileEntity(armPos);
		if (!(te instanceof TileEntityGrandChandelierSconce)) {
			return GrandChandelierLight.EMPTY;
		}

		return ((TileEntityGrandChandelierSconce)te).getLight();
	}

	private static void releaseArmLight(World world, BlockPos armPos, GrandChandelierLight light, boolean drop) {
		if (!drop || light == GrandChandelierLight.EMPTY) {
			return;
		}

		if (light == GrandChandelierLight.LAVA && BlockObjectHolder.light_metal_ironage_block_floor_lava_clear != null) {
			world.setBlockState(armPos, BlockObjectHolder.light_metal_ironage_block_floor_lava_clear.getDefaultState(), 3);
			world.scheduleUpdate(armPos, BlockObjectHolder.light_metal_ironage_block_floor_lava_clear,
				BlockObjectHolder.light_metal_ironage_block_floor_lava_clear.tickRate(world));
			return;
		}

		ItemStack stack = light.getDropStack();
		if (drop && stack != null && world.getGameRules().getBoolean("doTileDrops")) {
			spawnAsEntity(world, armPos, stack);
		}
	}

	@Override
	public boolean removedByPlayer(IBlockState state, World world, BlockPos pos, EntityPlayer player, boolean willHarvest) {
		if (!world.isRemote && !removingStructure) {
			breakStructure(world, pos, player, false);
			return true;
		}
		return super.removedByPlayer(state, world, pos, player, willHarvest);
	}

	@Override
	public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, TileEntity te,
			ItemStack stack) {
		// The structure teardown handles the one visible drop while preserving TE-backed metal.
	}

	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
		if (!worldIn.isRemote && state.getBlock() == this && fallingSources.remove(pos)) {
			super.breakBlock(worldIn, pos, state);
			return;
		}
		if (!worldIn.isRemote && !removingStructure && state.getBlock() == this) {
			breakStructure(worldIn, pos, null, false);
			return;
		}
		super.breakBlock(worldIn, pos, state);
	}

	@Override
	public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
		worldIn.scheduleUpdate(pos, this, tickRate(worldIn));
	}

	@Override
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn) {
		worldIn.scheduleUpdate(pos, this, tickRate(worldIn));
	}

	@Override
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, java.util.Random rand) {
		if (worldIn.isRemote || removingStructure) {
			return;
		}

		if (!isCompleteStructure(worldIn, pos)) {
			breakStructure(worldIn, pos, null, false);
			return;
		}

		if (!canHangFrom(worldIn, pos.up())) {
			breakStructure(worldIn, pos, null, true);
			return;
		}

		int newPower = getPowerFromAbove(worldIn, pos);
		if (((Integer)state.getValue(POWER)).intValue() != newPower) {
			MetalVariant metal = MetalVariantHelper.getMetal(worldIn, pos);
			IBlockState newState = MetalVariantHelper.withMetal(this.getDefaultState()
				.withProperty(POWER, Integer.valueOf(newPower)), metal);
			worldIn.setBlockState(pos, newState, 3);
			MetalVariantHelper.setMetal(worldIn, pos, metal);
			notifyPowerNeighbors(worldIn, pos);
		}

		for (EnumFacing facing : EnumFacing.HORIZONTALS) {
			worldIn.scheduleUpdate(pos.offset(facing), BlockObjectHolder.chandelier_grand_sconce,
				BlockObjectHolder.chandelier_grand_sconce.tickRate(worldIn));
		}
	}

	private int getPowerFromAbove(World worldIn, BlockPos pos) {
		BlockPos abovePos = pos.up();
		int power = worldIn.getRedstonePower(abovePos, EnumFacing.UP);

		if (worldIn.getBlockState(abovePos).getBlock() == BlockObjectHolder.chain_top && power > 0) {
			power = Math.max(0, power - MetalVariantHelper.getMetal(worldIn, pos).getChainPowerLoss());
		}

		return power;
	}

	public static int getHubPower(World world, BlockPos hubPos) {
		IBlockState state = world.getBlockState(hubPos);
		if (state.getBlock() == BlockObjectHolder.chandelier_grand_hub) {
			return ((Integer)state.getValue(POWER)).intValue();
		}
		return 0;
	}

	@Override
	public int tickRate(World worldIn) {
		return 2;
	}

	@Override
	public boolean canProvidePower(IBlockState state) {
		return true;
	}

	@Override
	public int getWeakPower(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side) {
		return side == EnumFacing.UP ? ((Integer)state.getValue(POWER)).intValue() : 0;
	}

	@Override
	public boolean canConnectRedstone(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side) {
		return side == null || side == EnumFacing.DOWN;
	}

	private void notifyPowerNeighbors(World worldIn, BlockPos pos) {
		worldIn.notifyNeighborsOfStateChange(pos, this);
		worldIn.notifyNeighborsOfStateChange(pos.down(), this);
	}

	static boolean isRemovingStructure() {
		return removingStructure;
	}

	@Override
	protected void onStartFalling(EntityFallingBlock fallingEntity) {
		super.onStartFalling(fallingEntity);
		if (fallingEntity.tileEntityData == null) {
			fallingEntity.tileEntityData = new NBTTagCompound();
		}
		fallingEntity.tileEntityData.setString("Metal", getMetalFromState(fallingEntity.getBlock()).getName());
	}

	@Override
	public void onEndFalling(World worldIn, BlockPos pos) {
		if (worldIn.isRemote) {
			return;
		}

		IBlockState state = worldIn.getBlockState(pos);
		MetalVariant metal = getMetalFromState(state);
		if (canRebuildAfterFall(worldIn, pos)) {
			placeStructure(worldIn, pos, metal);
		} else {
			worldIn.setBlockToAir(pos);
			if (worldIn.getGameRules().getBoolean("doTileDrops")) {
				spawnAsEntity(worldIn, pos, MetalVariantHelper.getDrop(BlockObjectHolder.chandelier_grand_hub, metal, 1));
			}
		}
	}

	private boolean canRebuildAfterFall(World worldIn, BlockPos hubPos) {
		if (!isReplaceableAfterFall(worldIn, hubPos)) {
			return false;
		}
		for (EnumFacing facing : EnumFacing.HORIZONTALS) {
			if (!isReplaceableAfterFall(worldIn, hubPos.offset(facing))) {
				return false;
			}
		}
		return true;
	}

	private boolean isReplaceableAfterFall(World worldIn, BlockPos pos) {
		IBlockState state = worldIn.getBlockState(pos);
		return state.getBlock() == this || state.getBlock().isReplaceable(worldIn, pos);
	}

	public static boolean canHangFrom(World worldIn, BlockPos supportPos) {
		IBlockState support = worldIn.getBlockState(supportPos);
		return support.getBlock() == BlockObjectHolder.chain_top
			|| support.isSideSolid(worldIn, supportPos, EnumFacing.DOWN);
	}

	@Override
	public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
		return Lists.newArrayList(MetalVariantHelper.getDrop(BlockObjectHolder.chandelier_grand_hub, world, pos));
	}

	@Override
	public int damageDropped(IBlockState state) {
		return ((MetalVariant)state.getValue(MetalVariantHelper.METAL)).getMeta();
	}

	private static MetalVariant getMetalFromState(IBlockState state) {
		if (state != null && state.getProperties().containsKey(MetalVariantHelper.METAL)) {
			return state.getValue(MetalVariantHelper.METAL);
		}

		return MetalVariant.IRON;
	}
}
