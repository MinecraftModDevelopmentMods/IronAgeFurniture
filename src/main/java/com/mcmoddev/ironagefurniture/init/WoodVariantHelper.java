package com.mcmoddev.ironagefurniture.init;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.mcmoddev.ironagefurniture.IronAgeFurnitureConfiguration;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Loader;

final class WoodVariantHelper {
	private static final String[] VANILLA_SUFFIXES = new String[] {
		"oak", "spruce", "birch", "jungle", "acacia", "big_oak"
	};
	private static final String[] BIOMESOPLENTY_SUFFIXES = new String[] {
		"biomesoplenty_cherry", "biomesoplenty_ebony", "biomesoplenty_ethereal", "biomesoplenty_eucalyptus",
		"biomesoplenty_fir", "biomesoplenty_hellbark", "biomesoplenty_jacaranda", "biomesoplenty_magic",
		"biomesoplenty_mahogany", "biomesoplenty_mangrove", "biomesoplenty_palm", "biomesoplenty_pine",
		"biomesoplenty_redwood", "biomesoplenty_sacred_oak", "biomesoplenty_umbran", "biomesoplenty_willow"
	};
	private static final String[] NATURA_SUFFIXES = new String[] {
		"natura_amaranth", "natura_bloodwood", "natura_darkwood", "natura_eucalyptus",
		"natura_fusewood", "natura_ghostwood", "natura_hopseed", "natura_maple",
		"natura_redwood", "natura_sakura", "natura_silverbell", "natura_tiger", "natura_willow"
	};
	private static final String[] FORESTRY_SUFFIXES = new String[] {
		"forestry_acacia", "forestry_balsa", "forestry_baobab", "forestry_cherry", "forestry_chestnut",
		"forestry_citrus", "forestry_cocobolo", "forestry_ebony", "forestry_giganteum",
		"forestry_greenheart", "forestry_ipe", "forestry_kapok", "forestry_larch", "forestry_lime",
		"forestry_mahoe", "forestry_mahogany", "forestry_maple", "forestry_padauk", "forestry_palm",
		"forestry_papaya", "forestry_pine", "forestry_plum", "forestry_poplar", "forestry_sequoia",
		"forestry_teak", "forestry_walnut", "forestry_wenge", "forestry_willow", "forestry_zebrawood"
	};

	private WoodVariantHelper() {
		throw new IllegalAccessError("This class cannot be instantiated");
	}

	interface ItemStackVariantConsumer {
		void accept(String suffix, ItemStack itemStack);
	}

	static List<String> getEnabledWoodSuffixes() {
		List<String> suffixes = new ArrayList<String>();
		addSuffixes(suffixes, VANILLA_SUFFIXES);

		if (IronAgeFurnitureConfiguration.INTEGRATION_BIOMESOPLENTY && Loader.isModLoaded("BiomesOPlenty")) {
			addSuffixes(suffixes, BIOMESOPLENTY_SUFFIXES);
		}
		if (IronAgeFurnitureConfiguration.INTEGRATION_NATURA && Loader.isModLoaded("natura")) {
			addSuffixes(suffixes, NATURA_SUFFIXES);
		}
		if (IronAgeFurnitureConfiguration.INTEGRATION_IMMERSIVEENGINEERING && Loader.isModLoaded("immersiveengineering")) {
			suffixes.add("immersiveengineering_treatedWood");
		}
		if (IronAgeFurnitureConfiguration.INTEGRATION_FORESTRY && Loader.isModLoaded("forestry")) {
			addSuffixes(suffixes, FORESTRY_SUFFIXES);
		}

		Collections.sort(suffixes);
		return suffixes;
	}

	static void forEachEnabledPlankVariant(ItemStackVariantConsumer consumer) {
		consumer.accept("oak", new ItemStack(Blocks.PLANKS, 1, 0));
		consumer.accept("spruce", new ItemStack(Blocks.PLANKS, 1, 1));
		consumer.accept("birch", new ItemStack(Blocks.PLANKS, 1, 2));
		consumer.accept("jungle", new ItemStack(Blocks.PLANKS, 1, 3));
		consumer.accept("acacia", new ItemStack(Blocks.PLANKS, 1, 4));
		consumer.accept("big_oak", new ItemStack(Blocks.PLANKS, 1, 5));

		addBiomesOPlentyPlankVariants(consumer);
		addNaturaPlankVariants(consumer);
		addImmersiveEngineeringPlankVariants(consumer);
		addForestryPlankVariants(consumer);
	}

