package basashi.dools.inventory;

import basashi.dools.container.ContainerItemSelect;
import basashi.dools.core.ModCommon;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.IInteractionObject;

public class IntaractionObjectGuiSelect implements IInteractionObject {

	public IntaractionObjectGuiSelect() {

	}

	@Override
	public ITextComponent getName() {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	@Override
	public boolean hasCustomName() {
		// TODO 自動生成されたメソッド・スタブ
		return false;
	}

	@Override
	public ITextComponent getCustomName() {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	@Override
	public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn) {
		return new ContainerItemSelect(playerIn);
	}

	@Override
	public String getGuiID() {
		// TODO 自動生成されたメソッド・スタブ
		return GUI_ID_SELECT.toString();
	}

	public static final ResourceLocation GUI_ID_SELECT = new ResourceLocation(ModCommon.MOD_ID, "gui_select");
}
