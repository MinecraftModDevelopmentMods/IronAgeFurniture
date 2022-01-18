package com.mcmoddev.ironagefurniture.api.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.fmllegacy.network.NetworkHooks;

import java.util.List;

public class Seat extends Entity {
	private BlockPos source;

    public Seat(Level world) {
        super(Entities.SEAT.get(), world);
        this.noPhysics = true;
    }

    private Seat(Level world, BlockPos source, double yOffset) {
        this(world);
        
        this.source = source;
        this.setPos(source.getX() + 0.5, source.getY() + yOffset, source.getZ() + 0.5);
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compound) {}

    @Override
    protected boolean canRide(Entity entity) { return true; }
    
    @Override
    public Packet<?> getAddEntityPacket() { return NetworkHooks.getEntitySpawningPacket(this); }
    
    @Override
    protected void defineSynchedData() {}

    @Override
    public double getPassengersRidingOffset() { return 0.0; }
    
    public BlockPos getSource() { return source; }
    
    @Override
    protected void readAdditionalSaveData(CompoundTag compound) {}
    
    @Override
    public void tick() {
    	super.tick();
    	
        if(this.source == null)
            this.source = this.blockPosition();
        
        if(!this.level.isClientSide)
            if(this.getPassengers().isEmpty() || this.level.isEmptyBlock(this.source))
            {
                this.remove(RemovalReason.DISCARDED);
                this.level.updateNeighbourForOutputSignal(blockPosition(), this.level.getBlockState(blockPosition()).getBlock());
            }
    }
    
    public static InteractionResult create(Level level, BlockPos pos, double yOffset, Player player)
    {
        if(!level.isClientSide())
        {
            List<Seat> seatEntities = level.getEntitiesOfClass(Seat.class, new AABB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1.0, pos.getY() + 1.0, pos.getZ() + 1.0));
            
            if(seatEntities.isEmpty())
            {
                Seat seatEntity = new Seat(level, pos, yOffset);
                level.addFreshEntity(seatEntity);
                player.startRiding(seatEntity, false);
            }
        }
        return InteractionResult.SUCCESS;
    }
}