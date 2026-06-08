package com.mcmoddev.ironagefurniture.api.tile;

import com.mcmoddev.ironagefurniture.api.MetalVariantHelper;
import com.mcmoddev.ironagefurniture.api.MetalVariantHelper.MetalVariant;

import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

public class TileEntityMetalVariant extends TileEntity {
	private MetalVariant metal = MetalVariant.IRON;

	public MetalVariant getMetal() {
		MetalVariant storedMetal = this.metal == null || !this.metal.isAvailable() ? MetalVariant.IRON : this.metal;
		if (storedMetal != MetalVariant.IRON) {
			return storedMetal;
		}
		if (this.world == null || !this.world.isRemote) {
			return storedMetal;
		}

		MetalVariant stateMetal = this.getBlockStateMetal();
		return stateMetal == null || !stateMetal.isAvailable() ? storedMetal : stateMetal;
	}

	private MetalVariant getBlockStateMetal() {
		if (this.world == null || this.pos == null) {
			return MetalVariant.IRON;
		}

		IBlockState state = this.world.getBlockState(this.pos);
		if (state != null && state.getProperties().containsKey(MetalVariantHelper.METAL)) {
			return state.getValue(MetalVariantHelper.METAL);
		}

		return MetalVariant.IRON;
	}

	public void setMetal(MetalVariant metal) {
		MetalVariant newMetal = metal == null || !metal.isAvailable() ? MetalVariant.IRON : metal;

		if (this.metal != newMetal) {
			this.metal = newMetal;
			this.markForUpdate();
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		MetalVariant oldMetal = this.getMetal();
		this.metal = compound.hasKey("Metal") ? MetalVariant.byName(compound.getString("Metal")) : MetalVariant.IRON;

		if (this.getMetal() != oldMetal && this.world != null && this.pos != null) {
			this.markForUpdate();
		}
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		compound.setString("Metal", this.getMetal().getName());
		return compound;
	}

	@Override
	public SPacketUpdateTileEntity getUpdatePacket() {
		return new SPacketUpdateTileEntity(this.pos, 0, this.getUpdateTag());
	}

	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
		this.readFromNBT(pkt.getNbtCompound());

		if (this.world != null && this.pos != null && this.world.isRemote) {
			this.world.markBlockRangeForRenderUpdate(this.pos, this.pos);
		}
	}

	@Override
	public NBTTagCompound getUpdateTag() {
		return this.writeToNBT(new NBTTagCompound());
	}

	private void markForUpdate() {
		this.markDirty();

		if (this.world != null && !this.world.isRemote && this.pos != null) {
			this.world.notifyBlockUpdate(this.pos, this.world.getBlockState(this.pos),
				this.world.getBlockState(this.pos), 3);
		}
	}
}
