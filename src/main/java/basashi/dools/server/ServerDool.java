package basashi.dools.server;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;

import basashi.dools.container.ContainerItemSelect;
import basashi.dools.entity.EntityDool;
import basashi.dools.network.MessageHandler;
import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;

public class ServerDool {

	public byte[] getData(EntityDool pFigure) {
		try {
			ByteArrayOutputStream lba = new ByteArrayOutputStream();
			DataOutputStream lds = new DataOutputStream(lba);
			LivingEntity lentity = pFigure.renderEntity;
			lds.writeInt(pFigure.getEntityId());		// ID
			lds.writeByte(0); // UpdateCount			// UpdateCount
			lds.writeFloat(pFigure.additionalYaw);		// Yaw
			lds.writeFloat(pFigure.zoom);				// zoom
			lds.writeBoolean(pFigure.isMove);			// move
			lds.writeBoolean(pFigure.isFigureRide);		// ride
			lds.writeBoolean(lentity.isSneaking());
			lds.writeBoolean(lentity.isChild());
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
				LivingEntity lel = pFigure.renderEntity;
				lds.readInt();									// EntityID
				lds.readByte();									// UpdateCount
				pFigure.additionalYaw = lds.readFloat();		// Yaw
				pFigure.zoom = lds.readFloat();					// zoom
				pFigure.setZoom(pFigure.zoom);
				pFigure.isMove = lds.readBoolean();				// move
				pFigure.isFigureRide = lds.readBoolean(); 		// ride
				if (pFigure.world.isRemote) {
					// Client
					if (pFigure.isFigureRide){
						lel.startRiding(pFigure);
					}else{
						lel.stopRiding();
					}
				} else {
					// Seerver
				}

				lel.setSneaking(lds.readBoolean());				//sneak
				boolean isChild = lds.readBoolean();
				if (lel instanceof AgeableEntity) {
					((AgeableEntity)lel).setGrowingAge(isChild?-1:1);		// child
				}else if (lel instanceof ZombieEntity) {
					((ZombieEntity)lel).setChild(isChild);
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

	public void sendItem(int pIndex, EntityDool pdool, boolean pClient, ServerPlayerEntity player) {
		try {
			if (pClient){
				MessageHandler.Send_MessageItem(pIndex, pdool.getEntityId(), pdool.renderEntity.getItemStackFromSlot(ContainerItemSelect.slotFromIndex.get(pIndex)));
			}else{
				if (player != null) {
					MessageHandler.Send_MessageItem_Client(pIndex, pdool.getEntityId(), pdool.renderEntity.getItemStackFromSlot(ContainerItemSelect.slotFromIndex.get(pIndex)),player);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void reciveItem(EntityDool pFigure, EquipmentSlotType slotidx, ItemStack item) {
		pFigure.renderEntity.setItemStackToSlot(slotidx, item);
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
	public void readEntityFromNBT(EntityDool pFigure, CompoundNBT CompoundNBT) {}

	/**
	 * 特殊なデータ書き込みを実行
	 */
	public void writeEntityToNBT(EntityDool pFigure, CompoundNBT CompoundNBT) {}

	/**
	 * 姿勢制御用
	 */
	public void setRotation(EntityDool pFigure) {}

	/**
	 * サーバーへ設定されたアイテムを送信。
	 */
	public void sendItems(EntityDool pdool, boolean pClient, ServerPlayerEntity player) {
		sendItem(5, pdool, pClient, player);
		sendItem(4, pdool, pClient, player);
		sendItem(3, pdool, pClient, player);
		sendItem(2, pdool, pClient, player);
		sendItem(1, pdool, pClient, player);
		sendItem(0, pdool, pClient, player);
	}

}
