package zone.moddev.mc.ironagefurniture.init;

import zone.moddev.mc.ironagefurniture.Ironagefurniture;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.state.BlockBehaviour;

/**
 * Applies stable registry identities before Minecraft constructs blocks and
 * items. Minecraft 26.2 derives description and loot identities during
 * construction, so assigning the registry entry afterward is too late.
 */
public final class RegistrationProperties {
    public static BlockBehaviour.Properties block(BlockBehaviour.Properties properties, String path) {
        return properties.setId(ResourceKey.create(Registries.BLOCK, id(path)));
    }

    public static Item.Properties item(Item.Properties properties, String path) {
        return properties.setId(ResourceKey.create(Registries.ITEM, id(path)));
    }

    private static Identifier id(String path) {
        return Identifier.fromNamespaceAndPath(Ironagefurniture.MODID, path);
    }

    private RegistrationProperties() {
        throw new IllegalAccessError("Not an instantiable class");
    }
}
