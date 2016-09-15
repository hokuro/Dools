package basashi.dools.server;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import basashi.dools.entity.EntityDool;
import net.minecraft.entity.passive.EntityRabbit;

public class ServerDool_Rabbit extends ServerDool {
	@Override
	public void sendData(EntityDool pFigure, DataOutput pData)
			throws IOException {
		super.sendData(pFigure, pData);
		EntityRabbit lentity = (EntityRabbit)pFigure.renderEntity;
		pData.writeInt(lentity.func_175531_cl());
	}

	@Override
	public void reciveData(EntityDool pFigure, DataInput pData)
			throws IOException {
		super.reciveData(pFigure, pData);
		EntityRabbit lentity = (EntityRabbit)pFigure.renderEntity;
		lentity.func_175529_r(pData.readInt());
	}
}
