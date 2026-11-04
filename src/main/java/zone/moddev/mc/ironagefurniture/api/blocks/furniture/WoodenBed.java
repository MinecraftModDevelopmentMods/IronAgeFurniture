package zone.moddev.mc.ironagefurniture.api.blocks.furniture;

/** The mattress and wooden frame occupy two cells, or four for a double. */
public final class WoodenBed extends FurnitureBed {
    public WoodenBed(String name, boolean doubleBed) { super(name, doubleBed); }
    @Override protected int height() { return 1; }
}
