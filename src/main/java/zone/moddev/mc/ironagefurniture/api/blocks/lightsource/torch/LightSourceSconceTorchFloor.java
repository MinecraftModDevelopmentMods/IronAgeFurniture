package zone.moddev.mc.ironagefurniture.api.blocks.lightsource.torch;

import java.util.ArrayList;
import java.util.List;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.redstone.Orientation;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;
import zone.moddev.mc.ironagefurniture.api.SconceMetalData;
import zone.moddev.mc.ironagefurniture.api.blocks.lightholder.LightHolderSconceFloor;
import zone.moddev.mc.ironagefurniture.init.ModVanillaLights;
import zone.moddev.mc.ironagefurniture.init.PhaseFourLighting;
import zone.moddev.mc.ironagefurniture.init.RegistrationProperties;

public class LightSourceSconceTorchFloor extends LightHolderSconceFloor {
    protected static final VoxelShape AABB = Block.box(6, 0, 6, 10, 13, 10);
    protected ParticleOptions flameParticle = ParticleTypes.FLAME;

    public LightSourceSconceTorchFloor(Properties properties) { super(properties); }
    public LightSourceSconceTorchFloor(float hardness, float resistance, SoundType sound, String name) {
        this(RegistrationProperties.block(Properties.of().strength(hardness, resistance).sound(sound).lightLevel(state -> 14), name));
    }
    @Override public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
        List<ItemStack> drops = new ArrayList<>();
        drops.add(SconceMetalData.frame(state));
        if (ShouldDrop()) drops.add(new ItemStack(LightDrop()));
        return drops;
    }
    protected Block LightDrop() { return Blocks.TORCH; }
    protected boolean ShouldDrop() { return true; }
    protected boolean CanEx() { return true; }
    protected boolean HasFlame() { return true; }
    protected boolean InvertDirection() { return false; }
    protected boolean isLit() { return true; }
    protected Block UnlitVariant() { return ModVanillaLights.light_metal_ironage_sconce_floor_torch_iron_unlit.get(); }
    protected Block LitVariant() { return ModVanillaLights.light_metal_ironage_sconce_floor_torch_iron.get(); }
    protected Block EmptyVariant() { return ModVanillaLights.light_metal_ironage_sconce_floor_empty_iron.get(); }
    protected Block DropVariant() { return ModVanillaLights.light_metal_ironage_sconce_floor_empty_iron.get(); }

    @Override protected void generateShapes(ImmutableList<BlockState> states) {
        ImmutableMap.Builder<BlockState, VoxelShape> shapes = ImmutableMap.builder();
        for (BlockState state : states) shapes.put(state, AABB);
        _shapes = shapes.build();
    }
    @Override protected boolean shouldRedstoneWireConnectTo(BlockState state, BlockGetter level, BlockPos pos, @Nullable Direction direction) {
        return CanEx();
    }
    @Override public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (isLit() && HasFlame()) {
            level.addParticle(ParticleTypes.SMOKE, pos.getX() + .5, pos.getY() + .9, pos.getZ() + .5, 0, 0, 0);
            level.addParticle(flameParticle, pos.getX() + .5, pos.getY() + .9, pos.getZ() + .5, 0, 0, 0);
        }
    }
    @Override public void onPlace(BlockState state, Level level, BlockPos pos, BlockState previous, boolean moving) {
        super.onPlace(state, level, pos, previous, moving);
        if (CanEx() && !level.isClientSide()) level.scheduleTick(pos, this, 2);
    }
    @Override protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block changed, @Nullable Orientation orientation, boolean moving) {
        if (!level.isClientSide()) level.scheduleTick(pos, this, 2);
    }
    protected boolean hasNeighborSignal(Level level, BlockPos pos, BlockState state) { return level.hasNeighborSignal(pos); }
    @Override public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (!CanEx()) return;
        boolean power = hasNeighborSignal(level, pos, state);
        boolean next = state.getValue(WATERLOGGED) ? power : isLit() || power;
        if (isLit() != next) replace(state, level, pos, next ? LitVariant() : UnlitVariant());
    }
    protected void replace(BlockState state, LevelAccessor level, BlockPos pos, Block block) {
        level.setBlock(pos, block.defaultBlockState().withPropertiesOf(state), UPDATE_ALL);
    }
    @Override public boolean placeLiquid(LevelAccessor level, BlockPos pos, BlockState state, FluidState fluid) {
        if (state.getValue(WATERLOGGED) || !fluid.is(Fluids.WATER)) return false;
        if (!level.isClientSide()) {
            Block target = CanEx() ? UnlitVariant() : this;
            replace(state.setValue(WATERLOGGED, true), level, pos, target);
            level.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
            level.scheduleTick(pos, target, 2);
        }
        return true;
    }
    @Override protected InteractionResult ActivateSconce(BlockState state, Level level, BlockPos pos,
            Player player, InteractionHand hand, BlockHitResult hit) {
        ItemStack held = player.getItemInHand(hand);
        if (PhaseFourLighting.addSecondTorch(state, level, pos, player, hand)) return InteractionResult.SUCCESS_SERVER;
        if (CanEx() && held.is(Items.WATER_BUCKET)) {
            replace(state, level, pos, UnlitVariant());
            return InteractionResult.SUCCESS_SERVER;
        }
        if (CanEx() && !isLit() && !state.getValue(WATERLOGGED) && (held.is(Items.TORCH) || held.is(Items.FLINT_AND_STEEL))) {
            replace(state, level, pos, LitVariant());
            if (held.is(Items.FLINT_AND_STEEL) && !player.isCreative())
                held.hurtAndBreak(1, player, hand == InteractionHand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND);
            return InteractionResult.SUCCESS_SERVER;
        }
        if (held.isEmpty() || held.is(LightDrop().asItem())) {
            BlockState empty = EmptyVariant().defaultBlockState().withPropertiesOf(state);
            if (InvertDirection()) empty = empty.setValue(DIRECTION, state.getValue(DIRECTION).getOpposite());
            level.setBlock(pos, empty, UPDATE_ALL);
            if (!player.isCreative()) {
                ItemStack content = new ItemStack(LightDrop());
                player.getInventory().placeItemBackInInventory(content, net.minecraft.util.Prediction.SERVER_ONLY);
            }
            return InteractionResult.SUCCESS_SERVER;
        }
        return InteractionResult.PASS;
    }
}
