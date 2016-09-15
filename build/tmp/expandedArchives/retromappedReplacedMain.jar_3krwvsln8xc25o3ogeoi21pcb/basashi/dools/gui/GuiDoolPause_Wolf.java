package basashi.dools.gui;

import basashi.dools.entity.EntityDool;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.passive.EntityWolf;

public class GuiDoolPause_Wolf extends GuiDoolPause {

	private EntityWolf ew;
	private String button102[] = { "Contract", "Wild" };
	private String button103[] = { "Sitting", "Standing" };
	private String button104[] = { "Angry", "Calm" };

	public GuiDoolPause_Wolf(EntityDool entityfigure) {
		super(entityfigure);
		ew = (EntityWolf) targetEntity.renderEntity;
	}

	public void func_73866_w_() {
		super.func_73866_w_();
		field_146292_n.add(new GuiButton(102, field_146294_l / 2 - 140, field_146295_m / 6 + 0 + 12, 80, 20, button102[ew.func_70909_n() ? 0 : 1]));
		field_146292_n .add(new GuiButton(103, field_146294_l / 2 - 140, field_146295_m / 6 + 24 + 12, 80, 20, button103[ew.func_70906_o() ? 0 : 1]));
		field_146292_n.add(new GuiButton(104, field_146294_l / 2 - 140, field_146295_m / 6 + 48 + 12, 80, 20, button104[ew.func_70919_bu() ? 0 : 1]));

		field_146292_n.add(new GuiButton(150, field_146294_l / 2 - 120, field_146295_m / 6 + 72 + 12, 40, 20, String.format("%d", (int)ew.func_110143_aJ())));
		field_146292_n.add(new GuiButton(151, field_146294_l / 2 - 140, field_146295_m / 6 + 72 + 12, 20, 20, "+"));
		field_146292_n.add(new GuiButton(152, field_146294_l / 2 - 80, field_146295_m / 6 + 72 + 12, 20, 20, "-"));
	}

	protected void func_146284_a(GuiButton guibutton) {
		super.func_146284_a(guibutton);

		if (!guibutton.field_146124_l) {
			return;
		}
		switch (guibutton.field_146127_k) {
		case 102:
			ew.func_70903_f(!ew.func_70909_n());
			//ew.setOwnerId(ew.isTamed() ? "Dool" : "");
			guibutton.field_146126_j = button102[ew.func_70909_n() ? 0 : 1];
			break;

		case 103:
			ew.func_70904_g(!ew.func_70906_o());
			guibutton.field_146126_j = button103[ew.func_70906_o() ? 0 : 1];
			break;

		case 104:
			ew.func_70916_h(!ew.func_70919_bu());
			guibutton.field_146126_j = button104[ew.func_70919_bu() ? 0 : 1];
			break;
		}

		float lhealth = ew.func_110143_aJ();
		if (guibutton.field_146127_k == 150) {
			ew.func_70606_j(10F);
		}
		if (guibutton.field_146127_k == 151) {
			if (lhealth < ew.func_110138_aP())
				ew.func_70691_i(1);
		}
		if (guibutton.field_146127_k == 152) {
			if (lhealth > 1)
				ew.func_70606_j(lhealth-1);
		}
		for (int k = 0; k < field_146292_n.size(); k++) {
			GuiButton gb = (GuiButton) field_146292_n.get(k);
			if (gb.field_146127_k == 150) {
				gb.field_146126_j = String.format("%d", (int)ew.func_110143_aJ());
			}
		}

	}

}
