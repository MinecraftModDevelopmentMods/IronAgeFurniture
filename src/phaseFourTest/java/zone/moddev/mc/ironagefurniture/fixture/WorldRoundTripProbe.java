package zone.moddev.mc.ironagefurniture.fixture;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.function.BiConsumer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtAccounter;
import net.minecraft.nbt.NbtIo;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import zone.moddev.mc.ironagefurniture.api.CandleInventory;
import zone.moddev.mc.ironagefurniture.api.SconceMetalData;
import zone.moddev.mc.ironagefurniture.api.UpholsteryItemData;
import zone.moddev.mc.ironagefurniture.api.blocks.base.FurnitureBlock;
import zone.moddev.mc.ironagefurniture.api.blocks.furniture.FurnitureBed;
import zone.moddev.mc.ironagefurniture.api.blocks.furniture.ShieldChair;
import zone.moddev.mc.ironagefurniture.api.blocks.lightsource.phasefour.CandleSconceBlock;
import zone.moddev.mc.ironagefurniture.api.enumerations.SconceMetal;
import zone.moddev.mc.ironagefurniture.api.enumerations.UpholsteryColour;
import zone.moddev.mc.ironagefurniture.api.tile.CandleSconceBlockEntity;
import zone.moddev.mc.ironagefurniture.api.tile.ShieldChairBlockEntity;
import zone.moddev.mc.ironagefurniture.init.PhaseFourLighting;

