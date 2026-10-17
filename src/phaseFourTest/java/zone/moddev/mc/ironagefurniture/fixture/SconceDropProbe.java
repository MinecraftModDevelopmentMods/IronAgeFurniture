package zone.moddev.mc.ironagefurniture.fixture;

import com.mojang.authlib.GameProfile;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.BiConsumer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
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
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import zone.moddev.mc.ironagefurniture.api.SconceMetalData;
import zone.moddev.mc.ironagefurniture.api.blocks.base.FurnitureBlock;
import zone.moddev.mc.ironagefurniture.api.blocks.base.LightHolderSconce;
import zone.moddev.mc.ironagefurniture.api.blocks.lightsource.phasefour.CandleSconceBlock;
import zone.moddev.mc.ironagefurniture.api.blocks.lightsource.phasefour.RockSaltSconce;
import zone.moddev.mc.ironagefurniture.api.entity.ReleasedLavaLamp;
import zone.moddev.mc.ironagefurniture.api.tile.CandleSconceBlockEntity;
import zone.moddev.mc.ironagefurniture.init.ModVanillaLights;

/** Audit actual player mining and support loss across every registered sconce state. */
final class SconceDropProbe {
    static void run(ServerLevel level, BiConsumer<Boolean, String> check) {
        var profile = new GameProfile(UUID.fromString("4158f138-5ea4-4a04-8c5d-79721157305b"), "IAFSconceDrops");
        var player = new ServerPlayer(level.getServer(), level, profile, ClientInformation.createDefault());
        new ServerGamePacketListenerImpl(level.getServer(), new Connection(PacketFlow.SERVERBOUND), player,
                CommonListenerCookie.createInitial(profile, false));
        BlockPos pos = new BlockPos(40, 140, 40);
        AABB area = new AABB(pos).inflate(4);
        player.setPos(pos.getX() + 2.5, pos.getY(), pos.getZ() + .5);
        int cases = 0, families = 0;
        for (Block block : BuiltInRegistries.BLOCK.stream().filter(value -> value instanceof LightHolderSconce).toList()) {
            if (block instanceof RockSaltSconce && BuiltInRegistries.BLOCK.getValue(RockSaltSconce.LAMP) == Blocks.AIR) continue;
            families++;
            String path = BuiltInRegistries.BLOCK.getKey(block).getPath();
            boolean wall = path.contains("_wall_");
            boolean lava = path.contains("_lava_");
            for (BlockState state : block.getStateDefinition().getPossibleStates()) for (int mode = 0; mode < 4; mode++) {
                boolean silk = mode == 1, creative = mode == 2, supportLoss = mode == 3;
                BlockPos support = wall ? pos.relative(state.getValue(FurnitureBlock.DIRECTION).getOpposite()) : pos.below();
                player.gameMode.changeGameModeForPlayer(creative ? GameType.CREATIVE : GameType.SURVIVAL);
                ItemStack tool = silk ? new ItemStack(Items.DIAMOND_PICKAXE) : ItemStack.EMPTY;
                if (silk) tool.enchant(level.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.SILK_TOUCH), 1);
                player.setItemInHand(InteractionHand.MAIN_HAND, tool);
                level.setBlockAndUpdate(support, Blocks.STONE.defaultBlockState());
                level.setBlockAndUpdate(pos, state);
                List<ItemStack> contents = new ArrayList<>();
                if (block instanceof CandleSconceBlock) {
                    var storage = (CandleSconceBlockEntity)level.getBlockEntity(pos);
                    int count = state.getValue(CandleSconceBlock.COUNT);
                    for (int index = 0; index < count; index++) {
                        ItemStack candle = new ItemStack(BuiltInRegistries.ITEM.getValue(
                                net.minecraft.resources.Identifier.withDefaultNamespace(index % 2 == 0 ? "red_candle" : "blue_candle")));
                        candle.set(net.minecraft.core.component.DataComponents.CUSTOM_NAME, Component.literal("Recovered candle " + index));
                        contents.add(candle.copy()); storage.insert(candle);
                    }
                    level.setBlockAndUpdate(pos, state);
                } else if (!creative) {
                    if (lava) { if (silk && !supportLoss) contents.add(new ItemStack(ModVanillaLights.light_metal_ironage_block_floor_lava_clear.get())); }
                    else if (path.contains("_redtorch_")) contents.add(new ItemStack(Items.REDSTONE_TORCH));
                    else if (path.contains("_soultorch_")) contents.add(new ItemStack(Items.SOUL_TORCH));
                    else if (path.contains("_torch_")) contents.add(new ItemStack(Items.TORCH, path.contains("_twin") ? 2 : 1));
                    else if (path.contains("_glow_")) contents.add(new ItemStack(ModVanillaLights.light_metal_ironage_block_floor_glow_clear.get()));
                    else if (path.contains("_rocksalt_")) contents.add(new ItemStack(BuiltInRegistries.BLOCK.getValue(RockSaltSconce.LAMP)));
                    else if (path.contains("_red_")) contents.add(new ItemStack(ModVanillaLights.light_metal_ironage_block_floor_red_clear.get()));
                    else check.accept(path.contains("_empty_"), "Every sconce family has an explicit expected content: " + path);
                }
                check.accept(ItemStack.matches(state.getCloneItemStack(level, pos, false), SconceMetalData.frame(state)),
                        "Pick-block returns the existing item ID for the frame's own metal");
                check.accept(player.gameMode.destroyBlock(supportLoss ? support : pos), "Player mining succeeds for " + path + " mode=" + mode);
                List<ItemEntity> drops = level.getEntitiesOfClass(ItemEntity.class, area);
                ItemStack frame = SconceMetalData.frame(state);
                check.accept(drops.stream().filter(item -> item.getItem().is(frame.getItem())).mapToInt(item -> item.getItem().getCount()).sum() == (creative ? 0 : 1),
                        "One correct-metal frame, without Creative duplication: " + state + " mode=" + mode);
                for (ItemStack expected : contents) check.accept(drops.stream().filter(item -> ItemStack.isSameItemSameComponents(item.getItem(), expected))
                        .mapToInt(item -> item.getItem().getCount()).sum() == expected.getCount(), "Original light contents survive: " + state + " mode=" + mode);
                int total = contents.stream().mapToInt(ItemStack::getCount).sum() + (creative ? 0 : 1);
                check.accept(drops.stream().mapToInt(item -> item.getItem().getCount()).sum() == total, "No missing or duplicated light drops: " + state + " mode=" + mode);
                for (ItemEntity drop : drops) if (drop.getItem().is(frame.getItem()))
                    check.accept(drop.fireImmune() && !drop.hurtServer(level, level.damageSources().inFire(), 10) && !drop.isRemoved(), "Every dropped metal frame survives fire");
                var released = level.getEntitiesOfClass(ReleasedLavaLamp.class, area);
                check.accept(released.size() == (lava && !creative && (supportLoss || wall && !silk) ? 1 : 0),
                        "Only non-Silk lava releases a falling vial: " + state + " mode=" + mode);
                released.forEach(ReleasedLavaLamp::discard); drops.forEach(ItemEntity::discard);
                level.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
                level.setBlockAndUpdate(support, Blocks.AIR.defaultBlockState());
                cases++;
            }
        }
        System.out.println("IAF_SCONCE_DROPS_PASS families=" + families + " cases=" + cases);
    }
    private SconceDropProbe() { }
}
