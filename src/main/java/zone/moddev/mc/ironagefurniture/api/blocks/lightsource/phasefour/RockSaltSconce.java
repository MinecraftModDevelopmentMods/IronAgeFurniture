package zone.moddev.mc.ironagefurniture.api.blocks.lightsource.phasefour;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import zone.moddev.mc.ironagefurniture.api.blocks.base.FurnitureBlock;
import zone.moddev.mc.ironagefurniture.api.blocks.lightsource.glow.LightSourceSconceGlowFloor;
import zone.moddev.mc.ironagefurniture.init.ModVanillaLights;
import zone.moddev.mc.ironagefurniture.init.RegistrationProperties;

/** Mineralogy provides the lamp; an absent provider never becomes a hard dependency. */
public final class RockSaltSconce extends LightSourceSconceGlowFloor {
    public static final Identifier LAMP = Identifier.parse("mineralogy:rocksaltlamp");
    private final boolean wall;
    public RockSaltSconce(String path, boolean wall) {
        super(RegistrationProperties.block(Properties.of().strength(1, 10).sound(SoundType.METAL).noOcclusion().lightLevel(state -> 15), path));
        this.wall = wall;
    }
    @Override protected Block LightDrop() { return BuiltInRegistries.BLOCK.getValue(LAMP); }
    @Override protected Block EmptyVariant() { return wall ? ModVanillaLights.light_metal_ironage_sconce_wall_empty_iron.get() : super.EmptyVariant(); }
    @Override public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        Direction support = wall ? state.getValue(DIRECTION) : Direction.UP;
        return Block.canSupportCenter(level, pos.relative(support.getOpposite()), support);
    }
    @Override protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos,
            Direction direction, BlockPos neighbourPos, BlockState neighbour, RandomSource random) {
        return state.canSurvive(level, pos) ? super.updateShape(state, level, ticks, pos, direction, neighbourPos, neighbour, random) : Blocks.AIR.defaultBlockState();
    }
    @Override public VoxelShape getShape(BlockState state, net.minecraft.world.level.BlockGetter level, BlockPos pos, CollisionContext context) {
        return FurnitureBlock.rotate(wall ? Block.box(-.5, 2, 5.5, 6.5, 13, 10.5) : Block.box(5.5, 0, 5.5, 10.5, 12, 10.5), state.getValue(DIRECTION));
    }
}
