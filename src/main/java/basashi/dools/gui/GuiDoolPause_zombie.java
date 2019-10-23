package basashi.dools.gui;

import basashi.dools.entity.EntityDool;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.monster.ZombieEntity;

public class GuiDoolPause_zombie extends GuiDoolPause {

	public ZombieEntity entity;
	public GuiDoolPause_zombie(EntityDool entityfigure) {
		super(entityfigure);
		entity = (ZombieEntity)entityfigure.renderEntity;
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
