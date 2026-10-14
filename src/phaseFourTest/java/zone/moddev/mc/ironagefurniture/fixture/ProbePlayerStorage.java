package zone.moddev.mc.ironagefurniture.fixture;

import java.io.File;
import java.lang.reflect.Field;
import net.minecraft.server.management.PlayerList;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.FolderName;
import net.minecraft.world.storage.PlayerData;

/** Accesses the real save reader so upgrade tests exercise vanilla datafixing. */
final class ProbePlayerStorage {
    private ProbePlayerStorage() { }
    static PlayerData storage(ServerWorld world) {
        try {
            for (Field field : PlayerList.class.getDeclaredFields()) {
                if (field.getType() != PlayerData.class) continue;
                field.setAccessible(true);
                return (PlayerData)field.get(world.getServer().getPlayerList());
            }
            throw new IllegalStateException("Cannot locate the server's player storage");
        } catch (ReflectiveOperationException failure) { throw new IllegalStateException(failure); }
    }
    static File folder(ServerWorld world) {
        return world.getServer().getWorldPath(FolderName.LEVEL_DATA_FILE).getParent().toFile();
    }
}
