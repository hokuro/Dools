/**
 *
 */
package basashi.dools.network;

import basashi.dools.container.ContainerItemSelect;
import basashi.dools.core.Dools;
import basashi.dools.entity.EntityDool;
import basashi.dools.server.ServerDool;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * @author as
 *
 */
public class MessageItem implements IMessage, IMessageHandler<MessageItem, IMessage> {

	private EntityEquipmentSlot slot;
	private int slotIdx;
	private EntityDool dool;

	private int entityId;
	private ItemStack item;

	public MessageItem(){}
	public MessageItem(int slot, EntityDool dool){
		slotIdx = slot;
		this.slot = ContainerItemSelect.slotFromIndex.get(slotIdx);
		this.dool = dool;
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
			slot = ContainerItemSelect.slotFromIndex.get(slotIdx);
			item = ByteBufUtils.readItemStack(buf);
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
		ContainerItemSelect.slotFromIndex.get(slotIdx);
		litemstack = dool.renderEntity.getItemStackFromSlot(this.slot);
		buf.writeInt(dool.getEntityId());
		buf.writeByte(slotIdx);
		ByteBufUtils.writeItemStack(buf,litemstack);
	}

	@Override
	public IMessage onMessage(MessageItem message, MessageContext ctx) {
		try{
			WorldServer lworld = (WorldServer) ctx.getServerHandler().playerEntity.worldObj;
			Entity lentity = null;
			EntityDool ldool = null;
			ServerDool lserver = null;
			int leid = 0;
			lentity = lworld.getEntityByID(message.entityId);
			if ( lentity instanceof EntityDool){
				ldool = (EntityDool)lentity;
				lserver = Dools.getServerFigure(ldool);
			}
			ldool.renderEntity.setItemStackToSlot(message.slot, message.item);
			// クライアントへItemStackを送信
			lserver.sendItem(message.slotIdx, ldool, false);
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return null;
	}

}
