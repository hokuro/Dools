package basashi.dools.gui;

import basashi.dools.entity.EntityDool;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.monster.EndermiteEntity;

public class GuiDoolPause_endermite extends GuiDoolPause {
	private EndermiteEntity entity;

	public GuiDoolPause_endermite(EntityDool entityfigure) {
		super(entityfigure);
		entity = (EndermiteEntity) targetEntity.renderEntity;
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
