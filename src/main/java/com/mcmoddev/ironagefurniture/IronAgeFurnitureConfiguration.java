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
    	public final ForgeConfigSpec.BooleanValue GENERATE_BENCHES;
    	public final ForgeConfigSpec.BooleanValue INTEGRATION_BIOMESOPLENTY;
    	public final ForgeConfigSpec.BooleanValue INTEGRATION_BIOMESYOUGO;
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
            
            this.GENERATE_BENCHES = builder
            		.comment("Generate benches.")
                    .translation("ironagefurniture.generation.generateBenches")
                    .define("generateBenches", true);
            
            this.INTEGRATION_BIOMESOPLENTY = builder
                    .comment("Integrate with Biomes O Plenty.")
                    .translation("ironagefurniture.integration.bopIntegration")
                    .define("bopIntegration", true);
            
            this.INTEGRATION_BIOMESYOUGO = builder
                    .comment("Integrate with Oh The Biomes You Go.")
                    .translation("ironagefurniture.integration.bygIntegration")
                    .define("bygIntegration", true);
            
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