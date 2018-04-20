package basashi.dools.gui;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import basashi.dools.core.log.ModLog;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiSlot;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;

public abstract class GuiMobSelect extends GuiScreen {

	public Map<String, Entity> entityMap;
	public static List<String> orderlist = new ArrayList<String>();
	public static Map<Class, String> entityMapClass = new HashMap<Class, String>();
	public static List<String> exclusionList = new ArrayList<String>();

	protected String screenTitle;
	protected GuiSlot selectPanel;

	public FontRenderer fontRenderObj(){
		return this.fontRenderer;
	}



	public GuiMobSelect(World pWorld) {
		entityMap = new HashMap<String,Entity>();// new TreeMap<String, Entity>();
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
				for (ResourceLocation res:  EntityList.getEntityNameList()){
					if (EntityList.getClass(res)!=null){
						entityMapClass.put(EntityList.getClass(res),res.toString());
					}
				}
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
				lentity = (Entity)le.getKey().getConstructor(World.class).newInstance(world);
				if (!(lentity instanceof EntityLivingBase)){
					lentity = null;
				}else{
					entityMap.put(le.getValue(), lentity);
					Order(le.getValue());
				}
			} catch (Exception e) {
				ModLog.log().debug("Entity [" + le.getValue() + "] can't created." + e.toString());
			}
		}
	}

	private void Order(String var){
		if (orderlist.contains(var)){return;}
		
		String next = (new ResourceLocation(var)).getResourcePath();
		int insert = 0;
		boolean bk = false;
		for (int i = 0; i < orderlist.size();i++){
			insert = i;
			if ((new ResourceLocation(orderlist.get(i))).getResourcePath().compareTo(next) >=0 ){
				bk = true;
				break;
			}
		}
		ModLog.log().debug("insert:" + insert);
		if (bk){
			try{
				orderlist.add(insert, var);
			}catch(Exception ex){
				ModLog.log().debug("insert:" + insert);
			}
		}else{
			orderlist.add(var);
		}
	}


	@Override
	public void initGui() {
		selectPanel = new GuiSlotMobSelect(mc, this);
		selectPanel.registerScrollButtons(3, 4);
	}

	@Override
	public void drawScreen(int px, int py, float pf) {
//		float lhealthScale = BossStatus.healthScale;
//		int lstatusBarLength = BossStatus.statusBarTime;
//		String lbossName = BossStatus.bossName;
//		boolean lfield_82825_d = BossStatus.hasColorModifier;

		drawDefaultBackground();
		selectPanel.drawScreen(px, py, pf);
		drawCenteredString(fontRenderObj(), I18n.translateToLocal(screenTitle), width / 2, 20, 0xffffff);
		super.drawScreen(px, py, pf);

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