/** A separate-process disk round trip, not just a codec round trip in memory. */
final class WorldRoundTripProbe {
    private static final Path EXPECTED = Path.of("native-roundtrip-expectations.nbt");
    static void run(ServerLevel level, BiConsumer<Boolean, String> check) throws Exception {
        if (Files.exists(EXPECTED)) { verify(level, check); return; }
        ListTag records = new ListTag();
        List<ItemStack> colours = BuiltInRegistries.ITEM.stream().map(ItemStack::new).filter(CandleInventory::accepts).toList();
        int index = 0;
        for (SconceMetal metal : SconceMetal.values()) for (boolean wall : new boolean[]{false, true})
                for (Direction facing : Direction.Plane.HORIZONTAL) for (int colour = 0; colour < colours.size(); colour++) {
            BlockPos pos = position(index++);
            level.setBlockAndUpdate(wall ? pos.relative(facing.getOpposite()) : pos.below(), Blocks.STONE.defaultBlockState());
            BlockState state = (wall ? PhaseFourLighting.WALL_CANDLES.get() : PhaseFourLighting.FLOOR_CANDLES.get()).defaultBlockState()
                    .setValue(FurnitureBlock.DIRECTION, facing).setValue(SconceMetalData.METAL, metal);
            level.setBlockAndUpdate(pos, state);
            CandleSconceBlockEntity storage = (CandleSconceBlockEntity)level.getBlockEntity(pos);
            for (int slot = 0; slot < 4; slot++) {
                ItemStack candle = decorated(colours.get((colour + slot) % colours.size()), colour * 4 + slot);
                check.accept(storage.insert(candle), "Round-trip fixture accepts each component-bearing candle");
            }
            boolean wet = (colour & 1) == 0;
            boolean lit = (colour & 2) == 0;
            if (wet && lit) level.setBlockAndUpdate(pos.above(), Blocks.REDSTONE_BLOCK.defaultBlockState());
            level.setBlockAndUpdate(pos, level.getBlockState(pos).setValue(CandleSconceBlock.LIT, lit).setValue(FurnitureBlock.WATERLOGGED, wet));
            records.add(record(level, pos));
        }
        for (Block block : BuiltInRegistries.BLOCK.stream().filter(value -> value instanceof ShieldChair).toList())
                for (Direction facing : Direction.Plane.HORIZONTAL) {
            BlockPos pos = position(index++);
            level.setBlockAndUpdate(pos.below(), Blocks.STONE.defaultBlockState());
            level.setBlockAndUpdate(pos, block.defaultBlockState().setValue(FurnitureBlock.DIRECTION, facing));
            ItemStack shield = decorated(new ItemStack(Items.SHIELD), index); shield.setDamageValue(index % 300);
            ((ShieldChairBlockEntity)level.getBlockEntity(pos)).setShield(shield);
            records.add(record(level, pos));
        }
        for (Block block : BuiltInRegistries.BLOCK.stream().filter(value -> value instanceof FurnitureBed bed && bed.itemBlock() == value).toList())
                for (Direction facing : Direction.Plane.HORIZONTAL) {
            BlockPos pos = position(index++);
            UpholsteryColour colour = UpholsteryColour.values()[index % 16];
            BlockState state = block.defaultBlockState().setValue(FurnitureBed.DIRECTION, facing).setValue(UpholsteryItemData.COLOUR, colour);
            var parts = ((FurnitureBed)block).structure(pos, state);
            for (BlockPos part : parts.keySet()) level.setBlock(part.below(), Blocks.STONE.defaultBlockState(), Block.UPDATE_CLIENTS);
            level.setBlock(pos, state, Block.UPDATE_CLIENTS);
            block.setPlacedBy(level, pos, state, null, UpholsteryItemData.create(block, colour));
            for (BlockPos part : parts.keySet()) records.add(record(level, part));
        }
        CompoundTag root = new CompoundTag(); root.put("Blocks", records);
        try (var output = Files.newOutputStream(EXPECTED)) { NbtIo.writeCompressed(root, output); }
        verify(level, check);
        System.out.println("IAF_ROUNDTRIP_SEEDED blocks=" + records.size());
    }
    private static ItemStack decorated(ItemStack source, int index) {
        ItemStack stack = source.copyWithCount(1);
        stack.set(DataComponents.CUSTOM_NAME, Component.literal("Disk round trip " + index));
        stack.set(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, (index & 1) == 0);
        CompoundTag custom = new CompoundTag(); custom.putLong("Long", 123456789012345L); custom.putByte("Byte", (byte)7);
        custom.putString("Text", "Retain the complete component payload"); stack.set(DataComponents.CUSTOM_DATA, CustomData.of(custom));
        return stack;
    }
    private static BlockPos position(int index) { return new BlockPos((index % 64) * 5, 180, (index / 64) * 5); }
    private static CompoundTag record(ServerLevel level, BlockPos pos) {
        CompoundTag record = new CompoundTag(); record.putInt("X", pos.getX()); record.putInt("Y", pos.getY()); record.putInt("Z", pos.getZ());
        record.put("State", BlockState.CODEC.encodeStart(NbtOps.INSTANCE, level.getBlockState(pos)).getOrThrow());
        BlockEntity storage = level.getBlockEntity(pos);
        if (storage != null) record.put("Contents", storage.saveWithoutMetadata(level.registryAccess()));
        return record;
    }
    private static void verify(ServerLevel level, BiConsumer<Boolean, String> check) throws Exception {
        CompoundTag root;
        try (var input = Files.newInputStream(EXPECTED)) { root = NbtIo.readCompressed(input, NbtAccounter.unlimitedHeap()); }
        ListTag records = root.getListOrEmpty("Blocks");
        check.accept(records.size() > 4000, "Disk-round-trip expectations are complete");
        for (int index = 0; index < records.size(); index++) {
            CompoundTag expected = records.getCompoundOrEmpty(index);
            BlockPos pos = new BlockPos(expected.getIntOr("X", 0), expected.getIntOr("Y", 0), expected.getIntOr("Z", 0));
            check.accept(expected.equals(record(level, pos)), "Exact state and complete stored components survive disk reload at " + pos);
        }
        System.out.println("IAF_ROUNDTRIP_PASS blocks=" + records.size());
    }
    private WorldRoundTripProbe() { }
}
