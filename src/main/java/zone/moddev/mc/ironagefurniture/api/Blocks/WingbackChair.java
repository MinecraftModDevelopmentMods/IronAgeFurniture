package zone.moddev.mc.ironagefurniture.api.Blocks;

import zone.moddev.mc.ironagefurniture.api.Enumerations.ChairPart;

import net.minecraft.block.material.Material;
import net.minecraft.util.math.AxisAlignedBB;

public class WingbackChair extends MultiBlockChair {
	private static final AxisAlignedBB UPPER_BB = new AxisAlignedBB(0.0625D, 0.0D, 0.5D, 0.9375D, 0.75D, 1.0D);
	private static final AxisAlignedBB UPPER_LEFT_SIDE_COLLISION_BB = new AxisAlignedBB(0.0D, 0.0D, 0.5D, 0.125D, 0.75D, 1.0D);
	private static final AxisAlignedBB UPPER_RIGHT_SIDE_COLLISION_BB = new AxisAlignedBB(0.875D, 0.0D, 0.5D, 1.0D, 0.75D, 1.0D);
	private static final AxisAlignedBB UPPER_BACK_COLLISION_BB = new AxisAlignedBB(0.125D, 0.0D, 0.875D, 0.875D, 0.75D, 1.0D);
	private static final AxisAlignedBB[] UPPER_COLLISION_BOXES = new AxisAlignedBB[] {
		UPPER_LEFT_SIDE_COLLISION_BB,
		UPPER_RIGHT_SIDE_COLLISION_BB,
		UPPER_BACK_COLLISION_BB
	};

	public WingbackChair(Material materialIn, String name, float resistance, float hardness) {
		super(materialIn, name, resistance, hardness);
	}

	@Override
	protected int getChairHeight() {
		return 2;
	}

	@Override
	protected AxisAlignedBB getUpperPartBoundingBox(ChairPart part) {
		return UPPER_BB;
	}

	@Override
	protected AxisAlignedBB[] getUpperPartCollisionBoxes(ChairPart part) {
		return UPPER_COLLISION_BOXES;
	}
}
