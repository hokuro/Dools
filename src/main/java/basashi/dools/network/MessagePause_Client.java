package basashi.dools.network;

import basashi.dools.core.Dools;
import basashi.dools.core.log.ModLog;
import basashi.dools.entity.EntityDool;
import basashi.dools.server.ServerDool;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessagePause_Client implements IMessage, IMessageHandler<MessagePause_Client, IMessage> {

	private byte data[];

	public MessagePause_Client(){}
	public MessagePause_Client(byte[] pause){
		data = pause;
	}

	@Override
	public IMessage onMessage(MessagePause_Client message, MessageContext ctx) {
		try{
			// 独自パケットチャンネルの受信
			World lworld = Minecraft.getMinecraft().world;
			Entity lentity = null;
			EntityDool lfigure = null;
			ServerDool lserver = null;
			PacketBuffer buf = new PacketBuffer(Unpooled.wrappedBuffer(message.data));

			int leid = buf.readInt();
			lentity = lworld.getEntityByID(leid);
			if (lentity instanceof EntityDool) {
				lfigure = (EntityDool)lentity;
				lserver = Dools.getServerFigure(lfigure);
			}
			// サーバーから姿勢制御データ等を受信
			ModLog.log().debug("DataSet ID:"+lentity.getEntityId()+"(size:"+message.data.length+") Client.");
			lserver.setData((EntityDool)lentity,message.data);
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return null;
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

}
