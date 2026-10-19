package zone.moddev.mc.ironagefurniture.api.recipes;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import zone.moddev.mc.ironagefurniture.Ironagefurniture;
import zone.moddev.mc.ironagefurniture.api.blocks.furniture.CanopyBed;
import zone.moddev.mc.ironagefurniture.api.blocks.furniture.WingbackChair;
import zone.moddev.mc.ironagefurniture.api.blocks.furniture.WoodenBed;
import zone.moddev.mc.ironagefurniture.api.enumerations.UpholsteryColour;
import zone.moddev.mc.ironagefurniture.api.items.UpholsteredBlockItem;

/** Discover matching upgrades while the crafting table is still open. */
@EventBusSubscriber(modid = Ironagefurniture.MODID)
public final class FurnitureUpgradeDiscovery {
    @SubscribeEvent public static void crafted(PlayerEvent.ItemCraftedEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;
        ItemStack crafted = event.getCrafting();
        if (!(crafted.getItem() instanceof UpholsteredBlockItem item)) return;
        String path = BuiltInRegistries.BLOCK.getKey(item.getBlock()).getPath();
        String colour = item.getColour().getName();
        String suffix = item.getColour() == UpholsteryColour.RED ? "" : "_" + colour;
        if (item.getBlock() instanceof WingbackChair && path.startsWith("chair_wood_ironage_wingback_")) {
            grant(player, crafted, "chair_wood_ironage_throne_" + path.substring("chair_wood_ironage_wingback_".length()) + "_" + colour);
        } else if (item.getBlock() instanceof WoodenBed bed && !bed.isDoubleBed()) {
            String wood = path.substring("bed_wood_foot_".length());
            grant(player, crafted, "bed_canopy_foot_lower_" + wood + "_" + colour);
            grant(player, crafted, "bed_wood_foot_left_" + wood + suffix);
        } else if (item.getBlock() instanceof CanopyBed bed && !bed.isDoubleBed()) {
            grant(player, crafted, "bed_canopy_foot_left_lower_" + path.substring("bed_canopy_foot_lower_".length()) + suffix);
        }
    }
    private static void grant(ServerPlayer player, ItemStack crafted, String path) {
        var server = player.level().getServer();
        Identifier id = Identifier.fromNamespaceAndPath(Ironagefurniture.MODID, path);
        var recipe = server.getRecipeManager().byKey(ResourceKey.create(Registries.RECIPE, id)).orElse(null);
        var advancement = server.getAdvancements().get(Identifier.fromNamespaceAndPath(Ironagefurniture.MODID, "recipes/phase_four/" + path));
        if (recipe == null || advancement == null || !advancement.value().criteria().containsKey("has_ingredient")) return;
        if (recipe.value().placementInfo().ingredients().stream().noneMatch(ingredient -> ingredient.test(crafted))) return;
        player.getAdvancements().award(advancement, "has_ingredient");
    }
    private FurnitureUpgradeDiscovery() { }
}
