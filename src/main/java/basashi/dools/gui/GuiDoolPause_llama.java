package basashi.dools.gui;

import basashi.dools.core.Dools;
import basashi.dools.entity.EntityDool;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.passive.EntityLlama;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.DataParameter;

public class GuiDoolPause_llama extends GuiDoolPause {
	public EntityLlama entity;
	public static final ItemStack[] colors = {
			ItemStack.EMPTY,
			new ItemStack(Blocks.CARPET,1,EnumDyeColor.BLACK.getDyeDamage()),
			new ItemStack(Blocks.CARPET,1,EnumDyeColor.RED.getDyeDamage()),
			new ItemStack(Blocks.CARPET,1,EnumDyeColor.GREEN.getDyeDamage()),
			new ItemStack(Blocks.CARPET,1,EnumDyeColor.BROWN.getDyeDamage()),
			new ItemStack(Blocks.CARPET,1,EnumDyeColor.BLUE.getDyeDamage()),
			new ItemStack(Blocks.CARPET,1,EnumDyeColor.PURPLE.getDyeDamage()),
			new ItemStack(Blocks.CARPET,1,EnumDyeColor.CYAN.getDyeDamage()),
			new ItemStack(Blocks.CARPET,1,EnumDyeColor.SILVER.getDyeDamage()),
			new ItemStack(Blocks.CARPET,1,EnumDyeColor.GRAY.getDyeDamage()),
			new ItemStack(Blocks.CARPET,1,EnumDyeColor.PINK.getDyeDamage()),
			new ItemStack(Blocks.CARPET,1,EnumDyeColor.LIME.getDyeDamage()),
			new ItemStack(Blocks.CARPET,1,EnumDyeColor.YELLOW.getDyeDamage()),
			new ItemStack(Blocks.CARPET,1,EnumDyeColor.LIGHT_BLUE.getDyeDamage()),
			new ItemStack(Blocks.CARPET,1,EnumDyeColor.MAGENTA.getDyeDamage()),
			new ItemStack(Blocks.CARPET,1,EnumDyeColor.ORANGE.getDyeDamage()),
			new ItemStack(Blocks.CARPET,1,EnumDyeColor.WHITE.getDyeDamage())

	};
	public String[] button101 ={"chest on","chest off"};
	private int valiant = 0;
	private int color = 0;

	public GuiDoolPause_llama(EntityDool entityfigure) {
		super(entityfigure);
		entity = (EntityLlama)entityfigure.renderEntity;
	}

	@Override
	public void initGui() {
		super.initGui();
		buttonList.add(new GuiButton(101, width / 2 - 140, height / 6 + 0 + 12, 80, 20, entity.hasChest() ? button101[1]:button101[0]));
		buttonList.add(new GuiButton(102, width / 2 - 140, height / 6 + 0 + 12+20, 80, 20, "Color "+ entity.getVariant()));

		color = entity.getDataManager().get((DataParameter<Integer>)Dools.getPrivateValue(EntityLlama.class, null, "DATA_COLOR_ID")).intValue()+1;
		buttonList.add(new GuiButton(103, width / 2 - 140, height / 6 + 0 + 12+40, 100, 20, color==0?"None":colors[color].getDisplayName()));
	}

	@Override
	protected void actionPerformed(GuiButton guibutton) {
		if (!guibutton.enabled) {
			return;
		}
		switch (guibutton.id) {
		case 101:
			entity.setChested(!entity.hasChest());
			guibutton.displayString =  entity.hasChest() ? button101[1]:button101[0];
			break;
		case 102:
			this.valiant++;
			if (this.valiant > 3){
				valiant = 0;
			}
			entity.setVariant(valiant);
			guibutton.displayString =  "Color "+ entity.getVariant();
			break;
		case 103:
			color++;
			if (color >= colors.length){color = 0;}
			entity.getDataManager().set((DataParameter<Integer>)Dools.getPrivateValue(EntityLlama.class, null, "DATA_COLOR_ID"),color-1);
			guibutton.displayString = color==0?"None":colors[color].getDisplayName();
		}

		super.actionPerformed(guibutton);
	}
}
