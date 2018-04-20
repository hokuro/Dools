package basashi.dools.server;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import basashi.dools.entity.EntityDool;
import net.minecraft.entity.passive.EntityRabbit;

public class ServerDool_rabbit extends ServerDool {
	@Override
	public void sendData(EntityDool pFigure, DataOutput pData)
			throws IOException {
		super.sendData(pFigure, pData);
		EntityRabbit lentity = (EntityRabbit)pFigure.renderEntity;
		pData.writeInt(lentity.getRabbitType());
	}

	@Override
	public void reciveData(EntityDool pFigure, DataInput pData)
			throws IOException {
		super.reciveData(pFigure, pData);
		EntityRabbit lentity = (EntityRabbit)pFigure.renderEntity;
		lentity.setRabbitType(pData.readInt());
	}
}
