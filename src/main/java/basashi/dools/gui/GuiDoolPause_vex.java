package basashi.dools.gui;

import basashi.dools.entity.EntityDool;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.monster.EntityVex;

public class GuiDoolPause_vex extends GuiDoolPause {
	public EntityVex entity;

	public String[] button101 ={"Normal","Angree"};

	public GuiDoolPause_vex(EntityDool entityfigure) {
		super(entityfigure);
		entity = (EntityVex)entityfigure.renderEntity;
	}

	@Override
	public void initGui() {
		super.initGui();
		buttonList.add(new GuiButton(101, width / 2 - 140, height / 6 + 0 + 12, 80, 20, entity.isCharging() ? button101[0]:button101[1]));
	}

	@Override
	protected void actionPerformed(GuiButton guibutton) {
		if (!guibutton.enabled) {
			return;
		}
		switch (guibutton.id) {
		case 101:
			entity.setCharging(!entity.isCharging());
			guibutton.displayString =  entity.isCharging() ? button101[0]:button101[1];
			break;
		}

		super.actionPerformed(guibutton);
	}
}
