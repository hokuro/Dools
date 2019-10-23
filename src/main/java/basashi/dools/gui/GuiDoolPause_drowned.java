package basashi.dools.gui;

import basashi.dools.entity.EntityDool;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.monster.DrownedEntity;

public class GuiDoolPause_drowned extends GuiDoolPause {

	private DrownedEntity entity;
	public GuiDoolPause_drowned(EntityDool entityfigure) {
		super(entityfigure);
		entity = (DrownedEntity)entityfigure.renderEntity;
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
