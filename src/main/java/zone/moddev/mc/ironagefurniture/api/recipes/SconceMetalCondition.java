package zone.moddev.mc.ironagefurniture.api.recipes;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.common.crafting.conditions.IConditionSerializer;
import zone.moddev.mc.ironagefurniture.api.SconceMetalData;
import zone.moddev.mc.ironagefurniture.api.enumerations.SconceMetal;

/** Hide a metal recipe unless its supplying mod, tags and configuration agree. */
public final class SconceMetalCondition implements ICondition {
    public static final ResourceLocation ID = new ResourceLocation("ironagefurniture", "metal_available");
    private final SconceMetal metal;
    public SconceMetalCondition(SconceMetal metal) { this.metal = metal; }
    @Override public ResourceLocation getID() { return ID; }
    @Override public boolean test() { return SconceMetalData.available(metal); }
    public static final class Serializer implements IConditionSerializer<SconceMetalCondition> {
        @Override public ResourceLocation getID() { return ID; }
        @Override public void write(JsonObject json, SconceMetalCondition condition) {
            json.addProperty("metal", condition.metal.getName());
        }
        @Override public SconceMetalCondition read(JsonObject json) {
            String name = json.get("metal").getAsString();
            SconceMetal metal = SconceMetal.byName(name);
            if (!metal.getName().equals(name)) throw new JsonParseException("Unknown sconce metal: " + name);
            return new SconceMetalCondition(metal);
        }
    }
}
