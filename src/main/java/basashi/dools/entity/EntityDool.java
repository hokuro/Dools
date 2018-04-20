package basashi.dools.entity;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import basashi.dools.config.ConfigValue;
import basashi.dools.core.Dools;
import basashi.dools.item.ItemDool;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
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
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;

public class EntityDool extends Entity implements IEntityAdditionalSpawnData{
    private static final DataParameter<Byte> Watch0 = EntityDataManager.<Byte>createKey(Entity.class, DataSerializers.BYTE);

	public static final String NAEM = "entitydool";
	public EntityLivingBase renderEntity;
	public float zoom;
	public String mobString;
	public int mobIndex;
	private int health;
	public Method afterrender;
	public float additionalYaw;
	public byte changeCount;
	public float fyOffset;
	protected boolean isFirst = false;
	public boolean isFigureRide = false;

	public EntityDool(World worldIn) {
		super(worldIn);
		health = 5;
		additionalYaw = 0.0F;
		changeCount = -1;
		fyOffset = 0F;
		zoom = ConfigValue.General.defaultZoomRate;
		mobString = "";
	}

	public EntityDool(World world, Entity entity) {
		this(world);

		if (entity == null || !(entity instanceof EntityLivingBase)) {
			entity = EntityList.createEntityByIDFromName(new ResourceLocation("Zombie"), world);
		}
		setRenderEntity((EntityLivingBase) entity);
		Dools.proxy.getGui(this);
	}

	public EntityDool(World world, int index) {
		this(world, EntityList.createEntityByID(index, world));
	}

	@Override
	protected boolean canTriggerWalking() {
		return false;
	}


	@Override
	protected void entityInit() {
		//dataWatcher.addObject(5, (byte)-1);
		this.dataManager.register(Watch0, (byte)-1);
	}

	@Override
	public AxisAlignedBB getCollisionBox(Entity entity) {
		return entity.getEntityBoundingBox();
	}

