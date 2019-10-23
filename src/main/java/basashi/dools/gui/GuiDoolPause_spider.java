package basashi.dools.gui;

import basashi.dools.entity.EntityDool;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.monster.SpiderEntity;

public class GuiDoolPause_spider extends GuiDoolPause {
	private SpiderEntity entity;
	public GuiDoolPause_spider(EntityDool entityfigure) {
		super(entityfigure);
		entity = (SpiderEntity)entityfigure.renderEntity;
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