	static void forEachEnabledSlabVariant(ItemStackVariantConsumer consumer) {
		consumer.accept("oak", new ItemStack(Blocks.WOODEN_SLAB, 1, 0));
		consumer.accept("spruce", new ItemStack(Blocks.WOODEN_SLAB, 1, 1));
		consumer.accept("birch", new ItemStack(Blocks.WOODEN_SLAB, 1, 2));
		consumer.accept("jungle", new ItemStack(Blocks.WOODEN_SLAB, 1, 3));
		consumer.accept("acacia", new ItemStack(Blocks.WOODEN_SLAB, 1, 4));
		consumer.accept("big_oak", new ItemStack(Blocks.WOODEN_SLAB, 1, 5));

		addBiomesOPlentySlabVariants(consumer);
		addNaturaSlabVariants(consumer);
		addImmersiveEngineeringSlabVariants(consumer);
		addForestrySlabVariants(consumer);
	}

	static void forEachEnabledLogVariant(ItemStackVariantConsumer consumer) {
		consumer.accept("oak", new ItemStack(Blocks.LOG, 1, 0));
		consumer.accept("spruce", new ItemStack(Blocks.LOG, 1, 1));
		consumer.accept("birch", new ItemStack(Blocks.LOG, 1, 2));
		consumer.accept("jungle", new ItemStack(Blocks.LOG, 1, 3));
		consumer.accept("acacia", new ItemStack(Blocks.LOG2, 1, 0));
		consumer.accept("big_oak", new ItemStack(Blocks.LOG2, 1, 1));

		addBiomesOPlentyLogVariants(consumer);
		addNaturaLogVariants(consumer);
		addForestryLogVariants(consumer);
	}

	static List<String> getEnabledLogSuffixes() {
		final List<String> suffixes = new ArrayList<String>();

		forEachEnabledLogVariant(new ItemStackVariantConsumer() {
			@Override
			public void accept(String suffix, ItemStack itemStack) {
				suffixes.add(suffix);
			}
		});

		Collections.sort(suffixes);
		return suffixes;
	}

	private static void addBiomesOPlentyPlankVariants(ItemStackVariantConsumer consumer) {
		if (!IronAgeFurnitureConfiguration.INTEGRATION_BIOMESOPLENTY || !Loader.isModLoaded("BiomesOPlenty")) {
			return;
		}

		Block planks = Block.getBlockFromName("BiomesOPlenty:planks_0");
		accept(consumer, "biomesoplenty_sacred_oak", planks, 0);
		accept(consumer, "biomesoplenty_cherry", planks, 1);
		accept(consumer, "biomesoplenty_umbran", planks, 2);
		accept(consumer, "biomesoplenty_fir", planks, 3);
		accept(consumer, "biomesoplenty_ethereal", planks, 4);
		accept(consumer, "biomesoplenty_magic", planks, 5);
		accept(consumer, "biomesoplenty_mangrove", planks, 6);
		accept(consumer, "biomesoplenty_palm", planks, 7);
		accept(consumer, "biomesoplenty_redwood", planks, 8);
		accept(consumer, "biomesoplenty_willow", planks, 9);
		accept(consumer, "biomesoplenty_pine", planks, 10);
		accept(consumer, "biomesoplenty_hellbark", planks, 11);
		accept(consumer, "biomesoplenty_jacaranda", planks, 12);
		accept(consumer, "biomesoplenty_mahogany", planks, 13);
		accept(consumer, "biomesoplenty_ebony", planks, 14);
		accept(consumer, "biomesoplenty_eucalyptus", planks, 15);
	}

