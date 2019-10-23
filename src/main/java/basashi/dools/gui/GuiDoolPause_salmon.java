package basashi.dools.gui;

import basashi.dools.entity.EntityDool;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.passive.fish.SalmonEntity;

public class GuiDoolPause_salmon extends GuiDoolPause {
	private SalmonEntity entity;

	public GuiDoolPause_salmon(EntityDool entityfigure) {
		super(entityfigure);
		entity = (SalmonEntity)entityfigure.renderEntity;
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
