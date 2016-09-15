package basashi.dools.gui;


import java.io.IOException;

import org.lwjgl.opengl.EXTRescaleNormal;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import basashi.dools.container.ContainerItemSelect;
import basashi.dools.core.Dools;
import basashi.dools.core.log.ModLog;
import basashi.dools.entity.EntityDool;
import basashi.dools.network.MessagePause;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;

public class GuiDoolPause extends GuiScreen {

	protected String screenTitle;
	protected EntityDool targetEntity;
	protected String button10[] = { "Dismount", "Ride" };
	protected String button11[] = { "Stand", "Sneak" };
	public static float button13[] = { 1, 2, 4, 6 };
	protected String button16[] = { "A", "C" };
	protected GuiSlider figureYaw;

	public GuiDoolPause(EntityDool entityfigure) {
		screenTitle = "Figure Pause";
		targetEntity = entityfigure;
	}

	@Override
	public boolean func_73868_f() {
		// フィギュアをいじっていると時を忘れるよね？
		return false;
	}

	@Override
	protected void func_73869_a(char par1, int par2) {
		if (par2 == 1) {
			// データをサーバーへ送る
			Dools.INSTANCE.sendToServer(new MessagePause(Dools.getServerFigure(targetEntity).getData(targetEntity)));
			ModLog.log().debug("DataSendToServer.");
			Dools.getServerFigure(targetEntity).sendItems(targetEntity, true);
		}
		try {
			super.func_73869_a(par1, par2);
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
	}

	@Override
	public void func_146281_b() {
		super.func_146281_b();
	}

	@Override
	public void func_73863_a(int i, int j, float f) {
		func_146276_q_();
		func_73732_a(this.field_146289_q, screenTitle, field_146294_l / 2, 20, 0xffffff);

		// 基準の向きを変更
		targetEntity.additionalYaw = figureYaw.getSliderValue();
		// キャラ
		int l = field_146294_l / 2;
		int k = field_146295_m / 6 + 72;// height / 2;
		EntityLivingBase elt;
		try{
			elt = (EntityLiving) targetEntity.renderEntity;
		}catch(Exception ex){
			elt = (EntityLivingBase) targetEntity.renderEntity;
		}
		GL11.glEnable(EXTRescaleNormal.GL_RESCALE_NORMAL_EXT);
		GL11.glEnable(GL11.GL_COLOR_MATERIAL);
		GL11.glPushMatrix();
		GL11.glTranslatef(l, k + 30F, 50F);
		// float f1 = 45F;
		float f1 = 80F / elt.field_70131_O * (elt.func_70631_g_()?0.5F:1.0F);
		GL11.glScalef(-f1, f1, f1);
		GL11.glRotatef(180F, 0.0F, 0.0F, 1.0F);
		float f2 = elt.field_70761_aq;
		float f5 = (float) (l + 0) - i;
		float f6 = (float) ((k + 30) - 50) - j;
		GL11.glRotatef(135F, 0.0F, 1.0F, 0.0F);
		RenderHelper.func_74519_b();
		GL11.glRotatef(targetEntity.additionalYaw - 135F, 0.0F, 1.0F, 0.0F);
		elt.field_70761_aq = 0F;
		elt.field_70177_z = (float) Math.atan(f5 / 40F) * 40F;
		elt.field_70125_A = -(float) Math.atan(f6 / 40F) * 20F;
		elt.field_70759_as = elt.field_70177_z;
		elt.field_70758_at = elt.field_70177_z;
		Dools.getServerFigure(targetEntity).setRotation(targetEntity);
		GL11.glTranslatef(0.0F, (float)elt.func_70033_W(), 0.0F);
		Minecraft.func_71410_x().func_175598_ae().field_78735_i = 180F;
		//Minecraft.getMinecraft().getRenderManager().renderEntityWithPosYaw(elt, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F);
		Minecraft.func_71410_x().func_175598_ae().func_188391_a(elt, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F,false);
		elt.field_70761_aq = f2;
		elt.field_70126_B = elt.field_70177_z + f2;
		elt.field_70127_C = elt.field_70125_A;
		elt.field_70759_as = elt.field_70177_z + f2;
		elt.field_70758_at = elt.field_70177_z + f2;
		Dools.getServerFigure(targetEntity).setRotation(targetEntity);

		// 影だかバイオームだかの処理?
		GL11.glPopMatrix();
		RenderHelper.func_74518_a();
		GL11.glDisable(GL12.GL_RESCALE_NORMAL);
		OpenGlHelper.func_77473_a(OpenGlHelper.field_77476_b);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		OpenGlHelper.func_77473_a(OpenGlHelper.field_77478_a);
		super.func_73863_a(i, j, f);
	}


	// 継承して独自処理を書く必要がある物

	@Override
	public void func_73866_w_() {
		if (targetEntity.renderEntity != null && GuiItemSelect.isChange) {
			setItems();
		}
		GuiItemSelect.isChange = false;

		field_146292_n.add(new GuiButton(10, field_146294_l / 2 + 60, field_146295_m / 6 + 48 + 12, 80, 20,
				(button10[targetEntity.renderEntity.func_184218_aH() ? 1 : 0])));
		field_146292_n.add(new GuiButton(11, field_146294_l / 2 + 60, field_146295_m / 6 + 72 + 12, 80, 20,
				(button11[targetEntity.renderEntity.func_70093_af() ? 1 : 0])));
		field_146292_n.add(new GuiButton(13, field_146294_l / 2 + 60, field_146295_m / 6 + 0 + 12, 60, 20,
				(String.format("1 / %.0f", targetEntity.zoom))));
		field_146292_n.add(new GuiButton(16, field_146294_l / 2 + 120, field_146295_m / 6 + 0 + 12, 20, 20,
				(button16[targetEntity.renderEntity.func_70631_g_() ? 1 : 0])));
		field_146292_n.add(new GuiButton(17, field_146294_l / 2 - 140, field_146295_m / 6 + 96 + 12, 80, 20,
				("EquipSelect")));
		figureYaw = new GuiSlider(15, field_146294_l / 2 - 50, field_146295_m / 6 + 96 + 12,
				"", (targetEntity.additionalYaw + 180F) / 360F, 360F, -180F).setStrFormat("%s%.2f").setDisplayString();
		field_146292_n.add(figureYaw);

		field_146292_n.add(new GuiButton(20, field_146294_l / 2 + 80, field_146295_m / 6 + 24 + 12,
				40, 20, String.format("%.2f", targetEntity.fyOffset)));
		field_146292_n.add(new GuiButton(21, field_146294_l / 2 + 60, field_146295_m / 6 + 24 + 12,
				20, 20, ("+")));
		field_146292_n.add(new GuiButton(22, field_146294_l / 2 + 120, field_146295_m / 6 + 24 + 12,
				20, 20, ("-")));


		//StringTranslate stringtranslate = StringTranslate.getInstance();
//		buttonList.add(new GuiButton(10, width / 2 + 60, height / 6 + 48 + 12, 80, 20,
//				stringtranslate.translateKey(button10[targetEntity.renderEntity.isRiding() ? 1 : 0])));
//		buttonList.add(new GuiButton(11, width / 2 + 60, height / 6 + 72 + 12, 80, 20,
//				stringtranslate.translateKey(button11[targetEntity.renderEntity.isSneaking() ? 1 : 0])));
//		buttonList.add(new GuiButton(13, width / 2 + 60, height / 6 + 0 + 12, 60, 20,
//				stringtranslate.translateKey(String.format("1 / %.0f", targetEntity.zoom))));
//		buttonList.add(new GuiButton(16, width / 2 + 120, height / 6 + 0 + 12, 20, 20,
//				stringtranslate.translateKey(button16[targetEntity.renderEntity.isChild() ? 1 : 0])));
//		buttonList.add(new GuiButton(17, width / 2 - 140, height / 6 + 96 + 12, 80, 20,
//				stringtranslate.translateKey("EquipSelect")));
//		figureYaw = new GuiSlider(15, width / 2 - 50, height / 6 + 96 + 12,
//				"", (targetEntity.additionalYaw + 180F) / 360F, 360F, -180F).setStrFormat("%s%.2f").setDisplayString();
//		buttonList.add(figureYaw);
//
//		buttonList.add(new GuiButton(20, width / 2 + 80, height / 6 + 24 + 12,
//				40, 20, String.format("%.2f", targetEntity.fyOffset)));
//		buttonList.add(new GuiButton(21, width / 2 + 60, height / 6 + 24 + 12,
//				20, 20, stringtranslate.translateKey("+")));
//		buttonList.add(new GuiButton(22, width / 2 + 120, height / 6 + 24 + 12,
//				20, 20, stringtranslate.translateKey("-")));
	}

	public float getGuiRenderYawOffset() {
		return 0.0F;
	}

	/**
	 * GUIの操作をした時の処理
	 */
	@Override
	protected void func_146284_a(GuiButton guibutton) {
		if (!guibutton.field_146124_l) {
			return;
		}
		if (guibutton.field_146127_k == 10) {
			if (targetEntity.isFigureRide) {
				// 載ってる
				if (targetEntity.renderEntity.func_184218_aH()){
					targetEntity.renderEntity.func_184210_p();
					guibutton.field_146126_j = button10[0];
				}
			} else {
				// 載ってない
				if (!targetEntity.renderEntity.func_184218_aH()){
					targetEntity.renderEntity.func_184205_a(targetEntity,true);
					guibutton.field_146126_j = button10[1];
				}
			}
			targetEntity.isFigureRide = targetEntity.renderEntity.func_184218_aH();
		}
		if (guibutton.field_146127_k == 11) {
			if (targetEntity.renderEntity.func_70093_af()) {
				// しゃがみ
				targetEntity.renderEntity.func_70095_a(false);
				guibutton.field_146126_j = button11[0];
			} else {
				// 立ち
				targetEntity.renderEntity.func_70095_a(true);
				guibutton.field_146126_j = button11[1];
			}
		}
		if (guibutton.field_146127_k == 13) {
			// 倍率
			int i = 0;
			float z;
			for (int j = 0; j < button13.length; j++) {
				z = button13[j] > 0 ? button13[j] : 1 / -button13[j];
				if (targetEntity.zoom == z) {
					if (j + 1 < button13.length) {
						i = j + 1;
					}
					break;
				}
			}
			z = button13[i] > 0 ? button13[i] : 1 / -button13[i];
			targetEntity.setZoom(z);
			if (z >= 1) {
				guibutton.field_146126_j = String.format("1 / %.0f", targetEntity.zoom);
			} else {
				guibutton.field_146126_j = String.format("%.0f / 1", -button13[i]);
			}
		}
		if (guibutton.field_146127_k == 16) {
			// 幼生態判定
			if (targetEntity.renderEntity instanceof EntityAgeable) {
				EntityAgeable lentity = (EntityAgeable)targetEntity.renderEntity;
				lentity.func_70873_a(lentity.func_70631_g_() ? 0 : -1);
				guibutton.field_146126_j = button16[lentity.func_70631_g_() ? 1 : 0];
			}
		}
		if (guibutton.field_146127_k == 17) {
			// 装備品の設定
			GuiItemSelect.clearInventory();
			getItems();
			field_146297_k.func_147108_a(new GuiItemSelect(this, targetEntity.renderEntity, field_146297_k.field_71439_g));
		}

		if (guibutton.field_146127_k == 20) {
			targetEntity.fyOffset = 0;
		}
		if (guibutton.field_146127_k == 21) {
			targetEntity.fyOffset += 0.05;
		}
		if (guibutton.field_146127_k == 22) {
			targetEntity.fyOffset -= 0.05;
		}
		for (int k = 0; k < field_146292_n.size(); k++) {
			GuiButton gb = (GuiButton) field_146292_n.get(k);
			if (gb.field_146127_k == 20) {
				gb.field_146126_j = String.format("%.2f", targetEntity.fyOffset);
			}
		}

	}

	/**
	 * アイテムを設定する
	 */
	public void setItems() {
		targetEntity.renderEntity.func_184201_a(EntityEquipmentSlot.MAINHAND,
				GuiItemSelect.inventoryItem.func_70301_a(ContainerItemSelect.slotFromType.get(EntityEquipmentSlot.MAINHAND)));
		targetEntity.renderEntity.func_184201_a(EntityEquipmentSlot.OFFHAND,
				GuiItemSelect.inventoryItem.func_70301_a(ContainerItemSelect.slotFromType.get(EntityEquipmentSlot.OFFHAND)));
		targetEntity.renderEntity.func_184201_a(EntityEquipmentSlot.FEET,
				GuiItemSelect.inventoryItem.func_70301_a(ContainerItemSelect.slotFromType.get(EntityEquipmentSlot.FEET)));
		targetEntity.renderEntity.func_184201_a(EntityEquipmentSlot.LEGS,
				GuiItemSelect.inventoryItem.func_70301_a(ContainerItemSelect.slotFromType.get(EntityEquipmentSlot.LEGS)));
		targetEntity.renderEntity.func_184201_a(EntityEquipmentSlot.CHEST,
				GuiItemSelect.inventoryItem.func_70301_a(ContainerItemSelect.slotFromType.get(EntityEquipmentSlot.CHEST)));
		targetEntity.renderEntity.func_184201_a(EntityEquipmentSlot.HEAD,
				GuiItemSelect.inventoryItem.func_70301_a(ContainerItemSelect.slotFromType.get(EntityEquipmentSlot.HEAD)));

//		targetEntity.renderEntity.setCurrentItemOrArmor(0, GuiItemSelect.inventoryItem.getStackInSlot(4));
//		targetEntity.renderEntity.setCurrentItemOrArmor(1, GuiItemSelect.inventoryItem.getStackInSlot(3));
//		targetEntity.renderEntity.setCurrentItemOrArmor(2, GuiItemSelect.inventoryItem.getStackInSlot(2));
//		targetEntity.renderEntity.setCurrentItemOrArmor(3, GuiItemSelect.inventoryItem.getStackInSlot(1));
//		targetEntity.renderEntity.setCurrentItemOrArmor(4, GuiItemSelect.inventoryItem.getStackInSlot(0));
	}

	/**
	 * 現在のアイテムをGUIへ移す。
	 */
	public void getItems() {
		GuiItemSelect.inventoryItem.func_70299_a(ContainerItemSelect.slotFromType.get(EntityEquipmentSlot.FEET),
				targetEntity.renderEntity.func_184582_a(EntityEquipmentSlot.FEET));
		GuiItemSelect.inventoryItem.func_70299_a(ContainerItemSelect.slotFromType.get(EntityEquipmentSlot.LEGS)
				, targetEntity.renderEntity.func_184582_a(EntityEquipmentSlot.LEGS));
		GuiItemSelect.inventoryItem.func_70299_a(ContainerItemSelect.slotFromType.get(EntityEquipmentSlot.CHEST),
				targetEntity.renderEntity.func_184582_a(EntityEquipmentSlot.CHEST));
		GuiItemSelect.inventoryItem.func_70299_a(ContainerItemSelect.slotFromType.get(EntityEquipmentSlot.HEAD)
				, targetEntity.renderEntity.func_184582_a(EntityEquipmentSlot.HEAD));
		GuiItemSelect.inventoryItem.func_70299_a(ContainerItemSelect.slotFromType.get(EntityEquipmentSlot.MAINHAND),
				targetEntity.renderEntity.func_184582_a(EntityEquipmentSlot.MAINHAND));
		GuiItemSelect.inventoryItem.func_70299_a(ContainerItemSelect.slotFromType.get(EntityEquipmentSlot.OFFHAND),
				targetEntity.renderEntity.func_184582_a(EntityEquipmentSlot.OFFHAND));

//		GuiItemSelect.inventoryItem.setInventorySlotContents(0, targetEntity.renderEntity.getCurrentArmor(3));
//		GuiItemSelect.inventoryItem.setInventorySlotContents(1, targetEntity.renderEntity.getCurrentArmor(2));
//		GuiItemSelect.inventoryItem.setInventorySlotContents(2, targetEntity.renderEntity.getCurrentArmor(1));
//		GuiItemSelect.inventoryItem.setInventorySlotContents(3, targetEntity.renderEntity.getCurrentArmor(0));
//		GuiItemSelect.inventoryItem.setInventorySlotContents(4, targetEntity.renderEntity.getHeldItem());
	}

	public static void afterRender(EntityDool entityfigure) {
		// キャラクターをレンダリングした後の特殊処理
		// staticなので継承するわけではないから自分で作る。
	
	}

}
