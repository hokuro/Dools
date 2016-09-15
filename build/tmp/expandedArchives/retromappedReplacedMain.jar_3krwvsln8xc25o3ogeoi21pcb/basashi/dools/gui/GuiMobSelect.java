package basashi.dools.gui;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import basashi.dools.core.log.ModLog;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiSlot;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;

public abstract class GuiMobSelect extends GuiScreen {

	public Map<String, Entity> entityMap;
	public static Map<Class, String> entityMapClass = new HashMap<Class, String>();
	public static List<String> exclusionList = new ArrayList<String>();

	protected String screenTitle;
	protected GuiSlot selectPanel;

	public FontRenderer fontRenderObj(){
		return this.field_146289_q;
	}



	public GuiMobSelect(World pWorld) {
		entityMap = new TreeMap<String, Entity>();
		initEntitys(pWorld, true);
	}

	public GuiMobSelect(World pWorld, Map<String, Entity> pMap) {
		entityMap = pMap;
		initEntitys(pWorld, false);
	}


	public void initEntitys(World world, boolean pForce) {
		// 表示用EntityListの初期化
		if (entityMapClass.isEmpty()) {
			try {
				Map lmap = EntityList.field_75626_c;
				entityMapClass.putAll(lmap);
			}
			catch (Exception e) {
				ModLog.log().debug("EntityClassMap copy failed.");
			}
		}

		if (entityMap == null) return;
		if (!pForce && !entityMap.isEmpty()) return;

		for (Map.Entry<Class, String> le : entityMapClass.entrySet()) {
			if (Modifier.isAbstract(le.getKey().getModifiers())) continue;
			int li = 0;
			Entity lentity = null;
			try {
				// 表示用のEntityを作る
				do {
					lentity = (EntityLivingBase)le.getKey().getConstructor(World.class).newInstance(world);
				} while (lentity != null && checkEntity(le.getValue(), lentity, li++));
			} catch (Exception e) {
				ModLog.log().debug("Entity [" + le.getValue() + "] can't created.");
			}
		}
	}

	/**
	 * 渡されたEntityのチェック及び加工。
	 * trueを返すと同じクラスのエンティティを再度渡してくる、そのときpIndexはカウントアップされる
	 */
	protected boolean checkEntity(String pName, Entity pEntity, int pIndex) {
		entityMap.put(pName, pEntity);
		return false;
	}

	@Override
	public void func_73866_w_() {
		selectPanel = new GuiSlotMobSelect(field_146297_k, this);
		selectPanel.func_148134_d(3, 4);
	}

	@Override
	public void func_73863_a(int px, int py, float pf) {
//		float lhealthScale = BossStatus.healthScale;
//		int lstatusBarLength = BossStatus.statusBarTime;
//		String lbossName = BossStatus.bossName;
//		boolean lfield_82825_d = BossStatus.hasColorModifier;

		func_146276_q_();
		selectPanel.func_148128_a(px, py, pf);
		func_73732_a(field_146289_q, I18n.func_74838_a(screenTitle), field_146294_l / 2, 20, 0xffffff);
		super.func_73863_a(px, py, pf);

		// GUIで表示した分のボスのステータスを表示しない
//		BossStatus.healthScale = lhealthScale;
//		BossStatus.statusBarTime = lstatusBarLength;
//		BossStatus.bossName = lbossName;
//		BossStatus.hasColorModifier = lfield_82825_d;
	}

	/**
	 *  スロットがクリックされた
	 */
	public abstract void clickSlot(int pIndex, boolean pDoubleClick, String pName, EntityLivingBase pEntity);

	/**
	 *  スロットの描画
	 */
	public abstract void drawSlot(int pSlotindex, int pX, int pY, int pDrawheight, Tessellator pTessellator, String pName, Entity pEntity);

}
