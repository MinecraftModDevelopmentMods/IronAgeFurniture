package zone.moddev.mc.ironagefurniture.fixture;

import net.minecraftforge.fml.common.Mod;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

/** A test-only provider of standard metal tags, not a Base Metals release. */
@Mod("basemetals")
public final class MetalContractFixture {
    private static final String[] METALS = {"adamantine", "aquarium", "bismuth", "brass", "bronze", "coldiron",
            "copper", "cupronickel", "electrum", "invar", "lead", "mithril", "nickel", "pewter", "platinum",
            "silver", "starsteel", "steel", "tin", "zinc"};

    @Mod.EventBusSubscriber(modid = "basemetals", bus = Mod.EventBusSubscriber.Bus.MOD)
    public static final class Registrations {
        @SubscribeEvent public static void blocks(RegistryEvent.Register<Block> event) {
            for (String metal : METALS) event.getRegistry().register(new Block(Block.Properties.copy(Blocks.GOLD_BLOCK))
                    .setRegistryName("basemetals", metal + "_block"));
        }
        @SubscribeEvent public static void items(RegistryEvent.Register<Item> event) {
            for (String metal : METALS) event.getRegistry().register(new Item(new Item.Properties())
                    .setRegistryName("basemetals", metal + "_nugget"));
        }
    }
}
