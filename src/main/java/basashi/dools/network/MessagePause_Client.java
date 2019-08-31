package basashi.dools.network;

import java.util.function.Supplier;

import basashi.dools.core.Dools;
import basashi.dools.core.log.ModLog;
import basashi.dools.entity.EntityDool;
import basashi.dools.server.ServerDool;
import io.netty.buffer.Unpooled;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent;

public class MessagePause_Client {

	private byte data[];

	public MessagePause_Client(){}
	public MessagePause_Client(byte[] pause){
		data = pause;
	}

	public static void encode(MessagePause_Client pkt, PacketBuffer buf)
	{
		buf.writeInt(pkt.data.length);
		for (int i = 0; i < pkt.data.length; i++) {
			buf.writeByte(pkt.data[i]);
		}
	}

	public static MessagePause_Client decode(PacketBuffer buf)
	{
		int size = buf.readInt();
		byte[] buffer = new byte[size];
		for (int i = 0; i < buffer.length; i++) {
			buffer[i] = buf.readByte();
 		}
		return new MessagePause_Client(buffer);
	}

	public static class Handler
	{
		public static void handle(final MessagePause_Client pkt, Supplier<NetworkEvent.Context> ctx)
		{
			ctx.get().enqueueWork(() -> {
				try{
					// 独自パケットチャンネルの受信
					World lworld = Minecraft.getInstance().world;
					Entity lentity = null;
					EntityDool lfigure = null;
					ServerDool lserver = null;
					PacketBuffer buf = new PacketBuffer(Unpooled.wrappedBuffer(pkt.data));

					int leid = buf.readInt();
					lentity = lworld.getEntityByID(leid);
					if (lentity instanceof EntityDool) {
						lfigure = (EntityDool)lentity;
						lserver = Dools.getServerFigure(lfigure);
					}
					// サーバーから姿勢制御データ等を受信
					ModLog.log().debug("DataSet ID:"+lentity.getEntityId()+"(size:"+pkt.data.length+") Client.");
					lserver.setData((EntityDool)lentity,pkt.data);
				}catch(Exception ex){
					ex.printStackTrace();
				}
			});
			ctx.get().setPacketHandled(true);
		}
	}

}
