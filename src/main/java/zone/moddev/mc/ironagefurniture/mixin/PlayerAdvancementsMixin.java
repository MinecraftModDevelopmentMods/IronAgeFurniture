package zone.moddev.mc.ironagefurniture.mixin;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
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

    @Redirect(method = "load", at = @At(value = "INVOKE", target = "Lcom/google/gson/internal/Streams;parse(Lcom/google/gson/stream/JsonReader;)Lcom/google/gson/JsonElement;"))
    private JsonElement iaf$readProgress(JsonReader reader, ServerAdvancementManager manager) {
        if (iaf$legacyProgress == null) iaf$legacyProgress = new LegacyAdvancementProgressMigration();
        return iaf$legacyProgress.prepare(Streams.parse(reader), manager);
    }

    @Redirect(method = "save", at = @At(value = "INVOKE", target = "Lcom/google/gson/Gson;toJson(Lcom/google/gson/JsonElement;Lcom/google/gson/stream/JsonWriter;)V"))
    private void iaf$saveProgress(Gson gson, JsonElement encoded, JsonWriter writer) {
        gson.toJson(iaf$legacyProgress == null ? encoded : iaf$legacyProgress.finish(encoded), writer);
    }
}
