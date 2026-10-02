package zone.moddev.mc.ironagefurniture.fixture;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.FakePlayerFactory;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import org.apache.logging.log4j.LogManager;
import zone.moddev.mc.ironagefurniture.api.Enumerations.UpholsteryColour;
import zone.moddev.mc.ironagefurniture.api.UpholsteryColourHelper;

/** Exercises the packaged recipes and keeps a disposable bed save/reload fixture. */
public final class BedCraftingProbe {
    private BedCraftingProbe() { }

    public static void verify(MinecraftServer server) {
        if (Boolean.getBoolean("iaf.probe.bedSaveOnly")) {
            verifySavedBeds(server);
            return;
        }
        verifyRecipes(server.getWorld(0));
        verifySavedBeds(server);
    }

    public static void verifyRecipes(WorldServer world) {
        int woods = 0;
        for (Block bed : ForgeRegistries.BLOCKS) {
            String path = bed.getRegistryName().getPath();
            if (!bed.getRegistryName().getNamespace().equals("ironagefurniture")
                    || !path.startsWith("bed_wood_foot_") || path.startsWith("bed_wood_foot_left_")) continue;
            String wood = path.substring("bed_wood_foot_".length());
            for (UpholsteryColour colour : UpholsteryColour.values()) {
                String woodenId = colour == UpholsteryColour.RED ? path : path + "_" + colour.getSerializedName();
                IRecipe wooden = recipe(woodenId);
                ItemStack planks = wooden.getIngredients().get(1).getMatchingStacks()[0].copy();
                InventoryCrafting grid = grid(new ItemStack(Items.BED, 4, colour.getCarpetMetadata()), planks);
                checkCraft(world, wooden, grid, bed, colour);
                checkAdvancement(woodenId, grid.getStackInSlot(0));
                String canopyId = "bed_canopy_foot_lower_" + wood + "_" + colour.getSerializedName();
                Block canopy = ForgeRegistries.BLOCKS.getValue(new ResourceLocation("ironagefurniture", "bed_canopy_foot_lower_" + wood));
                IRecipe upgrade = recipe(canopyId);
                ItemStack source = wooden.getCraftingResult(grid);
                checkAdvancement(canopyId, source);
                source.setItemDamage((colour.getItemMetadata() + 1) % 16);
                checkAdvancement(canopyId, source);
                checkCraft(world, upgrade, grid(source, planks), canopy, colour);
                checkCraft(world, upgrade, grid(new ItemStack(bed, 1, colour.getItemMetadata()), planks), canopy, colour);
                check(upgrade.getIngredients().get(0).apply(source), "Canopy ingredient ignores stable Color");
                check(!upgrade.matches(grid(source, new ItemStack(Blocks.DIRT)), world), "Wrong wood accepted");
                InventoryCrafting extra = grid(source, planks);
                extra.setInventorySlotContents(1, new ItemStack(Blocks.CARPET));
                check(!upgrade.matches(extra, world), "Obsolete carpet-selected canopy recipe still matches");
                ItemStack other = UpholsteryColourHelper.createStack(bed, 1,
                        UpholsteryColour.values()[(colour.ordinal() + 1) % 16]);
                check(!recipe("bed_wood_foot_left_" + wood).matches(grid(source, other), world), "Mixed-colour double bed accepted");
            }
            ++woods;
        }
        check(woods >= 6, "No vanilla beds loaded");
        LogManager.getLogger().info("IRON AGE FURNITURE BED CRAFTING PROBE PASSED: {} woods, 16 colours, exact unlock predicates", woods);
    }

    private static void checkCraft(WorldServer world, IRecipe recipe, InventoryCrafting grid, Block expected, UpholsteryColour colour) {
        check(recipe.matches(grid, world), "Bed recipe did not match " + recipe.getRegistryName() + " " + colour);
        ItemStack output = CraftingManager.findMatchingResult(grid, world);
        check(output.getItem() == Item.getItemFromBlock(expected) && output.getCount() == 1
                && output.getMetadata() == colour.getItemMetadata() && UpholsteryColourHelper.getColour(output) == colour
                && output.getTagCompound().getString("Color").equals(colour.getSerializedName()),
                "Wrong packaged crafting result: " + recipe.getRegistryName() + " " + output);
        for (ItemStack remainder : recipe.getRemainingItems(grid)) check(remainder.isEmpty(), "Bed upgrade duplicates an ingredient");
    }

