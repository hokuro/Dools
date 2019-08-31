package basashi.dools.entity;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import basashi.dools.config.ConfigValue;
import basashi.dools.core.Dools;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.INBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
//import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;

public class EntityDool extends Entity implements IEntityAdditionalSpawnData{
    private static final DataParameter<Byte> Watch0 = EntityDataManager.<Byte>createKey(Entity.class, DataSerializers.BYTE);

	public static final String NAEM = "entitydool";
	public EntityLivingBase renderEntity;
	public float zoom;
	public String mobString;
	//public int mobIndex;
	private int health;
	public Method afterrender;
	public float additionalYaw;
	public byte changeCount;
	public float fyOffset;
	protected boolean isFirst = false;
	public boolean isFigureRide = false;

	public EntityDool(World worldIn) {
		super(Dools.RegistryEvents.DOOL, worldIn);
		health = 5;
		additionalYaw = 0.0F;
		changeCount = -1;
		fyOffset = 0F;
		zoom = ConfigValue.general.DefaultZoomRate();
		mobString = "";
	}

	public EntityDool(World world, Entity entity) {
		this(world);

		if (entity == null || !(entity instanceof EntityLivingBase)) {
			entity = EntityType.create(world, EntityType.ZOMBIE.getRegistryName());
		}
		setRenderEntity((EntityLivingBase) entity);
		Dools.proxy.getGui(this);
		this.mobString = entity.getType().getRegistryName().toString();
	}

	public EntityDool(World world, String registreKey) {
		this(world, EntityType.getById(registreKey).create(world));
		this.mobString = registreKey;
	}

	@Override
	protected boolean canTriggerWalking() {
		return false;
	}

