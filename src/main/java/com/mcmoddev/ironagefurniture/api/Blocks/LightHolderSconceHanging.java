package com.mcmoddev.ironagefurniture.api.Blocks;

import java.util.List;
import java.util.Random;

import com.google.common.collect.Lists;
import com.mcmoddev.ironagefurniture.BlockObjectHolder;
import com.mcmoddev.ironagefurniture.Ironagefurniture;
import com.mcmoddev.ironagefurniture.api.MetalVariantHelper;
import com.mcmoddev.ironagefurniture.api.MetalVariantHelper.MetalVariant;
import com.mcmoddev.ironagefurniture.api.MineralogyCompat;
import com.mcmoddev.ironagefurniture.api.entity.EntityFallingMetalBlock;
import com.mcmoddev.ironagefurniture.api.tile.TileEntityGrandChandelierSconce;
import com.mcmoddev.ironagefurniture.client.particle.CandleFlameParticle;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

public class LightHolderSconceHanging extends BlockFalling implements ITileEntityProvider {
	public static final PropertyDirection FACING = BlockHorizontal.FACING;
	public static final PropertyEnum<GrandChandelierLight> LIGHT =
		PropertyEnum.create("light", GrandChandelierLight.class);

	private static final AxisAlignedBB SHAPE_AABB = new AxisAlignedBB(
		2.0D / 16.0D, 2.0D / 16.0D, 2.0D / 16.0D,
		14.0D / 16.0D, 1.0D, 14.0D / 16.0D
	);
	private static final AxisAlignedBB COLLISION_AABB = new AxisAlignedBB(
		3.0D / 16.0D, 3.0D / 16.0D, 3.0D / 16.0D,
		13.0D / 16.0D, 13.0D / 16.0D, 13.0D / 16.0D
	);

	private final float baseResistance;
	private final float baseHardness;

