package basashi.dools.server;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import basashi.dools.entity.EntityDool;
import net.minecraft.entity.passive.EntityBat;

public class ServerDool_bat extends ServerDool {
	@Override
	public void sendData(EntityDool pFigure, DataOutput pData)
			throws IOException {
		EntityBat lentity = (EntityBat)pFigure.renderEntity;
		pData.writeBoolean(lentity.getIsBatHanging());
	}

	@Override
	public void reciveData(EntityDool pFigure, DataInput pData)
			throws IOException {
		EntityBat lentity = (EntityBat)pFigure.renderEntity;
		lentity.setIsBatHanging(pData.readBoolean());
	}

}
