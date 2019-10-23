package basashi.dools.render;

import java.lang.reflect.InvocationTargetException;

import org.lwjgl.opengl.GL11;

import basashi.dools.entity.EntityDool;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;

public class EntityDoolRender extends EntityRenderer<EntityDool> {

	public EntityDoolRender(EntityRendererManager manager) {
		super(manager);
		shadowSize = 0.0F;
	}

	@Override
	public void doRender(EntityDool entity, double x, double y, double z, float yaw, float tic) {
		EntityDool ef = entity;
		if (ef.renderEntity != null) {
			GL11.glPushMatrix();
			GL11.glTranslatef(
					(float) x,
					(float) y + ef.fyOffset + ((ef.renderEntity.isPassenger()
							? (float) ef.renderEntity.getYOffset()
									: (float)0.0) / ef.zoom),
					(float) z);
			float fz = 1F / ef.zoom;
			GL11.glScalef(fz, fz, fz);
			GL11.glRotatef(ef.rotationYaw + ef.additionalYaw, 0F, 1F, 0F);
			renderManager.renderEntity(ef.renderEntity, 0, 0, 0, ef.additionalYaw, tic,false);
			GL11.glPopMatrix();
			callAfterRender(ef);
		}
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityDool entity) {
		// TODO 自動生成されたメソッド・スタブ
		return null;
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
}
