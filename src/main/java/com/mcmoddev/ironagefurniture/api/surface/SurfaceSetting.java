package com.mcmoddev.ironagefurniture.api.surface;

import com.mcmoddev.ironagefurniture.api.Blocks.BottleRack;
import com.mcmoddev.ironagefurniture.api.Items.ItemDrinkware;
import com.mcmoddev.ironagefurniture.api.Items.ItemDrinkware.VesselType;
import com.mcmoddev.ironagefurniture.api.SurfaceItemRules;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;

public class SurfaceSetting {
	private static final String ITEMS_TAG = "SurfaceItems";
	private static final String AXIS_TAG = "SurfaceAxis";
	private static final double GUEST_A_POSITION = 0.25D;
	private static final double CENTER_POSITION = 0.5D;
	private static final double GUEST_B_POSITION = 0.75D;

	public static enum Slot {
		GUEST_A(0),
		CENTER(1),
		GUEST_B(2);

		private final int index;

		private Slot(int index) {
			this.index = index;
		}

		public int getIndex() {
			return this.index;
		}

		public static Slot byIndex(int index) {
			for (Slot slot : values()) {
				if (slot.index == index) {
					return slot;
				}
			}

			return CENTER;
		}
	}

	public static enum Category {
		BOTTLE,
		CANDLE,
		DRINKWARE,
		MEAL,
		SINGLETON
	}

	private final ItemStack[] items = new ItemStack[Slot.values().length];
	private final EnumFacing[] facings = new EnumFacing[Slot.values().length];
	private EnumFacing.Axis axis;

	public SurfaceSetting() {
		this.clear();
	}

	public boolean hasAnyItem() {
		return this.getItemCount() > 0;
	}

	public int getItemCount() {
		int count = 0;

		for (ItemStack itemStack : this.items) {
			if (!isEmpty(itemStack)) {
				count++;
			}
		}

		return count;
	}

	public ItemStack getItem(Slot slot) {
		return slot == null ? null : this.items[slot.getIndex()];
	}

	public EnumFacing getFacing(Slot slot) {
		if (slot == null) {
			return EnumFacing.NORTH;
		}

		EnumFacing facing = this.facings[slot.getIndex()];
		return horizontalOrNorth(facing);
	}

	public EnumFacing.Axis getAxis() {
		return this.axis;
	}

	public ItemStack getCompatibilityItem() {
		if (!isEmpty(this.getItem(Slot.CENTER))) {
			return this.getItem(Slot.CENTER);
		}
		if (!isEmpty(this.getItem(Slot.GUEST_A))) {
			return this.getItem(Slot.GUEST_A);
		}

		return this.getItem(Slot.GUEST_B);
	}

	public EnumFacing getCompatibilityFacing() {
		for (Slot slot : new Slot[] { Slot.CENTER, Slot.GUEST_A, Slot.GUEST_B }) {
			if (!isEmpty(this.getItem(slot))) {
				return this.getFacing(slot);
			}
		}

		return EnumFacing.NORTH;
	}

	public void setSingleItem(ItemStack itemStack, EnumFacing facing) {
		this.clear();

		if (!isEmpty(itemStack)) {
			this.put(Slot.CENTER, copySingle(itemStack), facing);
		}
	}

	public ItemStack removeCompatibilityItem() {
		for (Slot slot : new Slot[] { Slot.CENTER, Slot.GUEST_A, Slot.GUEST_B }) {
			if (!isEmpty(this.getItem(slot))) {
				return this.remove(slot);
			}
		}

		return null;
	}

	public ItemStack remove(Slot slot) {
		if (slot == null) {
			return null;
		}

		int index = slot.getIndex();
		ItemStack itemStack = this.items[index];
		this.items[index] = null;
		this.facings[index] = EnumFacing.NORTH;

		if (!this.hasAnyItem()) {
			this.axis = null;
		}

		return itemStack;
	}

	public void replace(Slot slot, ItemStack itemStack) {
		if (slot == null) {
			return;
		}

		this.items[slot.getIndex()] = copySingle(itemStack);
	}

	public boolean canInsert(ItemStack itemStack, EnumFacing facing, float hitX, float hitZ,
			EnumFacing.Axis forcedAxis) {
		SurfaceSetting copy = this.copy();
		return copy.insert(itemStack, facing, hitX, hitZ, forcedAxis);
	}

