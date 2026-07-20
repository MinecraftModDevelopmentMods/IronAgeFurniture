package com.mcmoddev.ironagefurniture.client.render;

import java.util.Locale;

import com.mcmoddev.ironagefurniture.IronAgeFurnitureConfiguration;
import com.mcmoddev.ironagefurniture.api.Blocks.BottleRack;
import com.mcmoddev.ironagefurniture.api.Items.ItemDrinkware;
import com.mcmoddev.ironagefurniture.api.Items.ItemDrinkware.VesselType;
import com.mcmoddev.ironagefurniture.api.Items.ItemBlockGlassVase;
import com.mcmoddev.ironagefurniture.api.Items.ItemBlockOrnament;
import com.mcmoddev.ironagefurniture.api.VasePlantHelper;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.init.Items;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemRecord;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Loader;

import org.lwjgl.opengl.GL11;

public final class SurfaceDisplayRenderHelper {
	private static final String HARVESTCRAFT_MODID = "harvestcraft";
	private static final float MODEL_PIXELS_PER_BLOCK = 16.0F;
	private static final double DISPLAY_SURFACE_EPSILON = 0.001D;
	private static final float SURFACE_BOTTLE_SCALE = 1.05F;
	private static final double BOTTLE_MODEL_BOTTOM = 0.228D;
	private static ResourceLocation whiteSurfaceTexture;

	private enum FoodRenderKind {
		NONE,
		BOWL,
		DRINK,
		JAR,
		PLATE,
		SNACK,
		INGREDIENT,
		KITCHENWARE
	}

	private SurfaceDisplayRenderHelper() {
	}

	public static boolean renderSpecialSurfaceItem(ItemStack itemStack, double x, double y, double z,
			double itemX, double itemZ, double surfaceY, double blockSurfaceY, float yaw) {
		if (itemStack == null || itemStack.stackSize <= 0) {
			return false;
		}

		if (itemStack.getItem() instanceof ItemDrinkware) {
			renderDrinkware(itemStack, x, y, z, itemX, itemZ, blockSurfaceY, yaw, 1.0F);
			return true;
		}

		if (BottleRack.isValidBottleItem(itemStack)) {
			renderBottle(itemStack, x, y, z, itemX, itemZ, blockSurfaceY, yaw);
			return true;
		}

		if (isOrnament(itemStack)) {
			renderOrnament(itemStack, x, y, z, itemX, itemZ, blockSurfaceY, yaw);
			renderVasePlant(itemStack, x, y, z, itemX, itemZ, blockSurfaceY, yaw);
			return true;
		}

		String harvestCraftItemName = getHarvestCraftItemName(itemStack);
		FoodRenderKind harvestCraftFood = getHarvestCraftFoodRenderKind(harvestCraftItemName);
		if (harvestCraftFood != FoodRenderKind.NONE) {
			renderHarvestCraftFood(harvestCraftItemName, harvestCraftFood, x, y, z, itemX, itemZ,
				blockSurfaceY, yaw);
			return true;
		}

		if (!isBook(itemStack) && !isRecord(itemStack)) {
			return false;
		}

		boolean book = isBook(itemStack);
		double displayY = blockSurfaceY + (book ? -0.003D : 0.002D);
		GlStateManager.pushMatrix();
		GlStateManager.translate(x + itemX, y + displayY, z + itemZ);
		GlStateManager.rotate(yaw, 0.0F, 1.0F, 0.0F);
		GlStateManager.disableLighting();

		if (book) {
			renderClosedBook(itemStack);
		} else {
			GlStateManager.disableTexture2D();
			renderRecord(itemStack);
			GlStateManager.enableTexture2D();
		}

		GlStateManager.enableLighting();
		GlStateManager.popMatrix();
		return true;
	}

	public static void renderDrinkware(ItemStack itemStack, double x, double y, double z,
			double itemX, double itemZ, double surfaceY, float yaw, float layoutScale) {
		if (itemStack == null || !(itemStack.getItem() instanceof ItemDrinkware)) {
			return;
		}

		VesselType vessel = ItemDrinkware.getVariant(itemStack).getVessel();
		float scale;
		float modelBottomPixels;

		switch (vessel) {
		case TANKARD:
			scale = 0.46F;
			modelBottomPixels = 2.0F;
			break;
		case WINE_GLASS:
			scale = 0.42F;
			modelBottomPixels = 2.0F;
			break;
		case SPIRIT_GLASS:
			scale = 0.36F;
			modelBottomPixels = 3.0F;
			break;
		case SHOT_GLASS:
			scale = 0.29F;
			modelBottomPixels = 3.0F;
			break;
		case MUG:
		default:
			scale = 0.44F;
			modelBottomPixels = 2.0F;
			break;
		}

		scale *= layoutScale;
		double modelBottomOffset = modelBottomPixels / MODEL_PIXELS_PER_BLOCK * scale;
		GlStateManager.pushMatrix();
		GlStateManager.translate(x + itemX,
			y + surfaceY + scale / 2.0F - modelBottomOffset + DISPLAY_SURFACE_EPSILON,
			z + itemZ);
		GlStateManager.rotate(yaw, 0.0F, 1.0F, 0.0F);
		GlStateManager.scale(scale, scale, scale);
		Minecraft.getMinecraft().getRenderItem().renderItem(itemStack,
			ItemCameraTransforms.TransformType.NONE);
		GlStateManager.popMatrix();
	}

	private static void renderBottle(ItemStack itemStack, double x, double y, double z,
			double itemX, double itemZ, double surfaceY, float yaw) {
		double bottomOffset = BOTTLE_MODEL_BOTTOM * SURFACE_BOTTLE_SCALE;

		GlStateManager.pushMatrix();
		GlStateManager.translate(x + itemX,
			y + surfaceY + bottomOffset + DISPLAY_SURFACE_EPSILON,
			z + itemZ);
		GlStateManager.rotate(yaw, 0.0F, 1.0F, 0.0F);
		GlStateManager.rotate(90.0F, 1.0F, 0.0F, 0.0F);
		GlStateManager.scale(SURFACE_BOTTLE_SCALE, SURFACE_BOTTLE_SCALE, SURFACE_BOTTLE_SCALE);
		TileEntityBottleRackRenderer.renderBottleModel(itemStack);
		GlStateManager.popMatrix();
	}

	public static void renderPottedPlant(ItemStack plantStack, double x, double y, double z, double surfaceY) {
		if (plantStack == null || plantStack.stackSize <= 0) {
			return;
		}

		GlStateManager.pushMatrix();
		GlStateManager.translate(x + 0.5D, y + surfaceY + 0.46D, z + 0.5D);
		GlStateManager.scale(0.52F, 0.52F, 0.52F);
		Minecraft.getMinecraft().getRenderItem().renderItem(plantStack,
			ItemCameraTransforms.TransformType.FIXED);
		GlStateManager.popMatrix();
	}

	public static void renderGlassVasePlant(ItemStack plantStack, double x, double y, double z, double itemX,
			double itemZ, double surfaceY, float yaw) {
		if (plantStack == null || plantStack.stackSize <= 0) {
			return;
		}

		GlStateManager.pushMatrix();
		GlStateManager.translate(x + itemX, y + surfaceY, z + itemZ);
		GlStateManager.rotate(yaw, 0.0F, 1.0F, 0.0F);

		renderVaseStem();

		GlStateManager.translate(0.0D, 0.40D, 0.0D);
		GlStateManager.scale(0.56F, 0.56F, 0.56F);
		Minecraft.getMinecraft().getRenderItem().renderItem(plantStack,
			ItemCameraTransforms.TransformType.FIXED);
		GlStateManager.popMatrix();
	}

	private static void renderVasePlant(ItemStack vaseStack, double x, double y, double z, double itemX,
			double itemZ, double surfaceY, float yaw) {
		ItemStack plantStack = VasePlantHelper.getPlant(vaseStack);

		renderGlassVasePlant(plantStack, x, y, z, itemX, itemZ, surfaceY, yaw);
	}

	public static boolean isBook(ItemStack itemStack) {
		return itemStack.getItem() == Items.BOOK
			|| itemStack.getItem() == Items.WRITABLE_BOOK
			|| itemStack.getItem() == Items.WRITTEN_BOOK
			|| itemStack.getItem() == Items.ENCHANTED_BOOK;
	}

	private static boolean isRecord(ItemStack itemStack) {
		return itemStack.getItem() instanceof ItemRecord;
	}

	private static boolean isOrnament(ItemStack itemStack) {
		return itemStack.getItem() instanceof ItemBlockOrnament
			|| itemStack.getItem() instanceof ItemBlockGlassVase;
	}

	private static String getHarvestCraftItemName(ItemStack itemStack) {
		ResourceLocation registryName = itemStack.getItem().getRegistryName();
		if (registryName == null || !HARVESTCRAFT_MODID.equals(registryName.getResourceDomain())) {
			return null;
		}

		if (!IronAgeFurnitureConfiguration.INTEGRATION_HARVESTCRAFT
				|| !Loader.isModLoaded(HARVESTCRAFT_MODID)) {
			return null;
		}

		String itemName = registryName.getResourcePath().toLowerCase(Locale.ROOT);
		if (itemStack.getItem() instanceof ItemFood || isHarvestCraftSurfaceItemName(itemName)) {
			return itemName;
		}

		return null;
	}

	private static FoodRenderKind getHarvestCraftFoodRenderKind(String itemName) {
		if (itemName == null) {
			return FoodRenderKind.NONE;
		}

		if (isHarvestCraftKitchenwareName(itemName)) {
			return FoodRenderKind.KITCHENWARE;
		}

		if (isDrinkFoodName(itemName)) {
			return FoodRenderKind.DRINK;
		}

		if (isJarFoodName(itemName)) {
			return FoodRenderKind.JAR;
		}

		if (isBowlFoodName(itemName)) {
			return FoodRenderKind.BOWL;
		}

		if (isLooseSnackFoodName(itemName)) {
			return FoodRenderKind.SNACK;
		}

		if (isRawIngredientName(itemName)) {
			return FoodRenderKind.INGREDIENT;
		}

		if (isPlateFoodName(itemName)) {
			return FoodRenderKind.PLATE;
		}

		if (isSnackFoodName(itemName)) {
			return FoodRenderKind.SNACK;
		}

		if (isIngredientFoodName(itemName)) {
			return FoodRenderKind.INGREDIENT;
		}

		return FoodRenderKind.NONE;
	}

	private static boolean isDrinkFoodName(String itemName) {
		if (containsAny(itemName, "coffeebean", "coffeeseed")) {
			return false;
		}

		return containsAny(itemName, "juice", "smoothie", "coffee", "soda", "cider", "milkshake",
			"hotchocolate", "eggnog", "lemonade", "lemonaide", "limeade", "freshmilk",
			"coconutmilk", "soymilk", "freshwater", "bubblywater", "energydrink", "fruitpunch",
			"ironbrew", "espresso", "chocolatemilk", "pinacolada")
			|| itemName.contains("teaitem")
			|| isBottleIngredientName(itemName);
	}

	private static boolean isBowlFoodName(String itemName) {
		if (containsAny(itemName, "burger", "chilichocolate", "poppers", "biscuitsandgravy",
				"lambwithmintsauce", "sardinesinhotsauce")) {
			return false;
		}

		if (containsAny(itemName, "oatmeal", "porridge", "cereal", "museli")) {
			return true;
		}

		if (containsAny(itemName, "flour", "powder", "spice", "pepper", "cinnamon", "nutmeg",
				"salt", "sugar", "masala", "fivespice", "curryleaf")) {
			return false;
		}

		return containsAny(itemName, "soup", "stew", "curry", "gumbo", "jambalaya", "chili",
			"borscht", "noodle", "ramen", "pho", "oatmeal", "porridge", "cereal", "salad",
			"grits", "risotto", "macncheese", "beansandrice", "friedrice", "ricepudding",
			"sauce", "dip", "dressing", "ketchup", "gravy", "guacamole", "hummus", "coleslaw",
			"kimchi", "yogurt", "icecream", "custard", "creamitem", "creamedcorn",
			"steamedrice", "dhal", "museli", "refriedbeans", "salsa", "stock", "vindaloo",
			"tikkamasala");
	}

	private static boolean isPlateFoodName(String itemName) {
		if (itemName.contains("cornmeal")) {
			return false;
		}

		return containsAny(itemName, "pizza", "burger", "sandwich", "toast", "taco", "burrito",
			"quesadilla", "waffle", "pancake", "fries", "chips", "sushi", "roll", "dumpling",
			"springroll", "pie", "cake", "cookie", "donut", "brownie", "muffin", "cupcake",
			"cracker", "pretzel", "biscuit", "bread", "pasta", "spaghetti", "lasagna",
			"potpie", "quiche", "omelet", "omelette", "hash", "jerky", "sausage", "bacon",
			"ham", "steak", "roast", "chicken", "beef", "pork", "calamari", "shrimp", "crab",
			"lobster", "mutton", "turkey", "meat", "meal", "breakfast", "lunch", "dinner",
			"baked", "butteredpotato", "mashedpotato", "mashedpotatoes", "roastpotatoes",
			"mashedsweetpotatoes", "potatocakes", "potatoandcheesepirogi", "tunapotato",
			"candiedsweetpotatoes", "sweetpotatosouffle", "fritter", "cobbler", "baritem",
			"baklava", "bananasplit",
			"bangersandmash", "beansontoast", "blt", "cornonthecob", "futomaki", "dimsum",
			"footlong", "hotdog", "hotwings", "kebab", "wrap", "skewers", "pasty", "wellington",
			"macitem", "parmitem", "porkchop", "ribs", "wings", "fajita", "nachos", "tostada",
			"enchilada", "empanada", "salisburysteak", "fishsticks", "jaffa", "lamington",
			"stirfry", "steamed", "grilled", "stuffed", "glazed", "herbbutter", "crumble",
			"trifle", "pudding", "tart", "snaps", "caramelapple", "candiedginger",
			"celeryandpeanutbutter", "veggiestrips", "strips", "braised", "charsiu",
			"chorizo", "cooked", "croissant", "damper", "fried", "bun", "lemonmeringue",
			"manjuu", "marinated", "mochi", "naan", "nachoes", "paneer", "pavlova",
			"pbandj", "pickled", "poached", "poutine", "scone", "patties", "sesameball",
			"spagetti", "spicebun", "spicygreens", "suadero", "summersquash",
			"sweetpickle", "toadinthehole", "tortilla", "mcpam", "lamb", "candied",
			"okracreole", "peasandcelery", "pepperoni", "timtam", "zestyzucchini",
			"zeppole", "zucchinibake", "poppers", "sardinesinhotsauce", "platter");
	}

	private static boolean isJarFoodName(String itemName) {
		return itemName.endsWith("jellyitem")
			|| itemName.endsWith("chutneyitem")
			|| equalsAny(itemName, "almondbutteritem", "cashewbutteritem", "chestnutbutteritem",
				"peanutbutteritem", "pistachiobutteritem", "honeyitem", "royaljellyitem",
				"caramelitem", "mayoitem", "mustarditem", "nutellaitem", "vegemiteitem");
	}

	private static boolean isBottleIngredientName(String itemName) {
		return itemName.endsWith("syrupitem")
			|| equalsAny(itemName, "oliveoilitem", "sesameoilitem", "vinegaritem", "soysauceitem",
				"hotsauceitem", "hoisinsauceitem", "saladdressingitem", "sweetandsoursauceitem");
	}

	private static boolean isBottleDrinkShapeName(String itemName) {
		return containsAny(itemName, "soda", "cola", "rootbeer", "cider")
			|| isBottleIngredientName(itemName);
	}

	private static boolean isPotatoPlateFoodName(String itemName) {
		return containsAny(itemName, "bakedsweetpotato", "loadedbakedpotato", "scallionbakedpotato",
			"butteredpotato", "tunapotato", "roastpotatoes", "candiedsweetpotatoes",
			"mashedpotatoes", "garlicmashedpotatoes", "mashedsweetpotatoes", "potatocakes",
			"potatoandcheesepirogi", "poutine", "sweetpotatosouffle", "bakedturnips");
	}

	private static boolean isSnackFoodName(String itemName) {
		if (itemName.contains("waterchestnut") && !itemName.endsWith("seeditem")) {
			return false;
		}

		return isLooseSnackFoodName(itemName)
			|| containsAny(itemName, "jellybeans", "gummy", "cottoncandy", "cornflakes")
			|| isNutSnackName(itemName)
			|| itemName.endsWith("seedsitem")
			|| itemName.endsWith("seeditem");
	}

	private static boolean isLooseSnackFoodName(String itemName) {
		if (itemName.contains("waterchestnut") && !itemName.endsWith("seeditem")) {
			return false;
		}

		return isNutSnackName(itemName)
			|| itemName.endsWith("seedsitem")
			|| itemName.endsWith("seeditem")
			|| containsAny(itemName, "jellybeans", "gummy", "cottoncandy", "cornflakes",
				"popcorn", "trailmix", "pralines", "taffy", "turkishdelight", "chocovoxels",
				"marshmellow", "marshmellows", "marzipan", "toastedcoconut", "theatrebox")
			|| equalsAny(itemName, "chilichocolateitem", "chocolatecaramelfudgeitem",
				"chocolatecherryitem", "chocolatestrawberryitem");
	}

	private static boolean isNutSnackName(String itemName) {
		return equalsAny(itemName, "almonditem", "cashewitem", "chestnutitem", "peanutitem",
			"pecanitem", "pistachioitem", "walnutitem", "roastedchestnutitem",
			"candiedwalnutsitem");
	}

	private static boolean isIngredientFoodName(String itemName) {
		return itemName.endsWith("item") && !isHarvestCraftUtilityItemName(itemName);
	}

	private static boolean isHarvestCraftSurfaceItemName(String itemName) {
		return itemName.endsWith("item")
			&& (!isHarvestCraftUtilityItemName(itemName) || isHarvestCraftKitchenwareName(itemName));
	}

	private static boolean isHarvestCraftKitchenwareName(String itemName) {
		return equalsAny(itemName, "bakewareitem", "cuttingboarditem", "juiceritem", "mixingbowlitem",
			"mortarandpestleitem", "potitem", "saucepanitem", "skilletitem");
	}

	private static boolean isHarvestCraftUtilityItemName(String itemName) {
		return itemName.contains("hardenedleather")
			|| itemName.contains("baititem")
			|| equalsAny(itemName, "bakewareitem", "cuttingboarditem", "cropbagitem", "juiceritem",
				"mixingbowlitem", "mortarpestleitem", "mortarandpestleitem", "potitem",
				"saucepanitem", "skilletitem", "beeitem", "queenbeeitem", "seedbagitem",
				"saplingbagitem");
	}

	private static boolean containsAny(String itemName, String... values) {
		for (String value : values) {
			if (itemName.contains(value)) {
				return true;
			}
		}

		return false;
	}

	private static boolean equalsAny(String itemName, String... values) {
		for (String value : values) {
			if (itemName.equals(value)) {
				return true;
			}
		}

		return false;
	}

	private static void renderHarvestCraftFood(String itemName, FoodRenderKind kind, double x, double y,
			double z, double itemX, double itemZ, double surfaceY, float yaw) {
		GlStateManager.pushMatrix();
		GlStateManager.translate(x + itemX, y + surfaceY + 0.002D, z + itemZ);
		GlStateManager.rotate(yaw, 0.0F, 1.0F, 0.0F);
		double scale = getHarvestCraftFoodScale(kind);
		GlStateManager.scale(scale, scale, scale);
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		GlStateManager.enableTexture2D();
		GlStateManager.enableLighting();
		GlStateManager.enableRescaleNormal();
		GlStateManager.disableCull();
		bindWhiteSurfaceTexture();

		switch (kind) {
		case BOWL:
			renderHarvestCraftBowl(itemName);
			break;
		case DRINK:
			renderHarvestCraftDrink(itemName);
			break;
		case JAR:
			renderHarvestCraftJar(itemName);
			break;
		case PLATE:
			renderHarvestCraftPlate(itemName);
			break;
		case SNACK:
			renderHarvestCraftSnackBowl(itemName);
			break;
		case INGREDIENT:
			renderHarvestCraftIngredient(itemName);
			break;
		case KITCHENWARE:
			renderHarvestCraftKitchenware(itemName);
			break;
		default:
			break;
		}

		GlStateManager.enableCull();
		GlStateManager.disableRescaleNormal();
		GlStateManager.enableLighting();
		GlStateManager.enableTexture2D();
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		GlStateManager.popMatrix();
	}

	private static double getHarvestCraftFoodScale(FoodRenderKind kind) {
		switch (kind) {
		case DRINK:
			return 0.58D;
		case JAR:
			return 0.66D;
		case SNACK:
			return 0.70D;
		case INGREDIENT:
			return 0.68D;
		case BOWL:
			return 0.72D;
		case PLATE:
			return 0.74D;
		case KITCHENWARE:
			return 0.72D;
		default:
			return 0.82D;
		}
	}

	private static void bindWhiteSurfaceTexture() {
		if (whiteSurfaceTexture == null) {
			DynamicTexture texture = new DynamicTexture(1, 1);
			texture.getTextureData()[0] = 0xFFFFFFFF;
			texture.updateDynamicTexture();
			whiteSurfaceTexture = Minecraft.getMinecraft().getTextureManager()
				.getDynamicTextureLocation("ironagefurniture_surface_food", texture);
		}

		Minecraft.getMinecraft().getTextureManager().bindTexture(whiteSurfaceTexture);
	}

	private static void renderHarvestCraftBowl(String itemName) {
		if (isCreamDessertBowlName(itemName)) {
			renderHarvestCraftYogurtCup(itemName);
			return;
		}

		float[] ceramic = new float[] { 0.78F, 0.75F, 0.68F };
		float[] rim = darken(ceramic, 0.82F);
		float[] shadow = darken(ceramic, 0.48F);
		float[] food = inferFoodColor(itemName);
		float[] accent = getFoodAccentColor(itemName, food);

		drawCuboid(-0.170D, 0.000D, -0.142D, 0.170D, 0.024D, 0.142D, shadow[0], shadow[1],
			shadow[2]);
		drawCuboid(-0.205D, 0.024D, -0.170D, 0.205D, 0.054D, 0.170D, darken(ceramic, 0.90F)[0],
			darken(ceramic, 0.90F)[1], darken(ceramic, 0.90F)[2]);
		drawCuboid(-0.248D, 0.054D, -0.212D, -0.194D, 0.142D, 0.212D, ceramic[0], ceramic[1],
			ceramic[2]);
		drawCuboid(0.194D, 0.054D, -0.212D, 0.248D, 0.142D, 0.212D, ceramic[0], ceramic[1],
			ceramic[2]);
		drawCuboid(-0.248D, 0.054D, -0.212D, 0.248D, 0.142D, -0.158D, ceramic[0], ceramic[1],
			ceramic[2]);
		drawCuboid(-0.248D, 0.054D, 0.158D, 0.248D, 0.142D, 0.212D, ceramic[0], ceramic[1],
			ceramic[2]);
		drawCuboid(-0.290D, 0.142D, -0.244D, 0.290D, 0.172D, -0.190D, rim[0], rim[1], rim[2]);
		drawCuboid(-0.290D, 0.142D, 0.190D, 0.290D, 0.172D, 0.244D, rim[0], rim[1], rim[2]);
		drawCuboid(-0.290D, 0.142D, -0.244D, -0.224D, 0.172D, 0.244D, rim[0], rim[1], rim[2]);
		drawCuboid(0.224D, 0.142D, -0.244D, 0.290D, 0.172D, 0.244D, rim[0], rim[1], rim[2]);
		drawCuboid(-0.182D, 0.132D, -0.144D, 0.182D, 0.154D, 0.144D, darken(food, 0.86F)[0],
			darken(food, 0.86F)[1], darken(food, 0.86F)[2]);
		drawCuboid(-0.148D, 0.154D, -0.108D, 0.090D, 0.178D, 0.100D, food[0], food[1], food[2]);
		drawCuboid(0.030D, 0.156D, -0.090D, 0.148D, 0.180D, 0.015D, brighten(food, 1.08F)[0],
			brighten(food, 1.08F)[1], brighten(food, 1.08F)[2]);
		drawCuboid(-0.116D, 0.180D, -0.022D, -0.050D, 0.198D, 0.030D, accent[0], accent[1],
			accent[2]);
		drawCuboid(0.056D, 0.182D, 0.038D, 0.120D, 0.200D, 0.090D, accent[0], accent[1],
			accent[2]);
		drawCuboid(-0.018D, 0.182D, -0.082D, 0.045D, 0.198D, -0.038D, accent[0], accent[1],
			accent[2]);
	}

