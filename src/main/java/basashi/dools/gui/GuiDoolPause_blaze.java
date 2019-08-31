package basashi.dools.gui;

import basashi.dools.entity.EntityDool;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.monster.EntityBlaze;

public class GuiDoolPause_blaze extends GuiDoolPause {
	public EntityBlaze entity;
	public String[] button101 = {"Burn off", "Burn on"};


	public GuiDoolPause_blaze(EntityDool entityfigure) {
		super(entityfigure);
		entity = (EntityBlaze)entityfigure.renderEntity;
	}

	@Override
	public void initGui() {
		super.initGui();
		// 実際のフィギュアが燃えないのでオミット

		GuiButton b1 = new GuiButton(101, width / 2 - 140, height / 6 + 0 + 12, 80, 20, entity.isBurning() ? button101[1] : button101[0]) {
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
			entity.setOnFire(!entity.isBurning());
			guibutton.displayString = entity.isBurning() ? button101[1] : button101[0];
			break;
		}

		super.actionPerformed(guibutton);
	}
}
