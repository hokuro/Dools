package basashi.dools.server;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import basashi.dools.entity.EntityDool;
import basashi.dools.gui.GuiDoolPause_llama;
import net.minecraft.entity.passive.EntityLlama;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class ServerDool_llama extends ServerDool {
	@Override
	public void sendData(EntityDool pFigure, DataOutput pData)
			throws IOException {
		EntityLlama lentity = (EntityLlama)pFigure.renderEntity;
		pData.writeBoolean(lentity.hasChest());
		pData.writeInt(lentity.getVariant());
		//pData.writeInt(lentity.getDataManager().get((DataParameter<Integer>)Dools.getPrivateValue(EntityLlama.class, null, "DATA_COLOR_ID")));
	}

	@Override
	public void reciveData(EntityDool pFigure, DataInput pData)
			throws IOException {
		EntityLlama lentity = (EntityLlama)pFigure.renderEntity;
		lentity.setChested(pData.readBoolean());
		lentity.setVariant(pData.readInt());
		//lentity.getDataManager().set((DataParameter<Integer>)Dools.getPrivateValue(EntityLlama.class, null, "DATA_COLOR_ID"),pData.readInt());

	}



	@Override
	public void readEntityFromNBT(EntityDool pFigure, NBTTagCompound nbttagcompound) {
		EntityLlama lentity = (EntityLlama)pFigure.renderEntity;
		int color = nbttagcompound.getInt("color");


		NBTTagCompound compound = new NBTTagCompound();
		lentity.writeAdditional(compound);
		if (color == 0) {
			compound.removeTag("DecorItem");
		}else {
			ItemStack stack = GuiDoolPause_llama.colors[color];
			compound.setTag("DecorItem", stack.write(new NBTTagCompound()));
		}
		lentity.readAdditional(compound);
		//lentity.getDataManager().set((DataParameter<Integer>)Dools.getPrivateValue(EntityLlama.class, null, "DATA_COLOR_ID"),color);
	}

	@Override
	public void writeEntityToNBT(EntityDool pFigure, NBTTagCompound nbttagcompound) {
		EntityLlama lentity = (EntityLlama)pFigure.renderEntity;
		int color = lentity.getColor() == null?0:lentity.getColor().getId()+1;
		nbttagcompound.setInt("color", color);
	}

}
