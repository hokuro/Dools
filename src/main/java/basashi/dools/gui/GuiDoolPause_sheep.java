package basashi.dools.gui;

import basashi.dools.entity.EntityDool;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.item.DyeColor;
import net.minecraft.util.text.TranslationTextComponent;

public class GuiDoolPause_sheep extends GuiDoolPause {
	public static final String customname = "jeb_";
	private SheepEntity entity;
	private String button102[] = { "Fullfrontal", "Sheared" };

	public GuiDoolPause_sheep(EntityDool entityfigure) {
		super(entityfigure);
		entity = (SheepEntity) targetEntity.renderEntity;
	}

	@Override
	public void init() {
		super.init();
		this.addButton(new Button(width / 2 - 140, height / 6 + 0 + 12, 80, 20, button102[entity.getSheared() ? 0 : 1], (bt)->{actionPerformed(102, bt);}));
		this.addButton(new Button(width / 2 - 140, height / 6 + 24 + 12, 80, 20, entity.getFleeceColor().name(), (bt)->{actionPerformed(103, bt);}));
		this.addButton(new Button(width / 2 - 140, height / 6 + 48 + 12, 80, 20, entity.hasCustomName()?"None":customname, (bt)->{actionPerformed(104, bt);}));
	}

	@Override
	protected void actionPerformed(int id, Button button) {
		switch(id){
		case 102:
			entity.setSheared(!entity.getSheared());
			button.setMessage(button102[entity.getSheared() ? 0 : 1]);
			break;

		case 103:
			if (entity.getFleeceColor().ordinal() == 15) {
				entity.setFleeceColor(DyeColor.values()[0]);
			} else {
				entity.setFleeceColor(DyeColor.values()[entity.getFleeceColor().ordinal() + 1]);
			}
			button.setMessage(entity.getFleeceColor().name());
			break;
		case 104:
			if (entity.hasCustomName()){
				entity.setCustomName(null);
			}else{
				entity.setCustomName(new TranslationTextComponent(customname));
			}
			button.setMessage(entity.hasCustomName()?"None":customname);
		}
		super.actionPerformed(id, button);
	}
}
