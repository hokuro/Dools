package basashi.dools.gui;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import com.mojang.blaze3d.platform.GLX;

import basashi.dools.container.ContainerItemSelect;
import basashi.dools.core.Dools;
import basashi.dools.core.log.ModLog;
import basashi.dools.entity.EntityDool;
import basashi.dools.network.MessageHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.fml.client.config.GuiSlider;

public class GuiDoolPause extends Screen {

	protected String screenTitle;
	protected EntityDool targetEntity;
	protected String buttin_ride10[] = { "Dismount", "Ride" };
	protected String button_sneak11[] = { "Stand", "Sneak" };
	public static float button_zoom13[] = { 1, 2, 4, 6 };
	protected String button_child16[] = { "A", "C" };
	protected GuiSlider figureYaw;
	protected String button_child19[] = {"Move","Stop"};
	protected Button offset;

	public GuiDoolPause(EntityDool entityfigure) {
		super(new StringTextComponent(""));
		screenTitle = "Figure Pause";
		targetEntity = entityfigure;
	}

	@Override
	public boolean isPauseScreen() {
		// iフィギュアをいじっていると時を忘れるよね？
		return false;
	}

	@Override
	public boolean keyPressed(int p_keyPressed_1_, int p_keyPressed_2_, int p_keyPressed_3_) {
		if (p_keyPressed_1_ == 256) {
			// iデータをサーバーへ送る
			MessageHandler.Send_MessagePause(Dools.getServerFigure(targetEntity).getData(targetEntity));
			ModLog.log().debug("DataSendToServer.");
			Dools.getServerFigure(targetEntity).sendItems(targetEntity, true, null);
		}
		return super.keyPressed(p_keyPressed_1_, p_keyPressed_2_, p_keyPressed_3_);
	}

	@Override
	public void onClose() {
		super.onClose();
	}

	@Override
	public void removed() {
		if (this.minecraft != null) {
			this.minecraft.keyboardListener.enableRepeatEvents(false);
		}
	}

	@Override
	public void render(int i, int j, float f) {
		super.renderBackground();
		drawCenteredString(this.font, screenTitle, width / 2, 20, 0xffffff);

		// i基準の向きを変更
		targetEntity.additionalYaw = (float)figureYaw.getValue();
		// iキャラ
		int l = width / 2;
		int k = height / 6 + 72;
		LivingEntity elt;
		try{
			elt = (LivingEntity) targetEntity.renderEntity;
		}catch(Exception ex){
			elt = (LivingEntity) targetEntity.renderEntity;
		}
		GL11.glEnable(GL11.GL_COLOR_MATERIAL);
		GL11.glPushMatrix();
		GL11.glTranslatef(l, k + 30F, 50F);
		// float f1 = 45F;
		float f1 = 80F / elt.getHeight() * (elt.isChild()?0.5F:1.0F);
		GL11.glScalef(-f1, f1, f1);
		GL11.glRotatef(180F, 0.0F, 0.0F, 1.0F);
		float f2 = elt.renderYawOffset;
		float f5 = (float) (l + 0) - i;
		float f6 = (float) ((k + 30) - 50) - j;
		GL11.glRotatef(135F, 0.0F, 1.0F, 0.0F);
		RenderHelper.enableStandardItemLighting();
		GL11.glRotatef(targetEntity.additionalYaw - 135F, 0.0F, 1.0F, 0.0F);
		elt.renderYawOffset = 0F;
		elt.rotationYaw = (float) Math.atan(f5 / 40F) * 40F;
		elt.rotationPitch = -(float) Math.atan(f6 / 40F) * 20F;
		elt.rotationYawHead = elt.rotationYaw;
		elt.prevRotationYawHead = elt.rotationYaw;
		Dools.getServerFigure(targetEntity).setRotation(targetEntity);
		GL11.glTranslatef(0.0F, (float)elt.getYOffset(), 0.0F);
		Minecraft.getInstance().getRenderManager().playerViewY = 180F;
		Minecraft.getInstance().getRenderManager().renderEntity(elt, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F,false);
		elt.renderYawOffset = f2;
		elt.prevRotationYaw = elt.rotationYaw + f2;
		elt.prevRotationPitch = elt.rotationPitch;
		elt.rotationYawHead = elt.rotationYaw + f2;
		elt.prevRotationYawHead = elt.rotationYaw + f2;
		Dools.getServerFigure(targetEntity).setRotation(targetEntity);

		// i影だかバイオームだかの処理?
		GL11.glPopMatrix();
		RenderHelper.disableStandardItemLighting();
		GL11.glDisable(GL12.GL_RESCALE_NORMAL);
		GLX.glActiveTexture(GLX.GL_TEXTURE1);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GLX.glActiveTexture(GLX.GL_TEXTURE0);
		super.render(i, j, f);
	}


