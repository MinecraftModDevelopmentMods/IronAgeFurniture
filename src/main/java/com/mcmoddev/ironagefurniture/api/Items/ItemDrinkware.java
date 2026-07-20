package com.mcmoddev.ironagefurniture.api.Items;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.annotation.Nullable;

import com.mcmoddev.ironagefurniture.IronAgeFurnitureConfiguration;
import com.mcmoddev.ironagefurniture.api.DrinkDisplayHelper;
import com.mcmoddev.ironagefurniture.api.FoudreBrewingRegistry;
import com.mcmoddev.ironagefurniture.api.MetalVariantHelper.MetalVariant;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.FluidTankProperties;
import net.minecraftforge.fluids.capability.IFluidTankProperties;
import net.minecraftforge.fluids.capability.templates.FluidHandlerItemStackSimple;
import net.minecraftforge.fml.common.Loader;

public class ItemDrinkware extends Item {
	private static final int VESSEL_SHIFT = 12;
	private static final int MATERIAL_SHIFT = 8;
	private static final int INDEX_MASK = 0xFF;
	private static final int DEFAULT_TINT = 0xFFFFFFFF;
	private static final int STAINED_GLASS_TINT_PERCENT = 45;

	/* Append new woods only. Their positions are persisted in item metadata. */
	private static final String[] WOOD_SUFFIXES = new String[] {
		"oak", "spruce", "birch", "jungle", "acacia", "big_oak",
		"biomesoplenty_cherry", "biomesoplenty_ebony", "biomesoplenty_ethereal",
		"biomesoplenty_eucalyptus", "biomesoplenty_fir", "biomesoplenty_hellbark",
		"biomesoplenty_jacaranda", "biomesoplenty_magic", "biomesoplenty_mahogany",
		"biomesoplenty_mangrove", "biomesoplenty_palm", "biomesoplenty_pine",
		"biomesoplenty_redwood", "biomesoplenty_sacred_oak", "biomesoplenty_umbran",
		"biomesoplenty_willow", "natura_amaranth", "natura_bloodwood", "natura_darkwood",
		"natura_eucalyptus", "natura_fusewood", "natura_ghostwood", "natura_hopseed",
		"natura_maple", "natura_redwood", "natura_sakura", "natura_silverbell",
		"natura_tiger", "natura_willow", "immersiveengineering_treatedWood",
		"forestry_acacia", "forestry_balsa", "forestry_baobab", "forestry_cherry",
		"forestry_chestnut", "forestry_citrus", "forestry_cocobolo", "forestry_ebony",
		"forestry_giganteum", "forestry_greenheart", "forestry_ipe", "forestry_kapok",
		"forestry_larch", "forestry_lime", "forestry_mahoe", "forestry_mahogany",
		"forestry_maple", "forestry_padauk", "forestry_palm", "forestry_papaya",
		"forestry_pine", "forestry_plum", "forestry_poplar", "forestry_sequoia",
		"forestry_teak", "forestry_walnut", "forestry_wenge", "forestry_willow",
		"forestry_zebrawood"
	};

	private static final MetalVariant[] METALS = new MetalVariant[] {
		MetalVariant.IRON, MetalVariant.GOLD, MetalVariant.PEWTER, MetalVariant.SILVER,
		MetalVariant.COPPER, MetalVariant.BRONZE, MetalVariant.BRASS, MetalVariant.TIN,
		MetalVariant.STEEL
	};

	private static final int[] GLASS_COLORS = new int[] {
		0xFFFFFFFF, 0xFFF9FFFF, 0xFFF9801D, 0xFFC74EBD, 0xFF3AB3DA, 0xFFFED83D,
		0xFF80C71F, 0xFFF38BAA, 0xFF474F52, 0xFF9D9D97, 0xFF169C9C, 0xFF8932B8,
		0xFF3C44AA, 0xFF835432, 0xFF5E7C16, 0xFFB02E26, 0xFF1D1D21
	};

	private static final int[] METAL_COLORS = new int[] {
		0xFFD8D8D8, 0xFFFFD34E, 0xFFB8B6AA, 0xFFD8DEE0, 0xFFC9794C,
		0xFFB47A45, 0xFFD4A447, 0xFFC8C0AA, 0xFF89939A
	};

	private static final List<String> MODEL_NAMES = buildModelNames();

	static {
		validateMetadataLayout();
	}

	public ItemDrinkware() {
		this.setHasSubtypes(true);
		this.setMaxDamage(0);
		this.setMaxStackSize(64);
	}

	@Override
	public int getMetadata(int damage) {
		return damage;
	}

