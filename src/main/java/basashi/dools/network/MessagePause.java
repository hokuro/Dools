package basashi.dools.network;

import java.util.function.Supplier;

import basashi.dools.core.Dools;
import basashi.dools.core.log.ModLog;
import basashi.dools.entity.EntityDool;
import basashi.dools.server.ServerDool;
import io.netty.buffer.Unpooled;
import net.minecraft.entity.Entity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkEvent;

public class MessagePause {

	private byte[] data;

	public MessagePause(){}
	public MessagePause(byte[] dool){
		data = dool;
	}

	public static void encode(MessagePause pkt, PacketBuffer buf) {
		buf.writeInt(pkt.data.length);
		for (int i = 0; i < pkt.data.length; i++) {
			buf.writeByte(pkt.data[i]);
		}
	}

	public static MessagePause decode(PacketBuffer buf) {
		int size = buf.readInt();
		byte[] buffer = new byte[size];
		for (int i = 0; i < buffer.length; i++) {
			buffer[i] = buf.readByte();
 		}
		return new MessagePause(buffer);
	}

	public static class Handler {
		public static void handle(final MessagePause pkt, Supplier<NetworkEvent.Context> ctx) {
			ctx.get().enqueueWork(() -> {
				try{
					ServerWorld lworld = (ServerWorld) ctx.get().getSender().world;
					Entity lentity = null;
					EntityDool ldool = null;
					ServerDool lserver = null;
					int leid = 0;
					PacketBuffer buf = new PacketBuffer(Unpooled.wrappedBuffer(pkt.data));
					lentity = lworld.getEntityByID(buf.readInt());
					if (lentity instanceof EntityDool){
						ldool = (EntityDool)lentity;
						lserver = Dools.getServerFigure(ldool);
						// iクライアントから姿勢制御データ等を受信
						lserver.setData(ldool, pkt.data);
						ModLog.log().debug("DataSet ID:"+lentity.getEntityId()+" Server.");
						MessageHandler.Send_MessagePause_Client(ctx.get().getSender(), lserver.getData(ldool));
						ModLog.log().debug("DataSendToAllClient.");
					}
				}catch(Exception ex){
					ex.printStackTrace();
				}
			});
			ctx.get().setPacketHandled(true);
		}
	}

}
