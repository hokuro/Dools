package basashi.dools.gui;

import basashi.dools.entity.EntityDool;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.passive.horse.MuleEntity;

public class GuiDoolPause_mule extends GuiDoolPause {
	public MuleEntity entity;

	public String[] button101 ={"chest on","chest off"};

	public GuiDoolPause_mule(EntityDool entityfigure) {
		super(entityfigure);
		entity = (MuleEntity)entityfigure.renderEntity;
	}

	@Override
	public void init() {
		super.init();
		this.addButton(new Button(width / 2 - 140, height / 6 + 0 + 12, 80, 20, entity.hasChest() ? button101[1]:button101[0], (bt)->{actionPerformed(101, bt);}));
	}

	@Override
	protected void actionPerformed(int id, Button button) {
		switch (id) {
		case 101:
			entity.setChested(!entity.hasChest());
			button.setMessage( entity.hasChest() ? button101[1]:button101[0]);
			break;
		}

		super.actionPerformed(id, button);
	}
}
