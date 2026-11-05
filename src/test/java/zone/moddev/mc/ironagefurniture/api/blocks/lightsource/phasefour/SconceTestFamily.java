package zone.moddev.mc.ironagefurniture.api.blocks.lightsource.phasefour;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import zone.moddev.mc.ironagefurniture.api.blocks.base.LightHolderSconce;
import zone.moddev.mc.ironagefurniture.init.SconceVariants;

public final class SconceTestFamily {
    private static final AtomicInteger NEXT = new AtomicInteger();

    public static <T extends LightHolderSconce> T create(Function<String, T> factory) {
        String path = "light_metal_ironage_sconce_test_" + NEXT.incrementAndGet() + "_iron";
        List<T> variants = new ArrayList<>();
        SconceVariants.register(block -> { }, path, name -> {
            T block = factory.apply("ironagefurniture:" + name);
            variants.add(block);
            return block;
        });
        return variants.get(0);
    }

    private SconceTestFamily() { }
}
