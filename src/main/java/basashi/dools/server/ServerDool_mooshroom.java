package basashi.dools.server;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import basashi.dools.entity.EntityDool;
import net.minecraft.entity.passive.MooshroomEntity;
import net.minecraft.nbt.CompoundNBT;

public class ServerDool_mooshroom extends ServerDool {
	@Override
 	public void sendData(EntityDool pFigure, DataOutput pData) throws IOException {
		super.sendData(pFigure, pData);

		MooshroomEntity entity = (MooshroomEntity)pFigure.renderEntity;
		MooshroomEntity.Type tp = entity.getMooshroomType();
		if (tp == MooshroomEntity.Type.RED) {
			pData.writeInt(0);
		}else {
			pData.writeInt(1);
		}
	}

	@Override
 	public void reciveData(EntityDool pFigure, DataInput pData) throws IOException {
		super.reciveData(pFigure, pData);
		MooshroomEntity entity = (MooshroomEntity)pFigure.renderEntity;
		CompoundNBT nbt = new CompoundNBT();
		int tp = pData.readInt();
		entity.writeAdditional(nbt);
		if (tp == 0) {
			nbt.putString("Type", MooshroomEntity.Type.RED.name().toLowerCase());
		}else {
			nbt.putString("Type", MooshroomEntity.Type.BROWN.name().toLowerCase());
		}
		entity.readAdditional(nbt);
	}

	@Override
	public void readEntityFromNBT(EntityDool pFigure, CompoundNBT CompoundNBT) {
		MooshroomEntity entity = (MooshroomEntity)pFigure.renderEntity;
		entity.readAdditional(CompoundNBT);
	}

	@Override
	public void writeEntityToNBT(EntityDool pFigure, CompoundNBT CompoundNBT) {
		MooshroomEntity entity = (MooshroomEntity)pFigure.renderEntity;
		MooshroomEntity.Type tp = entity.getMooshroomType();
		entity.writeAdditional(CompoundNBT);
		if (tp == MooshroomEntity.Type.BROWN) {
			CompoundNBT.putString("Type", MooshroomEntity.Type.RED.name().toLowerCase());
		}else {
			CompoundNBT.putString("Type", MooshroomEntity.Type.BROWN.name().toLowerCase());
		}
	}
}
