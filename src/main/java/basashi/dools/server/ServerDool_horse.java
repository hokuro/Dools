package basashi.dools.server;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import basashi.dools.entity.EntityDool;
import basashi.dools.gui.GuiDoolPause_horse;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.nbt.NBTTagCompound;

public class ServerDool_horse extends ServerDool {
	@Override
	public void sendData(EntityDool pFigure, DataOutput pData)
			throws IOException {
		EntityHorse lentity = (EntityHorse)pFigure.renderEntity;
		pData.writeBoolean(lentity.isBreeding());
		pData.writeBoolean(lentity.isHorseSaddled());
		pData.writeInt(Math.max(GuiDoolPause_horse.hoseTYpe.indexOf(lentity.getHorseArmorType()),0));
		pData.writeInt(lentity.getHorseVariant());
	}

	@Override
	public void reciveData(EntityDool pFigure, DataInput pData)
			throws IOException {
		EntityHorse lentity = (EntityHorse)pFigure.renderEntity;
		lentity.setBreeding(pData.readBoolean());
		lentity.setHorseSaddled(pData.readBoolean());
		int idx = pData.readInt();
		lentity.setHorseArmorStack(GuiDoolPause_horse.iarmor[idx]);
		lentity.setHorseVariant(pData.readInt());
	}

	@Override
	public void readEntityFromNBT(EntityDool pFigure, NBTTagCompound nbttagcompound) {
		EntityHorse lentity = (EntityHorse)pFigure.renderEntity;
		lentity.setHorseSaddled(nbttagcompound.getBoolean("saddl"));
		lentity.setHorseArmorStack(GuiDoolPause_horse.iarmor[nbttagcompound.getInt("armor")]);
	}

	@Override
	public void writeEntityToNBT(EntityDool pFigure, NBTTagCompound nbttagcompound) {
		EntityHorse lentity = (EntityHorse)pFigure.renderEntity;
		nbttagcompound.setBoolean("saddl", lentity.isHorseSaddled());
		nbttagcompound.setInt("armor", Math.max(GuiDoolPause_horse.hoseTYpe.indexOf(lentity.getHorseArmorType()),0));

	}

}
