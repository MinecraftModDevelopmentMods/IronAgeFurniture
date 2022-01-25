package com.mcmoddev.ironagefurniture.api.Enumerations;

import net.minecraft.util.StringRepresentable;

public enum LightType implements StringRepresentable {
	 TORCH("torch"),
     CANDLE("candle"),
     GLOWSTONE("glowstone"),
     CELESTIAL("celestial");
	
     private final String name;

     private LightType(String name)
     {
         this.name = name;
     }

     public String toString()
     {
         return this.name;
     }

     public String getName()
     {
         return this.name;
     }
     
     @Override
 	public String getSerializedName() {
 		return this.name;
 	}
}
