package zone.moddev.mc.ironagefurniture.api.recipes;

import static org.junit.Assert.*;
import com.google.gson.JsonParser;
import io.netty.buffer.Unpooled;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Bootstrap;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.VanillaIngredientSerializer;
import org.junit.BeforeClass;
import org.junit.Test;
import zone.moddev.mc.ironagefurniture.api.ShieldChairItemData;
import zone.moddev.mc.ironagefurniture.api.ShieldChairItemDataTest;

public class ShieldChairRecipeTest {
    @BeforeClass public static void bootstrapMinecraft() {
        Bootstrap.register();
        CraftingHelper.register(new ResourceLocation("minecraft:item"), VanillaIngredientSerializer.INSTANCE);
    }

    private static ShieldChairRecipe recipe() {
        return new ShieldChairRecipe.Serializer().read(new ResourceLocation("ironagefurniture:test"),
                new JsonParser().parse("{\"ingredients\":[{\"item\":\"minecraft:shield\"},"
                        + "{\"item\":\"minecraft:oak_planks\"}],\"result\":{\"item\":\"minecraft:oak_planks\"}}")
                        .getAsJsonObject());
    }

    private static CraftingInventory grid() {
        return new CraftingInventory(new Container(null, 0) {
            @Override public boolean canInteractWith(PlayerEntity player) { return false; }
        }, 3, 3);
    }

    @Test public void craftingCopiesTheShieldAndConsumesOnlyTheExpectedIngredients() {
        ShieldChairRecipe recipe = recipe();
        CraftingInventory grid = grid();
        ItemStack shield = ShieldChairItemDataTest.decoratedShield();
        grid.setInventorySlotContents(0, shield);
        grid.setInventorySlotContents(8, new ItemStack(Items.OAK_PLANKS));
        assertTrue(recipe.matches(grid, null));
        assertEquals(shield.write(new net.minecraft.nbt.CompoundNBT()), ShieldChairItemData.getShield(
                recipe.getCraftingResult(grid)).write(new net.minecraft.nbt.CompoundNBT()));
        assertEquals(1, shield.getCount());
        assertTrue(recipe.getRemainingItems(grid).stream().allMatch(ItemStack::isEmpty));
        grid.setInventorySlotContents(4, new ItemStack(Items.STICK));
        assertFalse(recipe.matches(grid, null));
        grid.setInventorySlotContents(4, ItemStack.EMPTY);
        grid.setInventorySlotContents(8, new ItemStack(Items.BIRCH_PLANKS));
        assertFalse(recipe.matches(grid, null));
    }

    @Test public void recipeSynchronizesWithTheClientWithoutChangingItsGroupOrIngredients() {
        ShieldChairRecipe original = recipe();
        PacketBuffer buffer = new PacketBuffer(Unpooled.buffer());
        try {
            ShieldChairRecipe.Serializer serializer = new ShieldChairRecipe.Serializer();
            serializer.write(buffer, original);
            ShieldChairRecipe restored = serializer.read(original.getId(), buffer);
            assertEquals(original.getId(), restored.getId());
            assertEquals(original.getGroup(), restored.getGroup());
            assertEquals(2, restored.getIngredients().size());
            CraftingInventory grid = grid();
            grid.setInventorySlotContents(0, new ItemStack(Items.SHIELD));
            grid.setInventorySlotContents(1, new ItemStack(Items.OAK_PLANKS));
            assertTrue(restored.matches(grid, null));
        } finally { buffer.release(); }
    }
}
