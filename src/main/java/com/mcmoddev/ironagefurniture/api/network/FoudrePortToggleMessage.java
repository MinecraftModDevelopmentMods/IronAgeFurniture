package com.mcmoddev.ironagefurniture.api.network;

import com.mcmoddev.ironagefurniture.api.tile.TileEntityFoudre;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class FoudrePortToggleMessage implements IMessage {
	private BlockPos pos;
	private boolean inlet;

	public FoudrePortToggleMessage() {
	}

	public FoudrePortToggleMessage(BlockPos pos, boolean inlet) {
		this.pos = pos;
		this.inlet = inlet;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
		this.inlet = buf.readBoolean();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(this.pos.getX());
		buf.writeInt(this.pos.getY());
		buf.writeInt(this.pos.getZ());
		buf.writeBoolean(this.inlet);
	}

	public static class Handler implements IMessageHandler<FoudrePortToggleMessage, IMessage> {
		@Override
		public IMessage onMessage(final FoudrePortToggleMessage message, final MessageContext ctx) {
			final EntityPlayerMP player = ctx.getServerHandler().playerEntity;
			player.getServerWorld().addScheduledTask(new Runnable() {
				@Override
				public void run() {
					TileEntity tileEntity = player.getServerWorld().getTileEntity(message.pos);

					if (!(tileEntity instanceof TileEntityFoudre)
							|| !((TileEntityFoudre)tileEntity).isUsableByPlayer(player)) {
						return;
					}

					if (message.inlet) {
						((TileEntityFoudre)tileEntity).toggleInletOpen();
					} else {
						((TileEntityFoudre)tileEntity).toggleOutletOpen();
					}
				}
			});
			return null;
		}
	}
}
