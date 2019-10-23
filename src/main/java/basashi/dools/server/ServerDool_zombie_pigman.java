package basashi.dools.server;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import basashi.dools.entity.EntityDool;
import net.minecraft.entity.monster.ZombiePigmanEntity;
import net.minecraft.nbt.CompoundNBT;

public class ServerDool_zombie_pigman extends ServerDool {
	@Override
 	public void sendData(EntityDool pFigure, DataOutput pData) throws IOException {
		super.sendData(pFigure, pData);
		ZombiePigmanEntity entity = (ZombiePigmanEntity)pFigure.renderEntity;
		CompoundNBT nbt = new CompoundNBT();
		entity.writeAdditional(nbt);
		short angry = nbt.getShort("Anger");
		pData.writeShort(angry);
	}

	@Override
 	public void reciveData(EntityDool pFigure, DataInput pData) throws IOException {
		super.reciveData(pFigure, pData);
		ZombiePigmanEntity entity = (ZombiePigmanEntity)pFigure.renderEntity;
		int angar = pData.readShort();
		CompoundNBT nbt = new CompoundNBT();
		entity.writeAdditional(nbt);
		nbt.putShort("Anger", (short)angar);
		entity.readAdditional(nbt);
	}

	@Override
	public void readEntityFromNBT(EntityDool pFigure, CompoundNBT CompoundNBT) {
		ZombiePigmanEntity entity = (ZombiePigmanEntity)pFigure.renderEntity;
		short angry = CompoundNBT.getShort("Anger");
		CompoundNBT nbt = new CompoundNBT();
		entity.writeAdditional(nbt);
		nbt.putShort("Anger", (short)angry);
		entity.readAdditional(nbt);


	}

	@Override
	public void writeEntityToNBT(EntityDool pFigure, CompoundNBT CompoundNBT) {
		ZombiePigmanEntity entity = (ZombiePigmanEntity)pFigure.renderEntity;
		CompoundNBT nbt = new CompoundNBT();
		entity.writeAdditional(nbt);
		short angry = nbt.getShort("Anger");
		CompoundNBT.putShort("Anger", (short)angry);
	}
}
