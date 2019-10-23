package basashi.dools.core;

import java.util.HashMap;
import java.util.Map;

import basashi.dools.config.ConfigValue;
import basashi.dools.core.log.ModLog;
import basashi.dools.core.proxy.ClientProxy;
import basashi.dools.core.proxy.CommonProxy;
import basashi.dools.creative.CreativeTabDools;
import basashi.dools.entity.EntityDool;
import basashi.dools.entity.EntityDoolPlayer;
import basashi.dools.item.ItemDool;
import basashi.dools.network.MessageHandler;
import basashi.dools.render.EntityDoolRender;
import basashi.dools.render.EntityPlayerRender;
import basashi.dools.server.ServerDool;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerStartedEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod( ModCommon.MOD_ID)
public class Dools {

	public static final ItemGroup group_dool = new CreativeTabDools("Dools");
	public static final String ITEM_NAME_DOOL = "dool";
	public static final Item item_dool = new ItemDool(new Item.Properties().maxStackSize(16).group(Dools.group_dool)).setRegistryName(ModCommon.MOD_ID,ITEM_NAME_DOOL);

	public static CommonProxy proxy = DistExecutor.runForDist(() -> ClientProxy::new, () -> CommonProxy::new);

	public Dools() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);
        FMLJavaModLoadingContext.get().getModEventBus().addGenericListener(ContainerType.class, this::onContainerRegistry);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onServerStart);
        // コンフィグ読み込み
    	ModLoadingContext.get().
        registerConfig(
        		net.minecraftforge.fml.config.ModConfig.Type.COMMON,
        		ConfigValue.spec);

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
		RenderingRegistry.registerEntityRenderingHandler(EntityDoolPlayer.
				class, EntityPlayerRender::new);
	}

	@OnlyIn(Dist.CLIENT)
	public void guiHandler(){
	}


//	@ObjectHolder(ModCommon.MOD_ID + ":" + ModCommon.MOD_CONTAINER_ITEMSELECT)
//	public static ContainerType<ContainerItemSelect> CONTAINER_ITEMSELECT;

	public void onContainerRegistry(final RegistryEvent.Register<ContainerType<?>> event) {
//		event.getRegistry().register(IForgeContainerType.create((wid, playerInv, extraData)->{
//			int entityId = extraData.readInt();
//			Entity ent = ((ServerWorld)playerInv.player.world).getEntityByID(entityId);
//			playerInv.player.container.scre
//			return new ContainerItemSelect(wid, playerInv, ent, );
//		}).setRegistryName(ModCommon.MOD_ID + ":" + ModCommon.MOD_CONTAINER_ITEMSELECT));
	}

	@Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {
        @SubscribeEvent
        public static void onItemRegistry(final RegistryEvent.Register<Item> itemRegistryEvent) {
        	itemRegistryEvent.getRegistry().register(Dools.item_dool);
        }

        public static EntityType<EntityDool> DOOL;
        public static EntityType<EntityDoolPlayer> DOOLPLAYER;
        @SubscribeEvent
        public static void onEntityRegistry(final RegistryEvent.Register<EntityType<?>> etRegistryEvent){
        	DOOL = EntityType.Builder.<EntityDool>create(EntityDool::new,EntityClassification.MISC)
        			.setTrackingRange(256).setUpdateInterval(1).setShouldReceiveVelocityUpdates(true).size(1.0F,1.0F)
        			.setCustomClientFactory(EntityDool::new).build(ModCommon.MOD_ID + ":" + EntityDool.NAME);
        	DOOL.setRegistryName(new ResourceLocation(ModCommon.MOD_ID,EntityDool.NAME));
        	etRegistryEvent.getRegistry().register(DOOL);


        	DOOLPLAYER = EntityType.Builder.<EntityDoolPlayer>create(EntityDoolPlayer::new,EntityClassification.MISC)
        			.setTrackingRange(256).setUpdateInterval(1).setShouldReceiveVelocityUpdates(true).size(1.0F,1.0F)
        			.setCustomClientFactory(EntityDoolPlayer::new).build(ModCommon.MOD_ID + ":" + EntityDoolPlayer.NAME);
        	DOOLPLAYER.setRegistryName(new ResourceLocation(ModCommon.MOD_ID, EntityDoolPlayer.NAME));
        	etRegistryEvent.getRegistry().register(DOOLPLAYER);
        }
	}

    @SubscribeEvent
	public void onServerStart(FMLServerStartedEvent event) {
		initGuiMap();
	}

	public static Map<String, Class> guiClassMap = new HashMap<String,Class>();
	public static Map<String, ServerDool> serverMap = new HashMap<String, ServerDool>();

	public static void initGuiMap() {
		Registry.ENTITY_TYPE.forEach((etype)->{
			String path = etype.getRegistryName().getPath();
			addGui(path);
		});
	}

	public static void addGui(String pName) {
		if (pName != null && pName.length() > 0) {
			ClassLoader classloader1 = Dools.class.getClassLoader();
			String lcs1, lcs2;
			Class lclass1 = null, lclass2 = null;
			ServerDool lserver = null;

			try {
				lcs1 = (new StringBuilder()).append("basashi.dools.gui.")
						.append("GuiDoolPause_").append(pName)
						.toString();
				lclass1 = classloader1.loadClass(lcs1);


				lcs2 = (new StringBuilder()).append("basashi.dools.server.")
						.append("ServerDool_").append(pName)
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
			ModLog.log().debug("LoadGUI no server or gui:" + pName);
		}
	}

	public static ServerDool getServerFigure(EntityDool pEntity) {
		if (pEntity.renderEntity == null) {
			return null;
		}
		ResourceLocation ls = new ResourceLocation(pEntity.mobString);
		if (serverMap.containsKey(ls.getPath())) {
			return serverMap.get(ls.getPath());
		}
		return new ServerDool();
	}
}