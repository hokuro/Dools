package basashi.dools.entity;

import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import basashi.dools.config.ConfigValue;
import basashi.dools.core.Dools;
import basashi.dools.core.log.ModLog;
import basashi.dools.item.ItemDool;
import basashi.dools.network.MessageHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntitySize;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.network.FMLPlayMessages;
import net.minecraftforge.fml.network.NetworkHooks;

public class EntityDool extends Entity implements IEntityAdditionalSpawnData{
	public static final String NAME = "entitydool";
	public LivingEntity renderEntity;
	public float zoom;
	public String mobString;
	private int health;
	public Method afterrender;
	public float additionalYaw;
	public byte changeCount;
	public float fyOffset;
	protected boolean isFirst = false;
	public boolean isFigureRide = false;
	public boolean isMove = true;

	public EntityDool(World worldIn) {
		this(Dools.RegistryEvents.DOOL, worldIn);
	}

	public EntityDool(EntityType<?> etype, World world) {
		super(etype,world);
		health = 5;
		additionalYaw = 0.0F;
		changeCount = -1;
		fyOffset = 0F;
		zoom = ConfigValue.general.DefaultZoomRate();
		mobString = "";
	}

	public EntityDool(FMLPlayMessages.SpawnEntity packet, World world) {
		this(world);
	}

	public EntityDool(World world, Entity entity) {
		this(world);
		if (entity == null || !(entity instanceof LivingEntity)) {
			entity = EntityType.create(Registry.ENTITY_TYPE.getId(EntityType.ZOMBIE), world);
		}
		setRenderEntity((LivingEntity) entity);
		this.mobString = entity.getType().getRegistryName().toString();
	}

	@Override
	protected boolean canTriggerWalking() {
		return false;
	}

	@Override
	protected void registerData() {
	}

	@Override
	public AxisAlignedBB getCollisionBox(Entity entity) {
		return entity.getBoundingBox();
	}

	@Override
	public boolean canBePushed() {
		return true;
	}

	@Override
	protected void readAdditional(CompoundNBT tagCompund) {
		String s = tagCompund.getString("mobString");
		zoom = tagCompund.getFloat("zoom");
		health = tagCompund.getShort("Health");
		additionalYaw = tagCompund.getFloat("additionalYaw");
		isMove = tagCompund.getBoolean("isMove");
		if (s != null) {
			String nameKey = tagCompund.getString("mobString");
			mobString = checkMobString(nameKey);
			EntityType<?> etype = Registry.ENTITY_TYPE.getOrDefault(new ResourceLocation(mobString));
			Entity lentity = etype.create(world);
			if (lentity == null || !(lentity instanceof LivingEntity)) {
				ModLog.log().info("missing create: " + mobString);
				this.remove();
			}

			LivingEntity lel = (LivingEntity)lentity;
			lel.readAdditional(tagCompund.getCompound("Entity"));
			lel.getDataManager().set((DataParameter<Byte>)ObfuscationReflectionHelper.getPrivateValue(Entity.class, null, "FLAGS"),tagCompund.getByte("DataWatcher0"));
			lel.prevRotationPitch = tagCompund.getFloat("prevPitch");
			lel.prevRotationYaw = tagCompund.getFloat("prevYaw");
			lel.prevRotationYawHead = lel.rotationYawHead = lel.prevRotationYaw;
			isFigureRide=tagCompund.getBoolean("isFigureRide");
			setRenderEntity(lel);

			fyOffset = tagCompund.getFloat("yOffset");
			Dools.getServerFigure(this).readEntityFromNBT(this, tagCompund);
			Dools.getServerFigure(this).setRotation(this);
		}
	}

	private String checkMobString(String name) {
		String ret = EntityType.ZOMBIE.getRegistryName().toString();
		Iterator<EntityType<?>> it = Registry.ENTITY_TYPE.iterator();
		while(it.hasNext()) {
			EntityType<?> et = it.next();
			if (et.getRegistryName().toString().equals(name)) {
				ret = name;
			}
		}
		return ret;
	}

	@Override
	protected void writeAdditional(CompoundNBT tagCompound) {
		if (mobString == null) {
			mobString = EntityType.ZOMBIE.getRegistryName().toString();
		}
		tagCompound.putString("mobString", mobString);
		tagCompound.putFloat("zoom", zoom);
		tagCompound.putShort("Health", (byte) health);
		tagCompound.putFloat("additionalYaw", additionalYaw);
		tagCompound.putBoolean("isMove", this.isMove);
		CompoundNBT lnbt = new CompoundNBT();
		if (renderEntity != null) {
			renderEntity.writeAdditional(lnbt);
			tagCompound.putByte("DataWatcher0", renderEntity.getDataManager().get((DataParameter<Byte>)ObfuscationReflectionHelper.getPrivateValue(Entity.class, null, "FLAGS")));
			tagCompound.putFloat("prevPitch", renderEntity.prevRotationPitch);
			tagCompound.putFloat("prevYaw", renderEntity.prevRotationYaw);
			tagCompound.putFloat("yOffset", fyOffset);
			tagCompound.putBoolean("isFigureRide", isFigureRide);
			Dools.getServerFigure(this).writeEntityToNBT(this, tagCompound);
		}
		(( Map<String, INBT>)ObfuscationReflectionHelper.getPrivateValue(CompoundNBT.class, tagCompound, "tagMap")).put("Entity", lnbt);
	}

