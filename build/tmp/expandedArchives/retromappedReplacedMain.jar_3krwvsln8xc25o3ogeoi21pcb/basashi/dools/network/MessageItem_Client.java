package basashi.dools.network;

import basashi.dools.container.ContainerItemSelect;
import basashi.dools.core.Dools;
import basashi.dools.entity.EntityDool;
import basashi.dools.server.ServerDool;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageItem_Client implements IMessage, IMessageHandler<MessageItem_Client, IMessage>  {
	private int slotIdx;
	private EntityDool dool;
	private EntityEquipmentSlot slot;

	private int entityId;
	private ItemStack item;

	public MessageItem_Client(){}

	public MessageItem_Client(int slot, EntityDool dool){
		slotIdx = slot;
		this.dool = dool;
		this.slot = ContainerItemSelect.slotFromIndex.get(slotIdx);
	}

	/* (非 Javadoc)
	 * @see net.minecraftforge.fml.common.network.simpleimpl.IMessage#fromBytes(io.netty.buffer.ByteBuf)
	 */
	@Override
	public void fromBytes(ByteBuf buf) {
		int lslotid3 = 0;
		ItemStack lis3 = null;
		try {
			entityId =buf.readInt();
			slotIdx = buf.readByte();
			item = ByteBufUtils.readItemStack(buf);
			slot = ContainerItemSelect.slotFromIndex.get(slotIdx);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/* (非 Javadoc)
	 * @see net.minecraftforge.fml.common.network.simpleimpl.IMessage#toBytes(io.netty.buffer.ByteBuf)
	 */
	@Override
	public void toBytes(ByteBuf buf) {
		// TODO 自動生成されたメソッド・スタブ
		ItemStack litemstack;
		litemstack = dool.renderEntity.func_184582_a(slot);
		buf.writeInt(dool.func_145782_y());
		buf.writeByte(slotIdx);
		ByteBufUtils.writeItemStack(buf,litemstack);
	}

	@Override
	public IMessage onMessage(MessageItem_Client message, MessageContext ctx) {
		try{
			World lworld = Minecraft.func_71410_x().field_71441_e;
			Entity lentity = null;
			EntityDool ldool = null;
			ServerDool lserver = null;
			int leid = 0;
			lentity = lworld.func_73045_a(message.entityId);
			if ( lentity instanceof EntityDool){
				ldool = (EntityDool)lentity;
				lserver = Dools.getServerFigure(ldool);
			}
			lserver.reciveItem(ldool, message.slot,message.item);
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return null;
	}
}
