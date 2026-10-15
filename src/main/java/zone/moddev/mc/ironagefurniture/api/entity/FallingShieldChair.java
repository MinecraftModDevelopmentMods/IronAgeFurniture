package zone.moddev.mc.ironagefurniture.api.entity;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraftforge.fmllegacy.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fmllegacy.network.NetworkHooks;
import zone.moddev.mc.ironagefurniture.api.ShieldChairItemData;
import zone.moddev.mc.ironagefurniture.api.tile.ShieldChairTileEntity;
import zone.moddev.mc.ironagefurniture.registers.entities;

/** A falling shield chair carries the same saved shield as a placed chair. */
public final class FallingShieldChair extends FallingBlockEntity implements IEntityAdditionalSpawnData {
    public FallingShieldChair(EntityType<? extends FallingShieldChair> type, Level world) {
        super(type, world);
    }

    public FallingShieldChair(Level world, BlockPos pos, BlockState state, ShieldChairTileEntity tile) {
        this(entities.FALLING_SHIELD_CHAIR.get(), world);
        CompoundTag saved = new CompoundTag();
        saved.put("BlockState", NbtUtils.writeBlockState(state));
        saved.putInt("Time", 0);
        saved.putBoolean("DropItem", true);
        saved.put("TileEntityData", tile == null ? ShieldChairItemData.writeShield(ShieldChairItemData.getShield(ItemStack.EMPTY))
                : tile.save(new CompoundTag()));
        readAdditionalSaveData(saved);
        setPos(pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D);
        setStartPos(pos);
        xo = getX();
        yo = getY();
        zo = getZ();
    }

    @Override public ItemEntity spawnAtLocation(ItemStack stack, float offsetY) {
        if (!level.isClientSide && !stack.isEmpty() && getBlockState().getBlock().asItem() == stack.getItem()) {
            stack = ShieldChairItemData.createChair(getBlockState().getBlock(),
                    ShieldChairItemData.readShield(blockData == null ? new CompoundTag() : blockData));
        }
        return super.spawnAtLocation(stack, offsetY);
    }

    @Override public Packet<?> getAddEntityPacket() { return NetworkHooks.getEntitySpawningPacket(this); }
    @Override public void writeSpawnData(FriendlyByteBuf buffer) {
        CompoundTag saved = new CompoundTag();
        addAdditionalSaveData(saved);
        buffer.writeNbt(saved);
    }
    @Override public void readSpawnData(FriendlyByteBuf buffer) {
        CompoundTag saved = buffer.readNbt();
        if (saved != null) readAdditionalSaveData(saved);
    }
}