	@Override
	public int getItemStackLimit(ItemStack stack) {
		return 64;
	}

	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt) {
		return new DrinkwareFluidHandler(stack);
	}

	@Override
	public void getSubItems(Item itemIn, CreativeTabs tab, List<ItemStack> subItems) {
		for (VesselType vessel : VesselType.values()) {
			for (MaterialType material : MaterialType.values()) {
				int count = getVariantCount(material);

				for (int index = 0; index < count; index++) {
					Variant variant = new Variant(vessel, material, index);

					if (variant.isValid() && variant.isAvailable()) {
						subItems.add(new ItemStack(itemIn, 1, variant.getMetadata()));
					}
				}
			}
		}
	}

	@Override
	public String getItemStackDisplayName(ItemStack stack) {
		Variant variant = getVariant(stack);
		FluidStack fluid = getFluid(stack);
		String vesselName = variant.getMaterialDisplayName() + " " + variant.getVessel().getDisplayName();
		return fluid != null && fluid.getFluid() != null
			? vesselName + " of " + DrinkDisplayHelper.getDisplayName(fluid) : vesselName;
	}

	@Override
	public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityLivingBase entityLiving) {
		EntityPlayer player = entityLiving instanceof EntityPlayer ? (EntityPlayer)entityLiving : null;
		FluidStack fluid = getFluid(stack);

		if (fluid == null || !FoudreBrewingRegistry.isDrinkable(fluid)) {
			return stack;
		}

		if (player != null) {
			player.addStat(StatList.getObjectUseStats(this));
		}

		if (player == null || !player.capabilities.isCreativeMode) {
			ItemStack empty = DrinkContainerHelper.createEmptyContainer(stack);
			stack.stackSize--;

			if (stack.stackSize <= 0) {
				return empty;
			}

			DrinkContainerHelper.giveOrDrop(player, empty);
		}

		return stack;
	}

	@Override
	public int getMaxItemUseDuration(ItemStack stack) {
		return 32;
	}

	@Override
	public EnumAction getItemUseAction(ItemStack stack) {
		return EnumAction.DRINK;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn,
			EnumHand hand) {
		FluidStack fluid = getFluid(itemStackIn);

		if (fluid == null || !FoudreBrewingRegistry.isDrinkable(fluid)) {
			return new ActionResult<ItemStack>(EnumActionResult.PASS, itemStackIn);
		}

		playerIn.setActiveHand(hand);
		return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemStackIn);
	}

	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		if (!worldIn.isRemote) {
			DrinkContainerHelper.normalizeFluidAge(stack);
		}
	}

	@Override
	public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
		FluidStack fluid = getFluid(stack);

		if (fluid != null && fluid.getFluid() != null && fluid.amount > 0) {
			for (String line : DrinkDisplayHelper.getQualityTooltipLines(fluid)) {
				if (!line.isEmpty()) {
					tooltip.add(TextFormatting.GRAY + line);
				}
			}

			tooltip.add(fluid.amount + " / " + this.getCapacity(stack) + " mB");
		}

		String label = DrinkContainerHelper.getContainerLabel(stack);

		if (label != null && !label.isEmpty()) {
			tooltip.add(TextFormatting.GRAY + "Served at " + label);
		}
	}

	public int getCapacity(ItemStack stack) {
		return getVariant(stack).getVessel().getCapacity();
	}

	public String getModelName(ItemStack stack) {
		Variant variant = getVariant(stack);
		return "drinkware_" + variant.getVessel().getName() + "_" + variant.getMaterial().getModelStyle()
			+ (isFilled(stack) ? "_filled" : "_empty");
	}

	public int getMaterialTint(ItemStack stack) {
		Variant variant = getVariant(stack);

		switch (variant.getMaterial()) {
		case GLASS:
			return GLASS_COLORS[Math.min(variant.getVariantIndex(), GLASS_COLORS.length - 1)];
		case WOOD:
			return getWoodColor(variant.getWoodSuffix());
		case METAL:
			return METAL_COLORS[Math.min(variant.getVariantIndex(), METAL_COLORS.length - 1)];
		case CLAY:
			return 0xFFB86F4A;
		default:
			return DEFAULT_TINT;
		}
	}

	public int getFluidTint(ItemStack stack) {
		return DrinkContainerHelper.getFluidColor(stack, DEFAULT_TINT);
	}

	public int getGlassFilteredFluidTint(ItemStack stack) {
		Variant variant = getVariant(stack);
		int fluidTint = this.getFluidTint(stack);

		if (variant.getMaterial() != MaterialType.GLASS || variant.getVariantIndex() == 0) {
			return fluidTint;
		}

		return blendTint(fluidTint, this.getMaterialTint(stack), STAINED_GLASS_TINT_PERCENT);
	}

	public int getShelfSizeGroup(ItemStack stack) {
		return getVariant(stack).getVessel().getShelfSizeGroup();
	}

	public static boolean isFilled(ItemStack stack) {
		return DrinkContainerHelper.isFilled(stack);
	}

	@Nullable
	public static FluidStack getFluid(ItemStack stack) {
		return DrinkContainerHelper.getFluid(stack);
	}

	public static Variant getVariant(ItemStack stack) {
		return getVariant(stack == null || stack.getItem() == null ? 0 : stack.getMetadata());
	}

	public static Variant getVariant(int metadata) {
		VesselType vessel = VesselType.byId((metadata >> VESSEL_SHIFT) & 0xF);
		MaterialType material = MaterialType.byId((metadata >> MATERIAL_SHIFT) & 0xF);
		Variant variant = new Variant(vessel, material, metadata & INDEX_MASK);
		return variant.isValid() ? variant : new Variant(VesselType.TANKARD, MaterialType.GLASS, 0);
	}

	public static int getMetadata(VesselType vessel, MaterialType material, int variantIndex) {
		return (vessel.getId() << VESSEL_SHIFT) | (material.getId() << MATERIAL_SHIFT)
			| (variantIndex & INDEX_MASK);
	}

	public static int getWoodIndex(String suffix) {
		for (int i = 0; i < WOOD_SUFFIXES.length; i++) {
			if (WOOD_SUFFIXES[i].equals(suffix)) {
				return i;
			}
		}

		return -1;
	}

	public static int getMetalIndex(MetalVariant metal) {
		for (int i = 0; i < METALS.length; i++) {
			if (METALS[i] == metal) {
				return i;
			}
		}

		return -1;
	}

	public static List<MetalVariant> getAvailableMetals() {
		List<MetalVariant> metals = new ArrayList<MetalVariant>();

		for (MetalVariant metal : METALS) {
			if (metal.isAvailable()) {
				metals.add(metal);
			}
		}

		return Collections.unmodifiableList(metals);
	}

	public static List<String> getModelNames() {
		return MODEL_NAMES;
	}

	private static List<String> buildModelNames() {
		Set<String> names = new HashSet<String>();

		for (VesselType vessel : VesselType.values()) {
			for (MaterialType material : MaterialType.values()) {
				Variant variant = new Variant(vessel, material, 0);

				if (variant.isValid()) {
					names.add("drinkware_" + vessel.getName() + "_" + material.getModelStyle() + "_empty");
					names.add("drinkware_" + vessel.getName() + "_" + material.getModelStyle() + "_filled");
				}
			}
		}

		List<String> sorted = new ArrayList<String>(names);
		Collections.sort(sorted);
		return Collections.unmodifiableList(sorted);
	}

	private static int getVariantCount(MaterialType material) {
		switch (material) {
		case GLASS:
			return GLASS_COLORS.length;
		case WOOD:
			return WOOD_SUFFIXES.length;
		case METAL:
			return METALS.length;
		case CLAY:
			return 1;
		default:
			return 0;
		}
	}

	private static int blendTint(int baseColor, int overlayColor, int overlayPercent) {
		int basePercent = 100 - overlayPercent;
		int red = ((baseColor >> 16 & 255) * basePercent
			+ (overlayColor >> 16 & 255) * overlayPercent) / 100;
		int green = ((baseColor >> 8 & 255) * basePercent
			+ (overlayColor >> 8 & 255) * overlayPercent) / 100;
		int blue = ((baseColor & 255) * basePercent
			+ (overlayColor & 255) * overlayPercent) / 100;
		return 0xFF000000 | red << 16 | green << 8 | blue;
	}

	private static boolean isWoodAvailable(String suffix) {
		if (suffix == null) {
			return false;
		}

		if (suffix.startsWith("biomesoplenty_")) {
			return IronAgeFurnitureConfiguration.INTEGRATION_BIOMESOPLENTY
				&& Loader.isModLoaded("BiomesOPlenty");
		}

		if (suffix.startsWith("natura_")) {
			return IronAgeFurnitureConfiguration.INTEGRATION_NATURA && Loader.isModLoaded("natura");
		}

		if (suffix.startsWith("immersiveengineering_")) {
			return IronAgeFurnitureConfiguration.INTEGRATION_IMMERSIVEENGINEERING
				&& Loader.isModLoaded("immersiveengineering");
		}

		if (suffix.startsWith("forestry_")) {
			return IronAgeFurnitureConfiguration.INTEGRATION_FORESTRY && Loader.isModLoaded("forestry");
		}

		return true;
	}

	private static String getWoodDisplayName(String suffix) {
		String name = suffix;

		if ("big_oak".equals(name)) {
			return "Dark Oak Wood";
		}

		String[] prefixes = new String[] { "biomesoplenty_", "natura_", "forestry_",
			"immersiveengineering_" };

		for (String prefix : prefixes) {
			if (name.startsWith(prefix)) {
				name = name.substring(prefix.length());
				break;
			}
		}

		name = name.replace("treatedWood", "treated_wood");
		return titleCase(name) + " Wood";
	}

	private static int getWoodColor(String suffix) {
		String name = suffix == null ? "" : suffix.toLowerCase(Locale.ROOT);

		if (containsAny(name, "ebony", "darkwood", "wenge", "cocobolo")) return 0xFF3A2418;
		if (containsAny(name, "bloodwood", "redwood", "mahogany", "padauk")) return 0xFF8E3F2B;
		if (containsAny(name, "birch", "balsa", "silverbell", "ghostwood")) return 0xFFD6C58C;
		if (containsAny(name, "spruce", "fir", "pine", "larch")) return 0xFF80623C;
		if (containsAny(name, "acacia", "eucalyptus", "cherry", "plum")) return 0xFFB66643;
		if (containsAny(name, "willow", "greenheart", "hopseed", "lime")) return 0xFF7E8050;
		if (containsAny(name, "treated", "tiger", "zebra")) return 0xFF9D7A4E;
		return 0xFFA47A4B;
	}

	private static String titleCase(String value) {
		String[] words = value.replace('_', ' ').split(" ");
		StringBuilder result = new StringBuilder();

		for (String word : words) {
			if (word.isEmpty()) continue;
			if (result.length() > 0) result.append(' ');
			result.append(Character.toUpperCase(word.charAt(0))).append(word.substring(1));
		}

		return result.toString();
	}

	private static boolean containsAny(String value, String... values) {
		for (String candidate : values) {
			if (value.contains(candidate)) return true;
		}
		return false;
	}

	private static void validateMetadataLayout() {
		Set<Integer> metadata = new HashSet<Integer>();

		for (VesselType vessel : VesselType.values()) {
			for (MaterialType material : MaterialType.values()) {
				for (int index = 0; index < getVariantCount(material); index++) {
					Variant variant = new Variant(vessel, material, index);
					if (variant.isValid() && !metadata.add(Integer.valueOf(variant.getMetadata()))) {
						throw new IllegalStateException("Duplicate drinkware metadata " + variant.getMetadata());
					}
				}
			}
		}
	}

	private final class DrinkwareFluidHandler extends FluidHandlerItemStackSimple {
		private DrinkwareFluidHandler(ItemStack container) {
			// ItemStack invokes initCapabilities before assigning its item damage in 1.10.
			super(container, VesselType.TANKARD.getCapacity());
		}

		@Override
		public IFluidTankProperties[] getTankProperties() {
			return new IFluidTankProperties[] { new FluidTankProperties(this.getFluid(), this.getDynamicCapacity()) };
		}

		@Override
		public int fill(FluidStack resource, boolean doFill) {
			int dynamicCapacity = this.getDynamicCapacity();

			if (this.container.stackSize != 1 || resource == null || resource.amount < dynamicCapacity
					|| !this.canFillFluidType(resource) || this.getFluid() != null) {
				return 0;
			}

			if (doFill) {
				FluidStack filled = resource.copy();
				filled.amount = dynamicCapacity;
				this.setFluid(filled);
			}

			return dynamicCapacity;
		}

		@Override
		public FluidStack drain(int maxDrain, boolean doDrain) {
			int dynamicCapacity = this.getDynamicCapacity();
			FluidStack contained = this.getFluid();

			if (this.container.stackSize != 1 || maxDrain < dynamicCapacity || contained == null
					|| contained.amount != dynamicCapacity || !this.canDrainFluidType(contained)) {
				return null;
			}

			FluidStack drained = contained.copy();

			if (doDrain) {
				this.setContainerToEmpty();
			}

			return drained;
		}

		@Override
		public boolean canFillFluidType(FluidStack fluid) {
			return fluid != null && FoudreBrewingRegistry.isBottleable(fluid);
		}

		@Override
		protected void setContainerToEmpty() {
			super.setContainerToEmpty();

			if (this.container.hasTagCompound()) {
				this.container.getTagCompound().removeTag(DrinkContainerHelper.SERVING_LABEL_TAG);

				if (this.container.getTagCompound().hasNoTags()) {
					this.container.setTagCompound(null);
				}
			}
		}

		private int getDynamicCapacity() {
			return this.container.getItem() == null ? VesselType.TANKARD.getCapacity()
				: ItemDrinkware.this.getCapacity(this.container);
		}
	}

	public static final class Variant {
		private final VesselType vessel;
		private final MaterialType material;
		private final int variantIndex;

		private Variant(VesselType vessel, MaterialType material, int variantIndex) {
			this.vessel = vessel;
			this.material = material;
			this.variantIndex = variantIndex;
		}

		public VesselType getVessel() { return this.vessel; }
		public MaterialType getMaterial() { return this.material; }
		public int getVariantIndex() { return this.variantIndex; }
		public int getMetadata() { return ItemDrinkware.getMetadata(this.vessel, this.material, this.variantIndex); }

		@Nullable
		public String getWoodSuffix() {
			return this.material == MaterialType.WOOD && this.variantIndex >= 0
				&& this.variantIndex < WOOD_SUFFIXES.length ? WOOD_SUFFIXES[this.variantIndex] : null;
		}

		public String getMaterialDisplayName() {
			switch (this.material) {
			case GLASS:
				return this.variantIndex == 0 ? "Clear Glass"
					: titleCase(getDyeName(this.variantIndex - 1)) + " Glass";
			case WOOD:
				return getWoodDisplayName(this.getWoodSuffix());
			case METAL:
				return METALS[this.variantIndex].getDisplayName();
			case CLAY:
				return "Clay";
			default:
				return "Unknown";
			}
		}

		public boolean isValid() {
			if (this.vessel == null || this.material == null || this.variantIndex < 0
					|| this.variantIndex >= getVariantCount(this.material)) {
				return false;
			}

			switch (this.vessel) {
			case TANKARD:
				return this.material == MaterialType.GLASS || this.material == MaterialType.WOOD
					|| this.material == MaterialType.METAL;
			case WINE_GLASS:
			case SPIRIT_GLASS:
			case SHOT_GLASS:
				return this.material == MaterialType.GLASS;
			case MUG:
				return this.material == MaterialType.WOOD || this.material == MaterialType.METAL
					|| this.material == MaterialType.CLAY;
			default:
				return false;
			}
		}

		public boolean isAvailable() {
			if (!this.isValid()) return false;
			if (this.material == MaterialType.WOOD) return isWoodAvailable(this.getWoodSuffix());
			if (this.material == MaterialType.METAL) return METALS[this.variantIndex].isAvailable();
			return true;
		}
	}

	public enum VesselType {
		TANKARD(0, "tankard", "Tankard", 125, 0),
		WINE_GLASS(1, "wine_glass", "Wine Glass", 60, 0),
		SPIRIT_GLASS(2, "spirit_glass", "Spirit Glass", 30, 1),
		SHOT_GLASS(3, "shot_glass", "Shot Glass", 15, 2),
		MUG(4, "mug", "Mug", 60, 0);

		private final int id;
		private final String name;
		private final String displayName;
		private final int capacity;
		private final int shelfSizeGroup;

		private VesselType(int id, String name, String displayName, int capacity, int shelfSizeGroup) {
			this.id = id;
			this.name = name;
			this.displayName = displayName;
			this.capacity = capacity;
			this.shelfSizeGroup = shelfSizeGroup;
		}

		public int getId() { return this.id; }
		public String getName() { return this.name; }
		public String getDisplayName() { return this.displayName; }
		public int getCapacity() { return this.capacity; }
		public int getShelfSizeGroup() { return this.shelfSizeGroup; }

		private static VesselType byId(int id) {
			for (VesselType value : values()) if (value.id == id) return value;
			return TANKARD;
		}
	}

	public enum MaterialType {
		GLASS(0, "glass"), WOOD(1, "wood"), METAL(2, "metal"), CLAY(3, "clay");

		private final int id;
		private final String modelStyle;

		private MaterialType(int id, String modelStyle) {
			this.id = id;
			this.modelStyle = modelStyle;
		}

		public int getId() { return this.id; }
		public String getModelStyle() { return this.modelStyle; }

		private static MaterialType byId(int id) {
			for (MaterialType value : values()) if (value.id == id) return value;
			return GLASS;
		}
	}

	private static String getDyeName(int metadata) {
		String[] names = new String[] { "white", "orange", "magenta", "light_blue", "yellow", "lime",
			"pink", "gray", "silver", "cyan", "purple", "blue", "brown", "green", "red", "black" };
		return names[Math.max(0, Math.min(metadata, names.length - 1))];
	}
}
