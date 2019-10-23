package basashi.dools.gui;

import basashi.dools.entity.EntityDool;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.passive.horse.SkeletonHorseEntity;

public class GuiDoolPause_skeleton_horse extends GuiDoolPause {
	public SkeletonHorseEntity entity;
	public String[] button101 ={"saddle on","saddle off"};

	public GuiDoolPause_skeleton_horse(EntityDool entityfigure) {
		super(entityfigure);
		entity = (SkeletonHorseEntity)entityfigure.renderEntity;
	}

	@Override
	public void init() {
		super.init();
		this.addButton(new Button(width / 2 - 140, height / 6 + 0 + 12, 80, 20, entity.isHorseSaddled() ? button101[1]:button101[0], (bt)->{actionPerformed(101, bt);}));
	}

	@Override
	protected void actionPerformed(int id, Button button) {
		switch(id){
		case 101:
			entity.setHorseSaddled(!entity.isHorseSaddled());
			button.setMessage( entity.isHorseSaddled() ? button101[1]:button101[0]);
			break;
		}
		super.actionPerformed(id, button);
	}
}
