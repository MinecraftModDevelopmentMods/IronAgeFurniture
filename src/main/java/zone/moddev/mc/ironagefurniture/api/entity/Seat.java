package zone.moddev.mc.ironagefurniture.api.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import java.util.List;

public class Seat extends Entity {
	private BlockPos source;

    public Seat(Level world) {
        super(zone.moddev.mc.ironagefurniture.api.entity.Entities.SEAT.get(), world);
        this.noPhysics = true;
    }

    private Seat(Level world, BlockPos source, double yOffset) {
        this(world);

        this.source = source;
        this.setPos(source.getX() + 0.5, source.getY() + yOffset, source.getZ() + 0.5);
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {}

    @Override
    protected boolean canRide(Entity entity) { return true; }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {}

    @Override
    public boolean hurtServer(ServerLevel level, DamageSource source, float amount) { return false; }

    @Override
    public Vec3 getPassengerRidingPosition(Entity passenger) { return position(); }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {}

    @Override
    public void tick() {
    	super.tick();

        if(this.source == null)
            this.source = this.blockPosition();

		if (this.level().isClientSide())
			return;

		if(this.getPassengers().isEmpty() || this.level().isEmptyBlock(this.source))
		{
			this.remove(RemovalReason.DISCARDED);
			this.level().updateNeighbourForOutputSignal(blockPosition(), this.level().getBlockState(blockPosition()).getBlock());
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
                player.startRiding(seatEntity, false, true);
            }
        }
        return InteractionResult.SUCCESS;
    }
}
