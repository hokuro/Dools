package basashi.dools.gui;

import basashi.dools.entity.EntityDool;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.passive.EntityPig;

public class GuiDoolPause_pig extends GuiDoolPause {
	public EntityPig entity;

	public String[] button101 ={"saddle on","saddle off"};

	public GuiDoolPause_pig(EntityDool entityfigure) {
		super(entityfigure);
		entity = (EntityPig)entityfigure.renderEntity;
	}

	@Override
	public void initGui() {
		super.initGui();
		buttonList.add(new GuiButton(101, width / 2 - 140, height / 6 + 0 + 12, 80, 20, entity.getSaddled() ? button101[1]:button101[0]));
	}

	@Override
	protected void actionPerformed(GuiButton guibutton) {
		if (!guibutton.enabled) {
			return;
		}
		switch (guibutton.id) {
		case 101:
			entity.setSaddled(!entity.getSaddled());
			guibutton.displayString =  entity.getSaddled() ? button101[1]:button101[0];
			break;
		}

		super.actionPerformed(guibutton);
	}
}