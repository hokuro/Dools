package basashi.dools.server;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import basashi.dools.entity.EntityDool;
import net.minecraft.block.Block;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.nbt.NBTTagCompound;

public class ServerDool_enderman extends ServerDool {

	@Override
	public void sendData(EntityDool pFigure, DataOutput pData)
			throws IOException {
		super.sendData(pFigure, pData);
		EntityEnderman lentity = (EntityEnderman)pFigure.renderEntity;
		//if (lentity.getHeldBlockState() == null){
		if (lentity.func_195405_dq() == null){
			pData.writeBoolean(false);
		}else{
			pData.writeBoolean(true);
			//pData.writeInt(Block.getIdFromBlock(lentity.getHeldBlockState().getBlock()));
			//pData.writeInt(IRegistry.field_212618_g.getId(lentity.func_195405_dq().getBlock()));
			pData.writeInt(Block.getStateId(lentity.func_195405_dq()));
		}
		pData.writeBoolean(lentity.isScreaming());
	}

	@Override
	public void reciveData(EntityDool pFigure, DataInput pData)
			throws IOException {
		super.reciveData(pFigure, pData);
		EntityEnderman lentity = (EntityEnderman)pFigure.renderEntity;
		if (pData.readBoolean()){
			lentity.func_195406_b(Block.getStateById(pData.readInt()));
		}
		if (pData.readBoolean()){
			lentity.setAttackTarget(lentity);
		}else{
			lentity.setAttackTarget(null);
		}
	}

	@Override
	public void readEntityFromNBT(EntityDool pFigure, NBTTagCompound nbttagcompound) {
		if (nbttagcompound.getBoolean("Attacking")){// != ((EntityEnderman)pFigure.renderEntity).isScreaming()){
			((EntityEnderman)pFigure.renderEntity).setAttackTarget(pFigure.renderEntity);
		}else{
			((EntityEnderman)pFigure.renderEntity).setAttackTarget(null);
		}
	}

	@Override
	public void writeEntityToNBT(EntityDool pFigure, NBTTagCompound nbttagcompound) {
		nbttagcompound.setBoolean("Attacking", ((EntityEnderman)pFigure.renderEntity).isScreaming());
	}

}
