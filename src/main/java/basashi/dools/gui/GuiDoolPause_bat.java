package basashi.dools.gui;

import basashi.dools.entity.EntityDool;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.passive.BatEntity;

public class GuiDoolPause_bat extends GuiDoolPause {
	public BatEntity entity;
	public String[] button_fly101 = {"Fly","Hanging"};


	public GuiDoolPause_bat(EntityDool entityfigure) {
		super(entityfigure);
		entity = (BatEntity)entityfigure.renderEntity;
	}

	@Override
	public void init() {
		super.init();
		this.addButton(new Button(width / 2 - 140, height / 6 + 0 + 12, 80, 20, entity.getIsBatHanging() ? button_fly101[0]:button_fly101[1], (bt)->{actionPerformed(101,bt);}));
	}

	@Override
	protected void actionPerformed(int id, Button button) {
		switch(id){
		case 101:
			entity.setIsBatHanging(!entity.getIsBatHanging());
			button.setMessage( entity.getIsBatHanging() ? button_fly101[0]:button_fly101[1]);
			break;
		}
		super.actionPerformed(id, button);
	}
}
