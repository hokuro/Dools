package basashi.dools.gui;

import basashi.dools.entity.EntityDool;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.passive.EntityParrot;

public class GuiDoolPause_parrot extends GuiDoolPause {
	public EntityParrot entity;

	private int variant = 0;

	public GuiDoolPause_parrot(EntityDool entityfigure) {
		super(entityfigure);
		entity = (EntityParrot)entityfigure.renderEntity;
	}

	@Override
	public void initGui() {
		super.initGui();
		variant = entity.getVariant();
		buttonList.add(new GuiButton(101, width / 2 - 140, height / 6 + 0 + 12, 80, 20, "Parrot "+variant));
	}

	@Override
	protected void actionPerformed(GuiButton guibutton) {
		if (!guibutton.enabled) {
			return;
		}
		switch (guibutton.id) {
		case 101:
			variant++;
			if (variant > 4){
				variant = 0;
			}
			entity.setVariant(variant);
			guibutton.displayString =  "Parrot "+variant;
			break;
		}

		super.actionPerformed(guibutton);
	}
}
