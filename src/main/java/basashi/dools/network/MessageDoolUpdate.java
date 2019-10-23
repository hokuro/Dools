package basashi.dools.network;

import java.util.function.Supplier;

import basashi.dools.core.Dools;
import basashi.dools.entity.EntityDool;
import basashi.dools.server.ServerDool;
import net.minecraft.entity.Entity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkEvent;

public class MessageDoolUpdate {
	private int entityId;

	public MessageDoolUpdate(int entityId){
		this.entityId = entityId;
	}

	public static void encode(MessageDoolUpdate pkt, PacketBuffer buf) {
		buf.writeInt(pkt.entityId);
	}

	public static MessageDoolUpdate decode(PacketBuffer buf) {
		return new MessageDoolUpdate(buf.readInt());
	}

	public static class Handler {
		public static void handle(final MessageDoolUpdate pkt, Supplier<NetworkEvent.Context> ctx) {
			ctx.get().enqueueWork(() -> {
				try{
					ServerWorld lworld = (ServerWorld) ctx.get().getSender().world;
					Entity lentity = null;
					EntityDool ldool = null;
					ServerDool lserver = null;
					int leid = 0;

					lentity = lworld.getEntityByID(pkt.entityId);
					if ( lentity instanceof EntityDool){
						ldool = (EntityDool)lentity;
						lserver = Dools.getServerFigure(ldool);
					}

					// iクライアントから姿勢制御データ要求を受信
					MessageHandler.Send_MessagePause_Client(ctx.get().getSender(), lserver.getData(ldool));
					lserver.sendItems(ldool, false, ctx.get().getSender());
				}catch(Exception e){
					e.printStackTrace();
				}
			});
			ctx.get().setPacketHandled(true);
		}
	}

}
