package zone.moddev.mc.ironagefurniture.fixture;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.InteractionResult;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.GameType;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.registries.ForgeRegistries;
import zone.moddev.mc.ironagefurniture.api.UpholsteryItemData;
import zone.moddev.mc.ironagefurniture.api.blocks.furniture.Chair;
import zone.moddev.mc.ironagefurniture.api.blocks.furniture.MultiBlockChair;
import zone.moddev.mc.ironagefurniture.api.enumerations.UpholsteryColour;

/** Places the real items from each player heading; setting states directly cannot catch reversed placement. */
final class ChairPlacementRuntimeProbe {
    private ChairPlacementRuntimeProbe() { }

    static int run(ServerLevel world, ServerPlayer player) {
        BlockPos base = new BlockPos(160, 80, 160);
        ItemStack held = player.getMainHandItem().copy();
        Vec3 position = player.position();
        float yaw = player.getYRot(), pitch = player.getXRot();
        GameType mode = player.gameMode.getGameModeForPlayer();
        int cases = 0;
        try {
            player.setGameMode(GameType.SURVIVAL);
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
                            world.setBlock(base.below(), Blocks.STONE.defaultBlockState(), 2);
                            for (int offset = 0; offset < chair.getChairHeight(); offset++)
                                world.setBlock(base.above(offset), wet ? Blocks.WATER.defaultBlockState() : Blocks.AIR.defaultBlockState(), 2);
                            player.absMoveTo(base.getX() + .5 - heading.getStepX() * 2,
                                    base.getY(), base.getZ() + .5 - heading.getStepZ() * 2,
                                    heading.toYRot(), 0);
                            ItemStack stack = UpholsteryItemData.create(block, colour);
                            player.setItemInHand(InteractionHand.MAIN_HAND, stack);
                            BlockHitResult hit = new BlockHitResult(new Vec3(base.getX() + .5,
                                    base.getY(), base.getZ() + .5), Direction.UP, base.below(), false);
                            UseOnContext use = new UseOnContext(player, InteractionHand.MAIN_HAND, hit);
                            BlockPlaceContext placement = new BlockPlaceContext(use);
                            require(placement.getClickedPos().equals(base) && placement.getHorizontalDirection() == heading,
                                    "Placement context does not use the tested player heading");
                            BlockState reference = classic.getStateForPlacement(placement);
                            BlockState preview = chair.getStateForPlacement(placement);
                            require(reference != null && reference.getValue(Chair.DIRECTION) == heading,
                                    "Working classic-chair direction changed");
                            require(preview != null && preview.getValue(Chair.DIRECTION) == reference.getValue(Chair.DIRECTION),
                                    "Tall chair reverses classic-chair placement: " + path + "/" + colour + "/" + heading);
                            require(((BlockItem) stack.getItem()).useOn(use).consumesAction(),
                                    "Real item placement failed: " + path);
                            require(stack.isEmpty(), "Survival placement did not consume one chair");
                            for (int offset = 0; offset < chair.getChairHeight(); offset++) {
                                BlockPos pos = base.above(offset);
                                BlockState state = world.getBlockState(pos);
                                require(state.getBlock() == chair && state.getValue(Chair.DIRECTION) == heading,
                                        "Placed chair part faces away from the classic chair: " + state);
                                require(state.getValue(MultiBlockChair.PART) == chair.partForOffset(offset)
                                        && state.getValue(UpholsteryItemData.COLOUR) == colour && state.getValue(Chair.WATERLOGGED) == wet,
                                        "Placement lost a part, colour or water");
                                require(NbtUtils.readBlockState(NbtUtils.writeBlockState(state)) == state,
                                        "Saved chair direction changed on palette round trip");
                                double height = offset == 0 ? .65 : .5;
                                Vec3 centre = new Vec3(pos.getX() + .5, pos.getY() + height, pos.getZ() + .5);
                                Vec3 forward = new Vec3(heading.getStepX(), 0, heading.getStepZ());
                                BlockHitResult back = state.getCollisionShape(world, pos).clip(
                                        centre.subtract(forward.scale(.5)), centre.add(forward.scale(.5)), pos);
                                require(back != null && back.getLocation().distanceTo(centre.add(forward.scale(.375))) < .0001,
                                        "Chair back collision is not behind its seat: " + state);
                            }
                            // Removing the base cleans up the complete structure without harvesting drops.
                            world.removeBlock(base, false);
                            for (int offset = 0; offset < chair.getChairHeight(); offset++) {
                                require(!(world.getBlockState(base.above(offset)).getBlock() instanceof MultiBlockChair),
                                        "Placement left an orphaned chair part");
                                world.removeBlock(base.above(offset), false);
                            }
                            cases++;
                        }
            }
            require(cases >= 1536, "Not all vanilla tall chairs, colours and facings were placed");
            org.apache.logging.log4j.LogManager.getLogger().info("IAF CHAIR PLACEMENT PROBE PASSED: {} cases", cases);
            return cases;
        } finally {
            player.setItemInHand(InteractionHand.MAIN_HAND, held);
            player.absMoveTo(position.x, position.y, position.z, yaw, pitch);
            player.setGameMode(mode);
        }
    }
    private static void require(boolean condition, String message) {
        if (!condition) throw new IllegalStateException(message);
    }
}
