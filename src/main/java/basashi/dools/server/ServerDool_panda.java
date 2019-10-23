package basashi.dools.server;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import basashi.dools.entity.EntityDool;
import net.minecraft.entity.passive.PandaEntity;
import net.minecraft.nbt.CompoundNBT;

public class ServerDool_panda extends ServerDool {
	@Override
 	public void sendData(EntityDool pFigure, DataOutput pData) throws IOException {
		super.sendData(pFigure, pData);
		PandaEntity entity = (PandaEntity)pFigure.renderEntity;
		pData.writeInt(entity.getMainGene().getIndex());
		pData.writeInt(entity.getHiddenGene().getIndex());
	}

	@Override
 	public void reciveData(EntityDool pFigure, DataInput pData) throws IOException {
		super.reciveData(pFigure, pData);
		PandaEntity entity = (PandaEntity)pFigure.renderEntity;
		int main = pData.readInt();
		int hide = pData.readInt();
		entity.setMainGene(PandaEntity.Type.byIndex(main));
		entity.setHiddenGene(PandaEntity.Type.byIndex(hide));
	}

	@Override
	public void readEntityFromNBT(EntityDool pFigure, CompoundNBT CompoundNBT) {
	}

	@Override
	public void writeEntityToNBT(EntityDool pFigure, CompoundNBT CompoundNBT) {
	}
}
