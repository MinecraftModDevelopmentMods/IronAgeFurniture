package zone.moddev.mc.ironagefurniture;

import net.neoforged.neoforge.common.ModConfigSpec;

/** Server choices affect availability, never the identities stored in a world. */
public final class IronAgeFurnitureConfiguration {
    public static final ModConfigSpec SYNCED_SPEC;
    public static final ModConfigSpec.BooleanValue CLASSIC_CHAIRS, SHIELD_CHAIRS, STOOLS, BENCHES;
    public static final ModConfigSpec.BooleanValue WINGBACKS, THRONES, WOOD_BEDS, CANOPY_BEDS, LIGHTS;
    public static final ModConfigSpec.BooleanValue BASE_METALS, MINERALOGY, FORCE_CFM_CONVERSION;

    static {
        ModConfigSpec.Builder server = new ModConfigSpec.Builder();
        server.comment("Choose the furniture available for crafting. Existing furniture is kept.").push("furniture");
        CLASSIC_CHAIRS = server.define("classicChairs", true);
        SHIELD_CHAIRS = server.define("shieldChairs", true);
        STOOLS = server.define("stools", true);
        BENCHES = server.define("benches", true);
        WINGBACKS = server.define("wingbacks", true);
        THRONES = server.define("thrones", true);
        WOOD_BEDS = server.define("woodenBeds", true);
        CANOPY_BEDS = server.define("canopyBeds", true);
        LIGHTS = server.define("lights", true);
        server.pop().push("integration");
        BASE_METALS = server.comment("Use installed Base Metals materials for additional sconces.").define("baseMetals", true);
        MINERALOGY = server.comment("Allow Mineralogy rock-salt lamps in sconces.").define("mineralogy", true);
        FORCE_CFM_CONVERSION = server.comment("Convert supported old Crayfish chairs even while their provider is installed. Back up the world first.")
                .define("forceCfmChairConversion", false);
        server.pop();
        SYNCED_SPEC = server.build();
    }

    public static boolean enabled(ModConfigSpec.BooleanValue value) {
        // Registry setup runs before a world loads its synchronised settings.
        return SYNCED_SPEC.isLoaded() ? value.get() : value.getDefault();
    }
    private IronAgeFurnitureConfiguration() { }
}
