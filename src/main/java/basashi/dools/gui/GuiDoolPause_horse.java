package basashi.dools.gui;

import java.util.HashMap;
import java.util.Map;

import basashi.dools.entity.EntityDool;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.passive.horse.AbstractHorseEntity;
import net.minecraft.entity.passive.horse.HorseEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.HorseArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

public class GuiDoolPause_horse extends GuiDoolPause {

	private HorseEntity entity;

	public static final Map<Integer,ItemStack> iarmor = new HashMap<Integer, ItemStack>(){
		{put(0, ItemStack.EMPTY);}
		{put(3, Items.LEATHER_HORSE_ARMOR.getDefaultInstance());}
		{put(5, Items.IRON_HORSE_ARMOR.getDefaultInstance());}
		{put(7, Items.GOLDEN_HORSE_ARMOR.getDefaultInstance());}
		{put(11,Items.DIAMOND_HORSE_ARMOR.getDefaultInstance());}

		{put(1,ItemStack.EMPTY);}
		{put(2,ItemStack.EMPTY);}
		{put(4,ItemStack.EMPTY);}
		{put(6,ItemStack.EMPTY);}
		{put(8,ItemStack.EMPTY);}
		{put(9,ItemStack.EMPTY);}
		{put(10,ItemStack.EMPTY);}
	};

	private String[] strbutton101 = {"Breed on","Breed off"};
	private String[] strbutton102 = { "UnSaddled", "Saddled" };

	private int armortype = 0;
	private int color1 = 0;
	private int color2 = 0;

	public GuiDoolPause_horse(EntityDool entityfigure) {
		super(entityfigure);
		entity = (HorseEntity) targetEntity.renderEntity;
	}

	@Override
	public void init() {
		super.init();
		buttons.remove(4);
		this.addButton(new Button(width / 2 - 140, height / 6 + 0 + 12, 80, 20, entity.isHorseSaddled() ? strbutton102[1]:strbutton102[0], (bt)->{actionPerformed(102, bt);}));

		ItemStack stack = entity.func_213803_dV();
		if (stack.getItem() instanceof HorseArmorItem) {
			HorseArmorItem armor = (HorseArmorItem)stack.getItem();
			// iアーマータイプの取り出し(5:iron 7:gold 11:diamodn)
			armortype = armor.func_219977_e();
		}
		this.addButton(new Button(width / 2 - 140, height / 6 + 0 + 12 + 20, 80, 20, String.valueOf(armortype), (bt)->{actionPerformed(103, bt);}));

		int val = entity.getHorseVariant();
		color1 = (val & 255) % 7;
		color2 = ((val & 65280) >> 8)%5;
		this.addButton(new Button(width / 2 - 140, height / 6 + 0 + 12 + 40, 80, 20, "Texture: "+color1, (bt)->{actionPerformed(104,bt);}));
		this.addButton(new Button(width / 2 - 140, height / 6 + 0 + 12 + 60, 80, 20, "Mark: "+color2, (bt)->{actionPerformed(105, bt);}));
	}

	@Override
	protected void actionPerformed(int id, Button button) {
		int color;
		IInventory inv = (IInventory)ObfuscationReflectionHelper.getPrivateValue(AbstractHorseEntity.class, entity, "horseChest");
		CompoundNBT compound = new CompoundNBT();
		entity.writeAdditional(compound);

		switch (id) {
		case 102:
		    if (entity.isHorseSaddled()) {
		    	compound.remove("SaddleItem");
				inv.setInventorySlotContents(0, ItemStack.EMPTY);
		    }else {
		    	compound.put("SaddleItem", Items.SADDLE.getDefaultInstance().write(new CompoundNBT()));
		    }
		    entity.readAdditional(compound);

//			entity.setHorseSaddled(!entity.isHorseSaddled());
			button.setMessage(entity.isHorseSaddled() ? strbutton102[1]:strbutton102[0]);
			break;
		case 103:
			while(true) {
				armortype++;
				if (!iarmor.containsKey(armortype)) {
					armortype = 0;
					break;
				}
				if (!iarmor.get(armortype).isEmpty()) {
					break;
				}
			}
			ItemStack armor = GuiDoolPause_horse.iarmor.get(armortype);
			// NBTにアーマーの状態を書き込み
		    if (armor.isEmpty()) {
		    	compound.remove("ArmorItem");
				inv.setInventorySlotContents(1, ItemStack.EMPTY);
		    }else {
		    	compound.put("ArmorItem", armor.write(new CompoundNBT()));
		    }
		    // i馬に改造NBTを流し込む
		    entity.readAdditional(compound);
		    button.setMessage(String.valueOf(armortype));
			break;
		case 104:
			color1++;
			if (color1 > 6){
				color1 = 0;
			}
			color = color2<<8 | color1;
			entity.setHorseVariant(color);
			button.setMessage("Texture: "+color1);
			break;
		case 105:
			color2++;
			if (color2 > 4){
				color2 = 0;
			}
			color = color2<<8 | color1;
			entity.setHorseVariant(color);
			button.setMessage("Mark: "+color2);
			break;
		}
		super.actionPerformed(id, button);
	}
}
