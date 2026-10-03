package zone.moddev.mc.ironagefurniture.fixture;

import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameType;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.registries.ForgeRegistries;
import zone.moddev.mc.ironagefurniture.BlockObjectHolder;
import zone.moddev.mc.ironagefurniture.api.MineralogyCompat;
import zone.moddev.mc.ironagefurniture.api.blocks.lightsource.phasefour.Candle;
import zone.moddev.mc.ironagefurniture.api.blocks.lightsource.phasefour.RockSaltSconce;
import zone.moddev.mc.ironagefurniture.init.PhaseFourLighting;

/** Optional registration and the published Mineralogy lamp's round trip. */
final class RockSaltRuntimeProbe {
    private RockSaltRuntimeProbe() { }
    static int run(ServerWorld world, FakePlayer player) {
        if (!MineralogyCompat.isEnabled()) {
            for (boolean wall : new boolean[]{false, true}) require(!ForgeRegistries.BLOCKS.containsKey(
                    new ResourceLocation("ironagefurniture", PhaseFourLighting.rockSaltId(wall))), "Rock-salt content leaked without its dependency");
            return 0;
        }
        Block lamp = MineralogyCompat.getRockSaltLamp();
        require(lamp != null && "mineralogy:rocksaltlamp".equals(lamp.getRegistryName().toString()), "Wrong Mineralogy lamp");
        BlockPos pos = new BlockPos(240, 80, 160);
        int cases = 0;
        for (boolean wall : new boolean[]{false, true}) for (Direction facing : Direction.Plane.HORIZONTAL) for (boolean wet : new boolean[]{false, true}) {
            player.interactionManager.setGameType(GameType.SURVIVAL);
            player.inventory.clear();
            world.setBlockState(pos.down(), Blocks.STONE.getDefaultState(), 2);
            world.setBlockState(pos.offset(facing.getOpposite()), Blocks.STONE.getDefaultState(), 2);
            Block frame = wall ? BlockObjectHolder.light_metal_ironage_sconce_wall_empty_iron : BlockObjectHolder.light_metal_ironage_sconce_floor_empty_iron;
            BlockRayTraceResult hit = new BlockRayTraceResult(new Vec3d(pos), facing, pos, false);
            world.setBlockState(pos, frame.getDefaultState().with(Candle.DIRECTION, facing).with(Candle.WATERLOGGED, wet), 2);
            player.setHeldItem(Hand.MAIN_HAND, new ItemStack(lamp, 2));
            activate(world, player, pos, hit);
            BlockState state = world.getBlockState(pos);
            require(state.getBlock() == PhaseFourLighting.rockSalt(wall) && state.get(Candle.DIRECTION) == facing && state.get(Candle.WATERLOGGED) == wet,
                    "Inserting rock salt lost facing or water");
            require(player.getHeldItem(Hand.MAIN_HAND).getCount() == 1, "Incorrect lamp consumption");
            require(state.getLightValue(world, pos) == 15 && NBTUtil.readBlockState(NBTUtil.writeBlockState(state)) == state, "Lamp light/save state changed");
            RockSaltSconce sconce = (RockSaltSconce) state.getBlock();
            require(sconce.getPickBlock(state, hit, world, pos, player).getItem() == BlockObjectHolder.light_metal_ironage_sconce_floor_empty_iron.asItem(), "Pick returned hidden lamp state");
            if (!wet) {
                require(sconce.receiveFluid(world, pos, state, Fluids.WATER.getStillFluidState(false)), "Rock salt refused water");
                state = world.getBlockState(pos);
                require(state.getBlock() == sconce && state.get(Candle.WATERLOGGED) && state.getLightValue(world, pos) == 15, "Water extinguished rock salt");
            }
            player.setHeldItem(Hand.MAIN_HAND, ItemStack.EMPTY);
            activate(world, player, pos, hit);
            require(world.getBlockState(pos).getBlock() == frame && world.getBlockState(pos).get(Candle.WATERLOGGED)
                    && player.getHeldItem(Hand.MAIN_HAND).getItem() == lamp.asItem(), "Removal lost original lamp or water");
            world.setBlockState(pos, state, 2);
            world.getEntitiesWithinAABB(ItemEntity.class, new AxisAlignedBB(pos).grow(2)).forEach(Entity::remove);
            player.setHeldItem(Hand.MAIN_HAND, new ItemStack(Items.IRON_PICKAXE));
            require(player.interactionManager.tryHarvestBlock(pos), "Could not mine rock-salt sconce");
            List<ItemEntity> drops = world.getEntitiesWithinAABB(ItemEntity.class, new AxisAlignedBB(pos).grow(2), entity -> !entity.removed);
            require(drops.stream().anyMatch(drop -> drop.getItem().getItem() == lamp.asItem() && drop.getItem().getCount() == 1)
                    && drops.stream().anyMatch(drop -> drop.getItem().getItem() == BlockObjectHolder.light_metal_ironage_sconce_floor_empty_iron.asItem()), "Mining did not return lamp and frame");
            require(world.getBlockState(pos).getBlock() == Blocks.WATER, "Mining lost water");
            drops.forEach(Entity::remove);
            player.interactionManager.setGameType(GameType.CREATIVE);
            world.setBlockState(pos, frame.getDefaultState().with(Candle.DIRECTION, facing).with(Candle.WATERLOGGED, wet), 2);
            player.setHeldItem(Hand.MAIN_HAND, new ItemStack(lamp, 2));
            activate(world, player, pos, hit);
            require(player.getHeldItem(Hand.MAIN_HAND).getCount() == 2, "Creative insertion consumed lamp");
            player.setHeldItem(Hand.MAIN_HAND, ItemStack.EMPTY);
            activate(world, player, pos, hit);
            require(player.getHeldItem(Hand.MAIN_HAND).isEmpty(), "Creative removal duplicated lamp");
            world.setBlockState(pos, state, 2);
            require(player.interactionManager.tryHarvestBlock(pos), "Creative mining failed");
            require(world.getEntitiesWithinAABB(ItemEntity.class, new AxisAlignedBB(pos).grow(2), entity -> !entity.removed).isEmpty(), "Creative mining duplicated lamp");
            player.interactionManager.setGameType(GameType.SURVIVAL);

            // Separate cells survive shutdown and are checked on the next load.
            BlockPos saved = new BlockPos(240 + cases * 3, 80, 240);
            BlockState expected = PhaseFourLighting.rockSalt(wall).getDefaultState().with(Candle.DIRECTION, facing).with(Candle.WATERLOGGED, wet);
            if (!world.getBlockState(saved).isAir()) require(world.getBlockState(saved) == expected, "Rock-salt reload changed state");
            world.setBlockState(saved.down(), Blocks.STONE.getDefaultState(), 2);
            if (wall) world.setBlockState(saved.offset(facing.getOpposite()), Blocks.STONE.getDefaultState(), 2);
            world.setBlockState(saved, expected, 2);
            cases++;
        }
        world.removeBlock(pos, false);
        return cases;
    }
    private static void activate(ServerWorld world, FakePlayer player, BlockPos pos, BlockRayTraceResult hit) {
        BlockState state = world.getBlockState(pos);
        require(state.getBlock().onBlockActivated(state, world, pos, player, Hand.MAIN_HAND, hit), "Rock-salt interaction refused");
    }
    private static void require(boolean passed, String message) { if (!passed) throw new IllegalStateException(message); }
}
