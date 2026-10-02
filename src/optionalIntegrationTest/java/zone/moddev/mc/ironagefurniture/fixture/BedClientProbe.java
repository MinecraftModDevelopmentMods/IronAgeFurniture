package zone.moddev.mc.ironagefurniture.fixture;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import org.apache.logging.log4j.LogManager;

/** Optional, self-closing development-client check for crafting and inventory models. */
@Mod.EventBusSubscriber(modid = OptionalIntegrationRecipeProbe.MOD_ID, value = Side.CLIENT)
public final class BedClientProbe {
    private static boolean finished;
    private BedClientProbe() { }

    @SubscribeEvent public static void tick(TickEvent.ClientTickEvent event) throws java.io.IOException {
        if (finished || !Boolean.getBoolean("iaf.probe.clientBeds") || event.phase != TickEvent.Phase.END) return;
        Minecraft client = Minecraft.getMinecraft();
        if (!(client.currentScreen instanceof GuiMainMenu)) return;
        finished = true;
        BedCraftingProbe.verifyRecipes(null);
        int previews = 0;
        for (IRecipe recipe : CraftingManager.REGISTRY) {
            ItemStack output = recipe.getRecipeOutput();
            if (output.isEmpty() || !output.getItem().getRegistryName().toString().startsWith("ironagefurniture:bed_")) continue;
            IBakedModel model = client.getRenderItem().getItemModelWithOverrides(output, null, null);
            if (model == null || model == client.getRenderItem().getItemModelMesher().getModelManager().getMissingModel())
                throw new IllegalStateException("Missing bed inventory model: " + recipe.getRegistryName());
            ++previews;
        }
        Files.write(Paths.get("bed-client-pass.properties"), ("status=PASS\npreviews=" + previews + "\n").getBytes(StandardCharsets.UTF_8));
        LogManager.getLogger().info("IRON AGE FURNITURE BED CLIENT PROBE PASSED: {} recipe previews", previews);
        client.shutdown();
    }
}
