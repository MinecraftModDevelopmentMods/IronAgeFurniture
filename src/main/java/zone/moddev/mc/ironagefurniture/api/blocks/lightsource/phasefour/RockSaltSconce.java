package zone.moddev.mc.ironagefurniture.api.blocks.lightsource.phasefour;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import zone.moddev.mc.ironagefurniture.api.blocks.base.FurnitureBlock;
import zone.moddev.mc.ironagefurniture.api.blocks.lightsource.glow.LightSourceSconceGlowFloor;
import zone.moddev.mc.ironagefurniture.init.ModVanillaLights;

/** Mineralogy provides the lamp; an absent provider never becomes a hard dependency. */
public final class RockSaltSconce extends LightSourceSconceGlowFloor {
    public static final ResourceLocation LAMP = new ResourceLocation("mineralogy:rocksaltlamp");
    private final boolean wall;
    public RockSaltSconce(String path, boolean wall) {
        super(Properties.of().strength(1, 10).sound(SoundType.METAL).noOcclusion().lightLevel(state -> 15));
        this.wall = wall;
    }
    @Override protected Block LightDrop() { return BuiltInRegistries.BLOCK.get(LAMP); }
    private static boolean providerLampAvailable() {
        return BuiltInRegistries.BLOCK.containsKey(LAMP) && BuiltInRegistries.ITEM.containsKey(LAMP);
    }
    @Override protected net.minecraft.world.InteractionResult ActivateSconce(BlockState state,
            net.minecraft.world.level.Level level, BlockPos pos, net.minecraft.world.entity.player.Player player,
            net.minecraft.world.InteractionHand hand, net.minecraft.world.phys.BlockHitResult hit) {
        // AIR is the native fallback for a missing registry entry, not a lamp to return.
        // Keep the installed identity until its provider returns or the full frame is recovered.
        if (!providerLampAvailable()) return net.minecraft.world.InteractionResult.FAIL;
        return super.ActivateSconce(state, level, pos, player, hand, hit);
    }
    @Override public java.util.List<net.minecraft.world.item.ItemStack> getDrops(BlockState state,
            net.minecraft.world.level.storage.loot.LootParams.Builder builder) {
        return providerLampAvailable() ? super.getDrops(state, builder)
                : java.util.List.of(zone.moddev.mc.ironagefurniture.api.SconceMetalData.storedRockSaltFrame(state));
    }
    @Override protected Block EmptyVariant() { return wall ? ModVanillaLights.light_metal_ironage_sconce_wall_empty_iron.get() : super.EmptyVariant(); }
    @Override public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        Direction support = wall ? state.getValue(DIRECTION) : Direction.UP;
        return Block.canSupportCenter(level, pos.relative(support.getOpposite()), support);
    }
    @Override public BlockState updateShape(BlockState state, Direction direction, BlockState neighbour,
            net.minecraft.world.level.LevelAccessor level, BlockPos pos, BlockPos neighbourPos) {
        return state.canSurvive(level, pos) ? super.updateShape(state, direction, neighbour, level, pos, neighbourPos) : Blocks.AIR.defaultBlockState();
    }
    @Override public VoxelShape getShape(BlockState state, net.minecraft.world.level.BlockGetter level, BlockPos pos, CollisionContext context) {
        return FurnitureBlock.rotate(wall ? Block.box(-.5, 2, 5.5, 6.5, 13, 10.5) : Block.box(5.5, 0, 5.5, 10.5, 12, 10.5), state.getValue(DIRECTION));
    }
}