	private static boolean isCreamDessertBowlName(String itemName) {
		return containsAny(itemName, "yogurt", "icecream", "custard")
			|| equalsAny(itemName, "coconutcreamitem", "heavycreamitem");
	}

	private static void renderHarvestCraftYogurtCup(String itemName) {
		float[] ceramic = new float[] { 0.78F, 0.75F, 0.68F };
		float[] rim = darken(ceramic, 0.78F);
		float[] yogurt = containsAny(itemName, "chocolate", "mocha")
			? new float[] { 0.46F, 0.28F, 0.13F }
			: new float[] { 0.90F, 0.84F, 0.66F };
		float[] accent = getCreamDessertAccentColor(itemName);

		drawCuboid(-0.118D, 0.000D, -0.108D, 0.118D, 0.022D, 0.108D, darken(ceramic, 0.48F)[0],
			darken(ceramic, 0.48F)[1], darken(ceramic, 0.48F)[2]);
		drawCuboid(-0.145D, 0.022D, -0.135D, -0.105D, 0.184D, 0.135D, ceramic[0], ceramic[1],
			ceramic[2]);
		drawCuboid(0.105D, 0.022D, -0.135D, 0.145D, 0.184D, 0.135D, ceramic[0], ceramic[1],
			ceramic[2]);
		drawCuboid(-0.145D, 0.022D, -0.135D, 0.145D, 0.184D, -0.095D, ceramic[0], ceramic[1],
			ceramic[2]);
		drawCuboid(-0.145D, 0.022D, 0.095D, 0.145D, 0.184D, 0.135D, ceramic[0], ceramic[1],
			ceramic[2]);
		drawCuboid(-0.166D, 0.184D, -0.154D, 0.166D, 0.214D, -0.106D, rim[0], rim[1], rim[2]);
		drawCuboid(-0.166D, 0.184D, 0.106D, 0.166D, 0.214D, 0.154D, rim[0], rim[1], rim[2]);
		drawCuboid(-0.166D, 0.184D, -0.154D, -0.118D, 0.214D, 0.154D, rim[0], rim[1], rim[2]);
		drawCuboid(0.118D, 0.184D, -0.154D, 0.166D, 0.214D, 0.154D, rim[0], rim[1], rim[2]);
		drawCuboid(-0.098D, 0.184D, -0.084D, 0.098D, 0.206D, 0.084D, yogurt[0], yogurt[1],
			yogurt[2]);
		drawCuboid(-0.090D, 0.208D, -0.020D, 0.032D, 0.224D, 0.020D, accent[0], accent[1],
			accent[2]);
		drawCuboid(-0.018D, 0.210D, -0.076D, 0.024D, 0.226D, 0.070D, accent[0], accent[1],
			accent[2]);
		drawCuboid(0.046D, 0.208D, -0.004D, 0.090D, 0.226D, 0.052D, brighten(yogurt, 1.08F)[0],
			brighten(yogurt, 1.08F)[1], brighten(yogurt, 1.08F)[2]);
	}

	private static float[] getCreamDessertAccentColor(String itemName) {
		if (containsAny(itemName, "plain", "vanilla", "coconut", "creamitem", "heavycream")) {
			return new float[] { 0.92F, 0.78F, 0.42F };
		}

		return inferFoodColor(itemName);
	}

	private static void renderHarvestCraftDrink(String itemName) {
		if (itemName.contains("ironbrew")) {
			renderIronBrewDrink();
			return;
		}

		boolean mug = containsAny(itemName, "coffee", "teaitem", "hotchocolate", "cocoa");
		float[] liquid = inferDrinkColor(itemName);
		float[] glass = new float[] { 0.86F, 0.95F, 1.00F };
		float[] rim = new float[] { 0.68F, 0.72F, 0.74F };

		if (mug) {
			float[] ceramic = new float[] { 0.80F, 0.77F, 0.68F };
			float[] ceramicRim = darken(ceramic, 0.72F);

			drawCuboid(-0.110D, 0.000D, -0.105D, 0.110D, 0.026D, 0.105D, ceramicRim[0],
				ceramicRim[1], ceramicRim[2]);
			drawCuboid(-0.125D, 0.026D, -0.125D, -0.095D, 0.235D, 0.125D, ceramic[0],
				ceramic[1], ceramic[2]);
			drawCuboid(0.095D, 0.026D, -0.125D, 0.125D, 0.235D, 0.125D, ceramic[0],
				ceramic[1], ceramic[2]);
			drawCuboid(-0.125D, 0.026D, -0.125D, 0.125D, 0.235D, -0.095D, ceramic[0],
				ceramic[1], ceramic[2]);
			drawCuboid(-0.125D, 0.026D, 0.095D, 0.125D, 0.235D, 0.125D, ceramic[0],
				ceramic[1], ceramic[2]);
			drawCuboid(-0.085D, 0.204D, -0.085D, 0.085D, 0.224D, 0.085D, liquid[0],
				liquid[1], liquid[2]);
			drawCuboid(-0.138D, 0.235D, -0.138D, 0.138D, 0.260D, 0.138D, ceramicRim[0],
				ceramicRim[1], ceramicRim[2]);
			drawCuboid(0.108D, 0.085D, -0.026D, 0.172D, 0.205D, 0.026D, ceramic[0],
				ceramic[1], ceramic[2]);
			drawCuboid(0.146D, 0.075D, -0.026D, 0.196D, 0.110D, 0.026D, ceramic[0],
				ceramic[1], ceramic[2]);
			drawCuboid(0.146D, 0.180D, -0.026D, 0.196D, 0.215D, 0.026D, ceramic[0],
				ceramic[1], ceramic[2]);
		} else if (isBottleDrinkShapeName(itemName)) {
			float[] liquidHighlight = brighten(liquid, 1.12F);

			drawCuboid(-0.070D, 0.026D, -0.066D, 0.070D, 0.202D, 0.066D, liquid[0], liquid[1],
				liquid[2]);
			drawCuboid(-0.052D, 0.202D, -0.050D, 0.052D, 0.246D, 0.050D, liquidHighlight[0],
				liquidHighlight[1], liquidHighlight[2]);
			drawCuboid(-0.024D, 0.246D, -0.022D, 0.024D, 0.318D, 0.022D, liquidHighlight[0],
				liquidHighlight[1], liquidHighlight[2]);

			beginGlassLayer();
			drawCuboid(-0.085D, 0.000D, -0.080D, 0.085D, 0.030D, 0.080D, rim[0], rim[1],
				rim[2], 0.62F);
			drawCuboid(-0.094D, 0.030D, -0.089D, -0.074D, 0.210D, 0.089D, glass[0],
				glass[1], glass[2], 0.34F);
			drawCuboid(0.074D, 0.030D, -0.089D, 0.094D, 0.210D, 0.089D, glass[0], glass[1],
				glass[2], 0.34F);
			drawCuboid(-0.094D, 0.030D, -0.089D, 0.094D, 0.210D, -0.069D, glass[0],
				glass[1], glass[2], 0.34F);
			drawCuboid(-0.094D, 0.030D, 0.069D, 0.094D, 0.210D, 0.089D, glass[0], glass[1],
				glass[2], 0.34F);
			drawCuboid(-0.078D, 0.210D, -0.074D, 0.078D, 0.250D, 0.074D, glass[0],
				glass[1], glass[2], 0.30F);
			drawCuboid(-0.042D, 0.248D, -0.040D, 0.042D, 0.330D, 0.040D, glass[0],
				glass[1], glass[2], 0.36F);
			drawCuboid(-0.052D, 0.330D, -0.050D, 0.052D, 0.356D, 0.050D, rim[0], rim[1],
				rim[2], 0.64F);
			drawCuboid(-0.087D, 0.062D, -0.091D, -0.076D, 0.174D, -0.080D, 1.0F, 1.0F,
				0.95F, 0.42F);
			endGlassLayer();
		} else {
			drawCuboid(-0.085D, 0.026D, -0.080D, 0.085D, 0.190D, 0.080D, liquid[0],
				liquid[1], liquid[2]);
			drawCuboid(-0.074D, 0.190D, -0.070D, 0.074D, 0.208D, 0.070D,
				brighten(liquid, 1.10F)[0], brighten(liquid, 1.10F)[1],
				brighten(liquid, 1.10F)[2]);

			beginGlassLayer();
			drawCuboid(-0.110D, 0.000D, -0.105D, 0.110D, 0.026D, 0.105D, rim[0], rim[1],
				rim[2], 0.60F);
			drawCuboid(-0.125D, 0.205D, -0.120D, 0.125D, 0.232D, 0.120D, rim[0], rim[1],
				rim[2], 0.58F);
			drawCuboid(-0.130D, 0.026D, -0.130D, -0.108D, 0.232D, 0.130D, glass[0],
				glass[1], glass[2], 0.32F);
			drawCuboid(0.108D, 0.026D, -0.130D, 0.130D, 0.232D, 0.130D, glass[0],
				glass[1], glass[2], 0.32F);
			drawCuboid(-0.130D, 0.026D, -0.130D, 0.130D, 0.232D, -0.108D, glass[0],
				glass[1], glass[2], 0.32F);
			drawCuboid(-0.130D, 0.026D, 0.108D, 0.130D, 0.232D, 0.130D, glass[0],
				glass[1], glass[2], 0.32F);
			drawCuboid(-0.122D, 0.064D, -0.126D, -0.110D, 0.178D, -0.114D, 1.0F, 1.0F,
				0.95F, 0.40F);
			endGlassLayer();
			drawCuboid(0.020D, 0.220D, 0.008D, 0.037D, 0.360D, 0.025D, 0.86F, 0.86F, 0.82F);
		}
	}

	private static void renderIronBrewDrink() {
		float[] iron = new float[] { 0.55F, 0.55F, 0.52F };
		float[] darkIron = new float[] { 0.30F, 0.31F, 0.32F };
		float[] brightIron = new float[] { 0.70F, 0.70F, 0.66F };
		float[] brew = new float[] { 0.86F, 0.38F, 0.04F };
		float[] glow = new float[] { 0.96F, 0.72F, 0.12F };
		float[] red = new float[] { 0.62F, 0.08F, 0.04F };

		drawCuboid(-0.120D, 0.000D, -0.112D, 0.120D, 0.026D, 0.112D, darkIron[0], darkIron[1],
			darkIron[2]);
		drawCuboid(-0.136D, 0.026D, -0.130D, -0.096D, 0.222D, 0.130D, iron[0], iron[1],
			iron[2]);
		drawCuboid(0.096D, 0.026D, -0.130D, 0.136D, 0.222D, 0.130D, iron[0], iron[1],
			iron[2]);
		drawCuboid(-0.136D, 0.026D, -0.130D, 0.136D, 0.222D, -0.090D, iron[0], iron[1],
			iron[2]);
		drawCuboid(-0.136D, 0.026D, 0.090D, 0.136D, 0.222D, 0.130D, iron[0], iron[1],
			iron[2]);
		drawCuboid(-0.092D, 0.188D, -0.082D, 0.092D, 0.216D, 0.082D, brew[0], brew[1],
			brew[2]);
		drawCuboid(-0.048D, 0.218D, -0.052D, 0.038D, 0.238D, 0.020D, glow[0], glow[1],
			glow[2]);
		drawCuboid(0.025D, 0.220D, 0.010D, 0.082D, 0.240D, 0.058D, red[0], red[1], red[2]);
		drawCuboid(-0.152D, 0.222D, -0.148D, 0.152D, 0.252D, 0.148D, darkIron[0], darkIron[1],
			darkIron[2]);
		drawCuboid(-0.118D, 0.252D, -0.112D, 0.118D, 0.274D, 0.112D, brightIron[0],
			brightIron[1], brightIron[2]);
		drawCuboid(0.122D, 0.078D, -0.034D, 0.190D, 0.202D, 0.034D, brightIron[0],
			brightIron[1], brightIron[2]);
		drawCuboid(0.172D, 0.066D, -0.030D, 0.222D, 0.112D, 0.030D, brightIron[0],
			brightIron[1], brightIron[2]);
		drawCuboid(0.172D, 0.168D, -0.030D, 0.222D, 0.214D, 0.030D, brightIron[0],
			brightIron[1], brightIron[2]);
		drawCuboid(-0.110D, 0.056D, -0.134D, -0.072D, 0.180D, -0.128D, brightIron[0],
			brightIron[1], brightIron[2]);
	}

	private static void renderHarvestCraftJar(String itemName) {
		float[] contents = inferFoodColor(itemName);
		float[] contentsTop = brighten(contents, 1.12F);
		float[] glass = new float[] { 0.86F, 0.95F, 1.00F };
		float[] rim = new float[] { 0.64F, 0.70F, 0.76F };
		float[] lid = containsAny(itemName, "honey", "royaljelly")
			? new float[] { 0.78F, 0.62F, 0.22F }
			: new float[] { 0.64F, 0.66F, 0.70F };
		float[] lidDark = darken(lid, 0.82F);
		float[] label = new float[] { 0.84F, 0.78F, 0.58F };

		drawCuboid(-0.070D, 0.030D, -0.064D, 0.070D, 0.154D, 0.064D, contents[0],
			contents[1], contents[2]);
		drawCuboid(-0.060D, 0.154D, -0.054D, 0.060D, 0.170D, 0.054D, contentsTop[0],
			contentsTop[1], contentsTop[2]);

		beginGlassLayer();
		drawCuboid(-0.086D, 0.000D, -0.080D, 0.086D, 0.030D, 0.080D, rim[0], rim[1], rim[2],
			0.56F);
		drawCuboid(-0.090D, 0.030D, -0.084D, -0.072D, 0.172D, 0.084D, glass[0], glass[1],
			glass[2], 0.34F);
		drawCuboid(0.072D, 0.030D, -0.084D, 0.090D, 0.172D, 0.084D, glass[0], glass[1],
			glass[2], 0.34F);
		drawCuboid(-0.090D, 0.030D, -0.084D, 0.090D, 0.172D, -0.066D, glass[0], glass[1],
			glass[2], 0.34F);
		drawCuboid(-0.090D, 0.030D, 0.066D, 0.090D, 0.172D, 0.084D, glass[0], glass[1],
			glass[2], 0.34F);
		drawCuboid(-0.088D, 0.170D, -0.080D, 0.088D, 0.190D, 0.080D, rim[0], rim[1], rim[2],
			0.50F);
		drawCuboid(-0.082D, 0.060D, -0.086D, -0.071D, 0.136D, -0.075D, 1.0F, 1.0F, 0.95F,
			0.42F);
		endGlassLayer();

		drawCuboid(-0.058D, 0.076D, -0.088D, 0.058D, 0.126D, -0.081D, label[0], label[1],
			label[2]);
		drawCuboid(-0.078D, 0.190D, -0.070D, 0.078D, 0.214D, 0.070D, lid[0], lid[1], lid[2]);
		drawCuboid(-0.094D, 0.212D, -0.084D, 0.094D, 0.236D, 0.084D, lidDark[0],
			lidDark[1], lidDark[2]);
	}

	private static void renderHarvestCraftSnackBowl(String itemName) {
		float[] ceramic = new float[] { 0.78F, 0.75F, 0.68F };
		float[] rim = darken(ceramic, 0.80F);
		float[] snack = inferFoodColor(itemName);
		float[] darkSnack = darken(snack, 0.72F);
		float[] accent = getFoodAccentColor(itemName, snack);

		drawCuboid(-0.190D, 0.000D, -0.140D, 0.190D, 0.026D, 0.140D, darken(ceramic, 0.46F)[0],
			darken(ceramic, 0.46F)[1], darken(ceramic, 0.46F)[2]);
		drawCuboid(-0.225D, 0.026D, -0.170D, 0.225D, 0.062D, 0.170D, ceramic[0], ceramic[1],
			ceramic[2]);
		drawCuboid(-0.255D, 0.062D, -0.195D, 0.255D, 0.088D, 0.195D, rim[0], rim[1], rim[2]);
		drawCuboid(-0.190D, 0.088D, -0.140D, 0.190D, 0.102D, 0.140D, darkSnack[0],
			darkSnack[1], darkSnack[2]);
		drawCuboid(-0.145D, 0.104D, -0.080D, -0.075D, 0.132D, -0.015D, snack[0], snack[1],
			snack[2]);
		drawCuboid(-0.042D, 0.106D, -0.102D, 0.030D, 0.132D, -0.040D, snack[0], snack[1],
			snack[2]);
		drawCuboid(0.058D, 0.104D, -0.065D, 0.136D, 0.132D, 0.000D, snack[0], snack[1],
			snack[2]);
		drawCuboid(-0.108D, 0.106D, 0.030D, -0.030D, 0.134D, 0.095D, accent[0], accent[1],
			accent[2]);
		drawCuboid(0.020D, 0.108D, 0.020D, 0.100D, 0.136D, 0.090D, darkSnack[0],
			darkSnack[1], darkSnack[2]);
	}

	private static void renderHarvestCraftKitchenware(String itemName) {
		if (itemName.equals("cuttingboarditem")) {
			renderCuttingBoardKitchenware();
		} else if (itemName.equals("bakewareitem")) {
			renderBakewareKitchenware();
		} else if (itemName.equals("juiceritem")) {
			renderJuicerKitchenware();
		} else if (itemName.equals("mixingbowlitem")) {
			renderMixingBowlKitchenware();
		} else if (itemName.equals("mortarandpestleitem")) {
			renderMortarAndPestleKitchenware();
		} else if (itemName.equals("potitem")) {
			renderPotKitchenware();
		} else if (itemName.equals("saucepanitem")) {
			renderSaucepanKitchenware();
		} else if (itemName.equals("skilletitem")) {
			renderSkilletKitchenware();
		}
	}

	private static void renderBakewareKitchenware() {
		float[] metal = new float[] { 0.60F, 0.60F, 0.57F };
		float[] dark = darken(metal, 0.55F);
		float[] bright = brighten(metal, 1.18F);

		drawCuboid(-0.235D, 0.000D, -0.160D, 0.235D, 0.018D, 0.160D, dark[0], dark[1], dark[2]);
		drawCuboid(-0.210D, 0.018D, -0.135D, 0.210D, 0.040D, 0.135D, metal[0], metal[1],
			metal[2]);
		drawCuboid(-0.245D, 0.040D, -0.170D, 0.245D, 0.068D, -0.130D, dark[0], dark[1],
			dark[2]);
		drawCuboid(-0.245D, 0.040D, 0.130D, 0.245D, 0.068D, 0.170D, dark[0], dark[1],
			dark[2]);
		drawCuboid(-0.245D, 0.040D, -0.170D, -0.205D, 0.068D, 0.170D, dark[0], dark[1],
			dark[2]);
		drawCuboid(0.205D, 0.040D, -0.170D, 0.245D, 0.068D, 0.170D, dark[0], dark[1],
			dark[2]);
		drawCuboid(-0.170D, 0.070D, -0.095D, 0.160D, 0.078D, -0.070D, bright[0], bright[1],
			bright[2]);
	}

	private static void renderCuttingBoardKitchenware() {
		float[] board = new float[] { 0.52F, 0.32F, 0.14F };
		float[] boardDark = darken(board, 0.62F);
		float[] boardLight = brighten(board, 1.15F);
		float[] blade = new float[] { 0.72F, 0.74F, 0.72F };
		float[] handle = new float[] { 0.22F, 0.12F, 0.05F };

		drawCuboid(-0.230D, 0.000D, -0.142D, 0.190D, 0.026D, 0.142D, boardDark[0],
			boardDark[1], boardDark[2]);
		drawCuboid(-0.210D, 0.026D, -0.125D, 0.180D, 0.052D, 0.125D, board[0], board[1],
			board[2]);
		drawCuboid(0.175D, 0.026D, -0.050D, 0.245D, 0.052D, 0.050D, board[0], board[1],
			board[2]);
		drawCuboid(-0.165D, 0.054D, -0.095D, 0.125D, 0.062D, -0.070D, boardLight[0],
			boardLight[1], boardLight[2]);
		drawCuboid(-0.135D, 0.064D, -0.060D, 0.032D, 0.084D, -0.020D, blade[0], blade[1],
			blade[2]);
		drawCuboid(-0.092D, 0.084D, -0.020D, 0.068D, 0.102D, 0.020D, blade[0], blade[1],
			blade[2]);
		drawCuboid(0.060D, 0.066D, 0.016D, 0.176D, 0.094D, 0.056D, handle[0], handle[1],
			handle[2]);
	}

	private static void renderJuicerKitchenware() {
		float[] ceramic = new float[] { 0.78F, 0.76F, 0.70F };
		float[] rim = darken(ceramic, 0.74F);
		float[] shadow = darken(ceramic, 0.48F);

		drawCuboid(-0.130D, 0.000D, -0.110D, 0.130D, 0.020D, 0.110D, shadow[0], shadow[1],
			shadow[2]);
		drawCuboid(-0.160D, 0.020D, -0.130D, 0.160D, 0.050D, 0.130D, ceramic[0], ceramic[1],
			ceramic[2]);
		drawCuboid(-0.185D, 0.050D, -0.155D, 0.185D, 0.074D, 0.155D, rim[0], rim[1], rim[2]);
		drawCuboid(-0.090D, 0.074D, -0.078D, 0.090D, 0.122D, 0.078D, ceramic[0], ceramic[1],
			ceramic[2]);
		drawCuboid(-0.052D, 0.122D, -0.046D, 0.052D, 0.190D, 0.046D, brighten(ceramic, 1.10F)[0],
			brighten(ceramic, 1.10F)[1], brighten(ceramic, 1.10F)[2]);
		drawCuboid(-0.012D, 0.190D, -0.012D, 0.012D, 0.224D, 0.012D, rim[0], rim[1], rim[2]);
	}

	private static void renderMixingBowlKitchenware() {
		float[] clay = new float[] { 0.62F, 0.32F, 0.12F };
		float[] rim = darken(clay, 0.72F);
		float[] inside = new float[] { 0.82F, 0.62F, 0.38F };

		drawCuboid(-0.170D, 0.000D, -0.130D, 0.170D, 0.024D, 0.130D, darken(clay, 0.42F)[0],
			darken(clay, 0.42F)[1], darken(clay, 0.42F)[2]);
		drawCuboid(-0.210D, 0.024D, -0.160D, -0.160D, 0.140D, 0.160D, clay[0], clay[1],
			clay[2]);
		drawCuboid(0.160D, 0.024D, -0.160D, 0.210D, 0.140D, 0.160D, clay[0], clay[1],
			clay[2]);
		drawCuboid(-0.210D, 0.024D, -0.160D, 0.210D, 0.140D, -0.110D, clay[0], clay[1],
			clay[2]);
		drawCuboid(-0.210D, 0.024D, 0.110D, 0.210D, 0.140D, 0.160D, clay[0], clay[1],
			clay[2]);
		drawCuboid(-0.240D, 0.140D, -0.185D, 0.240D, 0.170D, 0.185D, rim[0], rim[1], rim[2]);
		drawCuboid(-0.135D, 0.142D, -0.095D, 0.135D, 0.158D, 0.095D, inside[0], inside[1],
			inside[2]);
	}

	private static void renderMortarAndPestleKitchenware() {
		float[] stone = new float[] { 0.46F, 0.46F, 0.42F };
		float[] dark = darken(stone, 0.58F);
		float[] light = brighten(stone, 1.20F);

		drawCuboid(-0.150D, 0.000D, -0.115D, 0.150D, 0.026D, 0.115D, dark[0], dark[1], dark[2]);
		drawCuboid(-0.185D, 0.026D, -0.142D, -0.135D, 0.145D, 0.142D, stone[0], stone[1],
			stone[2]);
		drawCuboid(0.135D, 0.026D, -0.142D, 0.185D, 0.145D, 0.142D, stone[0], stone[1],
			stone[2]);
		drawCuboid(-0.185D, 0.026D, -0.142D, 0.185D, 0.145D, -0.092D, stone[0], stone[1],
			stone[2]);
		drawCuboid(-0.185D, 0.026D, 0.092D, 0.185D, 0.145D, 0.142D, stone[0], stone[1],
			stone[2]);
		drawCuboid(-0.215D, 0.145D, -0.170D, 0.215D, 0.172D, 0.170D, dark[0], dark[1],
			dark[2]);
		drawCuboid(-0.094D, 0.146D, -0.066D, 0.094D, 0.162D, 0.066D, light[0], light[1],
			light[2]);
		drawCuboid(0.040D, 0.162D, -0.132D, 0.088D, 0.252D, -0.084D, dark[0], dark[1],
			dark[2]);
		drawCuboid(0.080D, 0.232D, -0.176D, 0.128D, 0.316D, -0.128D, light[0], light[1],
			light[2]);
	}

