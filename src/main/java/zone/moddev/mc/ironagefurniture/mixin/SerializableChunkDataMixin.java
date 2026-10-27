package zone.moddev.mc.ironagefurniture.mixin;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.chunk.PalettedContainerFactory;
import net.minecraft.world.level.chunk.storage.SerializableChunkData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import zone.moddev.mc.ironagefurniture.migration.LegacyWorldDataHook;
import zone.moddev.mc.ironagefurniture.migration.NativeLegacyBridge;

/** Finish mod migrations before palettes are decoded, including same-version saves. */
@Mixin(SerializableChunkData.class)
abstract class SerializableChunkDataMixin {
    @Inject(method = "parse", at = @At("HEAD"))
    private static void iaf$beforeDecode(LevelHeightAccessor height, PalettedContainerFactory containers, CompoundTag chunk,
            CallbackInfoReturnable<SerializableChunkData> callback) {
        // Another mod may finish storage upgrading in a cancellable HEAD hook.
        // Its early return skips our storage RETURN hook, but never this decoder.
        if (chunk.getIntOr("DataVersion", 0) > net.minecraft.SharedConstants.getCurrentVersion().dataVersion().version()) return;
        NativeLegacyBridge.finishChunk(LegacyWorldDataHook.finalizeLegacyChunk(chunk));
    }
}
