package basashi.dools.gui;

import basashi.dools.entity.EntityDool;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.monster.PhantomEntity;

public class GuiDoolPause_phantom extends GuiDoolPause {
	private PhantomEntity entity;

	public GuiDoolPause_phantom(EntityDool entityfigure) {
		super(entityfigure);
		entity = (PhantomEntity)entityfigure.renderEntity;
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
