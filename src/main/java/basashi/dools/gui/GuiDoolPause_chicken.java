package basashi.dools.gui;

import basashi.dools.entity.EntityDool;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.passive.ChickenEntity;

public class GuiDoolPause_chicken extends GuiDoolPause {
	private ChickenEntity entity;
	public GuiDoolPause_chicken(EntityDool entityfigure) {
		super(entityfigure);
		entity = (ChickenEntity)entityfigure.renderEntity;
	}

	@Override
	public void init() {
		super.init();
	}

	@Override
	protected void actionPerformed(int id, Button button) {
		super.actionPerformed(id, button);
	}
}
