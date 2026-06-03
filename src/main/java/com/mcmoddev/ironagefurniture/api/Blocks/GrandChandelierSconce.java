package com.mcmoddev.ironagefurniture.api.Blocks;

import java.util.List;
import java.util.Random;

import com.google.common.collect.Lists;
import com.mcmoddev.ironagefurniture.BlockObjectHolder;
import com.mcmoddev.ironagefurniture.api.MetalVariantHelper;
import com.mcmoddev.ironagefurniture.api.MetalVariantHelper.MetalVariant;
import com.mcmoddev.ironagefurniture.api.tile.TileEntityGrandChandelierSconce;
import com.mcmoddev.ironagefurniture.client.particle.CandleFlameParticle;

import net.minecraft.block.Block;
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
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class GrandChandelierSconce extends Block implements ITileEntityProvider {
	public static final PropertyDirection FACING = BlockHorizontal.FACING;
	public static final PropertyEnum<GrandChandelierLight> LIGHT = PropertyEnum.create("light", GrandChandelierLight.class);

	private static final AxisAlignedBB NORTH_AABB = new AxisAlignedBB(5.0D / 16.0D, 2.0D / 16.0D, 0.0D,
		11.0D / 16.0D, 11.0D / 16.0D, 16.0D / 16.0D);

	private final float baseResistance;
	private final float baseHardness;

	public GrandChandelierSconce(Material materialIn, String name, float resistance, float hardness) {
		super(materialIn);
		this.baseResistance = resistance;
		this.baseHardness = hardness;
		this.setDefaultState(this.blockState.getBaseState()
			.withProperty(FACING, EnumFacing.NORTH)
			.withProperty(LIGHT, GrandChandelierLight.EMPTY)
			.withProperty(MetalVariantHelper.METAL, MetalVariant.IRON));
		this.setUnlocalizedName(name);
		this.setSoundType(SoundType.METAL);
		this.setHardness(hardness);
		this.setResistance(resistance);
	}

	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntityGrandChandelierSconce();
	}

	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		return new TileEntityGrandChandelierSconce();
	}

	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
		return MetalVariantHelper.withMetal(state.withProperty(LIGHT, getStoredLight(worldIn, pos)), worldIn, pos);
	}

	private static GrandChandelierLight getStoredLight(IBlockAccess worldIn, BlockPos pos) {
		TileEntity te = worldIn.getTileEntity(pos);
		if (te instanceof TileEntityGrandChandelierSconce) {
			return ((TileEntityGrandChandelierSconce)te).getLight();
		}
		return GrandChandelierLight.EMPTY;
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
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] { FACING, LIGHT, MetalVariantHelper.METAL });
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
		return rotate(NORTH_AABB, state.getValue(FACING));
	}

	@Override
	public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox,
			List<AxisAlignedBB> collidingBoxes, Entity entityIn) {
		addCollisionBoxToList(pos, entityBox, collidingBoxes, rotate(NORTH_AABB, state.getValue(FACING)));
	}

	private static AxisAlignedBB rotate(AxisAlignedBB bb, EnumFacing facing) {
		switch (facing) {
			case EAST:
				return new AxisAlignedBB(0.0D, bb.minY, bb.minX, 1.0D - bb.minZ, bb.maxY, bb.maxX);
			case SOUTH:
				return new AxisAlignedBB(1.0D - bb.maxX, bb.minY, 1.0D - bb.maxZ,
					1.0D - bb.minX, bb.maxY, 1.0D - bb.minZ);
			case WEST:
				return new AxisAlignedBB(bb.minZ, bb.minY, 1.0D - bb.maxX, bb.maxZ, bb.maxY, 1.0D - bb.minX);
			case NORTH:
			default:
				return bb;
		}
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
	public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos) {
		return getStoredLight(world, pos).getLightLevel();
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
		GrandChandelierLight current = getStoredLight(worldIn, pos);

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
		ItemStack drop = current.getDropStack();
		if (heldItem != null && heldItem.stackSize > 0 && !isSameLightItem(heldItem, current)) {
			return false;
		}

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
			return getHubPower(world, pos) > 0 ? GrandChandelierLight.REDTORCH_UNLIT : GrandChandelierLight.REDTORCH;
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
		if (isItemFromBlock(heldItem, BlockObjectHolder.light_metal_ironage_block_floor_red_clear)) {
			return GrandChandelierLight.redLevel(getHubPower(world, pos));
		}
		return GrandChandelierLight.EMPTY;
	}

	private void consumeHeld(EntityPlayer player, ItemStack heldItem) {
		if (!player.capabilities.isCreativeMode) {
			heldItem.stackSize--;
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
		if (light.isRedLamp()) {
			return isItemFromBlock(heldItem, BlockObjectHolder.light_metal_ironage_block_floor_red_clear);
		}
		return false;
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

	private void setLight(World world, BlockPos pos, GrandChandelierLight light) {
		TileEntity te = world.getTileEntity(pos);
		if (te instanceof TileEntityGrandChandelierSconce) {
			((TileEntityGrandChandelierSconce)te).setLight(light);
		}
		world.checkLight(pos);
	}

	@Override
	public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
		worldIn.scheduleUpdate(pos, this, tickRate(worldIn));
	}

	@Override
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn) {
		BlockPos hub = GrandChandelierHub.findHub(worldIn, pos, state);
		if (hub != null) {
			worldIn.scheduleUpdate(hub, BlockObjectHolder.chandelier_grand_hub,
				BlockObjectHolder.chandelier_grand_hub.tickRate(worldIn));
		}
		worldIn.scheduleUpdate(pos, this, tickRate(worldIn));
	}

	@Override
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
		if (worldIn.isRemote) {
			return;
		}

		BlockPos hub = GrandChandelierHub.findHub(worldIn, pos, state);
		if (hub == null || !GrandChandelierHub.isCompleteStructure(worldIn, hub)) {
			worldIn.setBlockToAir(pos);
			return;
		}

		GrandChandelierLight current = getStoredLight(worldIn, pos);
		int power = GrandChandelierHub.getHubPower(worldIn, hub);
		if (current.isRedLamp()) {
			setLight(worldIn, pos, GrandChandelierLight.redLevel(power));
		} else if (current.isRedTorchFamily()) {
			setLight(worldIn, pos, power > 0 ? GrandChandelierLight.REDTORCH_UNLIT : GrandChandelierLight.REDTORCH);
		} else if ((current.isUnlitTorch() || current.isUnlitCandle()) && power > 0) {
			setLight(worldIn, pos, current.litVersion());
		}
	}

	@Override
	public int tickRate(World worldIn) {
		return 2;
	}

	@Override
	public boolean removedByPlayer(IBlockState state, World world, BlockPos pos, EntityPlayer player, boolean willHarvest) {
		if (!world.isRemote && !GrandChandelierHub.isRemovingStructure()) {
			GrandChandelierHub.breakStructureFromPart(world, pos, state, player);
			return true;
		}
		return super.removedByPlayer(state, world, pos, player, willHarvest);
	}

	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
		if (!worldIn.isRemote && !GrandChandelierHub.isRemovingStructure() && state.getBlock() == this) {
			GrandChandelierHub.breakStructureFromPart(worldIn, pos, state, null);
			return;
		}
		super.breakBlock(worldIn, pos, state);
	}

	@Override
	public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
		return Lists.newArrayList();
	}

	@Override
	public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random rand) {
		GrandChandelierLight light = getStoredLight(world, pos);
		if (light.isLitTorch()) {
			if (light.isTwinTorch()) {
				spawnTorchFlame(world, pos, state.getValue(FACING), 5.25D / 16.0D);
				spawnTorchFlame(world, pos, state.getValue(FACING), 10.75D / 16.0D);
			} else {
				spawnTorchFlame(world, pos, state.getValue(FACING), 0.5D);
			}
		} else if (light.isLitCandle()) {
			spawnCandleFlame(world, pos, state.getValue(FACING), light.getCandleCount(), rand);
		} else if (light == GrandChandelierLight.LAVA && rand.nextInt(25) == 0) {
			double[] point = rotatePoint(0.5D, 0.27D, 0.18D, state.getValue(FACING));
			world.spawnParticle(EnumParticleTypes.LAVA, pos.getX() + point[0], pos.getY() + point[1],
				pos.getZ() + point[2], 0.0D, 0.0D, 0.0D);
			world.playSound(pos.getX() + point[0], pos.getY() + point[1], pos.getZ() + point[2],
				net.minecraft.init.SoundEvents.BLOCK_LAVA_POP, SoundCategory.BLOCKS, 0.2F, 1.0F, false);
		} else if (light.isRedLamp() && light.getLightLevel() > 0) {
			double[] point = rotatePoint(0.5D, 0.27D, 0.18D, state.getValue(FACING));
			world.spawnParticle(EnumParticleTypes.REDSTONE, pos.getX() + point[0], pos.getY() + point[1],
				pos.getZ() + point[2], 0.0D, 0.0D, 0.0D);
		}
	}

	private void spawnTorchFlame(World world, BlockPos pos, EnumFacing facing, double x) {
		double[] point = rotatePoint(x, 0.76D, 0.045D, facing);
		world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, pos.getX() + point[0], pos.getY() + point[1],
			pos.getZ() + point[2], 0.0D, 0.0D, 0.0D);
		world.spawnParticle(EnumParticleTypes.FLAME, pos.getX() + point[0], pos.getY() + point[1],
			pos.getZ() + point[2], 0.0D, 0.0D, 0.0D);
	}

	private void spawnCandleFlame(World world, BlockPos pos, EnumFacing facing, int count, Random rand) {
		double[] candleXs = getCandleFlameXs(count);
		for (int i = 0; i < candleXs.length; i++) {
			double[] point = rotatePoint(candleXs[i], 0.765D, 0.10D, facing);
			CandleFlameParticle.spawn(world, pos.getX() + point[0], pos.getY() + point[1], pos.getZ() + point[2]);
		}
	}

	private double[] getCandleFlameXs(int count) {
		switch (count) {
			case 4:
				return new double[] { 5.15D / 16.0D, 7.05D / 16.0D, 8.95D / 16.0D, 10.85D / 16.0D };
			case 3:
				return new double[] { 5.8D / 16.0D, 8.0D / 16.0D, 10.2D / 16.0D };
			case 2:
				return new double[] { 6.65D / 16.0D, 9.35D / 16.0D };
			case 1:
			default:
				return new double[] { 0.5D };
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

	private int getHubPower(World world, BlockPos pos) {
		BlockPos hub = GrandChandelierHub.findHub(world, pos, world.getBlockState(pos));
		return hub == null ? 0 : GrandChandelierHub.getHubPower(world, hub);
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
			|| isItemFromBlock(heldItem, BlockObjectHolder.light_metal_ironage_block_floor_red_clear);
	}

	private boolean isEmptySconceItem(ItemStack heldItem) {
		return isItemFromBlock(heldItem, BlockObjectHolder.light_metal_ironage_sconce_floor_empty_iron)
			|| isItemFromBlock(heldItem, BlockObjectHolder.light_metal_ironage_sconce_wall_empty_iron);
	}

	private boolean isItemFromBlock(ItemStack stack, Block block) {
		return stack != null && stack.stackSize > 0 && block != null
			&& stack.getItem() == Item.getItemFromBlock(block);
	}
}