	private static void renderPotKitchenware() {
		float[] metal = new float[] { 0.33F, 0.34F, 0.34F };
		float[] dark = darken(metal, 0.55F);
		float[] light = brighten(metal, 1.35F);

		drawCuboid(-0.150D, 0.000D, -0.130D, 0.150D, 0.024D, 0.130D, dark[0], dark[1], dark[2]);
		drawCuboid(-0.190D, 0.024D, -0.160D, -0.142D, 0.190D, 0.160D, metal[0], metal[1],
			metal[2]);
		drawCuboid(0.142D, 0.024D, -0.160D, 0.190D, 0.190D, 0.160D, metal[0], metal[1],
			metal[2]);
		drawCuboid(-0.190D, 0.024D, -0.160D, 0.190D, 0.190D, -0.112D, metal[0], metal[1],
			metal[2]);
		drawCuboid(-0.190D, 0.024D, 0.112D, 0.190D, 0.190D, 0.160D, metal[0], metal[1],
			metal[2]);
		drawCuboid(-0.220D, 0.190D, -0.190D, 0.220D, 0.220D, 0.190D, dark[0], dark[1],
			dark[2]);
		drawCuboid(-0.110D, 0.220D, -0.095D, 0.110D, 0.240D, 0.095D, light[0], light[1],
			light[2]);
		drawCuboid(-0.270D, 0.084D, -0.040D, -0.190D, 0.135D, 0.040D, dark[0], dark[1],
			dark[2]);
		drawCuboid(0.190D, 0.084D, -0.040D, 0.270D, 0.135D, 0.040D, dark[0], dark[1],
			dark[2]);
	}

	private static void renderSaucepanKitchenware() {
		float[] metal = new float[] { 0.36F, 0.37F, 0.38F };
		float[] dark = darken(metal, 0.48F);
		float[] light = brighten(metal, 1.35F);

		drawCuboid(-0.145D, 0.000D, -0.105D, 0.145D, 0.022D, 0.105D, dark[0], dark[1], dark[2]);
		drawCuboid(-0.175D, 0.022D, -0.135D, -0.130D, 0.122D, 0.135D, metal[0], metal[1],
			metal[2]);
		drawCuboid(0.130D, 0.022D, -0.135D, 0.175D, 0.122D, 0.135D, metal[0], metal[1],
			metal[2]);
		drawCuboid(-0.175D, 0.022D, -0.135D, 0.175D, 0.122D, -0.090D, metal[0], metal[1],
			metal[2]);
		drawCuboid(-0.175D, 0.022D, 0.090D, 0.175D, 0.122D, 0.135D, metal[0], metal[1],
			metal[2]);
		drawCuboid(-0.205D, 0.122D, -0.165D, 0.205D, 0.148D, 0.165D, dark[0], dark[1],
			dark[2]);
		drawCuboid(0.180D, 0.060D, -0.030D, 0.360D, 0.092D, 0.030D, dark[0], dark[1],
			dark[2]);
		drawCuboid(0.350D, 0.056D, -0.040D, 0.420D, 0.100D, 0.040D, light[0], light[1],
			light[2]);
	}

	private static void renderSkilletKitchenware() {
		float[] iron = new float[] { 0.15F, 0.16F, 0.16F };
		float[] dark = new float[] { 0.04F, 0.045F, 0.045F };
		float[] light = new float[] { 0.30F, 0.31F, 0.31F };

		drawCuboid(-0.172D, 0.000D, -0.130D, 0.172D, 0.024D, 0.130D, dark[0], dark[1], dark[2]);
		drawCuboid(-0.205D, 0.024D, -0.158D, 0.205D, 0.058D, 0.158D, iron[0], iron[1],
			iron[2]);
		drawCuboid(-0.230D, 0.058D, -0.185D, 0.230D, 0.082D, -0.145D, dark[0], dark[1],
			dark[2]);
		drawCuboid(-0.230D, 0.058D, 0.145D, 0.230D, 0.082D, 0.185D, dark[0], dark[1],
			dark[2]);
		drawCuboid(-0.230D, 0.058D, -0.185D, -0.190D, 0.082D, 0.185D, dark[0], dark[1],
			dark[2]);
		drawCuboid(0.190D, 0.058D, -0.185D, 0.230D, 0.082D, 0.185D, dark[0], dark[1],
			dark[2]);
		drawCuboid(0.205D, 0.030D, -0.026D, 0.420D, 0.060D, 0.026D, dark[0], dark[1],
			dark[2]);
		drawCuboid(0.410D, 0.026D, -0.034D, 0.485D, 0.066D, 0.034D, light[0], light[1],
			light[2]);
		drawCuboid(-0.110D, 0.084D, -0.092D, 0.060D, 0.094D, -0.070D, light[0], light[1],
			light[2]);
	}

	private static void renderHarvestCraftIngredient(String itemName) {
		if (isSeedPackageIngredientName(itemName)) {
			renderSeedPackageIngredient(itemName);
			return;
		}

		renderPlateBase();

		if (isPowderIngredientName(itemName)) {
			renderPowderIngredient(itemName);
		} else if (isEggIngredientName(itemName)) {
			renderEggIngredient(itemName);
		} else if (isRawFilletIngredientName(itemName)) {
			renderRawFilletIngredient(itemName);
		} else if (isBlockIngredientName(itemName)) {
			renderBlockIngredient(itemName);
		} else if (isLeafIngredientName(itemName)) {
			renderLeafIngredient(itemName);
		} else if (isGrainIngredientName(itemName)) {
			renderGrainIngredient(itemName);
		} else if (isRootIngredientName(itemName)) {
			renderRootIngredient(itemName);
		} else {
			renderProduceIngredient(itemName);
		}
	}

	private static void renderPotatoPlateFood(String itemName) {
		if (containsAny(itemName, "mashedpotatoes", "garlicmashedpotatoes", "mashedsweetpotatoes")) {
			float[] potato = containsAny(itemName, "sweet") ? new float[] { 0.88F, 0.48F, 0.13F }
				: new float[] { 0.84F, 0.72F, 0.45F };
			float[] butter = new float[] { 0.94F, 0.78F, 0.22F };
			float[] green = new float[] { 0.18F, 0.48F, 0.16F };

			drawCuboid(-0.165D, 0.064D, -0.105D, 0.165D, 0.092D, 0.105D, darken(potato, 0.72F)[0],
				darken(potato, 0.72F)[1], darken(potato, 0.72F)[2]);
			drawCuboid(-0.135D, 0.092D, -0.080D, 0.060D, 0.150D, 0.080D, potato[0], potato[1],
				potato[2]);
			drawCuboid(-0.010D, 0.105D, -0.060D, 0.135D, 0.162D, 0.065D, brighten(potato, 1.08F)[0],
				brighten(potato, 1.08F)[1], brighten(potato, 1.08F)[2]);
			drawCuboid(-0.032D, 0.164D, -0.026D, 0.036D, 0.184D, 0.030D, butter[0], butter[1],
				butter[2]);
			drawCuboid(0.062D, 0.166D, 0.018D, 0.122D, 0.182D, 0.052D, green[0], green[1],
				green[2]);
			return;
		}

		if (containsAny(itemName, "roastpotatoes", "candiedsweetpotatoes", "potatocakes", "bakedturnips")) {
			float[] potato = containsAny(itemName, "sweet", "candied") ? new float[] { 0.88F, 0.44F, 0.10F }
				: new float[] { 0.78F, 0.58F, 0.28F };
			float[] skin = darken(potato, 0.62F);
			float[] glaze = containsAny(itemName, "candied") ? new float[] { 0.92F, 0.60F, 0.16F }
				: new float[] { 0.82F, 0.66F, 0.32F };

			drawCuboid(-0.155D, 0.064D, -0.090D, -0.060D, 0.126D, -0.010D, skin[0], skin[1],
				skin[2]);
			drawCuboid(-0.135D, 0.128D, -0.076D, -0.046D, 0.154D, 0.004D, potato[0], potato[1],
				potato[2]);
			drawCuboid(-0.025D, 0.064D, -0.105D, 0.070D, 0.126D, -0.020D, potato[0], potato[1],
				potato[2]);
			drawCuboid(-0.005D, 0.128D, -0.088D, 0.088D, 0.154D, -0.004D, glaze[0], glaze[1],
				glaze[2]);
			drawCuboid(0.075D, 0.064D, 0.010D, 0.160D, 0.122D, 0.095D, skin[0], skin[1],
				skin[2]);
			drawCuboid(0.090D, 0.124D, 0.022D, 0.174D, 0.150D, 0.108D, potato[0], potato[1],
				potato[2]);
			return;
		}

		boolean sweet = containsAny(itemName, "sweet");
		float[] skin = sweet ? new float[] { 0.52F, 0.22F, 0.10F }
			: new float[] { 0.46F, 0.28F, 0.13F };
		float[] flesh = sweet ? new float[] { 0.90F, 0.46F, 0.10F }
			: new float[] { 0.86F, 0.72F, 0.42F };
		float[] topping = containsAny(itemName, "tuna") ? new float[] { 0.72F, 0.64F, 0.52F }
			: new float[] { 0.94F, 0.78F, 0.24F };
		float[] toppingDark = containsAny(itemName, "loaded") ? new float[] { 0.42F, 0.16F, 0.08F }
			: darken(topping, 0.72F);
		float[] green = new float[] { 0.16F, 0.44F, 0.14F };

		drawCuboid(-0.210D, 0.064D, -0.075D, 0.210D, 0.090D, 0.075D, darken(skin, 0.58F)[0],
			darken(skin, 0.58F)[1], darken(skin, 0.58F)[2]);
		drawCuboid(-0.175D, 0.090D, -0.102D, 0.175D, 0.130D, 0.102D, skin[0], skin[1],
			skin[2]);
		drawCuboid(-0.220D, 0.096D, -0.068D, -0.160D, 0.126D, 0.068D, skin[0], skin[1],
			skin[2]);
		drawCuboid(0.160D, 0.096D, -0.068D, 0.220D, 0.126D, 0.068D, skin[0], skin[1],
			skin[2]);
		drawCuboid(-0.136D, 0.130D, -0.065D, 0.136D, 0.158D, 0.065D, flesh[0], flesh[1],
			flesh[2]);
		drawCuboid(-0.092D, 0.158D, -0.040D, 0.092D, 0.184D, 0.040D, brighten(flesh, 1.08F)[0],
			brighten(flesh, 1.08F)[1], brighten(flesh, 1.08F)[2]);
		drawCuboid(-0.038D, 0.186D, -0.028D, 0.042D, 0.206D, 0.030D, topping[0], topping[1],
			topping[2]);
		drawCuboid(0.054D, 0.184D, -0.030D, 0.112D, 0.202D, 0.030D, toppingDark[0],
			toppingDark[1], toppingDark[2]);
		drawCuboid(-0.118D, 0.182D, 0.020D, -0.056D, 0.198D, 0.056D, green[0], green[1],
			green[2]);
		drawCuboid(0.112D, 0.176D, 0.030D, 0.168D, 0.192D, 0.062D, green[0], green[1],
			green[2]);
	}

	private static boolean isPowderIngredientName(String itemName) {
		if (containsAny(itemName, "bellpepper", "chilipepper", "curryleaf", "peppercorn",
				"peppermint", "spiceleaf")) {
			return false;
		}

		return containsAny(itemName, "flour", "powder", "spice", "pepper", "cinnamon", "nutmeg",
			"salt", "sugar", "masala", "fivespice", "currypowder", "batter", "cornmeal");
	}

	private static boolean isEggIngredientName(String itemName) {
		return itemName.contains("egg") && !itemName.contains("eggplant");
	}

	private static boolean isRawIngredientName(String itemName) {
		return itemName.endsWith("rawitem") || itemName.startsWith("raw");
	}

	private static boolean isSeedPackageIngredientName(String itemName) {
		return itemName.endsWith("seedboxitem") || itemName.endsWith("seedpacketitem");
	}

	private static void renderSeedPackageIngredient(String itemName) {
		float[] paper = new float[] { 0.78F, 0.70F, 0.52F };
		float[] fold = darken(paper, 0.72F);
		float[] seed = inferFoodColor(itemName);
		float[] seedDark = darken(seed, 0.64F);

		if (itemName.endsWith("seedboxitem")) {
			drawCuboid(-0.172D, 0.000D, -0.112D, 0.172D, 0.038D, 0.112D, fold[0], fold[1],
				fold[2]);
			drawCuboid(-0.145D, 0.038D, -0.090D, 0.145D, 0.120D, 0.090D, paper[0], paper[1],
				paper[2]);
			drawCuboid(-0.170D, 0.038D, -0.112D, -0.145D, 0.120D, 0.112D, fold[0],
				fold[1], fold[2]);
			drawCuboid(0.145D, 0.038D, -0.112D, 0.170D, 0.120D, 0.112D, fold[0],
				fold[1], fold[2]);
			drawCuboid(-0.094D, 0.122D, -0.050D, -0.030D, 0.150D, 0.014D, seed[0],
				seed[1], seed[2]);
			drawCuboid(0.010D, 0.122D, -0.030D, 0.076D, 0.150D, 0.034D, seedDark[0],
				seedDark[1], seedDark[2]);
			return;
		}

		drawCuboid(-0.188D, 0.000D, -0.130D, 0.188D, 0.018D, 0.130D, fold[0], fold[1], fold[2]);
		drawCuboid(-0.168D, 0.018D, -0.112D, 0.168D, 0.046D, 0.112D, paper[0], paper[1],
			paper[2]);
		drawCuboid(-0.150D, 0.048D, -0.092D, 0.150D, 0.060D, -0.072D, fold[0], fold[1],
			fold[2]);
		drawCuboid(-0.150D, 0.048D, 0.072D, 0.150D, 0.060D, 0.092D, fold[0], fold[1],
			fold[2]);
		drawCuboid(-0.052D, 0.062D, -0.030D, 0.026D, 0.086D, 0.036D, seed[0], seed[1],
			seed[2]);
		drawCuboid(0.042D, 0.062D, -0.018D, 0.092D, 0.082D, 0.030D, seedDark[0], seedDark[1],
			seedDark[2]);
	}

	private static boolean isRawFilletIngredientName(String itemName) {
		return isRawIngredientName(itemName)
			|| containsAny(itemName, "anchovy", "bass", "carp", "catfish", "charr", "clam", "eel", "fish",
				"grouper", "herring", "jellyfish", "octopus", "perch", "scallop", "snapper", "tilapia",
				"trout", "tuna", "walleye");
	}

	private static boolean isRawFishShapedIngredientName(String itemName) {
		return containsAny(itemName, "anchovy", "bass", "carp", "catfish", "charr", "eel", "fish",
			"grouper", "herring", "mudfish", "perch", "sardine", "snapper", "tilapia", "trout",
			"tuna", "walleye", "salmon")
			&& !containsAny(itemName, "calamari", "clam", "crab", "crayfish", "fishtrapbait",
				"fishandchips", "fishdinner", "fishlettucewrap", "fishsandwich", "fishsticks",
				"fishtaco", "jellyfish", "lobster", "mussel", "octopus", "scallop", "shrimp",
				"snail");
	}

	private static boolean isRawShellfishIngredientName(String itemName) {
		return containsAny(itemName, "calamari", "clam", "crab", "crayfish", "jellyfish", "lobster",
			"mussel", "octopus", "scallop", "shrimp", "snail");
	}

	private static boolean isBlockIngredientName(String itemName) {
		return containsAny(itemName, "butter", "cheese", "tofu", "dough", "cream", "custard",
			"wax", "cotton", "caramel", "honeycomb");
	}

	private static boolean isLeafIngredientName(String itemName) {
		return containsAny(itemName, "curryleaf", "peppermint", "spiceleaf", "tealeaf", "seaweed",
			"lettuce", "spinach", "cabbage");
	}

	private static boolean isGrainIngredientName(String itemName) {
		return equalsAny(itemName, "barleyitem", "beanitem", "coffeebeanitem", "cornitem",
			"oatsitem", "peasitem", "peppercornitem", "raisinsitem", "riceitem", "ryeitem",
			"soybeanitem", "vanillabeanitem");
	}

	private static boolean isRootIngredientName(String itemName) {
		return containsAny(itemName, "bambooshoot", "celery", "edibleroot", "garlic", "ginger",
			"leek", "onion", "parsnip", "rhubarb", "rutabaga", "scallion", "turnip",
			"sweetpotato", "beet")
			|| equalsAny(itemName, "carrotitem", "radishitem");
	}

	private static void renderPowderIngredient(String itemName) {
		float[] ceramic = new float[] { 0.78F, 0.75F, 0.68F };
		float[] powder = inferFoodColor(itemName);
		float[] rim = darken(ceramic, 0.78F);

		drawCuboid(-0.140D, 0.064D, -0.110D, 0.140D, 0.086D, 0.110D, darken(ceramic, 0.48F)[0],
			darken(ceramic, 0.48F)[1], darken(ceramic, 0.48F)[2]);
		drawCuboid(-0.170D, 0.086D, -0.135D, 0.170D, 0.126D, 0.135D, ceramic[0], ceramic[1],
			ceramic[2]);
		drawCuboid(-0.192D, 0.126D, -0.152D, 0.192D, 0.148D, 0.152D, rim[0], rim[1], rim[2]);
		drawCuboid(-0.135D, 0.148D, -0.100D, 0.135D, 0.166D, 0.100D, powder[0], powder[1],
			powder[2]);
		drawCuboid(-0.045D, 0.168D, -0.030D, 0.045D, 0.184D, 0.030D, brighten(powder, 1.10F)[0],
			brighten(powder, 1.10F)[1], brighten(powder, 1.10F)[2]);
	}

	private static void renderEggIngredient(String itemName) {
		float[] shell = itemName.contains("fried") ? new float[] { 0.92F, 0.90F, 0.74F }
			: new float[] { 0.88F, 0.84F, 0.68F };
		float[] yolk = new float[] { 0.94F, 0.66F, 0.12F };

		drawCuboid(-0.150D, 0.064D, -0.075D, -0.030D, 0.102D, 0.060D, shell[0], shell[1],
			shell[2]);
		drawCuboid(-0.105D, 0.104D, -0.020D, -0.055D, 0.132D, 0.030D, yolk[0], yolk[1],
			yolk[2]);
		drawCuboid(0.035D, 0.064D, -0.060D, 0.145D, 0.104D, 0.075D, shell[0], shell[1],
			shell[2]);
		drawCuboid(0.070D, 0.106D, -0.010D, 0.122D, 0.134D, 0.040D, yolk[0], yolk[1],
			yolk[2]);
	}

	private static void renderRawFilletIngredient(String itemName) {
		if (isRawFishShapedIngredientName(itemName)) {
			renderRawFishIngredient(itemName);
			return;
		}

		if (isRawShellfishIngredientName(itemName)) {
			renderRawShellfishIngredient(itemName);
			return;
		}

		float[] flesh = inferFoodColor(itemName);
		float[] dark = darken(flesh, 0.72F);
		float[] pale = brighten(flesh, 1.12F);

		drawCuboid(-0.175D, 0.064D, -0.080D, 0.165D, 0.100D, 0.080D, dark[0], dark[1], dark[2]);
		drawCuboid(-0.150D, 0.102D, -0.060D, 0.135D, 0.130D, 0.060D, flesh[0], flesh[1],
			flesh[2]);
		drawCuboid(-0.125D, 0.132D, -0.048D, -0.070D, 0.146D, 0.048D, pale[0], pale[1],
			pale[2]);
		drawCuboid(-0.020D, 0.132D, -0.048D, 0.036D, 0.146D, 0.048D, pale[0], pale[1],
			pale[2]);
		drawCuboid(0.084D, 0.132D, -0.048D, 0.140D, 0.146D, 0.048D, pale[0], pale[1],
			pale[2]);
	}

	private static void renderRawFishIngredient(String itemName) {
		float[] body = getRawFishBodyColor(itemName);
		float[] dark = darken(body, 0.62F);
		float[] belly = getRawFishBellyColor(itemName);
		float[] fin = getRawFishFinColor(itemName, body);

		drawCuboid(-0.198D, 0.064D, -0.046D, 0.078D, 0.086D, 0.046D, dark[0], dark[1],
			dark[2]);
		drawCuboid(-0.174D, 0.086D, -0.074D, 0.074D, 0.130D, 0.074D, body[0], body[1],
			body[2]);
		drawCuboid(0.046D, 0.092D, -0.058D, 0.148D, 0.126D, 0.058D, brighten(body, 1.04F)[0],
			brighten(body, 1.04F)[1], brighten(body, 1.04F)[2]);
		drawCuboid(-0.136D, 0.130D, -0.040D, 0.070D, 0.148D, 0.040D, belly[0], belly[1],
			belly[2]);
		drawCuboid(-0.232D, 0.088D, -0.078D, -0.166D, 0.124D, -0.024D, fin[0], fin[1],
			fin[2]);
		drawCuboid(-0.232D, 0.088D, 0.024D, -0.166D, 0.124D, 0.078D, fin[0], fin[1],
			fin[2]);
		drawCuboid(-0.030D, 0.128D, -0.118D, 0.050D, 0.146D, -0.066D, fin[0], fin[1],
			fin[2]);
		drawCuboid(-0.048D, 0.128D, 0.066D, 0.034D, 0.146D, 0.118D, fin[0], fin[1],
			fin[2]);
		drawCuboid(-0.150D, 0.150D, -0.018D, 0.018D, 0.162D, 0.018D, brighten(body, 1.12F)[0],
			brighten(body, 1.12F)[1], brighten(body, 1.12F)[2]);
		drawCuboid(0.124D, 0.128D, -0.024D, 0.146D, 0.150D, -0.004D, 0.04F, 0.04F, 0.04F);
	}

	private static void renderRawShellfishIngredient(String itemName) {
		if (itemName.contains("jellyfish")) {
			renderRawJellyfishIngredient();
			return;
		}

		if (itemName.contains("calamari")) {
			renderRawCalamariIngredient();
			return;
		}

		if (containsAny(itemName, "crayfish", "shrimp", "octopus")) {
			renderRawCrustaceanIngredient(itemName);
			return;
		}

		float[] shell = containsAny(itemName, "clam", "mussel", "scallop", "snail")
			? (itemName.contains("scallop") ? new float[] { 0.78F, 0.66F, 0.25F }
				: new float[] { 0.60F, 0.56F, 0.50F })
			: new float[] { 0.82F, 0.40F, 0.28F };
		float[] flesh = containsAny(itemName, "clam", "mussel", "scallop")
			? new float[] { 0.86F, 0.70F, 0.58F }
			: new float[] { 0.90F, 0.52F, 0.42F };
		float[] dark = darken(shell, 0.64F);

		drawCuboid(-0.170D, 0.064D, -0.082D, -0.030D, 0.120D, 0.064D, dark[0], dark[1],
			dark[2]);
		drawCuboid(-0.150D, 0.120D, -0.060D, -0.010D, 0.146D, 0.044D, shell[0], shell[1],
			shell[2]);
		drawCuboid(-0.122D, 0.148D, -0.040D, -0.040D, 0.164D, 0.030D, flesh[0], flesh[1],
			flesh[2]);
		drawCuboid(0.030D, 0.064D, -0.070D, 0.148D, 0.116D, 0.050D, shell[0], shell[1],
			shell[2]);
		drawCuboid(0.050D, 0.118D, -0.050D, 0.168D, 0.142D, 0.032D, dark[0], dark[1],
			dark[2]);
		drawCuboid(0.082D, 0.144D, -0.030D, 0.132D, 0.160D, 0.020D, flesh[0], flesh[1],
			flesh[2]);
	}

	private static void renderRawJellyfishIngredient() {
		float[] dome = new float[] { 0.48F, 0.53F, 0.75F };
		float[] pale = new float[] { 0.66F, 0.70F, 0.88F };
		float[] dark = new float[] { 0.22F, 0.27F, 0.48F };

		drawCuboid(-0.120D, 0.064D, -0.092D, 0.120D, 0.108D, 0.092D, dark[0], dark[1],
			dark[2]);
		drawCuboid(-0.160D, 0.108D, -0.120D, 0.160D, 0.162D, 0.120D, dome[0], dome[1],
			dome[2]);
		drawCuboid(-0.105D, 0.162D, -0.084D, 0.105D, 0.186D, 0.084D, pale[0], pale[1],
			pale[2]);
		drawCuboid(-0.118D, 0.064D, -0.040D, -0.080D, 0.132D, -0.010D, dark[0], dark[1],
			dark[2]);
		drawCuboid(-0.040D, 0.064D, 0.012D, -0.006D, 0.124D, 0.046D, dome[0], dome[1],
			dome[2]);
		drawCuboid(0.036D, 0.064D, -0.050D, 0.070D, 0.134D, -0.018D, dark[0], dark[1],
			dark[2]);
		drawCuboid(0.096D, 0.064D, 0.018D, 0.130D, 0.118D, 0.054D, dome[0], dome[1],
			dome[2]);
	}

