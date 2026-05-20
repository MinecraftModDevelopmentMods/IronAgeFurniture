package com.mcmoddev.ironagefurniture.api.Blocks;

import com.mcmoddev.ironagefurniture.BlockObjectHolder;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public class LightSourceChandelierTorchUnlit extends LightSourceChandelierCandleUnlit {
    public LightSourceChandelierTorchUnlit(Material materialIn, String name, float resistance, float hardness) {
        super(materialIn, name, resistance, hardness);
    }

    @Override
    protected Block GetLitVariant() {
        return BlockObjectHolder.chandelier_torch;
    }

    @Override
    protected Block VisibleDropBlock() {
        return BlockObjectHolder.chandelier_torch;
    }
}
