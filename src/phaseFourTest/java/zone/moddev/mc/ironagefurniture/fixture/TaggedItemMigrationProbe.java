package zone.moddev.mc.ironagefurniture.fixture;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import com.mojang.authlib.GameProfile;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.tileentity.ChestTileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.FakePlayerFactory;
import net.minecraftforge.registries.ForgeRegistries;
import zone.moddev.mc.ironagefurniture.api.UpholsteryItemData;
import zone.moddev.mc.ironagefurniture.api.SconceMetalData;
import zone.moddev.mc.ironagefurniture.api.blocks.furniture.FurnitureBed;
import zone.moddev.mc.ironagefurniture.api.blocks.furniture.MultiBlockChair;
import zone.moddev.mc.ironagefurniture.api.enumerations.UpholsteryColour;
import zone.moddev.mc.ironagefurniture.api.enumerations.SconceMetal;

/** Saves old tagged stacks using the previous jar, then reads that real save with the new jar. */
final class TaggedItemMigrationProbe {
    private static final String FILE = "tagged-source.properties";
    private static final java.util.UUID PLAYER = java.util.UUID.fromString("2b74fc90-ac03-4f73-83d4-bcb872120774");
    private TaggedItemMigrationProbe() { }
    static int run(ServerWorld world, boolean seed) throws Exception {
        Properties records = new Properties();
        List<ItemStack> stacks = new ArrayList<>();
        FakePlayer player = FakePlayerFactory.get(world, new GameProfile(PLAYER, "TaggedSource"));
        if (seed) {
            for (Block block : ForgeRegistries.BLOCKS.getValues()) {
                if (!(block instanceof FurnitureBed) && !(block instanceof MultiBlockChair)) continue;
                if (block instanceof FurnitureBed && ((FurnitureBed)block).itemBlock() != block) continue;
                for (UpholsteryColour colour : UpholsteryColour.values()) {
                    int index = stacks.size();
                    forRecord(records, index, block.getRegistryName().toString(), "Color", colour.getSerializedName());
                    ItemStack stack = new ItemStack(block.asItem(), 3);
                    decorate(stack, index); stack.getTag().putString("Color", colour.getSerializedName()); stacks.add(stack);
                    BlockPos pos = furniturePosition(index);
                    BlockState state = block.defaultBlockState().setValue(UpholsteryItemData.COLOUR, colour)
                            .setValue(FurnitureBed.DIRECTION, Direction.from2DDataValue(colour.ordinal() % 4))
                            .setValue(FurnitureBed.WATERLOGGED, colour.ordinal() % 2 == 1);
                    Map<BlockPos, BlockState> parts = parts(block, pos, state);
                    parts.forEach((part, value) -> {
                        if (!parts.containsKey(part.below())) world.setBlock(part.below(), Blocks.STONE.defaultBlockState(), 2);
                        world.setBlock(part, value, 2);
                    });
                }
            }
            for (SconceMetal metal : SconceMetal.values()) {
                int index = stacks.size();
                String id = "ironagefurniture:light_metal_ironage_sconce_floor_empty_iron";
                forRecord(records, index, id, "Metal", metal.getSerializedName());
                ItemStack stack = new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation(id)), 3);
                decorate(stack, index); stack.getTag().putString("Metal", metal.getSerializedName()); stacks.add(stack);
                Block block = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(id));
                BlockPos pos = furniturePosition(index);
                world.setBlock(pos.below(), Blocks.STONE.defaultBlockState(), 2);
                world.setBlock(pos, block.defaultBlockState().setValue(SconceMetalData.METAL, metal), 2);
            }
            records.setProperty("count", Integer.toString(stacks.size()));
        } else {
            try (java.io.InputStream input = Files.newInputStream(Paths.get(FILE))) { records.load(input); }
        }
        int count = Integer.parseInt(records.getProperty("count")), checks = 0;
        for (int index = 0; index < count; index++) {
            BlockPos chestPos = chestPosition(index);
            if (seed && index % 13 == 0) world.setBlock(chestPos, Blocks.CHEST.defaultBlockState(), 2);
            ChestTileEntity chest = (ChestTileEntity)world.getBlockEntity(chestPos);
            require(chest != null, "Tagged source chest disappeared: " + chestPos);
            int slot = index % 13 * 2;
            if (seed) {
                ItemStack stack = stacks.get(index);
                chest.setItem(slot, stack.copy());
                ItemStack box = new ItemStack(Items.SHULKER_BOX);
                CompoundNBT content = stack.save(new CompoundNBT()); content.putByte("Slot", (byte) 0);
                ListNBT items = new ListNBT(); items.add(content);
                box.getOrCreateTagElement("BlockEntityTag").put("Items", items);
                chest.setItem(slot + 1, box);
                ItemEntity entity = new ItemEntity(world, dropPosition(index).getX() + .5, 90,
                        dropPosition(index).getZ() + .5, stack.copy());
                entity.setExtendedLifetime(); entity.setNeverPickUp(); entity.setNoGravity(true); entity.setDeltaMovement(0, 0, 0);
                world.getChunk(dropPosition(index));
                require(world.addFreshEntity(entity), "Could not save source item entity: " + index);
            } else {
                verify(chest.getItem(slot), records, index); checks++;
                CompoundNBT nested = chest.getItem(slot + 1).getTag().getCompound("BlockEntityTag")
                        .getList("Items", 10).getCompound(0);
                verify(ItemStack.of(nested), records, index); checks++;
                BlockPos drop = dropPosition(index); world.getChunk(drop);
                List<ItemEntity> entities = world.getEntitiesOfClass(ItemEntity.class, new AxisAlignedBB(drop).inflate(1));
                require(entities.size() == 1, "Saved tagged item disappeared/duplicated: " + index + "/" + entities.size());
                verify(entities.get(0).getItem(), records, index); checks++;
                BlockPos pos = furniturePosition(index);
                Block expected = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(records.getProperty(index + ".id")));
                BlockState state = world.getBlockState(pos);
                require(state.getBlock() == expected, "Placed identity changed: " + index);
                if (records.getProperty(index + ".kind").equals("Color")) {
                    UpholsteryColour colour = UpholsteryColour.byName(records.getProperty(index + ".variant"));
                    require(state.getValue(UpholsteryItemData.COLOUR) == colour, "Placed colour changed: " + index);
                    require(state.getValue(FurnitureBed.DIRECTION) == Direction.from2DDataValue(colour.ordinal() % 4), "Facing changed");
                    require(state.getValue(FurnitureBed.WATERLOGGED) == (colour.ordinal() % 2 == 1), "Water state changed: " + index);
                    for (Map.Entry<BlockPos, BlockState> part : parts(expected, pos, state).entrySet()) {
                        require(world.getBlockState(part.getKey()) == part.getValue(), "Structure part changed: " + part.getKey()); checks++;
                    }
                } else require(state.getValue(SconceMetalData.METAL).getName().equals(records.getProperty(index + ".variant")), "Placed metal changed");
                checks++;
            }
        }
        if (!seed) require(world.getLevelStorage().load(player) != null, "Tagged source player disappeared");
        for (int slot = 0; slot < 27; slot++) {
            int index = slot < 16 ? slot : count - 23 + slot - 16;
            if (seed) {
                player.inventory.setItem(slot, stacks.get(index).copy());
                player.getEnderChestInventory().setItem(slot, stacks.get(index).copy());
            } else {
                verify(player.inventory.getItem(slot), records, index);
                verify(player.getEnderChestInventory().getItem(slot), records, index); checks += 2;
            }
        }
        world.getLevelStorage().save(player);
        if (seed) try (java.io.OutputStream output = Files.newOutputStream(Paths.get(FILE))) { records.store(output, "Previous 1.14 tagged format"); }
        return seed ? count : checks;
    }
    private static void forRecord(Properties records, int index, String id, String kind, String variant) {
        records.setProperty(index + ".id", id); records.setProperty(index + ".kind", kind);
        records.setProperty(index + ".variant", variant);
    }
    private static void decorate(ItemStack stack, int index) {
        stack.getOrCreateTag().putString("Keep", "case-" + index);
        stack.getTag().putInt("CustomModelData", index + 100);
        CompoundNBT display = new CompoundNBT(); display.putString("Name", "{\"text\":\"Family furniture " + index + "\"}");
        stack.getTag().put("display", display);
    }
    private static void verify(ItemStack stack, Properties records, int index) {
        String base = records.getProperty(index + ".id"), variant = records.getProperty(index + ".variant");
        String expected = records.getProperty(index + ".kind").equals("Color")
                ? base + (variant.equals("red") ? "" : "_" + variant)
                : base.replace("_empty_iron", "_empty_" + (variant.equals("iron") || variant.equals("gold") ? "" : "basemetals_") + variant);
        require(!stack.isEmpty() && stack.getItem().getRegistryName().toString().equals(expected), "Tagged item lost identity: " + index + ": " + stack);
        require(stack.getCount() == 3 && stack.hasTag(), "Tagged item lost count or data");
        require(stack.getTag().getString("Keep").equals("case-" + index)
                && stack.getTag().getInt("CustomModelData") == index + 100
                && stack.getTag().getCompound("display").getString("Name").equals("{\"text\":\"Family furniture " + index + "\"}"), "Other item data changed");
        require(!stack.getTag().contains("Color") && !stack.getTag().contains("Metal") && !stack.getTag().contains("Damage"), "Obsolete variant data remained");
    }
    private static BlockPos chestPosition(int index) { return new BlockPos(1200 + index / 13 * 3, 85, 1200); }
    private static BlockPos furniturePosition(int index) { return new BlockPos(1536 + index % 16 * 10, 80, 1536 + index / 16 * 10); }
    private static BlockPos dropPosition(int index) { return new BlockPos(1200 + index % 32 * 3, 90, 1216 + index / 32 * 3); }
    private static Map<BlockPos, BlockState> parts(Block block, BlockPos pos, BlockState state) {
        if (block instanceof FurnitureBed) {
            Map<BlockPos, BlockState> result = ((FurnitureBed)block).structure(pos, state);
            // Actual placement takes water from each occupied cell. This
            // synthetic source instead fills every part of the wet cases.
            result.replaceAll((part, value) -> value.setValue(FurnitureBed.WATERLOGGED, state.getValue(FurnitureBed.WATERLOGGED)));
            return result;
        }
        Map<BlockPos, BlockState> result = new java.util.LinkedHashMap<>();
        MultiBlockChair chair = (MultiBlockChair)block;
        for (int height = 0; height < chair.getChairHeight(); height++) result.put(pos.above(height), state.setValue(MultiBlockChair.PART, chair.partForOffset(height)));
        return result;
    }
    private static void require(boolean okay, String message) { if (!okay) throw new IllegalStateException(message); }
}