	private static void renderRawCalamariIngredient() {
		float[] squid = new float[] { 0.48F, 0.48F, 0.56F };
		float[] pale = new float[] { 0.58F, 0.57F, 0.65F };
		float[] dark = new float[] { 0.29F, 0.29F, 0.35F };

		drawCuboid(-0.138D, 0.064D, -0.086D, 0.040D, 0.132D, 0.086D, dark[0], dark[1],
			dark[2]);
		drawCuboid(-0.105D, 0.132D, -0.066D, 0.078D, 0.170D, 0.066D, squid[0], squid[1],
			squid[2]);
		drawCuboid(0.072D, 0.070D, -0.040D, 0.154D, 0.126D, 0.040D, pale[0], pale[1],
			pale[2]);
		drawCuboid(0.130D, 0.064D, -0.088D, 0.168D, 0.112D, -0.044D, dark[0], dark[1],
			dark[2]);
		drawCuboid(0.130D, 0.064D, 0.044D, 0.168D, 0.112D, 0.088D, dark[0], dark[1],
			dark[2]);
		drawCuboid(0.154D, 0.064D, -0.018D, 0.194D, 0.106D, 0.018D, squid[0], squid[1],
			squid[2]);
	}

	private static void renderRawCrustaceanIngredient(String itemName) {
		float[] shell = itemName.contains("shrimp") ? new float[] { 0.80F, 0.38F, 0.40F }
			: new float[] { 0.60F, 0.24F, 0.24F };
		float[] pale = itemName.contains("shrimp") ? new float[] { 0.96F, 0.60F, 0.57F }
			: new float[] { 0.75F, 0.33F, 0.31F };
		float[] dark = darken(shell, 0.68F);

		if (itemName.contains("octopus")) {
			drawCuboid(-0.080D, 0.086D, -0.082D, 0.080D, 0.178D, 0.082D, shell[0], shell[1],
				shell[2]);
			drawCuboid(-0.136D, 0.064D, -0.110D, -0.092D, 0.112D, -0.030D, dark[0], dark[1],
				dark[2]);
			drawCuboid(-0.052D, 0.064D, -0.126D, -0.010D, 0.118D, -0.040D, shell[0], shell[1],
				shell[2]);
			drawCuboid(0.034D, 0.064D, -0.112D, 0.076D, 0.120D, -0.030D, dark[0], dark[1],
				dark[2]);
			drawCuboid(0.100D, 0.064D, -0.070D, 0.144D, 0.116D, 0.010D, shell[0], shell[1],
				shell[2]);
			drawCuboid(-0.122D, 0.064D, 0.030D, -0.080D, 0.112D, 0.110D, shell[0], shell[1],
				shell[2]);
			drawCuboid(-0.030D, 0.064D, 0.040D, 0.014D, 0.118D, 0.126D, dark[0], dark[1],
				dark[2]);
			drawCuboid(0.070D, 0.064D, 0.026D, 0.112D, 0.112D, 0.102D, shell[0], shell[1],
				shell[2]);
			return;
		}

		drawCuboid(-0.154D, 0.064D, -0.062D, -0.030D, 0.112D, 0.062D, dark[0], dark[1],
			dark[2]);
		drawCuboid(-0.056D, 0.084D, -0.070D, 0.094D, 0.140D, 0.070D, shell[0], shell[1],
			shell[2]);
		drawCuboid(0.060D, 0.140D, -0.046D, 0.146D, 0.166D, 0.046D, pale[0], pale[1],
			pale[2]);
		drawCuboid(0.108D, 0.076D, -0.088D, 0.150D, 0.118D, -0.044D, dark[0], dark[1],
			dark[2]);
		drawCuboid(0.108D, 0.076D, 0.044D, 0.150D, 0.118D, 0.088D, dark[0], dark[1],
			dark[2]);
		drawCuboid(-0.094D, 0.112D, -0.108D, -0.054D, 0.136D, -0.066D, shell[0], shell[1],
			shell[2]);
		drawCuboid(-0.094D, 0.112D, 0.066D, -0.054D, 0.136D, 0.108D, shell[0], shell[1],
			shell[2]);
	}

	private static void renderBlockIngredient(String itemName) {
		float[] base = inferFoodColor(itemName);
		float[] shadow = darken(base, 0.68F);
		float[] accent = containsAny(itemName, "cheese", "butter") ? new float[] { 0.94F, 0.76F, 0.20F }
			: brighten(base, 1.12F);

		drawCuboid(-0.165D, 0.064D, -0.095D, 0.040D, 0.112D, 0.095D, shadow[0], shadow[1],
			shadow[2]);
		drawCuboid(-0.145D, 0.112D, -0.078D, 0.060D, 0.156D, 0.078D, base[0], base[1],
			base[2]);
		drawCuboid(0.090D, 0.064D, -0.078D, 0.165D, 0.136D, 0.078D, base[0], base[1],
			base[2]);
		drawCuboid(-0.120D, 0.158D, -0.048D, -0.030D, 0.176D, 0.048D, accent[0], accent[1],
			accent[2]);
	}

	private static void renderLeafIngredient(String itemName) {
		float[] leaf = inferFoodColor(itemName);
		float[] dark = darken(leaf, 0.62F);
		float[] stem = new float[] { 0.20F, 0.36F, 0.10F };

		drawCuboid(-0.175D, 0.064D, -0.090D, 0.060D, 0.088D, -0.035D, dark[0], dark[1], dark[2]);
		drawCuboid(-0.150D, 0.090D, -0.075D, 0.090D, 0.114D, -0.020D, leaf[0], leaf[1],
			leaf[2]);
		drawCuboid(-0.030D, 0.066D, -0.010D, 0.175D, 0.090D, 0.045D, leaf[0], leaf[1],
			leaf[2]);
		drawCuboid(-0.098D, 0.116D, 0.040D, 0.110D, 0.138D, 0.094D, dark[0], dark[1],
			dark[2]);
		drawCuboid(-0.145D, 0.140D, -0.004D, 0.142D, 0.154D, 0.018D, stem[0], stem[1],
			stem[2]);
	}

	private static void renderGrainIngredient(String itemName) {
		float[] grain = inferFoodColor(itemName);
		float[] dark = darken(grain, 0.62F);
		float[] pale = brighten(grain, 1.10F);

		drawCuboid(-0.165D, 0.064D, -0.105D, 0.155D, 0.083D, 0.105D, dark[0], dark[1], dark[2]);
		drawCuboid(-0.142D, 0.085D, -0.074D, -0.088D, 0.116D, -0.020D, grain[0], grain[1],
			grain[2]);
		drawCuboid(-0.068D, 0.087D, -0.098D, -0.006D, 0.118D, -0.040D, pale[0], pale[1],
			pale[2]);
		drawCuboid(0.032D, 0.085D, -0.068D, 0.094D, 0.116D, -0.008D, grain[0], grain[1],
			grain[2]);
		drawCuboid(0.096D, 0.086D, 0.018D, 0.150D, 0.114D, 0.074D, dark[0], dark[1], dark[2]);
		drawCuboid(-0.112D, 0.088D, 0.032D, -0.052D, 0.118D, 0.090D, pale[0], pale[1],
			pale[2]);
		drawCuboid(-0.010D, 0.092D, 0.012D, 0.055D, 0.124D, 0.074D, grain[0], grain[1],
			grain[2]);
	}

	private static void renderRootIngredient(String itemName) {
		float[] root = inferFoodColor(itemName);
		float[] dark = darken(root, 0.64F);
		float[] pale = brighten(root, 1.12F);
		float[] green = new float[] { 0.16F, 0.44F, 0.12F };

		drawCuboid(-0.170D, 0.064D, -0.072D, -0.010D, 0.116D, 0.030D, dark[0], dark[1],
			dark[2]);
		drawCuboid(-0.140D, 0.118D, -0.058D, 0.020D, 0.146D, 0.044D, root[0], root[1],
			root[2]);
		drawCuboid(0.050D, 0.064D, -0.050D, 0.172D, 0.112D, 0.062D, root[0], root[1],
			root[2]);
		drawCuboid(0.070D, 0.114D, -0.032D, 0.154D, 0.138D, 0.046D, pale[0], pale[1],
			pale[2]);
		drawCuboid(-0.052D, 0.148D, 0.042D, 0.034D, 0.168D, 0.078D, green[0], green[1],
			green[2]);
		drawCuboid(0.102D, 0.140D, 0.050D, 0.164D, 0.158D, 0.086D, green[0], green[1],
			green[2]);
	}

	private static void renderProduceIngredient(String itemName) {
		if (itemName.contains("mushroom")) {
			renderMushroomProduce(itemName);
			return;
		}

		if (itemName.contains("grub")) {
			renderGrubProduce();
			return;
		}

		if (itemName.contains("vanilla")) {
			renderVanillaProduce();
			return;
		}

		if (itemName.contains("coconut")) {
			renderCoconutProduce();
			return;
		}

		if (itemName.contains("pineapple")) {
			renderPineappleProduce();
			return;
		}

		if (containsAny(itemName, "artichoke", "broccoli", "brusselsprout", "cauliflower")) {
			renderBulkyProduceIngredient(itemName);
			return;
		}

		if (containsAny(itemName, "banana", "lemon", "lime")) {
			renderLongProduceIngredient(itemName);
			return;
		}

		if (containsAny(itemName, "apricot", "avocado", "cactusfruit", "grapefruit", "mango",
				"orange", "peach", "pear", "persimmon", "tomato")) {
			renderRoundProduceIngredient(itemName);
			return;
		}

		if (containsAny(itemName, "asparagus", "cucumber", "eggplant", "gherkin", "okra",
				"pepper", "pickle", "waterchestnut", "zucchini")) {
			renderLongProduceIngredient(itemName);
			return;
		}

		if (containsAny(itemName, "blackberry", "blueberry", "candleberry", "cherry", "cranberry",
				"date", "fig", "gooseberry", "grape", "kiwi", "olive", "plum", "raspberry",
				"strawberry")) {
			renderBerryProduceIngredient(itemName);
			return;
		}

		if (containsAny(itemName, "cantaloupe", "dragonfruit", "durian", "papaya", "pomegranate",
				"starfruit", "watermelon", "wintersquash")) {
			renderLargeProduceIngredient(itemName);
			return;
		}

		float[] produce = inferFoodColor(itemName);
		float[] dark = darken(produce, 0.68F);
		float[] accent = getFoodAccentColor(itemName, produce);

		drawCuboid(-0.160D, 0.064D, -0.090D, -0.070D, 0.138D, -0.006D, dark[0], dark[1],
			dark[2]);
		drawCuboid(-0.140D, 0.138D, -0.074D, -0.052D, 0.164D, 0.010D, produce[0], produce[1],
			produce[2]);
		drawCuboid(-0.025D, 0.064D, -0.070D, 0.070D, 0.142D, 0.020D, produce[0], produce[1],
			produce[2]);
		drawCuboid(0.090D, 0.064D, 0.000D, 0.165D, 0.136D, 0.090D, dark[0], dark[1],
			dark[2]);
		drawCuboid(0.104D, 0.138D, 0.012D, 0.178D, 0.160D, 0.102D, produce[0], produce[1],
			produce[2]);
		drawCuboid(-0.020D, 0.146D, 0.034D, 0.040D, 0.164D, 0.074D, accent[0], accent[1],
			accent[2]);
	}

	private static void renderMushroomProduce(String itemName) {
		float[] cap = itemName.contains("white") ? new float[] { 0.78F, 0.72F, 0.58F }
			: new float[] { 0.34F, 0.18F, 0.08F };
		float[] stem = new float[] { 0.78F, 0.70F, 0.54F };
		float[] shadow = darken(cap, 0.62F);

		drawCuboid(-0.150D, 0.064D, -0.045D, -0.090D, 0.126D, 0.045D, stem[0], stem[1],
			stem[2]);
		drawCuboid(-0.190D, 0.126D, -0.085D, -0.050D, 0.166D, 0.085D, cap[0], cap[1],
			cap[2]);
		drawCuboid(-0.162D, 0.166D, -0.060D, -0.075D, 0.188D, 0.060D, shadow[0], shadow[1],
			shadow[2]);
		drawCuboid(0.030D, 0.064D, -0.035D, 0.090D, 0.118D, 0.035D, stem[0], stem[1],
			stem[2]);
		drawCuboid(-0.010D, 0.118D, -0.075D, 0.130D, 0.154D, 0.075D, cap[0], cap[1],
			cap[2]);
		drawCuboid(0.018D, 0.154D, -0.050D, 0.104D, 0.174D, 0.050D, shadow[0], shadow[1],
			shadow[2]);
	}

	private static void renderGrubProduce() {
		float[] body = new float[] { 0.82F, 0.70F, 0.48F };
		float[] shadow = darken(body, 0.70F);

		drawCuboid(-0.180D, 0.064D, -0.050D, -0.085D, 0.108D, 0.050D, body[0], body[1],
			body[2]);
		drawCuboid(-0.100D, 0.078D, -0.052D, -0.005D, 0.122D, 0.052D, shadow[0], shadow[1],
			shadow[2]);
		drawCuboid(-0.020D, 0.092D, -0.048D, 0.080D, 0.136D, 0.048D, body[0], body[1],
			body[2]);
		drawCuboid(0.078D, 0.104D, -0.034D, 0.145D, 0.132D, 0.034D, shadow[0], shadow[1],
			shadow[2]);
	}

	private static void renderVanillaProduce() {
		float[] pod = new float[] { 0.20F, 0.10F, 0.04F };
		float[] pale = new float[] { 0.70F, 0.58F, 0.34F };

		drawCuboid(-0.185D, 0.064D, -0.034D, 0.050D, 0.092D, -0.006D, pod[0], pod[1],
			pod[2]);
		drawCuboid(-0.150D, 0.094D, -0.018D, 0.090D, 0.122D, 0.010D, pod[0], pod[1],
			pod[2]);
		drawCuboid(-0.030D, 0.124D, 0.018D, 0.172D, 0.150D, 0.046D, pod[0], pod[1],
			pod[2]);
		drawCuboid(-0.090D, 0.126D, -0.006D, -0.035D, 0.138D, 0.006D, pale[0], pale[1],
			pale[2]);
	}

	private static void renderCoconutProduce() {
		float[] shell = new float[] { 0.34F, 0.18F, 0.08F };
		float[] flesh = new float[] { 0.84F, 0.78F, 0.58F };
		float[] shadow = darken(shell, 0.62F);

		drawCuboid(-0.160D, 0.064D, -0.105D, 0.020D, 0.142D, 0.105D, shell[0], shell[1],
			shell[2]);
		drawCuboid(-0.138D, 0.142D, -0.082D, -0.002D, 0.166D, 0.082D, flesh[0], flesh[1],
			flesh[2]);
		drawCuboid(0.055D, 0.064D, -0.084D, 0.165D, 0.136D, 0.084D, shadow[0], shadow[1],
			shadow[2]);
		drawCuboid(0.075D, 0.136D, -0.060D, 0.146D, 0.156D, 0.060D, flesh[0], flesh[1],
			flesh[2]);
	}

	private static void renderPineappleProduce() {
		float[] fruit = new float[] { 0.84F, 0.64F, 0.12F };
		float[] rind = new float[] { 0.54F, 0.36F, 0.08F };
		float[] leaf = new float[] { 0.18F, 0.48F, 0.14F };

		drawCuboid(-0.105D, 0.064D, -0.090D, 0.105D, 0.178D, 0.090D, rind[0], rind[1],
			rind[2]);
		drawCuboid(-0.080D, 0.084D, -0.066D, 0.080D, 0.184D, 0.066D, fruit[0], fruit[1],
			fruit[2]);
		drawCuboid(-0.044D, 0.186D, -0.030D, 0.000D, 0.245D, 0.030D, leaf[0], leaf[1],
			leaf[2]);
		drawCuboid(0.006D, 0.186D, -0.045D, 0.052D, 0.238D, 0.002D, leaf[0], leaf[1],
			leaf[2]);
		drawCuboid(-0.075D, 0.186D, 0.006D, -0.026D, 0.232D, 0.052D, leaf[0], leaf[1],
			leaf[2]);
	}

	private static void renderBulkyProduceIngredient(String itemName) {
		float[] head = itemName.contains("cauliflower")
			? new float[] { 0.78F, 0.76F, 0.58F }
			: inferFoodColor(itemName);
		float[] dark = darken(head, 0.66F);
		float[] leaf = itemName.contains("cauliflower") ? new float[] { 0.20F, 0.42F, 0.14F }
			: darken(head, 0.52F);
		float[] stem = new float[] { 0.44F, 0.52F, 0.20F };

		drawCuboid(-0.160D, 0.064D, -0.060D, -0.075D, 0.124D, 0.030D, stem[0], stem[1],
			stem[2]);
		drawCuboid(-0.190D, 0.108D, -0.105D, -0.060D, 0.178D, 0.035D, head[0], head[1],
			head[2]);
		drawCuboid(-0.120D, 0.150D, -0.070D, 0.012D, 0.210D, 0.060D, dark[0], dark[1],
			dark[2]);
		drawCuboid(0.045D, 0.064D, -0.050D, 0.122D, 0.118D, 0.050D, stem[0], stem[1],
			stem[2]);
		drawCuboid(0.010D, 0.108D, -0.088D, 0.162D, 0.180D, 0.080D, head[0], head[1],
			head[2]);
		drawCuboid(0.078D, 0.154D, -0.044D, 0.190D, 0.205D, 0.068D, dark[0], dark[1],
			dark[2]);
		drawCuboid(-0.198D, 0.084D, 0.036D, -0.052D, 0.116D, 0.100D, leaf[0], leaf[1],
			leaf[2]);
		drawCuboid(0.015D, 0.082D, 0.052D, 0.178D, 0.114D, 0.110D, leaf[0], leaf[1],
			leaf[2]);
	}

	private static void renderLongProduceIngredient(String itemName) {
		float[] produce = inferFoodColor(itemName);
		float[] dark = darken(produce, 0.66F);
		float[] pale = brighten(produce, 1.12F);
		float[] stem = new float[] { 0.18F, 0.42F, 0.12F };

		drawCuboid(-0.205D, 0.064D, -0.042D, 0.030D, 0.112D, 0.042D, dark[0], dark[1],
			dark[2]);
		drawCuboid(-0.180D, 0.114D, -0.030D, 0.056D, 0.140D, 0.030D, produce[0], produce[1],
			produce[2]);
		drawCuboid(0.070D, 0.064D, -0.036D, 0.190D, 0.108D, 0.036D, produce[0], produce[1],
			produce[2]);
		drawCuboid(0.090D, 0.110D, -0.025D, 0.170D, 0.132D, 0.025D, pale[0], pale[1],
			pale[2]);
		drawCuboid(-0.218D, 0.118D, -0.010D, -0.176D, 0.138D, 0.010D, stem[0], stem[1],
			stem[2]);
	}

	private static void renderRoundProduceIngredient(String itemName) {
		float[] produce = inferFoodColor(itemName);
		float[] dark = darken(produce, 0.68F);
		float[] pale = brighten(produce, 1.10F);
		float[] leaf = new float[] { 0.16F, 0.42F, 0.10F };

		drawCuboid(-0.168D, 0.064D, -0.078D, -0.060D, 0.142D, 0.030D, dark[0], dark[1],
			dark[2]);
		drawCuboid(-0.148D, 0.142D, -0.058D, -0.040D, 0.166D, 0.050D, produce[0],
			produce[1], produce[2]);
		drawCuboid(-0.018D, 0.064D, -0.095D, 0.088D, 0.146D, 0.018D, produce[0],
			produce[1], produce[2]);
		drawCuboid(0.002D, 0.146D, -0.070D, 0.064D, 0.166D, -0.006D, pale[0], pale[1],
			pale[2]);
		drawCuboid(0.098D, 0.064D, 0.000D, 0.178D, 0.132D, 0.082D, dark[0], dark[1],
			dark[2]);
		drawCuboid(-0.086D, 0.168D, 0.036D, -0.008D, 0.188D, 0.076D, leaf[0], leaf[1],
			leaf[2]);
		drawCuboid(0.122D, 0.134D, 0.052D, 0.170D, 0.150D, 0.088D, leaf[0], leaf[1],
			leaf[2]);
	}

	private static void renderBerryProduceIngredient(String itemName) {
		float[] berry = inferFoodColor(itemName);
		float[] dark = darken(berry, 0.68F);
		float[] leaf = new float[] { 0.16F, 0.42F, 0.10F };

		drawCuboid(-0.160D, 0.064D, -0.075D, -0.090D, 0.126D, -0.005D, berry[0], berry[1],
			berry[2]);
		drawCuboid(-0.078D, 0.066D, -0.095D, -0.008D, 0.128D, -0.025D, dark[0], dark[1],
			dark[2]);
		drawCuboid(0.010D, 0.064D, -0.052D, 0.082D, 0.130D, 0.020D, berry[0], berry[1],
			berry[2]);
		drawCuboid(0.092D, 0.066D, -0.018D, 0.160D, 0.126D, 0.050D, dark[0], dark[1],
			dark[2]);
		drawCuboid(-0.070D, 0.132D, 0.035D, 0.050D, 0.150D, 0.075D, leaf[0], leaf[1],
			leaf[2]);
	}

	private static void renderLargeProduceIngredient(String itemName) {
		float[] produce = inferFoodColor(itemName);
		float[] rind = darken(produce, 0.62F);
		float[] accent = getFoodAccentColor(itemName, produce);

		drawCuboid(-0.185D, 0.064D, -0.095D, 0.060D, 0.132D, 0.095D, rind[0], rind[1],
			rind[2]);
		drawCuboid(-0.150D, 0.132D, -0.070D, 0.035D, 0.158D, 0.070D, produce[0], produce[1],
			produce[2]);
		drawCuboid(0.078D, 0.064D, -0.080D, 0.178D, 0.142D, 0.080D, produce[0], produce[1],
			produce[2]);
		drawCuboid(0.098D, 0.144D, -0.056D, 0.158D, 0.164D, 0.056D, accent[0], accent[1],
			accent[2]);
	}

