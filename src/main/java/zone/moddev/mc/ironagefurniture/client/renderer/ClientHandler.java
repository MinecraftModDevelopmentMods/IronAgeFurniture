package zone.moddev.mc.ironagefurniture.client.renderer;

import zone.moddev.mc.ironagefurniture.Ironagefurniture;
import zone.moddev.mc.ironagefurniture.ModItems;
import zone.moddev.mc.ironagefurniture.api.entity.Entities;
import zone.moddev.mc.ironagefurniture.init.ModVanillaChairs;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.event.EntityRenderersEvent;
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
		                   ModItems.REGISTER.getEntries().forEach(item -> output.accept(item.get()));
		               });
		    });
		}

}
