package basashi.dools.gui;

import basashi.dools.entity.EntityDool;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.passive.MooshroomEntity;
import net.minecraft.nbt.CompoundNBT;

public class GuiDoolPause_mooshroom extends GuiDoolPause {
	private MooshroomEntity entity;
	public GuiDoolPause_mooshroom(EntityDool entityfigure) {
		super(entityfigure);
		entity = (MooshroomEntity)entityfigure.renderEntity;
	}

	@Override
	public void init() {
		super.init();
		this.addButton(new Button(width / 2 - 140, height / 6 + 0 + 12, 80, 20, entity.getMooshroomType().name(), (bt)->{actionPerformed(101, bt);}));
	}

	@Override
	protected void actionPerformed(int id, Button button) {
		switch(id){
		case 101:
			MooshroomEntity.Type tp = entity.getMooshroomType();
			CompoundNBT nbt = new CompoundNBT();
			entity.writeAdditional(nbt);
			if (tp == MooshroomEntity.Type.BROWN) {
				nbt.putString("Type", MooshroomEntity.Type.RED.name().toLowerCase());
			}else {
				nbt.putString("Type", MooshroomEntity.Type.BROWN.name().toLowerCase());
			}
			entity.readAdditional(nbt);
			button.setMessage(entity.getMooshroomType().name());
			break;
		}
		super.actionPerformed(id, button);
	}
}
