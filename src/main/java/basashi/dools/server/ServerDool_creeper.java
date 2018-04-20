package basashi.dools.server;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import basashi.dools.entity.EntityDool;
import net.minecraft.entity.monster.EntityCreeper;

public class ServerDool_creeper extends ServerDool {
	@Override
	public void sendData(EntityDool pFigure, DataOutput pData)
			throws IOException {
		EntityCreeper lentity = (EntityCreeper)pFigure.renderEntity;
		pData.writeBoolean(lentity.getPowered());
	}

	@Override
	public void reciveData(EntityDool pFigure, DataInput pData)
			throws IOException {
		EntityCreeper lentity = (EntityCreeper)pFigure.renderEntity;
		if (pData.readBoolean()){
			lentity.onStruckByLightning(null);
		}
	}

}
