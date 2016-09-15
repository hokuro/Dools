package basashi.dools.gui;

import basashi.dools.core.Dools;
import basashi.dools.entity.EntityDool;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.network.datasync.DataParameter;
import net.minecraftforge.fml.common.registry.VillagerRegistry;
public class GuiDoolPause_Zombie extends GuiDoolPause {

	public EntityZombie efzombie;


	public GuiDoolPause_Zombie(EntityDool entityfigure) {
		super(entityfigure);
		efzombie = (EntityZombie)entityfigure.renderEntity;
	}

	@Override
	public void func_73866_w_() {
		super.func_73866_w_();
		field_146292_n.add(new GuiButton(100, field_146294_l / 2 - 140, field_146295_m / 6 + 0 + 12, 80, 20, efzombie.func_82231_m() ? "Villager" : "Crafter"));
	}

	@Override
	protected void func_146284_a(GuiButton guibutton) {
		if (!guibutton.field_146124_l) {
			return;
		}
		switch (guibutton.field_146127_k) {
		case 100:
			// スキンのロード元
			int type = efzombie.func_184736_de()+1;
			if( VillagerRegistry.instance().getRegistry().getKeys().size() <= type){type = -1;}
			efzombie.func_184735_a(type);
			//efzombie.setVillager(!efzombie.isVillager());
			guibutton.field_146126_j = efzombie.func_82231_m() ? "Villager" : "Crafter";
			break;
		case 16:
			// 幼生態判定
			efzombie.func_184212_Q().func_187227_b((DataParameter<Boolean>)Dools.getPrivateValue(EntityZombie.class, null, "IS_CHILD"),
				!efzombie.func_70631_g_() ? true: false);
			//efzombie.getDataWatcher().updateObject(12, !efzombie.isChild() ? (byte)1 : (byte)0);
			guibutton.field_146126_j = button16[efzombie.func_70631_g_() ? 1 : 0];
			return;
		}

		super.func_146284_a(guibutton);
	}

}
