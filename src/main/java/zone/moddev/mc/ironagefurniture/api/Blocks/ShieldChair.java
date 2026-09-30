package zone.moddev.mc.ironagefurniture.api.Blocks;

import java.util.ArrayList;
import java.util.List;

import zone.moddev.mc.ironagefurniture.api.ShieldChairItemData;
import zone.moddev.mc.ironagefurniture.api.tile.TileEntityShieldChair;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

/** A chair whose shield is an actual, individually preserved vanilla shield. */
public class ShieldChair extends Chair {
	private static final AxisAlignedBB LEFT_POST = new AxisAlignedBB(0.0625D, 0.5D, 0.8125D, 0.1875D, 1.5D, 1.0D);
	private static final AxisAlignedBB RIGHT_POST = new AxisAlignedBB(0.8125D, 0.5D, 0.8125D, 0.9375D, 1.5D, 1.0D);

	public ShieldChair(Material material, String name, float resistance, float hardness) {
		super(material, name, resistance, hardness);
	}

	@Override public boolean hasTileEntity(IBlockState state) { return true; }
	@Override public TileEntity createTileEntity(World world, IBlockState state) { return new TileEntityShieldChair(); }

	@Override public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state,
			EntityLivingBase placer, ItemStack chairItem) {
		super.onBlockPlacedBy(world, pos, state, placer, chairItem);
		if (!world.isRemote) {
			TileEntityShieldChair tile = tile(world, pos);
			if (tile != null) tile.setShield(ShieldChairItemData.getShield(chairItem));
		}
	}

	@Override public boolean onBlockActivated(World world, BlockPos pos, IBlockState state,
			EntityPlayer player, EnumHand hand, EnumFacing side,
			float hitX, float hitY, float hitZ) {
		ItemStack held = player.getHeldItem(hand);
		TileEntityShieldChair tile = tile(world, pos);
		if (tile == null) return false;

		if (player.isSneaking() && hand == EnumHand.MAIN_HAND && held.isEmpty() && tile.hasShield()) {
			if (!world.isRemote) {
				ItemStack returned = tile.getShield();
				tile.setShield(null);
				if (!player.inventory.addItemStackToInventory(returned)) spawnAsEntity(world, pos, returned);
			}
			return true;
		}

		if (!held.isEmpty() && held.getItem() == Items.SHIELD && !tile.hasShield()) {
			if (!world.isRemote) {
				ItemStack attached = held.copy();
				attached.setCount(1);
				tile.setShield(attached);
				if (!player.capabilities.isCreativeMode) {
					held.shrink(1);
					if (held.isEmpty()) player.setHeldItem(hand, ItemStack.EMPTY);
				}
			}
			return true;
		}

		if (player.isSneaking()) return false;
		return super.onBlockActivated(world, pos, state, player, hand, side, hitX, hitY, hitZ);
	}

	@Override public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
		List<ItemStack> drops = new ArrayList<ItemStack>();
		drops.add(ShieldChairItemData.createChair(this, ItemStack.EMPTY));
		TileEntityShieldChair tile = tile(world, pos);
		// A pre-upgrade block has no saved tile entity and still carries its plain shield.
		ItemStack shield = tile == null ? new ItemStack(Items.SHIELD) : tile.getShield();
		if (!shield.isEmpty()) drops.add(shield);
		return drops;
	}

	@Override public boolean removedByPlayer(IBlockState state, World world, BlockPos pos,
			EntityPlayer player, boolean willHarvest) {
		if (!willHarvest || world.isRemote || player.capabilities.isCreativeMode)
			return super.removedByPlayer(state, world, pos, player, willHarvest);
		// Forge harvests after this call. Retain the tile until getDrops has copied the shield.
		onBlockHarvested(world, pos, state, player);
		return true;
	}

	@Override public void harvestBlock(World world, EntityPlayer player, BlockPos pos,
			IBlockState state, TileEntity tile, ItemStack tool) {
		try {
			super.harvestBlock(world, player, pos, state, tile, tool);
		} finally {
			if (world.getBlockState(pos).getBlock() == this) world.setBlockToAir(pos);
		}
	}

	@Override public ItemStack getItem(World world, BlockPos pos, IBlockState state) {
		TileEntityShieldChair tile = tile(world, pos);
		return ShieldChairItemData.createChair(this,
				tile == null ? new ItemStack(Items.SHIELD) : tile.getShield());
	}

	@Override public ItemStack getPickBlock(IBlockState state, RayTraceResult target,
			World world, BlockPos pos, EntityPlayer player) {
		return getItem(world, pos, state);
	}

	@Override public void addCollisionBoxToList(IBlockState state, World world, BlockPos pos,
			AxisAlignedBB entityBox, List<AxisAlignedBB> boxes, Entity entity, boolean isActualState) {
		TileEntityShieldChair tile = tile(world, pos);
		if (tile == null || tile.hasShield()) {
			super.addCollisionBoxToList(state, world, pos, entityBox, boxes, entity, isActualState);
			return;
		}
		if (!(entity instanceof zone.moddev.mc.ironagefurniture.api.entity.Seat)) {
			addCollisionBoxToList(pos, entityBox, boxes, BASEBB);
			EnumFacing facing = state.getValue(FACING);
			addCollisionBoxToList(pos, entityBox, boxes, rotatePost(LEFT_POST, facing));
			addCollisionBoxToList(pos, entityBox, boxes, rotatePost(RIGHT_POST, facing));
		}
	}

	private static AxisAlignedBB rotatePost(AxisAlignedBB box, EnumFacing facing) {
		switch (facing) {
		case NORTH: return new AxisAlignedBB(1 - box.maxX, box.minY, 1 - box.maxZ,
				1 - box.minX, box.maxY, 1 - box.minZ);
		case EAST: return new AxisAlignedBB(1 - box.maxZ, box.minY, box.minX,
				1 - box.minZ, box.maxY, box.maxX);
		case WEST: return new AxisAlignedBB(box.minZ, box.minY, 1 - box.maxX,
				box.maxZ, box.maxY, 1 - box.minX);
		default: return box;
		}
	}

	private static TileEntityShieldChair tile(IBlockAccess world, BlockPos pos) {
		TileEntity tile = world.getTileEntity(pos);
		return tile instanceof TileEntityShieldChair ? (TileEntityShieldChair)tile : null;
	}
}
