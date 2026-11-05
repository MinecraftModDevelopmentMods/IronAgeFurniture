package zone.moddev.mc.ironagefurniture.init;

import java.util.EnumMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import net.minecraft.block.Block;
import zone.moddev.mc.ironagefurniture.api.SconceMetalData;
import zone.moddev.mc.ironagefurniture.api.blocks.base.LightHolderSconce;
import zone.moddev.mc.ironagefurniture.api.enumerations.SconceMetal;

/** Registers a block for each metal while sharing the original sconce behaviour. */
public final class SconceVariants {
    private static final Map<String, Map<SconceMetal, Block>> FAMILIES = new LinkedHashMap<>();

    public static void register(Consumer<Block> register, String ironPath,
            Function<String, ? extends LightHolderSconce> factory) {
        Map<SconceMetal, Block> family = new EnumMap<>(SconceMetal.class);
        if (FAMILIES.putIfAbsent(ironPath, family) != null)
            throw new IllegalArgumentException("Duplicate sconce family: " + ironPath);
        for (SconceMetal metal : SconceMetal.values()) {
            String path = SconceMetalData.blockPath(ironPath, metal);
            Block block = factory.apply(path);
            family.put(metal, block);
            register.accept(block);
        }
    }

    public static Block material(Block block, SconceMetal metal) {
        if (block.getRegistryName() == null) return block;
        String path = block.getRegistryName().getPath();
        if (SconceMetalData.material(path) == metal) return block;
        Map<SconceMetal, Block> family = FAMILIES.get(SconceMetalData.ironPath(path));
        if (family == null) throw new IllegalArgumentException("Not a registered sconce family: " + path);
        return family.get(metal);
    }

    private SconceVariants() { }
}
