package com.mcmoddev.ironagefurniture.api.Enumerations;

import net.minecraft.util.StringRepresentable;

public enum PlacementPosition implements StringRepresentable {
	 WALL("wall"),
     FLOOR("floor");
	
     private final String name;

     private PlacementPosition(String name)
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
