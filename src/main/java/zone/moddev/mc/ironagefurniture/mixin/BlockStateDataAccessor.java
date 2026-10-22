package zone.moddev.mc.ironagefurniture.mixin;

import com.mojang.serialization.Dynamic;
import net.minecraft.util.datafix.fixes.BlockStateData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(BlockStateData.class)
public interface BlockStateDataAccessor {
    @Accessor("MAP") static Dynamic<?>[] iaf$getLegacyStateMap() { throw new AssertionError("Legacy state accessor was not applied"); }
}
