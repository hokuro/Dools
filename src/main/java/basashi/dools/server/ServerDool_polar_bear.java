package basashi.dools.server;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import basashi.dools.core.Dools;
import basashi.dools.entity.EntityDool;
import net.minecraft.entity.monster.EntityPolarBear;
import net.minecraft.nbt.NBTTagCompound;

public class ServerDool_polar_bear extends ServerDool {
	@Override
	public void sendData(EntityDool pFigure, DataOutput pData)
			throws IOException {
		EntityPolarBear lentity = (EntityPolarBear)pFigure.renderEntity;
		pData.writeBoolean(lentity.isStanding());
	}

	@Override
	public void reciveData(EntityDool pFigure, DataInput pData)
			throws IOException {
		EntityPolarBear lentity = (EntityPolarBear)pFigure.renderEntity;
		lentity.setStanding(pData.readBoolean());
		if (lentity.isStanding()){
			Dools.setPrivateValue(EntityPolarBear.class, lentity, 6, "clientSideStandAnimation0");
			Dools.setPrivateValue(EntityPolarBear.class, lentity, 6, "clientSideStandAnimation");
		}else{

			Dools.setPrivateValue(EntityPolarBear.class, lentity, 0, "clientSideStandAnimation0");
			Dools.setPrivateValue(EntityPolarBear.class, lentity, 0, "clientSideStandAnimation");
		}
	}
	/*
	 * 特殊なデータ読み込みを実行
	 */
	 @Override
	public void readEntityFromNBT(EntityDool pFigure, NBTTagCompound nbttagcompound) {
		 boolean stand = nbttagcompound.getBoolean("stand");
		 ((EntityPolarBear)pFigure.renderEntity).setStanding(stand);
		 if (stand){
			Dools.setPrivateValue(EntityPolarBear.class, (EntityPolarBear)pFigure.renderEntity, 6, "clientSideStandAnimation0");
			Dools.setPrivateValue(EntityPolarBear.class, (EntityPolarBear)pFigure.renderEntity, 6, "clientSideStandAnimation");
		}else{
			Dools.setPrivateValue(EntityPolarBear.class, (EntityPolarBear)pFigure.renderEntity, 0, "clientSideStandAnimation0");
			Dools.setPrivateValue(EntityPolarBear.class, (EntityPolarBear)pFigure.renderEntity, 0, "clientSideStandAnimation");
		}


	 }

	/**
	 * 特殊なデータ書き込みを実行
	 */
	 @Override
	public void writeEntityToNBT(EntityDool pFigure, NBTTagCompound nbttagcompound) {
		 nbttagcompound.setBoolean("stand",((EntityPolarBear)pFigure.renderEntity).isStanding());
	}
}
