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
    @SubscribeEvent public static void registerItems(RegistryEvent.Register<Item> event) {
        for (Block block : ForgeRegistries.BLOCKS.getValues()) {
            if (!(block instanceof MultiBlockChair) && !(block instanceof FurnitureBed)) continue;
            if (block instanceof FurnitureBed && ((FurnitureBed)block).itemBlock() != block) continue;
            event.getRegistry().register(new UpholsteredBlockItem(block, new Item.Properties().group(Ironagefurniture.IAF_GROUP))
                    .setRegistryName(block.getRegistryName()));
        }
    }
}
