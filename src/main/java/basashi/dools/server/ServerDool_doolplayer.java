package basashi.dools.server;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import basashi.dools.entity.EntityDool;
import basashi.dools.entity.EntityDoolPlayer;

public class ServerDool_doolplayer extends ServerDool {

	@Override
	public void sendData(EntityDool pFigure, DataOutput pData)
			throws IOException {
		EntityDoolPlayer lentity = (EntityDoolPlayer)pFigure.renderEntity;
		if (lentity.skinUser != null) {
			pData.writeUTF(lentity.skinUser);
		} else {
			pData.writeUTF("");
		}
		pData.writeInt(lentity.isSlim()?1:0);
		pData.writeBoolean(lentity.isHat);
		pData.writeBoolean(lentity.isJacket);
		pData.writeBoolean(lentity.isLeftLeg);
		pData.writeBoolean(lentity.isRightLeg);
		pData.writeBoolean(lentity.isLeftSleeve);
		pData.writeBoolean(lentity.isRightSleeve);
		pData.writeBoolean(lentity.isSpectator);
	}

	@Override
	public void reciveData(EntityDool pFigure, DataInput pData)
			throws IOException {
		EntityDoolPlayer lentity = (EntityDoolPlayer)pFigure.renderEntity;
		lentity.skinUser = pData.readUTF();
		lentity.setURLSkin();
		lentity.setSlim(pData.readInt());
		lentity.isHat=pData.readBoolean();
		lentity.isJacket=pData.readBoolean();
		lentity.isLeftLeg=pData.readBoolean();
		lentity.isRightLeg=pData.readBoolean();
		lentity.isLeftSleeve=pData.readBoolean();
		lentity.isRightSleeve=pData.readBoolean();
		lentity.isSpectator=pData.readBoolean();
	}

}
