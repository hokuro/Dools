package basashi.dools.gui;

import basashi.dools.core.Dools;
import basashi.dools.entity.EntityDool;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.monster.EntityPolarBear;

public class GuiDoolPause_polar_bear extends GuiDoolPause {
	public EntityPolarBear entity;
	public String[] button101 = {"Stand","Shit"};


	public GuiDoolPause_polar_bear(EntityDool entityfigure) {
		super(entityfigure);
		entity = (EntityPolarBear)entityfigure.renderEntity;
	}

	@Override
	public void initGui() {
		super.initGui();
		buttonList.add(new GuiButton(101, width / 2 - 140, height / 6 + 0 + 12, 80, 20, entity.isStanding() ? button101[1]:button101[0]));
}

	@Override
	protected void actionPerformed(GuiButton guibutton) {
		if (!guibutton.enabled) {
			return;
		}
		switch (guibutton.id) {
		case 101:
			entity.setStanding(!entity.isStanding());
			guibutton.displayString = entity.isStanding() ? button101[1]:button101[0];

			if (entity.isStanding()){
				Dools.setPrivateValue(EntityPolarBear.class, entity, 6, "clientSideStandAnimation0");
				Dools.setPrivateValue(EntityPolarBear.class, entity, 6, "clientSideStandAnimation");
			}else{

				Dools.setPrivateValue(EntityPolarBear.class, entity, 0, "clientSideStandAnimation0");
				Dools.setPrivateValue(EntityPolarBear.class, entity, 0, "clientSideStandAnimation");
			}
			return;
		}

		super.actionPerformed(guibutton);
	}

}
