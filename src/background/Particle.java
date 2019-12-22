package de.ttt.background;

import java.awt.Color;

import de.ttt.math.MathUtil;

public class Particle {
	
	private static Color[] COLORS=new Color[]{new Color(0x352BC7),new Color(0xC2C4CF),new Color(0x42526D)};
	
	private Color color;
	
	private double x,y;
	private double size,speed;
	private double xVel,yVel;
	private double xVelBase;
	
	public Particle(double x, double y) {
		color=COLORS[MathUtil.getRandom().nextInt(COLORS.length)];
		this.x=x;
		this.y=y;
		size=MathUtil.getRandom().nextDouble()*7+1;
		speed=MathUtil.getRandom().nextDouble();
		xVelBase=MathUtil.getRandom().nextDouble()*0.25-0.125;
		
		xVel=0;
		yVel=0;
	}
	
	public void update(int width, int height, int movX, int movY) {
		double currentVelX=movX*30;
		xVel=MathUtil.lerpValueBothDir(xVel, currentVelX, 0.05, false);
		double currentVelY=movY*30;
		yVel=MathUtil.lerpValueBothDir(yVel, currentVelY, 0.05, false);
		
		y=y+speed*(yVel/100.0-1);
		x=x+speed*(xVel/100.0+xVelBase);
		
		if(y<=-size) y+=height+size;
		else if(y>=height) y-=height+size;
		if(x<=-size) x+=width+size;
		else if(x>=width) x-=width+size;
	}
	
	public Color getColor() {
		return color;
	}
	
	public double getX() {
		return x;
	}
	public double getY() {
		return y;
	}
	public double getSize() {
		return size;
	}
	
}
