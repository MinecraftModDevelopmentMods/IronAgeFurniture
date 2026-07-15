package com.mcmoddev.ironagefurniture.api.network;

import com.mcmoddev.ironagefurniture.api.container.ContainerFoudre;
import com.mcmoddev.ironagefurniture.api.tile.TileEntityFoudre;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class FoudreBarrelTransferMessage implements IMessage {
	private BlockPos pos;
	private boolean fillBarrel;

	public FoudreBarrelTransferMessage() {
	}

	public FoudreBarrelTransferMessage(BlockPos pos, boolean fillBarrel) {
		this.pos = pos;
		this.fillBarrel = fillBarrel;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
		this.fillBarrel = buf.readBoolean();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(this.pos.getX());
		buf.writeInt(this.pos.getY());
		buf.writeInt(this.pos.getZ());
		buf.writeBoolean(this.fillBarrel);
	}

	public static class Handler implements IMessageHandler<FoudreBarrelTransferMessage, IMessage> {
		@Override
		public IMessage onMessage(final FoudreBarrelTransferMessage message, final MessageContext ctx) {
			final EntityPlayerMP player = ctx.getServerHandler().playerEntity;
			player.getServerWorld().addScheduledTask(new Runnable() {
				@Override
				public void run() {
					TileEntityFoudre foudre = getOpenFoudre(player);

					if (foudre == null || !foudre.isUsableByPlayer(player)) {
						return;
					}

					if (message.fillBarrel) {
						foudre.fillAdjacentBarrel();
					} else {
						foudre.drainAdjacentBarrel();
					}
				}
			});
			return null;
		}

		private static TileEntityFoudre getOpenFoudre(EntityPlayerMP player) {
			return player.openContainer instanceof ContainerFoudre
				? ((ContainerFoudre)player.openContainer).getFoudre() : null;
		}
	}
}
