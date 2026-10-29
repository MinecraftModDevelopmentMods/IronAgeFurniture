package zone.moddev.mc.ironagefurniture.init;

import com.mojang.serialization.MapCodec;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import zone.moddev.mc.ironagefurniture.Ironagefurniture;
import zone.moddev.mc.ironagefurniture.api.recipes.FurnitureFeatureCondition;

public final class PhaseFourRecipes {
    public static final DeferredRegister<net.minecraft.world.item.crafting.RecipeSerializer<?>> SERIALIZERS =
            DeferredRegister.create(net.minecraft.core.registries.Registries.RECIPE_SERIALIZER, Ironagefurniture.MODID);
    public static final DeferredRegister<MapCodec<? extends ICondition>> CONDITIONS =
            DeferredRegister.create(ForgeRegistries.Keys.CONDITION_SERIALIZERS, Ironagefurniture.MODID);
    static {
        CONDITIONS.register("feature_enabled", () -> FurnitureFeatureCondition.CODEC);
        CONDITIONS.register("metal_available", () -> zone.moddev.mc.ironagefurniture.api.recipes.MetalAvailableCondition.CODEC);
        SERIALIZERS.register("shield_chair", () -> zone.moddev.mc.ironagefurniture.api.recipes.ShieldChairRecipe.SERIALIZER);
        SERIALIZERS.register("bed_recolour", () -> zone.moddev.mc.ironagefurniture.api.recipes.BedRecolourRecipe.SERIALIZER);
    }
    private PhaseFourRecipes() { }
}
