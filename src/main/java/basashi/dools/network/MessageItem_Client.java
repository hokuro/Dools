package basashi.dools.network;

import java.util.function.Supplier;

import basashi.dools.container.ContainerItemSelect;
import basashi.dools.core.Dools;
import basashi.dools.entity.EntityDool;
import basashi.dools.server.ServerDool;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent;

public class MessageItem_Client {
	private int slotIdx;
	private int entityId;
	private ItemStack item;
	private EquipmentSlotType slot;

	public MessageItem_Client(int sltId, int entId, ItemStack stack) {
		slotIdx = sltId;
		entityId = entId;
		item = stack;
		this.slot = ContainerItemSelect.slotFromIndex.get(slotIdx);
	}

	public static void encode(MessageItem_Client pkt, PacketBuffer buf) {
		buf.writeInt(pkt.slotIdx);
		buf.writeInt(pkt.entityId);
		buf.writeItemStack(pkt.item);
	}

	public static MessageItem_Client decode(PacketBuffer buf) {
		int lslotid3 = 0;
		ItemStack lis3 = ItemStack.EMPTY;
		try {
			int slotIdx = buf.readInt();
			int entityId =buf.readInt();
			ItemStack item = buf.readItemStack();
			return new MessageItem_Client(slotIdx, entityId, item);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static class Handler {
		public static void handle(final MessageItem_Client pkt, Supplier<NetworkEvent.Context> ctx) {
			ctx.get().enqueueWork(() -> {
				try{
					World lworld = Minecraft.getInstance().world;
					Entity lentity = null;
					EntityDool ldool = null;
					ServerDool lserver = null;
					int leid = 0;
					lentity = lworld.getEntityByID(pkt.entityId);
					if ( lentity instanceof EntityDool){
						ldool = (EntityDool)lentity;
						lserver = Dools.getServerFigure(ldool);
					}
					EquipmentSlotType slotIn = ContainerItemSelect.slotFromIndex.get(pkt.slotIdx);
					lserver.reciveItem(ldool, slotIn, pkt.item);
				}catch(Exception ex){
					ex.printStackTrace();
				}
			});
			ctx.get().setPacketHandled(true);
		}
	}
}
