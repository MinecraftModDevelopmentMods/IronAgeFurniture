package zone.moddev.mc.ironagefurniture;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import zone.moddev.mc.ironagefurniture.api.UpholsteryItemData;
import zone.moddev.mc.ironagefurniture.api.blocks.furniture.WoodenBed;
import zone.moddev.mc.ironagefurniture.api.enumerations.UpholsteryColour;
import zone.moddev.mc.ironagefurniture.api.items.UpholsteredBlockItem;

/** Migration tests need real destinations before atomic provider-safe rewrites may proceed. */
public final class NativeFurnitureFixtures {
    public static Block OAK_BED;
    private static boolean registered;
    public static synchronized void register() {
        if (registered) return;
        for (String wood : new String[]{"oak", "dark_oak"}) {
            for (String form : new String[]{"bed_wood_foot_", "bed_wood_foot_left_", "bed_canopy_foot_lower_",
                    "bed_canopy_foot_left_lower_", "chair_wood_ironage_wingback_", "chair_wood_ironage_throne_"}) {
                var id = new ResourceLocation("ironagefurniture", form + wood);
                Block block = new WoodenBed(id.getPath(), false).setRegistryName(id);
                net.minecraftforge.registries.ForgeRegistries.BLOCKS.register(block);
                if (wood.equals("oak") && form.equals("bed_wood_foot_")) OAK_BED = block;
                for (UpholsteryColour colour : UpholsteryColour.values())
                    net.minecraftforge.registries.ForgeRegistries.ITEMS.register(
                            new UpholsteredBlockItem(block, new Item.Properties(), colour).setRegistryName(UpholsteryItemData.itemId(id, colour)));
            }
        }
        for (String form : new String[]{"classic", "shield", "stool_short", "stool_tall", "bench_single", "bench_log_single", "bench_back_single"})
            net.minecraftforge.registries.ForgeRegistries.ITEMS.register(new Item(new Item.Properties()).setRegistryName(
                    new ResourceLocation("ironagefurniture", "chair_wood_ironage_" + form + "_dark_oak")));
        registered = true;
    }
    private NativeFurnitureFixtures() { }
}
