package zone.moddev.mc.ironagefurniture.init;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ObjectHolder;
import zone.moddev.mc.ironagefurniture.Ironagefurniture;
import zone.moddev.mc.ironagefurniture.api.blocks.furniture.ShieldChair;
import zone.moddev.mc.ironagefurniture.api.tile.ShieldChairTileEntity;

/** Tile types are built after block registration, so disabled woods stay absent. */
@Mod.EventBusSubscriber(modid = Ironagefurniture.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
@ObjectHolder(Ironagefurniture.MODID)
public final class PhaseFourTileEntities {
    public static final BlockEntityType<ShieldChairTileEntity> shield_chair = null;

    private PhaseFourTileEntities() { }

    @SubscribeEvent
    public static void registerTiles(RegistryEvent.Register<BlockEntityType<?>> event) {
        Block[] chairs = ForgeRegistries.BLOCKS.getValues().stream()
                .filter(block -> block instanceof ShieldChair).toArray(Block[]::new);
        event.getRegistry().register(BlockEntityType.Builder.<ShieldChairTileEntity>of(ShieldChairTileEntity::new, chairs)
                .build(null).setRegistryName(Ironagefurniture.MODID, "shield_chair"));
    }
}
