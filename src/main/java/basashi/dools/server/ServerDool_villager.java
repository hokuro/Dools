package basashi.dools.server;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import basashi.dools.entity.EntityDool;
import net.minecraft.entity.merchant.villager.VillagerData;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.merchant.villager.VillagerProfession;
import net.minecraft.entity.villager.IVillagerType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.registry.Registry;

public class ServerDool_villager extends ServerDool {
	@Override
 	public void sendData(EntityDool pFigure, DataOutput pData) throws IOException {
		super.sendData(pFigure, pData);
		VillagerEntity lentity = (VillagerEntity)pFigure.renderEntity;
		VillagerData data = lentity.getVillagerData();
		IVillagerType vtype = data.getType();
		int idx_type = Registry.VILLAGER_TYPE.getId(vtype);

		VillagerProfession profession = data.getProfession();
		int idx_profession = Registry.VILLAGER_PROFESSION.getId(profession);

		pData.writeBoolean(lentity.isChild());
		pData.writeInt(idx_type);
		pData.writeInt(idx_profession);
		pData.writeInt(data.getLevel());
	}

	@Override
 	public void reciveData(EntityDool pFigure, DataInput pData) throws IOException {
		super.reciveData(pFigure, pData);
		VillagerEntity lentity = (VillagerEntity)pFigure.renderEntity;
		lentity.setGrowingAge(pData.readBoolean()?-1:1);
		int idx_type = pData.readInt();
		IVillagerType vtype = Registry.VILLAGER_TYPE.getByValue(idx_type);

		int idx_profession = pData.readInt();
		VillagerProfession profession = Registry.VILLAGER_PROFESSION.getByValue(idx_profession);

		VillagerData data = lentity.getVillagerData();
		data = data.withType(vtype).withProfession(profession).withLevel(pData.readInt());
		lentity.setVillagerData(data);
	}

	@Override
	public void readEntityFromNBT(EntityDool pFigure, CompoundNBT CompoundNBT) {
	}

	@Override
	public void writeEntityToNBT(EntityDool pFigure, CompoundNBT CompoundNBT) {
	}
}
