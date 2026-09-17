package zone.moddev.mc.ironagefurniture;

import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;

@EventBusSubscriber(modid = Ironagefurniture.MODID, bus = EventBusSubscriber.Bus.MOD)
public class IronAgeFurnitureConfiguration
{
	private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();
	
//	private static final ModConfigSpec.BooleanValue GENERATE_CLASSIC_CHAIRS = BUILDER
//            .comment("Generate classic chairs.")
//            .translation("ironagefurniture.generation.generateClassicChairs")
//            .define("generateClassicChairs", true);
//	
//	private static final ModConfigSpec.BooleanValue GENERATE_SHIELD_CHAIRS = BUILDER
//	        .comment("Generate shield chairs.")
//	        .translation("ironagefurniture.generation.generateShieldChairs")
//	        .define("generateShieldChairs", true);
//
//	private static final ModConfigSpec.BooleanValue GENERATE_SHORT_STOOLS = BUILDER
//	        .comment("Generate short stools.")
//	        .translation("ironagefurniture.generation.generateShortStools")
//	        .define("generateShortStools", true);
//
//	private static final ModConfigSpec.BooleanValue GENERATE_TALL_STOOLS = BUILDER
//	        .comment("Generate tall stools.")
//	        .translation("ironagefurniture.generation.generateTallStools")
//	        .define("generateTallStools", true);
//
//	private static final ModConfigSpec.BooleanValue GENERATE_BENCHES = BUILDER
//	        .comment("Generate benches.")
//	        .translation("ironagefurniture.generation.generateBenches")
//	        .define("generateBenches", true);
//
//	private static final ModConfigSpec.BooleanValue INTEGRATION_BIOMESOPLENTY = BUILDER
//	        .comment("Integrate with Biomes O Plenty.")
//	        .translation("ironagefurniture.integration.bopIntegration")
//	        .define("bopIntegration", true);
//
//	private static final ModConfigSpec.BooleanValue INTEGRATION_BIOMESYOUGO = BUILDER
//	        .comment("Integrate with Oh The Biomes You Go.")
//	        .translation("ironagefurniture.integration.bygIntegration")
//	        .define("bygIntegration", true);
//
//	private static final ModConfigSpec.BooleanValue INTEGRATION_IMMERSIVEENGINEERING = BUILDER
//	        .comment("Integrate with Immersive Engineering.")
//	        .translation("ironagefurniture.integration.ieIntegration")
//	        .define("ieIntegration", true);

	static final ModConfigSpec SPEC = BUILDER.build();
	
//	public static boolean generateClassicChairs;
//	public static boolean generateShieldChairs;
//	public static boolean generateShortStools;
//	public static boolean generateTallStools;
//	public static boolean generateBenches;
//	public static boolean bopIntegration;
//	public static boolean bygIntegration;
//	public static boolean ieIntegration;
	

	@SubscribeEvent
    static void onLoad(final ModConfigEvent event)
    {
//		generateClassicChairs = GENERATE_CLASSIC_CHAIRS.get();
//		generateShieldChairs = GENERATE_SHIELD_CHAIRS.get();
//		generateShortStools = GENERATE_SHORT_STOOLS.get();
//		generateTallStools = GENERATE_TALL_STOOLS.get();
//		generateBenches = GENERATE_BENCHES.get();
//		bopIntegration = INTEGRATION_BIOMESOPLENTY.get();
//		bygIntegration = INTEGRATION_BIOMESYOUGO.get();
//		ieIntegration = INTEGRATION_IMMERSIVEENGINEERING.get();

    }
}
