package com.mcmoddev.ironagefurniture.api.Blocks;

import java.util.List;
import java.util.Random;

import com.mcmoddev.ironagefurniture.BlockObjectHolder;
import com.mcmoddev.ironagefurniture.api.MineralogyCompat;
import com.mcmoddev.ironagefurniture.api.tile.TileEntityDiningTable;
import com.mcmoddev.ironagefurniture.client.particle.CandleFlameParticle;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class LowTable extends DiningTable {
	public static final PropertyEnum<TableEmbeddedContent> CONTENTS = PropertyEnum.create("contents",
		TableEmbeddedContent.class);

	private static final AxisAlignedBB TOP_STANDALONE = new AxisAlignedBB(0.0625D, 0.4375D, 0.0625D,
		0.9375D, 0.5625D, 0.9375D);
	private static final AxisAlignedBB SHELF_STANDALONE = new AxisAlignedBB(0.1875D, 0.1875D, 0.1875D,
		0.8125D, 0.25D, 0.8125D);
	private static final AxisAlignedBB LEG_NORTH_WEST = new AxisAlignedBB(0.125D, 0.0D, 0.125D,
		0.25D, 0.4375D, 0.25D);
	private static final AxisAlignedBB LEG_NORTH_EAST = new AxisAlignedBB(0.75D, 0.0D, 0.125D,
		0.875D, 0.4375D, 0.25D);
	private static final AxisAlignedBB LEG_SOUTH_WEST = new AxisAlignedBB(0.125D, 0.0D, 0.75D,
		0.25D, 0.4375D, 0.875D);
	private static final AxisAlignedBB LEG_SOUTH_EAST = new AxisAlignedBB(0.75D, 0.0D, 0.75D,
		0.875D, 0.4375D, 0.875D);

	public LowTable(Material materialIn, String name, float resistance, float hardness) {
		super(materialIn, name, resistance, hardness);
		this.setLightOpacity(0);
		this.setDefaultState(this.blockState.getBaseState()
			.withProperty(CONNECTIONS, Integer.valueOf(0))
			.withProperty(CONTENTS, TableEmbeddedContent.NONE)
			.withProperty(DATA, Boolean.valueOf(false)));
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumHand hand, ItemStack heldItem, net.minecraft.util.EnumFacing side, float hitX, float hitY,
			float hitZ) {
		if (playerIn.isSneaking()) {
			return false;
		}

		TableEmbeddedContent heldContent = this.getEmbeddedContentForItem(heldItem);
		TileEntityDiningTable table = this.getTableEntity(worldIn, pos, false);

		if (table != null && table.getEmbeddedContent() == TableEmbeddedContent.FLOWER_POT) {
			if (this.canHandleFlowerPotClick(table, heldItem)) {
				if (worldIn.isRemote) {
					return true;
				}

				this.handleFlowerPotClick(worldIn, pos, table, playerIn, hand, heldItem);
			}

			return true;
		}

		if (heldContent != TableEmbeddedContent.NONE) {
			if (worldIn.isRemote) {
				return true;
			}

			if (table != null && table.hasEmbeddedContent()) {
				if (this.isSameItemStack(table.getEmbeddedItem(), heldItem)) {
					this.removeEmbeddedItemFromTable(worldIn, pos, table, playerIn);
				}

				return true;
			}

			if (table == null || table.canSetEmbeddedContent(heldContent)) {
				table = this.getTableEntity(worldIn, pos, true);

				if (table != null && table.setEmbeddedContent(heldContent, heldItem)) {
					if (!playerIn.capabilities.isCreativeMode) {
						heldItem.stackSize--;

						if (heldItem.stackSize <= 0) {
							playerIn.setHeldItem(hand, null);
						}
					}
				}
			}

			return true;
		}

		table = this.getTableEntity(worldIn, pos, false);

		if (table != null && table.hasEmbeddedContent()) {
			if (heldItem == null || heldItem.stackSize <= 0 || this.isSameItemStack(table.getEmbeddedItem(), heldItem)) {
				if (worldIn.isRemote) {
					return true;
				}

				this.removeEmbeddedItemFromTable(worldIn, pos, table, playerIn);
			}

			return true;
		}

		return super.onBlockActivated(worldIn, pos, state, playerIn, hand, heldItem, side, hitX, hitY, hitZ);
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return this.getTopBoundingBox(this.getConnectionMask(source, pos));
	}

	@Override
	public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox,
			List<AxisAlignedBB> collidingBoxes, Entity entityIn) {
		int connections = this.getConnectionMask(worldIn, pos);
		super.addCollisionBoxToList(pos, entityBox, collidingBoxes, this.getTopBoundingBox(connections));
		super.addCollisionBoxToList(pos, entityBox, collidingBoxes, this.getShelfBoundingBox(connections));

		if (!this.isConnected(connections, NORTH) && !this.isConnected(connections, WEST)) {
			super.addCollisionBoxToList(pos, entityBox, collidingBoxes, LEG_NORTH_WEST);
		}
		if (!this.isConnected(connections, NORTH) && !this.isConnected(connections, EAST)) {
			super.addCollisionBoxToList(pos, entityBox, collidingBoxes, LEG_NORTH_EAST);
		}
		if (!this.isConnected(connections, SOUTH) && !this.isConnected(connections, WEST)) {
			super.addCollisionBoxToList(pos, entityBox, collidingBoxes, LEG_SOUTH_WEST);
		}
		if (!this.isConnected(connections, SOUTH) && !this.isConnected(connections, EAST)) {
			super.addCollisionBoxToList(pos, entityBox, collidingBoxes, LEG_SOUTH_EAST);
		}
	}

	@Override
	public double getDisplayItemYOffset() {
		return 9.05D / 16.0D;
	}

	@Override
	public double getDisplayBlockSurfaceYOffset() {
		return 9.0D / 16.0D;
	}

	@Override
	protected boolean isDisplayExcluded(ItemStack heldItem) {
		return false;
	}

	@Override
	protected boolean supportsSurfaceBlocks() {
		return false;
	}

	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
		return super.getActualState(state, worldIn, pos)
			.withProperty(CONTENTS, this.getEmbeddedContents(worldIn, pos));
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] { CONNECTIONS, CONTENTS, DATA });
	}

	@Override
	public int getLightValue(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
		TileEntity tileEntity = worldIn.getTileEntity(pos);
		return tileEntity instanceof TileEntityDiningTable
			? ((TileEntityDiningTable)tileEntity).getEmbeddedLightLevel() : 0;
	}

	@Override
	public float getAmbientOcclusionLightValue(IBlockState state) {
		return state.getValue(CONTENTS) == TableEmbeddedContent.NONE ? super.getAmbientOcclusionLightValue(state)
			: 1.0F;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.CUTOUT;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random rand) {
		TableEmbeddedContent contents = this.getEmbeddedContents(world, pos);

		if (contents != TableEmbeddedContent.CANDLE && contents != TableEmbeddedContent.ROCK_SALT) {
			return;
		}

		double[] rotated = this.rotateTablePoint(this.getConnectionMask(world, pos), 8.0D / 16.0D, 8.5D / 16.0D);
		double x = pos.getX() + rotated[0];
		double y = pos.getY() + 15.7D / 16.0D;
		double z = pos.getZ() + rotated[1];

		if (contents == TableEmbeddedContent.ROCK_SALT) {
			if (rand.nextInt(3) == 0) {
				world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, x, y, z, 0.0D, 0.0D, 0.0D);
			}

			return;
		}

		if (rand.nextInt(3) == 0) {
			world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, x, y + 0.04D, z, 0.0D, 0.0D, 0.0D);
		}

		CandleFlameParticle.spawn(world, x, y, z);
	}

	private void removeEmbeddedItemFromTable(World worldIn, BlockPos pos, TileEntityDiningTable table,
			EntityPlayer playerIn) {
		ItemStack embeddedItem = table.removeEmbeddedItem();

		if (embeddedItem != null) {
			if (!playerIn.inventory.addItemStackToInventory(embeddedItem)) {
				EntityItem entityItem = new EntityItem(worldIn, pos.getX() + 0.5D, pos.getY() + 0.75D,
					pos.getZ() + 0.5D, embeddedItem);
				worldIn.spawnEntity(entityItem);
			}
		}

		this.removeTableEntityIfEmpty(worldIn, pos);
	}

	private boolean canHandleFlowerPotClick(TileEntityDiningTable table, ItemStack heldItem) {
		if (table.hasEmbeddedFlowerPotPlant()) {
			return heldItem == null || heldItem.stackSize <= 0
				|| this.isSameItemStack(table.getEmbeddedFlowerPotPlant(), heldItem);
		}

		return heldItem == null || heldItem.stackSize <= 0
			|| this.isSameItemStack(table.getEmbeddedItem(), heldItem)
			|| this.isFlowerPotPlantItem(heldItem);
	}

	private void handleFlowerPotClick(World worldIn, BlockPos pos, TileEntityDiningTable table,
			EntityPlayer playerIn, EnumHand hand, ItemStack heldItem) {
		if (table.hasEmbeddedFlowerPotPlant()) {
			this.returnEmbeddedItem(worldIn, pos, playerIn, table.removeEmbeddedFlowerPotPlant());
			this.removeTableEntityIfEmpty(worldIn, pos);
			return;
		}

		if (this.isFlowerPotPlantItem(heldItem)) {
			ItemStack plant = heldItem.copy();
			plant.stackSize = 1;
			table.setEmbeddedFlowerPotPlant(plant);

			if (!playerIn.capabilities.isCreativeMode) {
				heldItem.stackSize--;

				if (heldItem.stackSize <= 0) {
					playerIn.setHeldItem(hand, null);
				}
			}

			return;
		}

		this.returnEmbeddedItem(worldIn, pos, playerIn, table.removeEmbeddedItem());
		this.removeTableEntityIfEmpty(worldIn, pos);
	}

	private void returnEmbeddedItem(World worldIn, BlockPos pos, EntityPlayer playerIn, ItemStack itemStack) {
		if (itemStack != null && !playerIn.inventory.addItemStackToInventory(itemStack)) {
			EntityItem entityItem = new EntityItem(worldIn, pos.getX() + 0.5D, pos.getY() + 0.75D,
				pos.getZ() + 0.5D, itemStack);
			worldIn.spawnEntity(entityItem);
		}
	}

	private TableEmbeddedContent getEmbeddedContentForItem(ItemStack heldItem) {
		if (heldItem == null || heldItem.stackSize <= 0) {
			return TableEmbeddedContent.NONE;
		}

		Block heldBlock = this.getHeldItemBlock(heldItem);

		if (this.isItemFromBlock(heldItem, BlockObjectHolder.light_metal_ironage_block_floor_lava_clear)) {
			return TableEmbeddedContent.LAVA;
		}

		if (this.hasRegistryPath(heldBlock, "light_metal_ironage_block_floor_lava_clear")
				|| heldBlock instanceof LightSourceLava) {
			return TableEmbeddedContent.LAVA;
		}

		if (heldBlock instanceof LightSourceRed) {
			return TableEmbeddedContent.NONE;
		}

		if (MineralogyCompat.isRockSaltLampItem(heldItem)) {
			return TableEmbeddedContent.ROCK_SALT;
		}

		if (this.isItemFromBlock(heldItem, BlockObjectHolder.light_metal_ironage_block_floor_glow_clear)) {
			return TableEmbeddedContent.GLOW;
		}

		if (this.hasRegistryPath(heldBlock, "light_metal_ironage_block_floor_glow_clear")
				|| heldBlock instanceof LightSourceGlowdust) {
			return TableEmbeddedContent.GLOW;
		}

		if (this.isItemFromBlock(heldItem, BlockObjectHolder.light_metal_ironage_candle_floor)) {
			return TableEmbeddedContent.CANDLE;
		}

		if (this.hasRegistryPath(heldBlock, "light_metal_ironage_candle_floor")
				|| heldBlock instanceof LightSourceCandleFloor) {
			return TableEmbeddedContent.CANDLE;
		}

		if (this.isFlowerPotItem(heldItem)) {
			return TableEmbeddedContent.FLOWER_POT;
		}

		return TableEmbeddedContent.NONE;
	}

	private boolean isFlowerPotPlantItem(ItemStack heldItem) {
		Block heldBlock = this.getHeldItemBlock(heldItem);
		return heldBlock == Blocks.RED_FLOWER
			|| heldBlock == Blocks.YELLOW_FLOWER
			|| heldBlock == Blocks.SAPLING
			|| heldBlock == Blocks.BROWN_MUSHROOM
			|| heldBlock == Blocks.RED_MUSHROOM
			|| heldBlock == Blocks.DEADBUSH
			|| heldBlock == Blocks.CACTUS;
	}

	private Block getHeldItemBlock(ItemStack heldItem) {
		return heldItem != null && heldItem.getItem() instanceof ItemBlock
			? ((ItemBlock)heldItem.getItem()).getBlock() : null;
	}

	private boolean hasRegistryPath(Block block, String path) {
		return block != null && block.getRegistryName() != null
			&& path.equals(block.getRegistryName().getResourcePath());
	}

	private boolean isItemFromBlock(ItemStack heldItem, Block block) {
		return block != null && heldItem.getItem() == Item.getItemFromBlock(block);
	}

	private double[] rotateTablePoint(int connections, double x, double z) {
		switch (this.getModelRotationY(connections)) {
		case 90:
			return new double[] { 1.0D - z, x };
		case 180:
			return new double[] { 1.0D - x, 1.0D - z };
		case 270:
			return new double[] { z, 1.0D - x };
		default:
			return new double[] { x, z };
		}
	}

	private int getModelRotationY(int connections) {
		switch (connections) {
		case EAST:
		case EAST | SOUTH:
		case EAST | WEST:
		case EAST | SOUTH | WEST:
			return 90;
		case SOUTH:
		case SOUTH | WEST:
		case NORTH | SOUTH | WEST:
			return 180;
		case WEST:
		case NORTH | WEST:
		case NORTH | EAST | WEST:
			return 270;
		default:
			return 0;
		}
	}

	private TableEmbeddedContent getEmbeddedContents(IBlockAccess worldIn, BlockPos pos) {
		TileEntity tileEntity = worldIn.getTileEntity(pos);
		return tileEntity instanceof TileEntityDiningTable
			? ((TileEntityDiningTable)tileEntity).getEmbeddedContent() : TableEmbeddedContent.NONE;
	}

	private AxisAlignedBB getTopBoundingBox(int connections) {
		double minX = this.isConnected(connections, WEST) ? 0.0D : TOP_STANDALONE.minX;
		double maxX = this.isConnected(connections, EAST) ? 1.0D : TOP_STANDALONE.maxX;
		double minZ = this.isConnected(connections, NORTH) ? 0.0D : TOP_STANDALONE.minZ;
		double maxZ = this.isConnected(connections, SOUTH) ? 1.0D : TOP_STANDALONE.maxZ;

		return new AxisAlignedBB(minX, TOP_STANDALONE.minY, minZ, maxX, TOP_STANDALONE.maxY, maxZ);
	}

	private AxisAlignedBB getShelfBoundingBox(int connections) {
		double minX = this.isConnected(connections, WEST) ? 0.0D : SHELF_STANDALONE.minX;
		double maxX = this.isConnected(connections, EAST) ? 1.0D : SHELF_STANDALONE.maxX;
		double minZ = this.isConnected(connections, NORTH) ? 0.0D : SHELF_STANDALONE.minZ;
		double maxZ = this.isConnected(connections, SOUTH) ? 1.0D : SHELF_STANDALONE.maxZ;

		return new AxisAlignedBB(minX, SHELF_STANDALONE.minY, minZ, maxX, SHELF_STANDALONE.maxY, maxZ);
	}
}