	@Override
	public boolean canBePushed() {
		return true;
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound tagCompund) {
		String s = tagCompund.getString("mobString");
		zoom = tagCompund.getFloat("zoom");
		health = tagCompund.getShort("Health");
		additionalYaw = tagCompund.getFloat("additionalYaw");
		if (s != null) {
			Entity lentity = EntityList.createEntityByIDFromName(
					new ResourceLocation(tagCompund.getString("mobString")), world);
			if (lentity == null || !(lentity instanceof EntityLivingBase)) {
				// 存在しないMOB
				System.out.println(String.format("figua-lost:%s",
						tagCompund.getString("mobString")));
				Dools.proxy.initEntitys();
				lentity = (Entity) ItemDool.entityStringMap.values().toArray()[0];
			}
			if (lentity instanceof EntityLivingBase) {
				EntityLivingBase lel = (EntityLivingBase)lentity;
				lel.readFromNBT(tagCompund.getCompoundTag("Entity"));
				//lel.getDataWatcher().updateObject(0, tagCompund.getByte("DataWatcher0"));
				lel.getDataManager().set((DataParameter<Byte>)Dools.getPrivateValue(Entity.class, null, "FLAGS"),tagCompund.getByte("DataWatcher0"));
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
	protected void writeEntityToNBT(NBTTagCompound tagCompound) {
		if (mobString == null)
			mobString = "";
		tagCompound.setString("mobString", mobString);
		tagCompound.setFloat("zoom", zoom);
		tagCompound.setShort("Health", (byte) health);
		tagCompound.setFloat("additionalYaw", additionalYaw);
		NBTTagCompound lnbt = new NBTTagCompound();
		if (renderEntity != null) {
			renderEntity.writeToNBT(lnbt);
			//tagCompound.setByte("DataWatcher0", renderEntity.getDataWatcher().getWatchableObjectByte(0));
			tagCompound.setByte("DataWatcher0", renderEntity.getDataManager().get((DataParameter<Byte>)Dools.getPrivateValue(Entity.class, null, "FLAGS")));
			tagCompound.setFloat("prevPitch", renderEntity.prevRotationPitch);
			tagCompound.setFloat("prevYaw", renderEntity.prevRotationYaw);
			tagCompound.setFloat("yOffset", fyOffset);
			tagCompound.setBoolean("isFigureRide", isFigureRide);
			Dools.getServerFigure(this).writeEntityToNBT(this, tagCompound);
		}
		(( Map<String, NBTBase>)Dools.getPrivateValue(NBTTagCompound.class, tagCompound, "tagMap")).put("Entity", lnbt);
	}

	public boolean hasRenderEntity() {
		return renderEntity != null;
	}

	@Override
	public boolean attackEntityFrom(DamageSource damagesource, float i) {
		Entity entity = damagesource.getTrueSource();
		if (world.isRemote || isDead) {
			return true;
		}
		markVelocityChanged();
		if (entity instanceof EntityPlayer) {
			if (this.isRiding()){this.dismountRidingEntity();}
			ItemStack lis = new ItemStack(Dools.dool, 1, mobIndex);
			if (!lis.hasTagCompound()) {
				lis.setTagCompound(new NBTTagCompound());
			}
			NBTTagCompound lnbt = lis.getTagCompound();
			lnbt.setString("FigureName", mobString);

			if (!lis.isEmpty()){
				Dools.instance.registerModel(lis);
			}
			entityDropItem(lis, 0.0F);
			setDead();
		} else {
			health -= i;
			if (health <= 0) {
				setDead();
			}
		}
		return true;
	}

	@Override
	public boolean canBeCollidedWith() {
		return !isDead;
	}

	@Override
	 public void setPositionAndRotationDirect(double x, double y, double z, float yaw, float pitch, int posRotationIncrements, boolean p_180426_10_){
		this.setPosition(x,y, z);
		this.setRotation(yaw, pitch);
	}

	@Override
	public void onUpdate() {
		super.onUpdate();
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
		pushOutOfBlocks(posX, (this.getEntityBoundingBox().minY + this.getEntityBoundingBox().maxY) / 2D, posZ);
		move(MoverType.PLAYER, motionX, motionY, motionZ);

		// 速度減衰
		motionX *= 0.99000000953674316D;
		motionY *= 0.94999998807907104D;
		motionZ *= 0.99000000953674316D;

		// 当り判定
		List list = world.getEntitiesWithinAABBExcludingEntity(this,
				getEntityBoundingBox().expand(0.02D, 0.0D, 0.02D));
		if (list != null && list.size() > 0) {
			for (int j1 = 0; j1 < list.size(); j1++) {
				Entity entity = (Entity) list.get(j1);
				if (entity.canBePushed()) {
					entity.applyEntityCollision(this);
				}
			}
		}
		if (renderEntity != null) {
			setFlag(2, renderEntity.isRiding());
			renderEntity.setPosition(posX, posY, posZ);
			renderEntity.ticksExisted = this.ticksExisted;
		}
	}

	@Override
	//public boolean interactFirst(EntityPlayer entityplayer) {
	public EnumActionResult applyPlayerInteraction(EntityPlayer player, Vec3d vec, EnumHand hand){
		if (world.isRemote) {
			// Client
			Dools.proxy.openGuiPause(player, this, Minecraft.getMinecraft().world);
		}
		return EnumActionResult.SUCCESS;
	}


	@Override
	public void writeSpawnData(ByteBuf buffer) {
		buffer.writeFloat(rotationYaw);
		buffer.writeFloat(rotationPitch);
		buffer.writeInt(mobString.getBytes().length);
		buffer.writeBytes(mobString.getBytes());//     writeUTF(mobString);
	}

	@Override
	public void readSpawnData(ByteBuf additionalData) {
		Entity lentity;
		setRotation(additionalData.readFloat(), additionalData.readFloat());
		int len = additionalData.readInt();
		byte[] buf = new byte[len];
		additionalData.readBytes(buf);
		lentity = EntityList.createEntityByIDFromName(new ResourceLocation(new String(buf)), world);
		setRenderEntity((EntityLivingBase) lentity);
		Dools.proxy.getGui(this);
	}

	public void setRenderEntity(EntityLivingBase entity) {
		renderEntity = entity;
		if (renderEntity != null) {
			renderEntity.setWorld(world);
			mobString = EntityList.getKey(renderEntity).toString();
			mobIndex = EntityList.getID(renderEntity.getClass());
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
