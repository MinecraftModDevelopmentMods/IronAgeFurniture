package com.mcmoddev.ironagefurniture.api.Blocks;

import com.mcmoddev.ironagefurniture.api.Enumerations.BenchType;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.util.Direction;
import net.minecraftforge.common.ToolType;

public class Bench extends BackBench {

	public Bench(float hardness, float blastResistance, SoundType sound, String name) {
		super(hardness, blastResistance, sound, name);
	}
	
	
//	public Bench(Material materialIn, String name, float resistance, boolean tall, double yOffset, float hardness) {
//		super(materialIn, name, resistance, tall, yOffset, hardness);
//		// TODO Auto-generated constructor stub
//	}
//
//	private static final AxisAlignedBB BBSHORT = new AxisAlignedBB(0.2, 0.0, 0.2, 0.9, 0.45, 0.9);
//	
//		
//	@Override
//	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) 
//	{
//		return BBSHORT;
//	}
//	
//	@Override
//	public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox,
//			List<AxisAlignedBB> collidingBoxes, Entity entityIn) {
//		
//		if (!(entityIn instanceof Seat)) {
//			super.addCollisionBoxToList(pos, entityBox, collidingBoxes, BBSHORT);
//		}
//	}
}