	private static void renderHarvestCraftPlate(String itemName) {
		renderPlateBase();

		if (itemName.contains("creepercookie")) {
			renderCreeperCookieFood();
		} else if (itemName.contains("creeperwings")) {
			renderCreeperWingsFood();
		} else if (containsAny(itemName, "sundayroast", "roastchicken", "potroast")) {
			renderRoastDinnerPlateFood(itemName);
		} else if (isPotatoPlateFoodName(itemName)) {
			renderPotatoPlateFood(itemName);
		} else if (itemName.contains("caramelapple")) {
			renderCaramelAppleFood();
		} else if (itemName.contains("pizza")) {
			renderPizzaFood();
		} else if (containsAny(itemName, "sweetandsour", "stirfry", "kungpao", "generaltso",
				"orangechicken", "sesamechicken", "teriyaki", "bakedbeans", "eggplantparm",
				"paneer")) {
			renderSaucedPlateFood(itemName);
		} else if (containsAny(itemName, "pancake", "waffle")) {
			renderPancakePlateFood(itemName);
		} else if (containsAny(itemName, "burger", "hotdog", "footlong")) {
			renderBurgerPlateFood(itemName);
		} else if (containsAny(itemName, "delightedmeal", "homestylelunch", "ploughmanslunch",
				"mcpam", "bbqplatter")) {
			renderComboPlateFood(itemName);
		} else if (containsAny(itemName, "taco", "burrito", "quesadilla", "fajita", "nachos",
				"nachoes", "tostada", "enchilada", "poppers", "tortilla", "wrap")) {
			renderWrapPlateFood(itemName);
		} else if (containsAny(itemName, "pasta", "spaghetti", "spagetti", "lasagna", "chowmein",
				"lomein", "macitem", "broccolimac")) {
			renderPastaPlateFood(itemName);
		} else if (containsAny(itemName, "sushi", "futomaki", "californiaroll", "dimsum", "dumpling",
				"springroll", "chikoroll", "sesameball")) {
			renderSmallRollPlateFood(itemName);
		} else if (containsAny(itemName, "sandwich", "toast", "grilledcheese",
				"celeryandpeanutbutter", "blt", "pbandj")) {
			renderStackedPlateFood(itemName);
		} else if (containsAny(itemName, "baconandeggs", "friedegg", "omelet", "omelette", "hash",
				"breakfast", "bangersandmash", "toadinthehole")) {
			renderBreakfastPlateFood(itemName);
		} else if (containsAny(itemName, "calamari", "clam", "crab", "crayfish", "fish", "frog",
				"lobster", "mussel", "octopus", "salmon", "sardine", "scallop", "shrimp",
				"snail")) {
			renderSeafoodPlateFood(itemName);
		} else if (containsAny(itemName, "ovenroastedcauliflower", "roastedrootveggiemedley")) {
			renderVegetablePlateFood(itemName);
		} else if (containsAny(itemName, "bacon", "beef", "charsiu", "chorizo", "chicken", "ham",
				"jerky", "kebab", "lamb", "meat", "mutton", "pepperoni", "pork", "rabbit",
				"ribs", "roast", "sausage", "steak", "suadero", "turkey", "turtle", "venison",
				"wings", "cookedtof")) {
			renderMeatPlateFood(itemName);
		} else if (containsAny(itemName, "grilled", "steamed", "stuffed", "glazed", "herbbutter",
				"cornonthecob", "marinated", "pickled", "veggiestrips", "strips", "bakedbeets",
				"braised", "friedonions", "friedpecanokra", "spicygreens", "summersquash",
				"okracreole", "peasandcelery", "sweetpickle", "zestyzucchini", "zucchinibake")) {
			renderVegetablePlateFood(itemName);
		} else if (containsAny(itemName, "pie", "cake", "cookie", "donut", "brownie", "muffin",
				"cupcake", "baritem", "baklava", "biscuit", "cracker", "pretzel", "bread",
				"cobbler", "croissant", "crumble", "damper", "fritter", "jaffa", "lamington",
				"lemonmeringue", "manjuu", "mochi", "naan", "pavlova", "pudding", "quiche",
				"roll", "scone", "snaps", "spicebun", "tart", "timtam", "trifle", "zeppole",
				"bananasplit", "candiedginger", "candiedlemon", "honeybun", "pasty",
				"poachedpear")) {
			renderBakedPlateFood(itemName);
		} else if (containsAny(itemName, "fries", "chips")) {
			renderFriesPlateFood();
		} else {
			renderGenericPlateFood(itemName);
		}
	}

	private static void renderPlateBase() {
		float[] ceramic = new float[] { 0.86F, 0.84F, 0.78F };
		float[] rim = darken(ceramic, 0.78F);
		float[] shadow = darken(ceramic, 0.48F);

		drawCuboid(-0.245D, 0.000D, -0.175D, 0.245D, 0.018D, 0.175D, shadow[0], shadow[1],
			shadow[2]);
		drawCuboid(-0.225D, 0.018D, -0.155D, 0.225D, 0.043D, 0.155D, ceramic[0], ceramic[1],
			ceramic[2]);
		drawCuboid(-0.265D, 0.043D, -0.188D, 0.265D, 0.062D, 0.188D, rim[0], rim[1], rim[2]);
	}

	private static void renderPizzaFood() {
		float[] crust = new float[] { 0.66F, 0.38F, 0.16F };
		float[] cheese = new float[] { 0.94F, 0.76F, 0.22F };
		float[] tomato = new float[] { 0.70F, 0.12F, 0.08F };
		float[] herb = new float[] { 0.16F, 0.42F, 0.12F };

		drawCuboid(-0.205D, 0.064D, -0.135D, 0.205D, 0.088D, 0.135D, crust[0], crust[1],
			crust[2]);
		drawCuboid(-0.168D, 0.090D, -0.105D, 0.168D, 0.112D, 0.105D, cheese[0], cheese[1],
			cheese[2]);
		drawCuboid(-0.130D, 0.114D, -0.080D, -0.075D, 0.132D, -0.025D, tomato[0], tomato[1],
			tomato[2]);
		drawCuboid(0.048D, 0.114D, -0.054D, 0.104D, 0.132D, 0.000D, tomato[0], tomato[1],
			tomato[2]);
		drawCuboid(-0.018D, 0.114D, 0.045D, 0.040D, 0.132D, 0.096D, herb[0], herb[1], herb[2]);
	}

	private static void renderCaramelAppleFood() {
		float[] caramel = new float[] { 0.74F, 0.38F, 0.10F };
		float[] apple = new float[] { 0.70F, 0.12F, 0.08F };
		float[] stick = new float[] { 0.44F, 0.25F, 0.10F };
		float[] highlight = new float[] { 0.90F, 0.62F, 0.22F };

		drawCuboid(-0.035D, 0.064D, -0.145D, 0.000D, 0.230D, -0.110D, stick[0], stick[1],
			stick[2]);
		drawCuboid(-0.120D, 0.064D, -0.080D, 0.060D, 0.152D, 0.085D, caramel[0], caramel[1],
			caramel[2]);
		drawCuboid(-0.095D, 0.154D, -0.058D, 0.045D, 0.194D, 0.062D, highlight[0], highlight[1],
			highlight[2]);
		drawCuboid(0.070D, 0.064D, -0.072D, 0.175D, 0.136D, 0.070D, apple[0], apple[1],
			apple[2]);
		drawCuboid(0.082D, 0.138D, -0.048D, 0.152D, 0.164D, 0.050D, highlight[0], highlight[1],
			highlight[2]);
	}

	private static void renderCreeperCookieFood() {
		float[] green = new float[] { 0.26F, 0.60F, 0.12F };
		float[] darkGreen = new float[] { 0.08F, 0.28F, 0.05F };
		float[] face = new float[] { 0.02F, 0.08F, 0.02F };
		float[] highlight = new float[] { 0.42F, 0.74F, 0.20F };

		drawCuboid(-0.124D, 0.064D, -0.124D, 0.124D, 0.090D, 0.124D, darkGreen[0],
			darkGreen[1], darkGreen[2]);
		drawCuboid(-0.150D, 0.090D, -0.150D, 0.150D, 0.126D, 0.150D, green[0], green[1],
			green[2]);
		drawCuboid(-0.112D, 0.128D, -0.112D, -0.042D, 0.140D, -0.042D, face[0], face[1],
			face[2]);
		drawCuboid(0.042D, 0.128D, -0.112D, 0.112D, 0.140D, -0.042D, face[0], face[1],
			face[2]);
		drawCuboid(-0.034D, 0.128D, -0.026D, 0.034D, 0.140D, 0.054D, face[0], face[1],
			face[2]);
		drawCuboid(-0.078D, 0.128D, 0.044D, 0.078D, 0.140D, 0.116D, face[0], face[1],
			face[2]);
		drawCuboid(-0.142D, 0.126D, 0.104D, -0.060D, 0.138D, 0.138D, highlight[0],
			highlight[1], highlight[2]);
	}

	private static void renderCreeperWingsFood() {
		float[] green = new float[] { 0.16F, 0.50F, 0.10F };
		float[] darkGreen = new float[] { 0.06F, 0.23F, 0.04F };
		float[] pale = new float[] { 0.44F, 0.70F, 0.22F };
		float[] bone = new float[] { 0.82F, 0.76F, 0.58F };

		drawCuboid(-0.195D, 0.064D, -0.072D, -0.030D, 0.114D, 0.072D, darkGreen[0],
			darkGreen[1], darkGreen[2]);
		drawCuboid(-0.166D, 0.116D, -0.052D, -0.002D, 0.150D, 0.052D, green[0], green[1],
			green[2]);
		drawCuboid(-0.122D, 0.152D, -0.030D, -0.052D, 0.168D, 0.030D, pale[0], pale[1],
			pale[2]);
		drawCuboid(0.025D, 0.064D, -0.062D, 0.176D, 0.112D, 0.062D, green[0], green[1],
			green[2]);
		drawCuboid(0.046D, 0.114D, -0.044D, 0.196D, 0.146D, 0.044D, darkGreen[0],
			darkGreen[1], darkGreen[2]);
		drawCuboid(-0.012D, 0.128D, -0.014D, 0.055D, 0.146D, 0.014D, bone[0], bone[1],
			bone[2]);
	}

	private static void renderRoastDinnerPlateFood(String itemName) {
		float[] goldenSkin = new float[] { 0.86F, 0.46F, 0.12F };
		float[] roastMeat = itemName.contains("potroast") ? new float[] { 0.42F, 0.16F, 0.07F }
			: new float[] { 0.78F, 0.38F, 0.10F };
		float[] sear = darken(roastMeat, 0.62F);
		float[] bone = new float[] { 0.86F, 0.78F, 0.58F };
		float[] potato = new float[] { 0.82F, 0.62F, 0.30F };
		float[] carrot = new float[] { 0.86F, 0.34F, 0.08F };
		float[] greens = new float[] { 0.16F, 0.46F, 0.12F };
		float[] gravy = new float[] { 0.34F, 0.14F, 0.04F };

		if (itemName.contains("potroast")) {
			drawCuboid(-0.185D, 0.064D, -0.088D, 0.085D, 0.116D, 0.088D, sear);
			drawCuboid(-0.150D, 0.118D, -0.064D, 0.120D, 0.162D, 0.064D, roastMeat);
			drawCuboid(-0.060D, 0.164D, -0.038D, 0.052D, 0.184D, 0.038D, gravy);
			drawCuboid(0.116D, 0.064D, -0.105D, 0.184D, 0.118D, -0.040D, potato);
			drawCuboid(0.130D, 0.120D, -0.086D, 0.196D, 0.140D, -0.028D, brighten(potato, 1.08F));
			drawCuboid(0.115D, 0.064D, 0.018D, 0.192D, 0.116D, 0.078D, carrot);
			drawCuboid(0.030D, 0.064D, 0.070D, 0.125D, 0.104D, 0.118D, greens);
			return;
		}

		drawCuboid(-0.160D, 0.064D, -0.086D, 0.090D, 0.114D, 0.086D, sear);
		drawCuboid(-0.135D, 0.116D, -0.066D, 0.110D, 0.166D, 0.066D, goldenSkin);
		drawCuboid(-0.076D, 0.168D, -0.046D, 0.048D, 0.190D, 0.046D, brighten(goldenSkin, 1.10F));
		drawCuboid(0.092D, 0.076D, -0.104D, 0.178D, 0.132D, -0.036D, goldenSkin);
		drawCuboid(0.164D, 0.088D, -0.076D, 0.218D, 0.110D, -0.050D, bone);
		drawCuboid(0.082D, 0.074D, 0.034D, 0.166D, 0.126D, 0.104D, goldenSkin);
		drawCuboid(0.150D, 0.086D, 0.052D, 0.204D, 0.108D, 0.078D, bone);
		drawCuboid(-0.208D, 0.064D, -0.102D, -0.142D, 0.116D, -0.040D, potato);
		drawCuboid(-0.216D, 0.064D, 0.018D, -0.140D, 0.116D, 0.084D, greens);
		drawCuboid(-0.112D, 0.064D, 0.072D, -0.032D, 0.110D, 0.120D, carrot);
	}

	private static void renderSaucedPlateFood(String itemName) {
		float[] rice = new float[] { 0.86F, 0.80F, 0.62F };
		float[] sauce = containsAny(itemName, "sweetandsour", "orangechicken")
			? new float[] { 0.86F, 0.30F, 0.08F }
			: inferFoodColor(itemName);
		float[] green = new float[] { 0.18F, 0.46F, 0.13F };
		float[] meat = containsAny(itemName, "chicken", "turkey") ? new float[] { 0.72F, 0.48F, 0.24F }
			: new float[] { 0.48F, 0.20F, 0.12F };

		drawCuboid(-0.150D, 0.064D, -0.100D, 0.150D, 0.092D, 0.100D, rice[0], rice[1], rice[2]);
		drawCuboid(-0.115D, 0.094D, -0.070D, -0.035D, 0.145D, 0.020D, meat[0], meat[1],
			meat[2]);
		drawCuboid(-0.020D, 0.096D, -0.085D, 0.070D, 0.150D, -0.010D, sauce[0], sauce[1],
			sauce[2]);
		drawCuboid(0.060D, 0.092D, 0.010D, 0.145D, 0.140D, 0.082D, sauce[0], sauce[1],
			sauce[2]);
		drawCuboid(-0.135D, 0.146D, 0.030D, -0.065D, 0.164D, 0.075D, green[0], green[1],
			green[2]);
		drawCuboid(0.005D, 0.151D, 0.030D, 0.070D, 0.168D, 0.075D, green[0], green[1],
			green[2]);
	}

	private static void renderVegetablePlateFood(String itemName) {
		float[] food = inferFoodColor(itemName);
		float[] sear = containsAny(itemName, "grilled", "glazed")
			? new float[] { 0.48F, 0.24F, 0.08F }
			: darken(food, 0.72F);
		float[] accent = getFoodAccentColor(itemName, food);
		float[] pale = containsAny(itemName, "stuffed", "herbbutter")
			? new float[] { 0.86F, 0.76F, 0.46F }
			: brighten(food, 1.12F);

		drawCuboid(-0.170D, 0.064D, -0.090D, -0.065D, 0.120D, 0.080D, food[0], food[1],
			food[2]);
		drawCuboid(-0.154D, 0.122D, -0.068D, -0.080D, 0.146D, 0.058D, pale[0], pale[1],
			pale[2]);
		drawCuboid(-0.020D, 0.064D, -0.105D, 0.092D, 0.112D, 0.025D, sear[0], sear[1],
			sear[2]);
		drawCuboid(-0.002D, 0.114D, -0.090D, 0.112D, 0.142D, 0.042D, food[0], food[1],
			food[2]);
		drawCuboid(0.075D, 0.064D, 0.020D, 0.170D, 0.130D, 0.100D, accent[0], accent[1],
			accent[2]);
		drawCuboid(0.102D, 0.132D, 0.034D, 0.152D, 0.154D, 0.082D, pale[0], pale[1],
			pale[2]);
	}

	private static void renderStackedPlateFood(String itemName) {
		if (equalsAny(itemName, "beansontoastitem", "cheeseontoastitem", "cinnamontoastitem",
				"frenchtoastitem", "toastitem", "toastedwesternitem", "vegemiteontoastitem")) {
			renderToastPlateFood(itemName);
			return;
		}

		float[] bread = containsAny(itemName, "pancake", "waffle") ? new float[] { 0.76F, 0.50F, 0.22F }
			: new float[] { 0.72F, 0.47F, 0.24F };
		float[] filling = containsAny(itemName, "pancake", "waffle") ? new float[] { 0.88F, 0.67F, 0.24F }
			: inferFoodColor(itemName);
		float[] green = new float[] { 0.14F, 0.46F, 0.12F };
		float[] cheese = new float[] { 0.90F, 0.72F, 0.18F };

		drawCuboid(-0.185D, 0.064D, -0.112D, 0.185D, 0.098D, 0.112D, bread[0], bread[1],
			bread[2]);
		drawCuboid(-0.168D, 0.100D, -0.102D, 0.168D, 0.126D, 0.102D, filling[0], filling[1],
			filling[2]);
		drawCuboid(-0.178D, 0.128D, -0.108D, 0.178D, 0.144D, 0.108D, green[0], green[1],
			green[2]);
		drawCuboid(-0.160D, 0.146D, -0.094D, 0.160D, 0.162D, 0.094D, cheese[0], cheese[1],
			cheese[2]);
		drawCuboid(-0.180D, 0.164D, -0.106D, 0.180D, 0.198D, 0.106D, bread[0], bread[1],
			bread[2]);
	}

	private static void renderToastPlateFood(String itemName) {
		float[] crust = new float[] { 0.58F, 0.34F, 0.14F };
		float[] toast = new float[] { 0.78F, 0.58F, 0.28F };
		float[] topping = itemName.contains("beansontoast") ? new float[] { 0.72F, 0.18F, 0.06F }
			: itemName.contains("cheeseontoast") ? new float[] { 0.90F, 0.72F, 0.18F }
				: itemName.contains("cinnamontoast") ? new float[] { 0.34F, 0.18F, 0.08F }
					: itemName.contains("vegemite") ? new float[] { 0.12F, 0.06F, 0.02F }
						: new float[] { 0.92F, 0.80F, 0.34F };
		float[] garnish = itemName.contains("toastedwestern") ? new float[] { 0.70F, 0.12F, 0.08F }
			: brighten(topping, 1.12F);

		drawCuboid(-0.190D, 0.064D, -0.112D, 0.024D, 0.096D, 0.112D, crust);
		drawCuboid(-0.166D, 0.098D, -0.088D, 0.002D, 0.128D, 0.088D, toast);
		drawCuboid(-0.128D, 0.130D, -0.062D, -0.030D, 0.150D, 0.062D, topping);
		drawCuboid(0.040D, 0.064D, -0.108D, 0.206D, 0.094D, 0.108D, crust);
		drawCuboid(0.060D, 0.096D, -0.084D, 0.184D, 0.126D, 0.084D, toast);
		drawCuboid(0.080D, 0.128D, -0.056D, 0.164D, 0.148D, 0.056D, topping);

		if (!itemName.equals("toastitem")) {
			drawCuboid(-0.106D, 0.152D, -0.030D, -0.070D, 0.168D, 0.002D, garnish);
			drawCuboid(0.118D, 0.150D, 0.022D, 0.154D, 0.166D, 0.054D, garnish);
		}
	}

	private static void renderPancakePlateFood(String itemName) {
		float[] cake = itemName.contains("waffle") ? new float[] { 0.72F, 0.46F, 0.18F }
			: new float[] { 0.78F, 0.54F, 0.24F };
		float[] browned = darken(cake, 0.74F);
		float[] syrup = containsAny(itemName, "blueberry") ? new float[] { 0.24F, 0.10F, 0.42F }
			: new float[] { 0.46F, 0.20F, 0.06F };
		float[] butter = new float[] { 0.94F, 0.78F, 0.22F };

		drawCuboid(-0.170D, 0.064D, -0.105D, 0.170D, 0.090D, 0.105D, browned[0], browned[1],
			browned[2]);
		drawCuboid(-0.188D, 0.090D, -0.115D, 0.188D, 0.118D, 0.115D, cake[0], cake[1],
			cake[2]);
		drawCuboid(-0.165D, 0.118D, -0.098D, 0.165D, 0.144D, 0.098D, browned[0], browned[1],
			browned[2]);
		drawCuboid(-0.180D, 0.144D, -0.108D, 0.180D, 0.172D, 0.108D, cake[0], cake[1],
			cake[2]);
		drawCuboid(-0.092D, 0.174D, -0.052D, 0.092D, 0.194D, 0.052D, syrup[0], syrup[1],
			syrup[2]);
		drawCuboid(-0.030D, 0.196D, -0.026D, 0.040D, 0.218D, 0.032D, butter[0], butter[1],
			butter[2]);
		if (itemName.contains("waffle")) {
			drawCuboid(-0.132D, 0.198D, -0.082D, 0.132D, 0.206D, -0.062D, browned[0],
				browned[1], browned[2]);
			drawCuboid(-0.132D, 0.198D, 0.062D, 0.132D, 0.206D, 0.082D, browned[0],
				browned[1], browned[2]);
			drawCuboid(-0.072D, 0.198D, -0.090D, -0.052D, 0.206D, 0.090D, browned[0],
				browned[1], browned[2]);
			drawCuboid(0.052D, 0.198D, -0.090D, 0.072D, 0.206D, 0.090D, browned[0],
				browned[1], browned[2]);
		}
	}

	private static void renderBurgerPlateFood(String itemName) {
		float[] bun = new float[] { 0.72F, 0.44F, 0.18F };
		float[] meat = new float[] { 0.38F, 0.14F, 0.08F };
		float[] cheese = new float[] { 0.90F, 0.72F, 0.18F };
		float[] green = new float[] { 0.14F, 0.46F, 0.12F };
		float[] red = new float[] { 0.70F, 0.10F, 0.08F };

		if (containsAny(itemName, "hotdog", "footlong")) {
			drawCuboid(-0.220D, 0.064D, -0.065D, 0.220D, 0.102D, 0.065D, bun[0], bun[1],
				bun[2]);
			drawCuboid(-0.188D, 0.104D, -0.034D, 0.188D, 0.150D, 0.034D, meat[0], meat[1],
				meat[2]);
			drawCuboid(-0.170D, 0.152D, -0.014D, 0.170D, 0.166D, 0.014D, cheese[0],
				cheese[1], cheese[2]);
			drawCuboid(-0.130D, 0.168D, -0.020D, -0.060D, 0.184D, 0.020D, red[0], red[1],
				red[2]);
			drawCuboid(0.060D, 0.168D, -0.020D, 0.130D, 0.184D, 0.020D, red[0], red[1],
				red[2]);
			return;
		}

		float[] bunDark = darken(bun, 0.76F);
		float[] sesame = new float[] { 0.94F, 0.78F, 0.42F };

		drawCuboid(-0.128D, 0.064D, -0.114D, 0.128D, 0.088D, 0.114D, bunDark[0], bunDark[1],
			bunDark[2]);
		drawCuboid(-0.160D, 0.076D, -0.072D, 0.160D, 0.100D, 0.072D, bun[0], bun[1],
			bun[2]);
		drawCuboid(-0.150D, 0.102D, -0.118D, 0.150D, 0.130D, 0.118D, meat[0], meat[1],
			meat[2]);
		drawCuboid(-0.176D, 0.108D, -0.072D, 0.176D, 0.132D, 0.072D, darken(meat, 0.84F)[0],
			darken(meat, 0.84F)[1], darken(meat, 0.84F)[2]);
		drawCuboid(-0.122D, 0.134D, -0.094D, 0.122D, 0.148D, 0.094D, cheese[0], cheese[1],
			cheese[2]);
		drawCuboid(-0.154D, 0.136D, -0.044D, -0.112D, 0.152D, 0.008D, cheese[0], cheese[1],
			cheese[2]);
		drawCuboid(0.112D, 0.136D, -0.012D, 0.154D, 0.152D, 0.044D, cheese[0], cheese[1],
			cheese[2]);
		drawCuboid(-0.164D, 0.150D, -0.098D, 0.164D, 0.166D, -0.060D, green[0], green[1],
			green[2]);
		drawCuboid(-0.168D, 0.150D, 0.058D, 0.168D, 0.166D, 0.098D, green[0], green[1],
			green[2]);
		drawCuboid(-0.094D, 0.168D, -0.092D, -0.010D, 0.186D, -0.018D, red[0], red[1],
			red[2]);
		drawCuboid(0.024D, 0.168D, 0.018D, 0.108D, 0.186D, 0.092D, red[0], red[1],
			red[2]);
		drawCuboid(-0.142D, 0.188D, -0.104D, 0.142D, 0.218D, 0.104D, bun[0], bun[1],
			bun[2]);
		drawCuboid(-0.108D, 0.218D, -0.078D, 0.108D, 0.242D, 0.078D, brighten(bun, 1.08F)[0],
			brighten(bun, 1.08F)[1], brighten(bun, 1.08F)[2]);
		drawCuboid(-0.052D, 0.244D, -0.032D, -0.020D, 0.252D, -0.010D, sesame[0], sesame[1],
			sesame[2]);
		drawCuboid(0.022D, 0.244D, -0.006D, 0.054D, 0.252D, 0.016D, sesame[0], sesame[1],
			sesame[2]);
		drawCuboid(-0.010D, 0.244D, 0.040D, 0.022D, 0.252D, 0.062D, sesame[0], sesame[1],
			sesame[2]);
	}

