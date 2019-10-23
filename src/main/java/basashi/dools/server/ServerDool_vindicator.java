package basashi.dools.server;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import basashi.dools.entity.EntityDool;
import net.minecraft.entity.monster.VindicatorEntity;
import net.minecraft.nbt.CompoundNBT;

public class ServerDool_vindicator extends ServerDool {
	@Override
 	public void sendData(EntityDool pFigure, DataOutput pData) throws IOException {
		super.sendData(pFigure, pData);
		VindicatorEntity lentity = (VindicatorEntity)pFigure.renderEntity;
		pData.writeBoolean(lentity.isAggressive());
	}

	@Override
 	public void reciveData(EntityDool pFigure, DataInput pData) throws IOException {
		super.reciveData(pFigure, pData);
		VindicatorEntity lentity = (VindicatorEntity)pFigure.renderEntity;
		lentity.setAggroed(pData.readBoolean());
	}

	@Override
	public void readEntityFromNBT(EntityDool pFigure, CompoundNBT CompoundNBT) {
		VindicatorEntity entity = (VindicatorEntity)pFigure.renderEntity;
		entity.setAggroed(CompoundNBT.getBoolean("angry"));
	}

	@Override
	public void writeEntityToNBT(EntityDool pFigure, CompoundNBT CompoundNBT) {
		VindicatorEntity entity = (VindicatorEntity)pFigure.renderEntity;
		CompoundNBT.putBoolean("angry", entity.isAggressive());
	}
}
