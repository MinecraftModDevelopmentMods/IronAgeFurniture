package zone.moddev.mc.ironagefurniture.api.tile;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/** Keeps the complete vanilla shield stack; missing legacy tile data means a plain shield. */
public class TileEntityShieldChair extends TileEntity {
	private static final String EMPTY_TAG = "Empty";
	private static final String SHIELD_TAG = "Shield";
	private ItemStack shield = new ItemStack(Items.SHIELD);

	public boolean hasShield() { return shield != null; }
	public ItemStack getShield() { return shield == null ? null : shield.copy(); }
	/** Read-only rendering access; callers must never mutate the returned stack. */
	public ItemStack getShieldForRender() { return shield; }

	public void setShield(ItemStack next) {
		if (next != null && next.getItem() != Items.SHIELD) return;
		shield = next == null ? null : next.copy();
		if (shield != null) shield.stackSize = 1;
		markDirty();
		if (world != null && pos != null) {
			IBlockState state = world.getBlockState(pos);
			world.notifyBlockUpdate(pos, state, state, 3);
		}
	}

	@Override public void readFromNBT(NBTTagCompound tag) {
		super.readFromNBT(tag);
		if (tag.getBoolean(EMPTY_TAG)) shield = null;
		else if (tag.hasKey(SHIELD_TAG, 10)) {
			ItemStack loaded = ItemStack.loadItemStackFromNBT(tag.getCompoundTag(SHIELD_TAG));
			shield = loaded != null && loaded.getItem() == Items.SHIELD ? loaded : new ItemStack(Items.SHIELD);
			if (shield != null) shield.stackSize = 1;
		} else shield = new ItemStack(Items.SHIELD);
	}

	@Override public NBTTagCompound writeToNBT(NBTTagCompound tag) {
		super.writeToNBT(tag);
		if (shield == null) {
			tag.removeTag(SHIELD_TAG);
			tag.setBoolean(EMPTY_TAG, true);
		} else {
			tag.removeTag(EMPTY_TAG);
			tag.setTag(SHIELD_TAG, shield.writeToNBT(new NBTTagCompound()));
		}
		return tag;
	}

	@Override public NBTTagCompound getUpdateTag() { return writeToNBT(new NBTTagCompound()); }
	@Override public void handleUpdateTag(NBTTagCompound tag) { readFromNBT(tag); }
	@Override public SPacketUpdateTileEntity getUpdatePacket() {
		return new SPacketUpdateTileEntity(pos, 0, getUpdateTag());
	}
	@Override public void onDataPacket(NetworkManager manager, SPacketUpdateTileEntity packet) {
		readFromNBT(packet.getNbtCompound());
		if (world != null && pos != null) world.markBlockRangeForRenderUpdate(pos, pos);
	}
	@Override public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState) {
		return oldState.getBlock() != newState.getBlock();
	}
}