	private static void addBiomesOPlentyLogVariants(ItemStackVariantConsumer consumer) {
		if (!IronAgeFurnitureConfiguration.INTEGRATION_BIOMESOPLENTY || !Loader.isModLoaded("BiomesOPlenty")) {
			return;
		}

		Block logs0 = Block.getBlockFromName("BiomesOPlenty:log_0");
		accept(consumer, "biomesoplenty_sacred_oak", logs0, 4);
		accept(consumer, "biomesoplenty_cherry", logs0, 5);
		accept(consumer, "biomesoplenty_umbran", logs0, 6);
		accept(consumer, "biomesoplenty_fir", logs0, 7);

		Block logs1 = Block.getBlockFromName("BiomesOPlenty:log_1");
		accept(consumer, "biomesoplenty_ethereal", logs1, 4);
		accept(consumer, "biomesoplenty_magic", logs1, 5);
		accept(consumer, "biomesoplenty_mangrove", logs1, 6);
		accept(consumer, "biomesoplenty_palm", logs1, 7);

		Block logs2 = Block.getBlockFromName("BiomesOPlenty:log_2");
		accept(consumer, "biomesoplenty_redwood", logs2, 4);
		accept(consumer, "biomesoplenty_willow", logs2, 5);
		accept(consumer, "biomesoplenty_pine", logs2, 6);
		accept(consumer, "biomesoplenty_hellbark", logs2, 7);

		Block logs3 = Block.getBlockFromName("BiomesOPlenty:log_3");
		accept(consumer, "biomesoplenty_jacaranda", logs3, 4);
		accept(consumer, "biomesoplenty_mahogany", logs3, 5);
		accept(consumer, "biomesoplenty_ebony", logs3, 6);
		accept(consumer, "biomesoplenty_eucalyptus", logs3, 7);
	}

	private static void addBiomesOPlentySlabVariants(ItemStackVariantConsumer consumer) {
		if (!IronAgeFurnitureConfiguration.INTEGRATION_BIOMESOPLENTY || !Loader.isModLoaded("BiomesOPlenty")) {
			return;
		}

		Block slabs0 = Block.getBlockFromName("BiomesOPlenty:wood_slab_0");
		accept(consumer, "biomesoplenty_sacred_oak", slabs0, 0);
		accept(consumer, "biomesoplenty_cherry", slabs0, 1);
		accept(consumer, "biomesoplenty_umbran", slabs0, 2);
		accept(consumer, "biomesoplenty_fir", slabs0, 3);
		accept(consumer, "biomesoplenty_ethereal", slabs0, 4);
		accept(consumer, "biomesoplenty_magic", slabs0, 5);
		accept(consumer, "biomesoplenty_mangrove", slabs0, 6);
		accept(consumer, "biomesoplenty_palm", slabs0, 7);

		Block slabs1 = Block.getBlockFromName("BiomesOPlenty:wood_slab_1");
		accept(consumer, "biomesoplenty_redwood", slabs1, 0);
		accept(consumer, "biomesoplenty_willow", slabs1, 1);
		accept(consumer, "biomesoplenty_pine", slabs1, 2);
		accept(consumer, "biomesoplenty_hellbark", slabs1, 3);
		accept(consumer, "biomesoplenty_jacaranda", slabs1, 4);
		accept(consumer, "biomesoplenty_mahogany", slabs1, 5);
		accept(consumer, "biomesoplenty_ebony", slabs1, 6);
		accept(consumer, "biomesoplenty_eucalyptus", slabs1, 7);
	}

	private static void addNaturaPlankVariants(ItemStackVariantConsumer consumer) {
		if (!IronAgeFurnitureConfiguration.INTEGRATION_NATURA || !Loader.isModLoaded("natura")) {
			return;
		}

		Block overworldPlanks = Block.getBlockFromName("natura:overworld_planks");
		accept(consumer, "natura_maple", overworldPlanks, 0);
		accept(consumer, "natura_silverbell", overworldPlanks, 1);
		accept(consumer, "natura_amaranth", overworldPlanks, 2);
		accept(consumer, "natura_tiger", overworldPlanks, 3);
		accept(consumer, "natura_willow", overworldPlanks, 4);
		accept(consumer, "natura_eucalyptus", overworldPlanks, 5);
		accept(consumer, "natura_hopseed", overworldPlanks, 6);
		accept(consumer, "natura_sakura", overworldPlanks, 7);
		accept(consumer, "natura_redwood", overworldPlanks, 8);

		Block netherPlanks = Block.getBlockFromName("natura:nether_planks");
		accept(consumer, "natura_ghostwood", netherPlanks, 0);
		accept(consumer, "natura_bloodwood", netherPlanks, 1);
		accept(consumer, "natura_darkwood", netherPlanks, 2);
		accept(consumer, "natura_fusewood", netherPlanks, 3);
	}

