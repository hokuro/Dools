package basashi.dools.server;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import basashi.dools.core.Dools;
import basashi.dools.entity.EntityDool;
import net.minecraft.entity.passive.EntityLlama;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;

public class ServerDool_llama extends ServerDool {
	@Override
	public void sendData(EntityDool pFigure, DataOutput pData)
			throws IOException {
		EntityLlama lentity = (EntityLlama)pFigure.renderEntity;
		pData.writeBoolean(lentity.hasChest());
		pData.writeInt(lentity.getVariant());
		pData.writeInt(lentity.getDataManager().get((DataParameter<Integer>)Dools.getPrivateValue(EntityLlama.class, null, "DATA_COLOR_ID")));
	}

	@Override
	public void reciveData(EntityDool pFigure, DataInput pData)
			throws IOException {
		EntityLlama lentity = (EntityLlama)pFigure.renderEntity;
		lentity.setChested(pData.readBoolean());
		lentity.setVariant(pData.readInt());
		lentity.getDataManager().set((DataParameter<Integer>)Dools.getPrivateValue(EntityLlama.class, null, "DATA_COLOR_ID"),pData.readInt());

	}



	@Override
	public void readEntityFromNBT(EntityDool pFigure, NBTTagCompound nbttagcompound) {
		EntityLlama lentity = (EntityLlama)pFigure.renderEntity;
		int color = nbttagcompound.getInteger("color");
		lentity.getDataManager().set((DataParameter<Integer>)Dools.getPrivateValue(EntityLlama.class, null, "DATA_COLOR_ID"),color);
	}

	@Override
	public void writeEntityToNBT(EntityDool pFigure, NBTTagCompound nbttagcompound) {
		EntityLlama lentity = (EntityLlama)pFigure.renderEntity;
		int color = lentity.getDataManager().get((DataParameter<Integer>)Dools.getPrivateValue(EntityLlama.class, null, "DATA_COLOR_ID"));
		nbttagcompound.setInteger("color", color);
	}

}