	private static void renderComboPlateFood(String itemName) {
		float[] bread = new float[] { 0.72F, 0.48F, 0.22F };
		float[] meat = new float[] { 0.50F, 0.20F, 0.10F };
		float[] cheese = new float[] { 0.90F, 0.72F, 0.20F };
		float[] green = new float[] { 0.16F, 0.46F, 0.12F };
		float[] fried = new float[] { 0.90F, 0.68F, 0.18F };
		float[] red = new float[] { 0.68F, 0.10F, 0.08F };

		if (itemName.contains("mcpam")) {
			drawCuboid(-0.195D, 0.064D, -0.082D, -0.020D, 0.090D, 0.082D, darken(bread, 0.70F)[0],
				darken(bread, 0.70F)[1], darken(bread, 0.70F)[2]);
			drawCuboid(-0.175D, 0.092D, -0.066D, -0.040D, 0.116D, 0.066D, meat[0], meat[1],
				meat[2]);
			drawCuboid(-0.178D, 0.118D, -0.070D, -0.038D, 0.134D, 0.070D, cheese[0],
				cheese[1], cheese[2]);
			drawCuboid(-0.188D, 0.136D, -0.078D, -0.026D, 0.166D, 0.078D, bread[0], bread[1],
				bread[2]);
			drawCuboid(0.042D, 0.064D, -0.122D, 0.078D, 0.178D, -0.006D, fried[0], fried[1],
				fried[2]);
			drawCuboid(0.102D, 0.064D, -0.092D, 0.138D, 0.162D, 0.038D, fried[0], fried[1],
				fried[2]);
			drawCuboid(0.164D, 0.064D, -0.066D, 0.200D, 0.150D, 0.074D, fried[0], fried[1],
				fried[2]);
			return;
		}

		if (itemName.contains("ploughmans")) {
			drawCuboid(-0.196D, 0.064D, -0.110D, -0.060D, 0.124D, 0.050D, bread[0], bread[1],
				bread[2]);
			drawCuboid(-0.176D, 0.126D, -0.084D, -0.084D, 0.146D, 0.032D, darken(bread, 0.74F)[0],
				darken(bread, 0.74F)[1], darken(bread, 0.74F)[2]);
			drawCuboid(-0.015D, 0.064D, -0.090D, 0.100D, 0.126D, 0.020D, cheese[0], cheese[1],
				cheese[2]);
			drawCuboid(0.128D, 0.064D, -0.090D, 0.205D, 0.130D, -0.012D, red[0], red[1],
				red[2]);
			drawCuboid(0.110D, 0.064D, 0.042D, 0.205D, 0.100D, 0.086D, green[0], green[1],
				green[2]);
			return;
		}

		drawCuboid(-0.198D, 0.064D, -0.088D, -0.020D, 0.126D, 0.088D, darken(meat, 0.72F)[0],
			darken(meat, 0.72F)[1], darken(meat, 0.72F)[2]);
		drawCuboid(-0.178D, 0.128D, -0.066D, -0.042D, 0.152D, 0.066D, meat[0], meat[1],
			meat[2]);
		drawCuboid(0.026D, 0.064D, -0.110D, 0.115D, 0.124D, -0.024D, fried[0], fried[1],
			fried[2]);
		drawCuboid(0.044D, 0.126D, -0.092D, 0.100D, 0.146D, -0.042D, brighten(fried, 1.08F)[0],
			brighten(fried, 1.08F)[1], brighten(fried, 1.08F)[2]);
		drawCuboid(0.042D, 0.064D, 0.030D, 0.138D, 0.112D, 0.110D, green[0], green[1],
			green[2]);
		drawCuboid(0.152D, 0.064D, 0.010D, 0.210D, 0.126D, 0.072D, red[0], red[1], red[2]);
	}

	private static void renderWrapPlateFood(String itemName) {
		float[] tortilla = new float[] { 0.78F, 0.62F, 0.34F };
		float[] browned = new float[] { 0.56F, 0.32F, 0.12F };
		float[] meat = inferFoodColor(itemName);
		float[] green = new float[] { 0.14F, 0.46F, 0.12F };
		float[] tomato = new float[] { 0.70F, 0.10F, 0.08F };
		float[] cheese = new float[] { 0.90F, 0.72F, 0.18F };

		if (containsAny(itemName, "nachos", "nachoes", "tostada")) {
			drawCuboid(-0.170D, 0.064D, -0.104D, -0.055D, 0.102D, -0.012D, tortilla[0],
				tortilla[1], tortilla[2]);
			drawCuboid(-0.035D, 0.070D, -0.080D, 0.080D, 0.108D, 0.020D, browned[0],
				browned[1], browned[2]);
			drawCuboid(0.060D, 0.064D, 0.000D, 0.170D, 0.102D, 0.098D, tortilla[0],
				tortilla[1], tortilla[2]);
			drawCuboid(-0.060D, 0.110D, -0.030D, 0.050D, 0.134D, 0.050D, cheese[0],
				cheese[1], cheese[2]);
			drawCuboid(0.056D, 0.126D, 0.030D, 0.118D, 0.144D, 0.076D, tomato[0], tomato[1],
				tomato[2]);
			return;
		}

		drawCuboid(-0.205D, 0.064D, -0.070D, 0.205D, 0.104D, 0.070D, browned[0], browned[1],
			browned[2]);
		drawCuboid(-0.190D, 0.104D, -0.090D, 0.190D, 0.152D, 0.090D, tortilla[0],
			tortilla[1], tortilla[2]);
		drawCuboid(-0.150D, 0.154D, -0.040D, -0.030D, 0.178D, 0.040D, meat[0], meat[1],
			meat[2]);
		drawCuboid(-0.010D, 0.154D, -0.052D, 0.084D, 0.176D, 0.052D, green[0], green[1],
			green[2]);
		drawCuboid(0.078D, 0.156D, -0.030D, 0.146D, 0.178D, 0.030D, tomato[0], tomato[1],
			tomato[2]);
		drawCuboid(-0.174D, 0.180D, -0.018D, 0.170D, 0.194D, 0.018D, cheese[0], cheese[1],
			cheese[2]);
	}

	private static void renderPastaPlateFood(String itemName) {
		float[] pasta = new float[] { 0.82F, 0.68F, 0.38F };
		float[] sauce = containsAny(itemName, "lasagna", "spagetti", "spaghetti")
			? new float[] { 0.70F, 0.12F, 0.08F }
			: inferFoodColor(itemName);
		float[] cheese = new float[] { 0.90F, 0.72F, 0.18F };
		float[] herb = new float[] { 0.16F, 0.42F, 0.12F };
		float[] meat = new float[] { 0.38F, 0.14F, 0.08F };

		if (itemName.contains("lasagna")) {
			drawCuboid(-0.180D, 0.064D, -0.108D, 0.180D, 0.092D, 0.108D, pasta[0], pasta[1],
				pasta[2]);
			drawCuboid(-0.165D, 0.094D, -0.096D, 0.165D, 0.118D, 0.096D, sauce[0],
				sauce[1], sauce[2]);
			drawCuboid(-0.175D, 0.120D, -0.102D, 0.175D, 0.146D, 0.102D, pasta[0],
				pasta[1], pasta[2]);
			drawCuboid(-0.145D, 0.148D, -0.080D, 0.145D, 0.166D, 0.080D, cheese[0],
				cheese[1], cheese[2]);
			return;
		}

		drawCuboid(-0.160D, 0.064D, -0.100D, 0.160D, 0.088D, 0.100D, darken(pasta, 0.72F)[0],
			darken(pasta, 0.72F)[1], darken(pasta, 0.72F)[2]);
		drawCuboid(-0.185D, 0.088D, -0.085D, 0.145D, 0.112D, -0.050D, pasta[0], pasta[1],
			pasta[2]);
		drawCuboid(-0.145D, 0.114D, -0.025D, 0.185D, 0.138D, 0.010D, pasta[0], pasta[1],
			pasta[2]);
		drawCuboid(-0.175D, 0.140D, 0.045D, 0.155D, 0.164D, 0.080D, pasta[0], pasta[1],
			pasta[2]);
		drawCuboid(-0.065D, 0.166D, -0.045D, 0.080D, 0.190D, 0.050D, sauce[0], sauce[1],
			sauce[2]);
		if (containsAny(itemName, "meatball", "meatballs")) {
			drawCuboid(-0.120D, 0.168D, -0.018D, -0.066D, 0.212D, 0.036D, meat[0], meat[1],
				meat[2]);
			drawCuboid(0.095D, 0.166D, 0.020D, 0.150D, 0.210D, 0.074D, meat[0], meat[1],
				meat[2]);
		}
		drawCuboid(0.012D, 0.190D, -0.080D, 0.078D, 0.206D, -0.040D, herb[0], herb[1],
			herb[2]);
	}

	private static void renderSmallRollPlateFood(String itemName) {
		float[] wrapper = containsAny(itemName, "sushi", "futomaki", "californiaroll")
			? new float[] { 0.08F, 0.12F, 0.08F }
			: new float[] { 0.78F, 0.62F, 0.34F };
		float[] filling = containsAny(itemName, "sushi", "futomaki", "californiaroll")
			? new float[] { 0.86F, 0.80F, 0.62F }
			: inferFoodColor(itemName);
		float[] accent = containsAny(itemName, "sushi", "futomaki", "californiaroll")
			? new float[] { 0.72F, 0.14F, 0.08F }
			: getFoodAccentColor(itemName, filling);

		drawCuboid(-0.170D, 0.064D, -0.088D, -0.075D, 0.132D, 0.006D, wrapper[0],
			wrapper[1], wrapper[2]);
		drawCuboid(-0.150D, 0.132D, -0.068D, -0.095D, 0.156D, -0.014D, filling[0],
			filling[1], filling[2]);
		drawCuboid(-0.010D, 0.064D, -0.070D, 0.085D, 0.132D, 0.025D, wrapper[0],
			wrapper[1], wrapper[2]);
		drawCuboid(0.010D, 0.132D, -0.050D, 0.064D, 0.156D, 0.006D, filling[0], filling[1],
			filling[2]);
		drawCuboid(0.090D, 0.064D, 0.018D, 0.172D, 0.126D, 0.098D, wrapper[0], wrapper[1],
			wrapper[2]);
		drawCuboid(0.110D, 0.128D, 0.036D, 0.154D, 0.150D, 0.080D, accent[0], accent[1],
			accent[2]);
	}

	private static void renderBreakfastPlateFood(String itemName) {
		float[] egg = new float[] { 0.92F, 0.90F, 0.74F };
		float[] yolk = new float[] { 0.94F, 0.66F, 0.12F };
		float[] meat = new float[] { 0.44F, 0.16F, 0.08F };
		float[] hash = new float[] { 0.74F, 0.50F, 0.22F };

		drawCuboid(-0.175D, 0.064D, -0.094D, -0.030D, 0.100D, 0.040D, egg[0], egg[1],
			egg[2]);
		drawCuboid(-0.122D, 0.102D, -0.038D, -0.066D, 0.132D, 0.016D, yolk[0], yolk[1],
			yolk[2]);
		drawCuboid(0.030D, 0.064D, -0.098D, 0.175D, 0.092D, -0.058D, meat[0], meat[1],
			meat[2]);
		drawCuboid(0.014D, 0.098D, -0.044D, 0.158D, 0.126D, -0.004D, meat[0], meat[1],
			meat[2]);
		drawCuboid(-0.020D, 0.064D, 0.040D, 0.158D, 0.108D, 0.118D, hash[0], hash[1],
			hash[2]);
		drawCuboid(0.016D, 0.110D, 0.058D, 0.128D, 0.132D, 0.102D, darken(hash, 0.72F)[0],
			darken(hash, 0.72F)[1], darken(hash, 0.72F)[2]);
	}

	private static void renderSeafoodPlateFood(String itemName) {
		float[] fish = inferFoodColor(itemName);
		float[] pale = brighten(fish, 1.12F);
		float[] shell = containsAny(itemName, "crab", "crayfish", "lobster", "shrimp")
			? new float[] { 0.82F, 0.28F, 0.12F }
			: darken(fish, 0.70F);
		float[] lemon = new float[] { 0.92F, 0.78F, 0.20F };

		if (containsAny(itemName, "crab", "crayfish", "lobster", "shrimp")) {
			drawCuboid(-0.170D, 0.064D, -0.072D, -0.045D, 0.118D, 0.072D, shell[0],
				shell[1], shell[2]);
			drawCuboid(-0.035D, 0.074D, -0.055D, 0.082D, 0.124D, 0.055D, shell[0],
				shell[1], shell[2]);
			drawCuboid(0.090D, 0.066D, -0.035D, 0.175D, 0.108D, 0.035D, shell[0],
				shell[1], shell[2]);
			drawCuboid(-0.110D, 0.122D, -0.035D, -0.056D, 0.144D, 0.035D, pale[0],
				pale[1], pale[2]);
		} else {
			drawCuboid(-0.185D, 0.064D, -0.074D, 0.175D, 0.102D, 0.074D, darken(fish, 0.72F)[0],
				darken(fish, 0.72F)[1], darken(fish, 0.72F)[2]);
			drawCuboid(-0.155D, 0.104D, -0.056D, 0.138D, 0.136D, 0.056D, fish[0], fish[1],
				fish[2]);
			drawCuboid(-0.120D, 0.138D, -0.038D, -0.050D, 0.154D, 0.038D, pale[0],
				pale[1], pale[2]);
			drawCuboid(0.018D, 0.138D, -0.038D, 0.088D, 0.154D, 0.038D, pale[0],
				pale[1], pale[2]);
		}
		drawCuboid(0.090D, 0.146D, 0.052D, 0.160D, 0.166D, 0.092D, lemon[0], lemon[1],
			lemon[2]);
	}

	private static void renderMeatPlateFood(String itemName) {
		float[] meat = inferFoodColor(itemName);
		float[] sear = darken(meat, 0.62F);
		float[] glaze = containsAny(itemName, "honey", "maple", "bbq", "char") ? new float[] { 0.72F, 0.32F, 0.08F }
			: brighten(meat, 1.10F);
		float[] bone = new float[] { 0.84F, 0.78F, 0.58F };

		if (containsAny(itemName, "ribs", "wings")) {
			drawCuboid(-0.180D, 0.064D, -0.088D, -0.060D, 0.116D, -0.012D, meat[0], meat[1],
				meat[2]);
			drawCuboid(-0.150D, 0.118D, -0.052D, -0.025D, 0.136D, -0.032D, bone[0],
				bone[1], bone[2]);
			drawCuboid(-0.010D, 0.064D, -0.064D, 0.118D, 0.116D, 0.010D, glaze[0],
				glaze[1], glaze[2]);
			drawCuboid(0.022D, 0.118D, -0.028D, 0.150D, 0.136D, -0.008D, bone[0],
				bone[1], bone[2]);
			drawCuboid(0.065D, 0.064D, 0.030D, 0.176D, 0.112D, 0.100D, meat[0], meat[1],
				meat[2]);
			return;
		}

		if (containsAny(itemName, "bacon", "jerky", "sausage", "chorizo", "pepperoni")) {
			drawCuboid(-0.190D, 0.064D, -0.090D, 0.140D, 0.096D, -0.050D, sear[0], sear[1],
				sear[2]);
			drawCuboid(-0.150D, 0.102D, -0.030D, 0.180D, 0.134D, 0.010D, meat[0], meat[1],
				meat[2]);
			drawCuboid(-0.180D, 0.140D, 0.030D, 0.150D, 0.172D, 0.070D, glaze[0],
				glaze[1], glaze[2]);
			return;
		}

		drawCuboid(-0.175D, 0.064D, -0.090D, 0.080D, 0.120D, 0.090D, sear[0], sear[1],
			sear[2]);
		drawCuboid(-0.140D, 0.122D, -0.064D, 0.105D, 0.154D, 0.064D, meat[0], meat[1],
			meat[2]);
		drawCuboid(0.090D, 0.064D, -0.064D, 0.170D, 0.136D, 0.064D, meat[0], meat[1],
			meat[2]);
		drawCuboid(-0.055D, 0.156D, -0.040D, 0.035D, 0.174D, 0.040D, glaze[0], glaze[1],
			glaze[2]);
	}

	private static void renderBakedPlateFood(String itemName) {
		if (itemName.contains("chaoscookie")) {
			renderDecoratedCookieFood(new float[] { 0.18F, 0.12F, 0.24F },
				new float[] { 0.64F, 0.38F, 0.90F });
			return;
		}

		if (itemName.contains("lavendershortbread")) {
			renderDecoratedCookieFood(new float[] { 0.76F, 0.66F, 0.92F },
				new float[] { 0.46F, 0.26F, 0.66F });
			return;
		}

		if (itemName.contains("fairybread")) {
			renderFairyBreadFood();
			return;
		}

		if (containsAny(itemName, "cookie", "shortbread", "snaps")) {
			renderCookiePlateFood(itemName);
		} else if (itemName.contains("donut")) {
			renderDonutPlateFood(itemName);
		} else if (containsAny(itemName, "ricecake", "mochi", "manjuu")) {
			renderRiceCakePlateFood(itemName);
		} else if (containsAny(itemName, "pie", "quiche", "pasty")) {
			renderPiePlateFood(itemName);
		} else if (containsAny(itemName, "cake", "cheesecake", "cupcake", "muffin", "lamington",
				"pavlova")) {
			renderCakePlateFood(itemName);
		} else if (containsAny(itemName, "roll", "honeybun", "spicebun")) {
			renderBakedRollPlateFood(itemName);
		} else if (itemName.contains("pretzel")) {
			renderPretzelPlateFood(itemName);
		} else if (itemName.contains("croissant")) {
			renderCroissantPlateFood();
		} else if (containsAny(itemName, "bread", "biscuit", "cracker", "scone", "naan", "damper",
				"cornbread", "gingerbread")) {
			renderBreadPlateFood(itemName);
		} else if (containsAny(itemName, "fritter", "hushpuppies", "zeppole", "potatocakes")) {
			renderFriedBitesPlateFood(itemName);
		} else if (containsAny(itemName, "pudding", "trifle", "bananasplit", "yorkshire")) {
			renderPuddingPlateFood(itemName);
		} else if (containsAny(itemName, "baklava", "brownie", "baritem", "cobbler", "crumble",
				"jaffa", "tart", "timtam")) {
			renderSquareSweetPlateFood(itemName);
		} else if (containsAny(itemName, "candiedginger", "candiedlemon", "poachedpear")) {
			renderCandiedFruitPlateFood(itemName);
		} else {
			renderPastryFallbackFood(itemName);
		}
	}

	private static void renderCookiePlateFood(String itemName) {
		if (itemName.contains("creamcookie")) {
			float[] cookie = new float[] { 0.18F, 0.09F, 0.04F };
			float[] cream = new float[] { 0.88F, 0.82F, 0.64F };

			renderSmallCookieDisc(0.000D, 0.000D, 0.155D, 0.122D, 0.064D, 0.030D,
				darken(cookie, 0.60F), cookie);
			drawCuboid(-0.130D, 0.096D, -0.090D, 0.130D, 0.116D, 0.090D, cream);
			renderSmallCookieDisc(0.000D, 0.000D, 0.145D, 0.112D, 0.118D, 0.026D,
				darken(cookie, 0.68F), cookie);
			return;
		}

		float[] cookie = containsAny(itemName, "peanutbutter") ? new float[] { 0.62F, 0.44F, 0.18F }
			: containsAny(itemName, "raisin") ? new float[] { 0.62F, 0.42F, 0.20F }
				: new float[] { 0.68F, 0.48F, 0.24F };
		float[] chip = containsAny(itemName, "raisin") ? new float[] { 0.22F, 0.10F, 0.28F }
			: new float[] { 0.18F, 0.08F, 0.03F };

		renderSmallCookieDisc(-0.072D, -0.040D, 0.108D, 0.086D, 0.064D, 0.034D,
			darken(cookie, 0.70F), cookie);
		renderSmallCookieDisc(0.078D, 0.020D, 0.104D, 0.084D, 0.070D, 0.032D,
			darken(cookie, 0.70F), brighten(cookie, 1.06F));
		drawCuboid(-0.128D, 0.102D, -0.072D, -0.102D, 0.116D, -0.048D, chip);
		drawCuboid(-0.048D, 0.102D, -0.018D, -0.022D, 0.116D, 0.006D, chip);
		drawCuboid(0.040D, 0.106D, -0.010D, 0.066D, 0.120D, 0.014D, chip);
		drawCuboid(0.100D, 0.106D, 0.040D, 0.126D, 0.120D, 0.064D, chip);

		if (itemName.contains("peanutbutter")) {
			float[] fork = darken(cookie, 0.62F);
			drawCuboid(-0.128D, 0.118D, 0.036D, -0.010D, 0.128D, 0.052D, fork);
			drawCuboid(-0.070D, 0.118D, -0.028D, 0.044D, 0.128D, -0.012D, fork);
			drawCuboid(0.020D, 0.122D, 0.070D, 0.130D, 0.132D, 0.086D, fork);
		}
	}

	private static void renderDonutPlateFood(String itemName) {
		float[] dough = new float[] { 0.68F, 0.48F, 0.24F };
		float[] icing = null;

		if (itemName.contains("jellydonut")) {
			float[] paleDough = new float[] { 0.82F, 0.68F, 0.42F };
			float[] jelly = containsAny(itemName, "grape", "blueberry", "blackberry")
				? new float[] { 0.30F, 0.10F, 0.44F }
				: new float[] { 0.74F, 0.06F, 0.08F };

			renderSmallCookieDisc(0.000D, 0.000D, 0.160D, 0.125D, 0.064D, 0.052D,
				darken(paleDough, 0.72F), paleDough);
			drawCuboid(-0.050D, 0.118D, -0.035D, 0.054D, 0.142D, 0.038D, jelly);
			drawCuboid(-0.022D, 0.144D, -0.014D, 0.026D, 0.154D, 0.018D, brighten(jelly, 1.18F));
			return;
		}

		if (itemName.contains("chocolate")) {
			icing = new float[] { 0.18F, 0.08F, 0.03F };
		} else if (itemName.contains("frosted")) {
			icing = new float[] { 0.94F, 0.74F, 0.78F };
		} else if (itemName.contains("powdered")) {
			icing = new float[] { 0.90F, 0.86F, 0.74F };
		} else if (containsAny(itemName, "cinnamon", "sugar")) {
			icing = new float[] { 0.72F, 0.42F, 0.16F };
		}

		renderDonutRing(0.000D, 0.000D, dough, icing);
		if (itemName.contains("frosted")) {
			drawCuboid(-0.090D, 0.128D, -0.095D, -0.062D, 0.140D, -0.070D, 0.94F, 0.18F, 0.24F);
			drawCuboid(0.054D, 0.128D, -0.088D, 0.082D, 0.140D, -0.064D, 0.18F, 0.54F, 0.22F);
			drawCuboid(0.076D, 0.128D, 0.055D, 0.104D, 0.140D, 0.080D, 0.24F, 0.34F, 0.90F);
		}
	}

	private static void renderPiePlateFood(String itemName) {
		if (itemName.contains("pasty")) {
			renderPastyPlateFood();
			return;
		}

		float[] tin = new float[] { 0.46F, 0.46F, 0.44F };
		float[] crust = new float[] { 0.72F, 0.48F, 0.22F };
		float[] filling = getPieFillingColor(itemName);
		boolean savory = containsAny(itemName, "chickenpot", "cottagepie", "meatpie", "mincepie",
			"shepardspie", "spinachpie", "quiche");

		drawCuboid(-0.190D, 0.064D, -0.126D, 0.190D, 0.084D, 0.126D, darken(tin, 0.62F));
		drawCuboid(-0.170D, 0.086D, -0.110D, 0.170D, 0.112D, 0.110D, tin);
		drawCuboid(-0.150D, 0.114D, -0.098D, 0.150D, 0.144D, 0.098D, crust);
		drawCuboid(-0.112D, 0.146D, -0.070D, 0.112D, 0.168D, 0.070D, filling);
		drawCuboid(-0.170D, 0.146D, -0.112D, 0.170D, 0.170D, -0.084D, crust);
		drawCuboid(-0.170D, 0.146D, 0.084D, 0.170D, 0.170D, 0.112D, crust);
		drawCuboid(-0.170D, 0.146D, -0.112D, -0.142D, 0.170D, 0.112D, crust);
		drawCuboid(0.142D, 0.146D, -0.112D, 0.170D, 0.170D, 0.112D, crust);

		if (savory) {
			float[] topping = containsAny(itemName, "cottagepie", "shepardspie")
				? new float[] { 0.82F, 0.70F, 0.42F } : brighten(filling, 1.12F);
			drawCuboid(-0.086D, 0.170D, -0.054D, 0.086D, 0.188D, 0.054D, topping);
			drawCuboid(-0.134D, 0.172D, 0.036D, -0.072D, 0.188D, 0.078D, topping);
			drawCuboid(0.072D, 0.172D, -0.080D, 0.134D, 0.188D, -0.038D, topping);
		} else {
			drawCuboid(-0.116D, 0.170D, -0.020D, 0.116D, 0.184D, 0.002D, crust);
			drawCuboid(-0.016D, 0.170D, -0.070D, 0.006D, 0.184D, 0.070D, crust);
			drawCuboid(-0.080D, 0.186D, -0.052D, -0.044D, 0.200D, -0.018D, brighten(filling, 1.16F));
			drawCuboid(0.044D, 0.186D, 0.018D, 0.080D, 0.200D, 0.052D, brighten(filling, 1.16F));
		}
	}

