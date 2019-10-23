package basashi.dools.server;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import basashi.dools.entity.EntityDool;
import net.minecraft.entity.passive.CatEntity;
import net.minecraft.nbt.CompoundNBT;

public class ServerDool_cat extends ServerDool {
	@Override
 	public void sendData(EntityDool pFigure, DataOutput pData) throws IOException {
		super.sendData(pFigure, pData);
		CatEntity lentity = (CatEntity)pFigure.renderEntity;
		pData.writeInt(lentity.getCatType());
		pData.writeBoolean(lentity.isSitting());
		pData.writeBoolean(lentity.isTamed());
	}

	@Override
 	public void reciveData(EntityDool pFigure, DataInput pData) throws IOException {
		super.reciveData(pFigure, pData);
		CatEntity lentity = (CatEntity)pFigure.renderEntity;
		lentity.setCatType(pData.readInt());
		lentity.setSitting(pData.readBoolean());
		lentity.setTamed(pData.readBoolean());
	}

	@Override
	public void readEntityFromNBT(EntityDool pFigure, CompoundNBT CompoundNBT) {
	}

	@Override
	public void writeEntityToNBT(EntityDool pFigure, CompoundNBT CompoundNBT) {
	}
}
