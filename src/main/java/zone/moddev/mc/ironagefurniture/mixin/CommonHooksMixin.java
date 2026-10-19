package zone.moddev.mc.ironagefurniture.mixin;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.storage.LevelStorageSource;
import net.neoforged.neoforge.common.CommonHooks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import zone.moddev.mc.ironagefurniture.migration.LegacyWorldDataHook;

@Mixin(value = CommonHooks.class, remap = false)
abstract class CommonHooksMixin {
    @Inject(method = "readAdditionalLevelSaveData", at = @At("HEAD")) private static void iaf$capture(
            CompoundTag root, LevelStorageSource.LevelDirectory directory, CallbackInfo callback) {
        LegacyWorldDataHook.captureLegacyLevelData(root, directory);
    }
}
