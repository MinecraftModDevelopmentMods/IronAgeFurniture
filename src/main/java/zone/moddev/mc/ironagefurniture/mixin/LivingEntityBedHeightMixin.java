package zone.moddev.mc.ironagefurniture.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import zone.moddev.mc.ironagefurniture.api.blocks.furniture.FurnitureBed;

/** Apply the furniture mattress height without changing vanilla or other mods' beds. */
@Mixin(LivingEntity.class)
abstract class LivingEntityBedHeightMixin {
    @Inject(method = "setPosToBed", at = @At("HEAD"), cancellable = true)
    private void iaf$mattressHeight(BlockPos pos, CallbackInfo callback) {
        LivingEntity entity = (LivingEntity)(Object)this;
        var state = entity.getLevel().getBlockState(pos);
        if (!(state.getBlock() instanceof FurnitureBed bed)) return;
        var height = bed.getSleepHeight(state, entity.getLevel(), pos);
        if (height.isEmpty()) return;
        entity.setPos(pos.getX() + .5, pos.getY() + height.getAsDouble(), pos.getZ() + .5);
        callback.cancel();
    }
}
