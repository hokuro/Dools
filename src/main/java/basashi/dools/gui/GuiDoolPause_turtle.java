package basashi.dools.gui;

import basashi.dools.entity.EntityDool;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.passive.TurtleEntity;

public class GuiDoolPause_turtle extends GuiDoolPause {
	private TurtleEntity entity;
	public GuiDoolPause_turtle(EntityDool entityfigure) {
		super(entityfigure);
		entity = (TurtleEntity)entityfigure.renderEntity;
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
