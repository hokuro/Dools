package basashi.dools.core;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import basashi.dools.container.ContainerGuiSelect;
import basashi.dools.container.ContainerItemSelect;
import basashi.dools.container.ContainerPause;
import basashi.dools.entity.EntityDool;
import basashi.dools.entity.EntityDoolPlayer;
import basashi.dools.gui.GuiDoolPause;
import basashi.dools.network.MessageHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.DownloadImageBuffer;
import net.minecraft.client.renderer.texture.DownloadingTexture;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.inventory.container.SimpleNamedContainerProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StringUtils;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

public class CommonUtil {

	public GuiDoolPause getGui(EntityDool pEntity, ITextComponent titleIn, ContainerPause container, PlayerInventory playerInv) {
		if (Dools.guiClassMap.size() == 0) {
			Dools.initGuiMap();
		}
 		Class<GuiDoolPause> cl = Dools.guiClassMap.get(new ResourceLocation(pEntity.mobString).getPath());
		GuiDoolPause g = null;
		pEntity.afterrender = null;
		if (cl != null) {
			try {
				Constructor<GuiDoolPause> cn = cl.getConstructor(new Class[] { ContainerPause.class, PlayerInventory.class, ITextComponent.class, EntityDool.class });
				g = cn.newInstance(new Object[] {container, playerInv, titleIn, pEntity });

				pEntity.afterrender = g.getClass().getMethod("afterRender", new Class[] { EntityDool.class });
			} catch (Exception exception) {
				System.out.println("can't constract Gui.");
			}
		}
		if (g == null) {
			g = new GuiDoolPause(container, playerInv, titleIn, pEntity);
		}

		return g;
	}


	/**
	 * サーバーにフィギュア固有データを要求する。
	 */
	public void getDoolData(Entity pEntity) {
		// サーバーへ姿勢データの要求をする
		MessageHandler.Send_MessageDoolUpdte(pEntity.getEntityId());
		//Dools.INSTANCE.sendToServer(new MessageDoolUpdate(pEntity.getEntityId()));
	}

	public void openGuiSelect(PlayerEntity pEntity, World pWorld) {
        if (!pWorld.isRemote) {
        	NetworkHooks.openGui((ServerPlayerEntity)pEntity,
					new SimpleNamedContainerProvider((id,playerInv,player)-> {
						return new ContainerGuiSelect(id, pEntity.inventory);
					}, new StringTextComponent(ModCommon.MOD_ID + ":" + ModCommon.CONTAINER_GUIMOBSELECT)),
        			(buf)->{
						buf.writeDouble(pEntity.posX);
						buf.writeDouble(pEntity.posY);
						buf.writeDouble(pEntity.posZ);
					});
        }
		// Guiを表示してフィギュアを選択
		//pEntity.openGui(Dools.instance, 0, pWorld, pEntity.getPosition().getX(), pEntity.getPosition().getY(), pEntity.getPosition().getZ());
	}

	public void openGuiPause(PlayerEntity pPlayer, EntityDool pFigure, World pWorld) {
		Dools.guiDool = pFigure;
        if (!pWorld.isRemote)
        {
        	NetworkHooks.openGui((ServerPlayerEntity)pPlayer,
						new GuiPauseContainerProvider(ModCommon.MOD_ID + ":" + ModCommon.CONTAINER_GUIPAUSE, pFigure),
        			(buf)->{
        				buf.writeInt(pFigure.getEntityId());
					});
        }
	}

	public void openGuiItemSelect(PlayerEntity pPlayer, LivingEntity pFigure, World pWorld) {
        if (!pWorld.isRemote)
        {
        	NetworkHooks.openGui((ServerPlayerEntity)pPlayer,
						new GuiItemSelectContainerProvider(ModCommon.MOD_ID + ":" + ModCommon.CONTAINER_GUIPAUSE, pFigure),
        			(buf)->{
        				buf.writeInt(pFigure.getEntityId());
					});
        }
	}

	// プレーヤーモデルフィギュアのテクスチャ関連
	public String getDownloadSkinUrl(String par0Str) {
		return String.format(
				"http://skins.minecraft.net/MinecraftSkins/%s.png",
				new Object[] { StringUtils.stripControlCodes(par0Str) });
	}



	public static class GuiPauseContainerProvider implements INamedContainerProvider{
		private final ITextComponent name;
		private EntityDool entityDool;

		public GuiPauseContainerProvider(String title, EntityDool entity) {
			name = new StringTextComponent(title);
			entityDool = entity;
		}

		@Override
		public ITextComponent getDisplayName() {
			return this.name;
		}

		public Container createMenu(int id, PlayerInventory playerInv, PlayerEntity paleyr) {
			return new ContainerPause(Dools.CONTAINER_GUIPAUSE, id, entityDool);
		}
	}

	public static class GuiItemSelectContainerProvider implements INamedContainerProvider{
		private final ITextComponent name;
		private LivingEntity target;

		public GuiItemSelectContainerProvider(String title, LivingEntity figure) {
			name = new StringTextComponent(title);
			target = figure;
		}

		@Override
		public ITextComponent getDisplayName() {
			return this.name;
		}

		public Container createMenu(int id, PlayerInventory playerInv, PlayerEntity paleyr) {
			return new ContainerItemSelect(Dools.CONTAINER_GUIPAUSE, id, paleyr, target);
		}
	}
}
