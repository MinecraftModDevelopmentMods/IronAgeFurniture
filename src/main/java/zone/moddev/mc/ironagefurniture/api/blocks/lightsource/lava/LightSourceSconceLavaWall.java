package zone.moddev.mc.ironagefurniture.api.blocks.lightsource.lava;

import zone.moddev.mc.ironagefurniture.api.blocks.base.FurnitureBlock;
import zone.moddev.mc.ironagefurniture.api.blocks.lightsource.glow.LightSourceSconceGlowWall;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.fluid.FluidState;
import net.minecraft.block.material.Material;
import com.mojang.datafixers.util.Pair;
import net.minecraft.util.Direction;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.SoundCategory;

import java.util.Map;
import java.util.Random;

import zone.moddev.mc.ironagefurniture.BlockObjectHolder;

import net.minecraft.util.math.BlockPos;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.world.World;
import net.minecraft.world.IWorld;

public class LightSourceSconceLavaWall extends LightSourceSconceGlowWall {
	@Override
	public BlockState updateShape(BlockState state, Direction direction, BlockState state2, IWorld levelAccessor, BlockPos pos, BlockPos pos2) {
	    if (direction.getOpposite() == state.getValue(FurnitureBlock.DIRECTION) && !state.canSurvive(levelAccessor, pos)) {
	        levelAccessor.destroyBlock(pos, true);
	        return LightDrop().defaultBlockState().setValue(FurnitureBlock.WATERLOGGED, false);
	    }

	    return super.updateShape(state, direction, state2, levelAccessor, pos, pos2);
	}

	@Override
	public boolean removedByPlayer(BlockState state, World level, BlockPos pos, PlayerEntity player, boolean willHarvest,
									   FluidState fluid) {

		boolean isSilkTouch = false;

		ItemStack tool = player.inventory.getSelected();

		if (tool != null) {
			Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(tool);

			if (enchantments != null && !enchantments.isEmpty())
				isSilkTouch = enchantments.get(Enchantments.SILK_TOUCH) > 0;
		}

		if (isSilkTouch && !player.isCreative())
			Block.popResource(level, pos, new ItemStack(LightDrop(), 1));

		boolean destroyed = super.removedByPlayer(state, level, pos, player, willHarvest, fluid);

		if (!isSilkTouch && !player.isCreative()) {
			level.playSound(player, pos, SoundEvents.GLASS_BREAK, SoundCategory.BLOCKS, friction, explosionResistance);
			level.setBlock(pos, LightDrop().defaultBlockState().setValue(FurnitureBlock.WATERLOGGED, false), 3, 3);
		}

		return destroyed;
	}

	@Override
	protected Block LightDrop() {
		return BlockObjectHolder.light_metal_ironage_block_floor_lava_clear;
	}

	@Override
	protected boolean ShouldDrop() {
		return false;
	}

	public LightSourceSconceLavaWall(float hardness, float blastResistance, SoundType sound, String name) {
		super(Block.Properties.of(Material.METAL).strength(hardness, blastResistance).sound(sound).lightLevel((p_50886_) -> 14));

		this.registerDefaultState(this.getStateDefinition().any().setValue(FurnitureBlock.DIRECTION, Direction.NORTH));
		this.generateShapes(this.getStateDefinition().getPossibleStates());
		this.setRegistryName(name);
	}

	@Override
	public void animateTick(BlockState state, World level, BlockPos pos, Random rnd) {
		BlockPos blockpos = pos.above();

		if (level.getBlockState(blockpos).isAir() && !level.getBlockState(blockpos).isSolidRender(level, blockpos)) {
			if (rnd.nextInt(25) == 0) {
				Direction direction = state.getValue(FurnitureBlock.DIRECTION);

				Pair<Double, Double> rotated = FurnitureBlock.rotate(0.6D, 0.5D, state.getValue(FurnitureBlock.DIRECTION));

				double x = (double) pos.getX() + rotated.getFirst();
				double y = (double) pos.getY() + 0.5D;
				double z = (double) pos.getZ() + rotated.getSecond();

				Direction direction1 = direction.getOpposite();

				level.addParticle(ParticleTypes.LAVA, x + 0.27D * (double) direction1.getStepX(), y + 0.22D, z + 0.27D * (double) direction1.getStepZ(), 0.0D, 0.0D, 0.0D);
				level.playLocalSound(x, y, z, SoundEvents.LAVA_POP, SoundCategory.BLOCKS, 0.2F + rnd.nextFloat() * 0.2F, 0.9F + rnd.nextFloat() * 0.15F, false);
			}

			if (rnd.nextInt(200) == 0)
				level.playLocalSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.LAVA_AMBIENT, SoundCategory.BLOCKS, 0.2F + rnd.nextFloat() * 0.2F, 0.9F + rnd.nextFloat() * 0.15F, false);
		}

		super.animateTick(state, level, pos, rnd);
	}
}
