package zone.moddev.mc.ironagefurniture.migration;

import static org.junit.Assert.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import org.junit.Test;
import zone.moddev.mc.ironagefurniture.api.enumerations.UpholsteryColour;

public class LegacyRecipeBookMigrationTest {
    @Test public void namesPreserveSupportedWoodsAndLeaveUnavailableAddonsAlone() {
        for (UpholsteryColour colour : UpholsteryColour.values()) {
            assertEquals("ironagefurniture:chair_wood_ironage_bench_back_padded_"+colour.getSerializedName()+"_single_dark_oak",
                    LegacyRecipeBookMigration.currentRecipe("ironagefurniture:chair_wood_ironage_bench_back_padded_single_big_oak_"+colour.ordinal()));
            assertEquals("iafbygaddon:bed_wood_foot_byg_palm_"+colour.getSerializedName(),
                    LegacyRecipeBookMigration.currentRecipe("iafbygaddon:bed_wood_foot_byg_palm_"+colour.getSerializedName()));
        }
        assertEquals("ironagefurniture:bed_wood_foot_dark_oak", LegacyRecipeBookMigration.currentRecipe("ironagefurniture:bed_wood_foot_big_oak"));
    }
    @Test public void oldAndNewRecolourEntriesStayHiddenWithoutTouchingOtherMods() {
        CompoundTag player = new CompoundTag(), book = new CompoundTag();
        ListTag recipes = new ListTag();
        recipes.add(StringTag.valueOf("ironagefurniture:bed_wood_foot_oak_recolour"));
        recipes.add(StringTag.valueOf("ironagefurniture:bed_wood_foot_oak_recolour_blue"));
        recipes.add(StringTag.valueOf("minecraft:sign"));
        recipes.add(StringTag.valueOf("other:bed_recolour"));
        recipes.add(StringTag.valueOf("ironagefurniture:bed_wood_foot_big_oak"));
        book.put("recipes", recipes.copy()); book.put("toBeDisplayed", recipes.copy()); player.put("recipeBook", book);
        LegacyRecipeBookMigration.prepare(player);
        assertEquals(3, book.getList("recipes", 8).size());
        assertEquals("minecraft:oak_sign", book.getList("recipes", 8).getString(0));
        assertEquals("other:bed_recolour", book.getList("recipes", 8).getString(1));
        assertEquals("ironagefurniture:bed_wood_foot_dark_oak", book.getList("recipes", 8).getString(2));
        CompoundTag once = player.copy(); LegacyRecipeBookMigration.prepare(player); assertEquals(once, player);
        assertEquals(book.get("recipes"), book.get("toBeDisplayed"));
    }
}
