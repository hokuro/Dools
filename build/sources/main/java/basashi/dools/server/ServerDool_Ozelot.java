package basashi.dools.server;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import basashi.dools.entity.EntityDool;
import net.minecraft.entity.passive.EntityOcelot;

public class ServerDool_Ozelot extends ServerDool {

	@Override
	public void sendData(EntityDool pFigure, DataOutput pData)
			throws IOException {
		super.sendData(pFigure, pData);
		EntityOcelot lentity = (EntityOcelot)pFigure.renderEntity;
		pData.writeByte(lentity.getTameSkin());
		int lf = (lentity.isTamed() ? 1 : 0);
		pData.writeByte(lf);
	}

	@Override
	public void reciveData(EntityDool pFigure, DataInput pData)
			throws IOException {
		super.reciveData(pFigure, pData);
		EntityOcelot lentity = (EntityOcelot)pFigure.renderEntity;
		lentity.setTameSkin(pData.readByte());
		int lf = pData.readByte();
		lentity.setTamed((lf & 1) != 0);
	}

}
