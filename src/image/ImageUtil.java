package de.ttt.image;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import de.ttt.main.Main;
import de.ttt.render.ColorSchemeManager;

public class ImageUtil {
	
	private static BufferedImage cogBase,cog;
	private static BufferedImage settingsIcon;
	private static BufferedImage settingsPointer;
	private static ArrayList<BufferedImage> icons = new ArrayList<BufferedImage>();
	
	public static void loadResources() {
		try {
			
			cogBase=ImageIO.read(Main.class.getClassLoader().getResourceAsStream("assets/textures/cog.png"));
			cog=new BufferedImage(cogBase.getWidth(), cogBase.getHeight(), cogBase.getType());
			updateCogTexture();
			
			settingsIcon=ImageIO.read(Main.class.getClassLoader().getResourceAsStream("assets/textures/settingsIcon.png"));
			settingsPointer=ImageIO.read(Main.class.getClassLoader().getResourceAsStream("assets/textures/settingsPointer.png"));
			
			icons.add(ImageIO.read(Main.class.getClassLoader().getResourceAsStream("assets/textures/icon256.png")));
			icons.add(ImageIO.read(Main.class.getClassLoader().getResourceAsStream("assets/textures/icon128.png")));
			icons.add(ImageIO.read(Main.class.getClassLoader().getResourceAsStream("assets/textures/icon64.png")));
			icons.add(ImageIO.read(Main.class.getClassLoader().getResourceAsStream("assets/textures/icon32.png")));
			icons.add(ImageIO.read(Main.class.getClassLoader().getResourceAsStream("assets/textures/icon16.png")));
			
		} catch (Exception ex) {
			ex.printStackTrace();
			System.exit(1);
		}
	}
	private static void paintImage(BufferedImage baseImage, BufferedImage target, Color c) {
		double alpha=c.getAlpha()/255.0;
		for(int x=0; x<baseImage.getWidth(); x++) {
			for(int y=0; y<baseImage.getHeight(); y++) {
				Color base=new Color(baseImage.getRGB(x, y),true);
				
				double dAlpha=(base.getAlpha()/255.0)*alpha;
				target.setRGB(x, y, new Color(c.getRed(),c.getGreen(),c.getBlue(), (int)Math.round(dAlpha*255)).getRGB());
			}
		}
	}
	
	public static void updateCogTexture() {
		paintImage(cogBase, cog, ColorSchemeManager.getCogColor());
	}
	
	public static BufferedImage getCogImage() {
		return cog;
	}
	public static BufferedImage getSettingsIcon() {
		return settingsIcon;
	}
	public static BufferedImage getSettingsPointer() {
		return settingsPointer;
	}
	public static ArrayList<BufferedImage> getIcons() {
		return icons;
	}
	
}
