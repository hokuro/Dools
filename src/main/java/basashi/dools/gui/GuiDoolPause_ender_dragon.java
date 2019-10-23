package basashi.dools.gui;

import basashi.dools.entity.EntityDool;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;

public class GuiDoolPause_ender_dragon extends GuiDoolPause {

	private EnderDragonEntity entity;
	public GuiDoolPause_ender_dragon(EntityDool entityfigure) {
		super(entityfigure);
		entity = (EnderDragonEntity)entityfigure.renderEntity;
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
