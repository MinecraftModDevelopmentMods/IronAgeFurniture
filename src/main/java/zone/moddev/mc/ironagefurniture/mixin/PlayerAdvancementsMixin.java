package zone.moddev.mc.ironagefurniture.mixin;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonReader;
import net.minecraft.server.PlayerAdvancements;
import net.minecraft.server.ServerAdvancementManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import zone.moddev.mc.ironagefurniture.migration.LegacyAdvancementProgressMigration;

/** Player advancement storage is JSON, unlike the separate player/item NBT data-fix hooks. */
@Mixin(PlayerAdvancements.class)
abstract class PlayerAdvancementsMixin {
    @Unique private LegacyAdvancementProgressMigration iaf$legacyProgress;

    @Redirect(method = "load", at = @At(value = "INVOKE", target = "Lcom/google/gson/internal/Streams;parse(Lcom/google/gson/stream/JsonReader;)Lcom/google/gson/JsonElement;", remap = false))
    private JsonElement iaf$readProgress(JsonReader reader, ServerAdvancementManager manager) {
        if (iaf$legacyProgress == null) iaf$legacyProgress = new LegacyAdvancementProgressMigration();
        return iaf$legacyProgress.prepare(Streams.parse(reader), manager);
    }

    // 1.19.4 writes through Gson's Appendable overload, before the newer JsonWriter path.
    @Redirect(method = "save", at = @At(value = "INVOKE", target = "Lcom/google/gson/Gson;toJson(Lcom/google/gson/JsonElement;Ljava/lang/Appendable;)V", remap = false))
    private void iaf$saveProgress(Gson gson, JsonElement encoded, Appendable writer) {
        gson.toJson(iaf$legacyProgress == null ? encoded : iaf$legacyProgress.finish(encoded), writer);
    }
}