	public boolean insert(ItemStack itemStack, EnumFacing facing, float hitX, float hitZ,
			EnumFacing.Axis forcedAxis) {
		if (isEmpty(itemStack)) {
			return false;
		}

		Category incoming = classify(itemStack);

		if (!this.hasAnyItem()) {
			this.axis = incoming == Category.SINGLETON ? null : chooseAxis(hitX, hitZ, facing, forcedAxis);

			if (incoming == Category.BOTTLE || incoming == Category.CANDLE || incoming == Category.SINGLETON) {
				this.put(Slot.CENTER, copySingle(itemStack), facing);
			} else {
				this.put(this.preferredGuest(hitX, hitZ, this.axis), copySingle(itemStack), facing);
			}

			return true;
		}

		if (this.hasCategory(Category.SINGLETON) || incoming == Category.SINGLETON) {
			return false;
		}

		if (this.axis == null) {
			this.axis = chooseAxis(hitX, hitZ, facing, forcedAxis);
		} else if (forcedAxis != null) {
			this.axis = forcedAxis;
		}

		Slot preferred = this.preferredGuest(hitX, hitZ, this.axis);

		if (incoming == Category.MEAL) {
			return this.insertMeal(itemStack, facing, preferred);
		}
		if (this.hasCategory(Category.MEAL)) {
			return this.insertBesideMeal(itemStack, facing);
		}
		if (incoming == Category.CANDLE) {
			return this.insertCandle(itemStack, facing);
		}
		if (incoming == Category.BOTTLE) {
			return this.insertBottle(itemStack, facing, preferred);
		}
		if (incoming == Category.DRINKWARE) {
			return this.insertDrinkware(itemStack, facing, preferred);
		}

		return false;
	}

	public Slot findTarget(float hitX, float hitZ) {
		Slot closest = null;
		double closestDistance = Double.MAX_VALUE;

		for (Slot slot : Slot.values()) {
			ItemStack itemStack = this.getItem(slot);

			if (isEmpty(itemStack)) {
				continue;
			}

			AxisAlignedBB bounds = this.getItemBounds(slot, itemStack);

			if (hitX >= bounds.minX && hitX <= bounds.maxX && hitZ >= bounds.minZ && hitZ <= bounds.maxZ) {
				double dx = hitX - (bounds.minX + bounds.maxX) / 2.0D;
				double dz = hitZ - (bounds.minZ + bounds.maxZ) / 2.0D;
				double distance = dx * dx + dz * dz;

				if (distance < closestDistance) {
					closest = slot;
					closestDistance = distance;
				}
			}
		}

		return closest;
	}

	public double getX(Slot slot) {
		return this.axis == EnumFacing.Axis.X ? getSlotPosition(slot) : CENTER_POSITION;
	}

	public double getZ(Slot slot) {
		return this.axis == EnumFacing.Axis.Z ? getSlotPosition(slot) : CENTER_POSITION;
	}

	public AxisAlignedBB getItemBounds(Slot slot) {
		return this.getItemBounds(slot, this.getItem(slot));
	}

	public AxisAlignedBB getCombinedBounds() {
		AxisAlignedBB combined = null;

		for (Slot slot : Slot.values()) {
			if (isEmpty(this.getItem(slot))) {
				continue;
			}

			AxisAlignedBB bounds = this.getItemBounds(slot);
			combined = combined == null ? bounds : union(combined, bounds);
		}

		return combined;
	}

	public void clear() {
		for (int i = 0; i < this.items.length; i++) {
			this.items[i] = null;
			this.facings[i] = EnumFacing.NORTH;
		}

		this.axis = null;
	}

