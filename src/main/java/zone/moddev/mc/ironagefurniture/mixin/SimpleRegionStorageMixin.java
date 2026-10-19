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
    @Inject(method = "upgradeChunkTag(Lnet/minecraft/nbt/CompoundTag;ILnet/minecraft/nbt/CompoundTag;I)Lnet/minecraft/nbt/CompoundTag;", at = @At("HEAD"))
    private void iaf$prepare(CompoundTag chunk, int fallbackVersion, CompoundTag context, int targetVersion, CallbackInfoReturnable<CompoundTag> callback) {
        if (chunk.contains(IAF_PREPARED) || chunk.getCompoundOrEmpty("Level").contains(IAF_PREPARED)) return;
        int sourceVersion = chunk.getIntOr("DataVersion", fallbackVersion);
        NativeLegacyBridge.prepareChunk(chunk, chunk.getIntOr("DataVersion", fallbackVersion));
        LegacyWorldDataHook.prepareLegacyChunk(chunk);
        LegacyWorldDataHook.prepareVanillaTileEntitySchema(chunk);
        chunk.putInt(IAF_PREPARED, sourceVersion);
        if (chunk.contains("Level")) chunk.getCompoundOrEmpty("Level").putInt(IAF_PREPARED, sourceVersion);
    }
    @Inject(method = "upgradeChunkTag(Lnet/minecraft/nbt/CompoundTag;ILnet/minecraft/nbt/CompoundTag;I)Lnet/minecraft/nbt/CompoundTag;", at = @At("RETURN"), cancellable = true)
    private void iaf$finish(CompoundTag chunk, int fallbackVersion, CompoundTag context, int targetVersion, CallbackInfoReturnable<CompoundTag> callback) {
        // NeoForge first crosses the 1.13 schema, whose palettes still use dense longs.
        // Finish only after the final vanilla pass; early conversion corrupts that layout.
        if (targetVersion < net.minecraft.SharedConstants.getCurrentVersion().dataVersion().version()) return;
        CompoundTag result = callback.getReturnValue();
        result.remove(IAF_PREPARED);
        result.getCompoundOrEmpty("Level").remove(IAF_PREPARED);
        result = LegacyWorldDataHook.finalizeLegacyChunk(result);
        callback.setReturnValue(NativeLegacyBridge.finishChunk(result));
    }
}
