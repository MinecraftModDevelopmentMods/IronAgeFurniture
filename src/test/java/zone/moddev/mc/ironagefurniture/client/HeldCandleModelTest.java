package zone.moddev.mc.ironagefurniture.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.nio.charset.StandardCharsets;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.junit.Test;
import zone.moddev.mc.ironagefurniture.client.resources.GeneratedModelResourcePack;

import net.minecraft.util.ResourceLocation;

/** The flame belongs to the carried item, not to unlit candles placed in a world. */
public class HeldCandleModelTest {
    private JsonObject resource(String path) throws Exception {
        InputStream input = getClass().getResourceAsStream("/assets/ironagefurniture/" + path);
        assertNotNull("Missing candle resource: " + path, input);
        try (InputStreamReader reader = new InputStreamReader(input, StandardCharsets.UTF_8)) {
            return new JsonParser().parse(reader).getAsJsonObject();
        }
    }

    @Test
    public void carriedCandleKeepsItsGeometryAndAddsOnlyTwoSmallFlamePlanes() throws Exception {
        JsonObject block = resource("models/block/light_metal_ironage_candle_floor.json");
        JsonObject item = resource("models/item/light_metal_ironage_candle_floor.json");
        JsonArray baseElements = block.getAsJsonArray("elements");
        JsonArray itemElements = item.getAsJsonArray("elements");
        assertEquals(baseElements.size() + 2, itemElements.size());
        for (int index = 0; index < baseElements.size(); index++) {
            assertEquals(baseElements.get(index), itemElements.get(index));
        }
        JsonObject blockDisplay = block.getAsJsonObject("display");
        JsonObject itemDisplay = item.getAsJsonObject("display");
        for (java.util.Map.Entry<String, com.google.gson.JsonElement> transform
                : blockDisplay.entrySet()) {
            if (!"gui".equals(transform.getKey())) {
                assertEquals(transform.getValue(), itemDisplay.get(transform.getKey()));
            }
        }
        JsonObject blockGui = blockDisplay.getAsJsonObject("gui");
        JsonObject itemGui = itemDisplay.getAsJsonObject("gui");
        assertEquals(blockGui.get("rotation"), itemGui.get("rotation"));
        assertEquals(blockGui.get("translation"), itemGui.get("translation"));
        JsonArray blockScale = blockGui.getAsJsonArray("scale");
        JsonArray itemScale = itemGui.getAsJsonArray("scale");
        for (int axis = 0; axis < 3; axis++) {
            assertEquals(blockScale.get(axis).getAsDouble() * 0.8D,
                    itemScale.get(axis).getAsDouble(), 0.0001D);
        }
        assertEquals("minecraft:blocks/fire_layer_0",
                item.getAsJsonObject("textures").get("flame").getAsString());
        assertFalse(block.getAsJsonObject("textures").has("flame"));
        for (int index = baseElements.size(); index < itemElements.size(); index++) {
            JsonObject flame = itemElements.get(index).getAsJsonObject();
            assertFalse(flame.get("shade").getAsBoolean());
            assertEquals(2, flame.getAsJsonObject("faces").entrySet().size());
        }
    }

    @Test
    public void unlitPlacedCandleStillUsesTheUnmodifiedBlockModel() throws Exception {
        JsonObject state = resource("blockstates/light_metal_ironage_candle_floor_unlit.json");
        for (java.util.Map.Entry<String, com.google.gson.JsonElement> variant
                : state.getAsJsonObject("variants").entrySet()) {
            assertEquals("ironagefurniture:light_metal_ironage_candle_floor",
                    variant.getValue().getAsJsonObject().get("model").getAsString());
        }
    }

    @Test
    public void generatedPackLetsThePhysicalHeldItemModelTakePrecedence() throws Exception {
        Constructor<GeneratedModelResourcePack> constructor =
                GeneratedModelResourcePack.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        GeneratedModelResourcePack pack = constructor.newInstance();
        assertFalse(pack.resourceExists(new ResourceLocation("ironagefurniture",
                "models/item/light_metal_ironage_candle_floor.json")));
        assertTrue(pack.resourceExists(new ResourceLocation("ironagefurniture",
                "models/item/tallow.json")));
    }
}
