package basashi.dools.gui;

import basashi.dools.entity.EntityDool;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.monster.EntityGhast;

public class GuiDoolPause_ghast extends GuiDoolPause {

	private EntityGhast eg;
	private String button102[] = { "Charge", "Fire" };


	public GuiDoolPause_ghast(EntityDool entityfigua) {
		super(entityfigua);
		eg = (EntityGhast) targetEntity.renderEntity;
	}

	public void initGui() {
		super.initGui();
		buttonList.add(new GuiButton(102, width / 2 - 140, height / 6 + 0 + 12, 80, 20,
				button102[eg.isAttacking()? 0 : 1]));
	}

	protected void actionPerformed(GuiButton guibutton) {
		super.actionPerformed(guibutton);

		if (!guibutton.enabled) {
			return;
		}
		switch (guibutton.id) {
		case 102:
			if (eg.isAttacking()) {
				eg.setAttacking(false);
			} else {
				eg.setAttacking(true);
			}
			guibutton.displayString = button102[eg.isAttacking()? 0 : 1];
			break;
		}
	}

}
