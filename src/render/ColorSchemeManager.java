package de.ttt.render;

import java.awt.Color;

import de.ttt.image.ImageUtil;

public class ColorSchemeManager {
	
	private static ColorScheme scheme=ColorScheme.DARK_BLUE;
	
	public static void setScheme(ColorScheme s) {
		if(!scheme.equals(s)) {
			scheme=s;
			ImageUtil.updateCogTexture();
		}
	}
	
	public static Color getBackground1() {
		return scheme.getBackground1();
	}
	public static Color getBackground2() {
		return scheme.getBackground2();
	}
	public static Color getGrid() {
		return scheme.getGrid();
	}
	
	public static Color getButtonColor() {
		return scheme.getButtonColor();
	}
	public static Color getButtonText() {
		return scheme.getButtonText();
	}
	public static Color getButtonHover() {
		return scheme.getButtonHover();
	}
	
	public static Color[] getParticleColors() {
		return scheme.getParticleColors();
	}
	
	public static Color getCogColor() {
		return scheme.getCogColor();
	}
	
}
