package zone.moddev.mc.ironagefurniture.compat;

import net.minecraftforge.fml.ModList;
import org.apache.maven.artifact.versioning.DefaultArtifactVersion;

/** Public-API helpers for optional integrations selected during mod construction. */
public final class OptionalModSupport
{
    private OptionalModSupport()
    {
    }

    public static boolean isLoadedAtLeast(String modId, String minimumVersion)
    {
        DefaultArtifactVersion minimum = new DefaultArtifactVersion(minimumVersion);
        return ModList.get().getModContainerById(modId)
                .map(container -> container.getModInfo().getVersion())
                .map(installed -> installed.compareTo(minimum) >= 0)
                .orElse(false);
    }
}
