package com.mcmoddev.ironagefurniture.api.util;

import com.mcmoddev.ironagefurniture.api.Enumerations.Rotation;

import net.minecraft.util.EnumFacing;

public class Swivel {
	public static EnumFacing Rotate(EnumFacing facing, Rotation rotation) {
		switch (rotation) {
		case Ninty:
			switch (facing) {
				case NORTH:
					return EnumFacing.EAST;
	
				case SOUTH:
					return EnumFacing.WEST;
		
				case EAST:
					return EnumFacing.SOUTH;		
	
				case WEST:
					return EnumFacing.NORTH;
	
				default:
					return facing;		
			}
			
		case OneEighty:
			switch (facing) {
				case NORTH:
					return EnumFacing.SOUTH;
	
				case SOUTH:
					return EnumFacing.NORTH;
		
				case EAST:
					return EnumFacing.WEST;		
	
				case WEST:
					return EnumFacing.EAST;
	
				default:
					return facing;		
			}
		case TwoSeventy:
	
			switch (facing) {
				case NORTH:
					return EnumFacing.WEST;
	
				case SOUTH:
					return EnumFacing.EAST;
		
				case EAST:
					return EnumFacing.NORTH;		
	
				case WEST:
					return EnumFacing.SOUTH;
	
				default:
					return facing;		
			}
	
		default:
			return facing;
			
		}
	}
}
