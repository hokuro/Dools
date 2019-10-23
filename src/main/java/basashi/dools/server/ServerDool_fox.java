package basashi.dools.server;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import basashi.dools.entity.EntityDool;
import net.minecraft.entity.passive.FoxEntity;
import net.minecraft.nbt.CompoundNBT;

public class ServerDool_fox extends ServerDool {
	@Override
 	public void sendData(EntityDool pFigure, DataOutput pData) throws IOException {
		super.sendData(pFigure, pData);
		pData.writeInt(((FoxEntity)pFigure.renderEntity).getVariantType().getIndex());
	}

	@Override
 	public void reciveData(EntityDool pFigure, DataInput pData) throws IOException {
		super.reciveData(pFigure, pData);
		FoxEntity entity = (FoxEntity)pFigure.renderEntity;
		int foxIndex = pData.readInt();
		FoxEntity.Type foxtype = FoxEntity.Type.getTypeByIndex(foxIndex);
		CompoundNBT nbt = new CompoundNBT();
		entity.writeAdditional(nbt);
		nbt.putString("Type", foxtype.getName());
		entity.readAdditional(nbt);
	}

	@Override
	public void readEntityFromNBT(EntityDool pFigure, CompoundNBT CompoundNBT) {
	}

	@Override
	public void writeEntityToNBT(EntityDool pFigure, CompoundNBT CompoundNBT) {
	}
}
