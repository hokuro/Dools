package basashi.dools.server;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import basashi.dools.entity.EntityDool;
import net.minecraft.entity.monster.VexEntity;
import net.minecraft.nbt.CompoundNBT;

public class ServerDool_vex extends ServerDool {
	@Override
 	public void sendData(EntityDool pFigure, DataOutput pData) throws IOException {
		super.sendData(pFigure, pData);
		VexEntity lentity = (VexEntity)pFigure.renderEntity;
		pData.writeBoolean(lentity.isCharging());
	}

	@Override
 	public void reciveData(EntityDool pFigure, DataInput pData) throws IOException {
		super.reciveData(pFigure, pData);
		VexEntity lentity = (VexEntity)pFigure.renderEntity;
		lentity.setCharging(pData.readBoolean());
	}

	@Override
	public void readEntityFromNBT(EntityDool pFigure, CompoundNBT CompoundNBT) {
		VexEntity entity = (VexEntity)pFigure.renderEntity;
		entity.setCharging(CompoundNBT.getBoolean("angry"));
	}

	@Override
	public void writeEntityToNBT(EntityDool pFigure, CompoundNBT CompoundNBT) {
		VexEntity entity = (VexEntity)pFigure.renderEntity;
		CompoundNBT.putBoolean("angry", entity.isCharging());
	}
}
