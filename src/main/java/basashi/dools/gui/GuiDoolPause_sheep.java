package basashi.dools.gui;

import basashi.dools.entity.EntityDool;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.text.TextComponentTranslation;

public class GuiDoolPause_sheep extends GuiDoolPause {

	public static final String customname = "jeb_";
	private EntitySheep es;
	private String button102[] = { "Fullfrontal", "Sheared" };


	public GuiDoolPause_sheep(EntityDool entityfigua) {
		super(entityfigua);
		es = (EntitySheep) targetEntity.renderEntity;
	}

	public void initGui() {
		super.initGui();
		GuiButton b1 = new GuiButton(102, width / 2 - 140, height / 6 + 0 + 12, 80, 20, button102[es.getSheared() ? 0 : 1]) {
    		@Override
    		public void onClick(double mouseX, double moudeY){
    			actionPerformed(this);
    		}
    	};
    	GuiButton b2 = new GuiButton(103, width / 2 - 140, height / 6 + 24 + 12, 80, 20, es.getFleeceColor().name()) {
    		@Override
    		public void onClick(double mouseX, double moudeY){
    			actionPerformed(this);
    		}
    	};
    	GuiButton b3 = new GuiButton(104, width / 2 - 140, height / 6 + 48 + 12, 80, 20, es.hasCustomName()?"None":customname) {
    		@Override
    		public void onClick(double mouseX, double moudeY){
    			actionPerformed(this);
    		}
    	};

    	buttons.add(b1);
    	buttons.add(b2);
    	buttons.add(b3);
    	this.children.addAll(buttons);
	}

	protected void actionPerformed(GuiButton guibutton) {
		super.actionPerformed(guibutton);

		if (!guibutton.enabled) {
			return;
		}
		switch (guibutton.id) {
		case 102:
			es.setSheared(!es.getSheared());
			guibutton.displayString = button102[es.getSheared() ? 0 : 1];
			break;

		case 103:
			if (es.getFleeceColor().ordinal() == 15) {
				es.setFleeceColor(EnumDyeColor.values()[0]);
			} else {
				es.setFleeceColor(EnumDyeColor.values()[es.getFleeceColor().ordinal() + 1]);
			}
			guibutton.displayString = es.getFleeceColor().name();
			break;
		case 104:
			if (es.hasCustomName()){
				es.setCustomName(new TextComponentTranslation(""));
			}else{
				es.setCustomName(new TextComponentTranslation(customname));
			}
			guibutton.displayString = es.hasCustomName()?"None":customname;
		}
	}

}