	public void readFromNBT(NBTTagCompound compound) {
		this.clear();

		if (compound.hasKey(ITEMS_TAG, 9)) {
			NBTTagList list = compound.getTagList(ITEMS_TAG, 10);

			for (int i = 0; i < list.tagCount(); i++) {
				NBTTagCompound itemTag = list.getCompoundTagAt(i);
				Slot slot = Slot.byIndex(itemTag.getByte("SurfaceSlot") & 255);
				ItemStack itemStack = ItemStack.loadItemStackFromNBT(itemTag);

				if (!isEmpty(itemStack) && isEmpty(this.getItem(slot))) {
					EnumFacing facing = EnumFacing.getHorizontal(itemTag.getByte("Facing") & 3);
					this.put(slot, itemStack, facing);
				}
			}

			if (compound.hasKey(AXIS_TAG, 1)) {
				this.axis = compound.getByte(AXIS_TAG) == 0 ? EnumFacing.Axis.X : EnumFacing.Axis.Z;
			}
		} else if (compound.hasKey("DisplayedItem", 10)) {
			ItemStack legacyItem = ItemStack.loadItemStackFromNBT(compound.getCompoundTag("DisplayedItem"));
			EnumFacing facing = compound.hasKey("DisplayedFacing", 3)
				? EnumFacing.getHorizontal(compound.getInteger("DisplayedFacing") & 3) : EnumFacing.NORTH;

			if (!isEmpty(legacyItem)) {
				this.put(Slot.CENTER, legacyItem, facing);
			}
		}
	}

	public void writeToNBT(NBTTagCompound compound) {
		NBTTagList list = new NBTTagList();

		for (Slot slot : Slot.values()) {
			ItemStack itemStack = this.getItem(slot);

			if (isEmpty(itemStack)) {
				continue;
			}

			NBTTagCompound itemTag = new NBTTagCompound();
			itemStack.writeToNBT(itemTag);
			itemTag.setByte("SurfaceSlot", (byte)slot.getIndex());
			itemTag.setByte("Facing", (byte)this.getFacing(slot).getHorizontalIndex());
			list.appendTag(itemTag);
		}

		if (list.tagCount() > 0) {
			compound.setTag(ITEMS_TAG, list);

			if (this.axis != null) {
				compound.setByte(AXIS_TAG, (byte)(this.axis == EnumFacing.Axis.X ? 0 : 1));
			} else {
				compound.removeTag(AXIS_TAG);
			}
		} else {
			compound.removeTag(ITEMS_TAG);
			compound.removeTag(AXIS_TAG);
		}

		compound.removeTag("DisplayedItem");
		compound.removeTag("DisplayedFacing");
	}

	public static Category classify(ItemStack itemStack) {
		if (itemStack != null && itemStack.getItem() instanceof ItemDrinkware) {
			return Category.DRINKWARE;
		}
		if (BottleRack.isValidBottleItem(itemStack)) {
			return Category.BOTTLE;
		}
		if (SurfaceItemRules.isCandle(itemStack)) {
			return Category.CANDLE;
		}
		if (SurfaceItemRules.isMeal(itemStack)) {
			return Category.MEAL;
		}

		return Category.SINGLETON;
	}

	public static boolean isSettingItem(ItemStack itemStack) {
		Category category = classify(itemStack);
		return category == Category.BOTTLE || category == Category.CANDLE
			|| category == Category.DRINKWARE || category == Category.MEAL;
	}

	public boolean hasCandle() {
		return this.hasCategory(Category.CANDLE);
	}

	private boolean insertMeal(ItemStack itemStack, EnumFacing facing, Slot preferred) {
		if (this.hasCategory(Category.MEAL) || this.getItemCount() != 1) {
			return false;
		}

		Slot existingSlot = this.firstOccupiedSlot();
		Category existingCategory = classify(this.getItem(existingSlot));

		if (existingCategory != Category.BOTTLE && existingCategory != Category.DRINKWARE) {
			return false;
		}

		if (existingSlot == Slot.CENTER) {
			this.move(existingSlot, oppositeGuest(preferred));
			existingSlot = oppositeGuest(preferred);
		}

		this.put(oppositeGuest(existingSlot), copySingle(itemStack), facing);
		return true;
	}

	private boolean insertBesideMeal(ItemStack itemStack, EnumFacing facing) {
		if (this.getItemCount() != 1) {
			return false;
		}

		Category incoming = classify(itemStack);

		if (incoming != Category.BOTTLE && incoming != Category.DRINKWARE) {
			return false;
		}

		Slot mealSlot = this.findCategorySlot(Category.MEAL);

		if (mealSlot == Slot.CENTER) {
			mealSlot = Slot.GUEST_A;
			this.move(Slot.CENTER, mealSlot);
		}

		this.put(oppositeGuest(mealSlot), copySingle(itemStack), facing);
		return true;
	}

