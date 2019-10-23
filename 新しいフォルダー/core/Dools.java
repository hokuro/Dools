package basashi.dools.core;

import java.util.HashMap;
import java.util.Map;

import basashi.dools.config.ConfigValue;
import basashi.dools.container.ContainerGuiSelect;
import basashi.dools.container.ContainerItemSelect;
import basashi.dools.container.ContainerPause;
import basashi.dools.core.log.ModLog;
import basashi.dools.creative.CreativeTabDools;
import basashi.dools.entity.EntityDool;
import basashi.dools.entity.EntityDoolPlayer;
import basashi.dools.entity.render.EntityDoolRender;
import basashi.dools.entity.render.EntityPlayerRender;
import basashi.dools.gui.GuiDoolSelect;
import basashi.dools.gui.GuiItemSelect;
import basashi.dools.item.ItemDool;
import basashi.dools.network.MessageHandler;
import basashi.dools.server.ServerDool;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.gui.ScreenManager.IScreenFactory;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ObjectHolder;

@Mod( ModCommon.MOD_ID)
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
        FMLJavaModLoadingContext.get().getModEventBus().addGenericListener(ContainerType.class, this::onContainerRegistry);

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


	@ObjectHolder(ModCommon.MOD_ID + ":" + ModCommon.CONTAINER_GUIMOBSELECT)
	public static ContainerType<ContainerGuiSelect> CONTAINER_GUISELECT;
	@ObjectHolder(ModCommon.MOD_ID + ":" + ModCommon.CONTAINER_GUIPAUSE)
	public static ContainerType<ContainerPause> CONTAINER_GUIPAUSE;
	@ObjectHolder(ModCommon.MOD_ID + ":" + ModCommon.CONTAINER_GUIITEMSELECT)
	public static ContainerType<ContainerItemSelect> CONTAINER_ITEMSELECT;

	public static DoolScreenFactory fc = new DoolScreenFactory();

	@OnlyIn(Dist.CLIENT)
	public void guiHandler(){
		ScreenManager.registerFactory(CONTAINER_GUISELECT, fc);
		ScreenManager.registerFactory(CONTAINER_GUIPAUSE, fc);
		ScreenManager.registerFactory(CONTAINER_ITEMSELECT, GuiItemSelect::new);
	}

	public void onContainerRegistry(final RegistryEvent.Register<ContainerType<?>> event) {
		event.getRegistry().register(IForgeContainerType.create((wid, playerInv, extraData)->{
			int id = extraData.readInt();
			Entity ent = playerInv.player.world.getEntityByID(id);
			if (ent instanceof EntityDool) {
				return new ContainerPause(Dools.CONTAINER_GUIPAUSE, wid, (EntityDool)ent);
			}
			return null;
		}).setRegistryName(ModCommon.MOD_ID + ":" + ModCommon.CONTAINER_GUIPAUSE));


		event.getRegistry().register(IForgeContainerType.create((wid, playerInv, extraData)->{
			return new ContainerGuiSelect(wid,playerInv);
		}).setRegistryName(ModCommon.MOD_ID + ":" + ModCommon.CONTAINER_GUIMOBSELECT));


		event.getRegistry().register(IForgeContainerType.create((wid, playerInv, extraData)->{
			int id = extraData.readInt();
			Entity ent = playerInv.player.world.getEntityByID(id);
			if (ent instanceof EntityDool) {
				return new ContainerItemSelect(Dools.CONTAINER_ITEMSELECT, wid, playerInv.player, ((EntityDool)ent).renderEntity);
			}
			return null;
		}).setRegistryName(ModCommon.MOD_ID + ":" + ModCommon.CONTAINER_GUIITEMSELECT));
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
        	DOOL = EntityType.Builder.<EntityDool>create(EntityDool::new, EntityClassification.MISC)
        			.setTrackingRange(80).setUpdateInterval(5).setShouldReceiveVelocityUpdates(true).size(1,1)
        			.setCustomClientFactory(EntityDool::new).build(ModCommon.MOD_ID + ":" + NAME_REGISTER_DOOL);
        	DOOL.setRegistryName(new ResourceLocation(ModCommon.MOD_ID,NAME_REGISTER_DOOL));
        	etRegistryEvent.getRegistry().register(DOOL);

        	//if ( ConfigValue.general.IsFigurePlayer()){
        	DOOLPLAYER = EntityType.Builder.<EntityDoolPlayer>create(EntityDoolPlayer::new, EntityClassification.MISC)
        			.setTrackingRange(80).setUpdateInterval(5).setShouldReceiveVelocityUpdates(true).size(1,1)
        			.setCustomClientFactory(EntityDoolPlayer::new).build(ModCommon.MOD_ID + ":" + NAME_REGISTER_DOOLPLAYER);
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
		Registry.ENTITY_TYPE.forEach((etype)->{
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

	public static class DoolScreenFactory  implements IScreenFactory {
		@Override
		public Screen create(Container container, PlayerInventory playerInv, ITextComponent titl) {
			ContainerType cntType = container.getType();

    		if (cntType.getRegistryName().toString().equals(Dools.CONTAINER_GUISELECT.getRegistryName().toString())) {
    			if ((ItemDool.entDool != null)){
    				return new GuiDoolSelect(playerInv.player.world,ItemDool.entDool, (ContainerGuiSelect)container, playerInv, titl);
    			}
    		}else if (cntType.getRegistryName().toString().equals(Dools.CONTAINER_GUIPAUSE.getRegistryName().toString())){
    			if ((Dools.guiDool  != null)){
    				return Dools.proxy.getGui(Dools.guiDool, titl, (ContainerPause)container, playerInv);
    			}
    		}
    		return null;
		}
	}

}
