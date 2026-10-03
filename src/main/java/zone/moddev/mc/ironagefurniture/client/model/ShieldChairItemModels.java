package zone.moddev.mc.ironagefurniture.client.model;

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
import zone.moddev.mc.ironagefurniture.api.ShieldChairItemData;
import zone.moddev.mc.ironagefurniture.api.blocks.furniture.ShieldChair;

/** Empty chair items use the same resource-pack-overridable wooden frame as blocks. */
@Mod.EventBusSubscriber(modid = Ironagefurniture.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class ShieldChairItemModels {
    private ShieldChairItemModels() { }

    private static ResourceLocation emptyModel(ResourceLocation chair) {
        return new ResourceLocation(chair.getNamespace(), "block/" + chair.getPath() + "_frame");
    }

    @SubscribeEvent public static void registerModels(ModelRegistryEvent event) {
        ForgeRegistries.BLOCKS.getValues().stream().filter(block -> block instanceof ShieldChair)
                .forEach(block -> ModelLoader.addSpecialModel(emptyModel(block.getRegistryName())));
    }

    @SubscribeEvent public static void bakeModels(ModelBakeEvent event) {
        ForgeRegistries.BLOCKS.getValues().stream().filter(block -> block instanceof ShieldChair).forEach(block -> {
            ModelResourceLocation item = new ModelResourceLocation(block.getRegistryName(), "inventory");
            IBakedModel installed = event.getModelRegistry().get(item);
            IBakedModel empty = event.getModelRegistry().get(emptyModel(block.getRegistryName()));
            if (installed != null && empty != null) event.getModelRegistry().put(item, new ItemModel(installed, empty));
        });
    }

    private static final class ItemModel extends BakedModelWrapper<IBakedModel> {
        private final ItemOverrideList overrides;

        private ItemModel(IBakedModel installed, IBakedModel empty) {
            super(installed);
            overrides = new ItemOverrideList() {
                @Override public IBakedModel getModelWithOverrides(IBakedModel model, ItemStack stack,
                        World world, LivingEntity entity) {
                    return ShieldChairItemData.isEmptyFrame(stack) ? empty : installed.getOverrides()
                            .getModelWithOverrides(installed, stack, world, entity);
                }
            };
        }

        @Override public ItemOverrideList getOverrides() { return overrides; }
    }
}
