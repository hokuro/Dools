package basashi.dools.container;

import net.minecraft.block.Block;
import net.minecraft.block.SkullBlock;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

public class SlotArmorDool extends Slot {

	protected final Container parent;
	public EquipmentSlotType armorType;

	public SlotArmorDool(Container par1Container, IInventory inv, int index, int xPos, int yPos, EquipmentSlotType armorType) {
		super(inv, index, xPos, yPos);
		this.parent = par1Container;
		this.armorType = armorType;
	}

	public boolean isItemValid(ItemStack stack) {
		if (stack.isEmpty()) return false;
		Item litem = stack.getItem();

		if (litem instanceof ArmorItem) {
			return ((ArmorItem)litem).getEquipmentSlot() == armorType;
		}
		if (litem == Items.PUMPKIN || Block.getBlockFromItem(litem) instanceof SkullBlock) {
			return armorType == EquipmentSlotType.HEAD;
		}
		return false;
	}
}

