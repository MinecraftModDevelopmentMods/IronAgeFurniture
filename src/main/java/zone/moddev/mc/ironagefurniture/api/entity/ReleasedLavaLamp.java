package zone.moddev.mc.ironagefurniture.api.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import zone.moddev.mc.ironagefurniture.init.ModVanillaLights;

/** A released vial falls clear of its holder before colliding with the floor. */
public final class ReleasedLavaLamp extends Entity {
    private int sourceY, age;
    private boolean preserveOnLanding;
    public ReleasedLavaLamp(EntityType<? extends ReleasedLavaLamp> type, Level level) { super(type, level); }
    public ReleasedLavaLamp(Level level, double x, double y, double z, int sourceY, boolean preserve) {
        this(Entities.RELEASED_LAVA_LAMP.get(), level);
        this.sourceY = sourceY;
        preserveOnLanding = preserve;
        setPos(x, y, z);
        xo = x; yo = y; zo = z;
        noPhysics = true;
    }
    @Override protected void defineSynchedData(SynchedEntityData.Builder builder) { }
    @Override public boolean hurtServer(ServerLevel level, net.minecraft.world.damagesource.DamageSource source, float amount) { return false; }
    @Override protected void readAdditionalSaveData(ValueInput input) {
        sourceY = input.getIntOr("SconceSourceY", (int)getY());
        age = input.getIntOr("Age", 0);
        preserveOnLanding = input.getBooleanOr("PreserveOnLanding", false);
        noPhysics = getY() + .375 >= sourceY + .5625;
    }
    @Override protected void addAdditionalSaveData(ValueOutput output) {
        output.putInt("SconceSourceY", sourceY);
        output.putInt("Age", age);
        output.putBoolean("PreserveOnLanding", preserveOnLanding);
    }
    @Override public void tick() {
        super.tick();
        if (noPhysics && getY() + .375 < sourceY + .5625) noPhysics = false;
        setDeltaMovement(getDeltaMovement().add(0, -.04, 0));
        move(MoverType.SELF, getDeltaMovement());
        setDeltaMovement(getDeltaMovement().scale(.98));
        if (level() instanceof ServerLevel server) {
            boolean wet = server.getFluidState(blockPosition()).is(Fluids.WATER);
            if (wet || onGround() || ++age > 600 || getY() < server.getMinY() - 32) {
                BlockPos pos = blockPosition();
                if (preserveOnLanding) spawnAtLocation(server, new ItemStack(ModVanillaLights.light_metal_ironage_block_floor_lava_clear.get()));
                else if (wet) {
                    spawnAtLocation(server, new ItemStack(ModVanillaLights.obsidian_chunk.get()));
                    server.playSound(null, pos, SoundEvents.LAVA_EXTINGUISH, SoundSource.BLOCKS, .5F, 2.6F);
                } else {
                    var fire = BaseFireBlock.getState(server, pos);
                    if (server.getBlockState(pos).canBeReplaced() && fire.canSurvive(server, pos)) server.setBlockAndUpdate(pos, fire);
                }
                if (!preserveOnLanding) server.playSound(null, pos, SoundEvents.GLASS_BREAK, SoundSource.BLOCKS, 1, 1);
                discard();
            }
        }
    }
}
