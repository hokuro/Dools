package basashi.dools.gui;

import basashi.dools.entity.EntityDool;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.passive.EntityOcelot;

public class GuiDoolPause_Ozelot extends GuiDoolPause {

	private EntityOcelot eo;
	private String button102[] = { "Contract", "Wild" };
	private String button103[] = { "Sitting", "Standing" };
	private String button104[] = { "ozelot", "black", "red", "siamese" };

	public GuiDoolPause_Ozelot(EntityDool entityfigure) {
		super(entityfigure);
		eo = (EntityOcelot) targetEntity.renderEntity;
	}

	@Override
	public void func_73866_w_() {
		super.func_73866_w_();
		field_146292_n.add(new GuiButton(102, field_146294_l / 2 - 140,
				field_146295_m / 6 + 0 + 12, 80, 20, button102[eo.func_70909_n() ? 0 : 1]));
		field_146292_n
				.add(new GuiButton(103, field_146294_l / 2 - 140, field_146295_m / 6 + 24 + 12,
						80, 20, button103[eo.func_70906_o() ? 0 : 1]));
		field_146292_n.add(new GuiButton(104, field_146294_l / 2 - 140,
				field_146295_m / 6 + 48 + 12, 80, 20, button104[eo.func_70913_u()]));
	}

	@Override
	protected void func_146284_a(GuiButton guibutton) {
		super.func_146284_a(guibutton);

		if (!guibutton.field_146124_l) {
			return;
		}
		switch (guibutton.field_146127_k) {
		case 102:
			eo.func_70903_f(!eo.func_70909_n());
			//eo.setOwnerId(eo.isTamed() ? new UUID(1,1) : "");
			guibutton.field_146126_j = button102[eo.func_70909_n() ? 0 : 1];
			break;

		case 103:
			eo.func_70904_g(!eo.func_70906_o());
			guibutton.field_146126_j = button103[eo.func_70906_o() ? 0 : 1];
			break;

		case 104:
			eo.func_70912_b((eo.func_70913_u() + 1) % button104.length);
			guibutton.field_146126_j = button104[(int) eo.func_70913_u()];
			break;
		}
	}

}
