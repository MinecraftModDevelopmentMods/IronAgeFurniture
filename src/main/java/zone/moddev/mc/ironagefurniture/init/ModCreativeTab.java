package zone.moddev.mc.ironagefurniture.init;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import zone.moddev.mc.ironagefurniture.Ironagefurniture;
import zone.moddev.mc.ironagefurniture.ModItems;
import zone.moddev.mc.ironagefurniture.IronAgeFurnitureConfiguration;
import zone.moddev.mc.ironagefurniture.api.SconceMetalData;
import zone.moddev.mc.ironagefurniture.api.items.MetalSconceBlockItem;

/** Shared category and visibility rules for the built-in furniture families. */
public final class ModCreativeTab {
    /** Forge45 registers tabs through its mod-bus event, before tabs became a registry. */
    public static final class Tab implements java.util.function.Supplier<CreativeModeTab> {
        private final ResourceLocation id;
        private final String category, icon;
        private final Item fallback;
        private CreativeModeTab value;
        private Tab(String id, String category, String icon, Item fallback) {
            this.id = new ResourceLocation(Ironagefurniture.MODID, id);
            this.category = category; this.icon = icon; this.fallback = fallback;
        }
        @Override public CreativeModeTab get() {
            if (value == null) throw new IllegalStateException("Creative tab is not registered: " + id);
            return value;
        }
        public ResourceLocation getId() { return id; }
        private void register(net.minecraftforge.event.CreativeModeTabEvent.Register event) {
            value = event.registerCreativeModeTab(id, builder -> builder
                    .title(Component.translatable("itemGroup.ironagefurniture." + category))
                    .icon(() -> {
                        Item item = BuiltInRegistries.ITEM.get(new ResourceLocation(Ironagefurniture.MODID, icon));
                        return new ItemStack(item != null && item != Items.AIR && visible(item) ? item : fallback);
                    })
                    .displayItems((parameters, output) -> ModItems.REGISTER.getEntries().forEach(holder -> {
                        Item item = holder.get();
                        if (visible(item) && category.equals(category(item))) output.accept(item);
                    })));
        }
    }
    public static final Tab CHAIRS = new Tab("creative_tab", "chairs", "chair_wood_ironage_classic_oak", Items.OAK_STAIRS);
    public static final Tab BENCHES = new Tab("benches", "benches", "chair_wood_ironage_bench_single_oak", Items.OAK_SLAB);
    public static final Tab BEDS = new Tab("beds", "beds", "bed_canopy_foot_lower_oak", Items.RED_BED);
    public static final Tab LIGHTS = new Tab("lights", "lights", "light_metal_ironage_sconce_floor_empty_iron", Items.TORCH);
    /** @deprecated Use the named category. */
    @Deprecated public static final Tab FURNITURE = CHAIRS;
    /** @deprecated Use CHAIRS, BENCHES, BEDS or LIGHTS. */
    @Deprecated public static final Tab IAF_GROUP = CHAIRS;
    public static void register(net.minecraftforge.eventbus.api.IEventBus bus) {
        bus.addListener((net.minecraftforge.event.CreativeModeTabEvent.Register event) -> {
            CHAIRS.register(event); BENCHES.register(event); BEDS.register(event); LIGHTS.register(event);
        });
    }
    public static String category(Item item) {
        String path = BuiltInRegistries.ITEM.getKey(item).getPath();
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
        String path = BuiltInRegistries.ITEM.getKey(item).getPath();
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
