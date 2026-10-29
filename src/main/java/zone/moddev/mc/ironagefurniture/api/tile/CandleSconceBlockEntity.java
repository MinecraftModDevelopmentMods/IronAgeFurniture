package zone.moddev.mc.ironagefurniture.api.tile;

import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import zone.moddev.mc.ironagefurniture.api.CandleInventory;
import zone.moddev.mc.ironagefurniture.api.blocks.lightsource.phasefour.CandleSconceBlock;
import zone.moddev.mc.ironagefurniture.init.PhaseFourLighting;

/** No ticker or external inventory capability: candles are handled by direct clicks. */
public final class CandleSconceBlockEntity extends BlockEntity {
    private final CandleInventory candles = new CandleInventory();
    public CandleSconceBlockEntity(BlockPos pos, BlockState state) { super(PhaseFourLighting.CANDLE_STORAGE.get(), pos, state); }
    public List<ItemStack> candles() { return candles.snapshot(); }
    public boolean insert(ItemStack stack) {
        if (!candles.insert(stack)) return false;
        contentsChanged();
        return true;
    }
    public ItemStack removeNewest() {
        ItemStack result = candles.removeNewest();
        if (!result.isEmpty()) contentsChanged();
        return result;
    }
    private void contentsChanged() {
        setChanged();
        if (level == null || level.isClientSide()) return;
        BlockState state = getBlockState();
        if (!candles.snapshot().isEmpty()) level.setBlock(worldPosition, state.setValue(CandleSconceBlock.COUNT, candles.size()), Block.UPDATE_ALL);
        level.sendBlockUpdated(worldPosition, state, level.getBlockState(worldPosition), Block.UPDATE_CLIENTS);
    }
    @Override protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        candles.restore(tag.contains("Candles") ? ItemStack.CODEC.listOf()
                .parse(registries.createSerializationContext(NbtOps.INSTANCE), tag.get("Candles"))
                .result().orElse(List.of()) : List.of());
    }
    @Override protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.put("Candles", ItemStack.CODEC.listOf()
                .encodeStart(registries.createSerializationContext(NbtOps.INSTANCE), candles.snapshot()).getOrThrow());
    }
    @Override public CompoundTag getUpdateTag(HolderLookup.Provider registries) { return saveCustomOnly(registries); }
    @Override public ClientboundBlockEntityDataPacket getUpdatePacket() { return ClientboundBlockEntityDataPacket.create(this); }

    /** Native removal runs before the superclass discards this block entity. */
    public void dropCandles() {
        List<ItemStack> stored = candles.drain();
        if (level != null && !level.isClientSide()) for (ItemStack stack : stored) Block.popResource(level, worldPosition, stack);
    }
    public void clearCandles() { candles.drain(); setChanged(); }
}
