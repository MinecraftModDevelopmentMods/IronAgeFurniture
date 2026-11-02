package zone.moddev.mc.ironagefurniture.api.tile;

import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
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
    @Override public void load(CompoundTag tag) {
        super.load(tag);
        java.util.ArrayList<ItemStack> saved = new java.util.ArrayList<>();
        for (net.minecraft.nbt.Tag entry : tag.getList("Candles", net.minecraft.nbt.Tag.TAG_COMPOUND))
            saved.add(ItemStack.of((CompoundTag)entry));
        candles.restore(saved);
    }
    @Override protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        net.minecraft.nbt.ListTag saved = new net.minecraft.nbt.ListTag();
        for (ItemStack stack : candles.snapshot()) saved.add(stack.save(new CompoundTag()));
        tag.put("Candles", saved);
    }
    @Override public CompoundTag getUpdateTag() { return saveWithoutMetadata(); }
    @Override public ClientboundBlockEntityDataPacket getUpdatePacket() { return ClientboundBlockEntityDataPacket.create(this); }

    /** Native removal runs before the superclass discards this block entity. */
    public void dropCandles() {
        List<ItemStack> stored = candles.drain();
        if (level != null && !level.isClientSide()) for (ItemStack stack : stored) Block.popResource(level, worldPosition, stack);
    }
    public void clearCandles() { candles.drain(); setChanged(); }
}
