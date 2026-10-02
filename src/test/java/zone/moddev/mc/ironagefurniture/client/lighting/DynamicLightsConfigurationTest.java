package zone.moddev.mc.ironagefurniture.client.lighting;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.minecraft.block.Block;
import net.minecraft.init.Bootstrap;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import org.junit.BeforeClass;
import org.junit.Test;
import zone.moddev.mc.ironagefurniture.BlockObjectHolder;
import zone.moddev.mc.ironagefurniture.IronAgeFurnitureConfiguration;
import zone.moddev.mc.ironagefurniture.Ironagefurniture;
import zone.moddev.mc.ironagefurniture.api.Blocks.LightSourceRed;
import zone.moddev.mc.ironagefurniture.init.BlockInitialiser;

public class DynamicLightsConfigurationTest {
    @BeforeClass
    public static void bootstrap() {
        Bootstrap.register();
    }

    @Test
    public void documentedSettingsCoverEveryCarriedLightAtItsRealBrightness() throws Exception {
        Map<String, Block> originalBlocks = new HashMap<>(Ironagefurniture.BlockRegistry);
        Map<String, Item> originalItems = new HashMap<>(Ironagefurniture.ItemRegistry);
        Map<Field, Object> originalHolders = new HashMap<>();
        for (Field field : BlockObjectHolder.class.getFields()) {
            if (field.getType() == Block.class) originalHolders.put(field, field.get(null));
        }
        Map<Field, Boolean> originalOptions = new HashMap<>();
        for (String option : new String[] {
                "GENERATE_LIGHTS", "GENERATE_SCONCES", "GENERATE_GLOW_LAMPS",
                "GENERATE_LAVA_LAMPS", "GENERATE_REDSTONE_LAMPS" }) {
            Field field = IronAgeFurnitureConfiguration.class.getField(option);
            originalOptions.put(field, field.getBoolean(null));
        }

        try {
            Ironagefurniture.BlockRegistry.clear();
            Ironagefurniture.ItemRegistry.clear();
            for (Field field : originalOptions.keySet()) field.setBoolean(null, true);
            Method generateLights = BlockInitialiser.class.getDeclaredMethod("generateLights");
            generateLights.setAccessible(true);
            generateLights.invoke(null);

            Map<String, Integer> expected = new TreeMap<>();
            for (Map.Entry<String, Item> entry : Ironagefurniture.ItemRegistry.entrySet()) {
                assertTrue("Lighting registration must remain an ItemBlock: " + entry.getKey(),
                        entry.getValue() instanceof ItemBlock);
                Block block = ((ItemBlock) entry.getValue()).getBlock();
                int level = block.getLightValue(block.getDefaultState());
                if (level > 0 || block instanceof LightSourceRed) {
                    expected.put(Ironagefurniture.MODID + ":" + entry.getKey(), level);
                }
            }

            assertEquals(Integer.valueOf(12), expected.get("ironagefurniture:light_metal_ironage_candle_floor"));
            assertEquals(Integer.valueOf(15), expected.get("ironagefurniture:light_metal_ironage_block_floor_glow_clear"));
            assertEquals(Integer.valueOf(15), expected.get("ironagefurniture:light_metal_ironage_block_floor_lava_clear"));
            assertEquals(Integer.valueOf(0), expected.get("ironagefurniture:light_metal_ironage_block_floor_red_clear"));
            assertEquals(4, expected.size());

            String readme = new String(Files.readAllBytes(Paths.get("README.md")), StandardCharsets.UTF_8);
            Matcher entries = Pattern.compile("(ironagefurniture:[a-z0-9_]+)=(\\d+)").matcher(readme);
            Map<String, Integer> documented = new TreeMap<>();
            while (entries.find()) {
                int level = Integer.parseInt(entries.group(2));
                assertTrue("Invalid brightness for " + entries.group(1), level >= 0 && level <= 15);
                assertNull("Duplicate light entry: " + entries.group(1), documented.put(entries.group(1), level));
            }
            assertEquals("All carried lights must be listed, with no hidden or unlit states", expected, documented);
        } finally {
            Ironagefurniture.BlockRegistry.clear();
            Ironagefurniture.BlockRegistry.putAll(originalBlocks);
            Ironagefurniture.ItemRegistry.clear();
            Ironagefurniture.ItemRegistry.putAll(originalItems);
            for (Map.Entry<Field, Object> entry : originalHolders.entrySet()) {
                entry.getKey().set(null, entry.getValue());
            }
            for (Map.Entry<Field, Boolean> entry : originalOptions.entrySet()) {
                entry.getKey().setBoolean(null, entry.getValue());
            }
        }
    }
}
