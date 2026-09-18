package zone.moddev.mc.ironagefurniture.api.entity;

import net.minecraft.util.math.BlockPos;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.util.ActionResultType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.fml.network.NetworkHooks;
import zone.moddev.mc.ironagefurniture.registers.entities;
import java.util.List;

public class Seat extends Entity {
	private BlockPos source;

    public Seat(World world) {
        super(entities.SEAT.get(), world);
        this.noPhysics = true;
    }

    private Seat(World world, BlockPos source, double yOffset) {
        this(world);

        this.source = source;
        this.setPos(source.getX() + 0.5, source.getY() + yOffset, source.getZ() + 0.5);
    }

    @Override
    protected void addAdditionalSaveData(CompoundNBT compound) {}

    @Override
    protected boolean canRide(Entity entity) { return true; }

    @Override
    public IPacket<?> getAddEntityPacket() { return NetworkHooks.getEntitySpawningPacket(this); }

    @Override
    protected void defineSynchedData() {}

    @Override
    public double getRideHeight() { return 0.0; }

    @Override
    protected void readAdditionalSaveData(CompoundNBT compound) {}

    @Override
    public void tick() {
		super.tick();

        if(this.source == null)
            this.source = this.getCommandSenderBlockPosition();

		if (this.level.isClientSide)
			return;

		if(this.getPassengers().isEmpty() || this.level.isEmptyBlock(this.source))
		{
			this.remove();
			BlockPos currentPosition = this.getCommandSenderBlockPosition();
			this.level.updateNeighbourForOutputSignal(currentPosition, this.level.getBlockState(currentPosition).getBlock());
		}
	}

    public static ActionResultType create(World level, BlockPos pos, double yOffset, PlayerEntity player)
    {
        if(!level.isClientSide())
        {
            List<Seat> seatEntities = level.getEntitiesOfClass(Seat.class, new AxisAlignedBB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1.0, pos.getY() + 1.0, pos.getZ() + 1.0));

            if(seatEntities.isEmpty())
            {
                Seat seatEntity = new Seat(level, pos, yOffset);
                level.addFreshEntity(seatEntity);
                player.startRiding(seatEntity, false);
            }
        }
        return ActionResultType.SUCCESS;
    }
}
