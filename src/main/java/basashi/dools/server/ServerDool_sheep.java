package basashi.dools.server;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import basashi.dools.entity.EntityDool;
import basashi.dools.gui.GuiDoolPause_sheep;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.item.DyeColor;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.TranslationTextComponent;

public class ServerDool_sheep extends ServerDool {
	@Override
 	public void sendData(EntityDool pFigure, DataOutput pData) throws IOException {
		super.sendData(pFigure, pData);
		SheepEntity lentity = (SheepEntity)pFigure.renderEntity;
		pData.writeInt(lentity.getFleeceColor().getId());
		pData.writeBoolean(lentity.getSheared());
		pData.writeBoolean(lentity.hasCustomName());
	}

	@Override
 	public void reciveData(EntityDool pFigure, DataInput pData) throws IOException {
		super.reciveData(pFigure, pData);
		SheepEntity lentity = (SheepEntity)pFigure.renderEntity;
		lentity.setFleeceColor((DyeColor.values()[pData.readInt()]));
		lentity.setSheared(pData.readBoolean());
		if (pData.readBoolean()){
			lentity.setCustomName(new TranslationTextComponent(GuiDoolPause_sheep.customname));
		}else{
			lentity.setCustomName(null);
		}
	}

	@Override
	public void readEntityFromNBT(EntityDool pFigure, CompoundNBT CompoundNBT) {
	}

	@Override
	public void writeEntityToNBT(EntityDool pFigure, CompoundNBT CompoundNBT) {
	}
}
