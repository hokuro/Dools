package basashi.dools.gui;

import basashi.dools.entity.EntityDool;
import net.minecraft.block.Blocks;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.passive.horse.AbstractHorseEntity;
import net.minecraft.entity.passive.horse.LlamaEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.DyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

public class GuiDoolPause_llama extends GuiDoolPause {
	public LlamaEntity entity;
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
		entity = (LlamaEntity)entityfigure.renderEntity;
	}

	@Override
	public void init() {
		super.init();
		this.addButton(new Button(width / 2 - 140, height / 6 + 0 + 12, 80, 20, entity.hasChest() ? button101[1]:button101[0], (bt)->{actionPerformed(101, bt);}));
    	this.addButton(new Button(width / 2 - 140, height / 6 + 0 + 12+20, 80, 20, "Color "+ entity.getVariant(), (bt)->{actionPerformed(102, bt);}));

    	DyeColor col = entity.getColor();
    	if (col == null) {
    		color = 0;
    	}else {
    		color = col.getId()+1;
    	}
    	this.addButton(new Button(width / 2 - 140, height / 6 + 0 + 12+40, 100, 20, color==0?"None":colors[color].getDisplayName().getFormattedText(), (bt)->{actionPerformed(103, bt);}));
	}

	@Override
	protected void actionPerformed(int id, Button button) {
		switch(id){
		case 101:
			entity.setChested(!entity.hasChest());
			button.setMessage( entity.hasChest() ? button101[1]:button101[0]);
			break;
		case 102:
			this.valiant++;
			if (this.valiant > 3){
				valiant = 0;
			}
			entity.setVariant(valiant);
			button.setMessage("Color "+ entity.getVariant());
			break;
		case 103:

			IInventory inv = (IInventory)ObfuscationReflectionHelper.getPrivateValue(AbstractHorseEntity.class, entity, "horseChest");
			color++;
			if (color >= colors.length){color = 0;}

			CompoundNBT compound = new CompoundNBT();
			entity.writeAdditional(compound);
			if (color == 0) {
				compound.remove("DecorItem");
				inv.setInventorySlotContents(1, ItemStack.EMPTY);
			}else {
				ItemStack stack = colors[color];
				compound.put("DecorItem", stack.write(new CompoundNBT()));
			}
			entity.readAdditional(compound);

	    	DyeColor col = entity.getColor();
	    	if (col == null) {
	    		color = 0;
	    	}else {
	    		color = col.getId()+1;
	    	}
	    	button.setMessage(color==0?"None":colors[color].getDisplayName().getFormattedText());
		}
		super.actionPerformed(id, button);
	}
}
