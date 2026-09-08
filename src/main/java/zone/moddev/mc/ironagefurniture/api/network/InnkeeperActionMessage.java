package zone.moddev.mc.ironagefurniture.api.network;

import zone.moddev.mc.ironagefurniture.api.container.ContainerInnkeeper;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class InnkeeperActionMessage implements IMessage {
	private int action;

	public InnkeeperActionMessage() { }
	public InnkeeperActionMessage(int action) { this.action = action; }
	@Override public void fromBytes(ByteBuf buf) { this.action = buf.readInt(); }
	@Override public void toBytes(ByteBuf buf) { buf.writeInt(this.action); }

	public static class Handler implements IMessageHandler<InnkeeperActionMessage, IMessage> {
		@Override
		public IMessage onMessage(final InnkeeperActionMessage message, final MessageContext context) {
			final EntityPlayerMP player = context.getServerHandler().playerEntity;
			player.getServerWorld().addScheduledTask(new Runnable() {
				@Override public void run() {
					if (player.openContainer instanceof ContainerInnkeeper) {
						((ContainerInnkeeper)player.openContainer).performAction(player, message.action);
					}
				}
			});
			return null;
		}
	}
}
