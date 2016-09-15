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
		for (Object lo : Minecraft.getMinecraft().theWorld.playerEntities) {
			EntityPlayer lep = (EntityPlayer)lo;
			if (lep.getName() != null && !lep.getName().isEmpty()) {
				playerList.add(lep.getName());
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
	public void initGui() {
		super.initGui();
		String ls = playerIndex == 0 ? playerList.get(0) : efplayer.skinUser;
		String tx = button101[efplayer.isSlim()?1:0];
		buttonList.add(new GuiButton(100, width / 2 - 140, height / 6 + 0 + 12, 80, 20, ls));
		buttonList.add(new GuiButton(101, width / 2 + 60, height / 6 + 96 + 12, 80, 20, tx));

		buttonList.add(new GuiButton(120, width / 2 - 180, height / 6 + 24 + 12, 60, 20, button120[efplayer.isHat?0:1]));
		buttonList.add(new GuiButton(121, width / 2 - 180 + 60, height / 6 + 24 + 12, 60, 20, button121[efplayer.isJacket?0:1]));
		buttonList.add(new GuiButton(122, width / 2 - 180, height / 6 + 48 + 12, 60, 20, button122[efplayer.isLeftLeg?0:1]));
		buttonList.add(new GuiButton(123, width / 2 - 180 + 60, height / 6 + 48 + 12, 60, 20, button123[efplayer.isRightLeg?0:1]));
		buttonList.add(new GuiButton(124, width / 2 - 180, height / 6 + 72 + 12, 60, 20, button124[efplayer.isLeftSleeve?0:1]));
		buttonList.add(new GuiButton(125, width / 2 - 180 + 60, height / 6 + 72 + 12, 60, 20, button125[efplayer.isRightSleeve?0:1]));
	}

	@Override
	protected void actionPerformed(GuiButton guibutton) {
		super.actionPerformed(guibutton);
		boolean equip;
		if (!guibutton.enabled) {
			return;
		}
		switch (guibutton.id) {
		case 100:
			// スキンのロード元

			if (playerList.size() <= ++playerIndex) {
				playerIndex = 0;
			}
			guibutton.displayString = playerList.get(playerIndex);
			efplayer.skinUser = playerIndex == 0 ? "" : guibutton.displayString;
			efplayer.setURLSkin();
			break;
		case 101:
			int slim = efplayer.isSlim()?0:1;
			efplayer.setSlim(slim);
			guibutton.displayString = button101[slim];
			efplayer.setURLSkin();
			break;

		case 120:
			efplayer.isHat=!efplayer.isHat;
			guibutton.displayString = button120[efplayer.isHat?0:1];
			break;
		case 121:
			efplayer.isJacket=!efplayer.isJacket;
			guibutton.displayString = button121[efplayer.isJacket?0:1];
			break;
		case 122:
			efplayer.isLeftLeg=!efplayer.isLeftLeg;
			guibutton.displayString = button122[efplayer.isLeftLeg?0:1];
			break;
		case 123:
			efplayer.isRightLeg=!efplayer.isRightLeg;
			guibutton.displayString = button123[efplayer.isRightLeg?0:1];
			break;
		case 124:
			efplayer.isLeftSleeve=!efplayer.isLeftSleeve;
			guibutton.displayString = button124[efplayer.isLeftSleeve?0:1];
			break;
		case 125:
			efplayer.isRightSleeve=!efplayer.isRightSleeve;
			guibutton.displayString = button125[efplayer.isRightSleeve?0:1];
			break;
		case 126:
			efplayer.isSpectator = !efplayer.isSpectator;
			break;
		}
	}
	public static void afterRender(EntityDool entityfigure) {
		
	
	}

}
