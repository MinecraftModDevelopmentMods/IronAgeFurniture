package zone.moddev.mc.ironagefurniture.api.blocks.base;

import net.minecraft.util.Direction;

public abstract class LightHolderSconce extends LightHolder {
	public LightHolderSconce(Properties properties) {
		super(properties);

		this.setDefaultState(this.getStateContainer().getBaseState() .with(DIRECTION, Direction.NORTH) .with(WATERLOGGED, false));
        this.generateShapes(this.getStateContainer().getValidStates());
	}
}
