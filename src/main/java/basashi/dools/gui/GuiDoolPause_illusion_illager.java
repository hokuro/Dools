package basashi.dools.gui;

import basashi.dools.entity.EntityDool;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.monster.EntityIllusionIllager;
import net.minecraft.entity.monster.EntitySpellcasterIllager.SpellType;
import net.minecraft.nbt.NBTTagCompound;

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
		GuiButton b1 = new GuiButton(101, width / 2 - 140, height / 6 + 0 + 12, 80, 20, entity.isSpellcasting() ? button101[1]:button101[0]) {
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
			if (entity.isSpellcasting()){
				entity.setSpellType(SpellType.NONE);

				NBTTagCompound tag = new NBTTagCompound();
				entity.writeAdditional(tag);
				tag.setInt("SpellTicks",0);
				entity.readAdditional(tag);
				//Dools.setPrivateValue(EntitySpellcasterIllager.class, entity, 0, "spellTicks");
			}else{
				entity.setSpellType(SpellType.DISAPPEAR);

				NBTTagCompound tag = new NBTTagCompound();
				entity.writeAdditional(tag);
				tag.setInt("SpellTicks",10);
				entity.readAdditional(tag);
				//Dools.setPrivateValue(EntitySpellcasterIllager.class, entity, 10, "spellTicks");
			}
			guibutton.displayString = entity.isSpellcasting() ? button101[1]:button101[0];
			break;
		}

		super.actionPerformed(guibutton);
	}
}
