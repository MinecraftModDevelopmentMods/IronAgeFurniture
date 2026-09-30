package zone.moddev.mc.ironagefurniture.fixture;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import zone.moddev.mc.ironagefurniture.api.Enumerations.PaddedBenchColour;
import zone.moddev.mc.ironagefurniture.api.PaddedBenchColourHelper;

/** Checks real, disposable pre-Phase-4 worlds after Forge has loaded their chunks. */
final class LegacyWorldUpgradeProbe {
    private static final String[] COLOURS = {
            "red", "orange", "magenta", "light_blue", "yellow", "lime", "pink", "gray",
            "light_gray", "cyan", "purple", "blue", "brown", "green", "white", "black"
    };

    static void verify(MinecraftServer server, String source) {
        if ("sylvester-phase2".equals(source)) {
            verifySylvester(server);
            return;
        }
        if ("1.10-phase2".equals(source) || "1.12-phase2".equals(source)) {
            verifyRedOnly(server, source);
            return;
        }
        int startX;
        int benchZ;
        int backZ;
        int chestZ;
        switch (source) {
            case "1.10-phase3":
                startX = 112;
                benchZ = 208;
                backZ = 210;
                chestZ = 214;
                break;
            case "1.12-phase3":
                startX = -112;
                benchZ = 704;
                backZ = 706;
                chestZ = 710;
                break;
            default:
                throw new IllegalStateException("Unknown legacy source world " + source);
        }

        WorldServer world = server.getWorld(0);
        Block bench = registered("chair_wood_ironage_bench_padded_single_oak");
        Block back = registered("chair_wood_ironage_bench_back_padded_single_oak");
        for (int index = 0; index < COLOURS.length; index++) {
            PaddedBenchColour expected = PaddedBenchColour.byName(COLOURS[index]);
            BlockPos benchPos = new BlockPos(startX + index, 62, benchZ);
            BlockPos backPos = new BlockPos(startX + index, 62, backZ);
            require(world.getBlockState(benchPos).getBlock() == bench,
                    "Legacy padded bench missing at " + benchPos);
            require(world.getBlockState(backPos).getBlock() == back,
                    "Legacy padded back bench missing at " + backPos);
            require(PaddedBenchColourHelper.getColour(world, benchPos) == expected,
                    "Legacy bench colour changed at " + benchPos);
            require(PaddedBenchColourHelper.getColour(world, backPos) == expected,
                    "Legacy back-bench colour changed at " + backPos);
        }

        verifyChest(world, new BlockPos(startX, 62, chestZ), bench);
        verifyChest(world, new BlockPos(startX + 2, 62, chestZ), back);
        System.out.println("IRON AGE FURNITURE LEGACY WORLD PROBE PASSED: " + source
                + ", 32 placed coloured benches and 32 stored stacks");
    }

    private static void verifySylvester(MinecraftServer server) {
        WorldServer world = server.getWorld(0);
        String[] naturaRedwoodForms = {
                "classic", "shield", "stool_short", "stool_tall", "bench_single",
                "bench_padded_single", "bench_log_single", "bench_back_single",
                "bench_back_padded_single"
        };
        for (String form : naturaRedwoodForms) {
            registered("chair_wood_ironage_" + form + "_natura_redwood");
        }
        verifyBlock(world, new BlockPos(55, 65, 145),
                "chair_wood_ironage_classic_birch");
        verifyBlock(world, new BlockPos(57, 65, 145),
                "chair_wood_ironage_bench_back_single_birch");
        verifyBlock(world, new BlockPos(358, 63, 102),
                "chair_wood_ironage_bench_log_single_birch");
        BlockPos padded = new BlockPos(218, 65, 123);
        verifyBlock(world, padded, "chair_wood_ironage_bench_back_padded_single_oak");
        require(PaddedBenchColourHelper.getColour(world, padded) == PaddedBenchColour.RED,
                "Sylvester's Phase 2 padded bench is no longer red at " + padded);
        System.out.println("IRON AGE FURNITURE LEGACY WORLD PROBE PASSED: "
                + "Sylvester Phase 2, classic chair, three bench forms and nine Natura redwood IDs");
    }

