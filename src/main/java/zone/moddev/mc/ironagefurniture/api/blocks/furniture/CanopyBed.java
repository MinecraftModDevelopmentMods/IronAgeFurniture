package zone.moddev.mc.ironagefurniture.api.blocks.furniture;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import zone.moddev.mc.ironagefurniture.api.enumerations.CanopyBedPart;
import zone.moddev.mc.ironagefurniture.api.enumerations.WoodBedSide;

/** The old right-hand canopy ID remains a hidden partner of the double bed. */
public final class CanopyBed extends FurnitureBed {
    public static final EnumProperty<CanopyBedPart> PART = EnumProperty.create("part", CanopyBedPart.class);
    private CanopyBed left, right;
    public CanopyBed(String name, boolean doubleBed) {
        super(name, doubleBed);
        registerDefaultState(defaultBlockState().setValue(PART, CanopyBedPart.FOOT_LOWER));
    }
    public void setDoubleBlocks(CanopyBed left, CanopyBed right) { this.left = left; this.right = right; }
    @Override public FurnitureBed itemBlock() { return left == null ? this : left; }
    @Override protected FurnitureBed blockForSide(WoodBedSide side) {
        return left == null ? this : side == WoodBedSide.LEFT ? left : right;
    }
    @Override protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(PART);
    }
    @Override protected boolean head(BlockState state) { return state.getValue(PART).isHead(); }
    @Override protected boolean upper(BlockState state) { return state.getValue(PART).isUpper(); }
    @Override protected BlockState partState(BlockState state, boolean head, boolean upper) {
        return state.setValue(PART, head ? upper ? CanopyBedPart.HEAD_UPPER : CanopyBedPart.HEAD_LOWER
                : upper ? CanopyBedPart.FOOT_UPPER : CanopyBedPart.FOOT_LOWER);
    }
    @Override protected int height() { return 2; }
}
