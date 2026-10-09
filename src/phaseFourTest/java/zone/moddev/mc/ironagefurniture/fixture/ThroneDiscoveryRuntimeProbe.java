package zone.moddev.mc.ironagefurniture.fixture;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.inventory.container.SimpleNamedContainerProvider;
import net.minecraft.inventory.container.WorkbenchContainer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;

/** Crafts through the normal result-slot click path for a connected, real player. */
final class ThroneDiscoveryRuntimeProbe {
    static final List<ResourceLocation> expected = new ArrayList<>();
    static volatile boolean complete;
    static volatile Throwable failure;
    private ThroneDiscoveryRuntimeProbe() { }

    static void run(MinecraftServer server, ServerPlayerEntity player) {
        try {
            require(!(player instanceof net.minecraftforge.common.util.FakePlayer), "Discovery requires a real player");
            List<IRecipe<?>> thrones = new ArrayList<>();
            List<IRecipe<?>> wingbacks = new ArrayList<>();
            for (IRecipe<?> recipe : server.getRecipeManager().getRecipes()) {
                if (!"ironagefurniture".equals(recipe.getId().getNamespace())) continue;
                if (recipe.getId().getPath().startsWith("chair_wood_ironage_throne_")) thrones.add(recipe);
                if (recipe.getId().getPath().startsWith("chair_wood_ironage_wingback_")) wingbacks.add(recipe);
            }
            if (Boolean.getBoolean("iaf.probe.missingThroneTarget")) {
                ResourceLocation id = new ResourceLocation("ironagefurniture", "chair_wood_ironage_throne_oak_blue");
                IRecipe<?> wingback = server.getRecipeManager().getRecipe(new ResourceLocation("ironagefurniture", "chair_wood_ironage_wingback_oak_blue")).get();
                IRecipe<?> throne = server.getRecipeManager().getRecipe(id).orElse(null);
                Advancement target = server.getAdvancementManager().getAdvancement(new ResourceLocation("ironagefurniture", "recipes/upholstery/" + id.getPath()));
                require(throne == null || target == null, "Negative fixture did not remove recipe or advancement");
                player.closeScreen();
                player.inventory.clear();
                player.inventory.setItemStack(ItemStack.EMPTY);
                if (target != null) for (String criterion : target.getCriteria().keySet()) player.getAdvancements().revokeCriterion(target, criterion);
                if (throne != null) player.resetRecipes(Collections.singleton(throne));
                BlockPos pos = player.getPosition();
                player.world.setBlockState(pos, Blocks.CRAFTING_TABLE.getDefaultState(), 2);
                player.openContainer(new SimpleNamedContainerProvider((window, inventory, owner) -> new WorkbenchContainer(window, inventory,
                        IWorldPosCallable.of(player.world, pos)), new StringTextComponent("Missing throne test")));
                WorkbenchContainer table = (WorkbenchContainer)player.openContainer;
                for (int i = 0; i < 3; i++) table.putStackInSlot(i + 1, wingback.getIngredients().get(i).getMatchingStacks()[0].copy());
                require(ItemStack.areItemsEqual(table.getSlot(0).getStack(), wingback.getRecipeOutput()), "Negative fixture has no craftable wingback");
                table.slotClick(0, 0, ClickType.PICKUP, player);
                require(ItemStack.areItemsEqual(player.inventory.getItemStack(), wingback.getRecipeOutput()), "Negative fixture did not craft a wingback");
                require(player.openContainer == table, "Negative test closed table");
                require(throne == null || !player.getRecipeBook().isUnlocked(throne), "Missing advancement unlocked throne");
                require(target == null || !player.getAdvancements().getProgress(target).isDone(), "Missing recipe granted advancement");
                org.apache.logging.log4j.LogManager.getLogger().info("IAF MISSING THRONE TARGET PASSED: recipe={}, advancement={}", throne != null, target != null);
                return;
            }
            require(thrones.size() == wingbacks.size() && thrones.size() >= 96, "Incomplete throne catalog");
            if (Boolean.getBoolean("iaf.probe.discoveryReload")) {
                for (IRecipe<?> throne : thrones) {
                    require(player.getRecipeBook().isUnlocked(throne), "Reload lost learned throne " + throne.getId());
                    require(player.getAdvancements().getProgress(advancement(server, throne)).isDone(), "Reload lost throne advancement");
                    expected.add(throne.getId());
                }
            } else {
                player.closeScreen();
                player.inventory.clear();
                player.inventory.setItemStack(ItemStack.EMPTY);
                for (IRecipe<?> throne : thrones) reset(server, player, throne);
                player.inventory.setInventorySlotContents(0, new ItemStack(Items.CRAFTING_TABLE));
                CriteriaTriggers.INVENTORY_CHANGED.trigger(player, player.inventory);
                require(learned(player, thrones) == 0, "Crafting table unlocked thrones");
                player.inventory.clear();
                BlockPos pos = player.getPosition();
                player.world.setBlockState(pos, Blocks.CRAFTING_TABLE.getDefaultState(), 2);
                player.openContainer(new SimpleNamedContainerProvider((id, inventory, owner) ->
                        new WorkbenchContainer(id, inventory, IWorldPosCallable.of(player.world, pos)),
                        new StringTextComponent("Throne discovery test")));
                WorkbenchContainer table = (WorkbenchContainer)player.openContainer;
                for (IRecipe<?> wingback : wingbacks) {
                    ResourceLocation id = new ResourceLocation("ironagefurniture", wingback.getId().getPath().replace("_wingback_", "_throne_"));
                    IRecipe<?> throne = server.getRecipeManager().getRecipe(id).orElseThrow(() -> new IllegalStateException("Missing " + id));
                    for (ClickType click : new ClickType[] {ClickType.PICKUP, ClickType.QUICK_MOVE}) {
                        reset(server, player, throne);
                        player.inventory.clear();
                        player.inventory.setItemStack(ItemStack.EMPTY);
                        for (int slot = 1; slot <= 9; slot++) table.putStackInSlot(slot, ItemStack.EMPTY);
                        for (int i = 0; i < 3; i++) table.putStackInSlot(i + 1, wingback.getIngredients().get(i).getMatchingStacks()[0].copy());
                        require(ItemStack.areItemsEqual(table.getSlot(0).getStack(), wingback.getRecipeOutput()), "Crafting grid has wrong wingback");
                        table.slotClick(0, 0, click, player);
                        require(player.openContainer == table, "Crafting table closed during discovery");
                        require(player.getRecipeBook().isUnlocked(throne), "Throne not discovered while crafting table remains open: " + id + " / " + click);
                        require(player.getAdvancements().getProgress(advancement(server, throne)).isDone(), "Craft hook bypassed the matching advancement");
                        require(learned(player, thrones) == expected.size() + 1, "Crafting unlocked another wood or colour");
                    }
                    expected.add(id);
                }
                // A pre-existing wingback still uses ordinary inventory discovery.
                player.closeScreen();
                for (IRecipe<?> throne : thrones) {
                    reset(server, player, throne);
                    player.inventory.clear();
                    player.inventory.setItemStack(ItemStack.EMPTY);
                    player.inventory.setInventorySlotContents(0, throne.getIngredients().get(2).getMatchingStacks()[0].copy());
                    player.container.detectAndSendChanges();
                    require(player.getRecipeBook().isUnlocked(throne), "Existing wingback no longer discovers throne");
                }
                player.inventory.clear();
                player.inventory.setItemStack(ItemStack.EMPTY);
                player.getAdvancements().save();
            }
            org.apache.logging.log4j.LogManager.getLogger().info("IAF REAL-PLAYER THRONE DISCOVERY PASSED: {} recipes, reload={}", expected.size(), Boolean.getBoolean("iaf.probe.discoveryReload"));
        } catch (Throwable problem) { failure = problem; }
        finally { complete = true; }
    }

    private static Advancement advancement(MinecraftServer server, IRecipe<?> recipe) {
        Advancement result = server.getAdvancementManager().getAdvancement(new ResourceLocation("ironagefurniture", "recipes/upholstery/" + recipe.getId().getPath()));
        require(result != null, "Missing throne advancement");
        return result;
    }
    private static void reset(MinecraftServer server, ServerPlayerEntity player, IRecipe<?> recipe) {
        Advancement advancement = advancement(server, recipe);
        for (String criterion : advancement.getCriteria().keySet()) player.getAdvancements().revokeCriterion(advancement, criterion);
        player.resetRecipes(Collections.singleton(recipe));
    }
    private static long learned(ServerPlayerEntity player, List<IRecipe<?>> recipes) {
        return recipes.stream().filter(player.getRecipeBook()::isUnlocked).count();
    }
    private static void require(boolean condition, String message) { if (!condition) throw new IllegalStateException(message); }
}
