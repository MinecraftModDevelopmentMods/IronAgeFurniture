package zone.moddev.mc.ironagefurniture.api;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTDynamicOps;
import com.mojang.datafixers.Dynamic;
import net.minecraft.util.SharedConstants;
import net.minecraft.util.datafix.DataFixesManager;
import net.minecraft.util.datafix.TypeReferences;

/** The chair stores a whole shield stack, not just its banner pattern. */
public final class ShieldChairItemData {
    public static final String CHAIR_TAG = "IAFShieldChair";
    public static final String EMPTY_TAG = "Empty";
    public static final String SHIELD_TAG = "Shield";

    private ShieldChairItemData() { }

    public static boolean isEmptyFrame(ItemStack chair) {
        return chair.hasTag() && chair.getTag().contains(CHAIR_TAG, 10)
                && chair.getTag().getCompound(CHAIR_TAG).getBoolean(EMPTY_TAG);
    }

    public static ItemStack getShield(ItemStack chair) {
        if (chair.hasTag() && chair.getTag().contains(CHAIR_TAG, 10)) {
            return readShield(chair.getTag().getCompound(CHAIR_TAG));
        }
        // Before removable shields, every shield chair had a plain wooden shield.
        return new ItemStack(Items.SHIELD);
    }

    public static ItemStack readShield(CompoundNBT data) {
        if (data.getBoolean(EMPTY_TAG)) return ItemStack.EMPTY;
        if (data.contains(SHIELD_TAG, 10)) {
            CompoundNBT saved = data.getCompound(SHIELD_TAG).copy();
            // Vanilla's fixer does not visit a mod's custom nested item field.
            // A root Damage field identifies the pre-flattening item format.
            if (saved.contains("Damage", 99)) {
                saved = (CompoundNBT) DataFixesManager.getDataFixer().update(TypeReferences.ITEM_STACK,
                        new Dynamic<>(NBTDynamicOps.INSTANCE, saved), 1343,
                        SharedConstants.getVersion().getWorldVersion()).getValue();
            }
            ItemStack shield = ItemStack.read(saved);
            if (!shield.isEmpty() && shield.getItem() == Items.SHIELD) {
                shield.setCount(1);
                return shield;
            }
        }
        return new ItemStack(Items.SHIELD);
    }

    public static CompoundNBT writeShield(ItemStack shield) {
        CompoundNBT data = new CompoundNBT();
        if (shield == null || shield.isEmpty() || shield.getItem() != Items.SHIELD) {
            data.putBoolean(EMPTY_TAG, true);
        } else {
            ItemStack single = shield.copy();
            single.setCount(1);
            data.put(SHIELD_TAG, single.write(new CompoundNBT()));
        }
        return data;
    }

    public static ItemStack createChair(Block chair, ItemStack shield) {
        ItemStack result = new ItemStack(chair);
        result.getOrCreateTag().put(CHAIR_TAG, writeShield(shield));
        return result;
    }
}
