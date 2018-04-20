package basashi.dools.gui;

import basashi.dools.core.Dools;
import basashi.dools.entity.EntityDool;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.monster.EntityIllusionIllager;
import net.minecraft.entity.monster.EntitySpellcasterIllager;
import net.minecraft.entity.monster.EntitySpellcasterIllager.SpellType;

public class GuiDoolPause_illusion_illager extends GuiDoolPause {
	public EntityIllusionIllager entity;
	public String[] button101 = {"Spell On","Spell OFF"};


	public GuiDoolPause_illusion_illager(EntityDool entityfigure) {
		super(entityfigure);
		entity = (EntityIllusionIllager)entityfigure.renderEntity;
	}

	@Override
	public void initGui() {
		super.initGui();
		buttonList.add(new GuiButton(101, width / 2 - 140, height / 6 + 0 + 12, 80, 20, entity.isSpellcasting() ? button101[1]:button101[0]));
	}

	@Override
	protected void actionPerformed(GuiButton guibutton) {
		if (!guibutton.enabled) {
			return;
		}
		switch (guibutton.id) {
		case 101:
			if (entity.isSpellcasting()){
				entity.setSpellType(SpellType.NONE);
				Dools.setPrivateValue(EntitySpellcasterIllager.class, entity, 0, "spellTicks");
			}else{
				entity.setSpellType(SpellType.DISAPPEAR);
				Dools.setPrivateValue(EntitySpellcasterIllager.class, entity, 10, "spellTicks");
			}
			guibutton.displayString = entity.isSpellcasting() ? button101[1]:button101[0];
			break;
		}

		super.actionPerformed(guibutton);
	}
}
