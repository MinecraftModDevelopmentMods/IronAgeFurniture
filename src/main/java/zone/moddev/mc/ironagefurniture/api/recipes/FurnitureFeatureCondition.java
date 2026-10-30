package zone.moddev.mc.ironagefurniture.api.recipes;

import net.minecraftforge.common.crafting.conditions.ICondition;
import zone.moddev.mc.ironagefurniture.IronAgeFurnitureConfiguration;

/** Disabled furniture stays recoverable in saves but has no construction recipe. */
public record FurnitureFeatureCondition(String feature) implements ICondition {
    @Override public boolean test(IContext context) {
        var setting = switch (feature) {
            case "classicChairs" -> IronAgeFurnitureConfiguration.CLASSIC_CHAIRS;
            case "shieldChairs" -> IronAgeFurnitureConfiguration.SHIELD_CHAIRS;
            case "stools" -> IronAgeFurnitureConfiguration.STOOLS;
            case "benches" -> IronAgeFurnitureConfiguration.BENCHES;
            case "wingbacks" -> IronAgeFurnitureConfiguration.WINGBACKS;
            case "thrones" -> IronAgeFurnitureConfiguration.THRONES;
            case "woodenBeds" -> IronAgeFurnitureConfiguration.WOOD_BEDS;
            case "canopyBeds" -> IronAgeFurnitureConfiguration.CANOPY_BEDS;
            case "lights" -> IronAgeFurnitureConfiguration.LIGHTS;
            default -> null;
        };
        return setting != null && IronAgeFurnitureConfiguration.enabled(setting);
    }
    @Override public net.minecraft.resources.ResourceLocation getID() { return SERIALIZER.getID(); }
    public static final net.minecraftforge.common.crafting.conditions.IConditionSerializer<FurnitureFeatureCondition> SERIALIZER =
            new net.minecraftforge.common.crafting.conditions.IConditionSerializer<>() {
        @Override public void write(com.google.gson.JsonObject json, FurnitureFeatureCondition value) { json.addProperty("feature", value.feature()); }
        @Override public FurnitureFeatureCondition read(com.google.gson.JsonObject json) { return new FurnitureFeatureCondition(net.minecraft.util.GsonHelper.getAsString(json, "feature")); }
        @Override public net.minecraft.resources.ResourceLocation getID() { return new net.minecraft.resources.ResourceLocation("ironagefurniture", "feature_enabled"); }
    };
}
