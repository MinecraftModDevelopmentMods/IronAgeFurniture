package zone.moddev.mc.ironagefurniture.api;

import java.util.ArrayList;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import zone.moddev.mc.ironagefurniture.IronAgeFurnitureConfiguration;
import zone.moddev.mc.ironagefurniture.Ironagefurniture;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ClassInheritanceMultiMap;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.ChunkDataEvent;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.event.FMLMissingMappingsEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

/** The six CFM 4.1.2 wooden chairs have like-for-like classic-chair replacements. */
public final class CfmChairMigration {
    private static final String CFM = "cfm";
    private static final Map<String, String> CHAIRS;

    static {
        Map<String, String> chairs = new LinkedHashMap<String, String>();
        chairs.put("cfm:chair_oak", "chair_wood_ironage_classic_oak");
        chairs.put("cfm:chair_spruce", "chair_wood_ironage_classic_spruce");
        chairs.put("cfm:chair_birch", "chair_wood_ironage_classic_birch");
        chairs.put("cfm:chair_jungle", "chair_wood_ironage_classic_jungle");
        chairs.put("cfm:chair_acacia", "chair_wood_ironage_classic_acacia");
        chairs.put("cfm:chair_dark_oak", "chair_wood_ironage_classic_big_oak");
        CHAIRS = Collections.unmodifiableMap(chairs);
    }

    private boolean warnedMissingTarget;

    public static Map<String, String> chairMappings() {
        return CHAIRS;
    }

    public static void remapMissingMappings(FMLMissingMappingsEvent event) {
        // Missing CFM mappings are always repaired, independent of the opt-in conversion.
        for (FMLMissingMappingsEvent.MissingMapping mapping : event.getAll()) {
            String targetPath = CHAIRS.get(mapping.resourceLocation.toString());
            if (targetPath == null) {
                continue;
            }

            Block target = registeredTarget(targetPath);
            if (target == null) {
                FMLLog.warning("[%s] Cannot remap missing %s: classic chairs are not registered",
                    Ironagefurniture.MODID, mapping.resourceLocation);
                continue;
            }

            if (mapping.type == GameRegistry.Type.BLOCK) {
                mapping.remap(target);
                FMLLog.info("[%s] Remapped missing CFM chair block %s to %s",
                    Ironagefurniture.MODID, mapping.resourceLocation, target.getRegistryName());
            } else if (mapping.type == GameRegistry.Type.ITEM) {
                Item item = Item.getItemFromBlock(target);
                if (item != null) {
                    mapping.remap(item);
                    FMLLog.info("[%s] Remapped missing CFM chair item %s to %s",
                        Ironagefurniture.MODID, mapping.resourceLocation, item.getRegistryName());
                }
            }
        }
    }

