package basashi.dools.server;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import basashi.dools.entity.EntityDool;
import net.minecraft.entity.monster.ShulkerEntity;
import net.minecraft.item.DyeColor;
import net.minecraft.nbt.CompoundNBT;

public class ServerDool_shulker extends ServerDool {
	@Override
 	public void sendData(EntityDool pFigure, DataOutput pData) throws IOException {
		super.sendData(pFigure, pData);
		ShulkerEntity entity = (ShulkerEntity) pFigure.renderEntity;
		pData.writeInt(entity.getColor()==null?DyeColor.WHITE.getId():entity.getColor().getId());
		pData.writeInt(entity.getPeekTick());
	}

	@Override
 	public void reciveData(EntityDool pFigure, DataInput pData) throws IOException {
		super.reciveData(pFigure, pData);
		ShulkerEntity entity = (ShulkerEntity) pFigure.renderEntity;
		int color = pData.readInt();
		int peek = pData.readInt();
		CompoundNBT nbt = new CompoundNBT();
		entity.writeAdditional(nbt);
		nbt.putInt("Color", color);
		nbt.putInt("Peek", peek);
		entity.readAdditional(nbt);
	}

	@Override
	public void readEntityFromNBT(EntityDool pFigure, CompoundNBT CompoundNBT) {
	}

	@Override
	public void writeEntityToNBT(EntityDool pFigure, CompoundNBT CompoundNBT) {
	}
}
