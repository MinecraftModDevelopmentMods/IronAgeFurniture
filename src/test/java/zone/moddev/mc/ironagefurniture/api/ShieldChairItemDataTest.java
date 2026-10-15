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
        assertTrue(ShieldChairItemData.isEmptyFrame(empty));
        assertTrue(ShieldChairItemData.getShield(empty).isEmpty());
        assertTrue(ShieldChairItemData.getShield(ItemStack.of(empty.save(new CompoundTag()))).isEmpty());
    }

    @Test public void normalizesCountAndRejectsNonShields() {
        ItemStack shield = decoratedShield();
        shield.setCount(12);
        assertEquals(1, ShieldChairItemData.getShield(ShieldChairItemData.createChair(Blocks.OAK_PLANKS, shield)).getCount());
        assertEquals(12, shield.getCount());
        assertTrue(ShieldChairItemData.getShield(ShieldChairItemData.createChair(Blocks.OAK_PLANKS,
                new ItemStack(Items.DIAMOND))).isEmpty());
        CompoundTag invalid = new CompoundTag();
        invalid.put("Shield", new ItemStack(Items.DIAMOND).save(new CompoundTag()));
        assertEquals(Items.SHIELD, ShieldChairItemData.readShield(invalid).getItem());
    }

    @Test public void importsPreFlatteningDamageEnchantmentAndBannerDataIdempotently() {
        CompoundTag oldShield = new CompoundTag();
        oldShield.putString("id", "minecraft:shield");
        oldShield.putByte("Count", (byte) 1);
        oldShield.putShort("Damage", (short) 47);
        CompoundTag oldTag = new CompoundTag();
        oldTag.put("BlockEntityTag", decoratedShield().getTagElement("BlockEntityTag").copy());
        ListTag enchantments = new ListTag();
        CompoundTag unbreaking = new CompoundTag();
        unbreaking.putShort("id", (short) 34);
        unbreaking.putShort("lvl", (short) 3);
        enchantments.add(unbreaking);
        oldTag.put("ench", enchantments);
        oldTag.putString("AnotherModsData", "preserved");
        oldShield.put("tag", oldTag);
        CompoundTag legacy = new CompoundTag();
        legacy.put("Shield", oldShield);
        CompoundTag before = legacy.copy();
        ItemStack migrated = ShieldChairItemData.readShield(legacy);
        assertEquals(47, migrated.getDamageValue());
        assertEquals("minecraft:unbreaking", migrated.getEnchantmentTags().getCompound(0).getString("id"));
        // Before flattening, banner dye numbers run in the opposite order.
        assertEquals(1, migrated.getTagElement("BlockEntityTag").getInt("Base"));
        assertEquals(11, migrated.getTagElement("BlockEntityTag").getList("Patterns", 10).getCompound(0).getInt("Color"));
        assertEquals("cre", migrated.getTagElement("BlockEntityTag").getList("Patterns", 10).getCompound(0).getString("Pattern"));
        assertEquals("preserved", migrated.getTag().getString("AnotherModsData"));
        assertEquals(before, legacy);
        assertEquals(migrated.save(new CompoundTag()), ShieldChairItemData.readShield(
                ShieldChairItemData.writeShield(migrated)).save(new CompoundTag()));
    }
}
