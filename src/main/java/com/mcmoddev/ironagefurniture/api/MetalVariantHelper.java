package com.mcmoddev.ironagefurniture.api;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.common.base.Predicate;
import com.mcmoddev.ironagefurniture.IronAgeFurnitureConfiguration;
import com.mcmoddev.ironagefurniture.api.Blocks.ChainTop;
import com.mcmoddev.ironagefurniture.api.Blocks.GrandChandelierHub;
import com.mcmoddev.ironagefurniture.api.Blocks.GrandChandelierSconce;
import com.mcmoddev.ironagefurniture.api.tile.TileEntityMetalVariant;

import net.minecraft.block.Block;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.Explosion;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.oredict.OreDictionary;

public final class MetalVariantHelper {
	public static final PropertyEnum<MetalVariant> METAL = PropertyEnum.create("metal",
		MetalVariant.class, new Predicate<MetalVariant>() {
			@Override
			public boolean apply(MetalVariant input) {
				return input != null && input.isAvailable();
			}
		});

	private static final int IRON_HARDNESS_BASELINE = 8;

	private MetalVariantHelper() {
	}

	public static List<MetalVariant> getAvailableVariants() {
		List<MetalVariant> variants = new ArrayList<MetalVariant>();

		for (MetalVariant variant : MetalVariant.values()) {
			if (variant.isAvailable()) {
				variants.add(variant);
			}
		}

		return Collections.unmodifiableList(variants);
	}

	public static MetalVariant getMetal(IBlockAccess world, BlockPos pos) {
		if (world == null || pos == null) {
			return MetalVariant.IRON;
		}

		TileEntity tileEntity = world.getTileEntity(pos);

		if (tileEntity instanceof TileEntityMetalVariant) {
			return ((TileEntityMetalVariant)tileEntity).getMetal();
		}

		IBlockState state = world.getBlockState(pos);
		if (state != null && state.getProperties().containsKey(METAL)) {
			return state.getValue(METAL);
		}

		return MetalVariant.IRON;
	}

	public static void setMetal(World world, BlockPos pos, MetalVariant metal) {
		if (world == null || pos == null || world.isRemote) {
			return;
		}

		TileEntity tileEntity = world.getTileEntity(pos);

		if (tileEntity instanceof TileEntityMetalVariant) {
			((TileEntityMetalVariant)tileEntity).setMetal(metal);
		}
	}

	public static IBlockState withMetal(IBlockState state, IBlockAccess world, BlockPos pos) {
		if (state != null && state.getProperties().containsKey(METAL)) {
			return state.withProperty(METAL, getMetal(world, pos));
		}

		return state;
	}

	public static IBlockState withMetal(IBlockState state, MetalVariant metal) {
		if (state != null && state.getProperties().containsKey(METAL)) {
			return state.withProperty(METAL, metal == null || !metal.isAvailable() ? MetalVariant.IRON : metal);
		}

		return state;
	}

	public static boolean replaceBlockPreservingMetal(World world, BlockPos pos, IBlockState newState) {
		return replaceBlockPreservingMetal(world, pos, newState, 3);
	}

	public static boolean replaceBlockPreservingMetal(World world, BlockPos pos, IBlockState newState, int flags) {
		MetalVariant metal = getMetal(world, pos);
		boolean replaced = world.setBlockState(pos, withMetal(newState, metal), flags);

		if (replaced) {
			setMetal(world, pos, metal);
		}

		return replaced;
	}

	public static ItemStack getDrop(Block visibleBlock, IBlockAccess world, BlockPos pos) {
		return getDrop(visibleBlock, world, pos, 1);
	}

	public static ItemStack getDrop(Block visibleBlock, IBlockAccess world, BlockPos pos, int count) {
		return getDrop(visibleBlock, getMetal(world, pos), count);
	}

	public static ItemStack getDrop(Block visibleBlock, MetalVariant metal, int count) {
		return new ItemStack(visibleBlock, count, metal.getMeta());
	}

