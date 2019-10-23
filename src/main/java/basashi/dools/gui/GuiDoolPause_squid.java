package basashi.dools.gui;

import basashi.dools.entity.EntityDool;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.passive.SquidEntity;

public class GuiDoolPause_squid extends GuiDoolPause {
	private SquidEntity entity;
	public GuiDoolPause_squid(EntityDool entityfigure) {
		super(entityfigure);
		entity = (SquidEntity)entityfigure.renderEntity;
	}

	@Override
	public void init() {
		super.init();
	}

	@Override
	protected void actionPerformed(int id, Button button) {
		switch(id){

		}
		super.actionPerformed(id, button);
	}
}
