package basashi.dools.gui;

import java.util.UUID;

import basashi.dools.entity.EntityDool;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.item.EnumDyeColor;

public class GuiDoolPause_wolf extends GuiDoolPause {

	private EntityWolf ew;
	private String button102[] = { "Contract", "Wild" };
	private String button103[] = { "Sitting", "Standing" };
	private String button104[] = { "Angry", "Calm" };

	public GuiDoolPause_wolf(EntityDool entityfigure) {
		super(entityfigure);
		ew = (EntityWolf) targetEntity.renderEntity;
	}

	public void initGui() {
		super.initGui();
		buttonList.add(new GuiButton(105, width / 2 - 140, height / 6 + -24 + 12, 80, 20, ew.getCollarColor().getDyeColorName()));
		buttonList.add(new GuiButton(102, width / 2 - 140, height / 6 + 0 + 12, 80, 20, button102[ew.isTamed() ? 0 : 1]));
		buttonList .add(new GuiButton(103, width / 2 - 140, height / 6 + 24 + 12, 80, 20, button103[ew.isSitting() ? 0 : 1]));
		buttonList.add(new GuiButton(104, width / 2 - 140, height / 6 + 48 + 12, 80, 20, button104[ew.isAngry() ? 0 : 1]));

		buttonList.add(new GuiButton(150, width / 2 - 120, height / 6 + 72 + 12, 40, 20, String.format("%d", (int)ew.getHealth())));
		buttonList.add(new GuiButton(151, width / 2 - 140, height / 6 + 72 + 12, 20, 20, "+"));
		buttonList.add(new GuiButton(152, width / 2 - 80, height / 6 + 72 + 12, 20, 20, "-"));
	}

	protected void actionPerformed(GuiButton guibutton) {
		super.actionPerformed(guibutton);

		if (!guibutton.enabled) {
			return;
		}
		switch (guibutton.id) {
		case 102:
			boolean tame = ew.isTamed();
			if (tame){
				ew.setOwnerId(null);
			}else{
				ew.setOwnerId(new UUID(1,1));
			}
			ew.setTamed(!tame);
			guibutton.displayString = button102[ew.isTamed() ? 0 : 1];
			break;

		case 103:
			ew.setSitting(!ew.isSitting());
			guibutton.displayString = button103[ew.isSitting() ? 0 : 1];
			break;

		case 104:
			ew.setAngry(!ew.isAngry());
			guibutton.displayString = button104[ew.isAngry() ? 0 : 1];
			break;
		case 105:
			EnumDyeColor color = ew.getCollarColor();
		    ew.setCollarColor(EnumDyeColor.byDyeDamage(color.getDyeDamage()+1));
		    guibutton.displayString = ew.getCollarColor().getDyeColorName();
		    break;
		}

		float lhealth = ew.getHealth();
		if (guibutton.id == 150) {
			ew.setHealth(10F);
		}
		if (guibutton.id == 151) {
			if (lhealth < ew.getMaxHealth())
				ew.heal(1);
		}
		if (guibutton.id == 152) {
			if (lhealth > 1)
				ew.setHealth(lhealth-1);
		}
		for (int k = 0; k < buttonList.size(); k++) {
			GuiButton gb = (GuiButton) buttonList.get(k);
			if (gb.id == 150) {
				gb.displayString = String.format("%d", (int)ew.getHealth());
			}
		}

	}

}
