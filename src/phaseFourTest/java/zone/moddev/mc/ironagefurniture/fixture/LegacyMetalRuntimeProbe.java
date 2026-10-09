package zone.moddev.mc.ironagefurniture.fixture;

import com.mojang.authlib.GameProfile;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.UUID;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ChestTileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.FakePlayerFactory;
import net.minecraftforge.registries.ForgeRegistries;
import zone.moddev.mc.ironagefurniture.api.SconceMetalData;
import zone.moddev.mc.ironagefurniture.api.blocks.base.FurnitureBlock;
import zone.moddev.mc.ironagefurniture.api.enumerations.SconceMetal;

/** Reads a saved 1.12 fixture; it never creates the expected target furniture. */
final class LegacyMetalRuntimeProbe {
    private LegacyMetalRuntimeProbe() { }
    static int run(ServerWorld world) throws Exception {
        Properties source = new Properties();
        try (InputStream input = Files.newInputStream(Paths.get("legacy-metal-source-pass.properties"))) { source.load(input); }
        require("PASS".equals(source.getProperty("status")), "Source fixture did not pass");
        int blocks = Integer.parseInt(source.getProperty("blocks")), cases = 0;
        for (int index = 0; index < blocks; index++) {
            ResourceLocation id = new ResourceLocation(source.getProperty("block_" + index));
            Block expected = ForgeRegistries.BLOCKS.getValue(id);
            require(expected != null, "Port omitted historical sconce " + id);
            for (SconceMetal metal : SconceMetal.values()) for (int facing = 0; facing < 4; facing++) {
                BlockPos pos = new BlockPos(1024 + metal.ordinal()*8 + facing*2, 80, 1024 + index*3);
                BlockState actual = world.getBlockState(pos);
                require(actual.getBlock() == expected, "Flattening lost sconce " + id + " at " + pos + ": " + actual);
                require(SconceMetalData.get(actual) == metal, "Flattening lost metal " + metal + ": " + actual);
                require(actual.get(FurnitureBlock.DIRECTION) == Direction.byHorizontalIndex(facing), "Flattening changed facing");
                require(!actual.get(FurnitureBlock.WATERLOGGED), "Dry legacy sconce became wet");
                require(world.getTileEntity(pos) == null, "Obsolete metal tile was not removed");
                cases++;
            }
        }
        world.getChunk(new BlockPos(1024,80,1008));
        ChestTileEntity chest = (ChestTileEntity)world.getTileEntity(new BlockPos(1024,80,1008));
        require(chest != null, "Saved chest disappeared");
        for (SconceMetal metal : SconceMetal.values()) verify(chest.getStackInSlot(metal.ordinal()), metal, "metal-" + metal.getName(), 3);
        ItemStack nested = ItemStack.read(chest.getStackInSlot(24).getTag().getCompound("BlockEntityTag").getList("Items",10).getCompound(0));
        verify(nested, SconceMetal.STARSTEEL, "nested", 4);
        for (int x=1040;x<=1062;x++) world.getChunk(new BlockPos(x,80,1008));
        java.util.List<ItemEntity> drops = world.getEntitiesWithinAABB(ItemEntity.class, new AxisAlignedBB(1039,79,1007,1064,82,1010));
        require(drops.size() == 23, "Saved dropped frames disappeared: " + drops.size());
        for (SconceMetal metal : SconceMetal.values()) {
            ItemStack found = drops.stream().map(ItemEntity::getItem).filter(stack -> SconceMetalData.get(stack) == metal).findFirst()
                    .orElseThrow(() -> new IllegalStateException("Dropped frame lost " + metal));
            verify(found, metal, "metal-" + metal.getName(), 3);
        }
        FakePlayer player = FakePlayerFactory.get(world, new GameProfile(UUID.fromString("d66a4a43-33b1-4890-a8eb-1931d30e8b5d"), "LegacyMetalProbe"));
        require(world.getSaveHandler().readPlayerData(player) != null, "Saved player data did not load");
        for (SconceMetal metal : SconceMetal.values()) {
            verify(player.inventory.getStackInSlot(metal.ordinal()), metal, "metal-" + metal.getName(), 3);
            verify(player.getInventoryEnderChest().getStackInSlot(metal.ordinal()), metal, "metal-" + metal.getName(), 3);
        }
        world.getSaveHandler().writePlayerData(player);
        return cases + 93;
    }
    private static void verify(ItemStack stack, SconceMetal metal, String keep, int count) {
        require(stack.getItem() == ForgeRegistries.ITEMS.getValue(SconceMetalData.itemId(
                new ResourceLocation("ironagefurniture:light_metal_ironage_sconce_floor_empty_iron"), metal)), "Frame item lost");
        require(stack.getCount() == count && SconceMetalData.get(stack) == metal, "Legacy item lost metal/count: " + metal + ": " + stack.write(new CompoundNBT()));
        require(stack.hasTag() && keep.equals(stack.getTag().getString("Keep")), "Unrelated item NBT changed");
        require(!stack.getTag().contains("Damage"), "Legacy metal remained as durability");
        require(!stack.getTag().contains("Metal"), "Legacy metal field was not flattened");
    }
    private static void require(boolean okay, String message) { if (!okay) throw new IllegalStateException(message); }
}
