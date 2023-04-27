package com.mcmoddev.ironagefurniture.api.util;

import com.mcmoddev.ironagefurniture.api.enumerations.Rotation;

import net.minecraft.core.Direction;

public class Swivel {
	public static Direction Rotate(Direction facing, Rotation rotation) {
		switch (rotation) {
			case Ninty:
				switch (facing) {
					case NORTH:
						return Direction.EAST;
					case SOUTH:
						return Direction.WEST;
					case EAST:
						return Direction.SOUTH;
					case WEST:
						return Direction.NORTH;
					default:
						return facing;
				}
			case OneEighty:
				switch (facing) {
					case NORTH:
						return Direction.SOUTH;
					case SOUTH:
						return Direction.NORTH;
					case EAST:
						return Direction.WEST;
					case WEST:
						return Direction.EAST;
					default:
						return facing;
				}
			case TwoSeventy:
				switch (facing) {
					case NORTH:
						return Direction.WEST;
					case SOUTH:
						return Direction.EAST;
					case EAST:
						return Direction.NORTH;
					case WEST:
						return Direction.SOUTH;
					default:
						return facing;
				}
			default:
				return facing;
		}
	}
}
