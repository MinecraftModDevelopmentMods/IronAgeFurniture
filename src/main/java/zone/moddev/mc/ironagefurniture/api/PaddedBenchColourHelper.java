package zone.moddev.mc.ironagefurniture.api;

import java.util.Collections;
import java.util.List;

import zone.moddev.mc.ironagefurniture.api.Enumerations.PaddedBenchColour;
import zone.moddev.mc.ironagefurniture.api.Properties.PaddedBenchColourProperty;
import zone.moddev.mc.ironagefurniture.api.tile.TileEntityPaddedBench;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.ChunkCache;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.property.IExtendedBlockState;

public final class PaddedBenchColourHelper {
	private PaddedBenchColourHelper() {
		throw new IllegalAccessError("This class cannot be instantiated");
	}

	public static PaddedBenchColour getColour(IBlockAccess world, BlockPos pos) {
		TileEntity tileEntity;
		if (world instanceof ChunkCache) {
			tileEntity = ((ChunkCache)world).getTileEntity(pos, Chunk.EnumCreateEntityType.CHECK);
		} else {
			tileEntity = world.getTileEntity(pos);
		}
		return tileEntity instanceof TileEntityPaddedBench
				? ((TileEntityPaddedBench)tileEntity).getColour()
				: PaddedBenchColour.RED;
	}

	public static void setColour(World world, BlockPos pos, PaddedBenchColour colour) {
		TileEntity tileEntity = world.getTileEntity(pos);
		if (tileEntity instanceof TileEntityPaddedBench) {
			((TileEntityPaddedBench)tileEntity).setColour(colour);
		}
	}

	public static IBlockState withRenderColour(IBlockState state, IBlockAccess world, BlockPos pos) {
		if (state instanceof IExtendedBlockState) {
			return ((IExtendedBlockState)state).withProperty(
					PaddedBenchColourProperty.COLOUR, getColour(world, pos));
		}
		return state;
	}

	public static List<ItemStack> getDrops(Block block, IBlockAccess world, BlockPos pos) {
		return Collections.singletonList(new ItemStack(Item.getItemFromBlock(block), 1,
				getColour(world, pos).getItemMetadata()));
	}

	public static ItemStack getPickBlock(Block block, World world, BlockPos pos,
			IBlockState state, RayTraceResult target, EntityPlayer player) {
		return new ItemStack(Item.getItemFromBlock(block), 1,
				getColour(world, pos).getItemMetadata());
	}
}
