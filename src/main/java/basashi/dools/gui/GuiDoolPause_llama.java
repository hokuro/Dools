package basashi.dools.gui;

import basashi.dools.entity.EntityDool;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.passive.EntityLlama;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class GuiDoolPause_llama extends GuiDoolPause {
	public EntityLlama entity;
	public static final ItemStack[] colors = {
			ItemStack.EMPTY,
			new ItemStack(Blocks.WHITE_CARPET),
			new ItemStack(Blocks.ORANGE_CARPET),
			new ItemStack(Blocks.MAGENTA_CARPET),
			new ItemStack(Blocks.LIGHT_BLUE_CARPET),
			new ItemStack(Blocks.YELLOW_CARPET),
			new ItemStack(Blocks.LIME_CARPET),
			new ItemStack(Blocks.PINK_CARPET),
			new ItemStack(Blocks.GRAY_CARPET),
			new ItemStack(Blocks.LIGHT_GRAY_CARPET),
			new ItemStack(Blocks.CYAN_CARPET),
			new ItemStack(Blocks.PURPLE_CARPET),
			new ItemStack(Blocks.BLUE_CARPET),
			new ItemStack(Blocks.BROWN_CARPET),
			new ItemStack(Blocks.GREEN_CARPET),
			new ItemStack(Blocks.RED_CARPET),
			new ItemStack(Blocks.BLACK_CARPET)
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
		GuiButton b1 = new GuiButton(101, width / 2 - 140, height / 6 + 0 + 12, 80, 20, entity.hasChest() ? button101[1]:button101[0]) {
    		@Override
    		public void onClick(double mouseX, double moudeY){
    			actionPerformed(this);
    		}
    	};
		GuiButton b2 = new GuiButton(102, width / 2 - 140, height / 6 + 0 + 12+20, 80, 20, "Color "+ entity.getVariant()){
    		@Override
    		public void onClick(double mouseX, double moudeY){
    			actionPerformed(this);
    		}
    	};

    	EnumDyeColor col = entity.getColor();
    	if (col == null) {
    		color = 0;
    	}else {
    		color = col.getId()+1;
    	}
		GuiButton b3 = new GuiButton(103, width / 2 - 140, height / 6 + 0 + 12+40, 100, 20, color==0?"None":colors[color].getDisplayName().getFormattedText()) {
    		@Override
    		public void onClick(double mouseX, double moudeY){
    			actionPerformed(this);
    		}
    	};

    	buttons.add(b1);
    	buttons.add(b2);
    	buttons.add(b3);
    	this.children.addAll(buttons);
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

			NBTTagCompound compound = new NBTTagCompound();
			entity.writeAdditional(compound);
			if (color == 0) {
				compound.removeTag("DecorItem");
			}else {
				ItemStack stack = colors[color];
				compound.setTag("DecorItem", stack.write(new NBTTagCompound()));
			}
			entity.readAdditional(compound);
			//entity.getDataManager().set((DataParameter<Integer>)Dools.getPrivateValue(EntityLlama.class, null, "DATA_COLOR_ID"),color-1);

	    	EnumDyeColor col = entity.getColor();
	    	if (col == null) {
	    		color = 0;
	    	}else {
	    		color = col.getId()+1;
	    	}
			guibutton.displayString = color==0?"None":colors[color].getDisplayName().getFormattedText();
		}

		super.actionPerformed(guibutton);
	}
}
