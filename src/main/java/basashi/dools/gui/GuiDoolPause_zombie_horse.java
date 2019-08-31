package basashi.dools.gui;

import basashi.dools.entity.EntityDool;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.passive.EntityZombieHorse;

public class GuiDoolPause_zombie_horse extends GuiDoolPause {
	public EntityZombieHorse entity;

	public String[] button101 ={"saddle on","saddle off"};

	public GuiDoolPause_zombie_horse(EntityDool entityfigure) {
		super(entityfigure);
		entity = (EntityZombieHorse)entityfigure.renderEntity;
	}

	@Override
	public void initGui() {
		super.initGui();
		GuiButton b1 = new GuiButton(101, width / 2 - 140, height / 6 + 0 + 12, 80, 20, entity.isHorseSaddled() ? button101[1]:button101[0]) {
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
			entity.setHorseSaddled(!entity.isHorseSaddled());
			guibutton.displayString =  entity.isHorseSaddled() ? button101[1]:button101[0];
			break;
		}

		super.actionPerformed(guibutton);
	}
}
