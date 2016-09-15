package basashi.dools.network;

import basashi.dools.core.Dools;
import net.minecraftforge.fml.relauncher.Side;

public class MessageHandler {
	public MessageHandler(){

	}

	public static void init(){
		Dools.INSTANCE.registerMessage(MessageDoolUpdate.class, MessageDoolUpdate.class, 0, Side.SERVER);
		Dools.INSTANCE.registerMessage(MessageItem.class, MessageItem.class, 1, Side.SERVER);
		Dools.INSTANCE.registerMessage(MessagePause.class, MessagePause.class, 2, Side.SERVER);
		Dools.INSTANCE.registerMessage(MessageServer_SpawnDool.class, MessageServer_SpawnDool.class, 3, Side.SERVER);
		Dools.INSTANCE.registerMessage(MessageItem_Client.class, MessageItem_Client.class, 4, Side.CLIENT);
		Dools.INSTANCE.registerMessage(MessagePause_Client.class, MessagePause_Client.class, 5, Side.CLIENT);
	}


}