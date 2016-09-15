package basashi.dools.gui;

import basashi.dools.entity.EntityDool;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.monster.EntityGhast;

public class GuiDoolPause_Ghast extends GuiDoolPause {

	private EntityGhast eg;
	private String button102[] = { "Charge", "Fire" };


	public GuiDoolPause_Ghast(EntityDool entityfigua) {
		super(entityfigua);
		eg = (EntityGhast) targetEntity.renderEntity;
	}

	public void func_73866_w_() {
		super.func_73866_w_();
		field_146292_n.add(new GuiButton(102, field_146294_l / 2 - 140, field_146295_m / 6 + 0 + 12, 80, 20,
				button102[eg.func_110182_bF()? 0 : 1]));
	}

	protected void func_146284_a(GuiButton guibutton) {
		super.func_146284_a(guibutton);

		if (!guibutton.field_146124_l) {
			return;
		}
		switch (guibutton.field_146127_k) {
		case 102:
			if (eg.func_110182_bF()) {
				eg.func_175454_a(false);
			} else {
				eg.func_175454_a(true);
			}
			guibutton.field_146126_j = button102[eg.func_110182_bF()? 0 : 1];
			break;
		}
	}

}
