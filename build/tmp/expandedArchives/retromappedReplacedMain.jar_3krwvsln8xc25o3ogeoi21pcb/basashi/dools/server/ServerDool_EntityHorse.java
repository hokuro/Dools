package basashi.dools.server;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import basashi.dools.entity.EntityDool;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.passive.HorseArmorType;
import net.minecraft.nbt.NBTTagCompound;

public class ServerDool_EntityHorse extends ServerDool {
	@Override
	public void sendData(EntityDool pFigure, DataOutput pData)
			throws IOException {
		super.sendData(pFigure, pData);
		EntityHorse lentity = (EntityHorse)pFigure.renderEntity;
		pData.writeBoolean(lentity.func_110261_ca());
		pData.writeBoolean(lentity.func_110257_ck());
		pData.writeInt(lentity.func_184781_cZ().func_188595_k());
		pData.writeInt(lentity.func_110202_bQ());
	}

	@Override
	public void reciveData(EntityDool pFigure, DataInput pData)
			throws IOException {
		super.reciveData(pFigure, pData);
		EntityHorse lentity = (EntityHorse)pFigure.renderEntity;
		lentity.func_110207_m(pData.readBoolean());
		lentity.func_110251_o(pData.readBoolean());
		lentity.func_184778_a(HorseArmorType.func_188591_a(pData.readInt()));
		//lentity.setHorseType(pData.readInt());
		lentity.func_110235_q(pData.readInt());
	}


	@Override
	public void readEntityFromNBT(EntityDool pFigure, NBTTagCompound nbttagcompound) {
	}

	@Override
	public void writeEntityToNBT(EntityDool pFigure, NBTTagCompound nbttagcompound) {
	}

}
