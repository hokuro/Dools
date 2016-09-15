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
			targetFigure.setWorld(pWorld);
		}
	}

	@Override
	public void handleMouseInput() throws IOException{
		super.handleMouseInput();
		this.selectPanel.handleMouseInput();
	}

	@Override
	public void initGui() {
		super.initGui();
		buttonList.add(new GuiButton(300, width / 2 - 60, height - 44, 120, 20, "Select"));
	}

	@Override
	protected void actionPerformed(GuiButton guibutton) {
		if (!guibutton.enabled) {
			return;
		}
		if (guibutton.id == 300) {
			mc.displayGuiScreen(null);
			return;
		}
	}

	@Override
	public void onGuiClosed() {
		super.onGuiClosed();
		ServerDool isf = Dools.getServerFigure(targetFigure);
		String name="";
		if (isf != null) {
			isf.setRotation(targetFigure);
			name=targetFigure.mobString;
		}
		// 設定されたEntityに適合するパケットセンダーを実行
		Dools.INSTANCE.sendToServer(new MessageServer_SpawnDool(targetFigure.posX,targetFigure.posY,targetFigure.posZ,targetFigure.rotationYaw,name));
	}

	@Override
	public void clickSlot(int pIndex, boolean pDoubleClick, String pName, EntityLivingBase pEntity) {
		targetFigure.setRenderEntity(pEntity);
	}

	@Override
	public void drawSlot(int pSlotindex, int pX, int pY, int pDrawheight, Tessellator pTessellator, String pName, Entity pEntity) {
		drawString(fontRendererObj, pName, (width - fontRendererObj.getStringWidth(pName)) / 2, pY + 10, 0xffffff);
	}


}