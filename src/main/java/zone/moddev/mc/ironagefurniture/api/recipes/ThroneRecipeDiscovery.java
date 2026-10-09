package zone.moddev.mc.ironagefurniture.api.recipes;

import net.minecraft.advancements.Advancement;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import zone.moddev.mc.ironagefurniture.Ironagefurniture;
import zone.moddev.mc.ironagefurniture.api.blocks.furniture.CanopyBed;
import zone.moddev.mc.ironagefurniture.api.blocks.furniture.WingbackChair;
import zone.moddev.mc.ironagefurniture.api.blocks.furniture.WoodenBed;
import zone.moddev.mc.ironagefurniture.api.enumerations.UpholsteryColour;
import zone.moddev.mc.ironagefurniture.api.items.UpholsteredBlockItem;

/** Makes chair and bed upgrades discoverable without closing the crafting table. */
@Mod.EventBusSubscriber(modid = Ironagefurniture.MODID)
public final class ThroneRecipeDiscovery {
    private ThroneRecipeDiscovery() { }

    @SubscribeEvent
    public static void onCrafted(PlayerEvent.ItemCraftedEvent event) {
        if (!(event.getPlayer() instanceof ServerPlayerEntity)) return;
        ItemStack crafted = event.getCrafting();
        if (!(crafted.getItem() instanceof UpholsteredBlockItem)) return;
        UpholsteredBlockItem item = (UpholsteredBlockItem)crafted.getItem();
        ResourceLocation block = item.getBlock().getRegistryName();
        if (block == null || !Ironagefurniture.MODID.equals(block.getNamespace())) return;
        ServerPlayerEntity player = (ServerPlayerEntity)event.getPlayer();
        String path = block.getPath(), colour = item.getColour().getName();
        String suffix = item.getColour() == UpholsteryColour.RED ? "" : "_" + colour;
        String prefix = "chair_wood_ironage_wingback_";
        if (item.getBlock() instanceof WingbackChair && path.startsWith(prefix)) {
            grantUpgrade(player, crafted, "chair_wood_ironage_throne_" + path.substring(prefix.length()) + "_" + colour);
        } else if (item.getBlock() instanceof WoodenBed && !((WoodenBed)item.getBlock()).isDoubleBed()
                && path.startsWith("bed_wood_foot_")) {
            String wood = path.substring("bed_wood_foot_".length());
            grantUpgrade(player, crafted, "bed_canopy_foot_lower_" + wood + "_" + colour);
            grantUpgrade(player, crafted, "bed_wood_foot_left_" + wood + suffix);
        } else if (item.getBlock() instanceof CanopyBed && !((CanopyBed)item.getBlock()).isDoubleBed()
                && path.startsWith("bed_canopy_foot_lower_")) {
            String wood = path.substring("bed_canopy_foot_lower_".length());
            grantUpgrade(player, crafted, "bed_canopy_foot_left_lower_" + wood + suffix);
        }
    }

    private static void grantUpgrade(ServerPlayerEntity player, ItemStack crafted, String path) {
        MinecraftServer server = player.getServer();
        if (server == null) return;
        ResourceLocation id = new ResourceLocation(Ironagefurniture.MODID, path);
        IRecipe<?> recipe = server.getRecipeManager().getRecipe(id).orElse(null);
        Advancement advancement = server.getAdvancementManager().getAdvancement(
                new ResourceLocation(Ironagefurniture.MODID, "recipes/upholstery/" + id.getPath()));
        if (recipe == null || advancement == null || !advancement.getCriteria().containsKey("has_ingredient")) return;
        // Respect disabled integrations and datapacks that replace this recipe.
        if (recipe.getIngredients().stream().noneMatch(ingredient -> ingredient.test(crafted))) return;
        player.getAdvancements().grantCriterion(advancement, "has_ingredient");
    }
}
