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
		pData.writeFloat(lentity.func_110143_aJ());
		int lf = (lentity.func_70909_n() ? 1 : 0) |
				(lentity.func_70919_bu() ? 2 : 0);
		pData.writeByte(lf);
	}

	@Override
	public void reciveData(EntityDool pFigure, DataInput pData)
			throws IOException {
		super.reciveData(pFigure, pData);
		EntityWolf lentity = (EntityWolf)pFigure.renderEntity;
		lentity.func_70606_j(pData.readFloat());
		int lf = pData.readByte();
		lentity.func_70903_f((lf & 1) != 0);
		lentity.func_70916_h((lf & 2) != 0);
	}

}
