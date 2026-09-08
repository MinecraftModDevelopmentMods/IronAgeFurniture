package zone.moddev.mc.ironagefurniture;

import java.util.HashMap;
import java.util.Map;

import zone.moddev.mc.ironagefurniture.api.BarrelGuiHandler;
import zone.moddev.mc.ironagefurniture.api.BarrelFluidCompat;
import zone.moddev.mc.ironagefurniture.api.CreativeModeBreakTracker;
import zone.moddev.mc.ironagefurniture.api.DiningTableSurfaceInteractionHandler;
import zone.moddev.mc.ironagefurniture.api.FoudreBrewingRegistry;
import zone.moddev.mc.ironagefurniture.api.PotStillDistillingRegistry;
import zone.moddev.mc.ironagefurniture.api.InnkeeperManager;
import zone.moddev.mc.ironagefurniture.api.network.IronAgeFurnitureNetwork;
import zone.moddev.mc.ironagefurniture.api.drink.DrinkEffectHandler;
import zone.moddev.mc.ironagefurniture.api.entity.EntityFallingMetalBlock;
import zone.moddev.mc.ironagefurniture.api.entity.EntityThrownLavaLamp;
import zone.moddev.mc.ironagefurniture.api.entity.Seat;
import zone.moddev.mc.ironagefurniture.api.tile.TileEntityBarrel;
import zone.moddev.mc.ironagefurniture.api.tile.TileEntityBottleRack;
import zone.moddev.mc.ironagefurniture.api.tile.TileEntityCabinet;
import zone.moddev.mc.ironagefurniture.api.tile.TileEntityDiningTable;
import zone.moddev.mc.ironagefurniture.api.tile.TileEntityFoudre;
import zone.moddev.mc.ironagefurniture.api.tile.TileEntityFoudrePort;
import zone.moddev.mc.ironagefurniture.api.tile.TileEntityPotStill;
import zone.moddev.mc.ironagefurniture.api.tile.TileEntityPotStillPort;
import zone.moddev.mc.ironagefurniture.api.tile.TileEntityGlassVase;
import zone.moddev.mc.ironagefurniture.api.tile.TileEntityGrandChandelierSconce;
import zone.moddev.mc.ironagefurniture.api.tile.TileEntityHalfCabinet;
import zone.moddev.mc.ironagefurniture.api.tile.TileEntityHangingInnSign;
import zone.moddev.mc.ironagefurniture.api.tile.TileEntityMetalVariant;
import zone.moddev.mc.ironagefurniture.api.tile.TileEntitySurfaceDisplay;
import zone.moddev.mc.ironagefurniture.api.tile.TileEntityWallShelf;
import zone.moddev.mc.ironagefurniture.client.resources.GeneratedModelResourcePack;
import zone.moddev.mc.ironagefurniture.init.BlockInitialiser;
import zone.moddev.mc.ironagefurniture.init.ClientRenderInitialiser;
import zone.moddev.mc.ironagefurniture.init.ItemInitialiser;
import zone.moddev.mc.ironagefurniture.init.RecipeInitialiser;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;

@Mod(modid = Ironagefurniture.MODID, version = Ironagefurniture.VERSION)
public class Ironagefurniture
{
	public static final String MODID = "ironagefurniture";
    public static final String VERSION = "0.3.0.110021";

	static {
		FluidRegistry.enableUniversalBucket();
	}

	public static final int GUI_BARREL = 1;
	public static final int GUI_FOUDRE_LABEL = 2;
	public static final int GUI_POT_STILL = 3;
	public static final int GUI_INNKEEPER = 4;

	@Instance(MODID)
	public static Ironagefurniture instance;
    
	public static final Map<String,Block> BlockRegistry = new HashMap<String, Block>();
	public static final Map<String,Item> ItemRegistry = new HashMap<String, Item>();
    
