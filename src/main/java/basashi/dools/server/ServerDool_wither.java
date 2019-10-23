package basashi.dools.server;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import basashi.dools.entity.EntityDool;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.nbt.CompoundNBT;

public class ServerDool_wither extends ServerDool {
	@Override
 	public void sendData(EntityDool pFigure, DataOutput pData) throws IOException {
		super.sendData(pFigure, pData);
		WitherEntity lentity = (WitherEntity)pFigure.renderEntity;
		int inv = (lentity.getInvulTime());
		boolean armor = (lentity.isArmored());

		pData.writeInt(inv);
		pData.writeBoolean(armor);
	}

	@Override
 	public void reciveData(EntityDool pFigure, DataInput pData) throws IOException {
		super.reciveData(pFigure, pData);
		WitherEntity lentity = (WitherEntity)pFigure.renderEntity;
		int inv = pData.readInt();
		boolean armor = pData.readBoolean();
		lentity.setInvulTime(inv);
		if (armor){
			lentity.setHealth(1);
		}else{
			lentity.setHealth(lentity.getMaxHealth());
		}
	}

	@Override
	public void readEntityFromNBT(EntityDool pFigure, CompoundNBT CompoundNBT) {
	}

	@Override
	public void writeEntityToNBT(EntityDool pFigure, CompoundNBT CompoundNBT) {
	}
}
