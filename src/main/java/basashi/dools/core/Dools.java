package basashi.dools.core;

import java.util.HashMap;
import java.util.Map;

import basashi.dools.config.ConfigValue;
import basashi.dools.core.log.ModLog;
import basashi.dools.creative.CreativeTabDools;
import basashi.dools.entity.EntityDool;
import basashi.dools.entity.EntityDoolPlayer;
import basashi.dools.entity.render.EntityDoolRender;
import basashi.dools.entity.render.EntityPlayerRender;
import basashi.dools.gui.GuiDoolSelect;
import basashi.dools.inventory.IntaractionObjectGuiPause;
import basashi.dools.inventory.IntaractionObjectGuiSelect;
import basashi.dools.item.ItemDool;
import basashi.dools.network.MessageHandler;
import basashi.dools.server.ServerDool;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.IRegistry;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod( ModCommon.MOD_ID)
@Mod.EventBusSubscriber
public class Dools {
	public static Map<String, Class> guiClassMap = new HashMap<String,Class>();
	public static Map<String, ServerDool> serverMap = new HashMap<String, ServerDool>();
	public static ServerDool defServerFigure;
	public static CommonUtil proxy = new CommonUtil();


	public static Item itemdool;
	public static final ItemGroup tabsDool = new CreativeTabDools("Dools");
	public static EntityDool guiDool;

    public Dools() {
        // Register the doClientStuff method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);

        // コンフィグ読み込み
    	ModLoadingContext.get().
        registerConfig(
        		net.minecraftforge.fml.config.ModConfig.Type.COMMON,
        		ConfigValue.spec);

    	// メッセージ登録
    	MessageHandler.register();

        MinecraftForge.EVENT_BUS.register(this);
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
    	registRender();
    	guiHandler();

    }

	@OnlyIn(Dist.CLIENT)
	public void registRender(){
		RenderingRegistry.registerEntityRenderingHandler(EntityDool.class, EntityDoolRender::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityDoolPlayer.class, EntityPlayerRender::new);
	}

	@OnlyIn(Dist.CLIENT)
	public void guiHandler(){
        ModLoadingContext.get().registerExtensionPoint(ExtensionPoint.GUIFACTORY, () -> (openContainer) -> {
    		ResourceLocation location = openContainer.getId();
    		EntityPlayer player = Minecraft.getInstance().player;
    		World world = Minecraft.getInstance().world;

    		if (location.toString().equals(IntaractionObjectGuiSelect.GUI_ID_SELECT.toString())){
    			if ((ItemDool.entDool != null)){
    				return new GuiDoolSelect(world,ItemDool.entDool);
    			}
    		}else if (location.toString().equals(IntaractionObjectGuiPause.GUI_ID_PAUSE.toString())){
    			if ((Dools.guiDool  != null)){
    				return Dools.proxy.getGui(Dools.guiDool);
    			}
    		}
    		return null;
        });
	}

