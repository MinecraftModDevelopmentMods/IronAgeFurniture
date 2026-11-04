package zone.moddev.mc.ironagefurniture.api;

import static org.junit.Assert.*;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.Bootstrap;
import net.minecraft.network.chat.TextComponent;
import org.junit.BeforeClass;
import org.junit.Test;

public class ShieldChairItemDataTest {
    @BeforeClass public static void bootstrapMinecraft() { net.minecraft.SharedConstants.tryDetectVersion(); Bootstrap.bootStrap(); }

    public static ItemStack decoratedShield() {
        ItemStack shield = new ItemStack(Items.SHIELD);
        shield.setDamageValue(23);
        shield.setHoverName(new TextComponent("Family Crest"));
        shield.enchant(Enchantments.UNBREAKING, 3);
        CompoundTag banner = new CompoundTag();
        banner.putInt("Base", 14);
        ListTag patterns = new ListTag();
        CompoundTag pattern = new CompoundTag();
        pattern.putString("Pattern", "cre");
        pattern.putInt("Color", 4);
        patterns.add(pattern);
        banner.put("Patterns", patterns);
        shield.getOrCreateTag().put("BlockEntityTag", banner);
        shield.getOrCreateTag().putString("AnotherModsData", "keep this too");
        return shield;
    }

    @Test public void preservesCompleteShieldAndDoesNotShareMutableNbt() {
        ItemStack shield = decoratedShield();
        CompoundTag original = shield.save(new CompoundTag()).copy();
        ItemStack chair = ShieldChairItemData.createChair(Blocks.OAK_PLANKS, shield);
        ItemStack restored = ShieldChairItemData.getShield(chair);
        assertEquals(original, restored.save(new CompoundTag()));
        shield.setDamageValue(100);
        restored.getOrCreateTag().putString("AnotherModsData", "changed");
        assertEquals(original, ShieldChairItemData.getShield(chair).save(new CompoundTag()));
    }

    @Test public void oldChairsKeepTheirPlainShieldAndEmptyFramesStayEmpty() {
        assertEquals(Items.SHIELD, ShieldChairItemData.getShield(new ItemStack(Blocks.OAK_PLANKS)).getItem());
        ItemStack empty = ShieldChairItemData.createChair(Blocks.OAK_PLANKS, ItemStack.EMPTY);
        assertTrue(ShieldChairItemData.getShield(empty).isEmpty());
        assertTrue(ShieldChairItemData.getShield(empty).isEmpty());
        assertTrue(ShieldChairItemData.getShield(ItemStack.of(empty.save(new CompoundTag()))).isEmpty());
    }

    @Test public void normalizesCountWithoutConsumingTheInput() {
        ItemStack shield = decoratedShield(); shield.setCount(12);
        assertEquals(1, ShieldChairItemData.getShield(ShieldChairItemData.createChair(Blocks.OAK_PLANKS, shield)).getCount());
        assertEquals(12, shield.getCount());
    }
}