	private static void addNaturaLogVariants(ItemStackVariantConsumer consumer) {
		if (!IronAgeFurnitureConfiguration.INTEGRATION_NATURA || !Loader.isModLoaded("natura")) {
			return;
		}

		Block overworldLogs = Block.getBlockFromName("natura:overworld_logs");
		accept(consumer, "natura_maple", overworldLogs, 0);
		accept(consumer, "natura_silverbell", overworldLogs, 1);
		accept(consumer, "natura_amaranth", overworldLogs, 2);
		accept(consumer, "natura_tiger", overworldLogs, 3);

		Block overworldLogs2 = Block.getBlockFromName("natura:overworld_logs2");
		accept(consumer, "natura_willow", overworldLogs2, 0);
		accept(consumer, "natura_eucalyptus", overworldLogs2, 1);
		accept(consumer, "natura_hopseed", overworldLogs2, 2);
		accept(consumer, "natura_sakura", overworldLogs2, 3);

		Block netherLogs = Block.getBlockFromName("natura:nether_logs");
		accept(consumer, "natura_ghostwood", netherLogs, 0);
		accept(consumer, "natura_darkwood", netherLogs, 1);
		accept(consumer, "natura_fusewood", netherLogs, 2);

		Block netherLogs2 = Block.getBlockFromName("natura:nether_logs2");
		accept(consumer, "natura_bloodwood", netherLogs2, 0);
	}

	private static void addNaturaSlabVariants(ItemStackVariantConsumer consumer) {
		if (!IronAgeFurnitureConfiguration.INTEGRATION_NATURA || !Loader.isModLoaded("natura")) {
			return;
		}

		Block overworldSlabs = Block.getBlockFromName("natura:overworld_slab");
		accept(consumer, "natura_maple", overworldSlabs, 0);
		accept(consumer, "natura_silverbell", overworldSlabs, 1);
		accept(consumer, "natura_amaranth", overworldSlabs, 2);
		accept(consumer, "natura_tiger", overworldSlabs, 3);
		accept(consumer, "natura_willow", overworldSlabs, 4);

		Block overworldSlabs2 = Block.getBlockFromName("natura:overworld_slab2");
		accept(consumer, "natura_eucalyptus", overworldSlabs2, 0);
		accept(consumer, "natura_hopseed", overworldSlabs2, 1);
		accept(consumer, "natura_sakura", overworldSlabs2, 2);
		accept(consumer, "natura_redwood", overworldSlabs2, 3);

		Block netherSlabs = Block.getBlockFromName("natura:nether_slab");
		accept(consumer, "natura_ghostwood", netherSlabs, 0);
		accept(consumer, "natura_bloodwood", netherSlabs, 1);
		accept(consumer, "natura_darkwood", netherSlabs, 2);
		accept(consumer, "natura_fusewood", netherSlabs, 3);
	}

	private static void addImmersiveEngineeringPlankVariants(ItemStackVariantConsumer consumer) {
		if (!IronAgeFurnitureConfiguration.INTEGRATION_IMMERSIVEENGINEERING || !Loader.isModLoaded("immersiveengineering")) {
			return;
		}

		Block treatedWood = Block.getBlockFromName("immersiveengineering:treatedWood");
		accept(consumer, "immersiveengineering_treatedWood", treatedWood, 0);
		accept(consumer, "immersiveengineering_treatedWood", treatedWood, 1);
		accept(consumer, "immersiveengineering_treatedWood", treatedWood, 2);
	}

