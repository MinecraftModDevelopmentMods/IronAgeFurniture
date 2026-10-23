package zone.moddev.mc.ironagefurniture.api.tile;

import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
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
    @Override protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        candles.restore(input.read("Candles", ItemStack.CODEC.listOf()).orElse(List.of()));
    }
    @Override protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        output.store("Candles", ItemStack.CODEC.listOf(), candles.snapshot());
    }
    @Override public CompoundTag getUpdateTag(HolderLookup.Provider registries) { return saveCustomOnly(registries); }
    @Override public ClientboundBlockEntityDataPacket getUpdatePacket() { return ClientboundBlockEntityDataPacket.create(this); }
    @Override public void preRemoveSideEffects(BlockPos pos, BlockState state) {
        // This hook runs before chunk removal discards the block entity. The
        // loot table drops only the frame, so contents cannot be dropped twice.
        if (level != null && !level.isClientSide()) for (ItemStack stack : candles.drain()) Block.popResource(level, pos, stack);
        super.preRemoveSideEffects(pos, state);
    }
}
