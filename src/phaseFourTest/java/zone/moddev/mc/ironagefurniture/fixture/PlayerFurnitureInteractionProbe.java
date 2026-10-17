package zone.moddev.mc.ironagefurniture.fixture;

import com.mojang.authlib.GameProfile;
import java.util.UUID;
import java.util.function.BiConsumer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.server.level.ClientInformation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.CommonListenerCookie;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.TagValueInput;
import net.minecraft.world.level.storage.TagValueOutput;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import zone.moddev.mc.ironagefurniture.api.UpholsteryItemData;
import zone.moddev.mc.ironagefurniture.api.blocks.furniture.FurnitureBed;
import zone.moddev.mc.ironagefurniture.api.blocks.furniture.ShieldChair;
import zone.moddev.mc.ironagefurniture.api.entity.Entities;
import zone.moddev.mc.ironagefurniture.api.entity.FallingShieldChair;
import zone.moddev.mc.ironagefurniture.api.enumerations.UpholsteryColour;
import zone.moddev.mc.ironagefurniture.api.tile.ShieldChairBlockEntity;

/** Check the vanilla player sleeping path and shield ownership during a fall. */
final class PlayerFurnitureInteractionProbe {
    static void run(ServerLevel level, BiConsumer<Boolean, String> check) {
        var server = level.getServer();
        GameProfile profile = new GameProfile(UUID.fromString("264223f7-78ef-409e-a52e-aa9554a6ee45"), "IAFFurnitureProbe");
        ServerPlayer player = new ServerPlayer(server, level, profile, ClientInformation.createDefault());
        new ServerGamePacketListenerImpl(server, new Connection(PacketFlow.SERVERBOUND), player, CommonListenerCookie.createInitial(profile, false));
        player.gameMode.changeGameModeForPlayer(GameType.SURVIVAL);
        BlockPos base = new BlockPos(-12, 150, 12);
        for (int x = -4; x <= 4; x++) for (int z = -4; z <= 4; z++)
            level.setBlock(base.offset(x, -1, z), Blocks.STONE.defaultBlockState(), Block.UPDATE_CLIENTS);
        int colour = 0;
        for (Block block : BuiltInRegistries.BLOCK.stream().filter(value -> value instanceof FurnitureBed bed && bed.itemBlock() == value).toList())
                for (Direction facing : Direction.Plane.HORIZONTAL) {
            FurnitureBed bed = (FurnitureBed)block;
            BlockState state = block.defaultBlockState().setValue(FurnitureBed.DIRECTION, facing)
                    .setValue(UpholsteryItemData.COLOUR, UpholsteryColour.values()[colour++ % 16]);
            level.setBlock(base, state, Block.UPDATE_CLIENTS);
            block.setPlacedBy(level, base, state, null, UpholsteryItemData.create(block, state.getValue(UpholsteryItemData.COLOUR)));
            for (BlockPos part : bed.structure(base, state).keySet()) {
                player.setPos(base.getX() + .5, base.getY(), base.getZ() + .5);
                BlockPos head = bed.sleepPos(part, level.getBlockState(part));
                player.gameMode.useItemOn(player, level, ItemStack.EMPTY, InteractionHand.MAIN_HAND,
                        new BlockHitResult(Vec3.atCenterOf(part), Direction.UP, part, false));
                if (!player.isSleeping()) {
                    var rule = bed.getBedRule(level, head);
                    throw new AssertionError("Every bed part starts native sleeping: " + state + " clicked=" + part
                            + " canSleep=" + rule.canSleep(level) + " nativeResult=" + player.startSleepInBed(bed, level.getBlockState(head), rule, head));
                }
                check.accept(true, "Every bed part starts native sleeping");
                check.accept(player.getRespawnConfig() != null && player.getRespawnConfig().respawnData().pos().equals(head),
                        "Sleeping records the correct lower head as the respawn point");
                check.accept(level.getBlockState(head).getValue(FurnitureBed.OCCUPIED), "The slept-in bed is occupied");
                player.stopSleepInBed(true, true);
                check.accept(!level.getBlockState(head).getValue(FurnitureBed.OCCUPIED), "Waking releases the occupied flag");
            }
            level.destroyBlock(base, false);
            level.getEntitiesOfClass(ItemEntity.class, new AABB(base).inflate(5)).forEach(ItemEntity::discard);
        }
        for (int x = -4; x <= 4; x++) for (int z = -4; z <= 4; z++) {
            level.setBlock(base.offset(x, -1, z), Blocks.AIR.defaultBlockState(), Block.UPDATE_CLIENTS);
            level.setBlock(base.offset(x, -3, z), Blocks.STONE.defaultBlockState(), Block.UPDATE_CLIENTS);
        }
        for (Block block : BuiltInRegistries.BLOCK.stream().filter(value -> value instanceof ShieldChair).toList())
                for (Direction facing : Direction.Plane.HORIZONTAL) {
            BlockState state = block.defaultBlockState().setValue(FurnitureBed.DIRECTION, facing);
            level.setBlockAndUpdate(base.below(), Blocks.STONE.defaultBlockState());
            level.setBlockAndUpdate(base, state);
            ItemStack original = new ItemStack(Items.SHIELD);
            original.set(DataComponents.CUSTOM_NAME, Component.literal("Falling shield")); original.setDamageValue(89);
            original.set(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true);
            ((ShieldChairBlockEntity)level.getBlockEntity(base)).setShield(original);
            level.setBlockAndUpdate(base.below(), Blocks.AIR.defaultBlockState());
            state.tick(level, base, level.getRandom());
            var falling = level.getEntitiesOfClass(FallingShieldChair.class, new AABB(base).inflate(3));
            check.accept(falling.size() == 1 && ItemStack.matches(falling.getFirst().shield(), original), "Support loss transfers the full shield to one falling entity");
            check.accept(level.getEntitiesOfClass(ItemEntity.class, new AABB(base).inflate(3)).isEmpty(), "Starting a fall does not also drop the shield");
            var output = TagValueOutput.createWithContext(ProblemReporter.DISCARDING, level.registryAccess());
            falling.getFirst().save(output); falling.getFirst().discard();
            FallingShieldChair restored = new FallingShieldChair(zone.moddev.mc.ironagefurniture.api.entity.Entities.FALLING_SHIELD_CHAIR.get(), level);
            restored.load(TagValueInput.create(ProblemReporter.DISCARDING, level.registryAccess(), output.buildResult()));
            check.accept(ItemStack.matches(restored.shield(), original) && restored.visibleState().equals(state), "Reloading an in-flight chair preserves its frame and shield");
            for (int tick = 0; tick < 80 && !restored.isRemoved(); tick++) restored.tick();
            check.accept(restored.isRemoved() && level.getBlockEntity(base.below(2)) instanceof ShieldChairBlockEntity,
                    "The reloaded shield chair lands as a placed block");
            var landed = (ShieldChairBlockEntity)level.getBlockEntity(base.below(2));
            check.accept(ItemStack.matches(landed.shield(), original), "Landing restores the original shield components");
            level.destroyBlock(base.below(2), true);
            var drops = level.getEntitiesOfClass(ItemEntity.class, new AABB(base).inflate(5));
            check.accept(drops.stream().filter(item -> ItemStack.matches(item.getItem(), original)).count() == 1, "The landed chair drops its shield exactly once");
            drops.forEach(ItemEntity::discard);
        }
        for (int x = -4; x <= 4; x++) for (int z = -4; z <= 4; z++) level.setBlock(base.offset(x, -3, z), Blocks.AIR.defaultBlockState(), Block.UPDATE_CLIENTS);
    }
    private PlayerFurnitureInteractionProbe() { }
}
