package zone.moddev.mc.ironagefurniture.fixture;

import java.util.List;
import java.util.Random;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.GameType;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraftforge.common.util.FakePlayer;
import zone.moddev.mc.ironagefurniture.api.blocks.lightsource.phasefour.Candle;
import zone.moddev.mc.ironagefurniture.api.recipes.TallowSmeltingRecipe;
import zone.moddev.mc.ironagefurniture.init.PhaseFourLighting;
import zone.moddev.mc.ironagefurniture.init.PhaseFourRecipes;

/** Candle placement and fluid checks on the actual packaged Forge server. */
final class CandleRuntimeProbe {
    private CandleRuntimeProbe() { }
    static int run(ServerLevel world, FakePlayer player) {
        int cases = 0;
        BlockPos pos = new BlockPos(144, 80, 144);
        for (boolean wall : new boolean[]{false, true}) for (Direction facing : Direction.Plane.HORIZONTAL) {
            world.setBlock(pos.below(), Blocks.STONE.defaultBlockState(), 2);
            world.setBlock(pos.relative(facing.getOpposite()), Blocks.STONE.defaultBlockState(), 2);
            for (boolean wet : new boolean[]{false, true}) {
                world.setBlock(pos, wet ? Blocks.WATER.defaultBlockState() : Blocks.AIR.defaultBlockState(), 2);
                player.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(PhaseFourLighting.candle(false, true)));
                BlockHitResult hit = new BlockHitResult(Vec3.atLowerCornerOf(pos), wall ? facing : Direction.UP, pos, false);
                BlockState placed = PhaseFourLighting.candle(false, true).getStateForPlacement(
                        new BlockPlaceContext(new net.minecraft.world.item.context.UseOnContext(player, InteractionHand.MAIN_HAND, hit)));
                require(placed != null && placed.getBlock() == PhaseFourLighting.candle(wall, !wet), "Wrong candle placement variant");
                require(placed.getValue(Candle.WATERLOGGED) == wet, "Water was lost on candle placement");
                if (wall) require(placed.getValue(Candle.DIRECTION) == facing, "Wall candle faces the wrong way");
                else placed = placed.setValue(Candle.DIRECTION, facing);
                world.setBlock(pos, placed, 2);
                require(NbtUtils.readBlockState(NbtUtils.writeBlockState(placed)) == placed, "Candle save state changed");
                Candle candle = (Candle) placed.getBlock();
                require(candle.getPickBlock(placed, hit, world, pos, player).getItem() == PhaseFourLighting.candle(false, true).asItem(),
                        "Candle pick block returned a hidden state");
                player.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(Items.FLINT_AND_STEEL));
                if (wet) {
                    require(!candle.use(placed, world, pos, player, InteractionHand.MAIN_HAND, hit).consumesAction(), "Wet candle relit");
                } else {
                    require(candle.placeLiquid(world, pos, placed, Fluids.WATER.getSource(false)), "Water refused by candle");
                    require(world.getBlockState(pos).getBlock() == PhaseFourLighting.candle(wall, false)
                            && world.getBlockState(pos).getValue(Candle.WATERLOGGED), "Water failed to extinguish candle");
                }
                BlockState unlit = world.getBlockState(pos);
                require(((Candle) unlit.getBlock()).pickupBlock(world, pos, unlit).is(net.minecraft.world.item.Items.WATER_BUCKET), "Candle could not be drained");
                unlit = world.getBlockState(pos);
                require(!unlit.getValue(Candle.WATERLOGGED), "Drain left waterlogged state");
                require(unlit.getBlock().use(unlit, world, pos, player, InteractionHand.MAIN_HAND, hit).consumesAction(), "Dry candle did not relight");
                require(world.getBlockState(pos).getBlock() == PhaseFourLighting.candle(wall, true), "Wrong relit candle");
                List<ItemStack> drops = world.getBlockState(pos).getDrops(new LootContext.Builder(world));
                require(drops.size() == 1 && drops.get(0).getItem() == PhaseFourLighting.candle(false, true).asItem()
                        && drops.get(0).getCount() == 1, "Candle drop changed identity or quantity");
                BlockState wetState = world.getBlockState(pos).setValue(Candle.WATERLOGGED, true);
                world.setBlock(pos, wetState, 2);
                player.setGameMode(GameType.SURVIVAL);
                require(player.gameMode.destroyBlock(pos), "Real candle harvesting failed");
                require(world.getBlockState(pos).getBlock() == Blocks.WATER, "Breaking candle lost its water");
                BlockPos support = wall ? pos.relative(facing.getOpposite()) : pos.below();
                world.setBlock(pos, PhaseFourLighting.candle(wall, true).defaultBlockState().setValue(Candle.DIRECTION, facing), 2);
                world.setBlock(support, Blocks.AIR.defaultBlockState(), 3);
                require(world.getBlockState(pos).isAir(), "Unsupported candle did not break");
                world.setBlock(support, Blocks.STONE.defaultBlockState(), 2);
                cases++;
            }
        }
        String[] meats = {"cooked_porkchop", "cooked_beef", "cooked_mutton", "cooked_rabbit", "cooked_chicken", "rotten_flesh"};
        int[] counts = {3, 2, 2, 1, 1, 1};
        for (int index = 0; index < meats.length; index++) {
            Recipe<?> loaded = world.getServer().getRecipeManager().byKey(new ResourceLocation("ironagefurniture", "tallow_from_" + meats[index])).get();
            require(loaded instanceof TallowSmeltingRecipe, "Tallow recipe is not a furnace recipe");
            TallowSmeltingRecipe recipe = (TallowSmeltingRecipe) loaded;
            require(recipe.getResultItem().getCount() == counts[index], "Tallow smelting lost its output count");
            FriendlyByteBuf network = new FriendlyByteBuf(io.netty.buffer.Unpooled.buffer());
            try {
                PhaseFourRecipes.tallow_smelting.toNetwork(network, recipe);
                require(PhaseFourRecipes.tallow_smelting.fromNetwork(recipe.getId(), network).getResultItem().getCount() == counts[index],
                        "Tallow recipe network sync lost its output count");
            } finally { network.release(); }
        }
        return cases;
    }
    private static void require(boolean result, String message) { if (!result) throw new IllegalStateException(message); }
}
