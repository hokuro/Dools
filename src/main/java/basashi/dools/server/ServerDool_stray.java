package basashi.dools.server;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import basashi.dools.entity.EntityDool;
import net.minecraft.entity.monster.AbstractSkeletonEntity;
import net.minecraft.nbt.CompoundNBT;

public class ServerDool_stray extends ServerDool {
	@Override
 	public void sendData(EntityDool pFigure, DataOutput pData) throws IOException {
		super.sendData(pFigure, pData);
		AbstractSkeletonEntity lentity = (AbstractSkeletonEntity)pFigure.renderEntity;
		pData.writeBoolean(lentity.isAggressive());
	}

	@Override
 	public void reciveData(EntityDool pFigure, DataInput pData) throws IOException {
		super.reciveData(pFigure, pData);
		AbstractSkeletonEntity lentity = (AbstractSkeletonEntity)pFigure.renderEntity;
		lentity.setAggroed(pData.readBoolean());
	}

	@Override
	public void readEntityFromNBT(EntityDool pFigure, CompoundNBT CompoundNBT) {
		AbstractSkeletonEntity ent = (AbstractSkeletonEntity)pFigure.renderEntity;
		ent.setAggroed(CompoundNBT.getBoolean("ARM"));
	}

	@Override
	public void writeEntityToNBT(EntityDool pFigure, CompoundNBT CompoundNBT) {
		AbstractSkeletonEntity ent = (AbstractSkeletonEntity)pFigure.renderEntity;
		CompoundNBT.putBoolean("ARM",ent.isAggressive());
	}
}
