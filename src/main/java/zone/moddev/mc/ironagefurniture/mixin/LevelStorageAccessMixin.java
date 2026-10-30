package zone.moddev.mc.ironagefurniture.mixin;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.storage.LevelStorageSource;
import net.minecraftforge.common.ForgeHooks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import zone.moddev.mc.ironagefurniture.migration.LegacyWorldDataHook;

/** Forge47 passes the original in-memory level snapshot before loading its registries. */
@Mixin(value = ForgeHooks.class, remap = false)
abstract class LevelStorageAccessMixin {
    @Inject(method = "readAdditionalLevelSaveData", at = @At("HEAD"))
    private static void iaf$selectWorld(CompoundTag root, LevelStorageSource.LevelDirectory directory, CallbackInfo callback) {
        LegacyWorldDataHook.sanitizeForgeSnapshotAliases(root);
        LegacyWorldDataHook.prepareSelectedWorld(root, directory);
    }
}
