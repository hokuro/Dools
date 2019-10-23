package basashi.dools.core.proxy;

import java.lang.reflect.Constructor;

import basashi.dools.core.Dools;
import basashi.dools.entity.EntityDool;
import basashi.dools.gui.GuiDoolPause;
import net.minecraft.client.renderer.texture.DownloadingTexture;
import net.minecraft.util.ResourceLocation;

public class CommonProxy {
	public DownloadingTexture getDownloadImageSkin(ResourceLocation par0ResourceLocation, String par1Str, int kind) {
		return null;
	}

	public ResourceLocation getSkinUrl(String par0Str) {
		return null;
	}

	public void openGuiPause(EntityDool pFigure) {

	}

	public GuiDoolPause getGui(EntityDool pEntity) {
 		Class<GuiDoolPause> cl = Dools.guiClassMap.get(new ResourceLocation(pEntity.mobString).getPath());
		GuiDoolPause g = null;
		//pEntity.afterrender = null;
		if (cl != null) {
			try {
				Constructor<GuiDoolPause> cn = cl.getConstructor(new Class[] { EntityDool.class});
				g = cn.newInstance(new Object[] {pEntity });

				//pEntity.afterrender = g.getClass().getMethod("afterRender", new Class[] { EntityDool.class });
			} catch (Exception exception) {
				System.out.println("can't constract Gui.");
			}
		}
		if (g == null) {
			g = new GuiDoolPause(pEntity);
		}

		return g;
	}
}
