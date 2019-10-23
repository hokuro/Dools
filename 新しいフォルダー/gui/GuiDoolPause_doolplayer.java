package basashi.dools.gui;


import java.util.ArrayList;
import java.util.List;

import basashi.dools.container.ContainerPause;
import basashi.dools.entity.EntityDool;
import basashi.dools.entity.EntityDoolPlayer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;

public class GuiDoolPause_doolplayer extends GuiDoolPause {

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


	public GuiDoolPause_doolplayer(ContainerPause container, PlayerInventory playerInv, ITextComponent titleIn, EntityDool entityfigure) {
		super(container, playerInv, titleIn, entityfigure);
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
		GuiButton b1 = new GuiButton(100, width / 2 - 140, height / 6 + 0 + 12, 80, 20, ls, (bt)->{actionPerformed(bt);}) {
    		@Override
    		public void onClick(double mouseX, double moudeY){
    			actionPerformed(this);
    		}
    	};
		GuiButton b2 = new GuiButton(101, width / 2 + 60, height / 6 + 96 + 12, 80, 20, tx, (bt)->{actionPerformed(bt);}) {
    		@Override
    		public void onClick(double mouseX, double moudeY){
    			actionPerformed(this);
    		}
    	};
		GuiButton b3 = new GuiButton(120, width / 2 - 180, height / 6 + 24 + 12, 60, 20, button120[efplayer.isHat?0:1], (bt)->{actionPerformed(bt);}) {
    		@Override
    		public void onClick(double mouseX, double moudeY){
    			actionPerformed(this);
    		}
    	};
		GuiButton b4 = new GuiButton(121, width / 2 - 180 + 60, height / 6 + 24 + 12, 60, 20, button121[efplayer.isJacket?0:1], (bt)->{actionPerformed(bt);}) {
    		@Override
    		public void onClick(double mouseX, double moudeY){
    			actionPerformed(this);
    		}
    	};
		GuiButton b5 = new GuiButton(122, width / 2 - 180, height / 6 + 48 + 12, 60, 20, button122[efplayer.isLeftLeg?0:1], (bt)->{actionPerformed(bt);}) {
    		@Override
    		public void onClick(double mouseX, double moudeY){
    			actionPerformed(this);
    		}
    	};
		GuiButton b6 = new GuiButton(123, width / 2 - 180 + 60, height / 6 + 48 + 12, 60, 20, button123[efplayer.isRightLeg?0:1], (bt)->{actionPerformed(bt);}) {
    		@Override
    		public void onClick(double mouseX, double moudeY){
    			actionPerformed(this);
    		}
    	};
		GuiButton b7 = new GuiButton(124, width / 2 - 180, height / 6 + 72 + 12, 60, 20, button124[efplayer.isLeftSleeve?0:1], (bt)->{actionPerformed(bt);}) {
    		@Override
    		public void onClick(double mouseX, double moudeY){
    			actionPerformed(this);
    		}
    	};
		GuiButton b8 = new GuiButton(125, width / 2 - 180 + 60, height / 6 + 72 + 12, 60, 20, button125[efplayer.isRightSleeve?0:1], (bt)->{actionPerformed(bt);}) {
    		@Override
    		public void onClick(double mouseX, double moudeY){
    			actionPerformed(this);
    		}
    	};
    	this.addButton(b1);
    	this.addButton(b2);
    	this.addButton(b3);
    	this.addButton(b4);
    	this.addButton(b5);
    	this.addButton(b6);
    	this.addButton(b7);
    	this.addButton(b8);

	}

	@Override
	protected void actionPerformed(Button button) {
		GuiButton guibutton = (GuiButton)button;
		super.actionPerformed(guibutton);
		boolean equip;
		if (!guibutton.active) {
			return;
		}
		switch (guibutton.id) {
		case 100:
			// スキンのロード元

			if (playerList.size() <= ++playerIndex) {
				playerIndex = 0;
			}
			guibutton.setMessage(playerList.get(playerIndex));
			efplayer.skinUser = playerIndex == 0 ? "" : guibutton.getMessage();
			efplayer.setURLSkin();
			break;
		case 101:
			int slim = efplayer.isSlim()?0:1;
			efplayer.setSlim(slim);
			guibutton.setMessage(button101[slim]);
			efplayer.setURLSkin();
			break;

		case 120:
			efplayer.isHat=!efplayer.isHat;
			guibutton.setMessage(button120[efplayer.isHat?0:1]);
			break;
		case 121:
			efplayer.isJacket=!efplayer.isJacket;
			guibutton.setMessage(button121[efplayer.isJacket?0:1]);
			break;
		case 122:
			efplayer.isLeftLeg=!efplayer.isLeftLeg;
			guibutton.setMessage(button122[efplayer.isLeftLeg?0:1]);
			break;
		case 123:
			efplayer.isRightLeg=!efplayer.isRightLeg;
			guibutton.setMessage(button123[efplayer.isRightLeg?0:1]);
			break;
		case 124:
			efplayer.isLeftSleeve=!efplayer.isLeftSleeve;
			guibutton.setMessage(button124[efplayer.isLeftSleeve?0:1]);
			break;
		case 125:
			efplayer.isRightSleeve=!efplayer.isRightSleeve;
			guibutton.setMessage(button125[efplayer.isRightSleeve?0:1]);
			break;
		case 126:
			efplayer.isSpectator = !efplayer.isSpectator;
			break;
		}
	}
	public static void afterRender(ContainerPause container, PlayerInventory playerInv, ITextComponent titleIn, EntityDool entityfigure) {


	}

}
