package zone.moddev.mc.ironagefurniture.mixin;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.storage.LevelStorageSource;
import net.minecraftforge.common.ForgeHooks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import zone.moddev.mc.ironagefurniture.migration.LegacyWorldDataHook;

@Mixin(value = ForgeHooks.class, remap = false)
abstract class CommonHooksMixin {
    @Inject(method = "readAdditionalLevelSaveData", at = @At("HEAD")) private static void iaf$capture(
            LevelStorageSource.LevelStorageAccess access, LevelStorageSource.LevelDirectory directory, CallbackInfo callback) {
        try {
            LegacyWorldDataHook.captureLegacyLevelData(access.getDataTagRaw(false), directory);
        } catch (java.io.IOException failure) {
            throw new IllegalStateException("Cannot inspect the original world registry data before upgrading", failure);
        }
    }
}
