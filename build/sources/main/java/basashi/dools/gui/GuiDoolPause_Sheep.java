package basashi.dools.gui;

import basashi.dools.entity.EntityDool;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.item.EnumDyeColor;

public class GuiDoolPause_Sheep extends GuiDoolPause {

	private EntitySheep es;
	private String button102[] = { "Fullfrontal", "Sheared" };


	public GuiDoolPause_Sheep(EntityDool entityfigua) {
		super(entityfigua);
		es = (EntitySheep) targetEntity.renderEntity;
	}

	public void initGui() {
		super.initGui();
		buttonList.add(new GuiButton(102, width / 2 - 140, height / 6 + 0 + 12, 80, 20,
				button102[es.getSheared() ? 0 : 1]));
		buttonList.add(new GuiButton(103, width / 2 - 140, height / 6 + 24 + 12, 80, 20,
				es.getFleeceColor().name()));
	}

	protected void actionPerformed(GuiButton guibutton) {
		super.actionPerformed(guibutton);

		if (!guibutton.enabled) {
			return;
		}
		switch (guibutton.id) {
		case 102:
			es.setSheared(!es.getSheared());
			guibutton.displayString = button102[es.getSheared() ? 0 : 1];
			break;

		case 103:
			if (es.getFleeceColor().ordinal() == 15) {
				es.setFleeceColor(EnumDyeColor.values()[0]);
			} else {
				es.setFleeceColor(EnumDyeColor.values()[es.getFleeceColor().ordinal() + 1]);
			}
			guibutton.displayString = es.getFleeceColor().name();
			break;
		}
	}

}
