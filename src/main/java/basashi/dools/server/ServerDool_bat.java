package basashi.dools.server;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import basashi.dools.entity.EntityDool;
import net.minecraft.entity.passive.BatEntity;
import net.minecraft.nbt.CompoundNBT;

public class ServerDool_bat extends ServerDool {

	@Override
 	public void sendData(EntityDool pFigure, DataOutput pData) throws IOException {
		super.sendData(pFigure, pData);
		BatEntity lentity = (BatEntity)pFigure.renderEntity;
		pData.writeBoolean(lentity.getIsBatHanging());
	}

	@Override
 	public void reciveData(EntityDool pFigure, DataInput pData) throws IOException {
		super.reciveData(pFigure, pData);
		BatEntity lentity = (BatEntity)pFigure.renderEntity;
		lentity.setIsBatHanging(pData.readBoolean());
	}

	@Override
	public void readEntityFromNBT(EntityDool pFigure, CompoundNBT CompoundNBT) {
	}

	@Override
	public void writeEntityToNBT(EntityDool pFigure, CompoundNBT CompoundNBT) {
	}
}
