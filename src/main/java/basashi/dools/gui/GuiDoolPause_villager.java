package basashi.dools.gui;

import java.util.ArrayList;
import java.util.List;

import basashi.dools.entity.EntityDool;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.merchant.villager.VillagerData;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.merchant.villager.VillagerProfession;
import net.minecraft.entity.villager.IVillagerType;
import net.minecraft.util.registry.Registry;

public class GuiDoolPause_villager extends GuiDoolPause {

	public VillagerEntity entity;
	private List<VillagerProfession> professions = new ArrayList<VillagerProfession>();
	private List<IVillagerType > villagerType = new ArrayList<IVillagerType >();
	private int indexProfession = 0;
	private int indexType = 0;
	private int level;
	public GuiDoolPause_villager(EntityDool entityfigure) {
		super(entityfigure);
		entity = (VillagerEntity)entityfigure.renderEntity;
		indexProfession = 0;
		indexType = 0;
		level = 1;
	}

	@Override
	public void init() {
		super.init();
		Registry.VILLAGER_PROFESSION.forEach((vData)->{
			professions.add(vData);
		});
		Registry.VILLAGER_TYPE.forEach((vData)->{
			villagerType.add(vData);
		});

		indexProfession = professions.indexOf(entity.getVillagerData().getProfession());
		indexType = villagerType.indexOf(entity.getVillagerData().getType());
		level = entity.getVillagerData().getLevel();
		this.addButton(new Button(width / 2 - 140, height / 6 + 0 + 12, 80, 20, professions.get(indexProfession).getPointOfInterest().toString(),(bt)->{actionPerformed(101, bt);}));
		this.addButton(new Button(width / 2 - 140, height / 6 + 24 + 12, 80, 20, villagerType.get(indexType).toString(),(bt)->{actionPerformed(102, bt);}));
		this.addButton(new Button(width / 2 - 140, height / 6 + 48 + 12, 80, 20, String.valueOf(level) ,(bt)->{actionPerformed(103, bt);}));
	}

	@Override
	protected void actionPerformed(int id, Button button) {
		switch(id){
		case 101:
			indexProfession++;
			if (indexProfession >= professions.size()){indexProfession = 0;}

			entity.setVillagerData(entity.getVillagerData().withProfession(professions.get(indexProfession)));
			button.setMessage(professions.get(indexProfession).getPointOfInterest().toString());
			break;

		case 102:
			indexType++;
			if (indexType >= villagerType.size()){indexType = 0;}

			entity.setVillagerData(entity.getVillagerData().withType(villagerType.get(indexType)));
			button.setMessage(villagerType.get(indexType).toString());
			break;

		case 103:
			level++;
			if (!VillagerData.func_221128_d(level)){level = 1;}

			entity.setVillagerData(entity.getVillagerData().withLevel(level));
			button.setMessage(String.valueOf(level));
			break;
		}
		super.actionPerformed(id, button);
	}
}
