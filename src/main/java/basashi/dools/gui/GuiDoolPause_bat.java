package basashi.dools.gui;

import basashi.dools.entity.EntityDool;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.passive.EntityBat;

public class GuiDoolPause_bat extends GuiDoolPause {
	public EntityBat entity;

	public String[] button101 ={"Fly","Hanging"};

	public GuiDoolPause_bat(EntityDool entityfigure) {
		super(entityfigure);
		entity = (EntityBat)entityfigure.renderEntity;
	}

	@Override
	public void initGui() {
		super.initGui();
		buttonList.add(new GuiButton(101, width / 2 - 140, height / 6 + 0 + 12, 80, 20, entity.getIsBatHanging() ? button101[0]:button101[1]));
	}

	@Override
	protected void actionPerformed(GuiButton guibutton) {
		if (!guibutton.enabled) {
			return;
		}
		switch (guibutton.id) {
		case 101:
			entity.setIsBatHanging(!entity.getIsBatHanging());
			guibutton.displayString =  entity.getIsBatHanging() ? button101[0]:button101[1];
			break;
		}

		super.actionPerformed(guibutton);
	}
}
