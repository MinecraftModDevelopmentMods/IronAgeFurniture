package zone.moddev.mc.ironagefurniture.fixture;

import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.event.entity.player.PlayerEvent;
import zone.moddev.mc.ironagefurniture.api.blocks.lightsource.phasefour.CandleSconceBlock;
import zone.moddev.mc.ironagefurniture.api.tile.CandleSconceBlockEntity;
import zone.moddev.mc.ironagefurniture.init.PhaseFourLighting;
import zone.moddev.mc.ironagefurniture.init.ModVanillaLights;

/** A disposable gallery for checking real client packets and baked candle models. */
public final class ClientScene {
    private static int observers, connectedTicks, emptyTicks;
    private static int releaseTicks;
    private static boolean updated;
    public static List<net.minecraft.world.level.block.Block> shieldChairs() {
        return BuiltInRegistries.BLOCK.stream().filter(block -> block instanceof zone.moddev.mc.ironagefurniture.api.blocks.furniture.ShieldChair).toList();
    }
    public static BlockPos shieldPosition(int index) { return new BlockPos(24 + (index % 5) * 2, 100, (index / 5) * 2); }
    public static ItemStack shield(int index) {
        return shield(index, false);
    }
    public static ItemStack shield(int index, boolean update) {
        ItemStack stack = new ItemStack(net.minecraft.world.item.Items.SHIELD);
        stack.set(DataComponents.CUSTOM_NAME, Component.literal((update ? "Updated gallery shield " : "Gallery shield ") + index));
        stack.setDamageValue(index + (update ? 73 : 31)); stack.set(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, !update);
        stack.set(DataComponents.BASE_COLOR, net.minecraft.world.item.DyeColor.byId(index % 16));
        return stack;
    }
    public static final List<String> TYPES = List.of("candle", "white_candle", "orange_candle", "magenta_candle", "light_blue_candle",
            "yellow_candle", "lime_candle", "pink_candle", "gray_candle", "light_gray_candle", "cyan_candle", "purple_candle",
            "blue_candle", "brown_candle", "green_candle", "red_candle", "black_candle");
    public static BlockPos position(int index, boolean wall) { return new BlockPos((index % 9) * 2, 100, (index / 9) * 3 + (wall ? 8 : 0)); }
    public static ItemStack candle(int index, int slot) {
        return candle(index, slot, false);
    }
    public static ItemStack candle(int index, int slot, boolean update) {
        ItemStack stack = new ItemStack(BuiltInRegistries.ITEM.getValue(Identifier.withDefaultNamespace(TYPES.get((index + slot + (update ? 3 : 0)) % TYPES.size()))));
        stack.set(DataComponents.CUSTOM_NAME, Component.literal((update ? "Updated gallery candle " : "Gallery candle ") + index + ":" + slot));
        stack.set(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, slot % 2 == 1);
        return stack;
    }
    public static void create(MinecraftServer server) {
        observers = connectedTicks = emptyTicks = releaseTicks = 0;
        updated = false;
        var level = server.overworld();
        for (int x = -1; x <= 2; x++) for (int z = -1; z <= 2; z++) { level.setChunkForced(x, z, true); level.getChunk(x, z); }
        for (boolean wall : new boolean[]{false, true}) for (int index = 0; index < TYPES.size(); index++) {
            BlockPos pos = position(index, wall);
            Direction facing = Direction.from2DDataValue(index % 4);
            var holder = wall ? PhaseFourLighting.WALL_CANDLES.get() : PhaseFourLighting.FLOOR_CANDLES.get();
            BlockPos support = wall ? pos.relative(facing.getOpposite()) : pos.below();
            level.setBlockAndUpdate(support, Blocks.STONE.defaultBlockState());
            level.setBlockAndUpdate(pos, holder.defaultBlockState().setValue(CandleSconceBlock.DIRECTION, facing));
            var storage = (CandleSconceBlockEntity)level.getBlockEntity(pos);
            while (!storage.candles().isEmpty()) storage.removeNewest();
            for (int slot = 0; slot <= index % 4; slot++) storage.insert(candle(index, slot));
            level.setBlockAndUpdate(pos, level.getBlockState(pos).setValue(CandleSconceBlock.LIT, true));
        }
        List<net.minecraft.world.level.block.Block> shields = shieldChairs();
        for (int index = 0; index < shields.size(); index++) {
            BlockPos pos = shieldPosition(index);
            level.setBlockAndUpdate(pos.below(), Blocks.STONE.defaultBlockState());
            level.setBlockAndUpdate(pos, shields.get(index).defaultBlockState().setValue(CandleSconceBlock.DIRECTION, Direction.from2DDataValue(index % 4)));
            ((zone.moddev.mc.ironagefurniture.api.tile.ShieldChairBlockEntity)level.getBlockEntity(pos)).setShield(shield(index));
        }
    }
    public static void joined(PlayerEvent.PlayerLoggedInEvent event) {
        if (!Boolean.getBoolean("iaf.probe.clientRendering") || !(event.getEntity() instanceof ServerPlayer player)) return;
        player.setGameMode(GameType.CREATIVE);
        player.setYRot(180); player.setXRot(24);
        player.teleportTo(8.5, 106, 22);
        player.getAbilities().flying = true; player.onUpdateAbilities();
    }
    public static void tick(MinecraftServer server) {
        if (!Boolean.getBoolean("iaf.probe.multiplayer") && server.getPlayerList().getPlayerCount() > 0 && ++releaseTicks == 80) {
            var level = server.overworld();
            for (boolean wall : new boolean[]{false, true}) {
                BlockPos pos = new BlockPos(38, 104, wall ? 8 : 4);
                BlockPos support = wall ? pos.north() : pos.below();
                level.setBlockAndUpdate(pos.below(4), Blocks.STONE.defaultBlockState());
                level.setBlockAndUpdate(support, Blocks.STONE.defaultBlockState());
                var block = wall ? ModVanillaLights.light_metal_ironage_sconce_wall_lava_iron.get()
                        : ModVanillaLights.light_metal_ironage_sconce_floor_lava_iron.get();
                level.setBlockAndUpdate(pos, block.defaultBlockState().setValue(CandleSconceBlock.DIRECTION, Direction.SOUTH));
                level.setBlockAndUpdate(support, Blocks.AIR.defaultBlockState());
            }
            System.out.println("IAF_CLIENT_LAVA_RELEASED holders=2");
        }
        if (!Boolean.getBoolean("iaf.probe.multiplayer")) return;
        int players = server.getPlayerList().getPlayerCount();
        observers = Math.max(observers, players);
        if (!updated && players >= 2 && ++connectedTicks >= 60) {
            var level = server.overworld();
            for (boolean wall : new boolean[]{false, true}) for (int index = 0; index < TYPES.size(); index++) {
                var storage = (CandleSconceBlockEntity)level.getBlockEntity(position(index, wall));
                while (!storage.candles().isEmpty()) storage.removeNewest();
                for (int slot = 0; slot < (index + 1) % 4 + 1; slot++) storage.insert(candle(index, slot, true));
            }
            for (int index = 0; index < shieldChairs().size(); index++)
                ((zone.moddev.mc.ironagefurniture.api.tile.ShieldChairBlockEntity)level.getBlockEntity(shieldPosition(index))).setShield(shield(index, true));
            updated = true;
            System.out.println("IAF_NETWORK_SERVER_UPDATED observers=" + observers);
        }
        if (updated && players == 0 && ++emptyTicks >= 40) {
            System.out.println("IAF_NETWORK_SERVER_PASS observers=" + observers);
            server.halt(false);
        }
    }
    private ClientScene() { }
}
