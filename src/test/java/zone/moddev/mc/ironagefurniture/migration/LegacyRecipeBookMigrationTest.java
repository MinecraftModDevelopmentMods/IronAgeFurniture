package zone.moddev.mc.ironagefurniture.migration;

import static org.junit.Assert.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import org.junit.Test;
import zone.moddev.mc.ironagefurniture.api.enumerations.UpholsteryColour;

public class LegacyRecipeBookMigrationTest {
    @Test public void namesPreserveWoodsColoursAndRetiredAddOnFallbacks() {
        for (UpholsteryColour colour : UpholsteryColour.values()) {
            assertEquals("ironagefurniture:chair_wood_ironage_bench_back_padded_"+colour.getSerializedName()+"_single_dark_oak",
                    LegacyRecipeBookMigration.currentRecipe("ironagefurniture:chair_wood_ironage_bench_back_padded_single_big_oak_"+colour.ordinal()));
            assertEquals("ironagefurniture:bed_wood_foot_byg_palm"+(colour == UpholsteryColour.RED ? "" : "_"+colour.getSerializedName()),
                    LegacyRecipeBookMigration.currentRecipe("iafbygaddon:bed_wood_foot_byg_palm_"+colour.getSerializedName()));
        }
        assertEquals("ironagefurniture:bed_wood_foot_dark_oak", LegacyRecipeBookMigration.currentRecipe("ironagefurniture:bed_wood_foot_big_oak"));
    }
    @Test public void oldAndNewRecolourEntriesStayHiddenWithoutTouchingOtherMods() {
        CompoundNBT player = new CompoundNBT(), book = new CompoundNBT();
        ListNBT recipes = new ListNBT();
        recipes.add(StringNBT.valueOf("ironagefurniture:bed_wood_foot_oak_recolour"));
        recipes.add(StringNBT.valueOf("ironagefurniture:bed_wood_foot_oak_recolour_blue"));
        recipes.add(StringNBT.valueOf("minecraft:sign"));
        recipes.add(StringNBT.valueOf("other:bed_recolour"));
        recipes.add(StringNBT.valueOf("ironagefurniture:bed_wood_foot_big_oak"));
        book.put("recipes", recipes.copy()); book.put("toBeDisplayed", recipes.copy()); player.put("recipeBook", book);
        LegacyRecipeBookMigration.prepare(player);
        assertEquals(3, book.getList("recipes", 8).size());
        assertEquals("minecraft:oak_sign", book.getList("recipes", 8).getString(0));
        assertEquals("other:bed_recolour", book.getList("recipes", 8).getString(1));
        assertEquals("ironagefurniture:bed_wood_foot_dark_oak", book.getList("recipes", 8).getString(2));
        CompoundNBT once = player.copy(); LegacyRecipeBookMigration.prepare(player); assertEquals(once, player);
        assertEquals(book.get("recipes"), book.get("toBeDisplayed"));
    }
}
