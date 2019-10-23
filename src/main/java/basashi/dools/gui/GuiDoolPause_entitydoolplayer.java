package basashi.dools.gui;

import java.util.ArrayList;
import java.util.List;

import basashi.dools.entity.EntityDool;
import basashi.dools.entity.EntityDoolPlayer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.player.PlayerEntity;

public class GuiDoolPause_entitydoolplayer extends GuiDoolPause {

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


	public GuiDoolPause_entitydoolplayer(EntityDool entityfigure) {
		super(entityfigure);
		efplayer = (EntityDoolPlayer) entityfigure.renderEntity;
		playerList = new ArrayList<String>();
		playerList.add("Default");
		for (Object lo : Minecraft.getInstance().world.getPlayers()) {
			PlayerEntity lep = (PlayerEntity)lo;
			if (lep.getName() != null && !lep.getName().getFormattedText().isEmpty()) {
				playerList.add(lep.getName().getFormattedText());
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
	public void init() {
		super.init();
		String ls = playerIndex == 0 ? playerList.get(0) : efplayer.skinUser;
		String tx = button101[efplayer.isSlim()?1:0];
		this.addButton(new Button(width / 2 - 140, height / 6 + 0 + 12, 80, 20, ls, (bt)->{actionPerformed(100, bt);}));
		this.addButton(new Button(width / 2 + 60, height / 6 + 120 + 12, 80, 20, tx, (bt)->{actionPerformed(101, bt);}));
		this.addButton(new Button(width / 2 - 180, height / 6 + 24 + 12, 60, 20, button120[efplayer.isHat?0:1], (bt)->{actionPerformed(120, bt);}));
		this.addButton(new Button(width / 2 - 180 + 60, height / 6 + 24 + 12, 60, 20, button121[efplayer.isJacket?0:1], (bt)->{actionPerformed(121, bt);}));
		this.addButton(new Button(width / 2 - 180, height / 6 + 48 + 12, 60, 20, button122[efplayer.isLeftLeg?0:1], (bt)->{actionPerformed(122, bt);}));
		this.addButton(new Button(width / 2 - 180 + 60, height / 6 + 48 + 12, 60, 20, button123[efplayer.isRightLeg?0:1], (bt)->{actionPerformed(123, bt);}));
		this.addButton(new Button(width / 2 - 180, height / 6 + 72 + 12, 60, 20, button124[efplayer.isLeftSleeve?0:1], (bt)->{actionPerformed(124, bt);}));
		this.addButton(new Button(width / 2 - 180 + 60, height / 6 + 72 + 12, 60, 20, button125[efplayer.isRightSleeve?0:1], (bt)->{actionPerformed(125, bt);}));
	}

	@Override
	protected void actionPerformed(int id, Button button) {
		switch (id) {
		case 100:
			// スキンのロード元

			if (playerList.size() <= ++playerIndex) {
				playerIndex = 0;
			}
			button.setMessage(playerList.get(playerIndex));
			efplayer.skinUser = playerIndex == 0 ? "" : button.getMessage();
			efplayer.setURLSkin();
			break;
		case 101:
			int slim = efplayer.isSlim()?0:1;
			efplayer.setSlim(slim);
			button.setMessage(button101[slim]);
			efplayer.setURLSkin();
			break;

		case 120:
			efplayer.isHat=!efplayer.isHat;
			button.setMessage(button120[efplayer.isHat?0:1]);
			break;
		case 121:
			efplayer.isJacket=!efplayer.isJacket;
			button.setMessage(button121[efplayer.isJacket?0:1]);
			break;
		case 122:
			efplayer.isLeftLeg=!efplayer.isLeftLeg;
			button.setMessage(button122[efplayer.isLeftLeg?0:1]);
			break;
		case 123:
			efplayer.isRightLeg=!efplayer.isRightLeg;
			button.setMessage(button123[efplayer.isRightLeg?0:1]);
			break;
		case 124:
			efplayer.isLeftSleeve=!efplayer.isLeftSleeve;
			button.setMessage(button124[efplayer.isLeftSleeve?0:1]);
			break;
		case 125:
			efplayer.isRightSleeve=!efplayer.isRightSleeve;
			button.setMessage(button125[efplayer.isRightSleeve?0:1]);
			break;
		case 126:
			efplayer.isSpectator = !efplayer.isSpectator;
			break;
		}
		super.actionPerformed(id, button);
	}

}
