package basashi.dools.gui;

import basashi.dools.entity.EntityDool;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.boss.WitherEntity;

public class GuiDoolPause_wither extends GuiDoolPause {
	public WitherEntity entity;
	public static final String button101[] = {"no invlue","invlue"};
	public static final String button102[] = {"NoArmared","Armared"};

	public GuiDoolPause_wither(EntityDool entityfigure) {
		super(entityfigure);
		entity = (WitherEntity)entityfigure.renderEntity;
	}

	@Override
	public void init() {
		super.init();
		this.addButton(new Button(width/2-140, height/6 +12, 80, 20, entity.getInvulTime()==0?button101[0]:button101[1],(bt)->{actionPerformed(101, bt);}));
		this.addButton(new Button(width/2-140, height/6 +12 + 20, 80, 20, entity.isArmored()?button102[1]:button102[0],(bt)->{actionPerformed(102, bt);}));
	}

	@Override
	protected void actionPerformed(int id, Button button) {
		switch(id){
		case 101:
			if (entity.getInvulTime() <= 0){
				entity.setInvulTime(220);
				button.setMessage(button101[1]);
			}else{
				entity.setInvulTime(0);
				button.setMessage(button101[0]);
			}
			break;
		case 102:
			if (entity.isArmored()){
				entity.setHealth(entity.getMaxHealth());
				button.setMessage(button102[0]);
			}else{
				entity.setHealth(1);
				button.setMessage(button102[1]);
			}
			break;
		}
		super.actionPerformed(id, button);
	}
}
