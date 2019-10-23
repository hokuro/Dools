package basashi.dools.gui;

import basashi.dools.entity.EntityDool;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.passive.DolphinEntity;

public class GuiDoolPause_dolphin extends GuiDoolPause {
	private DolphinEntity entity;

	public GuiDoolPause_dolphin(EntityDool entityfigure) {
		super(entityfigure);
		entity = (DolphinEntity)entityfigure.renderEntity;
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
