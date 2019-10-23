package basashi.dools.gui;

import basashi.dools.entity.EntityDool;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.monster.GuardianEntity;

public class GuiDoolPause_guardian extends GuiDoolPause {
	private GuardianEntity entity;
	public GuiDoolPause_guardian(EntityDool entityfigure) {
		super(entityfigure);
		entity = (GuardianEntity) entityfigure.renderEntity;
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
