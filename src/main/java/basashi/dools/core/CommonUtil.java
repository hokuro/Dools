package basashi.dools.core;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import basashi.dools.entity.EntityDool;
import basashi.dools.entity.EntityDoolPlayer;
import basashi.dools.gui.GuiDoolPause;
import basashi.dools.inventory.IntaractionObjectGuiPause;
import basashi.dools.inventory.IntaractionObjectGuiSelect;
import basashi.dools.network.MessageHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ImageBufferDownload;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.ThreadDownloadImageData;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StringUtils;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

public class CommonUtil {

	public GuiDoolPause getGui(EntityDool pEntity) {
		if (Dools.guiClassMap.size() == 0) {
			Dools.initGuiMap();
		}
 		Class<GuiDoolPause> cl = Dools.guiClassMap.get(new ResourceLocation(pEntity.mobString).getPath());
		GuiDoolPause g = null;
		pEntity.afterrender = null;
		if (cl != null) {
			try {
				Constructor<GuiDoolPause> cn = cl.getConstructor(new Class[] { EntityDool.class });
				g = cn.newInstance(new Object[] { pEntity });

				pEntity.afterrender = g.getClass().getMethod("afterRender", new Class[] { EntityDool.class });
			} catch (Exception exception) {
				System.out.println("can't constract Gui.");
			}
		}
		if (g == null) {
			g = new GuiDoolPause(pEntity);
		}

		return g;
	}

	public boolean callAfterRender(EntityDool pEntity) {
		if (pEntity.afterrender == null) return false;

		try {
			pEntity.afterrender.invoke(null, new Object[] { pEntity });
			return true;
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * サーバーにフィギュア固有データを要求する。
	 */
	public void getDoolData(Entity pEntity) {
		// サーバーへ姿勢データの要求をする
		MessageHandler.Send_MessageDoolUpdte(pEntity.getEntityId());
		//Dools.INSTANCE.sendToServer(new MessageDoolUpdate(pEntity.getEntityId()));
	}

	public void openGuiSelect(EntityPlayer pEntity, World pWorld) {
        if (!pWorld.isRemote) {
        	NetworkHooks.openGui((EntityPlayerMP)pEntity,
        			new IntaractionObjectGuiSelect(),
        			(buf)->{
						buf.writeDouble(pEntity.posX);
						buf.writeDouble(pEntity.posY);
						buf.writeDouble(pEntity.posZ);
					});
        }
		// Guiを表示してフィギュアを選択
		//pEntity.openGui(Dools.instance, 0, pWorld, pEntity.getPosition().getX(), pEntity.getPosition().getY(), pEntity.getPosition().getZ());
	}

	public void openGuiPause(EntityPlayer pPlayer, EntityDool pFigure,World pWorld) {
		Dools.guiDool = pFigure;
        if (!pWorld.isRemote)
        {
        	NetworkHooks.openGui((EntityPlayerMP)pPlayer,
        			new IntaractionObjectGuiPause(pFigure),
        			(buf)->{
						buf.writeDouble(pPlayer.posX);
						buf.writeDouble(pPlayer.posY);
						buf.writeDouble(pPlayer.posZ);
					});
        }

		//pPlayer.openGui(Dools.instance, 1, pWorld, pPlayer.getPosition().getX(), pPlayer.getPosition().getY(), pPlayer.getPosition().getZ());
	}

	// プレーヤーモデルフィギュアのテクスチャ関連

	public ThreadDownloadImageData getDownloadImageSkin(ResourceLocation par0ResourceLocation, String par1Str, int kind) {
		TextureManager var4 = Minecraft.getInstance().getTextureManager();
		Object var5 = var4.getTexture(par0ResourceLocation);

		if (var5 == null) {
			var5 = new ThreadDownloadImageData(null, par1Str, DefaultPlayerSkin.getDefaultSkin(EntityDoolPlayer.UUIDS[kind]), new ImageBufferDownload());
			var4.loadTexture(par0ResourceLocation, (ITextureObject) var5);
		}

		return (ThreadDownloadImageData) var5;
	}

	public String getDownloadSkinUrl(String par0Str) {
		return String.format(
				"http://skins.minecraft.net/MinecraftSkins/%s.png",
				new Object[] { StringUtils.stripControlCodes(par0Str) });
	}

	public ResourceLocation getSkinUrl(String par0Str) {
		return new ResourceLocation("skins/" + StringUtils.stripControlCodes(par0Str));
	}
//
//
//	public boolean renderItem(Entity pEntity, ItemStack pItemStack, int pIndex) {
//		//特殊レンダーへ
//		if (pItemStack.getItemDamage() == 0) {
//			return false;
//		}
//		GL11.glPushMatrix();
//		if (pEntity != null) {
//			if (pItemStack == ItemDool.firstPerson) {
//				GL11.glTranslatef(-0.5F, 0.0F, 0.25F);
//				GL11.glRotatef(225F, 0F, 1F, 0F);
//			} else {
//				GL11.glTranslatef(-0.5F, 0.0F, 0.5F);
//				GL11.glRotatef(180F, 0F, 1F, 0F);
//			}
//			GL11.glScalef(2.5F, 2.5F, 2.5F);
//		}
//		ItemDool.firstPerson = null;
//
//		ItemDool.entDool.setWorld(Minecraft.getInstance().world);
//		ItemDool.entDool.setPositionAndRotation(0, 0, 0, 0F, 0F);
//		ItemDool.entDool.setRenderEntity(ItemDool.getEntityFromItemStack(pItemStack));
//		ItemDool.entDool.renderEntity.setPositionAndRotation(0, 0, 0, 0F, 0F);
//		ItemDool.entDool.renderEntity.prevRotationYawHead =
//				ItemDool.entDool.renderEntity.rotationYawHead = 0F;
//		//Minecraft.getMinecraft().getRenderManager().renderEntityWithPosYaw(ItemDool.entDool, 0, 0, 0, 0, 0);
//		//Minecraft.getMinecraft().getRenderManager().doRenderEntity(ItemDool.entDool, 0, 0, 0, 0, 0, false);
//		this.callAfterRender(ItemDool.entDool);
//
//		GL11.glPopMatrix();
//		return true;
//	}
}
