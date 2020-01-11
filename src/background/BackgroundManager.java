package de.ttt.background;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.image.BufferedImage;
import java.util.concurrent.CopyOnWriteArrayList;

import de.ttt.image.ImageUtil;
import de.ttt.main.Vars;
import de.ttt.math.MathUtil;
import de.ttt.render.ColorSchemeManager;
import de.ttt.render.RenderUtil;

public class BackgroundManager {
	
	private static CopyOnWriteArrayList<Particle> particles=new CopyOnWriteArrayList<Particle>();
	
	private static boolean ranInit=false;
	private static double cogRotation;
	
	private static int prevMouseX=-1,prevMouseY=-1;
	private static boolean prevHasMouse=false;
	
	private static void init(int width, int height) {
		cogRotation=MathUtil.getRandom().nextDouble()*90;
		for(int i=0; i<15; i++) {
			double x=MathUtil.getRandom().nextDouble()*width;
			double y=MathUtil.getRandom().nextDouble()*height;
			while(nearOtherCircle(x, y)) {
				x=MathUtil.getRandom().nextDouble()*width;
				y=MathUtil.getRandom().nextDouble()*height;
			}
			particles.add(new Particle(x,y));
		}
	}
	private static boolean nearOtherCircle(double x, double y) {
		for(Particle p:particles) {
			if(Math.pow((x - p.getX()), 2) + Math.pow((y - p.getY()), 2) < 100*100) return true; 
		}
		return false;
	}
	
	public static void update(int width, int height, int mouseX, int mouseY, boolean hasMouse) {
		cogRotation+=0.1;
		if(cogRotation>=360) cogRotation-=360;
		
		int movX=0,movY=0;
		if(prevHasMouse&&hasMouse) {
			movX=mouseX-prevMouseX;
			movY=mouseY-prevMouseY;
		}
		prevMouseX=mouseX;
		prevMouseY=mouseY;
		prevHasMouse=hasMouse;
		
		for(Particle p:particles) {
			p.update(width, height, movX, movY);
		}
	}
	public static void draw(Graphics2D graphics, int width, int height) {
		GradientPaint gradient = new GradientPaint(0, 0, ColorSchemeManager.getBackground1(), width, height, ColorSchemeManager.getBackground2());
		graphics.setPaint(gradient);
		graphics.fillRect(0, 0, width, height);
		graphics.setPaint(null);
		RenderUtil.setRenderingHints(graphics);
		
		BufferedImage image=ImageUtil.getCogImage();
		AffineTransform transform = new AffineTransform();
		transform.translate(Vars.BACKGROUND_COG_X, Vars.BACKGROUND_COG_Y);
		transform.rotate(Math.toRadians(cogRotation), image.getWidth()/2.0, image.getHeight()/2.0);
		graphics.drawImage(image, transform, null);
		
		if(!ranInit) {
			ranInit=true;
			init(width, height);
		}
		
		Color[] particleColors=ColorSchemeManager.getParticleColors();
		for(Particle p:particles) {
			graphics.setColor(particleColors[p.getColor()]);
			double size=p.getSize();
			graphics.fill(new Arc2D.Double(p.getX(), p.getY(), size, size, 0, 360, 0));
		}
	}
	
}
