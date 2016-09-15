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
		super(pMinecraft, pOwner.field_146294_l, pOwner.field_146295_m, 32, pOwner.field_146295_m - 52, 36);
		mc = pMinecraft;
		ownerGui = pOwner;
		selected = -1;
	}

	@Override
	protected int func_148127_b() {
		return ownerGui.entityMap.keySet().toArray().length;
	}

	@Override
	protected void func_148144_a(int var1, boolean var2, int mouseX, int mouseY) {
		String s = ownerGui.entityMap.keySet().toArray()[var1].toString();
		EntityLivingBase lel = (EntityLivingBase) ownerGui.entityMap.get(s);
		ownerGui.clickSlot(var1, var2, s, lel);
		selected = var1;
	}

	@Override
	protected boolean func_148131_a(int var1) {
		return var1 == selected;
	}

	@Override
	protected void func_148123_a() {
		ownerGui.func_146276_q_();
	}

	@Override
	public void func_148128_a(int par1, int par2, float par3) {
		super.func_148128_a(par1, par2, par3);
	}

	@Override
	protected void func_180791_a(int entryID, int pX, int pY, int pDrawheight, int mouseXIn, int mouseYIn) {
		// 基本スロットの描画、細かい所はオーナー側で
		// Entityの確保
		String s = ownerGui.entityMap.keySet().toArray()[entryID].toString();
		boolean lf = ownerGui.exclusionList.contains(s);
		EntityLivingBase entityliving = lf ? null : (EntityLivingBase) ownerGui.entityMap.get(s);

		// 独自描画
		ownerGui.drawSlot(entryID, pX, pY, pDrawheight, Tessellator.func_178181_a(), s, entityliving);

		// 除外判定
		if (lf) {
			ownerGui.func_73731_b(ownerGui.fontRenderObj(), "NoImage", pX + 15, pY + 12, 0xffffff);
			return;
		}
		entityliving.func_70029_a(mc.field_71441_e);

		// 伽羅の表示
		GL11.glEnable(GL11.GL_COLOR_MATERIAL);
		GL11.glPushMatrix();
		float f1 = 15F;
		if (entityliving.field_70131_O > 2F) {
			f1 = f1 * 3F / entityliving.field_70131_O;
		}
		float lxp = ((entryID & 1) == 0) ? (float) pX + 30F : (float) (ownerGui.field_146294_l - pX) - 30F;
		GL11.glTranslatef(lxp, pY + 30F, 50F + f1);
		GL11.glScalef(-f1, f1, f1);
		GL11.glRotatef(180F, 0.0F, 0.0F, 1.0F);
		float f5 = lxp - field_148150_g;
		float f6 = (float) ((pY + 30) - 10) - field_148162_h;
		GL11.glRotatef(135F, 0.0F, 1.0F, 0.0F);
		RenderHelper.func_74519_b();
		GL11.glRotatef(-135F, 0.0F, 1.0F, 0.0F);
		GL11.glRotatef(-(float) Math.atan(f6 / 40F) * 20F, 1.0F, 0.0F, 0.0F);
		entityliving.field_70761_aq = (float) Math.atan(f5 / 40F) * 20F;
		entityliving.field_70177_z = (float) Math.atan(f5 / 40F) * 40F;
		entityliving.field_70125_A = -(float) Math.atan(f6 / 40F) * 20F;
		entityliving.field_70758_at = entityliving.field_70759_as;
		entityliving.field_70759_as = entityliving.field_70177_z;
		GL11.glTranslatef(0.0F, (float)entityliving.func_70033_W(), 0.0F);
		Minecraft.func_71410_x().func_175598_ae().field_78735_i = 180F;
		try {
			Minecraft.func_71410_x().func_175598_ae().func_188391_a(entityliving, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, false);
		} catch (Exception e) {
			ownerGui.exclusionList.add(s);
		}
		// 影だかバイオームだかの処理?
		GL11.glPopMatrix();
		RenderHelper.func_74518_a();
		GL11.glDisable(GL12.GL_RESCALE_NORMAL);
		OpenGlHelper.func_77473_a(OpenGlHelper.field_77476_b);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		OpenGlHelper.func_77473_a(OpenGlHelper.field_77478_a);
	}

}
