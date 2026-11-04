package zone.moddev.mc.ironagefurniture.api;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;

/** Saves complete native block entity data without a placed identity or coordinates. */
public final class NativeBlockEntityData {
    private NativeBlockEntityData() { }
    public static CompoundTag saveWithoutMetadata(BlockEntity entity) {
        CompoundTag tag = entity.save(new CompoundTag());
        tag.remove("id"); tag.remove("x"); tag.remove("y"); tag.remove("z");
        return tag;
    }
}
