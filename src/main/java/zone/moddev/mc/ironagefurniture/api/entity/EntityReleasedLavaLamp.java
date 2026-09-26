package zone.moddev.mc.ironagefurniture.api.entity;

import io.netty.buffer.ByteBuf;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/** A falling lamp whose source cell is already occupied by the empty wall sconce. */
public class EntityReleasedLavaLamp extends EntityFallingMetalBlock {
    private static final float VIAL_WIDTH = 4.0F / 16.0F;
    private static final float VIAL_HEIGHT = 6.0F / 16.0F;
    private static final double HOLDER_BOTTOM_Y = 9.0D / 16.0D;
    private static final ThreadLocal<EntityReleasedLavaLamp> CURRENT_FALL = new ThreadLocal<EntityReleasedLavaLamp>();

    private int sourceY;
    private boolean preserveOnLanding;

    public EntityReleasedLavaLamp(World world) {
        super(world);
        resizeToVial();
        // Vanilla's first tick requires the falling block to still occupy its source cell.
        this.fallTime = 1;
        this.noClip = true;
    }

    public EntityReleasedLavaLamp(World world, double x, double y, double z, IBlockState lampState,
            int sourceY, boolean preserveOnLanding) {
        super(world, x, y, z, lampState);
        resizeToVial();
        this.sourceY = sourceY;
        this.preserveOnLanding = preserveOnLanding;
        // The source cell now contains the empty holder, not the falling lamp.
        this.fallTime = 1;
        this.noClip = true;
    }

    private void resizeToVial() {
        this.setSize(VIAL_WIDTH, VIAL_HEIGHT);
        // setSize keeps the old bounding-box corner; recenter it around the lamp.
        this.setPosition(this.posX, this.posY, this.posZ);
    }

    @Override
    public void onUpdate() {
        // The lamp begins inside the sconce ring and close to its supporting wall.
        // Restore collision before it can reach the block below the single air gap.
        if (this.noClip && hasClearedHolder(this.posY, this.sourceY)) {
            this.noClip = false;
        }

        EntityReleasedLavaLamp previous = CURRENT_FALL.get();
        CURRENT_FALL.set(this);
        try {
            super.onUpdate();
        } finally {
            if (previous == null) {
                CURRENT_FALL.remove();
            } else {
                CURRENT_FALL.set(previous);
            }
        }
    }

    static boolean hasClearedHolder(double lampBottomY, int sourceY) {
        return lampBottomY + VIAL_HEIGHT < sourceY + HOLDER_BOTTOM_Y;
    }

    /** Returns null for ordinary falling lamps, whose existing creative-break policy still applies. */
    public static Boolean preserveCurrentLanding(World world, BlockPos landingPos) {
        EntityReleasedLavaLamp falling = CURRENT_FALL.get();
        return falling != null && falling.world == world && new BlockPos(falling).equals(landingPos)
            ? Boolean.valueOf(falling.preserveOnLanding) : null;
    }

    @Override
    public void writeSpawnData(ByteBuf buffer) {
        super.writeSpawnData(buffer);
        buffer.writeInt(this.sourceY);
        buffer.writeBoolean(this.preserveOnLanding);
    }

    @Override
    public void readSpawnData(ByteBuf buffer) {
        super.readSpawnData(buffer);
        this.sourceY = buffer.readInt();
        this.preserveOnLanding = buffer.readBoolean();
        this.fallTime = 1;
        this.noClip = true;
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setInteger("SconceSourceY", this.sourceY);
        compound.setBoolean("PreserveOnLanding", this.preserveOnLanding);
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        this.sourceY = compound.hasKey("SconceSourceY")
            ? compound.getInteger("SconceSourceY") : (int)Math.floor(this.posY);
        this.preserveOnLanding = compound.getBoolean("PreserveOnLanding");
        this.noClip = true;
    }
}
