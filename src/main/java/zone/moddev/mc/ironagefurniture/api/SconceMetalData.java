package zone.moddev.mc.ironagefurniture.api;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.neoforged.fml.ModList;
import zone.moddev.mc.ironagefurniture.IronAgeFurnitureConfiguration;
import zone.moddev.mc.ironagefurniture.api.enumerations.SconceMetal;
import zone.moddev.mc.ironagefurniture.api.items.MetalSconceBlockItem;
import zone.moddev.mc.ironagefurniture.init.ModVanillaLights;

/** A missing metal provider hides recipes, not the material saved on furniture. */
public final class SconceMetalData {
    public static final EnumProperty<SconceMetal> METAL = EnumProperty.create("metal", SconceMetal.class);
    public static final String FRAME = "light_metal_ironage_sconce_floor_empty_iron";
    public static SconceMetal get(BlockState state) { return state.hasProperty(METAL) ? state.getValue(METAL) : SconceMetal.IRON; }
    public static SconceMetal get(ItemStack stack) {
        return stack.getItem() instanceof MetalSconceBlockItem item ? item.getMetal() : SconceMetal.IRON;
    }
    public static Identifier itemId(SconceMetal metal) {
        return Identifier.fromNamespaceAndPath("ironagefurniture", metal == SconceMetal.IRON ? FRAME
                : "light_metal_ironage_sconce_floor_empty_" + (metal.isBaseMetal() ? "basemetals_" : "") + metal.getName());
    }
    public static ItemStack frame(BlockState state) {
        return new ItemStack(BuiltInRegistries.ITEM.getValue(itemId(get(state))));
    }
    public static BlockState empty(BlockState state, boolean wall) {
        Block block = wall ? ModVanillaLights.light_metal_ironage_sconce_wall_empty_iron.get()
                : ModVanillaLights.light_metal_ironage_sconce_floor_empty_iron.get();
        return block.defaultBlockState().withPropertiesOf(state);
    }
    public static boolean available(SconceMetal metal) {
        if (!metal.isBaseMetal()) return true;
        if (!ModList.get().isLoaded("basemetals") || !IronAgeFurnitureConfiguration.enabled(IronAgeFurnitureConfiguration.BASE_METALS)) return false;
        TagKey<Item> nuggets = TagKey.create(net.minecraft.core.registries.Registries.ITEM,
                Identifier.fromNamespaceAndPath("c", "nuggets/" + metal.getName()));
        TagKey<Block> storage = TagKey.create(net.minecraft.core.registries.Registries.BLOCK,
                Identifier.fromNamespaceAndPath("c", "storage_blocks/" + metal.getName()));
        return BuiltInRegistries.ITEM.get(nuggets).map(set -> set.size() > 0).orElse(false)
                && BuiltInRegistries.BLOCK.get(storage).map(set -> set.size() > 0).orElse(false);
    }
    public static BlockState storageModel(SconceMetal metal) {
        if (metal.isBaseMetal() && available(metal)) {
            TagKey<Block> tag = TagKey.create(net.minecraft.core.registries.Registries.BLOCK,
                    Identifier.fromNamespaceAndPath("c", "storage_blocks/" + metal.getName()));
            return BuiltInRegistries.BLOCK.get(tag).flatMap(set -> set.stream()
                    .sorted(java.util.Comparator.comparing(holder -> BuiltInRegistries.BLOCK.getKey(holder.value()).toString()))
                    .findFirst()).map(holder -> holder.value().defaultBlockState())
                    .orElse(net.minecraft.world.level.block.Blocks.IRON_BLOCK.defaultBlockState());
        }
        return net.minecraft.world.level.block.Blocks.IRON_BLOCK.defaultBlockState();
    }
    private SconceMetalData() { }
}
