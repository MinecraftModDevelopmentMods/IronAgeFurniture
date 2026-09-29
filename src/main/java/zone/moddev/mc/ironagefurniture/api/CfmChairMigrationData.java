package zone.moddev.mc.ironagefurniture.api;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.WorldSavedData;

/** Remembers legacy numeric IDs until even unopened chunks have been upgraded. */
public final class CfmChairMigrationData extends WorldSavedData {
    public static final String NAME = "ironagefurniture_cfm_chair_migration";
    private final Map<Integer, String> blockIds = new LinkedHashMap<Integer, String>();

    public CfmChairMigrationData(String name) {
        super(name);
    }

    public Map<Integer, String> getBlockIds() {
        return Collections.unmodifiableMap(blockIds);
    }

    public void setBlockIds(Map<Integer, String> values) {
        blockIds.clear();
        blockIds.putAll(values);
        markDirty();
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        blockIds.clear();
        NBTTagList entries = compound.getTagList("BlockIds", 10);
        for (int i = 0; i < entries.tagCount(); ++i) {
            NBTTagCompound entry = entries.getCompoundTagAt(i);
            if (entry.hasKey("Id", 99) && entry.hasKey("Target", 8)) {
                blockIds.put(entry.getInteger("Id"), entry.getString("Target"));
            }
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        NBTTagList entries = new NBTTagList();
        for (Map.Entry<Integer, String> value : blockIds.entrySet()) {
            NBTTagCompound entry = new NBTTagCompound();
            entry.setInteger("Id", value.getKey());
            entry.setString("Target", value.getValue());
            entries.appendTag(entry);
        }
        compound.setTag("BlockIds", entries);
        return compound;
    }
}
