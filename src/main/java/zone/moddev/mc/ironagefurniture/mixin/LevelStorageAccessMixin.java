package zone.moddev.mc.ironagefurniture.mixin;

import com.mojang.serialization.Dynamic;
import java.io.IOException;
import net.minecraft.world.level.storage.LevelStorageSource;
import net.minecraft.nbt.CompoundTag;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import zone.moddev.mc.ironagefurniture.migration.LegacyWorldDataHook;

/** Forge invokes its additional-data hook after the native file fixer rewrites level.dat. */
@Mixin(LevelStorageSource.LevelStorageAccess.class)
abstract class LevelStorageAccessMixin {
    @Inject(method = "getDataTagRaw", at = @At("RETURN"))
    private void iaf$keepRegisteredFurniture(boolean fallback, CallbackInfoReturnable<CompoundTag> callback) {
        // Work on the reader's in-memory snapshot, never rewrite a source save.
        LegacyWorldDataHook.sanitizeForgeSnapshotAliases(callback.getReturnValue());
    }
    @Inject(method = "getDataTag(Z)Lcom/mojang/serialization/Dynamic;", at = @At("HEAD"))
    private void iaf$selectWorld(boolean fallback, CallbackInfoReturnable<Dynamic<?>> callback) throws IOException {
        var access = (LevelStorageSource.LevelStorageAccess)(Object)this;
        // Preserve the normal IOException/fallback path instead of masking a
        // damaged primary file with a different exception type.
        LegacyWorldDataHook.prepareSelectedWorld(access.getDataTagRaw(fallback), access.getLevelDirectory());
    }
}
