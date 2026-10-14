package zone.moddev.mc.ironagefurniture.api.blocks.furniture;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameters;
import zone.moddev.mc.ironagefurniture.api.ShieldChairItemData;
import zone.moddev.mc.ironagefurniture.api.entity.FallingShieldChair;
import zone.moddev.mc.ironagefurniture.api.tile.ShieldChairTileEntity;

/** The removable back is a real shield, including its damage and custom data. */
public final class ShieldChair extends Chair {
    @Override public boolean isFlammable(BlockState state, IBlockReader world, BlockPos pos, net.minecraft.util.Direction face) {
        return !FurnitureWoodProperties.fireproof(this);
    }
    @Override public int getFlammability(BlockState state, IBlockReader world, BlockPos pos, net.minecraft.util.Direction face) {
        return FurnitureWoodProperties.fireproof(this) ? 0 : super.getFlammability(state, world, pos, face);
    }
    @Override public int getFireSpreadSpeed(BlockState state, IBlockReader world, BlockPos pos, net.minecraft.util.Direction face) {
        return FurnitureWoodProperties.fireproof(this) ? 0 : super.getFireSpreadSpeed(state, world, pos, face);
    }
    public ShieldChair(float hardness, float resistance, SoundType sound, String name) {
        super(hardness, resistance, sound, name);
    }

    @Override public boolean hasTileEntity(BlockState state) { return true; }
    @Override public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new ShieldChairTileEntity();
    }

    @Override public void setPlacedBy(World world, BlockPos pos, BlockState state,
            LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(world, pos, state, placer, stack);
        ShieldChairTileEntity tile = tile(world, pos);
        if (!world.isClientSide && tile != null) tile.setShield(ShieldChairItemData.getShield(stack));
    }

    @Override public net.minecraft.util.ActionResultType use(BlockState state, World world, BlockPos pos,
            PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
        ShieldChairTileEntity tile = tile(world, pos);
        if (tile == null) return net.minecraft.util.ActionResultType.PASS;
        ItemStack held = player.getItemInHand(hand);
        if (player.isShiftKeyDown() && hand == Hand.MAIN_HAND && held.isEmpty() && tile.hasShield()) {
            if (!world.isClientSide) {
                ItemStack returned = tile.getShield();
                tile.setShield(ItemStack.EMPTY);
                if (!player.inventory.add(returned)) popResource(world, pos, returned);
            }
            return net.minecraft.util.ActionResultType.SUCCESS;
        }
        if (held.getItem() == Items.SHIELD && !tile.hasShield()) {
            if (!world.isClientSide) {
                tile.setShield(held);
                if (!player.isCreative()) held.shrink(1);
            }
            return net.minecraft.util.ActionResultType.SUCCESS;
        }
        return !player.isShiftKeyDown() ? super.use(state, world, pos, player, hand, hit) : net.minecraft.util.ActionResultType.PASS;
    }

    @Override public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
        List<ItemStack> drops = new ArrayList<>();
        drops.add(ShieldChairItemData.createChair(this, ItemStack.EMPTY));
        TileEntity savedTile = builder.getOptionalParameter(LootParameters.BLOCK_ENTITY);
        ItemStack shield = savedTile instanceof ShieldChairTileEntity
                ? ((ShieldChairTileEntity) savedTile).getShield() : new ItemStack(Items.SHIELD);
        if (!shield.isEmpty()) drops.add(shield);
        return drops;
    }

    @Override public ItemStack getPickBlock(BlockState state, RayTraceResult target,
            IBlockReader world, BlockPos pos, PlayerEntity player) {
        ShieldChairTileEntity tile = tile(world, pos);
        return ShieldChairItemData.createChair(this, tile == null ? new ItemStack(Items.SHIELD) : tile.getShield());
    }

    @Override public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos pos,
            ISelectionContext context) {
        ShieldChairTileEntity tile = tile(world, pos);
        if (tile == null || tile.hasShield()) return super.getShape(state, world, pos, context);
        VoxelShape frame = VoxelShapes.or(Block.box(1, 0, 1, 15, 8, 14),
                Block.box(1, 8, 1, 3, 22, 3), Block.box(13, 8, 1, 15, 22, 3));
        return getShapes(rotate(frame, net.minecraft.util.Direction.SOUTH))[state.getValue(DIRECTION).get2DDataValue()];
    }

    @Override public VoxelShape getOcclusionShape(BlockState state, IBlockReader world, BlockPos pos) {
        return getShape(state, world, pos, ISelectionContext.empty());
    }

    @Override public void tick(BlockState state, net.minecraft.world.server.ServerWorld world, BlockPos pos, Random random) {
        if (!world.isClientSide && pos.getY() >= 0 && isFree(world.getBlockState(pos.below()))) {
            // Vanilla falling blocks copy only their block state. Our entity also
            // keeps the shield if the chair lands, is saved mid-fall, or drops as an item.
            world.addFreshEntity(new FallingShieldChair(world, pos, state, tile(world, pos)));
        }
    }

    private static ShieldChairTileEntity tile(IBlockReader world, BlockPos pos) {
        TileEntity tile = world.getBlockEntity(pos);
        return tile instanceof ShieldChairTileEntity ? (ShieldChairTileEntity) tile : null;
    }
}
