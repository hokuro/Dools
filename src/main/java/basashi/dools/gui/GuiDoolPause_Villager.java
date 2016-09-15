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
	public void initGui() {
		super.initGui();

		buttonList.add(new GuiButton(100, width / 2 - 140, height / 6 + 0 + 12, 80, 20,
				Profession(efvillager.getProfession())));
	}

	@Override
	protected void actionPerformed(GuiButton guibutton) {
		super.actionPerformed(guibutton);

		if (!guibutton.enabled) {
			return;
		}
		switch (guibutton.id) {
		case 100:
			// スキンのロード元
			int li = efvillager.getProfession() + 1;

			if (li >= VillagerRegistry.instance().getRegistry().getKeys().size()) {
				li = 0;
			}
			efvillager.setProfession(li);
			guibutton.displayString = Profession(li);
			break;
		}
	}

	protected String Profession(int ind){
		return VillagerRegistry.instance().getRegistry().getValues().get(ind).getRegistryName().getResourcePath();
	}

}
