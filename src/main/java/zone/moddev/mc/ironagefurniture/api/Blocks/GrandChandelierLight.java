package zone.moddev.mc.ironagefurniture.api.Blocks;

import zone.moddev.mc.ironagefurniture.BlockObjectHolder;
import zone.moddev.mc.ironagefurniture.api.MineralogyCompat;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;

public enum GrandChandelierLight implements IStringSerializable {
	EMPTY("empty", 0),
	TORCH("torch", 14),
	TORCH_UNLIT("torch_unlit", 0),
	TORCH_TWIN("torch_twin", 15),
	TORCH_TWIN_UNLIT("torch_twin_unlit", 0),
	REDTORCH("redtorch", 8),
	REDTORCH_UNLIT("redtorch_unlit", 0),
	CANDLE_1("candle_1", 12),
	CANDLE_2("candle_2", 13),
	CANDLE_3("candle_3", 14),
	CANDLE_4("candle_4", 15),
	CANDLE_1_UNLIT("candle_1_unlit", 0),
	CANDLE_2_UNLIT("candle_2_unlit", 0),
	CANDLE_3_UNLIT("candle_3_unlit", 0),
	CANDLE_4_UNLIT("candle_4_unlit", 0),
	GLOW("glow", 14),
	LAVA("lava", 14),
	ROCK_SALT("rock_salt", 15),
	RED_0("red_0", 0),
	RED_1("red_1", 1),
	RED_2("red_2", 2),
	RED_3("red_3", 3),
	RED_4("red_4", 4),
	RED_5("red_5", 5),
	RED_6("red_6", 6),
	RED_7("red_7", 7),
	RED_8("red_8", 8),
	RED_9("red_9", 9),
	RED_10("red_10", 10),
	RED_11("red_11", 11),
	RED_12("red_12", 12),
	RED_13("red_13", 13),
	RED_14("red_14", 14),
	RED_15("red_15", 15);

	private final String name;
	private final int lightLevel;

	private GrandChandelierLight(String name, int lightLevel) {
		this.name = name;
		this.lightLevel = lightLevel;
	}

	@Override
	public String getName() {
		return this.name;
	}

	public int getLightLevel() {
		return this.lightLevel;
	}

	public boolean isLitTorch() {
		return this == TORCH || this == TORCH_TWIN;
	}

	public boolean isUnlitTorch() {
		return this == TORCH_UNLIT || this == TORCH_TWIN_UNLIT;
	}

	public boolean isTorchFamily() {
		return isLitTorch() || isUnlitTorch();
	}

	public boolean isTwinTorch() {
		return this == TORCH_TWIN || this == TORCH_TWIN_UNLIT;
	}

	public boolean isRedTorchFamily() {
		return this == REDTORCH || this == REDTORCH_UNLIT;
	}

	public boolean isCandle() {
		return this.name.startsWith("candle_");
	}

	public boolean isLitCandle() {
		return this == CANDLE_1 || this == CANDLE_2 || this == CANDLE_3 || this == CANDLE_4;
	}

	public boolean isUnlitCandle() {
		return isCandle() && !isLitCandle();
	}

	public int getCandleCount() {
		switch (this) {
			case CANDLE_2:
			case CANDLE_2_UNLIT:
				return 2;
			case CANDLE_3:
			case CANDLE_3_UNLIT:
				return 3;
			case CANDLE_4:
			case CANDLE_4_UNLIT:
				return 4;
			case CANDLE_1:
			case CANDLE_1_UNLIT:
				return 1;
			default:
				return 0;
		}
	}

	public boolean isRedLamp() {
		return this.name.startsWith("red_");
	}

	public ItemStack getDropStack() {
		if (this == EMPTY) {
			return null;
		}
		if (isTorchFamily()) {
			return new ItemStack(Blocks.TORCH, isTwinTorch() ? 2 : 1);
		}
		if (isRedTorchFamily()) {
			return new ItemStack(Blocks.REDSTONE_TORCH, 1);
		}
		if (isCandle()) {
			return new ItemStack(BlockObjectHolder.light_metal_ironage_candle_floor, getCandleCount());
		}
		if (this == GLOW) {
			return new ItemStack(BlockObjectHolder.light_metal_ironage_block_floor_glow_clear, 1);
		}
		if (this == LAVA) {
			return new ItemStack(BlockObjectHolder.light_metal_ironage_block_floor_lava_clear, 1);
		}
		if (this == ROCK_SALT) {
			return MineralogyCompat.getRockSaltLampStack();
		}
		if (isRedLamp()) {
			return new ItemStack(BlockObjectHolder.light_metal_ironage_block_floor_red_clear, 1);
		}
		return null;
	}

	public GrandChandelierLight litVersion() {
		if (isTorchFamily()) {
			return isTwinTorch() ? TORCH_TWIN : TORCH;
		}
		if (isCandle()) {
			return candle(getCandleCount(), true);
		}
		if (isRedTorchFamily()) {
			return REDTORCH;
		}
		return this;
	}

	public GrandChandelierLight unlitVersion() {
		if (isTorchFamily()) {
			return isTwinTorch() ? TORCH_TWIN_UNLIT : TORCH_UNLIT;
		}
		if (isCandle()) {
			return candle(getCandleCount(), false);
		}
		if (isRedTorchFamily()) {
			return REDTORCH_UNLIT;
		}
		return this;
	}

	public static GrandChandelierLight candle(int count, boolean lit) {
		switch (count) {
			case 2:
				return lit ? CANDLE_2 : CANDLE_2_UNLIT;
			case 3:
				return lit ? CANDLE_3 : CANDLE_3_UNLIT;
			case 4:
				return lit ? CANDLE_4 : CANDLE_4_UNLIT;
			default:
				return lit ? CANDLE_1 : CANDLE_1_UNLIT;
		}
	}

	public static GrandChandelierLight redLevel(int level) {
		switch (Math.max(0, Math.min(15, level))) {
			case 1:
				return RED_1;
			case 2:
				return RED_2;
			case 3:
				return RED_3;
			case 4:
				return RED_4;
			case 5:
				return RED_5;
			case 6:
				return RED_6;
			case 7:
				return RED_7;
			case 8:
				return RED_8;
			case 9:
				return RED_9;
			case 10:
				return RED_10;
			case 11:
				return RED_11;
			case 12:
				return RED_12;
			case 13:
				return RED_13;
			case 14:
				return RED_14;
			case 15:
				return RED_15;
			default:
				return RED_0;
		}
	}

	public static GrandChandelierLight byName(String name) {
		for (GrandChandelierLight light : values()) {
			if (light.name.equals(name)) {
				return light;
			}
		}
		return EMPTY;
	}
}
