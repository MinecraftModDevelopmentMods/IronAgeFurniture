package com.mcmoddev.ironagefurniture.api.Blocks;

import com.mcmoddev.ironagefurniture.api.Enumerations.Rotation;

import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.AxisAlignedBB;

/**
 * This class forms the base for horizontal blocks in ironagefurniture.
 *
 * @author SkyBlade1978
 *
 */
public class BlockHBase extends BlockHorizontal {

	public BlockHBase(Material materialIn) {
		super(materialIn);
		// TODO Auto-generated constructor stub
	}

	public static AxisAlignedBB RotateBB(Rotation rotation, AxisAlignedBB bbIn)
	{		
		switch (rotation)
		{
			case Ninty:
				return new AxisAlignedBB(bbIn.minZ, bbIn.minY, 1.0F - bbIn.maxX, bbIn.maxY, bbIn.maxY, 1.0F - bbIn.minX);
			case OneEighty:
				return new AxisAlignedBB(1.0F - bbIn.maxY, bbIn.minY, bbIn.minX, 1.0F - bbIn.minZ, bbIn.maxY, bbIn.maxX);
			case TwoSeventy:
				return new AxisAlignedBB(1.0F - bbIn.maxX, bbIn.minY, 1.0F - bbIn.maxY, 1.0F - bbIn.minX, bbIn.maxY, 1.0F - bbIn.minZ);
			default:
				return bbIn;
		}
	}
}
