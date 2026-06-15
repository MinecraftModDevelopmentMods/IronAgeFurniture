package com.mcmoddev.ironagefurniture.api.network;

import com.mcmoddev.ironagefurniture.api.tile.TileEntityFoudre;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class FoudreFlushMessage implements IMessage {
	private BlockPos pos;

	public FoudreFlushMessage() {
	}

	public FoudreFlushMessage(BlockPos pos) {
		this.pos = pos;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(this.pos.getX());
		buf.writeInt(this.pos.getY());
		buf.writeInt(this.pos.getZ());
	}

	public static class Handler implements IMessageHandler<FoudreFlushMessage, IMessage> {
		@Override
		public IMessage onMessage(final FoudreFlushMessage message, final MessageContext ctx) {
			final EntityPlayerMP player = ctx.getServerHandler().playerEntity;
			player.getServerWorld().addScheduledTask(new Runnable() {
				@Override
				public void run() {
					TileEntity tileEntity = player.getServerWorld().getTileEntity(message.pos);

					if (tileEntity instanceof TileEntityFoudre
							&& ((TileEntityFoudre)tileEntity).isUsableByPlayer(player)) {
						((TileEntityFoudre)tileEntity).flush();
					}
				}
			});
			return null;
		}
	}
}
