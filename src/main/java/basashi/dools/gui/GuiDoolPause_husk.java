package basashi.dools.gui;

import basashi.dools.entity.EntityDool;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.monster.HuskEntity;

public class GuiDoolPause_husk extends GuiDoolPause {
	private HuskEntity entity;
	public GuiDoolPause_husk(EntityDool entityfigure) {
		super(entityfigure);
		entity = (HuskEntity) targetEntity.renderEntity;
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
