package zone.moddev.mc.ironagefurniture.api.entity;

import java.lang.reflect.Field;

import zone.moddev.mc.ironagefurniture.api.MetalVariantHelper;
import zone.moddev.mc.ironagefurniture.api.MetalVariantHelper.MetalVariant;

import io.netty.buffer.ByteBuf;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

public class EntityFallingMetalBlock extends EntityFallingBlock implements IEntityAdditionalSpawnData {
    private static final Field FALL_TILE = ReflectionHelper.findField(EntityFallingBlock.class,
        "fallTile", "field_175132_d");

    private MetalVariant metal = MetalVariant.IRON;

    public EntityFallingMetalBlock(World worldIn) {
        super(worldIn);
        this.preventEntitySpawning = true;
        this.setSize(0.98F, 0.98F);
        this.setFallingState(Blocks.SAND.getDefaultState());
    }

    public EntityFallingMetalBlock(World worldIn, double x, double y, double z, IBlockState fallingBlockState) {
        super(worldIn, x, y, z, fallingBlockState);
        this.metal = this.getMetalFromState(fallingBlockState);
        this.setFallingState(this.withMetal(fallingBlockState, this.metal));
        this.writeMetalTileData();
    }

    @Override
    public IBlockState getBlock() {
        return this.withMetal(super.getBlock(), this.metal);
    }

    @Override
    public void writeSpawnData(ByteBuf buffer) {
        IBlockState state = this.getBlock();

        if (state == null) {
            state = Blocks.SAND.getDefaultState();
        }

        buffer.writeInt(Block.getStateId(state));
        buffer.writeByte(this.metal.getMeta());
    }

    @Override
    public void readSpawnData(ByteBuf additionalData) {
        IBlockState state = Block.getStateById(additionalData.readInt());
        this.metal = MetalVariant.byMeta(additionalData.readUnsignedByte());
        this.setFallingState(this.withMetal(state, this.metal));
        this.writeMetalTileData();
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setString("Metal", this.metal.getName());
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);

        if (compound.hasKey("Metal")) {
            this.metal = MetalVariant.byName(compound.getString("Metal"));
        } else if (this.tileEntityData != null && this.tileEntityData.hasKey("Metal")) {
            this.metal = MetalVariant.byName(this.tileEntityData.getString("Metal"));
        } else {
            this.metal = this.getMetalFromState(super.getBlock());
        }

        this.setFallingState(this.withMetal(super.getBlock(), this.metal));
        this.writeMetalTileData();
    }

    private MetalVariant getMetalFromState(IBlockState state) {
        if (state != null && state.getProperties().containsKey(MetalVariantHelper.METAL)) {
            return state.getValue(MetalVariantHelper.METAL);
        }

        return MetalVariant.IRON;
    }

    private IBlockState withMetal(IBlockState state, MetalVariant metal) {
        if (state != null && state.getProperties().containsKey(MetalVariantHelper.METAL)) {
            return state.withProperty(MetalVariantHelper.METAL, metal);
        }

        return state;
    }

    private void setFallingState(IBlockState state) {
        try {
            FALL_TILE.set(this, state);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Unable to set falling metal block state", e);
        }
    }

    private void writeMetalTileData() {
        if (this.tileEntityData == null) {
            this.tileEntityData = new NBTTagCompound();
        }

        this.tileEntityData.setString("Metal", this.metal.getName());
    }
}
