package basashi.dools.network;

import basashi.dools.core.Dools;
import basashi.dools.core.log.ModLog;
import basashi.dools.entity.EntityDool;
import basashi.dools.server.ServerDool;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.entity.Entity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessagePause implements IMessage, IMessageHandler<MessagePause, IMessage> {

	private byte[] data;

	public MessagePause(){}
	public MessagePause(byte[] dool){
		data = dool;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		// TODO 自動生成されたメソッド・スタブ

		data = new byte[buf.readableBytes()];
		buf.readBytes(data);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		// TODO 自動生成されたメソッド・スタブ
		buf.writeBytes(data);
	}

	@Override
	public IMessage onMessage(MessagePause message, MessageContext ctx) {
		try{
			WorldServer lworld = (WorldServer) ctx.getServerHandler().player.world;
			Entity lentity = null;
			EntityDool ldool = null;
			ServerDool lserver = null;
			int leid = 0;
			PacketBuffer buf = new PacketBuffer(Unpooled.wrappedBuffer(message.data));
			lentity = lworld.getEntityByID(buf.readInt());
			if ( lentity instanceof EntityDool){
				ldool = (EntityDool)lentity;
				lserver = Dools.getServerFigure(ldool);
			}
			// クライアントから姿勢制御データ等を受信
			lserver.setData(ldool, message.data);
			ModLog.log().debug("DataSet ID:"+lentity.getEntityId()+" Server.");
			Dools.INSTANCE.sendToAll(new MessagePause_Client(lserver.getData(ldool)));
			ModLog.log().debug("DataSendToAllClient.");
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return null;
	}

}
