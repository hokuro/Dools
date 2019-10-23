package basashi.dools.gui;

import basashi.dools.entity.EntityDool;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.monster.GiantEntity;

public class GuiDoolPause_giant extends GuiDoolPause {
	private GiantEntity entity;
	public GuiDoolPause_giant(EntityDool entityfigure) {
		super(entityfigure);
		entity = (GiantEntity) entityfigure.renderEntity;
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
