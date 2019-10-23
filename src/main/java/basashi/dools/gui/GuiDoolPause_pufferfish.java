package basashi.dools.gui;

import basashi.dools.entity.EntityDool;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.passive.fish.PufferfishEntity;

public class GuiDoolPause_pufferfish extends GuiDoolPause {
	private PufferfishEntity entity;
	public GuiDoolPause_pufferfish(EntityDool entityfigure) {
		super(entityfigure);
		entity = (PufferfishEntity)entityfigure.renderEntity;
	}

	@Override
	public void init() {
		super.init();
		this.addButton(new Button(width / 2 - 140, height / 6 + 0 + 12, 80, 20, entity.getPuffState() + "", (bt)->{actionPerformed(101, bt);}));
	}

	@Override
	protected void actionPerformed(int id, Button button) {
		switch(id){
		case 101:
			int state = entity.getPuffState()+1;
			if (state > 2) {state = 0;}
			entity.setPuffState(state);
			button.setMessage(entity.getPuffState() + "");

			break;
		}
		super.actionPerformed(id, button);
	}
}
