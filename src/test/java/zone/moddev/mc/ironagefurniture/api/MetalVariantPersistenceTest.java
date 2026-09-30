package zone.moddev.mc.ironagefurniture.api;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import net.minecraft.nbt.NBTTagCompound;
import org.junit.BeforeClass;
import org.junit.Test;
import net.minecraftforge.fml.common.registry.GameRegistry;
import zone.moddev.mc.ironagefurniture.api.MetalVariantHelper.MetalVariant;
import zone.moddev.mc.ironagefurniture.api.tile.TileEntityMetalVariant;

public class MetalVariantPersistenceTest {
    @BeforeClass public static void registerTileEntity() {
        GameRegistry.registerTileEntity(TileEntityMetalVariant.class,
                "ironagefurniture:sconce_metal_test");
    }

    @Test public void knownMetalIdentitySurvivesWithoutBaseMetalsLoaded() {
        assertFalse(MetalVariant.ADAMANTINE.isAvailable());
        assertEquals(MetalVariant.ADAMANTINE, MetalVariant.byMeta(2));
        assertEquals(MetalVariant.ADAMANTINE, MetalVariant.byName("adamantine"));

        TileEntityMetalVariant original = new TileEntityMetalVariant();
        original.setMetal(MetalVariant.ADAMANTINE);
        NBTTagCompound data = original.writeToNBT(new NBTTagCompound());
        assertEquals("adamantine", data.getString("Metal"));

        TileEntityMetalVariant restored = new TileEntityMetalVariant();
        restored.readFromNBT(data);
        assertEquals(MetalVariant.ADAMANTINE, restored.getMetal());
    }
}
