package basashi.dools.gui;

import org.lwjgl.opengl.GL11;

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

	protected int getHoverState(boolean flag) {
		return 0;
	}

	@Override
	public boolean mouseDragged(double i, double j, int p_mouseDragged_5_, double p_mouseDragged_6_, double p_mouseDragged_8_) {
		if (!visible) {
			return super.mouseDragged(i, j, p_mouseDragged_5_, p_mouseDragged_6_, p_mouseDragged_8_);
		}
		if (dragging) {
			sliderValue = (float) (i - (x + 4)) / (float) (width - 8);
			if (sliderValue < 0.0F) {
				sliderValue = 0.0F;
			}
			if (sliderValue > 1.0F) {
				sliderValue = 1.0F;
			}
			setDisplayString();
		}
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		drawTexturedModalRect(x + (int) (sliderValue * (float) (width - 8)),
				y, 0, 66, 4, 20);
		drawTexturedModalRect(x + (int) (sliderValue * (float) (width - 8)) + 4,
				y, 196, 66, 4, 20);
		return super.mouseDragged(i, j, p_mouseDragged_5_, p_mouseDragged_6_, p_mouseDragged_8_);
	}

	@Override
	protected boolean isPressable(double i, double j) {
		if (super.isPressable(i, j)) {
			sliderValue = (float) (i - (x + 4)) / (float) (width - 8);
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

	@Override
	public boolean mouseReleased(double mosuex, double mousey, int p_mouseReleased_5_) {
		dragging = false;
		return super.mouseReleased(mosuex, mousey, p_mouseReleased_5_);
	}

	public float getSliderValue() {
		return sliderValue * sliderMultiply + sliderOffset;
	}

	public GuiSlider setDisplayString() {
		displayString = String.format(strFormat, prefixStr, getSliderValue());
		return this;
	}

	public GuiSlider setStrFormat(String s) {
		strFormat = s;
		return this;
	}

}
