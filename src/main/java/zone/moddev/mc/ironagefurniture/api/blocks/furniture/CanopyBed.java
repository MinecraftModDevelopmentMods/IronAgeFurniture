package zone.moddev.mc.ironagefurniture.api.blocks.furniture;

import java.util.function.Supplier;
import zone.moddev.mc.ironagefurniture.api.enumerations.WoodBedSide;

/** A canopy adds an upper layer without turning any part into a separate item. */
public final class CanopyBed extends FurnitureBed {
    private Supplier<CanopyBed> left = () -> this;
    private Supplier<CanopyBed> right = () -> this;
    public CanopyBed(String name, boolean doubleBed) { super(name, doubleBed); }
    public void setDoubleBlocks(Supplier<CanopyBed> left, Supplier<CanopyBed> right) { this.left = left; this.right = right; }
    @Override protected int height() { return 2; }
    @Override protected FurnitureBed blockForSide(WoodBedSide side) { return side == WoodBedSide.LEFT ? left.get() : right.get(); }
    @Override public FurnitureBed itemBlock() { return left.get(); }
}
