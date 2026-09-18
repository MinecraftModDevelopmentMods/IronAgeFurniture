package zone.moddev.mc.ironagefurniture.api.blocks.lightholder;

import zone.moddev.mc.ironagefurniture.api.blocks.base.LightHolderSconce;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.storage.loot.LootContext.Builder;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.Hand;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import zone.moddev.mc.ironagefurniture.BlockObjectHolder;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;

public class LightHolderSconceFloor extends LightHolderSconce {
	public LightHolderSconceFloor(Properties properties) {
		super(properties);

		this.registerDefaultState(this.getStateDefinition().any().setValue(DIRECTION, Direction.NORTH).setValue(WATERLOGGED, false));
		this.generateShapes(this.getStateDefinition().getPossibleStates());
	}

	public LightHolderSconceFloor(float hardness, float blastResistance, SoundType sound, String name) {
		super(Block.Properties.of(Material.METAL).strength(hardness, blastResistance).sound(sound));

		this.registerDefaultState(this.getStateDefinition().any().setValue(DIRECTION, Direction.NORTH));
		this.generateShapes(this.getStateDefinition().getPossibleStates());
		this.setRegistryName(name);
	}

	@Override
	public BlockState updateShape(BlockState state, Direction direction, BlockState state2, IWorld levelAccessor, BlockPos pos, BlockPos pos2) {
		return direction == Direction.DOWN && !this.canSurvive(state, levelAccessor, pos) ?
			Blocks.AIR.defaultBlockState() :
			super.updateShape(state, direction, state2, levelAccessor, pos, pos2);
	}

	@Override
	public boolean canSurvive(BlockState state, IWorldReader levelReader, BlockPos pos) {
		return canSupportCenter(levelReader, pos.below(), Direction.UP);
	}

	@Override
	protected void generateShapes(ImmutableList<BlockState> states) {
		ImmutableMap.Builder<BlockState, VoxelShape> builder = new ImmutableMap.Builder<>();

		for (BlockState state : states) {
			VoxelShape shapes = VoxelShapes.empty();

			shapes = VoxelShapes.joinUnoptimized(shapes, getShapes(rotate(Block.box(5.5, 8, 5.5, 10.5, 9, 10.5), Direction.SOUTH))[state.getValue(DIRECTION).get2DDataValue()], IBooleanFunction.OR); // sconce holder
			shapes = VoxelShapes.joinUnoptimized(shapes, getShapes(rotate(Block.box(7.5, 0, 5.5, 8.5, 8, 8.5), Direction.SOUTH))[state.getValue(DIRECTION).get2DDataValue()], IBooleanFunction.OR); // sconce stand

			builder.put(state, shapes.optimize());
		}

		_shapes = builder.build();
	}

	protected Block GetWallVariant() {
		return BlockObjectHolder.light_metal_ironage_sconce_wall_empty_iron;
	}

	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		IWorldReader levelReader = context.getLevel();
		BlockPos clickedPos = context.getClickedPos();
		Direction clickedFace = context.getClickedFace();

		if (clickedFace.getAxis().isHorizontal()) {
			BlockState wallState = GetWallVariant().defaultBlockState().setValue(DIRECTION, clickedFace).setValue(WATERLOGGED, levelReader.getFluidState(clickedPos).getType() == Fluids.WATER);

			if (wallState.canSurvive(levelReader, clickedPos)) {
				return wallState;
			}
		}

	    if (canSupportCenter(levelReader, clickedPos.below(), Direction.UP)) {
	        return super.getStateForPlacement(context);
	    }

	    return null;
	}



	@Override
	public boolean canPlaceLiquid(IBlockReader blockGetter, BlockPos blockPos, BlockState blockState, Fluid fluid) {
		return true;
	}

	protected Block GetGlowVariant() {
		return BlockObjectHolder.light_metal_ironage_sconce_floor_glow_iron;
	}

	protected Block GetTorchVariant() {
		return BlockObjectHolder.light_metal_ironage_sconce_floor_torch_iron;
	}

	protected Block GetLavaVariant() {
		return BlockObjectHolder.light_metal_ironage_sconce_floor_lava_iron;
	}

	protected Block GetRedTorchVariant() {
		return BlockObjectHolder.light_metal_ironage_sconce_floor_redtorch_iron;
	}

	protected Block GetRedVariant() {
		return BlockObjectHolder.light_metal_ironage_sconce_floor_red_iron;
	}

	protected Block GetUnlitTorchVariant() {
		return BlockObjectHolder.light_metal_ironage_sconce_floor_torch_iron_unlit;
	}

	@Override
	protected ActionResultType ActivateSconce(BlockState state, World world, BlockPos pos, PlayerEntity player,
											   Hand hand, BlockRayTraceResult rayTraceResult) {

		ItemStack stackInHand = player.getItemInHand(hand);

		if (stackInHand.getItem() == Blocks.TORCH.asItem()) {
			Block torchSconce;

			if (state.getValue(BlockStateProperties.WATERLOGGED))
				torchSconce = GetUnlitTorchVariant();
			else
				torchSconce = GetTorchVariant();

			return getInteractionResult(state, world, pos, player, stackInHand, torchSconce);
		}

		if (stackInHand.getItem() == BlockObjectHolder.light_metal_ironage_block_floor_glow_clear.asItem())
			return getInteractionResult(state, world, pos, player, stackInHand, GetGlowVariant());

		if (stackInHand.getItem() == BlockObjectHolder.light_metal_ironage_block_floor_lava_clear.asItem()) {
			if (state.getValue(BlockStateProperties.WATERLOGGED)) {
					world.playSound(player, pos, SoundEvents.GLASS_BREAK, SoundCategory.BLOCKS, 1.0F, 1.0F);
					world.playSound(player, pos, SoundEvents.LAVA_EXTINGUISH, SoundCategory.BLOCKS, 0.5F, 2.6F);

				if (!player.isCreative())
					stackInHand.setCount(stackInHand.getCount() - 1);

				Block.popResource(world, pos, new ItemStack(BlockObjectHolder.obsidian_chunk, 1));

				return ActionResultType.CONSUME;
			}
			else
			{
				return getInteractionResult(state, world, pos, player, stackInHand, GetLavaVariant());
			}
		}

		if (stackInHand.getItem() == Blocks.REDSTONE_TORCH.asItem())
			return getInteractionResult(state, world, pos, player, stackInHand, GetRedTorchVariant());

		if (stackInHand.getItem() == BlockObjectHolder.light_metal_ironage_block_floor_red_clear.asItem())
			return getInteractionResult(state, world, pos, player, stackInHand, GetRedVariant());

		return ActionResultType.FAIL;
	}

	private static ActionResultType getInteractionResult(BlockState state, World world, BlockPos pos, PlayerEntity player, ItemStack stackInHand, Block sconce) {
		world.setBlock(pos, sconce.defaultBlockState()
			.setValue(DIRECTION, state.getValue(BlockStateProperties.HORIZONTAL_FACING))
			.setValue(WATERLOGGED, state.getValue(BlockStateProperties.WATERLOGGED)), 3);

		if (!player.isCreative())
			stackInHand.setCount(stackInHand.getCount() - 1);

		return ActionResultType.CONSUME;
	}

	@Override
	public List<ItemStack> getDrops(BlockState state, Builder builder) {
		List<ItemStack> drops;

		Item item = state.getBlock().asItem();
		ItemStack stack = new ItemStack(item, 1);
		drops = new ArrayList<>();
		drops.add(stack);

		return drops;
	}
}
