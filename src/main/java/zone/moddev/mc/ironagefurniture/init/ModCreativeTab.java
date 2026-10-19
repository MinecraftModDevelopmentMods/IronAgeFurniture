package zone.moddev.mc.ironagefurniture.init;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import zone.moddev.mc.ironagefurniture.IronAgeFurnitureConfiguration;
import zone.moddev.mc.ironagefurniture.Ironagefurniture;
import zone.moddev.mc.ironagefurniture.ModItems;
import zone.moddev.mc.ironagefurniture.api.SconceMetalData;
import zone.moddev.mc.ironagefurniture.api.items.MetalSconceBlockItem;

/** The same category and visibility rules are available to separate wood add-ons. */
public final class ModCreativeTab {
    public static final DeferredRegister<CreativeModeTab> REGISTER =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Ironagefurniture.MODID);
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> CHAIRS =
            tab("creative_tab", "chairs", 0, "chair_wood_ironage_classic_oak", Items.OAK_STAIRS);
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> BENCHES =
            tab("benches", "benches", 1, "chair_wood_ironage_bench_single_oak", Items.OAK_SLAB);
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> BEDS =
            tab("beds", "beds", 2, "bed_canopy_foot_lower_oak",
                    BuiltInRegistries.ITEM.getValue(Identifier.parse("minecraft:red_bed")));
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> LIGHTS =
            tab("lights", "lights", 3, SconceMetalData.FRAME, Items.TORCH);
    /** @deprecated Use the named category; this alias does not register another tab. */
    @Deprecated public static final DeferredHolder<CreativeModeTab, CreativeModeTab> FURNITURE = CHAIRS;
    /** @deprecated Use CHAIRS, BENCHES, BEDS or LIGHTS. */
    @Deprecated public static final DeferredHolder<CreativeModeTab, CreativeModeTab> IAF_GROUP = CHAIRS;

    private static DeferredHolder<CreativeModeTab, CreativeModeTab> tab(String id, String category, int order,
            String icon, Item fallback) {
        return REGISTER.register(id, () -> CreativeModeTab.builder(CreativeModeTab.Row.TOP, order)
                .title(Component.translatable("itemGroup.ironagefurniture." + category))
                .icon(() -> {
                    Item item = BuiltInRegistries.ITEM.getValue(Identifier.fromNamespaceAndPath("ironagefurniture", icon));
                    return new ItemStack(item != null && item != Items.AIR && visible(item) ? item : fallback);
                })
                .displayItems((parameters, output) -> ModItems.REGISTER.getEntries().forEach(holder -> {
                    Item item = holder.get();
                    if (visible(item) && category.equals(category(item))) output.accept(item);
                })).build());
    }
    public static String category(Item item) {
        String path = BuiltInRegistries.ITEM.getKey(item).getPath();
        if (path.startsWith("bed_")) return "beds";
        if (path.contains("_bench_")) return "benches";
        if (path.startsWith("chair_")) return "chairs";
        return "lights";
    }
    public static ResourceKey<CreativeModeTab> keyFor(Item item) {
        return switch (category(item)) {
            case "chairs" -> CHAIRS.getKey();
            case "benches" -> BENCHES.getKey();
            case "beds" -> BEDS.getKey();
            default -> LIGHTS.getKey();
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
