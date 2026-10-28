package zone.moddev.mc.ironagefurniture.api.recipes;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraftforge.common.crafting.conditions.ICondition;
import zone.moddev.mc.ironagefurniture.IronAgeFurnitureConfiguration;
import net.minecraftforge.fml.ModList;

/** Only installed providers with actual nugget tags receive construction recipes. */
public record MetalAvailableCondition(String metal) implements ICondition {
    public static final MapCodec<MetalAvailableCondition> CODEC = Codec.STRING.fieldOf("metal").xmap(MetalAvailableCondition::new, MetalAvailableCondition::metal);
    @Override public boolean test(IContext context, com.mojang.serialization.DynamicOps<?> ops) {
        if (!ModList.get().isLoaded("basemetals") || !IronAgeFurnitureConfiguration.enabled(IronAgeFurnitureConfiguration.BASE_METALS)) return false;
        var tag = net.minecraft.tags.TagKey.create(net.minecraft.core.registries.Registries.ITEM,
                net.minecraft.resources.ResourceLocation.fromNamespaceAndPath("c", "nuggets/" + metal));
        var blocks = net.minecraft.tags.TagKey.create(net.minecraft.core.registries.Registries.BLOCK,
                net.minecraft.resources.ResourceLocation.fromNamespaceAndPath("c", "storage_blocks/" + metal));
        return !context.getTag(tag).isEmpty() && !context.getTag(blocks).isEmpty();
    }
    @Override public MapCodec<? extends ICondition> codec() { return CODEC; }
}
