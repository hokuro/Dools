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
	public void initGui() {
		super.initGui();
		buttonList.add(new GuiButton(100, width / 2 - 140, height / 6 + 0 + 12, 80, 20, efzombie.isVillager() ? "Villager" : "Crafter"));
	}

	@Override
	protected void actionPerformed(GuiButton guibutton) {
		if (!guibutton.enabled) {
			return;
		}
		switch (guibutton.id) {
		case 100:
			// スキンのロード元
			int type = efzombie.getVillagerType()+1;
			if( VillagerRegistry.instance().getRegistry().getKeys().size() <= type){type = -1;}
			efzombie.setVillagerType(type);
			//efzombie.setVillager(!efzombie.isVillager());
			guibutton.displayString = efzombie.isVillager() ? "Villager" : "Crafter";
			break;
		case 16:
			// 幼生態判定
			efzombie.getDataManager().set((DataParameter<Boolean>)Dools.getPrivateValue(EntityZombie.class, null, "IS_CHILD"),
				!efzombie.isChild() ? true: false);
			//efzombie.getDataWatcher().updateObject(12, !efzombie.isChild() ? (byte)1 : (byte)0);
			guibutton.displayString = button16[efzombie.isChild() ? 1 : 0];
			return;
		}

		super.actionPerformed(guibutton);
	}

}
