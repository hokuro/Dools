package basashi.dools.server;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import basashi.dools.entity.EntityDool;
import net.minecraft.entity.monster.AbstractSkeleton;
import net.minecraft.nbt.NBTTagCompound;

public class ServerDool_skeleton extends ServerDool {
	@Override
	public void sendData(EntityDool pFigure, DataOutput pData)
			throws IOException {
		AbstractSkeleton lentity = (AbstractSkeleton)pFigure.renderEntity;
		pData.writeBoolean(lentity.isSwingingArms());
	}

	@Override
	public void reciveData(EntityDool pFigure, DataInput pData)
			throws IOException {
		AbstractSkeleton lentity = (AbstractSkeleton)pFigure.renderEntity;
		lentity.setSwingingArms(pData.readBoolean());
	}

	@Override
	public void readEntityFromNBT(EntityDool pFigure, NBTTagCompound nbttagcompound) {
		AbstractSkeleton ent = (AbstractSkeleton)pFigure.renderEntity;
		ent.setSwingingArms(nbttagcompound.getBoolean("ARM"));
	}

	@Override
	public void writeEntityToNBT(EntityDool pFigure, NBTTagCompound nbttagcompound) {
		AbstractSkeleton ent = (AbstractSkeleton)pFigure.renderEntity;
		nbttagcompound.setBoolean("ARM",ent.isSwingingArms());
	}
}
