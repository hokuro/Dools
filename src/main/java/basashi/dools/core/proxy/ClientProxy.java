package basashi.dools.core.proxy;

import basashi.dools.core.log.ModLog;
import basashi.dools.entity.EntityDool;
import basashi.dools.entity.EntityDoolPlayer;
import basashi.dools.gui.GuiDoolPause;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.DownloadImageBuffer;
import net.minecraft.client.renderer.texture.DownloadingTexture;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StringUtils;

public class ClientProxy extends CommonProxy {

	public ClientProxy() {
		ModLog.log().info("Client");
	}


	@Override
	public DownloadingTexture getDownloadImageSkin(ResourceLocation par0ResourceLocation, String par1Str, int kind) {
		TextureManager var4 = Minecraft.getInstance().getTextureManager();
		Object var5 = var4.getTexture(par0ResourceLocation);

		if (var5 == null) {
			var5 = new DownloadingTexture(null, par1Str, DefaultPlayerSkin.getDefaultSkin(EntityDoolPlayer.UUIDS[kind]), new DownloadImageBuffer());
			var4.loadTexture(par0ResourceLocation, (ITextureObject) var5);
		}

		return (DownloadingTexture) var5;
	}

	@Override
	public ResourceLocation getSkinUrl(String par0Str) {
		return new ResourceLocation("skins/" + StringUtils.stripControlCodes(par0Str));
	}


	@Override
	public void openGuiPause(EntityDool pFigure) {
		GuiDoolPause gui = getGui(pFigure);
		Minecraft.getInstance().displayGuiScreen(gui);
	}
}
