package basashi.dools.gui;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import basashi.dools.entity.EntityDool;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.monster.EndermanEntity;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.registry.Registry;

public class GuiDoolPause_enderman extends GuiDoolPause {

	private EndermanEntity entity;
	private int carringid;
	private Button bt150;
	private String button102[] = { "Attack", "Nomal" };
	private String button103[] = { "Carried", "Free" };
	private List<BlockState> canpic;

	public GuiDoolPause_enderman(EntityDool entityfigure) {
		super(entityfigure);
		entity = (EndermanEntity) targetEntity.renderEntity;
	}

	@Override
	public void init() {
		super.init();
		// iエンダーマンが持ってるブロックのIDを取り出す(1.13.2で関数名が不定になっている)
		carringid = -1;
		BlockState carryState = entity.getHeldBlockState();
		canpic = new ArrayList<BlockState>();
		int workIndex = 0;
		Iterator<Block> it = Registry.BLOCK.iterator();
		while(it.hasNext()) {
			Block blk = it.next();
			if (blk.isIn(BlockTags.ENDERMAN_HOLDABLE)) {
				canpic.add(blk.getDefaultState());
				if (carryState != null && carryState.getBlock() == blk) {
					carringid = workIndex;
				}
				workIndex++;
			}
		}
		this.addButton(new Button(width / 2 - 140, height / 6 + 0 + 12, 80, 20, button102[entity.isScreaming() ? 0 : 1], (bt)->{actionPerformed(102, bt);}));
    	this.addButton(new Button(width / 2 - 140, height / 6 + 24 + 12, 80, 20, button103[carringid < 0 ? 0 : 1], (bt)->{actionPerformed(103, bt);}));
		bt150 = new Button(width / 2 - 120, height / 6 + 48 + 12, 40, 20, String.format("%d", carringid), (bt)->{actionPerformed(150, bt);});
    	this.addButton(bt150);
		this.addButton(new Button(width / 2 - 140, height / 6 + 48 + 12, 20, 20, "+", (bt)->{actionPerformed(151, bt);}));
		this.addButton(new Button(width / 2 - 80, height / 6 + 48 + 12, 20, 20, "-", (bt)->{actionPerformed(152, bt);}));
	}

	@Override
	protected void actionPerformed(int id, Button button) {

		switch(id) {
		case 150:
			carringid = -1;
			break;
		case 151:
			carringid++;
			if (canpic.size() <= carringid) {carringid = -1;}
			break;

		}
		switch (id) {
		case 102:
			LivingEntity et = null;
			if (!entity.isScreaming()){et=entity;}
			entity.setAttackTarget(et);
			button.setMessage(button102[entity.isScreaming() ? 0 : 1]);
			break;

		case 103:
			if (entity.getHeldBlockState() != null){
				entity.func_195406_b(Blocks.AIR.getDefaultState());
				button.setMessage(button103[1]);
			}else{
				entity.func_195406_b(Block.getStateById(Block.getStateId(
						entity.getHeldBlockState()==null?Blocks.AIR.getDefaultState():entity.getHeldBlockState()) > 0 ? 0 : carringid));
				button.setMessage(button103[0]);
			}
			break;

		case 150:
		case 151:
		case 152:
			if (carringid < 0) {
				bt150.setMessage("none");
			}else {
				BlockState blk = canpic.get(carringid);
				entity.func_195406_b(blk);
				bt150.setMessage(I18n.format(blk.getBlock().getTranslationKey()));

			}
			break;
		}
		super.actionPerformed(id, button);
	}
}
