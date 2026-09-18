package zone.moddev.mc.ironagefurniture.api.entity;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.Direction;
import net.minecraft.particles.ItemParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.DamageSource;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ProjectileItemEntity;
import net.minecraft.item.Item;
import net.minecraft.world.World;
import net.minecraft.block.Blocks;
import net.minecraft.block.BlockState;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import zone.moddev.mc.ironagefurniture.BlockObjectHolder;
import zone.moddev.mc.ironagefurniture.registers.entities;

public class ThrownLavaLamp extends ProjectileItemEntity {
    private static final int ENTITY_FIRE_SECONDS = 5;

    public ThrownLavaLamp(EntityType<? extends ThrownLavaLamp> type, World level) {
        super(type, level);
    }

    public ThrownLavaLamp(World level, LivingEntity owner) {
        super(entities.THROWN_LAVA_LAMP.get(), owner, level);
    }

    @Override
    protected Item func_213885_i() {
        return BlockObjectHolder.light_metal_ironage_block_floor_lava_clear.asItem();
    }

    @Override
    public void handleStatusUpdate(byte eventId) {
        if (eventId != 3) {
            super.handleStatusUpdate(eventId);
            return;
        }

        ItemParticleData itemParticle = new ItemParticleData(ParticleTypes.ITEM, getItem());
        for (int i = 0; i < 8; i++) {
            world.addParticle(itemParticle, posX, posY, posZ,
                    (rand.nextDouble() - 0.5D) * 0.08D,
                    (rand.nextDouble() - 0.5D) * 0.08D,
                    (rand.nextDouble() - 0.5D) * 0.08D);
        }
        for (int i = 0; i < 3; i++)
            world.addParticle(ParticleTypes.LAVA, posX, posY, posZ, 0.0D, 0.0D, 0.0D);
    }

    @Override
    protected void onImpact(RayTraceResult result) {
        if (!world.isRemote) {
            if (result.getType() == RayTraceResult.Type.ENTITY) {
                Entity entity = ((EntityRayTraceResult) result).getEntity();
                entity.setFire(ENTITY_FIRE_SECONDS);
                entity.attackEntityFrom(DamageSource.causeThrownDamage(this, getThrower()), 0.0F);
            } else if (result.getType() == RayTraceResult.Type.BLOCK) {
                placeImpactFire((BlockRayTraceResult) result);
            }

            shatter(false);
        }
    }

    @Override
    public void tick() {
        super.tick();

        if (!world.isRemote && !removed && isInWater()) {
            BlockPos pos = getPosition();
            Direction direction = Direction.fromAngle(rotationYaw);
            world.setBlockState(pos, BlockObjectHolder.obsidian_chunk.getDefaultState()
                     .with(BlockStateProperties.HORIZONTAL_FACING, direction)
                     .with(BlockStateProperties.WATERLOGGED, true), 3);
            shatter(true);
        }
    }

    private void shatter(boolean extinguished) {
        world.playSound(null, posX, posY, posZ, SoundEvents.BLOCK_GLASS_BREAK,
                SoundCategory.BLOCKS, 1.0F, 1.0F);
        if (extinguished) {
            world.playSound(null, posX, posY, posZ, SoundEvents.BLOCK_FIRE_EXTINGUISH,
                    SoundCategory.BLOCKS, 0.5F, 2.6F);
        }

        world.setEntityState(this, (byte) 3);
        remove();
    }

    private void placeImpactFire(BlockRayTraceResult result) {
        Direction side = result.getFace();
        BlockPos hitPos = result.getPos();
        if (tryPlaceFire(hitPos .offset(side)))
            return;
        if (side != Direction.UP && tryPlaceFire(hitPos.up()))
            return;
        tryPlaceFire(hitPos);
    }

    private boolean tryPlaceFire(BlockPos pos) {
        BlockState existing = world.getBlockState(pos);
        BlockState fire = Blocks.FIRE.getDefaultState();
        if ((world.isAirBlock(pos) || existing.getMaterial().isReplaceable()) && fire.isValidPosition(world, pos)) {
            world.setBlockState(pos, fire, 3);
            return true;
        }
        return false;
    }
}
