package zone.moddev.mc.ironagefurniture.api;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class PowerAdvantageFluidCompatTest {
	private static final String API = "cyano/poweradvantage/api/fluid/FluidNetworkApi.class";
	private static final String[] LEGACY = {
			"cyano/poweradvantage/api/fluid/FluidConduitBlock.class",
			"cyano/poweradvantage/conduitnetwork/ConduitRegistry.class",
			"cyano/poweradvantage/init/Fluids.class",
			"cyano/poweradvantage/api/ConduitType.class",
			"cyano/poweradvantage/api/PowerRequest.class",
			"cyano/poweradvantage/api/IPowerMachine.class"
	};

	@Rule
	public final TemporaryFolder temporaryFolder = new TemporaryFolder();

	@Test
	public void detectsApiInJar() throws Exception {
		assertEquals(PowerAdvantageFluidCompat.Backend.API, detectJar(API));
	}

	@Test
	public void detectsCompleteLegacySurfaceInJar() throws Exception {
		assertEquals(PowerAdvantageFluidCompat.Backend.LEGACY, detectJar(LEGACY));
	}

	@Test
	public void apiWinsWhenBothSurfacesArePresent() throws Exception {
		String[] both = new String[LEGACY.length + 1];
		both[0] = API;
		System.arraycopy(LEGACY, 0, both, 1, LEGACY.length);
		assertEquals(PowerAdvantageFluidCompat.Backend.API, detectJar(both));
	}

	@Test
	public void incompleteLegacySurfaceIsUnsupported() throws Exception {
		assertEquals(PowerAdvantageFluidCompat.Backend.DISABLED,
				detectJar("cyano/poweradvantage/conduitnetwork/ConduitRegistry.class"));
	}

	@Test
	public void detectsDirectorySurfaces() throws Exception {
		File apiDirectory = temporaryFolder.newFolder("api-directory");
		writeClass(apiDirectory, API);
		assertEquals(PowerAdvantageFluidCompat.Backend.API,
				PowerAdvantageFluidCompat.detectBackend(apiDirectory));

		File legacyDirectory = temporaryFolder.newFolder("legacy-directory");
		for (String entry : LEGACY) {
			writeClass(legacyDirectory, entry);
		}
		assertEquals(PowerAdvantageFluidCompat.Backend.LEGACY,
				PowerAdvantageFluidCompat.detectBackend(legacyDirectory));
	}

	@Test
	public void malformedJarIsUnsupported() throws Exception {
		File malformed = temporaryFolder.newFile("malformed.jar");
		Files.write(malformed.toPath(), "not a jar".getBytes(StandardCharsets.UTF_8));
		assertEquals(PowerAdvantageFluidCompat.Backend.DISABLED,
				PowerAdvantageFluidCompat.detectBackend(malformed));
	}

	private PowerAdvantageFluidCompat.Backend detectJar(String... entries) throws Exception {
		File jar = temporaryFolder.newFile("surface-" + System.nanoTime() + ".jar");
		try (JarOutputStream output = new JarOutputStream(new FileOutputStream(jar))) {
			for (String entry : entries) {
				output.putNextEntry(new JarEntry(entry));
				output.write(0);
				output.closeEntry();
			}
		}
		return PowerAdvantageFluidCompat.detectBackend(jar);
	}

	private static void writeClass(File root, String entry) throws Exception {
		File output = new File(root, entry);
		Files.createDirectories(output.toPath().getParent());
		Files.write(output.toPath(), new byte[] { 0 });
	}
}
