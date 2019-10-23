package basashi.dools.gui;

import basashi.dools.entity.EntityDool;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.passive.SnowGolemEntity;

public class GuiDoolPause_snow_golem extends GuiDoolPause {
	public SnowGolemEntity entity;

	public String[] button101 ={"Mask On","Mask Off"};

	public GuiDoolPause_snow_golem(EntityDool entityfigure) {
		super(entityfigure);
		entity = (SnowGolemEntity)entityfigure.renderEntity;
	}

	@Override
	public void init() {
		super.init();
		this.addButton(new Button(width / 2 - 140, height / 6 + 0 + 12, 80, 20, entity.isPumpkinEquipped() ? button101[1]:button101[0], (bt)->{actionPerformed(101, bt);}));
	}

	@Override
	protected void actionPerformed(int id, Button button) {
		switch(id){
		case 101:
			entity.setPumpkinEquipped(!entity.isPumpkinEquipped());
			button.setMessage( entity.isPumpkinEquipped() ? button101[1]:button101[0]);
			break;
		}
		super.actionPerformed(id, button);
	}
}
