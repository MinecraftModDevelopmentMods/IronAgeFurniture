package zone.moddev.mc.ironagefurniture.fixture.client;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.BannerPatternLayers;
import net.minecraft.world.level.block.entity.BannerPatterns;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import zone.moddev.mc.ironagefurniture.api.blocks.base.FurnitureBlock;
import zone.moddev.mc.ironagefurniture.api.entity.Entities;
import zone.moddev.mc.ironagefurniture.api.entity.FallingShieldChair;
import zone.moddev.mc.ironagefurniture.api.tile.ShieldChairBlockEntity;
import zone.moddev.mc.ironagefurniture.client.renderer.FallingShieldChairRenderer;
import zone.moddev.mc.ironagefurniture.client.renderer.ShieldChairRenderer;

/** Inspect the actual submitted geometry, not merely whether a shield model exists. */
final class ShieldChairRenderChecks {
    private record Draw(Model<?> model, Matrix4f transform, RenderType type, int tint, TextureAtlasSprite sprite) { }
    private static final class Capture {
        final List<Draw> draws = new ArrayList<>();
        final SubmitNodeCollector collector = (SubmitNodeCollector)Proxy.newProxyInstance(
                SubmitNodeCollector.class.getClassLoader(), new Class<?>[]{SubmitNodeCollector.class}, (proxy, method, args) -> {
                    if (method.getName().equals("order")) return proxy;
                    if (method.isDefault()) return InvocationHandler.invokeDefault(proxy, method, args);
                    if (method.getName().equals("submitModel")) {
                        draws.add(new Draw((Model<?>)args[0], new Matrix4f(((PoseStack)args[2]).last().pose()),
                                (RenderType)args[3], (int)args[6], (TextureAtlasSprite)args[7]));
                    }
                    return null;
                });
    }

    static void verify(ShieldChairRenderer renderer, ShieldChairRenderer.State state, ItemStack shield,
            Direction facing, BiConsumer<Boolean, String> check) {
        Capture capture = new Capture();
        PoseStack poses = new PoseStack();
        renderer.submit(state, poses, capture.collector, new CameraRenderState());
        check.accept(poses.last().pose().equals(new Matrix4f()), "Shield submission restores the caller's pose");
        verifyDraws(capture, shield, facing, 0, check);
    }

    static void matrix(Minecraft client, ShieldChairRenderer renderer, BlockState frame,
            BiConsumer<Boolean, String> check) throws ReflectiveOperationException {
        var patterns = client.level.registryAccess().lookupOrThrow(Registries.BANNER_PATTERN);
        int samples = 0;
        for (Direction facing : Direction.Plane.HORIZONTAL) {
            BlockState block = frame.setValue(FurnitureBlock.DIRECTION, facing);
            for (int color = -1; color < 16; color++) for (int count : new int[]{0, 1, 17}) for (boolean foil : new boolean[]{false, true}) {
                ItemStack shield = new ItemStack(Items.SHIELD);
                if (color >= 0) shield.set(DataComponents.BASE_COLOR, DyeColor.byId(color));
                BannerPatternLayers.Builder layers = new BannerPatternLayers.Builder();
                for (int index = 0; index < count; index++)
                    layers.add(patterns.getOrThrow(index % 2 == 0 ? BannerPatterns.CROSS : BannerPatterns.BORDER), DyeColor.byId(index % 16));
                shield.set(DataComponents.BANNER_PATTERNS, layers.build());
                shield.set(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, foil);
                shield.setDamageValue(117);
                ItemStack original = shield.copy();
                var storage = new ShieldChairBlockEntity(BlockPos.ZERO, block);
                storage.setShield(shield);
                var state = renderer.createRenderState();
                renderer.extractRenderState(storage, state, 0, Vec3.ZERO, null);
                verify(renderer, state, shield, facing, check);
                check.accept(ItemStack.matches(original, storage.shield()), "Rendering never alters the stored shield components");
                samples++;
            }
            var empty = new ShieldChairBlockEntity(BlockPos.ZERO, block);
            empty.setShield(ItemStack.EMPTY);
            var emptyState = renderer.createRenderState();
            renderer.extractRenderState(empty, emptyState, 0, Vec3.ZERO, null);
            verify(renderer, emptyState, ItemStack.EMPTY, facing, check);

            // Use the real falling renderer without spawning or ticking a world entity.
            var entity = new FallingShieldChair(Entities.FALLING_SHIELD_CHAIR.get(), client.level);
            setEntityData(entity, "FRAME", block);
            ItemStack carried = new ItemStack(Items.SHIELD);
            carried.set(DataComponents.BASE_COLOR, DyeColor.BLUE);
            carried.set(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true);
            setEntityData(entity, "SHIELD", carried);
            var falling = (FallingShieldChairRenderer)(Object)client.getEntityRenderDispatcher().getRenderer(entity);
            var fallingState = falling.createRenderState();
            falling.extractRenderState(entity, fallingState, 0);
            Capture capture = new Capture();
            falling.submit(fallingState, new PoseStack(), capture.collector, new CameraRenderState());
            verifyDraws(capture, carried, facing, -.5F, check);
        }
        System.out.println("IAF_SHIELD_RENDER_MATRIX_PASS samples=" + samples + " facings=4 falling=4 colors=17 patterns=0,1,17");
    }

