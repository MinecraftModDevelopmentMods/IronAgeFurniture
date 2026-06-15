package com.mcmoddev.ironagefurniture.api.Blocks;

import com.mcmoddev.ironagefurniture.api.Enumerations.ChairPart;

import net.minecraft.block.material.Material;
import net.minecraft.util.math.AxisAlignedBB;

public class ThroneChair extends MultiBlockChair {
	private static final AxisAlignedBB MIDDLE_BB = new AxisAlignedBB(0.0625D, 0.0D, 0.625D, 0.9375D, 1.0D, 1.0D);
	private static final AxisAlignedBB UPPER_BB = new AxisAlignedBB(0.0625D, 0.0D, 0.5D, 0.9375D, 0.9375D, 1.0D);
	private static final AxisAlignedBB MIDDLE_LEFT_COLLISION_BB = new AxisAlignedBB(0.0D, 0.0D, 0.5625D, 0.125D, 1.0D, 1.0D);
	private static final AxisAlignedBB MIDDLE_RIGHT_COLLISION_BB = new AxisAlignedBB(0.875D, 0.0D, 0.5625D, 1.0D, 1.0D, 1.0D);
	private static final AxisAlignedBB MIDDLE_BACK_COLLISION_BB = new AxisAlignedBB(0.125D, 0.0D, 0.875D, 0.875D, 1.0D, 1.0D);
	private static final AxisAlignedBB UPPER_LEFT_SIDE_COLLISION_BB = new AxisAlignedBB(0.0D, 0.0D, 0.5D, 0.125D, 0.75D, 1.0D);
	private static final AxisAlignedBB UPPER_RIGHT_SIDE_COLLISION_BB = new AxisAlignedBB(0.875D, 0.0D, 0.5D, 1.0D, 0.75D, 1.0D);
	private static final AxisAlignedBB UPPER_BACK_COLLISION_BB = new AxisAlignedBB(0.125D, 0.0D, 0.875D, 0.875D, 0.75D, 1.0D);
	private static final AxisAlignedBB UPPER_FRONT_COLLISION_BB = new AxisAlignedBB(0.125D, 0.8125D, 0.0D, 0.875D, 0.9375D, 0.9375D);
	private static final AxisAlignedBB[] MIDDLE_COLLISION_BOXES = new AxisAlignedBB[] {
		MIDDLE_LEFT_COLLISION_BB,
		MIDDLE_RIGHT_COLLISION_BB,
		MIDDLE_BACK_COLLISION_BB
	};
	private static final AxisAlignedBB[] UPPER_COLLISION_BOXES = new AxisAlignedBB[] {
		UPPER_LEFT_SIDE_COLLISION_BB,
		UPPER_RIGHT_SIDE_COLLISION_BB,
		UPPER_BACK_COLLISION_BB,
		UPPER_FRONT_COLLISION_BB
	};

	public ThroneChair(Material materialIn, String name, float resistance, float hardness) {
		super(materialIn, name, resistance, hardness);
	}

	@Override
	protected int getChairHeight() {
		return 3;
	}

	@Override
	protected AxisAlignedBB getUpperPartBoundingBox(ChairPart part) {
		return part == ChairPart.MIDDLE ? MIDDLE_BB : UPPER_BB;
	}

	@Override
	protected AxisAlignedBB[] getUpperPartCollisionBoxes(ChairPart part) {
		return part == ChairPart.MIDDLE ? MIDDLE_COLLISION_BOXES : UPPER_COLLISION_BOXES;
	}
}
