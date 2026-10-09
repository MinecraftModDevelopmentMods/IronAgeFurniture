package zone.moddev.mc.ironagefurniture.api.recipes;

import static org.junit.Assert.*;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.Test;

public class CreativeTabLocaleTest {
    @Test public void allSupportedLocalesNameTheFourCategoriesAndRetainTheLegacyLabel() throws Exception {
        List<Path> locales;
        try (Stream<Path> paths = Files.list(Paths.get("src/main/resources/assets/ironagefurniture/lang"))) {
            locales = paths.filter(path -> path.toString().endsWith(".json")).collect(Collectors.toList());
        }
        assertEquals(17, locales.size());
        for (Path locale : locales) {
            try (Reader reader = Files.newBufferedReader(locale, StandardCharsets.UTF_8)) {
                JsonObject names = new JsonParser().parse(reader).getAsJsonObject();
                assertTrue(names.has("itemGroup.ironagefurniture"));
                for (String category : new String[] {"chairs", "benches", "beds", "lights"})
                    assertFalse(locale.toString(), names.get("itemGroup.ironagefurniture." + category).getAsString().trim().isEmpty());
            }
        }
    }
}
