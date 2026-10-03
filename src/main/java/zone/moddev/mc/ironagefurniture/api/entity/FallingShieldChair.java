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
                : tile.write(new CompoundNBT()));
        readAdditional(saved);
        setPosition(pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D);
        setOrigin(pos);
        prevPosX = posX;
        prevPosY = posY;
        prevPosZ = posZ;
    }

    @Override public ItemEntity entityDropItem(ItemStack stack, float offsetY) {
        if (!world.isRemote && !stack.isEmpty() && getBlockState().getBlock().asItem() == stack.getItem()) {
            stack = ShieldChairItemData.createChair(getBlockState().getBlock(),
                    ShieldChairItemData.readShield(tileEntityData == null ? new CompoundNBT() : tileEntityData));
        }
        return super.entityDropItem(stack, offsetY);
    }

    @Override public IPacket<?> createSpawnPacket() { return NetworkHooks.getEntitySpawningPacket(this); }
    @Override public void writeSpawnData(PacketBuffer buffer) {
        CompoundNBT saved = new CompoundNBT();
        writeAdditional(saved);
        buffer.writeCompoundTag(saved);
    }
    @Override public void readSpawnData(PacketBuffer buffer) {
        CompoundNBT saved = buffer.readCompoundTag();
        if (saved != null) readAdditional(saved);
    }
}
