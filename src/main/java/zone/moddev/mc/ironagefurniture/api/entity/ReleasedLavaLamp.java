package zone.moddev.mc.ironagefurniture.api.entity;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraftforge.fmllegacy.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fmllegacy.network.NetworkHooks;
import zone.moddev.mc.ironagefurniture.registers.entities;

/** A wall-sconce vial falls independently of the empty frame left behind. */
public final class ReleasedLavaLamp extends FallingBlockEntity implements IEntityAdditionalSpawnData {
    private static final double VIAL_HEIGHT = 6.0D / 16.0D;
    private static final double HOLDER_BOTTOM = 9.0D / 16.0D;
    private static final ThreadLocal<ReleasedLavaLamp> CURRENT_FALL = new ThreadLocal<>();
    private int sourceY;
    private boolean preserveOnLanding;

    public ReleasedLavaLamp(EntityType<? extends ReleasedLavaLamp> type, Level world) { super(type, world); }

    public ReleasedLavaLamp(Level world, double x, double y, double z, BlockState state,
            int sourceY, boolean preserveOnLanding) {
        this(entities.RELEASED_LAVA_LAMP.get(), world);
        CompoundTag saved = new CompoundTag();
        saved.put("BlockState", NbtUtils.writeBlockState(state));
        // The source is already an empty sconce. Skip vanilla's source-removal tick.
        saved.putInt("Time", 1);
        saved.putBoolean("DropItem", false);
        saved.putInt("SconceSourceY", sourceY);
        saved.putBoolean("PreserveOnLanding", preserveOnLanding);
        readAdditionalSaveData(saved);
        setPos(x, y, z);
        setStartPos(new BlockPos(x, sourceY, z));
        xo = x;
        yo = y;
        zo = z;
    }

    @Override public void tick() {
        // The vial starts partly inside its holder; collision resumes once clear.
        if (noPhysics && hasClearedHolder(getY(), sourceY)) noPhysics = false;
        ReleasedLavaLamp previous = CURRENT_FALL.get();
        CURRENT_FALL.set(this);
        try { super.tick(); }
        finally {
            if (previous == null) CURRENT_FALL.remove();
            else CURRENT_FALL.set(previous);
        }
    }

    static boolean hasClearedHolder(double bottomY, int sourceY) {
        return bottomY + VIAL_HEIGHT < sourceY + HOLDER_BOTTOM;
    }

    /** Null means an ordinary falling lamp, whose existing policy still applies. */
    public static Boolean preserveCurrentLanding(Level world, BlockPos pos) {
        ReleasedLavaLamp lamp = CURRENT_FALL.get();
        return lamp != null && lamp.level == world && lamp.blockPosition().equals(pos)
                ? Boolean.valueOf(lamp.preserveOnLanding) : null;
    }

    @Override protected void addAdditionalSaveData(CompoundTag saved) {
        super.addAdditionalSaveData(saved);
        saved.putInt("SconceSourceY", sourceY);
        saved.putBoolean("PreserveOnLanding", preserveOnLanding);
    }
    @Override protected void readAdditionalSaveData(CompoundTag saved) {
        super.readAdditionalSaveData(saved);
        sourceY = saved.contains("SconceSourceY") ? saved.getInt("SconceSourceY") : (int)Math.floor(getY());
        preserveOnLanding = saved.getBoolean("PreserveOnLanding");
        noPhysics = true;
    }
    @Override public Packet<?> getAddEntityPacket() { return NetworkHooks.getEntitySpawningPacket(this); }
    @Override public void writeSpawnData(FriendlyByteBuf buffer) {
        CompoundTag saved = new CompoundTag();
        addAdditionalSaveData(saved);
        buffer.writeNbt(saved);
    }
    @Override public void readSpawnData(FriendlyByteBuf buffer) {
        CompoundTag saved = buffer.readNbt();
        if (saved != null) readAdditionalSaveData(saved);
    }
}
