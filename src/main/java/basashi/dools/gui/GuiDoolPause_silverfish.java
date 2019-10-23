package basashi.dools.gui;

import basashi.dools.entity.EntityDool;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.monster.SilverfishEntity;

public class GuiDoolPause_silverfish extends GuiDoolPause {
	private SilverfishEntity entity;
	public GuiDoolPause_silverfish(EntityDool entityfigure) {
		super(entityfigure);
		entity = (SilverfishEntity) targetEntity.renderEntity;
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
