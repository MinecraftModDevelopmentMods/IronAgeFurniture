package zone.moddev.mc.ironagefurniture.migration;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;
import zone.moddev.mc.ironagefurniture.Ironagefurniture;
import zone.moddev.mc.ironagefurniture.api.SconceMetalData;
import zone.moddev.mc.ironagefurniture.api.blocks.base.LightHolderSconce;
import zone.moddev.mc.ironagefurniture.api.enumerations.SconceMetal;

/** Converts the old metal property before the 1.16.5 block codec reads the palette. */
final class LegacySconceMigration {
    static void migrate(CompoundNBT level) {
        for (INBT entry : level.getList("Sections", 10)) {
            CompoundNBT section = (CompoundNBT) entry;
            for (INBT state : section.getList("Palette", 10)) flattenState((CompoundNBT) state);
        }
        for (INBT entry : level.getList("Entities", 10)) migrateEntity((CompoundNBT) entry);
    }

    private static void migrateEntity(CompoundNBT entity) {
        if ("minecraft:falling_block".equals(entity.getString("id"))) {
            flattenState(entity.getCompound("BlockState"));
        }
        for (INBT passenger : entity.getList("Passengers", 10)) migrateEntity((CompoundNBT) passenger);
    }

    static void flattenState(CompoundNBT state) {
        CompoundNBT properties = state.getCompound("Properties");
        if (!properties.contains("metal", 8)) return;
        ResourceLocation id = ResourceLocation.tryParse(state.getString("Name"));
        if (id == null || !id.getNamespace().equals(Ironagefurniture.MODID)
                || !id.getPath().startsWith("light_metal_ironage_sconce_")) return;
        SconceMetal metal = SconceMetalData.material(id.getPath());
        if (metal == SconceMetal.IRON) {
            String savedMetal = properties.getString("metal");
            metal = SconceMetal.byName(savedMetal);
            if (!metal.getName().equals(savedMetal)) return;
        }
        ResourceLocation target = new ResourceLocation(id.getNamespace(),
                SconceMetalData.blockPath(SconceMetalData.ironPath(id.getPath()), metal));
        if (!(ForgeRegistries.BLOCKS.getValue(target) instanceof LightHolderSconce)) return;
        state.putString("Name", target.toString());
        properties.remove("metal");
        if (properties.isEmpty()) state.remove("Properties");
    }

    private LegacySconceMigration() { }
}
