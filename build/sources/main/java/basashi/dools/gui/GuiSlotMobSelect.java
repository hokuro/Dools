package basashi.dools.gui;


import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiSlot;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.EntityLivingBase;

public class GuiSlotMobSelect extends GuiSlot {

	protected int selected;
	protected Minecraft mc;
	public GuiMobSelect ownerGui;


	public GuiSlotMobSelect(Minecraft pMinecraft, GuiMobSelect pOwner) {
		super(pMinecraft, pOwner.width, pOwner.height, 32, pOwner.height - 52, 36);
		mc = pMinecraft;
		ownerGui = pOwner;
		selected = -1;
	}

	@Override
	protected int getSize() {
		return ownerGui.entityMap.keySet().toArray().length;
	}

	@Override
	protected void elementClicked(int var1, boolean var2, int mouseX, int mouseY) {
		String s = ownerGui.entityMap.keySet().toArray()[var1].toString();
		EntityLivingBase lel = (EntityLivingBase) ownerGui.entityMap.get(s);
		ownerGui.clickSlot(var1, var2, s, lel);
		selected = var1;
	}

	@Override
	protected boolean isSelected(int var1) {
		return var1 == selected;
	}

	@Override
	protected void drawBackground() {
		ownerGui.drawDefaultBackground();
	}

	@Override
	public void drawScreen(int par1, int par2, float par3) {
		super.drawScreen(par1, par2, par3);
	}

	@Override
	protected void drawSlot(int entryID, int pX, int pY, int pDrawheight, int mouseXIn, int mouseYIn) {
		// 基本スロットの描画、細かい所はオーナー側で
		// Entityの確保
		String s = ownerGui.entityMap.keySet().toArray()[entryID].toString();
		boolean lf = ownerGui.exclusionList.contains(s);
		EntityLivingBase entityliving = lf ? null : (EntityLivingBase) ownerGui.entityMap.get(s);

		// 独自描画
		ownerGui.drawSlot(entryID, pX, pY, pDrawheight, Tessellator.getInstance(), s, entityliving);

		// 除外判定
		if (lf) {
			ownerGui.drawString(ownerGui.fontRenderObj(), "NoImage", pX + 15, pY + 12, 0xffffff);
			return;
		}
		entityliving.setWorld(mc.theWorld);

		// 伽羅の表示
		GL11.glEnable(GL11.GL_COLOR_MATERIAL);
		GL11.glPushMatrix();
		float f1 = 15F;
		if (entityliving.height > 2F) {
			f1 = f1 * 3F / entityliving.height;
		}
		float lxp = ((entryID & 1) == 0) ? (float) pX + 30F : (float) (ownerGui.width - pX) - 30F;
		GL11.glTranslatef(lxp, pY + 30F, 50F + f1);
		GL11.glScalef(-f1, f1, f1);
		GL11.glRotatef(180F, 0.0F, 0.0F, 1.0F);
		float f5 = lxp - mouseX;
		float f6 = (float) ((pY + 30) - 10) - mouseY;
		GL11.glRotatef(135F, 0.0F, 1.0F, 0.0F);
		RenderHelper.enableStandardItemLighting();
		GL11.glRotatef(-135F, 0.0F, 1.0F, 0.0F);
		GL11.glRotatef(-(float) Math.atan(f6 / 40F) * 20F, 1.0F, 0.0F, 0.0F);
		entityliving.renderYawOffset = (float) Math.atan(f5 / 40F) * 20F;
		entityliving.rotationYaw = (float) Math.atan(f5 / 40F) * 40F;
		entityliving.rotationPitch = -(float) Math.atan(f6 / 40F) * 20F;
		entityliving.prevRotationYawHead = entityliving.rotationYawHead;
		entityliving.rotationYawHead = entityliving.rotationYaw;
		GL11.glTranslatef(0.0F, (float)entityliving.getYOffset(), 0.0F);
		Minecraft.getMinecraft().getRenderManager().playerViewY = 180F;
		try {
			Minecraft.getMinecraft().getRenderManager().doRenderEntity(entityliving, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, false);
		} catch (Exception e) {
			ownerGui.exclusionList.add(s);
		}
		// 影だかバイオームだかの処理?
		GL11.glPopMatrix();
		RenderHelper.disableStandardItemLighting();
		GL11.glDisable(GL12.GL_RESCALE_NORMAL);
		OpenGlHelper.setActiveTexture(OpenGlHelper.lightmapTexUnit);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		OpenGlHelper.setActiveTexture(OpenGlHelper.defaultTexUnit);
	}

}
