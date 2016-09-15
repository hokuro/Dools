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

	public void func_73866_w_() {
		super.func_73866_w_();
		field_146292_n.add(new GuiButton(102, field_146294_l / 2 - 140, field_146295_m / 6 + 0 + 12, 80, 20,
				button102[es.func_70892_o() ? 0 : 1]));
		field_146292_n.add(new GuiButton(103, field_146294_l / 2 - 140, field_146295_m / 6 + 24 + 12, 80, 20,
				es.func_175509_cj().name()));
	}

	protected void func_146284_a(GuiButton guibutton) {
		super.func_146284_a(guibutton);

		if (!guibutton.field_146124_l) {
			return;
		}
		switch (guibutton.field_146127_k) {
		case 102:
			es.func_70893_e(!es.func_70892_o());
			guibutton.field_146126_j = button102[es.func_70892_o() ? 0 : 1];
			break;

		case 103:
			if (es.func_175509_cj().ordinal() == 15) {
				es.func_175512_b(EnumDyeColor.values()[0]);
			} else {
				es.func_175512_b(EnumDyeColor.values()[es.func_175509_cj().ordinal() + 1]);
			}
			guibutton.field_146126_j = es.func_175509_cj().name();
			break;
		}
	}

}
