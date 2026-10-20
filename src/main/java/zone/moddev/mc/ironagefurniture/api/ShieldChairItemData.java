package zone.moddev.mc.ironagefurniture.api;

import java.util.List;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.level.block.Block;

/** Native nested-stack components preserve every part of the removable shield. */
public final class ShieldChairItemData {
    public static ItemStack getShield(ItemStack chair) {
        ItemContainerContents contents = chair.get(DataComponents.CONTAINER);
        if (contents == null) return new ItemStack(Items.SHIELD);
        return contents.nonEmptyStream().findFirst().orElse(ItemStack.EMPTY);
    }
    public static ItemStack createChair(Block chair, ItemStack shield) {
        return withShield(new ItemStack(chair), shield);
    }
    public static ItemStack withShield(ItemStack chair, ItemStack shield) {
        chair.set(DataComponents.CONTAINER, ItemContainerContents.fromItems(shield.isEmpty() ? List.of() : List.of(shield.copyWithCount(1))));
        return chair;
    }
    private ShieldChairItemData() { }
}
