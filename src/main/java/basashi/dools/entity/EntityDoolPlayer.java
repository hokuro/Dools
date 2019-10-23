package basashi.dools.entity;

import java.util.UUID;

import javax.annotation.Nullable;

import basashi.dools.core.Dools;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.play.NetworkPlayerInfo;
import net.minecraft.client.renderer.texture.DownloadingTexture;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.FMLPlayMessages;
import net.minecraftforge.fml.network.NetworkHooks;

public class EntityDoolPlayer extends MobEntity {
	public static final UUID AlexUUID = new UUID(0,1);
	public static final UUID SteevUUID = new UUID(0,0);
	public static final UUID[] UUIDS = new UUID[]{SteevUUID,AlexUUID};
	public static final String NAME = "entitydoolplayer";
	public double prevChasingPosX;
	public double prevChasingPosY;
	public double prevChasingPosZ;
	public double chasingPosX;
	public double chasingPosY;
	public double chasingPosZ;
	public float prevCameraYaw;
	public float cameraYaw;

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
	public CompoundNBT isLeftSholderEntity;
	public CompoundNBT isRightSholderEntity;

	private DownloadingTexture fskinDownload;
	private ResourceLocation fskinResorce;

	public EntityDoolPlayer(EntityType<? extends EntityDoolPlayer> etype, World world) {
		super(etype,world);
	}

	public EntityDoolPlayer(FMLPlayMessages.SpawnEntity packet, World world) {
		this(world);
	}


	public EntityDoolPlayer(World world) {
		this(Dools.RegistryEvents.DOOLPLAYER, world);
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
		isLeftSholderEntity = new CompoundNBT();
		isRightSholderEntity = new CompoundNBT();
		setURLSkin();
	}

	@Override
	public void readAdditional(CompoundNBT CompoundNBT) {
		super.readAdditional(CompoundNBT);
		skinUser = CompoundNBT.getString("SkinUser");
		skinSlim = CompoundNBT.getInt("SkinKind");
		isHat = CompoundNBT.getBoolean("WareHat");
		isJacket = CompoundNBT.getBoolean("WareJacket");
		isLeftLeg = CompoundNBT.getBoolean("WareLeftLeg");
		isRightLeg = CompoundNBT.getBoolean("WareRightLeg");
		isLeftSleeve = CompoundNBT.getBoolean("WareLeftSleev");
		isRightSleeve = CompoundNBT.getBoolean("WareRightSleev");
		isCape  = CompoundNBT.getBoolean("WareCape");
		isSpectator = CompoundNBT.getBoolean("Spector");
		isLeftSholderEntity = CompoundNBT.getCompound("isLeftSholderEntity");
		isRightSholderEntity = CompoundNBT.getCompound("isRightSholderEntity");
		setURLSkin();
	}

	@Override
	public void writeAdditional(CompoundNBT CompoundNBT) {
		super.writeAdditional(CompoundNBT);
		if (skinUser == null || skinUser.isEmpty()) {
			CompoundNBT.putString("SkinUser", "");
		} else {
			CompoundNBT.putString("SkinUser", skinUser);
		}
		CompoundNBT.putInt("SkinKind", skinSlim);
		CompoundNBT.putBoolean("WareHat",isHat);
		CompoundNBT.putBoolean("WareJacket",isJacket);
		CompoundNBT.putBoolean("WareLeftLeg",isLeftLeg);
		CompoundNBT.putBoolean("WareRightLeg",isRightLeg);
		CompoundNBT.putBoolean("WareLeftSleev",isLeftSleeve);
		CompoundNBT.putBoolean("WareRightSleev",isRightSleeve);
		CompoundNBT.putBoolean("WareCape",isCape);
		CompoundNBT.putBoolean("Spector",isSpectator);
		CompoundNBT.put("isLeftSholderEntity",isLeftSholderEntity==null?new CompoundNBT():isLeftSholderEntity);
		CompoundNBT.put("isRightSholderEntity",isRightSholderEntity==null?new CompoundNBT():isRightSholderEntity);
	}

