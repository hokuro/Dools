package basashi.dools.entity;

import java.util.UUID;

import basashi.dools.core.Dools;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ThreadDownloadImageData;
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
		super(world);
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
	public void func_70037_a(NBTTagCompound nbttagcompound) {
		super.func_70037_a(nbttagcompound);
		skinUser = nbttagcompound.func_74779_i("SkinUser");
		skinSlim = nbttagcompound.func_74762_e("SkinKind");
		isHat = nbttagcompound.func_74767_n("WareHat");
		isJacket = nbttagcompound.func_74767_n("WareJacket");
		isLeftLeg = nbttagcompound.func_74767_n("WareLeftLeg");
		isRightLeg = nbttagcompound.func_74767_n("WareRightLeg");
		isLeftSleeve = nbttagcompound.func_74767_n("WareLeftSleev");
		isRightSleeve = nbttagcompound.func_74767_n("WareRightSleev");
		isCape  = nbttagcompound.func_74767_n("WareCape");
		isSpectator = nbttagcompound.func_74767_n("Spector");
	}

	@Override
	public void func_70014_b(NBTTagCompound nbttagcompound) {
		super.func_70014_b(nbttagcompound);
		if (skinUser == null || skinUser.isEmpty()) {
			nbttagcompound.func_74778_a("SkinUser", "");
		} else {
			nbttagcompound.func_74778_a("SkinUser", skinUser);
		}
		nbttagcompound.func_74768_a("SkinKind", skinSlim);
		nbttagcompound.func_74757_a("WareHat",isHat);
		nbttagcompound.func_74757_a("WareJacket",isJacket);
		nbttagcompound.func_74757_a("WareLeftLeg",isLeftLeg);
		nbttagcompound.func_74757_a("WareRightLeg",isRightLeg);
		nbttagcompound.func_74757_a("WareLeftSleev",isLeftSleeve);
		nbttagcompound.func_74757_a("WareRightSleev",isRightSleeve);
		nbttagcompound.func_74757_a("WareCape",isCape);
		nbttagcompound.func_74757_a("Spector",isSpectator);
	}

	public void setURLSkin() {
		// URLスキンを有効にする
		if (!(Minecraft.func_71410_x() != null)) return;
		if (skinUser != null && !skinUser.isEmpty()) {
			fskinResorce = Dools.proxy.func_110311_f(skinUser);
			fskinDownload = Dools.proxy.getDownloadImageSkin(this.fskinResorce, skinUser, skinSlim);
		} else {
			UUID playerUUID;
			fskinResorce = DefaultPlayerSkin.func_177334_a(UUIDS[skinSlim]);
		}
	}

	public ThreadDownloadImageData func_110309_l() {
		return this.fskinDownload;
	}

	public ResourceLocation getLocationSkin(){
		return this.fskinResorce;
	}

	public boolean isSlim(){
		return skinSlim==0?false:true;
	}

	public void setSlim(int value){
		skinSlim = value;
	}
}