	@Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {
        @SubscribeEvent
        public static void onItemRegistry(final RegistryEvent.Register<Item> itemRegistryEvent) {
        	itemdool = new ItemDool(new Item.Properties().maxStackSize(16).group(Dools.tabsDool)).setRegistryName(ModCommon.MOD_ID,ItemDool.NAME);
        	itemRegistryEvent.getRegistry().register(itemdool);

    		try{
    			ItemDool.entDool = getEntityMob(null);
    		}catch (Exception e){
    			ModLog.log().debug("null world");
    		}
        }


        public static EntityType<EntityDool> DOOL;
        public static EntityType<EntityDoolPlayer> DOOLPLAYER;
        public static final String NAME_REGISTER_DOOL = "entitydool";
        public static final String NAME_REGISTER_DOOLPLAYER = "entitydoolplayer";

        @SubscribeEvent
        public static void onEntityRegistry(final RegistryEvent.Register<EntityType<?>> etRegistryEvent){
        	DOOL = EntityType.Builder.create(EntityDool.class, EntityDool::new).tracker(80, 5, true).build(ModCommon.MOD_ID + ":" + NAME_REGISTER_DOOL);
        	DOOL.setRegistryName(new ResourceLocation(ModCommon.MOD_ID,NAME_REGISTER_DOOL));
        	etRegistryEvent.getRegistry().register(DOOL);

        	//if ( ConfigValue.general.IsFigurePlayer()){
        		DOOLPLAYER = EntityType.Builder.create(EntityDoolPlayer.class, EntityDoolPlayer::new).tracker(80, 5, true).build(ModCommon.MOD_ID + ":" + NAME_REGISTER_DOOLPLAYER);
        		DOOLPLAYER.setRegistryName(new ResourceLocation(ModCommon.MOD_ID,NAME_REGISTER_DOOLPLAYER));
            	etRegistryEvent.getRegistry().register(DOOLPLAYER);
        	//}
        }

    }


//	public static <T, E> T getPrivateValue(Class<? super E> classToAccess, E instance, String ... fieldNames)
//    {
//		return ObfuscationReflectionHelper.getPrivateValue(classToAccess, instance, fieldNames);
//    }
//
//	public static <E> void setPrivateValue(Class<? super E> classToAccess, E instance, Object value, String ... fieldNames){
//		ObfuscationReflectionHelper.setPrivateValue(classToAccess, instance, value, fieldNames);
//	}


	/**
	 * EntityFigureのインスタンスを返す
	 */
	public static EntityDool getEntityMob(World pWorld) {
		return new EntityDool(pWorld);
	}

	/**
	 * 独自通信用処理を獲得する。
	 */
	public static ServerDool getServerFigure(EntityDool pEntity) {
		if (pEntity.renderEntity == null) {
			return null;
		}
		ResourceLocation ls = new ResourceLocation(pEntity.mobString);
		if (serverMap.containsKey(ls.getPath())) {
			return serverMap.get(ls.getPath());
		}
		if (defServerFigure == null) {
			defServerFigure = new ServerDool();
		}
		return defServerFigure;
	}

	public static void addGui(String pName) {
		if (pName != null && pName.length() > 0) {
			ClassLoader classloader1 = Dools.class.getClassLoader();
			String lcs1, lcs2;
			Class lclass1 = null, lclass2 = null;
			ServerDool lserver = null;
			String ppName = pName;

			try {
				lcs1 = (new StringBuilder()).append("basashi.dools.gui.")
						.append("GuiDoolPause_").append(ppName)
						.toString();
				lclass1 = classloader1.loadClass(lcs1);


				lcs2 = (new StringBuilder()).append("basashi.dools.server.")
						.append("ServerDool_").append(ppName)
						.toString();
				lclass2 = classloader1.loadClass(lcs2);
				lserver = (ServerDool)lclass2.newInstance();
			} catch (Exception e) {
			}
			if ((lclass1 != null) && lserver != null) {
				guiClassMap.put(pName, lclass1);
				serverMap.put(pName, lserver);
				ModLog.log().debug("LoadGUI success:" + pName);
				return;
			}
			ModLog.log().debug("LoadGUI fali:" + pName);
		}
	}

	public static void initGuiMap() {
		IRegistry.field_212629_r.forEach((etype)->{
			String path = etype.getRegistryName().getPath();
			addGui(path);
		});
	}

	public static void registerModel(ItemStack stack){
		if (Minecraft.getInstance().getItemRenderer().getItemModelMesher().getItemModel(stack) ==
			Minecraft.getInstance().getItemRenderer().getItemModelMesher().getModelManager().getMissingModel()){
			Minecraft.getInstance().getItemRenderer().getItemModelMesher().register(stack.getItem(), new ModelResourceLocation(ModCommon.MOD_ID + ":" + ItemDool.NAME, "inventory"));
		}
	}

}
