package zone.moddev.mc.ironagefurniture.api.Blocks;

import java.util.Random;

import zone.moddev.mc.ironagefurniture.BlockObjectHolder;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class LightSourceChandelierTorch extends LightSourceChandelierCandle {
    private static final double[][] TORCH_POINTS = new double[][] {
        { 8.0D / 16.0D, 10.4D / 16.0D, 15.0D / 16.0D },
        { 1.0D / 16.0D, 10.4D / 16.0D, 8.0D / 16.0D },
        { 8.0D / 16.0D, 10.4D / 16.0D, 1.0D / 16.0D },
        { 15.0D / 16.0D, 10.4D / 16.0D, 8.0D / 16.0D }
    };

    public LightSourceChandelierTorch(Material materialIn, String name, float resistance, float hardness) {
        super(materialIn, name, resistance, hardness);
    }

    @Override
    public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random rand) {
        if (!IsLit()) {
            return;
        }

        for (double[] torchPoint : TORCH_POINTS) {
            double x = pos.getX() + torchPoint[0];
            double y = pos.getY() + torchPoint[1];
            double z = pos.getZ() + torchPoint[2];

            world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, x, y, z, 0.0D, 0.0D, 0.0D);
            world.spawnParticle(EnumParticleTypes.FLAME, x, y, z, 0.0D, 0.0D, 0.0D);
        }
    }

    @Override
    protected Block GetUnlitVariant() {
        return BlockObjectHolder.chandelier_torch_unlit;
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
