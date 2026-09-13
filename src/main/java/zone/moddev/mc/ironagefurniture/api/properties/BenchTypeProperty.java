package zone.moddev.mc.ironagefurniture.api.properties;

import zone.moddev.mc.ironagefurniture.api.enumerations.BenchType;

import java.util.Arrays;
import java.util.function.Predicate;

import net.minecraft.world.level.block.state.properties.EnumProperty;

public final class BenchTypeProperty {

   /**
    * Create a new PropertyDirection with all directions that match the given Predicate
    */
   public static EnumProperty<BenchType> create(String name, Predicate<BenchType> filter) {
      return EnumProperty.create(name, BenchType.class, filter);
   }

   public static EnumProperty<BenchType> create(String name, BenchType... values) {
      return EnumProperty.create(name, BenchType.class, value -> Arrays.asList(values).contains(value));
   }

   private BenchTypeProperty() {
   }
}
