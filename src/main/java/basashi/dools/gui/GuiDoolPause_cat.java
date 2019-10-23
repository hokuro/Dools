package basashi.dools.gui;

import basashi.dools.entity.EntityDool;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.passive.CatEntity;

public class GuiDoolPause_cat extends GuiDoolPause {

	private CatEntity entity;
	private String button_tame102[] = { "Contract", "Wild" };
	private String button_sit103[] = { "Sitting", "Standing" };
	private String button_kind104[] = { "ozelot", "black", "red", "siamese" };

	public GuiDoolPause_cat(EntityDool entityfigure) {
		super(entityfigure);
		entity = (CatEntity) targetEntity.renderEntity;
	}

	@Override
	public void init() {
		super.init();
		this.addButton(new Button(width / 2 - 140, height / 6 + 0 + 12, 80, 20, button_tame102[entity.isTamed() ? 0 : 1], (bt)->{actionPerformed(102, bt);}));
    	this.addButton(new Button(width / 2 - 140, height / 6 + 24 + 12, 80, 20, button_sit103[entity.isSitting() ? 0 : 1], (bt)->{actionPerformed(103, bt);}));
    	this.addButton(new Button(width / 2 - 140, height / 6 + 48 + 12, 80, 20, skinName(), (bt)->{actionPerformed(104, bt);}));
	}

	@Override
	protected void actionPerformed(int id, Button button) {
		switch (id) {
		case 102:
			entity.setTamed(!entity.isTamed());
			button.setMessage(button_tame102[entity.isTamed() ? 0 : 1]);
			break;

		case 103:
			entity.setSitting(!entity.isSitting());
			button.setMessage(button_sit103[entity.isSitting() ? 0 : 1]);
			break;

		case 104:
			int catType = entity.getCatType() + 1;
			if (CatEntity.field_213425_bD.size() <= catType) {catType = 0;}
			entity.setCatType(catType);
			button.setMessage(skinName());
			break;
		}
		super.actionPerformed(id, button);
	}

	private String skinName() {
		String[] skinpath = entity.getCatTypeName().getPath().split("/");
		String ret = skinpath[skinpath.length-1].replace(".png", "");
		return ret;
	}
}
