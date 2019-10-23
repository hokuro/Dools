package basashi.dools.server;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import basashi.dools.entity.EntityDool;
import net.minecraft.entity.monster.GhastEntity;
import net.minecraft.nbt.CompoundNBT;

public class ServerDool_ghast extends ServerDool {
	@Override
 	public void sendData(EntityDool pFigure, DataOutput pData) throws IOException {
		super.sendData(pFigure, pData);
		GhastEntity lentity = (GhastEntity)pFigure.renderEntity;
		pData.writeBoolean(lentity.isAttacking());
	}

	@Override
 	public void reciveData(EntityDool pFigure, DataInput pData) throws IOException {
		super.reciveData(pFigure, pData);
		GhastEntity lentity = (GhastEntity)pFigure.renderEntity;
		lentity.setAttacking(pData.readBoolean());
	}

	@Override
	public void readEntityFromNBT(EntityDool pFigure, CompoundNBT CompoundNBT) {
		((GhastEntity)pFigure.renderEntity).setAttacking(CompoundNBT.getBoolean("dw16"));
	}

	@Override
	public void writeEntityToNBT(EntityDool pFigure, CompoundNBT CompoundNBT) {
		boolean b = ((GhastEntity)pFigure.renderEntity).isAttacking();
		CompoundNBT.putBoolean("dw16", b);
	}
}
