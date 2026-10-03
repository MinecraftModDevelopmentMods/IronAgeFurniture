package zone.moddev.mc.ironagefurniture.fixture;

import java.util.ArrayList;
import java.util.List;
import io.netty.buffer.Unpooled;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameType;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import zone.moddev.mc.ironagefurniture.BlockObjectHolder;
import zone.moddev.mc.ironagefurniture.api.blocks.base.FurnitureBlock;
import zone.moddev.mc.ironagefurniture.api.blocks.lightsource.lava.LightSourceSconceLavaWall;
import zone.moddev.mc.ironagefurniture.api.entity.ReleasedLavaLamp;
import zone.moddev.mc.ironagefurniture.registers.entities;

/** Checks the real release, saved/network data, collision and landing paths. */
final class LavaTrapRuntimeProbe {
    private final List<ReleasedLavaLamp> spawned = new ArrayList<>();
    private boolean cancel;
    @SubscribeEvent public void joining(EntityJoinWorldEvent event) {
        if (event.getEntity() instanceof ReleasedLavaLamp) {
            spawned.add((ReleasedLavaLamp)event.getEntity());
            if (cancel) event.setCanceled(true);
        }
    }

    static int run(ServerWorld world, FakePlayer player) {
        LavaTrapRuntimeProbe listener = new LavaTrapRuntimeProbe();
        MinecraftForge.EVENT_BUS.register(listener);
        world.addNewPlayer(player);
        int cases = 0;
        try {
            LightSourceSconceLavaWall sconce = (LightSourceSconceLavaWall)BlockObjectHolder.light_metal_ironage_sconce_wall_lava_iron;
            for (Direction facing : Direction.Plane.HORIZONTAL) for (boolean creative : new boolean[]{false, true})
                for (boolean water : new boolean[]{false, true}) {
                    BlockPos source = new BlockPos(480 + cases * 4, 84, 160);
                    BlockPos landing = source.down(4);
                    for (int y = 0; y < 6; y++) world.setBlockState(landing.up(y), Blocks.AIR.getDefaultState(), 2);
                    world.setBlockState(landing.down(), Blocks.STONE.getDefaultState(), 2);
                    if (water) world.setBlockState(landing, Blocks.WATER.getDefaultState(), 2);
                    world.setBlockState(source.offset(facing.getOpposite()), Blocks.STONE.getDefaultState(), 2);
                    player.setPosition(source.getX() + 2, source.getY(), source.getZ() + .5);
                    player.interactionManager.setGameType(creative ? GameType.CREATIVE : GameType.SURVIVAL);
                    BlockState state = sconce.getDefaultState().with(FurnitureBlock.DIRECTION, facing)
                            .with(zone.moddev.mc.ironagefurniture.api.SconceMetalData.METAL,
                                    zone.moddev.mc.ironagefurniture.api.enumerations.SconceMetal.GOLD);
                    world.setBlockState(source, state, 2);
                    int before = listener.spawned.size();
                    sconce.tick(state, world, source, world.rand);
                    require(listener.spawned.size() == before, "Unpowered wall sconce released its lamp");
                    world.setBlockState(source.up(), Blocks.REDSTONE_BLOCK.getDefaultState(), 2);
                    world.setBlockState(source.down(), Blocks.STONE.getDefaultState(), 2);
                    sconce.tick(state, world, source, world.rand);
                    require(listener.spawned.size() == before, "Blocked wall sconce released its lamp");
                    world.removeBlock(source.down(), false);
                    sconce.tick(state, world, source, world.rand);
                    require(listener.spawned.size() == before + 1, "Powered wall sconce did not release exactly one lamp");
                    ReleasedLavaLamp released = listener.spawned.get(before);
                    require(world.getBlockState(source).getBlock() == BlockObjectHolder.light_metal_ironage_sconce_wall_empty_iron
                            && world.getBlockState(source).get(FurnitureBlock.DIRECTION) == facing
                            && zone.moddev.mc.ironagefurniture.api.SconceMetalData.get(world.getBlockState(source))
                                    == zone.moddev.mc.ironagefurniture.api.enumerations.SconceMetal.GOLD,
                            "Release destroyed, rotated or changed the metal of the frame");
                    sconce.tick(state, world, source, world.rand);
                    require(listener.spawned.size() == before + 1, "Stale scheduled tick duplicated the lamp");
                    CompoundNBT saved = new CompoundNBT();
                    released.writeUnlessRemoved(saved);
                    require("ironagefurniture:released_lava_lamp".equals(saved.getString("id"))
                            && saved.getBoolean("PreserveOnLanding") == creative
                            && saved.getInt("SconceSourceY") == source.getY() && saved.getInt("Time") == 1,
                            "Released lamp lost its persistent identity or Creative policy");
                    Entity loaded = net.minecraft.entity.EntityType.loadEntityUnchecked(saved, world).orElse(null);
                    require(loaded instanceof ReleasedLavaLamp, "Released lamp could not reload");
                    require(((ReleasedLavaLamp)loaded).getBlockState().get(FurnitureBlock.DIRECTION) == facing, "Reload lost lamp facing");
                    PacketBuffer packet = new PacketBuffer(Unpooled.buffer());
                    released.writeSpawnData(packet);
                    ReleasedLavaLamp client = new ReleasedLavaLamp(entities.RELEASED_LAVA_LAMP.get(), world);
                    client.readSpawnData(packet);
                    require(client.getBlockState() == released.getBlockState() && client.noClip, "Client spawn lost lamp data");
                    packet.release();
                    world.removeEntity(released);
                    ReleasedLavaLamp falling = (ReleasedLavaLamp)loaded;
                    require(world.addEntity(falling), "Reloaded falling lamp could not enter the world");
                    for (int tick = 0; tick < 100 && !falling.removed; tick++) falling.tick();
                    require(falling.removed && !falling.noClip, "Released lamp failed to clear its frame and land");
                    world.removeEntity(falling);
                    Block expected = creative ? BlockObjectHolder.light_metal_ironage_block_floor_lava_clear
                            : water ? BlockObjectHolder.obsidian_chunk : Blocks.FIRE;
                    require(world.getBlockState(landing).getBlock() == expected, "Wrong trap landing result: " + world.getBlockState(landing));
                    if (water) require(world.getBlockState(landing).get(FurnitureBlock.WATERLOGGED), "Water landing lost water");
                    require(world.getEntitiesWithinAABB(ItemEntity.class, new AxisAlignedBB(source).expand(0, -5, 0).grow(1), entity -> !entity.removed).isEmpty(),
                            "Trap release duplicated frame or lamp items");
                    cases++;
                }
            BlockPos rollback = new BlockPos(480, 84, 176);
            world.setBlockState(rollback.south(), Blocks.STONE.getDefaultState(), 2);
            world.setBlockState(rollback.up(), Blocks.REDSTONE_BLOCK.getDefaultState(), 2);
            world.setBlockState(rollback.down(), Blocks.AIR.getDefaultState(), 2);
            BlockState original = sconce.getDefaultState().with(FurnitureBlock.DIRECTION, Direction.NORTH);
            world.setBlockState(rollback, original, 2);
            listener.cancel = true;
            sconce.tick(original, world, rollback, world.rand);
            require(world.getBlockState(rollback) == original, "Canceled entity spawn lost the original lamp");
            world.removeBlock(rollback, false);
            return cases;
        } finally {
            player.interactionManager.setGameType(GameType.SURVIVAL);
            world.removePlayer(player);
            MinecraftForge.EVENT_BUS.unregister(listener);
        }
    }
    private static void require(boolean passed, String message) { if (!passed) throw new IllegalStateException(message); }
}
