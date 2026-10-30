package zone.moddev.mc.ironagefurniture.mixin;

import java.util.Optional;
import java.util.function.Supplier;
import com.mojang.serialization.MapCodec;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.chunk.storage.ChunkStorage;
import net.minecraft.world.level.storage.DimensionDataStorage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import zone.moddev.mc.ironagefurniture.migration.LegacyWorldDataHook;
import zone.moddev.mc.ironagefurniture.migration.NativeLegacyBridge;

/** Target-native terrain upgrade; SimpleRegionStorage is the separate entity/POI path. */
@Mixin(ChunkStorage.class)
abstract class ChunkStorageMixin {
    @Inject(method = "upgradeChunkTag", at = @At("HEAD"))
    private void iaf$prepare(ResourceKey<Level> dimension, Supplier<DimensionDataStorage> savedData,
            CompoundTag chunk, Optional<ResourceKey<com.mojang.serialization.Codec<? extends ChunkGenerator>>> generator,
            CallbackInfoReturnable<CompoundTag> callback) {
        int version = ChunkStorage.getVersion(chunk);
        if (version > net.minecraft.SharedConstants.getCurrentVersion().getDataVersion().getVersion()
                || chunk.contains("IronAgeFurniturePreparedDataVersion")) return;
        NativeLegacyBridge.prepareChunk(chunk, version);
        LegacyWorldDataHook.prepareLegacyChunk(chunk);
        LegacyWorldDataHook.prepareVanillaTileEntitySchema(chunk);
        chunk.putInt("IronAgeFurniturePreparedDataVersion", version);
        if (chunk.contains("Level")) chunk.getCompound("Level").putInt("IronAgeFurniturePreparedDataVersion", version);
    }

    @Inject(method = "upgradeChunkTag", at = @At("RETURN"), cancellable = true)
    private void iaf$finish(ResourceKey<Level> dimension, Supplier<DimensionDataStorage> savedData,
            CompoundTag chunk, Optional<ResourceKey<com.mojang.serialization.Codec<? extends ChunkGenerator>>> generator,
            CallbackInfoReturnable<CompoundTag> callback) {
        CompoundTag result = callback.getReturnValue();
        if (ChunkStorage.getVersion(result) > net.minecraft.SharedConstants.getCurrentVersion().getDataVersion().getVersion()) return;
        callback.setReturnValue(NativeLegacyBridge.finishChunk(LegacyWorldDataHook.finalizeLegacyChunk(result)));
    }
}