	private static void renderCakePlateFood(String itemName) {
		if (containsAny(itemName, "cupcake", "muffin")) {
			renderMuffinPlateFood(itemName);
			return;
		}

		if (itemName.contains("pavlova")) {
			float[] meringue = new float[] { 0.90F, 0.86F, 0.72F };
			float[] cream = new float[] { 0.96F, 0.92F, 0.80F };
			float[] berry = new float[] { 0.76F, 0.08F, 0.08F };

			renderSmallCookieDisc(0.000D, 0.000D, 0.170D, 0.120D, 0.064D, 0.050D,
				darken(meringue, 0.72F), meringue);
			drawCuboid(-0.120D, 0.116D, -0.078D, 0.120D, 0.148D, 0.078D, cream);
			drawCuboid(-0.062D, 0.150D, -0.032D, -0.014D, 0.178D, 0.016D, berry);
			drawCuboid(0.022D, 0.150D, 0.018D, 0.070D, 0.178D, 0.066D, berry);
			return;
		}

		if (itemName.contains("lamington")) {
			float[] chocolate = new float[] { 0.20F, 0.10F, 0.04F };
			float[] coconut = new float[] { 0.88F, 0.84F, 0.70F };

			drawCuboid(-0.150D, 0.064D, -0.105D, 0.150D, 0.126D, 0.105D, darken(chocolate, 0.62F));
			drawCuboid(-0.132D, 0.128D, -0.088D, 0.132D, 0.166D, 0.088D, chocolate);
			drawCuboid(-0.100D, 0.168D, -0.060D, -0.070D, 0.184D, -0.030D, coconut);
			drawCuboid(-0.020D, 0.168D, 0.028D, 0.010D, 0.184D, 0.058D, coconut);
			drawCuboid(0.072D, 0.168D, -0.042D, 0.104D, 0.184D, -0.012D, coconut);
			return;
		}

		float[] cake = inferFoodColor(itemName);
		float[] frosting = getCakeFrostingColor(itemName);
		float[] crust = containsAny(itemName, "cheesecake") ? new float[] { 0.48F, 0.28F, 0.12F }
			: darken(cake, 0.72F);
		float[] topping = getFoodAccentColor(itemName, cake);

		drawCuboid(-0.170D, 0.064D, -0.105D, 0.170D, 0.094D, 0.105D, crust);
		drawCuboid(-0.150D, 0.096D, -0.090D, 0.150D, 0.132D, 0.090D, cake);
		drawCuboid(-0.150D, 0.134D, -0.090D, 0.150D, 0.154D, 0.090D, frosting);
		drawCuboid(-0.132D, 0.156D, -0.074D, 0.132D, 0.186D, 0.074D,
			containsAny(itemName, "cheesecake") ? frosting : brighten(cake, 1.08F));

		if (containsAny(itemName, "redvelvet", "holidaycake", "pineapple", "chocolate", "cheesecake")) {
			drawCuboid(-0.096D, 0.188D, -0.048D, -0.052D, 0.208D, -0.010D, topping);
			drawCuboid(0.042D, 0.188D, 0.010D, 0.086D, 0.208D, 0.050D, topping);
			if (itemName.contains("holidaycake")) {
				drawCuboid(-0.015D, 0.188D, 0.036D, 0.030D, 0.208D, 0.078D, 0.16F, 0.44F, 0.12F);
			}
		}
	}

	private static void renderMuffinPlateFood(String itemName) {
		float[] cake = inferFoodColor(itemName);
		float[] wrapper = new float[] { 0.58F, 0.50F, 0.42F };
		float[] top = containsAny(itemName, "blueberry", "durian", "pumpkin") ? brighten(cake, 1.08F)
			: new float[] { 0.82F, 0.62F, 0.30F };
		float[] dot = getFoodAccentColor(itemName, cake);

		drawCuboid(-0.168D, 0.064D, -0.070D, -0.070D, 0.116D, 0.052D, wrapper);
		drawCuboid(-0.184D, 0.118D, -0.088D, -0.052D, 0.158D, 0.070D, top);
		drawCuboid(0.012D, 0.064D, -0.072D, 0.110D, 0.116D, 0.052D, wrapper);
		drawCuboid(-0.004D, 0.118D, -0.090D, 0.128D, 0.158D, 0.070D, top);
		drawCuboid(0.128D, 0.064D, 0.000D, 0.196D, 0.108D, 0.080D, wrapper);
		drawCuboid(0.112D, 0.110D, -0.016D, 0.212D, 0.146D, 0.096D, top);
		drawCuboid(-0.132D, 0.160D, -0.030D, -0.100D, 0.174D, 0.000D, dot);
		drawCuboid(0.048D, 0.160D, 0.010D, 0.080D, 0.174D, 0.040D, dot);
	}

	private static void renderBakedRollPlateFood(String itemName) {
		float[] bread = new float[] { 0.70F, 0.44F, 0.18F };
		float[] filling = getFoodAccentColor(itemName, inferFoodColor(itemName));
		float[] dark = darken(bread, 0.62F);

		if (containsAny(itemName, "cinnamonroll", "honeybun")) {
			renderSmallCookieDisc(0.000D, 0.000D, 0.152D, 0.116D, 0.064D, 0.048D, dark, bread);
			drawCuboid(-0.104D, 0.116D, -0.020D, 0.102D, 0.136D, 0.006D, filling);
			drawCuboid(-0.022D, 0.138D, -0.070D, 0.006D, 0.156D, 0.070D, filling);
			drawCuboid(-0.062D, 0.158D, 0.040D, 0.060D, 0.174D, 0.064D, filling);
			return;
		}

		if (containsAny(itemName, "jamroll", "chocolateroll")) {
			float[] rollFilling = itemName.contains("chocolate") ? new float[] { 0.18F, 0.08F, 0.03F }
				: new float[] { 0.72F, 0.08F, 0.10F };

			drawCuboid(-0.205D, 0.064D, -0.060D, 0.145D, 0.128D, 0.060D, dark);
			drawCuboid(-0.180D, 0.130D, -0.048D, 0.170D, 0.164D, 0.048D, bread);
			drawCuboid(0.140D, 0.090D, -0.052D, 0.192D, 0.152D, 0.052D, rollFilling);
			drawCuboid(0.154D, 0.108D, -0.030D, 0.204D, 0.136D, 0.030D, brighten(bread, 1.10F));
			return;
		}

		if (itemName.contains("sausage")) {
			float[] sausage = new float[] { 0.42F, 0.15F, 0.08F };
			drawCuboid(-0.205D, 0.064D, -0.060D, 0.205D, 0.106D, 0.060D, dark);
			drawCuboid(-0.186D, 0.108D, -0.046D, 0.186D, 0.142D, 0.046D, bread);
			drawCuboid(-0.170D, 0.144D, -0.024D, 0.170D, 0.164D, 0.024D, sausage);
			return;
		}

		drawCuboid(-0.190D, 0.064D, -0.085D, -0.070D, 0.118D, 0.060D, dark);
		drawCuboid(-0.172D, 0.120D, -0.070D, -0.052D, 0.150D, 0.050D, bread);
		drawCuboid(-0.032D, 0.064D, -0.070D, 0.082D, 0.118D, 0.070D, bread);
		drawCuboid(-0.012D, 0.120D, -0.050D, 0.102D, 0.150D, 0.050D, brighten(bread, 1.08F));
		drawCuboid(0.108D, 0.064D, -0.048D, 0.190D, 0.112D, 0.068D, dark);
	}

	private static void renderPretzelPlateFood(String itemName) {
		float[] pretzel = new float[] { 0.68F, 0.40F, 0.14F };
		float[] salt = new float[] { 0.90F, 0.86F, 0.72F };
		float[] mustard = new float[] { 0.86F, 0.62F, 0.08F };

		drawCuboid(-0.188D, 0.064D, -0.090D, -0.042D, 0.100D, -0.052D, pretzel);
		drawCuboid(-0.188D, 0.064D, 0.052D, -0.042D, 0.100D, 0.090D, pretzel);
		drawCuboid(-0.192D, 0.094D, -0.072D, -0.154D, 0.148D, 0.072D, pretzel);
		drawCuboid(-0.076D, 0.094D, -0.072D, -0.038D, 0.148D, 0.072D, pretzel);
		drawCuboid(0.020D, 0.064D, -0.086D, 0.160D, 0.100D, -0.048D, pretzel);
		drawCuboid(0.020D, 0.064D, 0.048D, 0.160D, 0.100D, 0.086D, pretzel);
		drawCuboid(0.020D, 0.094D, -0.066D, 0.058D, 0.144D, 0.066D, pretzel);
		drawCuboid(0.122D, 0.094D, -0.066D, 0.160D, 0.144D, 0.066D, pretzel);
		drawCuboid(-0.120D, 0.150D, -0.016D, -0.090D, 0.164D, 0.012D, salt);
		drawCuboid(0.082D, 0.148D, 0.022D, 0.112D, 0.162D, 0.050D, salt);
		if (itemName.contains("mustard")) {
			drawCuboid(-0.030D, 0.066D, 0.092D, 0.048D, 0.086D, 0.140D, mustard);
		}
	}

	private static void renderCroissantPlateFood() {
		float[] crust = new float[] { 0.74F, 0.46F, 0.18F };
		float[] pale = new float[] { 0.88F, 0.62F, 0.26F };

		drawCuboid(-0.180D, 0.064D, -0.050D, -0.080D, 0.120D, 0.058D, crust);
		drawCuboid(-0.095D, 0.086D, -0.084D, 0.088D, 0.148D, 0.084D, pale);
		drawCuboid(0.074D, 0.064D, -0.050D, 0.180D, 0.120D, 0.058D, crust);
		drawCuboid(-0.154D, 0.122D, 0.040D, -0.084D, 0.146D, 0.072D, pale);
		drawCuboid(0.078D, 0.122D, -0.072D, 0.150D, 0.146D, -0.040D, pale);
	}

	private static void renderBreadPlateFood(String itemName) {
		float[] bread = containsAny(itemName, "cornbread", "honeybread") ? new float[] { 0.84F, 0.58F, 0.22F }
			: containsAny(itemName, "pumpkin", "zucchini") ? inferFoodColor(itemName)
				: new float[] { 0.70F, 0.46F, 0.20F };
		float[] crust = darken(bread, 0.66F);
		float[] crumb = brighten(bread, 1.18F);
		float[] accent = containsAny(itemName, "garlic") ? new float[] { 0.18F, 0.46F, 0.12F }
			: containsAny(itemName, "raisin", "nut", "chocolate") ? new float[] { 0.20F, 0.10F, 0.04F }
				: getFoodAccentColor(itemName, bread);

		drawCuboid(-0.205D, 0.064D, -0.072D, 0.155D, 0.108D, 0.072D, crust);
		drawCuboid(-0.180D, 0.110D, -0.060D, 0.180D, 0.150D, 0.060D, bread);
		drawCuboid(0.092D, 0.152D, -0.052D, 0.190D, 0.182D, 0.052D, crumb);
		drawCuboid(-0.130D, 0.152D, -0.040D, -0.082D, 0.168D, -0.008D, accent);
		drawCuboid(-0.030D, 0.152D, 0.012D, 0.020D, 0.168D, 0.044D, accent);
		drawCuboid(0.056D, 0.152D, -0.046D, 0.104D, 0.168D, -0.014D, accent);
	}

	private static void renderFriedBitesPlateFood(String itemName) {
		float[] fried = containsAny(itemName, "apple") ? new float[] { 0.74F, 0.50F, 0.20F }
			: new float[] { 0.82F, 0.58F, 0.20F };
		float[] dark = darken(fried, 0.66F);
		float[] pale = containsAny(itemName, "zeppole") ? new float[] { 0.90F, 0.82F, 0.62F }
			: brighten(fried, 1.10F);

		drawCuboid(-0.170D, 0.064D, -0.084D, -0.080D, 0.120D, 0.004D, dark);
		drawCuboid(-0.154D, 0.122D, -0.066D, -0.062D, 0.146D, 0.020D, fried);
		drawCuboid(-0.032D, 0.066D, -0.102D, 0.066D, 0.126D, -0.012D, fried);
		drawCuboid(-0.012D, 0.128D, -0.084D, 0.086D, 0.152D, 0.006D, pale);
		drawCuboid(0.078D, 0.064D, 0.018D, 0.170D, 0.118D, 0.098D, dark);
		drawCuboid(0.096D, 0.120D, 0.032D, 0.186D, 0.144D, 0.112D, fried);
	}

	private static void renderPuddingPlateFood(String itemName) {
		if (itemName.contains("bananasplit")) {
			float[] banana = new float[] { 0.90F, 0.76F, 0.24F };
			float[] cream = new float[] { 0.92F, 0.86F, 0.72F };
			float[] cherry = new float[] { 0.74F, 0.06F, 0.08F };

			drawCuboid(-0.196D, 0.064D, -0.062D, 0.196D, 0.100D, 0.062D, banana);
			drawCuboid(-0.120D, 0.102D, -0.050D, -0.030D, 0.152D, 0.050D, cream);
			drawCuboid(0.044D, 0.102D, -0.050D, 0.134D, 0.152D, 0.050D, cream);
			drawCuboid(-0.010D, 0.154D, -0.020D, 0.030D, 0.178D, 0.020D, cherry);
			return;
		}

		float[] bowl = new float[] { 0.78F, 0.72F, 0.60F };
		float[] pudding = containsAny(itemName, "trifle", "raspberry") ? new float[] { 0.72F, 0.08F, 0.10F }
			: containsAny(itemName, "yorkshire") ? new float[] { 0.80F, 0.52F, 0.20F }
				: inferFoodColor(itemName);
		float[] cream = new float[] { 0.92F, 0.86F, 0.70F };

		drawCuboid(-0.150D, 0.064D, -0.100D, 0.150D, 0.088D, 0.100D, darken(bowl, 0.62F));
		drawCuboid(-0.175D, 0.088D, -0.120D, 0.175D, 0.122D, 0.120D, bowl);
		drawCuboid(-0.132D, 0.124D, -0.086D, 0.132D, 0.154D, 0.086D, pudding);
		drawCuboid(-0.082D, 0.156D, -0.046D, 0.082D, 0.180D, 0.046D, cream);
		if (itemName.contains("trifle")) {
			drawCuboid(-0.110D, 0.126D, -0.080D, 0.110D, 0.140D, 0.080D, cream);
			drawCuboid(-0.030D, 0.182D, -0.018D, 0.030D, 0.202D, 0.018D, pudding);
		}
	}

	private static void renderSquareSweetPlateFood(String itemName) {
		if (containsAny(itemName, "cobbler", "crumble", "tart")) {
			float[] crust = new float[] { 0.70F, 0.46F, 0.20F };
			float[] filling = getPieFillingColor(itemName);
			float[] crumble = new float[] { 0.84F, 0.64F, 0.30F };

			drawCuboid(-0.170D, 0.064D, -0.105D, 0.170D, 0.106D, 0.105D, crust);
			drawCuboid(-0.130D, 0.108D, -0.076D, 0.130D, 0.140D, 0.076D, filling);
			drawCuboid(-0.118D, 0.142D, -0.068D, -0.070D, 0.162D, -0.026D, crumble);
			drawCuboid(-0.010D, 0.142D, 0.020D, 0.038D, 0.162D, 0.062D, crumble);
			drawCuboid(0.070D, 0.142D, -0.044D, 0.118D, 0.162D, -0.002D, crumble);
			return;
		}

		float[] base = containsAny(itemName, "brownie", "timtam", "jaffa", "chocolate")
			? new float[] { 0.20F, 0.10F, 0.04F } : inferFoodColor(itemName);
		float[] icing = containsAny(itemName, "jaffa") ? new float[] { 0.86F, 0.36F, 0.08F }
			: containsAny(itemName, "baklava") ? new float[] { 0.90F, 0.64F, 0.18F }
				: getFoodAccentColor(itemName, base);

		drawCuboid(-0.178D, 0.064D, -0.094D, 0.178D, 0.106D, 0.094D, darken(base, 0.66F));
		drawCuboid(-0.150D, 0.108D, -0.072D, 0.150D, 0.142D, 0.072D, base);
		drawCuboid(-0.132D, 0.144D, -0.056D, 0.132D, 0.162D, 0.056D, icing);
		drawCuboid(-0.080D, 0.164D, -0.040D, -0.042D, 0.178D, -0.006D, brighten(icing, 1.12F));
		drawCuboid(0.036D, 0.164D, 0.006D, 0.074D, 0.178D, 0.040D, brighten(icing, 1.12F));
	}

	private static void renderRiceCakePlateFood(String itemName) {
		float[] rice = new float[] { 0.88F, 0.84F, 0.68F };
		float[] accent = itemName.contains("mochi") ? new float[] { 0.78F, 0.72F, 0.96F }
			: itemName.contains("manjuu") ? new float[] { 0.92F, 0.76F, 0.92F }
				: new float[] { 0.72F, 0.62F, 0.42F };

		renderSmallCookieDisc(-0.092D, -0.042D, 0.082D, 0.070D, 0.064D, 0.036D,
			darken(rice, 0.78F), rice);
		renderSmallCookieDisc(0.030D, 0.014D, 0.082D, 0.070D, 0.066D, 0.036D,
			darken(accent, 0.78F), accent);
		renderSmallCookieDisc(0.128D, -0.052D, 0.066D, 0.056D, 0.064D, 0.032D,
			darken(rice, 0.78F), brighten(rice, 1.08F));
	}

	private static void renderCandiedFruitPlateFood(String itemName) {
		float[] fruit = inferFoodColor(itemName);
		float[] sugar = new float[] { 0.92F, 0.78F, 0.36F };
		float[] stem = new float[] { 0.22F, 0.42F, 0.10F };

		drawCuboid(-0.160D, 0.064D, -0.080D, -0.075D, 0.122D, -0.006D, fruit);
		drawCuboid(-0.148D, 0.124D, -0.064D, -0.064D, 0.146D, 0.012D, sugar);
		drawCuboid(-0.020D, 0.064D, -0.090D, 0.075D, 0.126D, 0.000D, fruit);
		drawCuboid(-0.002D, 0.128D, -0.070D, 0.088D, 0.150D, 0.020D, sugar);
		drawCuboid(0.094D, 0.064D, 0.016D, 0.170D, 0.116D, 0.090D, fruit);
		drawCuboid(0.118D, 0.118D, 0.044D, 0.154D, 0.144D, 0.080D, stem);
	}

	private static void renderPastryFallbackFood(String itemName) {
		float[] base = inferFoodColor(itemName);
		float[] crust = containsAny(itemName, "chocolate", "brownie") ? new float[] { 0.20F, 0.10F, 0.04F }
			: new float[] { 0.67F, 0.42F, 0.18F };
		float[] accent = getFoodAccentColor(itemName, base);

		drawCuboid(-0.166D, 0.064D, -0.094D, -0.012D, 0.112D, 0.090D, crust);
		drawCuboid(-0.140D, 0.114D, -0.070D, 0.020D, 0.152D, 0.068D, base);
		drawCuboid(0.038D, 0.064D, -0.080D, 0.170D, 0.108D, 0.072D, base);
		drawCuboid(0.058D, 0.110D, -0.058D, 0.188D, 0.142D, 0.054D, brighten(base, 1.08F));
		drawCuboid(-0.070D, 0.154D, -0.028D, -0.026D, 0.174D, 0.014D, accent);
		drawCuboid(0.092D, 0.144D, 0.018D, 0.136D, 0.164D, 0.060D, accent);
	}

	private static void renderPastyPlateFood() {
		float[] pastry = new float[] { 0.74F, 0.50F, 0.22F };
		float[] edge = darken(pastry, 0.68F);

		drawCuboid(-0.178D, 0.064D, -0.105D, 0.120D, 0.102D, 0.105D, edge);
		drawCuboid(-0.122D, 0.104D, -0.082D, 0.164D, 0.150D, 0.082D, pastry);
		drawCuboid(-0.068D, 0.152D, -0.056D, 0.120D, 0.178D, 0.056D, brighten(pastry, 1.10F));
		drawCuboid(0.104D, 0.118D, -0.092D, 0.152D, 0.138D, 0.092D, edge);
	}

	private static void renderDonutRing(double centerX, double centerZ, float[] dough, float[] icing) {
		float[] dark = darken(dough, 0.68F);

		drawCuboid(centerX - 0.150D, 0.064D, centerZ - 0.092D, centerX - 0.052D, 0.118D,
			centerZ + 0.092D, dark);
		drawCuboid(centerX + 0.052D, 0.064D, centerZ - 0.092D, centerX + 0.150D, 0.118D,
			centerZ + 0.092D, dough);
		drawCuboid(centerX - 0.104D, 0.064D, centerZ - 0.138D, centerX + 0.104D, 0.118D,
			centerZ - 0.050D, dough);
		drawCuboid(centerX - 0.104D, 0.064D, centerZ + 0.050D, centerX + 0.104D, 0.118D,
			centerZ + 0.138D, dark);

		if (icing != null) {
			drawCuboid(centerX - 0.125D, 0.120D, centerZ - 0.078D, centerX - 0.045D, 0.138D,
				centerZ + 0.078D, icing);
			drawCuboid(centerX + 0.045D, 0.120D, centerZ - 0.078D, centerX + 0.125D, 0.138D,
				centerZ + 0.078D, icing);
			drawCuboid(centerX - 0.086D, 0.120D, centerZ - 0.112D, centerX + 0.086D, 0.138D,
				centerZ - 0.046D, icing);
			drawCuboid(centerX - 0.086D, 0.120D, centerZ + 0.046D, centerX + 0.086D, 0.138D,
				centerZ + 0.112D, icing);
		}
	}

	private static void renderSmallCookieDisc(double centerX, double centerZ, double radiusX,
			double radiusZ, double y, double height, float[] edge, float[] top) {
		drawCuboid(centerX - radiusX * 0.78D, y, centerZ - radiusZ, centerX + radiusX * 0.78D,
			y + height * 0.58D, centerZ + radiusZ, edge);
		drawCuboid(centerX - radiusX, y, centerZ - radiusZ * 0.68D, centerX + radiusX,
			y + height * 0.58D, centerZ + radiusZ * 0.68D, edge);
		drawCuboid(centerX - radiusX * 0.76D, y + height * 0.58D, centerZ - radiusZ * 0.76D,
			centerX + radiusX * 0.76D, y + height, centerZ + radiusZ * 0.76D, top);
	}

	private static float[] getPieFillingColor(String itemName) {
		if (containsAny(itemName, "keylime", "spinach")) {
			return new float[] { 0.34F, 0.58F, 0.18F };
		}

		if (containsAny(itemName, "pumpkin", "sweetpotato")) {
			return new float[] { 0.86F, 0.42F, 0.10F };
		}

		if (containsAny(itemName, "cherry", "strawberry", "raspberry")) {
			return new float[] { 0.72F, 0.08F, 0.08F };
		}

		if (containsAny(itemName, "blueberry", "blackberry", "gooseberry")) {
			return new float[] { 0.26F, 0.12F, 0.48F };
		}

		if (containsAny(itemName, "pecan", "meat", "mince", "cottage", "shepard")) {
			return new float[] { 0.40F, 0.18F, 0.08F };
		}

		if (itemName.contains("chickenpot")) {
			return new float[] { 0.74F, 0.54F, 0.26F };
		}

		return inferFoodColor(itemName);
	}

	private static float[] getCakeFrostingColor(String itemName) {
		if (containsAny(itemName, "redvelvet", "cheesecake", "holidaycake", "lemondrizzle")) {
			return new float[] { 0.92F, 0.86F, 0.70F };
		}

		if (itemName.contains("chocolate")) {
			return new float[] { 0.18F, 0.08F, 0.03F };
		}

		if (itemName.contains("carrotcake")) {
			return new float[] { 0.90F, 0.82F, 0.62F };
		}

		if (itemName.contains("pineapple")) {
			return new float[] { 0.90F, 0.68F, 0.18F };
		}

		return new float[] { 0.84F, 0.70F, 0.42F };
	}

