package basashi.dools.gui;

import basashi.dools.core.Dools;
import basashi.dools.entity.EntityDool;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.passive.HorseArmorType;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.DataParameter;

public class GuiDoolPause_EntityHorse extends GuiDoolPause {
	private EntityHorse ehorse;
	private int val =0;
	private GuiButton button102;
	private GuiButton button150;
	private GuiButton button160;
	private String strbutton102[] = { "UnChested", "Chested" };
	private String strbutton103[] = { "UnSaddled", "Saddled" };
	private int armor=0;
	public static final ItemStack iarmor[] = {null,new ItemStack(Items.field_151138_bX),new ItemStack(Items.field_151136_bY),new ItemStack(Items.field_151125_bZ)};
	private String strarmor[] = {"None","Iron","Gold","Diamond"};
	private HorseArmorType[] hoseTYpe = new HorseArmorType[]{
			HorseArmorType.HORSE,
			HorseArmorType.DONKEY,
			HorseArmorType.MULE,
			HorseArmorType.SKELETON,
			HorseArmorType.ZOMBIE
	};


	public GuiDoolPause_EntityHorse(EntityDool entityfigure) {
		super(entityfigure);
		ehorse = (EntityHorse) targetEntity.renderEntity;
	}

	@Override
	public void func_73866_w_() {
		super.func_73866_w_();
		field_146292_n.remove(4);

		button102=new GuiButton(102, field_146294_l / 2 - 140,
				field_146295_m / 6 + 0 + 12, 80, 20,strbutton102[(ehorse.func_110261_ca()?1:0)]);
		button102.field_146124_l = ehorse.func_184781_cZ().func_188600_f();
		field_146292_n.add(button102);
		field_146292_n.add(new GuiButton(103, field_146294_l / 2 - 140,
				field_146295_m / 6 + 24 + 12, 80, 20, strbutton103[(ehorse.func_110257_ck()?1:0)]));
		field_146292_n.add(new GuiButton(104, field_146294_l / 2 - 140,
				field_146295_m / 6 + 48 + 12, 80, 20,ehorse.func_70005_c_()));

		button150=new GuiButton(150, field_146294_l / 2 - 120,
				field_146295_m / 6 + 72 + 12, 40, 20, Integer.toString(ehorse.func_110202_bQ()));
		field_146292_n.add(button150);
		field_146292_n.add(new GuiButton(151, field_146294_l / 2 - 140,
				field_146295_m / 6 + 72 + 12, 20, 20, "+"));
		field_146292_n.add(new GuiButton(152, field_146294_l / 2 - 80,
				field_146295_m / 6 + 72 + 12, 20, 20, "-"));
		armor = ehorse.func_184212_Q().func_187225_a((DataParameter<Integer>)Dools.getPrivateValue(EntityHorse.class,null,"HORSE_ARMOR"));
		button160 = new GuiButton(160, field_146294_l / 2 - 140,
				field_146295_m / 6 + 96 + 12, 80, 20,strarmor[armor]);
		//button160.enabled = ehorse.canWearArmor();
		field_146292_n.add(button160);
	}

	protected void func_146284_a(GuiButton guibutton) {
		super.func_146284_a(guibutton);

		if (!guibutton.field_146124_l) {
			return;
		}

		switch (guibutton.field_146127_k) {
		case 102:
			if (ehorse.func_184781_cZ().func_188600_f()){
				ehorse.func_110207_m(!ehorse.func_110261_ca());
				guibutton.field_146126_j = strbutton102[(ehorse.func_110261_ca()?1:0)];
			}
			break;
		case 103:
			ehorse.func_110251_o(!ehorse.func_110257_ck());
			guibutton.field_146126_j = strbutton103[(ehorse.func_110257_ck()?1:0)];
			break;
		case 104:
			HorseArmorType type;
			try{
				type = HorseArmorType.func_188591_a(ehorse.func_184781_cZ().ordinal()+1);
			}catch(Exception ex){
				type = HorseArmorType.func_188591_a(0);
			}
			ehorse.func_184778_a(type);
			guibutton.field_146126_j = ehorse.func_70005_c_();
			break;

		case 150:
			break;
		case 151:
			val = (val==6)?0:val+1;
			ehorse.func_110235_q(val);
			button150.field_146126_j = Integer.toString(val);
			break;
		case 152:
			val = (val==0)?6:val-1;
			ehorse.func_110235_q(val);
			button150.field_146126_j = Integer.toString(val);
			break;
		case 160:
			//armor =ehorse.getHorseArmorIndexSynced()==3?0:armor+1;
			armor = (armor+1)>=iarmor.length?0:armor+1;
			ehorse.func_146086_d(iarmor[armor]);
			guibutton.field_146126_j=strarmor[armor];
			break;
		}

		if ( guibutton.field_146127_k==104){
			if ( ehorse.func_184781_cZ().func_188600_f()){
				button102.field_146124_l = true;
			}else{
				button102.field_146124_l = false;
				ehorse.func_110207_m(ehorse.func_184781_cZ().func_188600_f()?ehorse.func_110261_ca():false);
				button102.field_146126_j = strbutton102[(ehorse.func_110261_ca()?1:0)];
			}

//			if (ehorse.canWearArmor()){
//				button160.enabled = true;
//			}else{
//				button160.enabled = false;
//				armor = 0;
//				ehorse.setHorseArmorStack(null);
//				ehorse.setHorseArmorStack(iarmor[armor]);
//				button160.displayString=strarmor[armor];
//			}
		}

	}
}
