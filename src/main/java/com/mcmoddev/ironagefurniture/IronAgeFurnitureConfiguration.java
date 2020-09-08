package com.mcmoddev.ironagefurniture;

import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public class IronAgeFurnitureConfiguration
{
    public static class Client
    {
        public final ForgeConfigSpec.BooleanValue GENERATE_CLASSIC_CHAIRS;
        public final ForgeConfigSpec.BooleanValue GENERATE_SHIELD_CHAIRS;
    	public final ForgeConfigSpec.BooleanValue GENERATE_SHORT_STOOLS;
    	public final ForgeConfigSpec.BooleanValue GENERATE_TALL_STOOLS;
    	public final ForgeConfigSpec.BooleanValue CFM_CONVERSION_RECIPES;
    	public final ForgeConfigSpec.BooleanValue INTEGRATION_BIOMESOPLENTY;
    	public final ForgeConfigSpec.BooleanValue INTEGRATION_NATURA;
    	public final ForgeConfigSpec.BooleanValue INTEGRATION_FORESTRY;
    	public final ForgeConfigSpec.BooleanValue INTEGRATION_IMMERSIVEENGINEERING;
        
        Client(ForgeConfigSpec.Builder builder)
        {
            builder.comment("Client configuration settings").push("client");
            
            this.GENERATE_CLASSIC_CHAIRS = builder
                    .comment("Generate classic chairs.")
                    .translation("ironagefurniture.generation.generateClassicChairs")
                    .define("generateClassicChairs", true);
            
            this.GENERATE_SHIELD_CHAIRS = builder
                    .comment("Generate shield chairs.")
                    .translation("ironagefurniture.generation.generateShieldChairs")
                    .define("generateShieldChairs", true);
            
            this.GENERATE_SHORT_STOOLS = builder
                    .comment("Generate short stools.")
                    .translation("ironagefurniture.generation.generateShortStools")
                    .define("generateShortStools", true);
            
            this.GENERATE_TALL_STOOLS = builder
            		.comment("Generate tall stools.")
                    .translation("ironagefurniture.generation.generateTallStools")
                    .define("generateTallStools", true);
            
            this.CFM_CONVERSION_RECIPES = builder
                    .comment("Adds recipes to convert from CFM furniture to IAF furniture.")
                    .translation("ironagefurniture.recipes.cfmRecipes")
                    .define("cfmRecipes", true);
            
            this.INTEGRATION_BIOMESOPLENTY = builder
                    .comment("Integrate with Biomes O Plenty.")
                    .translation("ironagefurniture.integration.bopIntegration")
                    .define("bopIntegration", true);
            
            this.INTEGRATION_NATURA = builder
            		.comment("Integrate with Natura.")
                    .translation("ironagefurniture.integration.naturaIntegration")
                    .define("naturaIntegration", true);
            
            this.INTEGRATION_FORESTRY = builder
            		.comment("Integrate with Forestry.")
                    .translation("ironagefurniture.integration.forestryIntegration")
                    .define("forestryIntegration", true);
            
            this.INTEGRATION_IMMERSIVEENGINEERING = builder
            		.comment("Integrate with Immersive Engineering.")
                    .translation("ironagefurniture.integration.ieIntegration")
                    .define("ieIntegration", true);
            
            builder.pop();
        }
    }

    static final ForgeConfigSpec clientSpec;
    public static final IronAgeFurnitureConfiguration.Client CLIENT;

    static
    {
        final Pair<IronAgeFurnitureConfiguration.Client, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(IronAgeFurnitureConfiguration.Client::new);
        clientSpec = specPair.getRight();
        CLIENT = specPair.getLeft();
    }
}