package basashi.dools.server;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.lang.reflect.Method;

import basashi.dools.entity.EntityDool;
import net.minecraft.entity.monster.BlazeEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

public class ServerDool_blaze extends ServerDool {
	@Override
	public void sendData(EntityDool pFigure, DataOutput pData) throws IOException {
		BlazeEntity lentity = (BlazeEntity)pFigure.renderEntity;
		pData.writeBoolean(lentity.isBurning());
	}

	@Override
	public void reciveData(EntityDool pFigure, DataInput pData) throws IOException {
		BlazeEntity lentity = (BlazeEntity)pFigure.renderEntity;
		boolean burning = pData.readBoolean();
		try {
			Method setOnFire = ObfuscationReflectionHelper.findMethod(BlazeEntity.class, "setOnFire", boolean.class);
			setOnFire.invoke(lentity, burning);
		}catch(Throwable ex) {

		}
	}

	@Override
	public void readEntityFromNBT(EntityDool pFigure, CompoundNBT CompoundNBT) {
	}

	@Override
	public void writeEntityToNBT(EntityDool pFigure, CompoundNBT CompoundNBT) {
	}
}