    private static void checkAdvancement(String id, ItemStack ingredient) {
        String resource = "assets/ironagefurniture/advancements/recipes/" + id + ".json";
        try (InputStreamReader reader = new InputStreamReader(BedCraftingProbe.class.getClassLoader()
                .getResourceAsStream(resource), StandardCharsets.UTF_8)) {
            JsonObject json = new JsonParser().parse(reader).getAsJsonObject();
            ItemPredicate predicate = ItemPredicate.deserialize(json.getAsJsonObject("criteria")
                    .getAsJsonObject("has_ingredient").getAsJsonObject("conditions").getAsJsonArray("items").get(0));
            check(predicate.test(ingredient), "Exact bed advancement does not recognise its ingredient: " + id);
            check(!predicate.test(new ItemStack(Blocks.CRAFTING_TABLE)), "Crafting table unlocks bed " + id);
        } catch (java.io.IOException exception) { throw new IllegalStateException(resource, exception); }
    }

    private static IRecipe recipe(String path) {
        IRecipe recipe = CraftingManager.REGISTRY.getObject(new ResourceLocation("ironagefurniture", path));
        check(recipe != null, "Missing bed recipe " + path);
        return recipe;
    }

    private static InventoryCrafting grid(ItemStack bed, ItemStack plank) {
        InventoryCrafting grid = new InventoryCrafting(new Container() {
            @Override public boolean canInteractWith(EntityPlayer player) { return false; }
        }, 2, 2);
        grid.setInventorySlotContents(0, bed.copy());
        grid.setInventorySlotContents(3, plank.copy());
        return grid;
    }

    private static void verifySavedBeds(MinecraftServer server) {
        WorldServer world = server.getWorld(0);
        EntityPlayer player = FakePlayerFactory.getMinecraft(world);
        String[] forms = { "bed_wood_foot_oak", "bed_wood_foot_left_oak",
                "bed_canopy_foot_lower_oak", "bed_canopy_foot_left_lower_oak" };
        boolean existing = world.getBlockState(new BlockPos(96, 110, 320)).getBlock().getRegistryName()
                .equals(new ResourceLocation("ironagefurniture", forms[0]));
        for (int form = 0; form < forms.length; ++form) {
            Block bed = ForgeRegistries.BLOCKS.getValue(new ResourceLocation("ironagefurniture", forms[form]));
            for (UpholsteryColour colour : UpholsteryColour.values()) {
                BlockPos pos = new BlockPos(96 + form * 8, 110, 320 + colour.getItemMetadata() * 5);
                if (!existing) {
                    for (int x = -2; x <= 2; ++x) for (int z = -2; z <= 2; ++z)
                        world.setBlockState(pos.add(x, -1, z), Blocks.STONE.getDefaultState(), 2);
                    world.setBlockState(pos, bed.getDefaultState().withProperty(net.minecraft.block.BlockHorizontal.FACING, EnumFacing.NORTH), 2);
                    bed.onBlockPlacedBy(world, pos, world.getBlockState(pos), player,
                            UpholsteryColourHelper.createStack(bed, 1, colour));
                }
                check(world.getBlockState(pos).getBlock() == bed && UpholsteryColourHelper.getColour(world, pos) == colour,
                        "Saved bed changed identity or colour: " + forms[form] + " " + colour);
                check(world.getBlockState(pos).getValue(net.minecraft.block.BlockHorizontal.FACING) == EnumFacing.NORTH,
                        "Saved bed changed facing");
                NBTTagCompound nbt = world.getTileEntity(pos).writeToNBT(new NBTTagCompound());
                check(colour.getSerializedName().equals(nbt.getString("Color")), "Saved bed colour missing from NBT");
            }
        }
        LogManager.getLogger().info("IRON AGE FURNITURE BED SAVE PROBE PASSED: 4 forms, 16 colours, {}", existing ? "reloaded" : "created");
    }

    private static void check(boolean condition, String message) {
        if (!condition) throw new IllegalStateException(message);
    }
}
