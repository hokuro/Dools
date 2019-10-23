package basashi.dools.network;

import java.util.function.Supplier;

import basashi.dools.container.ContainerItemSelect;
import basashi.dools.core.Dools;
import basashi.dools.entity.EntityDool;
import basashi.dools.server.ServerDool;
import net.minecraft.entity.Entity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkEvent;

public class MessageItem {

	private EquipmentSlotType slot;
	private int slotIdx;
	private int entityId;
	private ItemStack item;

	public MessageItem(int slotId, int entId, ItemStack stack) {
		slotIdx = slotId;
		entityId = entId;
		item = stack;
		this.slot = ContainerItemSelect.slotFromIndex.get(slotIdx);
	}

	public static void encode(MessageItem pkt, PacketBuffer buf)
	{
		buf.writeInt(pkt.slotIdx);
		buf.writeInt(pkt.entityId);
		buf.writeItemStack(pkt.item);
	}

	public static MessageItem decode(PacketBuffer buf)
	{
		int slotId = buf.readInt();
		int entId = buf.readInt();
		ItemStack stack = buf.readItemStack();
		return new MessageItem(slotId, entId, stack);
	}

	public static class Handler {
		public static void handle(final MessageItem pkt, Supplier<NetworkEvent.Context> ctx) {
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
					EquipmentSlotType slotIn = ContainerItemSelect.slotFromIndex.get(pkt.slotIdx);
					ldool.renderEntity.setItemStackToSlot(slotIn, pkt.item);
					// iクライアントへItemStackを送信
					lserver.sendItem(pkt.slotIdx, ldool, false, ctx.get().getSender());
				}catch(Exception ex){
					ex.printStackTrace();
				}
			});
			ctx.get().setPacketHandled(true);
		}
	}

}
