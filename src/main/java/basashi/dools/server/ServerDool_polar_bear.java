package basashi.dools.server;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import basashi.dools.entity.EntityDool;
import net.minecraft.entity.passive.PolarBearEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

public class ServerDool_polar_bear extends ServerDool {
	@Override
 	public void sendData(EntityDool pFigure, DataOutput pData) throws IOException {
		super.sendData(pFigure, pData);
		PolarBearEntity lentity = (PolarBearEntity)pFigure.renderEntity;
		pData.writeBoolean(lentity.isStanding());
	}

	@Override
 	public void reciveData(EntityDool pFigure, DataInput pData) throws IOException {
		super.reciveData(pFigure, pData);
		PolarBearEntity lentity = (PolarBearEntity)pFigure.renderEntity;
		lentity.setStanding(pData.readBoolean());
		if (lentity.isStanding()){
			ObfuscationReflectionHelper.setPrivateValue(PolarBearEntity.class, lentity, 6, "clientSideStandAnimation0");
			ObfuscationReflectionHelper.setPrivateValue(PolarBearEntity.class, lentity, 6, "clientSideStandAnimation");
		}else{
			ObfuscationReflectionHelper.setPrivateValue(PolarBearEntity.class, lentity, 0, "clientSideStandAnimation0");
			ObfuscationReflectionHelper.setPrivateValue(PolarBearEntity.class, lentity, 0, "clientSideStandAnimation");
		}
	}

	@Override
	public void readEntityFromNBT(EntityDool pFigure, CompoundNBT CompoundNBT) {
		 boolean stand = CompoundNBT.getBoolean("stand");
		 ((PolarBearEntity)pFigure.renderEntity).setStanding(stand);
		 if (stand){
			ObfuscationReflectionHelper.setPrivateValue(PolarBearEntity.class, (PolarBearEntity)pFigure.renderEntity, 6, "clientSideStandAnimation0");
			ObfuscationReflectionHelper.setPrivateValue(PolarBearEntity.class, (PolarBearEntity)pFigure.renderEntity, 6, "clientSideStandAnimation");
		}else{
			ObfuscationReflectionHelper.setPrivateValue(PolarBearEntity.class, (PolarBearEntity)pFigure.renderEntity, 0, "clientSideStandAnimation0");
			ObfuscationReflectionHelper.setPrivateValue(PolarBearEntity.class, (PolarBearEntity)pFigure.renderEntity, 0, "clientSideStandAnimation");
		}
	}

	@Override
	public void writeEntityToNBT(EntityDool pFigure, CompoundNBT CompoundNBT) {
		 CompoundNBT.putBoolean("stand",((PolarBearEntity)pFigure.renderEntity).isStanding());
	}
}
