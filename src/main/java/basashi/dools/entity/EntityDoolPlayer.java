package basashi.dools.entity;

import java.util.UUID;

import basashi.dools.core.Dools;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.ThreadDownloadImageData;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class EntityDoolPlayer extends EntityMob {
	public static final UUID AlexUUID = new UUID(0,1);
	public static final UUID SteevUUID = new UUID(0,0);
	public static final UUID[] UUIDS = new UUID[]{SteevUUID,AlexUUID};
	public static final String NAME = "DoolPlayer";

	public String skinUser;
	public int skinSlim = 0;
	public boolean isHat;
	public boolean isJacket;
	public boolean isLeftLeg;
	public boolean isRightLeg;
	public boolean isLeftSleeve;
	public boolean isRightSleeve;
	public boolean isSpectator;
	public boolean isCape;

	private ThreadDownloadImageData fskinDownload;
	private ResourceLocation fskinResorce;


	public EntityDoolPlayer(World world) {
		super(Dools.RegistryEvents.DOOLPLAYER, world);
		skinUser = null;
		skinSlim = 0;
		isHat = true;
		isJacket = true;
		isLeftLeg = true;
		isRightLeg = true;
		isLeftSleeve = true;
		isRightSleeve = true;
		isSpectator = false;
		isCape = true;
		setURLSkin();
	}

	@Override
	public void readAdditional(NBTTagCompound nbttagcompound) {
		super.readAdditional(nbttagcompound);
		skinUser = nbttagcompound.getString("SkinUser");
		skinSlim = nbttagcompound.getInt("SkinKind");
		isHat = nbttagcompound.getBoolean("WareHat");
		isJacket = nbttagcompound.getBoolean("WareJacket");
		isLeftLeg = nbttagcompound.getBoolean("WareLeftLeg");
		isRightLeg = nbttagcompound.getBoolean("WareRightLeg");
		isLeftSleeve = nbttagcompound.getBoolean("WareLeftSleev");
		isRightSleeve = nbttagcompound.getBoolean("WareRightSleev");
		isCape  = nbttagcompound.getBoolean("WareCape");
		isSpectator = nbttagcompound.getBoolean("Spector");
		setURLSkin();
	}

	@Override
	public void writeAdditional(NBTTagCompound nbttagcompound) {
		super.writeAdditional(nbttagcompound);
		if (skinUser == null || skinUser.isEmpty()) {
			nbttagcompound.setString("SkinUser", "");
		} else {
			nbttagcompound.setString("SkinUser", skinUser);
		}
		nbttagcompound.setInt("SkinKind", skinSlim);
		nbttagcompound.setBoolean("WareHat",isHat);
		nbttagcompound.setBoolean("WareJacket",isJacket);
		nbttagcompound.setBoolean("WareLeftLeg",isLeftLeg);
		nbttagcompound.setBoolean("WareRightLeg",isRightLeg);
		nbttagcompound.setBoolean("WareLeftSleev",isLeftSleeve);
		nbttagcompound.setBoolean("WareRightSleev",isRightSleeve);
		nbttagcompound.setBoolean("WareCape",isCape);
		nbttagcompound.setBoolean("Spector",isSpectator);
	}

	public ResourceLocation setURLSkin() {
		// URLスキンを有効にする
		if (!(Minecraft.getInstance() != null))
			return DefaultPlayerSkin.getDefaultSkin(UUIDS[skinSlim]);
		if (skinUser != null && !skinUser.isEmpty()) {
			fskinResorce = Dools.proxy.getSkinUrl(skinUser);
			fskinDownload = Dools.proxy.getDownloadImageSkin(this.fskinResorce, skinUser, skinSlim);
		} else {
			UUID playerUUID;
			fskinResorce = DefaultPlayerSkin.getDefaultSkin(UUIDS[skinSlim]);
		}
		return fskinResorce;
	}

	public ThreadDownloadImageData func_110309_l() {
		return this.fskinDownload;
	}

	public ResourceLocation getLocationSkin(){
		return setURLSkin();
	}

	public boolean isSlim(){
		return skinSlim==0?false:true;
	}

	public void setSlim(int value){
		skinSlim = value;
	}
}
