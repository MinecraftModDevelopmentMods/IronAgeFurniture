package zone.moddev.mc.ironagefurniture.fixture;

import java.io.File;
import java.lang.reflect.Field;
import net.minecraft.server.players.PlayerList;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.storage.LevelResource;
import net.minecraft.world.level.storage.PlayerDataStorage;

/** Accesses the real save reader so upgrade tests exercise vanilla datafixing. */
final class ProbePlayerStorage {
    private ProbePlayerStorage() { }
    static PlayerDataStorage storage(ServerLevel world) {
        try {
            for (Field field : PlayerList.class.getDeclaredFields()) {
                if (field.getType() != PlayerDataStorage.class) continue;
                field.setAccessible(true);
                return (PlayerDataStorage)field.get(world.getServer().getPlayerList());
            }
            throw new IllegalStateException("Cannot locate the server's player storage");
        } catch (ReflectiveOperationException failure) { throw new IllegalStateException(failure); }
    }
    static File folder(ServerLevel world) {
        return world.getServer().getWorldPath(LevelResource.LEVEL_DATA_FILE).getParent().toFile();
    }
}
