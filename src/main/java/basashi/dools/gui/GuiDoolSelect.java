package basashi.dools.gui;

import basashi.dools.core.Dools;
import basashi.dools.entity.EntityDool;
import basashi.dools.network.MessageHandler;
import basashi.dools.server.ServerDool;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EntityType;
import net.minecraft.world.World;

/**
 * ブランクのFigureの設置時選択用GUI。
 * GUIを閉じた時にサーバーへ情報を送信。
 */
public class GuiDoolSelect extends GuiMobSelect {

	protected EntityDool targetFigure;


	public GuiDoolSelect(World pWorld, EntityDool entityfigure) {
		//super(pWorld, ItemDool.entityStringMap);
		super(pWorld);
		screenTitle = "Figure Select";
		targetFigure = entityfigure;
		if (targetFigure != null) {
			targetFigure.setWorld(pWorld);
		}
	}



	@Override
	public boolean mouseClicked(double p_mouseClicked_1_, double p_mouseClicked_3_, int p_mouseClicked_5_) {
		super.mouseClicked(p_mouseClicked_1_,p_mouseClicked_3_,p_mouseClicked_5_);
		return this.selectPanel.mouseClicked(p_mouseClicked_1_, p_mouseClicked_3_, p_mouseClicked_5_);
	}

	@Override
	public void initGui() {
		super.initGui();
		GuiButton b1 = new GuiButton(300, width / 2 - 60, height - 44, 120, 20, "Select") {
			@Override
			public void onClick(double mouseX, double mouseY) {
				actionPerformed(this);
			}
		};

		buttons.add(b1);
		this.children.addAll(buttons);
	}

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
		MessageHandler.Send_MessageServer_SpawnDool(targetFigure.posX,targetFigure.posY,targetFigure.posZ,targetFigure.rotationYaw,name);
		//Dools.INSTANCE.sendToServer(new MessageServer_SpawnDool(targetFigure.posX,targetFigure.posY,targetFigure.posZ,targetFigure.rotationYaw,name));
	}

	@Override
	public void clickSlot(int pIndex, boolean pDoubleClick, String pName, EntityLivingBase pEntity) {
		targetFigure.setRenderEntity(pEntity);
	}

	@Override
	public void drawSlot(int pSlotindex, int pX, int pY, int pDrawheight, Tessellator pTessellator, String pName, Entity pEntity) {
		String name;
		name = I18n.format(EntityType.getById(pName).getTranslationKey());  //EntityList.getTranslationName(new ResourceLocation(pName))!=null?EntityList.getTranslationName(new ResourceLocation(pName)):pName;
		drawString(fontRenderObj(), name, (width - fontRenderer.getStringWidth(name)) / 2, pY + 10, 0xffffff);
	}


}
