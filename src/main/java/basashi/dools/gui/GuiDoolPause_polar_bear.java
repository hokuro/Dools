package basashi.dools.gui;

import basashi.dools.entity.EntityDool;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.passive.PolarBearEntity;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

public class GuiDoolPause_polar_bear extends GuiDoolPause {
	public PolarBearEntity entity;
	public String[] button101 = {"Stand","Shit"};

	public GuiDoolPause_polar_bear(EntityDool entityfigure) {
		super(entityfigure);
		entity = (PolarBearEntity)entityfigure.renderEntity;
	}

	@Override
	public void init() {
		super.init();
		this.addButton(new Button(width / 2 - 140, height / 6 + 0 + 12, 80, 20, entity.isStanding() ? button101[1]:button101[0], (bt)->{actionPerformed(101, bt);}));
	}

	@Override
	protected void actionPerformed(int id, Button button) {
		switch(id){
		case 101:
			entity.setStanding(!entity.isStanding());
			button.setMessage(entity.isStanding() ? button101[1]:button101[0]);

			if (entity.isStanding()){
				ObfuscationReflectionHelper.setPrivateValue(PolarBearEntity.class, entity, 6, "clientSideStandAnimation0");
				ObfuscationReflectionHelper.setPrivateValue(PolarBearEntity.class, entity, 6, "clientSideStandAnimation");
			}else{

				ObfuscationReflectionHelper.setPrivateValue(PolarBearEntity.class, entity, 0, "clientSideStandAnimation0");
				ObfuscationReflectionHelper.setPrivateValue(PolarBearEntity.class, entity, 0, "clientSideStandAnimation");
			}
			return;

		}
		super.actionPerformed(id, button);
	}
}
