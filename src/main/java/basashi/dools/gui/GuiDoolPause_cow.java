package basashi.dools.gui;

import basashi.dools.entity.EntityDool;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.passive.CowEntity;

public class GuiDoolPause_cow extends GuiDoolPause {
	private CowEntity entity;

	public GuiDoolPause_cow(EntityDool entityfigure) {
		super(entityfigure);
		entity = (CowEntity)entityfigure.renderEntity;
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
