package zone.moddev.mc.ironagefurniture.mixin;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.chunk.storage.SimpleRegionStorage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import zone.moddev.mc.ironagefurniture.migration.LegacyWorldDataHook;
import zone.moddev.mc.ironagefurniture.migration.NativeLegacyBridge;

@Mixin(SimpleRegionStorage.class)
abstract class SimpleRegionStorageMixin {
    @Unique private static final String IAF_PREPARED = "IronAgeFurniturePreparedDataVersion";
    @Inject(method = "upgradeChunkTag(Lnet/minecraft/nbt/CompoundTag;ILnet/minecraft/nbt/CompoundTag;)Lnet/minecraft/nbt/CompoundTag;", at = @At("HEAD"), cancellable = true)
    private void iaf$prepare(CompoundTag chunk, int fallbackVersion, CompoundTag context, CallbackInfoReturnable<CompoundTag> callback) {
        int sourceVersion = chunk.getIntOr("DataVersion", fallbackVersion);
        if (sourceVersion > net.minecraft.SharedConstants.getCurrentVersion().dataVersion().version()) {
            // The native wrapper stamps the current version after fixing. Do not
            // let it stamp an unsupported future chunk as an older save.
            callback.setReturnValue(chunk);
            return;
        }
        if (chunk.contains(IAF_PREPARED) || chunk.getCompoundOrEmpty("Level").contains(IAF_PREPARED)) return;
        NativeLegacyBridge.prepareChunk(chunk, chunk.getIntOr("DataVersion", fallbackVersion));
        LegacyWorldDataHook.prepareLegacyChunk(chunk);
        LegacyWorldDataHook.prepareVanillaTileEntitySchema(chunk);
        chunk.putInt(IAF_PREPARED, sourceVersion);
        if (chunk.contains("Level")) chunk.getCompoundOrEmpty("Level").putInt(IAF_PREPARED, sourceVersion);
    }
    @Inject(method = "upgradeChunkTag(Lnet/minecraft/nbt/CompoundTag;ILnet/minecraft/nbt/CompoundTag;)Lnet/minecraft/nbt/CompoundTag;", at = @At("RETURN"), cancellable = true)
    private void iaf$finish(CompoundTag chunk, int fallbackVersion, CompoundTag context, CallbackInfoReturnable<CompoundTag> callback) {
        // In 1.21.11 this overload always completes the vanilla upgrade to the
        // current data version before returning. Never finish an intermediate schema.
        CompoundTag result = callback.getReturnValue();
        if (result.getIntOr("DataVersion", 0) > net.minecraft.SharedConstants.getCurrentVersion().dataVersion().version()) return;
        result.remove(IAF_PREPARED);
        result.getCompoundOrEmpty("Level").remove(IAF_PREPARED);
        result = LegacyWorldDataHook.finalizeLegacyChunk(result);
        callback.setReturnValue(NativeLegacyBridge.finishChunk(result));
    }
}
