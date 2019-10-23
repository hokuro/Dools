package basashi.dools.gui;

import basashi.dools.entity.EntityDool;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.passive.horse.DonkeyEntity;

public class GuiDoolPause_donkey extends GuiDoolPause {

	public DonkeyEntity entity;
	public String[] button101 ={"chest on","chest off"};

	private Button button101b;
	public GuiDoolPause_donkey(EntityDool entityfigure) {
		super(entityfigure);
		entity = (DonkeyEntity)entityfigure.renderEntity;
	}

	@Override
	public void init() {
		super.init();
		button101b = new Button(width / 2 - 140, height / 6 + 0 + 12, 80, 20, entity.hasChest() ? button101[1]:button101[0], (bt)->{actionPerformed(101, bt);});
		this.addButton(button101b);
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
