package zone.moddev.mc.ironagefurniture.fixture;

import java.util.List;
import java.util.function.BiConsumer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import zone.moddev.mc.ironagefurniture.api.SconceMetalData;
import zone.moddev.mc.ironagefurniture.api.blocks.base.FurnitureBlock;
import zone.moddev.mc.ironagefurniture.api.blocks.lightsource.phasefour.CandleSconceBlock;
import zone.moddev.mc.ironagefurniture.api.blocks.lightsource.phasefour.TwinTorchSconce;
import zone.moddev.mc.ironagefurniture.api.enumerations.SconceMetal;
import zone.moddev.mc.ironagefurniture.api.tile.CandleSconceBlockEntity;
import zone.moddev.mc.ironagefurniture.init.ModVanillaLights;
import zone.moddev.mc.ironagefurniture.init.PhaseFourLighting;

final class LightingProbe {
    static void run(ServerLevel level, BiConsumer<Boolean, String> check) {
        BlockPos pos = new BlockPos(16, 100, 16);
        for (SconceMetal metal : SconceMetal.values()) for (boolean wall : new boolean[]{false, true}) for (Direction facing : Direction.Plane.HORIZONTAL) {
            BlockPos support = wall ? pos.relative(facing.getOpposite()) : pos.below();
            level.setBlockAndUpdate(support, Blocks.STONE.defaultBlockState());
            Block[] blocks = {wall ? ModVanillaLights.light_metal_ironage_sconce_wall_torch_iron.get() : ModVanillaLights.light_metal_ironage_sconce_floor_torch_iron.get(),
                    PhaseFourLighting.twin(wall, true), wall ? PhaseFourLighting.WALL_CANDLES.get() : PhaseFourLighting.FLOOR_CANDLES.get()};
            for (Block block : blocks) {
                BlockState state = SconceMetalData.withMetal(block.defaultBlockState().setValue(FurnitureBlock.DIRECTION, facing), metal).setValue(FurnitureBlock.WATERLOGGED, false);
                level.setBlockAndUpdate(pos, state);
                if (level.getBlockEntity(pos) instanceof CandleSconceBlockEntity storage) {
                    check.accept(storage.insert(new ItemStack(Items.CANDLE)), "Insert first candle");
                    check.accept(!level.getBlockState(pos).getValue(CandleSconceBlock.LIT), "First candle is unlit");
                    state = level.getBlockState(pos).setValue(CandleSconceBlock.LIT, true);
                    level.setBlockAndUpdate(pos, state);
                    for (int slot = 1; slot < 4; slot++) check.accept(storage.insert(new ItemStack(Items.CANDLE)), "Insert remaining candles");
                    check.accept(level.getBlockState(pos).getValue(CandleSconceBlock.LIT), "Insertion preserves flame");
                }
                state = level.getBlockState(pos);
                check.accept(!state.getShape(level, pos).isEmpty(), "Hit particles have nonempty bounds");
                check.accept(((SimpleWaterloggedBlock)block).placeLiquid(level, pos, state, net.minecraft.world.level.material.Fluids.WATER.getSource(false)), "Water enters holder: " + state);
                state = level.getBlockState(pos);
                check.accept(!lit(state), "Water extinguishes light");
                level.setBlockAndUpdate(pos.above(), Blocks.REDSTONE_BLOCK.defaultBlockState());
                state.tick(level, pos, level.getRandom());
                state = level.getBlockState(pos);
                check.accept(lit(state), "Power lights an underwater holder");
                check.accept(SconceMetalData.get(state) == metal, "Relighting preserves metal");
                level.setBlockAndUpdate(pos.above(), Blocks.AIR.defaultBlockState());
                state.tick(level, pos, level.getRandom());
                state = level.getBlockState(pos);
                check.accept(!lit(state), "Removing underwater power extinguishes light");
                check.accept(SconceMetalData.get(state) == metal && state.getValue(FurnitureBlock.DIRECTION) == facing, "Extinguishing preserves material and orientation");
                check.accept(level.getEntitiesOfClass(ItemEntity.class, new AABB(pos).inflate(3)).isEmpty(), "Lighting changes never drop contents");
                level.setBlockAndUpdate(support, Blocks.AIR.defaultBlockState());
                check.accept(level.getBlockState(pos).getBlock() != block && !(level.getBlockState(pos).getBlock() instanceof TwinTorchSconce), "Support loss removes the holder");
                List<ItemEntity> drops = level.getEntitiesOfClass(ItemEntity.class, new AABB(pos).inflate(3));
                ItemStack expectedFrame = SconceMetalData.frame(state);
                check.accept(drops.stream().filter(item -> item.getItem().is(expectedFrame.getItem())).mapToInt(item -> item.getItem().getCount()).sum() == 1, "One correct-metal frame drops");
                int expectedContents = block instanceof CandleSconceBlock ? 4 : block instanceof TwinTorchSconce ? 2 : 1;
                check.accept(drops.stream().mapToInt(item -> item.getItem().getCount()).sum() == expectedContents + 1, "All stored lights survive support loss");
                drops.forEach(ItemEntity::discard);
                level.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
                level.setBlockAndUpdate(support, Blocks.STONE.defaultBlockState());
            }
            level.setBlockAndUpdate(support, Blocks.AIR.defaultBlockState());
        }
        redstoneMaterials(level, check);
        LavaLifecycleProbe.run(level, check);
    }
    private static void redstoneMaterials(ServerLevel level, BiConsumer<Boolean, String> check) {
        BlockPos pos = new BlockPos(24, 110, 24);
        for (SconceMetal metal : SconceMetal.values()) for (boolean wall : new boolean[]{false, true})
                for (Direction facing : Direction.Plane.HORIZONTAL) {
            BlockPos support = wall ? pos.relative(facing.getOpposite()) : pos.below();
            Block[] lights = {wall ? ModVanillaLights.light_metal_ironage_sconce_wall_red_iron.get()
                    : ModVanillaLights.light_metal_ironage_sconce_floor_red_iron.get(),
                    wall ? ModVanillaLights.light_metal_ironage_sconce_wall_redtorch_iron.get()
                    : ModVanillaLights.light_metal_ironage_sconce_floor_redtorch_iron.get()};
            for (Block light : lights) {
                level.setBlockAndUpdate(support, Blocks.STONE.defaultBlockState());
                BlockState original = SconceMetalData.withMetal(light.defaultBlockState().setValue(FurnitureBlock.DIRECTION, facing)
                        , metal).setValue(FurnitureBlock.WATERLOGGED, false);
                level.setBlockAndUpdate(pos, original);
                level.setBlockAndUpdate(support, Blocks.REDSTONE_BLOCK.defaultBlockState());
                level.getBlockState(pos).tick(level, pos, level.getRandom());
                BlockState powered = level.getBlockState(pos);
                check.accept(powered.getBlock() != light, "The powered red light changes state");
                check.accept(SconceMetalData.get(powered) == metal && powered.getValue(FurnitureBlock.DIRECTION) == facing,
                        "Power changes preserve red-light metal and facing");
                level.setBlockAndUpdate(support, Blocks.STONE.defaultBlockState());
                powered.tick(level, pos, level.getRandom());
                BlockState restored = level.getBlockState(pos);
                check.accept(SconceMetalData.get(restored) == metal && restored.getValue(FurnitureBlock.DIRECTION) == facing,
                        "Removing power preserves red-light metal and facing");
                level.destroyBlock(pos, true);
                List<ItemEntity> drops = level.getEntitiesOfClass(ItemEntity.class, new AABB(pos).inflate(3));
                ItemStack frame = SconceMetalData.frame(restored);
                check.accept(drops.stream().filter(item -> item.getItem().is(frame.getItem())).mapToInt(item -> item.getItem().getCount()).sum() == 1,
                        "Red light destruction releases the original metal frame");
                check.accept(drops.stream().mapToInt(item -> item.getItem().getCount()).sum() == 2,
                        "Red light destruction releases both frame and contents");
                drops.forEach(ItemEntity::discard);
                level.setBlockAndUpdate(support, Blocks.AIR.defaultBlockState());
            }
        }
    }
    private static boolean lit(BlockState state) {
        if (state.hasProperty(CandleSconceBlock.LIT)) return state.getValue(CandleSconceBlock.LIT);
        return !net.minecraft.core.registries.BuiltInRegistries.BLOCK.getKey(state.getBlock()).getPath().endsWith("_unlit");
    }
    private LightingProbe() { }
}