	@Override
	protected void registerData() {
		//dataWatcher.addObject(5, (byte)-1);
		this.dataManager.register(Watch0, (byte)-1);
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
	protected void readAdditional(NBTTagCompound tagCompund) {
		String s = tagCompund.getString("mobString");
		zoom = tagCompund.getFloat("zoom");
		health = tagCompund.getShort("Health");
		additionalYaw = tagCompund.getFloat("additionalYaw");
		if (s != null) {
			Entity lentity = EntityType.getById(tagCompund.getString("mobString")).create(world);
			if (lentity == null || !(lentity instanceof EntityLivingBase)) {
				// 存在しないMOB
				System.out.println(String.format("figua-lost:%s",
						tagCompund.getString("mobString")));
				lentity = EntityType.create(world,EntityType.ZOMBIE.getRegistryName());
			}
			if (lentity instanceof EntityLivingBase) {
				EntityLivingBase lel = (EntityLivingBase)lentity;
				lel.readAdditional(tagCompund.getCompound("Entity"));
				//lel.getDataWatcher().updateObject(0, tagCompund.getByte("DataWatcher0"));
				lel.getDataManager().set((DataParameter<Byte>)ObfuscationReflectionHelper.getPrivateValue(Entity.class, null, "FLAGS"),tagCompund.getByte("DataWatcher0"));
				lel.prevRotationPitch = tagCompund.getFloat("prevPitch");
				lel.prevRotationYaw = tagCompund.getFloat("prevYaw");
				lel.prevRotationYawHead = lel.rotationYawHead = lel.prevRotationYaw;
				isFigureRide=tagCompund.getBoolean("isFigureRide");
				setRenderEntity(lel);
			}
			fyOffset = tagCompund.getFloat("yOffset");
			Dools.getServerFigure(this).readEntityFromNBT(this, tagCompund);
			Dools.getServerFigure(this).setRotation(this);
		}
	}

	@Override
	protected void writeAdditional(NBTTagCompound tagCompound) {
		if (mobString == null)
			mobString = "";
		tagCompound.setString("mobString", mobString);
		tagCompound.setFloat("zoom", zoom);
		tagCompound.setShort("Health", (byte) health);
		tagCompound.setFloat("additionalYaw", additionalYaw);
		NBTTagCompound lnbt = new NBTTagCompound();
		if (renderEntity != null) {
			renderEntity.writeAdditional(lnbt);
			//tagCompound.setByte("DataWatcher0", renderEntity.getDataWatcher().getWatchableObjectByte(0));
			tagCompound.setByte("DataWatcher0", renderEntity.getDataManager().get((DataParameter<Byte>)ObfuscationReflectionHelper.getPrivateValue(Entity.class, null, "FLAGS")));
			tagCompound.setFloat("prevPitch", renderEntity.prevRotationPitch);
			tagCompound.setFloat("prevYaw", renderEntity.prevRotationYaw);
			tagCompound.setFloat("yOffset", fyOffset);
			tagCompound.setBoolean("isFigureRide", isFigureRide);
			Dools.getServerFigure(this).writeEntityToNBT(this, tagCompound);
		}
		(( Map<String, INBTBase>)ObfuscationReflectionHelper.getPrivateValue(NBTTagCompound.class, tagCompound, "tagMap")).put("Entity", lnbt);
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
		if (entity instanceof EntityPlayer) {
			if (this.isPassenger()){this.stopRiding();}
			ItemStack lis = new ItemStack(Dools.itemdool);
			if (!lis.hasTag()) {
				lis.setTag(new NBTTagCompound());
			}
			NBTTagCompound lnbt = lis.getTag();
			lnbt.setString("DoolName", mobString);

//			if (!lis.isEmpty()){
//				Dools.registerModel(lis);
//			}
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
		if (!isFirst && world.isRemote) {
			// サーバーへ固有データの要求をする
			Dools.proxy.getDoolData(this);
			isFirst = true;
		}

		prevPosX = posX;
		prevPosY = posY;
		prevPosZ = posZ;

		// 落下分
		motionY -= 0.080000000000000002D;
		motionY *= 0.98000001907348633D;

		// 地上判定
		if (onGround) {
			motionX *= 0.5D;
			motionY *= 0.5D;
			motionZ *= 0.5D;
		}
		pushOutOfBlocks(posX, (this.getBoundingBox().minY + this.getBoundingBox().maxY) / 2D, posZ);
		move(MoverType.PLAYER, motionX, motionY, motionZ);

		// 速度減衰
		motionX *= 0.99000000953674316D;
		motionY *= 0.94999998807907104D;
		motionZ *= 0.99000000953674316D;

		// 当り判定
		List list = world.getEntitiesWithinAABBExcludingEntity(this,
				getBoundingBox().expand(0.02D, 0.0D, 0.02D));
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
			renderEntity.ticksExisted = this.ticksExisted;
		}
	}

	@Override
	//public boolean interactFirst(EntityPlayer entityplayer) {
	public EnumActionResult applyPlayerInteraction(EntityPlayer player, Vec3d vec, EnumHand hand){
		Dools.proxy.openGuiPause(player, this, this.world);
		return EnumActionResult.SUCCESS;
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
		Entity lentity;
		setRotation(additionalData.readFloat(), additionalData.readFloat());
		int len = additionalData.readInt();
		byte[] buf = new byte[len];
		additionalData.readBytes(buf);
		lentity = EntityType.create(world,new ResourceLocation(new String(buf)));
		setRenderEntity((EntityLivingBase) lentity);
		Dools.proxy.getGui(this);
	}

	public void setRenderEntity(EntityLivingBase entity) {
		renderEntity = entity;
		if (renderEntity != null) {
			renderEntity.setWorld(world);
			mobString = renderEntity.getType().getRegistryName().toString();
			//mobIndex = EntityList.getID(renderEntity.getClass());
			setZoom(zoom);
		}
	}


	public void setZoom(float z) {
		if (z == 0) {
			z = 4F;
		}
		zoom = z;
		width = renderEntity.width / zoom;
		height = renderEntity.height / zoom;
		setSize(width, height);
		setPosition(posX, posY, posZ);
		renderEntity.setRenderDistanceWeight(zoom);
	}

	public static void update_dool(){
	}


	protected void addPassenger(Entity passenger){

	}

	protected void removePassenger(Entity passenger){

	}


}
