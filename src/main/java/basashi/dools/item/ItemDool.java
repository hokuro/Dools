package basashi.dools.item;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

import basashi.dools.core.Dools;
import basashi.dools.core.log.ModLog;
import basashi.dools.entity.EntityDool;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EntityType;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.IRegistry;
import net.minecraft.world.World;

public class ItemDool extends Item {
	public static final String NAME="dool";
	public static Map<String,Entity> entityStringMap = new TreeMap<String,Entity>();
	public static EntityDool entDool;
	public static ItemStack firstPerson;

	public ItemDool(Item.Properties property){
		super(property);
	}

	@Override
    //public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ){
	public EnumActionResult onItemUse(ItemUseContext context) {
		ModLog.log().debug("start");
		if (context.getWorld().isAirBlock(context.getPos().add(0, 1, 0)) || context.getHitZ() <= 0.5D){
			double x, y, z;
			if (context.getHitX() == 1){
				x = context.getPos().getX() + context.getHitX();
				y = context.getPos().getY() + context.getHitY();
				z = context.getPos().getZ() + context.getHitZ();
			}else{
				x = context.getPos().getX() + 0.5D;
				y = context.getPos().getY() + 1.0D;
				z = context.getPos().getZ() + 0.5D;
			}
			ItemStack stack = context.getItem();
			float lyaw = (180F - context.getPlayer().getRotationYawHead())%360F;
			EntityLivingBase lelb = getEntityFromItemStack(stack, context.getWorld());
			if(stack.getDamage()>0){
				ModLog.log().debug("id "+stack.getDamage());
				if(!context.getWorld().isRemote){
					try{
						EntityDool lc = new EntityDool(context.getWorld(),stack.getTag().getString("DoolName"));
						lc.setPositionAndRotation(x,y,z,lyaw, 0F);
						context.getWorld().spawnEntity(lc);
						context.getWorld().playSound(context.getPlayer(), new BlockPos(context.getPlayer().posX,context.getPlayer().posY,context.getPlayer().posZ),
								SoundEvents.BLOCK_STONE_PLACE, SoundCategory.BLOCKS ,0.5F, 0.4F / ((new Random()).nextFloat() * 0.4F + 0.8F));
					}catch(Exception e){
						e.printStackTrace();
					}
				}
			}else{
				ModLog.log().debug("id 0");
				if (entDool == null) {
		            entDool = Dools.getEntityMob(null);
				}
				entDool.setWorld(context.getWorld());
				entDool.setPositionAndRotation(x,y,z,lyaw,0F);
		        if (!context.getWorld().isRemote) {
		        	Dools.proxy.openGuiSelect(context.getPlayer(), context.getWorld());
		        }
			}
			stack.shrink(1);
		}
		ModLog.log().debug("end");
		return EnumActionResult.FAIL;
	}



	/**
	 * ItemStackに関連付けられているEntityを返す。<br>
	 * 対象となるEntityが存在しない場合はItemDamageを０に設定し適当なEntityを返す。
	 * @param pItemStack
	 * @return
	 */
	public static EntityLivingBase getEntityFromItemStack(ItemStack pItemStack, World world) {
		if (pItemStack.hasTag()) {
			if (pItemStack.getTag().hasKey("DoolName")) {
				String ls = pItemStack.getTag().getString("DoolName");
				EntityType et = EntityType.getById(ls);
				if (et != null ) {
					Entity le = et.create(world);
					if (le instanceof EntityLivingBase) {
						pItemStack.setDamage(le.getEntityId());
						return (EntityLivingBase)le;
					}
				}
			}
		}

		pItemStack.setDamage(0);
		return EntityType.ZOMBIE.create(world);
	}

	@Override
	public void fillItemGroup(ItemGroup tab, NonNullList<ItemStack> subItems){
		if (tab != this.getGroup()){return;}
	  	// Creativeタブに追加するアイテム
		subItems.add(new ItemStack(Dools.itemdool, 1));

		List<ModelResourceLocation> resource = new ArrayList<ModelResourceLocation>();
		IRegistry.field_212629_r.forEach((etype)->{
			Class cls = etype.getEntityClass();
			boolean lp = true;
			while(lp) {
				if (cls == EntityLivingBase.class) {
					ItemStack stack = new ItemStack(Dools.itemdool,1);
					NBTTagCompound nbt = stack.getOrCreateTag();
					nbt.setString("DoolName", EntityType.getId(etype).toString());
					subItems.add(stack);
					lp = false;
				}else if (cls == Entity.class || cls == null) {
					lp = false;
				}
				cls = cls.getSuperclass();
			}
		});
	}

	@Override
	protected String getDefaultTranslationKey() {
		return "Dool";
	}

	@Override
	public String getTranslationKey(ItemStack stack) {
		if (stack.hasTag() && stack.getTag().hasKey("DoolName")) {
			String name = stack.getTag().getString("DoolName");
			return "Dool_"+ new ResourceLocation(name).getPath();
		}
		return getDefaultTranslationKey();
	}
}