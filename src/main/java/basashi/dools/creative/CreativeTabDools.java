package basashi.dools.creative;

import basashi.dools.core.Dools;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class CreativeTabDools extends ItemGroup {

	public CreativeTabDools(String label){
		super(label);
	}

	@Override
	public ItemStack createIcon() {
		return new ItemStack(Dools.item_dool);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public String getTranslationKey() {
		return this.getTabLabel();
	}
}
