package basashi.dools.server;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import basashi.dools.entity.EntityDool;
import net.minecraft.entity.monster.EntityVindicator;
import net.minecraft.nbt.NBTTagCompound;

public class ServerDool_vindication_illager extends ServerDool {
	@Override
	public void sendData(EntityDool pFigure, DataOutput pData)
			throws IOException {
		EntityVindicator lentity = (EntityVindicator)pFigure.renderEntity;
		pData.writeBoolean(lentity.isAggressive());
	}

	@Override
	public void reciveData(EntityDool pFigure, DataInput pData)
			throws IOException {
		EntityVindicator lentity = (EntityVindicator)pFigure.renderEntity;
		lentity.setAggressive(pData.readBoolean());
	}

	@Override
	public void readEntityFromNBT(EntityDool pFigure, NBTTagCompound nbttagcompound) {
		EntityVindicator entity = (EntityVindicator)pFigure.renderEntity;
		entity.setAggressive(nbttagcompound.getBoolean("angry"));
	}

	@Override
	public void writeEntityToNBT(EntityDool pFigure, NBTTagCompound nbttagcompound) {
		EntityVindicator entity = (EntityVindicator)pFigure.renderEntity;
		nbttagcompound.setBoolean("angry", entity.isAggressive());
	}
}