	public static ItemStack getIngotStack(MetalVariant metal) {
		if (metal == MetalVariant.IRON) {
			return new ItemStack(Items.IRON_INGOT);
		}
		if (metal == MetalVariant.GOLD) {
			return new ItemStack(Items.GOLD_INGOT);
		}

		List<ItemStack> stacks = OreDictionary.getOres(metal.getIngotOreName());

		if (stacks != null && !stacks.isEmpty()) {
			ItemStack stack = stacks.get(0).copy();
			stack.stackSize = 1;
			return stack;
		}

		return new ItemStack(Items.IRON_INGOT);
	}

	public static float getHardness(IBlockAccess world, BlockPos pos, float ironHardness) {
		return getHardness(getMetal(world, pos), ironHardness);
	}

	public static float getHardness(MetalVariant metal, float ironHardness) {
		return Math.max(0.1F, ironHardness * metal.getHardnessWeight() / (float)IRON_HARDNESS_BASELINE);
	}

	public static float getResistance(IBlockAccess world, BlockPos pos, float ironResistance) {
		return getResistance(getMetal(world, pos), ironResistance);
	}

	public static float getResistance(MetalVariant metal, float ironResistance) {
		return Math.max(1.0F, ironResistance * metal.getStrengthWeight() / (float)IRON_HARDNESS_BASELINE);
	}

	public static float getExplosionResistance(Block block, World world, BlockPos pos, Entity exploder,
			Explosion explosion, float ironResistance) {
		return getResistance(world, pos, ironResistance);
	}

	public static boolean isMetalVariantBlock(Block block) {
		return block instanceof ChainTop
			|| block instanceof com.mcmoddev.ironagefurniture.api.Blocks.LightHolderSconceFloor
			|| block instanceof com.mcmoddev.ironagefurniture.api.Blocks.LightSourceChandelierCandle
			|| block instanceof GrandChandelierHub
			|| block instanceof GrandChandelierSconce;
	}

	public enum MetalVariant implements IStringSerializable {
		IRON("iron", "Iron", 0, "minecraft:blocks/iron_block", "ingotIron", "nuggetIron", "barsIron", 8, 8, 1),
		GOLD("gold", "Gold", 1, "minecraft:blocks/gold_block", "ingotGold", "nuggetGold", "barsGold", 1, 1, 0),
		ADAMANTINE("adamantine", "Adamantine", 2, "basemetals:blocks/adamantine_block", "ingotAdamantine", "nuggetAdamantine", "barsAdamantine", 12, 100, 1),
		ANTIMONY("antimony", "Antimony", 3, "basemetals:blocks/antimony_block", "ingotAntimony", "nuggetAntimony", "barsAntimony", 1, 1, 1),
		AQUARIUM("aquarium", "Aquarium", 4, "basemetals:blocks/aquarium_block", "ingotAquarium", "nuggetAquarium", "barsAquarium", 4, 4, 1),
		BISMUTH("bismuth", "Bismuth", 5, "basemetals:blocks/bismuth_block", "ingotBismuth", "nuggetBismuth", "barsBismuth", 1, 1, 1),
		BRASS("brass", "Brass", 6, "basemetals:blocks/brass_block", "ingotBrass", "nuggetBrass", "barsBrass", 4, 4, 1),
		BRONZE("bronze", "Bronze", 7, "basemetals:blocks/bronze_block", "ingotBronze", "nuggetBronze", "barsBronze", 8, 8, 1),
		COLDIRON("coldiron", "Cold-Iron", 8, "basemetals:blocks/coldiron_block", "ingotColdIron", "nuggetColdIron", "barsColdIron", 7, 7, 1),
		COPPER("copper", "Copper", 9, "basemetals:blocks/copper_block", "ingotCopper", "nuggetCopper", "barsCopper", 4, 4, 1),
		CUPRONICKEL("cupronickel", "Cupronickel", 10, "basemetals:blocks/cupronickel_block", "ingotCupronickel", "nuggetCupronickel", "barsCupronickel", 6, 6, 1),
		ELECTRUM("electrum", "Electrum", 11, "basemetals:blocks/electrum_block", "ingotElectrum", "nuggetElectrum", "barsElectrum", 5, 5, 1),
		INVAR("invar", "Invar", 12, "basemetals:blocks/invar_block", "ingotInvar", "nuggetInvar", "barsInvar", 9, 9, 1),
		LEAD("lead", "Lead", 13, "basemetals:blocks/lead_block", "ingotLead", "nuggetLead", "barsLead", 1, 1, 1),
		MITHRIL("mithril", "Mithril", 14, "basemetals:blocks/mithril_block", "ingotMithril", "nuggetMithril", "barsMithril", 9, 9, 1),
		NICKEL("nickel", "Nickel", 15, "basemetals:blocks/nickel_block", "ingotNickel", "nuggetNickel", "barsNickel", 4, 4, 1),
		PEWTER("pewter", "Pewter", 16, "basemetals:blocks/pewter_block", "ingotPewter", "nuggetPewter", "barsPewter", 1, 1, 1),
		PLATINUM("platinum", "Platinum", 17, "basemetals:blocks/platinum_block", "ingotPlatinum", "nuggetPlatinum", "barsPlatinum", 3, 3, 1),
		SILVER("silver", "Silver", 18, "basemetals:blocks/silver_block", "ingotSilver", "nuggetSilver", "barsSilver", 5, 5, 1),
		STARSTEEL("starsteel", "Star-Steel", 19, "basemetals:blocks/starsteel_block", "ingotStarSteel", "nuggetStarSteel", "barsStarSteel", 10, 25, 1),
		STEEL("steel", "Steel", 20, "basemetals:blocks/steel_block", "ingotSteel", "nuggetSteel", "barsSteel", 8, 8, 1),
		TIN("tin", "Tin", 21, "basemetals:blocks/tin_block", "ingotTin", "nuggetTin", "barsTin", 3, 3, 1),
		ZINC("zinc", "Zinc", 22, "basemetals:blocks/zinc_block", "ingotZinc", "nuggetZinc", "barsZinc", 1, 1, 1);

