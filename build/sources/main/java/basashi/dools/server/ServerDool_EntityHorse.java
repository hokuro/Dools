package basashi.dools.server;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import basashi.dools.entity.EntityDool;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.passive.HorseArmorType;
import net.minecraft.nbt.NBTTagCompound;

public class ServerDool_EntityHorse extends ServerDool {
	@Override
	public void sendData(EntityDool pFigure, DataOutput pData)
			throws IOException {
		super.sendData(pFigure, pData);
		EntityHorse lentity = (EntityHorse)pFigure.renderEntity;
		pData.writeBoolean(lentity.isChested());
		pData.writeBoolean(lentity.isHorseSaddled());
		pData.writeInt(lentity.getType().getOrdinal());
		pData.writeInt(lentity.getHorseVariant());
	}

	@Override
	public void reciveData(EntityDool pFigure, DataInput pData)
			throws IOException {
		super.reciveData(pFigure, pData);
		EntityHorse lentity = (EntityHorse)pFigure.renderEntity;
		lentity.setChested(pData.readBoolean());
		lentity.setHorseSaddled(pData.readBoolean());
		lentity.setType(HorseArmorType.getArmorType(pData.readInt()));
		//lentity.setHorseType(pData.readInt());
		lentity.setHorseVariant(pData.readInt());
	}


	@Override
	public void readEntityFromNBT(EntityDool pFigure, NBTTagCompound nbttagcompound) {
	}

	@Override
	public void writeEntityToNBT(EntityDool pFigure, NBTTagCompound nbttagcompound) {
	}

}
