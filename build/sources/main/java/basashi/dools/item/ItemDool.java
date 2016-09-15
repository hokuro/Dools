package basashi.dools.item;

import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.TreeMap;

import basashi.dools.core.Dools;
import basashi.dools.core.log.ModLog;
import basashi.dools.entity.EntityDool;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;

public class ItemDool extends Item {
	public static final String NAME="dool";
	public static Map<String,Entity> entityStringMap = new TreeMap<String,Entity>();
	public static EntityDool entDool;
	public static ItemStack firstPerson;

	public ItemDool(){
		super();
		maxStackSize = 16;
		setHasSubtypes(true);
		setMaxDamage(0);
		setCreativeTab(Dools.tabsDool);
		this.setRegistryName(NAME);
		this.setUnlocalizedName(NAME);
	}

	@Override
    //public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ){
	public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ){
		ModLog.log().debug("start");
		if (worldIn.isAirBlock(pos.add(0, 1, 0)) || hitZ <= 0.5D){
			double x, y, z;
			if (hitX == 1){
				x = pos.getX() + hitX;
				y = pos.getY() + hitY;
				z = pos.getZ() + hitZ;
			}else{
				x = pos.getX() + 0.5D;
				y = pos.getY() + 1.0D;
				z = pos.getZ() + 0.5D;
			}
			float lyaw = (180F - playerIn.getRotationYawHead())%360F;
			EntityLivingBase lelb = getEntityFromItemStack(stack);
			if(stack.getItemDamage()>0){
				ModLog.log().debug("id "+stack.getItemDamage());
				if(!worldIn.isRemote){
					try{
						EntityDool lc = new EntityDool(worldIn,stack.getItemDamage());
						lc.setPositionAndRotation(x,y,z,lyaw, 0F);
						worldIn.spawnEntityInWorld(lc);
						worldIn.playSound(playerIn, new BlockPos(playerIn.posX,playerIn.posY,playerIn.posZ),
								SoundEvents.block_stone_place, SoundCategory.BLOCKS ,0.5F, 0.4F / ((new Random()).nextFloat() * 0.4F + 0.8F));
					}catch(Exception e){
						e.printStackTrace();
					}
				}
			}else{
				ModLog.log().debug("id 0");
				if(worldIn.isRemote){
					entDool.setWorld(worldIn);
					entDool.setPositionAndRotation(x,y,z,lyaw,0F);
					Dools.proxy.openGuiSelect(playerIn, worldIn);
				}
			}
			stack.stackSize--;
		}
		ModLog.log().debug("end");
		return EnumActionResult.FAIL;
	}



	@Override
	public String getUnlocalizedName(ItemStack itemstack) {
		if (itemstack.getItemDamage() != 0) {
			String ls;
			if (itemstack.hasTagCompound() && itemstack.getTagCompound().hasKey("DoolName")) {
				ls = itemstack.getTagCompound().getString("DoolName");
			} else {
				ls = EntityList.classToStringMapping.get(EntityList.getClassFromID(itemstack.getItemDamage()));
				//ls = EntityList.getStringFromID(itemstack.getItemDamage());
			}
			if (ls != null) {
				String ret = (new StringBuilder()).append(super.getUnlocalizedName()).append(".").append(ls).toString();
				return ret;
			} else {
				itemstack.setItemDamage(0);
			}
		}
		return super.getUnlocalizedName();
	}

	@Override
	public void getSubItems(Item itemIn, CreativeTabs tab, List<ItemStack> subItems){
	  	// Creativeタブに追加するアイテム
		subItems.add(new ItemStack(Dools.dool, 1));
		Map<Integer, Class> lmap = null;
		try {
			lmap = (Map<Integer, Class>)Dools.getPrivateValue(EntityList.class, null, "idToClassMapping");
		} catch (Exception e) {

		}
		if (lmap != null) {
			for (Entry<Integer, Class> le : lmap.entrySet()) {
				Class lcl = le.getValue();
				if (!Modifier.isAbstract(lcl.getModifiers()) && EntityLivingBase.class.isAssignableFrom(lcl)) {
					subItems.add(new ItemStack(Dools.dool, 1, le.getKey()));
				}
			}
		}
	}

	/**
	 * ItemStackに関連付けられているEntityを返す。<br>
	 * 対象となるEntityが存在しない場合はItemDamageを０に設定し適当なEntityを返す。
	 * @param pItemStack
	 * @return
	 */
	public static EntityLivingBase getEntityFromItemStack(ItemStack pItemStack) {
		Dools.proxy.initEntitys();
		String ls = EntityList.classToStringMapping.get(EntityList.getClassFromID(pItemStack.getItemDamage()));
		//String ls = EntityList.getStringFromID(pItemStack.getItemDamage());
		if (ls == null || !entityStringMap.containsKey(ls)) {
			if (pItemStack.hasTagCompound()) {
				if (pItemStack.getTagCompound().hasKey("DoolName")) {
					ls = pItemStack.getTagCompound().getString("DoolName");
					if (entityStringMap.containsKey(ls)) {
						EntityLivingBase le = (EntityLivingBase)entityStringMap.get(ls);
						pItemStack.setItemDamage(EntityList.getEntityID(le));
						return le;
					}
				}
			}
			pItemStack.setItemDamage(0);
			return (EntityLivingBase)entityStringMap.values().iterator().next();
		}

		return (EntityLivingBase)entityStringMap.get(ls);
	}

	@Override
    public String getItemStackDisplayName(ItemStack stack)
    {
		String[] spl = this.getUnlocalizedNameInefficiently(stack).split("\\.");
        return spl[spl.length-1].replace("Entity", "");
    }

    /**
     * Translates the unlocalized name of this item, but without the .name suffix, so the translation fails and the
     * unlocalized name itself is returned.
     */
	@Override
    public String getUnlocalizedNameInefficiently(ItemStack stack)
    {
        String s = this.getUnlocalizedName(stack);
        return s == null ? "" : I18n.translateToLocal(s);
    }
}