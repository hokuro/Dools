package basashi.dools.server;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import basashi.dools.core.Dools;
import basashi.dools.entity.EntityDool;
import net.minecraft.entity.monster.EntityEvoker;
import net.minecraft.entity.monster.EntitySpellcasterIllager;
import net.minecraft.entity.monster.EntitySpellcasterIllager.SpellType;

public class ServerDool_evocation_illager extends ServerDool {
	@Override
	public void sendData(EntityDool pFigure, DataOutput pData)
			throws IOException {
		EntityEvoker lentity = (EntityEvoker)pFigure.renderEntity;

		pData.writeBoolean(lentity.isSpellcasting());
	}
//
	@Override
	public void reciveData(EntityDool pFigure, DataInput pData)
			throws IOException {
		EntityEvoker lentity = (EntityEvoker)pFigure.renderEntity;
		if (pData.readBoolean()){
			lentity.setSpellType(SpellType.DISAPPEAR);
			Dools.setPrivateValue(EntitySpellcasterIllager.class, lentity, 10, "spellTicks");
		}else{
			lentity.setSpellType(SpellType.NONE);
			Dools.setPrivateValue(EntitySpellcasterIllager.class, lentity, 0, "spellTicks");
		}
	}
}
