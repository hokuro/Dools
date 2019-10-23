package basashi.dools.gui;

import basashi.dools.entity.EntityDool;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.monster.AbstractSkeletonEntity;

public class GuiDoolPause_skeleton extends GuiDoolPause {
	public AbstractSkeletonEntity entity;

	public String[] button101 ={"war","rest"};

	public GuiDoolPause_skeleton(EntityDool entityfigure) {
		super(entityfigure);
		entity = (AbstractSkeletonEntity)entityfigure.renderEntity;
	}

	@Override
	public void init() {
		super.init();
		this.addButton(new Button(width / 2 - 140, height / 6 + 0 + 12, 80, 20, entity.isAggressive() ? button101[1]:button101[0], (bt)->{actionPerformed(101, bt);}));
	}

	@Override
	protected void actionPerformed(int id, Button button) {
		switch(id){
		case 101:
			entity.setAggroed(!entity.isAggressive());
			button.setMessage( entity.isAggressive() ? button101[1]:button101[0]);
			break;
		}
		super.actionPerformed(id, button);
	}
}
