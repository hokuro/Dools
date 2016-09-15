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

	public void func_73866_w_() {
		super.func_73866_w_();

		int wcarringid = Block.func_149682_b(eenderman.func_175489_ck()==null?Blocks.field_150350_a:eenderman.func_175489_ck().func_177230_c());
		if (wcarringid == 0) {
			carringid = 2;
		}else{
			carringid = wcarringid;
		}
		field_146292_n.add(new GuiButton(102, field_146294_l / 2 - 140,
				field_146295_m / 6 + 0 + 12, 80, 20,
				button102[eenderman.func_70823_r() ? 0 : 1]));
		field_146292_n.add(new GuiButton(103, field_146294_l / 2 - 140,
				field_146295_m / 6 + 24 + 12, 80, 20,
				button103[wcarringid == 0 ? 0 : 1]));

		bt150 = new GuiButton(150, field_146294_l / 2 - 120, field_146295_m / 6 + 48 + 12, 40,
				20, String.format("%d", carringid));
		field_146292_n.add(bt150);
		field_146292_n.add(new GuiButton(151, field_146294_l / 2 - 140,
				field_146295_m / 6 + 48 + 12, 20, 20, "+"));
		field_146292_n.add(new GuiButton(152, field_146294_l / 2 - 80,
				field_146295_m / 6 + 48 + 12, 20, 20, "-"));

	}

	protected void func_146284_a(GuiButton guibutton) {
		super.func_146284_a(guibutton);

		if (!guibutton.field_146124_l) {
			return;
		}
		if (guibutton.field_146127_k == 150) {
			carringid = 2;
		}
		if (guibutton.field_146127_k == 151) {
			try {
				Set<Block> canpic = (Set<Block>) Dools.getPrivateValue(EntityEnderman.class, null, "carriableBlocks");
				int i = (carringid + 1) & 0xff;
				while (i != carringid) {
					if (canpic.contains(Block.func_149729_e(i))) {
						carringid = i;
						break;
					}
					i = (i + 1) & 0xff;
				}
			} catch (Exception exception) {
			}
		}
		if (guibutton.field_146127_k == 152) {
			try {
				Set<Block> canpic = (Set<Block>) Dools.getPrivateValue(EntityEnderman.class, null, "carriableBlocks");
				int i = (carringid + 1) & 0xff;
				while (i != carringid) {
					if (canpic.contains(Block.func_149729_e(i))) {
						carringid = i;
						break;
					}
					i = (i + 1) & 0xff;
				}
			} catch (Exception exception) {
			}
		}
		switch (guibutton.field_146127_k) {
		case 102:
			EntityLivingBase et = null;
			if (!eenderman.func_70823_r()){et=eenderman;}
			eenderman.func_70624_b(et);
			//eenderman.setScreaming(!eenderman.isScreaming());
			guibutton.field_146126_j = button102[eenderman.func_70823_r() ? 0 : 1];
			break;

		case 103:
			eenderman.func_175490_a(Block.func_176220_d(Block.func_149682_b(
					eenderman.func_175489_ck()==null?Blocks.field_150350_a:eenderman.func_175489_ck().func_177230_c()) > 0 ? 0 : carringid));
			guibutton.field_146126_j = button103[Block.func_149682_b(eenderman.func_175489_ck().func_177230_c()) > 0 ? 0 : 1];
			break;

		case 150:
		case 151:
		case 152:
			bt150.field_146126_j = String.format("%d", carringid);
			if (Block.func_149682_b(eenderman.func_175489_ck()==null?Blocks.field_150350_a:eenderman.func_175489_ck().func_177230_c()) > 0) {
				eenderman.func_175490_a(Block.func_176220_d(carringid));
			}
			break;
		}
	}

}
