package basashi.dools.server;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import basashi.dools.entity.EntityDool;
import net.minecraft.entity.monster.EntityZombieVillager;

public class ServerDool_zombie_villager extends ServerDool {
	@Override
	public void sendData(EntityDool pFigure, DataOutput pData)
			throws IOException {
		EntityZombieVillager lentity = (EntityZombieVillager)pFigure.renderEntity;
		pData.writeBoolean(lentity.isChild());
		pData.writeInt(lentity.getProfession());
	}

	@Override
	public void reciveData(EntityDool pFigure, DataInput pData)
			throws IOException {
		EntityZombieVillager lentity = (EntityZombieVillager)pFigure.renderEntity;
		lentity.setChild(pData.readBoolean());
		lentity.setProfession(pData.readInt());
	}
}
