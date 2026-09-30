package zone.moddev.mc.ironagefurniture.api;

import java.io.File;
import java.io.IOException;
import java.util.zip.ZipFile;

final class MetalTextureSupport {
	private MetalTextureSupport() {
	}

	static boolean hasBundledTexture(File modSource, String texture) {
		if (modSource == null || texture == null) {
			return false;
		}

		int separator = texture.indexOf(':');
		if (separator <= 0 || separator == texture.length() - 1) {
			return false;
		}

		String path = "assets/" + texture.substring(0, separator) + "/textures/"
				+ texture.substring(separator + 1) + ".png";
		if (modSource.isDirectory()) {
			return new File(modSource, path).isFile();
		}
		if (!modSource.isFile()) {
			return false;
		}
		try (ZipFile jar = new ZipFile(modSource)) {
			return jar.getEntry(path) != null;
		} catch (IOException ignored) {
			return false;
		}
	}
}
