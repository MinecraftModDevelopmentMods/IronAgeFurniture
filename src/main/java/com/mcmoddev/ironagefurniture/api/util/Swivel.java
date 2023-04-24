package com.mcmoddev.ironagefurniture.api.util;

import com.mcmoddev.ironagefurniture.api.Enumerations.Rotation;

import net.minecraft.core.Direction;

public class Swivel {
	public static Direction Rotate(Direction facing, Rotation rotation) {
		switch (rotation) {
			case Ninty -> {
				return switch (facing) {
					case NORTH -> Direction.EAST;
					case SOUTH -> Direction.WEST;
					case EAST -> Direction.SOUTH;
					case WEST -> Direction.NORTH;
					default -> facing;
				};
			}
			case OneEighty -> {
				return switch (facing) {
					case NORTH -> Direction.SOUTH;
					case SOUTH -> Direction.NORTH;
					case EAST -> Direction.WEST;
					case WEST -> Direction.EAST;
					default -> facing;
				};
			}
			case TwoSeventy -> {
				return switch (facing) {
					case NORTH -> Direction.WEST;
					case SOUTH -> Direction.EAST;
					case EAST -> Direction.NORTH;
					case WEST -> Direction.SOUTH;
					default -> facing;
				};
			}
			default -> {
				return facing;
			}
		}
	}
}
