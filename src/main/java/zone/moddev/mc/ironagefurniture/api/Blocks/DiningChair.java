package zone.moddev.mc.ironagefurniture.api.Blocks;

import java.util.List;

import zone.moddev.mc.ironagefurniture.api.entity.Seat;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class DiningChair extends Chair {
	public static final PropertyBool TUCKED = PropertyBool.create("tucked");
	private static final double TUCKED_OFFSET = 10.0D / 16.0D;

	public DiningChair(Material materialIn, String name, float resistance, double yOffset, float hardness) {
		super(materialIn, name, resistance, yOffset, hardness);
		this.setDefaultState(this.blockState.getBaseState()
			.withProperty(FACING, EnumFacing.NORTH)
			.withProperty(TUCKED, Boolean.valueOf(false)));
	}

	public DiningChair(Material materialIn, String name, float resistance, float hardness) {
		super(materialIn, name, resistance, hardness);
		this.setDefaultState(this.blockState.getBaseState()
			.withProperty(FACING, EnumFacing.NORTH)
			.withProperty(TUCKED, Boolean.valueOf(false)));
	}

	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY,
			float hitZ, int meta, EntityLivingBase placer, ItemStack stack) {
		return super.getStateForPlacement(world, pos, facing, hitX, hitY, hitZ, meta, placer, stack)
			.withProperty(TUCKED, Boolean.valueOf(false));
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return this.getDefaultState()
			.withProperty(FACING, EnumFacing.getHorizontal(meta & 3))
			.withProperty(TUCKED, Boolean.valueOf((meta & 4) != 0));
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		int meta = ((EnumFacing)state.getValue(FACING)).getHorizontalIndex();
		return ((Boolean)state.getValue(TUCKED)).booleanValue() ? meta | 4 : meta;
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] { FACING, TUCKED });
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
		if (playerIn.isSneaking()) {
			return this.handleTuckToggle(worldIn, pos, state);
		}

		if (((Boolean)state.getValue(TUCKED)).booleanValue()) {
			return true;
		}

		return super.onBlockActivated(worldIn, pos, state, playerIn, hand, heldItem, side, hitX, hitY, hitZ);
	}

	private boolean handleTuckToggle(World world, BlockPos pos, IBlockState state) {
		boolean tucked = ((Boolean)state.getValue(TUCKED)).booleanValue();
		if (!this.isFacingDiningTable(world, pos, state)) {
			if (tucked) {
				if (!world.isRemote) {
					world.setBlockState(pos, state.withProperty(TUCKED, Boolean.valueOf(false)), 3);
				}
				return true;
			}

			return false;
		}

		if (!world.isRemote) {
			world.setBlockState(pos, state.withProperty(TUCKED, Boolean.valueOf(!tucked)), 3);
		}
		return true;
	}

	@Override
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn) {
		if (((Boolean)state.getValue(TUCKED)).booleanValue() && !this.isFacingDiningTable(worldIn, pos, state)) {
			if (!worldIn.isRemote) {
				worldIn.setBlockState(pos, state.withProperty(TUCKED, Boolean.valueOf(false)), 3);
			}
			return;
		}

		super.neighborChanged(state, worldIn, pos, blockIn);
	}

	private boolean isFacingDiningTable(IBlockAccess world, BlockPos pos, IBlockState state) {
		BlockPos tablePos = pos.offset(((EnumFacing)state.getValue(FACING)).getOpposite());
		Block tableBlock = world.getBlockState(tablePos).getBlock();
		return tableBlock instanceof DiningTable && !(tableBlock instanceof LowTable);
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return this.shiftWhenTucked(super.getBoundingBox(state, source, pos), state);
	}

	@Override
	public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox,
			List<AxisAlignedBB> collidingBoxes, Entity entityIn) {
		if (entityIn instanceof Seat) {
			return;
		}

		AxisAlignedBB backBox;
		switch (state.getValue(FACING)) {
			case NORTH:
				backBox = BACKNORTH;
				break;
			case SOUTH:
				backBox = BACKSOUTH;
				break;
			case WEST:
				backBox = BACKWEST;
				break;
			default:
				backBox = BACKEAST;
				break;
		}

		super.addCollisionBoxToList(pos, entityBox, collidingBoxes, this.shiftWhenTucked(backBox, state));
		super.addCollisionBoxToList(pos, entityBox, collidingBoxes, this.shiftWhenTucked(BASEBB, state));
	}

	private AxisAlignedBB shiftWhenTucked(AxisAlignedBB box, IBlockState state) {
		if (!((Boolean)state.getValue(TUCKED)).booleanValue()) {
			return box;
		}

		EnumFacing front = ((EnumFacing)state.getValue(FACING)).getOpposite();
		return box.offset(front.getFrontOffsetX() * TUCKED_OFFSET, 0.0D,
			front.getFrontOffsetZ() * TUCKED_OFFSET);
	}
}
