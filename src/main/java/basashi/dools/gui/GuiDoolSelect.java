package basashi.dools.gui;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import com.mojang.blaze3d.platform.GLX;

import basashi.dools.core.Dools;
import basashi.dools.entity.EntityDool;
import basashi.dools.gui.GuiDoolSelect.GuiSlotMobSelect.MobSlotEntly;
import basashi.dools.network.MessageHandler;
import basashi.dools.server.ServerDool;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.gui.widget.list.ExtendedList;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class GuiDoolSelect extends Screen {

	protected EntityDool targetFigure;
	protected GuiDoolSelect.GuiSlotMobSelect list;
	protected static List<String> exclusionList = new ArrayList<String>();

	public GuiDoolSelect(EntityDool entityIn, ITextComponent titleIn) {
		super(titleIn);
		targetFigure = entityIn;
	}

	@Override
	protected void init() {
		this.minecraft.keyboardListener.enableRepeatEvents(true);
		this.list = new GuiDoolSelect.GuiSlotMobSelect(this);
		this.children.add(this.list);
		this.addButton(new Button(width / 2 - 60, height - 44, 120, 20, "Select",(bt)->{actionPerformed(300, bt);}));
	}

	protected void actionPerformed(int id, Button bt) {
		if (!bt.active) {
			return;
		}
		if (id == 300) {
			MobSlotEntly select = (MobSlotEntly)list.getSelected();
			targetFigure.setRenderEntity(select.getMobLiving());
			onClose();
			return;
		}
	}

	@Override
	public void onClose() {
		super.onClose();
		ServerDool isf = Dools.getServerFigure(targetFigure);
		String name="";
		if (isf != null) {
			isf.setRotation(targetFigure);
			name=targetFigure.mobString;
		}
		// i設定されたEntityに適合するパケットセンダーを実行
		MessageHandler.Send_MessageServer_SpawnDool(targetFigure.posX,targetFigure.posY,targetFigure.posZ,targetFigure.rotationYaw,name);
	}

	@Override
	public boolean mouseScrolled(double p_mouseScrolled_1_, double p_mouseScrolled_3_, double p_mouseScrolled_5_) {
		return this.list.mouseScrolled(p_mouseScrolled_1_, p_mouseScrolled_3_, p_mouseScrolled_5_);
	}

	@Override
	public void resize(Minecraft p_resize_1_, int p_resize_2_, int p_resize_3_) {
		this.init(p_resize_1_, p_resize_2_, p_resize_3_);
	}

	@Override
	public void removed() {
		this.minecraft.keyboardListener.enableRepeatEvents(false);
	}

	@Override
	public void render(int px, int py, float pf) {
		this.renderBackground();
		list.render(px, py, pf);
		drawCenteredString(this.font, I18n.format("Figure Select"), width / 2, 20, 0xffffff);
		super.render(px, py, pf);
	}

	public void drawSlot(int pSlotindex, int pX, int pY, Tessellator pTessellator, String pName, Entity pEntity) {
		drawString(this.font, pName, (width - font.getStringWidth(pName)) / 2, pY + 10, 0xffffff);
	}

	@OnlyIn(Dist.CLIENT)
	class GuiSlotMobSelect extends ExtendedList<MobSlotEntly> {
		public GuiDoolSelect ownerGui;
		private GuiSlotMobSelect(GuiDoolSelect pOwner) {
			super(Minecraft.getInstance(), pOwner.width, pOwner.height, 32, pOwner.height - 52, 36);
	  		ownerGui = pOwner;

	  		Registry.ENTITY_TYPE.forEach((et)->{
	  			Entity ent = et.create(Minecraft.getInstance().world);
	  			if (ent instanceof LivingEntity) {
	  				this.addEntry(new MobSlotEntly((LivingEntity)ent, et.getTranslationKey()));
	  				//ModLog.log().info(et.getRegistryName().toString());
	  			}
	  		});
		}

		protected boolean isFocused() {
			return ownerGui.getFocused() == this;
		}

		@Override
		public void setSelected(@Nullable MobSlotEntly p_setSelected_1_) {
			super.setSelected(p_setSelected_1_);
		}

		@Override
		protected void moveSelection(int p_moveSelection_1_) {
			super.moveSelection(p_moveSelection_1_);
		}

		@OnlyIn(Dist.CLIENT)
		class MobSlotEntly extends ExtendedList.AbstractListEntry<MobSlotEntly> {
			private final LivingEntity entityliving;
			private final String translationName;

			public MobSlotEntly(LivingEntity ent, String translationKey) {
				entityliving = ent;
				translationName = I18n.format(translationKey);

	      	}

			public LivingEntity getMobLiving() {
				return entityliving;
			}

	      	@Override
	         public void render(int entryID, int pY, int pX, int mouseXIn, int mouseYIn, int p_render_6_, int p_render_7_, boolean p_render_8_, float p_render_9_) {
	      		try{
	    			// i独自描画
	    			ownerGui.drawSlot(entryID, pX, pY, Tessellator.getInstance(), translationName, entityliving);
	    			entityliving.setWorld(Minecraft.getInstance().world);

	    			// i伽羅の表示
	    			GL11.glEnable(GL11.GL_COLOR_MATERIAL);
	    			GL11.glPushMatrix();
	    			float f1 = 15F;
	    			if (entityliving.getHeight() > 2F) {
	    				f1 = f1 * 3F / entityliving.getHeight();
	    			}
	    			float lxp = ((entryID & 1) == 0) ? (float) pX + 30F : (float) (ownerGui.width - pX) - 30F;
	    			GL11.glTranslatef(lxp, pY + 30F, 50F + f1);
	    			GL11.glScalef(-f1, f1, f1);
	    			GL11.glRotatef(180F, 0.0F, 0.0F, 1.0F);
	    			float f5 = lxp - p_render_6_;
	    			float f6 = (float) ((pY + 30) - 10) - p_render_7_;
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
	    			Minecraft.getInstance().getRenderManager().playerViewY = 180F;
	    			try {
	    				Minecraft.getInstance().getRenderManager().renderEntity(entityliving, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, false);
	    			} catch (Exception e) {
	    				ownerGui.exclusionList.add(entityliving.getName().getFormattedText());
	    			}
	    			// 影だかバイオームだかの処理?
	    			GL11.glPopMatrix();
	    			RenderHelper.disableStandardItemLighting();
	    			GL11.glDisable(GL12.GL_RESCALE_NORMAL);
	    			GLX.glActiveTexture(GLX.GL_TEXTURE1);
	    			GL11.glDisable(GL11.GL_TEXTURE_2D);
	    			GLX.glActiveTexture(GLX.GL_TEXTURE0);
	    		}
	    		catch(Exception eex){
	    		}
	      	}

	         @Override
	         public boolean mouseClicked(double var1, double var2, int var3) {
	            if (var3 == 0) {
	            	GuiSlotMobSelect.this.setSelected(this);
	               return true;
	            } else {
	               return false;
	            }
	         }
		}
	}
}
