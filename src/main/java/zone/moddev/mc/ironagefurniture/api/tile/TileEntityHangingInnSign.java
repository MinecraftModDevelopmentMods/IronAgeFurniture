package zone.moddev.mc.ironagefurniture.api.tile;

import java.util.UUID;

import javax.annotation.Nullable;

import zone.moddev.mc.ironagefurniture.IronAgeFurnitureConfiguration;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;

public class TileEntityHangingInnSign extends TileEntitySign {
	private String ownerId = "";
	private String keeperId = "";
	private String keeperName = "";
	private int keeperPreviousProfession = -1;
	private int purse = IronAgeFurnitureConfiguration.INNKEEPER_DAILY_PURSE;
	private long purseDay = -1L;

	public String getInnName() {
		StringBuilder name = new StringBuilder();

		for (ITextComponent line : this.signText) {
			String text = line == null ? "" : line.getUnformattedText().trim();

			if (!text.isEmpty()) {
				if (name.length() > 0) {
					name.append(' ');
				}

				name.append(text);
			}
		}

		return name.length() == 0 ? "The Inn" : name.toString();
	}

	public String getOwnerId() { return this.ownerId; }
	public String getKeeperId() { return this.keeperId; }
	public String getKeeperName() { return this.keeperName; }
	public int getKeeperPreviousProfession() { return this.keeperPreviousProfession; }
	public boolean hasKeeper() { return !this.keeperId.isEmpty(); }
	public boolean isOwner(UUID id) { return id != null && id.toString().equals(this.ownerId); }

	public void setInnName(String value) {
		String clean = value == null ? "" : value.trim();
		clean = clean.isEmpty() ? "The Inn" : clean.substring(0, Math.min(32, clean.length()));

		for (int line = 0; line < this.signText.length; line++) {
			this.signText[line] = new TextComponentString(line == 0 ? clean : "");
		}

		this.markForUpdate();
	}

	public void setOwner(UUID id) {
		this.ownerId = id == null ? "" : id.toString();
		this.markForUpdate();
	}

	public void setKeeper(@Nullable UUID id, int previousProfession, @Nullable String name) {
		this.keeperId = id == null ? "" : id.toString();
		this.keeperName = id == null || name == null ? "" : name;
		this.keeperPreviousProfession = id == null ? -1 : previousProfession;
		this.markForUpdate();
	}

	@Nullable
	public UUID getKeeperUuid() {
		try { return this.keeperId.isEmpty() ? null : UUID.fromString(this.keeperId); }
		catch (IllegalArgumentException ignored) { return null; }
	}

	public int getPurse() {
		this.refreshPurse();
		return this.purse;
	}

	public boolean isUsableByPlayer(EntityPlayer player) {
		return this.world != null && this.world.getTileEntity(this.pos) == this
			&& player.getDistanceSq(this.pos.getX() + 0.5D, this.pos.getY() + 0.5D,
				this.pos.getZ() + 0.5D) <= 1024.0D;
	}

	public boolean spendPurse(int emeralds) {
		this.refreshPurse();
		if (emeralds <= 0 || emeralds > this.purse) return false;
		this.purse -= emeralds;
		this.markForUpdate();
		return true;
	}

	private void refreshPurse() {
		if (this.world == null) return;
		long day = this.world.getTotalWorldTime() / 24000L;
		if (day != this.purseDay) {
			this.purseDay = day;
			this.purse = IronAgeFurnitureConfiguration.INNKEEPER_DAILY_PURSE;
			this.markForUpdate();
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound tag) {
		String legacyName = tag.hasKey("InnName", 8) ? tag.getString("InnName") : "";
		boolean hasSignText = false;

		for (int line = 1; line <= this.signText.length; line++) {
			String key = "Text" + line;

			if (tag.hasKey(key, 8)) {
				hasSignText = true;
			} else {
				String text = line == 1 && !legacyName.trim().isEmpty() ? legacyName : "";
				tag.setString(key, ITextComponent.Serializer.componentToJson(new TextComponentString(text)));
			}
		}

		super.readFromNBT(tag);

		if (!hasSignText && legacyName.trim().isEmpty()) {
			this.signText[0] = new TextComponentString("The Inn");
		}

		this.ownerId = tag.getString("OwnerId");
		this.keeperId = tag.getString("KeeperId");
		this.keeperName = tag.getString("KeeperName");
		this.keeperPreviousProfession = tag.getInteger("KeeperPreviousProfession");
		this.purse = tag.hasKey("Purse", 3) ? tag.getInteger("Purse") : IronAgeFurnitureConfiguration.INNKEEPER_DAILY_PURSE;
		this.purseDay = tag.getLong("PurseDay");
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag) {
		super.writeToNBT(tag);
		tag.setString("InnName", this.getInnName());
		tag.setString("OwnerId", this.ownerId);
		tag.setString("KeeperId", this.keeperId);
		tag.setString("KeeperName", this.keeperName);
		tag.setInteger("KeeperPreviousProfession", this.keeperPreviousProfession);
		tag.setInteger("Purse", this.purse);
		tag.setLong("PurseDay", this.purseDay);
		return tag;
	}

	@Override
	public SPacketUpdateTileEntity getUpdatePacket() {
		return super.getUpdatePacket();
	}

	@Override
	public NBTTagCompound getUpdateTag() { return this.writeToNBT(new NBTTagCompound()); }

	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity packet) {
		this.readFromNBT(packet.getNbtCompound());
	}

	private void markForUpdate() {
		this.markDirty();
		if (this.world != null) {
			this.world.notifyBlockUpdate(this.pos, this.world.getBlockState(this.pos),
				this.world.getBlockState(this.pos), 3);
		}
	}
}
