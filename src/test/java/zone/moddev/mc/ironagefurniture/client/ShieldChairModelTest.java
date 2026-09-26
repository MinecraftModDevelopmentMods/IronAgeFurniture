package zone.moddev.mc.ironagefurniture.client;

import static org.junit.Assert.*;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import org.junit.Test;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/** The in-world shield is rendered from its ItemStack, never from fixed banner geometry. */
public class ShieldChairModelTest {
	private JsonObject resource(String path) throws Exception {
		try (InputStreamReader reader = new InputStreamReader(
				getClass().getResourceAsStream("/assets/ironagefurniture/" + path),
				StandardCharsets.UTF_8)) {
			return new JsonParser().parse(reader).getAsJsonObject();
		}
	}

	@Test public void blockModelContainsOnlySeatAndSupportFrame() throws Exception {
		JsonArray elements = resource("models/block/template_chair_wood_ironage_shield.json")
				.getAsJsonArray("elements");
		assertEquals(12, elements.size());
		for (JsonElement element : elements) {
			JsonObject faces = element.getAsJsonObject().getAsJsonObject("faces");
			for (Map.Entry<String, JsonElement> face : faces.entrySet())
				assertNotEquals("#2", face.getValue().getAsJsonObject().get("texture").getAsString());
		}
	}

	@Test public void filledInventoryRestoresOriginalShieldGeometryForEveryWood() throws Exception {
		JsonArray original = resource("models/block/template_chair_wood_ironage_shield_inventory.json")
				.getAsJsonArray("elements");
		assertEquals(21, original.size());
		boolean shieldFace = false;
		for (JsonElement element : original) {
			JsonObject faces = element.getAsJsonObject().getAsJsonObject("faces");
			for (Map.Entry<String, JsonElement> face : faces.entrySet())
				shieldFace |= "#2".equals(face.getValue().getAsJsonObject().get("texture").getAsString());
		}
		assertTrue(shieldFace);

		JsonObject models = resource("generated/simple_model_resources.json").getAsJsonObject("generatedModels");
		int count = 0;
		for (Map.Entry<String, JsonElement> entry : models.entrySet()) {
			String item = entry.getKey();
			if (!item.startsWith("models/item/chair_wood_ironage_shield_")) continue;
			assertEquals("ironagefurniture:block/" + item.substring("models/item/".length(), item.length() - 5),
					entry.getValue().getAsJsonObject().get("parent").getAsString());
			String filledPath = item.replace("models/item/", "models/item/shield_chair_filled/");
			JsonObject filled = models.getAsJsonObject(filledPath);
			assertNotNull("Missing filled model for " + item, filled);
			assertEquals("ironagefurniture:block/template_chair_wood_ironage_shield_inventory",
					filled.get("parent").getAsString());
			String blockPath = item.replace("models/item/", "models/block/");
			assertEquals(models.getAsJsonObject(blockPath).getAsJsonObject("textures"),
					filled.getAsJsonObject("textures"));
			count++;
		}
		assertEquals(65, count);
	}
}
