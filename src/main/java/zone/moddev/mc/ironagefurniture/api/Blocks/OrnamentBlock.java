package zone.moddev.mc.ironagefurniture.api.Blocks;

import java.util.List;

import zone.moddev.mc.ironagefurniture.Ironagefurniture;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class OrnamentBlock extends BlockHBase {
	public static final PropertyInteger VARIANT = PropertyInteger.create("variant", 0, 3);

	private final String[] variantNames;
	private final String[] modelNames;
	private final AxisAlignedBB[] bounds;

	public OrnamentBlock(Material materialIn, String name, String[] variantNames, String[] modelNames,
			AxisAlignedBB[] bounds, float resistance, float hardness, SoundType soundType, String harvestTool) {
		super(materialIn);
		this.variantNames = variantNames;
		this.modelNames = modelNames;
		this.bounds = bounds;
		this.blockResistance = resistance;
		this.blockHardness = hardness;
		this.setSoundType(soundType);
		this.setCreativeTab(Ironagefurniture.ironagefurnitureTab);
		this.setDefaultState(this.blockState.getBaseState()
			.withProperty(FACING, EnumFacing.NORTH)
			.withProperty(VARIANT, Integer.valueOf(0)));

		if (harvestTool != null) {
			this.setHarvestLevel(harvestTool, 0);
		}
	}

	public int getVariantCount() {
		return this.variantNames.length;
	}

	public String getVariantName(int meta) {
		return this.variantNames[this.clampVariant(meta)];
	}

	public String getModelName(int meta) {
		return this.modelNames[this.clampVariant(meta)];
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return this.getDefaultState()
			.withProperty(FACING, EnumFacing.getHorizontal(meta & 3))
			.withProperty(VARIANT, Integer.valueOf((meta >> 2) & 3));
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return ((Integer)state.getValue(VARIANT)).intValue() * 4
			+ ((EnumFacing)state.getValue(FACING)).getHorizontalIndex();
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] { FACING, VARIANT });
	}

	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing side, float hitX, float hitY,
			float hitZ, int meta, EntityLivingBase placer, ItemStack stack) {
		int variant = stack == null ? 0 : this.clampVariant(stack.getMetadata());
		EnumFacing facing = placer == null ? EnumFacing.NORTH : placer.getHorizontalFacing().getOpposite();
		return this.getDefaultState().withProperty(FACING, facing).withProperty(VARIANT, Integer.valueOf(variant));
	}

	@Override
	public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
		return super.canPlaceBlockAt(worldIn, pos) && this.hasTopSupport(worldIn, pos);
	}

	@Override
	public boolean canPlaceBlockOnSide(World worldIn, BlockPos pos, EnumFacing side) {
		return side == EnumFacing.UP && this.canPlaceBlockAt(worldIn, pos);
	}

	@Override
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, net.minecraft.block.Block blockIn) {
		if (!this.hasTopSupport(worldIn, pos)) {
			this.dropBlockAsItem(worldIn, pos, state, 0);
			worldIn.setBlockState(pos, Blocks.AIR.getDefaultState(), 3);
		}
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return this.getRotatedBounds(state);
	}

	@Override
	public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox,
			List<AxisAlignedBB> collidingBoxes, Entity entityIn) {
		addCollisionBoxToList(pos, entityBox, collidingBoxes, this.getRotatedBounds(state));
	}

	@Override
	public int damageDropped(IBlockState state) {
		return ((Integer)state.getValue(VARIANT)).intValue();
	}

	@Override
	public void getSubBlocks(Item itemIn, CreativeTabs tab, List<ItemStack> list) {
		for (int meta = 0; meta < this.variantNames.length; meta++) {
			list.add(new ItemStack(itemIn, 1, meta));
		}
	}

	public int getDamageValue(World worldIn, BlockPos pos) {
		return ((Integer)worldIn.getBlockState(pos).getValue(VARIANT)).intValue();
	}

	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}

	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	private boolean hasTopSupport(World worldIn, BlockPos pos) {
		BlockPos supportPos = pos.down();
		return worldIn.getBlockState(supportPos).isSideSolid(worldIn, supportPos, EnumFacing.UP);
	}

	private AxisAlignedBB getRotatedBounds(IBlockState state) {
		AxisAlignedBB box = this.bounds[((Integer)state.getValue(VARIANT)).intValue()];
		EnumFacing facing = (EnumFacing)state.getValue(FACING);

		switch (facing) {
			case EAST:
				return rotateY90(box);
			case SOUTH:
				return rotateY180(box);
			case WEST:
				return rotateY270(box);
			case NORTH:
			default:
				return box;
		}
	}

	private int clampVariant(int meta) {
		if (meta < 0 || meta >= this.variantNames.length) {
			return 0;
		}

		return meta;
	}

	private static AxisAlignedBB rotateY90(AxisAlignedBB box) {
		return new AxisAlignedBB(1.0D - box.maxZ, box.minY, box.minX,
			1.0D - box.minZ, box.maxY, box.maxX);
	}

	private static AxisAlignedBB rotateY180(AxisAlignedBB box) {
		return new AxisAlignedBB(1.0D - box.maxX, box.minY, 1.0D - box.maxZ,
			1.0D - box.minX, box.maxY, 1.0D - box.minZ);
	}

	private static AxisAlignedBB rotateY270(AxisAlignedBB box) {
		return new AxisAlignedBB(box.minZ, box.minY, 1.0D - box.maxX,
			box.maxZ, box.maxY, 1.0D - box.minX);
	}
}
