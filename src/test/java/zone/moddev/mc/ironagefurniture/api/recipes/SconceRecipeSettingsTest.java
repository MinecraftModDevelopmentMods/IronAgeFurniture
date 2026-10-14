package zone.moddev.mc.ironagefurniture.api.recipes;

import static org.junit.Assert.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.io.File;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class SconceRecipeSettingsTest {
    @Rule public TemporaryFolder folder = new TemporaryFolder();
    @Test public void earlyPreviewHonoursTheOptionWithoutRewritingSettings() throws Exception {
        File config = folder.newFile("ironagefurniture-server.toml");
        for (boolean enabled : new boolean[]{false, true}) {
            byte[] bytes = ("# My settings\n[client]\nbaseMetalsIntegration = " + enabled + "\n")
                    .getBytes(StandardCharsets.UTF_8);
            Files.write(config.toPath(), bytes);
            assertEquals(enabled, SconceRecipeSettings.read(config));
            assertArrayEquals(bytes, Files.readAllBytes(config.toPath()));
        }
        assertNull(SconceRecipeSettings.read(new File(folder.getRoot(), "absent.toml")));
    }
}
