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
		field_77777_bU = 16;
		func_77627_a(true);
		func_77656_e(0);
		func_77637_a(Dools.tabsDool);
		this.setRegistryName(NAME);
		this.func_77655_b(NAME);
	}

	@Override
    //public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ){
	public EnumActionResult func_180614_a(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ){
		ModLog.log().debug("start");
		if (worldIn.func_175623_d(pos.func_177982_a(0, 1, 0)) || hitZ <= 0.5D){
			double x, y, z;
			if (hitX == 1){
				x = pos.func_177958_n() + hitX;
				y = pos.func_177956_o() + hitY;
				z = pos.func_177952_p() + hitZ;
			}else{
				x = pos.func_177958_n() + 0.5D;
				y = pos.func_177956_o() + 1.0D;
				z = pos.func_177952_p() + 0.5D;
			}
			float lyaw = (180F - playerIn.func_70079_am())%360F;
			EntityLivingBase lelb = getEntityFromItemStack(stack);
			if(stack.func_77952_i()>0){
				ModLog.log().debug("id "+stack.func_77952_i());
				if(!worldIn.field_72995_K){
					try{
						EntityDool lc = new EntityDool(worldIn,stack.func_77952_i());
						lc.func_70080_a(x,y,z,lyaw, 0F);
						worldIn.func_72838_d(lc);
						worldIn.func_184133_a(playerIn, new BlockPos(playerIn.field_70165_t,playerIn.field_70163_u,playerIn.field_70161_v),
								SoundEvents.field_187845_fY, SoundCategory.BLOCKS ,0.5F, 0.4F / ((new Random()).nextFloat() * 0.4F + 0.8F));
					}catch(Exception e){
						e.printStackTrace();
					}
				}
			}else{
				ModLog.log().debug("id 0");
				if(worldIn.field_72995_K){
					entDool.func_70029_a(worldIn);
					entDool.func_70080_a(x,y,z,lyaw,0F);
					Dools.proxy.openGuiSelect(playerIn, worldIn);
				}
			}
			stack.field_77994_a--;
		}
		ModLog.log().debug("end");
		return EnumActionResult.FAIL;
	}



	@Override
	public String func_77667_c(ItemStack itemstack) {
		if (itemstack.func_77952_i() != 0) {
			String ls;
			if (itemstack.func_77942_o() && itemstack.func_77978_p().func_74764_b("DoolName")) {
				ls = itemstack.func_77978_p().func_74779_i("DoolName");
			} else {
				ls = EntityList.field_75626_c.get(EntityList.func_90035_a(itemstack.func_77952_i()));
				//ls = EntityList.getStringFromID(itemstack.getItemDamage());
			}
			if (ls != null) {
				String ret = (new StringBuilder()).append(super.func_77658_a()).append(".").append(ls).toString();
				return ret;
			} else {
				itemstack.func_77964_b(0);
			}
		}
		return super.func_77658_a();
	}

	@Override
	public void func_150895_a(Item itemIn, CreativeTabs tab, List<ItemStack> subItems){
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
		String ls = EntityList.field_75626_c.get(EntityList.func_90035_a(pItemStack.func_77952_i()));
		//String ls = EntityList.getStringFromID(pItemStack.getItemDamage());
		if (ls == null || !entityStringMap.containsKey(ls)) {
			if (pItemStack.func_77942_o()) {
				if (pItemStack.func_77978_p().func_74764_b("DoolName")) {
					ls = pItemStack.func_77978_p().func_74779_i("DoolName");
					if (entityStringMap.containsKey(ls)) {
						EntityLivingBase le = (EntityLivingBase)entityStringMap.get(ls);
						pItemStack.func_77964_b(EntityList.func_75619_a(le));
						return le;
					}
				}
			}
			pItemStack.func_77964_b(0);
			return (EntityLivingBase)entityStringMap.values().iterator().next();
		}

		return (EntityLivingBase)entityStringMap.get(ls);
	}

	@Override
    public String func_77653_i(ItemStack stack)
    {
		String[] spl = this.func_77657_g(stack).split("\\.");
        return spl[spl.length-1].replace("Entity", "");
    }

    /**
     * Translates the unlocalized name of this item, but without the .name suffix, so the translation fails and the
     * unlocalized name itself is returned.
     */
	@Override
    public String func_77657_g(ItemStack stack)
    {
        String s = this.func_77667_c(stack);
        return s == null ? "" : I18n.func_74838_a(s);
    }
}