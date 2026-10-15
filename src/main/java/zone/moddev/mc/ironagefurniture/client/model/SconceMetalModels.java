package zone.moddev.mc.ironagefurniture.client.model;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.BlockModelShaper;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.model.BakedModelWrapper;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
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
            for (BlockState state : block.getStateDefinition().getPossibleStates()) {
                ModelResourceLocation key = BlockModelShaper.stateToModelLocation(state);
                BakedModel model = event.getModelRegistry().get(key);
                if (model != null) event.getModelRegistry().put(key, new MetalModel(model, SconceMetalData.get(state)));
            }
        }
        for (net.minecraft.world.item.Item item : ForgeRegistries.ITEMS.getValues()) {
            if (!(item instanceof zone.moddev.mc.ironagefurniture.api.items.MetalSconceBlockItem)) continue;
            ModelResourceLocation key = new ModelResourceLocation(item.getRegistryName(), "inventory");
            BakedModel model = event.getModelRegistry().get(key);
            if (model != null) event.getModelRegistry().put(key, new ItemModel(model));
        }
    }
    private static final class ItemModel extends BakedModelWrapper<BakedModel> {
        private final ItemOverrides overrides;
        ItemModel(BakedModel original) {
            super(original);
            Map<SconceMetal, BakedModel> models = new EnumMap<>(SconceMetal.class);
            for (SconceMetal metal : SconceMetal.values()) models.put(metal, new MetalModel(original, metal));
            overrides = new ItemOverrides() {
                @Override public BakedModel resolve(BakedModel model, ItemStack stack, net.minecraft.client.multiplayer.ClientLevel world, LivingEntity entity, int seed) {
                    return models.get(SconceMetalData.get(stack));
                }
            };
        }
        @Override public ItemOverrides getOverrides() { return overrides; }
    }
    private static final class MetalModel extends BakedModelWrapper<BakedModel> {
        private final SconceMetal metal;
        MetalModel(BakedModel original, SconceMetal metal) { super(original); this.metal = metal; }
        @Override public boolean doesHandlePerspectives() { return true; }
        @Override public BakedModel handlePerspective(ItemTransforms.TransformType transform, PoseStack stack) {
            // Keep the pack's camera transform without reverting to its iron
            // model when Forge renders an inventory, held or dropped item.
            BakedModel transformed = originalModel.handlePerspective(transform, stack);
            return transformed == originalModel ? this : new MetalModel(transformed, metal);
        }
        private TextureAtlasSprite texture() {
            // The supplying block's baked particle texture is already in the
            // atlas. This follows resource packs without guessing future paths.
            return Minecraft.getInstance().getBlockRenderer()
                    .getBlockModel(SconceMetalData.textureBlock(metal).defaultBlockState()).getParticleIcon();
        }
        private List<BakedQuad> replace(List<BakedQuad> quads) {
            TextureAtlasSprite sprite = texture();
            if (IRON.equals(sprite.getName())) return quads;
            return quads.stream().map(quad -> IRON.equals(quad.getSprite().getName())
                    ? retexture(quad, sprite) : quad).collect(Collectors.toList());
        }
        @Override public List<BakedQuad> getQuads(BlockState state, Direction side, Random random) {
            return replace(originalModel.getQuads(state, side, random));
        }
        @Override public List<BakedQuad> getQuads(BlockState state, Direction side, Random random, IModelData data) {
            return replace(originalModel.getQuads(state, side, random, data));
        }
        @Override public TextureAtlasSprite getParticleIcon() {
            return IRON.equals(originalModel.getParticleIcon().getName()) ? texture() : originalModel.getParticleIcon();
        }
        @Override public TextureAtlasSprite getParticleIcon(IModelData data) {
            TextureAtlasSprite original = originalModel.getParticleIcon(data);
            return IRON.equals(original.getName()) ? texture() : original;
        }
    }

    private static BakedQuad retexture(BakedQuad quad, TextureAtlasSprite target) {
        int[] vertices = quad.getVertices().clone();
        TextureAtlasSprite source = quad.getSprite();
        int stride = vertices.length / 4;
        for (int vertex = 0; vertex < 4; vertex++) {
            int uv = vertex * stride + 4;
            float u = Float.intBitsToFloat(vertices[uv]), v = Float.intBitsToFloat(vertices[uv + 1]);
            vertices[uv] = Float.floatToRawIntBits(target.getU0() + (u - source.getU0())
                    / (source.getU1() - source.getU0()) * (target.getU1() - target.getU0()));
            vertices[uv + 1] = Float.floatToRawIntBits(target.getV0() + (v - source.getV0())
                    / (source.getV1() - source.getV0()) * (target.getV1() - target.getV0()));
        }
        return new BakedQuad(vertices, quad.getTintIndex(), quad.getDirection(), target, quad.isShade());
    }
}
