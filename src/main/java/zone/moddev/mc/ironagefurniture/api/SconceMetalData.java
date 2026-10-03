package zone.moddev.mc.ironagefurniture.api;

import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.EnumProperty;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.ModList;
import zone.moddev.mc.ironagefurniture.IronAgeFurnitureConfiguration;
import zone.moddev.mc.ironagefurniture.api.blocks.base.LightHolderSconce;
import zone.moddev.mc.ironagefurniture.api.enumerations.SconceMetal;

/** Storage keeps an absent metal intact; only its recipe, tab entry and appearance depend on availability. */
public final class SconceMetalData {
    public static final EnumProperty<SconceMetal> METAL = EnumProperty.create("metal", SconceMetal.class);
    public static final String TAG = "Metal";
    private SconceMetalData() { }
    public static ResourceLocation nuggets(SconceMetal metal) { return new ResourceLocation("forge", "nuggets/" + metal.getName()); }
    public static ResourceLocation storage(SconceMetal metal) { return new ResourceLocation("forge", "storage_blocks/" + metal.getName()); }
    public static boolean available(SconceMetal metal) {
        if (metal == SconceMetal.IRON || metal == SconceMetal.GOLD) return true;
        return ModList.get() != null && ModList.get().isLoaded("basemetals")
                && IronAgeFurnitureConfiguration.CLIENT.INTEGRATION_BASEMETALS.get()
                && !ItemTags.getCollection().getOrCreate(nuggets(metal)).getAllElements().isEmpty()
                && !BlockTags.getCollection().getOrCreate(storage(metal)).getAllElements().isEmpty();
    }
    public static Block textureBlock(SconceMetal metal) {
        if (metal == SconceMetal.GOLD) return Blocks.GOLD_BLOCK;
        if (metal == SconceMetal.IRON || !available(metal)) return Blocks.IRON_BLOCK;
        return BlockTags.getCollection().getOrCreate(storage(metal)).getAllElements().iterator().next();
    }
    public static SconceMetal get(ItemStack stack) {
        return stack.hasTag() ? SconceMetal.byName(stack.getTag().getString(TAG)) : SconceMetal.IRON;
    }
    public static SconceMetal get(BlockState state) { return state.has(METAL) ? state.get(METAL) : SconceMetal.IRON; }
    public static ItemStack set(ItemStack stack, SconceMetal metal) {
        stack.getOrCreateTag().putString(TAG, metal.getName());
        return stack;
    }
    public static ItemStack create(Block block, SconceMetal metal) { return set(new ItemStack(block), metal); }
    public static BlockState preserve(BlockState old, BlockState replacement) {
        return replacement.has(METAL) ? replacement.with(METAL, get(old)) : replacement;
    }
    public static List<ItemStack> preserveDrops(BlockState state, List<ItemStack> drops) {
        for (ItemStack stack : drops) if (Block.getBlockFromItem(stack.getItem()) instanceof LightHolderSconce) set(stack, get(state));
        return drops;
    }
    /** Convert old item damage before vanilla starts treating it as durability. */
    public static boolean migrateItem(CompoundNBT stack) {
        if (!stack.getString("id").startsWith("ironagefurniture:light_metal_ironage_sconce_")) return false;
        CompoundNBT tag = stack.getCompound("tag");
        SconceMetal metal = tag.contains(TAG, 8) ? SconceMetal.byName(tag.getString(TAG))
                : SconceMetal.byLegacyMetadata(stack.contains("Damage", 99) ? stack.getInt("Damage") : tag.getInt("Damage"));
        boolean changed = !metal.getName().equals(tag.getString(TAG)) || stack.contains("Damage") || tag.contains("Damage");
        if (!changed) return false;
        tag.putString(TAG, metal.getName());
        tag.remove("Damage");
        stack.put("tag", tag);
        stack.remove("Damage");
        return true;
    }
}
