package zone.moddev.mc.ironagefurniture.api;

import zone.moddev.mc.ironagefurniture.api.Enumerations.UpholsteryColour;
import zone.moddev.mc.ironagefurniture.api.Properties.UpholsteryColourProperty;
import zone.moddev.mc.ironagefurniture.api.tile.TileEntityUpholstery;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ChunkCache;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.property.IExtendedBlockState;

public final class UpholsteryColourHelper {
	public static final String COLOUR_TAG = "Color";
	private UpholsteryColourHelper() { }

	public static UpholsteryColour getColour(ItemStack stack) {
		if (stack == null) return UpholsteryColour.RED;
		if (stack.hasTagCompound() && stack.getTagCompound().hasKey(COLOUR_TAG))
			return UpholsteryColour.byName(stack.getTagCompound().getString(COLOUR_TAG));
		return UpholsteryColour.byItemMetadata(stack.getMetadata());
	}

	public static ItemStack setColour(ItemStack stack, UpholsteryColour colour) {
		if (stack == null) return null;
		NBTTagCompound tag = stack.hasTagCompound() ? stack.getTagCompound() : new NBTTagCompound();
		tag.setString(COLOUR_TAG, (colour == null ? UpholsteryColour.RED : colour).getSerializedName());
		stack.setTagCompound(tag);
		return stack;
	}

	public static ItemStack createStack(Block block, int count, UpholsteryColour colour) {
		UpholsteryColour resolved = colour == null ? UpholsteryColour.RED : colour;
		return setColour(new ItemStack(Item.getItemFromBlock(block), count, resolved.getItemMetadata()), resolved);
	}

	public static UpholsteryColour getColour(IBlockAccess world, BlockPos pos) {
		TileEntity tile = world instanceof ChunkCache
				? ((ChunkCache)world).getTileEntity(pos, Chunk.EnumCreateEntityType.CHECK)
				: world.getTileEntity(pos);
		return tile instanceof TileEntityUpholstery
				? ((TileEntityUpholstery)tile).getColour() : UpholsteryColour.RED;
	}

	public static void setColour(World world, BlockPos pos, UpholsteryColour colour) {
		TileEntity tile = world.getTileEntity(pos);
		if (tile instanceof TileEntityUpholstery) ((TileEntityUpholstery)tile).setColour(colour);
	}

	public static IBlockState withRenderColour(IBlockState state, IBlockAccess world, BlockPos pos) {
		if (state instanceof IExtendedBlockState)
			return ((IExtendedBlockState)state).withProperty(UpholsteryColourProperty.COLOUR,
					getColour(world, pos));
		return state;
	}
}
