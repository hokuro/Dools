package basashi.dools.gui;

import basashi.dools.entity.EntityDool;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.passive.fish.TropicalFishEntity;

public class GuiDoolPause_tropical_fish extends GuiDoolPause {
	private TropicalFishEntity entity;
	private int index;
	public GuiDoolPause_tropical_fish(EntityDool entityfigure) {
		super(entityfigure);
		entity = (TropicalFishEntity)entityfigure.renderEntity;
	}

	@Override
	public void init() {
		super.init();
		index = 0;
		for (int val : TropicalFishEntity.SPECIAL_VARIANTS) {
			if (val == entity.getVariant()) {
				break;
			}
			index++;
		}
		this.addButton(new Button(width / 2 - 140, height / 6 + 0 + 12, 80, 20, index + "", (bt)->{actionPerformed(101, bt);}));
	}

	@Override
	protected void actionPerformed(int id, Button button) {
		switch(id){
		case 101:
			index++;
			if (index >= TropicalFishEntity.SPECIAL_VARIANTS.length) {
				index = 0;
			}
			entity.setVariant(TropicalFishEntity.SPECIAL_VARIANTS[index]);
			button.setMessage(index + "");
			break;
		}
		super.actionPerformed(id, button);
	}
}
