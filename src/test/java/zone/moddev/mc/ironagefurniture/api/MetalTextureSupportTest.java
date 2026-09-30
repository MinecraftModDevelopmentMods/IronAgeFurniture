package zone.moddev.mc.ironagefurniture.api;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;

import org.junit.Test;

public class MetalTextureSupportTest {
	@Test
	public void onlyMetalsWithBundledBlockTexturesAreAvailable() throws Exception {
		File jar = Files.createTempFile("iaf-metal-textures", ".jar").toFile();
		try {
			try (JarOutputStream output = new JarOutputStream(new FileOutputStream(jar))) {
				output.putNextEntry(new JarEntry("assets/basemetals/textures/blocks/adamantine_block.png"));
				output.write(1);
				output.closeEntry();
			}
			assertTrue(MetalTextureSupport.hasBundledTexture(jar,
				"basemetals:blocks/adamantine_block"));
			assertFalse(MetalTextureSupport.hasBundledTexture(jar,
				"basemetals:blocks/antimony_block"));
			assertFalse(MetalTextureSupport.hasBundledTexture(jar,
				"basemetals:blocks/bismuth_block"));
			assertFalse(MetalTextureSupport.hasBundledTexture(jar,
				"basemetals:blocks/pewter_block"));
		} finally {
			jar.delete();
		}
	}

	@Test
	public void newerBuildCanProvideTheAdditionalMetals() throws Exception {
		File jar = Files.createTempFile("iaf-new-metal-textures", ".jar").toFile();
		try {
			try (JarOutputStream output = new JarOutputStream(new FileOutputStream(jar))) {
				for (String metal : new String[] { "antimony", "bismuth", "pewter" }) {
					output.putNextEntry(new JarEntry("assets/basemetals/textures/blocks/"
						+ metal + "_block.png"));
					output.write(1);
					output.closeEntry();
				}
			}
			for (String metal : new String[] { "antimony", "bismuth", "pewter" }) {
				assertTrue(MetalTextureSupport.hasBundledTexture(jar,
					"basemetals:blocks/" + metal + "_block"));
			}
		} finally {
			jar.delete();
		}
	}

	@Test
	public void explodedDevelopmentResourcesAreSupported() throws Exception {
		File directory = Files.createTempDirectory("iaf-metal-resources").toFile();
		File texture = new File(directory,
			"assets/basemetals/textures/blocks/adamantine_block.png");
		try {
			assertTrue(texture.getParentFile().mkdirs());
			Files.write(texture.toPath(), new byte[] { 1 });
			assertTrue(MetalTextureSupport.hasBundledTexture(directory,
				"basemetals:blocks/adamantine_block"));
		} finally {
			texture.delete();
			texture.getParentFile().delete();
			texture.getParentFile().getParentFile().delete();
			texture.getParentFile().getParentFile().getParentFile().delete();
			texture.getParentFile().getParentFile().getParentFile().getParentFile().delete();
			directory.delete();
		}
	}

	@Test
	public void rejectsMalformedTextureReferences() {
		assertFalse(MetalTextureSupport.hasBundledTexture(null,
			"basemetals:blocks/antimony_block"));
		assertFalse(MetalTextureSupport.hasBundledTexture(new File("missing-metals.jar"), null));
		assertFalse(MetalTextureSupport.hasBundledTexture(new File("missing-metals.jar"),
			"antimony_block"));
	}
}
