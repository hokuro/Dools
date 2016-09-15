package basashi.dools.entity.render;

import org.lwjgl.opengl.GL11;

import basashi.dools.core.Dools;
import basashi.dools.entity.EntityDool;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public class EntityDoolRender extends Render {

	public EntityDoolRender(RenderManager manager) {
		super(manager);
		field_76989_e = 0.0F;
	}

	@Override
	public void func_76986_a(Entity entity, double x, double y, double z, float yaw, float tic) {
		EntityDool ef = (EntityDool) entity;
		if (ef.renderEntity != null) {
//			float lhealthScale = BossStatus.healthScale;
//			int lstatusBarLength = BossStatus.statusBarTime;
//			String lbossName = BossStatus.bossName;
//			boolean lhasColorModifier = BossStatus.hasColorModifier;

			GL11.glPushMatrix();
			GL11.glTranslatef(
					(float) x,
					(float) y + ef.fyOffset + ((ef.renderEntity.func_184218_aH()
							? (float) ef.renderEntity.func_70033_W()
									: (float)0.0) / ef.zoom),
					//									: (float)ef.renderEntity.getYOffset()) / ef.zoom),
					(float) z);
			float fz = 1F / ef.zoom;
			GL11.glScalef(fz, fz, fz);
			//GL11.glRotatef(ef.rotationYaw + ef.additionalYaw, 0F, 1F, 0F);
			GL11.glRotatef(ef.field_70177_z + ef.additionalYaw, 0F, 1F, 0F);
			//renderManager.renderEntityWithPosYaw(ef.renderEntity, 0, 0, 0, 0, 0);
			field_76990_c.func_188391_a(ef.renderEntity, 0, 0, 0, 0, 0,false);
			GL11.glPopMatrix();
			Dools.proxy.callAfterRender(ef);

//			BossStatus.healthScale = lhealthScale;
//			BossStatus.statusBarTime = lstatusBarLength;
//			BossStatus.bossName = lbossName;
//			BossStatus.hasColorModifier = lhasColorModifier;
		}
	}

	@Override
	protected ResourceLocation func_110775_a(Entity entity) {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

}