	public LightHolderSconceHanging(Material materialIn, String name, float resistance, float hardness) {
		super(materialIn);
		this.baseResistance = resistance;
		this.baseHardness = hardness;
		this.setDefaultState(this.blockState.getBaseState()
			.withProperty(FACING, EnumFacing.NORTH)
			.withProperty(LIGHT, GrandChandelierLight.EMPTY)
			.withProperty(MetalVariantHelper.METAL, MetalVariant.IRON));
		this.setUnlocalizedName(name);
		this.setSoundType(SoundType.METAL);
		this.setHarvestLevel("pickaxe", 1);
		this.setCreativeTab(Ironagefurniture.ironagefurnitureTab);
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] { FACING, LIGHT, MetalVariantHelper.METAL });
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return this.getDefaultState().withProperty(FACING, EnumFacing.getHorizontal(meta & 3));
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return ((EnumFacing)state.getValue(FACING)).getHorizontalIndex();
	}

	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
		return MetalVariantHelper.withMetal(
			state.withProperty(LIGHT, getStoredLight(worldIn, pos, state)), worldIn, pos);
	}

	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}

	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		return new TileEntityGrandChandelierSconce();
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntityGrandChandelierSconce();
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
	public float getExplosionResistance(World world, BlockPos pos, Entity exploder,
			net.minecraft.world.Explosion explosion) {
		return MetalVariantHelper.getResistance(world, pos, this.baseResistance);
	}

	@Override
	public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos) {
		return getStoredLight(world, pos, state).getLightLevel();
	}

	@Override
	public boolean canPlaceBlockOnSide(World worldIn, BlockPos pos, EnumFacing side) {
		return side == EnumFacing.DOWN && canHangFrom(worldIn, pos.up());
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

	private boolean isFallingLandingPos(World worldIn, BlockPos pos) {
		return !worldIn.isAirBlock(pos.down())
			&& !BlockFalling.canFallThrough(worldIn.getBlockState(pos.down()));
	}

	@Override
	public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
		worldIn.scheduleUpdate(pos, this, this.tickRate(worldIn));
	}

	@Override
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn) {
		worldIn.scheduleUpdate(pos, this, this.tickRate(worldIn));
	}

	@Override
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
		if (worldIn.isRemote) {
			return;
		}

		if (checkFallable(worldIn, pos)) {
			return;
		}

		GrandChandelierLight current = getStoredLight(worldIn, pos, state);
		int power = getPowerFromAbove(worldIn, pos);

		if (current.isRedLamp()) {
			setLight(worldIn, pos, GrandChandelierLight.redLevel(power));
		} else if (current.isRedTorchFamily()) {
			setLight(worldIn, pos, power > 0 ? GrandChandelierLight.REDTORCH_UNLIT : GrandChandelierLight.REDTORCH);
		} else if ((current.isUnlitTorch() || current.isUnlitCandle()) && power > 0) {
			setLight(worldIn, pos, current.litVersion());
		}
	}

	private boolean checkFallable(World worldIn, BlockPos pos) {
		if (!shouldFall(worldIn, pos)) {
			return false;
		}

		int range = 32;
		IBlockState sourceState = worldIn.getBlockState(pos)
			.withProperty(FACING, worldIn.getBlockState(pos).getValue(FACING))
			.withProperty(LIGHT, getStoredLight(worldIn, pos, worldIn.getBlockState(pos)))
			.withProperty(MetalVariantHelper.METAL, MetalVariantHelper.getMetal(worldIn, pos));

		if (!fallInstantly && worldIn.isAreaLoaded(pos.add(-range, -range, -range), pos.add(range, range, range))) {
			EntityFallingBlock fallingBlock = new EntityFallingMetalBlock(worldIn,
				pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D, sourceState);
			this.onStartFalling(fallingBlock);
			worldIn.spawnEntity(fallingBlock);
			return true;
		}

		worldIn.setBlockToAir(pos);
		BlockPos landingPos;

		for (landingPos = pos.down();
				(worldIn.isAirBlock(landingPos) || BlockFalling.canFallThrough(worldIn.getBlockState(landingPos)))
					&& landingPos.getY() > 0;
				landingPos = landingPos.down()) {
			;
		}

		if (landingPos.getY() > 0) {
			BlockPos finalPos = landingPos.up();
			worldIn.setBlockState(finalPos, sourceState, 3);
			MetalVariantHelper.setMetal(worldIn, finalPos, sourceState.getValue(MetalVariantHelper.METAL));
			setLight(worldIn, finalPos, sourceState.getValue(LIGHT));
		}

		return true;
	}

	@Override
	protected void onStartFalling(EntityFallingBlock fallingEntity) {
		IBlockState state = fallingEntity.getBlock();
		if (state == null) {
			return;
		}

		if (fallingEntity.tileEntityData == null) {
			fallingEntity.tileEntityData = new NBTTagCompound();
		}

		if (state.getProperties().containsKey(MetalVariantHelper.METAL)) {
			fallingEntity.tileEntityData.setString("Metal",
				state.getValue(MetalVariantHelper.METAL).getName());
		}
		if (state.getProperties().containsKey(LIGHT)) {
			fallingEntity.tileEntityData.setString("Light",
				state.getValue(LIGHT).getName());
		}
	}

	@Override
	public void onEndFalling(World worldIn, BlockPos pos) {
		if (!worldIn.isRemote) {
			IBlockState state = worldIn.getBlockState(pos);
			worldIn.notifyBlockUpdate(pos, state, state, 3);
			worldIn.scheduleUpdate(pos, this, this.tickRate(worldIn));
		}
	}

	private boolean shouldFall(World worldIn, BlockPos pos) {
		return (worldIn.isAirBlock(pos.down()) || BlockFalling.canFallThrough(worldIn.getBlockState(pos.down())))
			&& !canHangFrom(worldIn, pos.up())
			&& pos.getY() >= 0;
	}

	private boolean canHangFrom(World worldIn, BlockPos supportPos) {
		return GrandChandelierHub.canHangFrom(worldIn, supportPos);
	}

	private int getPowerFromAbove(World worldIn, BlockPos pos) {
		BlockPos abovePos = pos.up();
		IBlockState aboveState = worldIn.getBlockState(abovePos);
		int power = worldIn.getRedstonePower(abovePos, EnumFacing.UP);

		if (aboveState.getBlock() == BlockObjectHolder.chain_top && power > 0) {
			power -= MetalVariantHelper.getMetal(worldIn, pos).getChainPowerLoss();
		}

		return Math.max(0, Math.min(15, power));
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
		GrandChandelierLight current = getStoredLight(worldIn, pos, state);

		if (handleWaterOrRelight(worldIn, pos, playerIn, heldItem, current)) {
			return true;
		}
		if (handleInsertion(worldIn, pos, playerIn, hand, heldItem, current)) {
			return true;
		}
		if (handleRemoval(worldIn, pos, playerIn, hand, heldItem, current)) {
			return true;
		}
		if (isSconceLightSourceItem(heldItem) || isEmptySconceItem(heldItem)) {
			return true;
		}

		return false;
	}

	private boolean handleRemoval(World world, BlockPos pos, EntityPlayer player, EnumHand hand, ItemStack heldItem,
			GrandChandelierLight current) {
		if (current == GrandChandelierLight.EMPTY) {
			return false;
		}
		if (heldItem != null && heldItem.stackSize > 0 && !isSameLightItem(heldItem, current)) {
			return false;
		}

		ItemStack drop = current.getDropStack();
		if (!world.isRemote) {
			setLight(world, pos, GrandChandelierLight.EMPTY);
			if (drop != null && !player.capabilities.isCreativeMode) {
				giveToPlayer(world, pos, player, hand, heldItem, drop);
			}
		}
		return true;
	}

	private boolean handleWaterOrRelight(World world, BlockPos pos, EntityPlayer player, ItemStack heldItem,
			GrandChandelierLight current) {
		if (heldItem == null || heldItem.stackSize <= 0) {
			return false;
		}

		if (heldItem.getItem() == Items.WATER_BUCKET && (current.isLitTorch() || current.isLitCandle())) {
			if (!world.isRemote) {
				setLight(world, pos, current.unlitVersion());
			}
			return true;
		}

		if ((current.isUnlitTorch() || current.isUnlitCandle())
				&& (heldItem.getItem() == Items.FLINT_AND_STEEL
					|| heldItem.getItem() == Item.getItemFromBlock(Blocks.TORCH)
					|| isItemFromBlock(heldItem, BlockObjectHolder.light_metal_ironage_candle_floor))) {
			if (!world.isRemote) {
				setLight(world, pos, current.litVersion());
				if (heldItem.getItem() == Items.FLINT_AND_STEEL && !player.capabilities.isCreativeMode) {
					heldItem.damageItem(1, player);
				}
			}
			return true;
		}

		return false;
	}

	private boolean handleInsertion(World world, BlockPos pos, EntityPlayer player, EnumHand hand, ItemStack heldItem,
			GrandChandelierLight current) {
		if (heldItem == null || heldItem.stackSize <= 0) {
			return false;
		}

		if (current == GrandChandelierLight.TORCH && heldItem.getItem() == Item.getItemFromBlock(Blocks.TORCH)) {
			if (!world.isRemote) {
				setLight(world, pos, GrandChandelierLight.TORCH_TWIN);
				consumeHeld(player, heldItem);
			}
			return true;
		}

		if (current.isLitCandle() && isItemFromBlock(heldItem, BlockObjectHolder.light_metal_ironage_candle_floor)) {
			int count = current.getCandleCount();
			if (!world.isRemote) {
				if (count >= 4) {
					setLight(world, pos, GrandChandelierLight.EMPTY);
					if (!player.capabilities.isCreativeMode) {
						giveToPlayer(world, pos, player, hand, heldItem,
							new ItemStack(BlockObjectHolder.light_metal_ironage_candle_floor, 4));
					}
				} else {
					setLight(world, pos, GrandChandelierLight.candle(count + 1, true));
					consumeHeld(player, heldItem);
				}
			}
			return true;
		}

		if (current != GrandChandelierLight.EMPTY) {
			return false;
		}

		GrandChandelierLight newLight = getLightForHeldItem(world, pos, heldItem);
		if (newLight == GrandChandelierLight.EMPTY) {
			return false;
		}

		if (!world.isRemote) {
			setLight(world, pos, newLight);
			consumeHeld(player, heldItem);
		}
		return true;
	}

	private GrandChandelierLight getLightForHeldItem(World world, BlockPos pos, ItemStack heldItem) {
		if (heldItem.getItem() == Item.getItemFromBlock(Blocks.TORCH)) {
			return GrandChandelierLight.TORCH;
		}
		if (heldItem.getItem() == Item.getItemFromBlock(Blocks.REDSTONE_TORCH)) {
			return getPowerFromAbove(world, pos) > 0 ? GrandChandelierLight.REDTORCH_UNLIT : GrandChandelierLight.REDTORCH;
		}
		if (isItemFromBlock(heldItem, BlockObjectHolder.light_metal_ironage_candle_floor)) {
			return GrandChandelierLight.CANDLE_1;
		}
		if (isItemFromBlock(heldItem, BlockObjectHolder.light_metal_ironage_block_floor_glow_clear)) {
			return GrandChandelierLight.GLOW;
		}
		if (isItemFromBlock(heldItem, BlockObjectHolder.light_metal_ironage_block_floor_lava_clear)) {
			return GrandChandelierLight.LAVA;
		}
		if (MineralogyCompat.isRockSaltLampItem(heldItem)) {
			return GrandChandelierLight.ROCK_SALT;
		}
		if (isItemFromBlock(heldItem, BlockObjectHolder.light_metal_ironage_block_floor_red_clear)) {
			return GrandChandelierLight.redLevel(getPowerFromAbove(world, pos));
		}
		return GrandChandelierLight.EMPTY;
	}

	private static GrandChandelierLight getStoredLight(IBlockAccess worldIn, BlockPos pos, IBlockState fallbackState) {
		TileEntity te = worldIn.getTileEntity(pos);
		if (te instanceof TileEntityGrandChandelierSconce) {
			return ((TileEntityGrandChandelierSconce)te).getLight();
		}
		if (fallbackState != null && fallbackState.getProperties().containsKey(LIGHT)) {
			return fallbackState.getValue(LIGHT);
		}
		return GrandChandelierLight.EMPTY;
	}

	private void setLight(World world, BlockPos pos, GrandChandelierLight light) {
		GrandChandelierLight oldLight = getStoredLight(world, pos, world.getBlockState(pos));
		TileEntity te = world.getTileEntity(pos);
		if (te instanceof TileEntityGrandChandelierSconce) {
			((TileEntityGrandChandelierSconce)te).setLight(light);
		}
		world.checkLight(pos);

		if (oldLight.isRedTorchFamily() || light.isRedTorchFamily()) {
			world.notifyNeighborsOfStateChange(pos, this);
			for (EnumFacing facing : EnumFacing.values()) {
				world.notifyNeighborsOfStateChange(pos.offset(facing), this);
			}
		}
	}

	private void consumeHeld(EntityPlayer player, ItemStack heldItem) {
		if (!player.capabilities.isCreativeMode) {
			heldItem.stackSize--;
		}
	}

	private void giveToPlayer(World world, BlockPos pos, EntityPlayer player, EnumHand hand, ItemStack heldItem,
			ItemStack stack) {
		if (heldItem == null || heldItem.stackSize <= 0) {
			player.setHeldItem(hand, stack);
			return;
		}
		if (heldItem.isItemEqual(stack) && ItemStack.areItemStackTagsEqual(heldItem, stack)
				&& heldItem.stackSize + stack.stackSize <= heldItem.getMaxStackSize()) {
			heldItem.stackSize += stack.stackSize;
			return;
		}
		if (!player.inventory.addItemStackToInventory(stack)) {
			spawnAsEntity(world, pos, stack);
		}
	}

	private boolean isSameLightItem(ItemStack heldItem, GrandChandelierLight light) {
		if (light.isTorchFamily()) {
			return heldItem.getItem() == Item.getItemFromBlock(Blocks.TORCH);
		}
		if (light.isRedTorchFamily()) {
			return heldItem.getItem() == Item.getItemFromBlock(Blocks.REDSTONE_TORCH);
		}
		if (light.isCandle()) {
			return isItemFromBlock(heldItem, BlockObjectHolder.light_metal_ironage_candle_floor);
		}
		if (light == GrandChandelierLight.GLOW) {
			return isItemFromBlock(heldItem, BlockObjectHolder.light_metal_ironage_block_floor_glow_clear);
		}
		if (light == GrandChandelierLight.LAVA) {
			return isItemFromBlock(heldItem, BlockObjectHolder.light_metal_ironage_block_floor_lava_clear);
		}
		if (light == GrandChandelierLight.ROCK_SALT) {
			return MineralogyCompat.isRockSaltLampItem(heldItem);
		}
		if (light.isRedLamp()) {
			return isItemFromBlock(heldItem, BlockObjectHolder.light_metal_ironage_block_floor_red_clear);
		}
		return false;
	}

	private boolean isSconceLightSourceItem(ItemStack heldItem) {
		if (heldItem == null || heldItem.stackSize <= 0) {
			return false;
		}
		return heldItem.getItem() == Item.getItemFromBlock(Blocks.TORCH)
			|| heldItem.getItem() == Item.getItemFromBlock(Blocks.REDSTONE_TORCH)
			|| isItemFromBlock(heldItem, BlockObjectHolder.light_metal_ironage_candle_floor)
			|| isItemFromBlock(heldItem, BlockObjectHolder.light_metal_ironage_block_floor_glow_clear)
			|| isItemFromBlock(heldItem, BlockObjectHolder.light_metal_ironage_block_floor_lava_clear)
			|| isItemFromBlock(heldItem, BlockObjectHolder.light_metal_ironage_block_floor_red_clear)
			|| MineralogyCompat.isRockSaltLampItem(heldItem);
	}

	private boolean isEmptySconceItem(ItemStack heldItem) {
		return isItemFromBlock(heldItem, BlockObjectHolder.light_metal_ironage_sconce_floor_empty_iron)
			|| isItemFromBlock(heldItem, BlockObjectHolder.light_metal_ironage_sconce_wall_empty_iron);
	}

	private boolean isItemFromBlock(ItemStack stack, Block block) {
		return stack != null && stack.stackSize > 0 && block != null
			&& stack.getItem() == Item.getItemFromBlock(block);
	}

	@Override
	public boolean canProvidePower(IBlockState state) {
		return true;
	}

	@Override
	public int getWeakPower(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side) {
		return getStoredLight(world, pos, state) == GrandChandelierLight.REDTORCH && side != EnumFacing.DOWN ? 15 : 0;
	}

	@Override
	public int getStrongPower(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side) {
		return this.getWeakPower(state, world, pos, side);
	}

	@Override
	public boolean canConnectRedstone(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side) {
		return side == null || side != EnumFacing.UP;
	}

	@Override
	public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
		List<ItemStack> drops = Lists.newArrayList(MetalVariantHelper.getDrop(
			BlockObjectHolder.light_metal_ironage_sconce_floor_empty_iron, world, pos));
		ItemStack lightDrop = getStoredLight(world, pos, state).getDropStack();
		if (lightDrop != null) {
			drops.add(lightDrop);
		}
		return drops;
	}

	@Override
	public int damageDropped(IBlockState state) {
		if (state != null && state.getProperties().containsKey(MetalVariantHelper.METAL)) {
			return state.getValue(MetalVariantHelper.METAL).getMeta();
		}
		return 0;
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return SHAPE_AABB;
	}

	@Nullable
	@Override
	public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, World worldIn, BlockPos pos) {
		return COLLISION_AABB;
	}

	@Override
	public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox,
			List<AxisAlignedBB> collidingBoxes, Entity entityIn) {
		super.addCollisionBoxToList(pos, entityBox, collidingBoxes, COLLISION_AABB);
	}

	@Override
	public AxisAlignedBB getSelectedBoundingBox(IBlockState state, World worldIn, BlockPos pos) {
		return SHAPE_AABB.offset(pos);
	}

	@Nullable
	@Override
	public RayTraceResult collisionRayTrace(IBlockState state, World worldIn, BlockPos pos, Vec3d start, Vec3d end) {
		return this.rayTrace(pos, start, end, SHAPE_AABB);
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
	public int tickRate(World worldIn) {
		return 2;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random rand) {
		GrandChandelierLight light = getStoredLight(world, pos, state);
		if (light.isLitTorch()) {
			if (light.isTwinTorch()) {
				spawnTorchFlame(world, pos, state.getValue(FACING), 5.0D / 16.0D);
				spawnTorchFlame(world, pos, state.getValue(FACING), 11.0D / 16.0D);
			} else {
				spawnTorchFlame(world, pos, state.getValue(FACING), 5.0D / 16.0D);
			}
		} else if (light.isLitCandle()) {
			spawnCandleFlame(world, pos, state.getValue(FACING), light.getCandleCount());
		} else if (light == GrandChandelierLight.LAVA && rand.nextInt(25) == 0) {
			double[] point = rotatePoint(0.5D, 0.46D, 0.5D, state.getValue(FACING));
			world.spawnParticle(EnumParticleTypes.LAVA, pos.getX() + point[0], pos.getY() + point[1],
				pos.getZ() + point[2], 0.0D, 0.0D, 0.0D);
			world.playSound(pos.getX() + point[0], pos.getY() + point[1], pos.getZ() + point[2],
				net.minecraft.init.SoundEvents.BLOCK_LAVA_POP, SoundCategory.BLOCKS, 0.2F, 1.0F, false);
		} else if (light == GrandChandelierLight.ROCK_SALT) {
			double[] point = rotatePoint(0.5D, 0.50D, 0.5D, state.getValue(FACING));
			world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, pos.getX() + point[0], pos.getY() + point[1],
				pos.getZ() + point[2], 0.0D, 0.0D, 0.0D);
		} else if (light.isRedLamp() && light.getLightLevel() > 0) {
			double[] point = rotatePoint(0.5D, 0.46D, 0.5D, state.getValue(FACING));
			world.spawnParticle(EnumParticleTypes.REDSTONE, pos.getX() + point[0], pos.getY() + point[1],
				pos.getZ() + point[2], 0.0D, 0.0D, 0.0D);
		}
	}

	private void spawnTorchFlame(World world, BlockPos pos, EnumFacing facing, double x) {
		double[] point = rotatePoint(x, 0.58D, 0.5D, facing);
		world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, pos.getX() + point[0], pos.getY() + point[1],
			pos.getZ() + point[2], 0.0D, 0.0D, 0.0D);
		world.spawnParticle(EnumParticleTypes.FLAME, pos.getX() + point[0], pos.getY() + point[1],
			pos.getZ() + point[2], 0.0D, 0.0D, 0.0D);
	}

	private void spawnCandleFlame(World world, BlockPos pos, EnumFacing facing, int count) {
		double[] candleXs = getCandleFlameXs(count);
		for (int i = 0; i < candleXs.length; i++) {
			double[] point = rotatePoint(candleXs[i], 0.52D, 0.5D, facing);
			CandleFlameParticle.spawn(world, pos.getX() + point[0], pos.getY() + point[1], pos.getZ() + point[2]);
		}
	}

	private double[] getCandleFlameXs(int count) {
		switch (count) {
			case 4:
				return new double[] { 4.6D / 16.0D, 6.2D / 16.0D, 9.8D / 16.0D, 11.4D / 16.0D };
			case 3:
				return new double[] { 5.0D / 16.0D, 9.8D / 16.0D, 11.4D / 16.0D };
			case 2:
				return new double[] { 5.0D / 16.0D, 11.0D / 16.0D };
			case 1:
			default:
				return new double[] { 5.0D / 16.0D };
		}
	}

	private static double[] rotatePoint(double x, double y, double z, EnumFacing facing) {
		switch (facing) {
			case EAST:
				return new double[] { 1.0D - z, y, x };
			case SOUTH:
				return new double[] { 1.0D - x, y, 1.0D - z };
			case WEST:
				return new double[] { z, y, 1.0D - x };
			case NORTH:
			default:
				return new double[] { x, y, z };
		}
	}
}
