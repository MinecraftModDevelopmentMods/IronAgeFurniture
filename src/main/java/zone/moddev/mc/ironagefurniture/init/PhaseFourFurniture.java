package zone.moddev.mc.ironagefurniture.init;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import zone.moddev.mc.ironagefurniture.Ironagefurniture;
import zone.moddev.mc.ironagefurniture.IronAgeFurnitureConfiguration;
import zone.moddev.mc.ironagefurniture.api.blocks.furniture.*;
import zone.moddev.mc.ironagefurniture.api.items.UpholsteredBlockItem;
import zone.moddev.mc.ironagefurniture.api.UpholsteryItemData;
import zone.moddev.mc.ironagefurniture.api.SconceMetalData;
import zone.moddev.mc.ironagefurniture.api.enumerations.UpholsteryColour;
import zone.moddev.mc.ironagefurniture.api.enumerations.SconceMetal;
import zone.moddev.mc.ironagefurniture.api.items.MetalSconceBlockItem;

/** Called from the existing wood catalog, so optional woods use the same gates. */
@Mod.EventBusSubscriber(modid = Ironagefurniture.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class PhaseFourFurniture {
    private PhaseFourFurniture() { }
    public static void registerBlocks(RegistryEvent.Register<Block> event, String wood) {
        IronAgeFurnitureConfiguration.Client config = IronAgeFurnitureConfiguration.CLIENT;
        if (config.GENERATE_CLASSIC_CHAIRS.get() && config.GENERATE_WINGBACK_CHAIRS.get()) {
            event.getRegistry().register(new WingbackChair("chair_wood_ironage_wingback_" + wood));
            if (config.GENERATE_THRONES.get()) event.getRegistry().register(new ThroneChair("chair_wood_ironage_throne_" + wood));
        }
        if (config.GENERATE_WOOD_BEDS.get()) {
            event.getRegistry().register(new WoodenBed("bed_wood_foot_" + wood, false));
            event.getRegistry().register(new WoodenBed("bed_wood_foot_left_" + wood, true));
        }
        if (config.GENERATE_CANOPY_BEDS.get()) {
            CanopyBed single = new CanopyBed("bed_canopy_foot_lower_" + wood, false);
            CanopyBed left = new CanopyBed("bed_canopy_foot_left_lower_" + wood, true);
            CanopyBed right = new CanopyBed("bed_canopy_foot_right_lower_" + wood, true);
            left.setDoubleBlocks(left, right);
            right.setDoubleBlocks(left, right);
            event.getRegistry().registerAll(single, left, right);
        }
    }
    @SubscribeEvent(priority = net.minecraftforge.eventbus.api.EventPriority.LOWEST)
    public static void registerItems(RegistryEvent.Register<Item> event) {
        for (Block block : ForgeRegistries.BLOCKS.getValues()) {
            if (!(block instanceof MultiBlockChair) && !(block instanceof FurnitureBed)) continue;
            if (block instanceof FurnitureBed && ((FurnitureBed)block).itemBlock() != block) continue;
            for (UpholsteryColour colour : UpholsteryColour.values())
                event.getRegistry().register(new UpholsteredBlockItem(block,
                        new Item.Properties().group(block instanceof FurnitureBed
                                ? Ironagefurniture.IAF_BEDS_GROUP : Ironagefurniture.IAF_CHAIRS_GROUP), colour)
                        .setRegistryName(UpholsteryItemData.itemId(block.getRegistryName(), colour)));
        }
        // Register even temporarily unavailable metals so removing Base Metals
        // cannot erase stored sconces. Only their recipes and tab entries hide.
        for (Item item : new java.util.ArrayList<>(event.getRegistry().getValues())) {
            if (!(item instanceof MetalSconceBlockItem)) continue;
            Block block = ((MetalSconceBlockItem)item).getBlock();
            for (SconceMetal metal : SconceMetal.values()) if (metal != SconceMetal.IRON) {
                Item.Properties properties = new Item.Properties();
                if (item.getGroup() != null) properties.group(item.getGroup());
                event.getRegistry().register(new MetalSconceBlockItem(block, properties, metal)
                        .setRegistryName(SconceMetalData.itemId(item.getRegistryName(), metal)));
            }
        }
    }
}
