package com.mcmoddev.ironagefurniture.api.tile;

import com.mcmoddev.ironagefurniture.api.Blocks.GrandChandelierLight;

import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;

public class TileEntityGrandChandelierSconce extends TileEntityMetalVariant {
	private GrandChandelierLight light = GrandChandelierLight.EMPTY;

	public GrandChandelierLight getLight() {
		return this.light;
	}

	public void setLight(GrandChandelierLight light) {
		if (light == null) {
			light = GrandChandelierLight.EMPTY;
		}
		if (this.light == light) {
			return;
		}

		this.light = light;
		this.markForGrandChandelierUpdate();
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		compound.setString("Light", this.light.getName());
		return compound;
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		this.light = GrandChandelierLight.byName(compound.getString("Light"));
	}

	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
		int oldLight = this.light.getLightLevel();
		this.readFromNBT(pkt.getNbtCompound());
		this.refreshRender();

		if (oldLight != this.light.getLightLevel()) {
			this.refreshLighting();
		}
	}

	private void markForGrandChandelierUpdate() {
		this.markDirty();
		if (this.world == null || this.pos == null) {
			return;
		}

		IBlockState state = this.world.getBlockState(this.pos);
		if (!this.world.isRemote) {
			this.world.notifyBlockUpdate(this.pos, state, state, 3);
		}
		this.refreshLighting();
	}

	private void refreshLighting() {
		if (this.world == null || this.pos == null) {
			return;
		}

		this.world.checkLight(this.pos);

		for (EnumFacing facing : EnumFacing.values()) {
			this.world.checkLight(this.pos.offset(facing));
		}

		if (this.world.isRemote) {
			this.world.markBlockRangeForRenderUpdate(this.pos.add(-15, -15, -15), this.pos.add(15, 15, 15));
		}
	}

	private void refreshRender() {
		if (this.world != null && this.world.isRemote && this.pos != null) {
			this.world.markBlockRangeForRenderUpdate(this.pos, this.pos);
		}
	}
}
