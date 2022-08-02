package com.mcmoddev.ironagefurniture.Items;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

public class LavaLampItem extends BlockItem {

	public LavaLampItem(Block p_40565_, Properties p_40566_) {
		super(p_40565_, p_40566_);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Entity createEntity(Level level, Entity location, ItemStack stack) {
		// TODO Auto-generated method stub
		return super.createEntity(level, location, stack);
	}
	
	@Override
	public boolean onDroppedByPlayer(ItemStack item, Player player) {
		
		
		// TODO Auto-generated method stub
		return super.onDroppedByPlayer(item, player);
	}
}
