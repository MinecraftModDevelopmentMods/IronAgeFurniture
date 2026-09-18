package zone.moddev.mc.ironagefurniture.api.entity;

import net.minecraft.util.math.BlockPos;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
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
        this.noClip = true;
    }

    private Seat(World world, BlockPos source, double yOffset) {
        this(world);

        this.source = source;
        this.setPosition(source.getX() + 0.5, source.getY() + yOffset, source.getZ() + 0.5);
    }

    @Override
    protected void writeAdditional(CompoundNBT compound) {}

    @Override
    protected boolean canBeRidden(Entity entity) { return true; }

    @Override
    public IPacket<?> createSpawnPacket() { return NetworkHooks.getEntitySpawningPacket(this); }

    @Override
    protected void registerData() {}

    @Override
    public double getMountedYOffset() { return 0.0; }

    @Override
    protected void readAdditional(CompoundNBT compound) {}

    @Override
    public void tick() {
		super.tick();

        if(this.source == null)
            this.source = this.getPosition();

		if (this.world.isRemote)
			return;

		if(this.getPassengers().isEmpty() || this.world.isAirBlock(this.source))
		{
			this.remove();
			BlockPos currentPosition = this.getPosition();
			this.world.updateComparatorOutputLevel(currentPosition, this.world.getBlockState(currentPosition).getBlock());
		}
	}

    public static boolean create(World level, BlockPos pos, double yOffset, PlayerEntity player)
    {
        if(!level.isRemote)
        {
            List<Seat> seatEntities = level.getEntitiesWithinAABB(Seat.class, new AxisAlignedBB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1.0, pos.getY() + 1.0, pos.getZ() + 1.0));

            if(seatEntities.isEmpty())
            {
                Seat seatEntity = new Seat(level, pos, yOffset);
                level.addEntity(seatEntity);
                player.startRiding(seatEntity, false);
            }
        }
        return true;
    }
}
