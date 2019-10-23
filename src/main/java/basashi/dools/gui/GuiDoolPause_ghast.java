package basashi.dools.gui;

import basashi.dools.entity.EntityDool;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.monster.GhastEntity;

public class GuiDoolPause_ghast extends GuiDoolPause {

	private GhastEntity entity;
	private String button102[] = { "Charge", "Fire" };
	public GuiDoolPause_ghast(EntityDool entityfigure) {
		super(entityfigure);
		entity = (GhastEntity) entityfigure.renderEntity;
	}

	@Override
	public void init() {
		super.init();
		this.addButton(new Button(width / 2 - 140, height / 6 + 0 + 12, 80, 20, button102[entity.isAttacking()? 0 : 1], (bt)->{actionPerformed(102,bt);}));
	}

	@Override
	protected void actionPerformed(int id, Button button) {
		switch(id){
		case 102:
			if (entity.isAttacking()) {
				entity.setAttacking(false);
			} else {
				entity.setAttacking(true);
			}
			button.setMessage(button102[entity.isAttacking()? 0 : 1]);
			break;
		}
		super.actionPerformed(id, button);
	}
}
