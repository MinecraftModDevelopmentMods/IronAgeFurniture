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
import net.minecraft.network.IPacket;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;
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
    protected Item getDefaultItem() {
        return BlockObjectHolder.light_metal_ironage_block_floor_lava_clear.asItem();
    }

    @Override
    public void handleEntityEvent(byte eventId) {
        if (eventId != 3) {
            super.handleEntityEvent(eventId);
            return;
        }

        ItemParticleData itemParticle = new ItemParticleData(ParticleTypes.ITEM, getItem());
        for (int i = 0; i < 8; i++) {
            level.addParticle(itemParticle, getX(), getY(), getZ(),
                    (random.nextDouble() - 0.5D) * 0.08D,
                    (random.nextDouble() - 0.5D) * 0.08D,
                    (random.nextDouble() - 0.5D) * 0.08D);
        }
        for (int i = 0; i < 3; i++)
            level.addParticle(ParticleTypes.LAVA, getX(), getY(), getZ(), 0.0D, 0.0D, 0.0D);
    }

    @Override
    protected void onHit(RayTraceResult result) {
        if (!level.isClientSide) {
            if (result.getType() == RayTraceResult.Type.ENTITY) {
                Entity entity = ((EntityRayTraceResult) result).getEntity();
                entity.setSecondsOnFire(ENTITY_FIRE_SECONDS);
                entity.hurt(DamageSource.thrown(this, getOwner()), 0.0F);
            } else if (result.getType() == RayTraceResult.Type.BLOCK) {
                placeImpactFire((BlockRayTraceResult) result);
            }

            shatter(false);
        }
    }

    @Override
    public IPacket<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public void tick() {
        super.tick();

        if (!level.isClientSide && !removed && isInWater()) {
            BlockPos pos = getCommandSenderBlockPosition();
            Direction direction = Direction.fromYRot(yRot);
            level.setBlock(pos, BlockObjectHolder.obsidian_chunk.defaultBlockState()
                    .setValue(BlockStateProperties.HORIZONTAL_FACING, direction)
                    .setValue(BlockStateProperties.WATERLOGGED, true), 3);
            shatter(true);
        }
    }

    private void shatter(boolean extinguished) {
        level.playSound(null, getX(), getY(), getZ(), SoundEvents.GLASS_BREAK,
                SoundCategory.BLOCKS, 1.0F, 1.0F);
        if (extinguished) {
            level.playSound(null, getX(), getY(), getZ(), SoundEvents.LAVA_EXTINGUISH,
                    SoundCategory.BLOCKS, 0.5F, 2.6F);
        }

        level.broadcastEntityEvent(this, (byte) 3);
        remove();
    }

    private void placeImpactFire(BlockRayTraceResult result) {
        Direction side = result.getDirection();
        BlockPos hitPos = result.getBlockPos();
        if (tryPlaceFire(hitPos.relative(side)))
            return;
        if (side != Direction.UP && tryPlaceFire(hitPos.above()))
            return;
        tryPlaceFire(hitPos);
    }

    private boolean tryPlaceFire(BlockPos pos) {
        BlockState existing = level.getBlockState(pos);
        BlockState fire = Blocks.FIRE.defaultBlockState();
        if ((existing.isAir() || existing.getMaterial().isReplaceable()) && fire.canSurvive(level, pos)) {
            level.setBlock(pos, fire, 3);
            return true;
        }
        return false;
    }
}
