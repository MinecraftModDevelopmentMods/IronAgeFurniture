package com.mcmoddev.ironagefurniture.api.Enumerations;

import net.minecraft.util.StringRepresentable;

public enum BenchType implements StringRepresentable {
	 SINGLE("single"),
     MIDDLE("middle"),
	 END("end"),
	 LEFT("left"),
	 RIGHT("right");
	
     private final String name;

     private BenchType(String name)
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
