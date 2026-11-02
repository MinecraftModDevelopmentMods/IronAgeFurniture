package zone.moddev.mc.ironagefurniture.mixin;

import com.mojang.datafixers.DataFixer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.util.datafix.DataFixTypes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import zone.moddev.mc.ironagefurniture.migration.LegacyWorldDataHook;
import zone.moddev.mc.ironagefurniture.migration.NativeLegacyBridge;

/** Native player and separate-entity storage call the CompoundTag update bridge. */
@Mixin(NbtUtils.class)
abstract class DataFixTypesMixin {
    @Inject(method = "update(Lcom/mojang/datafixers/DataFixer;Lnet/minecraft/util/datafix/DataFixTypes;Lnet/minecraft/nbt/CompoundTag;II)Lnet/minecraft/nbt/CompoundTag;", at = @At("HEAD"))
    private static void iaf$prepare(DataFixer fixer, DataFixTypes type, CompoundTag input, int from, int to, CallbackInfoReturnable<CompoundTag> callback) {
        if (from > net.minecraft.SharedConstants.getCurrentVersion().getWorldVersion()) return;
        if (type == DataFixTypes.PLAYER || type == DataFixTypes.LEVEL || type == DataFixTypes.ENTITY_CHUNK) {
            LegacyWorldDataHook.preparePlayerData(input);
            NativeLegacyBridge.prepareItems(input, from);
        }
    }
    @Inject(method = "update(Lcom/mojang/datafixers/DataFixer;Lnet/minecraft/util/datafix/DataFixTypes;Lnet/minecraft/nbt/CompoundTag;II)Lnet/minecraft/nbt/CompoundTag;", at = @At("RETURN"))
    private static void iaf$finish(DataFixer fixer, DataFixTypes type, CompoundTag input, int from, int to, CallbackInfoReturnable<CompoundTag> callback) {
        int current = net.minecraft.SharedConstants.getCurrentVersion().getWorldVersion();
        if (from > current || to < current) return;
        CompoundTag result = callback.getReturnValue();
        if (type == DataFixTypes.STATS)
            zone.moddev.mc.ironagefurniture.migration.LegacyStatisticsMigration.finish(result);
        if (type == DataFixTypes.PLAYER || type == DataFixTypes.LEVEL || type == DataFixTypes.ENTITY_CHUNK)
            NativeLegacyBridge.finishItems(result);
    }
}
