package basashi.dools.server;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import basashi.dools.entity.EntityDool;
import net.minecraft.entity.passive.EntityPig;

public class ServerDool_pig extends ServerDool {
	@Override
	public void sendData(EntityDool pFigure, DataOutput pData)
			throws IOException {
		EntityPig lentity = (EntityPig)pFigure.renderEntity;
		pData.writeBoolean(lentity.getSaddled());
	}

	@Override
	public void reciveData(EntityDool pFigure, DataInput pData)
			throws IOException {
		EntityPig lentity = (EntityPig)pFigure.renderEntity;
		lentity.setSaddled(pData.readBoolean());
	}
}
