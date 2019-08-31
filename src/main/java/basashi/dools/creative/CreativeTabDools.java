package basashi.dools.creative;

import basashi.dools.core.Dools;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class CreativeTabDools extends ItemGroup {

	public CreativeTabDools(String label){
		super(label);
	}

	@Override
	public ItemStack createIcon() {
		// TODO 自動生成されたメソッド・スタブ
		return new ItemStack(Dools.itemdool);
	}
}