package zone.moddev.mc.ironagefurniture.init;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.neoforged.neoforge.common.conditions.ICondition;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import zone.moddev.mc.ironagefurniture.Ironagefurniture;
import zone.moddev.mc.ironagefurniture.api.recipes.BedRecolourRecipe;
import zone.moddev.mc.ironagefurniture.api.recipes.FurnitureFeatureCondition;
import zone.moddev.mc.ironagefurniture.api.recipes.ShieldChairRecipe;

/** Data-loading conditions for server-controlled furniture availability. */
public final class PhaseFourRecipes {
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS =
            DeferredRegister.create(BuiltInRegistries.RECIPE_SERIALIZER, Ironagefurniture.MODID);
    public static final DeferredRegister<MapCodec<? extends ICondition>> CONDITIONS =
            DeferredRegister.create(NeoForgeRegistries.CONDITION_SERIALIZERS, Ironagefurniture.MODID);
    static {
        SERIALIZERS.register("shield_chair", () -> ShieldChairRecipe.SERIALIZER);
        SERIALIZERS.register("bed_recolour", () -> BedRecolourRecipe.SERIALIZER);
        CONDITIONS.register("feature_enabled", () -> FurnitureFeatureCondition.CODEC);
        CONDITIONS.register("metal_available", () -> zone.moddev.mc.ironagefurniture.api.recipes.MetalAvailableCondition.CODEC);
    }
    private PhaseFourRecipes() { }
}
