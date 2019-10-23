package basashi.dools.server;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import basashi.dools.entity.EntityDool;
import basashi.dools.gui.GuiDoolPause_llama;
import net.minecraft.block.Block;
import net.minecraft.block.CarpetBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.horse.AbstractHorseEntity;
import net.minecraft.entity.passive.horse.LlamaEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

public class ServerDool_llama extends ServerDool {
	@Override
 	public void sendData(EntityDool pFigure, DataOutput pData) throws IOException {
		super.sendData(pFigure, pData);
		LlamaEntity lentity = (LlamaEntity)pFigure.renderEntity;
		pData.writeBoolean(lentity.hasChest());
		pData.writeInt(lentity.getVariant());
		pData.writeInt(lentity.getColor() == null?0:lentity.getColor().getId()+1);
	}

	@Override
 	public void reciveData(EntityDool pFigure, DataInput pData) throws IOException {
		super.reciveData(pFigure, pData);
		LlamaEntity lentity = (LlamaEntity)pFigure.renderEntity;
		lentity.setChested(pData.readBoolean());
		lentity.setVariant(pData.readInt());

		DataParameter<Integer> DATA_VARIANT_ID = ObfuscationReflectionHelper.getPrivateValue(LlamaEntity.class, lentity, "DATA_COLOR_ID");
		EntityDataManager dataManager = ObfuscationReflectionHelper.getPrivateValue(Entity.class, lentity, "dataManager");
		IInventory inv = (IInventory)ObfuscationReflectionHelper.getPrivateValue(AbstractHorseEntity.class, lentity, "horseChest");
		int color = pData.readInt();
		CompoundNBT compound = new CompoundNBT();
		lentity.writeAdditional(compound);
		if (color == 0) {
			compound.remove("DecorItem");
			inv.setInventorySlotContents(1, ItemStack.EMPTY);
			dataManager.set(DATA_VARIANT_ID, -1);
		}else {
			ItemStack stack = GuiDoolPause_llama.colors[color];
			compound.put("DecorItem", stack.write(new CompoundNBT()));
			dataManager.set(DATA_VARIANT_ID, ((CarpetBlock)Block.getBlockFromItem(GuiDoolPause_llama.colors[color].getItem())).getColor().getId() );
		}
		lentity.readAdditional(compound);
	}

	@Override
	public void readEntityFromNBT(EntityDool pFigure, CompoundNBT CompoundNBT) {
		LlamaEntity lentity = (LlamaEntity)pFigure.renderEntity;
		IInventory inv = (IInventory)ObfuscationReflectionHelper.getPrivateValue(AbstractHorseEntity.class, lentity, "horseChest");
		int color = CompoundNBT.getInt("color");
		CompoundNBT compound = new CompoundNBT();
		lentity.writeAdditional(compound);
		if (color == 0) {
			compound.remove("DecorItem");
			inv.setInventorySlotContents(1, ItemStack.EMPTY);
		}else {
			ItemStack stack = GuiDoolPause_llama.colors[color];
			compound.put("DecorItem", stack.write(new CompoundNBT()));
		}
		lentity.readAdditional(compound);
	}

	@Override
	public void writeEntityToNBT(EntityDool pFigure, CompoundNBT CompoundNBT) {
		LlamaEntity lentity = (LlamaEntity)pFigure.renderEntity;
		int color = lentity.getColor() == null?0:lentity.getColor().getId()+1;
		CompoundNBT.putInt("color", color);
	}
}
