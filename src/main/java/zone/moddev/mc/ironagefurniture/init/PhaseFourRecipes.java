package zone.moddev.mc.ironagefurniture.init;

import net.minecraftforge.registries.DeferredRegister;
import net.minecraft.world.item.crafting.RecipeSerializer;
import zone.moddev.mc.ironagefurniture.Ironagefurniture;

/** Forge47 uses explicit condition serializers and native JSON/network recipes. */
public final class PhaseFourRecipes {
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS = DeferredRegister.create(
            net.minecraft.core.registries.Registries.RECIPE_SERIALIZER, Ironagefurniture.MODID);
    static {
        SERIALIZERS.register("shield_chair", () -> zone.moddev.mc.ironagefurniture.api.recipes.ShieldChairRecipe.SERIALIZER);
        SERIALIZERS.register("bed_recolour", () -> zone.moddev.mc.ironagefurniture.api.recipes.BedRecolourRecipe.SERIALIZER);
    }
    public static void registerConditions(net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            net.minecraftforge.common.crafting.CraftingHelper.register(zone.moddev.mc.ironagefurniture.api.recipes.FurnitureFeatureCondition.SERIALIZER);
            net.minecraftforge.common.crafting.CraftingHelper.register(zone.moddev.mc.ironagefurniture.api.recipes.MetalAvailableCondition.SERIALIZER);
        });
    }
    private PhaseFourRecipes() { }
}
