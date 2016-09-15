package basashi.dools.container;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;

public class SlotArmorDool extends Slot {

	protected final Container parent;
	public EntityEquipmentSlot armorType;

	public SlotArmorDool(Container par1Container, IInventory par2IInventory, int par3, int par4, int par5, EntityEquipmentSlot armorType) {
		super(par2IInventory, par3, par4, par5);
		this.parent = par1Container;
		this.armorType = armorType;
	}

	public boolean isItemValid(ItemStack par1ItemStack) {
		if (par1ItemStack == null) return false;
		Item litem = par1ItemStack.getItem();

		par1ItemStack.getItem().isValidArmor(par1ItemStack, armorType, ((ContainerItemSelect)parent).openPlayer);

		if (litem instanceof ItemArmor) {
			return ((ItemArmor)litem).armorType == armorType;
		}
		if (Block.getBlockFromItem(litem) == Blocks.pumpkin || litem == Items.skull) {
			return armorType == EntityEquipmentSlot.HEAD;
		}
		return false;
	}
}

