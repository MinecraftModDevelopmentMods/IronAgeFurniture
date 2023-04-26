package com.mcmoddev.ironagefurniture.api.Blocks.Base;

import net.minecraft.core.Direction;

public abstract class LightHolderSconce extends LightHolder {
	public LightHolderSconce(Properties properties) {
		super(properties);

		this.registerDefaultState(this.getStateDefinition().any().setValue(DIRECTION, Direction.NORTH).setValue(WATERLOGGED, false));
        this.generateShapes(this.getStateDefinition().getPossibleStates());
	}
}
