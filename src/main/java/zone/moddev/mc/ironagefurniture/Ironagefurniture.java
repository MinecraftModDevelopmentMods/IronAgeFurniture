package zone.moddev.mc.ironagefurniture;

import java.util.HashMap;
import java.util.Map;

import zone.moddev.mc.ironagefurniture.api.entity.Seat;
import zone.moddev.mc.ironagefurniture.api.CreativeModeBreakTracker;
import zone.moddev.mc.ironagefurniture.api.entity.EntityThrownLavaLamp;
import zone.moddev.mc.ironagefurniture.api.entity.EntityReleasedLavaLamp;
import zone.moddev.mc.ironagefurniture.api.CfmChairMigration;
import zone.moddev.mc.ironagefurniture.api.tile.TileEntityMetalVariant;
import zone.moddev.mc.ironagefurniture.api.tile.TileEntityShieldChair;
import zone.moddev.mc.ironagefurniture.api.tile.TileEntityUpholstery;
import zone.moddev.mc.ironagefurniture.client.resources.GeneratedModelResourcePack;
import zone.moddev.mc.ironagefurniture.api.tile.TileEntityPaddedBench;
import zone.moddev.mc.ironagefurniture.init.BlockInitialiser;
import zone.moddev.mc.ironagefurniture.init.ClientItemInitialiser;
import zone.moddev.mc.ironagefurniture.init.ClientModelInitialiser;
import zone.moddev.mc.ironagefurniture.init.ClientRenderInitialiser;
import zone.moddev.mc.ironagefurniture.init.RecipeInitialiser;
import zone.moddev.mc.ironagefurniture.init.PhaseFourFurnitureInitialiser;
import zone.moddev.mc.ironagefurniture.init.PhaseFourLightingRecipes;
import zone.moddev.mc.ironagefurniture.init.ItemInitialiser;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLMissingMappingsEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;

@Mod(modid = Ironagefurniture.MODID, version = Ironagefurniture.VERSION)
public class Ironagefurniture
{
    public static final String MODID = "ironagefurniture";
    public static final String VERSION = "0.4.0.110021";

	public static final Map<String,Block> BlockRegistry = new HashMap<String, Block>();
	public static final Map<String,Item> ItemRegistry = new HashMap<String, Item>();
	private final CfmChairMigration cfmChairMigration = new CfmChairMigration();

    public static CreativeTabs ironagefurnitureTab = new CreativeTabs("ironagefurnitureTab"){
		@Override
		public Item getTabIconItem(){
			return Item.getItemFromBlock(BlockObjectHolder.chair_wood_ironage_classic_big_oak);
		}

		public boolean hasSearchBar() {
			return true;
		};
	};


    @EventHandler
    public void init(FMLInitializationEvent event)
    {
		if (event.getSide().isClient()) {
			ClientItemInitialiser.registerItemRenders();
		}

	EntityRegistry.registerModEntity(Seat.class, MODID + ":seat", 0, this, 80, 1, false);
		EntityRegistry.registerModEntity(EntityThrownLavaLamp.class,
				MODID + ":thrown_lava_lamp", 1, this, 64, 10, true);
		EntityRegistry.registerModEntity(EntityReleasedLavaLamp.class,
				MODID + ":released_lava_lamp", 2, this, 160, 20, true);

    	RecipeInitialiser.init();
		PhaseFourFurnitureInitialiser.registerRecipes();
		PhaseFourLightingRecipes.register();
    }

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
	IronAgeFurnitureConfiguration.init(event);
		MinecraftForge.EVENT_BUS.register(new CreativeModeBreakTracker());
		MinecraftForge.EVENT_BUS.register(cfmChairMigration);
		FMLCommonHandler.instance().bus().register(cfmChairMigration);
		GameRegistry.registerTileEntity(TileEntityPaddedBench.class, MODID + ":padded_bench_colour");
		GameRegistry.registerTileEntity(TileEntityUpholstery.class, MODID + ":upholstery_colour");
		GameRegistry.registerTileEntity(TileEntityShieldChair.class, MODID + ":shield_chair");
		GameRegistry.registerTileEntity(TileEntityMetalVariant.class, MODID + ":metal_variant");
		ItemInitialiser.init();
		if (event.getSide().isClient()) {
			ClientModelInitialiser.registerPaddedBenchModels();
		}
		BlockInitialiser.init();
		PhaseFourFurnitureInitialiser.registerBlocks();
		if (event.getSide().isClient()) {
			GeneratedModelResourcePack.install();
			ClientModelInitialiser.registerPaddedBenchItemModels();
			ClientModelInitialiser.registerUpholsteryItemModels();
			ClientItemInitialiser.registerPhaseFourItemModels();
			ClientRenderInitialiser.registerShieldChairRenderer();
			ClientRenderInitialiser.registerEntityRenderers();
		}

    }

	@EventHandler
	public void missingMappings(FMLMissingMappingsEvent event) {
		CfmChairMigration.remapMissingMappings(event);
	}
}
