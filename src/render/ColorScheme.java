package de.ttt.render;

import java.awt.Color;

public class ColorScheme {
	
	public static ColorScheme DARK_BLUE=new ColorScheme(
			new Color(0x343E77),
			new Color[]{new Color(0x352BC7),new Color(0xC2C4CF),new Color(0x42526D)},
			new Color(255,255,255), new Color(0x0B0D19), new Color(0x4B3691), new Color(66,52,248), new Color(255,255,255), new Color(255,255,255), new Color(255,255,255, 12)
	);
	public static ColorScheme LIGHT_GREEN=new ColorScheme(
			new Color(0x54D592),
			new Color[]{new Color(0xFF8F3D),new Color(0xFABE3A),new Color(0xF8F7FC)},
			new Color(255,255,255), new Color(0x199593), new Color(0x68E191), new Color(255,255,255), new Color(130,137,181), new Color(128,128,128), new Color(255,255,255, 24)
	);
	public static ColorScheme RED=new ColorScheme(
			new Color(0xC10156),
			new Color[]{new Color(130,137,181),new Color(0xC2C4CF),new Color(0xffffff)},
			new Color(255,255,255), new Color(0x700B59), new Color(0xF90D55), new Color(255,255,255), new Color(130,137,181), new Color(128,128,128), new Color(255,255,255, 24)
	);
	
	private Color displayColor;
	private Color[] particleColors;
	private Color grid,background1,background2;
	private Color buttonColor,buttonText,buttonHover;
	private Color cogColor;
	
	public ColorScheme(Color displayColor, Color[] particleColors, Color grid, Color background1, Color background2, Color buttonColor, Color buttonText, Color buttonHover, Color cogColor) {
		this.displayColor=displayColor;
		this.particleColors=particleColors;
		this.grid=grid;
		this.background1=background1;
		this.background2=background2;
		this.buttonColor=buttonColor;
		this.buttonText=buttonText;
		this.buttonHover=buttonHover;
		this.cogColor=cogColor;
	}
	
	public Color getDisplayColor(double alpha) {
		return new Color(displayColor.getRed(),displayColor.getGreen(),displayColor.getBlue(),(int) (alpha*255));
	}
	public Color[] getParticleColors() {
		return particleColors;
	}
	public Color getGrid() {
		return grid;
	}
	public Color getCogColor() {
		return cogColor;
	}
	public Color getBackground1() {
		return background1;
	}
	public Color getBackground2() {
		return background2;
	}
	public Color getButtonColor() {
		return buttonColor;
	}
	public Color getButtonText() {
		return buttonText;
	}
	public Color getButtonHover() {
		return buttonHover;
	}
	
}
