package zone.moddev.mc.ironagefurniture.api.entity;

import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.FallingBlockEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.IPacket;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.network.NetworkHooks;
import zone.moddev.mc.ironagefurniture.api.ShieldChairItemData;
import zone.moddev.mc.ironagefurniture.api.tile.ShieldChairTileEntity;
import zone.moddev.mc.ironagefurniture.registers.entities;

/** A falling shield chair carries the same saved shield as a placed chair. */
public final class FallingShieldChair extends FallingBlockEntity implements IEntityAdditionalSpawnData {
    public FallingShieldChair(EntityType<? extends FallingShieldChair> type, World world) {
        super(type, world);
    }

    public FallingShieldChair(World world, BlockPos pos, BlockState state, ShieldChairTileEntity tile) {
        this(entities.FALLING_SHIELD_CHAIR.get(), world);
        CompoundNBT saved = new CompoundNBT();
        saved.put("BlockState", NBTUtil.writeBlockState(state));
        saved.putInt("Time", 0);
        saved.putBoolean("DropItem", true);
        saved.put("TileEntityData", tile == null ? ShieldChairItemData.writeShield(ShieldChairItemData.getShield(ItemStack.EMPTY))
                : tile.save(new CompoundNBT()));
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
                    ShieldChairItemData.readShield(blockData == null ? new CompoundNBT() : blockData));
        }
        return super.spawnAtLocation(stack, offsetY);
    }

    @Override public IPacket<?> getAddEntityPacket() { return NetworkHooks.getEntitySpawningPacket(this); }
    @Override public void writeSpawnData(PacketBuffer buffer) {
        CompoundNBT saved = new CompoundNBT();
        addAdditionalSaveData(saved);
        buffer.writeNbt(saved);
    }
    @Override public void readSpawnData(PacketBuffer buffer) {
        CompoundNBT saved = buffer.readNbt();
        if (saved != null) readAdditionalSaveData(saved);
    }
}
