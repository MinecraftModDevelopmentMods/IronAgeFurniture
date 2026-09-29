package zone.moddev.mc.ironagefurniture.api;

import static org.junit.Assert.*;

import java.lang.reflect.Proxy;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;
import zone.moddev.mc.ironagefurniture.api.Blocks.ShieldChair;
import zone.moddev.mc.ironagefurniture.api.tile.TileEntityShieldChair;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.init.Bootstrap;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ShieldChairItemDataTest {
	@BeforeClass public static void bootstrap() {
		Bootstrap.register();
		GameRegistry.registerTileEntity(TileEntityShieldChair.class, "ironagefurniture:shield_chair_test");
	}

	private ItemStack decoratedShield() {
		ItemStack shield = new ItemStack(Items.SHIELD);
		shield.setItemDamage(71);
		shield.setStackDisplayName("Innkeeper's shield");
		NBTTagCompound tag = shield.getTagCompound();
		NBTTagCompound banner = new NBTTagCompound();
		banner.setInteger("Base", 5);
		tag.setTag("BlockEntityTag", banner);
		NBTTagCompound enchantment = new NBTTagCompound();
		enchantment.setString("sentinel", "preserve-all-nbt");
		tag.setTag("CustomTestData", enchantment);
		return shield;
	}

	@Test public void oldChairItemAndTileDefaultToPlainShield() {
		ItemStack oldItem = new ItemStack(Blocks.PLANKS);
		assertFalse(ShieldChairItemData.isEmptyFrame(oldItem));
		ItemStack recovered = ShieldChairItemData.getShield(oldItem);
		assertSame(Items.SHIELD, recovered.getItem());
		assertEquals(0, recovered.getItemDamage());
		assertTrue(new TileEntityShieldChair().hasShield());
	}

	@Test public void allOldFacingMetadataValuesRemainUnchanged() {
		ShieldChair chair = new ShieldChair(Material.WOOD, "test_shield_chair", 10, 1);
		for (int oldMeta = 0; oldMeta < 4; oldMeta++)
			assertEquals(oldMeta, chair.getMetaFromState(chair.getStateFromMeta(oldMeta)));
	}

	@Test public void emptyMarkerSurvivesItemAndTileRoundTrip() {
		ItemStack frame = ShieldChairItemData.createChair(Blocks.PLANKS, null);
		assertTrue(ShieldChairItemData.isEmptyFrame(frame));
		assertNull(ShieldChairItemData.getShield(frame));
		TileEntityShieldChair tile = new TileEntityShieldChair();
		tile.setShield(null);
		NBTTagCompound saved = tile.writeToNBT(new NBTTagCompound());
		TileEntityShieldChair loaded = new TileEntityShieldChair();
		loaded.readFromNBT(saved);
		assertFalse(loaded.hasShield());
	}

	@Test public void decoratedShieldIsCopiedWithoutDataLoss() {
		ItemStack original = decoratedShield();
		ItemStack chair = ShieldChairItemData.createChair(Blocks.PLANKS, original);
		assertFalse(ShieldChairItemData.isEmptyFrame(chair));
		ItemStack recovered = ShieldChairItemData.getShield(chair);
		assertEquals(original.writeToNBT(new NBTTagCompound()), recovered.writeToNBT(new NBTTagCompound()));
		recovered.setItemDamage(1);
		assertEquals(71, original.getItemDamage());

		TileEntityShieldChair tile = new TileEntityShieldChair();
		tile.setShield(original);
		NBTTagCompound saved = tile.writeToNBT(new NBTTagCompound());
		TileEntityShieldChair loaded = new TileEntityShieldChair();
		loaded.readFromNBT(saved);
		assertEquals(original.writeToNBT(new NBTTagCompound()),
				loaded.getShield().writeToNBT(new NBTTagCompound()));
	}

	@Test public void breakingReturnsFrameAndOnlyTheStoredShield() {
		ShieldChair chair = new ShieldChair(Material.WOOD, "test_shield_chair", 10, 1);
		TileEntityShieldChair tile = new TileEntityShieldChair();
		ItemStack decorated = decoratedShield();
		tile.setShield(decorated);
		IBlockAccess access = (IBlockAccess)Proxy.newProxyInstance(IBlockAccess.class.getClassLoader(),
				new Class<?>[] { IBlockAccess.class }, (proxy, method, args) ->
						"getTileEntity".equals(method.getName()) ? tile : null);
		List<ItemStack> full = chair.getDrops(access, BlockPos.ORIGIN, chair.getDefaultState(), 0);
		assertEquals(2, full.size());
		assertEquals(decorated.writeToNBT(new NBTTagCompound()),
				full.get(1).writeToNBT(new NBTTagCompound()));
		assertNull(ShieldChairItemData.getShield(full.get(0)));
		tile.setShield(null);
		List<ItemStack> empty = chair.getDrops(access, BlockPos.ORIGIN, chair.getDefaultState(), 0);
		assertEquals(1, empty.size());
		assertNull(ShieldChairItemData.getShield(empty.get(0)));
		IBlockAccess legacy = (IBlockAccess)Proxy.newProxyInstance(IBlockAccess.class.getClassLoader(),
				new Class<?>[] { IBlockAccess.class }, (proxy, method, args) -> null);
		assertSame(Items.SHIELD,
				chair.getDrops(legacy, BlockPos.ORIGIN, chair.getDefaultState(), 0).get(1).getItem());
	}
}
