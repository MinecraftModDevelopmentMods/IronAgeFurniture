package zone.moddev.mc.ironagefurniture.fixture.client.mixin;

import net.minecraft.client.MouseHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** Keep automated hidden-client probes from taking the desktop mouse. */
@Mixin(MouseHandler.class)
abstract class ProbeMouseCaptureMixin {
    @Inject(method = "grabMouse", at = @At("HEAD"), cancellable = true)
    private void probe$leaveDesktopMouseAlone(CallbackInfo callback) {
        if (Boolean.getBoolean("iaf.probe.clientRendering")) callback.cancel();
    }
}
