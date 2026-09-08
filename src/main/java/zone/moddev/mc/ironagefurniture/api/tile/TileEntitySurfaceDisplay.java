package zone.moddev.mc.ironagefurniture.api.tile;

import zone.moddev.mc.ironagefurniture.api.surface.SurfaceSetting;
import zone.moddev.mc.ironagefurniture.api.surface.SurfaceSettingHost;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;

public class TileEntitySurfaceDisplay extends TileEntity implements SurfaceSettingHost {
	private final SurfaceSetting surfaceSetting = new SurfaceSetting();

	public boolean hasDisplayedItem() {
		return this.surfaceSetting.hasAnyItem();
	}

	public ItemStack getDisplayedItem() {
		return this.surfaceSetting.getCompatibilityItem();
	}

	public EnumFacing getDisplayedFacing() {
		return this.surfaceSetting.getCompatibilityFacing();
	}

	public void setDisplayedItem(ItemStack itemStack, EnumFacing facing) {
		this.surfaceSetting.setSingleItem(itemStack, facing);
		this.markSurfaceSettingChanged();
	}

	public ItemStack removeDisplayedItem() {
		ItemStack itemStack = this.surfaceSetting.removeCompatibilityItem();
		this.markSurfaceSettingChanged();
		return itemStack;
	}

	@Override
	public SurfaceSetting getSurfaceSetting() {
		return this.surfaceSetting;
	}

	@Override
	public void markSurfaceSettingChanged() {
		this.markForUpdate();
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		this.surfaceSetting.readFromNBT(compound);
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);

		this.surfaceSetting.writeToNBT(compound);

		return compound;
	}

	@Override
	public SPacketUpdateTileEntity getUpdatePacket() {
		return new SPacketUpdateTileEntity(this.pos, 0, this.getUpdateTag());
	}

	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity packet) {
		this.readFromNBT(packet.getNbtCompound());

		if (this.world != null) {
			this.world.markBlockRangeForRenderUpdate(this.pos, this.pos);
		}
	}

	@Override
	public NBTTagCompound getUpdateTag() {
		return this.writeToNBT(new NBTTagCompound());
	}

	private void markForUpdate() {
		this.markDirty();

		if (this.world != null && !this.world.isRemote) {
			this.world.notifyBlockUpdate(this.pos, this.world.getBlockState(this.pos),
				this.world.getBlockState(this.pos), 3);
		}

		if (this.world != null) {
			this.world.checkLight(this.pos);

			for (EnumFacing facing : EnumFacing.values()) {
				this.world.checkLight(this.pos.offset(facing));
			}
		}
	}
}
