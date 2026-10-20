package zone.moddev.mc.ironagefurniture.init;

import java.util.EnumMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import zone.moddev.mc.ironagefurniture.api.SconceMetalData;
import zone.moddev.mc.ironagefurniture.api.enumerations.SconceMetal;

/** Each material has its own saved block identity, with shared light behaviour. */
public final class SconceVariants {
    private static final Map<String, Map<SconceMetal, DeferredHolder<Block, ? extends Block>>> FAMILIES = new LinkedHashMap<>();
    private record Registration(SconceMetal metal, Map<SconceMetal, DeferredHolder<Block, ? extends Block>> family) { }
    private static final Map<Block, Registration> BLOCKS = new java.util.concurrent.ConcurrentHashMap<>();

    public static <T extends Block> DeferredHolder<Block, T> register(DeferredRegister<Block> register,
            String ironPath, Function<String, T> factory) {
        Map<SconceMetal, DeferredHolder<Block, ? extends Block>> family = new EnumMap<>(SconceMetal.class);
        if (FAMILIES.putIfAbsent(ironPath, family) != null) throw new IllegalArgumentException("Duplicate sconce family: " + ironPath);
        DeferredHolder<Block, T> iron = register.register(ironPath, () -> create(factory, ironPath, SconceMetal.IRON, family));
        family.put(SconceMetal.IRON, iron);
        for (SconceMetal metal : SconceMetal.values()) if (metal != SconceMetal.IRON) {
            String path = SconceMetalData.blockPath(ironPath, metal);
            family.put(metal, register.register(path, () -> create(factory, path, metal, family)));
        }
        return iron;
    }

    private static <T extends Block> T create(Function<String, T> factory, String path, SconceMetal metal,
            Map<SconceMetal, DeferredHolder<Block, ? extends Block>> family) {
        T block = factory.apply(path);
        BLOCKS.put(block, new Registration(metal, family));
        return block;
    }

    public static boolean hasFamily(String ironPath) { return FAMILIES.containsKey(ironPath); }

    public static SconceMetal metal(Block block) {
        Registration registration = BLOCKS.get(block);
        return registration == null ? SconceMetal.IRON : registration.metal();
    }

    public static Block material(Block block, SconceMetal metal) {
        Registration registration = BLOCKS.get(block);
        if (registration == null) throw new IllegalArgumentException("Not a registered sconce block: " + block);
        return registration.family().get(metal).get();
    }

    public static boolean isFamily(Block block, Block iron) {
        Registration registration = BLOCKS.get(block);
        return registration != null && registration.family().get(SconceMetal.IRON).get() == iron;
    }

    private SconceVariants() { }
}
