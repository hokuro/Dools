package basashi.dools.core;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import basashi.dools.config.ConfigValue;
import basashi.dools.core.log.ModLog;
import basashi.dools.creative.CreativeTabDools;
import basashi.dools.entity.EntityDool;
import basashi.dools.entity.EntityDoolPlayer;
import basashi.dools.event.GuiEventHandler;
import basashi.dools.event.McEventHandler;
import basashi.dools.item.ItemDool;
import basashi.dools.network.MessageHandler;
import basashi.dools.server.ServerDool;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.LanguageMap;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLConstructionEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;

@Mod(modid = ModCommon.MOD_ID, name = ModCommon.MOD_NAME, version = ModCommon.MOD_VERSION)
public class Dools {
	@Mod.Instance(ModCommon.MOD_ID)
	public static Dools instance;
	@SidedProxy(clientSide = ModCommon.MOD_PACKAGE + ModCommon.MOD_CLIENT_SIDE, serverSide = ModCommon.MOD_PACKAGE + ModCommon.MOD_SERVER_SIDE)
	public static CommonProxy proxy;
	public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(ModCommon.MOD_CHANEL);
	public static final McEventHandler mcEvent = new McEventHandler();

	public static Map<String, Class> guiClassMap = new HashMap<String,Class>();
	public static Map<String, ServerDool> serverMap = new HashMap<String, ServerDool>();
	public static ServerDool defServerFigure;

	public static Item dool;
	public static Class classDool;
	public static final CreativeTabs tabsDool = new CreativeTabDools("Dools");
	public static EntityDool guiDool;
	@EventHandler
	public void construct(FMLConstructionEvent event) {
	}

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		//ModCommon.isDebug = true;
		// コンフィグ読み込み
		ConfigValue.init(event);

		// アイテム定義
		dool = new ItemDool();
		GameRegistry.register(dool);
		if(event.getSide().isClient()){
			// モデル登録
			ModelLoader.setCustomModelResourceLocation(dool, 0, new ModelResourceLocation(ModCommon.MOD_ID + ":" + ItemDool.NAME, "inventory"));
			proxy.setZoomRate();
		}

//		Map<Class<Entity>, Integer> ci =
//				(Map<Class<Entity>, Integer> )getPrivateValue(EntityList.class,null,"classToIDMapping");
		List<ResourceLocation> names = new ArrayList<ResourceLocation>();
		List<String> engs = new ArrayList<String>();
		engs.add("item.dool.name=dool");
		for (Class<? extends Entity> cl : EntityList.field_75626_c.keySet()){
			if (EntityLivingBase.class.isAssignableFrom(cl)) {
				engs.add((new StringBuilder("item.").append("dool.").append(EntityList.field_75626_c.get(cl)).append(".name=").append(EntityList.field_75626_c.get(cl))).toString());
				names.add(new ResourceLocation((new StringBuilder()).append(ModCommon.MOD_ID).append(":").append(ItemDool.NAME).append("_").append(EntityList.field_75626_c.get(cl)).toString()));

				if (event.getSide().isClient()){
					ModelLoader.setCustomModelResourceLocation((new ItemStack(dool,1,EntityList.func_180122_a(EntityList.field_75626_c.get(cl)))).func_77973_b(),
							EntityList.func_180122_a(EntityList.field_75626_c.get(cl)), new ModelResourceLocation(ModCommon.MOD_ID + ":" + ItemDool.NAME, "inventory"));
				}
			}
		}

		ResourceLocation[] wresource = new ResourceLocation[names.size()];
		names.toArray(wresource);
		// 名称の追加
		ModelLoader.registerItemVariants(dool, wresource);

		AddLang("en_US",engs);
		//AddLang("ja_JP",engs);
		MessageHandler.init();
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		//INSTANCE.register(this)

		// レシピ追加
		GameRegistry.addShapelessRecipe(new ItemStack(dool,1,0), Items.field_151055_y,Items.field_151119_aD);
		GameRegistry.addShapelessRecipe(new ItemStack(Items.field_151119_aD),dool);


		// エンティティ登録
		EntityRegistry.registerModEntity(EntityDool.class, EntityDool.NAEM, 0, this.instance, 80, 3, true);
		if ( ConfigValue.General.isFigurePlayer){
			EntityRegistry.registerModEntity(EntityDoolPlayer.class, EntityDoolPlayer.NAME, ConfigValue.General.UniqueEntityIdFigurePlayer, this.instance, 64, 10, false);
		}
		try{
			ItemDool.entDool = getEntityMob(null);
		}catch (Exception e){
			ModLog.log().debug("null world");
		}

		NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiEventHandler());
		proxy.registerRender();
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event){
		try{
			Map<String, Class<Entity>> sc = (Map<String,Class<Entity>>)getPrivateValue(EntityList.class,null,"stringToClassMapping");
			Map<Class<Entity>, Integer> ci = (Map<Class<Entity>, Integer> )getPrivateValue(EntityList.class,null,"classToIDMapping");


			Package packege1 = Dools.class.getPackage();
			String strpackege = "";
			if (packege1 != null) {
				strpackege = packege1.getName();
			}
			if (!strpackege.isEmpty()) {
				strpackege += ".";
			}
			if (sc != null && ci != null) {
				int cnt = 0;
				for (Map.Entry<String, Class<Entity>> me : sc.entrySet()) {
					Class<Entity> cl = (Class<Entity>) me.getValue();
					if (EntityLiving.class.isAssignableFrom(cl)
							&& !cl.isAssignableFrom(EntityLiving.class)
							&& !cl.isAssignableFrom(EntityMob.class)) {
						// オートでGUIを追加
						addGui(strpackege, me.getKey(),event.getSide().isClient());
					}
				}

			}
			defServerFigure = new ServerDool();
		}catch(Exception exception){
			exception.printStackTrace();
		}

		GameRegistry.addShapelessRecipe(new ItemStack(Items.field_151119_aD),
				new Object[] { new ItemStack(dool, 1, Short.MAX_VALUE) });
	}


	public void addGui(String pPackegeName, String pName, boolean isClient) {
		if (pName != null && pName.length() > 0) {
			ClassLoader classloader1 = Dools.class.getClassLoader();
			String lcs1, lcs2;
			Class lclass1 = null, lclass2 = null;
			ServerDool lserver = null;
			String ppName = pName;
			if (ppName.contains("dools")){
				ppName = pName.substring("dools.".length());
			}
			if (isClient) {
				try {
					lcs1 = (new StringBuilder()).append("basashi.dools.gui.")
							.append("GuiDoolPause_").append(ppName)
							.toString();
					lclass1 = classloader1.loadClass(lcs1);
				} catch (Exception e) {
				}
			}
			try {
				lcs2 = (new StringBuilder()).append("basashi.dools.server.")
						.append("ServerDool_").append(ppName)
						.toString();
				lclass2 = classloader1.loadClass(lcs2);
				lserver = (ServerDool)lclass2.newInstance();
			} catch (Exception e) {
			}
			if ((!isClient || lclass1 != null) && lserver != null) {
				if (isClient) {
					guiClassMap.put(pName, lclass1);
				}
				serverMap.put(pName, lserver);
				ModLog.log().debug("LoadGUI success:" + pName);
				return;
			}
			ModLog.log().debug("LoadGUI fali:" + pName);
		}
	}

	/**
	 * 独自通信用処理を獲得する。
	 */
	public static ServerDool getServerFigure(EntityDool pEntity) {
		if (pEntity.renderEntity == null) {
			return null;
		}
		String ls = pEntity.mobString;
		if (serverMap.containsKey(ls)) {
			return serverMap.get(ls);
		}
		return defServerFigure;
	}

	/**
	 * EntityFigureのインスタンスを返す
	 */
	public static EntityDool getEntityMob(World pWorld) throws Exception {
		return new EntityDool(pWorld);
	}

	public static <T, E> T getPrivateValue(Class<? super E> classToAccess, E instance, String ... fieldNames)
    {
		return ObfuscationReflectionHelper.getPrivateValue(classToAccess, instance, fieldNames);
    }

	public static <E> void setPrivateValue(Class<? super E> classToAccess, E instance, Object value, String ... fieldNames){
		ObfuscationReflectionHelper.setPrivateValue(classToAccess, instance, value, fieldNames);
	}

	private void AddLang(String lang, List<String> value){
		try {
			PipeOutputStream strm = new PipeOutputStream();
			for (String w : value){

				strm.write((w+"\n").getBytes());
			}
			LanguageMap.inject(strm.getInputStream());
			//LanguageRegistry.instance().injectLanguage(lang, StringTranslate.parseLangFile(strm.getInputStream()));
			strm.close();
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
	}

	public final class PipeOutputStream extends OutputStream {
		/**
		 * データを貯めこむバイト配列ストリーム.
		 */
		private final ByteArrayOutputStream inner = new ByteArrayOutputStream();

		@Override
		public void write(final int b) throws IOException {
			inner.write(b);
		}

		/**
		 * 入力ストリームを生成して返す.
		 * @return 入力ストリーム
		 */
		public InputStream getInputStream() {
			return new ByteArrayInputStream(inner.toByteArray());
		}
	}


























}
