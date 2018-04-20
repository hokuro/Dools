package basashi.dools.server;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import basashi.dools.entity.EntityDool;
import net.minecraft.entity.passive.EntityMule;

public class ServerDool_mule extends ServerDool {
	@Override
	public void sendData(EntityDool pFigure, DataOutput pData)
			throws IOException {
		EntityMule lentity = (EntityMule)pFigure.renderEntity;
		pData.writeBoolean(lentity.hasChest());
	}

	@Override
	public void reciveData(EntityDool pFigure, DataInput pData)
			throws IOException {
		EntityMule lentity = (EntityMule)pFigure.renderEntity;
		lentity.setChested(pData.readBoolean());
	}

}
