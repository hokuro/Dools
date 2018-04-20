package basashi.dools.gui;

import basashi.dools.entity.EntityDool;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.monster.AbstractSkeleton;

public class GuiDoolPause_skeleton extends GuiDoolPause {
	public AbstractSkeleton entity;

	public String[] button101 ={"war","rest"};

	public GuiDoolPause_skeleton(EntityDool entityfigure) {
		super(entityfigure);
		entity = (AbstractSkeleton)entityfigure.renderEntity;
	}

	@Override
	public void initGui() {
		super.initGui();
		buttonList.add(new GuiButton(101, width / 2 - 140, height / 6 + 0 + 12, 80, 20, entity.isSwingingArms() ? button101[1]:button101[0]));
	}

	@Override
	protected void actionPerformed(GuiButton guibutton) {
		if (!guibutton.enabled) {
			return;
		}
		switch (guibutton.id) {
		case 101:
			entity.setSwingingArms(!entity.isSwingingArms());
			guibutton.displayString =  entity.isSwingingArms() ? button101[1]:button101[0];
			break;
		}

		super.actionPerformed(guibutton);
	}
}
