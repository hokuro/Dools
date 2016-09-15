package basashi.dools.gui;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;

public class GuiSlider extends GuiButton {

	public String prefixStr;
	public float sliderValue;
	public boolean dragging;
	public String strFormat = "%s : %.2f";
	public float sliderMultiply = 1.0F;
	public float sliderOffset = 0.0F;

	public GuiSlider(int i, int j, int k, String s, float f) {
		super(i, j, k, 100, 20, "");
		sliderValue = 1.0F;
		dragging = false;
		sliderValue = f;
		prefixStr = s;
	}

	public GuiSlider(int i, int j, int k, String s, float f, float m, float o) {
		this(i, j, k, s, f);
		sliderMultiply = m;
		sliderOffset = o;
	}

	protected int func_146114_a(boolean flag) {
		return 0;
	}

	protected void func_146119_b(Minecraft minecraft, int i, int j) {
		if (!field_146125_m) {
			return;
		}
		if (dragging) {
			sliderValue = (float) (i - (field_146128_h + 4)) / (float) (field_146120_f - 8);
			if (sliderValue < 0.0F) {
				sliderValue = 0.0F;
			}
			if (sliderValue > 1.0F) {
				sliderValue = 1.0F;
			}
			setDisplayString();
		}
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		func_73729_b(field_146128_h + (int) (sliderValue * (float) (field_146120_f - 8)),
				field_146129_i, 0, 66, 4, 20);
		func_73729_b(field_146128_h + (int) (sliderValue * (float) (field_146120_f - 8)) + 4,
				field_146129_i, 196, 66, 4, 20);
	}

	public boolean func_146116_c(Minecraft minecraft, int i, int j) {
		if (super.func_146116_c(minecraft, i, j)) {
			sliderValue = (float) (i - (field_146128_h + 4)) / (float) (field_146120_f - 8);
			if (sliderValue < 0.0F) {
				sliderValue = 0.0F;
			}
			if (sliderValue > 1.0F) {
				sliderValue = 1.0F;
			}
			setDisplayString();
			dragging = true;
			return true;
		} else {
			return false;
		}
	}

	public void func_146118_a(int i, int j) {
		dragging = false;
	}

	public float getSliderValue() {
		return sliderValue * sliderMultiply + sliderOffset;
	}

	public GuiSlider setDisplayString() {
		field_146126_j = String.format(strFormat, prefixStr, getSliderValue());
		return this;
	}

	public GuiSlider setStrFormat(String s) {
		strFormat = s;
		return this;
	}

}
