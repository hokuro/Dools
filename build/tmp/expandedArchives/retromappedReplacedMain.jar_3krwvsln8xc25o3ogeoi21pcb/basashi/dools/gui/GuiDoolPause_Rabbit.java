package basashi.dools.gui;

import basashi.dools.entity.EntityDool;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.passive.EntityRabbit;

public class GuiDoolPause_Rabbit extends GuiDoolPause {
	EntityRabbit erabbit;
	private int rabbit = 0;
	private static final int[] rabtype = new int[]{0,1,2,3,4,5,99};

	public GuiDoolPause_Rabbit(EntityDool entityfigure) {
		super(entityfigure);
		// TODO 自動生成されたコンストラクター・スタブ
		erabbit = (EntityRabbit)entityfigure.renderEntity;
	}

	@Override
	public void func_73866_w_() {
		super.func_73866_w_();

		field_146292_n.add(new GuiButton(102, field_146294_l / 2 - 140,
				field_146295_m / 6 + 0 + 12, 80, 20, Integer.toString(rabtype[rabindex()])));
	}

	@Override
	protected void func_146284_a(GuiButton guibutton) {
		super.func_146284_a(guibutton);

		if (!guibutton.field_146124_l) {
			return;
		}
		switch (guibutton.field_146127_k) {
		case 102:
			int rabbit = rabindex();
			if ((rabbit+1) == rabtype.length){
				rabbit = 0;
			}else{rabbit += 1;}
			erabbit.func_175529_r(rabtype[rabbit]);
			guibutton.field_146126_j = Integer.toString(rabtype[rabbit]);
			break;
		}
	}

	private int rabindex(){
		int rab = 0;
		for (int i = 0; i < rabtype.length; i++){
			if (erabbit.func_175531_cl() == rabtype[i]){
				rab = i;
				break;
			}
		}
		return rab;
	}

}
