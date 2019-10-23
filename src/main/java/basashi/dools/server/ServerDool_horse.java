package basashi.dools.server;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import basashi.dools.entity.EntityDool;
import basashi.dools.gui.GuiDoolPause_horse;
import net.minecraft.entity.passive.horse.AbstractHorseEntity;
import net.minecraft.entity.passive.horse.HorseEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.HorseArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

public class ServerDool_horse extends ServerDool {
	@Override
 	public void sendData(EntityDool pFigure, DataOutput pData) throws IOException {
		super.sendData(pFigure, pData);
		HorseEntity lentity = (HorseEntity)pFigure.renderEntity;
		pData.writeBoolean(lentity.isBreeding());
		pData.writeBoolean(lentity.isHorseSaddled());
		ItemStack stack = lentity.func_213803_dV();
		int armorType = 0;
		if (stack.getItem() instanceof HorseArmorItem) {
			HorseArmorItem armor = (HorseArmorItem)stack.getItem();
			// iアーマータイプの取り出し(5:iron 7:gold 11:diamodn)
			armorType = armor.func_219977_e();
		}

		pData.writeInt(armorType);
		pData.writeInt(lentity.getHorseVariant());
	}

	@Override
 	public void reciveData(EntityDool pFigure, DataInput pData) throws IOException {
		super.reciveData(pFigure, pData);
		HorseEntity lentity = (HorseEntity)pFigure.renderEntity;
		boolean breeding = pData.readBoolean();
		boolean saddle = pData.readBoolean();
		lentity.setBreeding(breeding);

		IInventory inv = (IInventory)ObfuscationReflectionHelper.getPrivateValue(AbstractHorseEntity.class, lentity, "horseChest");
		CompoundNBT compound = new CompoundNBT();
		lentity.writeAdditional(compound);
	    if (saddle) {
	    	compound.put("SaddleItem", Items.SADDLE.getDefaultInstance().write(new CompoundNBT()));
	    }else {
	    	compound.remove("SaddleItem");
			inv.setInventorySlotContents(0, ItemStack.EMPTY);
	    }
	    lentity.readAdditional(compound);

		// iアーマーのタイプを取得
		int idx = pData.readInt();
		ItemStack armor = GuiDoolPause_horse.iarmor.get(idx);
		// NBTにアーマーの状態を書き込み
	    if (armor.isEmpty()) {
	    	compound.remove("ArmorItem");
			inv.setInventorySlotContents(1, ItemStack.EMPTY);
	    }else {
	    	compound.put("ArmorItem", armor.write(new CompoundNBT()));
	    }
	    // i馬に改造NBTを流し込む
	    lentity.readAdditional(compound);
		lentity.setHorseVariant(pData.readInt());
	}

	@Override
	public void readEntityFromNBT(EntityDool pFigure, CompoundNBT CompoundNBT) {
		HorseEntity lentity = (HorseEntity)pFigure.renderEntity;
		//lentity.setHorseSaddled(CompoundNBT.getBoolean("saddl"));
	}

	@Override
	public void writeEntityToNBT(EntityDool pFigure, CompoundNBT CompoundNBT) {
		HorseEntity lentity = (HorseEntity)pFigure.renderEntity;
		//CompoundNBT.putBoolean("saddl", lentity.isHorseSaddled());
	}
}
