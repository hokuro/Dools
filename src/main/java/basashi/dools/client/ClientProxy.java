package basashi.dools.client;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.lwjgl.opengl.GL11;

import basashi.dools.config.ConfigValue;
import basashi.dools.core.CommonProxy;
import basashi.dools.core.Dools;
import basashi.dools.entity.EntityDool;
import basashi.dools.entity.EntityDoolPlayer;
import basashi.dools.entity.render.EntityDoolRender;
import basashi.dools.entity.render.EntityPlayerRender;
import basashi.dools.gui.GuiDoolPause;
import basashi.dools.gui.GuiDoolSelect;
import basashi.dools.item.ItemDool;
import basashi.dools.network.MessageDoolUpdate;
import basashi.dools.render.IItemRenderManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IImageBuffer;
import net.minecraft.client.renderer.ImageBufferDownload;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StringUtils;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

/**
 * Client側専用の処理
 */
public class ClientProxy extends CommonProxy implements IItemRenderManager {

	public static ClientProxy instance = new ClientProxy();

	public void registerRender() {
		// TODO 自動生成されたメソッド・スタブ
		RenderingRegistry.registerEntityRenderingHandler(EntityDool.class,  new IRenderFactory<EntityDool>() {
			@Override
			public Render<? super EntityDool> createRenderFor(RenderManager manager) {
				return new EntityDoolRender(manager);
			}
		});
		RenderingRegistry.registerEntityRenderingHandler(EntityDoolPlayer.class,  new IRenderFactory<EntityDoolPlayer>() {
			@Override
			public Render<? super EntityDoolPlayer> createRenderFor(RenderManager manager) {
				return new EntityPlayerRender(manager);
			}
		});
		RenderManager manager = Minecraft.getMinecraft().getRenderManager();
		net.minecraftforge.fml.client.registry.RenderingRegistry.loadEntityRenderers(manager, manager.entityRenderMap);
	}


	public static void setZoomRate() {
		String s[] = ConfigValue.General.zoomRate.split(",");
		if (s.length > 0) {
			float az[] = new float[s.length];
			for (int i = 0; i < s.length; i++) {
				az[i] = Float.valueOf(s[i].trim());
			}
			GuiDoolPause.button13 = az;
		}
	}

	/**
	 * 独自のGUIを獲得する。
	 */
	public GuiDoolPause getGui(EntityDool pEntity) {
 		Class<GuiDoolPause> cl = Dools.guiClassMap.get(pEntity.mobString);
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

	public void initEntitys() {
		if (ItemDool.entityStringMap.isEmpty()) {
			// リストがブランクなら生成する
			new GuiDoolSelect(Minecraft.getMinecraft().theWorld, null);
		}
	}

	/**
	 * サーバーにフィギュア固有データを要求する。
	 */
	public void getDoolData(Entity pEntity) {
		// サーバーへ姿勢データの要求をする
		Dools.INSTANCE.sendToServer(new MessageDoolUpdate(pEntity.getEntityId()));
	}

	public void openGuiSelect(EntityPlayer pEntity, World pWorld) {
		// Guiを表示してフィギュアを選択
		pEntity.openGui(Dools.instance, 0, pWorld, pEntity.getPosition().getX(), pEntity.getPosition().getY(), pEntity.getPosition().getZ());
	}

	public void openGuiPause(EntityPlayer pPlayer, EntityDool pFigure,World pWorld) {
		Dools.guiDool = pFigure;
		pPlayer.openGui(Dools.instance, 1, pWorld, pPlayer.getPosition().getX(), pPlayer.getPosition().getY(), pPlayer.getPosition().getZ());
	}

	@Override
	public ResourceLocation getRenderTexture(ItemStack pItemStack) {
		return null;
	}

	// プレーヤーモデルフィギュアのテクスチャ関連

	public ThreadDownloadImageData getDownloadImageSkin(ResourceLocation par0ResourceLocation, String par1Str, int kind) {
		return func_110301_a(par0ResourceLocation, func_110300_d(par1Str),
				DefaultPlayerSkin.getDefaultSkin(EntityDoolPlayer.UUIDS[kind]), new ImageBufferDownload());
	}

	private ThreadDownloadImageData func_110301_a(ResourceLocation par0ResourceLocation, String par1Str,ResourceLocation par2ResourceLocation, IImageBuffer par3IImageBuffer) {
		TextureManager var4 = Minecraft.getMinecraft().getTextureManager();
		Object var5 = var4.getTexture(par0ResourceLocation);

		if (var5 == null) {
			var5 = new ThreadDownloadImageData(null, par1Str, par2ResourceLocation,par3IImageBuffer);
			var4.loadTexture(par0ResourceLocation, (ITextureObject) var5);
		}

		return (ThreadDownloadImageData) var5;
	}

	public String func_110300_d(String par0Str) {
		return String.format(
				"http://skins.minecraft.net/MinecraftSkins/%s.png",
				new Object[] { StringUtils.stripControlCodes(par0Str) });
	}

	public ResourceLocation func_110311_f(String par0Str) {
		return new ResourceLocation(
				"skins/" + StringUtils.stripControlCodes(par0Str));
	}

	@Override
	public boolean renderItemWorld(ItemStack pItemStack) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isRenderItemWorld(ItemStack pItemStack) {
		return pItemStack.getItemDamage() != 0;
	}

	@Override
	public boolean isRenderItem(ItemStack pItemStack) {
		// TODO Auto-generated method stub
		return pItemStack.getItemDamage() != 0;
	}

	@Override
	public boolean renderItem(Entity pEntity, ItemStack pItemStack, int pIndex) {
		//特殊レンダーへ
		if (pItemStack.getItemDamage() == 0) {
			return false;
		}
		GL11.glPushMatrix();
		if (pEntity != null) {
			if (pItemStack == ItemDool.firstPerson) {
				GL11.glTranslatef(-0.5F, 0.0F, 0.25F);
				GL11.glRotatef(225F, 0F, 1F, 0F);
			} else {
				GL11.glTranslatef(-0.5F, 0.0F, 0.5F);
				GL11.glRotatef(180F, 0F, 1F, 0F);
			}
			GL11.glScalef(2.5F, 2.5F, 2.5F);
		}
		ItemDool.firstPerson = null;

		ItemDool.entDool.setWorld(Minecraft.getMinecraft().theWorld);
		ItemDool.entDool.setPositionAndRotation(0, 0, 0, 0F, 0F);
		ItemDool.entDool.setRenderEntity(ItemDool.getEntityFromItemStack(pItemStack));
		ItemDool.entDool.renderEntity.setPositionAndRotation(0, 0, 0, 0F, 0F);
		ItemDool.entDool.renderEntity.prevRotationYawHead =
				ItemDool.entDool.renderEntity.rotationYawHead = 0F;
		//Minecraft.getMinecraft().getRenderManager().renderEntityWithPosYaw(ItemDool.entDool, 0, 0, 0, 0, 0);
		//Minecraft.getMinecraft().getRenderManager().doRenderEntity(ItemDool.entDool, 0, 0, 0, 0, 0, false);
		this.callAfterRender(ItemDool.entDool);

		GL11.glPopMatrix();
		return true;
	}

	@Override
	public boolean renderItemInFirstPerson(Entity pEntity, ItemStack pItemStack, float pDeltaTimepRenderPhatialTick) {
		// 元のコード丸パクリ
		ItemDool.firstPerson = pItemStack;
		return false;
	}

	@Override
	public boolean isRenderItemInFirstPerson(ItemStack pItemStack) {
		// TODO 自動生成されたメソッド・スタブ
		return false;
	}

}
