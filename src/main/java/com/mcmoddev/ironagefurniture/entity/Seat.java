package com.mcmoddev.ironagefurniture.entity;

import com.mcmoddev.ironagefurniture.util.Coordinates;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class Seat extends Entity
{
	public Coordinates SeatCoordinates = new Coordinates();
	
	public Seat(World worldIn) {
		super(worldIn);
		
		this.height = 0.01F;
		this.width = 0.01F;
		
		this.noClip = true;
	}

	public Seat(World world, double x, double y, double z, double y0ffset)
	{
		this(world);
		
		SeatCoordinates = new Coordinates(x, y, z);		
		setPosition(x + 0.5D, y + y0ffset, z + 0.5D);
	}

	@Override
	public double getMountedYOffset()
	{
		return this.height * 0.0D;
	}

	@Override
	protected boolean shouldSetPosAfterLoading()
	{
		return false;
	}

	@Override
	public void onEntityUpdate()
	{
		if (!this.world.isRemote)
		{
			if (!this.isBeingRidden() || this.world.isAirBlock(new BlockPos(SeatCoordinates.X, SeatCoordinates.Y, SeatCoordinates.Z)))
			{
				this.setDead();
				world.updateComparatorOutputLevel(getPosition(), world.getBlockState(getPosition()).getBlock());
			}
		}
	}

	@Override
	protected void entityInit()
	{
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound nbttagcompound)
	{
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound nbttagcompound)
	{
	}
}