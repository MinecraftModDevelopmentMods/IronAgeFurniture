package zone.moddev.mc.ironagefurniture.fixture;

import java.util.List;
import java.util.Map;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameType;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.registries.ForgeRegistries;
import zone.moddev.mc.ironagefurniture.Ironagefurniture;
import zone.moddev.mc.ironagefurniture.api.UpholsteryItemData;
import zone.moddev.mc.ironagefurniture.api.blocks.furniture.FurnitureBed;
import zone.moddev.mc.ironagefurniture.api.blocks.furniture.MultiBlockChair;
import zone.moddev.mc.ironagefurniture.api.enumerations.UpholsteryColour;
import zone.moddev.mc.ironagefurniture.api.items.UpholsteredBlockItem;

/** Uses the installed registries and real Forge harvesting, not mocked drops. */
final class UpholsteryRuntimeProbe {
    private UpholsteryRuntimeProbe() { }
    static void verifyLegacyBeds(ServerWorld world) {
        String[] forms = { "bed_wood_foot_oak", "bed_wood_foot_left_oak",
                "bed_canopy_foot_lower_oak", "bed_canopy_foot_left_lower_oak" };
        int parts = 0;
        for (int form = 0; form < forms.length; form++) for (UpholsteryColour colour : UpholsteryColour.values()) {
            FurnitureBed bed = (FurnitureBed) ForgeRegistries.BLOCKS.getValue(new ResourceLocation("ironagefurniture", forms[form]));
            BlockPos base = new BlockPos(96 + form * 8, 110, 320 + colour.getItemMetadata() * 5);
            BlockState state = world.getBlockState(base);
            require(state.getBlock() == bed, "Legacy bed lost its identity: " + base + ": " + state);
            require(state.get(FurnitureBed.DIRECTION) == Direction.NORTH, "Legacy bed lost facing");
            require(state.get(UpholsteryItemData.COLOUR) == colour, "Legacy bed lost colour: " + base + ": " + state);
            for (Map.Entry<BlockPos, BlockState> part : bed.structure(base, state).entrySet()) {
                require(world.getBlockState(part.getKey()) == part.getValue(), "Legacy bed part changed: " + part.getKey());
                require(world.getTileEntity(part.getKey()) == null, "Obsolete upholstery tile survived flattening");
                parts++;
            }
        }
        org.apache.logging.log4j.LogManager.getLogger().info("IAF LEGACY PHASE FOUR BED UPGRADE PASSED: 64 beds, {} parts", parts);
    }
    static int run(MinecraftServer server, ServerWorld world, FakePlayer player) {
        int forms = 0;
        for (Block block : ForgeRegistries.BLOCKS.getValues()) {
            if (!(block.asItem() instanceof UpholsteredBlockItem)) continue;
            NonNullList<ItemStack> creative = NonNullList.create();
            for (UpholsteryColour colour : UpholsteryColour.values()) {
                Item item = UpholsteryItemData.create(block, colour).getItem();
                item.fillItemGroup(Ironagefurniture.IAF_GROUP, creative);
            }
            require(creative.size() == 16, "Expected sixteen creative colours: " + block.getRegistryName());
            for (UpholsteryColour colour : UpholsteryColour.values()) {
                require(UpholsteryItemData.getColour(creative.get(colour.getItemMetadata())) == colour,
                        "Wrong creative colour");
                verifyRecipes(server, world, player, block, colour);
                for (Direction facing : Direction.Plane.HORIZONTAL) {
                    verifyStructure(world, player, block, colour, facing, false);
                    verifyStructure(world, player, block, colour, facing, true);
                }
            }
            forms++;
        }
        require(forms >= 36, "Vanilla upholstered furniture did not register");
        return forms;
    }
    private static CraftingInventory grid() {
        return new CraftingInventory(new Container(null, 0) {
            @Override public boolean canInteractWith(PlayerEntity player) { return false; }
        }, 3, 3);
    }
    @SuppressWarnings("unchecked") private static IRecipe<CraftingInventory> recipe(MinecraftServer server, String path) {
        return (IRecipe<CraftingInventory>) server.getRecipeManager().getRecipe(new ResourceLocation("ironagefurniture", path))
                .orElseThrow(() -> new IllegalStateException("Missing recipe " + path));
    }
    private static void verifyRecipes(MinecraftServer server, ServerWorld world, FakePlayer player, Block block, UpholsteryColour colour) {
        String id = block.getRegistryName().getPath();
        boolean namedRed = id.startsWith("chair_") || id.startsWith("bed_canopy_foot_lower_");
        String recipeId = id + (colour == UpholsteryColour.RED && !namedRed ? "" : "_" + colour.getName());
        IRecipe<CraftingInventory> recipe = recipe(server, recipeId);
        verifyRecipeBook(player, recipe);
        CraftingInventory grid = grid();
        if (id.startsWith("chair_")) {
            for (int index = 0; index < recipe.getIngredients().size(); index++)
                grid.setInventorySlotContents(index * 3, recipe.getIngredients().get(index).getMatchingStacks()[0].copy());
        } else {
            for (int index = 0; index < recipe.getIngredients().size(); index++) {
                ItemStack input = recipe.getIngredients().get(index).getMatchingStacks()[0].copy();
                if (input.getItem() instanceof UpholsteredBlockItem) input = UpholsteryItemData.recolour(input, colour);
                grid.setInventorySlotContents(index, input);
            }
        }
        require(recipe.matches(grid, world), "Recipe rejects its own colour: " + recipeId);
        ItemStack result = recipe.getCraftingResult(grid);
        require(result.getItem() == UpholsteryItemData.create(block, colour).getItem() && UpholsteryItemData.getColour(result) == colour
                && !result.hasTag(),
                "Crafting changed the output colour: " + recipeId);
        if (recipe instanceof zone.moddev.mc.ironagefurniture.api.recipes.UpholsteryUpgradeRecipe) {
            ItemStack input = grid.getStackInSlot(0);
            input.getOrCreateTag().putString("AnotherModsData", "keep");
            require("keep".equals(recipe.getCraftingResult(grid).getTag().getString("AnotherModsData")), "Upgrade lost item NBT");
            if (grid.getStackInSlot(1).getItem() instanceof UpholsteredBlockItem) {
                grid.setInventorySlotContents(1, UpholsteryItemData.recolour(grid.getStackInSlot(1),
                        colour == UpholsteryColour.RED ? UpholsteryColour.BLACK : UpholsteryColour.RED));
                require(!recipe.matches(grid, world) && recipe.getCraftingResult(grid).isEmpty(), "Mixed-colour double bed matched");
                grid.setInventorySlotContents(1, UpholsteryItemData.recolour(grid.getStackInSlot(1), colour));
            }
        }
        grid.setInventorySlotContents(8, new ItemStack(Items.STICK));
        require(!recipe.matches(grid, world), "Recipe accepts extra ingredients");
        if (block instanceof FurnitureBed) {
            IRecipe<CraftingInventory> recolour = recipe(server, id + "_recolour_" + colour.getName());
            grid = grid();
            grid.setInventorySlotContents(0, UpholsteryItemData.create(block, UpholsteryColour.BLACK));
            grid.setInventorySlotContents(1, new ItemStack(ForgeRegistries.ITEMS.getValue(
                    new ResourceLocation("minecraft", colour.getName() + "_carpet"))));
            require(recolour.matches(grid, world) && UpholsteryItemData.getColour(recolour.getCraftingResult(grid)) == colour,
                    "Carpet did not deliberately recolour bed");
        }
    }
    private static void verifyRecipeBook(FakePlayer player, IRecipe<CraftingInventory> recipe) {
        java.util.List<ItemStack> before = new java.util.ArrayList<>();
        for (int slot = 0; slot < player.inventory.getSizeInventory(); slot++)
            before.add(player.inventory.getStackInSlot(slot).copy());
        net.minecraft.inventory.container.WorkbenchContainer table =
                new net.minecraft.inventory.container.WorkbenchContainer(1, player.inventory);
        try {
            player.inventory.clear();
            int slot = 0;
            for (net.minecraft.item.crafting.Ingredient ingredient : recipe.getIngredients()) {
                ItemStack[] choices = ingredient.getMatchingStacks();
                if (choices.length == 0) continue;
                ItemStack input = choices[0].copy();
                require(!input.hasTag(), "Recipe-book ingredient still depends on NBT: " + recipe.getId());
                player.inventory.setInventorySlotContents(slot++, input);
            }
            player.getRecipeBook().unlock(recipe);
            new net.minecraft.item.crafting.ServerRecipePlacer<>(table).place(player, recipe, false);
            require(table.matches(recipe), "Recipe book did not move every ingredient: " + recipe.getId());
            CraftingInventory grid = grid();
            for (int index = 0; index < 9; index++) grid.setInventorySlotContents(index, table.getSlot(index + 1).getStack().copy());
            ItemStack actual = recipe.getCraftingResult(grid);
            require(ItemStack.areItemStacksEqual(actual, recipe.getRecipeOutput()), "Recipe-book output changed: " + recipe.getId());
        } finally {
            player.inventory.clear();
            for (int slot = 0; slot < before.size(); slot++) player.inventory.setInventorySlotContents(slot, before.get(slot));
        }
    }
    private static void verifyStructure(ServerWorld world, FakePlayer player, Block block, UpholsteryColour colour,
            Direction facing, boolean wet) {
        BlockPos base = new BlockPos(128, 80, 128);
        BlockState state = block.getDefaultState().with(UpholsteryItemData.COLOUR, colour)
                .with(FurnitureBed.DIRECTION, facing).with(FurnitureBed.WATERLOGGED, wet);
        Map<BlockPos, BlockState> parts = new java.util.LinkedHashMap<>();
        if (block instanceof FurnitureBed) parts.putAll(((FurnitureBed) block).structure(base, state));
        else {
            MultiBlockChair chair = (MultiBlockChair) block;
            for (int height = 0; height < chair.getChairHeight(); height++)
                parts.put(base.up(height), state.with(MultiBlockChair.PART, chair.partForOffset(height)));
        }
        // A fresh, isolated footprint for each case keeps delayed fluid ticks
        // and drops from an earlier case out of the next one's assertions.
        world.getEntitiesWithinAABB(ItemEntity.class, new AxisAlignedBB(base).grow(5)).forEach(Entity::remove);
        parts.forEach((pos, expected) -> {
            if (!parts.containsKey(pos.down())) world.setBlockState(pos.down(), Blocks.STONE.getDefaultState(), 2);
            world.setBlockState(pos, wet ? Blocks.WATER.getDefaultState() : Blocks.AIR.getDefaultState(), 2);
        });
        world.setBlockState(base, state, 2);
        block.onBlockPlacedBy(world, base, state, player, UpholsteryItemData.create(block, colour));
        for (Map.Entry<BlockPos, BlockState> part : parts.entrySet()) {
            BlockPos pos = part.getKey();
            BlockState actual = world.getBlockState(pos);
            require(actual == part.getValue().with(FurnitureBed.WATERLOGGED, wet), "Wrong structural part " + pos + ": " + actual);
            require(NBTUtil.readBlockState(NBTUtil.writeBlockState(actual)) == actual, "State palette round trip changed furniture");
            ItemStack pick = actual.getBlock().getPickBlock(actual, null, world, pos, player);
            require(pick.getItem() == UpholsteryItemData.create(block, colour).getItem()
                    && UpholsteryItemData.getColour(pick) == colour && !pick.hasTag(), "Pick block lost colour");
            List<ItemStack> drops = actual.getDrops(new LootContext.Builder(world));
            require(drops.size() == 1 && drops.get(0).getItem() == UpholsteryItemData.create(block, colour).getItem()
                    && UpholsteryItemData.getColour(drops.get(0)) == colour && !drops.get(0).hasTag(), "Drops lost colour or form");
            if (block instanceof FurnitureBed) {
                FurnitureBed bed = (FurnitureBed) actual.getBlock();
                require(bed.basePos(pos, actual).equals(base), "A bed part points at the wrong base");
                require(bed.isFlammable(actual, world, pos, Direction.UP), "Wooden bed cannot burn");
                require(!bed.sleepPos(pos, actual).equals(base), "Sleeping uses the foot rather than the head");
            }
        }
        // Break a different part for each colour/facing so lower, head, upper
        // and both halves of doubles all use real survival harvesting.
        BlockPos mine = new java.util.ArrayList<>(parts.keySet()).get(
                (colour.ordinal() + facing.getHorizontalIndex()) % parts.size());
        player.interactionManager.setGameType(GameType.SURVIVAL);
        player.setHeldItem(Hand.MAIN_HAND, new ItemStack(Items.DIAMOND_AXE));
        player.setPosition(mine.getX(), mine.getY(), mine.getZ());
        require(player.interactionManager.tryHarvestBlock(mine), "Cannot harvest furniture");
        List<ItemEntity> drops = world.getEntitiesWithinAABB(ItemEntity.class, new AxisAlignedBB(base).grow(5), item -> !item.removed);
        require(drops.stream().mapToInt(item -> item.getItem().getCount()).sum() == 1, "Multiblock dropped multiple items");
        require(UpholsteryItemData.getColour(drops.get(0).getItem()) == colour, "Forge harvesting lost colour");
        drops.forEach(Entity::remove);
        parts.forEach((pos, expected) -> {
            require(!(world.getBlockState(pos).getBlock() instanceof FurnitureBed)
                    && !(world.getBlockState(pos).getBlock() instanceof MultiBlockChair), "An orphaned part survived breaking");
            if (wet && !pos.equals(mine)) require(world.getBlockState(pos).getBlock() == Blocks.WATER, "Cleanup lost water");
            world.removeBlock(pos, false);
        });
    }
    private static void require(boolean condition, String message) { if (!condition) throw new IllegalStateException(message); }
}
