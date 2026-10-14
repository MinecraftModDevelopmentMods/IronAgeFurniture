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
		assertTrue(transformer.contains("ironagefurniture_legacy_player_files"));
		assertTrue(transformer.contains("ironagefurniture_legacy_level_data"));
		assertTrue(transformer.contains("net.minecraft.world.storage.PlayerData"));
		assertTrue(transformer.contains("prepareLevelPlayerData"));
		assertTrue(transformer.contains("preparePlayerData"));
		assertTrue(transformer.contains("Cannot locate raw player data readers"));
		assertTrue(transformer.contains("Type.getArgumentTypes(method.desc)"));
		assertTrue(transformer.contains("Cannot locate Forge 36 world registry reader"));
		assertTrue(transformer.contains("Cannot locate Forge 36 chunk updater"));
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
		assertTrue(events.contains("getEnderChestInventory"));
		assertTrue(hook.contains("iafbygaddon") || items.contains("isLegacyNamespace"));
		assertTrue(events.contains("EntityJoinWorldEvent"));
		assertTrue(!events.contains("ChunkEvent.Load"));
		assertTrue(items.contains("migrateNested"));
	}

	private static String text(String path) throws Exception {
		return new String(Files.readAllBytes(new File(path).toPath()), StandardCharsets.UTF_8);
	}
}
