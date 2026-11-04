package zone.moddev.mc.ironagefurniture.init;

import net.minecraftforge.registries.DeferredRegister;
import net.minecraft.world.item.crafting.RecipeSerializer;
import zone.moddev.mc.ironagefurniture.Ironagefurniture;

/** Registers native recipes and their loading conditions. */
public final class PhaseFourRecipes {
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS = DeferredRegister.create(
            net.minecraftforge.registries.ForgeRegistries.RECIPE_SERIALIZERS, Ironagefurniture.MODID);
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
