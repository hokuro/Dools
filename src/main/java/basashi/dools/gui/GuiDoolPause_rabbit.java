package basashi.dools.gui;

import basashi.dools.entity.EntityDool;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.passive.RabbitEntity;

public class GuiDoolPause_rabbit extends GuiDoolPause {

	private RabbitEntity entity;
	private int rabbit = 0;
	private static final int[] rabtype = new int[]{0,1,2,3,4,5,99};

	public GuiDoolPause_rabbit(EntityDool entityfigure) {
		super(entityfigure);
		entity = (RabbitEntity)entityfigure.renderEntity;
	}

	@Override
	public void init() {
		super.init();
		this.addButton(new Button(width / 2 - 140,height / 6 + 0 + 12, 80, 20, Integer.toString(rabtype[rabindex()]), (bt)->{actionPerformed(102, bt);}));
	}

	@Override
	protected void actionPerformed(int id, Button button) {
		switch(id){
		case 102:
			int rabbit = rabindex();
			if ((rabbit+1) == rabtype.length){
				rabbit = 0;
			}else{rabbit += 1;}
			entity.setRabbitType(rabtype[rabbit]);
			button.setMessage(Integer.toString(rabtype[rabbit]));
			break;
		}
		super.actionPerformed(id, button);
	}


	private int rabindex(){
		int rab = 0;
		for (int i = 0; i < rabtype.length; i++){
			if (entity.getRabbitType() == rabtype[i]){
				rab = i;
				break;
			}
		}
		return rab;
	}

}
