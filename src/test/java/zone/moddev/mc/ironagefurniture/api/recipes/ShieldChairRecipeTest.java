package zone.moddev.mc.ironagefurniture.api.recipes;

import static org.junit.Assert.*;
import com.google.gson.JsonParser;
import io.netty.buffer.Unpooled;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.Bootstrap;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.VanillaIngredientSerializer;
import org.junit.BeforeClass;
import org.junit.Test;
import zone.moddev.mc.ironagefurniture.api.ShieldChairItemData;
import zone.moddev.mc.ironagefurniture.api.ShieldChairItemDataTest;

public class ShieldChairRecipeTest {
    @BeforeClass public static void bootstrapMinecraft() {
        net.minecraft.SharedConstants.tryDetectVersion(); Bootstrap.bootStrap();
        CraftingHelper.register(new ResourceLocation("minecraft:item"), VanillaIngredientSerializer.INSTANCE);
    }

    private static ShieldChairRecipe recipe() {
        return ShieldChairRecipe.SERIALIZER.fromJson(new ResourceLocation("ironagefurniture:test"),
                new JsonParser().parse("{\"ingredients\":[{\"item\":\"minecraft:shield\"},"
                        + "{\"item\":\"minecraft:oak_planks\"}],\"result\":{\"item\":\"minecraft:oak_planks\"}}")
                        .getAsJsonObject());
    }

    private static CraftingContainer grid() {
        return new CraftingContainer(new AbstractContainerMenu(null, 0) {
            @Override public boolean stillValid(Player player) { return false; }
        }, 3, 3);
    }

    @Test public void craftingCopiesTheShieldAndConsumesOnlyTheExpectedIngredients() {
        ShieldChairRecipe recipe = recipe();
        CraftingContainer grid = grid();
        ItemStack shield = ShieldChairItemDataTest.decoratedShield();
        grid.setItem(0, shield);
        grid.setItem(8, new ItemStack(Items.OAK_PLANKS));
        assertTrue(recipe.matches(grid, null));
        assertEquals(shield.save(new net.minecraft.nbt.CompoundTag()), ShieldChairItemData.getShield(
                recipe.assemble(grid)).save(new net.minecraft.nbt.CompoundTag()));
        assertEquals(1, shield.getCount());
        assertTrue(recipe.getRemainingItems(grid).stream().allMatch(ItemStack::isEmpty));
        grid.setItem(4, new ItemStack(Items.STICK));
        assertFalse(recipe.matches(grid, null));
        grid.setItem(4, ItemStack.EMPTY);
        grid.setItem(8, new ItemStack(Items.BIRCH_PLANKS));
        assertFalse(recipe.matches(grid, null));
    }

    @Test public void recipeSynchronizesWithTheClientWithoutChangingItsGroupOrIngredients() {
        ShieldChairRecipe original = recipe();
        FriendlyByteBuf buffer = new FriendlyByteBuf(Unpooled.buffer());
        try {
            net.minecraft.world.item.crafting.RecipeSerializer<ShieldChairRecipe> serializer = ShieldChairRecipe.SERIALIZER;
            serializer.toNetwork(buffer, original);
            ShieldChairRecipe restored = serializer.fromNetwork(original.getId(), buffer);
            assertEquals(original.getId(), restored.getId());
            assertEquals(original.getGroup(), restored.getGroup());
            assertEquals(2, restored.getIngredients().size());
            CraftingContainer grid = grid();
            grid.setItem(0, new ItemStack(Items.SHIELD));
            grid.setItem(1, new ItemStack(Items.OAK_PLANKS));
            assertTrue(restored.matches(grid, null));
        } finally { buffer.release(); }
    }
}
