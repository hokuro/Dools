package basashi.dools.network;

import basashi.dools.core.ModCommon;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class MessageHandler {
	private static final String PROTOCOL_VERSION = Integer.toString(1);
	private static final SimpleChannel Handler = NetworkRegistry.ChannelBuilder
			.named(new ResourceLocation(ModCommon.MOD_ID,ModCommon.MOD_CHANEL))
			.clientAcceptedVersions(PROTOCOL_VERSION::equals)
			.serverAcceptedVersions(PROTOCOL_VERSION::equals)
			.networkProtocolVersion(() -> PROTOCOL_VERSION)
			.simpleChannel();

	public static void register()
	{
		int disc = 0;

		Handler.registerMessage(disc++, MessageServer_SpawnDool.class, MessageServer_SpawnDool::encode, MessageServer_SpawnDool::decode, MessageServer_SpawnDool.Handler::handle);
		Handler.registerMessage(disc++, MessageDoolUpdate.class, MessageDoolUpdate::encode, MessageDoolUpdate::decode, MessageDoolUpdate.Handler::handle);
		Handler.registerMessage(disc++, MessageItem.class, MessageItem::encode, MessageItem::decode, MessageItem.Handler::handle);
		Handler.registerMessage(disc++, MessagePause.class, MessagePause::encode, MessagePause::decode, MessagePause.Handler::handle);
		Handler.registerMessage(disc++, MessageItem_Client.class, MessageItem_Client::encode, MessageItem_Client::decode, MessageItem_Client.Handler::handle);
		Handler.registerMessage(disc++, MessagePause_Client.class, MessagePause_Client::encode, MessagePause_Client::decode, MessagePause_Client.Handler::handle);

	}

	public static void Send_MessageDoolUpdte(int entityId) {
		Handler.sendToServer(new MessageDoolUpdate(entityId));
	}

	public static void Send_MessagePause_Client(ServerPlayerEntity sender, byte[] data) {
		Handler.sendTo(new MessagePause_Client(data), sender.connection.netManager, NetworkDirection.PLAY_TO_CLIENT);
	}

	public static void Send_MessageServer_SpawnDool(double posX, double posY, double posZ, float rotationYaw, String name) {
		Handler.sendToServer(new MessageServer_SpawnDool(posX, posY, posZ, rotationYaw, name));
	}

	public static void Send_MessageItem(int slotIdx, int entityId, ItemStack item) {
		Handler.sendToServer(new MessageItem(slotIdx, entityId, item));
	}

	public static void Send_MessagePause(byte[] data) {
		Handler.sendToServer(new MessagePause(data));
	}

	public static void Send_MessageItem_Client(int pIndex, int entityId, ItemStack item, ServerPlayerEntity player) {
		Handler.sendTo(new MessageItem_Client(pIndex, entityId, item), player.connection.netManager, NetworkDirection.PLAY_TO_CLIENT);
	}
}