    @SubscribeEvent
    public void loadChunk(ChunkDataEvent.Load event) {
        World world = event.getWorld();
        if (world.isRemote) {
            return;
        }

        Chunk chunk = event.getChunk();
        if (!shouldForceConversion(Loader.isModLoaded(CFM),
                IronAgeFurnitureConfiguration.FORCE_CFM_CHAIR_CONVERSION)) {
            return;
        }

        Map<Block, Block> replacements = registeredBlockReplacements();
        if (replacements == null) {
            warnMissingTarget();
            return;
        }

        int blocks = migrateChunkBlocks(chunk, replacements);
        Map<String, String> itemIds = registeredItemIds();
        int items = migrateChunkInventories(chunk, itemIds);
        if (blocks + items > 0) {
            chunk.setModified(true);
            FMLLog.info("[%s] Converted %d CFM chairs and %d chair stacks in chunk %d,%d",
                Ironagefurniture.MODID, blocks, items, chunk.xPosition, chunk.zPosition);
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void convertPlacedChair(BlockEvent.PlaceEvent event) {
        World world = event.getWorld();
        if (world.isRemote || event.isCanceled() || !shouldForceConversion(Loader.isModLoaded(CFM),
                IronAgeFurnitureConfiguration.FORCE_CFM_CHAIR_CONVERSION)) {
            return;
        }
        IBlockState oldState = event.getPlacedBlock();
        Block oldBlock = oldState.getBlock();
        ResourceLocation oldId = oldBlock.getRegistryName();
        String targetPath = oldId == null ? null : CHAIRS.get(oldId.toString());
        Block target = targetPath == null ? null : registeredTarget(targetPath);
        if (target != null) {
            world.setBlockState(event.getPos(), target.getStateFromMeta(oldBlock.getMetaFromState(oldState)), 3);
        }
    }

    @SubscribeEvent
    public void playerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        EntityPlayer player = event.player;
        if (player.world.isRemote || !shouldForceConversion(Loader.isModLoaded(CFM),
                IronAgeFurnitureConfiguration.FORCE_CFM_CHAIR_CONVERSION)) {
            return;
        }
        Map<String, String> itemIds = registeredItemIds();
        migrateInventory(player.inventory, itemIds);
        migrateInventory(player.getInventoryEnderChest(), itemIds);
    }

    static boolean shouldForceConversion(boolean cfmLoaded, boolean optionEnabled) {
        return cfmLoaded && optionEnabled;
    }

    private static Block registeredTarget(String path) {
        ResourceLocation id = new ResourceLocation(Ironagefurniture.MODID, path);
        return Block.REGISTRY.containsKey(id) ? Block.REGISTRY.getObject(id) : null;
    }

    private Map<Block, Block> registeredBlockReplacements() {
        Map<Block, Block> replacements = new IdentityHashMap<Block, Block>();
        for (Map.Entry<String, String> entry : CHAIRS.entrySet()) {
            Block target = registeredTarget(entry.getValue());
            if (target == null) {
                return null;
            }
            ResourceLocation sourceId = new ResourceLocation(entry.getKey());
            if (Block.REGISTRY.containsKey(sourceId)) {
                replacements.put(Block.REGISTRY.getObject(sourceId), target);
            }
        }
        return replacements;
    }

    private static Map<String, String> registeredItemIds() {
        Map<String, String> ids = new LinkedHashMap<String, String>();
        for (Map.Entry<String, String> entry : CHAIRS.entrySet()) {
            Block target = registeredTarget(entry.getValue());
            Item item = target == null ? null : Item.getItemFromBlock(target);
            if (item != null && item.getRegistryName() != null) {
                ids.put(entry.getKey(), item.getRegistryName().toString());
            }
        }
        return ids;
    }

    static int migrateChunkBlocks(Chunk chunk, Map<Block, Block> replacements) {
        int converted = 0;
        for (ExtendedBlockStorage section : chunk.getBlockStorageArray()) {
            if (section == null) {
                continue;
            }
            converted += migrateSectionBlocks(section, replacements);
        }
        return converted;
    }

    static int migrateSectionBlocks(ExtendedBlockStorage section, Map<Block, Block> replacements) {
        int converted = 0;
        for (int y = 0; y < 16; ++y) {
            for (int z = 0; z < 16; ++z) {
                for (int x = 0; x < 16; ++x) {
                    IBlockState oldState = section.get(x, y, z);
                    Block oldBlock = oldState.getBlock();
                    Block target = replacements.get(oldBlock);
                    if (target != null) {
                        section.set(x, y, z, target.getStateFromMeta(oldBlock.getMetaFromState(oldState)));
                        ++converted;
                    }
                }
            }
        }
        return converted;
    }

    private static int migrateChunkInventories(Chunk chunk, Map<String, String> ids) {
        int converted = 0;
        for (TileEntity tile : chunk.getTileEntityMap().values()) {
            NBTTagCompound data = tile.writeToNBT(new NBTTagCompound());
            int changed = rewriteItemStacks(data, ids);
            if (changed > 0) {
                tile.readFromNBT(data);
                tile.markDirty();
                converted += changed;
            }
        }
        for (ClassInheritanceMultiMap<Entity> entities : chunk.getEntityLists()) {
            for (Entity entity : entities) {
                NBTTagCompound data = entity.serializeNBT();
                int changed = rewriteItemStacks(data, ids);
                if (changed > 0) {
                    entity.deserializeNBT(data);
                    converted += changed;
                }
            }
        }
        return converted;
    }

    private static void migrateInventory(IInventory inventory, Map<String, String> ids) {
        boolean changed = false;
        for (int slot = 0; slot < inventory.getSizeInventory(); ++slot) {
            ItemStack oldStack = inventory.getStackInSlot(slot);
            if (oldStack == null || oldStack.getItem().getRegistryName() == null) {
                continue;
            }
            String targetId = ids.get(oldStack.getItem().getRegistryName().toString());
            if (targetId == null) {
                continue;
            }
            Item target = Item.REGISTRY.getObject(new ResourceLocation(targetId));
            ItemStack replacement = new ItemStack(target, oldStack.stackSize, 0);
            if (oldStack.hasTagCompound()) {
                replacement.setTagCompound(oldStack.getTagCompound().copy());
            }
            inventory.setInventorySlotContents(slot, replacement);
            changed = true;
        }
        if (changed) {
            inventory.markDirty();
        }
    }

    static int rewriteItemStacks(NBTBase tag, Map<String, String> ids) {
        int converted = 0;
        if (tag instanceof NBTTagCompound) {
            NBTTagCompound compound = (NBTTagCompound)tag;
            if (compound.hasKey("id", 8) && compound.hasKey("Count", 99)) {
                String replacement = ids.get(compound.getString("id"));
                if (replacement != null) {
                    compound.setString("id", replacement);
                    compound.setShort("Damage", (short)0);
                    ++converted;
                }
            }
            for (String key : new ArrayList<String>(compound.getKeySet())) {
                NBTBase child = compound.getTag(key);
                if (child != null) {
                    converted += rewriteItemStacks(child, ids);
                }
            }
        } else if (tag instanceof NBTTagList) {
            NBTTagList list = (NBTTagList)tag;
            for (int i = 0; i < list.tagCount(); ++i) {
                converted += rewriteItemStacks(list.get(i), ids);
            }
        }
        return converted;
    }

    private void warnMissingTarget() {
        if (!warnedMissingTarget) {
            warnedMissingTarget = true;
            FMLLog.warning("[%s] CFM chair conversion is enabled, but classic chairs are disabled; no chunks will be rewritten",
                Ironagefurniture.MODID);
        }
    }
}
