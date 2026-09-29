package zone.moddev.mc.ironagefurniture.api;

import zone.moddev.mc.ironagefurniture.IronAgeFurnitureConfiguration;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Loader;

public final class MineralogyCompat {
	private static final String MODID = "mineralogy";
	private static final ResourceLocation ROCK_SALT_LAMP_ID = new ResourceLocation(MODID, "rocksaltlamp");

	private MineralogyCompat() {
	}

	public static boolean isEnabled() {
		return IronAgeFurnitureConfiguration.INTEGRATION_MINERALOGY && Loader.isModLoaded(MODID);
	}

	public static Block getRockSaltLampBlock() {
		if (!isEnabled()) {
			return null;
		}

		Block block = Block.REGISTRY.getObject(ROCK_SALT_LAMP_ID);
		if (block == null || block == Blocks.AIR) {
			return null;
		}

		return block;
	}

	public static boolean isRockSaltLampItem(ItemStack stack) {
		if (stack == null || stack.stackSize <= 0) {
			return false;
		}

		Block block = getRockSaltLampBlock();
		return block != null && stack.getItem() == Item.getItemFromBlock(block);
	}

	public static ItemStack getRockSaltLampStack() {
		Block block = getRockSaltLampBlock();
		return block == null ? null : new ItemStack(block, 1);
	}
}
