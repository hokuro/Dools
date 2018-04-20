package basashi.dools.server;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import basashi.dools.entity.EntityDool;
import net.minecraft.entity.monster.EntityZombie;

public class ServerDool_zombie<T> extends ServerDool {
	@Override
	public void sendData(EntityDool pFigure, DataOutput pData)
			throws IOException {
		EntityZombie lentity = (EntityZombie)pFigure.renderEntity;
		pData.writeBoolean(lentity.isChild());
	}

	@Override
	public void reciveData(EntityDool pFigure, DataInput pData)
			throws IOException {
		EntityZombie lentity = (EntityZombie)pFigure.renderEntity;
		lentity.setChild(pData.readBoolean());
	}

}
