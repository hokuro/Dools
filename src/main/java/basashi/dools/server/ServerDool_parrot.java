package basashi.dools.server;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import basashi.dools.entity.EntityDool;
import net.minecraft.entity.passive.EntityParrot;

public class ServerDool_parrot extends ServerDool {
	@Override
	public void sendData(EntityDool pFigure, DataOutput pData)
			throws IOException {
		EntityParrot lentity = (EntityParrot)pFigure.renderEntity;
		pData.writeInt(lentity.getVariant());
	}

	@Override
	public void reciveData(EntityDool pFigure, DataInput pData)
			throws IOException {
		EntityParrot lentity = (EntityParrot)pFigure.renderEntity;
		lentity.setVariant(pData.readInt());
	}
}