	private static void addImmersiveEngineeringSlabVariants(ItemStackVariantConsumer consumer) {
		if (!IronAgeFurnitureConfiguration.INTEGRATION_IMMERSIVEENGINEERING || !Loader.isModLoaded("immersiveengineering")) {
			return;
		}

		Block treatedWoodSlab = Block.getBlockFromName("immersiveengineering:treatedWoodSlab");
		accept(consumer, "immersiveengineering_treatedWood", treatedWoodSlab, 0);
		accept(consumer, "immersiveengineering_treatedWood", treatedWoodSlab, 1);
		accept(consumer, "immersiveengineering_treatedWood", treatedWoodSlab, 2);
	}

	private static void addForestryPlankVariants(ItemStackVariantConsumer consumer) {
		if (!IronAgeFurnitureConfiguration.INTEGRATION_FORESTRY || !Loader.isModLoaded("forestry")) {
			return;
		}

		Block planks0 = Block.getBlockFromName("forestry:planks.0");
		accept(consumer, "forestry_larch", planks0, 0);
		accept(consumer, "forestry_teak", planks0, 1);
		accept(consumer, "forestry_acacia", planks0, 2);
		accept(consumer, "forestry_lime", planks0, 3);
		accept(consumer, "forestry_chestnut", planks0, 4);
		accept(consumer, "forestry_wenge", planks0, 5);
		accept(consumer, "forestry_baobab", planks0, 6);
		accept(consumer, "forestry_sequoia", planks0, 7);
		accept(consumer, "forestry_kapok", planks0, 8);
		accept(consumer, "forestry_ebony", planks0, 9);
		accept(consumer, "forestry_mahogany", planks0, 10);
		accept(consumer, "forestry_balsa", planks0, 11);
		accept(consumer, "forestry_willow", planks0, 12);
		accept(consumer, "forestry_walnut", planks0, 13);
		accept(consumer, "forestry_greenheart", planks0, 14);
		accept(consumer, "forestry_cherry", planks0, 15);

		Block planks1 = Block.getBlockFromName("forestry:planks.1");
		accept(consumer, "forestry_mahoe", planks1, 0);
		accept(consumer, "forestry_poplar", planks1, 1);
		accept(consumer, "forestry_palm", planks1, 2);
		accept(consumer, "forestry_papaya", planks1, 3);
		accept(consumer, "forestry_pine", planks1, 4);
		accept(consumer, "forestry_plum", planks1, 5);
		accept(consumer, "forestry_maple", planks1, 6);
		accept(consumer, "forestry_citrus", planks1, 7);
		accept(consumer, "forestry_giganteum", planks1, 8);
		accept(consumer, "forestry_ipe", planks1, 9);
		accept(consumer, "forestry_padauk", planks1, 10);
		accept(consumer, "forestry_cocobolo", planks1, 11);
		accept(consumer, "forestry_zebrawood", planks1, 12);
	}

	private static void addForestrySlabVariants(ItemStackVariantConsumer consumer) {
		if (!IronAgeFurnitureConfiguration.INTEGRATION_FORESTRY || !Loader.isModLoaded("forestry")) {
			return;
		}

		Block slabs0 = Block.getBlockFromName("forestry:slabs.0");
		accept(consumer, "forestry_larch", slabs0, 0);
		accept(consumer, "forestry_teak", slabs0, 1);
		accept(consumer, "forestry_acacia", slabs0, 2);
		accept(consumer, "forestry_lime", slabs0, 3);
		accept(consumer, "forestry_chestnut", slabs0, 4);
		accept(consumer, "forestry_wenge", slabs0, 5);
		accept(consumer, "forestry_baobab", slabs0, 6);
		accept(consumer, "forestry_sequoia", slabs0, 7);

		Block slabs1 = Block.getBlockFromName("forestry:slabs.1");
		accept(consumer, "forestry_kapok", slabs1, 0);
		accept(consumer, "forestry_ebony", slabs1, 1);
		accept(consumer, "forestry_mahogany", slabs1, 2);
		accept(consumer, "forestry_balsa", slabs1, 3);
		accept(consumer, "forestry_willow", slabs1, 4);
		accept(consumer, "forestry_walnut", slabs1, 5);
		accept(consumer, "forestry_greenheart", slabs1, 6);
		accept(consumer, "forestry_cherry", slabs1, 7);

		Block slabs2 = Block.getBlockFromName("forestry:slabs.2");
		accept(consumer, "forestry_mahoe", slabs2, 0);
		accept(consumer, "forestry_poplar", slabs2, 1);
		accept(consumer, "forestry_palm", slabs2, 2);
		accept(consumer, "forestry_papaya", slabs2, 3);
		accept(consumer, "forestry_pine", slabs2, 4);
		accept(consumer, "forestry_plum", slabs2, 5);
		accept(consumer, "forestry_maple", slabs2, 6);
		accept(consumer, "forestry_citrus", slabs2, 7);

		Block slabs3 = Block.getBlockFromName("forestry:slabs.3");
		accept(consumer, "forestry_giganteum", slabs3, 0);
		accept(consumer, "forestry_ipe", slabs3, 1);
		accept(consumer, "forestry_padauk", slabs3, 2);
		accept(consumer, "forestry_cocobolo", slabs3, 3);
		accept(consumer, "forestry_zebrawood", slabs3, 4);
	}

