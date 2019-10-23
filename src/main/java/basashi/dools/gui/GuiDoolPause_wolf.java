package basashi.dools.gui;

import java.util.UUID;

import basashi.dools.entity.EntityDool;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.item.DyeColor;

public class GuiDoolPause_wolf extends GuiDoolPause {

	private WolfEntity entity;
	private String button102[] = { "Contract", "Wild" };
	private String button103[] = { "Sitting", "Standing" };
	private String button104[] = { "Angry", "Calm" };
	private Button health;
	public GuiDoolPause_wolf(EntityDool entityfigure) {
		super(entityfigure);
		entity = (WolfEntity) targetEntity.renderEntity;
	}

	@Override
	public void init() {
		super.init();
		this.addButton(new Button(width / 2 - 140, height / 6 + -24 + 12, 80, 20, entity.getCollarColor().getName(),(bt)->{actionPerformed(105, bt);}));
		this.addButton(new Button(width / 2 - 140, height / 6 + 0 + 12, 80, 20, button102[entity.isTamed() ? 0 : 1],(bt)->{actionPerformed(102, bt);}));
		this.addButton(new Button(width / 2 - 140, height / 6 + 24 + 12, 80, 20, button103[entity.isSitting() ? 0 : 1],(bt)->{actionPerformed(103, bt);}));
		this.addButton(new Button(width / 2 - 140, height / 6 + 48 + 12, 80, 20, button104[entity.isAngry() ? 0 : 1],(bt)->{actionPerformed(104, bt);}));

		this.addButton(health = new Button(width / 2 - 120, height / 6 + 72 + 12, 40, 20, String.format("%d", (int)entity.getHealth()),(bt)->{actionPerformed(150, bt);}));
		this.addButton(new Button(width / 2 - 140, height / 6 + 72 + 12, 20, 20, "+",(bt)->{actionPerformed(151, bt);}));
		this.addButton(new Button(width / 2 - 80, height / 6 + 72 + 12, 20, 20, "-",(bt)->{actionPerformed(152, bt);}));
	}

	@Override
	protected void actionPerformed(int id, Button button) {
		switch(id){
		case 102:
			boolean tame = entity.isTamed();
			if (tame){
				entity.setOwnerId(null);
			}else{
				entity.setOwnerId(new UUID(1,1));
			}
			entity.setTamed(!tame);
			button.setMessage(button102[entity.isTamed() ? 0 : 1]);
			break;

		case 103:
			entity.setSitting(!entity.isSitting());
			button.setMessage(button103[entity.isSitting() ? 0 : 1]);
			break;

		case 104:
			entity.setAngry(!entity.isAngry());
			button.setMessage(button104[entity.isAngry() ? 0 : 1]);
			break;
		case 105:
			DyeColor color = entity.getCollarColor();
			entity.setCollarColor(DyeColor.byId(color.getId()+1));
			button.setMessage(entity.getCollarColor().getName());
		    break;

		case 150:
			entity.setHealth(10F);
			button.setMessage(String.format("%d", (int)entity.getHealth()));
			break;

		}

		float lhealth = entity.getHealth();
		if (id == 151) {
			if (lhealth < entity.getMaxHealth()) {
				entity.heal(1);
				health.setMessage(String.format("%d", (int)entity.getHealth()));
			}
		}
		if (id == 152) {
			if (lhealth > 1) {
				entity.setHealth(lhealth-1);
				health.setMessage(String.format("%d", (int)entity.getHealth()));
			}
		}
		super.actionPerformed(id, button);
	}
}
