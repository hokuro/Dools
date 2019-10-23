package basashi.dools.gui;

import basashi.dools.entity.EntityDool;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.monster.RavagerEntity;

public class GuiDoolPause_ravager extends GuiDoolPause {
	private RavagerEntity entity;

	public GuiDoolPause_ravager(EntityDool entityfigure) {
		super(entityfigure);
		entity = (RavagerEntity)entityfigure.renderEntity;
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
