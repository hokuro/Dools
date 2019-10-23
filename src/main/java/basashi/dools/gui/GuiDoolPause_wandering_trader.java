package basashi.dools.gui;

import basashi.dools.entity.EntityDool;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.merchant.villager.WanderingTraderEntity;

public class GuiDoolPause_wandering_trader extends GuiDoolPause {
	private WanderingTraderEntity entity;
	public GuiDoolPause_wandering_trader(EntityDool entityfigure) {
		super(entityfigure);
		entity = (WanderingTraderEntity)entityfigure.renderEntity;
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
