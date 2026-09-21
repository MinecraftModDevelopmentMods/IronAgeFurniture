package zone.moddev.mc.ironagefurniture.api.tile;

import zone.moddev.mc.ironagefurniture.api.Enumerations.PaddedBenchColour;
import zone.moddev.mc.ironagefurniture.api.PaddedBenchColourHelper;

import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TileEntityPaddedBench extends TileEntity {
	private PaddedBenchColour colour = PaddedBenchColour.RED;

	public PaddedBenchColour getColour() {
		return this.colour == null ? PaddedBenchColour.RED : this.colour;
	}

	public void setColour(PaddedBenchColour colour) {
		PaddedBenchColour next = colour == null ? PaddedBenchColour.RED : colour;
		if (this.getColour() == next) {
			return;
		}

		this.colour = next;
		this.markDirty();
		if (this.world != null && this.pos != null) {
			IBlockState state = this.world.getBlockState(this.pos);
			this.world.notifyBlockUpdate(this.pos, state, state, 3);
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		this.colour = compound.hasKey(PaddedBenchColourHelper.COLOUR_TAG)
				? PaddedBenchColour.byName(compound.getString(PaddedBenchColourHelper.COLOUR_TAG))
				: PaddedBenchColour.RED;
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		compound.setString(PaddedBenchColourHelper.COLOUR_TAG,
				this.getColour().getSerializedName());
		return compound;
	}

	@Override
	public NBTTagCompound getUpdateTag() {
		return this.writeToNBT(new NBTTagCompound());
	}

	@Override
	public void handleUpdateTag(NBTTagCompound tag) {
		this.readFromNBT(tag);
	}

	@Override
	public SPacketUpdateTileEntity getUpdatePacket() {
		return new SPacketUpdateTileEntity(this.pos, 0, this.getUpdateTag());
	}

	@Override
	public void onDataPacket(NetworkManager networkManager, SPacketUpdateTileEntity packet) {
		this.readFromNBT(packet.getNbtCompound());
		if (this.world != null && this.pos != null) {
			this.world.markBlockRangeForRenderUpdate(this.pos, this.pos);
		}
	}

	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState) {
		return oldState.getBlock() != newState.getBlock();
	}
}
