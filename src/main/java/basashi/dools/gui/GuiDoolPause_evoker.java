package basashi.dools.gui;

import basashi.dools.entity.EntityDool;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.monster.EvokerEntity;
import net.minecraft.nbt.CompoundNBT;

public class GuiDoolPause_evoker extends GuiDoolPause {

	public EvokerEntity entity;
	public String[] button101 = {"Spell On","Spell OFF"};

	public GuiDoolPause_evoker(EntityDool entityfigure) {
		super(entityfigure);
		entity = (EvokerEntity)entityfigure.renderEntity;
	}

	@Override
	public void init() {
		super.init();
		this.addButton(new Button(width / 2 - 140, height / 6 + 0 + 12, 80, 20, entity.isSpellcasting() ? button101[1]:button101[0], (bt)->{actionPerformed(101, bt);}));
	}

	@Override
	protected void actionPerformed(int id, Button button) {
		switch(id){
			case 101:
			if (entity.isSpellcasting()){
				entity.setSpellType(net.minecraft.entity.monster.SpellcastingIllagerEntity.SpellType.NONE);
				CompoundNBT tag = new CompoundNBT();
				entity.writeAdditional(tag);
				tag.putInt("SpellTicks",0);
				entity.readAdditional(tag);
			}else{
				entity.setSpellType(net.minecraft.entity.monster.SpellcastingIllagerEntity.SpellType.DISAPPEAR);
				CompoundNBT tag = new CompoundNBT();
				entity.writeAdditional(tag);
				tag.putInt("SpellTicks",10);
				entity.readAdditional(tag);
			}
			button.setMessage(entity.isSpellcasting() ? button101[1]:button101[0]);
			break;
		}
		super.actionPerformed(id, button);
	}
}
