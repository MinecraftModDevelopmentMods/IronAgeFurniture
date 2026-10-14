package zone.moddev.mc.ironagefurniture.api.blocks.furniture;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BedPart;
import net.minecraft.state.properties.BlockStateProperties;

public final class WoodenBed extends FurnitureBed {
    public WoodenBed(String name, boolean doubleBed) {
        super(name, doubleBed);
        registerDefaultState(defaultBlockState().setValue(BlockStateProperties.BED_PART, BedPart.FOOT));
    }
    @Override protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(BlockStateProperties.BED_PART);
    }
    @Override protected boolean head(BlockState state) { return state.getValue(BlockStateProperties.BED_PART) == BedPart.HEAD; }
    @Override protected boolean upper(BlockState state) { return false; }
    @Override protected BlockState partState(BlockState state, boolean head, boolean upper) {
        return state.setValue(BlockStateProperties.BED_PART, head ? BedPart.HEAD : BedPart.FOOT);
    }
    @Override protected int height() { return 1; }
}
