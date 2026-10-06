package zone.moddev.mc.ironagefurniture.client.model;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;
import javax.vecmath.Matrix4f;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockModelShapes;
import net.minecraft.client.renderer.model.BakedQuad;
import net.minecraft.client.renderer.model.BakedQuadRetextured;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.model.ItemOverrideList;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.model.BakedModelWrapper;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.commons.lang3.tuple.Pair;
import zone.moddev.mc.ironagefurniture.Ironagefurniture;
import zone.moddev.mc.ironagefurniture.api.SconceMetalData;
import zone.moddev.mc.ironagefurniture.api.blocks.base.LightHolderSconce;
import zone.moddev.mc.ironagefurniture.api.enumerations.SconceMetal;

/** Keep the original model and replace only its iron frame's texture. */
@Mod.EventBusSubscriber(modid = Ironagefurniture.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class SconceMetalModels {
    private static final ResourceLocation IRON = new ResourceLocation("minecraft:block/iron_block");
    private SconceMetalModels() { }
    @SubscribeEvent public static void bake(ModelBakeEvent event) {
        for (Block block : ForgeRegistries.BLOCKS.getValues()) {
            if (!(block instanceof LightHolderSconce)) continue;
            for (BlockState state : block.getStateContainer().getValidStates()) {
                ModelResourceLocation key = BlockModelShapes.getModelLocation(state);
                IBakedModel model = event.getModelRegistry().get(key);
                if (model != null) event.getModelRegistry().put(key, new MetalModel(model, SconceMetalData.get(state)));
            }
        }
        for (net.minecraft.item.Item item : ForgeRegistries.ITEMS.getValues()) {
            if (!(item instanceof zone.moddev.mc.ironagefurniture.api.items.MetalSconceBlockItem)) continue;
            ModelResourceLocation key = new ModelResourceLocation(item.getRegistryName(), "inventory");
            IBakedModel model = event.getModelRegistry().get(key);
            if (model != null) event.getModelRegistry().put(key, new ItemModel(model));
        }
    }
    private static final class ItemModel extends BakedModelWrapper<IBakedModel> {
        private final ItemOverrideList overrides;
        ItemModel(IBakedModel original) {
            super(original);
            Map<SconceMetal, IBakedModel> models = new EnumMap<>(SconceMetal.class);
            for (SconceMetal metal : SconceMetal.values()) models.put(metal, new MetalModel(original, metal));
            overrides = new ItemOverrideList() {
                @Override public IBakedModel getModelWithOverrides(IBakedModel model, ItemStack stack, World world, LivingEntity entity) {
                    return models.get(SconceMetalData.get(stack));
                }
            };
        }
        @Override public ItemOverrideList getOverrides() { return overrides; }
    }
    private static final class MetalModel extends BakedModelWrapper<IBakedModel> {
        private final SconceMetal metal;
        MetalModel(IBakedModel original, SconceMetal metal) { super(original); this.metal = metal; }
        @Override public boolean doesHandlePerspectives() { return true; }
        @Override public Pair<? extends IBakedModel, Matrix4f> handlePerspective(ItemCameraTransforms.TransformType transform) {
            // Keep the pack's camera transform without reverting to its iron
            // model when Forge renders an inventory, held or dropped item.
            Pair<? extends IBakedModel, Matrix4f> perspective = originalModel.handlePerspective(transform);
            IBakedModel transformed = perspective.getLeft() == originalModel
                    ? this : new MetalModel(perspective.getLeft(), metal);
            return Pair.of(transformed, perspective.getRight());
        }
        private TextureAtlasSprite texture() {
            // The supplying block's baked particle texture is already in the
            // atlas. This follows resource packs without guessing future paths.
            return Minecraft.getInstance().getBlockRendererDispatcher()
                    .getModelForState(SconceMetalData.textureBlock(metal).getDefaultState()).getParticleTexture();
        }
        private List<BakedQuad> replace(List<BakedQuad> quads) {
            TextureAtlasSprite sprite = texture();
            if (IRON.equals(sprite.getName())) return quads;
            return quads.stream().map(quad -> IRON.equals(quad.getSprite().getName())
                    ? new BakedQuadRetextured(quad, sprite) : quad).collect(Collectors.toList());
        }
        @Override public List<BakedQuad> getQuads(BlockState state, Direction side, Random random) {
            return replace(originalModel.getQuads(state, side, random));
        }
        @Override public List<BakedQuad> getQuads(BlockState state, Direction side, Random random, IModelData data) {
            return replace(originalModel.getQuads(state, side, random, data));
        }
        @Override public TextureAtlasSprite getParticleTexture() {
            return IRON.equals(originalModel.getParticleTexture().getName()) ? texture() : originalModel.getParticleTexture();
        }
        @Override public TextureAtlasSprite getParticleTexture(IModelData data) {
            TextureAtlasSprite original = originalModel.getParticleTexture(data);
            return IRON.equals(original.getName()) ? texture() : original;
        }
    }
}
