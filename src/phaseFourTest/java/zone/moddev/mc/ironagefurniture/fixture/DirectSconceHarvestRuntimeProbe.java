package zone.moddev.mc.ironagefurniture.fixture;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameRules;
import net.minecraft.world.GameType;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.ForgeRegistries;
import zone.moddev.mc.ironagefurniture.BlockObjectHolder;
import zone.moddev.mc.ironagefurniture.api.MineralogyCompat;
import zone.moddev.mc.ironagefurniture.api.SconceMetalData;
import zone.moddev.mc.ironagefurniture.api.blocks.base.FurnitureBlock;
import zone.moddev.mc.ironagefurniture.api.blocks.base.LightHolderSconce;
import zone.moddev.mc.ironagefurniture.api.blocks.lightsource.phasefour.AdditionalSconce;
import zone.moddev.mc.ironagefurniture.api.blocks.lightsource.phasefour.RockSaltSconce;
import zone.moddev.mc.ironagefurniture.api.enumerations.SconceMetal;
import zone.moddev.mc.ironagefurniture.init.PhaseFourLighting;

/** Exercises Forge's direct player-breaking path, including hands and unsuitable tools. */
final class DirectSconceHarvestRuntimeProbe {
    private static final BlockPos POS = new BlockPos(352, 80, 352);
    private DirectSconceHarvestRuntimeProbe() { }

    static int run(ServerWorld world, FakePlayer player) {
        List<Block> sconces = new ArrayList<>();
        // Start with the reported case so the old jar fails on the actual twin-torch drop.
        sconces.add(PhaseFourLighting.sconce(false, true, true, 2));
        for (Block block : ForgeRegistries.BLOCKS.getValues())
            if (block instanceof LightHolderSconce && !sconces.contains(block)) sconces.add(block);
        require(sconces.size() == (MineralogyCompat.isEnabled() ? 68 : 66), "Unexpected sconce catalog");
        player.interactionManager.setGameType(GameType.SURVIVAL);
        player.setPosition(POS.getX() + 2, POS.getY(), POS.getZ() + .5);
        world.setBlockState(POS.down(), Blocks.STONE.getDefaultState(), 2);
        for (Direction facing : Direction.Plane.HORIZONTAL)
            world.setBlockState(POS.offset(facing), Blocks.STONE.getDefaultState(), 2);
        ItemStack silk = new ItemStack(Items.IRON_PICKAXE);
        silk.addEnchantment(Enchantments.SILK_TOUCH, 1);
        ItemStack fortune = new ItemStack(Items.IRON_PICKAXE);
        fortune.addEnchantment(Enchantments.FORTUNE, 3);
        ItemStack[] tools = {new ItemStack(Items.WOODEN_PICKAXE), new ItemStack(Items.STONE_PICKAXE),
                new ItemStack(Items.IRON_PICKAXE), new ItemStack(Items.IRON_AXE),
                new ItemStack(Items.DIAMOND_SHOVEL), silk, fortune};
        int cases = 0;
        for (Block block : sconces) {
            for (SconceMetal metal : SconceMetal.values()) for (Direction facing : Direction.Plane.HORIZONTAL)
                for (boolean wet : new boolean[]{false, true}) {
                    verify(world, player, state(block, metal, facing, wet), ItemStack.EMPTY, false, true);
                    cases++;
                }
            for (SconceMetal metal : new SconceMetal[]{SconceMetal.IRON, SconceMetal.GOLD, SconceMetal.ADAMANTINE})
                for (boolean wet : new boolean[]{false, true}) for (ItemStack tool : tools) {
                    verify(world, player, state(block, metal, Direction.NORTH, wet), tool, false, true);
                    cases++;
                }
            for (boolean wet : new boolean[]{false, true}) for (ItemStack tool : new ItemStack[]{ItemStack.EMPTY, silk}) {
                verify(world, player, state(block, SconceMetal.GOLD, Direction.EAST, wet), tool, true, false);
                cases++;
            }
        }
        boolean tileDrops = world.getGameRules().getBoolean(GameRules.DO_TILE_DROPS);
        try {
            world.getGameRules().get(GameRules.DO_TILE_DROPS).set(false, world.getServer());
            for (Block block : sconces) for (ItemStack tool : new ItemStack[]{ItemStack.EMPTY, silk}) {
                verify(world, player, state(block, SconceMetal.GOLD, Direction.WEST, false), tool, false, false);
                cases++;
            }
        } finally { world.getGameRules().get(GameRules.DO_TILE_DROPS).set(tileDrops, world.getServer()); }
        // Keep protection mods in control: a cancelled break must leave everything in place.
        CancelBreak cancelled = new CancelBreak();
        MinecraftForge.EVENT_BUS.register(cancelled);
        try {
            BlockState twin = state(sconces.get(0), SconceMetal.GOLD, Direction.NORTH, false);
            world.setBlockState(POS, twin, 2);
            clearDrops(world);
            player.setHeldItem(Hand.MAIN_HAND, ItemStack.EMPTY);
            require(!player.interactionManager.tryHarvestBlock(POS), "Cancelled break succeeded");
            require(world.getBlockState(POS) == twin && drops(world).isEmpty(), "Cancelled break changed contents");
            cases++;
        } finally { MinecraftForge.EVENT_BUS.unregister(cancelled); }
        world.removeBlock(POS, false);
        clearDrops(world);
        return cases;
    }

