package zone.moddev.mc.ironagefurniture.api.network;

import zone.moddev.mc.ironagefurniture.api.tile.TileEntityBarrel;
import zone.moddev.mc.ironagefurniture.api.tile.TileEntityFoudre;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class BarrelSealMessage implements IMessage {
	private BlockPos pos;
	private boolean sealed;

	public BarrelSealMessage() {
	}

	public BarrelSealMessage(BlockPos pos) {
		this(pos, false);
	}

	public BarrelSealMessage(BlockPos pos, boolean sealed) {
		this.pos = pos;
		this.sealed = sealed;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
		this.sealed = buf.readBoolean();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(this.pos.getX());
		buf.writeInt(this.pos.getY());
		buf.writeInt(this.pos.getZ());
		buf.writeBoolean(this.sealed);
	}

	public static class Handler implements IMessageHandler<BarrelSealMessage, IMessage> {
		@Override
		public IMessage onMessage(final BarrelSealMessage message, final MessageContext ctx) {
			final EntityPlayerMP player = ctx.getServerHandler().playerEntity;
			player.getServerWorld().addScheduledTask(new Runnable() {
				@Override
				public void run() {
					TileEntity tileEntity = player.getServerWorld().getTileEntity(message.pos);

					if (tileEntity instanceof TileEntityBarrel && !(tileEntity instanceof TileEntityFoudre)
							&& ((TileEntityBarrel)tileEntity).isUsableByPlayer(player)) {
						TileEntityBarrel barrel = (TileEntityBarrel)tileEntity;
						barrel.setSealed(message.sealed);
						barrel.markForFluidUpdate();
					}
				}
			});
			return null;
		}
	}
}
