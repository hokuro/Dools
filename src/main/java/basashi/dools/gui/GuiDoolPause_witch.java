package basashi.dools.gui;

import basashi.dools.entity.EntityDool;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.monster.WitchEntity;

public class GuiDoolPause_witch extends GuiDoolPause {
	private WitchEntity entity;
	public GuiDoolPause_witch(EntityDool entityfigure) {
		super(entityfigure);
		entity = (WitchEntity)entityfigure.renderEntity;
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
