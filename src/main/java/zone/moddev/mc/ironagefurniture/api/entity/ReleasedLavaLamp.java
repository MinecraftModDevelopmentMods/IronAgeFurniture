package zone.moddev.mc.ironagefurniture.api.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.FluidTags;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseFireBlock;
import zone.moddev.mc.ironagefurniture.init.ModVanillaLights;

/** Server-owned native physics; the vial clears its holder before collision. */
public final class ReleasedLavaLamp extends Entity {
    private int sourceY, age;
    private boolean preserveOnLanding;
    private int lerpSteps;
    private double lerpX, lerpY, lerpZ;
    private float lerpYRot, lerpXRot;

    public ReleasedLavaLamp(EntityType<? extends ReleasedLavaLamp> type, Level level) { super(type, level); }
    public ReleasedLavaLamp(Level level, double x, double y, double z, int sourceY, boolean preserve) {
        this(Entities.RELEASED_LAVA_LAMP.get(), level);
        this.sourceY = sourceY;
        preserveOnLanding = preserve;
        setPos(x, y, z);
        xo = x; yo = y; zo = z;
        noPhysics = true;
    }
    @Override protected void defineSynchedData() { }
    @Override public boolean hurt(DamageSource source, float amount) { return false; }
    @Override public void lerpTo(double x, double y, double z, float yaw, float pitch, int steps, boolean teleport) {
        if (!getLevel().isClientSide() || steps <= 0) {
            lerpSteps = 0;
            super.lerpTo(x, y, z, yaw, pitch, steps, teleport);
            return;
        }
        lerpX = x; lerpY = y; lerpZ = z;
        lerpYRot = yaw; lerpXRot = pitch;
        lerpSteps = steps;
    }
    @Override protected void readAdditionalSaveData(CompoundTag input) {
        lerpSteps = 0;
        sourceY = input.contains("SconceSourceY") ? input.getInt("SconceSourceY") : (int)getY();
        age = input.getInt("Age");
        preserveOnLanding = input.getBoolean("PreserveOnLanding");
        noPhysics = getY() + .375 >= sourceY + .5625;
    }
    @Override protected void addAdditionalSaveData(CompoundTag output) {
        output.putInt("SconceSourceY", sourceY);
        output.putInt("Age", age);
        output.putBoolean("PreserveOnLanding", preserveOnLanding);
    }
    @Override public void tick() {
        super.tick();
        if (!(getLevel() instanceof ServerLevel server)) {
            // Only interpolate authoritative packets; never run gravity, collision or impact here.
            if (lerpSteps > 0) {
                double amount = 1.0D / lerpSteps;
                setPos(net.minecraft.util.Mth.lerp(amount, getX(), lerpX),
                        net.minecraft.util.Mth.lerp(amount, getY(), lerpY),
                        net.minecraft.util.Mth.lerp(amount, getZ(), lerpZ));
                setRot(getYRot() + net.minecraft.util.Mth.wrapDegrees(lerpYRot - getYRot()) / lerpSteps,
                        getXRot() + (lerpXRot - getXRot()) / lerpSteps);
                lerpSteps--;
            }
            return;
        }
        if (noPhysics && getY() + .375 < sourceY + .5625) noPhysics = false;
        setDeltaMovement(getDeltaMovement().add(0, -.04, 0));
        move(MoverType.SELF, getDeltaMovement());
        setDeltaMovement(getDeltaMovement().scale(.98));
        boolean wet = server.getFluidState(blockPosition()).is(FluidTags.WATER);
        if (wet || onGround || ++age > 600 || getY() < server.getMinBuildHeight() - 32) {
            BlockPos pos = blockPosition();
            // Cause is captured before leaving the holder and survives save/reload; never consume
            // another falling lamp's nearby break record at an unrelated eventual landing.
            boolean preserve = preserveOnLanding;
            if (preserve) spawnAtLocation(new ItemStack(ModVanillaLights.light_metal_ironage_block_floor_lava_clear.get()));
            else if (wet) {
                spawnAtLocation(new ItemStack(ModVanillaLights.obsidian_chunk.get()));
                server.playSound(null, pos, SoundEvents.LAVA_EXTINGUISH, SoundSource.BLOCKS, .5F, 2.6F);
            } else {
                var fire = BaseFireBlock.getState(server, pos);
                if (server.getBlockState(pos).canBeReplaced() && fire.canSurvive(server, pos)) server.setBlockAndUpdate(pos, fire);
            }
            if (!preserve) server.playSound(null, pos, SoundEvents.GLASS_BREAK, SoundSource.BLOCKS, 1, 1);
            discard();
        }
    }
}
