package zone.moddev.mc.ironagefurniture;

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
        public final ForgeConfigSpec.BooleanValue GENERATE_WINGBACK_CHAIRS;
        public final ForgeConfigSpec.BooleanValue GENERATE_THRONES;
        public final ForgeConfigSpec.BooleanValue GENERATE_WOOD_BEDS;
        public final ForgeConfigSpec.BooleanValue GENERATE_CANOPY_BEDS;
    	public final ForgeConfigSpec.BooleanValue INTEGRATION_BIOMESOPLENTY;
    	public final ForgeConfigSpec.BooleanValue INTEGRATION_IMMERSIVEENGINEERING;
        public final ForgeConfigSpec.BooleanValue INTEGRATION_MINERALOGY;
        public final ForgeConfigSpec.BooleanValue FORCE_CFM_CHAIR_CONVERSION;
        
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

            this.GENERATE_WINGBACK_CHAIRS = builder.comment("Add upholstered wingback chairs.")
                    .translation("ironagefurniture.generation.generateWingbackChairs").define("generateWingbackChairs", true);
            this.GENERATE_THRONES = builder.comment("Add tall upholstered thrones.")
                    .translation("ironagefurniture.generation.generateThrones").define("generateThrones", true);
            this.GENERATE_WOOD_BEDS = builder.comment("Add single and double wooden beds.")
                    .translation("ironagefurniture.generation.generateWoodBeds").define("generateWoodBeds", true);
            this.GENERATE_CANOPY_BEDS = builder.comment("Add single and double canopy beds.")
                    .translation("ironagefurniture.generation.generateCanopyBeds").define("generateCanopyBeds", true);
            
            this.INTEGRATION_BIOMESOPLENTY = builder
                    .comment("Integrate with Biomes O Plenty.")
                    .translation("ironagefurniture.integration.bopIntegration")
                    .define("bopIntegration", true);
            
            this.INTEGRATION_IMMERSIVEENGINEERING = builder
            		.comment("Integrate with Immersive Engineering.")
                    .translation("ironagefurniture.integration.ieIntegration")
                    .define("ieIntegration", true);
            this.INTEGRATION_MINERALOGY = builder.comment("Allow Mineralogy rock-salt lamps in sconces.")
                    .translation("ironagefurniture.integration.mineralogyIntegration").define("mineralogyIntegration", true);
            this.FORCE_CFM_CHAIR_CONVERSION = builder.comment(
                    "Replace supported Crayfish wooden chairs with IAF chairs, even while CFM is installed. Back up the world first.")
                    .translation("ironagefurniture.integration.forceCfmChairConversion").define("forceCfmChairConversion", false);
            
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
