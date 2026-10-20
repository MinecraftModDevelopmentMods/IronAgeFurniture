package zone.moddev.mc.ironagefurniture.mixin;

import com.mojang.datafixers.DataFixer;
import com.mojang.serialization.Dynamic;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.datafix.DataFixTypes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import zone.moddev.mc.ironagefurniture.migration.LegacyWorldDataHook;
import zone.moddev.mc.ironagefurniture.migration.NativeLegacyBridge;

/** Player and separate entity storage need the same pre-decoding item recovery. */
@Mixin(DataFixTypes.class)
abstract class DataFixTypesMixin {
    @Inject(method = "update(Lcom/mojang/datafixers/DataFixer;Lcom/mojang/serialization/Dynamic;II)Lcom/mojang/serialization/Dynamic;", at = @At("HEAD"))
    private void iaf$prepare(DataFixer fixer, Dynamic<?> input, int from, int to, CallbackInfoReturnable<Dynamic<?>> callback) {
        if (!(input.getValue() instanceof CompoundTag tag)) return;
        DataFixTypes type = (DataFixTypes)(Object)this;
        if (type == DataFixTypes.PLAYER || type == DataFixTypes.LEVEL || type == DataFixTypes.ENTITY_CHUNK) {
            LegacyWorldDataHook.preparePlayerData(tag);
            NativeLegacyBridge.prepareItems(tag, from);
        }
    }
    @Inject(method = "update(Lcom/mojang/datafixers/DataFixer;Lcom/mojang/serialization/Dynamic;II)Lcom/mojang/serialization/Dynamic;", at = @At("RETURN"))
    private void iaf$finish(DataFixer fixer, Dynamic<?> input, int from, int to, CallbackInfoReturnable<Dynamic<?>> callback) {
        if (to < net.minecraft.SharedConstants.getCurrentVersion().dataVersion().version()
                || !(callback.getReturnValue().getValue() instanceof CompoundTag result)) return;
        DataFixTypes type = (DataFixTypes)(Object)this;
        if (type == DataFixTypes.PLAYER || type == DataFixTypes.LEVEL || type == DataFixTypes.ENTITY_CHUNK)
            NativeLegacyBridge.finishItems(result);
    }
}
