package basashi.dools.server;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import basashi.dools.entity.EntityDool;
import net.minecraft.entity.monster.EvokerEntity;
import net.minecraft.entity.monster.SpellcastingIllagerEntity.SpellType;
import net.minecraft.nbt.CompoundNBT;

public class ServerDool_evoker extends ServerDool {
	@Override
 	public void sendData(EntityDool pFigure, DataOutput pData) throws IOException {
		super.sendData(pFigure, pData);
		EvokerEntity lentity = (EvokerEntity)pFigure.renderEntity;
		pData.writeBoolean(lentity.isSpellcasting());
	}

	@Override
 	public void reciveData(EntityDool pFigure, DataInput pData) throws IOException {
		super.reciveData(pFigure, pData);
		EvokerEntity lentity = (EvokerEntity)pFigure.renderEntity;
		if (pData.readBoolean()){
			lentity.setSpellType(SpellType.DISAPPEAR);
			CompoundNBT tag = new CompoundNBT();
			lentity.writeAdditional(tag);
			tag.putInt("SpellTicks",10);
			lentity.readAdditional(tag);
		}else{
			lentity.setSpellType(SpellType.NONE);
			CompoundNBT tag = new CompoundNBT();
			lentity.writeAdditional(tag);
			tag.putInt("SpellTicks",0);
			lentity.readAdditional(tag);
		}
	}

	@Override
	public void readEntityFromNBT(EntityDool pFigure, CompoundNBT CompoundNBT) {
	}

	@Override
	public void writeEntityToNBT(EntityDool pFigure, CompoundNBT CompoundNBT) {
	}
}
