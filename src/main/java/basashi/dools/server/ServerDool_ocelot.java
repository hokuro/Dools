package basashi.dools.server;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import basashi.dools.entity.EntityDool;
import net.minecraft.entity.passive.EntityOcelot;

public class ServerDool_ocelot extends ServerDool {

	@Override
	public void sendData(EntityDool pFigure, DataOutput pData)
			throws IOException {
		super.sendData(pFigure, pData);
		EntityOcelot lentity = (EntityOcelot)pFigure.renderEntity;
		pData.writeInt(lentity.getTameSkin());
		pData.writeBoolean(lentity.isSitting());
		pData.writeBoolean(lentity.isTamed());
	}

	@Override
	public void reciveData(EntityDool pFigure, DataInput pData)
			throws IOException {
		super.reciveData(pFigure, pData);
		EntityOcelot lentity = (EntityOcelot)pFigure.renderEntity;
		lentity.setTameSkin(pData.readInt());
		lentity.setSitting(pData.readBoolean());
		lentity.setTamed(pData.readBoolean());
	}


//	@Override
//	public void readEntityFromNBT(EntityDool pFigure, NBTTagCompound nbttagcompound) {
//		EntityOcelot lentity = (EntityOcelot)pFigure.renderEntity;
//		lentity.setSitting(nbttagcompound.getBoolean("sit"));
//		lentity.setTamed(nbttagcompound.getBoolean("tame"));
//	}
//
//	@Override
//	public void writeEntityToNBT(EntityDool pFigure, NBTTagCompound nbttagcompound) {
//		EntityOcelot lentity = (EntityOcelot)pFigure.renderEntity;
//		nbttagcompound.setBoolean("sit", lentity.isSitting());
//		nbttagcompound.setBoolean("tame", lentity.isTamed());
//	}

}
