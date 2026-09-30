package zone.moddev.mc.ironagefurniture.client.model;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import zone.moddev.mc.ironagefurniture.api.Enumerations.UpholsteryColour;
import zone.moddev.mc.ironagefurniture.init.ClientModelInitialiser;

public class UpholsteryModelContractTest {
    @Test
    public void furnitureItemsSelectDistinctInventoryModels() {
        String name = "chair_wood_ironage_wingback_oak";
        for (UpholsteryColour colour : UpholsteryColour.values()) {
            assertEquals("ironagefurniture:upholstered/" + colour.getSerializedName()
                            + "/" + name + "#inventory",
                    ClientModelInitialiser.upholsteryItemModelLocation(name, colour).toString());
        }
    }

    @Test
    public void inventoryModelNamesCoverChairsAndBothBedForms() {
        assertEquals("chair_wood_ironage_wingback_oak_inventory",
                UpholsteryModelLoader.inventoryModelName("chair_wood_ironage_wingback_oak"));
        assertEquals("chair_wood_ironage_throne_oak_inventory",
                UpholsteryModelLoader.inventoryModelName("chair_wood_ironage_throne_oak"));
        assertEquals("bed_wood_single_inventory_oak",
                UpholsteryModelLoader.inventoryModelName("bed_wood_foot_oak"));
        assertEquals("bed_wood_double_inventory_oak",
                UpholsteryModelLoader.inventoryModelName("bed_wood_foot_left_oak"));
    }
}
