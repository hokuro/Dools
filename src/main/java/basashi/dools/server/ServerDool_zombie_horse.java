package basashi.dools.server;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import basashi.dools.entity.EntityDool;
import net.minecraft.entity.passive.horse.ZombieHorseEntity;
import net.minecraft.nbt.CompoundNBT;

public class ServerDool_zombie_horse extends ServerDool {
	@Override
 	public void sendData(EntityDool pFigure, DataOutput pData) throws IOException {
		super.sendData(pFigure, pData);
		ZombieHorseEntity lentity = (ZombieHorseEntity)pFigure.renderEntity;
		pData.writeBoolean(lentity.isHorseSaddled());
	}

	@Override
 	public void reciveData(EntityDool pFigure, DataInput pData) throws IOException {
		super.reciveData(pFigure, pData);
		ZombieHorseEntity lentity = (ZombieHorseEntity)pFigure.renderEntity;
		lentity.setHorseSaddled(pData.readBoolean());
	}

	@Override
	public void readEntityFromNBT(EntityDool pFigure, CompoundNBT CompoundNBT) {
		ZombieHorseEntity lentity = (ZombieHorseEntity)pFigure.renderEntity;
		lentity.setHorseSaddled(CompoundNBT.getBoolean("saddl"));
	}

	@Override
	public void writeEntityToNBT(EntityDool pFigure, CompoundNBT CompoundNBT) {
		ZombieHorseEntity lentity = (ZombieHorseEntity)pFigure.renderEntity;
		CompoundNBT.putBoolean("saddl", lentity.isHorseSaddled());
	}
}
