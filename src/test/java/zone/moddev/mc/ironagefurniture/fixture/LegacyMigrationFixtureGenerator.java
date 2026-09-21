package zone.moddev.mc.ironagefurniture.fixture;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import zone.moddev.mc.ironagefurniture.api.Enumerations.PaddedBenchColour;

import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

/**
 * Writes deterministic binary NBT examples using the actual 1.10 NBT classes.
 * These files are consumed by the 26.3 add-on's migration contract tests.
 */
public final class LegacyMigrationFixtureGenerator {
	private static final String[] BOP_WOODS = {
			"cherry", "ebony", "ethereal", "eucalyptus", "fir", "hellbark",
			"jacaranda", "magic", "mahogany", "mangrove", "palm", "pine",
			"redwood", "sacred_oak", "umbran", "willow"
	};
	private static final String[] FORMS = { "bench", "bench_back" };
	private static final String[] FACINGS = { "north", "east", "south", "west" };
	private static final String[] JOINS = { "single", "left", "middle", "right" };

	public static void main(String[] args) throws IOException {
		if (args.length != 1) {
			throw new IllegalArgumentException("Expected output directory argument");
		}
		File output = new File(args[0]);
		if (!output.isDirectory() && !output.mkdirs()) {
			throw new IOException("Could not create " + output);
		}
		write(new File(output, "iaf-1.10-red-only.nbt"), createFixture(false));
		write(new File(output, "iaf-1.10-multicolour.nbt"), createFixture(true));
	}

	private static NBTTagCompound createFixture(boolean multicolour) {
		NBTTagCompound root = new NBTTagCompound();
		root.setString("Format", "ironagefurniture-legacy-padded-bench-fixture-v1");
		root.setString("MinecraftVersion", "1.10.2");
		root.setBoolean("Multicolour", multicolour);

		NBTTagList items = createItems(multicolour);
		root.setTag("PlayerInventory", items);
		root.setTag("ChestItems", createItems(multicolour));
		root.setTag("DroppedItemEntities", createDroppedItems(multicolour));
		root.setTag("NestedContainers", createNestedContainers(multicolour));
		root.setTag("PlacedBlocks", createPlacedBlocks(multicolour));
		root.setTag("TileEntities", createTileEntities(multicolour));
		return root;
	}

	private static NBTTagList createItems(boolean multicolour) {
		NBTTagList result = new NBTTagList();
		for (String wood : BOP_WOODS) {
			for (String form : FORMS) {
				if (multicolour) {
					for (PaddedBenchColour colour : PaddedBenchColour.values()) {
						result.appendTag(item(wood, form, colour, true));
					}
				} else {
					result.appendTag(item(wood, form, PaddedBenchColour.RED, false));
				}
			}
		}
		return result;
	}

	private static NBTTagList createDroppedItems(boolean multicolour) {
		NBTTagList result = new NBTTagList();
		int index = 0;
		for (String wood : BOP_WOODS) {
			NBTTagCompound entity = new NBTTagCompound();
			entity.setString("id", "Item");
			entity.setInteger("FixtureIndex", index++);
			PaddedBenchColour colour = multicolour
					? PaddedBenchColour.values()[index % PaddedBenchColour.values().length]
					: PaddedBenchColour.RED;
			entity.setTag("Item", item(wood, FORMS[index % FORMS.length], colour, multicolour));
			result.appendTag(entity);
		}
		return result;
	}

	private static NBTTagList createNestedContainers(boolean multicolour) {
		NBTTagList result = new NBTTagList();
		NBTTagCompound container = new NBTTagCompound();
		container.setString("id", "minecraft:shulker_box");
		container.setByte("Count", (byte) 1);
		NBTTagCompound tag = new NBTTagCompound();
		NBTTagCompound blockEntityTag = new NBTTagCompound();
		blockEntityTag.setTag("Items", createItems(multicolour));
		tag.setTag("BlockEntityTag", blockEntityTag);
		container.setTag("tag", tag);
		result.appendTag(container);
		return result;
	}

	private static NBTTagList createPlacedBlocks(boolean multicolour) {
		NBTTagList result = new NBTTagList();
		int index = 0;
		for (String wood : BOP_WOODS) {
			for (String form : FORMS) {
				int colourCount = multicolour ? PaddedBenchColour.values().length : 1;
				for (int colourIndex = 0; colourIndex < colourCount; colourIndex++) {
					NBTTagCompound block = new NBTTagCompound();
					block.setString("Name", legacyId(wood, form));
					block.setInteger("x", index);
					block.setInteger("y", 64);
					block.setInteger("z", index / 16);
					NBTTagCompound properties = new NBTTagCompound();
					properties.setString("facing", FACINGS[index % FACINGS.length]);
					properties.setString("type", JOINS[index % JOINS.length]);
					block.setTag("Properties", properties);
					result.appendTag(block);
					index++;
				}
			}
		}
		return result;
	}

	private static NBTTagList createTileEntities(boolean multicolour) {
		NBTTagList result = new NBTTagList();
		if (!multicolour) {
			return result;
		}
		int index = 0;
		for (String wood : BOP_WOODS) {
			for (String form : FORMS) {
				for (PaddedBenchColour colour : PaddedBenchColour.values()) {
					NBTTagCompound tile = new NBTTagCompound();
					tile.setString("id", "ironagefurniture:padded_bench_colour");
					tile.setInteger("x", index);
					tile.setInteger("y", 64);
					tile.setInteger("z", index / 16);
					tile.setString("Color", colour.getSerializedName());
					result.appendTag(tile);
					index++;
				}
			}
		}
		return result;
	}

	private static NBTTagCompound item(String wood, String form,
			PaddedBenchColour colour, boolean includeColour) {
		NBTTagCompound item = new NBTTagCompound();
		item.setString("id", legacyId(wood, form));
		item.setByte("Count", (byte) 1);
		item.setShort("Damage", (short) colour.getItemMetadata());
		if (includeColour) {
			NBTTagCompound tag = new NBTTagCompound();
			tag.setString("Color", colour.getSerializedName());
			item.setTag("tag", tag);
		}
		return item;
	}

	private static String legacyId(String wood, String form) {
		return "ironagefurniture:chair_wood_ironage_" + form
				+ "_padded_single_biomesoplenty_" + wood;
	}

	private static void write(File destination, NBTTagCompound root) throws IOException {
		FileOutputStream output = new FileOutputStream(destination);
		try {
			CompressedStreamTools.writeCompressed(root, output);
		} finally {
			output.close();
		}
	}

	private LegacyMigrationFixtureGenerator() {
	}
}