	// 継承して独自処理を書く必要がある物

	@Override
	public void init() {
		if (targetEntity.renderEntity != null && GuiItemSelect.isChange) {
			setItems();
		}
		GuiItemSelect.isChange = false;

		this.addButton(new Button(width / 2 + 60, height / 6 + 48 + 12, 80, 20, (buttin_ride10[targetEntity.renderEntity.isPassenger() ? 1 : 0]), (bt)->{actionPerformed(10,bt);}));
		this.addButton(new Button(width / 2 + 60, height / 6 + 72 + 12, 80, 20, (button_sneak11[targetEntity.renderEntity.isSneaking() ? 1 : 0]), (bt)->{actionPerformed(11,bt);}));
		this.addButton(new Button(width / 2 + 60, height / 6 + 0 + 12, 60, 20, (String.format("1 / %.0f", targetEntity.zoom)), (bt)->{actionPerformed(13,bt);}));
		this.addButton(new Button(width / 2 + 120, height / 6 + 0 + 12, 20, 20, (button_child16[targetEntity.renderEntity.isChild() ? 1 : 0]), (bt)->{actionPerformed(16,bt);}));
		this.addButton(new Button(width / 2 + 60, height / 6 + 96 + 12, 80, 20, button_child19[targetEntity.isMove?0:1], (bt)->{actionPerformed(19,bt);}));
		this.addButton(new Button(width / 2 - 140, height / 6 + 96 + 12, 80, 20, ("EquipSelect"), (bt)->{actionPerformed(17,bt);}));
		figureYaw = new GuiSlider(width / 2 - 50, height / 6 + 96 + 12, 100, 20, "", "", -360D, 360D, targetEntity.additionalYaw, true, true, (bt)->{});
		//figureYaw = new GuiSlider(15, width / 2 - 50, height / 6 + 96 + 12, "", (targetEntity.additionalYaw + 180F) / 360F, 360F, -180F).setStrFormat("%s%.2f").setDisplayString();

		this.addButton(offset = new Button(width / 2 + 80, height / 6 + 24 + 12, 40, 20, String.format("%.2f", targetEntity.fyOffset), (bt)->{actionPerformed(20,bt);}));
		this.addButton(new Button(width / 2 + 60, height / 6 + 24 + 12, 20, 20, ("+"), (bt)->{actionPerformed(21,bt);}));
		this.addButton(new Button(width / 2 + 120, height / 6 + 24 + 12, 20, 20, ("-"), (bt)->{actionPerformed(22,bt);}));
    	this.addButton(figureYaw);
	}

	public float getGuiRenderYawOffset() {
		return 0.0F;
	}

	/**
	 * GUIの操作をした時の処理
	 */
	protected void actionPerformed(int id, Button button) {
		if (!button.active) {
			return;
		}
		switch(id) {
		case 10:
			if (targetEntity.isFigureRide) {
				// i載ってる
				if (targetEntity.renderEntity.isPassenger()){
					targetEntity.renderEntity.stopRiding();
					button.setMessage(buttin_ride10[0]);
				}
			} else {
				// i載ってない
				if (!targetEntity.renderEntity.isPassenger()){
					targetEntity.renderEntity.startRiding(targetEntity,true);
					button.setMessage(buttin_ride10[1]);
				}
			}
			targetEntity.isFigureRide = targetEntity.renderEntity.isPassenger();
			break;
		case 11:
			if (targetEntity.renderEntity.isSneaking()) {
				// iしゃがみ
				targetEntity.renderEntity.setSneaking(false);
				button.setMessage(button_sneak11[0]);
			} else {
				// i立ち
				targetEntity.renderEntity.setSneaking(true);
				button.setMessage(button_sneak11[1]);
			}
			break;
		case 13:
			// i倍率
			int i = 0;
			float z;
			for (int j = 0; j < button_zoom13.length; j++) {
				z = button_zoom13[j] > 0 ? button_zoom13[j] : 1 / -button_zoom13[j];
				if (targetEntity.zoom == z) {
					if (j + 1 < button_zoom13.length) {
						i = j + 1;
					}
					break;
				}
			}
			z = button_zoom13[i] > 0 ? button_zoom13[i] : 1 / -button_zoom13[i];
			targetEntity.setZoom(z);
			if (z >= 1) {
				button.setMessage(String.format("1 / %.0f", targetEntity.zoom));
			} else {
				button.setMessage(String.format("%.0f / 1", -button_zoom13[i]));
			}
			break;
		case 16:
			// i幼生態判定
			if (targetEntity.renderEntity instanceof AgeableEntity) {
				AgeableEntity lentity = (AgeableEntity)targetEntity.renderEntity;
				lentity.setGrowingAge(lentity.isChild() ? 0 : -1);
				button.setMessage(button_child16[lentity.isChild() ? 1 : 0]);
			} else if (targetEntity.renderEntity instanceof ZombieEntity) {
				ZombieEntity lentity = (ZombieEntity)targetEntity.renderEntity;
				((ZombieEntity)lentity).setChild(!lentity.isChild());
				button.setMessage(button_child16[lentity.isChild() ? 1 : 0]);
			}
			break;
		case 17:
			// i装備品の設定
			GuiItemSelect.clearInventory();
			getItems();
			minecraft.displayGuiScreen(new GuiItemSelect(minecraft.player.inventory, targetEntity.renderEntity));
			break;
		case 19:
			// imove
			GuiItemSelect.clearInventory();
			targetEntity.isMove = !targetEntity.isMove;
			button.setMessage(button_child19[targetEntity.isMove?0:1]);
			break;
		case 20:
			targetEntity.fyOffset = 0;
			button.setMessage(String.format("%.2f", targetEntity.fyOffset));
			break;
		case 21:
			targetEntity.fyOffset += 0.05;
			offset.setMessage(String.format("%.2f", targetEntity.fyOffset));
			break;
		case 22:
			targetEntity.fyOffset -= 0.05;
			offset.setMessage(String.format("%.2f", targetEntity.fyOffset));
			break;

		}
		MessageHandler.Send_MessagePause(Dools.getServerFigure(targetEntity).getData(targetEntity));
	}

