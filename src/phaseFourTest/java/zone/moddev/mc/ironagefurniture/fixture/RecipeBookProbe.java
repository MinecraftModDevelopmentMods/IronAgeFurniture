package zone.moddev.mc.ironagefurniture.fixture;

import com.google.gson.JsonParser;
import com.mojang.authlib.GameProfile;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.BiConsumer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.game.ServerboundPlaceRecipePacket;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ClientInformation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.CommonListenerCookie;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.CraftingMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.display.RecipeDisplayEntry;
import net.minecraft.world.level.block.Blocks;
import zone.moddev.mc.ironagefurniture.api.blocks.furniture.CanopyBed;
import zone.moddev.mc.ironagefurniture.api.blocks.furniture.WingbackChair;
import zone.moddev.mc.ironagefurniture.api.blocks.furniture.WoodenBed;
import zone.moddev.mc.ironagefurniture.api.enumerations.UpholsteryColour;
import zone.moddev.mc.ironagefurniture.api.items.UpholsteredBlockItem;

/** Drive the same placement packet and result-slot actions used by the recipe book. */
final class RecipeBookProbe {
    static void run(ServerLevel level, BiConsumer<Boolean, String> check) throws Exception {
        var server = level.getServer();
        BlockPos table = new BlockPos(2, 150, 2);
        level.setBlockAndUpdate(table, Blocks.CRAFTING_TABLE.defaultBlockState());
        GameProfile profile = new GameProfile(UUID.fromString("5f9119cc-c606-467f-9470-c8e5df30b6f4"), "IAFRecipeProbe");
        ServerPlayer player = new ServerPlayer(server, level, profile, ClientInformation.createDefault());
        new ServerGamePacketListenerImpl(server, new Connection(PacketFlow.SERVERBOUND), player, CommonListenerCookie.createInitial(profile, false));
        player.setPos(table.getX() + .5, table.getY(), table.getZ() + 1.5);
        ItemStack craftingTable = new ItemStack(Blocks.CRAFTING_TABLE);
        player.getInventory().setItem(0, craftingTable);
        net.minecraft.advancements.triggers.CriteriaTriggers.INVENTORY_CHANGED.trigger(player, player.getInventory(), craftingTable);
        check.accept(server.getRecipeManager().getRecipes().stream().filter(recipe -> recipe.id().identifier().getNamespace().equals("ironagefurniture"))
                .noneMatch(recipe -> player.getRecipeBook().contains(recipe.id())), "Obtaining a crafting table does not unlock unrelated IAF recipes");
        player.getInventory().clearContent();
        int recipes = 0;
        var resources = server.getResourceManager().listResources("recipe", id -> id.getNamespace().equals("ironagefurniture") && id.getPath().endsWith(".json"));
        for (var entry : resources.entrySet()) {
            String path = entry.getKey().getPath().substring("recipe/".length()).replaceFirst("\\.json$", "");
            if (!(path.startsWith("bed_") || path.startsWith("chair_wood_ironage_wingback_") || path.startsWith("chair_wood_ironage_throne_"))) continue;
            var key = ResourceKey.<Recipe<?>>create(Registries.RECIPE, Identifier.fromNamespaceAndPath("ironagefurniture", path));
            var holder = server.getRecipeManager().byKey(key).orElse(null);
            if (holder == null) continue; // Disabled optional recipes must not be manufactured by the probe.
            com.google.gson.JsonObject json;
            try (var reader = entry.getValue().openAsReader()) { json = JsonParser.parseReader(reader).getAsJsonObject(); }
            if (!json.get("type").getAsString().equals("minecraft:crafting_shapeless")) continue;
            List<ItemStack> inputs = new ArrayList<>();
            for (var ingredient : json.getAsJsonArray("ingredients")) {
                String id = ingredient.getAsString();
                var item = id.startsWith("#") ? BuiltInRegistries.ITEM.get(net.minecraft.tags.TagKey.create(Registries.ITEM, Identifier.parse(id.substring(1))))
                        .orElseThrow().get(0).value() : BuiltInRegistries.ITEM.getValue(Identifier.parse(id));
                inputs.add(new ItemStack(item, 3));
            }
            ItemStack expected = new ItemStack(BuiltInRegistries.ITEM.getValue(Identifier.parse(json.getAsJsonObject("result").get("id").getAsString())));
            List<String> upgrades = upgrades(expected);
            for (boolean shift : new boolean[]{false, true}) {
                player.getInventory().clearContent();
                for (String upgrade : upgrades) {
                    server.getRecipeManager().byKey(recipeKey(upgrade)).ifPresent(recipe -> player.resetRecipes(List.of(recipe)));
                    var advancement = server.getAdvancements().get(Identifier.fromNamespaceAndPath("ironagefurniture", "recipes/phase_four/" + upgrade));
                    if (advancement != null) for (String criterion : advancement.value().criteria().keySet()) player.getAdvancements().revoke(advancement, criterion);
                }
                CraftingMenu menu = new CraftingMenu(1, player.getInventory(), ContainerLevelAccess.create(level, table));
                player.containerMenu = menu;
                for (int slot = 0; slot < inputs.size(); slot++) player.getInventory().setItem(slot, inputs.get(slot).copy());
                for (String upgrade : upgrades) check.accept(!player.getRecipeBook().contains(recipeKey(upgrade)), "A new player has not learned an unrelated upgrade");
                player.awardRecipes(List.of(holder));
                List<RecipeDisplayEntry> displays = new ArrayList<>();
                server.getRecipeManager().listDisplaysForRecipe(key, displays::add);
                check.accept(displays.size() == 1, "One independent recipe-book display exists for " + path);
                player.connection.handlePlaceRecipe(new ServerboundPlaceRecipePacket(menu.containerId, displays.getFirst().id(), shift));
                check.accept(ItemStack.matches(menu.getResultSlot().getItem(), expected), "Native recipe-book autofill produces the correct flat variant: " + path);
                int batch = menu.getInputGridSlots().stream().map(slot -> slot.getItem()).filter(stack -> !stack.isEmpty()).mapToInt(ItemStack::getCount).min().orElseThrow();
                int before = inputs.stream().mapToInt(ItemStack::getCount).sum();
                if (shift) menu.clicked(0, 0, ContainerInput.QUICK_MOVE, player);
                else menu.clicked(0, 0, ContainerInput.PICKUP, player);
                int made = shift ? player.getInventory().getNonEquipmentItems().stream().filter(stack -> stack.is(expected.getItem())).mapToInt(ItemStack::getCount).sum()
                        : menu.getCarried().getCount();
                check.accept(made == batch, "Normal/shift crafting returns the complete expected batch: " + path + " shift=" + shift + " made=" + made + " expected=" + batch);
                int remaining = player.getInventory().getNonEquipmentItems().stream().filter(stack -> !stack.is(expected.getItem())).mapToInt(ItemStack::getCount).sum()
                        + menu.getInputGridSlots().stream().map(slot -> slot.getItem()).mapToInt(ItemStack::getCount).sum();
                check.accept(before - remaining == batch * inputs.size(), "Crafting consumes each exact ingredient once per result: " + path);
                for (String upgrade : upgrades) {
                    var upgradeKey = recipeKey(upgrade);
                    if (server.getRecipeManager().byKey(upgradeKey).isPresent())
                        check.accept(player.getRecipeBook().contains(upgradeKey), "Crafting immediately discovers the matching upgrade while the table stays open: " + upgrade);
                }
                check.accept(player.containerMenu == menu, "Discovery does not need the table to close");
            }
            recipes++;
            if (recipes % 128 == 0) System.out.println("IAF_RECIPE_BOOK_PROGRESS recipes=" + recipes);
        }
        boolean enabled = zone.moddev.mc.ironagefurniture.IronAgeFurnitureConfiguration.enabled(zone.moddev.mc.ironagefurniture.IronAgeFurnitureConfiguration.WOOD_BEDS);
        check.accept(!enabled || recipes >= 12 * 16 * 6, "Every enabled wood/colour upgrade recipe is exercised");
        level.setBlockAndUpdate(table, Blocks.AIR.defaultBlockState());
        System.out.println("IAF_RECIPE_BOOK_PASS recipes=" + recipes + " modes=normal,shift");
    }
    private static ResourceKey<Recipe<?>> recipeKey(String path) {
        return ResourceKey.create(Registries.RECIPE, Identifier.fromNamespaceAndPath("ironagefurniture", path));
    }
    private static List<String> upgrades(ItemStack output) {
        if (!(output.getItem() instanceof UpholsteredBlockItem item)) return List.of();
        String path = BuiltInRegistries.BLOCK.getKey(item.getBlock()).getPath();
        String colour = item.getColour().getName(), suffix = item.getColour() == UpholsteryColour.RED ? "" : "_" + colour;
        if (item.getBlock() instanceof WingbackChair) return List.of(path.replace("_wingback_", "_throne_") + "_" + colour);
        if (item.getBlock() instanceof WoodenBed bed && !bed.isDoubleBed()) {
            String wood = path.substring("bed_wood_foot_".length());
            return List.of("bed_canopy_foot_lower_" + wood + "_" + colour, "bed_wood_foot_left_" + wood + suffix);
        }
        if (item.getBlock() instanceof CanopyBed bed && !bed.isDoubleBed())
            return List.of("bed_canopy_foot_left_lower_" + path.substring("bed_canopy_foot_lower_".length()) + suffix);
        return List.of();
    }
    private RecipeBookProbe() { }
}
