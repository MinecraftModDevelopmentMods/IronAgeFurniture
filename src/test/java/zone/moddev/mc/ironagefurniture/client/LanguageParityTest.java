package zone.moddev.mc.ironagefurniture.client;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;

import zone.moddev.mc.ironagefurniture.api.Enumerations.UpholsteryColour;

/** Every supported 1.10 locale must cover the same identifiers and format args. */
public class LanguageParityTest {
    private static final String[] LOCALES = {
            "de_AU", "de_DE", "en_CA", "en_EN", "en_GB", "en_PT", "en_US",
            "es_ES", "es_MX", "fr_CA", "fr_FR", "ja_JP", "ko_KR",
            "pt_BR", "pt_PT", "ru_RU", "zh_CN"
    };
    private static final Pattern FORMAT = Pattern.compile("%(?:[0-9]+\\$)?(?:s|d|f|%)");

    @Test public void allMineralogyLocaleChoicesHaveCompleteSafeTranslations() throws Exception {
        Map<String, String> source = read("en_US");
        assertTrue("English catalogue is unexpectedly small", source.size() > 3000);
        for (String locale : LOCALES) {
            Map<String, String> translated = read(locale);
            assertEquals(locale + " keys", source.keySet(), translated.keySet());
            int changed = 0;
            for (Map.Entry<String, String> entry : source.entrySet()) {
                String value = translated.get(entry.getKey());
                assertFalse(locale + " empty value for " + entry.getKey(), value.trim().isEmpty());
                assertEquals(locale + " format for " + entry.getKey(),
                        formats(entry.getValue()), formats(value));
                if (!entry.getValue().equals(value)) changed++;
            }
            if (!locale.startsWith("en_")) {
                // Newer Phase 3 padded-bench colour names fall back to English where
                // the 1.0 donor has no translation; the other catalogued entries remain translated.
                assertTrue(locale + " should translate furniture and UI, not only copy English",
                        changed * 5 > source.size() * 2);
                assertNotEquals(locale + " must translate the Phase 4 bed", source.get(
                        "tile.ironagefurniture.bed_canopy_foot_lower_oak.name"), translated.get(
                        "tile.ironagefurniture.bed_canopy_foot_lower_oak.name"));
            }
        }
    }

    @Test public void regionalAliasesMatchMineralogyLocalePolicy() throws Exception {
        assertEquals(read("de_DE"), read("de_AU"));
        assertEquals(read("es_ES"), read("es_MX"));
        assertEquals(read("fr_FR"), read("fr_CA"));
        assertEquals(read("pt_BR"), read("pt_PT"));
        assertEquals(read("en_US"), read("en_CA"));
        assertEquals(read("en_US"), read("en_PT"));
    }

    @Test public void everyDisplayedUpholsteryColourHasATranslation() throws Exception {
        for (String locale : LOCALES) {
            Map<String, String> names = read(locale);
            for (UpholsteryColour colour : UpholsteryColour.values()) {
                if (colour == UpholsteryColour.RED) continue; // Legacy red items use their base name.
                String key = "item.ironagefurniture.upholstery." + colour.getSerializedName();
                assertTrue(locale + " missing " + key, names.containsKey(key));
                assertFalse(locale + " empty " + key, names.get(key).trim().isEmpty());
            }
        }
        assertEquals("Purple", read("en_US").get("item.ironagefurniture.upholstery.purple"));
    }

    private Map<String, String> read(String locale) throws Exception {
        String resource = "/assets/ironagefurniture/lang/" + locale + ".lang";
        InputStream input = getClass().getResourceAsStream(resource);
        assertNotNull("Missing locale " + resource, input);
        Map<String, String> values = new LinkedHashMap<String, String>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(input, StandardCharsets.UTF_8))) {
            String line;
            int number = 0;
            while ((line = reader.readLine()) != null) {
                number++;
                if (line.isEmpty() || line.startsWith("#")) continue;
                int separator = line.indexOf('=');
                assertTrue(resource + ":" + number + " has no '='", separator > 0);
                String key = line.substring(0, separator);
                assertFalse(resource + ":" + number + " duplicate " + key, values.containsKey(key));
                values.put(key, line.substring(separator + 1));
            }
        }
        return values;
    }

    private static List<String> formats(String value) {
        List<String> formats = new ArrayList<String>();
        Matcher matcher = FORMAT.matcher(value);
        while (matcher.find()) formats.add(matcher.group());
        return formats;
    }
}
