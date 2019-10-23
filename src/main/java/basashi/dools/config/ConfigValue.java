package basashi.dools.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ConfigValue {
	private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
	public static final General general = new General(BUILDER);
	public static final ForgeConfigSpec spec = BUILDER.build();

	public static class General{

		public final ForgeConfigSpec.ConfigValue<String> zoomRate;
		public final ForgeConfigSpec.ConfigValue<Double>  defaultZoomRate;


		public General(ForgeConfigSpec.Builder builder){
			builder.push("General");
			zoomRate = builder.comment("Zoom rate.").define("zoomRate", "1, 2, 4, 6");
			defaultZoomRate = builder.comment("default Zoom rate.").define("defaultZoomRate", 4D);
			builder.pop();
		}

		public String ZoomRate(){return this.zoomRate.get();}
		public float DefaultZoomRate(){return  this.defaultZoomRate.get().floatValue();}

		public float getNextZoomRate(float zoom) {
			float ret = zoom;
			String[] zooms = ZoomRate().split(",");
			int next = 0;
			for (int i = 0; i < zooms.length; i++) {
				if (zoom == new Float(zooms[i])) {
					next = i+1;
					break;
				}
			}
			if (zooms.length <= next) {
				ret = new Float(zooms[0]);
			}else {
				ret = new Float(zooms[next]);
			}
			return ret;
		}
	}
}
