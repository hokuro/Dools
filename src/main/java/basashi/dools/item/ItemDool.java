package basashi.dools.item;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

import basashi.dools.core.Dools;
import basashi.dools.core.log.ModLog;
import basashi.dools.entity.EntityDool;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
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
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
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
	public EnumActionResult onItemUse(EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ){
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
			ItemStack stack = playerIn.getHeldItem(hand);
			float lyaw = (180F - playerIn.getRotationYawHead())%360F;
			EntityLivingBase lelb = getEntityFromItemStack(stack);
			if(stack.getItemDamage()>0){
				ModLog.log().debug("id "+stack.getItemDamage());
				if(!worldIn.isRemote){
					try{
						EntityDool lc = new EntityDool(worldIn,stack.getItemDamage());
						lc.setPositionAndRotation(x,y,z,lyaw, 0F);
						worldIn.spawnEntity(lc);
						worldIn.playSound(playerIn, new BlockPos(playerIn.posX,playerIn.posY,playerIn.posZ),
								SoundEvents.BLOCK_STONE_PLACE, SoundCategory.BLOCKS ,0.5F, 0.4F / ((new Random()).nextFloat() * 0.4F + 0.8F));
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
			stack.shrink(1);
		}
		ModLog.log().debug("end");
		return EnumActionResult.FAIL;
	}



	@Override
	public String getUnlocalizedName(ItemStack itemstack) {
		if (itemstack.getItemDamage() != 0) {
			return getItemStackDisplayName(itemstack);
		}
		return super.getUnlocalizedName();
	}

	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> subItems){
		if (tab != this.getCreativeTab()){return;}
	  	// Creativeタブに追加するアイテム
		subItems.add(new ItemStack(Dools.dool, 1));

		List<ModelResourceLocation> resource = new ArrayList<ModelResourceLocation>();
		for (ResourceLocation res : EntityList.getEntityNameList()){
			try{
				Class cl = EntityList.getClass(res);
				if (!Modifier.isAbstract(cl.getModifiers()) && EntityLivingBase.class.isAssignableFrom(cl)){
					ItemStack stack = new ItemStack(Dools.dool, 1, EntityList.getID(cl));
					subItems.add(stack);
					Dools.instance.registerModel(stack);
				}
			}catch(Exception e){

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
		String ls = null;
		for (ResourceLocation res : EntityList.getEntityNameList()){
			if (EntityList.getClass(res) == EntityList.getClassFromID(pItemStack.getItemDamage())){
					ls = res.toString();
					break;
			}
		}
		//String ls = EntityList.getStringFromID(pItemStack.getItemDamage());
		if (ls == null || !entityStringMap.containsKey(ls)) {
			if (pItemStack.hasTagCompound()) {
				if (pItemStack.getTagCompound().hasKey("DoolName")) {
					ls = pItemStack.getTagCompound().getString("DoolName");
					if (entityStringMap.containsKey(ls)) {
						EntityLivingBase le = (EntityLivingBase)entityStringMap.get(ls);
						pItemStack.setItemDamage(EntityList.getID(le.getClass()));
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
		String name = "Dool";
		Class cl = EntityList.getClassFromID(stack.getMetadata());
		for (ResourceLocation res : EntityList.getEntityNameList()){
			if (EntityList.getClass(res) == cl){
				name += "_"+EntityList.getTranslationName(res);
			}
		}

		Dools.instance.registerModel(stack);
		return name;
    }

    /**
     * Translates the unlocalized name of this item, but without the .name suffix, so the translation fails and the
     * unlocalized name itself is returned.
     */
	@Override
    public String getUnlocalizedNameInefficiently(ItemStack stack)
    {
		return getItemStackDisplayName(stack);
    }
}