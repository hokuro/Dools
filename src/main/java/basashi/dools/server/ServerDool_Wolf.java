package basashi.dools.server;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import basashi.dools.entity.EntityDool;
import net.minecraft.entity.passive.EntityWolf;

public class ServerDool_Wolf extends ServerDool {

	@Override
	public void sendData(EntityDool pFigure, DataOutput pData)
			throws IOException {
		super.sendData(pFigure, pData);
		EntityWolf lentity = (EntityWolf)pFigure.renderEntity;
		pData.writeFloat(lentity.getHealth());
		int lf = (lentity.isTamed() ? 1 : 0) |
				(lentity.isAngry() ? 2 : 0);
		pData.writeByte(lf);
	}

	@Override
	public void reciveData(EntityDool pFigure, DataInput pData)
			throws IOException {
		super.reciveData(pFigure, pData);
		EntityWolf lentity = (EntityWolf)pFigure.renderEntity;
		lentity.setHealth(pData.readFloat());
		int lf = pData.readByte();
		lentity.setTamed((lf & 1) != 0);
		lentity.setAngry((lf & 2) != 0);
	}

}
