package basashi.dools.gui;

import java.util.ArrayList;
import java.util.List;

import basashi.dools.core.Dools;
import basashi.dools.entity.EntityDool;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.RegistryNamespaced;
import net.minecraftforge.fml.common.registry.VillagerRegistry;
import net.minecraftforge.fml.common.registry.VillagerRegistry.VillagerProfession;

public class GuiDoolPause_villager extends GuiDoolPause {

	public EntityVillager entity;
	protected static String sb100[] = { "farmer", "librarian", "priest", "smith", "butcher", "villager" };
	private static RegistryNamespaced<ResourceLocation, VillagerProfession> register = null;
	public static List<String> name = new ArrayList<String>();
	public static List<Integer> id = new ArrayList<Integer>();
	private int index = 0;

	public GuiDoolPause_villager(EntityDool entityfigure) {
		super(entityfigure);
		entity = (EntityVillager)entityfigure.renderEntity;
		index = 0;
	}

	@Override
	public void initGui() {
		super.initGui();
		if (register == null){
			register = Dools.getPrivateValue(VillagerRegistry.class, VillagerRegistry.instance(), "REGISTRY");
			for (ResourceLocation res : register.getKeys()){
				id.add(register.getIDForObject(register.getObject(res)));
				name.add(res.getResourcePath());
			}
		}
		index = Math.max(id.indexOf(entity.getProfession()),0);
		buttonList.add(new GuiButton(101, width / 2 - 140, height / 6 + 0 + 12, 80, 20, name.get(index)));
	}

	@Override
	protected void actionPerformed(GuiButton guibutton) {
		if (!guibutton.enabled) {
			return;
		}
		switch (guibutton.id) {
		case 101:
			index++;
			if (index >= name.size()){index = 0;}
			entity.setProfession(id.get(index));
			guibutton.displayString =  name.get(index);
			break;

//		case 16:
//			// 幼生態判定
//			entity.setChild(!entity.isChild());
//			guibutton.displayString = button16[entity.isChild() ? 1 : 0];
//			return;
		}

		super.actionPerformed(guibutton);
	}

}