    private static BlockState state(Block block, SconceMetal metal, Direction facing, boolean wet) {
        return block.getDefaultState().with(SconceMetalData.METAL, metal)
                .with(FurnitureBlock.DIRECTION, facing).with(FurnitureBlock.WATERLOGGED, wet);
    }

    private static void verify(ServerWorld world, FakePlayer player, BlockState state, ItemStack tool,
            boolean creative, boolean tileDrops) {
        world.removeBlock(POS, false);
        world.setBlockState(POS, state, 2);
        clearDrops(world);
        player.interactionManager.setGameType(creative ? GameType.CREATIVE : GameType.SURVIVAL);
        player.setHeldItem(Hand.MAIN_HAND, tool.copy());
        float expectedSpeed = player.getDigSpeed(state, POS) / state.getBlockHardness(world, POS)
                / (net.minecraftforge.common.ForgeHooks.canHarvestBlock(state, player, world, POS) ? 30 : 100);
        require(Math.abs(state.getPlayerRelativeBlockHardness(player, world, POS) - expectedSpeed) < .00001F,
                "Mining speed changed: " + state);
        require(state.getHarvestTool() == ToolType.PICKAXE
                && state.getHarvestLevel() == SconceMetalData.get(state).harvestLevel(), "Metal/tool policy changed");
        require(player.interactionManager.tryHarvestBlock(POS), "Direct breaking failed: " + state);
        Map<Item, Integer> expected = new LinkedHashMap<>();
        if (!creative && tileDrops) {
            expected.put(SconceMetalData.create(BlockObjectHolder.light_metal_ironage_sconce_floor_empty_iron,
                    SconceMetalData.get(state)).getItem(), 1);
            Item content = content(state.getBlock());
            boolean lava = state.getBlock().getRegistryName().getPath().contains("_lava_");
            if (content != null && (!lava || net.minecraft.enchantment.EnchantmentHelper.getEnchantmentLevel(
                    Enchantments.SILK_TOUCH, tool) > 0)) {
                int count = state.getBlock() instanceof AdditionalSconce ? ((AdditionalSconce)state.getBlock()).contentsCount() : 1;
                expected.put(content, count);
            }
        }
        Map<Item, Integer> actual = new LinkedHashMap<>();
        for (ItemEntity drop : drops(world)) actual.merge(drop.getItem().getItem(), drop.getItem().getCount(), Integer::sum);
        require(actual.equals(expected), "Incorrect direct-mining drops: " + state + ", tool=" + tool
                + ", expected=" + expected + ", actual=" + actual);
        boolean shatters = !creative && state.getBlock().getRegistryName().getPath().contains("_lava_")
                && net.minecraft.enchantment.EnchantmentHelper.getEnchantmentLevel(Enchantments.SILK_TOUCH, tool) == 0;
        require(world.getBlockState(POS).getBlock() == (shatters ? Blocks.FIRE
                : state.get(FurnitureBlock.WATERLOGGED) ? Blocks.WATER : Blocks.AIR), "Wrong remaining block: " + state);
        world.notifyNeighborsOfStateChange(POS, state.getBlock());
        require(drops(world).stream().mapToInt(drop -> drop.getItem().getCount()).sum()
                == actual.values().stream().mapToInt(Integer::intValue).sum(), "Neighbour update duplicated drops");
        clearDrops(world);
    }

    private static Item content(Block block) {
        if (block instanceof AdditionalSconce) return ((AdditionalSconce)block).isTwinTorch()
                ? Items.TORCH : PhaseFourLighting.candle(false, true).asItem();
        if (block instanceof RockSaltSconce) return MineralogyCompat.getRockSaltLamp().asItem();
        String path = block.getRegistryName().getPath();
        if (path.contains("_empty_")) return null;
        if (path.contains("_redtorch_")) return Items.REDSTONE_TORCH;
        if (path.contains("_torch_")) return Items.TORCH;
        if (path.contains("_glow_")) return BlockObjectHolder.light_metal_ironage_block_floor_glow_clear.asItem();
        if (path.contains("_lava_")) return BlockObjectHolder.light_metal_ironage_block_floor_lava_clear.asItem();
        if (path.contains("_red_")) return BlockObjectHolder.light_metal_ironage_block_floor_red_clear.asItem();
        throw new IllegalStateException("Unknown sconce contents: " + path);
    }

    private static List<ItemEntity> drops(ServerWorld world) {
        return world.getEntitiesWithinAABB(ItemEntity.class, new AxisAlignedBB(POS).grow(2), entity -> !entity.removed);
    }
    private static void clearDrops(ServerWorld world) { drops(world).forEach(world::removeEntity); }
    private static void require(boolean passed, String message) { if (!passed) throw new IllegalStateException(message); }
    public static final class CancelBreak {
        @SubscribeEvent public void breaking(BlockEvent.BreakEvent event) {
            if (event.getPos().equals(POS)) event.setCanceled(true);
        }
    }
}
