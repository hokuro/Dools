package basashi.dools.gui;

import java.util.ArrayList;
import java.util.List;

import basashi.dools.entity.EntityDool;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.passive.HorseArmorType;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class GuiDoolPause_horse extends GuiDoolPause {
	private EntityHorse ehorse;
	private int val =0;
	private GuiButton button102;
	private GuiButton button150;
	private GuiButton button160;
	//private String strbutton102[] = { "UnChested", "Chested" };
	private int armor=0;

	public static final ItemStack iarmor[] = {ItemStack.EMPTY,
			new ItemStack(Items.IRON_HORSE_ARMOR),
			new ItemStack(Items.GOLDEN_HORSE_ARMOR),
			new ItemStack(Items.DIAMOND_HORSE_ARMOR)};
	private String strarmor[] = {"None","Iron","Gold","Diamond"};


	private String[] strbutton101 = {"Breed on","Breed off"};
	private String[] strbutton102 = { "UnSaddled", "Saddled" };

	private int armortype = 0;
	public static final List<HorseArmorType> hoseTYpe = new ArrayList<HorseArmorType>(){
			{add(HorseArmorType.NONE);}
			{add(HorseArmorType.IRON);}
			{add(HorseArmorType.GOLD);}
			{add(HorseArmorType.DIAMOND);}
	};

	private int color1 = 0;
	private int color2 = 0;

	public GuiDoolPause_horse(EntityDool entityfigure) {
		super(entityfigure);
		ehorse = (EntityHorse) targetEntity.renderEntity;
	}

	@Override
	public void initGui() {
		super.initGui();
		buttonList.remove(4);
		buttonList.add(new GuiButton(102, width / 2 - 140, height / 6 + 0 + 12, 80, 20, ehorse.isHorseSaddled() ? strbutton102[1]:strbutton102[0]));

		armortype = Math.max(hoseTYpe.indexOf(ehorse.getHorseArmorType()),0);
		buttonList.add(new GuiButton(103, width / 2 - 140, height / 6 + 0 + 12 + 20, 80, 20, hoseTYpe.get(armortype).name()));


		int val = ehorse.getHorseVariant();
		color1 = (val & 255) % 7;
		color2 = ((val & 65280) >> 8)%5;

		buttonList.add(new GuiButton(104, width / 2 - 140, height / 6 + 0 + 12 + 40, 80, 20, "Texture: "+color1));
		buttonList.add(new GuiButton(105, width / 2 - 140, height / 6 + 0 + 12 + 60, 80, 20, "Mark: "+color2));

	}

	protected void actionPerformed(GuiButton guibutton) {
		super.actionPerformed(guibutton);

		if (!guibutton.enabled) {
			return;
		}
		int color;
		switch (guibutton.id) {
		case 102:
			ehorse.setHorseSaddled(!ehorse.isHorseSaddled());
			guibutton.displayString = ehorse.isHorseSaddled() ? strbutton102[1]:strbutton102[0];
			break;
		case 103:
			armortype++;
			if (hoseTYpe.size() <= armortype){
				armortype = 0;
			}
			ehorse.setHorseArmorStack(iarmor[armortype]);
			guibutton.displayString =hoseTYpe.get(armortype).name();
			break;
		case 104:
			color1++;
			if (color1 > 6){
				color1 = 0;
			}
			color = color2<<8 | color1;
			ehorse.setHorseVariant(color);
			guibutton.displayString ="Texture: "+color1;
			break;
		case 105:
			color2++;
			if (color2 > 4){
				color2 = 0;
			}
			color = color2<<8 | color1;
			ehorse.setHorseVariant(color);
			guibutton.displayString ="Mark: "+color2;
			break;
		}
	}
}
