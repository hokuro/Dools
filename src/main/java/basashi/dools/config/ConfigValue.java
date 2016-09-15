package basashi.dools.config;

import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ConfigValue {

	private static final ModConfig config = new ModConfig();

	public static void init(FMLPreInitializationEvent event){
		config.init(new Class<?>[]{General.class}, event);
	}

	public static class General{
	    @ConfigProperty(comment = "ItemID(shiftIndex = ItemID - 256)")
		public static int ItemID = 22203;
		@ConfigProperty(comment = "Zoom rate.")
		public static String zoomRate = "1, 2, 4, 6";
		@ConfigProperty(comment = "default Zoom rate.")
		public static float defaultZoomRate = 4F;
		@ConfigProperty(comment = "use Player Figure.")
		public static boolean isFigurePlayer = true;
	 	@ConfigProperty(comment = "EntityID. 0 is auto assigned.")
		public static int UniqueEntityIdFigurePlayer = 224;
	}


}
