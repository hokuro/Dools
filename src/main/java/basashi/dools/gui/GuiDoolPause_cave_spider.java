package basashi.dools.gui;

import basashi.dools.entity.EntityDool;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.monster.CaveSpiderEntity;

public class GuiDoolPause_cave_spider extends GuiDoolPause {
	private CaveSpiderEntity entity;

	public GuiDoolPause_cave_spider(EntityDool entityfigure) {
		super(entityfigure);
		entity = (CaveSpiderEntity)entityfigure.renderEntity;
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
