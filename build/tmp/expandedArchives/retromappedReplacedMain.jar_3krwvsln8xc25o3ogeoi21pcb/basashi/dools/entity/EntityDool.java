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
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
//import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;

public class EntityDool extends Entity implements IEntityAdditionalSpawnData {
    private static final DataParameter<Byte> Watch0 = EntityDataManager.<Byte>func_187226_a(Entity.class, DataSerializers.field_187191_a);

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
	}

	public EntityDool(World world, Entity entity) {
		this(world);

		if (entity == null || !(entity instanceof EntityLivingBase)) {
			entity = EntityList.func_75620_a("Zombie", world);
		}
		setRenderEntity((EntityLivingBase) entity);
		Dools.proxy.getGui(this);
	}

	public EntityDool(World world, int index) {
		this(world, EntityList.func_75616_a(index, world));
	}

	@Override
	protected boolean func_70041_e_() {
		return false;
	}


	@Override
	protected void func_70088_a() {
		//dataWatcher.addObject(5, (byte)-1);
		field_70180_af.func_187214_a(Watch0, (byte)-1);
	}

	@Override
	public AxisAlignedBB func_70114_g(Entity entity) {
		return entity.func_174813_aQ();
	}

	@Override
	public boolean func_70104_M() {
		return true;
	}

	@Override
	protected void func_70037_a(NBTTagCompound tagCompund) {
		String s = tagCompund.func_74779_i("mobString");
		zoom = tagCompund.func_74760_g("zoom");
		health = tagCompund.func_74765_d("Health");
		additionalYaw = tagCompund.func_74760_g("additionalYaw");
		if (s != null) {
			Entity lentity = EntityList.func_75620_a(
					tagCompund.func_74779_i("mobString"), field_70170_p);
			if (lentity == null || !(lentity instanceof EntityLivingBase)) {
				// 存在しないMOB
				System.out.println(String.format("figua-lost:%s",
						tagCompund.func_74779_i("mobString")));
				Dools.proxy.initEntitys();
				lentity = (Entity) ItemDool.entityStringMap.values().toArray()[0];
			}
			if (lentity instanceof EntityLivingBase) {
				EntityLivingBase lel = (EntityLivingBase)lentity;
				lel.func_70020_e(tagCompund.func_74775_l("Entity"));
				//lel.getDataWatcher().updateObject(0, tagCompund.getByte("DataWatcher0"));
				lel.func_184212_Q().func_187227_b((DataParameter<Byte>)Dools.getPrivateValue(Entity.class, null, "FLAGS"),tagCompund.func_74771_c("DataWatcher0"));
				lel.field_70127_C = tagCompund.func_74760_g("prevPitch");
				lel.field_70126_B = tagCompund.func_74760_g("prevYaw");
				lel.field_70758_at = lel.field_70759_as = lel.field_70126_B;
				isFigureRide=tagCompund.func_74767_n("isFigureRide");
				setRenderEntity(lel);
			}
			fyOffset = tagCompund.func_74760_g("yOffset");
			Dools.getServerFigure(this).readEntityFromNBT(this, tagCompund);
			Dools.getServerFigure(this).setRotation(this);
		}
	}

	@Override
	protected void func_70014_b(NBTTagCompound tagCompound) {
		if (mobString == null)
			mobString = "";
		tagCompound.func_74778_a("mobString", mobString);
		tagCompound.func_74776_a("zoom", zoom);
		tagCompound.func_74777_a("Health", (byte) health);
		tagCompound.func_74776_a("additionalYaw", additionalYaw);
		NBTTagCompound lnbt = new NBTTagCompound();
		if (renderEntity != null) {
			renderEntity.func_70109_d(lnbt);
			//tagCompound.setByte("DataWatcher0", renderEntity.getDataWatcher().getWatchableObjectByte(0));
			tagCompound.func_74774_a("DataWatcher0", renderEntity.func_184212_Q().func_187225_a((DataParameter<Byte>)Dools.getPrivateValue(Entity.class, null, "FLAGS")));
			tagCompound.func_74776_a("prevPitch", renderEntity.field_70127_C);
			tagCompound.func_74776_a("prevYaw", renderEntity.field_70126_B);
			tagCompound.func_74776_a("yOffset", fyOffset);
			tagCompound.func_74757_a("isFigureRide", isFigureRide);
			Dools.getServerFigure(this).writeEntityToNBT(this, tagCompound);
		}
		(( Map<String, NBTBase>)Dools.getPrivateValue(NBTTagCompound.class, tagCompound, "tagMap")).put("Entity", lnbt);
	}

	public boolean hasRenderEntity() {
		return renderEntity != null;
	}

	@Override
	public boolean func_70097_a(DamageSource damagesource, float i) {
		Entity entity = damagesource.func_76346_g();
		if (field_70170_p.field_72995_K || field_70128_L) {
			return true;
		}
		func_70018_K();
		if (entity instanceof EntityPlayer) {
			if (this.func_184218_aH()){this.func_184210_p();}
			ItemStack lis = new ItemStack(Dools.dool, 1, mobIndex);
			if (!lis.func_77942_o()) {
				lis.func_77982_d(new NBTTagCompound());
			}
			NBTTagCompound lnbt = lis.func_77978_p();
			lnbt.func_74778_a("FigureName", mobString);
			func_70099_a(lis, 0.0F);
			func_70106_y();
		} else {
			health -= i;
			if (health <= 0) {
				func_70106_y();
			}
		}
		return true;
	}

	@Override
	public boolean func_70067_L() {
		return !field_70128_L;
	}

	@Override
	 public void func_180426_a(double x, double y, double z, float yaw, float pitch, int posRotationIncrements, boolean p_180426_10_){
		this.func_70107_b(x,y, z);
		this.func_70101_b(yaw, pitch);
	}

	@Override
	public void func_70071_h_() {
		super.func_70071_h_();
		if (!isFirst && field_70170_p.field_72995_K) {
			// サーバーへ固有データの要求をする
			Dools.proxy.getDoolData(this);
			isFirst = true;
		}

		field_70169_q = field_70165_t;
		field_70167_r = field_70163_u;
		field_70166_s = field_70161_v;

		// 落下分
		field_70181_x -= 0.080000000000000002D;
		field_70181_x *= 0.98000001907348633D;

		// 地上判定
		if (field_70122_E) {
			field_70159_w *= 0.5D;
			field_70181_x *= 0.5D;
			field_70179_y *= 0.5D;
		}
		func_145771_j(field_70165_t, (this.func_174813_aQ().field_72338_b + this.func_174813_aQ().field_72337_e) / 2D, field_70161_v);
		func_70091_d(field_70159_w, field_70181_x, field_70179_y);

		// 速度減衰
		field_70159_w *= 0.99000000953674316D;
		field_70181_x *= 0.94999998807907104D;
		field_70179_y *= 0.99000000953674316D;

		// 当り判定
		List list = field_70170_p.func_72839_b(this,
				func_174813_aQ().func_72314_b(0.02D, 0.0D, 0.02D));
		if (list != null && list.size() > 0) {
			for (int j1 = 0; j1 < list.size(); j1++) {
				Entity entity = (Entity) list.get(j1);
				if (entity.func_70104_M()) {
					entity.func_70108_f(this);
				}
			}
		}
		if (renderEntity != null) {
			func_70052_a(2, renderEntity.func_184218_aH());
			renderEntity.func_70107_b(field_70165_t, field_70163_u, field_70161_v);
		}
	}

	@Override
	//public boolean interactFirst(EntityPlayer entityplayer) {
	public EnumActionResult func_184199_a(EntityPlayer player, Vec3d vec, ItemStack stack, EnumHand hand){
		if (field_70170_p.field_72995_K) {
			// Client
			Dools.proxy.openGuiPause(player, this, Minecraft.func_71410_x().field_71441_e);
		}
		return EnumActionResult.SUCCESS;
	}


	@Override
	public void writeSpawnData(ByteBuf buffer) {
		buffer.writeFloat(field_70177_z);
		buffer.writeFloat(field_70125_A);
		buffer.writeInt(mobString.getBytes().length);
		buffer.writeBytes(mobString.getBytes());//     writeUTF(mobString);
	}

	@Override
	public void readSpawnData(ByteBuf additionalData) {
		Entity lentity;
		func_70101_b(additionalData.readFloat(), additionalData.readFloat());
		int len = additionalData.readInt();
		byte[] buf = new byte[len];
		additionalData.readBytes(buf);
		lentity = EntityList.func_75620_a(new String(buf), field_70170_p);
		setRenderEntity((EntityLivingBase) lentity);
		Dools.proxy.getGui(this);
	}

	public void setRenderEntity(EntityLivingBase entity) {
		renderEntity = entity;
		if (renderEntity != null) {
			renderEntity.func_70029_a(field_70170_p);
			mobString = EntityList.func_75621_b(renderEntity);
			mobIndex = EntityList.func_75619_a(renderEntity);
			setZoom(zoom);
		}
	}


	public void setZoom(float z) {
		if (z == 0) {
			z = 4F;
		}
		zoom = z;
		field_70130_N = renderEntity.field_70130_N / zoom;
		field_70131_O = renderEntity.field_70131_O / zoom;
		func_70105_a(field_70130_N, field_70131_O);
		func_70107_b(field_70165_t, field_70163_u, field_70161_v);
		renderEntity.func_184227_b(zoom);
	}

	public static void update_dool(){
	}


	protected void func_184200_o(Entity passenger){

	}

	protected void func_184225_p(Entity passenger){

	}


}
