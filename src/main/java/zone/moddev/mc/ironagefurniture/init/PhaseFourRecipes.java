package zone.moddev.mc.ironagefurniture.init;

import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;
import zone.moddev.mc.ironagefurniture.Ironagefurniture;
import zone.moddev.mc.ironagefurniture.api.recipes.BedRecolourRecipe;
import zone.moddev.mc.ironagefurniture.api.recipes.ShieldChairRecipe;
import zone.moddev.mc.ironagefurniture.api.recipes.UpholsteryUpgradeRecipe;
import zone.moddev.mc.ironagefurniture.api.recipes.TallowSmeltingRecipe;

@Mod.EventBusSubscriber(modid = Ironagefurniture.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
@ObjectHolder(Ironagefurniture.MODID)
public final class PhaseFourRecipes {
    public static final RecipeSerializer<ShieldChairRecipe> shield_chair = null;
    public static final RecipeSerializer<UpholsteryUpgradeRecipe> upholstery_upgrade = null;
    public static final RecipeSerializer<BedRecolourRecipe> bed_recolour = null;
    public static final RecipeSerializer<TallowSmeltingRecipe> tallow_smelting = null;
    public static final RecipeSerializer<zone.moddev.mc.ironagefurniture.api.recipes.MetalSconceRecipe> metal_sconce = null;

    private PhaseFourRecipes() { }

    @SubscribeEvent
    public static void buildShapelessRecipes(RegistryEvent.Register<RecipeSerializer<?>> event) {
        event.getRegistry().register(new ShieldChairRecipe.Serializer()
                .setRegistryName(Ironagefurniture.MODID, "shield_chair"));
        event.getRegistry().register(new UpholsteryUpgradeRecipe.Serializer()
                .setRegistryName(Ironagefurniture.MODID, "upholstery_upgrade"));
        event.getRegistry().register(new BedRecolourRecipe.Serializer()
                .setRegistryName(Ironagefurniture.MODID, "bed_recolour"));
        event.getRegistry().register(new TallowSmeltingRecipe.Serializer()
                .setRegistryName(Ironagefurniture.MODID, "tallow_smelting"));
        event.getRegistry().register(new zone.moddev.mc.ironagefurniture.api.recipes.MetalSconceRecipe.Serializer()
                .setRegistryName(Ironagefurniture.MODID, "metal_sconce"));
    }
}
