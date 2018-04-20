package basashi.dools.gui;

import basashi.dools.entity.EntityDool;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.monster.EntityZombie;
public class GuiDoolPause_zombie extends GuiDoolPause {

	public EntityZombie entity;


	public GuiDoolPause_zombie(EntityDool entityfigure) {
		super(entityfigure);
		entity = (EntityZombie)entityfigure.renderEntity;
	}

	@Override
	public void initGui() {
		super.initGui();
	}

	@Override
	protected void actionPerformed(GuiButton guibutton) {
		if (!guibutton.enabled) {
			return;
		}
		switch (guibutton.id) {
		case 16:
			// 幼生態判定
			entity.setChild(!entity.isChild());
			guibutton.displayString = button16[entity.isChild() ? 1 : 0];
			return;
		}

		super.actionPerformed(guibutton);
	}

}
