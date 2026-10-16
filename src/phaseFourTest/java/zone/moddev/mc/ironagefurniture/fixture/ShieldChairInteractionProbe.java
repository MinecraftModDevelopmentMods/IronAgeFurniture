package zone.moddev.mc.ironagefurniture.fixture;

import com.mojang.authlib.GameProfile;
import java.util.UUID;
import java.util.function.BiConsumer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
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
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import zone.moddev.mc.ironagefurniture.api.ShieldChairItemData;
import zone.moddev.mc.ironagefurniture.api.blocks.base.FurnitureBlock;
import zone.moddev.mc.ironagefurniture.api.blocks.furniture.ShieldChair;
import zone.moddev.mc.ironagefurniture.api.tile.ShieldChairBlockEntity;

/** Use the player interaction dispatcher, not just the block's removal method. */
final class ShieldChairInteractionProbe {
    static void run(ServerLevel level, BiConsumer<Boolean, String> check) {
        GameProfile profile = new GameProfile(UUID.fromString("89af1dc4-1940-49cc-b2fa-342118e1d45b"), "IAFShieldProbe");
        ServerPlayer player = new ServerPlayer(level.getServer(), level, profile, ClientInformation.createDefault());
        new ServerGamePacketListenerImpl(level.getServer(), new Connection(PacketFlow.SERVERBOUND), player, CommonListenerCookie.createInitial(profile, false));
        BlockPos pos = new BlockPos(12, 145, -12);
        player.setPos(pos.getX() + .5, pos.getY(), pos.getZ() + 2);
        ItemStack original = ClientScene.shield(7);
        int cases = 0;
        for (Block block : BuiltInRegistries.BLOCK.stream().filter(value -> value instanceof ShieldChair).toList())
                for (Direction facing : Direction.Plane.HORIZONTAL) for (boolean creative : new boolean[]{false, true})
                for (int hands = 0; hands < 3; hands++) {
            player.stopRiding();
            player.gameMode.changeGameModeForPlayer(creative ? GameType.CREATIVE : GameType.SURVIVAL);
            player.getInventory().clearContent();
            level.getEntitiesOfClass(ItemEntity.class, new AABB(pos).inflate(6)).forEach(ItemEntity::discard);
            level.setBlock(pos.below(), Blocks.STONE.defaultBlockState(), Block.UPDATE_CLIENTS);
            level.setBlock(pos, block.defaultBlockState().setValue(FurnitureBlock.DIRECTION, facing), Block.UPDATE_CLIENTS);
            var storage = (ShieldChairBlockEntity)level.getBlockEntity(pos);
            storage.setShield(original);
            var state = level.getBlockState(pos);
            Vec3 centre = Vec3.atLowerCornerOf(pos).add(.5, 18.0 / 16, .5);
            Vec3 direction = new Vec3(facing.getStepX(), 0, facing.getStepZ());
            Vec3 start = centre.subtract(direction.scale(2)), end = centre.add(direction.scale(2));
            if (!Boolean.getBoolean("iaf.probe.skipShieldOutline")) {
                check.accept(state.getShape(level, pos).clip(start, end, pos) != null, "A mounted shield can be selected: " + facing);
                Vec3 plate = centre.add(direction.scale(14.565 / 16 - .5));
                check.accept(state.getShape(level, pos).clip(plate.subtract(direction.scale(.01)), plate.add(direction.scale(.01)), pos) != null,
                        "The mounted outline includes the actual rendered shield plate: " + facing);
                storage.setShield(ItemStack.EMPTY);
                check.accept(state.getShape(level, pos).clip(start, end, pos) == null, "The empty frame has no phantom shield selection: " + facing);
                check.accept(state.getCollisionShape(level, pos).clip(start, end, pos) == null, "The empty frame has no phantom shield collision: " + facing);
                storage.setShield(original);
            }
            InteractionHand hand = hands == 2 ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND;
            if (hands == 1) player.setItemInHand(InteractionHand.OFF_HAND, new ItemStack(Items.TORCH));
            if (hands == 2) for (int slot = 0; slot < player.getInventory().getNonEquipmentItems().size(); slot++)
                player.getInventory().setItem(slot, new ItemStack(Items.STONE, 64));
            player.setItemInHand(hand, ItemStack.EMPTY);
            player.setShiftKeyDown(true);
            var hit = new BlockHitResult(Vec3.atCenterOf(pos), facing, pos, false);
            var result = player.gameMode.useItemOn(player, level, ItemStack.EMPTY, hand, hit);
            check.accept(result.consumesAction() && storage.shield().isEmpty(), "Sneak-click retrieves the shield through the native player dispatcher");
            check.accept(!player.isPassenger(), "Retrieving a shield does not sit the player on the chair");
            check.accept(returned(level, pos, player, original) == 1, "The exact stored shield returns once, including Creative/full inventory");
            ItemStack emptyChair = block.getCloneItemStack(level, pos, state, true, player);
            check.accept(ShieldChairItemData.getShield(emptyChair).isEmpty(), "Picking the empty chair retains explicit empty storage");
            var ops = level.registryAccess().createSerializationContext(net.minecraft.nbt.NbtOps.INSTANCE);
            var savedItem = ItemStack.CODEC.encodeStart(ops, emptyChair).getOrThrow();
            check.accept(ShieldChairItemData.getShield(ItemStack.CODEC.parse(ops, savedItem).getOrThrow()).isEmpty(),
                    "An empty chair remains empty through the native saved-item codec");
            var output = net.minecraft.world.level.storage.TagValueOutput.createWithContext(net.minecraft.util.ProblemReporter.DISCARDING, level.registryAccess());
            storage.saveWithFullMetadata(output);
            var restored = new ShieldChairBlockEntity(pos, state);
            restored.loadWithComponents(net.minecraft.world.level.storage.TagValueInput.create(net.minecraft.util.ProblemReporter.DISCARDING, level.registryAccess(), output.buildResult()));
            check.accept(restored.shield().isEmpty(), "Saving and loading an empty chair never supplies a replacement shield");
            player.getInventory().clearContent();
            player.setItemInHand(InteractionHand.OFF_HAND, ItemStack.EMPTY);
            player.setShiftKeyDown(false);
            ItemStack attach = original.copy(); player.setItemInHand(InteractionHand.MAIN_HAND, attach);
            player.gameMode.useItemOn(player, level, attach, InteractionHand.MAIN_HAND, hit);
            check.accept(ItemStack.matches(storage.shield(), original), "Reattaching keeps every shield component");
            check.accept(attach.getCount() == (creative ? 1 : 0), "Attaching consumes one shield only in Survival");
            check.accept(state.getShape(level, pos).clip(start, end, pos) != null, "Reattaching restores the shield outline immediately");
            level.removeBlock(pos, false);
            cases++;
        }
        level.getEntitiesOfClass(ItemEntity.class, new AABB(pos).inflate(6)).forEach(ItemEntity::discard);
        System.out.println("IAF_SHIELD_INTERACTION_PASS cases=" + cases);
    }
    private static int returned(ServerLevel level, BlockPos pos, ServerPlayer player, ItemStack original) {
        return player.getInventory().getNonEquipmentItems().stream().filter(stack -> ItemStack.isSameItemSameComponents(stack, original)).mapToInt(ItemStack::getCount).sum()
                + level.getEntitiesOfClass(ItemEntity.class, new AABB(pos).inflate(6)).stream().map(ItemEntity::getItem)
                    .filter(stack -> ItemStack.isSameItemSameComponents(stack, original)).mapToInt(ItemStack::getCount).sum();
    }
    private ShieldChairInteractionProbe() { }
}
