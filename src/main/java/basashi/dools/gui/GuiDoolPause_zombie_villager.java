package basashi.dools.gui;

import java.util.ArrayList;
import java.util.List;

import basashi.dools.entity.EntityDool;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.monster.EntityZombieVillager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.RegistryNamespaced;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.registry.VillagerRegistry;
import net.minecraftforge.fml.common.registry.VillagerRegistry.VillagerProfession;

public class GuiDoolPause_zombie_villager extends GuiDoolPause {
	public EntityZombieVillager entity;

	private static RegistryNamespaced<VillagerProfession> register = null;
	public static List<String> name = new ArrayList<String>();
	public static List<Integer> id = new ArrayList<Integer>();
	private int index = 0;

	public GuiDoolPause_zombie_villager(EntityDool entityfigure) {
		super(entityfigure);
		entity = (EntityZombieVillager)entityfigure.renderEntity;
		index = 0;
	}

	@Override
	public void initGui() {
		super.initGui();
		if (register == null){
			register = ObfuscationReflectionHelper.getPrivateValue(VillagerRegistry.class, VillagerRegistry.instance(), "REGISTRY");
			for (ResourceLocation res : register.getKeys()){
				id.add(register.getId(register.get(res)));
				name.add(res.getPath());
			}
		}
		index = Math.max(id.indexOf(entity.getProfession()),0);

		GuiButton b1 = new GuiButton(101, width / 2 - 140, height / 6 + 0 + 12, 80, 20, name.get(index)) {
    		@Override
    		public void onClick(double mouseX, double moudeY){
    			actionPerformed(this);
    		}
    	};
    	buttons.add(b1);
    	this.children.addAll(buttons);
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

		case 16:
			// 幼生態判定
			entity.setChild(!entity.isChild());
			guibutton.displayString = button16[entity.isChild() ? 1 : 0];
			return;
		}

		super.actionPerformed(guibutton);
	}
}
