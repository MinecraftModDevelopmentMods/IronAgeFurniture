package zone.moddev.mc.ironagefurniture.fixture;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import zone.moddev.mc.ironagefurniture.api.UpholsteryItemData;
import zone.moddev.mc.ironagefurniture.api.blocks.furniture.FurnitureBed;
import zone.moddev.mc.ironagefurniture.api.blocks.furniture.MultiBlockChair;
import zone.moddev.mc.ironagefurniture.api.blocks.furniture.ShieldChair;
import zone.moddev.mc.ironagefurniture.api.enumerations.UpholsteryColour;
import zone.moddev.mc.ironagefurniture.api.tile.ShieldChairBlockEntity;

final class FurnitureProbe {
    static void run(ServerLevel level, BiConsumer<Boolean, String> check) {
        BlockPos base = new BlockPos(-16, 110, -16);
        List<Block> furniture = BuiltInRegistries.BLOCK.stream().filter(block -> block instanceof FurnitureBed bed && bed.itemBlock() == block || block instanceof MultiBlockChair).toList();
        check.accept(furniture.size() >= 72, "All core Phase 4 furniture families are registered");
        for (Block block : furniture) for (UpholsteryColour colour : UpholsteryColour.values()) for (Direction facing : Direction.Plane.HORIZONTAL) {
            BlockState state = block.defaultBlockState().setValue(FurnitureBed.DIRECTION, facing).setValue(UpholsteryItemData.COLOUR, colour);
            List<BlockPos> parts = new ArrayList<>();
            if (block instanceof FurnitureBed bed) parts.addAll(bed.structure(base, state).keySet());
            else for (int offset = 0; offset < ((MultiBlockChair)block).getChairHeight(); offset++) parts.add(base.above(offset));
            for (BlockPos broken : parts) {
                for (BlockPos part : parts) level.setBlock(part.below(), Blocks.STONE.defaultBlockState(), Block.UPDATE_CLIENTS);
                level.setBlock(base, state, Block.UPDATE_CLIENTS);
                block.setPlacedBy(level, base, state, null, UpholsteryItemData.create(block, colour));
                for (BlockPos part : parts) {
                    BlockState placed = level.getBlockState(part);
                    check.accept(placed.getBlock() instanceof FurnitureBed || placed.getBlock() instanceof MultiBlockChair, "Every structure part is present");
                    check.accept(placed.getValue(UpholsteryItemData.COLOUR) == colour && placed.getValue(FurnitureBed.DIRECTION) == facing, "Every part retains colour and facing");
                }
                level.destroyBlock(broken, true);
                for (BlockPos part : parts) check.accept(!(level.getBlockState(part).getBlock() instanceof FurnitureBed)
                        && !(level.getBlockState(part).getBlock() instanceof MultiBlockChair), "Breaking any part removes the complete structure");
                List<ItemEntity> drops = level.getEntitiesOfClass(ItemEntity.class, new AABB(base).inflate(5));
                check.accept(drops.stream().mapToInt(item -> item.getItem().getCount()).sum() == 1, "A multiblock drops exactly one item: "
                        + state + " broken=" + broken + " drops=" + drops.stream().map(ItemEntity::getItem).toList());
                check.accept(drops.size() == 1 && ItemStack.isSameItemSameComponents(drops.getFirst().getItem(), UpholsteryItemData.create(block, colour)), "The dropped furniture has the original flat colour identity");
                drops.forEach(ItemEntity::discard);
                for (BlockPos part : parts) level.setBlock(part.below(), Blocks.AIR.defaultBlockState(), Block.UPDATE_CLIENTS);
            }
        }
        for (Block block : BuiltInRegistries.BLOCK.stream().filter(value -> value instanceof ShieldChair).toList()) {
            BlockState state = block.defaultBlockState().setValue(FurnitureBed.DIRECTION, Direction.NORTH);
            level.setBlock(base.below(), Blocks.STONE.defaultBlockState(), Block.UPDATE_CLIENTS);
            level.setBlockAndUpdate(base, state);
            ItemStack shield = new ItemStack(Items.SHIELD);
            shield.set(DataComponents.CUSTOM_NAME, Component.literal("Kept shield")); shield.setDamageValue(53);
            ShieldChairBlockEntity storage = (ShieldChairBlockEntity)level.getBlockEntity(base);
            storage.setShield(shield);
            level.destroyBlock(base, true);
            List<ItemEntity> drops = level.getEntitiesOfClass(ItemEntity.class, new AABB(base).inflate(5));
            check.accept(drops.size() == 2, "A shield chair releases one frame and its stored shield");
            check.accept(drops.stream().anyMatch(item -> ItemStack.isSameItemSameComponents(item.getItem(), shield)), "The original shield components survive destruction");
            drops.forEach(ItemEntity::discard);
            level.setBlock(base.below(), Blocks.AIR.defaultBlockState(), Block.UPDATE_CLIENTS);
        }
    }
    private FurnitureProbe() { }
}
