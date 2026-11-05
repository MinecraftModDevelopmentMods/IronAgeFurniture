package zone.moddev.mc.ironagefurniture.api;

import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.IProperty;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.ModList;
import zone.moddev.mc.ironagefurniture.IronAgeFurnitureConfiguration;
import zone.moddev.mc.ironagefurniture.api.blocks.base.LightHolderSconce;
import zone.moddev.mc.ironagefurniture.api.enumerations.SconceMetal;
import zone.moddev.mc.ironagefurniture.api.items.MetalSconceBlockItem;
import net.minecraftforge.registries.ForgeRegistries;
import zone.moddev.mc.ironagefurniture.init.SconceVariants;

/** Storage keeps an absent metal intact; only its recipe, tab entry and appearance depend on availability. */
public final class SconceMetalData {
    public static final String TAG = "Metal";
    private SconceMetalData() { }
    public static ResourceLocation nuggets(SconceMetal metal) { return new ResourceLocation("forge", "nuggets/" + metal.getSerializedName()); }
    public static ResourceLocation storage(SconceMetal metal) { return new ResourceLocation("forge", "storage_blocks/" + metal.getSerializedName()); }
    public static boolean available(SconceMetal metal) {
        if (metal == SconceMetal.IRON || metal == SconceMetal.GOLD) return true;
        return ModList.get() != null && ModList.get().isLoaded("basemetals")
                && IronAgeFurnitureConfiguration.CLIENT.INTEGRATION_BASEMETALS.get()
                && !ItemTags.getAllTags().getTagOrEmpty(nuggets(metal)).getValues().isEmpty()
                && !BlockTags.getAllTags().getTagOrEmpty(storage(metal)).getValues().isEmpty();
    }
    public static Block textureBlock(SconceMetal metal) {
        if (metal == SconceMetal.GOLD) return Blocks.GOLD_BLOCK;
        if (metal == SconceMetal.IRON || !available(metal)) return Blocks.IRON_BLOCK;
        return BlockTags.getAllTags().getTagOrEmpty(storage(metal)).getValues().iterator().next();
    }
    public static SconceMetal get(ItemStack stack) {
        if (stack.getItem() instanceof MetalSconceBlockItem) {
            SconceMetal fixed = ((MetalSconceBlockItem)stack.getItem()).getMetal();
            if (fixed != SconceMetal.IRON) return fixed;
        }
        return stack.hasTag() ? SconceMetal.byName(stack.getTag().getString(TAG)) : SconceMetal.IRON;
    }
    public static SconceMetal get(BlockState state) {
        ResourceLocation id = state.getBlock().getRegistryName();
        return id == null ? SconceMetal.IRON : material(id.getPath());
    }
    public static SconceMetal material(String path) {
        if (!path.startsWith("light_metal_ironage_sconce_")) return SconceMetal.IRON;
        for (SconceMetal metal : SconceMetal.values()) {
            String token = blockMetalToken(metal);
            int start = path.indexOf(token, "light_metal_ironage_sconce_".length());
            if (start >= 0) {
                int end = start + token.length();
                if (end == path.length() || path.charAt(end) == '_') return metal;
            }
        }
        return SconceMetal.IRON;
    }
    public static String ironPath(String path) {
        SconceMetal metal = material(path);
        if (metal == SconceMetal.IRON) return path;
        return path.replaceFirst(blockMetalToken(metal) + "(?=_|$)", "_iron");
    }
    public static String blockPath(String ironPath, SconceMetal metal) {
        return ironPath.replaceFirst("_iron(?=_|$)", blockMetalToken(metal));
    }
    private static String blockMetalToken(SconceMetal metal) {
        // Copper uses the same saved block identity as 1.17.1. Keep the historical Base Metals item ID below.
        return "_" + (metal.isBaseMetal() && metal != SconceMetal.COPPER ? "basemetals_" : "") + metal.getName();
    }
    public static BlockState withMetal(BlockState state, SconceMetal metal) {
        Block variant = SconceVariants.material(state.getBlock(), metal);
        if (variant == state.getBlock()) return state;
        BlockState result = variant.defaultBlockState();
        for (IProperty<?> property : state.getProperties()) result = copyProperty(state, result, property);
        return result;
    }
    private static <T extends Comparable<T>> BlockState copyProperty(BlockState from, BlockState to, IProperty<T> property) {
        return to.hasProperty(property) ? to.setValue(property, from.getValue(property)) : to;
    }
    public static ItemStack set(ItemStack stack, SconceMetal metal) {
        if (stack.getItem() instanceof MetalSconceBlockItem) {
            ItemStack result = create(((MetalSconceBlockItem)stack.getItem()).getBlock(), metal);
            result.setCount(stack.getCount());
            if (stack.hasTag()) result.setTag(stack.getTag().copy());
            if (result.hasTag()) {
                result.getTag().remove(TAG);
                if (result.getTag().isEmpty()) result.setTag(null);
            }
            return result;
        }
        stack.getOrCreateTag().putString(TAG, metal.getSerializedName());
        return stack;
    }
    public static ItemStack create(Block block, SconceMetal metal) {
        net.minecraft.item.Item item = ForgeRegistries.ITEMS.getValue(itemId(block.getRegistryName(), metal));
        if (item instanceof MetalSconceBlockItem) return new ItemStack(item);
        ItemStack stack = new ItemStack(block);
        stack.getOrCreateTag().putString(TAG, metal.getSerializedName());
        return stack;
    }
    public static ResourceLocation itemId(ResourceLocation iron, SconceMetal metal) {
        String path = ironPath(iron.getPath()).replaceFirst("_iron(?=_|$)",
                "_" + (metal.isBaseMetal() ? "basemetals_" : "") + metal.getName());
        return new ResourceLocation(iron.getNamespace(), path);
    }
    public static BlockState preserve(BlockState old, BlockState replacement) {
        return replacement.getBlock() instanceof LightHolderSconce ? withMetal(replacement, get(old)) : replacement;
    }
    public static List<ItemStack> preserveDrops(BlockState state, List<ItemStack> drops) {
        for (int index = 0; index < drops.size(); index++)
            if (Block.byItem(drops.get(index).getItem()) instanceof LightHolderSconce)
                drops.set(index, set(drops.get(index), get(state)));
        return drops;
    }
    /** Convert old item damage before vanilla starts treating it as durability. */
    public static boolean migrateItem(CompoundNBT stack) {
        String id = stack.getString("id");
        if (!id.startsWith("ironagefurniture:light_metal_ironage_sconce_")) return false;
        CompoundNBT tag = stack.getCompound("tag");
        SconceMetal fixed = SconceMetal.IRON;
        String ironId = id;
        for (SconceMetal candidate : SconceMetal.values()) if (candidate != SconceMetal.IRON) {
            String token = "_" + (candidate.isBaseMetal() ? "basemetals_" : "") + candidate.getName();
            String base = id.replaceFirst(token + "(?=_|$)", "_iron");
            if (!base.equals(id)) { fixed = candidate; ironId = base; break; }
        }
        SconceMetal metal = fixed != SconceMetal.IRON ? fixed : tag.contains(TAG, 8) ? SconceMetal.byName(tag.getString(TAG))
                : SconceMetal.byLegacyMetadata(stack.contains("Damage", 99) ? stack.getInt("Damage") : tag.getInt("Damage"));
        String target = itemId(new ResourceLocation(ironId), metal).toString();
        boolean changed = !id.equals(target) || tag.contains(TAG) || stack.contains("Damage") || tag.contains("Damage");
        if (!changed) return false;
        stack.putString("id", target);
        tag.remove(TAG);
        tag.remove("Damage");
        if (tag.isEmpty()) stack.remove("tag"); else stack.put("tag", tag);
        stack.remove("Damage");
        return true;
    }
}
