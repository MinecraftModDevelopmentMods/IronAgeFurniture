package com.mcmoddev.ironagefurniture.api.network;

import com.mcmoddev.ironagefurniture.api.Enumerations.FluidPortMode;
import com.mcmoddev.ironagefurniture.api.tile.TileEntityPotStill;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public final class PotStillPortModeMessage implements IMessage {
	private BlockPos pos;
	private FluidPortMode mode = FluidPortMode.LOCKED;

	public PotStillPortModeMessage() {
	}

	public PotStillPortModeMessage(BlockPos pos, FluidPortMode mode) {
		this.pos = pos;
		this.mode = mode == null ? FluidPortMode.LOCKED : mode;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
		this.mode = FluidPortMode.fromId(buf.readByte());
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(this.pos.getX());
		buf.writeInt(this.pos.getY());
		buf.writeInt(this.pos.getZ());
		buf.writeByte(this.mode.getId());
	}

	public static class Handler implements IMessageHandler<PotStillPortModeMessage, IMessage> {
		@Override
		public IMessage onMessage(final PotStillPortModeMessage message, final MessageContext ctx) {
			final EntityPlayerMP player = ctx.getServerHandler().playerEntity;
			player.getServerWorld().addScheduledTask(new Runnable() {
				@Override
				public void run() {
					TileEntity tileEntity = player.getServerWorld().getTileEntity(message.pos);

					if (tileEntity instanceof TileEntityPotStill
							&& ((TileEntityPotStill)tileEntity).isUsableByPlayer(player)) {
						((TileEntityPotStill)tileEntity).setPortMode(message.mode);
					}
				}
			});
			return null;
		}
	}
}
