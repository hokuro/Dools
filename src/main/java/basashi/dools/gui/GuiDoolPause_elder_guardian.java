package basashi.dools.gui;

import basashi.dools.entity.EntityDool;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.monster.ElderGuardianEntity;

public class GuiDoolPause_elder_guardian extends GuiDoolPause {

	private ElderGuardianEntity entity;
	public GuiDoolPause_elder_guardian(EntityDool entityfigure) {
		super(entityfigure);
		entity = (ElderGuardianEntity)entityfigure.renderEntity;
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
