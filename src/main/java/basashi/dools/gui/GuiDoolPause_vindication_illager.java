package basashi.dools.gui;

import basashi.dools.entity.EntityDool;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.monster.EntityVindicator;

public class GuiDoolPause_vindication_illager extends GuiDoolPause {
	public EntityVindicator entity;

	public String[] button101 ={"normal","aggressive"};

	public GuiDoolPause_vindication_illager(EntityDool entityfigure) {
		super(entityfigure);
		entity = (EntityVindicator)entityfigure.renderEntity;
	}

	@Override
	public void initGui() {
		super.initGui();
		GuiButton b1 = new GuiButton(101, width / 2 - 140, height / 6 + 0 + 12, 80, 20, entity.isAggressive() ? button101[0]:button101[1]) {
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
			entity.setAggressive(!entity.isAggressive());
			guibutton.displayString =  entity.isAggressive() ? button101[0]:button101[1];
			break;
		}

		super.actionPerformed(guibutton);
	}
}
