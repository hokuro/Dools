package basashi.dools.server;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import basashi.dools.core.Dools;
import basashi.dools.entity.EntityDool;
import net.minecraft.block.Block;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;

public class ServerDool_Enderman extends ServerDool {

	@Override
	public void sendData(EntityDool pFigure, DataOutput pData)
			throws IOException {
		super.sendData(pFigure, pData);
		EntityEnderman lentity = (EntityEnderman)pFigure.renderEntity;
		pData.writeInt(Block.func_149682_b(lentity.func_175489_ck().func_177230_c()));
		pData.writeBoolean(lentity.func_70823_r());
	}

	@Override
	public void reciveData(EntityDool pFigure, DataInput pData)
			throws IOException {
		super.reciveData(pFigure, pData);
		EntityEnderman lentity = (EntityEnderman)pFigure.renderEntity;
		lentity.func_175490_a(Block.func_176220_d(pData.readInt()));
		lentity.func_184206_a((DataParameter<Boolean>)Dools.getPrivateValue(EntityEnderman.class, null, "SCREAMING"));
		//setScreaming(pData.readBoolean());
	}

	@Override
	public void readEntityFromNBT(EntityDool pFigure, NBTTagCompound nbttagcompound) {
		//((EntityEnderman)pFigure.renderEntity).setScreaming(nbttagcompound.getBoolean("Attacking"));
		if (nbttagcompound.func_74767_n("Attacking") != ((EntityEnderman)pFigure.renderEntity).func_70823_r()){
			pFigure.renderEntity.func_184206_a((DataParameter<Boolean>)Dools.getPrivateValue(EntityEnderman.class, null, "SCREAMING"));
		}
	}

	public void writeEntityToNBT(EntityDool pFigure, NBTTagCompound nbttagcompound) {
		nbttagcompound.func_74757_a("Attacking", ((EntityEnderman)pFigure.renderEntity).func_70823_r());
	}

}
