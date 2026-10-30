package zone.moddev.mc.ironagefurniture.api.recipes;

import net.minecraftforge.common.crafting.conditions.ICondition;
import zone.moddev.mc.ironagefurniture.IronAgeFurnitureConfiguration;
import net.minecraftforge.fml.ModList;

/** Only installed providers with actual nugget tags receive construction recipes. */
public record MetalAvailableCondition(String metal) implements ICondition {
    @Override public boolean test(IContext context) {
        if (!ModList.get().isLoaded("basemetals") || !IronAgeFurnitureConfiguration.enabled(IronAgeFurnitureConfiguration.BASE_METALS)) return false;
        var tag = net.minecraft.tags.TagKey.create(net.minecraft.core.registries.Registries.ITEM,
                new net.minecraft.resources.ResourceLocation("forge", "nuggets/" + metal));
        var blocks = net.minecraft.tags.TagKey.create(net.minecraft.core.registries.Registries.BLOCK,
                new net.minecraft.resources.ResourceLocation("forge", "storage_blocks/" + metal));
        return !context.getTag(tag).isEmpty() && !context.getTag(blocks).isEmpty();
    }
    @Override public net.minecraft.resources.ResourceLocation getID() { return SERIALIZER.getID(); }
    public static final net.minecraftforge.common.crafting.conditions.IConditionSerializer<MetalAvailableCondition> SERIALIZER =
            new net.minecraftforge.common.crafting.conditions.IConditionSerializer<>() {
        @Override public void write(com.google.gson.JsonObject json, MetalAvailableCondition value) { json.addProperty("metal", value.metal()); }
        @Override public MetalAvailableCondition read(com.google.gson.JsonObject json) { return new MetalAvailableCondition(net.minecraft.util.GsonHelper.getAsString(json, "metal")); }
        @Override public net.minecraft.resources.ResourceLocation getID() { return new net.minecraft.resources.ResourceLocation("ironagefurniture", "metal_available"); }
    };
}
