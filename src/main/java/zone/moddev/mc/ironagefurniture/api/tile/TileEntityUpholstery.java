package zone.moddev.mc.ironagefurniture.api.tile;

import zone.moddev.mc.ironagefurniture.api.Enumerations.UpholsteryColour;
import zone.moddev.mc.ironagefurniture.api.UpholsteryColourHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TileEntityUpholstery extends TileEntity {
	private UpholsteryColour colour = UpholsteryColour.RED;

	public UpholsteryColour getColour() { return colour == null ? UpholsteryColour.RED : colour; }

	public void setColour(UpholsteryColour next) {
		UpholsteryColour resolved = next == null ? UpholsteryColour.RED : next;
		if (getColour() == resolved) return;
		colour = resolved;
		markDirty();
		if (world != null && pos != null) {
			IBlockState state = world.getBlockState(pos);
			world.notifyBlockUpdate(pos, state, state, 3);
		}
	}

	@Override public void readFromNBT(NBTTagCompound tag) {
		super.readFromNBT(tag);
		colour = tag.hasKey(UpholsteryColourHelper.COLOUR_TAG)
				? UpholsteryColour.byName(tag.getString(UpholsteryColourHelper.COLOUR_TAG)) : UpholsteryColour.RED;
	}

	@Override public NBTTagCompound writeToNBT(NBTTagCompound tag) {
		super.writeToNBT(tag);
		tag.setString(UpholsteryColourHelper.COLOUR_TAG, getColour().getSerializedName());
		return tag;
	}

	@Override public NBTTagCompound getUpdateTag() { return writeToNBT(new NBTTagCompound()); }
	@Override public void handleUpdateTag(NBTTagCompound tag) { readFromNBT(tag); }
	@Override public SPacketUpdateTileEntity getUpdatePacket() {
		return new SPacketUpdateTileEntity(pos, 0, getUpdateTag());
	}
	@Override public void onDataPacket(NetworkManager manager, SPacketUpdateTileEntity packet) {
		readFromNBT(packet.getNbtCompound());
		if (world != null && pos != null) world.markBlockRangeForRenderUpdate(pos, pos);
	}
	@Override public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState) {
		return oldState.getBlock() != newState.getBlock();
	}
}
