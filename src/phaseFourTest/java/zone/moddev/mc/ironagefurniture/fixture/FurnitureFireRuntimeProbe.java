package zone.moddev.mc.ironagefurniture.fixture;

import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FireBlock;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.registries.ForgeRegistries;
import zone.moddev.mc.ironagefurniture.api.UpholsteryItemData;
import zone.moddev.mc.ironagefurniture.api.blocks.furniture.Chair;
import zone.moddev.mc.ironagefurniture.api.blocks.furniture.FurnitureBed;
import zone.moddev.mc.ironagefurniture.api.blocks.furniture.MultiBlockChair;
import zone.moddev.mc.ironagefurniture.api.enumerations.UpholsteryColour;
import zone.moddev.mc.ironagefurniture.api.items.UpholsteredBlockItem;

/** Calls the game's actual fire-harvesting path for complete furniture structures. */
final class FurnitureFireRuntimeProbe {
    private FurnitureFireRuntimeProbe() { }

    static int run(ServerWorld world) throws Exception {
        Method catchFire = null;
        Class<?>[] arguments = {World.class, BlockPos.class, int.class, Random.class, int.class, Direction.class};
        for (Method method : FireBlock.class.getDeclaredMethods())
            if (java.util.Arrays.equals(method.getParameterTypes(), arguments)) catchFire = method;
        require(catchFire != null, "Could not locate the Forge fire-consumption method");
        catchFire.setAccessible(true);
        BlockPos base = new BlockPos(192, 80, 192);
        int cases = 0;
        for (Block block : ForgeRegistries.BLOCKS.getValues()) {
            if (!(block.asItem() instanceof UpholsteredBlockItem)) continue;
            boolean fireproof = block.getRegistryName().getPath().endsWith("_warped")
                    || block.getRegistryName().getPath().endsWith("_crimson");
            for (UpholsteryColour colour : UpholsteryColour.values()) {
                BlockState state = block.defaultBlockState().setValue(UpholsteryItemData.COLOUR, colour)
                        .setValue(Chair.DIRECTION, Direction.NORTH).setValue(Chair.WATERLOGGED, false);
                Map<BlockPos, BlockState> parts = new LinkedHashMap<>();
                if (block instanceof FurnitureBed) parts.putAll(((FurnitureBed)block).structure(base, state));
                else {
                    MultiBlockChair chair = (MultiBlockChair)block;
                    for (int height = 0; height < chair.getChairHeight(); height++)
                        parts.put(base.above(height), state.setValue(MultiBlockChair.PART, chair.partForOffset(height)));
                }
                parts.forEach((pos, part) -> {
                    if (!parts.containsKey(pos.below())) world.setBlock(pos.below(), Blocks.STONE.defaultBlockState(), 2);
                    world.setBlock(pos, part, 2);
                });
                for (Direction face : Direction.values()) {
                    require(block.isFlammable(state, world, base, face) == !fireproof
                            && block.getFlammability(state, world, base, face) == (fireproof ? 0 : 20)
                            && block.getFireSpreadSpeed(state, world, base, face) == (fireproof ? 0 : 5),
                            "Furniture does not match its wood's fire resistance: " + block);
                }
                Random consume = new Random(0) {
                    private int calls;
                    @Override public int nextInt(int bound) { return calls++ == 0 ? 0 : bound - 1; }
                };
                catchFire.invoke(Blocks.FIRE, world, base, 300, consume, 0, Direction.UP);
                for (BlockPos pos : parts.keySet()) require(fireproof
                        ? world.getBlockState(pos) == parts.get(pos) : world.getBlockState(pos).isAir(),
                        "Wrong furniture fire result: " + pos + " / " + block);
                parts.keySet().forEach(pos -> world.removeBlock(pos, false));
                cases++;
            }
        }
        require(cases >= 576, "Vanilla fire matrix is incomplete");
        return cases;
    }

    private static void require(boolean value, String message) { if (!value) throw new IllegalStateException(message); }
}
