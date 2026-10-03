package zone.moddev.mc.ironagefurniture.api.entity;

import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.FallingBlockEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.IPacket;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.network.NetworkHooks;
import zone.moddev.mc.ironagefurniture.registers.entities;

/** A wall-sconce vial falls independently of the empty frame left behind. */
public final class ReleasedLavaLamp extends FallingBlockEntity implements IEntityAdditionalSpawnData {
    private static final double VIAL_HEIGHT = 6.0D / 16.0D;
    private static final double HOLDER_BOTTOM = 9.0D / 16.0D;
    private static final ThreadLocal<ReleasedLavaLamp> CURRENT_FALL = new ThreadLocal<>();
    private int sourceY;
    private boolean preserveOnLanding;

    public ReleasedLavaLamp(EntityType<? extends ReleasedLavaLamp> type, World world) { super(type, world); }

    public ReleasedLavaLamp(World world, double x, double y, double z, BlockState state,
            int sourceY, boolean preserveOnLanding) {
        this(entities.RELEASED_LAVA_LAMP.get(), world);
        CompoundNBT saved = new CompoundNBT();
        saved.put("BlockState", NBTUtil.writeBlockState(state));
        // The source is already an empty sconce. Skip vanilla's source-removal tick.
        saved.putInt("Time", 1);
        saved.putBoolean("DropItem", false);
        saved.putInt("SconceSourceY", sourceY);
        saved.putBoolean("PreserveOnLanding", preserveOnLanding);
        readAdditional(saved);
        setPosition(x, y, z);
        setOrigin(new BlockPos(x, sourceY, z));
        prevPosX = x;
        prevPosY = y;
        prevPosZ = z;
    }

    @Override public void tick() {
        // The vial starts partly inside its holder; collision resumes once clear.
        if (noClip && hasClearedHolder(posY, sourceY)) noClip = false;
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
    public static Boolean preserveCurrentLanding(World world, BlockPos pos) {
        ReleasedLavaLamp lamp = CURRENT_FALL.get();
        return lamp != null && lamp.world == world && new BlockPos(lamp).equals(pos)
                ? Boolean.valueOf(lamp.preserveOnLanding) : null;
    }

    @Override protected void writeAdditional(CompoundNBT saved) {
        super.writeAdditional(saved);
        saved.putInt("SconceSourceY", sourceY);
        saved.putBoolean("PreserveOnLanding", preserveOnLanding);
    }
    @Override protected void readAdditional(CompoundNBT saved) {
        super.readAdditional(saved);
        sourceY = saved.contains("SconceSourceY") ? saved.getInt("SconceSourceY") : (int)Math.floor(posY);
        preserveOnLanding = saved.getBoolean("PreserveOnLanding");
        noClip = true;
    }
    @Override public IPacket<?> createSpawnPacket() { return NetworkHooks.getEntitySpawningPacket(this); }
    @Override public void writeSpawnData(PacketBuffer buffer) {
        CompoundNBT saved = new CompoundNBT();
        writeAdditional(saved);
        buffer.writeCompoundTag(saved);
    }
    @Override public void readSpawnData(PacketBuffer buffer) {
        CompoundNBT saved = buffer.readCompoundTag();
        if (saved != null) readAdditional(saved);
    }
}