	/**
	 * アイテムを設定する
	 */
	public void setItems() {
		targetEntity.renderEntity.setItemStackToSlot(EquipmentSlotType.MAINHAND,
				GuiItemSelect.getItmInventory().getStackInSlot(ContainerItemSelect.slotFromType.get(EquipmentSlotType.MAINHAND)));
		targetEntity.renderEntity.setItemStackToSlot(EquipmentSlotType.OFFHAND,
				GuiItemSelect.getItmInventory().getStackInSlot(ContainerItemSelect.slotFromType.get(EquipmentSlotType.OFFHAND)));
		targetEntity.renderEntity.setItemStackToSlot(EquipmentSlotType.FEET,
				GuiItemSelect.getItmInventory().getStackInSlot(ContainerItemSelect.slotFromType.get(EquipmentSlotType.FEET)));
		targetEntity.renderEntity.setItemStackToSlot(EquipmentSlotType.LEGS,
				GuiItemSelect.getItmInventory().getStackInSlot(ContainerItemSelect.slotFromType.get(EquipmentSlotType.LEGS)));
		targetEntity.renderEntity.setItemStackToSlot(EquipmentSlotType.CHEST,
				GuiItemSelect.getItmInventory().getStackInSlot(ContainerItemSelect.slotFromType.get(EquipmentSlotType.CHEST)));
		targetEntity.renderEntity.setItemStackToSlot(EquipmentSlotType.HEAD,
				GuiItemSelect.getItmInventory().getStackInSlot(ContainerItemSelect.slotFromType.get(EquipmentSlotType.HEAD)));
	}

	/**
	 * 現在のアイテムをGUIへ移す。
	 */
	public void getItems() {
		GuiItemSelect.getItmInventory().setInventorySlotContents(ContainerItemSelect.slotFromType.get(EquipmentSlotType.FEET),
				targetEntity.renderEntity.getItemStackFromSlot(EquipmentSlotType.FEET));
		GuiItemSelect.getItmInventory().setInventorySlotContents(ContainerItemSelect.slotFromType.get(EquipmentSlotType.LEGS)
				, targetEntity.renderEntity.getItemStackFromSlot(EquipmentSlotType.LEGS));
		GuiItemSelect.getItmInventory().setInventorySlotContents(ContainerItemSelect.slotFromType.get(EquipmentSlotType.CHEST),
				targetEntity.renderEntity.getItemStackFromSlot(EquipmentSlotType.CHEST));
		GuiItemSelect.getItmInventory().setInventorySlotContents(ContainerItemSelect.slotFromType.get(EquipmentSlotType.HEAD)
				, targetEntity.renderEntity.getItemStackFromSlot(EquipmentSlotType.HEAD));
		GuiItemSelect.getItmInventory().setInventorySlotContents(ContainerItemSelect.slotFromType.get(EquipmentSlotType.MAINHAND),
				targetEntity.renderEntity.getItemStackFromSlot(EquipmentSlotType.MAINHAND));
		GuiItemSelect.getItmInventory().setInventorySlotContents(ContainerItemSelect.slotFromType.get(EquipmentSlotType.OFFHAND),
				targetEntity.renderEntity.getItemStackFromSlot(EquipmentSlotType.OFFHAND));
	}

	public static void afterRender(EntityDool entityfigure) {
		// iキャラクターをレンダリングした後の特殊処理
		// staticなので継承するわけではないから自分で作る。
	}
}
