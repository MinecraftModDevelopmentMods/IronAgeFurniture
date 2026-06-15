package com.mcmoddev.ironagefurniture.api.entity;

import com.mcmoddev.ironagefurniture.BlockObjectHolder;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class EntityThrownLavaLamp extends EntityThrowable {
    private static final int ENTITY_FIRE_SECONDS = 5;

    public EntityThrownLavaLamp(World worldIn) {
        super(worldIn);
    }

    public EntityThrownLavaLamp(World worldIn, EntityLivingBase throwerIn) {
        super(worldIn, throwerIn);
    }

    public EntityThrownLavaLamp(World worldIn, double x, double y, double z) {
        super(worldIn, x, y, z);
    }

    @Override
    protected void onImpact(RayTraceResult result) {
        this.spawnBreakParticles();

        if (!this.world.isRemote) {
            this.world.playSound(null, this.posX, this.posY, this.posZ,
                SoundEvents.BLOCK_GLASS_BREAK, SoundCategory.BLOCKS, 1.0F, 1.0F);

            if (result.entityHit != null) {
                this.igniteEntity(result.entityHit);
            } else if (result.typeOfHit == RayTraceResult.Type.BLOCK) {
                this.placeImpactFire(result);
            }

            this.setDead();
        }
    }

    private void igniteEntity(Entity entity) {
        entity.setFire(ENTITY_FIRE_SECONDS);
        entity.attackEntityFrom(DamageSource.causeThrownDamage(this, this.getThrower()), 0.0F);
    }

    private void placeImpactFire(RayTraceResult result) {
        EnumFacing side = result.sideHit == null ? EnumFacing.UP : result.sideHit;
        BlockPos hitPos = result.getBlockPos();

        if (this.tryPlaceFire(hitPos.offset(side))) {
            return;
        }

        if (side != EnumFacing.UP && this.tryPlaceFire(hitPos.up())) {
            return;
        }

        this.tryPlaceFire(hitPos);
    }

    private boolean tryPlaceFire(BlockPos pos) {
        IBlockState state = this.world.getBlockState(pos);
        Block block = state.getBlock();

        if ((this.world.isAirBlock(pos) || block.isReplaceable(this.world, pos))
                && Blocks.FIRE.canPlaceBlockAt(this.world, pos)) {
            this.world.setBlockState(pos, Blocks.FIRE.getDefaultState(), 3);
            return true;
        }

        return false;
    }

    private void spawnBreakParticles() {
        Item item = Item.getItemFromBlock(BlockObjectHolder.light_metal_ironage_block_floor_lava_clear);
        int itemId = Item.getIdFromItem(item);

        for (int i = 0; i < 8; i++) {
            this.world.spawnParticle(EnumParticleTypes.ITEM_CRACK, this.posX, this.posY, this.posZ,
                ((double)this.rand.nextFloat() - 0.5D) * 0.08D,
                ((double)this.rand.nextFloat() - 0.5D) * 0.08D,
                ((double)this.rand.nextFloat() - 0.5D) * 0.08D,
                new int[] { itemId });
        }

        for (int i = 0; i < 3; i++) {
            this.world.spawnParticle(EnumParticleTypes.LAVA, this.posX, this.posY, this.posZ,
                0.0D, 0.0D, 0.0D, new int[0]);
        }
    }

}
