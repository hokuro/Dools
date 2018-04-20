package basashi.dools.gui;

import basashi.dools.entity.EntityDool;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.boss.EntityWither;

public class GuiDoolPause_wither extends GuiDoolPause {
	public EntityWither wither;
	public static final String button101[] = {"no invlue","invlue"};
	public static final String button102[] = {"NoArmared","Armared"};


	public GuiDoolPause_wither(EntityDool entityfigure) {
		super(entityfigure);
		wither = (EntityWither)entityfigure.renderEntity;
	}

	@Override
	public void initGui(){
		super.initGui();
		buttonList.add(new GuiButton(101, width/2-140, height/6 +12, 80, 20, wither.getInvulTime()==0?button101[0]:button101[1]));
		buttonList.add(new GuiButton(102, width/2-140, height/6 +12 + 20, 80, 20, wither.isArmored()?button102[1]:button102[0]));
	}

	@Override
	protected void actionPerformed(GuiButton guibutton){
		if (!guibutton.enabled){return;}

		switch(guibutton.id){
		case 101:
			if (wither.getInvulTime() <= 0){
				wither.setInvulTime(220);
				guibutton.displayString = button101[1];
			}else{
				wither.setInvulTime(0);
				guibutton.displayString = button101[0];
			}
			break;
		case 102:
			if (wither.isArmored()){
				wither.setHealth(wither.getMaxHealth());
				guibutton.displayString = button102[0];
			}else{
				wither.setHealth(1);
				guibutton.displayString = button102[1];
			}
			break;
		}
		super.actionPerformed(guibutton);
	}

}
