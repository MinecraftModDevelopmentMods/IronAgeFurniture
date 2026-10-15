package zone.moddev.mc.ironagefurniture.fixture;

import com.mojang.authlib.GameProfile;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.BiConsumer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
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
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import zone.moddev.mc.ironagefurniture.api.CandleInventory;
import zone.moddev.mc.ironagefurniture.api.SconceMetalData;
import zone.moddev.mc.ironagefurniture.api.blocks.base.FurnitureBlock;
import zone.moddev.mc.ironagefurniture.api.blocks.lightsource.phasefour.CandleSconceBlock;
import zone.moddev.mc.ironagefurniture.api.enumerations.SconceMetal;
import zone.moddev.mc.ironagefurniture.api.tile.CandleSconceBlockEntity;
import zone.moddev.mc.ironagefurniture.init.ModVanillaLights;

/** Exercise normal server-player interactions, including the held item's fallback path. */
final class PlayerLightInteractionProbe {
    static void run(ServerLevel level, BiConsumer<Boolean, String> check) {
        if (!zone.moddev.mc.ironagefurniture.IronAgeFurnitureConfiguration.enabled(zone.moddev.mc.ironagefurniture.IronAgeFurnitureConfiguration.LIGHTS)) return;
        GameProfile profile = new GameProfile(UUID.fromString("4574f5c4-d864-4d3a-8655-b872ce5dfdc1"), "IAFLightProbe");
        ServerPlayer player = new ServerPlayer(level.getServer(), level, profile, ClientInformation.createDefault());
        new ServerGamePacketListenerImpl(level.getServer(), new Connection(PacketFlow.SERVERBOUND), player, CommonListenerCookie.createInitial(profile, false));
        BlockPos pos = new BlockPos(12, 140, 12);
        player.setPos(pos.getX() + 2.5, pos.getY(), pos.getZ() + .5);
        List<ItemStack> colours = BuiltInRegistries.ITEM.stream().map(ItemStack::new).filter(CandleInventory::accepts).toList();
        for (SconceMetal metal : SconceMetal.values()) for (boolean wall : new boolean[]{false, true})
                for (Direction facing : Direction.Plane.HORIZONTAL) for (boolean creative : new boolean[]{false, true}) {
            player.gameMode.changeGameModeForPlayer(creative ? GameType.CREATIVE : GameType.SURVIVAL);
            BlockPos support = wall ? pos.relative(facing.getOpposite()) : pos.below();
            for (int colour = 0; colour < colours.size(); colour++) {
                reset(level, pos, support, player);
                level.setBlockAndUpdate(support, Blocks.STONE.defaultBlockState());
                BlockState frame = (wall ? ModVanillaLights.light_metal_ironage_sconce_wall_empty_iron.get()
                        : ModVanillaLights.light_metal_ironage_sconce_floor_empty_iron.get()).defaultBlockState()
                        .setValue(FurnitureBlock.DIRECTION, facing).setValue(SconceMetalData.METAL, metal);
                level.setBlockAndUpdate(pos, frame);
                List<ItemStack> originals = new ArrayList<>();
                for (int slot = 0; slot < 4; slot++) {
                    ItemStack held = colours.get((colour + slot) % colours.size()).copyWithCount(2);
                    held.set(DataComponents.CUSTOM_NAME, Component.literal("Stored candle " + colour + ":" + slot));
                    held.set(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, slot % 2 == 0);
                    CompoundTag extra = new CompoundTag(); extra.putByte("Byte", (byte)7); extra.putLong("Long", 123456789012345L);
                    extra.putString("Text", "Keep the complete item"); held.set(DataComponents.CUSTOM_DATA, CustomData.of(extra));
                    originals.add(held.copyWithCount(1));
                    click(level, player, pos, InteractionHand.MAIN_HAND, held);
                    check.accept(held.getCount() == (creative ? 2 : 1), "Normal candle insertion consumes exactly one outside Creative");
                    check.accept(level.getBlockEntity(pos) instanceof CandleSconceBlockEntity, "Clicking the empty frame creates candle storage");
                    var storage = (CandleSconceBlockEntity)level.getBlockEntity(pos);
                    check.accept(storage.candles().size() == slot + 1 && ItemStack.matches(storage.candles().get(slot), originals.get(slot)), "Click insertion stores the original complete candle");
                    if (slot == 0) {
                        check.accept(!level.getBlockState(pos).getValue(CandleSconceBlock.LIT), "A first inserted candle starts unlit");
                        click(level, player, pos, InteractionHand.MAIN_HAND, new ItemStack(Items.FLINT_AND_STEEL));
                    }
                    check.accept(level.getBlockState(pos).getValue(CandleSconceBlock.LIT), "Further candle insertion preserves a lit holder");
                    check.accept(level.getBlockState(pos).getLightEmission() == 12 + slot, "One through four candles retain IAF brightness");
                }
                ItemStack fifth = colours.getLast().copyWithCount(2);
                click(level, player, pos, InteractionHand.MAIN_HAND, fifth);
                check.accept(fifth.getCount() == 2 && ((CandleSconceBlockEntity)level.getBlockEntity(pos)).candles().size() == 4, "A full holder consumes nothing");
                check.accept(level.getBlockState(pos.above()).isAir(), "A fifth candle is not placed through the holder");
                ItemStack water = new ItemStack(Items.WATER_BUCKET);
                click(level, player, pos, InteractionHand.MAIN_HAND, water);
                check.accept(!level.getBlockState(pos).getValue(CandleSconceBlock.LIT) && !level.getBlockState(pos).getValue(FurnitureBlock.WATERLOGGED)
                        && level.getBlockState(pos.above()).isAir() && water.is(Items.WATER_BUCKET), "A bucket extinguishes without placing water or consuming the bucket");
                check.accept(drops(level, pos).isEmpty(), "Lighting and insertion never release destruction drops");
                // An empty offhand allows retrieval while all 36 main-inventory slots are full.
                for (int slot = 0; slot < player.getInventory().getNonEquipmentItems().size(); slot++) player.getInventory().setItem(slot, new ItemStack(Items.STONE, 64));
                for (int slot = 3; slot >= 0; slot--) {
                    click(level, player, pos, InteractionHand.OFF_HAND, ItemStack.EMPTY);
                    List<ItemEntity> dropped = drops(level, pos);
                    check.accept(dropped.size() == 1 && ItemStack.matches(dropped.getFirst().getItem(), originals.get(slot)),
                            "A full inventory safely drops the newest exact candle: creative=" + creative + " metal=" + metal
                            + " wall=" + wall + " facing=" + facing + " slot=" + slot + " actual=" + dropped.stream().map(ItemEntity::getItem).toList());
                    dropped.forEach(ItemEntity::discard);
                }
                check.accept(level.getBlockState(pos).getBlock() == frame.getBlock() && SconceMetalData.get(level.getBlockState(pos)) == metal,
                        "Removing the last candle restores the correct metal frame");
                player.getInventory().clearContent(); player.setItemInHand(InteractionHand.OFF_HAND, ItemStack.EMPTY);
                for (ItemStack original : originals) click(level, player, pos, InteractionHand.MAIN_HAND, original.copy());
                player.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
                check.accept(player.gameMode.destroyBlock(pos), "A player can mine the candle holder by hand");
                List<ItemEntity> broken = drops(level, pos);
                ItemStack expectedFrame = SconceMetalData.frame(frame);
                check.accept(broken.stream().filter(item -> item.getItem().is(expectedFrame.getItem())).mapToInt(item -> item.getItem().getCount()).sum() == (creative ? 0 : 1), "Creative does not duplicate the metal frame");
                for (ItemStack original : originals) check.accept(broken.stream().filter(item -> ItemStack.isSameItemSameComponents(original, item.getItem()))
                        .mapToInt(item -> item.getItem().getCount()).sum() == 1, "Direct mining releases each exact candle once, including Creative");
                reset(level, pos, support, player);
            }
        }
    }
    private static void click(ServerLevel level, ServerPlayer player, BlockPos pos, InteractionHand hand, ItemStack held) {
        player.setItemInHand(hand, held);
        var result = player.gameMode.useItemOn(player, level, held, hand, new BlockHitResult(Vec3.atCenterOf(pos), Direction.UP, pos, false));
        if (!result.consumesAction()) throw new AssertionError("Sconce interaction fell through to the held item: " + level.getBlockState(pos) + " " + held);
    }
    private static List<ItemEntity> drops(ServerLevel level, BlockPos pos) { return level.getEntitiesOfClass(ItemEntity.class, new AABB(pos).inflate(4)); }
    private static void reset(ServerLevel level, BlockPos pos, BlockPos support, ServerPlayer player) {
        level.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState()); level.setBlockAndUpdate(support, Blocks.AIR.defaultBlockState());
        drops(level, pos).forEach(ItemEntity::discard); player.getInventory().clearContent(); player.setItemInHand(InteractionHand.OFF_HAND, ItemStack.EMPTY);
    }
    private PlayerLightInteractionProbe() { }
}
