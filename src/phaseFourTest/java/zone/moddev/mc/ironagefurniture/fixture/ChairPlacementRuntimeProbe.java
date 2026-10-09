package zone.moddev.mc.ironagefurniture.fixture;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameType;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.registries.ForgeRegistries;
import zone.moddev.mc.ironagefurniture.api.UpholsteryItemData;
import zone.moddev.mc.ironagefurniture.api.blocks.furniture.Chair;
import zone.moddev.mc.ironagefurniture.api.blocks.furniture.MultiBlockChair;
import zone.moddev.mc.ironagefurniture.api.enumerations.UpholsteryColour;

/** Places the real items from each player heading; setting states directly cannot catch reversed placement. */
final class ChairPlacementRuntimeProbe {
    private ChairPlacementRuntimeProbe() { }

    static int run(ServerWorld world, ServerPlayerEntity player) {
        BlockPos base = new BlockPos(160, 80, 160);
        ItemStack held = player.getHeldItemMainhand().copy();
        Vec3d position = player.getPositionVec();
        float yaw = player.rotationYaw, pitch = player.rotationPitch;
        GameType mode = player.interactionManager.getGameType();
        int cases = 0;
        try {
            player.interactionManager.setGameType(GameType.SURVIVAL);
            for (Block block : ForgeRegistries.BLOCKS.getValues()) {
                if (!(block instanceof MultiBlockChair)) continue;
                MultiBlockChair chair = (MultiBlockChair) block;
                String path = block.getRegistryName().getPath();
                String classicPath = path.replace("_wingback_", "_classic_").replace("_throne_", "_classic_");
                Block classic = ForgeRegistries.BLOCKS.getValue(new ResourceLocation("ironagefurniture", classicPath));
                require(classic instanceof Chair, "Missing reference classic chair: " + classicPath);
                for (UpholsteryColour colour : UpholsteryColour.values())
                    for (Direction heading : Direction.Plane.HORIZONTAL)
                        for (boolean wet : new boolean[] {false, true}) {
                            world.setBlockState(base.down(), Blocks.STONE.getDefaultState(), 2);
                            for (int offset = 0; offset < chair.getChairHeight(); offset++)
                                world.setBlockState(base.up(offset), wet ? Blocks.WATER.getDefaultState() : Blocks.AIR.getDefaultState(), 2);
                            player.setPositionAndRotation(base.getX() + .5 - heading.getXOffset() * 2,
                                    base.getY(), base.getZ() + .5 - heading.getZOffset() * 2,
                                    heading.getHorizontalAngle(), 0);
                            ItemStack stack = UpholsteryItemData.create(block, colour);
                            player.setHeldItem(Hand.MAIN_HAND, stack);
                            BlockRayTraceResult hit = new BlockRayTraceResult(new Vec3d(base.getX() + .5,
                                    base.getY(), base.getZ() + .5), Direction.UP, base.down(), false);
                            ItemUseContext use = new ItemUseContext(player, Hand.MAIN_HAND, hit);
                            BlockItemUseContext placement = new BlockItemUseContext(use);
                            require(placement.getPos().equals(base) && placement.getPlacementHorizontalFacing() == heading,
                                    "Placement context does not use the tested player heading");
                            BlockState reference = classic.getStateForPlacement(placement);
                            BlockState preview = chair.getStateForPlacement(placement);
                            require(reference != null && reference.get(Chair.DIRECTION) == heading,
                                    "Working classic-chair direction changed");
                            require(preview != null && preview.get(Chair.DIRECTION) == reference.get(Chair.DIRECTION),
                                    "Tall chair reverses classic-chair placement: " + path + "/" + colour + "/" + heading);
                            require(((BlockItem) stack.getItem()).onItemUse(use) == ActionResultType.SUCCESS,
                                    "Real item placement failed: " + path);
                            require(stack.isEmpty(), "Survival placement did not consume one chair");
                            for (int offset = 0; offset < chair.getChairHeight(); offset++) {
                                BlockPos pos = base.up(offset);
                                BlockState state = world.getBlockState(pos);
                                require(state.getBlock() == chair && state.get(Chair.DIRECTION) == heading,
                                        "Placed chair part faces away from the classic chair: " + state);
                                require(state.get(MultiBlockChair.PART) == chair.partForOffset(offset)
                                        && state.get(UpholsteryItemData.COLOUR) == colour && state.get(Chair.WATERLOGGED) == wet,
                                        "Placement lost a part, colour or water");
                                require(NBTUtil.readBlockState(NBTUtil.writeBlockState(state)) == state,
                                        "Saved chair direction changed on palette round trip");
                                double height = offset == 0 ? .65 : .5;
                                Vec3d centre = new Vec3d(pos.getX() + .5, pos.getY() + height, pos.getZ() + .5);
                                Vec3d forward = new Vec3d(heading.getXOffset(), 0, heading.getZOffset());
                                BlockRayTraceResult back = state.getCollisionShape(world, pos).rayTrace(
                                        centre.subtract(forward.scale(.5)), centre.add(forward.scale(.5)), pos);
                                require(back != null && back.getHitVec().distanceTo(centre.add(forward.scale(.375))) < .0001,
                                        "Chair back collision is not behind its seat: " + state);
                            }
                            // Removing the base cleans up the complete structure without harvesting drops.
                            world.removeBlock(base, false);
                            for (int offset = 0; offset < chair.getChairHeight(); offset++) {
                                require(!(world.getBlockState(base.up(offset)).getBlock() instanceof MultiBlockChair),
                                        "Placement left an orphaned chair part");
                                world.removeBlock(base.up(offset), false);
                            }
                            cases++;
                        }
            }
            require(cases >= 1536, "Not all vanilla tall chairs, colours and facings were placed");
            org.apache.logging.log4j.LogManager.getLogger().info("IAF CHAIR PLACEMENT PROBE PASSED: {} cases", cases);
            return cases;
        } finally {
            player.setHeldItem(Hand.MAIN_HAND, held);
            player.setPositionAndRotation(position.x, position.y, position.z, yaw, pitch);
            player.interactionManager.setGameType(mode);
        }
    }
    private static void require(boolean condition, String message) {
        if (!condition) throw new IllegalStateException(message);
    }
}
