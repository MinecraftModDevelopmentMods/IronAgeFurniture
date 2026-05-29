package com.mcmoddev.ironagefurniture.api.Blocks;

import java.util.List;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class LowTable extends DiningTable {
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
		return 0.60D;
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
