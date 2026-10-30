package zone.moddev.mc.ironagefurniture.api;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;

/** Native nested item NBT keeps the shield's complete saved details. */
public final class ShieldChairItemData {
    public static ItemStack getShield(ItemStack chair) {
        CompoundTag data = chair.getTag();
        if (data == null || !data.contains("Shield")) return new ItemStack(Items.SHIELD);
        return ItemStack.of(data.getCompound("Shield"));
    }
    public static ItemStack createChair(Block chair, ItemStack shield) { return withShield(new ItemStack(chair), shield); }
    public static ItemStack withShield(ItemStack chair, ItemStack shield) {
        chair.getOrCreateTag().put("Shield", shield.isEmpty() ? new CompoundTag() : shield.copyWithCount(1).save(new CompoundTag()));
        chair.getOrCreateTag().putInt("IronAgeFurnitureShieldVersion", net.minecraft.SharedConstants.getCurrentVersion().getDataVersion().getVersion());
        return chair;
    }
    private ShieldChairItemData() { }
}
