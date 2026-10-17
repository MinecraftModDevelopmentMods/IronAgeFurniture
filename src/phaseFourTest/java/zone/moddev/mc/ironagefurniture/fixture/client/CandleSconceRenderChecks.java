package zone.moddev.mc.ironagefurniture.fixture.client;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.block.BlockModelRenderState;
import net.minecraft.client.renderer.block.dispatch.BlockStateModelPart;
import net.minecraft.client.renderer.block.model.BlockDisplayContext;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import zone.moddev.mc.ironagefurniture.api.blocks.base.FurnitureBlock;
import zone.moddev.mc.ironagefurniture.api.blocks.lightsource.phasefour.CandleSconceBlock;
import zone.moddev.mc.ironagefurniture.api.tile.CandleSconceBlockEntity;
import zone.moddev.mc.ironagefurniture.client.renderer.CandleSconceRenderer;
import zone.moddev.mc.ironagefurniture.fixture.ClientScene;

/** Measures the submitted candles and baked dishes in the real client. */
final class CandleSconceRenderChecks {
    private record Draw(Matrix4f pose, List<BlockStateModelPart> parts) { }
    private static final class Capture {
        final List<Draw> draws = new ArrayList<>();
        @SuppressWarnings("unchecked")
        final SubmitNodeCollector collector = (SubmitNodeCollector)Proxy.newProxyInstance(
                SubmitNodeCollector.class.getClassLoader(), new Class<?>[]{SubmitNodeCollector.class}, (proxy, method, args) -> {
                    if (method.getName().equals("order")) return proxy;
                    if (method.isDefault()) return InvocationHandler.invokeDefault(proxy, method, args);
                    if (method.getName().equals("submitBlockModel"))
                        draws.add(new Draw(new Matrix4f(((PoseStack)args[0]).last().pose()), (List<BlockStateModelPart>)args[2]));
                    return null;
                });
    }

    static void matrix(Minecraft client, BiConsumer<Boolean, String> check) throws ReflectiveOperationException {
        int samples = 0;
        for (var block : BuiltInRegistries.BLOCK.stream().filter(value -> value instanceof CandleSconceBlock).toList()) {
            CandleSconceBlock holder = (CandleSconceBlock)block;
            for (Direction facing : Direction.Plane.HORIZONTAL) for (int count = 1; count <= 4; count++)
                    for (boolean lit : new boolean[]{false, true}) for (int colour = 0; colour < ClientScene.TYPES.size(); colour++) {
                var blockState = block.defaultBlockState().setValue(FurnitureBlock.DIRECTION, facing)
                        .setValue(CandleSconceBlock.COUNT, count).setValue(CandleSconceBlock.LIT, lit);
                var storage = new CandleSconceBlockEntity(BlockPos.ZERO, blockState);
                for (int slot = 0; slot < count; slot++) storage.insert(ClientScene.candle(colour, slot));
                List<ItemStack> before = storage.candles();
                var renderer = (CandleSconceRenderer)(Object)client.getBlockEntityRenderDispatcher().getRenderer(storage);
                var state = renderer.createRenderState();
                renderer.extractRenderState(storage, state, 0, Vec3.ZERO, null);
                Capture capture = new Capture();
                PoseStack poses = new PoseStack();
                renderer.submit(state, poses, capture.collector, new CameraRenderState());
                check.accept(poses.last().pose().equals(new Matrix4f()), "Candle rendering restores the caller's pose");
                for (int slot = 0; slot < count; slot++) {
                    Draw draw = capture.draws.get(capture.draws.size() - count + slot);
                    Vector3f scale = draw.pose.getScale(new Vector3f());
                    check.accept(close(scale.x, .75) && close(scale.y, .75) && close(scale.z, .75),
                            "Sconce candles are three-quarter size, not full-size vanilla models");
                    Vec3 origin = holder.candleOrigin(blockState, slot, count);
                    Vector3f base = draw.pose.transformPosition(.5F, 0, .5F, new Vector3f());
                    Vector3f wick = draw.pose.transformPosition(.5F, 7F / 16F, .5F, new Vector3f());
                    check.accept(close(base.x, origin.x) && close(base.z, origin.z) && close(base.y, 10D / 16D),
                            "Each smaller candle sits on the dish rather than floating above or intersecting it");
                    Vec3 flame = holder.candleWick(blockState, slot, count);
                    check.accept(close(wick.x, flame.x) && close(wick.y, flame.y) && close(wick.z, flame.z),
                            "Placed flame and smoke originate at the scaled candle wick in every facing");
                    check.accept(ItemStack.matches(before.get(slot), storage.candles().get(slot)), "Rendering leaves complete stored candle items untouched");
                }
                BlockModelRenderState frame = new BlockModelRenderState();
                new net.minecraft.client.renderer.block.BlockModelResolver(client.getModelManager()).update(frame, blockState, BlockDisplayContext.create());
                frame.submit(poses, capture.collector, state.lightCoords, 0, 0);
                var centre = FurnitureBlock.rotate(holder.isWall() ? .25 : .5, .5, facing);
                check.accept(hasDish(capture, centre.getA(), centre.getB()),
                        "Every floor/wall metal candle holder has a four-pixel support plate beneath the candles");
                var shape = holder.getShape(blockState, client.level, BlockPos.ZERO, net.minecraft.world.phys.shapes.CollisionContext.empty());
                check.accept(shape.max(Direction.Axis.Y) <= 11D / 16D, "Selection and collision stop at the holder, not the candle tips");
                samples++;
            }
        }
        try {
            Class.forName("zone.moddev.mc.ironagefurniture.client.particle.HeldCandleEffects");
            check.accept(false, "Held candle effects are removed, not merely disabled by default");
        } catch (ClassNotFoundException expected) { check.accept(true, "IAF adds no held candle particles"); }
        System.out.println("IAF_CLIENT_CANDLE_GEOMETRY_PASS samples=" + samples);
    }

    private static boolean hasDish(Capture capture, double x, double z) {
        for (Draw draw : capture.draws) for (BlockStateModelPart part : draw.parts)
                for (Direction face : new Direction[]{null, Direction.UP, Direction.DOWN, Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST})
                for (var quad : part.getQuads(face)) {
            double minX = 100, minZ = 100, maxX = -100, maxZ = -100;
            boolean top = true;
            for (int vertex = 0; vertex < 4; vertex++) {
                Vector3f point = draw.pose.transformPosition(quad.position(vertex), new Vector3f());
                top &= close(point.y, 10D / 16D);
                minX = Math.min(minX, point.x); maxX = Math.max(maxX, point.x);
                minZ = Math.min(minZ, point.z); maxZ = Math.max(maxZ, point.z);
            }
            if (top && close(minX, x - .125) && close(maxX, x + .125)
                    && close(minZ, z - .125) && close(maxZ, z + .125)) return true;
        }
        return false;
    }
    private static boolean close(double first, double second) { return Math.abs(first - second) < .00001; }
    private CandleSconceRenderChecks() { }
}
