package basashi.dools.gui;

import basashi.dools.entity.EntityDool;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.passive.fish.CodEntity;

public class GuiDoolPause_cod extends GuiDoolPause {
	private CodEntity entity;

	public GuiDoolPause_cod(EntityDool entityfigure) {
		super(entityfigure);
		entity = (CodEntity)entityfigure.renderEntity;
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