	private boolean insertBottle(ItemStack itemStack, EnumFacing facing, Slot preferred) {
		if (this.hasCategory(Category.CANDLE)) {
			return false;
		}

		int bottleCount = this.countCategory(Category.BOTTLE);
		int drinkwareCount = this.countCategory(Category.DRINKWARE);

		if (bottleCount == 0 && drinkwareCount <= 2) {
			if (!isEmpty(this.getItem(Slot.CENTER)) && drinkwareCount == 1 && this.getItemCount() == 1) {
				this.move(Slot.CENTER, preferred);
			}

			if (isEmpty(this.getItem(Slot.CENTER))) {
				this.put(Slot.CENTER, copySingle(itemStack), facing);
				return true;
			}
		}

		if (bottleCount != 1 || drinkwareCount != 0 || this.getItemCount() != 1) {
			return false;
		}

		Slot oldBottleSlot = this.findCategorySlot(Category.BOTTLE);

		if (oldBottleSlot == Slot.CENTER) {
			this.move(Slot.CENTER, oppositeGuest(preferred));
			this.put(preferred, copySingle(itemStack), facing);
			return true;
		}

		Slot target = oppositeGuest(oldBottleSlot);

		if (isEmpty(this.getItem(target))) {
			this.put(target, copySingle(itemStack), facing);
			return true;
		}

		return false;
	}

	private boolean insertDrinkware(ItemStack itemStack, EnumFacing facing, Slot preferred) {
		int bottleCount = this.countCategory(Category.BOTTLE);
		int candleCount = this.countCategory(Category.CANDLE);
		int drinkwareCount = this.countCategory(Category.DRINKWARE);

		if (bottleCount + candleCount > 1 || drinkwareCount >= 2) {
			return false;
		}

		if (bottleCount + candleCount == 1) {
			Slot centerSlot = bottleCount == 1
				? this.findCategorySlot(Category.BOTTLE) : this.findCategorySlot(Category.CANDLE);

			if (centerSlot != Slot.CENTER && this.getItemCount() == 1) {
				this.move(centerSlot, Slot.CENTER);
			}
		}

		if (bottleCount + candleCount == 0 && drinkwareCount == 1 && this.getItemCount() == 1
				&& !isEmpty(this.getItem(Slot.CENTER))) {
			this.move(Slot.CENTER, oppositeGuest(preferred));
		}

		Slot target = preferred;

		if (!isEmpty(this.getItem(target))) {
			target = oppositeGuest(target);
		}

		if (!isEmpty(this.getItem(target))) {
			return false;
		}

		this.put(target, copySingle(itemStack), facing);
		return true;
	}

	private boolean insertCandle(ItemStack itemStack, EnumFacing facing) {
		if (this.hasCategory(Category.BOTTLE) || this.hasCategory(Category.CANDLE)
				|| this.hasCategory(Category.MEAL) || this.countCategory(Category.DRINKWARE) > 2
				|| !isEmpty(this.getItem(Slot.CENTER))) {
			return false;
		}

		this.put(Slot.CENTER, copySingle(itemStack), facing);
		return true;
	}

	private AxisAlignedBB getItemBounds(Slot slot, ItemStack itemStack) {
		double width = 0.5D;
		double height = 0.75D;
		Category category = classify(itemStack);

		if (category == Category.BOTTLE) {
			width = 0.22D;
			height = 0.68D;
		} else if (category == Category.CANDLE) {
			width = 0.34D;
			height = 0.42D;
		} else if (category == Category.MEAL) {
			width = 0.42D;
			height = 0.20D;
		} else if (category == Category.DRINKWARE && itemStack.getItem() instanceof ItemDrinkware) {
			VesselType vessel = ItemDrinkware.getVariant(itemStack).getVessel();

			switch (vessel) {
			case TANKARD:
			case MUG:
				width = 0.30D;
				height = 0.44D;
				break;
			case WINE_GLASS:
				width = 0.22D;
				height = 0.42D;
				break;
			case SPIRIT_GLASS:
				width = 0.18D;
				height = 0.32D;
				break;
			case SHOT_GLASS:
				width = 0.14D;
				height = 0.22D;
				break;
			default:
				break;
			}
		}

		double centerX = this.getX(slot);
		double centerZ = this.getZ(slot);
		double half = width / 2.0D;
		return new AxisAlignedBB(centerX - half, 0.0D, centerZ - half,
			centerX + half, height, centerZ + half);
	}

