package zone.moddev.mc.ironagefurniture.api;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fml.ModList;
import zone.moddev.mc.ironagefurniture.IronAgeFurnitureConfiguration;
import zone.moddev.mc.ironagefurniture.api.enumerations.SconceMetal;
import zone.moddev.mc.ironagefurniture.api.items.MetalSconceBlockItem;
import zone.moddev.mc.ironagefurniture.init.ModVanillaLights;

/** A missing metal provider hides recipes, not the material saved on furniture. */
public final class SconceMetalData {
    public static final String FRAME = "light_metal_ironage_sconce_floor_empty_iron";
    /** Native custom-data key; the value is the actual saved provider item identity. */
    public static final String STORED_LIGHT = "ironagefurniture:stored_light";
    public static SconceMetal get(BlockState state) {
        return zone.moddev.mc.ironagefurniture.init.SconceVariants.metal(state.getBlock());
    }
    public static SconceMetal material(String path) {
        if (!path.startsWith("light_metal_ironage_sconce_")) return SconceMetal.IRON;
        for (SconceMetal metal : SconceMetal.values()) {
            String token = "_" + (metal.isBaseMetal() ? "basemetals_" : "") + metal.getName();
            int index = path.indexOf(token, "light_metal_ironage_sconce_".length());
            if (index >= 0 && (index + token.length() == path.length() || path.charAt(index + token.length()) == '_')) return metal;
        }
        return SconceMetal.IRON;
    }
    public static String ironPath(String path) {
        SconceMetal metal = material(path);
        return metal == SconceMetal.IRON ? path : path.replace("_" + (metal.isBaseMetal() ? "basemetals_" : "") + metal.getName(), "_iron");
    }
    public static String blockPath(String ironPath, SconceMetal metal) {
        int index = ironPath.indexOf("_iron", "light_metal_ironage_sconce_".length());
        if (!ironPath.startsWith("light_metal_ironage_sconce_") || index < 0
                || index + 5 < ironPath.length() && ironPath.charAt(index + 5) != '_')
            throw new IllegalArgumentException("Not an iron sconce family: " + ironPath);
        return ironPath.substring(0, index) + "_" + (metal.isBaseMetal() ? "basemetals_" : "") + metal.getName()
                + ironPath.substring(index + "_iron".length());
    }
    public static BlockState withMetal(BlockState state, SconceMetal metal) {
        return zone.moddev.mc.ironagefurniture.init.SconceVariants.material(state.getBlock(), metal).withPropertiesOf(state);
    }
    /** Change the installed light while retaining the frame's registered material. */
    public static BlockState transition(Block target, BlockState state) {
        return zone.moddev.mc.ironagefurniture.init.SconceVariants.material(target, get(state)).withPropertiesOf(state);
    }
    public static SconceMetal get(ItemStack stack) {
        return stack.getItem() instanceof MetalSconceBlockItem item ? item.getMetal() : SconceMetal.IRON;
    }
    public static ResourceLocation itemId(SconceMetal metal) {
        return new ResourceLocation("ironagefurniture", metal == SconceMetal.IRON ? FRAME
                : "light_metal_ironage_sconce_floor_empty_" + (metal.isBaseMetal() ? "basemetals_" : "") + metal.getName());
    }
    public static ItemStack frame(BlockState state) {
        return new ItemStack(BuiltInRegistries.ITEM.get(itemId(get(state))));
    }
    /** One existing frame item carries an unavailable installed lamp without a new registry ID. */
    public static ItemStack storedRockSaltFrame(BlockState state) {
        ItemStack result = frame(state);
        result.getOrCreateTag().putString(STORED_LIGHT,
                zone.moddev.mc.ironagefurniture.api.blocks.lightsource.phasefour.RockSaltSconce.LAMP.toString());
        // Every hidden placed sconce already has a name in all supported resource locales.
        result.setHoverName(
                net.minecraft.network.chat.Component.translatable(state.getBlock().getDescriptionId()));
        return result;
    }
    /** Restore only the known stored light; unknown future payloads fail placement without consumption. */
    public static BlockState restoreStoredLight(BlockState placement, ItemStack item) {
        var data = item.hasTag() ? item.getTag() : new net.minecraft.nbt.CompoundTag();
        if (!data.contains(STORED_LIGHT)) return placement;
        if (!data.contains(STORED_LIGHT, net.minecraft.nbt.Tag.TAG_STRING)
                || !data.getString(STORED_LIGHT).equals(
                        zone.moddev.mc.ironagefurniture.api.blocks.lightsource.phasefour.RockSaltSconce.LAMP.toString())) return null;
        boolean wall = BuiltInRegistries.BLOCK.getKey(placement.getBlock()).getPath().contains("_sconce_wall_");
        Block lamp = (wall ? zone.moddev.mc.ironagefurniture.init.PhaseFourLighting.WALL_SALT
                : zone.moddev.mc.ironagefurniture.init.PhaseFourLighting.FLOOR_SALT).get();
        return transition(lamp, placement);
    }
    public static BlockState empty(BlockState state, boolean wall) {
        Block block = wall ? ModVanillaLights.light_metal_ironage_sconce_wall_empty_iron.get()
                : ModVanillaLights.light_metal_ironage_sconce_floor_empty_iron.get();
        return transition(block, state);
    }
    public static boolean available(SconceMetal metal) {
        if (!metal.isBaseMetal()) return true;
        if (!ModList.get().isLoaded("basemetals") || !IronAgeFurnitureConfiguration.enabled(IronAgeFurnitureConfiguration.BASE_METALS)) return false;
        TagKey<Item> nuggets = TagKey.create(net.minecraft.core.registries.Registries.ITEM,
                new ResourceLocation("forge", "nuggets/" + metal.getName()));
        TagKey<Block> storage = TagKey.create(net.minecraft.core.registries.Registries.BLOCK,
                new ResourceLocation("forge", "storage_blocks/" + metal.getName()));
        return BuiltInRegistries.ITEM.getTag(nuggets).map(set -> set.size() > 0).orElse(false)
                && BuiltInRegistries.BLOCK.getTag(storage).map(set -> set.size() > 0).orElse(false);
    }
    public static BlockState storageModel(SconceMetal metal) {
        if (metal.isBaseMetal() && available(metal)) {
            TagKey<Block> tag = TagKey.create(net.minecraft.core.registries.Registries.BLOCK,
                    new ResourceLocation("forge", "storage_blocks/" + metal.getName()));
            return BuiltInRegistries.BLOCK.getTag(tag).flatMap(set -> set.stream()
                    .sorted(java.util.Comparator.comparing(holder -> BuiltInRegistries.BLOCK.getKey(holder.value()).toString()))
                    .findFirst()).map(holder -> holder.value().defaultBlockState())
                    .orElse(net.minecraft.world.level.block.Blocks.IRON_BLOCK.defaultBlockState());
        }
        return net.minecraft.world.level.block.Blocks.IRON_BLOCK.defaultBlockState();
    }
    private SconceMetalData() { }
}
