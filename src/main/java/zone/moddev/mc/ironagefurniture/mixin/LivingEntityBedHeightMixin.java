package zone.moddev.mc.ironagefurniture.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import zone.moddev.mc.ironagefurniture.api.blocks.furniture.FurnitureBed;

/** Keep the furniture mattress height without changing vanilla or other beds. */
@Mixin(LivingEntity.class)
abstract class LivingEntityBedHeightMixin {
    @Inject(method = "setPosToBed", at = @At("HEAD"), cancellable = true)
    private void iaf$mattressHeight(BlockPos pos, CallbackInfo callback) {
        LivingEntity sleeper = (LivingEntity)(Object)this;
        var state = sleeper.level().getBlockState(pos);
        if (!(state.getBlock() instanceof FurnitureBed bed)) return;
        var height = bed.getSleepHeight(state, sleeper.level(), pos);
        if (height.isEmpty()) return;
        sleeper.setPos(pos.getX() + .5, pos.getY() + height.getAsDouble(), pos.getZ() + .5);
        callback.cancel();
    }
}
