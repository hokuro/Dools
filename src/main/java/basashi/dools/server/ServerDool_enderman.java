package basashi.dools.server;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import basashi.dools.entity.EntityDool;
import net.minecraft.block.Block;
import net.minecraft.entity.monster.EndermanEntity;
import net.minecraft.nbt.CompoundNBT;

public class ServerDool_enderman extends ServerDool {
	@Override
 	public void sendData(EntityDool pFigure, DataOutput pData) throws IOException {
		super.sendData(pFigure, pData);
		EndermanEntity lentity = (EndermanEntity)pFigure.renderEntity;
		if (lentity.getHeldBlockState() == null){
			pData.writeBoolean(false);
		}else{
			pData.writeBoolean(true);
			pData.writeInt(Block.getStateId(lentity.getHeldBlockState()));
		}
		pData.writeBoolean(lentity.isScreaming());
	}

	@Override
 	public void reciveData(EntityDool pFigure, DataInput pData) throws IOException {
		super.reciveData(pFigure, pData);
		EndermanEntity lentity = (EndermanEntity)pFigure.renderEntity;
		if (pData.readBoolean()){
			lentity.func_195406_b(Block.getStateById(pData.readInt()));
		}
		if (pData.readBoolean()){
			lentity.setAttackTarget(lentity);
		}else{
			lentity.setAttackTarget(null);
		}
	}

	@Override
	public void readEntityFromNBT(EntityDool pFigure, CompoundNBT CompoundNBT) {
		if (CompoundNBT.getBoolean("Attacking")){
			((EndermanEntity)pFigure.renderEntity).setAttackTarget(pFigure.renderEntity);
		}else{
			((EndermanEntity)pFigure.renderEntity).setAttackTarget(null);
		}
	}

	@Override
	public void writeEntityToNBT(EntityDool pFigure, CompoundNBT CompoundNBT) {
		CompoundNBT.putBoolean("Attacking", ((EndermanEntity)pFigure.renderEntity).isScreaming());
	}
}