    public static CreativeTabs ironagefurnitureTab = new CreativeTabs("ironagefurnitureTab"){
		@Override
		public Item getTabIconItem(){
			if (BlockObjectHolder.chair_wood_ironage_classic_big_oak != null) {
				return Item.getItemFromBlock(BlockObjectHolder.chair_wood_ironage_classic_big_oak);
			}
			if (BlockObjectHolder.light_metal_ironage_sconce_floor_empty_iron != null) {
				return Item.getItemFromBlock(BlockObjectHolder.light_metal_ironage_sconce_floor_empty_iron);
			}
			return Items.IRON_INGOT;
		}
		
		public boolean hasSearchBar() {
			return true;
		};
	};
    
    
    @EventHandler
    public void init(FMLInitializationEvent event)
    {
		BarrelFluidCompat.init();
		FoudreBrewingRegistry.initRecipes();
		PotStillDistillingRegistry.initRecipes();

    	// register renderers
    	if(event.getSide().isClient()) {
    		ItemInitialiser.RegisterItemRenders();
    		ClientRenderInitialiser.RegisterTileEntityRenderers();
    	}
    	
    	GameRegistry.registerTileEntity(TileEntityDiningTable.class, MODID + ":table_dining");
		GameRegistry.registerTileEntity(TileEntityCabinet.class, MODID + ":cabinet_wood_ironage");
		GameRegistry.registerTileEntity(TileEntityHalfCabinet.class, MODID + ":half_cabinet_wood_ironage");
		GameRegistry.registerTileEntity(TileEntityBarrel.class, MODID + ":barrel_wood_ironage");
		GameRegistry.registerTileEntity(TileEntityFoudre.class, MODID + ":foudre_wood_ironage");
		GameRegistry.registerTileEntity(TileEntityFoudrePort.class, MODID + ":foudre_wood_ironage_port");
		GameRegistry.registerTileEntity(TileEntityPotStill.class, MODID + ":pot_still_wood_ironage");
		GameRegistry.registerTileEntity(TileEntityPotStillPort.class, MODID + ":pot_still_wood_ironage_port");
		GameRegistry.registerTileEntity(TileEntityGlassVase.class, MODID + ":ornament_glass_vase");
		GameRegistry.registerTileEntity(TileEntityWallShelf.class, MODID + ":shelf_wall");
		GameRegistry.registerTileEntity(TileEntityBottleRack.class, MODID + ":bottle_rack_wood_ironage");
		GameRegistry.registerTileEntity(TileEntityMetalVariant.class, MODID + ":metal_variant");
		GameRegistry.registerTileEntity(TileEntityGrandChandelierSconce.class, MODID + ":chandelier_grand_sconce");
		GameRegistry.registerTileEntity(TileEntityHangingInnSign.class, MODID + ":hanging_inn_sign");
		GameRegistry.registerTileEntity(TileEntitySurfaceDisplay.class, MODID + ":surface_display");
    	EntityRegistry.registerModEntity(Seat.class, MODID + ":seat", 0, this, 80, 1, false);
		if (BlockObjectHolder.light_metal_ironage_block_floor_lava_clear != null) {
			EntityRegistry.registerModEntity(EntityThrownLavaLamp.class, MODID + ":thrown_lava_lamp", 1, this, 64, 10, true);
		}
		if (hasChandelierBlocks()) {
			EntityRegistry.registerModEntity(EntityFallingMetalBlock.class,
				MODID + ":falling_metal_block", 2, this, 160, 20, true);
		}

		NetworkRegistry.INSTANCE.registerGuiHandler(instance, new BarrelGuiHandler());
    	
    	RecipeInitialiser.init();
    }
    
    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
    	IronAgeFurnitureConfiguration.init(event);
		DrinkEffectHandler.init();
		InnkeeperManager.init();
		IronAgeFurnitureNetwork.init();
		FoudreBrewingRegistry.registerFluids();
		PotStillDistillingRegistry.registerFluids();
    	MinecraftForge.EVENT_BUS.register(new CreativeModeBreakTracker());
    	MinecraftForge.EVENT_BUS.register(new DiningTableSurfaceInteractionHandler());
		ItemInitialiser.init();
    	BlockInitialiser.init();
		if(event.getSide().isClient()) {
			GeneratedModelResourcePack.install();
			ClientRenderInitialiser.RegisterBlockStateMappers();
			ItemInitialiser.RegisterItemModels();
			ClientRenderInitialiser.RegisterEntityRenderers();
			ClientRenderInitialiser.RegisterGuiEventHandlers();
		}
    	
    }

	private static boolean hasChandelierBlocks() {
		return BlockObjectHolder.chandelier_candle != null
			|| BlockObjectHolder.chandelier_torch != null
			|| BlockObjectHolder.chandelier_glowstone != null
			|| BlockObjectHolder.chandelier_lava != null
			|| BlockObjectHolder.chandelier_redstone != null
			|| BlockObjectHolder.chandelier_grand_hub != null;
	}
}
