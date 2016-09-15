package basashi.dools.network;

import basashi.dools.core.Dools;
import basashi.dools.core.log.ModLog;
import basashi.dools.entity.EntityDool;
import basashi.dools.server.ServerDool;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageDoolUpdate implements IMessageHandler<MessageDoolUpdate, IMessage>, IMessage {
	private int entityId;

	public MessageDoolUpdate(){}

	public MessageDoolUpdate(int entityId){
		this.entityId = entityId;
	}


	@Override
	public void fromBytes(ByteBuf buf) {
		// TODO 自動生成されたメソッド・スタブ
		entityId = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		// TODO 自動生成されたメソッド・スタブ
		buf.writeInt(entityId);
	}

	@Override
	public IMessage onMessage(MessageDoolUpdate message, MessageContext ctx) {
		try{
			WorldServer lworld = (WorldServer) ctx.getServerHandler().field_147369_b.field_70170_p;
			Entity lentity = null;
			EntityDool ldool = null;
			ServerDool lserver = null;
			int leid = 0;

			lentity = lworld.func_73045_a(message.entityId);
			if ( lentity instanceof EntityDool){
				ldool = (EntityDool)lentity;
				lserver = Dools.getServerFigure(ldool);
			}

			// クライアントから姿勢制御データ要求を受信
			ModLog.log().debug("RequestFromClient("+ldool.func_145782_y()+":"+lserver.getClass().getSimpleName()+").");
			Dools.INSTANCE.sendToAll(new MessagePause_Client(lserver.getData(ldool)));
			lserver.sendItems(ldool, false);
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}

}