	private static void renderDecoratedCookieFood(float[] cookie, float[] accent) {
		float[] dark = darken(cookie, 0.62F);
		float[] light = brighten(cookie, 1.16F);

		drawCuboid(-0.140D, 0.064D, -0.108D, 0.140D, 0.092D, 0.108D, dark[0], dark[1],
			dark[2]);
		drawCuboid(-0.162D, 0.092D, -0.130D, 0.162D, 0.126D, 0.130D, cookie[0], cookie[1],
			cookie[2]);
		drawCuboid(-0.106D, 0.128D, -0.080D, -0.060D, 0.142D, -0.030D, accent[0], accent[1],
			accent[2]);
		drawCuboid(0.018D, 0.128D, -0.088D, 0.068D, 0.142D, -0.038D, light[0], light[1],
			light[2]);
		drawCuboid(0.064D, 0.128D, 0.020D, 0.112D, 0.142D, 0.068D, accent[0], accent[1],
			accent[2]);
		drawCuboid(-0.050D, 0.128D, 0.040D, -0.006D, 0.142D, 0.088D, light[0], light[1],
			light[2]);
	}

	private static void renderFairyBreadFood() {
		float[] crust = new float[] { 0.58F, 0.36F, 0.15F };
		float[] bread = new float[] { 0.88F, 0.82F, 0.62F };
		float[] sugar = new float[] { 0.96F, 0.92F, 0.78F };

		drawCuboid(-0.185D, 0.064D, -0.116D, 0.185D, 0.094D, 0.116D, crust[0], crust[1],
			crust[2]);
		drawCuboid(-0.158D, 0.096D, -0.092D, 0.158D, 0.134D, 0.092D, bread[0], bread[1],
			bread[2]);
		drawCuboid(-0.135D, 0.136D, -0.072D, 0.135D, 0.148D, 0.072D, sugar[0], sugar[1],
			sugar[2]);
		drawCuboid(-0.088D, 0.150D, -0.048D, -0.052D, 0.164D, -0.012D, 0.92F, 0.20F, 0.24F);
		drawCuboid(-0.020D, 0.150D, 0.018D, 0.016D, 0.164D, 0.054D, 0.18F, 0.56F, 0.20F);
		drawCuboid(0.052D, 0.150D, -0.060D, 0.088D, 0.164D, -0.024D, 0.24F, 0.36F, 0.90F);
		drawCuboid(0.076D, 0.150D, 0.026D, 0.112D, 0.164D, 0.062D, 0.94F, 0.78F, 0.18F);
	}

	private static void renderFriesPlateFood() {
		float[] fried = new float[] { 0.91F, 0.72F, 0.26F };
		float[] browned = new float[] { 0.72F, 0.44F, 0.12F };

		drawCuboid(-0.150D, 0.064D, -0.100D, -0.106D, 0.160D, 0.096D, fried[0], fried[1],
			fried[2]);
		drawCuboid(-0.075D, 0.064D, -0.120D, -0.032D, 0.178D, 0.076D, browned[0], browned[1],
			browned[2]);
		drawCuboid(0.000D, 0.064D, -0.094D, 0.044D, 0.166D, 0.108D, fried[0], fried[1],
			fried[2]);
		drawCuboid(0.075D, 0.064D, -0.104D, 0.118D, 0.154D, 0.088D, fried[0], fried[1],
			fried[2]);
	}

	private static void renderGenericPlateFood(String itemName) {
		float[] food = inferFoodColor(itemName);
		float[] accent = getFoodAccentColor(itemName, food);

		float[] base = darken(food, 0.78F);

		drawCuboid(-0.150D, 0.064D, -0.096D, 0.150D, 0.092D, 0.096D, base[0], base[1], base[2]);
		drawCuboid(-0.125D, 0.094D, -0.072D, -0.032D, 0.142D, 0.020D, food[0], food[1], food[2]);
		drawCuboid(0.000D, 0.098D, -0.082D, 0.096D, 0.152D, 0.012D, food[0], food[1], food[2]);
		drawCuboid(0.054D, 0.092D, 0.020D, 0.132D, 0.136D, 0.080D, food[0], food[1], food[2]);
		drawCuboid(-0.110D, 0.144D, 0.034D, -0.048D, 0.164D, 0.078D, accent[0], accent[1],
			accent[2]);
		drawCuboid(0.030D, 0.154D, 0.026D, 0.092D, 0.174D, 0.076D, accent[0], accent[1],
			accent[2]);
	}

	private static float[] inferDrinkColor(String itemName) {
		if (containsAny(itemName, "coffee", "hotchocolate", "cocoa")) {
			return new float[] { 0.20F, 0.10F, 0.04F };
		}

		if (equalsAny(itemName, "soysauceitem", "hoisinsauceitem")) {
			return new float[] { 0.18F, 0.08F, 0.03F };
		}

		if (equalsAny(itemName, "hotsauceitem", "sweetandsoursauceitem")) {
			return new float[] { 0.78F, 0.16F, 0.04F };
		}

		if (containsAny(itemName, "syrup", "honey")) {
			return new float[] { 0.74F, 0.38F, 0.08F };
		}

		if (containsAny(itemName, "oil")) {
			return new float[] { 0.82F, 0.68F, 0.20F };
		}

		if (itemName.contains("vinegar")) {
			return new float[] { 0.86F, 0.76F, 0.42F };
		}

		if (containsAny(itemName, "rootbeer", "cola")) {
			return new float[] { 0.22F, 0.10F, 0.04F };
		}

		if (itemName.contains("teaitem")) {
			return new float[] { 0.66F, 0.38F, 0.12F };
		}

		if (containsAny(itemName, "milkshake", "eggnog", "yogurt", "cream", "vanilla", "coconut")) {
			return new float[] { 0.90F, 0.84F, 0.66F };
		}

		return inferFoodColor(itemName);
	}

	private static float[] getRawFishBodyColor(String itemName) {
		if (itemName.contains("rawtofish")) {
			return new float[] { 0.76F, 0.68F, 0.42F };
		}

		if (itemName.contains("trout")) {
			return new float[] { 0.25F, 0.50F, 0.22F };
		}

		if (itemName.contains("charr")) {
			return new float[] { 0.56F, 0.53F, 0.66F };
		}

		if (containsAny(itemName, "snapper", "perch")) {
			return new float[] { 0.75F, 0.00F, 0.00F };
		}

		if (itemName.contains("salmon")) {
			return new float[] { 0.74F, 0.34F, 0.28F };
		}

		if (containsAny(itemName, "anchovy", "herring", "sardine")) {
			return itemName.contains("herring") ? new float[] { 0.10F, 0.25F, 0.53F }
				: new float[] { 0.28F, 0.34F, 0.46F };
		}

		if (containsAny(itemName, "tuna", "walleye")) {
			return itemName.contains("walleye") ? new float[] { 0.78F, 0.45F, 0.15F }
				: new float[] { 0.16F, 0.18F, 0.25F };
		}

		if (itemName.contains("eel")) {
			return new float[] { 0.38F, 0.48F, 0.00F };
		}

		if (itemName.contains("greenheartfish")) {
			return new float[] { 0.11F, 0.27F, 0.06F };
		}

		if (itemName.contains("bass")) {
			return new float[] { 0.13F, 0.53F, 0.35F };
		}

		if (itemName.contains("carp")) {
			return new float[] { 0.52F, 0.35F, 0.36F };
		}

		if (itemName.contains("catfish")) {
			return new float[] { 0.36F, 0.38F, 0.49F };
		}

		if (itemName.contains("grouper")) {
			return new float[] { 0.60F, 0.35F, 0.24F };
		}

		if (itemName.contains("mudfish")) {
			return new float[] { 0.47F, 0.31F, 0.13F };
		}

		if (itemName.contains("tilapia")) {
			return new float[] { 0.25F, 0.50F, 0.22F };
		}

		return new float[] { 0.44F, 0.52F, 0.58F };
	}

	private static float[] getRawFishBellyColor(String itemName) {
		if (itemName.contains("rawtofish")) {
			return new float[] { 0.84F, 0.80F, 0.65F };
		}

		if (containsAny(itemName, "bass", "tilapia", "trout", "walleye")) {
			return new float[] { 0.84F, 0.78F, 0.36F };
		}

		if (containsAny(itemName, "perch", "snapper")) {
			return new float[] { 0.88F, 0.85F, 0.28F };
		}

		if (containsAny(itemName, "salmon", "charr")) {
			return new float[] { 0.88F, 0.62F, 0.52F };
		}

		if (containsAny(itemName, "herring", "sardine", "anchovy", "tuna", "catfish")) {
			return new float[] { 0.80F, 0.82F, 0.80F };
		}

		if (containsAny(itemName, "eel", "mudfish")) {
			return new float[] { 0.52F, 0.48F, 0.34F };
		}

		return new float[] { 0.78F, 0.74F, 0.62F };
	}

	private static float[] getRawFishFinColor(String itemName, float[] body) {
		if (containsAny(itemName, "salmon", "charr", "snapper", "perch")) {
			return new float[] { 0.58F, 0.18F, 0.16F };
		}

		if (itemName.contains("walleye")) {
			return new float[] { 0.33F, 0.21F, 0.10F };
		}

		if (containsAny(itemName, "bass", "carp", "catfish", "grouper", "mudfish", "tilapia",
				"trout")) {
			return new float[] { 0.24F, 0.34F, 0.20F };
		}

		return darken(body, 0.70F);
	}

	private static float[] inferFoodColor(String itemName) {
		if (containsAny(itemName, "cotton", "wovencotton")) {
			return new float[] { 0.82F, 0.80F, 0.70F };
		}

		if (containsAny(itemName, "wax", "honeycomb")) {
			return new float[] { 0.86F, 0.64F, 0.16F };
		}

		if (containsAny(itemName, "theatrebox", "popcorn")) {
			return new float[] { 0.82F, 0.68F, 0.34F };
		}

		if (itemName.contains("vanillabean")) {
			return new float[] { 0.20F, 0.10F, 0.04F };
		}

		if (containsAny(itemName, "barley", "beanitem", "oats", "rice", "rye", "soybean")) {
			return new float[] { 0.72F, 0.56F, 0.30F };
		}

		if (containsAny(itemName, "curryleaf", "spiceleaf", "tealeaf", "seaweed")) {
			return new float[] { 0.20F, 0.48F, 0.14F };
		}

		if (itemName.contains("mayo")) {
			return new float[] { 0.86F, 0.82F, 0.62F };
		}

		if (itemName.contains("mustard")) {
			return new float[] { 0.88F, 0.66F, 0.10F };
		}

		if (itemName.contains("chutney")) {
			return new float[] { 0.78F, 0.34F, 0.10F };
		}

		if (itemName.contains("chaoscookie")) {
			return new float[] { 0.18F, 0.12F, 0.24F };
		}

		if (itemName.contains("lavender")) {
			return new float[] { 0.76F, 0.66F, 0.92F };
		}

		if (itemName.contains("redvelvet")) {
			return new float[] { 0.62F, 0.04F, 0.04F };
		}

		if (itemName.contains("holidaycake")) {
			return new float[] { 0.86F, 0.82F, 0.62F };
		}

		if (itemName.contains("pistachio")) {
			return new float[] { 0.48F, 0.54F, 0.22F };
		}

		if (itemName.contains("cashew")) {
			return new float[] { 0.70F, 0.52F, 0.28F };
		}

		if (containsAny(itemName, "sweetandsour", "orangechicken", "orangec", "marmalade")) {
			return new float[] { 0.86F, 0.30F, 0.08F };
		}

		if (containsAny(itemName, "chocolate", "coffee", "cocoa", "peanut", "pecan", "walnut",
				"almond", "chestnut", "cinnamon", "mushroom", "gravy", "toast", "rye")) {
			return new float[] { 0.34F, 0.18F, 0.08F };
		}

		if (containsAny(itemName, "chicken", "turkey")) {
			return new float[] { 0.72F, 0.48F, 0.24F };
		}

		if (containsAny(itemName, "beef", "steak", "burger", "bacon", "ham", "sausage", "pork",
				"mutton", "turkey", "meat")) {
			return new float[] { 0.48F, 0.20F, 0.12F };
		}

		if (containsAny(itemName, "tomato", "strawberry", "raspberry", "cherry", "apple",
				"cranberry", "pomegranate", "watermelon", "pepper")) {
			return new float[] { 0.70F, 0.10F, 0.08F };
		}

		if (containsAny(itemName, "carrot", "pumpkin", "orange", "apricot", "sweetpotato", "yam",
				"mango", "peach")) {
			return new float[] { 0.86F, 0.42F, 0.10F };
		}

		if (containsAny(itemName, "potato", "turnip", "parsnip")) {
			return new float[] { 0.72F, 0.50F, 0.24F };
		}

		if (containsAny(itemName, "cheese", "corn", "lemon", "banana", "pineapple", "honey",
				"egg", "custard")) {
			return new float[] { 0.90F, 0.70F, 0.18F };
		}

		if (containsAny(itemName, "avocado", "pea", "leek", "cucumber", "spinach", "lettuce",
			"cabbage", "broccoli", "asparagus", "celery", "lime", "pickle", "vegetable",
			"salad", "curryleaf", "spiceleaf")) {
			return new float[] { 0.22F, 0.52F, 0.14F };
		}

		if (containsAny(itemName, "beet", "blueberry", "blackberry", "grape", "plum", "fig")) {
			return new float[] { 0.28F, 0.12F, 0.46F };
		}

		if (containsAny(itemName, "rice", "coconut", "cream", "vanilla", "yogurt", "milk")) {
			return new float[] { 0.84F, 0.78F, 0.58F };
		}

		if (containsAny(itemName, "fish", "calamari", "shrimp", "crab", "lobster")) {
			return new float[] { 0.78F, 0.56F, 0.48F };
		}

		return new float[] { 0.68F, 0.38F, 0.16F };
	}

	private static float[] getFoodAccentColor(String itemName, float[] foodColor) {
		if (itemName.contains("theatrebox")) {
			return new float[] { 0.74F, 0.10F, 0.08F };
		}

		if (containsAny(itemName, "sweetandsour", "orangechicken", "orangec")) {
			return new float[] { 0.18F, 0.46F, 0.13F };
		}

		if (containsAny(itemName, "cheese", "corn", "egg", "lemon")) {
			return new float[] { 0.94F, 0.78F, 0.22F };
		}

		if (containsAny(itemName, "tomato", "pepper", "berry", "cherry", "apple")) {
			return new float[] { 0.75F, 0.10F, 0.08F };
		}

		if (containsAny(itemName, "bacon", "beef", "pork", "ham", "sausage")) {
			return new float[] { 0.38F, 0.14F, 0.08F };
		}

		if (foodColor[1] > foodColor[0]) {
			return new float[] { 0.90F, 0.70F, 0.18F };
		}

		return new float[] { 0.16F, 0.44F, 0.12F };
	}

	private static void renderOrnament(ItemStack itemStack, double x, double y, double z, double itemX,
			double itemZ, double surfaceY, float yaw) {
		float scale = getOrnamentScale(itemStack);

		GlStateManager.pushMatrix();
		GlStateManager.translate(x + itemX, y + surfaceY + 0.5D * scale, z + itemZ);
		GlStateManager.rotate(yaw, 0.0F, 1.0F, 0.0F);
		GlStateManager.scale(scale, scale, scale);
		Minecraft.getMinecraft().getRenderItem().renderItem(itemStack,
			ItemCameraTransforms.TransformType.NONE);
		GlStateManager.popMatrix();
	}

	private static float getOrnamentScale(ItemStack itemStack) {
		if (itemStack.getItem() instanceof ItemBlockGlassVase) {
			return 0.83F;
		}

		if (itemStack.getItem() instanceof ItemBlockOrnament) {
			String modelName = ((ItemBlockOrnament)itemStack.getItem()).getModelName(itemStack.getMetadata());

			if (modelName.contains("_urn")) {
				return 0.78F;
			}
		}

		return 0.62F;
	}

	private static void renderClosedBook(ItemStack itemStack) {
		float[] cover = getBookCoverColor(itemStack);
		float[] shadow = darken(cover, 0.42F);
		float[] pages = new float[] { 0.78F, 0.72F, 0.52F };
		float[] pageLines = new float[] { 0.48F, 0.40F, 0.25F };
		float[] gold = new float[] { 0.86F, 0.58F, 0.12F };

		GlStateManager.disableTexture2D();
		GlStateManager.disableCull();
		drawCuboid(-0.31D, 0.000D, -0.22D, 0.31D, 0.026D, 0.22D, shadow[0], shadow[1], shadow[2]);
		drawCuboid(-0.235D, 0.028D, -0.178D, 0.270D, 0.098D, 0.178D, pages[0], pages[1], pages[2]);
		drawCuboid(-0.31D, 0.100D, -0.22D, 0.31D, 0.136D, 0.22D, cover[0], cover[1], cover[2]);
		drawCuboid(-0.330D, 0.028D, -0.22D, -0.310D, 0.098D, 0.22D, shadow[0], shadow[1], shadow[2]);

		drawCuboid(0.270D, 0.034D, -0.165D, 0.286D, 0.096D, 0.165D, pageLines[0], pageLines[1],
			pageLines[2]);
		drawCuboid(0.232D, 0.034D, -0.165D, 0.248D, 0.096D, 0.165D, pageLines[0], pageLines[1],
			pageLines[2]);

		drawBookTopPattern(gold);
		GlStateManager.enableCull();
		GlStateManager.enableTexture2D();
	}

	private static void renderVaseStem() {
		GlStateManager.disableTexture2D();
		GlStateManager.disableLighting();
		GlStateManager.disableDepth();

		drawCuboid(-0.012D, 0.14D, -0.012D, 0.012D, 0.40D, 0.012D, 0.08F, 0.34F, 0.08F);
		drawCuboid(-0.075D, 0.23D, -0.008D, -0.012D, 0.29D, 0.008D, 0.08F, 0.30F, 0.07F);
		drawCuboid(0.012D, 0.31D, -0.008D, 0.070D, 0.37D, 0.008D, 0.08F, 0.30F, 0.07F);

		GlStateManager.enableDepth();
		GlStateManager.enableLighting();
		GlStateManager.enableTexture2D();
	}

	private static void renderRecord(ItemStack itemStack) {
		int hash = itemStack.getItem().getUnlocalizedName().hashCode();
		float labelR = 0.25F + ((hash >> 16) & 3) * 0.16F;
		float labelG = 0.25F + ((hash >> 8) & 3) * 0.16F;
		float labelB = 0.25F + (hash & 3) * 0.16F;

		drawCuboid(-0.25D, 0.0D, -0.25D, 0.25D, 0.035D, 0.25D, 0.03F, 0.03F, 0.035F);
		drawCuboid(-0.12D, 0.038D, -0.12D, 0.12D, 0.050D, 0.12D, labelR, labelG, labelB);
		drawCuboid(-0.035D, 0.052D, -0.035D, 0.035D, 0.058D, 0.035D, 0.02F, 0.02F, 0.02F);
	}

	private static float[] getBookCoverColor(ItemStack itemStack) {
		if (itemStack.getItem() == Items.ENCHANTED_BOOK) {
			return new float[] { 0.18F, 0.07F, 0.30F };
		}

		if (itemStack.getItem() == Items.WRITABLE_BOOK) {
			return new float[] { 0.04F, 0.22F, 0.10F };
		}

		if (itemStack.getItem() == Items.WRITTEN_BOOK) {
			return new float[] { 0.05F, 0.10F, 0.30F };
		}

		return new float[] { 0.38F, 0.07F, 0.04F };
	}

	private static float[] darken(float[] color, float amount) {
		return new float[] { color[0] * amount, color[1] * amount, color[2] * amount };
	}

	private static float[] brighten(float[] color, float amount) {
		return new float[] { Math.min(1.0F, color[0] * amount), Math.min(1.0F, color[1] * amount),
			Math.min(1.0F, color[2] * amount) };
	}

	private static void drawBookTopPattern(float[] gold) {
		double y1 = 0.137D;
		double y2 = 0.143D;

		drawCuboid(-0.245D, y1, -0.165D, 0.220D, y2, -0.145D, gold[0], gold[1], gold[2]);
		drawCuboid(-0.245D, y1, 0.145D, 0.220D, y2, 0.165D, gold[0], gold[1], gold[2]);
		drawCuboid(-0.245D, y1, -0.165D, -0.225D, y2, 0.165D, gold[0], gold[1], gold[2]);
		drawCuboid(0.200D, y1, -0.165D, 0.220D, y2, 0.165D, gold[0], gold[1], gold[2]);

		drawCuboid(-0.120D, y1, -0.075D, 0.100D, y2, -0.055D, gold[0], gold[1], gold[2]);
		drawCuboid(-0.120D, y1, 0.055D, 0.100D, y2, 0.075D, gold[0], gold[1], gold[2]);
		drawCuboid(-0.120D, y1, -0.075D, -0.100D, y2, 0.075D, gold[0], gold[1], gold[2]);
		drawCuboid(0.080D, y1, -0.075D, 0.100D, y2, 0.075D, gold[0], gold[1], gold[2]);

		drawCuboid(-0.300D, y1, -0.170D, -0.282D, y2, 0.170D, gold[0], gold[1], gold[2]);
		drawCuboid(-0.318D, y1, -0.080D, -0.264D, y2, -0.060D, gold[0], gold[1], gold[2]);
		drawCuboid(-0.318D, y1, 0.060D, -0.264D, y2, 0.080D, gold[0], gold[1], gold[2]);
	}

	private static void drawCuboid(double minX, double minY, double minZ, double maxX, double maxY,
			double maxZ, float[] color) {
		drawCuboid(minX, minY, minZ, maxX, maxY, maxZ, color[0], color[1], color[2]);
	}

	private static void drawCuboid(double minX, double minY, double minZ, double maxX, double maxY,
			double maxZ, float[] color, float alpha) {
		drawCuboid(minX, minY, minZ, maxX, maxY, maxZ, color[0], color[1], color[2], alpha);
	}

	private static void drawCuboid(double minX, double minY, double minZ, double maxX, double maxY,
			double maxZ, float r, float g, float b) {
		drawCuboid(minX, minY, minZ, maxX, maxY, maxZ, r, g, b, 1.0F);
	}

	private static void drawCuboid(double minX, double minY, double minZ, double maxX, double maxY,
			double maxZ, float r, float g, float b, float alpha) {
		Tessellator tessellator = Tessellator.getInstance();
		VertexBuffer renderer = tessellator.getBuffer();
		renderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR_NORMAL);

		addFace(renderer, minX, minY, minZ, maxX, minY, minZ, maxX, maxY, minZ, minX, maxY, minZ,
			r, g, b, alpha, 0.0F, 0.0F, -1.0F);
		addFace(renderer, minX, minY, maxZ, minX, maxY, maxZ, maxX, maxY, maxZ, maxX, minY, maxZ,
			r, g, b, alpha, 0.0F, 0.0F, 1.0F);
		addFace(renderer, minX, minY, minZ, minX, minY, maxZ, maxX, minY, maxZ, maxX, minY, minZ,
			r, g, b, alpha, 0.0F, -1.0F, 0.0F);
		addFace(renderer, minX, maxY, minZ, maxX, maxY, minZ, maxX, maxY, maxZ, minX, maxY, maxZ,
			r, g, b, alpha, 0.0F, 1.0F, 0.0F);
		addFace(renderer, minX, minY, minZ, minX, maxY, minZ, minX, maxY, maxZ, minX, minY, maxZ,
			r, g, b, alpha, -1.0F, 0.0F, 0.0F);
		addFace(renderer, maxX, minY, minZ, maxX, minY, maxZ, maxX, maxY, maxZ, maxX, maxY, minZ,
			r, g, b, alpha, 1.0F, 0.0F, 0.0F);

		tessellator.draw();
	}

	private static void addFace(VertexBuffer renderer, double x1, double y1, double z1, double x2, double y2,
			double z2, double x3, double y3, double z3, double x4, double y4, double z4, float r,
			float g, float b, float alpha, float normalX, float normalY, float normalZ) {
		renderer.pos(x1, y1, z1).tex(0.0D, 0.0D).color(r, g, b, alpha)
			.normal(normalX, normalY, normalZ).endVertex();
		renderer.pos(x2, y2, z2).tex(1.0D, 0.0D).color(r, g, b, alpha)
			.normal(normalX, normalY, normalZ).endVertex();
		renderer.pos(x3, y3, z3).tex(1.0D, 1.0D).color(r, g, b, alpha)
			.normal(normalX, normalY, normalZ).endVertex();
		renderer.pos(x4, y4, z4).tex(0.0D, 1.0D).color(r, g, b, alpha)
			.normal(normalX, normalY, normalZ).endVertex();
	}

	private static void beginGlassLayer() {
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GlStateManager.depthMask(false);
	}

	private static void endGlassLayer() {
		GlStateManager.depthMask(true);
		GL11.glDisable(GL11.GL_BLEND);
	}
}
