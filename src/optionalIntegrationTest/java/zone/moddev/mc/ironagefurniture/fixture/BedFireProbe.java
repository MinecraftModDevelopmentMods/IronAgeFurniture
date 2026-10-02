package zone.moddev.mc.ironagefurniture.fixture;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFire;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.FakePlayerFactory;
import org.apache.logging.log4j.LogManager;
import zone.moddev.mc.ironagefurniture.api.Blocks.MultiBlockBed;
import zone.moddev.mc.ironagefurniture.api.Blocks.MultiBlockWoodBed;
import zone.moddev.mc.ironagefurniture.api.Enumerations.UpholsteryColour;
import zone.moddev.mc.ironagefurniture.api.UpholsteryColourHelper;

/** Exercises vanilla fire ticks against beds in a disposable packaged server world. */
public final class BedFireProbe {
    private BedFireProbe() { }

    public static void verify(MinecraftServer server) {
        WorldServer world = (WorldServer)server.getEntityWorld();
        EntityPlayer player = FakePlayerFactory.getMinecraft(world);
        BlockPos base = new BlockPos(128, 130, 480);
        world.getWorldInfo().setRaining(false);
        world.getGameRules().setOrCreateGameRule("doFireTick", "true");
        int registered = 0;
        for (Block block : Block.REGISTRY) {
            if (!isBed(block)) continue;
            for (EnumFacing face : EnumFacing.values()) {
                check(block.getFlammability(world, base, face) == 20
                        && block.getFireSpreadSpeed(world, base, face) == 5
                        && block.isFlammable(world, base, face),
                        "Bed does not use chair fire rates: " + block.getRegistryName() + " / " + face);
            }
            registered++;
        }
        check(registered >= 30, "Bed registrations are missing");
        String[] forms = { "bed_wood_foot_oak", "bed_wood_foot_left_oak",
                "bed_canopy_foot_lower_oak", "bed_canopy_foot_left_lower_oak" };
        int[] expectedParts = { 2, 4, 4, 8 };
        int burns = 0;
        for (int form = 0; form < forms.length; form++) {
            Block bed = Block.REGISTRY.getObject(new ResourceLocation("ironagefurniture", forms[form]));
            check(isBed(bed), "Missing bed " + forms[form]);
            for (EnumFacing facing : EnumFacing.HORIZONTALS) {
                for (int part = 0; part < expectedParts[form]; part++) {
                    // Exercise both vanilla outcomes: consuming a block, and replacing it with fire.
                    for (boolean replaceWithFire : new boolean[] { false, true }) {
                        clear(world, base);
                        for (int x = -3; x <= 3; x++) for (int z = -3; z <= 3; z++)
                            world.setBlockState(base.add(x, -1, z), Blocks.STONE.getDefaultState(), 2);
                        world.setBlockState(base, bed.getDefaultState().withProperty(BlockHorizontal.FACING, facing), 2);
                        bed.onBlockPlacedBy(world, base, world.getBlockState(base), player,
                                UpholsteryColourHelper.createStack(bed, 1, UpholsteryColour.PINK));
                        List<BlockPos> parts = parts(world, base);
                        check(parts.size() == expectedParts[form], "Bed placement is incomplete: " + forms[form]);
                        BlockPos target = parts.get(part);
                        BlockPos fire = null;
                        for (EnumFacing face : EnumFacing.values()) {
                            BlockPos candidate = target.offset(face);
                            if (world.isAirBlock(candidate)) { fire = candidate; break; }
                        }
                        check(fire != null, "No exposed face on bed part");
                        BlockPos neighbour = base.add(4, 0, 0);
                        world.setBlockState(neighbour, Blocks.PLANKS.getDefaultState(), 2);
                        AxisAlignedBB area = new AxisAlignedBB(base.add(-3, -1, -3), base.add(5, 4, 5));
                        int itemsBefore = world.getEntitiesWithinAABB(EntityItem.class, area).size();
                        IBlockState flame = Blocks.FIRE.getDefaultState()
                                .withProperty(BlockFire.AGE, replaceWithFire ? 0 : 15);
                        world.setBlockState(fire, flame, 2);
                        Blocks.FIRE.updateTick(world, fire, flame, new BurnRandom(replaceWithFire));
                        check(parts(world, base).isEmpty(), "Burning left bed fragments: " + forms[form]
                                + " / " + facing + " / part " + part);
                        for (BlockPos position : parts) check(world.getTileEntity(position) == null,
                                "Burning left an upholstery tile entity");
                        check(world.getEntitiesWithinAABB(EntityItem.class, area).size() == itemsBefore,
                                "Burning dropped an intact bed");
                        check(world.getBlockState(neighbour).getBlock() == Blocks.PLANKS,
                                "Bed cleanup removed unrelated neighbouring blocks");
                        burns++;
                    }
                }
            }
        }
        clear(world, base);
        LogManager.getLogger().info("IRON AGE FURNITURE BED FIRE PROBE PASSED: {} registered bed blocks, {} burns, all forms/facings/parts, no orphan tiles or intact-bed drops",
                registered, burns);
    }

    private static boolean isBed(Block block) {
        return block instanceof MultiBlockWoodBed || block instanceof MultiBlockBed;
    }

    private static List<BlockPos> parts(WorldServer world, BlockPos base) {
        List<BlockPos> result = new ArrayList<BlockPos>();
        for (int x = -2; x <= 2; x++) for (int y = 0; y <= 1; y++) for (int z = -2; z <= 2; z++) {
            BlockPos pos = base.add(x, y, z);
            if (isBed(world.getBlockState(pos).getBlock())) result.add(pos);
        }
        return result;
    }

    private static void clear(WorldServer world, BlockPos base) {
        for (int x = -3; x <= 4; x++) for (int y = 0; y <= 4; y++) for (int z = -3; z <= 4; z++)
            world.setBlockToAir(base.add(x, y, z));
    }

    private static void check(boolean condition, String message) {
        if (!condition) throw new IllegalStateException(message);
    }

    private static final class BurnRandom extends Random {
        private static final long serialVersionUID = 1L;
        private final boolean replaceWithFire;
        BurnRandom(boolean replaceWithFire) { this.replaceWithFire = replaceWithFire; }
        @Override public int nextInt(int bound) {
            if (bound == 4) return 1; // Keep the aged source fire alive long enough to consume the bed.
            if (bound >= 250) return 0; // Force the adjacent burn roll, avoiding a time-dependent test.
            if (bound == 25 && !replaceWithFire) return 24;
            return bound >= 100 ? bound - 1 : 0;
        }
        @Override public float nextFloat() { return 1.0F; }
    }
}
