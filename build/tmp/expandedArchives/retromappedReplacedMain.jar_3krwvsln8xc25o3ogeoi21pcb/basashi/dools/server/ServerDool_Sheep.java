package basashi.dools.server;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import basashi.dools.entity.EntityDool;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.item.EnumDyeColor;

public class ServerDool_Sheep extends ServerDool {

	@Override
	public void sendData(EntityDool pFigure, DataOutput pData)
			throws IOException {
		super.sendData(pFigure, pData);
		EntitySheep lentity = (EntitySheep)pFigure.renderEntity;
		pData.writeByte(lentity.func_175509_cj().func_176765_a());
		pData.writeBoolean(lentity.func_70892_o());
	}

	@Override
	public void reciveData(EntityDool pFigure, DataInput pData)
			throws IOException {
		super.reciveData(pFigure, pData);
		EntitySheep lentity = (EntitySheep)pFigure.renderEntity;
		lentity.func_175512_b((EnumDyeColor.func_176764_b(pData.readByte())));
		lentity.func_70893_e(pData.readBoolean());
	}

}
