package zone.moddev.mc.ironagefurniture.client.model;

import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
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
            BakedModel installed = event.getModelRegistry().get(item);
            BakedModel empty = event.getModelRegistry().get(emptyModel(block.getRegistryName()));
            if (installed != null && empty != null) event.getModelRegistry().put(item, new ItemModel(installed, empty));
        });
    }

    private static final class ItemModel extends BakedModelWrapper<BakedModel> {
        private final ItemOverrides overrides;

        private ItemModel(BakedModel installed, BakedModel empty) {
            super(installed);
            overrides = new ItemOverrides() {
                @Override public BakedModel resolve(BakedModel model, ItemStack stack,
                        net.minecraft.client.multiplayer.ClientLevel world, LivingEntity entity, int seed) {
                    return ShieldChairItemData.isEmptyFrame(stack) ? empty : installed.getOverrides()
                            .resolve(installed, stack, world, entity, seed);
                }
            };
        }

        @Override public ItemOverrides getOverrides() { return overrides; }
    }
}
