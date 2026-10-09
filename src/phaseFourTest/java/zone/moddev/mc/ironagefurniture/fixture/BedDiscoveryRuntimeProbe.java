package zone.moddev.mc.ironagefurniture.fixture;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.inventory.container.SimpleNamedContainerProvider;
import net.minecraft.inventory.container.WorkbenchContainer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;

/** Keeps the real player's crafting table open while discovering the next bed. */
final class BedDiscoveryRuntimeProbe {
    static final Set<ResourceLocation> expected = new LinkedHashSet<>();
    static volatile boolean complete;
    static volatile Throwable failure;
    private BedDiscoveryRuntimeProbe() { }

    static void run(MinecraftServer server, ServerPlayerEntity player) {
        try {
            require(!(player instanceof net.minecraftforge.common.util.FakePlayer), "Bed discovery needs a real player");
            List<IRecipe<?>> upgrades = new ArrayList<>();
            Map<Item, IRecipe<?>> singles = new LinkedHashMap<>();
            for (IRecipe<?> recipe : server.getRecipeManager().getRecipes()) {
                String path = recipe.getId().getPath();
                if (!"ironagefurniture".equals(recipe.getId().getNamespace()) || path.contains("_recolour_")) continue;
                if (path.startsWith("bed_canopy_foot_lower_") || path.startsWith("bed_canopy_foot_left_lower_")
                        || path.startsWith("bed_wood_foot_left_")) upgrades.add(recipe);
                if ((path.startsWith("bed_wood_foot_") && !path.startsWith("bed_wood_foot_left_"))
                        || path.startsWith("bed_canopy_foot_lower_")) singles.put(recipe.getRecipeOutput().getItem(), recipe);
            }
            require(upgrades.size() >= 288 && singles.size() * 3 == upgrades.size() * 2, "Incomplete bed discovery catalog");
            if (Boolean.getBoolean("iaf.probe.discoveryReload")) {
                for (IRecipe<?> upgrade : upgrades) {
                    require(player.getRecipeBook().isUnlocked(upgrade), "Reload lost bed recipe " + upgrade.getId());
                    require(player.getAdvancements().getProgress(advancement(server, upgrade)).isDone(), "Reload lost bed advancement");
                    expected.add(upgrade.getId());
                }
            } else {
                player.closeScreen();
                player.inventory.clear();
                player.inventory.setItemStack(ItemStack.EMPTY);
                for (IRecipe<?> upgrade : upgrades) reset(server, player, upgrade);
                player.inventory.setInventorySlotContents(0, new ItemStack(Items.CRAFTING_TABLE));
                CriteriaTriggers.INVENTORY_CHANGED.trigger(player, player.inventory);
                require(upgrades.stream().noneMatch(player.getRecipeBook()::isUnlocked), "Crafting table unlocked bed upgrades");
                BlockPos pos = player.getPosition();
                player.world.setBlockState(pos, Blocks.CRAFTING_TABLE.getDefaultState(), 2);
                player.openContainer(new SimpleNamedContainerProvider((window, inventory, owner) -> new WorkbenchContainer(
                        window, inventory, IWorldPosCallable.of(player.world, pos)), new StringTextComponent("Bed discovery test")));
                WorkbenchContainer table = (WorkbenchContainer)player.openContainer;
                for (Map.Entry<Item, IRecipe<?>> source : singles.entrySet()) {
                    List<IRecipe<?>> matching = new ArrayList<>();
                    for (IRecipe<?> upgrade : upgrades)
                        if (upgrade.getIngredients().get(0).test(new ItemStack(source.getKey()))) matching.add(upgrade);
                    require(!matching.isEmpty(), "Single bed has no upgrade");
                    for (ClickType click : new ClickType[] {ClickType.PICKUP, ClickType.QUICK_MOVE}) {
                        for (IRecipe<?> upgrade : matching) reset(server, player, upgrade);
                        player.inventory.clear();
                        player.inventory.setItemStack(ItemStack.EMPTY);
                        for (int slot = 1; slot <= 9; slot++) table.putStackInSlot(slot, ItemStack.EMPTY);
                        for (int input = 0; input < source.getValue().getIngredients().size(); input++)
                            table.putStackInSlot(input + 1, source.getValue().getIngredients().get(input).getMatchingStacks()[0].copy());
                        require(ItemStack.areItemsEqual(table.getSlot(0).getStack(), source.getValue().getRecipeOutput()), "Cannot craft single bed");
                        table.slotClick(0, 0, click, player);
                        require(player.openContainer == table, "Bed discovery closed the table");
                        // Vanilla also learns the recipe just crafted. A single
                        // canopy is itself one of the upgrades being audited.
                        if (upgrades.contains(source.getValue())) expected.add(source.getValue().getId());
                        for (IRecipe<?> upgrade : matching) {
                            require(player.getRecipeBook().isUnlocked(upgrade), "Bed upgrade not discovered while crafting table remains open: "
                                    + upgrade.getId() + " / " + click);
                            require(player.getAdvancements().getProgress(advancement(server, upgrade)).isDone(), "Bed hook bypassed advancement");
                        }
                        for (IRecipe<?> upgrade : upgrades)
                            require(player.getRecipeBook().isUnlocked(upgrade) == (expected.contains(upgrade.getId()) || matching.contains(upgrade)),
                                    "Bed crafting discovered another wood or colour: " + upgrade.getId());
                    }
                    for (IRecipe<?> upgrade : matching) expected.add(upgrade.getId());
                }
                // Picking up an existing single still uses ordinary inventory discovery.
                player.closeScreen();
                for (IRecipe<?> upgrade : upgrades) {
                    reset(server, player, upgrade);
                    player.inventory.clear();
                    player.inventory.setItemStack(ItemStack.EMPTY);
                    // Two upgrades can share a single bed. Send the empty
                    // inventory first so a repeated pickup is a real change.
                    player.container.detectAndSendChanges();
                    player.inventory.setInventorySlotContents(0, upgrade.getIngredients().get(0).getMatchingStacks()[0].copy());
                    player.container.detectAndSendChanges();
                    require(player.getRecipeBook().isUnlocked(upgrade), "Existing single does not discover bed upgrade: " + upgrade.getId());
                }
                player.inventory.clear();
                player.inventory.setItemStack(ItemStack.EMPTY);
                player.getAdvancements().save();
            }
            org.apache.logging.log4j.LogManager.getLogger().info("IAF REAL-PLAYER BED DISCOVERY PASSED: {} recipes, reload={}",
                    expected.size(), Boolean.getBoolean("iaf.probe.discoveryReload"));
        } catch (Throwable problem) { failure = problem; }
        finally { complete = true; }
    }

    private static Advancement advancement(MinecraftServer server, IRecipe<?> recipe) {
        Advancement result = server.getAdvancementManager().getAdvancement(new ResourceLocation("ironagefurniture", "recipes/upholstery/" + recipe.getId().getPath()));
        require(result != null, "Missing bed advancement " + recipe.getId());
        return result;
    }
    private static void reset(MinecraftServer server, ServerPlayerEntity player, IRecipe<?> recipe) {
        Advancement target = advancement(server, recipe);
        for (String criterion : target.getCriteria().keySet()) player.getAdvancements().revokeCriterion(target, criterion);
        player.resetRecipes(Collections.singleton(recipe));
    }
    private static void require(boolean condition, String message) { if (!condition) throw new IllegalStateException(message); }
}
