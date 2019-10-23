package basashi.dools.server;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import basashi.dools.entity.EntityDool;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.nbt.CompoundNBT;

public class ServerDool_creeper extends ServerDool {
	@Override
 	public void sendData(EntityDool pFigure, DataOutput pData) throws IOException {
		super.sendData(pFigure, pData);
		CreeperEntity entity = (CreeperEntity)pFigure.renderEntity;
		pData.writeBoolean(entity.getPowered());

	}

	@Override
 	public void reciveData(EntityDool pFigure, DataInput pData) throws IOException {
		super.reciveData(pFigure, pData);
		boolean power = pData.readBoolean();

		CompoundNBT nbt = new CompoundNBT();
		pFigure.renderEntity.writeAdditional(nbt);
		nbt.putBoolean("powered",power);
		pFigure.renderEntity.readAdditional(nbt);
	}

	@Override
	public void readEntityFromNBT(EntityDool pFigure, CompoundNBT CompoundNBT) {
	}

	@Override
	public void writeEntityToNBT(EntityDool pFigure, CompoundNBT CompoundNBT) {
	}
}