	private void put(Slot slot, ItemStack itemStack, EnumFacing facing) {
		this.items[slot.getIndex()] = itemStack;
		this.facings[slot.getIndex()] = horizontalOrNorth(facing);
	}

	private void move(Slot from, Slot to) {
		this.items[to.getIndex()] = this.items[from.getIndex()];
		this.facings[to.getIndex()] = this.facings[from.getIndex()];
		this.items[from.getIndex()] = null;
		this.facings[from.getIndex()] = EnumFacing.NORTH;
	}

	private boolean hasCategory(Category category) {
		return this.countCategory(category) > 0;
	}

	private int countCategory(Category category) {
		int count = 0;

		for (Slot slot : Slot.values()) {
			if (!isEmpty(this.getItem(slot)) && classify(this.getItem(slot)) == category) {
				count++;
			}
		}

		return count;
	}

	private Slot findCategorySlot(Category category) {
		for (Slot slot : Slot.values()) {
			if (!isEmpty(this.getItem(slot)) && classify(this.getItem(slot)) == category) {
				return slot;
			}
		}

		return null;
	}

	private Slot firstOccupiedSlot() {
		for (Slot slot : Slot.values()) {
			if (!isEmpty(this.getItem(slot))) {
				return slot;
			}
		}

		return null;
	}

	private Slot preferredGuest(float hitX, float hitZ, EnumFacing.Axis selectedAxis) {
		float coordinate = selectedAxis == EnumFacing.Axis.X ? hitX : hitZ;
		return coordinate < 0.5F ? Slot.GUEST_A : Slot.GUEST_B;
	}

	private SurfaceSetting copy() {
		SurfaceSetting copy = new SurfaceSetting();
		copy.axis = this.axis;

		for (Slot slot : Slot.values()) {
			if (!isEmpty(this.getItem(slot))) {
				copy.put(slot, this.getItem(slot).copy(), this.getFacing(slot));
			}
		}

		return copy;
	}

	private static EnumFacing.Axis chooseAxis(float hitX, float hitZ, EnumFacing facing,
			EnumFacing.Axis forcedAxis) {
		if (forcedAxis != null) {
			return forcedAxis;
		}

		float xDistance = Math.abs(hitX - 0.5F);
		float zDistance = Math.abs(hitZ - 0.5F);

		if (Math.max(xDistance, zDistance) > 0.10F) {
			return xDistance >= zDistance ? EnumFacing.Axis.X : EnumFacing.Axis.Z;
		}

		return facing != null && facing.getAxis().isHorizontal()
			? facing.getAxis() : EnumFacing.Axis.X;
	}

	private static Slot oppositeGuest(Slot slot) {
		return slot == Slot.GUEST_A ? Slot.GUEST_B : Slot.GUEST_A;
	}

	private static double getSlotPosition(Slot slot) {
		switch (slot) {
		case GUEST_A:
			return GUEST_A_POSITION;
		case GUEST_B:
			return GUEST_B_POSITION;
		default:
			return CENTER_POSITION;
		}
	}

	private static ItemStack copySingle(ItemStack itemStack) {
		if (isEmpty(itemStack)) {
			return null;
		}

		ItemStack copy = itemStack.copy();
		copy.stackSize = 1;
		return copy;
	}

	private static EnumFacing horizontalOrNorth(EnumFacing facing) {
		return facing != null && facing.getAxis().isHorizontal() ? facing : EnumFacing.NORTH;
	}

	private static boolean isEmpty(ItemStack itemStack) {
		return itemStack == null || itemStack.stackSize <= 0;
	}

	private static AxisAlignedBB union(AxisAlignedBB first, AxisAlignedBB second) {
		return new AxisAlignedBB(Math.min(first.minX, second.minX), Math.min(first.minY, second.minY),
			Math.min(first.minZ, second.minZ), Math.max(first.maxX, second.maxX),
			Math.max(first.maxY, second.maxY), Math.max(first.maxZ, second.maxZ));
	}
}