    @SuppressWarnings("unchecked")
    private static <T> void setEntityData(FallingShieldChair entity, String name, T value) throws ReflectiveOperationException {
        var field = FallingShieldChair.class.getDeclaredField(name);
        field.setAccessible(true);
        entity.getEntityData().set((EntityDataAccessor<T>)field.get(null), value);
    }

    private static void verifyDraws(Capture capture, ItemStack shield, Direction facing, float offset,
            BiConsumer<Boolean, String> check) {
        if (shield.isEmpty()) {
            check.accept(capture.draws.isEmpty(), "An empty shield chair submits no shield geometry");
            return;
        }
        BannerPatternLayers patterns = shield.getOrDefault(DataComponents.BANNER_PATTERNS, BannerPatternLayers.EMPTY);
        DyeColor color = shield.get(DataComponents.BASE_COLOR);
        boolean patterned = color != null || !patterns.layers().isEmpty();
        int layerCount = Math.min(16, patterns.layers().size());
        int expected = 1 + (patterned ? 1 + layerCount + (shield.hasFoil() ? 1 : 0) : 0);
        check.accept(!capture.draws.isEmpty(), "A stored shield submits visible geometry");
        for (Draw draw : capture.draws) {
            check.accept(draw.model.allParts().size() == 1 && !draw.model.root().hasChild("handle"),
                    "Chair shield submits the plate only, never the handheld model or handle");
            PoseStack pose = new PoseStack();
            pose.last().pose().set(draw.transform);
            float[] bounds = {Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY,
                    Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY};
            draw.model.root().getExtentsForGui(pose, point -> {
                bounds[0] = Math.min(bounds[0], point.x()); bounds[3] = Math.max(bounds[3], point.x());
                bounds[1] = Math.min(bounds[1], point.y()); bounds[4] = Math.max(bounds[4], point.y());
                bounds[2] = Math.min(bounds[2], point.z()); bounds[5] = Math.max(bounds[5], point.z());
            });
            boolean alongZ = facing.getAxis() == Direction.Axis.Z;
            check.accept(close(bounds[4] - bounds[1], 1) && close(bounds[1], .5F), "Chair shield retains its original sixteen-pixel height");
            check.accept(close(alongZ ? bounds[3] - bounds[0] : bounds[5] - bounds[2], 10F / 16F), "Chair shield retains its original ten-pixel width");
            check.accept(close(alongZ ? bounds[5] - bounds[2] : bounds[3] - bounds[0], .75F / 16F), "Chair shield retains its original plate thickness");
            Vector3f front = draw.transform.transformPosition(0, 0, -2F / 16F, new Vector3f());
            Vector3f back = draw.transform.transformPosition(0, 0, -1F / 16F, new Vector3f());
            Vector3f up = draw.transform.transformPosition(0, -11F / 16F, 0, new Vector3f());
            Vector3f down = draw.transform.transformPosition(0, 11F / 16F, 0, new Vector3f());
            check.accept((front.x - back.x) * facing.getStepX() + (front.z - back.z) * facing.getStepZ() > 0,
                    "The patterned front faces the correct direction: " + facing);
            check.accept(up.y > down.y, "Shield designs remain upright");
            check.accept(close(alongZ ? (bounds[0] + bounds[3]) / 2 : (bounds[2] + bounds[5]) / 2, .5F + offset), "Shield stays centred between the chair posts");
            float near = facing.getAxisDirection() == Direction.AxisDirection.POSITIVE ? .886875F : .06625F;
            check.accept(close(alongZ ? bounds[2] : bounds[0], near + offset), "Shield remains at the established chair-back depth");
        }
        check.accept(capture.draws.size() == expected, "Only the expected shield, banner-pattern and glint layers are submitted");
        Draw base = capture.draws.getFirst();
        check.accept(base.sprite.contents().name().equals((patterned ? Sheets.SHIELD_BASE : Sheets.SHIELD_BASE_NO_PATTERN).texture()), "The shield uses the correct resource-pack base sprite");
        if (patterned) {
            check.accept(capture.draws.get(1).tint == (color == null ? DyeColor.WHITE : color).getTextureDiffuseColor(), "Shield base colour is preserved");
            for (int index = 0; index < layerCount; index++) {
                var layer = patterns.layers().get(index);
                Draw draw = capture.draws.get(index + 2);
                check.accept(draw.tint == layer.color().getTextureDiffuseColor()
                        && draw.sprite.contents().name().equals(Sheets.getShieldSprite(layer.pattern()).texture()), "Shield pattern order, design and colour are preserved");
            }
        }
        if (shield.hasFoil()) check.accept(capture.draws.getLast().type == (patterned
                ? RenderTypes.patternedShieldGlint() : RenderTypes.entitySolidGlint(Sheets.SHIELD_BASE_NO_PATTERN.atlasLocation())), "Enchanted shields retain their glint");
    }

    private static boolean close(float actual, float expected) { return Math.abs(actual - expected) < .00001F; }
    private ShieldChairRenderChecks() { }
}
