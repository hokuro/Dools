package basashi.dools.gui;

import basashi.dools.entity.EntityDool;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.monster.EntitySnowman;

public class GuiDoolPause_snowman extends GuiDoolPause {
	public EntitySnowman entity;

	public String[] button101 ={"Mask On","Mask Off"};

	public GuiDoolPause_snowman(EntityDool entityfigure) {
		super(entityfigure);
		entity = (EntitySnowman)entityfigure.renderEntity;
	}

	@Override
	public void initGui() {
		super.initGui();
		buttonList.add(new GuiButton(101, width / 2 - 140, height / 6 + 0 + 12, 80, 20, entity.isPumpkinEquipped() ? button101[1]:button101[0]));
	}

	@Override
	protected void actionPerformed(GuiButton guibutton) {
		if (!guibutton.enabled) {
			return;
		}
		switch (guibutton.id) {
		case 101:
			entity.setPumpkinEquipped(!entity.isPumpkinEquipped());
			guibutton.displayString =  entity.isPumpkinEquipped() ? button101[1]:button101[0];
			break;
		}

		super.actionPerformed(guibutton);
	}
}
