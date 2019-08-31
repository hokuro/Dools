package basashi.dools.gui;

import java.util.Set;

import com.google.common.collect.Sets;

import basashi.dools.entity.EntityDool;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.init.Blocks;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.registry.IRegistry;

public class GuiDoolPause_enderman extends GuiDoolPause {

	private EntityEnderman eenderman;
	private int carringid;
	private GuiButton bt150;
	private String button102[] = { "Attack", "Nomal" };
	private String button103[] = { "Carried", "Free" };
	private Set<IBlockState> canpic;


	public GuiDoolPause_enderman(EntityDool entityfigua) {
		super(entityfigua);
		eenderman = (EntityEnderman) targetEntity.renderEntity;

		canpic = Sets.<IBlockState>newIdentityHashSet();
		IRegistry.field_212618_g.forEach((blk)->{
			if (blk.isIn(BlockTags.ENDERMAN_HOLDABLE)) {
				canpic.add(blk.getDefaultState());
			}
		});
	}

	public void initGui() {
		super.initGui();

		// エンダーマンが持ってるブロックのIDを取り出す(1.13.2で関数名が不定になっている)
		int wcarringid =  Block.getStateId(eenderman.func_195405_dq()==null?Blocks.AIR.getDefaultState():eenderman.func_195405_dq());
		if (wcarringid == 0) {
			carringid = 2;
		}else{
			carringid = wcarringid;
		}
		GuiButton b1 = new GuiButton(102, width / 2 - 140,
				height / 6 + 0 + 12, 80, 20,
				button102[eenderman.isScreaming() ? 0 : 1]){
    		@Override
    		public void onClick(double mouseX, double moudeY){
    			actionPerformed(this);
    		}
    	};
    	GuiButton b2 = new GuiButton(103, width / 2 - 140,
				height / 6 + 24 + 12, 80, 20,
				button103[wcarringid == 0 ? 0 : 1]){
    		@Override
    		public void onClick(double mouseX, double moudeY){
    			actionPerformed(this);
    		}
    	};

		bt150 = new GuiButton(150, width / 2 - 120, height / 6 + 48 + 12, 40,
				20, String.format("%d", carringid)){
		    		@Override
		    		public void onClick(double mouseX, double moudeY){
		    			actionPerformed(this);
		    		}
		    	};
	    GuiButton b3 = new GuiButton(151, width / 2 - 140,
				height / 6 + 48 + 12, 20, 20, "+"){
    		@Override
    		public void onClick(double mouseX, double moudeY){
    			actionPerformed(this);
    		}
    	};
    	GuiButton b4 = new GuiButton(152, width / 2 - 80,
				height / 6 + 48 + 12, 20, 20, "-"){
    		@Override
    		public void onClick(double mouseX, double moudeY){
    			actionPerformed(this);
    		}
    	};

    	buttons.add(b1);
    	buttons.add(b2);
    	buttons.add(bt150);
    	buttons.add(b3);
    	buttons.add(b4);
    	this.children.addAll(buttons);

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
				int i = (carringid + 1);
				while (i != carringid) {
					if (canpic.contains(Block.getStateById(i))) {
						carringid = i;
						break;
					}
					i = (i + 1) ;
					if (i == Integer.MAX_VALUE) {
						i = 0;
					}
				}
			} catch (Exception exception) {
			}
		}
		if (guibutton.id == 152) {
			try {
				int i = (carringid - 1);
				while (i != carringid) {
					if (canpic.contains(Block.getStateById(i))) {
						carringid = i;
						break;
					}
					i = (i - 1);
					if (i < 0) {
						i = Integer.MAX_VALUE;
					}
				}
			} catch (Exception exception) {
			}
		}
		switch (guibutton.id) {
		case 102:
			EntityLivingBase et = null;
			if (!eenderman.isScreaming()){et=eenderman;}
			eenderman.setAttackTarget(et);
			guibutton.displayString = button102[eenderman.isScreaming() ? 0 : 1];
			break;

		case 103:
			if (eenderman.func_195405_dq() != null){
				eenderman.func_195406_b(Blocks.AIR.getDefaultState());
				guibutton.displayString = button103[1];
			}else{
				eenderman.func_195406_b(Block.getStateById(Block.getStateId(
						eenderman.func_195405_dq()==null?Blocks.AIR.getDefaultState():eenderman.func_195405_dq()) > 0 ? 0 : carringid));

				guibutton.displayString = button103[0];
			}

			break;

		case 150:
		case 151:
		case 152:
			bt150.displayString = String.format("%d", carringid);
			if (Block.getStateId(eenderman.func_195405_dq()==null?Blocks.AIR.getDefaultState():eenderman.func_195405_dq()) > 0) {
				eenderman.func_195406_b(Block.getStateById(carringid));
			}
			break;
		}
	}

}
