package basashi.dools.gui;

import basashi.dools.entity.EntityDool;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.passive.horse.TraderLlamaEntity;

public class GuiDoolPause_trader_llama extends GuiDoolPause_llama {
	private TraderLlamaEntity entity;
	public GuiDoolPause_trader_llama(EntityDool entityfigure) {
		super(entityfigure);
		entity = (TraderLlamaEntity)entityfigure.renderEntity;
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
