package zone.moddev.mc.ironagefurniture.api.recipes;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import java.io.File;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.loading.FMLConfig;
import net.minecraftforge.fml.loading.FMLPaths;
import zone.moddev.mc.ironagefurniture.IronAgeFurnitureConfiguration;
import zone.moddev.mc.ironagefurniture.Ironagefurniture;

/** Recipe loading precedes Forge's server-config loading on this Minecraft version. */
@Mod.EventBusSubscriber(modid = Ironagefurniture.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class SconceRecipeSettings {
    private static volatile Boolean earlyEnabled;
    private SconceRecipeSettings() { }
    public static void prepare(File levelDat) {
        File config = new File(levelDat.getParentFile(), "serverconfig/ironagefurniture-server.toml");
        if (!config.isFile()) config = FMLPaths.GAMEDIR.get().resolve(FMLConfig.defaultConfigPath())
                .resolve("ironagefurniture-server.toml").toFile();
        earlyEnabled = read(config);
    }
    static Boolean read(File config) {
        if (!config.isFile()) return null;
        // This is a read-only preview. Forge still loads and validates the
        // actual config normally; do not save or overwrite a player's settings.
        try (CommentedFileConfig data = CommentedFileConfig.builder(config).sync().build()) {
            data.load();
            Object value = data.getOptional("client.baseMetalsIntegration").orElse(Boolean.TRUE);
            return value instanceof Boolean ? (Boolean)value : Boolean.FALSE;
        }
    }
    public static boolean enabled() {
        Boolean value = earlyEnabled;
        return value == null ? IronAgeFurnitureConfiguration.CLIENT.INTEGRATION_BASEMETALS.get() : value;
    }
    @SubscribeEvent public static void loaded(ModConfig.Loading event) { update(event.getConfig()); }
    @SubscribeEvent public static void reloaded(ModConfig.Reloading event) { update(event.getConfig()); }
    private static void update(ModConfig config) {
        if (config.getType() == ModConfig.Type.SERVER && Ironagefurniture.MODID.equals(config.getModId()))
            earlyEnabled = IronAgeFurnitureConfiguration.CLIENT.INTEGRATION_BASEMETALS.get();
    }
}
