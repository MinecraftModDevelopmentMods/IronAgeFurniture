package zone.moddev.mc.ironagefurniture.fixture;

import java.util.List;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.fluid.Fluids;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameType;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraftforge.common.util.FakePlayer;
import zone.moddev.mc.ironagefurniture.api.blocks.lightsource.phasefour.Candle;
import zone.moddev.mc.ironagefurniture.api.recipes.TallowSmeltingRecipe;
import zone.moddev.mc.ironagefurniture.init.PhaseFourLighting;
import zone.moddev.mc.ironagefurniture.init.PhaseFourRecipes;

/** Candle placement and fluid checks on the actual packaged Forge server. */
final class CandleRuntimeProbe {
    private CandleRuntimeProbe() { }
    static int run(ServerWorld world, FakePlayer player) {
        int cases = 0;
        BlockPos pos = new BlockPos(144, 80, 144);
        for (boolean wall : new boolean[]{false, true}) for (Direction facing : Direction.Plane.HORIZONTAL) {
            world.setBlockState(pos.down(), Blocks.STONE.getDefaultState(), 2);
            world.setBlockState(pos.offset(facing.getOpposite()), Blocks.STONE.getDefaultState(), 2);
            for (boolean wet : new boolean[]{false, true}) {
                world.setBlockState(pos, wet ? Blocks.WATER.getDefaultState() : Blocks.AIR.getDefaultState(), 2);
                player.setHeldItem(Hand.MAIN_HAND, new ItemStack(PhaseFourLighting.candle(false, true)));
                BlockRayTraceResult hit = new BlockRayTraceResult(new Vec3d(pos), wall ? facing : Direction.UP, pos, false);
                BlockState placed = PhaseFourLighting.candle(false, true).getStateForPlacement(
                        new BlockItemUseContext(new net.minecraft.item.ItemUseContext(player, Hand.MAIN_HAND, hit)));
                require(placed != null && placed.getBlock() == PhaseFourLighting.candle(wall, !wet), "Wrong candle placement variant");
                require(placed.get(Candle.WATERLOGGED) == wet, "Water was lost on candle placement");
                if (wall) require(placed.get(Candle.DIRECTION) == facing, "Wall candle faces the wrong way");
                else placed = placed.with(Candle.DIRECTION, facing);
                world.setBlockState(pos, placed, 2);
                require(NBTUtil.readBlockState(NBTUtil.writeBlockState(placed)) == placed, "Candle save state changed");
                Candle candle = (Candle) placed.getBlock();
                require(candle.getPickBlock(placed, hit, world, pos, player).getItem() == PhaseFourLighting.candle(false, true).asItem(),
                        "Candle pick block returned a hidden state");
                player.setHeldItem(Hand.MAIN_HAND, new ItemStack(Items.FLINT_AND_STEEL));
                if (wet) {
                    require(!candle.onBlockActivated(placed, world, pos, player, Hand.MAIN_HAND, hit), "Wet candle relit");
                } else {
                    require(candle.receiveFluid(world, pos, placed, Fluids.WATER.getStillFluidState(false)), "Water refused by candle");
                    require(world.getBlockState(pos).getBlock() == PhaseFourLighting.candle(wall, false)
                            && world.getBlockState(pos).get(Candle.WATERLOGGED), "Water failed to extinguish candle");
                }
                BlockState unlit = world.getBlockState(pos);
                require(((Candle) unlit.getBlock()).pickupFluid(world, pos, unlit) == Fluids.WATER, "Candle could not be drained");
                unlit = world.getBlockState(pos);
                require(!unlit.get(Candle.WATERLOGGED), "Drain left waterlogged state");
                require(unlit.getBlock().onBlockActivated(unlit, world, pos, player, Hand.MAIN_HAND, hit), "Dry candle did not relight");
                require(world.getBlockState(pos).getBlock() == PhaseFourLighting.candle(wall, true), "Wrong relit candle");
                List<ItemStack> drops = world.getBlockState(pos).getDrops(new LootContext.Builder(world));
                require(drops.size() == 1 && drops.get(0).getItem() == PhaseFourLighting.candle(false, true).asItem()
                        && drops.get(0).getCount() == 1, "Candle drop changed identity or quantity");
                BlockState wetState = world.getBlockState(pos).with(Candle.WATERLOGGED, true);
                world.setBlockState(pos, wetState, 2);
                player.interactionManager.setGameType(GameType.SURVIVAL);
                require(player.interactionManager.tryHarvestBlock(pos), "Real candle harvesting failed");
                require(world.getBlockState(pos).getBlock() == Blocks.WATER, "Breaking candle lost its water");
                BlockPos support = wall ? pos.offset(facing.getOpposite()) : pos.down();
                world.setBlockState(pos, PhaseFourLighting.candle(wall, true).getDefaultState().with(Candle.DIRECTION, facing), 2);
                world.setBlockState(support, Blocks.AIR.getDefaultState(), 3);
                require(world.getBlockState(pos).isAir(), "Unsupported candle did not break");
                world.setBlockState(support, Blocks.STONE.getDefaultState(), 2);
                cases++;
            }
        }
        String[] meats = {"cooked_porkchop", "cooked_beef", "cooked_mutton", "cooked_rabbit", "cooked_chicken", "rotten_flesh"};
        int[] counts = {3, 2, 2, 1, 1, 1};
        for (int index = 0; index < meats.length; index++) {
            IRecipe<?> loaded = world.getServer().getRecipeManager().getRecipe(new ResourceLocation("ironagefurniture", "tallow_from_" + meats[index])).get();
            require(loaded instanceof TallowSmeltingRecipe, "Tallow recipe is not a furnace recipe");
            TallowSmeltingRecipe recipe = (TallowSmeltingRecipe) loaded;
            require(recipe.getRecipeOutput().getCount() == counts[index], "Tallow smelting lost its output count");
            PacketBuffer network = new PacketBuffer(io.netty.buffer.Unpooled.buffer());
            try {
                PhaseFourRecipes.tallow_smelting.write(network, recipe);
                require(PhaseFourRecipes.tallow_smelting.read(recipe.getId(), network).getRecipeOutput().getCount() == counts[index],
                        "Tallow recipe network sync lost its output count");
            } finally { network.release(); }
        }
        return cases;
    }
    private static void require(boolean result, String message) { if (!result) throw new IllegalStateException(message); }
}
