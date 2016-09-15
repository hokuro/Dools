package basashi.dools.gui;

import java.util.Set;

import basashi.dools.core.Dools;
import basashi.dools.entity.EntityDool;
import net.minecraft.block.Block;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.init.Blocks;

public class GuiDoolPause_Enderman extends GuiDoolPause {

	private EntityEnderman eenderman;
	private int carringid;
	private GuiButton bt150;
	private String button102[] = { "Attack", "Nomal" };
	private String button103[] = { "Carried", "Free" };


	public GuiDoolPause_Enderman(EntityDool entityfigua) {
		super(entityfigua);
		eenderman = (EntityEnderman) targetEntity.renderEntity;
	}

	public void initGui() {
		super.initGui();

		int wcarringid = Block.getIdFromBlock(eenderman.getHeldBlockState()==null?Blocks.air:eenderman.getHeldBlockState().getBlock());
		if (wcarringid == 0) {
			carringid = 2;
		}else{
			carringid = wcarringid;
		}
		buttonList.add(new GuiButton(102, width / 2 - 140,
				height / 6 + 0 + 12, 80, 20,
				button102[eenderman.isScreaming() ? 0 : 1]));
		buttonList.add(new GuiButton(103, width / 2 - 140,
				height / 6 + 24 + 12, 80, 20,
				button103[wcarringid == 0 ? 0 : 1]));

		bt150 = new GuiButton(150, width / 2 - 120, height / 6 + 48 + 12, 40,
				20, String.format("%d", carringid));
		buttonList.add(bt150);
		buttonList.add(new GuiButton(151, width / 2 - 140,
				height / 6 + 48 + 12, 20, 20, "+"));
		buttonList.add(new GuiButton(152, width / 2 - 80,
				height / 6 + 48 + 12, 20, 20, "-"));

	}

	protected void actionPerformed(GuiButton guibutton) {
		super.actionPerformed(guibutton);

		if (!guibutton.enabled) {
			return;
		}
		if (guibutton.id == 150) {
			carringid = 2;
		}
		if (guibutton.id == 151) {
			try {
				Set<Block> canpic = (Set<Block>) Dools.getPrivateValue(EntityEnderman.class, null, "carriableBlocks");
				int i = (carringid + 1) & 0xff;
				while (i != carringid) {
					if (canpic.contains(Block.getBlockById(i))) {
						carringid = i;
						break;
					}
					i = (i + 1) & 0xff;
				}
			} catch (Exception exception) {
			}
		}
		if (guibutton.id == 152) {
			try {
				Set<Block> canpic = (Set<Block>) Dools.getPrivateValue(EntityEnderman.class, null, "carriableBlocks");
				int i = (carringid + 1) & 0xff;
				while (i != carringid) {
					if (canpic.contains(Block.getBlockById(i))) {
						carringid = i;
						break;
					}
					i = (i + 1) & 0xff;
				}
			} catch (Exception exception) {
			}
		}
		switch (guibutton.id) {
		case 102:
			EntityLivingBase et = null;
			if (!eenderman.isScreaming()){et=eenderman;}
			eenderman.setAttackTarget(et);
			//eenderman.setScreaming(!eenderman.isScreaming());
			guibutton.displayString = button102[eenderman.isScreaming() ? 0 : 1];
			break;

		case 103:
			eenderman.setHeldBlockState(Block.getStateById(Block.getIdFromBlock(
					eenderman.getHeldBlockState()==null?Blocks.air:eenderman.getHeldBlockState().getBlock()) > 0 ? 0 : carringid));
			guibutton.displayString = button103[Block.getIdFromBlock(eenderman.getHeldBlockState().getBlock()) > 0 ? 0 : 1];
			break;

		case 150:
		case 151:
		case 152:
			bt150.displayString = String.format("%d", carringid);
			if (Block.getIdFromBlock(eenderman.getHeldBlockState()==null?Blocks.air:eenderman.getHeldBlockState().getBlock()) > 0) {
				eenderman.setHeldBlockState(Block.getStateById(carringid));
			}
			break;
		}
	}

}
