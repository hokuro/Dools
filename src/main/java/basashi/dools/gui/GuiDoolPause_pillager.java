package basashi.dools.gui;

import basashi.dools.entity.EntityDool;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.monster.PillagerEntity;

public class GuiDoolPause_pillager extends GuiDoolPause {
	private PillagerEntity entity;

	public GuiDoolPause_pillager(EntityDool entityfigure) {
		super(entityfigure);
		entity = (PillagerEntity)entityfigure.renderEntity;
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
