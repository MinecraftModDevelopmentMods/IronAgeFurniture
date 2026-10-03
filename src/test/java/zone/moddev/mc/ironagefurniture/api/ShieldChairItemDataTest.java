package zone.moddev.mc.ironagefurniture.api;

import static org.junit.Assert.*;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.registry.Bootstrap;
import net.minecraft.util.text.StringTextComponent;
import org.junit.BeforeClass;
import org.junit.Test;

public class ShieldChairItemDataTest {
    @BeforeClass public static void bootstrapMinecraft() { Bootstrap.register(); }

    public static ItemStack decoratedShield() {
        ItemStack shield = new ItemStack(Items.SHIELD);
        shield.setDamage(23);
        shield.setDisplayName(new StringTextComponent("Family Crest"));
        shield.addEnchantment(Enchantments.UNBREAKING, 3);
        CompoundNBT banner = new CompoundNBT();
        banner.putInt("Base", 14);
        ListNBT patterns = new ListNBT();
        CompoundNBT pattern = new CompoundNBT();
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
        CompoundNBT original = shield.write(new CompoundNBT()).copy();
        ItemStack chair = ShieldChairItemData.createChair(Blocks.OAK_PLANKS, shield);
        ItemStack restored = ShieldChairItemData.getShield(chair);
        assertEquals(original, restored.write(new CompoundNBT()));
        shield.setDamage(100);
        restored.getOrCreateTag().putString("AnotherModsData", "changed");
        assertEquals(original, ShieldChairItemData.getShield(chair).write(new CompoundNBT()));
    }

    @Test public void oldChairsKeepTheirPlainShieldAndEmptyFramesStayEmpty() {
        assertEquals(Items.SHIELD, ShieldChairItemData.getShield(new ItemStack(Blocks.OAK_PLANKS)).getItem());
        ItemStack empty = ShieldChairItemData.createChair(Blocks.OAK_PLANKS, ItemStack.EMPTY);
        assertTrue(ShieldChairItemData.isEmptyFrame(empty));
        assertTrue(ShieldChairItemData.getShield(empty).isEmpty());
        assertTrue(ShieldChairItemData.getShield(ItemStack.read(empty.write(new CompoundNBT()))).isEmpty());
    }

    @Test public void normalizesCountAndRejectsNonShields() {
        ItemStack shield = decoratedShield();
        shield.setCount(12);
        assertEquals(1, ShieldChairItemData.getShield(ShieldChairItemData.createChair(Blocks.OAK_PLANKS, shield)).getCount());
        assertEquals(12, shield.getCount());
        assertTrue(ShieldChairItemData.getShield(ShieldChairItemData.createChair(Blocks.OAK_PLANKS,
                new ItemStack(Items.DIAMOND))).isEmpty());
        CompoundNBT invalid = new CompoundNBT();
        invalid.put("Shield", new ItemStack(Items.DIAMOND).write(new CompoundNBT()));
        assertEquals(Items.SHIELD, ShieldChairItemData.readShield(invalid).getItem());
    }

    @Test public void importsPreFlatteningDamageEnchantmentAndBannerDataIdempotently() {
        CompoundNBT oldShield = new CompoundNBT();
        oldShield.putString("id", "minecraft:shield");
        oldShield.putByte("Count", (byte) 1);
        oldShield.putShort("Damage", (short) 47);
        CompoundNBT oldTag = new CompoundNBT();
        oldTag.put("BlockEntityTag", decoratedShield().getChildTag("BlockEntityTag").copy());
        ListNBT enchantments = new ListNBT();
        CompoundNBT unbreaking = new CompoundNBT();
        unbreaking.putShort("id", (short) 34);
        unbreaking.putShort("lvl", (short) 3);
        enchantments.add(unbreaking);
        oldTag.put("ench", enchantments);
        oldTag.putString("AnotherModsData", "preserved");
        oldShield.put("tag", oldTag);
        CompoundNBT legacy = new CompoundNBT();
        legacy.put("Shield", oldShield);
        CompoundNBT before = legacy.copy();
        ItemStack migrated = ShieldChairItemData.readShield(legacy);
        assertEquals(47, migrated.getDamage());
        assertEquals("minecraft:unbreaking", migrated.getEnchantmentTagList().getCompound(0).getString("id"));
        // Before flattening, banner dye numbers run in the opposite order.
        assertEquals(1, migrated.getChildTag("BlockEntityTag").getInt("Base"));
        assertEquals(11, migrated.getChildTag("BlockEntityTag").getList("Patterns", 10).getCompound(0).getInt("Color"));
        assertEquals("cre", migrated.getChildTag("BlockEntityTag").getList("Patterns", 10).getCompound(0).getString("Pattern"));
        assertEquals("preserved", migrated.getTag().getString("AnotherModsData"));
        assertEquals(before, legacy);
        assertEquals(migrated.write(new CompoundNBT()), ShieldChairItemData.readShield(
                ShieldChairItemData.writeShield(migrated)).write(new CompoundNBT()));
    }
}
