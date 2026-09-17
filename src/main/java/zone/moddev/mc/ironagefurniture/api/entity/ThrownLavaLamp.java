package zone.moddev.mc.ironagefurniture.api.entity;

import zone.moddev.mc.ironagefurniture.init.ModVanillaLights;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

public class ThrownLavaLamp extends ThrowableItemProjectile {
	private static final int ENTITY_FIRE_SECONDS = 5;

	public ThrownLavaLamp(EntityType<? extends ThrownLavaLamp> type, Level level) {
		super(type, level);
	}

	public ThrownLavaLamp(Level level, LivingEntity owner) {
		super(Entities.THROWN_LAVA_LAMP.get(), owner, level);
	}

	@Override
	protected Item getDefaultItem() {
		return ModVanillaLights.light_metal_ironage_block_floor_lava_clear.get().asItem();
	}

	@Override
	public void handleEntityEvent(byte eventId) {
		if (eventId != 3) {
			super.handleEntityEvent(eventId);
			return;
		}

		ItemParticleOption itemParticle = new ItemParticleOption(ParticleTypes.ITEM, getItem());
		for (int i = 0; i < 8; i++) {
			level().addParticle(itemParticle, getX(), getY(), getZ(),
				(random.nextDouble() - 0.5D) * 0.08D,
				(random.nextDouble() - 0.5D) * 0.08D,
				(random.nextDouble() - 0.5D) * 0.08D);
		}

		for (int i = 0; i < 3; i++)
			level().addParticle(ParticleTypes.LAVA, getX(), getY(), getZ(), 0.0D, 0.0D, 0.0D);
	}

	@Override
	protected void onHitEntity(EntityHitResult result) {
		super.onHitEntity(result);

		if (!level().isClientSide) {
			Entity entity = result.getEntity();
			entity.igniteForSeconds(ENTITY_FIRE_SECONDS);
			entity.hurt(damageSources().thrown(this, getOwner()), 0.0F);
		}
	}

	@Override
	protected void onHitBlock(BlockHitResult result) {
		super.onHitBlock(result);

		if (!level().isClientSide)
			placeImpactFire(result);
	}

	@Override
	protected void onHit(HitResult result) {
		super.onHit(result);

		if (!level().isClientSide) {
			level().playSound(null, getX(), getY(), getZ(), SoundEvents.GLASS_BREAK, SoundSource.BLOCKS, 1.0F, 1.0F);
			level().broadcastEntityEvent(this, (byte)3);
			discard();
		}
	}

	private void placeImpactFire(BlockHitResult result) {
		Direction side = result.getDirection();
		BlockPos hitPos = result.getBlockPos();

		if (tryPlaceFire(hitPos.relative(side)))
			return;

		if (side != Direction.UP && tryPlaceFire(hitPos.above()))
			return;

		tryPlaceFire(hitPos);
	}

	private boolean tryPlaceFire(BlockPos pos) {
		BlockState existing = level().getBlockState(pos);
		BlockState fire = BaseFireBlock.getState(level(), pos);

		if ((existing.isAir() || existing.canBeReplaced()) && fire.canSurvive(level(), pos)) {
			level().setBlock(pos, fire, 3);
			return true;
		}

		return false;
	}
}