		private final String name;
		private final String displayName;
		private final int meta;
		private final String blockTexture;
		private final String ingotOreName;
		private final String nuggetOreName;
		private final String barsOreName;
		private final int hardnessWeight;
		private final int strengthWeight;
		private final int chainPowerLoss;

		private MetalVariant(String name, String displayName, int meta, String blockTexture, String ingotOreName,
				String nuggetOreName, String barsOreName, int hardnessWeight, int strengthWeight, int chainPowerLoss) {
			this.name = name;
			this.displayName = displayName;
			this.meta = meta;
			this.blockTexture = blockTexture;
			this.ingotOreName = ingotOreName;
			this.nuggetOreName = nuggetOreName;
			this.barsOreName = barsOreName;
			this.hardnessWeight = hardnessWeight;
			this.strengthWeight = strengthWeight;
			this.chainPowerLoss = chainPowerLoss;
		}

		@Override
		public String getName() {
			return this.name;
		}

		public String getDisplayName() {
			return this.displayName;
		}

		public int getMeta() {
			return this.meta;
		}

		public String getBlockTexture() {
			return this.blockTexture;
		}

		public String getIngotOreName() {
			return this.ingotOreName;
		}

		public String getNuggetOreName() {
			return this.nuggetOreName;
		}

		public String getBarsOreName() {
			return this.barsOreName;
		}

		public int getHardnessWeight() {
			return this.hardnessWeight;
		}

		public int getStrengthWeight() {
			return this.strengthWeight;
		}

		public int getChainPowerLoss() {
			return this.chainPowerLoss;
		}

		public boolean isAvailable() {
			return this == IRON || this == GOLD
				|| (IronAgeFurnitureConfiguration.INTEGRATION_BASEMETALS && Loader.isModLoaded("basemetals"));
		}

		public static MetalVariant byMeta(int meta) {
			for (MetalVariant variant : values()) {
				if (variant.meta == meta && variant.isAvailable()) {
					return variant;
				}
			}

			return IRON;
		}

		public static MetalVariant byName(String name) {
			for (MetalVariant variant : values()) {
				if (variant.name.equals(name) && variant.isAvailable()) {
					return variant;
				}
			}

			return IRON;
		}
	}
}
