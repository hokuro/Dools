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
		pData.writeInt(Block.getIdFromBlock(lentity.getHeldBlockState().getBlock()));
		pData.writeBoolean(lentity.isScreaming());
	}

	@Override
	public void reciveData(EntityDool pFigure, DataInput pData)
			throws IOException {
		super.reciveData(pFigure, pData);
		EntityEnderman lentity = (EntityEnderman)pFigure.renderEntity;
		lentity.setHeldBlockState(Block.getStateById(pData.readInt()));
		lentity.notifyDataManagerChange((DataParameter<Boolean>)Dools.getPrivateValue(EntityEnderman.class, null, "SCREAMING"));
		//setScreaming(pData.readBoolean());
	}

	@Override
	public void readEntityFromNBT(EntityDool pFigure, NBTTagCompound nbttagcompound) {
		//((EntityEnderman)pFigure.renderEntity).setScreaming(nbttagcompound.getBoolean("Attacking"));
		if (nbttagcompound.getBoolean("Attacking") != ((EntityEnderman)pFigure.renderEntity).isScreaming()){
			pFigure.renderEntity.notifyDataManagerChange((DataParameter<Boolean>)Dools.getPrivateValue(EntityEnderman.class, null, "SCREAMING"));
		}
	}

	public void writeEntityToNBT(EntityDool pFigure, NBTTagCompound nbttagcompound) {
		nbttagcompound.setBoolean("Attacking", ((EntityEnderman)pFigure.renderEntity).isScreaming());
	}

}
