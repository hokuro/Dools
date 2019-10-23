package basashi.dools.gui;

import java.lang.reflect.Method;

import basashi.dools.entity.EntityDool;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.monster.BlazeEntity;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

public class GuiDoolPause_blaze extends GuiDoolPause {
	public BlazeEntity entity;
	public String[] button101 = {"Burn off", "Burn on"};

	public GuiDoolPause_blaze(EntityDool entityfigure) {
		super(entityfigure);
		entity = (BlazeEntity)entityfigure.renderEntity;
	}

	@Override
	public void init() {
		super.init();
		this.addButton(new Button(width / 2 - 140, height / 6 + 0 + 12, 80, 20, entity.isBurning() ? button101[1] : button101[0], (bt)->{actionPerformed(101, bt);}));
	}

	@Override
	protected void actionPerformed(int id, Button button) {
		switch(id){
		case 101:
			try {
				Method setOnFire = ObfuscationReflectionHelper.findMethod(BlazeEntity.class, "setOnFire", boolean.class);
				setOnFire.invoke(entity, !entity.isBurning());
				button.setMessage(entity.isBurning() ? button101[1] : button101[0]);
			}catch(Throwable ex) {

			}
			break;
		}
		super.actionPerformed(id, button);
	}
}
