package de.ttt.image;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import de.ttt.main.Main;

public class ImageUtil {
	
	private static BufferedImage cog;
	private static ArrayList<BufferedImage> icons = new ArrayList<BufferedImage>();
	
	public static void loadResources() {
		try {
			
			Color cogColor=new Color(255,255,255, 12);
			cog=ImageIO.read(Main.class.getClassLoader().getResourceAsStream("assets/textures/cog.png"));
			paintImage(cog, cogColor);
			
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
	private static void paintImage(BufferedImage image, Color c) {
		double alpha=c.getAlpha()/255.0;
		for(int x=0; x<image.getWidth(); x++) {
			for(int y=0; y<image.getHeight(); y++) {
				Color base=new Color(image.getRGB(x, y),true);
				
				double dAlpha=(base.getAlpha()/255.0)*alpha;
				image.setRGB(x, y, new Color(c.getRed(),c.getGreen(),c.getBlue(), (int)Math.round(dAlpha*255)).getRGB());
			}
		}
	}
	
	public static BufferedImage getCogImage() {
		return cog;
	}
	public static ArrayList<BufferedImage> getIcons() {
		return icons;
	}
	
}
