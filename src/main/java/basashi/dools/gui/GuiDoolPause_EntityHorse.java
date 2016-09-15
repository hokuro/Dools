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
	public static final ItemStack iarmor[] = {null,new ItemStack(Items.iron_horse_armor),new ItemStack(Items.golden_horse_armor),new ItemStack(Items.diamond_horse_armor)};
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
	public void initGui() {
		super.initGui();
		buttonList.remove(4);

		button102=new GuiButton(102, width / 2 - 140,
				height / 6 + 0 + 12, 80, 20,strbutton102[(ehorse.isChested()?1:0)]);
		button102.enabled = ehorse.getType().canBeChested();
		buttonList.add(button102);
		buttonList.add(new GuiButton(103, width / 2 - 140,
				height / 6 + 24 + 12, 80, 20, strbutton103[(ehorse.isHorseSaddled()?1:0)]));
		buttonList.add(new GuiButton(104, width / 2 - 140,
				height / 6 + 48 + 12, 80, 20,ehorse.getName()));

		button150=new GuiButton(150, width / 2 - 120,
				height / 6 + 72 + 12, 40, 20, Integer.toString(ehorse.getHorseVariant()));
		buttonList.add(button150);
		buttonList.add(new GuiButton(151, width / 2 - 140,
				height / 6 + 72 + 12, 20, 20, "+"));
		buttonList.add(new GuiButton(152, width / 2 - 80,
				height / 6 + 72 + 12, 20, 20, "-"));
		armor = ehorse.getDataManager().get((DataParameter<Integer>)Dools.getPrivateValue(EntityHorse.class,null,"HORSE_ARMOR"));
		button160 = new GuiButton(160, width / 2 - 140,
				height / 6 + 96 + 12, 80, 20,strarmor[armor]);
		//button160.enabled = ehorse.canWearArmor();
		buttonList.add(button160);
	}

	protected void actionPerformed(GuiButton guibutton) {
		super.actionPerformed(guibutton);

		if (!guibutton.enabled) {
			return;
		}

		switch (guibutton.id) {
		case 102:
			if (ehorse.getType().canBeChested()){
				ehorse.setChested(!ehorse.isChested());
				guibutton.displayString = strbutton102[(ehorse.isChested()?1:0)];
			}
			break;
		case 103:
			ehorse.setHorseSaddled(!ehorse.isHorseSaddled());
			guibutton.displayString = strbutton103[(ehorse.isHorseSaddled()?1:0)];
			break;
		case 104:
			HorseArmorType type;
			try{
				type = HorseArmorType.getArmorType(ehorse.getType().ordinal()+1);
			}catch(Exception ex){
				type = HorseArmorType.getArmorType(0);
			}
			ehorse.setType(type);
			guibutton.displayString = ehorse.getName();
			break;

		case 150:
			break;
		case 151:
			val = (val==6)?0:val+1;
			ehorse.setHorseVariant(val);
			button150.displayString = Integer.toString(val);
			break;
		case 152:
			val = (val==0)?6:val-1;
			ehorse.setHorseVariant(val);
			button150.displayString = Integer.toString(val);
			break;
		case 160:
			//armor =ehorse.getHorseArmorIndexSynced()==3?0:armor+1;
			armor = (armor+1)>=iarmor.length?0:armor+1;
			ehorse.setHorseArmorStack(iarmor[armor]);
			guibutton.displayString=strarmor[armor];
			break;
		}

		if ( guibutton.id==104){
			if ( ehorse.getType().canBeChested()){
				button102.enabled = true;
			}else{
				button102.enabled = false;
				ehorse.setChested(ehorse.getType().canBeChested()?ehorse.isChested():false);
				button102.displayString = strbutton102[(ehorse.isChested()?1:0)];
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
