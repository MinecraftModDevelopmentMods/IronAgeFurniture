package zone.moddev.mc.ironagefurniture.api;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.CandleBlock;

/** Four exact stacks in insertion order; callers never receive mutable storage. */
public final class CandleInventory {
    public static final int CAPACITY = 4;
    private final List<ItemStack> items = new ArrayList<>(CAPACITY);

    public static boolean accepts(ItemStack stack) {
        return stack.getItem() instanceof BlockItem item && item.getBlock() instanceof CandleBlock
                && BuiltInRegistries.ITEM.getKey(item).getNamespace().equals("minecraft");
    }
    public int size() { return items.size(); }
    public boolean insert(ItemStack stack) {
        if (!accepts(stack) || items.size() >= CAPACITY) return false;
        items.add(stack.copyWithCount(1));
        return true;
    }
    public ItemStack removeNewest() { return items.isEmpty() ? ItemStack.EMPTY : items.removeLast(); }
    public List<ItemStack> snapshot() { return items.stream().map(ItemStack::copy).toList(); }
    public List<ItemStack> drain() {
        List<ItemStack> result = snapshot();
        items.clear();
        return result;
    }
    public void restore(List<ItemStack> saved) {
        if (saved.size() > CAPACITY || saved.stream().anyMatch(stack -> !accepts(stack) || stack.getCount() != 1))
            throw new IllegalArgumentException("A candle sconce stores up to four individual vanilla candles");
        items.clear();
        saved.forEach(stack -> items.add(stack.copy()));
    }
}
