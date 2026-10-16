package zone.moddev.mc.ironagefurniture.fixture;

import java.util.function.BiConsumer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.phys.AABB;
import zone.moddev.mc.ironagefurniture.api.SconceMetalData;
import zone.moddev.mc.ironagefurniture.api.blocks.base.FurnitureBlock;
import zone.moddev.mc.ironagefurniture.api.entity.ReleasedLavaLamp;
import zone.moddev.mc.ironagefurniture.api.enumerations.SconceMetal;
import zone.moddev.mc.ironagefurniture.init.ModVanillaLights;

/** Check support loss, redstone release and water independently of player mining. */
final class LavaLifecycleProbe {
    static void run(ServerLevel level, BiConsumer<Boolean, String> check) {
        BlockPos pos = new BlockPos(4, 160, 4);
        AABB area = new AABB(pos).inflate(5);
        for (SconceMetal metal : SconceMetal.values()) for (boolean wall : new boolean[]{false, true})
                for (Direction facing : Direction.Plane.HORIZONTAL) {
            var block = wall ? ModVanillaLights.light_metal_ironage_sconce_wall_lava_iron.get() : ModVanillaLights.light_metal_ironage_sconce_floor_lava_iron.get();
            var state = SconceMetalData.withMetal(block.defaultBlockState().setValue(FurnitureBlock.DIRECTION, facing), metal);
            BlockPos support = wall ? pos.relative(facing.getOpposite()) : pos.below();
            level.setBlockAndUpdate(pos.below(3), Blocks.STONE.defaultBlockState());
            level.setBlockAndUpdate(support, Blocks.STONE.defaultBlockState()); level.setBlockAndUpdate(pos, state);
            level.setBlockAndUpdate(support, Blocks.AIR.defaultBlockState());
            var drops = level.getEntitiesOfClass(ItemEntity.class, area);
            check.accept(drops.stream().filter(item -> item.getItem().is(SconceMetalData.frame(state).getItem())).mapToInt(item -> item.getItem().getCount()).sum() == 1,
                    "Support loss returns exactly one original-metal lava frame");
            check.accept(drops.stream().mapToInt(item -> item.getItem().getCount()).sum() == 1, "Support loss does not also duplicate an intact lamp");
            land(level, pos, check);
            drops.forEach(ItemEntity::discard); level.setBlockAndUpdate(pos.below(2), Blocks.AIR.defaultBlockState());

            level.setBlockAndUpdate(support, Blocks.STONE.defaultBlockState()); level.setBlockAndUpdate(pos, state);
            check.accept(((SimpleWaterloggedBlock)block).placeLiquid(level, pos, state, net.minecraft.world.level.material.Fluids.WATER.getSource(false)),
                    "Water enters a lava holder");
            var cooled = level.getBlockState(pos);
            check.accept(cooled.getBlock() == SconceMetalData.empty(state, wall).getBlock() && SconceMetalData.get(cooled) == metal
                    && cooled.getValue(FurnitureBlock.WATERLOGGED), "Lava/water conversion leaves the same wet metal frame");
            drops = level.getEntitiesOfClass(ItemEntity.class, area);
            check.accept(drops.size() == 1 && drops.getFirst().getItem().is(ModVanillaLights.obsidian_chunk.get().asItem()) && drops.getFirst().getItem().getCount() == 1,
                    "Lava/water conversion returns exactly one obsidian chunk");
            check.accept(level.getEntitiesOfClass(ReleasedLavaLamp.class, area).isEmpty(), "Water conversion does not also release a falling lamp");
            drops.forEach(ItemEntity::discard); level.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());

            if (wall) {
                level.setBlockAndUpdate(pos, state); level.setBlockAndUpdate(pos.above(), Blocks.REDSTONE_BLOCK.defaultBlockState());
                state.tick(level, pos, level.getRandom());
                check.accept(level.getBlockState(pos).getBlock() == SconceMetalData.empty(state, true).getBlock()
                        && SconceMetalData.get(level.getBlockState(pos)) == metal, "Redstone releases the wall lamp without losing its frame or metal");
                check.accept(level.getEntitiesOfClass(ItemEntity.class, area).isEmpty(), "Power does not drop another frame or an intact lamp");
                land(level, pos, check);
                level.setBlockAndUpdate(pos.above(), Blocks.AIR.defaultBlockState()); level.setBlockAndUpdate(pos.below(2), Blocks.AIR.defaultBlockState());
            }
            level.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState()); level.setBlockAndUpdate(support, Blocks.AIR.defaultBlockState());
            level.setBlockAndUpdate(pos.below(3), Blocks.AIR.defaultBlockState());
            level.getEntitiesOfClass(ItemEntity.class, area).forEach(ItemEntity::discard);
        }
    }
    private static void land(ServerLevel level, BlockPos pos, BiConsumer<Boolean, String> check) {
        var lamps = level.getEntitiesOfClass(ReleasedLavaLamp.class, new AABB(pos).inflate(5));
        check.accept(lamps.size() == 1, "Exactly one lamp falls");
        var lamp = lamps.getFirst();
        var saved = net.minecraft.world.level.storage.TagValueOutput.createWithContext(net.minecraft.util.ProblemReporter.DISCARDING, level.registryAccess());
        lamp.save(saved);
        lamp.discard();
        lamp = new ReleasedLavaLamp(zone.moddev.mc.ironagefurniture.api.entity.Entities.RELEASED_LAVA_LAMP.get(), level);
        lamp.load(net.minecraft.world.level.storage.TagValueInput.create(net.minecraft.util.ProblemReporter.DISCARDING, level.registryAccess(), saved.buildResult()));
        for (int tick = 0; tick < 80 && !lamp.isRemoved(); tick++) lamp.tick();
        check.accept(lamp.isRemoved() && level.getBlockState(pos.below(2)).is(Blocks.FIRE), "The saved/reloaded released lamp falls, shatters and ignites its landing");
    }
    private LavaLifecycleProbe() { }
}
