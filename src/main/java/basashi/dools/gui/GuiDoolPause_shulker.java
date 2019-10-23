package basashi.dools.gui;

import basashi.dools.entity.EntityDool;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.monster.ShulkerEntity;
import net.minecraft.item.DyeColor;
import net.minecraft.nbt.CompoundNBT;

public class GuiDoolPause_shulker extends GuiDoolPause {
	private ShulkerEntity entity;

	public GuiDoolPause_shulker(EntityDool entityfigure) {
		super(entityfigure);
		entity = (ShulkerEntity) targetEntity.renderEntity;
	}

	@Override
	public void init() {
		super.init();
		DyeColor coler = entity.getColor()==null?DyeColor.WHITE:entity.getColor();
		int tick = entity.getPeekTick();
		this.addButton(new Button(width / 2 - 140, height / 6 + 0 + 12, 80, 20, "color:" + coler.getTranslationKey(), (bt)->{actionPerformed(102, bt);}));
		this.addButton(new Button(width / 2 - 140, height / 6 + 24 + 12, 80, 20, "tick:" + tick, (bt)->{actionPerformed(103, bt);}));
	}

	@Override
	protected void actionPerformed(int id, Button button) {
		CompoundNBT nbt = new CompoundNBT();
		entity.writeAdditional(nbt);
		switch(id){
		case 102:
			DyeColor color = entity.getColor()==null?DyeColor.WHITE:entity.getColor();
			nbt.putInt("Color", DyeColor.byId(color.getId()+1).getId());
			entity.readAdditional(nbt);
			button.setMessage("color:" + entity.getColor().getTranslationKey());
			break;
		case 103:
			int tick = entity.getPeekTick() + 1;
			nbt.putInt("Peek", tick);
			entity.readAdditional(nbt);
			button.setMessage("tick:" + entity.getPeekTick());
			break;
		}
		super.actionPerformed(id, button);
	}
}