	public ResourceLocation setURLSkin() {
		// URLスキンを有効にする
		if (!(Minecraft.getInstance() != null))
			return DefaultPlayerSkin.getDefaultSkin(UUIDS[skinSlim]);
		if (skinUser != null && !skinUser.isEmpty()) {
			fskinResorce = Dools.proxy.getSkinUrl(skinUser.toLowerCase());
			fskinDownload = Dools.proxy.getDownloadImageSkin(this.fskinResorce, skinUser, skinSlim);
		} else {
			UUID playerUUID;
			fskinResorce = DefaultPlayerSkin.getDefaultSkin(UUIDS[skinSlim]);
		}
		return fskinResorce;
	}

	public DownloadingTexture func_110309_l() {
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

	@Override
	public IPacket<?> createSpawnPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}

	@Override
	public void tick() {
		super.tick();
		this.updateCape();
	}

	@Override
	public void livingTick() {
		this.prevCameraYaw = this.cameraYaw;
		float f;
		if (this.onGround && !(this.getHealth() <= 0.0F) && !this.isSwimming()) {
			f = Math.min(0.1F, MathHelper.sqrt(func_213296_b(this.getMotion())));
		} else {
			f = 0.0F;
		}
		this.cameraYaw += (f - this.cameraYaw) * 0.4F;
	}

	public void updateRidden() {
		if (!this.world.isRemote && this.isSneaking() && this.isPassenger()) {
		} else {
			this.prevCameraYaw = this.cameraYaw;
			this.cameraYaw = 0.0F;
		}
	}

	private void updateCape() {
		this.prevChasingPosX = this.chasingPosX;
		this.prevChasingPosY = this.chasingPosY;
		this.prevChasingPosZ = this.chasingPosZ;
		double d0 = this.posX - this.chasingPosX;
		double d1 = this.posY - this.chasingPosY;
		double d2 = this.posZ - this.chasingPosZ;
		double d3 = 10.0D;
		if (d0 > 10.0D) {
			this.chasingPosX = this.posX;
			this.prevChasingPosX = this.chasingPosX;
		}

		if (d2 > 10.0D) {
			this.chasingPosZ = this.posZ;
			this.prevChasingPosZ = this.chasingPosZ;
		}

		if (d1 > 10.0D) {
			this.chasingPosY = this.posY;
			this.prevChasingPosY = this.chasingPosY;
		}

		if (d0 < -10.0D) {
			this.chasingPosX = this.posX;
			this.prevChasingPosX = this.chasingPosX;
		}

		if (d2 < -10.0D) {
			this.chasingPosZ = this.posZ;
			this.prevChasingPosZ = this.chasingPosZ;
		}

		if (d1 < -10.0D) {
			this.chasingPosY = this.posY;
			this.prevChasingPosY = this.chasingPosY;
		}

		this.chasingPosX += d0 * 0.25D;
		this.chasingPosZ += d2 * 0.25D;
		this.chasingPosY += d1 * 0.25D;
	}

	private NetworkPlayerInfo playerInfo;
	@Nullable
	protected NetworkPlayerInfo getPlayerInfo() {
		if (this.playerInfo == null) {
			this.playerInfo = Minecraft.getInstance().getConnection().getPlayerInfo(Minecraft.getInstance().player.getUniqueID());
		}
		return this.playerInfo;
	}

	@Nullable
	public ResourceLocation getLocationCape() {
		NetworkPlayerInfo networkplayerinfo = this.getPlayerInfo();
		return networkplayerinfo == null ? null : networkplayerinfo.getLocationCape();
	}

	public CompoundNBT getLeftShoulderEntity() {
		if (isLeftSholderEntity != null) {
			return isLeftSholderEntity;
		}
		return new CompoundNBT();
	}

	public CompoundNBT getRightShoulderEntity() {
		if (isLeftSholderEntity != null) {
			return isRightSholderEntity;
		}
		return new CompoundNBT();
	}

}
