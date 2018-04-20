package basashi.dools.server;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import basashi.dools.entity.EntityDool;
import net.minecraft.entity.passive.EntityZombieHorse;
import net.minecraft.nbt.NBTTagCompound;

public class ServerDool_zombie_horse extends ServerDool {
	@Override
	public void sendData(EntityDool pFigure, DataOutput pData)
			throws IOException {
		EntityZombieHorse lentity = (EntityZombieHorse)pFigure.renderEntity;
		pData.writeBoolean(lentity.isHorseSaddled());
	}

	@Override
	public void reciveData(EntityDool pFigure, DataInput pData)
			throws IOException {
		EntityZombieHorse lentity = (EntityZombieHorse)pFigure.renderEntity;
		lentity.setHorseSaddled(pData.readBoolean());
	}

	@Override
	public void readEntityFromNBT(EntityDool pFigure, NBTTagCompound nbttagcompound) {
		EntityZombieHorse lentity = (EntityZombieHorse)pFigure.renderEntity;
		lentity.setHorseSaddled(nbttagcompound.getBoolean("saddl"));
	}

	@Override
	public void writeEntityToNBT(EntityDool pFigure, NBTTagCompound nbttagcompound) {
		EntityZombieHorse lentity = (EntityZombieHorse)pFigure.renderEntity;
		nbttagcompound.setBoolean("saddl", lentity.isHorseSaddled());
	}
}
