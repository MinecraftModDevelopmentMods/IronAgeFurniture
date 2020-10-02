package com.mcmoddev.ironagefurniture.api.Enumerations;

import net.minecraft.util.IStringSerializable;

public enum BenchType implements IStringSerializable {
	 SINGLE("single"),
     MIDDLE("middle"),
	 END("end");
	
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
}
