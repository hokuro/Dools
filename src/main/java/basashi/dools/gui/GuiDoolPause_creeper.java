package basashi.dools.gui;

import basashi.dools.entity.EntityDool;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.monster.EntityCreeper;

public class GuiDoolPause_creeper extends GuiDoolPause {
	public EntityCreeper entity;

	public String[] button101 ={"powerd on","powerd off"};

	public GuiDoolPause_creeper(EntityDool entityfigure) {
		super(entityfigure);
		entity = (EntityCreeper)entityfigure.renderEntity;
	}

	@Override
	public void initGui() {
		super.initGui();
		buttonList.add(new GuiButton(101, width / 2 - 140, height / 6 + 0 + 12, 80, 20, entity.getPowered() ? button101[1]:button101[0]));
	}

	@Override
	protected void actionPerformed(GuiButton guibutton) {
		if (!guibutton.enabled) {
			return;
		}
		switch (guibutton.id) {
		case 101:
			if (!entity.getPowered()){
				entity.onStruckByLightning(null);
				guibutton.enabled = false;
			}
			guibutton.displayString =  entity.getPowered() ? button101[1]:button101[0];
			break;
		}

		super.actionPerformed(guibutton);
	}
}
