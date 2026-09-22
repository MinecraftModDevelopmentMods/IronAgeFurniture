package zone.moddev.mc.ironagefurniture.api.Blocks;

import java.util.List;

import zone.moddev.mc.ironagefurniture.api.PaddedBenchColourHelper;
import zone.moddev.mc.ironagefurniture.api.Properties.PaddedBenchColourProperty;
import zone.moddev.mc.ironagefurniture.api.tile.TileEntityPaddedBench;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;

public class PaddedBackBench extends BackBench {
	public PaddedBackBench(Material materialIn, String name, float resistance, boolean tall,
			double yOffset, float hardness) {
		super(materialIn, name, resistance, tall, yOffset, hardness);
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new ExtendedBlockState(this, new IProperty[] { FACING, BackBench.TYPE },
				new IUnlistedProperty[] { PaddedBenchColourProperty.COLOUR });
	}

	@Override
	public IBlockState getExtendedState(IBlockState state, IBlockAccess world, BlockPos pos) {
		return PaddedBenchColourHelper.withRenderColour(state, world, pos);
	}

	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}

	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		return new TileEntityPaddedBench();
	}

	@Override
	public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
		return PaddedBenchColourHelper.getDrops(this, world, pos);
	}

	@Override
	public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world,
			BlockPos pos, EntityPlayer player) {
		return PaddedBenchColourHelper.getPickBlock(this, world, pos, state, target, player);
	}
}

