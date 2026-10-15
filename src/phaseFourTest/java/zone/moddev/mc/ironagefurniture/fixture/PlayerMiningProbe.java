package zone.moddev.mc.ironagefurniture.fixture;

import com.mojang.authlib.GameProfile;
import java.util.List;
import java.util.UUID;
import java.util.function.BiConsumer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.server.level.ClientInformation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.CommonListenerCookie;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import zone.moddev.mc.ironagefurniture.api.SconceMetalData;
import zone.moddev.mc.ironagefurniture.api.blocks.base.FurnitureBlock;
import zone.moddev.mc.ironagefurniture.api.entity.ReleasedLavaLamp;
import zone.moddev.mc.ironagefurniture.api.enumerations.SconceMetal;
import zone.moddev.mc.ironagefurniture.init.ModVanillaLights;
import zone.moddev.mc.ironagefurniture.init.PhaseFourLighting;

/** Exercise the normal server-player harvest path, rather than Level.destroyBlock alone. */
final class PlayerMiningProbe {
    static void run(ServerLevel level, BiConsumer<Boolean, String> check) {
        GameProfile profile = new GameProfile(UUID.fromString("1276429e-a235-43df-91ca-2362b60549b5"), "IAFHarvestProbe");
        ServerPlayer player = new ServerPlayer(level.getServer(), level, profile, ClientInformation.createDefault());
        new ServerGamePacketListenerImpl(level.getServer(), new Connection(PacketFlow.SERVERBOUND), player,
                CommonListenerCookie.createInitial(profile, false));
        player.gameMode.changeGameModeForPlayer(GameType.SURVIVAL);
        BlockPos pos = new BlockPos(8, 120, 8);
        for (SconceMetal metal : SconceMetal.values()) for (boolean wall : new boolean[]{false, true})
                for (Direction facing : Direction.Plane.HORIZONTAL) for (int toolIndex = 0; toolIndex < 4; toolIndex++) {
            BlockPos support = wall ? pos.relative(facing.getOpposite()) : pos.below();
            Block block = wall ? ModVanillaLights.light_metal_ironage_sconce_wall_lava_iron.get()
                    : ModVanillaLights.light_metal_ironage_sconce_floor_lava_iron.get();
            BlockState state = block.defaultBlockState().setValue(FurnitureBlock.DIRECTION, facing)
                    .setValue(FurnitureBlock.WATERLOGGED, false).setValue(SconceMetalData.METAL, metal);
            level.setBlockAndUpdate(pos.below(3), Blocks.STONE.defaultBlockState());
            level.setBlockAndUpdate(support, Blocks.STONE.defaultBlockState());
            level.setBlockAndUpdate(pos, state);
            ItemStack tool = switch (toolIndex) { case 0 -> ItemStack.EMPTY; case 1 -> new ItemStack(Items.WOODEN_PICKAXE);
                default -> new ItemStack(Items.DIAMOND_PICKAXE); };
            boolean silk = toolIndex == 3;
            if (silk) tool.enchant(level.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.SILK_TOUCH), 1);
            player.setItemInHand(InteractionHand.MAIN_HAND, tool);
            check.accept(!state.getShape(level, pos).isEmpty() && state.getShape(level, pos).bounds().getYsize() > 0,
                    "Lava-sconce hand-hit particles have nonempty bounds");
            check.accept(state.getDestroyProgress(player, level, pos) > 0, "Lava sconces are mineable with hands and tools");
            float ironProgress = state.setValue(SconceMetalData.METAL, SconceMetal.IRON).getDestroyProgress(player, level, pos);
            check.accept(Math.abs(state.getDestroyProgress(player, level, pos) - ironProgress / metal.hardness(1)) < .00001F,
                    "Mining speed follows the saved metal's relative hardness");
            check.accept(player.gameMode.destroyBlock(pos), "Normal player mining removes the lava sconce");
            List<ItemEntity> drops = level.getEntitiesOfClass(ItemEntity.class, new AABB(pos).inflate(5));
            ItemStack frame = SconceMetalData.frame(state);
            check.accept(drops.stream().filter(item -> item.getItem().is(frame.getItem())).mapToInt(item -> item.getItem().getCount()).sum() == 1,
                    "Normal player mining returns the correct metal frame");
            int lamps = drops.stream().filter(item -> item.getItem().is(ModVanillaLights.light_metal_ironage_block_floor_lava_clear.get().asItem()))
                    .mapToInt(item -> item.getItem().getCount()).sum();
            check.accept(lamps == (silk ? 1 : 0), "Only Silk Touch returns an intact lamp");
            List<ReleasedLavaLamp> falling = level.getEntitiesOfClass(ReleasedLavaLamp.class, new AABB(pos).inflate(5));
            check.accept(falling.size() == (wall && !silk ? 1 : 0), "Non-Silk wall mining releases one falling lamp");
            for (ReleasedLavaLamp lamp : falling) {
                for (int tick = 0; tick < 80 && !lamp.isRemoved(); tick++) lamp.tick();
                check.accept(lamp.isRemoved(), "Released wall lamp reaches its landing and shatters");
                check.accept(level.getBlockState(pos.below(2)).is(Blocks.FIRE), "A falling wall lamp ignites the landing surface");
            }
            check.accept(wall || silk || level.getBlockState(pos).is(Blocks.FIRE), "A floor lamp shatters into flame without Silk Touch");
            drops.forEach(ItemEntity::discard);
            level.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
            level.setBlockAndUpdate(pos.below(2), Blocks.AIR.defaultBlockState());
            level.setBlockAndUpdate(support, Blocks.AIR.defaultBlockState());
            level.setBlockAndUpdate(pos.below(3), Blocks.AIR.defaultBlockState());
        }
        for (SconceMetal metal : SconceMetal.values()) for (boolean wall : new boolean[]{false, true})
                for (Direction facing : Direction.Plane.HORIZONTAL) for (boolean wet : new boolean[]{false, true})
                for (boolean lit : new boolean[]{false, true}) for (boolean twin : new boolean[]{false, true})
                for (boolean creative : new boolean[]{false, true}) {
            player.gameMode.changeGameModeForPlayer(creative ? GameType.CREATIVE : GameType.SURVIVAL);
            player.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
            BlockPos support = wall ? pos.relative(facing.getOpposite()) : pos.below();
            level.setBlockAndUpdate(support, Blocks.STONE.defaultBlockState());
            Block block = twin ? PhaseFourLighting.twin(wall, lit) : BuiltInRegistries.BLOCK.getValue(net.minecraft.resources.Identifier.parse(
                    "ironagefurniture:light_metal_ironage_sconce_" + (wall ? "wall" : "floor") + "_torch_iron" + (lit ? "" : "_unlit")));
            BlockState state = block.defaultBlockState().setValue(FurnitureBlock.DIRECTION, facing)
                    .setValue(FurnitureBlock.WATERLOGGED, wet).setValue(SconceMetalData.METAL, metal);
            level.setBlockAndUpdate(pos, state);
            check.accept(!state.getShape(level, pos).isEmpty(), "Single/twin torch hit particles have bounds in every wet and lit state");
            check.accept(player.gameMode.destroyBlock(pos), "Normal hand mining removes single/twin torch holders");
            List<ItemEntity> drops = level.getEntitiesOfClass(ItemEntity.class, new AABB(pos).inflate(4));
            ItemStack frame = SconceMetalData.frame(state);
            check.accept(drops.stream().filter(item -> item.getItem().is(frame.getItem())).mapToInt(item -> item.getItem().getCount()).sum() == (creative ? 0 : 1),
                    "Hand mining returns one correct-metal torch frame, without Creative duplication");
            check.accept(drops.stream().filter(item -> item.getItem().is(Items.TORCH)).mapToInt(item -> item.getItem().getCount()).sum() == (creative ? 0 : twin ? 2 : 1),
                    "Hand mining returns all single/twin torches in every wet and lit state");
            drops.forEach(ItemEntity::discard);
            level.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
            level.setBlockAndUpdate(support, Blocks.AIR.defaultBlockState());
        }
        player.gameMode.changeGameModeForPlayer(GameType.SURVIVAL);
        for (boolean silk : new boolean[]{false, true}) {
            BlockState state = ModVanillaLights.light_metal_ironage_block_floor_lava_clear.get().defaultBlockState();
            level.setBlockAndUpdate(pos.below(), Blocks.STONE.defaultBlockState()); level.setBlockAndUpdate(pos, state);
            ItemStack tool = silk ? new ItemStack(Items.DIAMOND_PICKAXE) : ItemStack.EMPTY;
            if (silk) tool.enchant(level.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.SILK_TOUCH), 1);
            player.setItemInHand(InteractionHand.MAIN_HAND, tool);
            check.accept(!state.getShape(level, pos).isEmpty() && state.getShape(level, pos).bounds().getYsize() > 0,
                    "Standalone lamp hand-hit particles have nonempty bounds");
            check.accept(state.getDestroyProgress(player, level, pos) >= .01F, "Standalone lamps take no more than five seconds to mine by hand");
            check.accept(player.gameMode.destroyBlock(pos), "A real player can mine a standalone lava lamp");
            var drops = level.getEntitiesOfClass(ItemEntity.class, new AABB(pos).inflate(4));
            check.accept(drops.stream().filter(item -> item.getItem().is(state.getBlock().asItem())).mapToInt(item -> item.getItem().getCount()).sum() == (silk ? 1 : 0),
                    "Only Silk Touch recovers a standalone lamp intact");
            check.accept(silk || level.getBlockState(pos).is(Blocks.FIRE), "Ordinary standalone lamp mining shatters it into flame");
            drops.forEach(ItemEntity::discard); level.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState()); level.setBlockAndUpdate(pos.below(), Blocks.AIR.defaultBlockState());
        }
    }
    private PlayerMiningProbe() { }
}
