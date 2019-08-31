package basashi.dools.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ConfigValue {

	private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
	public static final General general = new General(BUILDER);
	public static final ForgeConfigSpec spec = BUILDER.build();

	public static class General{
//	    @ConfigProperty(comment = "ItemID(shiftIndex = ItemID - 256)")
//		public static int ItemID = 22203;
//	 	@ConfigProperty(comment = "EntityID. 0 is auto assigned.")
//		public static int UniqueEntityIdFigurePlayer = 224;

		public final ForgeConfigSpec.ConfigValue<String> zoomRate;
		public final ForgeConfigSpec.ConfigValue<Float>  defaultZoomRate;
		public final ForgeConfigSpec.ConfigValue<Boolean> isFigurePlayer;


		public General(ForgeConfigSpec.Builder builder){
			builder.push("General");
			zoomRate = builder.comment("Zoom rate.").define("zoomRate", "1, 2, 4, 6");
			defaultZoomRate = builder.comment("default Zoom rate.").define("defaultZoomRate", 4F);
			isFigurePlayer = builder.comment("use Player Figure.").define("isFigurePlayer", true);

			builder.pop();
		}

		public String ZoomRate(){return this.zoomRate.get();}
		public float DefaultZoomRate(){return this.defaultZoomRate.get();}
		public boolean IsFigurePlayer(){return this.isFigurePlayer.get();}
	}
}
