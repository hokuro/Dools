package basashi.dools.server;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import basashi.dools.entity.EntityDool;
import net.minecraft.entity.monster.EntityBlaze;

public class ServerDool_blaze extends ServerDool {
	@Override
	public void sendData(EntityDool pFigure, DataOutput pData)
			throws IOException {
		EntityBlaze lentity = (EntityBlaze)pFigure.renderEntity;
		pData.writeBoolean(lentity.isBurning());
	}
//
	@Override
	public void reciveData(EntityDool pFigure, DataInput pData)
			throws IOException {
		EntityBlaze lentity = (EntityBlaze)pFigure.renderEntity;
		lentity.setOnFire(pData.readBoolean());
	}
}
