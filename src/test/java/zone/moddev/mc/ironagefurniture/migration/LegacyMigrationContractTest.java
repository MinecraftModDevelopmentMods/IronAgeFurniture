package zone.moddev.mc.ironagefurniture.migration;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import org.junit.Test;

public class LegacyMigrationContractTest {
	@Test
	public void coremodRunsBeforeAndAfterVanillaFlattening() throws Exception {
		String transformer = text("src/main/resources/coremods/ironagefurniture_legacy_world_fix.js");
		assertTrue(transformer.contains("net.minecraft.world.chunk.storage.ChunkLoader"));
		assertTrue(transformer.contains("prepareLegacyChunk"));
		assertTrue(transformer.contains("finalizeLegacyChunk"));
		assertTrue(new File("src/main/resources/META-INF/coremods.json").isFile());
	}

	@Test
	public void migrationCoversBlocksPlayerStorageContainersAndDroppedItems() throws Exception {
		String hook = text("src/main/java/zone/moddev/mc/ironagefurniture/migration/LegacyWorldDataHook.java");
		String events = text("src/main/java/zone/moddev/mc/ironagefurniture/migration/LegacyMigrationEvents.java");
		String items = text("src/main/java/zone/moddev/mc/ironagefurniture/migration/LegacyPaddedItemMigration.java");
		assertTrue(hook.contains("BlockStateFlatteningMap"));
		assertTrue(hook.contains("ensurePaddedBenchTileEntities"));
		assertTrue(hook.contains("migrateChunkContents"));
		assertTrue(hook.contains("ironagefurniture_legacy_registry.dat"));
		assertTrue(events.contains("PlayerLoggedInEvent"));
		assertTrue(events.contains("getInventoryEnderChest"));
		assertTrue(events.contains("EntityJoinWorldEvent"));
		assertTrue(!events.contains("ChunkEvent.Load"));
		assertTrue(items.contains("migrateNested"));
	}

	private static String text(String path) throws Exception {
		return new String(Files.readAllBytes(new File(path).toPath()), StandardCharsets.UTF_8);
	}
}
