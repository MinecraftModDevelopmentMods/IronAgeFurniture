package zone.moddev.mc.ironagefurniture.mixin;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.storage.LevelStorageSource;
import net.minecraftforge.common.ForgeHooks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import zone.moddev.mc.ironagefurniture.migration.LegacyWorldDataHook;

/** Forge40 also has a deprecated one-argument overload; select the world-aware hook. */
@Mixin(value = ForgeHooks.class, remap = false)
abstract class LevelStorageAccessMixin {
    @Inject(method = "readAdditionalLevelSaveData(Lnet/minecraft/nbt/CompoundTag;Ljava/nio/file/Path;)V", at = @At("HEAD"))
    private static void iaf$selectWorld(CompoundTag root, java.nio.file.Path directory, CallbackInfo callback) {
        int version = root.getCompound("Data").getInt("DataVersion");
        if (version > net.minecraft.SharedConstants.getCurrentVersion().getWorldVersion()) return;
        LegacyWorldDataHook.sanitizeForgeSnapshotAliases(root);
        LegacyWorldDataHook.prepareSelectedWorld(root, directory);
        // Preserve embedded single-player items before native level-data decoding.
        LegacyWorldDataHook.preparePlayerData(root);
        zone.moddev.mc.ironagefurniture.migration.NativeLegacyBridge.prepareItems(root, version);
    }
}
