package basashi.dools.gui;


import java.util.ArrayList;
import java.util.List;

import basashi.dools.entity.EntityDool;
import basashi.dools.entity.EntityDoolPlayer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;

public class GuiDoolPause_DoolPlayer extends GuiDoolPause {

	public EntityDoolPlayer efplayer;
	protected List<String> playerList;
	protected int playerIndex;
	public static final String button101[] ={"Steev","Alex"};
	public static final String button120[] ={"Hat ON","Hat OFF"};
	public static final String button121[] ={"Jaket ON","Jaket OFF"};
	public static final String button122[] ={"LLeg ON","LLeg OFF"};
	public static final String button123[] ={"RLeg ON","RLeg OFF"};
	public static final String button124[] ={"LArm ON","LArm OFF"};
	public static final String button125[] ={"RArm ON","RArm OFF"};


	public GuiDoolPause_DoolPlayer(EntityDool entityfigure) {
		super(entityfigure);
		efplayer = (EntityDoolPlayer) entityfigure.renderEntity;
		playerList = new ArrayList<String>();
		playerList.add("Default");
		for (Object lo : Minecraft.func_71410_x().field_71441_e.field_73010_i) {
			EntityPlayer lep = (EntityPlayer)lo;
			if (lep.func_70005_c_() != null && !lep.func_70005_c_().isEmpty()) {
				playerList.add(lep.func_70005_c_());
			}
		}
		if (efplayer.skinUser != null && !efplayer.skinUser.isEmpty()) {
			if (playerList.contains(efplayer.skinUser)) {
				playerIndex = playerList.indexOf(efplayer.skinUser);
			} else {
				playerIndex = -1;
			}
		} else {
			playerIndex = 0;
		}
	}

	@Override
	public void func_73866_w_() {
		super.func_73866_w_();
		String ls = playerIndex == 0 ? playerList.get(0) : efplayer.skinUser;
		String tx = button101[efplayer.isSlim()?1:0];
		field_146292_n.add(new GuiButton(100, field_146294_l / 2 - 140, field_146295_m / 6 + 0 + 12, 80, 20, ls));
		field_146292_n.add(new GuiButton(101, field_146294_l / 2 + 60, field_146295_m / 6 + 96 + 12, 80, 20, tx));

		field_146292_n.add(new GuiButton(120, field_146294_l / 2 - 180, field_146295_m / 6 + 24 + 12, 60, 20, button120[efplayer.isHat?0:1]));
		field_146292_n.add(new GuiButton(121, field_146294_l / 2 - 180 + 60, field_146295_m / 6 + 24 + 12, 60, 20, button121[efplayer.isJacket?0:1]));
		field_146292_n.add(new GuiButton(122, field_146294_l / 2 - 180, field_146295_m / 6 + 48 + 12, 60, 20, button122[efplayer.isLeftLeg?0:1]));
		field_146292_n.add(new GuiButton(123, field_146294_l / 2 - 180 + 60, field_146295_m / 6 + 48 + 12, 60, 20, button123[efplayer.isRightLeg?0:1]));
		field_146292_n.add(new GuiButton(124, field_146294_l / 2 - 180, field_146295_m / 6 + 72 + 12, 60, 20, button124[efplayer.isLeftSleeve?0:1]));
		field_146292_n.add(new GuiButton(125, field_146294_l / 2 - 180 + 60, field_146295_m / 6 + 72 + 12, 60, 20, button125[efplayer.isRightSleeve?0:1]));
	}

	@Override
	protected void func_146284_a(GuiButton guibutton) {
		super.func_146284_a(guibutton);
		boolean equip;
		if (!guibutton.field_146124_l) {
			return;
		}
		switch (guibutton.field_146127_k) {
		case 100:
			// スキンのロード元

			if (playerList.size() <= ++playerIndex) {
				playerIndex = 0;
			}
			guibutton.field_146126_j = playerList.get(playerIndex);
			efplayer.skinUser = playerIndex == 0 ? "" : guibutton.field_146126_j;
			efplayer.setURLSkin();
			break;
		case 101:
			int slim = efplayer.isSlim()?0:1;
			efplayer.setSlim(slim);
			guibutton.field_146126_j = button101[slim];
			efplayer.setURLSkin();
			break;

		case 120:
			efplayer.isHat=!efplayer.isHat;
			guibutton.field_146126_j = button120[efplayer.isHat?0:1];
			break;
		case 121:
			efplayer.isJacket=!efplayer.isJacket;
			guibutton.field_146126_j = button121[efplayer.isJacket?0:1];
			break;
		case 122:
			efplayer.isLeftLeg=!efplayer.isLeftLeg;
			guibutton.field_146126_j = button122[efplayer.isLeftLeg?0:1];
			break;
		case 123:
			efplayer.isRightLeg=!efplayer.isRightLeg;
			guibutton.field_146126_j = button123[efplayer.isRightLeg?0:1];
			break;
		case 124:
			efplayer.isLeftSleeve=!efplayer.isLeftSleeve;
			guibutton.field_146126_j = button124[efplayer.isLeftSleeve?0:1];
			break;
		case 125:
			efplayer.isRightSleeve=!efplayer.isRightSleeve;
			guibutton.field_146126_j = button125[efplayer.isRightSleeve?0:1];
			break;
		case 126:
			efplayer.isSpectator = !efplayer.isSpectator;
			break;
		}
	}
	public static void afterRender(EntityDool entityfigure) {
		
	
	}

}
