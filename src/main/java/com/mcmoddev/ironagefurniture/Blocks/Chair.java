package com.mcmoddev.ironagefurniture.Blocks;

import com.mcmoddev.ironagefurniture.Ironagefurniture;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

public class Chair extends Block {

	public Chair(Material materialIn, String name, float resistance) {
		super(materialIn);
		
		if (materialIn == Material.ROCK) {	
			this.setSoundType(SoundType.STONE);
			this.setHarvestLevel("pickaxe", 0);
		}
		
		if (materialIn == Material.WOOD) {	
			this.setSoundType(SoundType.WOOD);
			this.setHarvestLevel("axe", 0);
		}
		
		if (materialIn == Material.IRON) {	
			this.setSoundType(SoundType.METAL);
			this.setHarvestLevel("pickaxe", 1);
		}
		
		this.blockResistance = resistance;
		this.setCreativeTab(Ironagefurniture.ironagefurnitureTab);
		
		// TODO Auto-generated constructor stub
	}

}
