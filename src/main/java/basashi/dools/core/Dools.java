package basashi.dools.core;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityList;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
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
import net.minecraftforge.fml.common.registry.ForgeRegistries;
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
		ForgeRegistries.ITEMS.register(dool);
		if(event.getSide().isClient()){
			// モデル登録
			ModelLoader.setCustomModelResourceLocation(dool, 0, new ModelResourceLocation(ModCommon.MOD_ID + ":" + ItemDool.NAME, "inventory"));
			proxy.setZoomRate();
		}



		MessageHandler.init();
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		//INSTANCE.register(this)

		// レシピ追加
		GameRegistry.addShapelessRecipe(
				new ResourceLocation(ModCommon.MOD_ID+":"+ItemDool.NAME),
				new ResourceLocation(ModCommon.MOD_ID+":"+ItemDool.NAME),
				new ItemStack(dool,1,0),
				Ingredient.fromItem(Items.STICK), Ingredient.fromItem(Items.CLAY_BALL));
		GameRegistry.addShapelessRecipe(
				new ResourceLocation(ModCommon.MOD_ID+":"+ItemDool.NAME+"2"),
				new ResourceLocation(ModCommon.MOD_ID+":"+ItemDool.NAME),
				new ItemStack(Items.CLAY_BALL),
				Ingredient.fromItem(dool));


		// エンティティ登録
		EntityRegistry.registerModEntity(new ResourceLocation(ModCommon.MOD_ID+":"+EntityDool.NAEM), EntityDool.class, EntityDool.NAEM, 0, this.instance, 80, 3, true);
		if ( ConfigValue.General.isFigurePlayer){
			EntityRegistry.registerModEntity(new ResourceLocation(ModCommon.MOD_ID+":"+EntityDoolPlayer.NAME), EntityDoolPlayer.class, EntityDoolPlayer.NAME, ConfigValue.General.UniqueEntityIdFigurePlayer, this.instance, 64, 10, false);
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
			for (ResourceLocation res : EntityList.getEntityNameList()){
				addGui(res.getResourcePath(),event.getSide().isClient());
			}
			defServerFigure = new ServerDool();
			NonNullList<ItemStack> s = NonNullList.create();
			dool.getSubItems(tabsDool, s);
		}catch(Exception exception){
			exception.printStackTrace();
		}
	}


	public void addGui(String pName, boolean isClient) {
		if (pName != null && pName.length() > 0) {
			ClassLoader classloader1 = Dools.class.getClassLoader();
			String lcs1, lcs2;
			Class lclass1 = null, lclass2 = null;
			ServerDool lserver = null;
			String ppName = pName;

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

	public void registerModel(ItemStack stack){
		if (Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getItemModel(stack) ==
				Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getModelManager().getMissingModel()){
			Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(Dools.dool, stack.getMetadata(), new ModelResourceLocation(ModCommon.MOD_ID + ":" + ItemDool.NAME, "inventory"));
		}
	}

	/**
	 * 独自通信用処理を獲得する。
	 */
	public static ServerDool getServerFigure(EntityDool pEntity) {
		if (pEntity.renderEntity == null) {
			return null;
		}
		ResourceLocation ls = new ResourceLocation(pEntity.mobString);
		if (serverMap.containsKey(ls.getResourcePath())) {
			return serverMap.get(ls.getResourcePath());
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
