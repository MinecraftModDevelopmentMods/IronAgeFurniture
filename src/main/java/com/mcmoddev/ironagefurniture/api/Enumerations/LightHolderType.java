package com.mcmoddev.ironagefurniture.api.Enumerations;

import net.minecraft.util.StringRepresentable;

public enum LightHolderType implements StringRepresentable {
	 SCONCE("sconce"),
     CANDLESTICK("candlestick");
	
     private final String name;

     private LightHolderType(String name)
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
