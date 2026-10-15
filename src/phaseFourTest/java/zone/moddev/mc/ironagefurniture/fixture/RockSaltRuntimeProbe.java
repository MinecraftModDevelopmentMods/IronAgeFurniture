package zone.moddev.mc.ironagefurniture.fixture;

import java.util.List;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.AABB;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.GameType;
import net.minecraft.server.level.ServerLevel;
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
    static int run(ServerLevel world, FakePlayer player) {
        if (!MineralogyCompat.isEnabled()) {
            if (MineralogyCompat.getRockSaltLamp() == null) {
                for (boolean wall : new boolean[]{false, true}) require(!ForgeRegistries.BLOCKS.containsKey(
                        new ResourceLocation("ironagefurniture", PhaseFourLighting.rockSaltId(wall))), "Rock-salt content leaked without its dependency");
            } else {
                // Registries are already frozen when the world's config is read.
                // Keep existing blocks recoverable, but reject new insertion.
                ItemStack lamp = new ItemStack(MineralogyCompat.getRockSaltLamp(), 2);
                player.setItemInHand(InteractionHand.MAIN_HAND, lamp);
                BlockPos pos = new BlockPos(240, 80, 160);
                BlockState frame = BlockObjectHolder.light_metal_ironage_sconce_floor_empty_iron.defaultBlockState();
                world.setBlock(pos.below(), Blocks.STONE.defaultBlockState(), 2);
                world.setBlock(pos, frame, 2);
                require(!PhaseFourLighting.insertRockSalt(frame, world, pos, player, InteractionHand.MAIN_HAND, false)
                        && lamp.getCount() == 2 && world.getBlockState(pos) == frame, "Disabled rock-salt insertion changed the frame or consumed a lamp");
            }
            return 0;
        }
        Block lamp = MineralogyCompat.getRockSaltLamp();
        require(lamp != null && "mineralogy:rocksaltlamp".equals(lamp.getRegistryName().toString()), "Wrong Mineralogy lamp");
        BlockPos pos = new BlockPos(240, 80, 160);
        int cases = 0;
        for (boolean wall : new boolean[]{false, true}) for (Direction facing : Direction.Plane.HORIZONTAL) for (boolean wet : new boolean[]{false, true}) {
            player.setGameMode(GameType.SURVIVAL);
            player.getInventory().clearContent();
            world.setBlock(pos.below(), Blocks.STONE.defaultBlockState(), 2);
            world.setBlock(pos.relative(facing.getOpposite()), Blocks.STONE.defaultBlockState(), 2);
            Block frame = wall ? BlockObjectHolder.light_metal_ironage_sconce_wall_empty_iron : BlockObjectHolder.light_metal_ironage_sconce_floor_empty_iron;
            BlockHitResult hit = new BlockHitResult(Vec3.atLowerCornerOf(pos), facing, pos, false);
            world.setBlock(pos, frame.defaultBlockState().setValue(Candle.DIRECTION, facing).setValue(Candle.WATERLOGGED, wet), 2);
            player.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(lamp, 2));
            interact(world, player, pos, hit);
            BlockState state = world.getBlockState(pos);
            require(state.getBlock() == PhaseFourLighting.rockSalt(wall) && state.getValue(Candle.DIRECTION) == facing && state.getValue(Candle.WATERLOGGED) == wet,
                    "Inserting rock salt lost facing or water");
            require(player.getItemInHand(InteractionHand.MAIN_HAND).getCount() == 1, "Incorrect lamp consumption");
            require(state.getLightEmission() == 15 && NbtUtils.readBlockState(NbtUtils.writeBlockState(state)) == state, "Lamp light/save state changed");
            RockSaltSconce sconce = (RockSaltSconce) state.getBlock();
            require(sconce.getPickBlock(state, hit, world, pos, player).getItem() == BlockObjectHolder.light_metal_ironage_sconce_floor_empty_iron.asItem(), "Pick returned hidden lamp state");
            if (!wet) {
                require(sconce.placeLiquid(world, pos, state, Fluids.WATER.getSource(false)), "Rock salt refused water");
                state = world.getBlockState(pos);
                require(state.getBlock() == sconce && state.getValue(Candle.WATERLOGGED) && state.getLightEmission() == 15, "Water extinguished rock salt");
            }
            player.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
            interact(world, player, pos, hit);
            require(world.getBlockState(pos).getBlock() == frame && world.getBlockState(pos).getValue(Candle.WATERLOGGED)
                    && player.getItemInHand(InteractionHand.MAIN_HAND).getItem() == lamp.asItem(), "Removal lost original lamp or water");
            world.setBlock(pos, state, 2);
            world.getEntitiesOfClass(ItemEntity.class, new AABB(pos).inflate(2)).forEach(entity -> entity.discard());
            player.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(Items.IRON_PICKAXE));
            require(player.gameMode.destroyBlock(pos), "Could not mine rock-salt sconce");
            List<ItemEntity> drops = world.getEntitiesOfClass(ItemEntity.class, new AABB(pos).inflate(2), entity -> !entity.isRemoved());
            require(drops.stream().anyMatch(drop -> drop.getItem().getItem() == lamp.asItem() && drop.getItem().getCount() == 1)
                    && drops.stream().anyMatch(drop -> drop.getItem().getItem() == BlockObjectHolder.light_metal_ironage_sconce_floor_empty_iron.asItem()), "Mining did not return lamp and frame");
            require(world.getBlockState(pos).getBlock() == Blocks.WATER, "Mining lost water");
            drops.forEach(entity -> entity.discard());
            player.setGameMode(GameType.CREATIVE);
            world.setBlock(pos, frame.defaultBlockState().setValue(Candle.DIRECTION, facing).setValue(Candle.WATERLOGGED, wet), 2);
            player.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(lamp, 2));
            interact(world, player, pos, hit);
            require(player.getItemInHand(InteractionHand.MAIN_HAND).getCount() == 2, "Creative insertion consumed lamp");
            player.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
            interact(world, player, pos, hit);
            require(player.getItemInHand(InteractionHand.MAIN_HAND).isEmpty(), "Creative removal duplicated lamp");
            world.setBlock(pos, state, 2);
            require(player.gameMode.destroyBlock(pos), "Creative mining failed");
            require(world.getEntitiesOfClass(ItemEntity.class, new AABB(pos).inflate(2), entity -> !entity.isRemoved()).isEmpty(), "Creative mining duplicated lamp");
            player.setGameMode(GameType.SURVIVAL);

            // Separate cells survive shutdown and are checked on the next load.
            BlockPos saved = new BlockPos(240 + cases * 3, 80, 240);
            BlockState expected = PhaseFourLighting.rockSalt(wall).defaultBlockState().setValue(Candle.DIRECTION, facing).setValue(Candle.WATERLOGGED, wet);
            if (Boolean.getBoolean("iaf.probe.metalReload")) require(world.getBlockState(saved) == expected, "Rock-salt reload changed state");
            world.setBlock(saved.below(), Blocks.STONE.defaultBlockState(), 2);
            if (wall) world.setBlock(saved.relative(facing.getOpposite()), Blocks.STONE.defaultBlockState(), 2);
            world.setBlock(saved, expected, 2);
            cases++;
        }
        world.removeBlock(pos, false);
        return cases;
    }
    private static void interact(ServerLevel world, FakePlayer player, BlockPos pos, BlockHitResult hit) {
        BlockState state = world.getBlockState(pos);
        require(state.getBlock().use(state, world, pos, player, InteractionHand.MAIN_HAND, hit).consumesAction(), "Rock-salt interaction refused");
    }
    private static void require(boolean passed, String message) { if (!passed) throw new IllegalStateException(message); }
}
