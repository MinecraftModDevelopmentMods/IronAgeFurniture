package zone.moddev.mc.ironagefurniture.migration;

import static org.junit.Assert.*;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.registry.Bootstrap;
import org.junit.BeforeClass;
import org.junit.Test;
import zone.moddev.mc.ironagefurniture.api.blocks.lightholder.LightHolderSconceFloor;
import zone.moddev.mc.ironagefurniture.api.enumerations.SconceMetal;
import zone.moddev.mc.ironagefurniture.api.SconceMetalData;
import zone.moddev.mc.ironagefurniture.api.recipes.SconceMetalCondition;

public class SconceMetalDataTest {
    @BeforeClass public static void bootstrap() { Bootstrap.register(); }
    @Test public void everyLegacyMetalKeepsItsStableNumberNameAndItemData() {
        String[] names = {"iron", "gold", "adamantine", "antimony", "aquarium", "bismuth", "brass", "bronze", "coldiron",
                "copper", "cupronickel", "electrum", "invar", "lead", "mithril", "nickel", "pewter", "platinum",
                "silver", "starsteel", "steel", "tin", "zinc"};
        for (int index = 0; index < names.length; index++) {
            SconceMetal metal = SconceMetal.byLegacyMetadata(index);
            assertEquals(names[index], metal.getName());
            assertEquals(metal, SconceMetal.byName(names[index]));
            ItemStack stack = SconceMetalData.set(new ItemStack(Blocks.IRON_BLOCK, 3), metal);
            stack.getTag().putString("OtherData", "keep");
            ItemStack copy = ItemStack.read(stack.write(new CompoundNBT()));
            assertEquals(metal, SconceMetalData.get(copy));
            assertEquals(3, copy.getCount());
            assertEquals("keep", copy.getTag().getString("OtherData"));
        }
        assertEquals(SconceMetal.IRON, SconceMetal.byName("missing"));
        assertEquals(SconceMetal.IRON, SconceMetal.byLegacyMetadata(-1));
        assertEquals(SconceMetal.IRON, SconceMetal.byLegacyMetadata(23));
    }
    @Test public void unavailableMetalsRemainValidSavedStatesAndDefaultsAreIron() {
        LightHolderSconceFloor block = new LightHolderSconceFloor(net.minecraft.block.Block.Properties.from(Blocks.IRON_BLOCK));
        assertEquals(SconceMetal.IRON, SconceMetalData.get(block.getDefaultState()));
        assertEquals(184, block.getStateContainer().getValidStates().size());
        for (SconceMetal metal : SconceMetal.values()) {
            BlockState state = block.getDefaultState().with(SconceMetalData.METAL, metal);
            assertEquals(metal, SconceMetalData.get(SconceMetalData.preserve(state, block.getDefaultState())));
            assertEquals(metal.hardness(Blocks.IRON_BLOCK.getDefaultState().getBlockHardness(null, null)),
                    state.getBlockHardness(null, null), .001F);
            if (metal != SconceMetal.IRON && metal != SconceMetal.GOLD) assertFalse(SconceMetalData.available(metal));
        }
    }
    @Test public void migrationPrefersMetalNbtAndIsIdempotentInNestedContainers() {
        for (SconceMetal metal : SconceMetal.values()) {
            CompoundNBT stack = new CompoundNBT();
            stack.putString("id", "ironagefurniture:light_metal_ironage_sconce_floor_empty_iron");
            stack.putByte("Count", (byte) 5); stack.putShort("Damage", (short) metal.ordinal());
            ListNBT contents = new ListNBT(); contents.add(stack);
            CompoundNBT container = new CompoundNBT(); container.put("Items", contents);
            assertTrue(LegacyPaddedItemMigration.migrateChunkContents(container));
            assertEquals(metal.getName(), stack.getCompound("tag").getString("Metal"));
            assertEquals(5, stack.getByte("Count")); assertFalse(stack.contains("Damage"));
            assertFalse(LegacyPaddedItemMigration.migrateChunkContents(container));
            stack.putShort("Damage", (short) 0); stack.getCompound("tag").putString("OtherData", "keep");
            assertTrue(SconceMetalData.migrateItem(stack));
            assertEquals(metal.getName(), stack.getCompound("tag").getString("Metal"));
            assertEquals("keep", stack.getCompound("tag").getString("OtherData"));
        }
    }
    @Test public void recipeConditionsRoundTripAndRejectUnknownMetals() {
        SconceMetalCondition.Serializer serializer = new SconceMetalCondition.Serializer();
        for (SconceMetal metal : SconceMetal.values()) {
            JsonObject json = new JsonObject(); serializer.write(json, new SconceMetalCondition(metal));
            assertEquals(metal.getName(), json.get("metal").getAsString());
            assertEquals(metal == SconceMetal.IRON || metal == SconceMetal.GOLD, serializer.read(json).test());
        }
        JsonObject unknown = new JsonObject(); unknown.addProperty("metal", "misspelled");
        try { serializer.read(unknown); fail("Unknown recipe metal accepted"); }
        catch (JsonParseException expected) { assertTrue(expected.getMessage().contains("misspelled")); }
    }
    @Test public void playerInventoryMigrationReadsBothPreAndPostFlatteningDamage() {
        for (SconceMetal metal : SconceMetal.values()) {
            CompoundNBT player = new CompoundNBT(), stack = new CompoundNBT(), tag = new CompoundNBT();
            stack.putString("id", "ironagefurniture:light_metal_ironage_sconce_floor_empty_iron");
            stack.putByte("Count", (byte) 3); tag.putInt("Damage", metal.ordinal()); tag.putString("Keep", "saved"); stack.put("tag", tag);
            ListNBT inventory = new ListNBT(); inventory.add(stack); player.put("Inventory", inventory);
            LegacyWorldDataHook.preparePlayerData(player);
            assertEquals(metal.getName(), tag.getString("Metal")); assertFalse(tag.contains("Damage"));
            assertEquals("saved", tag.getString("Keep"));
            assertFalse(LegacyPaddedItemMigration.migrateChunkContents(player));
        }
    }
}
