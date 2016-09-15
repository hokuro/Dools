package basashi.dools.server;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import basashi.dools.core.Dools;
import basashi.dools.entity.EntityDool;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.network.datasync.DataParameter;

public class ServerDool_Zombie<T> extends ServerDool {
	@Override
	public void sendData(EntityDool pFigure, DataOutput pData)
			throws IOException {
		EntityZombie lentity = (EntityZombie)pFigure.renderEntity;
		int lf = (lentity.func_184736_de());
		pData.writeByte(lf);
	}
//
	@Override
	public void reciveData(EntityDool pFigure, DataInput pData)
			throws IOException {
		EntityZombie lentity = (EntityZombie)pFigure.renderEntity;
		int lf = pData.readByte();
		lentity.func_184735_a(lf);
		lentity.func_184212_Q().func_187227_b((DataParameter<Boolean>)Dools.getPrivateValue(EntityZombie.class, null, "IS_CHILD"),
				((lf & 2) != 0));
//		lentity.setVillager((lf & 1) != 0);
//		lentity.getDataWatcher().updateObject(12, ((lf & 2) != 0) ? (byte)1 : (byte)0);
	}

}
