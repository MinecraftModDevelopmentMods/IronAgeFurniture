package zone.moddev.mc.ironagefurniture.api.entity;

import java.lang.reflect.Field;

import io.netty.buffer.ByteBuf;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

/** Carries a released wall-sconce lamp after the source becomes an empty holder. */
public class EntityReleasedLavaLamp extends EntityFallingBlock implements IEntityAdditionalSpawnData {
    // The test JVM and the packaged game do not always report the same
    // deobfuscation state, so try both actual field names.
    private static final Field FALL_TILE = ReflectionHelper.findField(EntityFallingBlock.class,
            new String[] {"fallTile", "field_175132_d"});
    private static final float VIAL_WIDTH = 4.0F / 16.0F;
    private static final float VIAL_HEIGHT = 6.0F / 16.0F;
    private static final double HOLDER_BOTTOM_Y = 9.0D / 16.0D;
    private static final ThreadLocal<EntityReleasedLavaLamp> CURRENT_FALL =
            new ThreadLocal<EntityReleasedLavaLamp>();

    private int sourceY;
    private boolean preserveOnLanding;

    public EntityReleasedLavaLamp(World world) {
        super(world);
        setFallingState(Blocks.SAND.getDefaultState());
        resizeToVial();
        this.fallTime = 1;
        this.noClip = true;
    }

    public EntityReleasedLavaLamp(World world, double x, double y, double z, IBlockState lampState,
            int sourceY, boolean preserveOnLanding) {
        super(world, x, y, z, lampState);
        resizeToVial();
        this.sourceY = sourceY;
        this.preserveOnLanding = preserveOnLanding;
        // Vanilla's first tick expects the falling block to remain in its old cell.
        this.fallTime = 1;
        this.noClip = true;
    }

    private void resizeToVial() {
        setSize(VIAL_WIDTH, VIAL_HEIGHT);
        setPosition(this.posX, this.posY, this.posZ);
    }

    @Override
    public void onUpdate() {
        if (this.noClip && hasClearedHolder(this.posY, this.sourceY)) {
            this.noClip = false;
        }
        EntityReleasedLavaLamp previous = CURRENT_FALL.get();
        CURRENT_FALL.set(this);
        try {
            super.onUpdate();
        } finally {
            if (previous == null) CURRENT_FALL.remove();
            else CURRENT_FALL.set(previous);
        }
    }

    static boolean hasClearedHolder(double lampBottomY, int sourceY) {
        return lampBottomY + VIAL_HEIGHT < sourceY + HOLDER_BOTTOM_Y;
    }

    /** Null identifies an ordinary falling lamp, which retains its existing break policy. */
    public static Boolean preserveCurrentLanding(World world, BlockPos landingPos) {
        EntityReleasedLavaLamp falling = CURRENT_FALL.get();
        return falling != null && falling.world == world && new BlockPos(falling).equals(landingPos)
                ? Boolean.valueOf(falling.preserveOnLanding) : null;
    }

    @Override
    public void writeSpawnData(ByteBuf buffer) {
        IBlockState state = getBlock();
        buffer.writeInt(Block.getStateId(state == null ? Blocks.SAND.getDefaultState() : state));
        buffer.writeInt(this.sourceY);
        buffer.writeBoolean(this.preserveOnLanding);
    }

    @Override
    public void readSpawnData(ByteBuf buffer) {
        setFallingState(Block.getStateById(buffer.readInt()));
        this.sourceY = buffer.readInt();
        this.preserveOnLanding = buffer.readBoolean();
        this.fallTime = 1;
        this.noClip = true;
    }

    private void setFallingState(IBlockState state) {
        try {
            FALL_TILE.set(this, state);
        } catch (IllegalAccessException error) {
            throw new IllegalStateException("Cannot set the released lamp's falling state", error);
        }
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
