package zone.moddev.mc.ironagefurniture.api.Enumerations;

import net.minecraft.util.IStringSerializable;

public enum BenchType implements IStringSerializable {
	 SINGLE("single"),
     MIDDLE("middle"),
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
}
