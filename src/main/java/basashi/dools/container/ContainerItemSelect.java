package basashi.dools.container;

import java.util.HashMap;
import java.util.Map;

import basashi.dools.gui.GuiItemSelect;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public class ContainerItemSelect extends Container {

	public static final Map<EquipmentSlotType,Integer> slotFromType = new HashMap<EquipmentSlotType,Integer>(){
			{put(EquipmentSlotType.HEAD,0);}
			{put(EquipmentSlotType.CHEST,1);}
			{put(EquipmentSlotType.LEGS,2);}
			{put(EquipmentSlotType.FEET,3);}
			{put(EquipmentSlotType.MAINHAND,4);}
			{put(EquipmentSlotType.OFFHAND,5);}
	};
	public static final Map<Integer,EquipmentSlotType> slotFromIndex = new HashMap<Integer,EquipmentSlotType>(){
		{put(0,EquipmentSlotType.HEAD);}
		{put(1,EquipmentSlotType.CHEST);}
		{put(2,EquipmentSlotType.LEGS);}
		{put(3,EquipmentSlotType.FEET);}
		{put(4,EquipmentSlotType.MAINHAND);}
		{put(5,EquipmentSlotType.OFFHAND);}
	};

    public final NonNullList<ItemStack> itemList = NonNullList.create();
	private LivingEntity targetEntity;

	protected IInventory inventory1;
	protected IInventory inventory2;
	public PlayerInventory playerInv;

	public ContainerItemSelect(PlayerInventory pInv, LivingEntity target) {
		//super(Dools.CONTAINER_ITEMSELECT, id);
		super(null, 0);
		playerInv = pInv;
		inventory1 = GuiItemSelect.getTmpInventory();
		inventory2 = GuiItemSelect.getItmInventory();

		targetEntity = target;

		for (int row = 0; row < 5; ++row) {
			for (int col = 0; col < 9; ++col) {
				this.addSlot(new Slot(inventory1, row * 9 + col, 9 + col * 18, 18 + row * 18));
			}
		}

		this.addSlot(new SlotArmorDool(this, inventory2, slotFromType.get(EquipmentSlotType.HEAD), 9 + 0 * 18, 112, EquipmentSlotType.HEAD));
		this.addSlot(new SlotArmorDool(this, inventory2, slotFromType.get(EquipmentSlotType.CHEST), 9 + 1 * 18, 112, EquipmentSlotType.CHEST));
		this.addSlot(new SlotArmorDool(this, inventory2, slotFromType.get(EquipmentSlotType.LEGS), 9 + 2 * 18, 112, EquipmentSlotType.LEGS));
		this.addSlot(new SlotArmorDool(this, inventory2, slotFromType.get(EquipmentSlotType.FEET), 9 + 3 * 18, 112, EquipmentSlotType.FEET));
		this.addSlot(new Slot(inventory2, slotFromType.get(EquipmentSlotType.MAINHAND), 9+slotFromType.get(EquipmentSlotType.MAINHAND)*18,112));
		this.addSlot(new Slot(inventory2, slotFromType.get(EquipmentSlotType.OFFHAND), 9+slotFromType.get(EquipmentSlotType.OFFHAND)*18,112));
		this.scrollTo(0.0F);
	}

	@Override
	public boolean canInteractWith(PlayerEntity par1PlayerEntity) {
		return true;
	}

	public void scrollTo(float pos) {
		int i = (this.itemList.size() + 9 - 1) / 9 - 5;
		int j = (int)((double)(pos * (float)i) + 0.5D);

		if (j < 0) {
			j = 0;
		}

		for (int k = 0; k < 5; ++k) {
			for (int l = 0; l < 9; ++l) {
				int index = l + (k + j) * 9;

				if (index >= 0 && index < this.itemList.size()) {
					inventory1.setInventorySlotContents(l + k * 9, (ItemStack) this.itemList.get(index));
				} else {
					inventory1.setInventorySlotContents(l + k * 9, (ItemStack) ItemStack.EMPTY);
				}
			}
		}
	}

	public boolean canScroll() {
		return this.itemList.size() > 45;
	}

	public boolean hasMoreThan1PageOfItemsInList() {
		return this.itemList.size() > 45;
	}


	@Override
	public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
		if (index >= this.inventorySlots.size() - 9 && index < this.inventorySlots.size()) {
			Slot slot = this.inventorySlots.get(index);
			if (slot != null && slot.getHasStack()) {
				slot.putStack(ItemStack.EMPTY);
			}
		}
		return ItemStack.EMPTY;
	}

	@Override
	public boolean canMergeSlot(ItemStack stack, Slot slotIn) {
		return slotIn.inventory != inventory1;
	}

	@Override
	public boolean canDragIntoSlot(Slot slotIn) {
		return slotIn.inventory != inventory1;
	}

	@Override
	public void putStackInSlot(int index, ItemStack stack) {
		// GUIを開いている時にアイテムを回収した時の処理
		playerInv.player.container.getSlot(index).putStack(stack);
	}

	public LivingEntity getTargetEntity() {
		return targetEntity;
	}

}
