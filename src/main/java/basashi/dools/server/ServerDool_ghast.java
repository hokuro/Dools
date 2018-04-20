package basashi.dools.server;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import basashi.dools.entity.EntityDool;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.nbt.NBTTagCompound;

public class ServerDool_ghast extends ServerDool {

	@Override
	public void sendData(EntityDool pFigure, DataOutput pData)
			throws IOException {
		super.sendData(pFigure, pData);
		EntityGhast lentity = (EntityGhast)pFigure.renderEntity;
		pData.writeBoolean(lentity.isAttacking());
	}

	@Override
	public void reciveData(EntityDool pFigure, DataInput pData)
			throws IOException {
		super.reciveData(pFigure, pData);
		EntityGhast lentity = (EntityGhast)pFigure.renderEntity;
		lentity.setAttacking(pData.readBoolean());
	}

	@Override
	public void readEntityFromNBT(EntityDool pFigure, NBTTagCompound nbttagcompound) {
		((EntityGhast)pFigure.renderEntity).setAttacking(nbttagcompound.getBoolean("dw16"));
	}

	@Override
	public void writeEntityToNBT(EntityDool pFigure, NBTTagCompound nbttagcompound) {
		boolean b = ((EntityGhast)pFigure.renderEntity).isAttacking();
		nbttagcompound.setBoolean("dw16", b);
	}

}
