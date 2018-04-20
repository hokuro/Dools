package basashi.dools.server;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import basashi.dools.entity.EntityDool;
import net.minecraft.entity.monster.EntitySnowman;

public class ServerDool_snowman extends ServerDool {
	@Override
	public void sendData(EntityDool pFigure, DataOutput pData)
			throws IOException {
		EntitySnowman lentity = (EntitySnowman)pFigure.renderEntity;
		pData.writeBoolean(lentity.isPumpkinEquipped());
	}

	@Override
	public void reciveData(EntityDool pFigure, DataInput pData)
			throws IOException {
		EntitySnowman lentity = (EntitySnowman)pFigure.renderEntity;
		lentity.setPumpkinEquipped(pData.readBoolean());
	}
}
