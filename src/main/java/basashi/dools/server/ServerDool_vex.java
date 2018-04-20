package basashi.dools.server;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import basashi.dools.entity.EntityDool;
import net.minecraft.entity.monster.EntityVex;
import net.minecraft.nbt.NBTTagCompound;

public class ServerDool_vex extends ServerDool {
	@Override
	public void sendData(EntityDool pFigure, DataOutput pData)
			throws IOException {
		EntityVex lentity = (EntityVex)pFigure.renderEntity;
		pData.writeBoolean(lentity.isCharging());
	}

	@Override
	public void reciveData(EntityDool pFigure, DataInput pData)
			throws IOException {
		EntityVex lentity = (EntityVex)pFigure.renderEntity;
		lentity.setCharging(pData.readBoolean());
	}

	@Override
	public void readEntityFromNBT(EntityDool pFigure, NBTTagCompound nbttagcompound) {
		EntityVex entity = (EntityVex)pFigure.renderEntity;
		entity.setCharging(nbttagcompound.getBoolean("angry"));
	}

	@Override
	public void writeEntityToNBT(EntityDool pFigure, NBTTagCompound nbttagcompound) {
		EntityVex entity = (EntityVex)pFigure.renderEntity;
		nbttagcompound.setBoolean("angry", entity.isCharging());
	}
}
