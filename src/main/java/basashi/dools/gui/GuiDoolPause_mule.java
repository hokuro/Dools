package basashi.dools.gui;

import basashi.dools.entity.EntityDool;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.passive.EntityMule;

public class GuiDoolPause_mule extends GuiDoolPause {
	public EntityMule entity;

	public String[] button101 ={"chest on","chest off"};
	private GuiButton button101b;

	public GuiDoolPause_mule(EntityDool entityfigure) {
		super(entityfigure);
		entity = (EntityMule)entityfigure.renderEntity;
	}

	@Override
	public void initGui() {
		super.initGui();
		GuiButton b1 = new GuiButton(101, width / 2 - 140, height / 6 + 0 + 12, 80, 20, entity.hasChest() ? button101[1]:button101[0]) {
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
		if (!guibutton.enabled) {
			return;
		}
		switch (guibutton.id) {
		case 101:
			entity.setChested(!entity.hasChest());
			guibutton.displayString =  entity.hasChest() ? button101[1]:button101[0];
			break;
		}

		super.actionPerformed(guibutton);

		if (entity.isChild()){
			entity.setChested(false);
			button101b.displayString = button101[0];
			button101b.enabled = false;
		}else{
			button101b.enabled = true;
		}
	}
}
