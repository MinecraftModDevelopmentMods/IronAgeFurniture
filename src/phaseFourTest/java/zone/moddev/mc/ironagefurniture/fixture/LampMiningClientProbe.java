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
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.GameType;
import net.minecraftforge.registries.ForgeRegistries;
import zone.moddev.mc.ironagefurniture.BlockObjectHolder;
import zone.moddev.mc.ironagefurniture.api.blocks.base.FurnitureBlock;

/** Exercises client mining prediction and the hit particles drawn after the final swing. */
final class LampMiningClientProbe {
    private LampMiningClientProbe() { }

    static int run(Minecraft game) {
        BlockPos pos = game.player.blockPosition().offset(2, 1, 2);
        game.player.removeAllEffects();
        game.player.setOnGround(true);
        int cases = 0;
        // Test the reported crash first, before checking the shorter glass mining time.
        for (Direction facing : Direction.Plane.HORIZONTAL) {
            int swings = mine(game, pos, BlockObjectHolder.light_metal_ironage_block_floor_lava_clear
                    .defaultBlockState().setValue(FurnitureBlock.DIRECTION, facing), ItemStack.EMPTY, false);
            require(swings <= 15, "A glass lava vial takes too long to break by hand: " + swings);
            cases++;
        }
        for (Block block : ForgeRegistries.BLOCKS.getValues()) {
            String path = block.getRegistryName().getPath();
            if (!"ironagefurniture".equals(block.getRegistryName().getNamespace())
                    || !path.startsWith("light_metal_ironage_block_floor_")
                    || !(path.contains("_lava_") || path.contains("_glow_") || path.contains("_red_"))) continue;
            for (BlockState state : block.getStateDefinition().getPossibleStates()) {
                game.level.setBlock(pos, state, 2);
                require(!state.getShape(game.level, pos).isEmpty(), "Lamp selection shape is empty: " + state);
                for (Direction hit : Direction.values()) {
                    game.particleEngine.addBlockHitEffects(pos, hit(pos, hit));
                    cases++;
                }
            }
            int swings = mine(game, pos, block.defaultBlockState(), ItemStack.EMPTY, false);
            require(swings <= 15, "Glass lamp takes too long to break by hand: " + path + "/" + swings);
            cases++;
        }
        ItemStack silk = new ItemStack(Items.DIAMOND_PICKAXE);
        silk.enchant(Enchantments.SILK_TOUCH, 1);
        Block[] lavaLights = {BlockObjectHolder.light_metal_ironage_block_floor_lava_clear,
                BlockObjectHolder.light_metal_ironage_sconce_floor_lava_iron,
                BlockObjectHolder.light_metal_ironage_sconce_wall_lava_iron};
        for (Block block : lavaLights) for (Direction facing : Direction.Plane.HORIZONTAL)
            for (ItemStack tool : new ItemStack[]{ItemStack.EMPTY, new ItemStack(Items.DIAMOND_PICKAXE), silk}) {
                mine(game, pos, block.defaultBlockState().setValue(FurnitureBlock.DIRECTION, facing), tool, false);
                mine(game, pos, block.defaultBlockState().setValue(FurnitureBlock.DIRECTION, facing), tool, true);
                cases += 2;
            }
        game.level.removeBlock(pos, false);
        game.level.removeBlock(pos.below(), false);
        for (Direction side : Direction.Plane.HORIZONTAL) game.level.removeBlock(pos.relative(side), false);
        return cases;
    }

    private static int mine(Minecraft game, BlockPos pos, BlockState state, ItemStack tool, boolean creative) {
        game.gameMode.stopDestroyBlock();
        game.gameMode.setLocalMode(creative ? GameType.CREATIVE : GameType.SURVIVAL);
        game.player.setItemInHand(Hand.MAIN_HAND, tool.copy());
        game.level.setBlock(pos.below(), Blocks.STONE.defaultBlockState(), 2);
        for (Direction side : Direction.Plane.HORIZONTAL)
            game.level.setBlock(pos.relative(side), Blocks.STONE.defaultBlockState(), 2);
        game.level.setBlock(pos, state, 2);
        require(game.gameMode.startDestroyBlock(pos, Direction.UP), "Cannot start client mining: " + state);
        int swings = 0;
        while (game.level.getBlockState(pos).getBlock() == state.getBlock() && swings++ < 250) {
            if (game.gameMode.continueDestroyBlock(pos, Direction.UP))
                // Minecraft.continueAttack does this even on the swing that removes the block.
                game.particleEngine.addBlockHitEffects(pos, hit(pos, Direction.UP));
        }
        Block expected = state.getValue(FurnitureBlock.WATERLOGGED) ? Blocks.WATER : Blocks.AIR;
        require(game.level.getBlockState(pos).getBlock() == expected,
                "Client predicted fire or lost removal: " + state + " -> " + game.level.getBlockState(pos));
        return swings;
    }

    private static BlockRayTraceResult hit(BlockPos pos, Direction face) {
        return new BlockRayTraceResult(Vector3d.atLowerCornerOf(pos).add(.5, .3, .5), face, pos, false);
    }

    private static void require(boolean success, String message) {
        if (!success) throw new IllegalStateException(message);
    }
}
