package basashi.dools.container;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import basashi.dools.gui.GuiItemSelect;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

/**
 * 装備選択用、丸パクリ。
 */
public class ContainerItemSelect extends Container {

	public static final Map<EntityEquipmentSlot,Integer> slotFromType = new HashMap<EntityEquipmentSlot,Integer>(){
			{put(EntityEquipmentSlot.HEAD,0);}
			{put(EntityEquipmentSlot.CHEST,1);}
			{put(EntityEquipmentSlot.LEGS,2);}
			{put(EntityEquipmentSlot.FEET,3);}
			{put(EntityEquipmentSlot.MAINHAND,4);}
			{put(EntityEquipmentSlot.OFFHAND,5);}
	};
	public static final Map<Integer,EntityEquipmentSlot> slotFromIndex = new HashMap<Integer,EntityEquipmentSlot>(){
		{put(0,EntityEquipmentSlot.HEAD);}
		{put(1,EntityEquipmentSlot.CHEST);}
		{put(2,EntityEquipmentSlot.LEGS);}
		{put(3,EntityEquipmentSlot.FEET);}
		{put(4,EntityEquipmentSlot.MAINHAND);}
		{put(5,EntityEquipmentSlot.OFFHAND);}
};

	public List itemList = new ArrayList();
	public EntityPlayer openPlayer;

	public ContainerItemSelect(EntityPlayer pPlayer) {
		int var3;
		openPlayer = pPlayer;

		for (var3 = 0; var3 < 5; ++var3) {
			for (int var4 = 0; var4 < 9; ++var4) {
				this.func_75146_a(new Slot(GuiItemSelect.inventory,
						var3 * 9 + var4, 9 + var4 * 18, 18 + var3 * 18));
			}
		}

		this.func_75146_a(new SlotArmorDool(this, GuiItemSelect.inventoryItem, slotFromType.get(EntityEquipmentSlot.HEAD), 9 + 0 * 18, 112, EntityEquipmentSlot.HEAD));
		this.func_75146_a(new SlotArmorDool(this, GuiItemSelect.inventoryItem, slotFromType.get(EntityEquipmentSlot.CHEST), 9 + 1 * 18, 112, EntityEquipmentSlot.CHEST));
		this.func_75146_a(new SlotArmorDool(this, GuiItemSelect.inventoryItem, slotFromType.get(EntityEquipmentSlot.LEGS), 9 + 2 * 18, 112, EntityEquipmentSlot.LEGS));
		this.func_75146_a(new SlotArmorDool(this, GuiItemSelect.inventoryItem, slotFromType.get(EntityEquipmentSlot.FEET), 9 + 3 * 18, 112, EntityEquipmentSlot.FEET));
		this.func_75146_a(new Slot(GuiItemSelect.inventoryItem, slotFromType.get(EntityEquipmentSlot.MAINHAND), 9+slotFromType.get(EntityEquipmentSlot.MAINHAND)*18,112));
		this.func_75146_a(new Slot(GuiItemSelect.inventoryItem, slotFromType.get(EntityEquipmentSlot.OFFHAND), 9+slotFromType.get(EntityEquipmentSlot.OFFHAND)*18,112));
//		for (var3 = 4; var3 < 9; ++var3) {
//			this.addSlotToContainer(new Slot(GuiItemSelect.inventoryItem, var3, 9 + var3 * 18, 112));
//		}
		this.scrollTo(0.0F);
	}

	@Override
	public boolean func_75145_c(EntityPlayer par1EntityPlayer) {
		return true;
	}

	public void scrollTo(float par1) {
		int var2 = this.itemList.size() / 9 - 5 + 1;
		int var3 = (int) ((double) (par1 * (float) var2) + 0.5D);

		if (var3 < 0) {
			var3 = 0;
		}

		for (int var4 = 0; var4 < 5; ++var4) {
			for (int var5 = 0; var5 < 9; ++var5) {
				int var6 = var5 + (var4 + var3) * 9;

				if (var6 >= 0 && var6 < this.itemList.size()) {
					GuiItemSelect.inventory.func_70299_a(
							var5 + var4 * 9, (ItemStack) this.itemList.get(var6));
				} else {
					GuiItemSelect.inventory.func_70299_a(
							var5 + var4 * 9, (ItemStack) null);
				}
			}
		}
	}

	public boolean hasMoreThan1PageOfItemsInList() {
		return this.itemList.size() > 45;
	}

	@Override
	protected void func_75133_b(int par1, int par2, boolean par3, EntityPlayer par4EntityPlayer) {
	}

	@Override
	public ItemStack func_82846_b(EntityPlayer par1EntityPlayer, int par2) {
		return null;
	}

	@Override
	public boolean func_94530_a(ItemStack par1ItemStack, Slot par2Slot) {
		return par2Slot.field_75221_f > 90;
	}

	@Override
	public boolean func_94531_b(Slot par1Slot) {
		return false;
	}

	@Override
	public void func_75141_a(int par1, ItemStack par2ItemStack) {
		// GUIを開いている時にアイテムを回収した時の処理
		openPlayer.field_71069_bz.func_75139_a(par1).func_75215_d(par2ItemStack);
	}

}
