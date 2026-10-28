package zone.moddev.mc.ironagefurniture.mixin;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.ProtoChunk;
import net.minecraft.world.level.chunk.storage.ChunkSerializer;
import net.minecraft.world.level.chunk.storage.RegionStorageInfo;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import zone.moddev.mc.ironagefurniture.migration.LegacyWorldDataHook;
import zone.moddev.mc.ironagefurniture.migration.NativeLegacyBridge;

/** Same-version and other-mod early returns still finish before native palette decoding. */
@Mixin(ChunkSerializer.class)
abstract class ChunkSerializerMixin {
    @Inject(method = "read", at = @At("HEAD"))
    private static void iaf$beforeDecode(ServerLevel level, PoiManager points, RegionStorageInfo storage,
            ChunkPos pos, CompoundTag chunk, CallbackInfoReturnable<ProtoChunk> callback) {
        if (chunk.getInt("DataVersion") > net.minecraft.SharedConstants.getCurrentVersion().getDataVersion().getVersion()) return;
        NativeLegacyBridge.finishChunk(LegacyWorldDataHook.finalizeLegacyChunk(chunk));
    }
}
