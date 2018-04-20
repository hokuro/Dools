package basashi.dools.server;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import basashi.dools.entity.EntityDool;
import net.minecraft.entity.passive.EntityVillager;

public class ServerDool_villager extends ServerDool {
	@Override
	public void sendData(EntityDool pFigure, DataOutput pData)
			throws IOException {
		EntityVillager lentity = (EntityVillager)pFigure.renderEntity;
		pData.writeInt(lentity.getProfession());
	}

	@Override
	public void reciveData(EntityDool pFigure, DataInput pData)
			throws IOException {
		EntityVillager lentity = (EntityVillager)pFigure.renderEntity;
		lentity.setProfession(pData.readInt());
	}

}
