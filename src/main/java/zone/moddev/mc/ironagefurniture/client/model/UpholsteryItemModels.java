package zone.moddev.mc.ironagefurniture.client.model;

import java.util.EnumMap;
import java.util.Map;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemOverrideList;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.BakedModelWrapper;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import zone.moddev.mc.ironagefurniture.Ironagefurniture;
import zone.moddev.mc.ironagefurniture.api.UpholsteryItemData;
import zone.moddev.mc.ironagefurniture.api.enumerations.UpholsteryColour;
import zone.moddev.mc.ironagefurniture.api.items.UpholsteredBlockItem;

/** Inventory colours use the same wool textures and resource-pack models as placed furniture. */
@Mod.EventBusSubscriber(modid = Ironagefurniture.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class UpholsteryItemModels {
    private UpholsteryItemModels() { }
    private static ResourceLocation model(ResourceLocation item, UpholsteryColour colour) {
        return new ResourceLocation(item.getNamespace(), "item/" + item.getPath() + "_" + colour.getName());
    }
    @SubscribeEvent public static void registerModels(ModelRegistryEvent event) {
        ForgeRegistries.ITEMS.getValues().stream().filter(item -> item instanceof UpholsteredBlockItem).forEach(item -> {
            for (UpholsteryColour colour : UpholsteryColour.values()) ModelLoader.addSpecialModel(model(item.getRegistryName(), colour));
        });
    }
    @SubscribeEvent public static void bakeModels(ModelBakeEvent event) {
        ForgeRegistries.ITEMS.getValues().stream().filter(item -> item instanceof UpholsteredBlockItem).forEach(item -> {
            ModelResourceLocation key = new ModelResourceLocation(item.getRegistryName(), "inventory");
            IBakedModel original = event.getModelRegistry().get(key);
            Map<UpholsteryColour, IBakedModel> colours = new EnumMap<>(UpholsteryColour.class);
            for (UpholsteryColour colour : UpholsteryColour.values()) {
                IBakedModel delegate = event.getModelRegistry().get(model(item.getRegistryName(), colour));
                if (delegate == null) throw new IllegalStateException("Missing upholstered item model: " + model(item.getRegistryName(), colour));
                colours.put(colour, delegate);
            }
            event.getModelRegistry().put(key, new ColourModel(original, colours));
        });
    }
    private static final class ColourModel extends BakedModelWrapper<IBakedModel> {
        private final ItemOverrideList overrides;
        ColourModel(IBakedModel original, Map<UpholsteryColour, IBakedModel> colours) {
            super(original);
            overrides = new ItemOverrideList() {
                @Override public IBakedModel getModelWithOverrides(IBakedModel model, ItemStack stack, World world, LivingEntity entity) {
                    IBakedModel delegate = colours.get(UpholsteryItemData.getColour(stack));
                    return delegate.getOverrides().getModelWithOverrides(delegate, stack, world, entity);
                }
            };
        }
        @Override public ItemOverrideList getOverrides() { return overrides; }
    }
}
