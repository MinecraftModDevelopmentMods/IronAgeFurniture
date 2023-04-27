package com.mcmoddev.ironagefurniture.api.properties;

import com.google.common.collect.Lists;
import com.mcmoddev.ironagefurniture.api.enumerations.BenchType;

import java.util.Arrays;
import java.util.Collection;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import net.minecraft.world.level.block.state.properties.EnumProperty;

public class BenchTypeProperty extends EnumProperty<BenchType> {
   protected BenchTypeProperty(String name, Collection<BenchType> values) {
      super(name, BenchType.class, values);
   }

   /**
    * Create a new PropertyDirection with all directions that match the given Predicate
    */
   public static BenchTypeProperty create(String name, Predicate<BenchType> filter) {
      return create(name, Arrays.stream(BenchType.values()).filter(filter).collect(Collectors.toList()));
   }

   public static BenchTypeProperty create(String p_196962_0_, BenchType... p_196962_1_) {
      return create(p_196962_0_, Lists.newArrayList(p_196962_1_));
   }

   /**
    * Create a new PropertyDirection for the given direction values
    */
   public static BenchTypeProperty create(String name, Collection<BenchType> values) {
      return new BenchTypeProperty(name, values);
   }
}
