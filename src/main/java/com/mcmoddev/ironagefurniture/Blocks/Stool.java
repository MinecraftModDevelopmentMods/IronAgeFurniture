package com.mcmoddev.ironagefurniture.Blocks;

import java.util.List;

import com.mcmoddev.ironagefurniture.entity.Seat;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class Stool extends Chair {

	boolean tall = false;
	
	private static final AxisAlignedBB BBSHORT = new AxisAlignedBB(0.2, 0.0, 0.2, 0.8, 0.45, 0.8);
	private static final AxisAlignedBB BBTALL = new AxisAlignedBB(0.2, 0.0, 0.2, 0.8, 0.8, 0.8);
	
	public Stool(Material materialIn, String name, float resistance, boolean tall, double yOffset) {
		super(materialIn, name, resistance, yOffset);
		this.tall = tall;
		// TODO Auto-generated constructor stub
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) 
	{
		if (tall)
			return BBTALL;
		else
			return BBSHORT;
	}
	
	@Override
	public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox,
			List<AxisAlignedBB> collidingBoxes, Entity entityIn) {
		
		if (!(entityIn instanceof Seat)) {
			if (tall)
				super.addCollisionBoxToList(pos, entityBox, collidingBoxes, BBTALL);
			else
				super.addCollisionBoxToList(pos, entityBox, collidingBoxes, BBSHORT);
		}
	}
}
