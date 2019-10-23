package basashi.dools.gui;

import basashi.dools.entity.EntityDool;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.monster.ZombiePigmanEntity;
import net.minecraft.nbt.CompoundNBT;

public class GuiDoolPause_zombie_pigman extends GuiDoolPause {
	private ZombiePigmanEntity entity;
	private String[] button_anger101 = new String[] {"normal","angry"};

	public GuiDoolPause_zombie_pigman(EntityDool entityfigure) {
		super(entityfigure);
		entity = (ZombiePigmanEntity)entityfigure.renderEntity;
	}

	@Override
	public void init() {
		super.init();
		CompoundNBT nbt = new CompoundNBT();
		entity.writeAdditional(nbt);
		short angar = nbt.getShort("Anger");
		this.addButton(new Button(width / 2 - 140, height / 6 + 0 + 12, 80, 20, angar > 0 ? button_anger101[1]:button_anger101[0], (bt)->{actionPerformed(101, bt);}));
	}

	@Override
	protected void actionPerformed(int id, Button button) {
		switch(id){
		case 101:
			CompoundNBT nbt = new CompoundNBT();
			entity.writeAdditional(nbt);
			int angar = nbt.getShort("Anger");
			if (angar > 0) {
				angar = -1;
			} else {
				angar = 400 + Minecraft.getInstance().world.rand.nextInt(400);
			}
			nbt.putShort("Anger", (short)angar);
			entity.readAdditional(nbt);
			break;
		}
		super.actionPerformed(id, button);
	}
}
