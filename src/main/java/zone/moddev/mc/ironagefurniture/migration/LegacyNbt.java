package zone.moddev.mc.ironagefurniture.migration;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;

/** Native 1.21.1 NBT reads with explicit legacy fallback and list-type semantics. */
final class LegacyNbt {
    static CompoundTag compound(CompoundTag tag, String key) { return tag.getCompound(key); }
    static CompoundTag compound(ListTag list, int index) { return list.getCompound(index); }
    static ListTag list(CompoundTag tag, String key) {
        return tag.get(key) instanceof ListTag list ? list : new ListTag();
    }
    static String string(CompoundTag tag, String key, String fallback) {
        return tag.contains(key, Tag.TAG_STRING) ? tag.getString(key) : fallback;
    }
    static String string(ListTag list, int index, String fallback) {
        return index >= 0 && index < list.size() && list.get(index) instanceof net.minecraft.nbt.StringTag
                ? list.getString(index) : fallback;
    }
    static int integer(CompoundTag tag, String key, int fallback) {
        return tag.contains(key, Tag.TAG_ANY_NUMERIC) ? tag.getInt(key) : fallback;
    }
    static byte byteValue(CompoundTag tag, String key, byte fallback) {
        return tag.contains(key, Tag.TAG_ANY_NUMERIC) ? tag.getByte(key) : fallback;
    }
    static boolean bool(CompoundTag tag, String key, boolean fallback) {
        return tag.contains(key, Tag.TAG_ANY_NUMERIC) ? tag.getBoolean(key) : fallback;
    }
    private LegacyNbt() { }
}
