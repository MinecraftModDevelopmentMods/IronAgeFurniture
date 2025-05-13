package com.mcmoddev.ironagefurniture.client.renderer;

import com.mcmoddev.ironagefurniture.Ironagefurniture;
import com.mcmoddev.ironagefurniture.ModVanillaChairs;
import com.mcmoddev.ironagefurniture.api.entity.Entities;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.FoliageColor;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.CreativeModeTabEvent;

public class ClientHandler {
	 public static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event)
	    {
	        event.registerEntityRenderer(Entities.SEAT.get(), SeatRenderer::new);
	    }
	 
	 public static void onRegisterCreativeTab(CreativeModeTabEvent.Register event) {
	        event.registerCreativeModeTab(new ResourceLocation(Ironagefurniture.MODID, "creative_tab"), builder -> {
	            builder.title(Component.translatable("itemGroup." + Ironagefurniture.MODID))
	                   .icon(() -> new ItemStack(ModVanillaChairs.chair_wood_ironage_classic_oak.get()))
	                   .displayItems((parameters, output) -> {
	                       // Add all chair items to the creative tab
	                       output.accept(ModVanillaChairs.chair_wood_ironage_classic_oak.get().asItem());
//	                       output.accept(ModVanillaChairs.chair_wood_ironage_classic_acacia.get().asItem());
//	                       output.accept(ModVanillaChairs.chair_wood_ironage_classic_dark_oak.get().asItem());
//	                       output.accept(ModVanillaChairs.chair_wood_ironage_classic_birch.get().asItem());
//	                       output.accept(ModVanillaChairs.chair_wood_ironage_classic_jungle.get().asItem());
//	                       output.accept(ModVanillaChairs.chair_wood_ironage_classic_spruce.get().asItem());
	                   });
	        });
	    }
}
