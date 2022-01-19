package com.mcmoddev.ironagefurniture.api.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import java.util.List;

public class Seat extends Entity {
	private BlockPos source;

    public Seat(World world) {
        super(Entities.SEAT, world);
        this.noPhysics = true;
    }

    private Seat(World world, BlockPos source, double yOffset) {
        this(world);
        this.source = source;
        this.setPos(source.getX() + 0.5, source.getY() + yOffset, source.getZ() + 0.5);
    }

    @Override
    protected void defineSynchedData() {
    }

    @Override
    public void tick() {
        super.tick();
        if (source == null) {
            source = this.getOnPos();
        }
        if (!this.level.isClientSide) {
            if (this.getPassengers().isEmpty() || this.level.isEmptyBlock(source)) {
                this.remove();
                level.updateNeighbourForOutputSignal(getOnPos(), level.getBlockState(getOnPos()).getBlock());
            }
        }
    }

    @Override
    protected void readAdditionalSaveData(CompoundNBT compound) {

    }

    @Override
    protected void addAdditionalSaveData(CompoundNBT compound) {

    }

    @Override
    public double getPassengersRidingOffset() {
        return 0.0;
    }

    public BlockPos getSource() {
        return source;
    }

    @Override
    protected boolean canRide(Entity entity) {
        return true;
    }

    @Override
    public IPacket<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    public static ActionResultType create(World world, BlockPos pos, double yOffset, PlayerEntity player) {
        if (!world.isClientSide) {
            List<Seat> seats = world.getEntitiesOfClass(Seat.class, new AxisAlignedBB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1.0, pos.getY() + 1.0, pos.getZ() + 1.0));
            if (seats.isEmpty()) {
                Seat seat = new Seat(world, pos, yOffset);
                world.addFreshEntity(seat);
                player.startRiding(seat, false);
            }
        }
        return ActionResultType.SUCCESS;
    }
}