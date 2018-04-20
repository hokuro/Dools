package basashi.dools.server;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.UUID;

import basashi.dools.entity.EntityDool;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.item.EnumDyeColor;

public class ServerDool_wolf extends ServerDool {

	@Override
	public void sendData(EntityDool pFigure, DataOutput pData)
			throws IOException {
		super.sendData(pFigure, pData);
		EntityWolf lentity = (EntityWolf)pFigure.renderEntity;
		pData.writeFloat(lentity.getHealth());
		pData.writeBoolean(lentity.isSitting());
		int lf = (lentity.isTamed() ? 1 : 0) |
				(lentity.isAngry() ? 2 : 0);
		pData.writeByte(lf);
		pData.writeInt(lentity.getCollarColor().getDyeDamage());
	}

	@Override
	public void reciveData(EntityDool pFigure, DataInput pData)
			throws IOException {
		super.reciveData(pFigure, pData);
		EntityWolf lentity = (EntityWolf)pFigure.renderEntity;
		lentity.setHealth(pData.readFloat());
		lentity.setSilent(pData.readBoolean());
		int lf = pData.readByte();
		lentity.setTamed((lf & 1) != 0);
		lentity.setAngry((lf & 2) != 0);
		lentity.setCollarColor(EnumDyeColor.byDyeDamage(pData.readInt()));
		if (lentity.isTamed()){
			lentity.setOwnerId(new UUID(1,1));
		}else{
			lentity.setOwnerId(null);
		}
	}

}