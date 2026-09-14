package zone.moddev.mc.ironagefurniture.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import zone.moddev.mc.ironagefurniture.Ironagefurniture;
import zone.moddev.mc.ironagefurniture.ModItems;

public final class ModCreativeTab {
    public static final DeferredRegister<CreativeModeTab> REGISTER =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Ironagefurniture.MODID);

    public static final RegistryObject<CreativeModeTab> FURNITURE = REGISTER.register("creative_tab", () ->
            CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup." + Ironagefurniture.MODID))
                    .icon(() -> new ItemStack(ModVanillaChairs.chair_wood_ironage_classic_oak.get()))
                    .displayItems((parameters, output) ->
                            ModItems.REGISTER.getEntries().forEach(item -> output.accept(item.get())))
                    .build());

    private ModCreativeTab() {
    }
}
