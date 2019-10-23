package basashi.dools.gui;

import basashi.dools.entity.EntityDool;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.passive.FoxEntity;
import net.minecraft.nbt.CompoundNBT;

public class GuiDoolPause_fox extends GuiDoolPause {
	private FoxEntity entity;

	public GuiDoolPause_fox(EntityDool entityfigure) {
		super(entityfigure);
		entity = (FoxEntity)entityfigure.renderEntity;
	}

	@Override
	public void init() {
		super.init();
		FoxEntity.Type foxtype = entity.getVariantType();
		this.addButton(new Button(width / 2 - 140, height / 6 + 0 + 12, 80, 20, foxtype.getName(), (bt)->{actionPerformed(101, bt);}));
	}

	@Override
	protected void actionPerformed(int id, Button button) {
		switch(id){
		case 101:
			int work = entity.getVariantType().getIndex()+1;
			if (work >= FoxEntity.Type.values().length) {work = 0;}
			FoxEntity.Type foxtype = FoxEntity.Type.getTypeByIndex(work);
			CompoundNBT nbt = new CompoundNBT();
			entity.writeAdditional(nbt);
			nbt.putString("Type", foxtype.getName());
			entity.readAdditional(nbt);
			break;
		}
		super.actionPerformed(id, button);
	}
}
