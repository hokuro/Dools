package basashi.dools.gui;

import basashi.dools.entity.EntityDool;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.monster.VexEntity;

public class GuiDoolPause_vex extends GuiDoolPause {
	private VexEntity entity;
	public String[] button101 ={"Normal","Angree"};
	public GuiDoolPause_vex(EntityDool entityfigure) {
		super(entityfigure);
		entity = (VexEntity)entityfigure.renderEntity;
	}

	@Override
	public void init() {
		super.init();
		this.addButton(new Button(width / 2 - 140, height / 6 + 0 + 12, 80, 20, entity.isCharging() ? button101[0]:button101[1], (bt)->{actionPerformed(101, bt);}));
	}

	@Override
	protected void actionPerformed(int id, Button button) {
		switch(id){
		case 101:
			entity.setCharging(!entity.isCharging());
			button.setMessage( entity.isCharging() ? button101[0]:button101[1]);
			break;
		}
		super.actionPerformed(id, button);
	}
}
