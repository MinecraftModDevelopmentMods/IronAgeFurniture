package com.mcmoddev.ironagefurniture.api.tile;

import com.mcmoddev.ironagefurniture.api.Blocks.GrandChandelierLight;

import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;

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

	private void markForGrandChandelierUpdate() {
		this.markDirty();
		if (this.world == null || this.pos == null) {
			return;
		}

		IBlockState state = this.world.getBlockState(this.pos);
		this.world.notifyBlockUpdate(this.pos, state, state, 3);
		this.world.checkLight(this.pos);
		if (this.world.isRemote) {
			this.world.markBlockRangeForRenderUpdate(this.pos.add(-1, -1, -1), this.pos.add(1, 1, 1));
		}
	}
}
