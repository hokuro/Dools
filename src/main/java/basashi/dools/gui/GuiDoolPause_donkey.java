package basashi.dools.gui;

import basashi.dools.entity.EntityDool;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.passive.EntityDonkey;

public class GuiDoolPause_donkey extends GuiDoolPause {
	public EntityDonkey entity;

	public String[] button101 ={"chest on","chest off"};
	private GuiButton button101b;

	public GuiDoolPause_donkey(EntityDool entityfigure) {
		super(entityfigure);
		entity = (EntityDonkey)entityfigure.renderEntity;
	}

	@Override
	public void initGui() {
		super.initGui();
		button101b = new GuiButton(101, width / 2 - 140, height / 6 + 0 + 12, 80, 20, entity.hasChest() ? button101[1]:button101[0]);
		buttonList.add(button101b);
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