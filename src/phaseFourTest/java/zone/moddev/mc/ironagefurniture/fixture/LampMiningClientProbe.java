package zone.moddev.mc.ironagefurniture.fixture;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameType;
import net.minecraftforge.registries.ForgeRegistries;
import zone.moddev.mc.ironagefurniture.BlockObjectHolder;
import zone.moddev.mc.ironagefurniture.api.blocks.base.FurnitureBlock;

/** Exercises client mining prediction and the hit particles drawn after the final swing. */
final class LampMiningClientProbe {
    private LampMiningClientProbe() { }

    static int run(Minecraft game) {
        BlockPos pos = game.player.getPosition().add(2, 1, 2);
        game.player.clearActivePotions();
        game.player.onGround = true;
        int cases = 0;
        ItemStack silk = new ItemStack(Items.DIAMOND_PICKAXE);
        silk.addEnchantment(Enchantments.SILK_TOUCH, 1);
        Block[] lavaLights = {BlockObjectHolder.light_metal_ironage_block_floor_lava_clear,
                BlockObjectHolder.light_metal_ironage_sconce_floor_lava_iron,
                BlockObjectHolder.light_metal_ironage_sconce_wall_lava_iron};
        // Also exercise Forge's client removal hook directly. Vanilla 1.14's
        // controller can bypass it, but other client harvesting paths can call it.
        for (Block block : lavaLights) for (Direction facing : Direction.Plane.HORIZONTAL)
            for (ItemStack tool : new ItemStack[]{ItemStack.EMPTY, new ItemStack(Items.DIAMOND_PICKAXE), silk})
                for (boolean creative : new boolean[]{false, true}) {
                    game.playerController.setGameType(creative ? GameType.CREATIVE : GameType.SURVIVAL);
                    game.player.setHeldItem(Hand.MAIN_HAND, tool.copy());
                    game.world.setBlockState(pos.down(), Blocks.STONE.getDefaultState(), 2);
                    for (Direction side : Direction.Plane.HORIZONTAL)
                        game.world.setBlockState(pos.offset(side), Blocks.STONE.getDefaultState(), 2);
                    BlockState state = block.getDefaultState().with(FurnitureBlock.DIRECTION, facing);
                    game.world.setBlockState(pos, state, 2);
                    require(block.removedByPlayer(state, game.world, pos, game.player, false, state.getFluidState()),
                            "Client removal hook failed: " + state);
                    game.particles.addBlockHitEffects(pos, hit(pos, Direction.UP));
                    require(game.world.isAirBlock(pos), "Client removal hook predicted fire: " + state);
                    cases++;
                }
        // Test the reported crash first, before checking the shorter glass mining time.
        for (Direction facing : Direction.Plane.HORIZONTAL) {
            int swings = mine(game, pos, BlockObjectHolder.light_metal_ironage_block_floor_lava_clear
                    .getDefaultState().with(FurnitureBlock.DIRECTION, facing), ItemStack.EMPTY, false);
            require(swings <= 15, "A glass lava vial takes too long to break by hand: " + swings);
            cases++;
        }
        for (Block block : ForgeRegistries.BLOCKS.getValues()) {
            String path = block.getRegistryName().getPath();
            if (!"ironagefurniture".equals(block.getRegistryName().getNamespace())
                    || !path.startsWith("light_metal_ironage_block_floor_")
                    || !(path.contains("_lava_") || path.contains("_glow_") || path.contains("_red_"))) continue;
            for (BlockState state : block.getStateContainer().getValidStates()) {
                game.world.setBlockState(pos, state, 2);
                require(!state.getShape(game.world, pos).isEmpty(), "Lamp selection shape is empty: " + state);
                for (Direction hit : Direction.values()) {
                    game.particles.addBlockHitEffects(pos, hit(pos, hit));
                    cases++;
                }
            }
            int swings = mine(game, pos, block.getDefaultState(), ItemStack.EMPTY, false);
            require(swings <= 15, "Glass lamp takes too long to break by hand: " + path + "/" + swings);
            cases++;
        }
        for (Block block : lavaLights) for (Direction facing : Direction.Plane.HORIZONTAL)
            for (ItemStack tool : new ItemStack[]{ItemStack.EMPTY, new ItemStack(Items.DIAMOND_PICKAXE), silk}) {
                mine(game, pos, block.getDefaultState().with(FurnitureBlock.DIRECTION, facing), tool, false);
                mine(game, pos, block.getDefaultState().with(FurnitureBlock.DIRECTION, facing), tool, true);
                cases += 2;
            }
        game.world.removeBlock(pos, false);
        game.world.removeBlock(pos.down(), false);
        for (Direction side : Direction.Plane.HORIZONTAL) game.world.removeBlock(pos.offset(side), false);
        return cases;
    }

    private static int mine(Minecraft game, BlockPos pos, BlockState state, ItemStack tool, boolean creative) {
        game.playerController.resetBlockRemoving();
        game.playerController.setGameType(creative ? GameType.CREATIVE : GameType.SURVIVAL);
        game.player.setHeldItem(Hand.MAIN_HAND, tool.copy());
        game.world.setBlockState(pos.down(), Blocks.STONE.getDefaultState(), 2);
        for (Direction side : Direction.Plane.HORIZONTAL)
            game.world.setBlockState(pos.offset(side), Blocks.STONE.getDefaultState(), 2);
        game.world.setBlockState(pos, state, 2);
        require(game.playerController.clickBlock(pos, Direction.UP), "Cannot start client mining: " + state);
        int swings = 0;
        while (game.world.getBlockState(pos).getBlock() == state.getBlock() && swings++ < 250) {
            if (game.playerController.onPlayerDamageBlock(pos, Direction.UP))
                // Minecraft.continueAttack does this even on the swing that removes the block.
                game.particles.addBlockHitEffects(pos, hit(pos, Direction.UP));
        }
        Block expected = state.get(FurnitureBlock.WATERLOGGED) ? Blocks.WATER : Blocks.AIR;
        require(game.world.getBlockState(pos).getBlock() == expected,
                "Client predicted fire or lost removal: " + state + " -> " + game.world.getBlockState(pos));
        return swings;
    }

    private static BlockRayTraceResult hit(BlockPos pos, Direction face) {
        return new BlockRayTraceResult(new Vec3d(pos).add(.5, .3, .5), face, pos, false);
    }

    private static void require(boolean success, String message) {
        if (!success) throw new IllegalStateException(message);
    }
}
