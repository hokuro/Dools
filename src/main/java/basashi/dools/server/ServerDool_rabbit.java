package basashi.dools.server;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import basashi.dools.entity.EntityDool;
import net.minecraft.entity.passive.RabbitEntity;
import net.minecraft.nbt.CompoundNBT;

public class ServerDool_rabbit extends ServerDool {
	@Override
 	public void sendData(EntityDool pFigure, DataOutput pData) throws IOException {
		super.sendData(pFigure, pData);
		RabbitEntity lentity = (RabbitEntity)pFigure.renderEntity;
		pData.writeInt(lentity.getRabbitType());
	}

	@Override
 	public void reciveData(EntityDool pFigure, DataInput pData) throws IOException {
		super.reciveData(pFigure, pData);
		RabbitEntity lentity = (RabbitEntity)pFigure.renderEntity;
		lentity.setRabbitType(pData.readInt());
	}

	@Override
	public void readEntityFromNBT(EntityDool pFigure, CompoundNBT CompoundNBT) {
	}

	@Override
	public void writeEntityToNBT(EntityDool pFigure, CompoundNBT CompoundNBT) {
	}
}
