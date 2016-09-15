package basashi.dools.gui;


import basashi.dools.entity.EntityDool;
import net.minecraft.entity.boss.EntityDragon;

public class GuiDoolPause_EnderDragon extends GuiDoolPause {

	public EntityDragon edragon;

	public GuiDoolPause_EnderDragon(EntityDool entityfigua) {
		super(entityfigua);
		edragon = (EntityDragon) targetEntity.renderEntity;
	}

	private float func_40159_b(double d) {
		for (; d >= 180D; d -= 360D) {
		}
		for (; d < -180D; d += 360D) {
		}
		return (float) d;
	}

	public float getGuiRenderYawOffset() {
		return 180F;
	}

	public static void afterRender(EntityDool entityfigure) {
		// キャラクターをレンダリングした後の特殊処理
//		RenderDragon.entityDragon = null;
		// TODO:ステータスバーの削除をするにはBossStatusの値を書き戻してやる。
	}

}
