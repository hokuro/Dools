package basashi.dools.gui;

import basashi.dools.entity.EntityDool;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.monster.MagmaCubeEntity;
import net.minecraft.nbt.CompoundNBT;

public class GuiDoolPause_magma_cube extends GuiDoolPause {

	private MagmaCubeEntity entity;
	public GuiDoolPause_magma_cube(EntityDool entityfigure) {
		super(entityfigure);
		entity = (MagmaCubeEntity)entityfigure.renderEntity;
	}

	@Override
	public void init() {
		super.init();
		int size = entity.getSlimeSize();
		this.addButton(new Button(width / 2 - 140, height / 6 + 0 + 12, 80, 20, size + "", (bt)->{actionPerformed(101, bt);}));
		this.addButton(new Button(width / 2 - 140, height / 6 + 24 + 12, 80, 20, size + "", (bt)->{actionPerformed(102, bt);}));
	}

	@Override
	protected void actionPerformed(int id, Button button) {
		int size;
		CompoundNBT nbt;
		switch(id){
		case 101:
			size = entity.getSlimeSize() + 1;
			nbt = new CompoundNBT();
			entity.writeAdditional(nbt);
			nbt.putInt("Size", size);
			entity.readAdditional(nbt);
			button.setMessage(entity.getSlimeSize() + "");
			break;
		case 102:
			size = entity.getSlimeSize() - 1;
			nbt = new CompoundNBT();
			entity.writeAdditional(nbt);
			nbt.putInt("Size", size);
			entity.readAdditional(nbt);
			button.setMessage(entity.getSlimeSize() + "");
			break;
		}
		super.actionPerformed(id, button);
	}
}
