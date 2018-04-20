package basashi.dools.creative;

import basashi.dools.core.Dools;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class CreativeTabDools extends CreativeTabs {

	public CreativeTabDools(String label){
		super(label);
	}

	@SideOnly(Side.CLIENT)
	public String getTranslatedTabLabel(){
		return "Dools";
	}

	@Override
	public ItemStack getTabIconItem() {
		// TODO 自動生成されたメソッド・スタブ
		return new ItemStack(Dools.dool);
	}

	@SideOnly(Side.CLIENT)
	public int getIconItemDamage(){
		return 0;
	}


}