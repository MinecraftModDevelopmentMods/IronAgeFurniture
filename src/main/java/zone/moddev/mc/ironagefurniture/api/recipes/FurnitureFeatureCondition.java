package zone.moddev.mc.ironagefurniture.api.recipes;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.neoforged.neoforge.common.conditions.ICondition;
import zone.moddev.mc.ironagefurniture.IronAgeFurnitureConfiguration;

/** Disabled furniture stays recoverable in saves but has no construction recipe. */
public record FurnitureFeatureCondition(String feature) implements ICondition {
    public static final MapCodec<FurnitureFeatureCondition> CODEC = Codec.STRING.fieldOf("feature")
            .xmap(FurnitureFeatureCondition::new, FurnitureFeatureCondition::feature);
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
    @Override public MapCodec<? extends ICondition> codec() { return CODEC; }
}