    private static void verifyBlock(WorldServer world, BlockPos pos, String path) {
        Block actual = world.getBlockState(pos).getBlock();
        require(actual == registered(path), "Sylvester furniture changed at " + pos
                + ": expected " + path + ", found " + actual.getRegistryName());
    }

    private static void verifyRedOnly(MinecraftServer server, String source) {
        WorldServer world = server.getWorld(0);
        BlockPos spawn = world.getSpawnPoint();
        int paddedBlocks = 0;
        int classicBlocks = 0;
        int paddedItems = 0;
        for (int x = spawn.getX() - 24; x <= spawn.getX() + 24; x++) {
            for (int z = spawn.getZ() - 24; z <= spawn.getZ() + 24; z++) {
                for (int y = 60; y <= 100; y++) {
                    BlockPos pos = new BlockPos(x, y, z);
                    ResourceLocation id = world.getBlockState(pos).getBlock().getRegistryName();
                    if (id == null) continue;
                    if ("minecraft".equals(id.getNamespace()) && "chest".equals(id.getPath())
                            && world.getTileEntity(pos) instanceof TileEntityChest) {
                        TileEntityChest chest = (TileEntityChest) world.getTileEntity(pos);
                        for (int slot = 0; slot < chest.getSizeInventory(); slot++) {
                            ItemStack stack = chest.getStackInSlot(slot);
                            if (!stack.isEmpty() && stack.getItem().getRegistryName() != null
                                    && "ironagefurniture".equals(stack.getItem().getRegistryName().getNamespace())
                                    && stack.getItem().getRegistryName().getPath().contains("padded")) {
                                paddedItems++;
                                require(PaddedBenchColourHelper.getColour(stack) == PaddedBenchColour.RED,
                                        "Red-only bench item changed colour at " + pos + " slot " + slot);
                            }
                        }
                    }
                    if (!"ironagefurniture".equals(id.getNamespace())) continue;
                    if (id.getPath().contains("padded")) {
                        paddedBlocks++;
                        require(PaddedBenchColourHelper.getColour(world, pos) == PaddedBenchColour.RED,
                                "Red-only bench changed colour at " + pos);
                    } else if (id.getPath().contains("chair_wood_ironage")) {
                        classicBlocks++;
                    }
                }
            }
        }
        require(paddedBlocks >= 2, "No placed red-only padded benches survived from " + source
                + " near spawn " + spawn);
        require(classicBlocks >= 1, "No ordinary IAF seating survived from " + source);
        System.out.println("IRON AGE FURNITURE LEGACY WORLD PROBE PASSED: " + source
                + ", " + paddedBlocks + " red padded benches, " + classicBlocks
                + " other chairs/benches, " + paddedItems + " stored red padded items");
    }

    private static void verifyChest(WorldServer world, BlockPos pos, Block expectedBlock) {
        if (!(world.getTileEntity(pos) instanceof TileEntityChest)) {
            throw new IllegalStateException("Legacy fixture chest missing at " + pos);
        }
        TileEntityChest chest = (TileEntityChest) world.getTileEntity(pos);
        for (int index = 0; index < COLOURS.length; index++) {
            ItemStack stack = chest.getStackInSlot(index);
            require(!stack.isEmpty() && stack.getItem() == net.minecraft.item.Item.getItemFromBlock(expectedBlock),
                    "Legacy bench item missing at " + pos + " slot " + index);
            require(PaddedBenchColourHelper.getColour(stack) == PaddedBenchColour.byName(COLOURS[index]),
                    "Legacy bench item colour changed at " + pos + " slot " + index);
        }
    }

    private static Block registered(String path) {
        Block block = ForgeRegistries.BLOCKS.getValue(new ResourceLocation("ironagefurniture", path));
        require(block != null, "Missing padded bench registry entry " + path);
        return block;
    }

    private static void require(boolean condition, String message) {
        if (!condition) throw new IllegalStateException(message);
    }

    private LegacyWorldUpgradeProbe() {}
}
