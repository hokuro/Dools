package basashi.dools.gui;

import java.io.IOException;

import basashi.dools.core.Dools;
import basashi.dools.entity.EntityDool;
import basashi.dools.item.ItemDool;
import basashi.dools.network.MessageServer_SpawnDool;
import basashi.dools.server.ServerDool;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;

/**
 * ブランクのFigureの設置時選択用GUI。
 * GUIを閉じた時にサーバーへ情報を送信。
 */
public class GuiDoolSelect extends GuiMobSelect {

	protected EntityDool targetFigure;


	public GuiDoolSelect(World pWorld, EntityDool entityfigure) {
		super(pWorld, ItemDool.entityStringMap);
		screenTitle = "Figure Select";
		targetFigure = entityfigure;
		if (targetFigure != null) {
			targetFigure.func_70029_a(pWorld);
		}
	}

	@Override
	public void func_146274_d() throws IOException{
		super.func_146274_d();
		this.selectPanel.func_178039_p();
	}

	@Override
	public void func_73866_w_() {
		super.func_73866_w_();
		field_146292_n.add(new GuiButton(300, field_146294_l / 2 - 60, field_146295_m - 44, 120, 20, "Select"));
	}

	@Override
	protected void func_146284_a(GuiButton guibutton) {
		if (!guibutton.field_146124_l) {
			return;
		}
		if (guibutton.field_146127_k == 300) {
			field_146297_k.func_147108_a(null);
			return;
		}
	}

	@Override
	public void func_146281_b() {
		super.func_146281_b();
		ServerDool isf = Dools.getServerFigure(targetFigure);
		String name="";
		if (isf != null) {
			isf.setRotation(targetFigure);
			name=targetFigure.mobString;
		}
		// 設定されたEntityに適合するパケットセンダーを実行
		Dools.INSTANCE.sendToServer(new MessageServer_SpawnDool(targetFigure.field_70165_t,targetFigure.field_70163_u,targetFigure.field_70161_v,targetFigure.field_70177_z,name));
	}

	@Override
	public void clickSlot(int pIndex, boolean pDoubleClick, String pName, EntityLivingBase pEntity) {
		targetFigure.setRenderEntity(pEntity);
	}

	@Override
	public void drawSlot(int pSlotindex, int pX, int pY, int pDrawheight, Tessellator pTessellator, String pName, Entity pEntity) {
		func_73731_b(field_146289_q, pName, (field_146294_l - field_146289_q.func_78256_a(pName)) / 2, pY + 10, 0xffffff);
	}


}
