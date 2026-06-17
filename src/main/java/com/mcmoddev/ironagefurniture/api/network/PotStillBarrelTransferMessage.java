package com.mcmoddev.ironagefurniture.api.network;

import com.mcmoddev.ironagefurniture.api.tile.TileEntityPotStill;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PotStillBarrelTransferMessage implements IMessage {
	private BlockPos pos;
	private boolean fillBarrel;

	public PotStillBarrelTransferMessage() {
	}

	public PotStillBarrelTransferMessage(BlockPos pos, boolean fillBarrel) {
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

	public static class Handler implements IMessageHandler<PotStillBarrelTransferMessage, IMessage> {
		@Override
		public IMessage onMessage(final PotStillBarrelTransferMessage message, final MessageContext ctx) {
			final EntityPlayerMP player = ctx.getServerHandler().playerEntity;
			player.getServerWorld().addScheduledTask(new Runnable() {
				@Override
				public void run() {
					TileEntity tileEntity = player.getServerWorld().getTileEntity(message.pos);

					if (!(tileEntity instanceof TileEntityPotStill)
							|| !((TileEntityPotStill)tileEntity).isUsableByPlayer(player)) {
						return;
					}

					if (message.fillBarrel) {
						((TileEntityPotStill)tileEntity).fillAdjacentBarrel();
					} else {
						((TileEntityPotStill)tileEntity).drainAdjacentBarrel();
					}
				}
			});
			return null;
		}
	}
}