	public boolean hasRenderEntity() {
		return renderEntity != null;
	}

	@Override
	public boolean attackEntityFrom(DamageSource damagesource, float i) {
		Entity entity = damagesource.getTrueSource();
		if (world.isRemote || !this.isAlive()) {
			return true;
		}
		markVelocityChanged();
		if (entity instanceof PlayerEntity) {
			if (this.isPassenger()){this.stopRiding();}
			ItemStack lis = new ItemStack(Dools.item_dool);
			ItemDool.setLivingName(lis, this.mobString);
			ItemDool.setLivingNBT(lis, this.renderEntity);

			entityDropItem(lis, 0.0F);
			remove();
		} else {
			health -= i;
			if (health <= 0) {
				remove();
			}
		}
		return true;
	}

	@Override
	public boolean canBeCollidedWith() {
		return this.isAlive();
	}

	@Override
	 public void setPositionAndRotationDirect(double x, double y, double z, float yaw, float pitch, int posRotationIncrements, boolean p_180426_10_){
		this.setPosition(x,y, z);
		this.setRotation(yaw, pitch);
	}

	@Override
	public void tick() {
		super.tick();
		if (world.isRemote) {
			if (!this.hasRenderEntity()) {
			}else if (!isFirst) {
				// iサーバーへ固有データの要求をする
				MessageHandler.Send_MessageDoolUpdte(this.getEntityId());
				isFirst = true;
			}
		}

		prevPosX = posX;
		prevPosY = posY;
		prevPosZ = posZ;
		double mx = this.getMotion().getX();
		double my = this.getMotion().getY();
		double mz = this.getMotion().getZ();

		// i落下分
		my -= 0.080000000000000002D;
		my *= 0.98000001907348633D;

		// i地上判定
		if (onGround) {
			mx *= 0.5D;
			my *= 0.5D;
			mz *= 0.5D;
		}
		pushOutOfBlocks(posX, (this.getBoundingBox().minY + this.getBoundingBox().maxY) / 2D, posZ);
		move(MoverType.PLAYER, new Vec3d(mx, my, mz));

		// i速度減衰
		mx *= 0.99000000953674316D;
		my *= 0.94999998807907104D;
		mz *= 0.99000000953674316D;
		setMotion(mx,my,mz);

		// i当り判定
		List list = world.getEntitiesWithinAABBExcludingEntity(this, getBoundingBox().expand(0.02D, 0.0D, 0.02D));
		if (list != null && list.size() > 0) {
			for (int j1 = 0; j1 < list.size(); j1++) {
				Entity entity = (Entity) list.get(j1);
				if (entity.canBePushed()) {
					entity.applyEntityCollision(this);
				}
			}
		}
		if (renderEntity != null) {
			setFlag(2, renderEntity.isPassenger());
			renderEntity.setPosition(posX, posY, posZ);
			if (isMove) {
				renderEntity.ticksExisted = this.ticksExisted;
			}
		}
	}

	@Override
	public ActionResultType applyPlayerInteraction(PlayerEntity player, Vec3d vec, Hand hand){
		Dools.proxy.openGuiPause(this);
		//setZoom(ConfigValue.general.getNextZoomRate(zoom));
		return ActionResultType.SUCCESS;
	}


	@Override
	public void writeSpawnData(PacketBuffer buffer) {
		buffer.writeFloat(rotationYaw);
		buffer.writeFloat(rotationPitch);
		buffer.writeInt(mobString.getBytes().length);
		buffer.writeBytes(mobString.getBytes());//     writeUTF(mobString);
	}

	@Override
	public void readSpawnData(PacketBuffer additionalData) {
		setRotation(additionalData.readFloat(), additionalData.readFloat());
		int len = additionalData.readInt();
		byte[] buf = new byte[len];
		additionalData.readBytes(buf);
		String nameKey = new String(buf);
		mobString = checkMobString(nameKey);

		Entity lentity = Registry.ENTITY_TYPE.getOrDefault(new ResourceLocation(mobString)).create(world);
		if (lentity == null || !(lentity instanceof LivingEntity)) {
			ModLog.log().info("missing create: " + mobString);
			this.remove();
		}
		setRenderEntity((LivingEntity) lentity);;
	}

	public void setRenderEntity(LivingEntity entity) {
		renderEntity = entity;
		if (renderEntity != null) {
			renderEntity.setWorld(world);
			mobString = renderEntity.getType().getRegistryName().toString();
			setZoom(zoom);
		}
	}

	public void setZoom(float z) {
		if (z == 0) {
			z = 4F;
		}
		zoom = z;
		float width = renderEntity.getWidth() / zoom;
		float height = renderEntity.getHeight() / zoom;
		ObfuscationReflectionHelper.setPrivateValue(Entity.class, this, EntitySize.flexible(width, height), "size");
		setPosition(posX, posY, posZ);
		renderEntity.setRenderDistanceWeight(zoom);
	}

	public static void update_dool(){
	}


	protected void addPassenger(Entity passenger){

	}

	protected void removePassenger(Entity passenger){

	}

	@Override
	public IPacket<?> createSpawnPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}
}
