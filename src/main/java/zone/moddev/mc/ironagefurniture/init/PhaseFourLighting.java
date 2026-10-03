package zone.moddev.mc.ironagefurniture.init;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.particles.ParticleType;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import zone.moddev.mc.ironagefurniture.Ironagefurniture;
import zone.moddev.mc.ironagefurniture.api.blocks.lightsource.phasefour.Candle;
import zone.moddev.mc.ironagefurniture.api.blocks.lightsource.phasefour.AdditionalSconce;
import zone.moddev.mc.ironagefurniture.api.blocks.lightsource.phasefour.LightInteractions;
import zone.moddev.mc.ironagefurniture.api.blocks.lightsource.phasefour.RockSaltSconce;
import zone.moddev.mc.ironagefurniture.api.MineralogyCompat;

/** New light identities remain compatible with the accepted 1.10/1.12 release. */
@Mod.EventBusSubscriber(modid = Ironagefurniture.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class PhaseFourLighting {
    public static final BasicParticleType CANDLE_FLAME = new BasicParticleType(false);
    private PhaseFourLighting() { }
    public static Block block(String path) {
        return ForgeRegistries.BLOCKS.getValue(new ResourceLocation(Ironagefurniture.MODID, path));
    }
    public static String candleId(boolean wall, boolean lit) {
        return "light_metal_ironage_candle_" + (wall ? "wall" : "floor") + (lit ? "" : "_unlit");
    }
    public static Block candle(boolean wall, boolean lit) { return block(candleId(wall, lit)); }
    public static String sconceId(boolean wall, boolean lit, boolean twinTorch, int count) {
        String path = "light_metal_ironage_sconce_" + (wall ? "wall" : "floor") + (twinTorch ? "_torch_iron_twin" : "_candle_iron");
        if (!twinTorch && count > 1) path += "_" + new String[]{"", "", "two", "three", "four"}[count];
        return path + (lit ? "" : "_unlit");
    }
    public static Block sconce(boolean wall, boolean lit, boolean twinTorch, int count) { return block(sconceId(wall, lit, twinTorch, count)); }
    public static String rockSaltId(boolean wall) { return "light_metal_ironage_sconce_" + (wall ? "wall" : "floor") + "_rocksalt_iron"; }
    public static Block rockSalt(boolean wall) { return block(rockSaltId(wall)); }
    public static boolean isLightItem(ItemStack stack) {
        net.minecraft.item.Item item = stack.getItem();
        return item == net.minecraft.item.Items.TORCH || item == net.minecraft.item.Items.REDSTONE_TORCH
                || item == candle(false, true).asItem() || MineralogyCompat.isRockSaltLamp(stack)
                || item == zone.moddev.mc.ironagefurniture.BlockObjectHolder.light_metal_ironage_block_floor_glow_clear.asItem()
                || item == zone.moddev.mc.ironagefurniture.BlockObjectHolder.light_metal_ironage_block_floor_lava_clear.asItem()
                || item == zone.moddev.mc.ironagefurniture.BlockObjectHolder.light_metal_ironage_block_floor_red_clear.asItem();
    }
    public static boolean insertRockSalt(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, boolean wall) {
        ItemStack held = player.getHeldItem(hand);
        if (!MineralogyCompat.isRockSaltLamp(held)) return false;
        if (!world.isRemote) {
            LightInteractions.replace(world, pos, state, rockSalt(wall));
            if (!player.isCreative()) held.shrink(1);
        }
        return true;
    }
    public static boolean insertCandle(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, boolean wall) {
        ItemStack held = player.getHeldItem(hand);
        if (held.getItem() != candle(false, true).asItem()) return false;
        if (!world.isRemote) {
            LightInteractions.replace(world, pos, state, sconce(wall, !state.get(Candle.WATERLOGGED), false, 1));
            if (!player.isCreative()) held.shrink(1);
        }
        return true;
    }
    public static boolean addSecondTorch(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand) {
        String path = state.getBlock().getRegistryName().getPath();
        boolean single = path.equals("light_metal_ironage_sconce_floor_torch_iron") || path.equals("light_metal_ironage_sconce_wall_torch_iron");
        if (!single || player.getHeldItem(hand).getItem() != net.minecraft.item.Items.TORCH) return false;
        if (!world.isRemote) {
            LightInteractions.replace(world, pos, state, sconce(path.contains("_wall_"), !state.get(Candle.WATERLOGGED), true, 2));
            if (!player.isCreative()) player.getHeldItem(hand).shrink(1);
        }
        return true;
    }
    @SubscribeEvent public static void registerParticles(RegistryEvent.Register<ParticleType<?>> event) {
        event.getRegistry().register(CANDLE_FLAME.setRegistryName(Ironagefurniture.MODID, "candle_flame"));
    }
    @SubscribeEvent public static void registerBlocks(RegistryEvent.Register<Block> event) {
        if (MineralogyCompat.isEnabled()) for (boolean wall : new boolean[]{false, true})
            event.getRegistry().register(new RockSaltSconce(rockSaltId(wall), wall));
        for (boolean wall : new boolean[]{false, true}) for (boolean lit : new boolean[]{false, true})
            event.getRegistry().register(new Candle(candleId(wall, lit), wall, lit));
        for (boolean wall : new boolean[]{false, true}) for (boolean lit : new boolean[]{false, true}) {
            event.getRegistry().register(new AdditionalSconce(sconceId(wall, lit, true, 2), wall, lit, true, 2));
            for (int count = 1; count <= 4; count++)
                event.getRegistry().register(new AdditionalSconce(sconceId(wall, lit, false, count), wall, lit, false, count));
        }
    }
    @SubscribeEvent public static void registerItems(RegistryEvent.Register<Item> event) {
        for (boolean wall : new boolean[]{false, true}) for (boolean lit : new boolean[]{false, true}) {
            Item.Properties properties = new Item.Properties();
            if (!wall && lit) properties.group(Ironagefurniture.IAF_GROUP);
            event.getRegistry().register(new BlockItem(candle(wall, lit), properties).setRegistryName(candle(wall, lit).getRegistryName()));
        }
        event.getRegistry().register(new Item(new Item.Properties().group(Ironagefurniture.IAF_GROUP))
                .setRegistryName(Ironagefurniture.MODID, "tallow"));
        for (Block block : ForgeRegistries.BLOCKS.getValues()) if (block instanceof AdditionalSconce || block instanceof RockSaltSconce)
            event.getRegistry().register(new zone.moddev.mc.ironagefurniture.api.items.MetalSconceBlockItem(block, new Item.Properties()).setRegistryName(block.getRegistryName()));
    }
}
