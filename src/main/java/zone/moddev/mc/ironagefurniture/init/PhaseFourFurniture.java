package zone.moddev.mc.ironagefurniture.init;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import zone.moddev.mc.ironagefurniture.Ironagefurniture;
import zone.moddev.mc.ironagefurniture.ModItems;
import zone.moddev.mc.ironagefurniture.api.blocks.furniture.CanopyBed;
import zone.moddev.mc.ironagefurniture.api.blocks.furniture.ThroneChair;
import zone.moddev.mc.ironagefurniture.api.blocks.furniture.WingbackChair;
import zone.moddev.mc.ironagefurniture.api.blocks.furniture.WoodenBed;
import zone.moddev.mc.ironagefurniture.api.enumerations.UpholsteryColour;
import zone.moddev.mc.ironagefurniture.api.items.UpholsteredBlockItem;

/** Shared registrations let separate wood add-ons use exactly the same furniture. */
public final class PhaseFourFurniture {
    public static final DeferredRegister<Block> REGISTER = DeferredRegister.create(net.minecraft.core.registries.Registries.BLOCK, Ironagefurniture.MODID);
    public static final String[] VANILLA_WOODS = {"oak", "spruce", "birch", "jungle", "acacia", "dark_oak",
            "mangrove", "cherry", "bamboo", "pale_oak", "crimson", "warped"};
    static {
        for (String wood : VANILLA_WOODS) registerWood(wood, REGISTER, ModItems.REGISTER);
    }

    public static Map<String, RegistryObject<Block>> registerWood(String wood, DeferredRegister<Block> blocks,
            DeferredRegister<Item> items) {
        Map<String, RegistryObject<Block>> result = new LinkedHashMap<>();
        add(result, blocks, items, "chair_wood_ironage_wingback_" + wood, () -> new WingbackChair("chair_wood_ironage_wingback_" + wood), true);
        add(result, blocks, items, "chair_wood_ironage_throne_" + wood, () -> new ThroneChair("chair_wood_ironage_throne_" + wood), true);
        add(result, blocks, items, "bed_wood_foot_" + wood, () -> new WoodenBed("bed_wood_foot_" + wood, false), true);
        add(result, blocks, items, "bed_wood_foot_left_" + wood, () -> new WoodenBed("bed_wood_foot_left_" + wood, true), true);
        add(result, blocks, items, "bed_canopy_foot_lower_" + wood, () -> new CanopyBed("bed_canopy_foot_lower_" + wood, false), true);
        String leftId = "bed_canopy_foot_left_lower_" + wood;
        String rightId = "bed_canopy_foot_right_lower_" + wood;
        add(result, blocks, items, leftId, () -> {
            CanopyBed bed = new CanopyBed(leftId, true);
            bed.setDoubleBlocks(() -> (CanopyBed)result.get(leftId).get(), () -> (CanopyBed)result.get(rightId).get());
            return bed;
        }, true);
        add(result, blocks, items, rightId, () -> {
            CanopyBed bed = new CanopyBed(rightId, true);
            bed.setDoubleBlocks(() -> (CanopyBed)result.get(leftId).get(), () -> (CanopyBed)result.get(rightId).get());
            return bed;
        }, false);
        return result;
    }

    private static void add(Map<String, RegistryObject<Block>> result, DeferredRegister<Block> blocks,
            DeferredRegister<Item> items, String path, Supplier<Block> factory, boolean visible) {
        RegistryObject<Block> block = blocks.register(path, factory);
        result.put(path, block);
        if (visible) for (UpholsteryColour colour : UpholsteryColour.values()) {
            String itemPath = path + (colour == UpholsteryColour.RED ? "" : "_" + colour.getSerializedName());
            items.register(itemPath, () -> new UpholsteredBlockItem(block.get(),
                    RegistrationProperties.item(new Item.Properties(), itemPath).useBlockDescriptionPrefix(), colour));
        }
    }
    private PhaseFourFurniture() { }
}
