package basashi.dools.server;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;

import basashi.dools.entity.EntityDool;
import basashi.dools.network.MessageHandler;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

/**
 * �T�[�o�[���̎�M�����B GUI���쐬�����ۂ͂��������邱�ƁB
 */
public class ServerDool {

	public byte[] getData(EntityDool pFigure) {
		try {
			ByteArrayOutputStream lba = new ByteArrayOutputStream();
			DataOutputStream lds = new DataOutputStream(lba);
			EntityLivingBase lentity = pFigure.renderEntity;
			lds.writeInt(pFigure.getEntityId());		// ID
			lds.writeByte(0); // UpdateCount			// UpdateCount
			lds.writeFloat(pFigure.additionalYaw);		// Yaw
			lds.writeFloat(pFigure.zoom);				// zoom
			int lf = (pFigure.isFigureRide ? 1 : 0) |
					(lentity.isSneaking() ? 2 : 0) |
					(lentity.isChild() ? 4 : 0);
			lds.writeByte(lf); 							// Flags
			lds.writeFloat(lentity.rotationYaw);		// Yaw
			lds.writeFloat(lentity.rotationPitch);		// Piti
			lds.writeFloat(pFigure.fyOffset);			// yoffset
			sendData(pFigure, lds);
			return lba.toByteArray();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public void setData(EntityDool pFigure, byte[] pData) {
		if (pFigure.hasRenderEntity()) {
			try {
				ByteArrayInputStream lba = new ByteArrayInputStream(pData);
				DataInputStream lds = new DataInputStream(lba);
				EntityLivingBase lel = pFigure.renderEntity;
				lds.readInt();									// EntityID
				lds.readByte();									// UpdateCount
				pFigure.additionalYaw = lds.readFloat();		// Yaw
				pFigure.zoom = lds.readFloat();					// zoom
				pFigure.setZoom(pFigure.zoom);
				int lf = lds.readByte(); 						// Flags
				pFigure.isFigureRide = (lf & 1) != 0;
				if (pFigure.world.isRemote) {
					// Client
					if (pFigure.isFigureRide){
						lel.startRiding(pFigure);

					}else{
						lel.stopRiding();
					}
					//lel.ridingEntity = pFigure.isFigureRide ? pFigure : null;
				} else {
					// Seerver
				}
				lel.setSneaking((lf & 2) != 0);
				if (lel instanceof EntityAgeable) {
					((EntityAgeable)lel).setGrowingAge(-(lf & 4));
				}
				lel.rotationYaw = lel.prevRotationYaw =
						lel.rotationYawHead = lel.prevRotationYawHead = lds.readFloat();	// RotationYaw
				lel.rotationPitch = lel.prevRotationPitch = lds.readFloat();				// Pitch
				pFigure.fyOffset = lds.readFloat();											// Yoffset
				reciveData(pFigure, lds);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void sendItem(int pIndex, EntityDool pdool, boolean pClient) {
		this.sendItem(pIndex, pdool, pClient, null);
	}

	public void sendItem(int pIndex, EntityDool pdool, boolean pClient, EntityPlayerMP player) {
		try {
			//MessageItem mitem = new MessageItem(pIndex, pdool);
			if (pClient){
				MessageHandler.Send_MessageItem(pIndex, pdool);
				//Dools.INSTANCE.sendToServer(mitem);
			}else{
				if (player != null) {
					MessageHandler.Send_MessageItem_Client(pIndex,pdool,player);
					//Dools.INSTANCE.sendToAll(new MessageItem_Client(pIndex,pdool));
				}else {
					MessageHandler.Send_MessageItem_Client(pIndex,pdool);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void reciveItem(EntityDool pFigure, EntityEquipmentSlot slotidx, ItemStack item) {
		pFigure.renderEntity.setItemStackToSlot(slotidx, item);
		//pFigure.renderEntity.setCurrentItemOrArmor(lslotid3, lis3);
	}

	/**
	 * 固有データを相手へ送る。
	 * 継承はこちら。
	 */
	public void sendData(EntityDool pFigure, DataOutput pData) throws IOException {}

	/**
	 * 固有データを受け取ってEntityへ設定する。
	 * 継承はこちら。
	 */
	public void reciveData(EntityDool pFigure, DataInput pData) throws IOException {}

	/**
	 * 特殊なデータ読み込みを実行
	 */
	public void readEntityFromNBT(EntityDool pFigure, NBTTagCompound nbttagcompound) {}

	/**
	 * 特殊なデータ書き込みを実行
	 */
	public void writeEntityToNBT(EntityDool pFigure, NBTTagCompound nbttagcompound) {}

	/**
	 * 姿勢制御用
	 */
	public void setRotation(EntityDool pFigure) {}

	/**
	 * サーバーへ設定されたアイテムを送信。
	 */
	public void sendItems(EntityDool pdool, boolean pClient) {
		sendItem(5, pdool, pClient);
		sendItem(4, pdool, pClient);
		sendItem(3, pdool, pClient);
		sendItem(2, pdool, pClient);
		sendItem(1, pdool, pClient);
		sendItem(0, pdool, pClient);
	}

}
