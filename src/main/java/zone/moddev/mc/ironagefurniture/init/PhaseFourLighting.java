package zone.moddev.mc.ironagefurniture.init;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import zone.moddev.mc.ironagefurniture.Ironagefurniture;
import zone.moddev.mc.ironagefurniture.ModItems;
import zone.moddev.mc.ironagefurniture.api.CandleInventory;
import zone.moddev.mc.ironagefurniture.api.SconceMetalData;
import zone.moddev.mc.ironagefurniture.api.blocks.base.FurnitureBlock;
import zone.moddev.mc.ironagefurniture.api.blocks.lightsource.phasefour.CandleSconceBlock;
import zone.moddev.mc.ironagefurniture.api.blocks.lightsource.phasefour.TwinTorchSconce;
import zone.moddev.mc.ironagefurniture.api.enumerations.SconceMetal;
import zone.moddev.mc.ironagefurniture.api.items.MetalSconceBlockItem;
import zone.moddev.mc.ironagefurniture.api.tile.CandleSconceBlockEntity;

/** Additional light families keep the original sconce frame identities. */
public final class PhaseFourLighting {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(BuiltInRegistries.BLOCK, Ironagefurniture.MODID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, Ironagefurniture.MODID);
    public static final DeferredHolder<Block, CandleSconceBlock> FLOOR_CANDLES = SconceVariants.register(BLOCKS, "light_metal_ironage_sconce_floor_candle_iron",
            path -> new CandleSconceBlock(path, false));
    public static final DeferredHolder<Block, CandleSconceBlock> WALL_CANDLES = SconceVariants.register(BLOCKS, "light_metal_ironage_sconce_wall_candle_iron",
            path -> new CandleSconceBlock(path, true));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<CandleSconceBlockEntity>> CANDLE_STORAGE =
            BLOCK_ENTITIES.register("candle_sconce", () -> new BlockEntityType<>(CandleSconceBlockEntity::new,
                    BuiltInRegistries.BLOCK.stream().filter(block -> block instanceof CandleSconceBlock).collect(java.util.stream.Collectors.toSet())));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<zone.moddev.mc.ironagefurniture.api.tile.MetalSconceBlockEntity>> METAL_FRAME =
            BLOCK_ENTITIES.register("metal_sconce", () -> new BlockEntityType<>(zone.moddev.mc.ironagefurniture.api.tile.MetalSconceBlockEntity::new,
                    BuiltInRegistries.BLOCK.stream().filter(block -> block instanceof zone.moddev.mc.ironagefurniture.api.blocks.base.LightHolderSconce
                            && !(block instanceof CandleSconceBlock)).collect(java.util.stream.Collectors.toSet())));
    public static final DeferredHolder<Item, Item> TALLOW = ModItems.REGISTER.register("tallow",
            () -> new Item(RegistrationProperties.item(new Item.Properties(), "tallow")));
    public static final DeferredHolder<Block, zone.moddev.mc.ironagefurniture.api.blocks.lightsource.phasefour.RockSaltSconce> FLOOR_SALT =
            SconceVariants.register(BLOCKS, "light_metal_ironage_sconce_floor_rocksalt_iron", path -> new zone.moddev.mc.ironagefurniture.api.blocks.lightsource.phasefour.RockSaltSconce(path, false));
    public static final DeferredHolder<Block, zone.moddev.mc.ironagefurniture.api.blocks.lightsource.phasefour.RockSaltSconce> WALL_SALT =
            SconceVariants.register(BLOCKS, "light_metal_ironage_sconce_wall_rocksalt_iron", path -> new zone.moddev.mc.ironagefurniture.api.blocks.lightsource.phasefour.RockSaltSconce(path, true));
    private static final java.util.Map<String, DeferredHolder<Block, TwinTorchSconce>> TWINS = new java.util.HashMap<>();
    static {
        for (boolean wall : new boolean[]{false, true}) for (boolean lit : new boolean[]{false, true}) {
            String path = "light_metal_ironage_sconce_" + (wall ? "wall" : "floor") + "_torch_iron_twin" + (lit ? "" : "_unlit");
            TWINS.put(wall + ":" + lit, SconceVariants.register(BLOCKS, path, name -> new TwinTorchSconce(name, wall, lit)));
        }
        for (SconceMetal metal : SconceMetal.values()) if (metal != SconceMetal.IRON) {
            String path = SconceMetalData.itemId(metal).getPath();
            ModItems.REGISTER.register(path, () -> new MetalSconceBlockItem(SconceVariants.material(ModVanillaLights.light_metal_ironage_sconce_floor_empty_iron.get(), metal),
                    RegistrationProperties.item(new Item.Properties(), path).useBlockDescriptionPrefix(), metal));
        }
    }
    public static TwinTorchSconce twin(boolean wall, boolean lit) { return TWINS.get(wall + ":" + lit).get(); }
    public static boolean addSecondTorch(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand) {
        if (!player.getItemInHand(hand).is(net.minecraft.world.item.Items.TORCH)) return false;
        boolean wall = SconceVariants.isFamily(state.getBlock(), ModVanillaLights.light_metal_ironage_sconce_wall_torch_iron.get());
        if (!wall && !SconceVariants.isFamily(state.getBlock(), ModVanillaLights.light_metal_ironage_sconce_floor_torch_iron.get())) return false;
        if (!world.isClientSide()) {
            world.setBlock(pos, SconceMetalData.transition(twin(wall, !state.getValue(FurnitureBlock.WATERLOGGED)), state), Block.UPDATE_ALL);
            if (!player.isCreative()) player.getItemInHand(hand).shrink(1);
        }
        return true;
    }
    public static boolean insertCandle(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand) {
        ItemStack held = player.getItemInHand(hand);
        if (!CandleInventory.accepts(held)) return false;
        boolean wall = SconceVariants.isFamily(state.getBlock(), ModVanillaLights.light_metal_ironage_sconce_wall_empty_iron.get());
        if (!wall && !SconceVariants.isFamily(state.getBlock(), ModVanillaLights.light_metal_ironage_sconce_floor_empty_iron.get())) return false;
        if (world.isClientSide()) return true;
        CandleSconceBlock block = wall ? WALL_CANDLES.get() : FLOOR_CANDLES.get();
        world.setBlock(pos, SconceMetalData.transition(block, state), Block.UPDATE_ALL);
        if (world.getBlockEntity(pos) instanceof CandleSconceBlockEntity storage && storage.insert(held)) {
            if (!player.isCreative()) held.shrink(1);
        } else world.setBlock(pos, state, Block.UPDATE_ALL);
        return true;
    }
    public static boolean insertRockSalt(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand) {
        if (!net.neoforged.fml.ModList.get().isLoaded("mineralogy") || !zone.moddev.mc.ironagefurniture.IronAgeFurnitureConfiguration.enabled(
                zone.moddev.mc.ironagefurniture.IronAgeFurnitureConfiguration.MINERALOGY)) return false;
        Item lamp = BuiltInRegistries.ITEM.getValue(zone.moddev.mc.ironagefurniture.api.blocks.lightsource.phasefour.RockSaltSconce.LAMP);
        if (lamp == net.minecraft.world.item.Items.AIR || !player.getItemInHand(hand).is(lamp)) return false;
        boolean wall = SconceVariants.isFamily(state.getBlock(), ModVanillaLights.light_metal_ironage_sconce_wall_empty_iron.get());
        if (!wall && !SconceVariants.isFamily(state.getBlock(), ModVanillaLights.light_metal_ironage_sconce_floor_empty_iron.get())) return false;
        if (!world.isClientSide()) {
            world.setBlock(pos, SconceMetalData.transition((wall ? WALL_SALT : FLOOR_SALT).get(), state), Block.UPDATE_ALL);
            if (!player.isCreative()) player.getItemInHand(hand).shrink(1);
        }
        return true;
    }
    private PhaseFourLighting() { }
}
