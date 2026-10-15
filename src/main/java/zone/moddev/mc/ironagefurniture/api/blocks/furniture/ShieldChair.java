package zone.moddev.mc.ironagefurniture.api.blocks.furniture;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.InteractionHand;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import zone.moddev.mc.ironagefurniture.api.ShieldChairItemData;
import zone.moddev.mc.ironagefurniture.api.entity.FallingShieldChair;
import zone.moddev.mc.ironagefurniture.api.tile.ShieldChairTileEntity;

/** The removable back is a real shield, including its damage and custom data. */
public final class ShieldChair extends Chair implements net.minecraft.world.level.block.EntityBlock {
    @Override public boolean isFlammable(BlockState state, BlockGetter world, BlockPos pos, net.minecraft.core.Direction face) {
        return !FurnitureWoodProperties.fireproof(this);
    }
    @Override public int getFlammability(BlockState state, BlockGetter world, BlockPos pos, net.minecraft.core.Direction face) {
        return FurnitureWoodProperties.fireproof(this) ? 0 : super.getFlammability(state, world, pos, face);
    }
    @Override public int getFireSpreadSpeed(BlockState state, BlockGetter world, BlockPos pos, net.minecraft.core.Direction face) {
        return FurnitureWoodProperties.fireproof(this) ? 0 : super.getFireSpreadSpeed(state, world, pos, face);
    }
    public ShieldChair(float hardness, float resistance, SoundType sound, String name) {
        super(hardness, resistance, sound, name);
    }

    @Override public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new ShieldChairTileEntity(pos, state);
    }

    @Override public void setPlacedBy(Level world, BlockPos pos, BlockState state,
            LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(world, pos, state, placer, stack);
        ShieldChairTileEntity tile = tile(world, pos);
        if (!world.isClientSide && tile != null) tile.setShield(ShieldChairItemData.getShield(stack));
    }

    @Override public net.minecraft.world.InteractionResult use(BlockState state, Level world, BlockPos pos,
            Player player, InteractionHand hand, BlockHitResult hit) {
        ShieldChairTileEntity tile = tile(world, pos);
        if (tile == null) return net.minecraft.world.InteractionResult.PASS;
        ItemStack held = player.getItemInHand(hand);
        if (player.isShiftKeyDown() && hand == InteractionHand.MAIN_HAND && held.isEmpty() && tile.hasShield()) {
            if (!world.isClientSide) {
                ItemStack returned = tile.getShield();
                tile.setShield(ItemStack.EMPTY);
                if (!player.getInventory().add(returned)) popResource(world, pos, returned);
            }
            return net.minecraft.world.InteractionResult.SUCCESS;
        }
        if (held.getItem() == Items.SHIELD && !tile.hasShield()) {
            if (!world.isClientSide) {
                tile.setShield(held);
                if (!player.isCreative()) held.shrink(1);
            }
            return net.minecraft.world.InteractionResult.SUCCESS;
        }
        return !player.isShiftKeyDown() ? super.use(state, world, pos, player, hand, hit) : net.minecraft.world.InteractionResult.PASS;
    }

    @Override public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
        List<ItemStack> drops = new ArrayList<>();
        drops.add(ShieldChairItemData.createChair(this, ItemStack.EMPTY));
        BlockEntity savedTile = builder.getOptionalParameter(LootContextParams.BLOCK_ENTITY);
        ItemStack shield = savedTile instanceof ShieldChairTileEntity
                ? ((ShieldChairTileEntity) savedTile).getShield() : new ItemStack(Items.SHIELD);
        if (!shield.isEmpty()) drops.add(shield);
        return drops;
    }

    @Override public ItemStack getPickBlock(BlockState state, HitResult target,
            BlockGetter world, BlockPos pos, Player player) {
        ShieldChairTileEntity tile = tile(world, pos);
        return ShieldChairItemData.createChair(this, tile == null ? new ItemStack(Items.SHIELD) : tile.getShield());
    }

    @Override public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos,
            CollisionContext context) {
        ShieldChairTileEntity tile = tile(world, pos);
        if (tile == null || tile.hasShield()) return super.getShape(state, world, pos, context);
        VoxelShape frame = Shapes.or(Block.box(1, 0, 1, 15, 8, 14),
                Block.box(1, 8, 1, 3, 22, 3), Block.box(13, 8, 1, 15, 22, 3));
        return getShapes(rotate(frame, net.minecraft.core.Direction.SOUTH))[state.getValue(DIRECTION).get2DDataValue()];
    }

    @Override public VoxelShape getOcclusionShape(BlockState state, BlockGetter world, BlockPos pos) {
        return getShape(state, world, pos, CollisionContext.empty());
    }

    @Override public void tick(BlockState state, net.minecraft.server.level.ServerLevel world, BlockPos pos, Random random) {
        if (!world.isClientSide && pos.getY() >= 0 && isFree(world.getBlockState(pos.below()))) {
            // Vanilla falling blocks copy only their block state. Our entity also
            // keeps the shield if the chair lands, is saved mid-fall, or drops as an item.
            world.addFreshEntity(new FallingShieldChair(world, pos, state, tile(world, pos)));
        }
    }

    private static ShieldChairTileEntity tile(BlockGetter world, BlockPos pos) {
        BlockEntity tile = world.getBlockEntity(pos);
        return tile instanceof ShieldChairTileEntity ? (ShieldChairTileEntity) tile : null;
    }
}
