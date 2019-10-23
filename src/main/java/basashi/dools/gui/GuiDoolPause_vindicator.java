package basashi.dools.gui;

import basashi.dools.entity.EntityDool;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.monster.VindicatorEntity;

public class GuiDoolPause_vindicator extends GuiDoolPause {
	public VindicatorEntity entity;
	public String[] button101 ={"normal","aggressive"};

	public GuiDoolPause_vindicator(EntityDool entityfigure) {
		super(entityfigure);
		entity = (VindicatorEntity)entityfigure.renderEntity;
	}

	@Override
	public void init() {
		super.init();
		this.addButton(new Button(width / 2 - 140, height / 6 + 0 + 12, 80, 20, entity.isAggressive() ? button101[0]:button101[1],(bt)->{actionPerformed(101, bt);}));
	}

	@Override
	protected void actionPerformed(int id, Button button) {
		switch(id){
		case 101:
			entity.setAggroed(!entity.isAggressive());
			button.setMessage( entity.isAggressive() ? button101[0]:button101[1]);
			break;
		}
		super.actionPerformed(id, button);
	}
}
