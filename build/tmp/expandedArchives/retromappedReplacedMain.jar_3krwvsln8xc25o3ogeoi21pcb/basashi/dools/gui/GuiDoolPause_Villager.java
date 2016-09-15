package basashi.dools.gui;

import basashi.dools.entity.EntityDool;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraftforge.fml.common.registry.VillagerRegistry;

public class GuiDoolPause_Villager extends GuiDoolPause {

	public EntityVillager efvillager;
	protected static String sb100[] = { "farmer", "librarian", "priest", "smith", "butcher", "villager" };


	public GuiDoolPause_Villager(EntityDool entityfigure) {
		super(entityfigure);
		efvillager = (EntityVillager)entityfigure.renderEntity;
	}

	@Override
	public void func_73866_w_() {
		super.func_73866_w_();

		field_146292_n.add(new GuiButton(100, field_146294_l / 2 - 140, field_146295_m / 6 + 0 + 12, 80, 20,
				Profession(efvillager.func_70946_n())));
	}

	@Override
	protected void func_146284_a(GuiButton guibutton) {
		super.func_146284_a(guibutton);

		if (!guibutton.field_146124_l) {
			return;
		}
		switch (guibutton.field_146127_k) {
		case 100:
			// スキンのロード元
			int li = efvillager.func_70946_n() + 1;

			if (li >= VillagerRegistry.instance().getRegistry().getKeys().size()) {
				li = 0;
			}
			efvillager.func_70938_b(li);
			guibutton.field_146126_j = Profession(li);
			break;
		}
	}

	protected String Profession(int ind){
		return VillagerRegistry.instance().getRegistry().getValues().get(ind).getRegistryName().func_110623_a();
	}

}
