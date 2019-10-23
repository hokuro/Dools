package basashi.dools.gui;

import basashi.dools.entity.EntityDool;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.nbt.CompoundNBT;

public class GuiDoolPause_creeper extends GuiDoolPause {

	public CreeperEntity entity;

	public String[] button_power101 ={"powerd on","powerd off"};
	public GuiDoolPause_creeper(EntityDool entityfigure) {
		super(entityfigure);
		entity = (CreeperEntity)entityfigure.renderEntity;
	}

	@Override
	public void init() {
		super.init();
		this.addButton(new Button(width / 2 - 140, height / 6 + 0 + 12, 80, 20, entity.getPowered() ? button_power101[1]:button_power101[0], (bt)->{actionPerformed(101, bt);}));
	}

	@Override
	protected void actionPerformed(int id, Button button) {
		CompoundNBT nbt;
		switch(id){
		case 101:
			boolean power = entity.getPowered();
			nbt = new CompoundNBT();
			entity.writeAdditional(nbt);
			nbt.putBoolean("powered",!power);
			entity.readAdditional(nbt);
			button.setMessage( entity.getPowered() ? button_power101[1]:button_power101[0]);
			break;
		}
		super.actionPerformed(id, button);
	}
}
