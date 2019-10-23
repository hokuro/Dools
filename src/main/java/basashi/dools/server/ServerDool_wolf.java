package basashi.dools.server;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.UUID;

import basashi.dools.entity.EntityDool;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.item.DyeColor;
import net.minecraft.nbt.CompoundNBT;

public class ServerDool_wolf extends ServerDool {
	@Override
 	public void sendData(EntityDool pFigure, DataOutput pData) throws IOException {
		super.sendData(pFigure, pData);
		WolfEntity lentity = (WolfEntity)pFigure.renderEntity;
		pData.writeFloat(lentity.getHealth());
		pData.writeBoolean(lentity.isSitting());
		int lf = (lentity.isTamed() ? 1 : 0) |
				(lentity.isAngry() ? 2 : 0);
		pData.writeByte(lf);
		pData.writeInt(lentity.getCollarColor().getId());
	}

	@Override
 	public void reciveData(EntityDool pFigure, DataInput pData) throws IOException {
		super.reciveData(pFigure, pData);
		WolfEntity lentity = (WolfEntity)pFigure.renderEntity;
		lentity.setHealth(pData.readFloat());
		lentity.setSitting(pData.readBoolean());
		int lf = pData.readByte();
		lentity.setTamed((lf & 1) != 0);
		lentity.setAngry((lf & 2) != 0);
		lentity.setCollarColor(DyeColor.values()[pData.readInt()]);
		if (lentity.isTamed()){
			lentity.setOwnerId(new UUID(1,1));
		}else{
			lentity.setOwnerId(null);
		}
	}

	@Override
	public void readEntityFromNBT(EntityDool pFigure, CompoundNBT CompoundNBT) {
	}

	@Override
	public void writeEntityToNBT(EntityDool pFigure, CompoundNBT CompoundNBT) {
	}
}