	private static void addForestryLogVariants(ItemStackVariantConsumer consumer) {
		if (!IronAgeFurnitureConfiguration.INTEGRATION_FORESTRY || !Loader.isModLoaded("forestry")) {
			return;
		}

		Block logs0 = Block.getBlockFromName("forestry:logs.0");
		accept(consumer, "forestry_larch", logs0, 0);
		accept(consumer, "forestry_teak", logs0, 1);
		accept(consumer, "forestry_acacia", logs0, 2);
		accept(consumer, "forestry_lime", logs0, 3);

		Block logs1 = Block.getBlockFromName("forestry:logs.1");
		accept(consumer, "forestry_chestnut", logs1, 0);
		accept(consumer, "forestry_wenge", logs1, 1);
		accept(consumer, "forestry_baobab", logs1, 2);
		accept(consumer, "forestry_sequoia", logs1, 3);

		Block logs2 = Block.getBlockFromName("forestry:logs.2");
		accept(consumer, "forestry_kapok", logs2, 0);
		accept(consumer, "forestry_ebony", logs2, 1);
		accept(consumer, "forestry_mahogany", logs2, 2);
		accept(consumer, "forestry_balsa", logs2, 3);

		Block logs3 = Block.getBlockFromName("forestry:logs.3");
		accept(consumer, "forestry_willow", logs3, 0);
		accept(consumer, "forestry_walnut", logs3, 1);
		accept(consumer, "forestry_greenheart", logs3, 2);
		accept(consumer, "forestry_cherry", logs3, 3);

		Block logs4 = Block.getBlockFromName("forestry:logs.4");
		accept(consumer, "forestry_mahoe", logs4, 0);
		accept(consumer, "forestry_poplar", logs4, 1);
		accept(consumer, "forestry_palm", logs4, 2);
		accept(consumer, "forestry_papaya", logs4, 3);

		Block logs5 = Block.getBlockFromName("forestry:logs.5");
		accept(consumer, "forestry_pine", logs5, 0);
		accept(consumer, "forestry_plum", logs5, 1);
		accept(consumer, "forestry_maple", logs5, 2);
		accept(consumer, "forestry_citrus", logs5, 3);

		Block logs6 = Block.getBlockFromName("forestry:logs.6");
		accept(consumer, "forestry_giganteum", logs6, 0);
		accept(consumer, "forestry_ipe", logs6, 1);
		accept(consumer, "forestry_padauk", logs6, 2);
		accept(consumer, "forestry_cocobolo", logs6, 3);

		Block logs7 = Block.getBlockFromName("forestry:logs.7");
		accept(consumer, "forestry_zebrawood", logs7, 0);
	}

	private static void addSuffixes(List<String> suffixes, String[] values) {
		for (String suffix : values) {
			suffixes.add(suffix);
		}
	}

	private static void accept(ItemStackVariantConsumer consumer, String suffix, Block block, int meta) {
		if (block != null) {
			consumer.accept(suffix, new ItemStack(block, 1, meta));
		}
	}
}
