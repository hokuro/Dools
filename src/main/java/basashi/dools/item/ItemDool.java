package basashi.dools.item;

import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

import org.codehaus.plexus.util.StringUtils;

import basashi.dools.core.log.ModLog;
import basashi.dools.entity.EntityDool;
import basashi.dools.gui.GuiDoolSelect;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

public class ItemDool extends Item {
	public static final String NAME="dool";
	public static Map<String,Entity> entityStringMap = new TreeMap<String,Entity>();
	public static ItemStack firstPerson;

	public ItemDool(Item.Properties property){
		super(property);
	}

	@Override
	public ActionResultType onItemUse(ItemUseContext context) {
		World world = context.getWorld();
		ItemStack itemstack = context.getItem();

		BlockPos blockpos = context.getPos();
		Direction direction = context.getFace();
		BlockState blockstate = world.getBlockState(blockpos);

		BlockPos blockpos1;
		if (blockstate.getCollisionShape(world, blockpos).isEmpty()) {
			blockpos1 = blockpos;
		} else {
			blockpos1 = blockpos.offset(direction);
		}
		float lyaw = (180F - context.getPlayer().getRotationYawHead())%360F;
		String name = getLivingName(itemstack);

		ModLog.log().debug("start: " + name);
		if(!StringUtils.isEmpty(name)){
			if (!world.isRemote) {
				// i名前が登録済み
				ModLog.log().debug("id " + name);
				try{
					// i登録済みのエンティティを召喚
					EntityDool lc = new EntityDool(context.getWorld(), getLivingNBT(itemstack));
					lc.setPositionAndRotation(blockpos1.getX(), blockpos1.getY(), blockpos1.getZ(), lyaw, 0.0F);
					context.getWorld().addEntity(lc);
					context.getWorld().playSound(context.getPlayer(), new BlockPos(context.getPlayer().posX,context.getPlayer().posY,context.getPlayer().posZ),
							SoundEvents.BLOCK_STONE_PLACE, SoundCategory.BLOCKS ,0.5F, 0.4F / ((new Random()).nextFloat() * 0.4F + 0.8F));
					itemstack.shrink(1);
				}catch(Exception e){
					e.printStackTrace();
				}
			}
			return ActionResultType.SUCCESS;
		} else {
			if (world.isRemote) {
				EntityDool lc = new EntityDool(world);
				lc.setPositionAndRotation(blockpos1.getX(), blockpos1.getY(), blockpos1.getZ(), lyaw, 0.0F);
				Minecraft.getInstance().displayGuiScreen(new GuiDoolSelect(lc, new TranslationTextComponent("select")));
			} else {
				itemstack.shrink(1);
			}
			ModLog.log().debug("end");
			return ActionResultType.SUCCESS;
		}
	}

	public static final String KEY_LIVINGNAME = "doolname";
	public static final String KEY_LIVINGNBT = "doollivingnbt";

	public static void setLivingName(ItemStack stack, String value) {
		CompoundNBT compound = stack.getOrCreateTag();
		compound.putString(KEY_LIVINGNAME, value);
		stack.setTag(compound);
	}

	public static String getLivingName(ItemStack stack) {
		String ret = "";
		CompoundNBT compound  = stack.getOrCreateTag();
		if (compound.contains(KEY_LIVINGNAME)) {
			ret = compound.getString(KEY_LIVINGNAME);
		}
		return ret;
	}

	public static void setLivingNBT(ItemStack stack, LivingEntity entity) {
		CompoundNBT compound = stack.getOrCreateTag();
		CompoundNBT entityNBT = entity.writeWithoutTypeId(new CompoundNBT());
		entity.writeAdditional(entityNBT);
		compound.put(KEY_LIVINGNBT, entityNBT);
		stack.setTag(compound);
	}

	public static LivingEntity getLivingNBT(ItemStack stack) {
		String name = getLivingName(stack);
		Entity ent;
		if (StringUtils.isEmpty(name)) {
			ent = EntityType.ZOMBIE.create(Minecraft.getInstance().world);
		} else {
			ent = Registry.ENTITY_TYPE.getOrDefault(new ResourceLocation(name)).create(Minecraft.getInstance().world);
		}
		if (ent instanceof LivingEntity) {
			return getLivingNBT(stack, (LivingEntity)ent);
		}
		return null;
	}

	public static LivingEntity getLivingNBT(ItemStack stack, LivingEntity living) {
		CompoundNBT compound = stack.getOrCreateTag();
		if (compound.contains(KEY_LIVINGNBT)) {
			CompoundNBT entityNBT = (CompoundNBT)compound.get(KEY_LIVINGNBT);
			living.read(entityNBT);
			living.readAdditional(entityNBT);
		}
		return living;
	}

	@Override
	public void fillItemGroup(ItemGroup tab, NonNullList<ItemStack> subItems){
		if (tab != this.getGroup()){return;}
	  	// Creativeタブに追加するアイテム
		subItems.add(new ItemStack(this, 1));

		Registry.ENTITY_TYPE.forEach((etype)->{
			Entity cls = etype.create(Minecraft.getInstance().world);
			if (cls instanceof LivingEntity) {
				ItemStack stack = new ItemStack(this,1);
				setLivingName(stack, etype.getRegistryName().toString());
				subItems.add(stack);
			}
		});
	}

	@Override
	protected String getDefaultTranslationKey() {
		return "Dool";
	}

	@Override
	public String getTranslationKey(ItemStack stack) {
		String name = getLivingName(stack);
		if (!StringUtils.isEmpty(name)){
			return "Dool_"+ I18n.format(Registry.ENTITY_TYPE.getOrDefault(new ResourceLocation(name)).getTranslationKey());
		}
		return getDefaultTranslationKey();
	}
}
