package zone.moddev.mc.ironagefurniture.init;

import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;
import zone.moddev.mc.ironagefurniture.Ironagefurniture;
import zone.moddev.mc.ironagefurniture.api.recipes.ShieldChairRecipe;
import zone.moddev.mc.ironagefurniture.api.recipes.UpholsteryUpgradeRecipe;
import zone.moddev.mc.ironagefurniture.api.recipes.TallowSmeltingRecipe;

@Mod.EventBusSubscriber(modid = Ironagefurniture.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
@ObjectHolder(Ironagefurniture.MODID)
public final class PhaseFourRecipes {
    public static final IRecipeSerializer<ShieldChairRecipe> shield_chair = null;
    public static final IRecipeSerializer<UpholsteryUpgradeRecipe> upholstery_upgrade = null;
    public static final IRecipeSerializer<TallowSmeltingRecipe> tallow_smelting = null;

    private PhaseFourRecipes() { }

    @SubscribeEvent
    public static void registerRecipes(RegistryEvent.Register<IRecipeSerializer<?>> event) {
        event.getRegistry().register(new ShieldChairRecipe.Serializer()
                .setRegistryName(Ironagefurniture.MODID, "shield_chair"));
        event.getRegistry().register(new UpholsteryUpgradeRecipe.Serializer()
                .setRegistryName(Ironagefurniture.MODID, "upholstery_upgrade"));
        event.getRegistry().register(new TallowSmeltingRecipe.Serializer()
                .setRegistryName(Ironagefurniture.MODID, "tallow_smelting"));
    }
}
