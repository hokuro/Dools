package basashi.dools.gui;

import basashi.dools.entity.EntityDool;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.passive.ParrotEntity;

public class GuiDoolPause_parrot extends GuiDoolPause {
	public ParrotEntity entity;
	private int variant = 0;

	public GuiDoolPause_parrot(EntityDool entityfigure) {
		super(entityfigure);
		entity = (ParrotEntity)entityfigure.renderEntity;
	}

	@Override
	public void init() {
		super.init();
		this.addButton(new Button(width / 2 - 140, height / 6 + 0 + 12, 80, 20, "Parrot "+variant, (bt)->{actionPerformed(101, bt);}));
	}

	@Override
	protected void actionPerformed(int id, Button button) {
		switch (id) {
		case 101:
			variant++;
			if (variant > 4){
				variant = 0;
			}
			entity.setVariant(variant);
			button.setMessage( "Parrot "+variant);
			break;
		}
		super.actionPerformed(id, button);
	}
}
