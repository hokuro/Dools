package basashi.dools.server;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import basashi.dools.entity.EntityDool;
import net.minecraft.entity.monster.SlimeEntity;
import net.minecraft.nbt.CompoundNBT;

public class ServerDool_slime extends ServerDool {
	@Override
 	public void sendData(EntityDool pFigure, DataOutput pData) throws IOException {
		super.sendData(pFigure, pData);
		SlimeEntity entity = (SlimeEntity)pFigure.renderEntity;
		pData.writeInt(entity.getSlimeSize());
	}

	@Override
 	public void reciveData(EntityDool pFigure, DataInput pData) throws IOException {
		super.reciveData(pFigure, pData);
		SlimeEntity entity = (SlimeEntity)pFigure.renderEntity;
		int size = pData.readInt();
		CompoundNBT nbt = new CompoundNBT();
		entity.writeAdditional(nbt);
		nbt.putInt("Size", size);
		entity.readAdditional(nbt);
	}

	@Override
	public void readEntityFromNBT(EntityDool pFigure, CompoundNBT CompoundNBT) {
	}

	@Override
	public void writeEntityToNBT(EntityDool pFigure, CompoundNBT CompoundNBT) {
	}
}
