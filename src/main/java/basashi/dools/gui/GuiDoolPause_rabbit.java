package basashi.dools.gui;

import basashi.dools.entity.EntityDool;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.passive.EntityRabbit;

public class GuiDoolPause_rabbit extends GuiDoolPause {
	EntityRabbit erabbit;
	private int rabbit = 0;
	private static final int[] rabtype = new int[]{0,1,2,3,4,5,99};

	public GuiDoolPause_rabbit(EntityDool entityfigure) {
		super(entityfigure);
		// TODO 自動生成されたコンストラクター・スタブ
		erabbit = (EntityRabbit)entityfigure.renderEntity;
	}

	@Override
	public void initGui() {
		super.initGui();

		GuiButton b1 = new GuiButton(102, width / 2 - 140,height / 6 + 0 + 12, 80, 20, Integer.toString(rabtype[rabindex()])) {
    		@Override
    		public void onClick(double mouseX, double moudeY){
    			actionPerformed(this);
    		}
    	};
    	buttons.add(b1);
    	this.children.addAll(buttons);
	}

	@Override
	protected void actionPerformed(GuiButton guibutton) {
		super.actionPerformed(guibutton);

		if (!guibutton.enabled) {
			return;
		}
		switch (guibutton.id) {
		case 102:
			int rabbit = rabindex();
			if ((rabbit+1) == rabtype.length){
				rabbit = 0;
			}else{rabbit += 1;}
			erabbit.setRabbitType(rabtype[rabbit]);
			guibutton.displayString = Integer.toString(rabtype[rabbit]);
			break;
		}
	}

	private int rabindex(){
		int rab = 0;
		for (int i = 0; i < rabtype.length; i++){
			if (erabbit.getRabbitType() == rabtype[i]){
				rab = i;
				break;
			}
		}
		return rab;
	}

}
