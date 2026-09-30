package zone.moddev.mc.ironagefurniture.api;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import net.minecraft.init.Items;
import net.minecraft.init.Blocks;
import net.minecraft.init.Bootstrap;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.block.Block;
import org.junit.Test;
import org.junit.BeforeClass;

public class ShieldChairItemDataTest {
    private static Block chair;

    @BeforeClass
    public static void bootstrapMinecraft() {
        Bootstrap.register();
        chair = Blocks.PLANKS;
    }

    @Test
    public void preservesTheCompleteShieldStack() {
        ItemStack shield = new ItemStack(Items.SHIELD);
        shield.setItemDamage(23);
        shield.setStackDisplayName("Family Crest");
        NBTTagCompound pattern = new NBTTagCompound();
        pattern.setString("Base", "14");
        shield.setTagInfo("BlockEntityTag", pattern);

        ItemStack chairStack = ShieldChairItemData.createChair(chair, shield);
        ItemStack restored = ShieldChairItemData.getShield(chairStack);
        assertFalse(ShieldChairItemData.isEmptyFrame(chairStack));
        assertEquals(shield.writeToNBT(new NBTTagCompound()),
                restored.writeToNBT(new NBTTagCompound()));
        assertEquals(1, restored.getCount());
    }

    @Test
    public void oldUntaggedChairsHavePlainShieldsButEmptyFramesStayEmpty() {
        ItemStack oldChair = new ItemStack(chair);
        ItemStack oldShield = ShieldChairItemData.getShield(oldChair);
        assertEquals(Items.SHIELD, oldShield.getItem());
        assertFalse(oldShield.hasTagCompound());

        ItemStack empty = ShieldChairItemData.createChair(chair, ItemStack.EMPTY);
        assertTrue(ShieldChairItemData.isEmptyFrame(empty));
        assertTrue(ShieldChairItemData.getShield(empty).isEmpty());
    }
}
