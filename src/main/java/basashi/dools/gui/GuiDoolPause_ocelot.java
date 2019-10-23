package basashi.dools.gui;

import basashi.dools.entity.EntityDool;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.passive.OcelotEntity;

public class GuiDoolPause_ocelot extends GuiDoolPause {
	private OcelotEntity entity;

	public GuiDoolPause_ocelot(EntityDool entityfigure) {
		super(entityfigure);
		entity = (OcelotEntity) targetEntity.renderEntity;
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
