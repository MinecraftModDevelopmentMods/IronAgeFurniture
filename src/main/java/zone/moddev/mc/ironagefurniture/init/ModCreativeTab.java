package zone.moddev.mc.ironagefurniture.init;

import net.minecraft.core.Registry;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;
import zone.moddev.mc.ironagefurniture.Ironagefurniture;
import zone.moddev.mc.ironagefurniture.ModItems;
import zone.moddev.mc.ironagefurniture.IronAgeFurnitureConfiguration;
import zone.moddev.mc.ironagefurniture.api.SconceMetalData;
import zone.moddev.mc.ironagefurniture.api.items.MetalSconceBlockItem;

/** Native constructor-registered tabs share the furniture visibility rules. */
public final class ModCreativeTab {
    public static final class Tab implements java.util.function.Supplier<CreativeModeTab> {
        private final ResourceLocation id;
        private final CreativeModeTab value;
        private Tab(String id, String category, String icon, Item fallback) {
            this.id = new ResourceLocation(Ironagefurniture.MODID, id);
            this.value = new CreativeModeTab("ironagefurniture." + category) {
                @Override public ItemStack makeIcon() {
                    Item item = Registry.ITEM.get(new ResourceLocation(Ironagefurniture.MODID, icon));
                    return new ItemStack(item != null && item != Items.AIR && visible(item) ? item : fallback);
                }
                @Override public void fillItemList(NonNullList<ItemStack> output) {
                    ModItems.REGISTER.getEntries().forEach(holder -> {
                        Item item = holder.get();
                        if (visible(item) && category.equals(category(item))) output.add(item.getDefaultInstance());
                    });
                }
            };
        }
        @Override public CreativeModeTab get() { return value; }
        public ResourceLocation getId() { return id; }
    }
    public static final Tab CHAIRS = new Tab("creative_tab", "chairs", "chair_wood_ironage_classic_oak", Items.OAK_STAIRS);
    public static final Tab BENCHES = new Tab("benches", "benches", "chair_wood_ironage_bench_single_oak", Items.OAK_SLAB);
    public static final Tab BEDS = new Tab("beds", "beds", "bed_canopy_foot_lower_oak", Items.RED_BED);
    public static final Tab LIGHTS = new Tab("lights", "lights", "light_metal_ironage_sconce_floor_empty_iron", Items.TORCH);
    @Deprecated public static final Tab FURNITURE = CHAIRS;
    @Deprecated public static final Tab IAF_GROUP = CHAIRS;
    public static void register(net.minecraftforge.eventbus.api.IEventBus bus) { }
    public static CreativeModeTab forBlock(Block block) {
        String path = Registry.BLOCK.getKey(block).getPath();
        if (path.startsWith("bed_")) return BEDS.get();
        if (path.contains("_bench_")) return BENCHES.get();
        if (path.startsWith("chair_")) return CHAIRS.get();
        return LIGHTS.get();
    }
    public static String category(Item item) {
        String path = Registry.ITEM.getKey(item).getPath();
        if (path.startsWith("bed_")) return "beds";
        if (path.contains("_bench_")) return "benches";
        if (path.startsWith("chair_")) return "chairs";
        return "lights";
    }
    public static ResourceLocation keyFor(Item item) {
        return switch (category(item)) {
            case "chairs" -> CHAIRS.getId();
            case "benches" -> BENCHES.getId();
            case "beds" -> BEDS.getId();
            default -> LIGHTS.getId();
        };
    }
    public static boolean visible(Item item) {
        String path = Registry.ITEM.getKey(item).getPath();
        var setting = path.contains("_wingback_") ? IronAgeFurnitureConfiguration.WINGBACKS
                : path.contains("_throne_") ? IronAgeFurnitureConfiguration.THRONES
                : path.startsWith("bed_canopy_") ? IronAgeFurnitureConfiguration.CANOPY_BEDS
                : path.startsWith("bed_wood_") ? IronAgeFurnitureConfiguration.WOOD_BEDS
                : path.contains("_bench_") ? IronAgeFurnitureConfiguration.BENCHES
                : path.contains("_shield_") ? IronAgeFurnitureConfiguration.SHIELD_CHAIRS
                : path.contains("_stool_") ? IronAgeFurnitureConfiguration.STOOLS
                : path.startsWith("chair_") ? IronAgeFurnitureConfiguration.CLASSIC_CHAIRS
                : IronAgeFurnitureConfiguration.LIGHTS;
        return IronAgeFurnitureConfiguration.enabled(setting)
                && (!(item instanceof MetalSconceBlockItem frame) || SconceMetalData.available(frame.getMetal()));
    }
    private ModCreativeTab() { }
}
